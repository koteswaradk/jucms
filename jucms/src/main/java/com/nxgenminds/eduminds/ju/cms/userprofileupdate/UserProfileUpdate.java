package com.nxgenminds.eduminds.ju.cms.userprofileupdate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.models.UserProfileInfoModel;
import com.nxgenminds.eduminds.ju.cms.models.Users;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpPostClientImageUpdate;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpPutClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.InternalStorageContentProvider;
import com.nxgenminds.eduminds.ju.cms.utils.PlaceJSONParser;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


import eu.janmuller.android.simplecropimage.CropImage;

public class UserProfileUpdate extends Activity {
	private Button done;
	private ImageView profile_pic,cover_pic;
	private EditText user_firstname,user_phone,user_email,user_blog,user_edu,user_work,user_bio,user_lastname;
	public ImageLoader imageLoader;
	DisplayImageOptions profile_options,cover_option;
	private ProgressDialog pDialog;
	AutoCompleteTextView user_address;
	PlacesTask placesTask;
	ParserTask parserTask;
	private String lastname_tmp,firstname_tmp,phone_tmp,email_tmp,blog_tmp,location_tmp,edu_tmp,work_tmp,bio_tmp;
	RadioButton gallery,camera;
	RadioGroup  select; 
	private  File mFileTemp;
	public static final String TAG = "UserProfileUpdate";    
	public static final int REQUEST_CODE_GALLERY      = 0x1;
	public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
	public static final int REQUEST_CODE_CROP_IMAGE   = 0x3;
	public static final int SONG_RESULT = 0x4;
	public int flag;
	public String photo_updated_path;
	private JSONObject jsonObjectReceive;
	private static String updateString="NoUpdate";
	private  boolean photoFlag;
	UserProfileInfoModel user_model;
	private String error_status;
	JSONObject jsonObjRecvUpdate;

	private AlertDialogManager alert = new AlertDialogManager();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_update);
		imageLoader =imageLoader.getInstance();
		Typeface typeFace=Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/BentonSans-Regular.otf");
		photoFlag=false;
		profile_options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.placeholder)
		.showImageForEmptyUri(R.drawable.placeholder) 
		.showImageOnFail(R.drawable.placeholder)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();

		cover_option = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.placeholder_timefeed)
		.showImageForEmptyUri(R.drawable.placeholder_timefeed) 
		.showImageOnFail(R.drawable.placeholder_timefeed)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();

		done=(Button)findViewById(R.id.update_done);
		profile_pic=(ImageView)findViewById(R.id.update_profilepic);
		cover_pic=(ImageView)findViewById(R.id.update_user_cover_pic);
		user_address=(AutoCompleteTextView)findViewById(R.id.update_useraddress);
		user_address.setThreshold(1);
		user_firstname=(EditText)findViewById(R.id.update_FirstName);
		user_lastname=(EditText)findViewById(R.id.update_LastName);
		user_phone=(EditText)findViewById(R.id.update_userphone);
		user_email=(EditText)findViewById(R.id.update_useremail);
		user_blog=(EditText)findViewById(R.id.update_userblog);
		user_edu=(EditText)findViewById(R.id.update_userstudied);
		user_work=(EditText)findViewById(R.id.update_userwork);
		user_bio=(EditText)findViewById(R.id.update_user_bio_content);

		if(Util.ROLE.equalsIgnoreCase("class monitor"))
		{
			user_firstname.setEnabled(false);
			user_lastname.setEnabled(false);
			user_phone.setEnabled(false);
			user_email.setEnabled(false);
			user_blog.setEnabled(false);
			user_edu.setEnabled(false);
			user_work.setEnabled(false);
			user_bio.setEnabled(false);
			user_address.setEnabled(false);
			done.setVisibility(View.INVISIBLE);

		}
		else if(Util.ROLE.equalsIgnoreCase("teacher")){
			user_firstname.setEnabled(true);
			user_lastname.setEnabled(true);
			user_phone.setEnabled(true);
			user_email.setEnabled(true);
			user_blog.setEnabled(true);
			user_edu.setEnabled(true);
			user_address.setEnabled(true);
			user_work.setEnabled(true);
			user_bio.setEnabled(true);
			done.setVisibility(View.INVISIBLE);

		}
		else if(Util.ROLE.equalsIgnoreCase("alumni")){
			user_firstname.setEnabled(true);
			user_lastname.setEnabled(true);
			user_phone.setEnabled(true);
			user_email.setEnabled(true);
			user_blog.setEnabled(true);
			user_edu.setEnabled(true);
			user_work.setEnabled(true);
			user_address.setEnabled(true);
			user_bio.setEnabled(true);
			done.setVisibility(View.INVISIBLE);

		}
		else if(Util.ROLE.equalsIgnoreCase("attendence admin") ||Util.ROLE.equalsIgnoreCase("feedback admin") ||
				Util.ROLE.equalsIgnoreCase("internship admin") || Util.ROLE.equalsIgnoreCase("admin") || Util.ROLE.equalsIgnoreCase("sprit45 admin")
				|| Util.ROLE.equalsIgnoreCase("timetable admin") || Util.ROLE.equalsIgnoreCase("alumni admin")){
			user_firstname.setEnabled(true);
			user_lastname.setEnabled(true);
			user_phone.setEnabled(true);
			user_email.setEnabled(true);
			user_blog.setEnabled(true);
			user_edu.setEnabled(true);
			user_address.setEnabled(true);
			user_work.setEnabled(true);
			user_bio.setEnabled(true);
			done.setVisibility(View.INVISIBLE);

		}
		else if(Util.ROLE.equalsIgnoreCase("student")){
			user_firstname.setEnabled(false);
			user_lastname.setEnabled(false);
			user_phone.setEnabled(false);
			user_email.setEnabled(false);
			user_blog.setEnabled(false);
			user_address.setEnabled(false);
			user_edu.setEnabled(false);
			user_work.setEnabled(false);
			user_bio.setEnabled(false);
			done.setVisibility(View.INVISIBLE);

		}

		else if(Util.ROLE.equalsIgnoreCase("parent")){
			user_firstname.setEnabled(false);
			user_lastname.setEnabled(false);
			user_address.setEnabled(false);
			user_phone.setEnabled(false);
			user_email.setEnabled(false);
			user_blog.setEnabled(false);
			user_edu.setEnabled(false);
			user_work.setEnabled(false);
			user_bio.setEnabled(false);
			done.setVisibility(View.INVISIBLE);

		}
		else if(Util.ROLE.equalsIgnoreCase("office admin")){
			user_firstname.setEnabled(true);
			user_lastname.setEnabled(true);
			user_phone.setEnabled(true);
			user_email.setEnabled(true);
			user_blog.setEnabled(true);
			user_edu.setEnabled(true);
			user_address.setEnabled(true);
			user_work.setEnabled(true);
			user_bio.setEnabled(true);
			done.setVisibility(View.INVISIBLE);

		}
		else if(Util.ROLE.equalsIgnoreCase("office staff")){
			user_firstname.setEnabled(true);
			user_lastname.setEnabled(true);
			user_phone.setEnabled(true);
			user_email.setEnabled(true);
			user_blog.setEnabled(true);
			user_edu.setEnabled(true);
			user_address.setEnabled(true);
			user_work.setEnabled(true);
			user_bio.setEnabled(true);
			done.setVisibility(View.INVISIBLE);

		}

		ConnectionDetector conn = new ConnectionDetector(getApplicationContext());
		if(conn.isConnectingToInternet())
		{
			new UserProfileInfoFragmentAsync().execute();	
		}
		else 
		{
			//Crouton.makeText(UserProfileUpdate.this, getString(R.string.crouton_message), Style.ALERT).show();

		}

		done.setOnClickListener(new  OnClickListener() {

			@Override
			public void onClick(View v) {
				ConnectionDetector conn = new ConnectionDetector(UserProfileUpdate.this);
				if(conn.isConnectingToInternet())
				{
					new profileUpdate().execute();
				}
				else{
					//Crouton.makeText(UserProfileUpdate.this, getString(R.string.crouton_message), Style.ALERT).show();
				}
			}
		});

		cover_pic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				flag=2;
				final Dialog dialog = new Dialog(UserProfileUpdate.this);  
				dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
						WindowManager.LayoutParams.FLAG_FULLSCREEN);  
				dialog.setContentView(R.layout.dialog_select_gallery);  
				dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
				dialog.setTitle("Select");
				dialog.show(); 
				select=(RadioGroup)dialog.findViewById(R.id.select);
				gallery =(RadioButton)dialog.findViewById(R.id.gallery);
				gallery.setChecked(false);
				camera =(RadioButton)dialog.findViewById(R.id.camera);
				camera.setChecked(false);
				camera.setOnClickListener(new Button.OnClickListener(){

					@Override
					public void onClick(View v) {
						dialog.dismiss();  
						String state = Environment.getExternalStorageState();
						if (Environment.MEDIA_MOUNTED.equals(state)) {
							mFileTemp = new File(Environment.getExternalStorageDirectory(), Util.TEMP_PHOTO_FILE_NAME);
						}
						else {
							mFileTemp = new File(getApplicationContext().getFilesDir(), Util.TEMP_PHOTO_FILE_NAME);
						}
						takePicture();


					}});
				gallery.setOnClickListener(new Button.OnClickListener(){

					@Override
					public void onClick(View v) {
						dialog.dismiss();  
						openGallery();
						String state = Environment.getExternalStorageState();
						if (Environment.MEDIA_MOUNTED.equals(state)) {
							mFileTemp = new File(Environment.getExternalStorageDirectory(), Util.TEMP_PHOTO_FILE_NAME);
						}
						else {
							mFileTemp = new File(getApplicationContext().getFilesDir(), Util.TEMP_PHOTO_FILE_NAME);
						}

					}});
			} 
		});

		profile_pic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				flag=1;
				final Dialog dialog = new Dialog(UserProfileUpdate.this);  
				dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
						WindowManager.LayoutParams.FLAG_FULLSCREEN);  
				dialog.setContentView(R.layout.dialog_select_gallery);  
				dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
				dialog.setTitle("SELECT");
				dialog.show(); 
				select=(RadioGroup)dialog.findViewById(R.id.select);
				gallery =(RadioButton)dialog.findViewById(R.id.gallery);
				gallery.setChecked(false);
				camera =(RadioButton)dialog.findViewById(R.id.camera);
				camera.setChecked(false);
				camera.setOnClickListener(new Button.OnClickListener(){

					@Override
					public void onClick(View v) {
						dialog.dismiss();  
						String state = Environment.getExternalStorageState();
						if (Environment.MEDIA_MOUNTED.equals(state)) {
							mFileTemp = new File(Environment.getExternalStorageDirectory(), Util.TEMP_PHOTO_FILE_NAME);
						}
						else {
							mFileTemp = new File(getApplicationContext().getFilesDir(), Util.TEMP_PHOTO_FILE_NAME);
						}
						takePicture();


					}});
				gallery.setOnClickListener(new Button.OnClickListener(){

					@Override
					public void onClick(View v) {
						dialog.dismiss();  
						openGallery();
						String state = Environment.getExternalStorageState();
						if (Environment.MEDIA_MOUNTED.equals(state)) {
							mFileTemp = new File(Environment.getExternalStorageDirectory(), Util.TEMP_PHOTO_FILE_NAME);
						}
						else {
							mFileTemp = new File(getApplicationContext().getFilesDir(), Util.TEMP_PHOTO_FILE_NAME);
						}

					}});

			}
		});

		user_address.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				placesTask = new PlacesTask();
				ConnectionDetector conn = new ConnectionDetector(UserProfileUpdate.this);
				if(conn.isConnectingToInternet()){
					placesTask.execute(s.toString());
				} else{
					alert.showAlertDialog(UserProfileUpdate.this,"Connection Error","Check your Internet Connection",false);
				}


			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	private class UserProfileInfoFragmentAsync extends AsyncTask<Void, Void, UserProfileInfoModel>{

		@Override
		protected void onPreExecute() {
			if(pDialog == null){
				pDialog = Util.createProgressDialog(UserProfileUpdate.this);
				pDialog.setCancelable(false);
				pDialog.show();}
			else{
				pDialog.setCancelable(false);
				pDialog.show();

			}}

		@Override
		protected UserProfileInfoModel doInBackground(Void... params) {

			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(Util.API+"user/"+Util.USER_ID);
			user_model=new Gson().fromJson(jsonObjectRecived.toString(), UserProfileInfoModel.class);
			return user_model;
		}

		@Override
		protected void onPostExecute(UserProfileInfoModel user_info_model) {
			super.onPostExecute(user_info_model);
			pDialog.dismiss();
			if(user_info_model != null){
				error_status=user_info_model.getStatus();
				if(error_status.equalsIgnoreCase("1"))
				{
					Users user_info = new Users();
					user_info = user_info_model.getUsers()[0];

					if(user_info.getLastname()==null||user_info.getLastname().equalsIgnoreCase("null") || user_info.getLastname().equalsIgnoreCase("")){
						user_lastname.setText("");
					}
					else
					{
						user_lastname.setText(user_info.getLastname());
					}
					if(user_info.getFirstname()==null||user_info.getFirstname().equalsIgnoreCase("null") || user_info.getFirstname().equalsIgnoreCase("")){
						user_firstname.setText("");
					}
					else
					{
						user_firstname.setText(user_info.getFirstname());
					}

					if(user_info.getMobile()==null||user_info.getMobile().equalsIgnoreCase("null") ||user_info.getMobile().equalsIgnoreCase("")){
						user_phone.setText("");}
					else{user_phone.setText(user_info.getMobile());}


					if(user_info.getEdu()==null||user_info.getEdu().equalsIgnoreCase("null") || user_info.getEdu().equalsIgnoreCase("")){
						user_edu.setText("");}
					else{user_edu.setText(user_info.getEdu());}

					if(user_info.getWork()==null||user_info.getWork().equalsIgnoreCase("null") || user_info.getWork().equalsIgnoreCase("")){
						user_work.setText("");}
					else{user_work.setText(user_info.getWork());}

					if(user_info.getEmail()==null||user_info.getEmail().equalsIgnoreCase("null") ||user_info.getEmail().equalsIgnoreCase("")){
						user_email.setText("");}
					else{user_email.setText(user_info.getEmail());}

					if(user_info.getBio()==null||user_info.getBio().equalsIgnoreCase("null") || user_info.getBio().equalsIgnoreCase("")){
						user_bio.setText("");}
					else{user_bio.setText(user_info.getBio());}

					if(user_info.getCurrent_city_name()==null||user_info.getCurrent_city_name().equalsIgnoreCase("null") || user_info.getCurrent_city_name().equalsIgnoreCase("")){
						user_address.setText("");}
					else{user_address.setText(user_info.getCurrent_city_name());}

					if(user_info.getWebsite_blog()==null||user_info.getWebsite_blog().equalsIgnoreCase("null") || user_info.getWebsite_blog().equalsIgnoreCase("")){
						user_blog.setText("");}
					else{user_blog.setText(user_info.getWebsite_blog());}

					imageLoader.displayImage(user_info.getProfile_photo(), profile_pic, profile_options, new SimpleImageLoadingListener() {
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
					imageLoader.displayImage(user_info.getCover_photo(), cover_pic, cover_option, new SimpleImageLoadingListener() {
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

					// getting all the edittext value in temp variable
					lastname_tmp=user_info.getLastname();
					firstname_tmp=user_info.getFirstname();
					phone_tmp=user_info.getMobile();
					email_tmp=user_info.getEmail();
					blog_tmp=user_info.getWebsite_blog();
					location_tmp=user_info.getCurrent_city_name();
					edu_tmp=user_info.getEdu();
					work_tmp=user_info.getWork();
					bio_tmp=user_info.getBio();

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
			user_address.setAdapter(adapter);
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

			String input="Karnataka";

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
			ConnectionDetector conn = new ConnectionDetector(UserProfileUpdate.this);
			if(conn.isConnectingToInternet()){
				parserTask.execute(result);
			} else{
				alert.showAlertDialog(UserProfileUpdate.this,"Connection Error","Check your Internet Connection",false);
			}


		}
	}

	private class profileUpdate extends AsyncTask<Void, Void, JSONObject>{
		@Override
		protected void onPreExecute() {
			if(pDialog == null){
				pDialog = Util.createProgressDialog(UserProfileUpdate.this);
				pDialog.setCancelable(false);
				pDialog.show();}
			else{
				pDialog.setCancelable(false);
				pDialog.show();
			}}
		@Override
		protected JSONObject doInBackground(Void... params) {
			JSONObject jsonObjSend = new JSONObject();

			try {
				if(user_firstname.getText().toString().trim().length()>0)
				{
					jsonObjSend.put("firstname",user_firstname.getText().toString());
				}
				if(user_lastname.getText().toString().trim().length()>0)
				{
					jsonObjSend.put("lastname",user_lastname.getText().toString());
				}
				if(user_phone.getText().toString().trim().length()>0)
				{
					jsonObjSend.put("mobile",user_phone.getText().toString());
				}
				if(user_email.getText().toString().trim().length()>0)
				{
					jsonObjSend.put("email",user_email.getText().toString());
				}
				if(user_blog.getText().toString().trim().length()>0)
				{
					jsonObjSend.put("website_blog",user_blog.getText().toString());
				}
				if(user_address.getText().toString().trim().length()>0)
				{
					jsonObjSend.put("current_location",user_address.getText().toString());
				}
				if(user_edu.getText().toString().trim().length()>0)
				{
					jsonObjSend.put("edu",user_edu.getText().toString());
				}
				if(user_work.getText().toString().trim().length()>0)
				{
					jsonObjSend.put("work",user_work.getText().toString());
				}
				if(user_bio.getText().toString().trim().length()>0)
				{
					jsonObjSend.put("bio",user_bio.getText().toString());
				}

			} 
			catch (JSONException e) {
				e.printStackTrace();
			}
			if(user_email.getText().toString().trim().length()>0 && user_firstname.getText().toString().trim().length()>0 
					&& user_lastname.getText().toString().trim().length()>0 && user_phone.getText().toString().trim().length()>0)
			{
				jsonObjRecvUpdate = HttpPutClient.sendHttpPost(Util.API+"user/"+Util.USER_ID, jsonObjSend);
			}
			else
			{
				// error msg for mandatory  fields
			}
			return jsonObjRecvUpdate;
		}
		@Override
		protected void onPostExecute(JSONObject result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			updateString="update";
			Intent intent = new Intent();
			intent.putExtra("update", updateString);
			setResult(RESULT_OK,intent);
			finish();
		}

	}
	// change profile and cover photo
	private void takePicture() {  

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 

		try {
			Uri mImageCaptureUri = null;
			String state = Environment.getExternalStorageState();

			if (Environment.MEDIA_MOUNTED.equals(state)) {
				mImageCaptureUri = Uri.fromFile(mFileTemp);
			}
			else {

				mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
			}	
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
			intent.putExtra("return-data", true);
			startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
		} catch (ActivityNotFoundException e) {

		}
	}

	private void openGallery() {

		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY); 
	}

	private void startCropImage() {
		Intent intent = new Intent(UserProfileUpdate.this, CropImage.class);
		intent.putExtra(CropImage.IMAGE_PATH, mFileTemp.getPath());
		intent.putExtra(CropImage.SCALE, true);

		if(flag==1){
			intent.putExtra(CropImage.ASPECT_X, 2);
			intent.putExtra(CropImage.ASPECT_Y, 2);
		}else{
			intent.putExtra(CropImage.ASPECT_X, 3);
			intent.putExtra(CropImage.ASPECT_Y, 2);
		}
		startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
	}
	public static void copyStream(InputStream input, OutputStream output)
			throws IOException {

		byte[] buffer = new byte[1024];
		int bytesRead;
		while ((bytesRead = input.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode !=RESULT_OK) {

			return;
		}

		Bitmap bitmap;

		switch (requestCode) {

		case REQUEST_CODE_GALLERY:
			try {
				InputStream inputStream = UserProfileUpdate.this.getContentResolver().openInputStream(data.getData());
				FileOutputStream fileOutputStream = new FileOutputStream(mFileTemp);
				copyStream(inputStream, fileOutputStream);
				fileOutputStream.close();
				inputStream.close();
				startCropImage();

			} catch (Exception e) {

			}

			break;
		case REQUEST_CODE_TAKE_PICTURE:

			startCropImage();
			break;
		case REQUEST_CODE_CROP_IMAGE:

			String path = data.getStringExtra(CropImage.IMAGE_PATH);
			if (path == null) {
				return;
			}

			bitmap = BitmapFactory.decodeFile(mFileTemp.getPath());

			if(flag==1)
			{   ConnectionDetector conn = new ConnectionDetector(UserProfileUpdate.this);
			if(conn.isConnectingToInternet())
			{
				new PicUploadAsync().execute(1,7);
			}
			else
			{
				//Crouton.makeText(UserProfileUpdate.this, getString(R.string.crouton_message), Style.ALERT).show();
			}
			}
			else if (flag==2) {
				ConnectionDetector conn = new ConnectionDetector(UserProfileUpdate.this);
				if(conn.isConnectingToInternet())
				{
					new PicUploadAsync().execute(2,9);
				}
				else
				{
					//Crouton.makeText(UserProfileUpdate.this, getString(R.string.crouton_message), Style.ALERT).show();
				}
			}
			else
			{
			}

			break;
		}
		super.onActivityResult(requestCode, resultCode, data); 
	}
	private class PicUploadAsync extends AsyncTask<Integer, Void, Integer>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			// Showing progress dialog before sending http request
			if(pDialog == null){
				pDialog = Util.createProgressDialog(UserProfileUpdate.this);
				pDialog.setCancelable(false);
				pDialog.show();}
			else{
				pDialog.setCancelable(false);
				pDialog.show();
			}}

		@Override
		protected Integer doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			Integer param_flag=params[0];
			Integer api_flag=params[1];
			if(api_flag==7)
			{
				jsonObjectReceive = HttpPostClientImageUpdate.sendHttpPostImage(Util.API+"photo", mFileTemp,param_flag);
			}else
			{
				jsonObjectReceive = HttpPostClientImageUpdate.sendHttpPostImage(Util.API+"photo", mFileTemp,param_flag);
			}

			try {
				if(api_flag==7)
				{
					Util.NM_PROFILE_PIC=jsonObjectReceive.getString("actual_photo_path");
				}
				else
				{
					Util.USER_COVER_PIC=jsonObjectReceive.getString("actual_photo_path");
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			pDialog.dismiss();
			photoFlag=true;
			updateString="update";
			if(flag==1){
				imageLoader.displayImage(Util.NM_PROFILE_PIC, profile_pic, profile_options, new SimpleImageLoadingListener() {
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


				imageLoader.displayImage(Util.NM_PROFILE_PIC, profile_pic, profile_options, new SimpleImageLoadingListener() {
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


			}
			else if (flag==2) {
				imageLoader.displayImage(Util.USER_COVER_PIC, cover_pic, cover_option, new SimpleImageLoadingListener() {
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


			}
		}

	}
	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		intent.putExtra("update", updateString);
		setResult(RESULT_OK,intent);
		finish();
		//super.onBackPressed();

	}

}
