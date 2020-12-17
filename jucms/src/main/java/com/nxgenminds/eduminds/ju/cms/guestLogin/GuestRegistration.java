package com.nxgenminds.eduminds.ju.cms.guestLogin;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.nxgenminds.eduminds.ju.cms.HomePage;
import com.nxgenminds.eduminds.ju.cms.NavMainActivity;
import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.ResetPassword;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpPostClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.SessionManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class GuestRegistration extends ActionBarActivity {

	private Button register;
	private EditText name,mobile,email,edittext_password;
	private TextView dob;
	private Spinner gender;
	private DatePickerDialog.OnDateSetListener date_d;
	private Calendar calendar;
	ConnectionDetector conn = new ConnectionDetector(GuestRegistration.this);
	AlertDialogManager alert = new AlertDialogManager();
	private ProgressDialog pDialog;
	private String username,user_id,password,token;

	private String loginId="";
	private String loginuser="";
	private String loginemail="";
	private String logintoken="";
	private String FirstLogin;

	SessionManager session;
	Context context;
	GoogleCloudMessaging gcm;
	String regid;

	private String gender_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guest_registration);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();

		context = getApplicationContext();
		session = new SessionManager(getApplicationContext()); 

		gender = (Spinner) findViewById(R.id.guestlogingender);
		dob=(TextView)findViewById(R.id.guestlogindob);
		register=(Button)findViewById(R.id.guestloginRegister);
		name=(EditText)findViewById(R.id.guestloginName);
		mobile=(EditText)findViewById(R.id.guestloginPhoneNumber);
		email=(EditText)findViewById(R.id.guestloginemail);
		edittext_password=(EditText)findViewById(R.id.guestpassword);

		calendar = Calendar.getInstance();

		dob.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				new DatePickerDialog(GuestRegistration.this, 2, date_d, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

			}
		});

		date_d = new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {

				calendar.set(Calendar.YEAR, year);
				calendar.set(Calendar.MONTH, monthOfYear);
				calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				String date_format = "yyyy-MM-dd";
				SimpleDateFormat sdf = new SimpleDateFormat(date_format);
				dob.setText(sdf.format(calendar.getTime()).toString()); 

			}
		};

		register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getMemberDetails();
				
				Log.i("After gwtting member details", "executed");
			}
		});


		gender.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent,View view, int position, long id) {
				
				if(gender.getSelectedItem().toString().equalsIgnoreCase("Male"))
				{
					gender_id="1";
				}
				else if(gender.getSelectedItem().toString().equalsIgnoreCase("Female"))
				{
					gender_id="2";
				}

				else
				{
					gender_id="3";
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}

		});

	}
	private class RegisterAsync extends AsyncTask<Void, Void, JSONObject>{

		@Override
		protected void onPreExecute() {
			if(pDialog == null){
				pDialog = Util.createProgressDialog(GuestRegistration.this);
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
				jsonObjSend.put("name",name.getText().toString().trim());
				jsonObjSend.put("dob",dob.getText().toString().trim());
				jsonObjSend.put("mobile",mobile.getText().toString().trim());
				jsonObjSend.put("email",email.getText().toString().trim());
				jsonObjSend.put("gender_id",gender_id);
				jsonObjSend.put("password",edittext_password.toString().trim());
			} 
			catch (JSONException e) {
				e.printStackTrace();
			}

			JSONObject jsonObjRecv = HttpPostClient.sendHttpPost(Util.API+"guests", jsonObjSend);
			return jsonObjRecv;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			pDialog.dismiss();
			JSONObject userResponse = null;
			try{
				if(HttpPostClient.statuscode == 500){
					Toast.makeText(GuestRegistration.this, "Server Error", Toast.LENGTH_SHORT).show();
				}else if(HttpPostClient.statuscode == 504){
					Toast.makeText(GuestRegistration.this, "Server Gateway Time Out", Toast.LENGTH_SHORT).show();
				}else if(result == null){
					Toast.makeText(getApplicationContext(), "Something went wrong !", Toast.LENGTH_SHORT).show();
				}
				else if(HttpPostClient.statuscode == 200 & result !=null)
				{
					if(result.getString("status").equalsIgnoreCase("0")){
						Toast.makeText(getApplicationContext(), result.getString("message").toString(), Toast.LENGTH_SHORT).show();
					}
					else if(result.getString("status").equalsIgnoreCase("1")){
						// session manager
						userResponse = result.getJSONObject("user_det");
						user_id = userResponse.getString("user_id");
						username = userResponse.getString("username");
						password=userResponse.getString("password");
						token = userResponse.getString("csrf_token");

						if(token!=null)
						{
							if(conn.isConnectingToInternet()){
								Log.i("calling login asynch", "executed");
								new LoginAsync().execute();

							}else{
								alert.showAlertDialog(GuestRegistration.this, "Connection Error", "Please check your internet connection", false);
							}
						}


					}
					else{
						Toast.makeText(getApplicationContext(), "Something went wrong !", Toast.LENGTH_SHORT).show();
					}
				}
			}catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	private class LoginAsync extends AsyncTask<Void, Void, JSONObject>{

		@Override
		protected void onPreExecute() {
			if(pDialog == null){
				pDialog = Util.createProgressDialog(GuestRegistration.this);
				pDialog.setCancelable(false);
				pDialog.show();}
			else{
				pDialog.setCancelable(false);
				pDialog.show();
			}}

		@Override
		protected JSONObject doInBackground(Void... params) {
			JSONObject jsonObjSend = new JSONObject();
			//	JSONObject userResponse = null;

			try {
				jsonObjSend.put("username",username);
				jsonObjSend.put("password",password);
			} 
			catch (JSONException e) {
				e.printStackTrace();
			}

			JSONObject jsonObjRecv = HttpPostClient.sendHttpPost(Util.LOGINAPI, jsonObjSend);
			return jsonObjRecv;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			pDialog.dismiss();
			JSONObject userResponse = null;
			try{
				if(HttpPostClient.statuscode == 500){
					Toast.makeText(GuestRegistration.this, "Server Error", Toast.LENGTH_SHORT).show();
				}else if(HttpPostClient.statuscode == 504){
					Toast.makeText(GuestRegistration.this, "Server Gateway Time Out", Toast.LENGTH_SHORT).show();
				}else if(result == null){
					Toast.makeText(getApplicationContext(), "Something went wrong !", Toast.LENGTH_SHORT).show();
				}
				else if(HttpPostClient.statuscode == 200 & result !=null)
				{
					if(result.getString("status").equalsIgnoreCase("0")){
						Toast.makeText(getApplicationContext(), result.getString("message").toString(), Toast.LENGTH_SHORT).show();
					}
					else if(result.getString("status").equalsIgnoreCase("1")){
						// session manager
						userResponse = result.getJSONObject("userDet");
						loginId = userResponse.getString("user_id");
						loginuser = userResponse.getString("username");
						loginemail = userResponse.getString("email");
						FirstLogin=userResponse.getString("is_first_time_login");
						logintoken = result.getString("csrf_token");
						Util.ADMIN = userResponse.getString("is_admin");

						if(logintoken!=null)
						{
							Util.API_TOKEN = logintoken;
							Util.USER_ID = loginId;
							Util.ROLE = userResponse.getString("role");
							Util.COMPANY_ID = userResponse.getString("company_id");
							Util.msgUserName= userResponse.getString("username");
							Util.STREAM_ID = userResponse.getString("stream_id");
							Util.SEMESTER_ID = userResponse.getString("semester_id");
							Util.SECTION_ID = userResponse.getString("section_id");
							Util.CHAT_USERNAME = username;
							Util.CHAT_PASSWORD = password;
							session.createLoginSession(username,password,
									loginId,Util.API_TOKEN,Util.ROLE,Util.SEMESTER_ID,Util.CHAT_USERNAME,Util.CHAT_PASSWORD,Util.ADMIN);
						}
						//end
						Log.i("calling check play services", "executed");
						if (checkPlayServices()) {
							gcm = GoogleCloudMessaging.getInstance(context);
							regid = getRegistrationId(context);
							
							if (regid.isEmpty()) {
								Log.i("calling register in background from check play service", "executed");
								registerInBackground();
							}
						} else {
						}

						if(FirstLogin.equalsIgnoreCase("1"))
						{

							Intent i = new Intent(getApplicationContext(),ResetPassword.class);
							i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
							i.putExtra("loginID", loginId);
							i.putExtra("old_password", password);
							startActivity(i);
							finish();	
						}else
						{
							Intent i = new Intent(getApplicationContext(),NavMainActivity.class);
							i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
							i.putExtra("loginID", loginId);
							startActivity(i);
							finish();	
						}

					}
					else{
						Toast.makeText(getApplicationContext(), "Something went wrong !", Toast.LENGTH_SHORT).show();
					}
				}
			}catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}


	/**
	 * Check the device to make sure it has the Google Play Services APK. If
	 * it doesn't, display a dialog that allows users to download the APK from
	 * the Google Play Store or enable it in the device's system settings.
	 */

	public boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						Util.PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				finish();
			}
			Log.i("check play service returning false", "executed");
			return false;
		}
		Log.i("check play service returning true", "executed");
		return true;
	}

	private void sendRegistrationIdToServer(String id) throws IOException {

		JSONObject jsonObjSend = new JSONObject();

		try {
			jsonObjSend.put("user",username);
			jsonObjSend.put("type","android");
			jsonObjSend.put("token",id);
			Util.GCM_KEY_STORE = id;
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
		Log.i("posted regid to server", "executed");
		HttpPostClient.sendHttpPost(Util.API_PUSH_URL+"/subscribe", jsonObjSend);
	}

	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration ID and the app versionCode in the application's
	 * shared preferences.
	 */

	public void registerInBackground() {

		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					regid = gcm.register(Util.GCMSENDER_ID);
					msg = "Device registered, registration ID=" + regid;

					// You should send the registration ID to your server over HTTP, so it
					// can use GCM/HTTP or CCS to send messages to your app.
					Log.i("got regid sending to server", "executed");
					sendRegistrationIdToServer(regid);

					// For this demo: we don't need to send it because the device will send
					// upstream messages to a server that echo back the message using the
					// 'from' address in the message.

					// Persist the regID - no need to register again.
					storeRegistrationId(context, regid);
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					// If there is an error, don't just keep trying to register.
					// Require the user to click a button again, or perform
					// exponential back-off.
				}
				return msg;
			}
		}.execute(null, null, null);
	}

	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private SharedPreferences getYouflikGcmPreferences(Context context) {
		// This sample app persists the registration ID in shared preferences, but
		// how you store the regID in your app is up to you.
		return getSharedPreferences(HomePage.class.getSimpleName(),
				Context.MODE_PRIVATE);
	}

	/**
	 * Stores the registration ID and the app versionCode in the application's
	 * {@code SharedPreferences}.
	 *
	 * @param context application's context.
	 * @param regId registration ID
	 */
	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getYouflikGcmPreferences(context);
		int appVersion = getAppVersion(context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(Util.PROPERTY_REG_ID, regId);
		editor.putInt(Util.PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}

	/**
	 * Gets the current registration ID for application on GCM service, if there is one.
	 * <p>
	 * If result is empty, the app needs to register.
	 *
	 * @return registration ID, or empty string if there is no existing
	 *         registration ID.
	 */
	@SuppressLint("NewApi")
	public String getRegistrationId(Context context) {
		final SharedPreferences prefs = getYouflikGcmPreferences(context);
		String registrationId = prefs.getString(Util.PROPERTY_REG_ID, "");
		if (registrationId.isEmpty()) {
			return "";
		}

		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.

		int registeredVersion = prefs.getInt(Util.PROPERTY_APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			return "";
		}

		return registrationId;
	}

	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	//user defined method to check for valid email 
	boolean isEmailValid(CharSequence iemail) {
		return android.util.Patterns.EMAIL_ADDRESS.matcher(iemail).matches();
	}

	// Get the details of the member,validate it
	@SuppressLint("NewApi")
	private void getMemberDetails() {
		CharSequence cs = (CharSequence) email.getText().toString().trim();		

		if(isEmailValid(cs) || email.getText().toString().trim().length()>0)
		{ 
			email.setError(null);}
		else{email.setError("Enter valid Email");}


		if (mobile.getText().toString().trim().length() <10) {
			mobile.setError("Enter valid mobile number");
		}

		if(dob.getText().toString().trim().length()<=0){
			Toast.makeText(getApplicationContext(), "Select your DOB", Toast.LENGTH_SHORT).show();
		}

		if(name.getText().toString().trim().length()<=0){
			name.setError("Enter your name");
		}
		TextView txt = (TextView) gender.getSelectedView();
		if(gender.getSelectedItemPosition()==0){
			txt.setError("Select the Gender");
		}


		if ((name.getText().toString().trim().length()>=4)
				&&  (mobile.length() == 10)	 
				&& (!(dob.length() <= 0)) 
				&& (gender.getSelectedItemPosition()!=0)
				&& (!(email.length() <= 0)) )
		{		
			name.setError(null);
			if(conn.isConnectingToInternet()){
				InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
				inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
				Log.i("calling register asynch", "executed");
				new RegisterAsync().execute();
				
			}else{
				alert.showAlertDialog(GuestRegistration.this, "Connection Error", "Please check your internet connection", false);
			}	  

		}
	}

}
