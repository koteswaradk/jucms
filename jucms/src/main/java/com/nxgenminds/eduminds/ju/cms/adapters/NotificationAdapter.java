package com.nxgenminds.eduminds.ju.cms.adapters;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.nxgenminds.eduminds.ju.cms.models.NotificationsModel;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpPutClient;
import com.nxgenminds.eduminds.ju.cms.thirdpartyprofile.ThirdPartyTabMenuActivity;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class NotificationAdapter extends BaseAdapter{

	private ArrayList<NotificationsModel> listData;
	private LayoutInflater layoutInflater;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private Context prova;

	public NotificationAdapter(Context context,ArrayList<NotificationsModel> listData){
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
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		final NotificationsModel notificationItem = (NotificationsModel) listData.get(position);
		if (layoutInflater == null)
			layoutInflater = (LayoutInflater) prova.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(convertView == null){
			convertView = layoutInflater.inflate(R.layout.custom_notifications_friend_request, null);
			holder = new ViewHolder();
			holder.textViewName = (TextView) convertView.findViewById(R.id.NotificationUserName);
			holder.imageViewUserPhoto = (ImageView) convertView.findViewById(R.id.notificationsImage);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		String str = ("<b>" + notificationItem.getFullname()) + " </b>" + " " + "<font color=\"#808080\">" + notificationItem.getNotification_text() + "</font>";
		holder.textViewName.setText(Html.fromHtml(str));

		imageLoader.displayImage(notificationItem.getActual_photo_path(), holder.imageViewUserPhoto, options, new SimpleImageLoadingListener() {
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
		
		holder.imageViewUserPhoto.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(prova,ThirdPartyTabMenuActivity.class);
				intent.putExtra("UserID",notificationItem.getFrom_user_id());
				intent.putExtra("ThirdPartyRole",notificationItem.getRole());
				Util.THIRD_PARTY_NAME  = notificationItem.getFirstname();
				Util.THIRD_PARTY_ID = notificationItem.getFrom_user_id() ;
				prova.startActivity(intent);
				Util.intership_flag=true;
				
			}
			
		});


		return convertView;
	}

	static class ViewHolder{
		ImageView imageViewUserPhoto;
		TextView textViewName;
	}

	private class NotificationFriendAcceptStatus extends AsyncTask<String, Void, Integer>{

		@Override
		protected Integer doInBackground(String... params) {
			// TODO Auto-generated method stub
			// JSON object to hold the information, which is sent to the server
			JSONObject jsonObjSend = new JSONObject();
			String friend_request_id = params[0];
			String accept_reject_status = params[1];
			String friend_req_send_user = params[2];
			String notification_type_status = params[3];
			String notification_id = params[4];

			try {
				jsonObjSend.put("accept_reject_status",accept_reject_status);
				jsonObjSend.put("friend_req_send_user",friend_req_send_user);
				jsonObjSend.put("notification_id",notification_id);
				System.out.println("Notifications Post IDS"+ accept_reject_status + " " +friend_req_send_user);
			} 
			catch (JSONException e) {
				e.printStackTrace();
			}

			JSONObject jsonObjRecv = HttpPutClient.sendHttpPost(Util.API+"friend_request/"+friend_request_id, jsonObjSend);
			Log.i("jsonObjRecv",jsonObjRecv.toString());
			return null;
		}

	}

}
