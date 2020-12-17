package com.nxgenminds.eduminds.ju.cms.settings;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpPutClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class SettingsNotificationsFragment extends Fragment implements OnClickListener {

	private Button button_nclose,button_nselected;
	private Fragment settingsfragment;
	private FrameLayout container;
	public ToggleButton like_post,msg,noti_sound,noti_vib,accept_request,events,poll;
	private ProgressDialog pDialog;
	public String lp,msgg,noti_s,noti_v,a_r,evnt,pol;
	LinearLayout post_cont,msg_cont,event_cont;

	private AlertDialogManager alert = new AlertDialogManager();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.settings_notifications, container, false);
		return rootView;
	}
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		container = (FrameLayout)getView().findViewById(R.id.content_frame);
		settingsfragment = new SettingsFragment();

		button_nclose = (Button)getView().findViewById(R.id.settings_notify_button_close);
		button_nselected = (Button)getView().findViewById(R.id.settings_notify_button_ok);    

		like_post=(ToggleButton)getView().findViewById(R.id.settings_notify_tbutton_likepost);
		msg=(ToggleButton)getView().findViewById(R.id.settings_notify_tbutton_sendmessage);
		noti_sound=(ToggleButton)getView().findViewById(R.id.settings_notify_tbutton_sound);
		noti_vib=(ToggleButton)getView().findViewById(R.id.settings_notify_tbutton_vib);
		events=(ToggleButton)getView().findViewById(R.id.settings_events_tbutton_connectionrequest);

		post_cont=(LinearLayout)getView().findViewById(R.id.cont_post);
		msg_cont=(LinearLayout)getView().findViewById(R.id.cont_msg);
		event_cont=(LinearLayout)getView().findViewById(R.id.cont_events);

		button_nclose.setOnClickListener(this);
		button_nselected.setOnClickListener(this);

		/// setting based on role


		if(Util.ROLE.equalsIgnoreCase("class monitor") || Util.ROLE.equalsIgnoreCase("attendence admin") || Util.ROLE.equalsIgnoreCase("feedback admin")
				|| Util.ROLE.equalsIgnoreCase("internship admin")||Util.ROLE.equalsIgnoreCase("sprit45 admin")||Util.ROLE.equalsIgnoreCase("timetable admin")||
				Util.ROLE.equalsIgnoreCase("teacher")||Util.ROLE.equalsIgnoreCase("admin") ||	Util.ROLE.equalsIgnoreCase("alumni admin") ||	Util.ROLE.equalsIgnoreCase("office admin"))
		{
			post_cont.setVisibility(View.VISIBLE);
			event_cont.setVisibility(View.VISIBLE);
		}
		else
		{
			post_cont.setVisibility(View.GONE);
			event_cont.setVisibility(View.GONE);
		}

		if(	Util.ROLE.equalsIgnoreCase("office admin") ||Util.ROLE.equalsIgnoreCase("office staff") )
		{
			msg_cont.setVisibility(View.GONE);

		}
		else
		{
			msg_cont.setVisibility(View.VISIBLE);
		}

		///

		if(Util.noti_post_like!=null){
			if(Util.noti_post_like.equalsIgnoreCase("1") )
			{
				like_post.setChecked(true);
			}
			else if (Util.noti_post_like.equalsIgnoreCase("0")) {
				like_post.setChecked(false);
			}
		}
		else
		{
			like_post.setChecked(false);
		}
		if(Util.noti_message!=null)
			if(Util.noti_message.equalsIgnoreCase("1")) 
			{
				msg.setChecked(true);
			}  
			else if (Util.noti_message.equalsIgnoreCase("0")) {
				msg.setChecked(false);
			}
			else
			{
				msg.setChecked(false);
			}
		if(Util.noti_sound!=null)
			if(Util.noti_sound.equalsIgnoreCase("1"))
			{
				noti_sound.setChecked(true);
			}
			else if (Util.noti_sound.equalsIgnoreCase("0")) {
				noti_sound.setChecked(false);
				//noti_sound.toggle();
			}
			else
			{
				noti_sound.setChecked(false);
			}


		if(Util.noti_event!=null){
			if(Util.noti_event.equalsIgnoreCase("1"))
			{
				events.setChecked(true);
			}
			else if (Util.noti_event.equalsIgnoreCase("0")) { 
				events.setChecked(false);
			}
		}
		else
		{
			events.setChecked(false);
		}

		if(Util.noti_vibration!=null){
			if(Util.noti_vibration.equalsIgnoreCase("1"))
			{
				noti_vib.setChecked(true);
			}
			else if (Util.noti_vibration.equalsIgnoreCase("0")) {
				noti_vib.setChecked(false);
			}
		}
		else
		{
			noti_vib.setChecked(false);
		}

		getView().setFocusableInTouchMode(true);
		getView().requestFocus();
		getView().setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if( keyCode == KeyEvent.KEYCODE_BACK ) {

					// Toast.makeText(getActivity(), "back pressed", Toast.LENGTH_SHORT).show();
					replaceFragment();

					return true;
				} else {
					return false;
				}
			}
		});
	} 


	//method to replace the current fragment with settings fragment
	public void replaceFragment(){
		FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
		ft.setCustomAnimations(R.anim.left_in, R.anim.right_out);
		ft.replace(R.id.content_frame, settingsfragment);
		ft.commit();

	}  

	//onclick method for close and ok buttons
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.settings_notify_button_close:
			replaceFragment();
			
			break;
		case R.id.settings_notify_button_ok:  
			if(like_post.isChecked()==true){
				lp="1";
			}
			else
			{
				lp="0";
			}
			if(msg.isChecked()==true){
				msgg="1";
			}
			else
			{
				msgg="0";
			}
			if(noti_sound.isChecked()==true){
				noti_s="1";
			}
			else
			{
				noti_s="0";
			}
			if(noti_vib.isChecked()==true){
				noti_v="1";
			}
			else
			{
				noti_v="0";
			}

			if(events.isChecked()==true){
				evnt="1";
			}
			else
			{
				evnt="0";
			}


			ConnectionDetector conn = new ConnectionDetector(getActivity());
			if(conn.isConnectingToInternet()){
				new UpdateUserNotificationSettings().execute();
			} else{
				alert.showAlertDialog(getActivity(),"Connection Error","Check your Internet Connection",false);
			}

			//replaceFragment();
			break;
		default:
			break;
		}
	}

	private class UpdateUserNotificationSettings extends AsyncTask<Void, Void, Integer>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			// Showing progress dialog before sending http request
			if(pDialog == null){
				pDialog = Util.createProgressDialog(getActivity());
				pDialog.setCancelable(false);
				pDialog.show();}
			else{
				pDialog.setCancelable(false);
				pDialog.show();
			}}

		@Override
		protected Integer doInBackground(Void... params) {
			// TODO Auto-generated method stub
			// JSON object to hold the information, which is sent to the server
			JSONObject jsonObjSend = new JSONObject();

			try {

				jsonObjSend.put("noti_sound",noti_s);
				jsonObjSend.put("noti_vibration",noti_v);
				jsonObjSend.put("noti_for_post_like",lp);
				jsonObjSend.put("noti_for_message",msgg);
				jsonObjSend.put("noti_for_events",evnt);



			} 
			catch (JSONException e) {
				e.printStackTrace();
			}
			String URL = Util.API + "settings/"+Util.USER_ID;
			JSONObject jsonObjRecv = HttpPutClient.sendHttpPost(URL, jsonObjSend);
			return null;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			replaceFragment();

		}


	}

}

