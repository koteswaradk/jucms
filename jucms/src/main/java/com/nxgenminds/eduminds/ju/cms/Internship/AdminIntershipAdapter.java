package com.nxgenminds.eduminds.ju.cms.Internship;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.nxgenminds.eduminds.ju.cms.models.ComanyModel;


public class AdminIntershipAdapter extends BaseAdapter{

	private ArrayList<ComanyModel> listData;
	private LayoutInflater layoutInflater;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private Context prova;

	public AdminIntershipAdapter(Context context,ArrayList<ComanyModel> listData){
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
	public View getView(int position,  View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (layoutInflater == null)
			layoutInflater = (LayoutInflater) prova.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(convertView == null){
			convertView = layoutInflater.inflate(R.layout.custom_admin_intern, null);
			holder = new ViewHolder();
			holder.messageFriendName = (TextView) convertView.findViewById(R.id.company_name);
			holder.messageFriendMessage = (TextView) convertView.findViewById(R.id.company_website);
			holder.messageFriendPhoto= (ImageView) convertView.findViewById(R.id.company_logo);

			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		final ComanyModel messagesItem = (ComanyModel) listData.get(position);

		holder.messageFriendName.setText(messagesItem.getCompany_name());


		holder.messageFriendMessage.setText(messagesItem.getCompany_website());


		String imageUrl = messagesItem.getCompany_logo_path();

		imageLoader.displayImage(imageUrl, holder.messageFriendPhoto, options, new SimpleImageLoadingListener() {
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
		ImageView messageFriendPhoto;
		TextView messageFriendName;
		TextView messageFriendMessage;
	}



}
