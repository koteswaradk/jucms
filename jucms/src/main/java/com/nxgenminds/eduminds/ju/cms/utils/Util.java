package com.nxgenminds.eduminds.ju.cms.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.BadTokenException;
import android.widget.ImageView;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.Feedback.FeedBackQuestionModel;
import com.nxgenminds.eduminds.ju.cms.events.FeedBackAnswerModel;


public class Util {

	//public static String USERNAME;
	public static String PASSWORD;
	public static String EMAIL;
	public static String API_TOKEN;
	public static String USER_ID;
	public static String ROLE;
	public static String USER_ROLE_ID;
	public static String IS_ADMIN;
	public static String STREAM_ID;
	public static String SEMESTER_ID;
	public static String SECTION_ID;
	public static String NM_PROFILE_PIC;
	public static String USER_COVER_PIC;
	public static String msgUserName;
	public static boolean intership_flag=false;
	public static String COMPANY_ID;
	
	//yoobikwiti gcm project number for Bschool app
	public static final String GCMSENDER_ID="631802537602";
	
	public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	public static final String PROPERTY_APP_VERSION = "appVersion";
	public static final String PROPERTY_REG_ID = "registration_id";
	public static String GCM_KEY_STORE = "";
	// Admin
	public static String ADMIN;

	public static String THIRD_PARTY_NAME;
	public static String THIRD_PARTY_ROLE;
	public static String THIRD_PARTY_ID;
	
	// Base URL
	public static String API = "http://www.yoobikwiti.com/jucms/public/index.php/Api/v1/"; 

	//production URL
	public static String API_PUSH_URL="http://www.yoobikwiti.com:8002";
	public static String API_WEBSITE_URL="http://www.yoobikwiti.com";
	
	public static String LOGINAPI = API + "login";
	public static String ConnectionAPI = API + "friends?user_id=";
	///
	public static  String stringArray_Menu_id[];
	public static String stringArray_Menu_name[];

	//storing the user details for nav items
	public static String user_firstname;
	public static String user_profile_pic;
	public static String user_cover_pic;

	//insights 
	public static double[] VALUES_I = new double[] {90, 250,80,60,450,120 };  


	public static class Config {
		public static final boolean DEVELOPER_MODE = false;
	}	

	public static class Extra {
		public static final String IMAGES = "com.nostra13.example.universalimageloader.IMAGES";
		public static final String IMAGE_POSITION = "com.nostra13.example.universalimageloader.IMAGE_POSITION";
	}

	public static final String TEMP_PHOTO_FILE_NAME = "jucms_temp_photo.jpg";
	public static final String TEMP_VIDEO_FILE_NAME = "jucms_temp_photo.mp4";

	//insights
	public static  String friends;
	public static String profile_view;
	public static String post;
	public static String likes;
	public static String photos;
	public static String videos; 

	//members
	public static String male;
	public static String female;
	public static String user;
	public static double male_percentage;
	public static double female_percentage;
	public static double male_percentage_final;
	public static double female_percentage_final;

	// Chat
	public static String HOST = "www.yoobikwiti.com";
	public static final int PORT = 5222;
	public static final String SERVICE = "yoobikwiti.com";
	public static  String CHAT_USERNAME ;
	public static  String CHAT_PASSWORD;
	public static String CHAT_LOGIN_JID;
	public static boolean IncomingChatMessage = false;
	public static String with_User_Image;

	//settings
	public static String noti_sound;
	public static String noti_vibration;
	public static String noti_post_like;
	public static String noti_message;
	public static String noti_conn;
	public static String noti_event;
	public static String noti_poll;
	public static String font_size;
	public static String language;
	//


	//events
	public static int FLAG_ARCHIVE_EVENT = 0;
	public static int EVENT_UPDATE_FLAG =0;

	public static final int  REQUEST_CODE_FOR_CREATE_EVENT=1; 
	public static final int  REQUEST_CODE_FOR_UPDATE_EVENT=2;
	public static final int  REQUEST_CODE_FOR_DELETE_EVENT=3;
	public static final int  REQUEST_CODE_FOR_DETAIL_EVENT=4;
	public static final int  REQUEST_CODE_FOR_UPCOMING_EVENTS=5;
	public static final int  REQUEST_CODE_FOR_CURRENT_EVENTS=6;
	public static final int  REQUEST_CODE_FOR_ARCHIVE_EVENTS=7;
	//	


	// feedback events
	public static String FEED_BACK_EVENT_ID;
	public static String FEEDBACK_EVENT_NAME;
	public static String FEEDBACK_EVENT_DESC;
	public static ArrayList<FeedBackQuestionModel> FEEDBACK_QUESTION_ARRAYLIST = new ArrayList<FeedBackQuestionModel>();
	public static ArrayList<FeedBackAnswerModel> FEEDBACK_ANSWER_ARRAYLIST =  new ArrayList<FeedBackAnswerModel>();
	//	

	public static ProgressDialog createProgressDialog(Context mContext) {
		ProgressDialog dialog = new ProgressDialog(mContext);
		try {
			dialog.show();
		} catch (BadTokenException e) {

		}
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.progressdialog);
		ImageView imageView = (ImageView) dialog.findViewById(R.id.spinnerImageView);
		AnimationDrawable spinner = (AnimationDrawable) imageView.getBackground();
		spinner.start();
		dialog.getWindow().getAttributes().gravity=Gravity.BOTTOM;
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();  
		lp.dimAmount=0.2f;
		dialog.getWindow().setAttributes(lp); 
		//dialog.setIndeterminate(true);
		// dialog.setMessage(Message);
		return dialog;
	}
	// User Update
	public static String  USER_FIRSTNAME,USER_LASTNAME,USER_EMAIL,USER_PROFESSION,USER_FULLADDRESS,USER_GENDER,USER_BLOODGROUP,
	USER_CITY,USER_STUDIEDAT,USER_WORKINGAT,USER_BIO,USER_PINCODE,USER_PHONENUMBER,USER_DOB,
	USER_CITY_ID,USER_GENDER_ID,USER_BLOODGROUP_ID;

	public static ArrayList<String> myList ;

	// flag for nav drawer
	public static boolean msgFlag=false;
	public static boolean eventFlag=false;
	public static boolean broadcastFlag=false;
	public static String S_USER_ID;

	/** A method to download json data from url */
	public static String downloadUrl(String strUrl) throws IOException{
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try{
			URL url = new URL(strUrl);

			// Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();

			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while( ( line = br.readLine()) != null){
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		}catch(Exception e){
			Log.d("Exception while downloading url", e.toString());
		}finally{
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}

}
