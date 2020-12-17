package com.nxgenminds.eduminds.ju.cms;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpPostClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.SessionManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class ResetPassword extends Activity implements OnFocusChangeListener {
	private EditText mEditNewPassword,mEditConfirmPassword;
	private Button mSavePassword,mCancel;

	private String mNewPassword,mConfirmPassword,mOldPassword,mUserID;
	private static String SETTINGS_CHANGE_PASSWORD_API=Util.API+"change_password";

	private ProgressDialog pDialog;
	private AlertDialogManager alert = new AlertDialogManager();
	private SessionManager session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reset_password);

		Bundle bundle = getIntent().getExtras();
		assert bundle != null;
		mOldPassword = bundle.getString("old_password");
		mUserID = bundle.getString("loginID");


		mEditNewPassword = (EditText)findViewById(R.id.reset_new_password);
		mEditConfirmPassword = (EditText)findViewById(R.id.reset_re_password);
		mSavePassword = (Button)findViewById(R.id.reset_password);
		session = new SessionManager(ResetPassword.this); 

		mEditNewPassword.setOnFocusChangeListener(this);
		mEditConfirmPassword.setOnFocusChangeListener(this);

		mSavePassword.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				mNewPassword     = (mEditNewPassword).getText().toString().trim();
				mConfirmPassword = (mEditConfirmPassword).getText().toString().trim();
				if(mNewPassword.length()>5 && mConfirmPassword.length()>5){
					if(mNewPassword.equalsIgnoreCase(mConfirmPassword))
					{
						ConnectionDetector conn = new ConnectionDetector(ResetPassword.this);
						if(conn.isConnectingToInternet()){
							new ResetPasswordAsync().execute();
						}
					}
					else
					{
						Toast.makeText(ResetPassword.this,"Both entered password are not same !",Toast.LENGTH_LONG).show();

					}
				}else {
					Toast.makeText(ResetPassword.this,"Password should be atleast 6 characters",Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	class ResetPasswordAsync extends AsyncTask<Void,Void,JSONObject>{
		String message = null,errorMessages=null;	
		@Override
		protected void onPreExecute(){
			if(pDialog==null){
				pDialog = Util.createProgressDialog(ResetPassword.this);
				pDialog.show();
			} else { pDialog.show(); }
		}
		@Override
		protected JSONObject doInBackground(Void... params) {

			JSONObject jsonObjSend = new JSONObject();
			JSONObject receivedJsonObject = null;
			try {

				jsonObjSend.put("old_password",mOldPassword);
				jsonObjSend.put("password",mNewPassword);
				jsonObjSend.put("is_first_time", "1");
				receivedJsonObject = HttpPostClient.sendHttpPost(SETTINGS_CHANGE_PASSWORD_API, jsonObjSend);
			}
			catch (JSONException e) { e.printStackTrace();}
			return receivedJsonObject;
		}
		@Override
		protected void onPostExecute(JSONObject result){
			super.onPostExecute(result);
			pDialog.dismiss();
			if(result!=null){
				try{
					message = result.getString("message");
					if(result.has("errorMessages")){
						errorMessages = result.getString("errorMessages");
						Toast.makeText(ResetPassword.this, errorMessages, Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(ResetPassword.this, message, Toast.LENGTH_LONG).show();
					}
					if(result.getString("status").equalsIgnoreCase("1")){
						session.changePassword(mNewPassword);
						// now navigate to NavMainActivity
						Intent i = new Intent(getApplicationContext(),NavMainActivity.class);
						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
						i.putExtra("loginID", mUserID);
						startActivity(i);
						finish();	
					}
				}catch(JSONException e){ e.printStackTrace();}
			}
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {

		int id =v.getId();
		int len = ((EditText) v).getText().toString().trim().length();
		if((!hasFocus) && len<6){
			((EditText) v).setError("Password Should be atleast 6 characters");
		}

	}
}
