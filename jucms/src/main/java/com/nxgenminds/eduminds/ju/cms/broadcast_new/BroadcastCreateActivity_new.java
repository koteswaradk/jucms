package com.nxgenminds.eduminds.ju.cms.broadcast_new;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpPostClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class BroadcastCreateActivity_new extends ActionBarActivity  implements OnCheckedChangeListener {
	private EditText compose;
	private TextView addContacts;
	private Button submit;
	public static final int result = 111;
	private static String custom_name=" ";
	private static String custom_id=" ";
	private static String members_id=" ";
	private static String compose_text=" ";
	private static String visibility=" ";
	private Fragment broadcastList;
	private String createBroadcastURL = Util.API + "send_broadcast";
	private RadioGroup radioGroup_privacy;
	private RadioButton radioButton_custom,radioButton_public;
	public LinearLayout contactContainer;
	private static Integer flag;
	private String refresh="false";
	private ProgressDialog pDialog;

	private AlertDialogManager alert = new AlertDialogManager();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_create_broadcast_compose);
		ActionBar actionBar = getSupportActionBar();
		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayUseLogoEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

		addContacts = (TextView) findViewById(R.id.broad_contactsDisplay);
		compose = (EditText) findViewById(R.id.broad_composeMessage);
		submit = (Button) findViewById(R.id.broad_sendMessage);
		radioGroup_privacy = (RadioGroup)findViewById(R.id.broad_radiogroup_privacy);
		radioButton_custom = (RadioButton)findViewById(R.id.broad_radio_custom);
		radioButton_public  = (RadioButton)findViewById(R.id.broad_radio_public);
		contactContainer =(LinearLayout)findViewById(R.id.broad_contactContain);
		contactContainer.setVisibility(View.GONE);
		addContacts.setText("");
		compose.setText("");
		radioGroup_privacy.setOnCheckedChangeListener(this);


		addContacts.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(BroadcastCreateActivity_new.this,BroadcastCustomFiltersListActivity_new.class);
				i.putExtra("usercheckedid",custom_name);
				startActivityForResult(i,result);
			}
		});


		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(compose.getText().toString().trim().length()>0 )
				{
					if(addContacts.getText().length()>0)
					{
						if(radioButton_custom.isChecked() || radioButton_public.isChecked())
						{
							//refresh="true";
							ConnectionDetector conn = new ConnectionDetector(BroadcastCreateActivity_new.this);
							if(conn.isConnectingToInternet()){
								new CreateBroadcastAsyncClass().execute();
							}else{
								alert.showAlertDialog(BroadcastCreateActivity_new.this,"Connection Error","Check your Internet Connection",false);
							}


						}
						else
						{
							Toast.makeText(getApplicationContext(), "Select the type of Broadcast", Toast.LENGTH_SHORT).show();
						}
					}
					else
					{
						Toast.makeText(getApplicationContext(), "Select the contacts", Toast.LENGTH_SHORT).show();

					}
				}
				else{
					Toast.makeText(getApplicationContext(), "Please write a message", Toast.LENGTH_SHORT).show();

				}
			}
		});

	}

	@SuppressWarnings("deprecation")
	@Override
	public void onBackPressed() {
		if(compose.getText().toString().trim().length()>0 )
		{

			AlertDialog alert_back = new AlertDialog.Builder(this).create();
			alert_back.setTitle("Exit?");
			alert_back.setMessage("Your broadcast will be discarded");

			alert_back.setButton("Cancel", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});

			alert_back.setButton2("Yes", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					addContacts.setText("");
					compose.setText(" ");
					compose.setHint("Write Your Message");
					radioGroup_privacy.clearCheck();
					radioButton_custom.setEnabled(true);
					radioButton_public.setEnabled(true);
					contactContainer.setVisibility(View.GONE);
					custom_name=" ";
					custom_id=" ";
					members_id=" ";
					Intent intent = new Intent();
					refresh="false";
					intent.putExtra("Refresh",refresh);

					setResult(RESULT_OK,intent);
					BroadcastCreateActivity_new.this.finish();

				}
			});
			alert_back.show();


		}
		else
		{
			addContacts.setText("");
			compose.setText(" ");
			compose.setHint("Write Your Message");
			radioGroup_privacy.clearCheck();
			radioButton_custom.setEnabled(true);
			radioButton_public.setEnabled(true);
			contactContainer.setVisibility(View.GONE);
			custom_name=" ";
			custom_id=" ";
			members_id=" ";
			BroadcastCreateActivity_new.this.finish();
		}

	}
	private static String removeLastChar(String str) {
		return str.substring(1,str.length()-1);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode != RESULT_OK) {

			return;
		}

		Bitmap bitmap;

		switch (requestCode) {

		case result:
			addContacts.setText("");
			custom_id=" ";

			custom_name=data.getStringExtra("CustomUserName");
			custom_id=data.getStringExtra("CustomUserID");
			//remove first and last character
			addContacts.setText(removeLastChar(custom_id).replaceAll("\\s+",""));
			//members_id=removeLastChar(custom_name).replaceAll("\\s+","");
			members_id=removeLastChar(custom_name);


			break;
		}
		super.onActivityResult(requestCode, resultCode, data); 
	}
	class CreateBroadcastAsyncClass extends AsyncTask<Void, Void, JSONObject>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			// Showing progress dialog before sending http request
			pDialog = new ProgressDialog(BroadcastCreateActivity_new.this);
			pDialog.setMessage("Sending ...");
			pDialog.setIndeterminate(true);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected JSONObject doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONArray connectionsResponse = null;
			JSONObject CreatBroadcastJsonObject = new JSONObject();

			try {
				CreatBroadcastJsonObject.put("broadcast_msg_txt",compose.getText());
				if(visibility.equalsIgnoreCase("1")){
					CreatBroadcastJsonObject.put("is_public",visibility); 
				}
				else
				{
					CreatBroadcastJsonObject.put("is_public",visibility); 
					CreatBroadcastJsonObject.put("filter_csv",members_id);
					System.out.println("members_id"+members_id);

				}

			} catch (JSONException e) {
				e.printStackTrace();
			}


			JSONObject jsonObjectReceived = HttpPostClient.sendHttpPost(createBroadcastURL,CreatBroadcastJsonObject);

			System.out.println("jsonObjectReceived----"+jsonObjectReceived);
			return jsonObjectReceived;
		}
		@Override
		protected void onPostExecute(JSONObject result)
		{
			super.onPostExecute(result);
			pDialog.dismiss();

			if(result!=null)
			{
				try {
					if(result.getString("status").equalsIgnoreCase("1"))
					{
						Toast.makeText(BroadcastCreateActivity_new.this,result.getString("message"), Toast.LENGTH_SHORT).show();
						refresh="true";
						addContacts.setText("");
						compose.setText(" ");
						compose.setHint("Write Your Message");
						radioGroup_privacy.clearCheck();
						radioButton_custom.setEnabled(true);
						radioButton_public.setEnabled(true);
						contactContainer.setVisibility(View.GONE);
						custom_name=" ";
						custom_id=" ";
						Intent return_back = new Intent();
						return_back.putExtra("Refresh", refresh);

						setResult(RESULT_OK,return_back);
						finish();


					}
					else
					{
						Toast.makeText(BroadcastCreateActivity_new.this, "Something went wrong !", Toast.LENGTH_SHORT).show();

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			else
			{
				Toast.makeText(BroadcastCreateActivity_new.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch(checkedId)
		{
		case R.id.broad_radio_public:{
			contactContainer.setVisibility(View.GONE);
			visibility="1";
			addContacts.setText("JU CMS");
			custom_name="";
			custom_id=" ";

		}
		break;

		case R.id.broad_radio_custom:{
			contactContainer.setVisibility(View.VISIBLE);
			visibility="0";
			addContacts.setText("");
			addContacts.setHint("Add Contacts");
		}
		break;
		}
	}


}
