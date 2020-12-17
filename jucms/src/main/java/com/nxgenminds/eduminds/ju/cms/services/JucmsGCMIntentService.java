package com.nxgenminds.eduminds.ju.cms.services;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.broadcastreceivers.JucmsGCMBroadCastReceiver;
import com.nxgenminds.eduminds.ju.cms.userLogin.MainSplashScreen;
import com.nxgenminds.eduminds.ju.cms.utils.NewIteamsChecking;


public class JucmsGCMIntentService extends IntentService {

	//public static final int NOTIFICATION_ID = 1;
	public static String TAG;
	//String goTo ;
	String title ,subTitle;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;
	NewIteamsChecking checkingiteams;

	public JucmsGCMIntentService() {
		super("GcmIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		System.out.println("service");
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		// The getMessageType() intent parameter must be the intent you received in your BroadcastReceiver.

		String messageType = gcm.getMessageType(intent);
		System.out.println(messageType);
		//goTo = extras.getString("price");

		String jsonstring = extras.getString("jk_noti");
		System.out.println("EXTRAS "+extras.toString());
		System.out.println("jsonstring "+jsonstring.toString());
		try {
			JSONObject jsonObject = new JSONObject(jsonstring);
			title = jsonObject.getString("title");
			subTitle = jsonObject.getString("text");
		} catch (JSONException e) {
			e.printStackTrace();
		}		
		checkingiteams = new NewIteamsChecking(getApplicationContext());

		if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
			/*
			 * Filter messages based on message type. Since it is likely that GCM
			 * will be extended in the future with new message types, just ignore
			 * any message types you're not interested in, or that you don't
			 * recognize.
			 */
			if (GoogleCloudMessaging.
					MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
				sendNotification("Send error: " + extras.toString());
			} else if (GoogleCloudMessaging.
					MESSAGE_TYPE_DELETED.equals(messageType)) {
				sendNotification("Deleted messages on server: " +
						extras.toString());
				// If it's a regular GCM message, do some work.
			} else if (GoogleCloudMessaging.
					MESSAGE_TYPE_MESSAGE.equals(messageType)) {

				sendNotification("Received: " + extras.toString());
				Log.i(TAG, "Received: " + extras.toString());
			}
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		JucmsGCMBroadCastReceiver.completeWakefulIntent(intent);
	}

	// Put the message into a notification and post it.
	// This is just one simple example of what you might choose to do with
	// a GCM message.

	private void sendNotification(String msg) {
		int NotificationNewID = 0;
		NotificationCompat.Builder mBuilder =	new NotificationCompat.Builder(this)
		.setSmallIcon(R.drawable.jgipushicon)
		.setContentTitle("New "+title)
		.setContentText(subTitle);

		if(title.equalsIgnoreCase("Notification")){
			checkingiteams.pushNotificationNew("notification");
			NotificationNewID = 11111;
		}else if(title.equalsIgnoreCase("Event")){
			checkingiteams.pushNotificationNew("event");
			NotificationNewID = 22222;
		}else if(title.equalsIgnoreCase("Message")){
			checkingiteams.pushNotificationNew("message");
			NotificationNewID = 33333;
		}else if(title.equalsIgnoreCase("Broadcast")){
			checkingiteams.pushNotificationNew("broadcast");
			NotificationNewID = 44444;
		}else if(title.equalsIgnoreCase("Group")){
			checkingiteams.pushNotificationNew("group");
			NotificationNewID = 55555;
		}

		Intent resultIntent = new Intent(this, MainSplashScreen.class);

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =	stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

		mBuilder.setContentIntent(resultPendingIntent);
		mBuilder.setAutoCancel(true);
		mBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
		//Vibration
		mBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

		//LED
		mBuilder.setLights(Color.RED, 3000, 3000);
		NotificationManager mNotificationManager =
				(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.

		mNotificationManager.notify(NotificationNewID, mBuilder.build());

	}
}