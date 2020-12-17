package com.nxgenminds.eduminds.ju.cms.broadcast_new;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.thirdpartyprofile.ThirdPartyTabMenuActivity;
import com.nxgenminds.eduminds.ju.cms.utils.Util;



public class BroadcastListAdapter_new extends BaseAdapter{

	private ArrayList<BroadcastListModel_new> listData;
	private LayoutInflater layoutInflater;
	private ImageLoader imageLoader;
	private DisplayImageOptions options,dialog_options;
	private Context prova;
	private ProgressDialog pDialog;

	private TextView dialog_fullname,dialog_message_text;
	private ImageView dialog_user_image;
	private Button dialog_cancel;


	public BroadcastListAdapter_new(Context context,ArrayList<BroadcastListModel_new> listData){
		this.listData = listData;
		//layoutInflater = LayoutInflater.from(context);
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
			convertView = layoutInflater.inflate(R.layout.new_custom_view_broadcast, null);
			holder = new ViewHolder();
			holder.textBroadcastTitle = (TextView) convertView.findViewById(R.id.broadcast_title);
			holder.textGroupList = (TextView) convertView.findViewById(R.id.broad_message);
			holder.textCreatedDate = (TextView)convertView.findViewById(R.id.broadcast_created_date);
			holder.flag=(ImageView)convertView.findViewById(R.id.image_info);
			holder.profile=(ImageView)convertView.findViewById(R.id.image_broadcast);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		final BroadcastListModel_new broadcastItem = (BroadcastListModel_new) listData.get(position);



		holder.textBroadcastTitle.setText(broadcastItem.getFirstname());
		holder.textGroupList.setText(broadcastItem.getBroadcast_text());

		if(broadcastItem.getCreated_date() ==null || broadcastItem.getCreated_date().equalsIgnoreCase("null")){
		}else{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());	
			try{
				Date date = sdf.parse(broadcastItem.getCreated_date());
				String set = "MMM dd,yyyy";
				SimpleDateFormat sdf_set = new SimpleDateFormat(set,Locale.getDefault());
				holder.textCreatedDate.setText(sdf_set.format(date));
			} catch(ParseException e){

			}

		}
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
		});		

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub


				final Dialog dialog = new Dialog(prova);  
				dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);  
				dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);  
				dialog.setContentView(R.layout.new_show_broadcast_dialog);  
				dialog.show(); 

				dialog_fullname=(TextView)dialog.findViewById(R.id.username_dialog);
				dialog_message_text=(TextView)dialog.findViewById(R.id.user_broadcast);
				dialog_user_image=(ImageView)dialog.findViewById(R.id.image_broadcast_dialog);
				dialog_cancel=(Button)dialog.findViewById(R.id.close_dialog);

				imageLoader.displayImage(broadcastItem.getFrom_user_profile_pic(), dialog_user_image, dialog_options, new SimpleImageLoadingListener() {
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


				dialog_fullname.setText(broadcastItem.getFirstname());
				dialog_message_text.setText(broadcastItem.getBroadcast_text());
				dialog_cancel.setOnClickListener(new Button.OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();  
					}});
			   }
		});

		//third party profile detail view disabled
		
		/*holder.profile.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {
			
				Intent intent = new Intent(prova,ThirdPartyTabMenuActivity.class);
				intent.putExtra("UserID",broadcastItem.getBroadcast_user_id());
				intent.putExtra("ThirdPartyRole",broadcastItem.getRole());
				Util.THIRD_PARTY_NAME  = broadcastItem.getFirstname();
				Util.THIRD_PARTY_ID=broadcastItem.getBroadcast_user_id();
				prova.startActivity(intent);
				Util.intership_flag=true;
				
			}
			
		});*/
		
		return convertView;
	}

	static class ViewHolder{
		TextView textBroadcastTitle;
		TextView textGroupList;
		TextView textCreatedDate;
		ImageView flag,profile;
	}

}
