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
import com.nxgenminds.eduminds.ju.cms.events.EventThemesModel;


public class ThemesAdapter extends BaseAdapter{

	private ArrayList<EventThemesModel> listData;
	private LayoutInflater layoutInflater;
	private Context prova;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	public ThemesAdapter(Context context,ArrayList<EventThemesModel> listData){
		this.listData = listData;
		prova = context;
		imageLoader = ImageLoader.getInstance();
		
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ic_empty)
		.showImageForEmptyUri(R.drawable.ic_empty) 
		.showImageOnFail(R.drawable.ic_empty)
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
		ViewHolder holder;
		if (layoutInflater == null)
			layoutInflater = (LayoutInflater) prova.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(convertView == null){
			convertView = layoutInflater.inflate(R.layout.custom_list_themes, null);
			holder = new ViewHolder();
			holder.themes= (ImageView) convertView.findViewById(R.id.theme_image);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		final EventThemesModel theme_item = (EventThemesModel) listData.get(position);
		String imageUrl = theme_item.getActual_theme_path();
		imageLoader.displayImage(imageUrl, holder.themes, options, new SimpleImageLoadingListener() {
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

		ImageView themes;


	}

}

