package com.nxgenminds.eduminds.ju.cms.events;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.Select_Connections;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpPutClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.PlaceJSONParser;
import com.nxgenminds.eduminds.ju.cms.utils.Util;




public class EventUpdateActivity extends ActionBarActivity  implements OnClickListener,OnDateSetListener,OnTimeSetListener, OnCheckedChangeListener  {
	
	private EditText  mEditEventName,mEditEventDescription;
	private AutoCompleteTextView  mAutoTextLocation;
	private LinearLayout invitelayout;
	private TextView mTextStartDate,mTextStartTime,mTextEndDate,mTextEndTime,mTextInvite;
	private Spinner mSpinnerEventTypes;
	boolean _public=false;
	private Button mBtncancel,mBtnUpdate;
	private RadioGroup mRadioGroupPrivacy;
	private RadioButton mRadioButtonCustom,mRadioButtonOrgEvent,mRadioButtonPublic;

	private ImageView image_event_privacy,mImageTheme; 
	private boolean date_start,time_start;

	private String mEventName,mEventId,mEventDesc,mFirstName,mLastName,
					mEventCreatedBy,mEventParticipiantCount,
					mEventLocation,mEventPhotoPath,mEventThemeId,mUserProfilePicture,
					mCreatedDate,mEventLatitude,mEventLongitude,mEventaltitude,
					mEventStartDate,mEventStartTime,mEventEndDate,mEventEndTime,
					mPublic,mCustom,mOrgEvent,mEventPrivacy,mEventDetailsStatus,
					mLoginUserParticipating,mEventTypeId,mCustomEventUsers;

	
	private DatePickerDialog.OnDateSetListener mDateListener;
	private TimePickerDialog.OnTimeSetListener mTimeListener;
	private Calendar mCalendar,myCalendar;
	
	private ArrayList<EventTypesModel> mArrayListEventTypes = new ArrayList<EventTypesModel>();;

	private static Integer flag=0;
	
	private String string_customUsers="";
	private static final String CREATE_EVENT_URL = Util.API + "event";
	private static final String GET_EVENT_TYPES = Util.API + "event_type";
	private static Date mStartingDate ,mEndDate,mCurrentDate,mStartingTime,mEndTime;
	
	private static String mStringStartDate,mStringEndDate,fdate,
	                      mStringEndTime,mStringStartTime,ctime,
	                      mStringCurrentDate;
	             
	private Date mDataStartDate,mDataEndDate,mDataStartTime,mDataEndTime;
	private static String EVENT_URL= Util.API+"event";
	private String date_format = "yyyy-MM-dd";
	
	private SimpleDateFormat sdf = new SimpleDateFormat(date_format);
	
	public static Date checkDate;

	public static final int REQUEST_CODE_GALLERY      = 0x1;
	public static final int REQUEST_CODE_CROP_IMAGE   = 0x3;
	
	private String custom_name="";
	private String custom_id="";
	public static final int result = 111;
	public static final int themeResult=222;
	
	public ImageLoader imageLoader;
	private DisplayImageOptions image_option;
	
	private ProgressDialog pDialog;
		
	private String theme_id="";
	private String event_theme_id="1";
	
	
	
	private String checkActivity;
	
	private ParserTask parserTask;
	private PlacesTask placesTask;
	
	AlertDialogManager alert = new AlertDialogManager();
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_event);
		Util.EVENT_UPDATE_FLAG =1;
		Bundle extras = getIntent().getExtras();
		mEventId = extras.getString("EventId");
		
		
		ActionBar actionBar = getSupportActionBar();
		actionBar = getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);	
		
		View v = LayoutInflater.from(this).inflate(R.layout.custom_create_event,null);
		ActionBar.LayoutParams layout = new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		actionBar.setCustomView(v,layout);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#E1E1E1")));

		initview();
	    ConnectionDetector conn = new ConnectionDetector(EventUpdateActivity.this);	
		if(conn.isConnectingToInternet()){
			new EventsAsyncClass().execute(mEventId);
		}
		else{
			Toast.makeText(EventUpdateActivity.this,"Check your Internet Connection",Toast.LENGTH_LONG).show();
		}


				 
		mBtnUpdate=(Button)findViewById(R.id.invite);
		mBtnUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mEventName = mEditEventName.getText().toString();
				mEventDesc = mEditEventDescription.getText().toString();
				mStringStartDate = mTextStartDate.getText().toString();
				mStringEndDate = mTextEndDate.getText().toString();
				mStringStartTime = mTextStartTime.getText().toString();
				mStringEndTime = mTextEndTime.getText().toString();
				mEventLocation = mAutoTextLocation.getText().toString().trim();

				if(!(mEventName.length()==0) &&!(mStringStartDate.length()==0) && !(flag==0)){  
                        ConnectionDetector conn = new ConnectionDetector(EventUpdateActivity.this);
						if(conn.isConnectingToInternet()){
							new UpdateEventAsyncClass().execute(mEventId);
						}else{
							Toast.makeText(EventUpdateActivity.this,"Check your Internet Connection",Toast.LENGTH_LONG).show();
						}
				 }
				else {
					if(mEventName.length()==0){
						mEditEventName.setError("Enter the event Name");
					}
					if(mStringStartDate.length()==0){
						mTextStartDate.setError("Enter the Event Start Date");
					}
					if(flag==0){
						Toast.makeText(getApplicationContext(),"Select the Event Privacy - custom/public/org event",Toast.LENGTH_SHORT).show();
					}
					if(flag == 3 && string_customUsers.length()==0 ){
						Toast.makeText(getApplicationContext(),"Select the custom/public/org users",Toast.LENGTH_SHORT).show();
					}
				
				}
				}
			
		});
		
		mBtncancel=(Button)findViewById(R.id.cancel);
		mBtncancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Util.EVENT_UPDATE_FLAG = 0;
				if(flag ==3){
					EventUserSearchActivity.mArrayListSelectedUsers.clear();
					}
				clearData();
				finish();
			}
		});

		
		image_event_privacy.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
					if(v.getId()==R.id.event_image_add){
						Toast.makeText(getApplicationContext(), "Select custom members", Toast.LENGTH_SHORT).show();
						// get the contacts for custom list
						Util.EVENT_UPDATE_FLAG =1;
						Intent addcon = new Intent(getApplicationContext(),EventUserSearchActivity.class);
						if(mTextInvite.getText().toString().trim().length()==0){
							addcon.putExtra("first_search",1);
						}
						else{
							addcon.putExtra("first_search",0);
						}
						addcon.putExtra("event_id",mEventId);
						addcon.putExtra("usercheckedid", custom_id);
						//Toast.makeText(getApplicationContext(), custom_id, Toast.LENGTH_SHORT).show();
						startActivityForResult(addcon,result);
					}
				
			
			}
		});

		///setting option for image loader
		imageLoader =imageLoader.getInstance();
		image_option = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.placeholder)
		.showImageForEmptyUri(R.drawable.placeholder) 
		.showImageOnFail(R.drawable.placeholder)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		//end setting option for image loader

		mDateListener = new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				// TODO Auto-generated method stub
				mCalendar.set(Calendar.YEAR, year);
				mCalendar.set(Calendar.MONTH, monthOfYear);
				mCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth );
				
				if(date_start)
				{
					
					mStringStartDate=sdf.format(mCalendar.getTime()).toString();

					try {
						mStartingDate=sdf.parse(mStringStartDate); 
						int sameStartDate = mStartingDate.compareTo(mCurrentDate);
						if((mStartingDate.after(mCurrentDate)) || (sameStartDate ==0)){
							mTextStartDate.setText(mStringStartDate);
						}else{
							Toast.makeText(EventUpdateActivity.this, "Event starts before today", Toast.LENGTH_SHORT).show();
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else 
				{
					mStringEndDate=sdf.format(mCalendar.getTime()).toString();
					try {
						mEndDate=sdf.parse(mStringEndDate);
						int sameEndDate = mEndDate.compareTo(mCurrentDate);
						if((mEndDate.after(mCurrentDate)) || (sameEndDate == 0))
						{
							mTextEndDate.setText(mStringEndDate);
						}
						else
						{
							Toast.makeText(EventUpdateActivity.this,"Event ends before today", Toast.LENGTH_LONG).show();
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		};
		
		mTimeListener = new TimePickerDialog.OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				mCalendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
				mCalendar.set(Calendar.MINUTE,minute);
				String time_format = "HH:mm";
				SimpleDateFormat stf = new SimpleDateFormat(time_format,Locale.getDefault());
				if(time_start){
					//stime=stf.format(checkDate).toString();
					mStringStartTime=stf.format(mCalendar.getTime()).toString();

					try {
						mStartingTime =stf.parse(mStringStartTime);
						mTextStartTime.setText(mStringStartTime);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				else
				{ 
					if(mStringEndDate.length()==0){
						Toast.makeText(EventUpdateActivity.this,"Choose the End Date", Toast.LENGTH_LONG).show();
					}
					else
					{
						mStringEndTime=stf.format(mCalendar.getTime()).toString();
						try{
							mEndTime = stf.parse(mStringEndTime);
							mTextEndTime.setText(mStringEndTime);
						} catch (ParseException e) {e.printStackTrace();}
					}
				}
			}
		};
		mTextStartDate.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				date_start = true;
				mCalendar.setTime(mDataStartDate);
				new DatePickerDialog(EventUpdateActivity.this,2,mDateListener,mCalendar.get(Calendar.YEAR),mCalendar.get(Calendar.MONTH),mCalendar.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
		
		mTextStartTime.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(mTextStartDate.getText().toString().length()==0){
					Toast.makeText(getApplicationContext(), "Enter the start date",Toast.LENGTH_LONG).show();
				} else{  
					time_start = true;
					
					if(mStringStartTime == null || mStringStartTime.equalsIgnoreCase("null") || mStringStartTime.length()==0){
					} else{
					   mCalendar.setTime(mDataStartTime);	
				    }
					new TimePickerDialog(EventUpdateActivity.this,2,mTimeListener,mCalendar.get(Calendar.HOUR_OF_DAY),mCalendar.get(Calendar.MINUTE),false).show();
			 }	
			}
		});
		
		
		mTextEndDate.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mTextStartDate.getText().toString().length()==0){
					Toast.makeText(getApplicationContext(), "Enter the start date",Toast.LENGTH_LONG).show();
				} else{
				  date_start = false;
					if(mStringEndDate == null || mStringEndDate.equalsIgnoreCase("null") || mStringEndDate.length()==0){
					} else{
						mCalendar.setTime(mDataEndDate);
					}
					
					new DatePickerDialog(EventUpdateActivity.this,2,mDateListener,mCalendar.get(Calendar.YEAR),mCalendar.get(Calendar.MONTH),mCalendar.get(Calendar.DAY_OF_MONTH)).show();
			   }	
			}
		});
		
		mTextEndTime.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mTextStartDate.getText().toString().length()==0 && mTextStartTime.getText().toString().length()==0){
					Toast.makeText(getApplicationContext(), "Enter the start date & time",Toast.LENGTH_LONG).show();
				} else{
				time_start = false;
					if(mStringEndTime == null || mStringEndTime.equalsIgnoreCase("null") || mStringEndTime.length()==0){
					} else{
						mCalendar.setTime(mDataEndTime);	
				    }
					new TimePickerDialog(EventUpdateActivity.this,2,mTimeListener,mCalendar.get(Calendar.HOUR_OF_DAY),mCalendar.get(Calendar.MINUTE),false).show();
				}
			}
		});
      
		mAutoTextLocation.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
		   }

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				placesTask = new PlacesTask();
				
				ConnectionDetector conn = new ConnectionDetector(EventUpdateActivity.this);
	            if(conn.isConnectingToInternet()){
	            	placesTask.execute(s.toString());
	            } else{
	            	alert.showAlertDialog(EventUpdateActivity.this,"Connection Error","Check your Internet Connection",false);
	            }

				
		
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
			
		
		});


	  mAutoTextLocation.setOnItemSelectedListener(new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
			
			
		});

		mImageTheme.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent theme = new Intent(getApplicationContext(),EventThemes.class);
				startActivityForResult(theme,themeResult);
			}
		});
	}


	public void initview()
	{
		//identifying widgets 
		findViewById(R.id.event_editText_EventName).setFocusable(false);
		imageLoader = ImageLoader.getInstance();
		invitelayout = (LinearLayout) findViewById(R.id.inviteFriends_display_c);
		invitelayout.setVisibility(View.GONE);
		mEditEventName = (EditText)findViewById(R.id.event_editText_EventName);
		mEditEventDescription =(EditText)findViewById(R.id.event_editText_EventDescription);

		mTextStartDate=(TextView)findViewById(R.id.event_textView_eventStartDate);
		mTextStartTime=(TextView)findViewById(R.id.event_textView_eventStartTime);
		mTextEndDate=(TextView)findViewById(R.id.event_textView_eventEndDate);
		mTextEndTime=(TextView)findViewById(R.id.event_textView_eventEndTime);
		mAutoTextLocation=(AutoCompleteTextView)findViewById(R.id.event_editText_eventgeoLocation);

		mRadioGroupPrivacy = (RadioGroup)findViewById(R.id.event_radiogroup_privacy);
		mRadioGroupPrivacy.setOnCheckedChangeListener(this);
		mRadioButtonCustom = (RadioButton)findViewById(R.id.event_radio_custom);
		mRadioButtonOrgEvent = (RadioButton)findViewById(R.id.event_radio_org);
		mRadioButtonPublic  = (RadioButton)findViewById(R.id.event_radio_public);

		image_event_privacy = (ImageView)findViewById(R.id.event_image_add);
		mTextInvite=(TextView)findViewById(R.id.inviteFriends_display);
		mImageTheme=(ImageView)findViewById(R.id.event_theme);
		
		mCalendar = Calendar.getInstance();
		myCalendar = Calendar.getInstance();
		mCurrentDate = myCalendar.getTime();
		
		imageLoader =imageLoader.getInstance();
		image_option = new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();

     }
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if(v.getId()==R.id.event_image_add){
			Toast.makeText(getApplicationContext(), "Select Custom Members", Toast.LENGTH_SHORT).show();
			Intent addcon = new Intent(getApplicationContext(),Select_Connections.class);
			startActivityForResult(addcon,result);

		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode != RESULT_OK) {return;}

		Bitmap bitmap;

		switch (requestCode) {	

		case result:
			invitelayout.setVisibility(View.VISIBLE);

			mTextInvite.setText("");
			string_customUsers="";
			/*if(custom_name.length()==0){
				
				
				custom_name=data.getStringExtra("CustomUserName");
				custom_id=data.getStringExtra("CustomUserID");
				
				System.out.println("First CustomName:"+custom_name);
				
			} else{
				if(data.getStringExtra("CustomUserName").length()>0){
					
					System.out.println("Before CustomName:"+custom_name);
					
					custom_name=custom_name+","+data.getStringExtra("CustomUserName");
					custom_id=custom_id+","+data.getStringExtra("CustomUserID");
					
					System.out.println("After CustomName:"+ custom_name);
				}
			 }
			*/
			
			if(data.getStringExtra("CustomUserName").length()>0){
				custom_name= data.getStringExtra("CustomUserName");
				custom_id=data.getStringExtra("CustomUserID");
			}
			mTextInvite.setText(custom_name);
			string_customUsers=custom_id;
			
			break;

		case themeResult:
			mEventPhotoPath=data.getStringExtra("themeImage");
			mEventThemeId=data.getStringExtra("theme_id");

			imageLoader.displayImage(mEventPhotoPath,mImageTheme, image_option, new SimpleImageLoadingListener() {
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
			break;

		}
		super.onActivityResult(requestCode, resultCode, data); 
	}
	public void clearData()
	{
		mEditEventName.getText().clear();
		mEditEventDescription.getText().clear();
		mTextStartDate.setText("");
		mTextStartTime.setText("");
		mTextEndDate.setText("");
		mTextEndTime.setText("");
		mRadioGroupPrivacy.clearCheck();
		mRadioButtonCustom.setEnabled(true);
		mRadioButtonOrgEvent.setEnabled(true);
		mRadioGroupPrivacy.setEnabled(true);
		image_event_privacy.setVisibility(View.INVISIBLE);
		mAutoTextLocation.setText("");
		mTextInvite.setText("");
		invitelayout.setVisibility(View.GONE);
		custom_name="";
		custom_id="";
	}
	@Override
	public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		// TODO Auto-generated method stub

	}
	class UpdateEventAsyncClass extends AsyncTask<String, Void, String>{
		private Intent intent;
		private String eventPrivacy;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			// Showing progress dialog before sending http request
			if(pDialog == null){
				pDialog = Util.createProgressDialog(EventUpdateActivity.this);
				pDialog.show();}
			else{

				pDialog.show();
			}}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			String Event_id =params[0];
			JSONArray connectionsResponse = null;
			JSONObject updateEventJsonObject = new JSONObject();
			String status = null;

			try
			{   
				updateEventJsonObject.put("event_name",mEventName);
				
				if(mEventDesc.length()>0){
				updateEventJsonObject.put("event_desc",mEventDesc); 
				}
				if(mEventLocation.length()>0){
					updateEventJsonObject.put("location_name",mEventLocation);
				}
				updateEventJsonObject.put("event_start_date",mStringStartDate);	
				updateEventJsonObject.put("event_theme_id", mEventThemeId);
				updateEventJsonObject.put("event_type_id",mEventTypeId);
				
				if(mStringEndDate.length()>0){
					updateEventJsonObject.put("event_end_date",mStringEndDate);
				}
				if(mStringStartTime.length()>0){
					updateEventJsonObject.put("event_start_time",mStringStartTime);
				}
				if(mStringEndTime.length()>0){
					updateEventJsonObject.put("event_end_time",mStringEndTime);
				}
				

                if(flag==1){
                	updateEventJsonObject.put("public","1");
                	updateEventJsonObject.put("org_event","0");
                	updateEventJsonObject.put("custom","0");	
                	mEventPrivacy = "Public Event";
				}
				else if (flag==2) {
					updateEventJsonObject.put("public","1");
					updateEventJsonObject.put("org_event","1");
					updateEventJsonObject.put("custom","0");
					mEventPrivacy = "Org Event";
				}
				else if (flag==3) {
					updateEventJsonObject.put("public","0");
					updateEventJsonObject.put("org_event","0");
					updateEventJsonObject.put("custom","1");
					updateEventJsonObject.put("csv_custom_list_users",mCustomEventUsers+","+string_customUsers);
					
					mEventPrivacy = "Custom Event";
					
					custom_name="";
					custom_id="";
				}
              }
			catch(JSONException e)
			{e.printStackTrace();}
			
			JSONObject jsonObjectReceived = HttpPutClient.sendHttpPost(EVENT_URL+"/"+Event_id,updateEventJsonObject);
			try
			{  
				if(jsonObjectReceived !=null){
				status = jsonObjectReceived.getString("status");
				
			}
			}
			catch(JSONException e){}
			return status;

		}
		@Override
		protected void onPostExecute(String result)
		{
			super.onPostExecute(result);
			pDialog.dismiss();
			if(result !=null)
			{
				if(result.equalsIgnoreCase("1")){
					/*if (!_public) {
						
					}*/
					Toast.makeText(getApplicationContext(), "Event Updated",Toast.LENGTH_LONG).show();
					Intent intent = new Intent();
					intent.putExtra("Refresh","true");
					intent.putExtra("EventName",mEventName);
					intent.putExtra("EventDescription",mEventDesc);
					intent.putExtra("EventCity",mEventLocation);
					intent.putExtra("FirstName",mFirstName);
					intent.putExtra("LastName",mLastName);
					intent.putExtra("EventPrivacy",mEventPrivacy);		
					intent.putExtra("EventAttendes",mEventParticipiantCount);
					intent.putExtra("EventThemeUrl",mEventPhotoPath);
					intent.putExtra("EventStartDate",mStringStartDate);
                    intent.putExtra("EventStartTime",mStringStartTime);
                    intent.putExtra("EventEndDate",mStringEndDate);
                    intent.putExtra("EventEndTime",mStringEndTime);				
					setResult(Activity.RESULT_OK,intent);
					Util.EVENT_UPDATE_FLAG =0;
					if(flag ==3){
						EventUserSearchActivity.mArrayListSelectedUsers.clear();
						}
					finish();
				}
			}//// need to specify the condition for auto selecting the tabs**************************************************************


		}
	}

	private static String removeLastChar(String str) {
		return str.substring(0,str.length()-1);
	}
	@Override
	public void onCheckedChanged(RadioGroup rg, int checkedId) {
		// TODO Auto-generated method stub
		switch(checkedId)
		{
		case R.id.event_radio_public:{
			Toast.makeText(this, "public is selected for update", Toast.LENGTH_SHORT).show();
			image_event_privacy.setVisibility(View.INVISIBLE);
			invitelayout.setVisibility(View.GONE);
			flag=1;
			_public=true;
			mRadioButtonCustom.setVisibility(View.INVISIBLE);
			mCustom = "0";
			mOrgEvent = "0";
			custom_name="";
			custom_id="";
			mTextInvite.setText("");
			string_customUsers="";
		}
		break;
		case R.id.event_radio_org:{
			
			Toast.makeText(this, "org event is selected for update", Toast.LENGTH_SHORT).show();
			invitelayout.setVisibility(View.GONE);
			image_event_privacy.setVisibility(View.INVISIBLE);
			flag=2;	
			custom_name="";
			custom_id="";
			mTextInvite.setText("");
			string_customUsers="";
			mPublic ="1";
			mCustom="0";
		}

		break;
		case R.id.event_radio_custom:{
			if (_public) {
				Toast.makeText(this, "Sorry public to custom not allowed Data will not be updated", Toast.LENGTH_SHORT).show();
			}else{
			Toast.makeText(this, " custom is selected for update", Toast.LENGTH_SHORT).show();
			image_event_privacy.setVisibility(View.VISIBLE);
			invitelayout.setVisibility(View.VISIBLE);
			flag=3;
			mPublic ="0";
			mOrgEvent="0";
			}
		}
		break;
		}
	} 



	

		
	
	/** A class to parse the Google Places in JSON format */
	private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

		JSONObject jObject;

		@Override
		protected List<HashMap<String, String>> doInBackground(String... jsonData) {

			List<HashMap<String, String>> places = null;

			PlaceJSONParser placeJsonParser = new PlaceJSONParser();

			try{
				jObject = new JSONObject(jsonData[0]);
				places = placeJsonParser.parse(jObject);

			}catch(Exception e){
			}
			return places;
		}

		@Override
		protected void onPostExecute(List<HashMap<String, String>> result) {

			String[] from = new String[] { "description"};
			int[] to = new int[] { android.R.id.text1 };
			SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result, android.R.layout.simple_list_item_1, from, to);
			mAutoTextLocation.setAdapter(adapter);
		}
	}

	// Fetches all places from GooglePlaces AutoComplete Web Service
	private class PlacesTask extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... place) {
			// For storing data from web service
			String data = "";

			// Obtain browser key from https://code.google.com/apis/console
			String key = "AIzaSyCuUvvgFmC8I2e-Cwavzl7dkvEcq8aUoIs";

			String input="Gujarat";

			try {
				input = "input=" + URLEncoder.encode(place[0], "utf-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}

			// place type to be searched
			String types = "types=geocode";

			// Sensor enabled
			String sensor = "sensor=true";

			// Building the parameters to the web service
			String parameters = input+"&"+types+"&"+sensor+"&"+"key="+key;

			// Output format
			String output = "json";

			// Building the url to the web service
			String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"+output+"?"+parameters;
			try{
				// Fetching the data from we service
				data = Util.downloadUrl(url);
			}catch(Exception e){
			}
			return data;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			// Creating ParserTask
			parserTask = new ParserTask();

			// Starting Parsing the JSON string returned by Web Service
			ConnectionDetector conn = new ConnectionDetector(EventUpdateActivity.this);
            if(conn.isConnectingToInternet()){
            	parserTask.execute(result);
            } else{
            	alert.showAlertDialog(EventUpdateActivity.this,"Connection Error","Check your Internet Connection",false);
            }

			
		}
	}

private class EventsAsyncClass extends AsyncTask<String, Void, String>{
		
		@Override
		protected void onPreExecute() {
			
			// Showing progress dialog before sending http request
			if(pDialog == null){
				pDialog = Util.createProgressDialog(EventUpdateActivity.this);
				pDialog.setCancelable(false);
				pDialog.show();}
			else{
				pDialog.setCancelable(false);
				pDialog.show();
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
						 mFirstName = eventsDetails.getString("firstname");
						 mLastName = eventsDetails.getString("lastname");
						 mStringStartDate = eventsDetails.getString("event_start_date");
						 mStringStartTime = eventsDetails.getString("event_start_time");
						 mStringEndDate  = eventsDetails.getString("event_end_date");
						 mStringEndTime = eventsDetails.getString("event_end_time");
						 mEventCreatedBy = eventsDetails.getString("user_id");
						 mEventParticipiantCount = eventsDetails.getString("participants");
						 mEventThemeId= eventsDetails.getString("event_theme_id");
						 mEventPhotoPath = eventsDetails.getString("event_photo_path");
						 mUserProfilePicture = eventsDetails.getString("user_profile_picture");
						 mEventPrivacy = eventsDetails.getString("event_privacy");
						 mEventTypeId = eventsDetails.getString("event_type_id");
						 mPublic = eventsDetails.getString("public");
						 mCustom = eventsDetails.getString("custom");
						 mOrgEvent = eventsDetails.getString("org_event");
						 if(mCustom.equalsIgnoreCase("1")){
						 mCustomEventUsers = eventsDetails.getString("csv_custom_list_users");
						 }
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
			pDialog.dismiss();
			
			SimpleDateFormat sdf = new SimpleDateFormat("H:mm:ss");
			SimpleDateFormat sdf2 = new SimpleDateFormat("H:mm");
			
			SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd");
			
			if(result!=null && result.equalsIgnoreCase("1")){
				
				mEditEventName.setText(mEventName);
				
				if(!((mEventDesc==null || mEventDesc.equalsIgnoreCase("null") || mEventDesc.equalsIgnoreCase("")))){
					mEditEventDescription.setText(mEventDesc);
				}
				
				if(!((mEventLocation==null || mEventLocation.equalsIgnoreCase("null") || mEventLocation.equalsIgnoreCase("")))){
					mAutoTextLocation.setText(mEventLocation);
				}
				
				mTextStartDate.setText(mStringStartDate);
				try{
				mDataStartDate = dateSdf.parse(mStringStartDate);
				
				} catch(ParseException e){
				}
				
				if(((mStringStartTime==null || mStringStartTime.equalsIgnoreCase("null") || mStringStartTime.equalsIgnoreCase("")))){
					
				} else{	
					try{
					mDataStartTime =  sdf.parse(mStringStartTime);
					mTextStartTime.setText(sdf2.format(mDataStartTime));
					} catch(ParseException e){
					}
				}
				
				
				if((mStringEndDate==null || mStringEndDate.equalsIgnoreCase("null") || mStringEndDate.equalsIgnoreCase(""))){
				} else{
					mTextEndDate.setText(mStringEndDate);
					try{
					mDataEndDate = dateSdf.parse(mStringEndDate);
					}catch(ParseException e){
						
					}
				}
				
				if((mStringEndTime==null || mStringEndTime.equalsIgnoreCase("null") || mStringEndTime.equalsIgnoreCase(""))){
					
				} else{
					try{
						mDataEndTime =  sdf.parse(mStringEndTime);
						mTextEndTime.setText(sdf2.format(mDataEndTime));
						} catch(ParseException e){
						}
				}
				
				if(mPublic.equalsIgnoreCase("1")){
					flag=1;
					mRadioButtonPublic.setChecked(true);
					mRadioButtonPublic.setClickable(false);
					mRadioButtonCustom.setClickable(true);
					mRadioButtonPublic.setClickable(true);
					mRadioButtonOrgEvent.setClickable(true);
				}
								
				if(mOrgEvent.equalsIgnoreCase("1")){
					flag=2;
					mRadioButtonOrgEvent.setChecked(true);
					mRadioButtonOrgEvent.setClickable(true);
					mRadioButtonPublic.setClickable(true);
					mRadioButtonCustom.setClickable(true);
					mRadioButtonOrgEvent.setClickable(false);
			   }
				
				if(mCustom.equalsIgnoreCase("1")){
					flag=3;
					mRadioButtonCustom.setChecked(true);
					mRadioButtonPublic.setClickable(true);
					mRadioButtonOrgEvent.setClickable(true);
					mRadioButtonCustom.setClickable(true);
					
					invitelayout.setVisibility(View.VISIBLE);
				}
				
				imageLoader.displayImage(mEventPhotoPath,  mImageTheme, image_option, new SimpleImageLoadingListener() {
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

@SuppressWarnings("deprecation")
@Override
public void onBackPressed() {

	AlertDialog alert_back = new AlertDialog.Builder(this).create();
	alert_back.setTitle("Exit?");
	alert_back.setMessage("Your event will not be updated");

	alert_back.setButton("Cancel", new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
		}
	});

	alert_back.setButton2("Yes", new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			clearData();
			if(flag ==3){
				EventUserSearchActivity.mArrayListSelectedUsers.clear();
			}
			Intent intent = new Intent();
			intent.putExtra("Refresh","false");
			setResult(Activity.RESULT_OK,intent);
			EventUpdateActivity.this.finish();
		}
	});
	alert_back.show();



}

}
