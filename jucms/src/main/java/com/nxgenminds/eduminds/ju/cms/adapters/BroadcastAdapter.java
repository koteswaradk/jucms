package com.nxgenminds.eduminds.ju.cms.adapters;

import java.util.ArrayList;

import android.app.ProgressDialog;
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
import com.nxgenminds.eduminds.ju.cms.models.BroadcastListModel;


public class BroadcastAdapter extends BaseAdapter{

	private ArrayList<BroadcastListModel> listData;
	private LayoutInflater layoutInflater;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private Context prova;
	private ProgressDialog pDialog;

	public BroadcastAdapter(Context context,ArrayList<BroadcastListModel> listData){
		this.listData = listData;
		imageLoader = ImageLoader.getInstance();
		prova = context;

		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.placeholder)
		.showImageForEmptyUri(R.drawable.placeholder) 
		.showImageOnFail(R.drawable.placeholder)
		.cacheInMemory(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		//	.displayer(new RoundedBitmapDisplayer(70))//////
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
			convertView = layoutInflater.inflate(R.layout.activity_broadcast_custom, null);
			holder = new ViewHolder();
			holder.textBroadcastTitle = (TextView) convertView.findViewById(R.id.broadcast_title);
			holder.textGroupList = (TextView) convertView.findViewById(R.id.group_added);
			holder.flag=(ImageView)convertView.findViewById(R.id.image_info);
			holder.profile=(ImageView)convertView.findViewById(R.id.image_broadcast);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		BroadcastListModel broadcastItem = (BroadcastListModel) listData.get(position);

		Typeface typeFace=Typeface.createFromAsset(prova.getAssets(),"fonts/BentonSans-Regular.otf");
		Typeface typeFace_thin=Typeface.createFromAsset(prova.getAssets(),"fonts/BentonSans-Regular.otf");
		// addind Font 

		holder.textBroadcastTitle.setText(broadcastItem.getFirstname());
		holder.textGroupList.setText(broadcastItem.getBroadcast_text());
		imageLoader.displayImage(broadcastItem.getFrom_user_profile_pic(), holder.profile, options, new SimpleImageLoadingListener() {
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
		if(broadcastItem.getBroadcast_flag().equalsIgnoreCase("1"))
		{
			holder.flag.setImageDrawable(prova.getResources().getDrawable(R.drawable.outbox));
		}
		else
		{
			holder.flag.setImageDrawable(prova.getResources().getDrawable(R.drawable.inbox));
		}

		holder.textBroadcastTitle.setTypeface(typeFace);
		holder.textGroupList.setTypeface(typeFace_thin);

		return convertView;
	}

	static class ViewHolder{
		TextView textBroadcastTitle;
		TextView textGroupList;
		ImageView flag,profile;

	}

}
