package com.nxgenminds.eduminds.ju.cms.utils;

import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class NewIteamsChecking {
	// Shared Preferences
	SharedPreferences prefCheckNavList;
	// Editor for Shared preferences
	Editor editor;
	// Context
	Context _context;
	// Shared pref mode
	int PRIVATE_MODE = 0;
	// Sharedpref file name
	private static final String PREF_NAME = "prefNavlistcheck";
	// User name (make variable public to access from outside)
	public static final String NOTIFICATIONS_PUSH = "notification_push";

	public static final String NOTIFICATIONS_EVENTS = "notification_events";

	public static final String NOTIFICATIONS_MESSAGES = "notification_messages";

	public static final String NOTIFICATIONS_BROADCAST = "notification_broadcast";

	public static final String NOTIFICATIONS_GROUP = "notification_group";

	// Constructor
	public NewIteamsChecking(Context context){
		this._context = context;
		prefCheckNavList = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = prefCheckNavList.edit();
	}

	public void pushNotificationNew(String notificationType){

		if(notificationType.equalsIgnoreCase("notification")){
			editor.putString(NOTIFICATIONS_PUSH, "new");
		}else if(notificationType.equalsIgnoreCase("event")){
			editor.putString(NOTIFICATIONS_EVENTS, "new");
		}else if(notificationType.equalsIgnoreCase("message")){
			editor.putString(NOTIFICATIONS_MESSAGES, "new");
		}else if(notificationType.equalsIgnoreCase("broadcast")){
			editor.putString(NOTIFICATIONS_BROADCAST, "new");
		}else if(notificationType.equalsIgnoreCase("group")){
			editor.putString(NOTIFICATIONS_GROUP, "new");
		}
		// commit changes
		editor.commit();
	}

	public void clickedpushNotificationNew(String notificationType){

		if(notificationType.equalsIgnoreCase("notification")){
			editor.putString(NOTIFICATIONS_PUSH, "old");
		}else if(notificationType.equalsIgnoreCase("event")){
			editor.putString(NOTIFICATIONS_EVENTS, "old");
		}else if(notificationType.equalsIgnoreCase("message")){
			editor.putString(NOTIFICATIONS_MESSAGES, "old");
		}else if(notificationType.equalsIgnoreCase("broadcast")){
			editor.putString(NOTIFICATIONS_BROADCAST, "old");
		}else if(notificationType.equalsIgnoreCase("group")){
			editor.putString(NOTIFICATIONS_GROUP, "old");
		}
		// commit changes
		editor.commit();
	}

	public HashMap<String, String>  checknavlistiteams(){
		HashMap<String, String> list = new HashMap<String, String>();
		list.put(NOTIFICATIONS_PUSH, prefCheckNavList.getString(NOTIFICATIONS_PUSH, "old"));
		list.put(NOTIFICATIONS_EVENTS, prefCheckNavList.getString(NOTIFICATIONS_EVENTS, "old"));
		list.put(NOTIFICATIONS_MESSAGES, prefCheckNavList.getString(NOTIFICATIONS_MESSAGES, "old"));
		list.put(NOTIFICATIONS_BROADCAST, prefCheckNavList.getString(NOTIFICATIONS_BROADCAST, "old"));
		list.put(NOTIFICATIONS_GROUP, prefCheckNavList.getString(NOTIFICATIONS_GROUP, "old"));
		return list;
	}


}
