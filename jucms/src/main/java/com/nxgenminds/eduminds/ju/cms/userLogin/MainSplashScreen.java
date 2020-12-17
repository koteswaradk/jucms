package com.nxgenminds.eduminds.ju.cms.userLogin;

import java.io.IOException;
import java.util.HashMap;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
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


public class MainSplashScreen extends ActionBarActivity {

	// Session Manager Class
	SessionManager session;
	private ProgressDialog pDialog;
	Context context;
	AlertDialogManager alert = new AlertDialogManager();
	// gcm 
	GoogleCloudMessaging gcm;
	AtomicInteger msgId = new AtomicInteger();
	static final String TAG = "JUCMS GCM";
	String regid;
	private String FirstLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_splash_screen);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		session = new SessionManager(getBaseContext()); 
		context = getApplicationContext();
		// METHOD 1     
		// Session Manager

		if(session.isLoggedIn()){
			// get user data from session
			HashMap<String, String> user = session.getUserDetails();

			Util.ADMIN=user.get(SessionManager.KEY_ISADMIN);
			Util.API_TOKEN=user.get(SessionManager.KEY_API_TOKEN);
			Util.USER_ID = user.get(SessionManager.KEY_USERID);
			Util.ROLE = user.get(SessionManager.KEY_ROLE);
			Util.SEMESTER_ID = user.get(SessionManager.KEY_SEMESTER);
			Util.CHAT_USERNAME = user.get(SessionManager.KEY_CHAT_USERNAME);
			Util.CHAT_PASSWORD = user.get(SessionManager.KEY_CHAT_PASSWORD);
			ConnectionDetector conn = new ConnectionDetector(MainSplashScreen.this);
			if(conn.isConnectingToInternet()){
				new LoginAsyncDirect().execute();
			}
			else
			{
				alert.showAlertDialog(MainSplashScreen.this, "Connection Error", "Please check your internet connection", false);
			}
		}else{
			Intent i=new Intent(getBaseContext(),HomePage.class);
			startActivity(i);
			finish();
		}


	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}





	private class LoginAsyncDirect extends AsyncTask<Void, Void, JSONObject>{

		@Override
		protected void onPreExecute() {
			if(pDialog == null){
				pDialog = Util.createProgressDialog(MainSplashScreen.this);
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
				jsonObjSend.put("username",Util.CHAT_USERNAME);
				jsonObjSend.put("password",Util.CHAT_PASSWORD);
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
					Toast.makeText(MainSplashScreen.this, "Server Error", Toast.LENGTH_SHORT).show();
				}else if(HttpPostClient.statuscode == 504){
					Toast.makeText(MainSplashScreen.this, "Server Gateway Time Out", Toast.LENGTH_SHORT).show();
				}else if(result == null){
					Toast.makeText(getApplicationContext(), "Something went wrong !", Toast.LENGTH_SHORT).show();
				}
				else if(HttpPostClient.statuscode == 200 & result !=null)
				{
					if(result.getString("status").equalsIgnoreCase("0")){
						Toast.makeText(getApplicationContext(), result.getString("message").toString(), Toast.LENGTH_SHORT).show();
						Intent i = new Intent(getApplicationContext(),MemberLogin.class);
						startActivity(i);
						finish();	
					}
					else if(result.getString("status").equalsIgnoreCase("1")){

						userResponse = result.getJSONObject("userDet");
						Util.ADMIN = userResponse.getString("is_admin");
						FirstLogin= userResponse.getString("is_first_time_login");
						Util.API_TOKEN = result.getString("csrf_token");
						Util.USER_ID = userResponse.getString("user_id");
						Util.ROLE = userResponse.getString("role");
						Util.STREAM_ID = userResponse.getString("stream_id");
						Util.SEMESTER_ID = userResponse.getString("semester_id");
						Util.COMPANY_ID = userResponse.getString("company_id");
						Util.msgUserName= userResponse.getString("username");
						Util.SECTION_ID = userResponse.getString("section_id");
						Util.CHAT_USERNAME = userResponse.getString("username");


						//GCM
						if (checkPlayServices()) {
							gcm = GoogleCloudMessaging.getInstance(context);
							regid = getRegistrationId(context);
							registerInBackground();
						} else {

						}

						if(FirstLogin.equalsIgnoreCase("1"))
						{
							Intent i = new Intent(getApplicationContext(),ResetPassword.class);
							i.putExtra("loginID", Util.USER_ID);
							i.putExtra("old_password", Util.CHAT_PASSWORD);
							startActivity(i);
							finish();	
						}else
						{
							Intent i = new Intent(getApplicationContext(),NavMainActivity.class);
							i.putExtra("loginID", Util.USER_ID);
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
			jsonObjSend.put("user",Util.CHAT_USERNAME);
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
