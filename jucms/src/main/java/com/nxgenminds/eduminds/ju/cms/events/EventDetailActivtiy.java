package com.nxgenminds.eduminds.ju.cms.events;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
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
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpPostClient;
import com.nxgenminds.eduminds.ju.cms.thirdpartyprofile.ThirdPartyTabMenuActivity;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class EventDetailActivtiy extends Activity {

	private ImageView eventImage;
	private ImageView eventUser;
	private TextView eventName;
	private TextView eventPlace;
	private TextView eventAttenders;
	private TextView eventPrivacy;
	private TextView eventgoingText;
	private TextView eventDec;
	private TextView eventStartDate;
	private TextView eventStartTime;
	private TextView eventEndDate;
	private TextView eventEndTime;
	private TextView eventCreatedUser;
	private ImageView EventOptions;
	private Button eventAddendenceButton,YesButton;
	private LinearLayout eventGoingYesLayout,eventGoingCountLayout;

	private String mEventName,mEventId,mEventDesc,
	mEventCreatedBy,mEventParticipiantCount,
	mFirstName,mLastName,mEmail,mEventLocation,
	mEventPhotoPath,mEventThemeId,mUserProfilePicture,
	mCreatedDate,mEventLatitude,mEventLongitude,mEventaltitude,
	mEventStartDate,mEventStartTime,mEventEndDate,mEventEndTime,
	mCityName,mPublic,mCustom,mOrgEvent,mEventPrivacy,mRole,
	mLoginUserParticipating,mEventTypeName,mLoginUserFeedBack,
	mArchive="";


	public ImageLoader imageLoader;
	DisplayImageOptions profile_options;

	private TextView eventTo;
	public  String refresh="false";

	private ProgressDialog mPDialog;
	private AlertDialogManager alert = new AlertDialogManager();

	private String RefreshFlag="false";
	private String mEventDetailsStatus = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_details);

		Bundle extras = getIntent().getExtras();
		mEventId = extras.getString("EventId");
		if(extras.containsKey("Archive")){
			mArchive = extras.getString("Archive");
		}

		init();

		ConnectionDetector con = new ConnectionDetector(EventDetailActivtiy.this);
		if(con.isConnectingToInternet()){
			new EventsAsyncClass().execute(mEventId);
		} else{
			alert.showAlertDialog(EventDetailActivtiy.this, "Connection Error", "Check your Internet Connection", false);
		}

		EventOptions.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builderSingle = new AlertDialog.Builder(
						EventDetailActivtiy.this);
				builderSingle.setIcon(R.drawable.jgiappicon);
				builderSingle.setTitle("Event Options");
				final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(EventDetailActivtiy.this,android.R.layout.select_dialog_singlechoice);
				arrayAdapter.add("Edit Event");
				arrayAdapter.add("Delete Event");

				builderSingle.setNegativeButton("cancel",new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

				builderSingle.setAdapter(arrayAdapter,
						new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String strName = arrayAdapter.getItem(which);
						if(strName.equalsIgnoreCase("Edit Event")){

							Intent intent = new Intent(EventDetailActivtiy.this,EventUpdateActivity.class);
							intent.putExtra("EventId", mEventId);
							startActivityForResult(intent,Util.REQUEST_CODE_FOR_UPDATE_EVENT);

						}else if(strName.equalsIgnoreCase("Delete Event")){
							ConnectionDetector conn = new ConnectionDetector(EventDetailActivtiy.this);
							if(conn.isConnectingToInternet()){
								new DeleteEventAsync().execute(mEventId);
							} else{
								alert.showAlertDialog(EventDetailActivtiy.this,"Connection Error","Check your Internet Connection",false);
							}
							finish();
						}
					}
				});
				builderSingle.show();
			}
		});
		//user profile disabled
		/*eventUser.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(EventDetailActivtiy.this,ThirdPartyTabMenuActivity.class);
				intent.putExtra("UserID",mEventCreatedBy);
				intent.putExtra("ThirdPartyRole",mRole);
				Util.THIRD_PARTY_NAME  = mFirstName;
				Util.THIRD_PARTY_ID = mEventCreatedBy;
				startActivity(intent);
				Util.intership_flag=true;
			}
		}) ;
		 */
		eventAddendenceButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				refresh="true";
				RefreshFlag = "true";
				ConnectionDetector conn = new ConnectionDetector(EventDetailActivtiy.this);
				if(conn.isConnectingToInternet()){
					new AttendingLikeAsync().execute(mEventId);
				} else{
					alert.showAlertDialog(EventDetailActivtiy.this,"Connection Error","Check your Internet Connection",false);
				}
			}
		});
	}

	private void init() {
		// TODO Auto-generated method stub

		eventImage = (ImageView) findViewById(R.id.eventImage);
		eventUser = (ImageView) findViewById(R.id.userImage);
		eventCreatedUser = (TextView) findViewById(R.id.eventCreatedBy);
		eventName = (TextView) findViewById(R.id.EventName);
		eventDec = (TextView) findViewById(R.id.EventDec);
		eventPlace = (TextView) findViewById(R.id.eventLocation);
		eventAttenders = (TextView) findViewById(R.id.eventAddendingCountDetails);
		eventPrivacy = (TextView) findViewById(R.id.EventPrivacy);
		eventAddendenceButton = (Button) findViewById(R.id.goingButton);
		eventTo = (TextView) findViewById(R.id.eventTo);
		YesButton = (Button) findViewById(R.id.YesButton);
		eventStartDate = (TextView) findViewById(R.id.startDate);
		eventEndDate = (TextView) findViewById(R.id.endDate);
		eventStartTime = (TextView) findViewById(R.id.startTime);
		eventEndTime = (TextView) findViewById(R.id.endTime);
		EventOptions = (ImageView) findViewById(R.id.EventOptions);

		eventGoingYesLayout = (LinearLayout)findViewById(R.id.goingYesLayout);
		eventGoingCountLayout = (LinearLayout)findViewById(R.id.eventGoingLayout);

		if(mArchive.equalsIgnoreCase("Archive")){
			eventGoingYesLayout.setVisibility(View.GONE);
		}else{
			eventGoingYesLayout.setVisibility(View.VISIBLE);
		}


		imageLoader =imageLoader.getInstance();
		profile_options = new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();

	}

	@Override
	public void onActivityResult(int requestCode,int resultCode,Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK)
		{
			if(requestCode == Util.REQUEST_CODE_FOR_UPDATE_EVENT)
			{
				if(data!=null){
					String refresh = data.getStringExtra("Refresh");
					if(refresh.equalsIgnoreCase("true")){
						RefreshFlag = "true";
						setUpdatedEventDetails(data);
					}
				}
			}
		}
	}


	private void setUpdatedEventDetails(Intent data) {

		SimpleDateFormat timeFormat1 = new SimpleDateFormat("H:mm");
		SimpleDateFormat timeFormat2 = new SimpleDateFormat("H:mm ");
		Date updatedStartTime = null,updatedEndTime =null;


		eventName.setText(data.getStringExtra("EventName"));
		if(data.getStringExtra("EventDescription") == null || data.getStringExtra("EventDescription").equalsIgnoreCase("null")
				|| data.getStringExtra("EventDescription").length()==0){
			eventDec.setText("");
		}else{
			eventDec.setText(data.getStringExtra("EventDescription"));
		}

		if(data.getStringExtra("EventCity") == null || data.getStringExtra("EventCity").equalsIgnoreCase("null")
				|| data.getStringExtra("EventCity").length()==0){
			eventPlace.setText("");
		}else{
			eventPlace.setText(data.getStringExtra("EventCity"));
		}

		if(data.getStringExtra("LastName") == null || data.getStringExtra("LastName").equalsIgnoreCase("null")
				|| data.getStringExtra("LastName").length()==0){
			eventCreatedUser.setText(data.getStringExtra("FirstName"));
		}else{
			eventCreatedUser.setText(data.getStringExtra("FirstName")+" "+data.getStringExtra("LastName"));
		} 

		eventPrivacy.setText(data.getStringExtra("EventPrivacy"));	
		eventStartDate.setText(data.getStringExtra("EventStartDate"));
		eventAttenders.setText(data.getStringExtra("EventAttendes"));
		mEventPhotoPath = data.getStringExtra("EventThemeUrl");



		if(data.getStringExtra("EventEndDate")==null ||data.getStringExtra("EventEndDate").equalsIgnoreCase("null") || data.getStringExtra("EventEndDate").length()==0)
		{  

		}
		else {
			eventTo.setVisibility(View.VISIBLE);
			eventEndDate.setText(data.getStringExtra("EventEndDate"));}

		try {
			if((data.getStringExtra("EventStartTime")==null || data.getStringExtra("EventStartTime").equalsIgnoreCase("null") ||data.getStringExtra("EventStartTime").length()==0)){

			} else{
				updatedStartTime = timeFormat1.parse(data.getStringExtra("EventStartTime"));
				eventStartTime.setText(timeFormat2.format(updatedStartTime));
			}

			if((data.getStringExtra("EventEndTime")==null || data.getStringExtra("EventEndTime").equalsIgnoreCase("null") ||data.getStringExtra("EventEndTime").length()==0)){

			} else{	
				updatedEndTime = timeFormat1.parse(data.getStringExtra("EventEndTime"));
				eventEndTime.setText(timeFormat2.format(updatedEndTime));
			}
		} 
		catch (ParseException e) {e.printStackTrace();}
		if(mEventPhotoPath!=null){
			imageLoader.displayImage(mEventPhotoPath, eventImage, profile_options, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingStarted(String imageUri, View view) {

				}

				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

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
		}
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
				Log.i("jsonObjRecv",jsonObjRecv.toString());
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
					eventAddendenceButton.setVisibility(View.GONE);
					YesButton.setVisibility(View.VISIBLE);
					int attendenceCount = Integer.parseInt(mEventParticipiantCount);
					attendenceCount = attendenceCount + 1;
					eventAttenders.setText(Integer.toString(attendenceCount));

					/*Intent intent = new Intent();
				intent.putExtra("Refresh",RefreshFlag);
				setResult(Activity.RESULT_OK,intent);
				finish();*/
				}
			}
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra("Refresh",RefreshFlag);
		setResult(Activity.RESULT_OK,intent);
		finish();
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
			Log.i("jsonObjRecv",jsonObjRecv.toString());
			return jsonObjRecv;
		}

		@Override
		protected void onPostExecute(JSONObject result){
			try{

				if(result !=null && result.getString("status").equalsIgnoreCase("1")){

					Toast.makeText(getApplicationContext(), result.getString("message"),Toast.LENGTH_LONG).show();
					Intent intent = new Intent();
					RefreshFlag = "true";
					intent.putExtra("Refresh",RefreshFlag);
					setResult(Activity.RESULT_OK,intent);
					finish();
				}
			} catch(JSONException e){

			}
		}
	}

	private class EventsAsyncClass extends AsyncTask<String, Void, String>{

		@Override
		protected void onPreExecute() {

			// Showing progress dialog before sending http request

			if(mPDialog == null){
				mPDialog = Util.createProgressDialog(EventDetailActivtiy.this);
				mPDialog.setCancelable(false);
				mPDialog.show();} 
			else{
				mPDialog.setCancelable(false);
				mPDialog.show();
			}}

		@Override
		protected String doInBackground(String... params) {
			String event_id = params[0];

			JSONArray connectionsResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(Util.API+"event/"+event_id);
			if(jsonObjectRecived != null){
				try{

					mEventDetailsStatus =jsonObjectRecived.getString("status");
					connectionsResponse = jsonObjectRecived.getJSONArray("events");
					if(connectionsResponse.length()>0){
						for(int i = 0; i< connectionsResponse.length();i++){
							JSONObject eventsDetails = connectionsResponse.getJSONObject(i);

							mEventId= eventsDetails.getString("event_id");
							mEventName = eventsDetails.getString("event_name");
							mEventDesc = eventsDetails.getString("event_desc");
							mEventLocation = eventsDetails.getString("location_name");
							mEventStartDate = eventsDetails.getString("event_start_date");
							mEventStartTime = eventsDetails.getString("event_start_time");
							mEventEndDate  = eventsDetails.getString("event_end_date");
							mEventEndTime = eventsDetails.getString("event_end_time");
							mEventCreatedBy = eventsDetails.getString("user_id");
							mEventParticipiantCount = eventsDetails.getString("participants");
							mFirstName = eventsDetails.getString("firstname");
							mLastName = eventsDetails.getString("lastname");
							//mEmail = eventsDetails.getString("email");
							mEventThemeId = eventsDetails.getString("event_theme_id");
							mEventPhotoPath = eventsDetails.getString("event_photo_path");
							mUserProfilePicture = eventsDetails.getString("user_profile_picture");
							mEventPrivacy = eventsDetails.getString("event_privacy");
							mLoginUserParticipating = eventsDetails.getString("login_user_participating");
							mEventTypeName = eventsDetails.getString("event_type_name");
							mLoginUserFeedBack = eventsDetails.getString("login_user_feedback");
							mRole = eventsDetails.getString("role");
						}

					}	 

				}catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return mEventDetailsStatus;
		}

		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);
			mPDialog.dismiss();
			if(!(mEventDetailsStatus==null) && mEventDetailsStatus.equalsIgnoreCase("1")){

				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat sdfTime = new SimpleDateFormat("H:mm:ss");

				try {
					Date date = formatter.parse(mEventStartDate);
					SimpleDateFormat formater = new SimpleDateFormat("MMM dd,yyyy");
					eventStartDate.setText(formater.format(date));

					if(mEventStartTime == null || mEventStartTime.equalsIgnoreCase("null") || mEventStartTime.equalsIgnoreCase("")){

					} else{
						Date startTime = sdfTime.parse(mEventStartTime);
						eventStartTime.setText(new SimpleDateFormat("H:mm ").format(startTime));

					}


					if(mEventEndDate == null || mEventEndDate.equalsIgnoreCase("null")|| mEventEndDate.equalsIgnoreCase("")){
						System.out.println("There is no event end date");
					}
					else
					{
						eventTo.setVisibility(View.VISIBLE); 	
						Date enddate = formatter.parse(mEventEndDate);
						SimpleDateFormat endformater = new SimpleDateFormat("MMM dd,yyyy");
						eventEndDate.setText(endformater.format(enddate));

					}

					if(mEventEndTime == null || mEventEndTime.equalsIgnoreCase("null")|| mEventEndTime.equalsIgnoreCase("")){
					} else{
						Date endTime = sdfTime.parse(mEventEndTime);
						eventEndTime.setText(new SimpleDateFormat("H:mm ").format(endTime));
					}

				} catch (ParseException e) {
					e.printStackTrace();
				}

				if(Util.USER_ID.equalsIgnoreCase(mEventCreatedBy) || mLoginUserParticipating.equalsIgnoreCase("1")){
					eventAddendenceButton.setVisibility(View.GONE);
					YesButton.setVisibility(View.VISIBLE);
				}else{
					eventAddendenceButton.setVisibility(View.VISIBLE);
					YesButton.setVisibility(View.GONE);
				}
				//edit 3 dots disabled
				/*if(mEventCreatedBy.equalsIgnoreCase(Util.USER_ID)){
					EventOptions.setVisibility(View.GONE);
				}else{
					EventOptions.setVisibility(View.GONE);
				}*/

				eventName.setText(mEventName);

				if(mEventDesc == null || mEventDesc.equalsIgnoreCase("null") || mEventDesc.equalsIgnoreCase("")){

				} else {
					eventDec.setText(mEventDesc);
				}

				if((mEventLocation == null) || mEventLocation.equalsIgnoreCase("null") || mEventLocation.equalsIgnoreCase("")){
					eventPlace.setText("Location NA");
				}else{
					eventPlace.setText(mEventLocation);
				}

				eventAttenders.setText(mEventParticipiantCount);
				eventPrivacy.setText(mEventPrivacy);
				if(mLastName== null || mLastName.equalsIgnoreCase("null")){
					eventCreatedUser.setText(mFirstName);	
				}else{
					eventCreatedUser.setText(mFirstName+mLastName);
				}
			}
			//changed the event name if null set as feedback 
			if(mEventTypeName == null || mEventTypeName.equalsIgnoreCase("null") || mEventTypeName.equalsIgnoreCase("FeedBack")){

				eventCreatedUser.setText("Feedback");
				eventGoingYesLayout.setVisibility(View.GONE);
				eventGoingCountLayout.setVisibility(View.INVISIBLE);
			}

			imageLoader.displayImage(mEventPhotoPath, eventImage, profile_options, new SimpleImageLoadingListener() {
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

			imageLoader.displayImage(mUserProfilePicture, eventUser, profile_options, new SimpleImageLoadingListener() {
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
		}
	}
}
