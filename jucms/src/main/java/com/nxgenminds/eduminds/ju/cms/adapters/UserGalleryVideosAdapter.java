package com.nxgenminds.eduminds.ju.cms.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.models.UserVideoGalleryModel;

public class UserGalleryVideosAdapter extends BaseAdapter{

	private ArrayList<UserVideoGalleryModel> listData;
	private LayoutInflater layoutInflater;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	public UserGalleryVideosAdapter(Context context,ArrayList<UserVideoGalleryModel> listData){
		this.listData = listData;
		layoutInflater = LayoutInflater.from(context);
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.placeholder_timefeed)
		.showImageForEmptyUri(R.drawable.placeholder_timefeed) 
		.showImageOnFail(R.drawable.placeholder_timefeed)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
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
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView == null){
			convertView = layoutInflater.inflate(R.layout.custom_gallery_videos, null);
			holder = new ViewHolder();
			holder.userPhoto = (ImageView) convertView.findViewById(R.id.userGalleryvideoImageView);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		UserVideoGalleryModel videoItem = (UserVideoGalleryModel) listData.get(position);
		String videoUrl = videoItem.getVideo_thumb_img_path_1();
		imageLoader.displayImage(videoUrl, holder.userPhoto, options, new SimpleImageLoadingListener() {
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
		ImageView userPhoto;
	}

}
