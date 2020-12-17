package com.nxgenminds.eduminds.ju.cms.messages;

import java.util.ArrayList;

import android.app.Activity;
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


public class SearchUserMessagesAdapter extends BaseAdapter{

	public ArrayList<SearchUserMessageModel> listData;
	private LayoutInflater layoutInflater;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private Context prova;
	String[] sections;
	private boolean state;
	Activity contex_a;

	public SearchUserMessagesAdapter(Activity contex_a,ArrayList<SearchUserMessageModel> listData){
		this.listData = listData;
		imageLoader = ImageLoader.getInstance();
		this.contex_a = contex_a;
		prova = contex_a;

		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.placeholder)
		.showImageForEmptyUri(R.drawable.placeholder) 
		.showImageOnFail(R.drawable.placeholder)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();

	}

	@Override
	public int getCount() {
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		SearchUserMessageModel connectionsItem = (SearchUserMessageModel) listData.get(position);
		LayoutInflater mInflater = (LayoutInflater) contex_a.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);


		if(convertView == null){
			convertView = mInflater.inflate(R.layout.custom_message_connections, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.image = (ImageView) convertView.findViewById(R.id.image);

			convertView.setTag(holder);		

		}else{
			holder = (ViewHolder) convertView.getTag();
		}



		// addind Font 
		Typeface typeFace=Typeface.createFromAsset(prova.getAssets(),"fonts/BentonSans-Book.otf");
		holder.name.setText(connectionsItem.getFullname());
		holder.name.setTypeface(typeFace);

		String imageUrl = connectionsItem.getProfile_photo();

		imageLoader.displayImage(imageUrl, holder.image, options, new SimpleImageLoadingListener() {
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


		return convertView;
	} 

	static class ViewHolder{
		ImageView image;
		TextView name;

	}


}



