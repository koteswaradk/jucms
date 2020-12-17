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
import com.nxgenminds.eduminds.ju.cms.models.ConnectionsModel;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class ConnectionAdapter extends BaseAdapter{

	private ArrayList<ConnectionsModel> listData;
	private LayoutInflater layoutInflater;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private Context prova;

	public ConnectionAdapter(Context context,ArrayList<ConnectionsModel> listData){
		this.listData = listData;
		imageLoader = ImageLoader.getInstance();
		prova = context;

		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.placeholder)
		.showImageForEmptyUri(R.drawable.placeholder) 
		.showImageOnFail(R.drawable.placeholder)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		//.displayer(new RoundedBitmapDisplayer(70))//////
		.build();

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listData.get(position);
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
			convertView = layoutInflater.inflate(R.layout.custom_connections, null);
			holder = new ViewHolder();
			holder.textViewName = (TextView) convertView.findViewById(R.id.connectionsname);
			holder.imageViewPhoto = (ImageView) convertView.findViewById(R.id.connectionsroundedimage);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		ConnectionsModel connectionsItem = (ConnectionsModel) listData.get(position);

		Typeface typeFace=Typeface.createFromAsset(prova.getAssets(),"fonts/BentonSans-Regular.otf");
		// addind Font 

		if(connectionsItem.getFirstname().equalsIgnoreCase(Util.user_firstname))
		{
			//Toast.makeText(prova, "you present", Toast.LENGTH_SHORT).show();
			holder.textViewName.setText("You");
			holder.textViewName.setTypeface(typeFace);
		}
		else
		{
	//Toast.makeText(prova, "Nooo", Toast.LENGTH_SHORT).show();
		holder.textViewName.setText(connectionsItem.getFirstname());
		holder.textViewName.setTypeface(typeFace);
		}
		String imageUrl = connectionsItem.getUser_profile_photo_thumb1();

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
		}
				);

		/*holder.imageViewPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			};
		});
		 */
		return convertView;
	}

	static class ViewHolder{
		ImageView imageViewPhoto;
		TextView textViewName;

	}

}
