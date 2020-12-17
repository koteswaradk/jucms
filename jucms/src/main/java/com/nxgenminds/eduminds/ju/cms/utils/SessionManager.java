package com.nxgenminds.eduminds.ju.cms.utils;

import java.util.HashMap;

import org.apache.http.client.HttpClient;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.nxgenminds.eduminds.ju.cms.HomePage;
import com.nxgenminds.eduminds.ju.cms.NavMainActivity;

public class SessionManager {
	// Shared Preferences

	SharedPreferences pref;

	// Editor for Shared preferences
	Editor editor;

	// Context
	Context _context;

	// Shared pref mode
	int PRIVATE_MODE = 0;

	// Sharedpref file name
	private static final String PREF_NAME = "jucmslogin";

	// All Shared Preferences Keys
	private static final String IS_LOGIN = "IsLoggedIn";

	// User name (make variable public to access from outside)
	public static final String KEY_NAME = "username";

	// Email address (make variable public to access from outside)
	public static final String KEY_PASSWORD = "password";

	public static final String KEY_USERID = "userid";

	public static final String KEY_API_TOKEN = "userapitoken";
	public static final String KEY_ROLE = "userrole";
	public static final String KEY_SEMESTER = "usersemester";
	public static final String KEY_CHAT_USERNAME = "userchatname";
	public static final String KEY_CHAT_PASSWORD = "userchatpassword";
	public static final String KEY_ISADMIN = "isadmin";

	public static HttpClient httpClient ;

	//Util.API_TOKEN,Util.ROLE,Util.SEMESTER,Util.CHAT_USERNAME,Util.CHAT_PASSWORD)

	// Constructor
	public SessionManager(Context context){
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	/**
	 * Create login session
	 * */
	public void createLoginSession(String name, String password , String UserId,
			String apikey,String role, String semester,String chatusername,String chatpassword,String isadmin){
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);

		// Storing name in pref
		editor.putString(KEY_NAME, name);

		// Storing email in pref
		editor.putString(KEY_PASSWORD, password);

		editor.putString(KEY_USERID, UserId);

		editor.putString(KEY_API_TOKEN, apikey);

		editor.putString(KEY_ROLE, role);

		editor.putString(KEY_SEMESTER, semester);

		editor.putString(KEY_CHAT_USERNAME, chatusername);

		editor.putString(KEY_CHAT_PASSWORD, chatpassword);

		editor.putString(KEY_ISADMIN, isadmin);


		// commit changes
		editor.commit();
	}   


	public void changePassword(String password){

		editor.putString(KEY_CHAT_PASSWORD, password);
		// commit changes
		editor.commit();

	}
	/**
	 * Check login method wil check user login status
	 * If false it will redirect user to login page
	 * Else won't do anything
	 * */
	public void checkLogin(){
		// Check login status
		if(!this.isLoggedIn()){
			// user is not logged in redirect him to Login Activity
			Intent i = new Intent(_context, HomePage.class);
			// Closing all the Activities
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			// Add new Flag to start new Activity
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			// Staring Login Activity
			_context.startActivity(i);
		}else{
			Intent i = new Intent(_context, NavMainActivity.class);
			_context.startActivity(i);
		}

	}



	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> getUserDetails(){
		HashMap<String, String> user = new HashMap<String, String>();
		// user name
		user.put(KEY_NAME, pref.getString(KEY_NAME, null));

		user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));

		user.put(KEY_USERID, pref.getString(KEY_USERID, null));

		user.put(KEY_API_TOKEN, pref.getString(KEY_API_TOKEN, null));

		user.put(KEY_ROLE, pref.getString(KEY_ROLE, null));

		user.put(KEY_SEMESTER, pref.getString(KEY_SEMESTER, null));

		user.put(KEY_CHAT_USERNAME, pref.getString(KEY_CHAT_USERNAME, null));

		user.put(KEY_CHAT_PASSWORD, pref.getString(KEY_CHAT_PASSWORD, null));

		user.put(KEY_ISADMIN, pref.getString(KEY_ISADMIN, null));
		// return user
		return user;
	}

	/**
	 * Clear session details
	 * */
	public void logoutUser(){
		// Clearing all data from Shared Preferences
		editor.clear();
		editor.commit();

		// After logout redirect user to Loing Activity
		Intent i = new Intent(_context, HomePage.class);
		// Closing all the Activities
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		// Add new Flag to start new Activity
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		// Staring Login Activity
		_context.startActivity(i);
	}

	/**
	 * Quick check for login
	 * **/
	// Get Login State
	public boolean isLoggedIn(){
		return pref.getBoolean(IS_LOGIN, false);


	}
}