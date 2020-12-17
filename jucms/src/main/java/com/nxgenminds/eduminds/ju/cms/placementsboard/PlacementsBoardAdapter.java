package com.nxgenminds.eduminds.ju.cms.placementsboard;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
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



public class PlacementsBoardAdapter extends BaseAdapter{

	private ArrayList<PlacementsBoardModel> listData;
	private LayoutInflater layoutInflater;
	public ImageLoader imageLoader;
	DisplayImageOptions profile_options;
	ImageView imageview;
	private Context prova;
	Bitmap bit_map_image;

	public PlacementsBoardAdapter(Context context,ArrayList<PlacementsBoardModel> listData){
		this.listData = listData;
		//layoutInflater = LayoutInflater.from(context);
		
		prova = context;
		//imageview=new ImageView(context);
	
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
		
		imageLoader =imageLoader.getInstance();
		profile_options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.pdfimage)
		.showImageForEmptyUri(R.drawable.placeholder) 
		.showImageOnFail(R.drawable.pdfimage)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		final ViewHolder holder;
		if (layoutInflater == null)
			layoutInflater = (LayoutInflater) prova.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(convertView == null){
			convertView = layoutInflater.inflate(R.layout.custom_noticegrid, null);
			holder = new ViewHolder();
			holder.textTitle = (TextView) convertView.findViewById(R.id.notice_title_name);
			holder.imageview=(ImageView)convertView.findViewById(R.id.notice_title_name_image);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
			
		}
		final PlacementsBoardModel noticeboardItem = (PlacementsBoardModel) listData.get(position);

		holder.textTitle.setText(noticeboardItem.getTitle());
		//holder.imageview.setImageBitmap(noticeboardItem.getBit_image());
		//holder.imageview.setBackground(background)
		if (noticeboardItem.getDownload_file_type().contains("jpg")||noticeboardItem.getDownload_file_type().contains("jpeg")||noticeboardItem.getDownload_file_type().contains("png")) {
			Log.i("only jpg or png", "tittle the file for display");
			if (noticeboardItem.getTitle().length()>=20) {
				holder.textTitle.setText(noticeboardItem.getTitle().trim().subSequence(0, 20)+"....");
				//holder.imageview.setImageBitmap(noticeboardItem.getBit_image());
				
			}else{
				//noticeboardItem.getBit_image()
				holder.textTitle.setText(noticeboardItem.getTitle());
				holder.imageview.setImageBitmap(bit_map_image);
			}
	
		} else {

			if (noticeboardItem.getTitle().length()>=20) {
				holder.textTitle.setText(noticeboardItem.getTitle().trim().subSequence(0, 20)+"....");
				//holder.imageview.setImageResource(R.drawable.pdfimage);
				
			}else{
				holder.textTitle.setText(noticeboardItem.getTitle());
				//holder.imageview.setImageResource(R.drawable.pdfimage);
				Log.i("only pdf", "only pdf");;
			}
			
		}
	/*	
	*/
		imageLoader.displayImage(noticeboardItem.getDownload_file_path(), holder.imageview, profile_options, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				
			}

			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {

			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

				//bit_map_image=loadedImage;
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
		TextView textTitle;
		ImageView imageview;
		
	}
	

}
