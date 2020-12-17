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
import android.graphics.Typeface;
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
import android.view.View.OnFocusChangeListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
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
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpPostClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.PlaceJSONParser;
import com.nxgenminds.eduminds.ju.cms.utils.Util;



public class UserCreateEvent extends ActionBarActivity  implements OnClickListener,OnDateSetListener,OnTimeSetListener, OnCheckedChangeListener  {
	
	
	private EditText mEditEventName,mEditEventDescription;
	private AutoCompleteTextView mAutoTextLocation;
	private LinearLayout invitelayout;
	private RelativeLayout privacylayout;
	private TextView mTextStartDate,mTextStartTime,mTextEndDate,mTextEndTime,mTextInvite;
	private Spinner mSpinnerEventTypes;
	private boolean _public=false;
	private Button mBtncancel,mBtnInvite;
	private RadioGroup mRadioGroupPrivacy;
	private RadioButton mRadioButtonCustom,mRadioButtonOrgEvent,mRadioButtonPublic;

	private ImageView image_event_privacy,mImageTheme; 
	private boolean date_start,time_start;

	private String mString_description,
	               mString_startDate,mString_startTime,
	               mString_endDate,mString_endTime,
	               mString_location,mString_eventType_id,
	               mString_eventName;
	
	
	private DatePickerDialog.OnDateSetListener mDateListener;
	private TimePickerDialog.OnTimeSetListener mTimeListener;
	private Calendar mCalendar,myCalendar;
	
	private ArrayList<EventTypesModel> mArrayListEventTypes = new ArrayList<EventTypesModel>();
	

	private static Integer flag=0;
	
	private String string_customUsers="";
	private static final String CREATE_EVENT_URL = Util.API + "event";
	private static final String GET_EVENT_TYPES = Util.API + "event_type";
	private static Date mStartingDate ,mEndDate,mCurrentDate,mStartingTime,mEndTime;
	
	private static String mStringStartDate,mStringEndDate,fdate,
	                      mStringEndTime,mStringStartTime,ctime,
	                      mStringCurrentDate;
	private static final String date_format = "yyyy-MM-dd";
	public static Date checkDate;

	public static final int REQUEST_CODE_GALLERY      = 0x1;
	public static final int REQUEST_CODE_CROP_IMAGE   = 0x3;
	
	private static String custom_name="";
	private static String custom_id="";
	public static final int result = 111;
	public static final int themeResult=222;
	
	public ImageLoader imageLoader;
	private DisplayImageOptions image_option;
	
	private ProgressDialog pDialog;
		
	private String theme_id="";
	private String event_theme_id="1";
	
	
	private SimpleDateFormat sdf;
	private String checkActivity;
	
	private ParserTask parserTask;
	private PlacesTask placesTask;
	
	AlertDialogManager alert = new AlertDialogManager();
	
	private String mExtras="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_event);
		
		Bundle extras = getIntent().getExtras();
        if(extras!=null){
        	mExtras = extras.getString("Group");
        }
		
		mArrayListEventTypes.clear();
		EventTypesModel model = new EventTypesModel();
		model.setEvent_type_id("0");
		model.setEvent_type_name("Select a Event Type");
		mArrayListEventTypes.add(model);
		
		/*ConnectionDetector conn = new ConnectionDetector(UserCreateEvent.this);
		if(conn.isConnectingToInternet()){
			new GetEventTypes().execute();
		}*/
		
		sdf = new SimpleDateFormat(date_format);
		myCalendar = Calendar.getInstance();
		mStringCurrentDate = sdf.format(myCalendar.getTime());
		try {
			mCurrentDate = sdf.parse(mStringCurrentDate);
		}
		catch (ParseException e1) {e1.printStackTrace();}


		ActionBar actionBar = getSupportActionBar();
		actionBar = getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);	
		
		View v = LayoutInflater.from(this).inflate(R.layout.custom_create_event,null);
		ActionBar.LayoutParams layout = new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		actionBar.setCustomView(v,layout);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#E1E1E1")));
		
		
		Button createButton = (Button) findViewById(R.id.invite);
		createButton.setText(getResources().getString(R.string.create_text));

		
		initview();
		
		mBtnInvite=(Button)findViewById(R.id.invite);
		mBtnInvite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				boolean isValidDate = true;
				boolean isValidTime = true;
				
				
				mString_eventName = mEditEventName.getText().toString();
				mString_description = mEditEventDescription.getText().toString();
				mString_startDate = mTextStartDate.getText().toString();
				mString_endDate = mTextEndDate.getText().toString();
				mString_startTime = mTextStartTime.getText().toString();
				mString_endTime = mTextEndTime.getText().toString();
				mString_location = mAutoTextLocation.getText().toString().trim();
                
				if(mString_endDate!=null){
				if((!(mString_endDate.isEmpty()) && mStartingDate.after(mEndDate))){
					Toast.makeText(UserCreateEvent.this, "Event ends before it starts", Toast.LENGTH_SHORT).show();	
					//mString_endDate="";
					isValidDate = false;
				}else{ isValidDate = true;}
				}
				
				if(mString_endTime!=null){
				if(!(mString_endTime.length()==0)){
					if(mStartingTime.after(mEndTime)){ 
						Toast.makeText(UserCreateEvent.this, "Event ends before start time", Toast.LENGTH_SHORT).show();	
						mString_endTime="";
						isValidTime = false;
					}
					else{ isValidTime = true;}
				}
				}
				
				
				if(mString_endDate==null || mString_endDate.equalsIgnoreCase("null") ||mString_endDate.length()==0){
					mString_endDate = mString_startDate;
					mTextEndDate.setText(mString_endDate);
					isValidDate = true;
				}
				/*if((mString_eventName.length()==0) || (mString_description.length()== 0)|| (mString_startDate.length()==0)
						|| (mString_startTime.length() == 0)) 
				{    
					Toast.makeText(getApplicationContext(),"Fill the blank Details",Toast.LENGTH_SHORT).show();
				}*/
				
				if(mExtras!=null &&  mExtras.equalsIgnoreCase("Group")){
					flag=1;
				}
				
				if(!(mString_eventName.length()==0) &&!(mString_startDate.length()==0) && !(flag==0)){  
					ConnectionDetector conn = new ConnectionDetector(UserCreateEvent.this);
					if(conn.isConnectingToInternet()){
					new CreateEventAsyncClass().execute();
					} else{
						alert.showAlertDialog(UserCreateEvent.this,"Connection Error","Check your Internet Connection", false);
					}
				} else {
					/*if(mString_eventType_id.equalsIgnoreCase("0")){
						((TextView)mSpinnerEventTypes.getSelectedView()).setError("Select the event type");
					}*/
					if(mString_eventName.length()==0){
						mEditEventName.setError("Enter the Event Name");
					}
					if(mString_startDate.length()==0){
						mTextStartDate.setError("Enter the Event Start Date");
					}
					if(flag==0){
						Toast.makeText(getApplicationContext(),"Select the Event Privacy - custom/public/org event",Toast.LENGTH_SHORT).show();
					}
					Toast.makeText(getApplicationContext(),"Fill the blank Details",Toast.LENGTH_SHORT).show();
					
				}
			}
		});
		mBtncancel=(Button)findViewById(R.id.cancel);
		mBtncancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				clearData();
				if(flag ==3){
					EventUserSearchActivity.mArrayListSelectedUsers.clear();
				}
				Intent intent = new Intent();
				intent.putExtra("Refresh","false");
				setResult(Activity.RESULT_OK,intent);
				finish();

			}
		});


		mCalendar = Calendar.getInstance();
		Typeface typeFace=Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/BentonSans-Regular.otf");
		mRadioGroupPrivacy.setOnCheckedChangeListener(this);
		image_event_privacy.setOnClickListener(this);

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
					checkDate=mCalendar.getTime();
					mStringStartDate=sdf.format(mCalendar.getTime()).toString();
					try {
						mStartingDate=sdf.parse(mStringStartDate);
						int sameStartDate = mStartingDate.compareTo(mCurrentDate);
						if((mStartingDate.after(mCurrentDate) || (sameStartDate ==0))){   
							mTextStartDate.setError(null);
							mTextStartDate.setText(mStringStartDate);	
						}
						else{
							Toast.makeText(UserCreateEvent.this, "Event starts before today", Toast.LENGTH_SHORT).show();	
						}
					} 
					catch (ParseException e) {e.printStackTrace();}
				}
				else 
				{
					mStringEndDate=sdf.format(mCalendar.getTime()).toString();
					try {
						mEndDate=sdf.parse(mStringEndDate);
						int sameEndDate = mEndDate.compareTo(mCurrentDate);
						if((mEndDate.after(mCurrentDate)) || (sameEndDate ==0)){
							mTextEndDate.setText(mStringEndDate);	
						}
						else
						{
							Toast.makeText(UserCreateEvent.this,"Event ends before today", Toast.LENGTH_LONG).show();
						}
					} 
					catch (ParseException e) { e.printStackTrace(); }
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
				if(time_start)
				{
					mStringStartTime=stf.format(mCalendar.getTime()).toString();
					try { 
					mStartingTime =stf.parse(mStringStartTime);
					mTextStartTime.setText(mStringStartTime);
					} 
					catch (ParseException e) {e.printStackTrace();}
				}
				else
				{      
					 
						mStringEndTime=stf.format(mCalendar.getTime()).toString();
						try {
							mEndTime=stf.parse(mStringEndTime);
							mTextEndTime.setText(mStringEndTime);
						}catch (ParseException e) {e.printStackTrace();}
					}
				} 
		};
		
		mTextStartDate.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				date_start = true;
				new DatePickerDialog(UserCreateEvent.this,2,mDateListener,mCalendar.get(Calendar.YEAR),mCalendar.get(Calendar.MONTH),mCalendar.get(Calendar.DAY_OF_MONTH)).show();
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
				new TimePickerDialog(UserCreateEvent.this,2,mTimeListener,mCalendar.get(Calendar.HOUR_OF_DAY),mCalendar.get(Calendar.MINUTE),false).show();
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
				new DatePickerDialog(UserCreateEvent.this,2,mDateListener,mCalendar.get(Calendar.YEAR),mCalendar.get(Calendar.MONTH),mCalendar.get(Calendar.DAY_OF_MONTH)).show();
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
				new TimePickerDialog(UserCreateEvent.this,2,mTimeListener,mCalendar.get(Calendar.HOUR_OF_DAY),mCalendar.get(Calendar.MINUTE),false).show();
				}
			}
		});


		
		mImageTheme.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent theme = new Intent(getApplicationContext(),EventThemes.class);
				startActivityForResult(theme,themeResult);

			}
		});
		
		mEditEventName.setOnFocusChangeListener(new OnFocusChangeListener(){

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus && mEditEventName.getText().toString().length()==0){
					mEditEventName.setError("Event Name must be given");
				} else {
					mEditEventName.setError(null);
				}
				
			}
		});
		
		mEditEventName.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if(mEditEventName.getText().toString().length()>0){
					mEditEventName.setError(null);
				}
				
			}

			@Override
			public void afterTextChanged(Editable s) {
				if(mEditEventName.getText().toString().length()>0){
					mEditEventName.setError(null);
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
				ConnectionDetector conn = new ConnectionDetector(UserCreateEvent.this);
	            if(conn.isConnectingToInternet()){
	            	placesTask.execute(s.toString());
	            } else{
	            	alert.showAlertDialog(UserCreateEvent.this,"Connection Error","Check your Internet Connection",false);
	            }
	         }

			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
			
		
		});

				
		/*mSpinnerEventTypes.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				EventTypesModel  model = mArrayListEventTypes.get(position);
				 if(position ==0 ){
					 mString_eventType_id = model.getEvent_type_id();
				} else {
					 ((TextView)parent.getSelectedView()).setError(null);
					 mString_eventType_id = model.getEvent_type_id();
				 }
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
		   }
			
		});
*/				
			
	}
	@SuppressWarnings("deprecation")
	@Override
	public void onBackPressed() {

		AlertDialog alert_back = new AlertDialog.Builder(this).create();
		alert_back.setTitle("Exit?");
		alert_back.setMessage("Your event will be discarded");

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
				UserCreateEvent.this.finish();
			}
		});
		alert_back.show();



	}
	public void initview()
	{
		//identifying widgets 
		imageLoader = ImageLoader.getInstance();
		
		privacylayout = (RelativeLayout) findViewById(R.id.InviteFriends_relativelayout);
		invitelayout = (LinearLayout) findViewById(R.id.inviteFriends_display_c);
		invitelayout.setVisibility(View.GONE);
		
		if(mExtras !=null && mExtras.equalsIgnoreCase("Group")){
			privacylayout.setVisibility(View.GONE);
			invitelayout.setVisibility(View.GONE);
		}
		
		mEditEventName = (EditText)findViewById(R.id.event_editText_EventName);
		mEditEventDescription =(EditText)findViewById(R.id.event_editText_EventDescription);
		//mSpinnerEventTypes = (Spinner)findViewById(R.id.create_event_types);
		

	     mTextStartDate=(TextView)findViewById(R.id.event_textView_eventStartDate);
		 mTextStartTime=(TextView)findViewById(R.id.event_textView_eventStartTime);
		 mTextEndDate=(TextView)findViewById(R.id.event_textView_eventEndDate);
		 mTextEndTime=(TextView)findViewById(R.id.event_textView_eventEndTime);
		 mAutoTextLocation=(AutoCompleteTextView)findViewById(R.id.event_editText_eventgeoLocation);
		 mAutoTextLocation.setThreshold(1);

		mRadioGroupPrivacy = (RadioGroup)findViewById(R.id.event_radiogroup_privacy);
		mRadioButtonCustom = (RadioButton)findViewById(R.id.event_radio_custom);
		mRadioButtonOrgEvent = (RadioButton)findViewById(R.id.event_radio_org);
		mRadioButtonPublic  = (RadioButton)findViewById(R.id.event_radio_public);
		
		if(Util.ROLE.equalsIgnoreCase("admin")){
			mRadioButtonOrgEvent.setVisibility(View.VISIBLE);
		}else{
			mRadioButtonOrgEvent.setVisibility(View.GONE);
		}

		image_event_privacy = (ImageView)findViewById(R.id.event_image_add);
		mTextInvite=(TextView)findViewById(R.id.inviteFriends_display);
		mImageTheme=(ImageView)findViewById(R.id.event_theme);

		image_option = new DisplayImageOptions.Builder()
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
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if(v.getId()==R.id.event_image_add)
		{
			Toast.makeText(getApplicationContext(), "Select custom members", Toast.LENGTH_SHORT).show();
			// get the contacts for custom list
			Intent addcon = new Intent(getApplicationContext(),EventUserSearchActivity.class);
			if(mTextInvite.getText().toString().trim().length()==0){
				addcon.putExtra("first_search",1);
			}
			else{
				addcon.putExtra("first_search",0);
			}
			addcon.putExtra("usercheckedid", custom_id);
			//Toast.makeText(getApplicationContext(), custom_id, Toast.LENGTH_SHORT).show();
			startActivityForResult(addcon,result);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode != RESULT_OK) {

			return;
		}

		Bitmap bitmap;

		switch (requestCode) {		

		case result:
			
			mTextInvite.setText("");
			string_customUsers="";
			invitelayout.setVisibility(View.VISIBLE);
			
				if(data.getStringExtra("CustomUserName").length()>0){
					custom_name= data.getStringExtra("CustomUserName");
					custom_id=data.getStringExtra("CustomUserID");
				}
			 
			
			mTextInvite.setText(custom_name);
			string_customUsers= custom_id;/// send this user id to server while creating the custom event  st.replaceAll("\\s+","")
			break;

		case themeResult:
			theme_id=data.getStringExtra("themeImage");
			event_theme_id=data.getStringExtra("theme_id");
			imageLoader.displayImage(theme_id, mImageTheme, image_option, new SimpleImageLoadingListener() {
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
		/*.getText().clear();
		mEditEventDescription.getText().clear();
		mTextStartData.setText("");
		start_time.setText("");
		end_date.setText("");
		end_time.setText("");
		mRadioGroupPrivacy.clearCheck();
		mRadioButtonCustom.setEnabled(true);
		mRadioButtonPrivate.setEnabled(true);
		mRadioButtonPublic.setEnabled(true);
		image_event_privacy.setVisibility(View.INVISIBLE);
		event_location.setSelection(0);
		mTextInvite.setText("");
		invitelayout.setVisibility(View.GONE);
		custom_name=" ";
		custom_id=" ";
*/	}
	@Override
	public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		// TODO Auto-generated method stub

	}
	class CreateEventAsyncClass extends AsyncTask<Void, Void, String>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			// Showing progress dialog before sending http request
			if(pDialog == null){
				pDialog = Util.createProgressDialog(UserCreateEvent.this);
				pDialog.setCancelable(false);
				pDialog.show();}
			else{
				pDialog.setCancelable(false);
				pDialog.show();
			}}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONArray connectionsResponse = null;
			JSONObject createEventJsonObject = new JSONObject();
			String error = null;

			try
			{  
				/*if(checkActivity.equalsIgnoreCase("Group")){
					createEventJsonObject.put("org_event", "1");
				}*/

				createEventJsonObject.put("event_name",mString_eventName);
				createEventJsonObject.put("event_start_date",mString_startDate);
				/*createEventJsonObject.put("event_type_id",mString_eventType_id);*/
				createEventJsonObject.put("event_desc",mString_description);
				createEventJsonObject.put("location_name",mString_location);
				if(mString_endDate.length()>0){
					createEventJsonObject.put("event_end_date",mString_endDate);
				}
				if(mString_startTime.length()>0){
					createEventJsonObject.put("event_start_time",mString_startTime);
				}
				if(mString_endTime.length()>0){
					createEventJsonObject.put("event_end_time",mString_endTime);
				}
				
				createEventJsonObject.put("event_theme_id", event_theme_id);
                
				if(mExtras !=null && mExtras.equalsIgnoreCase("Group")){
					createEventJsonObject.put("public","1");
					createEventJsonObject.put("org_event","1");
					createEventJsonObject.put("custom","0");
				} else{
				
					if(flag==1){
                	createEventJsonObject.put("public","1");
					createEventJsonObject.put("org_event",0);
					createEventJsonObject.put("custom","0");	
					}
						else if (flag==2) {
							createEventJsonObject.put("public","1");
							createEventJsonObject.put("org_event",1);
							createEventJsonObject.put("custom","0");
						}
						else if (flag==3) {
							createEventJsonObject.put("public","0");
							createEventJsonObject.put("org_event",0);
							createEventJsonObject.put("custom","1");
							createEventJsonObject.put("csv_custom_list_users",string_customUsers);
				}
			}
				

			}
			catch(JSONException e)
			{
				e.printStackTrace();
			}
			
			JSONObject jsonObjectReceived = HttpPostClient.sendHttpPost(CREATE_EVENT_URL,createEventJsonObject);
			try
			{
				error = jsonObjectReceived.getString("status");
				
			} 
			catch(JSONException e){}

			return error;
		}
		@Override
		protected void onPostExecute(String result)
		{
			super.onPostExecute(result);
			pDialog.dismiss();
			if(result.equalsIgnoreCase("1")){
				Toast.makeText(UserCreateEvent.this, "Event created successfully",Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				intent.putExtra("Refresh","true");
				setResult(Activity.RESULT_OK,intent);
				if(flag ==3){
				EventUserSearchActivity.mArrayListSelectedUsers.clear();
				}
				finish();
				custom_name="";
				custom_id="";
				/*UserFragmentEvents.addEvent.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.create));
				UserFragmentEvents.v_upcoming.setVisibility(View.INVISIBLE);
				UserFragmentEvents.v_archived.setVisibility(View.INVISIBLE);
				UserFragmentEvents.v_current.setVisibility(View.VISIBLE);*/
			}//// need to specify the condition for auto selecting the tabs**************************************************************
		}
	}

	private static String removeLastChar(String str) {
		return str.substring(1,str.length()-1);
	}
	@Override
	public void onCheckedChanged(RadioGroup rg, int checkedId) {
		// TODO Auto-generated method stub
		switch(checkedId)
		{
		
		case R.id.event_radio_public:{
			Toast.makeText(this, "public", Toast.LENGTH_LONG).show();
			image_event_privacy.setVisibility(View.INVISIBLE);
			invitelayout.setVisibility(View.GONE);
			flag=1;
			custom_name="";
			custom_id="";
			mTextInvite.setText("");
			mTextInvite.setHint("Invite people");
			string_customUsers=" ";
		}
		break;
		case R.id.event_radio_org:{
			Toast.makeText(this, "org", Toast.LENGTH_LONG).show();
			invitelayout.setVisibility(View.GONE);
			image_event_privacy.setVisibility(View.INVISIBLE);
			flag=2;	
			custom_name="";
			custom_id="";
			mTextInvite.setText("");
			mTextInvite.setHint("Invite people");
			string_customUsers=" ";
		}

		break;
		case R.id.event_radio_custom:{
			Toast.makeText(this, "custom", Toast.LENGTH_LONG).show();
			image_event_privacy.setVisibility(View.VISIBLE);
			invitelayout.setVisibility(View.VISIBLE);
			mTextInvite.setText("");
			mTextInvite.setHint("Invite people");
			flag=3;
		}
		break;
		}
	} 
	
	private class GetEventTypes extends AsyncTask<Void,Void,ArrayList<EventTypesModel>>{
      
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			// Showing progress dialog before sending http request
			if(pDialog == null){
				pDialog = Util.createProgressDialog(UserCreateEvent.this);
				pDialog.setCancelable(false);
				pDialog.show();}
			else{
				pDialog.setCancelable(false);
				pDialog.show();
			}}

		
		@Override
		protected ArrayList<EventTypesModel> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			JSONObject getEventTypes = HttpGetClient.sendHttpPost(GET_EVENT_TYPES);
			try{
			if(getEventTypes !=null && getEventTypes.getString("status").equalsIgnoreCase("1")){
				JSONArray eventArray = getEventTypes.getJSONArray("eventTypes");
				if(eventArray.length()>0){
					
					for(int i=0;i<mArrayListEventTypes.size();i++){
						JSONObject eventTypeData = eventArray.getJSONObject(i);
						EventTypesModel model = new EventTypesModel();
						model.setEvent_type_id(eventTypeData.getString("event_type_id"));
						model.setEvent_type_name(eventTypeData.getString("event_type_name"));
						mArrayListEventTypes.add(model);
					}
				}
			}
			}catch(JSONException e){
				
			}
			return mArrayListEventTypes;
		}
		
		@Override
		protected void onPostExecute(ArrayList<EventTypesModel> result)
		{
			super.onPostExecute(result);
			pDialog.dismiss();
			if(result!=null){
				if(result.size()>0){
				EventTypesAdapter adapter = new EventTypesAdapter(UserCreateEvent.this,result);
				mSpinnerEventTypes.setAdapter(adapter);
				
				}
			}
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
			ConnectionDetector conn = new ConnectionDetector(UserCreateEvent.this);
            if(conn.isConnectingToInternet()){
            	parserTask.execute(result);
            } else{
            	alert.showAlertDialog(UserCreateEvent.this,"Connection Error","Check your Internet Connection",false);
            }

			
		}
	}


}


