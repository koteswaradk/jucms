package com.nxgenminds.eduminds.ju.cms.adapters;

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
import com.nxgenminds.eduminds.ju.cms.models.UsersModel;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class UsersAdapter extends BaseAdapter{

	private ArrayList<UsersModel> mUserslistData;
	private LayoutInflater layoutInflater;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private Context prova;

	public UsersAdapter(Context context,ArrayList<UsersModel> listData){
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
		//.displayer(new RoundedBitmapDisplayer(70))//////
		.build();

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mUserslistData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mUserslistData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
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
		UsersModel userDetails = (UsersModel) mUserslistData.get(position);
        
		// adding Font
		Typeface typeFace=Typeface.createFromAsset(prova.getAssets(),"fonts/BentonSans-Regular.otf");
		 
        if(userDetails.getUser_id().equalsIgnoreCase(Util.USER_ID)){
			holder.textViewName.setText("You");
			holder.textViewName.setTypeface(typeFace);
		}
		else{
	    holder.textViewName.setText(userDetails.getFirstname());
		holder.textViewName.setTypeface(typeFace);
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
