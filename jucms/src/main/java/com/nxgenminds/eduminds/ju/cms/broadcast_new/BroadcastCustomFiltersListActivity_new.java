package com.nxgenminds.eduminds.ju.cms.broadcast_new;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class BroadcastCustomFiltersListActivity_new extends ActionBarActivity  {
	public Menu menuInstance;
	private ListView listconn;
	private ArrayList<BroadcastCustomFiltersModel_new> connectionssearch = new ArrayList<BroadcastCustomFiltersModel_new>();
	private EditText conn_search;
	StringBuffer response_uname,response_uID;
	String custom_users_name=" ";
	String custom_users_id=" ";
	BroadcastCustomFiltersAdapter_new conn_adapter;
	private ProgressDialog pDialog;
	AlertDialogManager alert = new AlertDialogManager();
	ArrayList <String> checkedUserName=new ArrayList<String>();
	ArrayList <String> checkedUserID=new ArrayList<String>();
	String received_userID;
	String trimeduid="";
	public static ArrayList<String> myList ;
	private static String pagination_Date_String = "";
	private boolean flag_loading = false;
	public static boolean searchPaginationFlag=false;
	private static int pageCount = 0;
	TextView msg;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.new_broadcast_custom_list);
		// get the intent
		Bundle bundle = getIntent().getExtras();
		received_userID = bundle.getString("usercheckedid");
		
		if(received_userID.equalsIgnoreCase(" "))
		{
			
			myList = new ArrayList<String>(Arrays.asList(received_userID.split(",")));
		}
		else
		{
			try{
			trimeduid=removeLastChar(received_userID);
			myList = new ArrayList<String>(Arrays.asList(trimeduid.split(",")));
			}catch(ArrayIndexOutOfBoundsException e){
				Toast.makeText(this, "please select proper data", Toast.LENGTH_SHORT).show();
			}
		}


		for(int id=0;id<myList.size();id++)
		{
			 
		}


		//end
		listconn = (ListView)findViewById(R.id.broad_list_connections);

		msg=(TextView)findViewById(R.id.broad_NoMembers);
        
		ConnectionDetector conn = new ConnectionDetector(BroadcastCustomFiltersListActivity_new.this);
        if(conn.isConnectingToInternet()){
        	new ConnectionsAsyncClass().execute();
        } else{
        	alert.showAlertDialog(BroadcastCustomFiltersListActivity_new.this,"Connection Error","Check your Internet Connection",false);
        }
       }



	private static String removeLastChar(String str) {
		//return str.substring(0,str.length()-1);
		return str;
	}
	@Override
	public boolean onCreateOptionsMenu (Menu menu) {
		menuInstance = menu;
		getMenuInflater().inflate(R.menu.menu_select_broadcast_filters, menu);
		return super.onCreateOptionsMenu(menu);

	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.broad_members_add: 
			listconn.setEnabled(false);
			if(conn_adapter!=null){
				int size = conn_adapter.getCount();
				

				if(size>0)
				{
					
					for(int i=0;i<size;i++){
						BroadcastCustomFiltersModel_new rowItem = (BroadcastCustomFiltersModel_new) conn_adapter.getItem(i);
						if(rowItem.isSelected()){

							checkedUserName.add(rowItem.getName().trim());
							checkedUserID.add(rowItem.getValue().trim());
						}
					}
					custom_users_name=checkedUserName.toString();
					custom_users_id=checkedUserID.toString();

					Intent intent = new Intent();
					intent.putExtra("CustomUserName", custom_users_name);
					intent.putExtra("CustomUserID", custom_users_id);
					
					setResult(RESULT_OK,intent);
					listconn.setEnabled(true);
					finish();
				}
				else
				{
					Toast.makeText(getApplicationContext(), "No Connections Found", Toast.LENGTH_SHORT).show();
				}
			}
			else
			{
				Toast.makeText(getApplicationContext(), "No Connections Found", Toast.LENGTH_SHORT).show();
			}
			return true;


		case R.id.broad_members_exit: 
			finish();
			return true;

		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
	}

	private class ConnectionsAsyncClass extends AsyncTask<Void, Void, ArrayList<BroadcastCustomFiltersModel_new>>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			// Showing progress dialog before sending http request
			pDialog = new ProgressDialog(BroadcastCustomFiltersListActivity_new.this);
			pDialog.setMessage("Loading ...");
			pDialog.setIndeterminate(true);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected ArrayList<BroadcastCustomFiltersModel_new> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONArray connectionsResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(Util.API+"broadcast_custom_filters");
			if(jsonObjectRecived != null){
				connectionssearch= new ArrayList<BroadcastCustomFiltersModel_new>();
				try{
					if(jsonObjectRecived.getString("error").equalsIgnoreCase("false")){
						connectionsResponse = jsonObjectRecived.getJSONArray("custom_filters");
						for(int i = 0; i< connectionsResponse.length();i++){
							BroadcastCustomFiltersModel_new broadcastData = new BroadcastCustomFiltersModel_new();
							JSONObject broadcastDetails;
							broadcastDetails = connectionsResponse.getJSONObject(i);
							broadcastData.setName(broadcastDetails.getString("name"));
							broadcastData.setValue(broadcastDetails.getString("value"));
							connectionssearch.add(broadcastData);
						}
					}else {
						return null;
					}
				} catch(JSONException e){

				}

			}else{
				return null;
			}

			return connectionssearch;
		}


		@Override
		protected void onPostExecute(ArrayList<BroadcastCustomFiltersModel_new> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if(result == null){
			}else if(result.size()==0){
				
			}else{
				conn_adapter = new BroadcastCustomFiltersAdapter_new(BroadcastCustomFiltersListActivity_new.this, result, result);
				listconn.setAdapter(conn_adapter);

			}

		}

	}


}
