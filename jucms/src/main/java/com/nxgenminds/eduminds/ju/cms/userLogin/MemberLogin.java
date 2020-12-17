package com.nxgenminds.eduminds.ju.cms.userLogin;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

public class MemberLogin extends ActionBarActivity {

	private EditText userName;
	private EditText userPhoneNumber;
	private CheckBox termsAndConditionsCheckBox;
	private Button registerButton;
	private TextView termsAndConditionsTextView;
	private TextView membertv,memberlogintv;
	private JSONObject jsonObjRecv;
	private String loginId="";
	private String loginuser="";
	private String loginemail="";
	private String logintoken="";
	private ProgressDialog pDialog;
	private String admin_v="";
	private String FirstLogin;

	AlertDialogManager alert = new AlertDialogManager();

	// Session Manager Class
	SessionManager session;
	GoogleCloudMessaging gcm;
	AtomicInteger msgId = new AtomicInteger();
	Context context;
	static final String TAG = "JUCMS GCM";
	String regid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.memberlogin);
		initView();
		// Session Manager
		context = getApplicationContext();
		session = new SessionManager(getApplicationContext()); 

		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();

		registerButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(userName.getText().toString().trim().length() >= 3 && userPhoneNumber.getText().toString().trim().length() >= 6){
					if(termsAndConditionsCheckBox.isChecked()){
						ConnectionDetector conn = new ConnectionDetector(MemberLogin.this);
						if(conn.isConnectingToInternet()){
							InputMethodManager inputManager = (InputMethodManager)
									getSystemService(Context.INPUT_METHOD_SERVICE); 
							inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
									InputMethodManager.HIDE_NOT_ALWAYS);
							new LoginAsync().execute();
						}else{
							alert.showAlertDialog(MemberLogin.this, "Connection Error", "Please check your internet connection", false);
						}
					}else{
						Toast.makeText(getApplicationContext(), "Accept terms and Conditions"  , Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(getApplicationContext(), "Please enter details"  , Toast.LENGTH_SHORT).show();
				}
			}
		});

		termsAndConditionsTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Util.API_WEBSITE_URL+"/policy.html"));
				startActivity(browserIntent);
				}
		});
	}

	private void initView() {
		membertv = (TextView) findViewById(R.id.memberloginMemberTextview);
		memberlogintv = (TextView) findViewById(R.id.memberloginLoginTextview);

		userName = (EditText) findViewById(R.id.memberloginName);
		userPhoneNumber = (EditText) findViewById(R.id.memberloginPhoneNumber);
		termsAndConditionsCheckBox = (CheckBox) findViewById(R.id.memberloginCheck);
		registerButton = (Button) findViewById(R.id.memberloginRegister);
		termsAndConditionsTextView = (TextView) findViewById(R.id.memberloginPolicy);
		/*	TelephonyManager tMgr = (TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
		String mPhoneNumber = tMgr.getLine1Number();
		userPhoneNumber.setText(mPhoneNumber);*/
		Typeface typeFace=Typeface.createFromAsset(getAssets(),"fonts/BentonSans-Thin.otf");
		Typeface typeFacemedium=Typeface.createFromAsset(getAssets(),"fonts/BentonSans-Medium.otf");
		// addind Font 
		membertv.setTypeface(typeFace);
		memberlogintv.setTypeface(typeFace);
		termsAndConditionsTextView.setTypeface(typeFace);
		userName.setTypeface(typeFacemedium);
		userPhoneNumber.setTypeface(typeFacemedium);
		registerButton.setTypeface(typeFacemedium);
	}

	private class LoginAsync extends AsyncTask<Void, Void, JSONObject>{

		@Override
		protected void onPreExecute() {
			if(pDialog == null){
				pDialog = Util.createProgressDialog(MemberLogin.this);
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
				jsonObjSend.put("username",userName.getText().toString().trim());
				jsonObjSend.put("password",userPhoneNumber.getText().toString().trim());
			} 
			catch (JSONException e) {
				e.printStackTrace();
			}

			jsonObjRecv = HttpPostClient.sendHttpPost(Util.LOGINAPI, jsonObjSend);
			return jsonObjRecv;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			pDialog.dismiss();
			JSONObject userResponse = null;
			try{
				if(HttpPostClient.statuscode == 500){
					Toast.makeText(MemberLogin.this, "Server Error", Toast.LENGTH_SHORT).show();
				}else if(HttpPostClient.statuscode == 504){
					Toast.makeText(MemberLogin.this, "Server Gateway Time Out", Toast.LENGTH_SHORT).show();
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
						logintoken = jsonObjRecv.getString("csrf_token");
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
							Util.CHAT_USERNAME = userName.getText().toString().trim();
							Util.CHAT_PASSWORD = userPhoneNumber.getText().toString().trim();
							session.createLoginSession(userName.getText().toString().trim(), userPhoneNumber.getText().toString().trim(),
									loginId,Util.API_TOKEN,Util.ROLE,Util.SEMESTER_ID,Util.CHAT_USERNAME,Util.CHAT_PASSWORD,Util.ADMIN);
						}
						//end

						if (checkPlayServices()) {
							gcm = GoogleCloudMessaging.getInstance(context);
							regid = getRegistrationId(context);
							if (regid.isEmpty()) {
								registerInBackground();
							}
						} else {
						}

						if(FirstLogin.equalsIgnoreCase("1"))
						{

							Intent i = new Intent(getApplicationContext(),ResetPassword.class);
							i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
							i.putExtra("loginID", loginId);
							i.putExtra("old_password", userPhoneNumber.getText().toString().trim());
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
			return false;
		}
		return true;
	}

	private void sendRegistrationIdToServer(String id) throws IOException {

		JSONObject jsonObjSend = new JSONObject();

		try {
			jsonObjSend.put("user",userName.getText().toString().trim());
			jsonObjSend.put("type","android");
			jsonObjSend.put("token",id);
			Util.GCM_KEY_STORE = id;
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
		
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

}
