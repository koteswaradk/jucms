package com.nxgenminds.eduminds.ju.cms.settings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.models.SettingsModel;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class SettingsFragment extends Fragment implements OnClickListener {

	Button btn_notification,btn_changepassword;
	FrameLayout container;
	FragmentManager fm;
	FragmentTransaction ft;
	Fragment settings_notificationfragment;
	Fragment settings_passwordfragment;
	private ProgressDialog pDialog;
	private SettingsModel settingsinfoData;
	
	private AlertDialogManager alert = new AlertDialogManager();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.settings, container, false);
		return rootView;
	}
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		btn_notification = (Button)getView().findViewById(R.id.settings_button_notifications);
		btn_changepassword = (Button)getView().findViewById(R.id.settings_button_changePassword);
		container = (FrameLayout)getView().findViewById(R.id.content_frame);
		settings_notificationfragment = new SettingsNotificationsFragment();
		settings_passwordfragment = new SettingsPasswordFragment();

		btn_notification.setOnClickListener(this);
		btn_changepassword.setOnClickListener(this);
		
		
		ConnectionDetector conn = new ConnectionDetector(getActivity());
        if(conn.isConnectingToInternet()){
        	new SettingsInfoFragmentAsync().execute();
        } else{
        	alert.showAlertDialog(getActivity(),"Connection Error","Check your Internet Connection",false);
        }

		
	}
	private class SettingsInfoFragmentAsync extends AsyncTask<Void, Void, SettingsModel>{

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
		protected SettingsModel doInBackground(Void... params) {
			// TODO Auto-generated method stub

			JSONArray SettingsInfoResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(Util.API+"settings/"+Util.USER_ID);
			if(jsonObjectRecived != null){

				try{
					SettingsInfoResponse = jsonObjectRecived.getJSONArray("userSettings");
				}catch(JSONException e){
					e.printStackTrace();
				}
				settingsinfoData = new SettingsModel();
				JSONObject settings_info_Details;
				try{
					if(SettingsInfoResponse.length()>0){
						settings_info_Details = SettingsInfoResponse.getJSONObject(0);
						settingsinfoData.setSetting_id(settings_info_Details.getString("setting_id"));
						settingsinfoData.setFor_user(settings_info_Details.getString("for_user"));
						settingsinfoData.setNoti_sound(settings_info_Details.getString("noti_sound"));
						settingsinfoData.setNoti_viberation(settings_info_Details.getString("noti_vibration"));
						settingsinfoData.setNoti_for_post_like(settings_info_Details.getString("noti_for_post_like"));
						settingsinfoData.setNoti_for_message(settings_info_Details.getString("noti_for_message"));
						settingsinfoData.setNoti_for_events(settings_info_Details.getString("noti_for_events"));
						settingsinfoData.setFont_size(settings_info_Details.getString("font_size"));
						settingsinfoData.setLanguage(settings_info_Details.getString("language"));
						settingsinfoData.setCreated_by(settings_info_Details.getString("created_by"));		
						return settingsinfoData;
					}
					else{
						return null;/////if array is null eg:- usersetting[0] in json
					}

				}catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				return null;
			}
			return null;
		}

		@Override
		protected void onPostExecute(SettingsModel app) {
			// TODO Auto-generated method stub
			super.onPostExecute(app);
			pDialog.dismiss();
			if(app == null){

			}else{
				//set the ui for settings
				Util.noti_sound=app.getNoti_sound();
				Util.noti_vibration=app.getNoti_viberation();
				Util.noti_post_like=app.getNoti_for_post_like();
				Util.noti_message=app.getNoti_for_message();
				Util.noti_event=app.getNoti_for_events();
				Util.font_size=app.getFont_size();
				Util.language=app.getLanguage();
				
			}

		}

	}
	//onclick events for button in settings to replace the fragments
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		fm=getActivity().getSupportFragmentManager();
		ft= fm.beginTransaction();

		int id;
		id = v.getId();

		switch(id)
		{
		case R.id.settings_button_notifications:		
			ft.setCustomAnimations(R.anim.right_in, R.anim.left_out);
			ft.replace(R.id.content_frame,settings_notificationfragment);
			ft.commit();

			break;

		case R.id.settings_button_changePassword:		
			ft.setCustomAnimations(R.anim.right_in, R.anim.left_out);
			ft.replace(R.id.content_frame,settings_passwordfragment);
			ft.commit();
			break;

		default:
			break;


		}


	}

}