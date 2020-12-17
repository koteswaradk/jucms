package com.nxgenminds.eduminds.ju.cms.Internship;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nxgenminds.eduminds.ju.cms.R;


public class CompanyAdminAdapter extends BaseAdapter{

	private ArrayList<ModelCompanyAdmin> mUserslistData;
	private LayoutInflater layoutInflater;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private Context prova;

	public CompanyAdminAdapter(Context context,ArrayList<ModelCompanyAdmin> listData){
		this.mUserslistData = listData;
		imageLoader = ImageLoader.getInstance();
		prova = context;
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.placeholder)
		.showImageForEmptyUri(R.drawable.placeholder) 
		.showImageOnFail(R.drawable.placeholder)
		.cacheInMemory(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();

	}

	@Override
	public int getCount() {
		return mUserslistData.size();
	}

	@Override
	public Object getItem(int position) {
		return mUserslistData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (layoutInflater == null)
			layoutInflater = (LayoutInflater) prova.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(convertView == null){
			convertView = layoutInflater.inflate(R.layout.custom_users, null);
			holder = new ViewHolder();
			holder.textViewName = (TextView) convertView.findViewById(R.id.custom_usersname);
			holder.imageViewPhoto = (ImageView) convertView.findViewById(R.id.custom_usersroundedimage);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		ModelCompanyAdmin userDetails = (ModelCompanyAdmin) mUserslistData.get(position);

		// adding Font
		Typeface typeFace=Typeface.createFromAsset(prova.getAssets(),"fonts/BentonSans-Regular.otf");

		if(userDetails.getLastname()==null || userDetails.getLastname().equalsIgnoreCase("null"))
		{
			holder.textViewName.setText(userDetails.getFirstname());
			//holder.textViewName.setTypeface(typeFace);
		}else
		{
			holder.textViewName.setText(userDetails.getFirstname() +" "+userDetails.getLastname());
			//holder.textViewName.setTypeface(typeFace);
		}

		String imageUrl = userDetails.getProfile_photo();

		imageLoader.displayImage(imageUrl, holder.imageViewPhoto, options, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
			}

			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

			}
		}, new ImageLoadingProgressListener() {
			@Override
			public void onProgressUpdate(String imageUri, View view, int current,
					int total) {

			}
		});

		return convertView;
	}

	static class ViewHolder{
		ImageView imageViewPhoto;
		TextView textViewName;

	}

}
