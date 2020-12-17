package com.nxgenminds.eduminds.ju.cms.settings;


import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpPostClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.SessionManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class SettingsPasswordFragment extends Fragment implements OnClickListener{
	private Button button_save,button_cancel;
	private EditText edit_currentPassword,edit_newPassword,edit_confirmPassword;
	private Fragment settingsfragment;
	private FrameLayout container;
	private ProgressDialog pDialog;
	String old_passwd,new_passwd,cnfrm_passwd,passwd;
	private SessionManager session;
	private AlertDialogManager alert = new AlertDialogManager();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.settings_password, container, false);
		return rootView;
	}
	
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		edit_currentPassword = (EditText)getView().findViewById(R.id.settings_pwd_edit_current);
		edit_newPassword =(EditText)getView().findViewById(R.id.settings_pwd_edit_new);
		edit_confirmPassword = (EditText)getView().findViewById(R.id.settings_pwd_edit_confirm);
		button_save = (Button) getView().findViewById(R.id.settings_pwd_button_save);
		button_cancel = (Button) getView().findViewById(R.id.settings_pwd_button_cancel);
		container = (FrameLayout)getView().findViewById(R.id.content_frame);
		session = new SessionManager(getActivity()); 

		settingsfragment= new SettingsFragment();

		button_save.setOnClickListener(this);
		button_cancel.setOnClickListener(this);
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
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int view_id;
		view_id = v.getId();

		switch(view_id)
		{
		case R.id.settings_pwd_button_save:
			//replaceFragment();
			//check for new and cnfrm passwd

			//end
			old_passwd=edit_currentPassword.getText().toString();
			new_passwd=edit_newPassword.getText().toString();
			cnfrm_passwd=edit_confirmPassword.getText().toString();
			if(old_passwd.length()>=6 && new_passwd.length()>=6 && cnfrm_passwd.length()>=6){

				if(!old_passwd.equalsIgnoreCase(new_passwd))
				{
					if(cnfrm_passwd.equalsIgnoreCase(new_passwd)){
						ConnectionDetector conn = new ConnectionDetector(getActivity());
						if(conn.isConnectingToInternet())	{
							new PasswordChangeAsync().execute();
						}
						else{
							alert.showAlertDialog(getActivity(), "Connection Error", "Please check your internet connection", false);
						}
					}
					else
					{
						Toast.makeText(getActivity(), "password mismatch", Toast.LENGTH_SHORT).show();
					}
				}
				else
				{
					Toast.makeText(getActivity(), "Your new password is same as your old password", Toast.LENGTH_SHORT).show();
				}
			}
			else
			{
				Toast.makeText(getActivity(), "Incomplete Information", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.settings_pwd_button_cancel:
			replaceFragment();
			break;
		}



	}
	private class PasswordChangeAsync extends AsyncTask<Void, Void, String>{

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
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			// JSON object to hold the information, which is sent to the server
			JSONObject jsonObjRecv;
			String sucess_msg = null;
			JSONObject jsonObjSend = new JSONObject();

			try {

				jsonObjSend.put("old_password",old_passwd);
				jsonObjSend.put("password",new_passwd);
				
			} 
			catch (JSONException e) {
				e.printStackTrace();
			}

			jsonObjRecv = HttpPostClient.sendHttpPost(Util.API + "change_password", jsonObjSend);
			try
			{
				sucess_msg=jsonObjRecv.getString("message");
			}
			catch(JSONException e)
			{

			}
			return sucess_msg;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if(result.equalsIgnoreCase("Old password is incorrect. Please enter the correct password."))
			{
				Toast.makeText(getActivity(), "Old password is incorrect. Please enter the correct password.", Toast.LENGTH_SHORT).show();
			}else
			{
				Toast.makeText(getActivity(), "Password Updated", Toast.LENGTH_SHORT).show();
				session.changePassword(new_passwd);
				replaceFragment();
			}

		}


	}
	public void replaceFragment()
	{
		FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
		ft.setCustomAnimations(R.anim.left_in, R.anim.right_out);
		ft.replace(R.id.content_frame, settingsfragment);
		ft.commit();
		InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

	}


}

