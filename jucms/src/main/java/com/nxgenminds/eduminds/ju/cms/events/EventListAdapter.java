package com.nxgenminds.eduminds.ju.cms.events;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.Feedback.FeedBackSystemFragment;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpPostClient;
import com.nxgenminds.eduminds.ju.cms.thirdpartyprofile.ThirdPartyTabMenuActivity;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class EventListAdapter extends BaseAdapter {

	private ArrayList<EventListGetModel> listData;
	private LayoutInflater layoutInflater;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private Context prova;
	private int deletePosition,goingPosition;

	private AlertDialogManager alert = new AlertDialogManager();

	public EventListAdapter(){
	}

	public EventListAdapter(Context context,ArrayList<EventListGetModel> listData){

		this.listData = listData;
		imageLoader = ImageLoader.getInstance();
		this.prova = context;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
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
			holder.eventLocationIcon = (ImageView)convertView.findViewById(R.id.eventLocationImage);
			holder.eventEditIcon = (ImageView) convertView.findViewById(R.id.eventEditImage);
			holder.attendenceLayout = (LinearLayout)convertView.findViewById(R.id.attendenceLayout);
			holder.attendenceText = (TextView)convertView.findViewById(R.id.attendencetext);

			holder.wentText = (TextView)convertView.findViewById(R.id.wentText);
			holder.noParticipationText = (TextView)convertView.findViewById(R.id.notParticipatedText);
			holder.feedBackText = (TextView)convertView.findViewById(R.id.FeedBackText);

			holder.wentText.setVisibility(View.GONE);
			holder.noParticipationText.setVisibility(View.GONE);
			holder.feedBackText.setVisibility(View.GONE);

			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}


		final EventListGetModel eventItem = (EventListGetModel) listData.get(position);

		/*
		//Adding Animations

		Animation animation = AnimationUtils.loadAnimation(prova, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.test_from_top);
		convertView.startAnimation(animation);
		lastPosition = position;

		// Adding Animations

		 */

		holder.eventName.setText(eventItem.getEvent_name());

		if(eventItem.getLocation_name()==null || eventItem.getLocation_name().equalsIgnoreCase("null")|| eventItem.getLocation_name().equalsIgnoreCase("")){
			holder.eventLocationIcon.setVisibility(View.GONE);
			holder.eventPlace.setVisibility(View.GONE);
		} else{
			holder.eventLocationIcon.setVisibility(View.VISIBLE);  
			holder.eventPlace.setVisibility(View.VISIBLE);	
			holder.eventPlace.setText(eventItem.getLocation_name());
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdfTime = new SimpleDateFormat("H:mm:ss");
		try {
			Date date = formatter.parse(eventItem.getEvent_start_date());
			SimpleDateFormat formater = new SimpleDateFormat("MMM dd,yyyy");

			if(eventItem.getEvent_start_time().equalsIgnoreCase(null) || eventItem.getEvent_start_time().equalsIgnoreCase("null")){
				holder.eventDatentime.setText(formater.format(date));
			} else {
				Date startTime = sdfTime.parse(eventItem.getEvent_start_time());
				holder.eventDatentime.setText(formater.format(date) +"," + new SimpleDateFormat("H:mm ").format(startTime));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}


		holder.eventAttenders.setText(eventItem.getParticipants());
		holder.attendenceText.setText("Going");
		//holder.attendenceText.setText("");
		


		if(eventItem.getEvent_type_name().equalsIgnoreCase("feedback")){ // check for the feedback event

			holder.attendenceLayout.setVisibility(View.INVISIBLE);
			holder.eventgoingText.setVisibility(View.GONE);
			holder.eventEditIcon.setVisibility(View.GONE);
			holder.eventAddendenceButton.setVisibility(View.GONE);
			holder.feedBackText.setVisibility(View.VISIBLE);

			if(Util.ROLE.equalsIgnoreCase("student") || Util.ROLE.equalsIgnoreCase("class monitor")){
				Calendar calendar = Calendar.getInstance();
				Date eventStartDate = null;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

				try{
					eventStartDate = sdf.parse(eventItem.getEvent_start_date());
				} catch(ParseException e){}

				if(eventStartDate.after(calendar.getTime())){
					holder.feedBackText.setText("FeedBack");
					holder.eventAddendenceButton.setVisibility(View.GONE);

				} else{
					if(eventItem.getLogin_user_feedback().equalsIgnoreCase("1")){
						holder.eventAddendenceButton.setVisibility(View.GONE);
						holder.feedBackText.setVisibility(View.VISIBLE);
						holder.feedBackText.setText("FeedBack Given");
					} else {
						holder.feedBackText.setText("FeedBack");
						holder.eventAddendenceButton.setVisibility(View.VISIBLE);
						holder.eventAddendenceButton.setText("Give FeedBack");
					}
				} 
			}else {  
				if(Util.ROLE.equalsIgnoreCase("admin") || Util.ROLE.equalsIgnoreCase("feedback admin")){
					holder.feedBackText.setText("FeedBack");
				}
			}

		} else { // check for others event

			holder.attendenceLayout.setVisibility(View.VISIBLE);
			holder.feedBackText.setVisibility(View.GONE);





			if(eventItem.getCreated_by().equalsIgnoreCase(Util.USER_ID)){

				holder.eventAddendenceButton.setVisibility(View.GONE);
				holder.eventEditIcon.setVisibility(View.VISIBLE);
				holder.eventgoingText.setVisibility(View.VISIBLE);
				holder.attendenceText.setText("Going");


			} else {

				holder.eventEditIcon.setVisibility(View.GONE);
				if(eventItem.getLogin_user_participating().equalsIgnoreCase("1")){

					holder.eventgoingText.setVisibility(View.VISIBLE);
					holder.eventAddendenceButton.setVisibility(View.GONE);
					holder.attendenceText.setText("Going");


				}	else {

					holder.eventgoingText.setVisibility(View.GONE);
					holder.eventAddendenceButton.setVisibility(View.VISIBLE);  
					holder.eventAddendenceButton.setText("GOING");
					holder.attendenceText.setText("Going");

				}

			}
		} 


		holder.eventEditIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builderSingle = new AlertDialog.Builder(prova);
				builderSingle.setIcon(R.drawable.jgiappicon);
				builderSingle.setTitle("Event Options");
				final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(prova,android.R.layout.select_dialog_singlechoice);
				arrayAdapter.add("Edit Event");
				arrayAdapter.add("Delete Event");

				builderSingle.setNegativeButton("cancel",new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

				builderSingle.setAdapter(arrayAdapter,new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String strName = arrayAdapter.getItem(which);
						if(strName.equalsIgnoreCase("Edit Event")){

							Intent intent = new Intent(prova,EventUpdateActivity.class);
							if(eventItem.getPublic().equalsIgnoreCase("1")|| eventItem.getOrg_event().equalsIgnoreCase("1")){
								intent.putExtra("Group","Group");
							}
							intent.putExtra("EventId",eventItem.getEvent_id());
							((ActionBarActivity)prova).startActivityForResult(intent,Util.REQUEST_CODE_FOR_UPDATE_EVENT);

						}else if(strName.equalsIgnoreCase("Delete Event")){
							deletePosition = position;
							ConnectionDetector conn = new ConnectionDetector(prova);
							if(conn.isConnectingToInternet()){
								new DeleteEventAsync().execute(eventItem.getEvent_id());
							} else{
								alert.showAlertDialog(prova,"Connection Error","Check your Internet Connection",false);
							}


						}

					}
				});
				builderSingle.show();
			}
		});


		eventItem.getEvent_photo_path_thumb1();

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
		});



		holder.eventAddendenceButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if((Util.ROLE.equalsIgnoreCase("student") || Util.ROLE.equalsIgnoreCase("class monitor"))&& eventItem.getEvent_type_name().equalsIgnoreCase("feedback")){
					Util.FEED_BACK_EVENT_ID = eventItem.getEvent_id();	
					Util.FEEDBACK_EVENT_NAME = eventItem.getEvent_name();
					Util.FEEDBACK_EVENT_DESC = eventItem.getEvent_desc();
					replaceFragment(new FeedBackSystemFragment());
				} else if(holder.eventAddendenceButton.getText().toString().equalsIgnoreCase("View FeedBack")){
					Intent intent = new Intent(prova,EventDetailActivtiy.class);
					intent.putExtra("EventId",eventItem.getEvent_id());
					((ActionBarActivity)prova).startActivityForResult(intent,Util.REQUEST_CODE_FOR_DETAIL_EVENT);

				}else {

					if(eventItem.getLogin_user_participating().equalsIgnoreCase("1")){
						// Do Nothing
					}else{
						goingPosition = position;
						ConnectionDetector conn = new ConnectionDetector(prova);
						if(conn.isConnectingToInternet()){
							new AttendingLikeAsync().execute(eventItem.getEvent_id());
						} else{
							alert.showAlertDialog(prova,"Connection Error","Check your Internet Connection",false);
						}

						/*int attendenceCount = Integer.parseInt(eventItem.getParticipants());
					attendenceCount = attendenceCount + 1;
					eventItem.setParticipants(String.valueOf(attendenceCount));
					eventItem.setLogin_user_participating("1");
					holder.eventAddendenceButton.setVisibility(View.GONE);
					holder.eventgoingText.setVisibility(View.VISIBLE);
					holder.eventAttenders.setText(eventItem.getParticipants());*/
					}

				}
			}
		});
		//user profile disabled
		/*holder.eventUser.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(prova,ThirdPartyTabMenuActivity.class);
				intent.putExtra("UserID",eventItem.getCreated_by());
				intent.putExtra("ThirdPartyRole",eventItem.getRole());
				Util.THIRD_PARTY_NAME  = eventItem.getFirstname();
				Util.THIRD_PARTY_ID = eventItem.getCreated_by() ;
				prova.startActivity(intent);
				Util.intership_flag=true;

			}

		});
		 */
		return convertView;
	}

	static class ViewHolder{
		ImageView eventImage,eventLocationIcon,eventEditIcon;
		ImageView eventUser;
		TextView eventName;
		TextView eventPlace;
		TextView eventDatentime;
		TextView eventAttenders;
		Button eventAddendenceButton;
		TextView eventgoingText;
		TextView attendenceText;
		LinearLayout attendenceLayout;

		TextView wentText,noParticipationText,feedBackText;
	}

	private class AttendingLikeAsync extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			// JSON object to hold the information, which is sent to the server
			JSONObject jsonObjSend = new JSONObject();
			String PostId = params[0];
			String status = null ;
			try {
				jsonObjSend.put("event_id",PostId);

				JSONObject jsonObjRecv = HttpPostClient.sendHttpPost(Util.API+"event_participant", jsonObjSend);

				status = jsonObjRecv.getString("status");
			} 
			catch (JSONException e) {
				e.printStackTrace();
			}
			return status;
		}

		@Override
		protected void onPostExecute(String result){

			super.onPostExecute(result);
			if(result!=null){
				if(result.equalsIgnoreCase("1")){
					EventListGetModel getEvent = listData.get(goingPosition);
					int count = Integer.parseInt(getEvent.getParticipants());
					count = count+1;
					getEvent.setParticipants(String.valueOf(count));
					getEvent.setLogin_user_participating("1");
					notifyDataSetChanged();
				}else {}
			}
		}

	}

	private class DeleteEventAsync extends AsyncTask<String, Void, JSONObject>{

		@Override
		protected JSONObject doInBackground(String... params) {
			// TODO Auto-generated method stub
			// JSON object to hold the information, which is sent to the server
			String EventId = params[0];
			JSONObject json = new JSONObject();
			try{
				json.put("event_id",EventId);
			} catch(JSONException e){}
			JSONObject jsonObjRecv = HttpPostClient.sendHttpPost(Util.API+"delete_event" ,json);

			return jsonObjRecv;
		}

		@Override
		protected void onPostExecute(JSONObject result){
			try{
				if(result !=null && result.getString("status").equalsIgnoreCase("1")){
					Toast.makeText(prova, result.getString("message"),Toast.LENGTH_LONG).show();
					listData.remove(deletePosition);
					notifyDataSetChanged();
				}
			} catch(JSONException e){

			}
		}
	}
	private void replaceFragment(Fragment f){
		FragmentManager fm = ((ActionBarActivity)prova).getSupportFragmentManager();   
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.content_frame,f);
		ft.commit();
	}

}
