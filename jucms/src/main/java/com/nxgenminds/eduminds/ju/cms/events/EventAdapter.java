package com.nxgenminds.eduminds.ju.cms.events;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.models.GroupEventModel;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpPostClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class EventAdapter extends BaseAdapter {

	private ArrayList<GroupEventModel> listData;
	private LayoutInflater layoutInflater;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private Context prova;
	private int lastPosition = -1;
	
	private AlertDialogManager alert = new AlertDialogManager();

	public EventAdapter(Context context,ArrayList<GroupEventModel> listData){
		this.listData = listData;
		imageLoader = ImageLoader.getInstance();
		prova = context;
		options = new DisplayImageOptions.Builder()
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
		final ViewHolder holder;
		if (layoutInflater == null)
			layoutInflater = (LayoutInflater) prova.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(convertView == null){
			convertView = layoutInflater.inflate(R.layout.custom_eventview_new, null);
			holder = new ViewHolder();
			holder.eventUser = (ImageView) convertView.findViewById(R.id.userImage); 
			holder.eventName = (TextView) convertView.findViewById(R.id.EventName);
			holder.eventPlace = (TextView) convertView.findViewById(R.id.eventLocation);
			holder.eventDatentime = (TextView) convertView.findViewById(R.id.eventDate);
			holder.eventAttenders = (TextView) convertView.findViewById(R.id.eventAddendingCount);
			holder.eventImage = (ImageView) convertView.findViewById(R.id.eventImage);
			holder.eventAddendenceButton = (Button) convertView.findViewById(R.id.eventAddendting);
			holder.eventgoingText = (TextView) convertView.findViewById(R.id.goingText);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		final GroupEventModel eventItem = (GroupEventModel) listData.get(position);

		Typeface typeFace=Typeface.createFromAsset(prova.getAssets(),"fonts/BentonSans-Bold.otf");
		Typeface typeFaceThin=Typeface.createFromAsset(prova.getAssets(),"fonts/BentonSans-Regular.otf");
		/*
		//Adding Animations

		Animation animation = AnimationUtils.loadAnimation(prova, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.test_from_top);
		convertView.startAnimation(animation);
		lastPosition = position;

		// Adding Animations

		 */
		holder.eventName.setText(eventItem.getEvent_name());
		holder.eventPlace.setText(eventItem.getCity_name());

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdfTime = new SimpleDateFormat("H:mm:ss");
		try {

			Date date = formatter.parse(eventItem.getEvent_start_date());
			SimpleDateFormat formater = new SimpleDateFormat("MMM dd,yyyy");
			Date startTime = sdfTime.parse(eventItem.getEvent_start_time());
			holder.eventDatentime.setText(formater.format(date) +"," + new SimpleDateFormat("hh:mm aa").format(startTime));

		} catch (ParseException e) {
			e.printStackTrace();
		}

		holder.eventAttenders.setText(eventItem.getParticipants());
		// addind Font 

		if(eventItem.getLogin_user_particating().equalsIgnoreCase("1")){
			holder.eventAddendenceButton.setVisibility(View.GONE);
			holder.eventgoingText.setVisibility(View.VISIBLE);
		}else{
			// do nothing

			holder.eventAddendenceButton.setVisibility(View.VISIBLE);
			holder.eventgoingText.setVisibility(View.GONE);
		}

		String ImageURL = eventItem.getEvent_photo_path_thumb1();

		holder.eventName.setTypeface(typeFace);
		holder.eventPlace.setTypeface(typeFaceThin);
		holder.eventDatentime.setTypeface(typeFaceThin);
		holder.eventAttenders.setTypeface(typeFaceThin);



		imageLoader.displayImage(eventItem.getUser_profile_picture(), holder.eventUser, options, new SimpleImageLoadingListener() {
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


		imageLoader.displayImage(eventItem.getEvent_photo_path(), holder.eventImage, options, new SimpleImageLoadingListener() {
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



		holder.eventAddendenceButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if(eventItem.getLogin_user_particating().equalsIgnoreCase("1")){
					// Do Nothing
				}else{
					
					ConnectionDetector conn = new ConnectionDetector(prova);
		            if(conn.isConnectingToInternet()){
		            	new AttendingLikeAsync().execute(eventItem.getEvent_id());
		            } else{
		            	alert.showAlertDialog(prova,"Connection Error","Check your Internet Connection",false);
		            }

					
					int attendenceCount = Integer.parseInt(eventItem.getParticipants());
					attendenceCount = attendenceCount + 1;
					eventItem.setParticipants(String.valueOf(attendenceCount));
					eventItem.setLogin_user_particating("1");
					holder.eventAddendenceButton.setVisibility(View.GONE);
					holder.eventgoingText.setVisibility(View.VISIBLE);
					holder.eventAttenders.setText(eventItem.getParticipants());
				}

			}
		});

		return convertView;
	}

	static class ViewHolder{
		ImageView eventImage;
		ImageView eventUser;
		TextView eventName;
		TextView eventPlace;
		TextView eventDatentime;
		TextView eventAttenders;
		Button eventAddendenceButton;
		TextView eventgoingText;
	}

	private class AttendingLikeAsync extends AsyncTask<String, Void, Integer>{

		@Override
		protected Integer doInBackground(String... params) {
			// TODO Auto-generated method stub
			// JSON object to hold the information, which is sent to the server
			JSONObject jsonObjSend = new JSONObject();
			String PostId = params[0];

			try {
				jsonObjSend.put("event_id",PostId);
				
			} 
			catch (JSONException e) {
				e.printStackTrace();
			}

			JSONObject jsonObjRecv = HttpPostClient.sendHttpPost(Util.API+"event_participants", jsonObjSend);
			
			return null;
		}

	}

}
