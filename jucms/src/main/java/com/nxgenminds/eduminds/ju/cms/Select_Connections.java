package com.nxgenminds.eduminds.ju.cms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nxgenminds.eduminds.ju.cms.adapters.ConnectionAdapter_Select_Connection;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.models.ConnectionsModel;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class Select_Connections extends ActionBarActivity  {
	public Menu menuInstance;
	private ListView listconn;
	private ArrayList<ConnectionsModel> connectionssearch = new ArrayList<ConnectionsModel>();
	private EditText conn_search;
	StringBuffer response_uname,response_uID;
	String custom_users_name=" ";
	String custom_users_id=" ";
	ConnectionAdapter_Select_Connection conn_adapter;
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
	//TextView msg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.create_broadcast_add_members);
		// get the intent
		Bundle bundle = getIntent().getExtras();
		received_userID = bundle.getString("usercheckedid");
		
		if(received_userID.equalsIgnoreCase(" "))
		{
			
			myList = new ArrayList<String>(Arrays.asList(received_userID.split(",")));
		}
		else
		{
			
			trimeduid=removeLastChar(received_userID);
			myList = new ArrayList<String>(Arrays.asList(trimeduid.split(",")));
		}


		for(int id=0;id<myList.size();id++)
		{
			 
		}


		//end
		listconn = (ListView)findViewById(R.id.list_connections);
		conn_search=(EditText)findViewById(R.id.connection_search);
		//msg=(TextView)findViewById(R.id.noconn);
		connectionssearch.clear();
		
		ConnectionDetector conn = new ConnectionDetector(Select_Connections.this);
        if(conn.isConnectingToInternet()){
        	new ConnectionsAsyncClass().execute();
		} else{
        	alert.showAlertDialog(Select_Connections.this,"Connection Error","Check your Internet Connection",false);
        }

		

		

		conn_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					searchPaginationFlag=true;
					pageCount = 0;
					connectionssearch.clear();
					//conn_adapter.notifyDataSetInvalidated();
					if(conn_adapter != null){
						ConnectionDetector conn = new ConnectionDetector(Select_Connections.this);
						if(conn.isConnectingToInternet()){
							conn_adapter.notifyDataSetInvalidated();
							new ConnectionsSearchAsyncClass().execute();
						}else{
							alert.showAlertDialog(Select_Connections.this, "Connection Error", "Please check your internet connection", false);
						}
					}else{
						//Toast.makeText(getApplicationContext(), "Connections Not Found", Toast.LENGTH_SHORT).show();
						//msg.setVisibility(View.VISIBLE);
					}
					return true;
				}
				return false;
			}


		});

		// Capture Text in EditText
		conn_search.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {  
				// TODO Auto-generated method stub
				if(conn_adapter != null){
					//msg.setVisibility(View.GONE);
					String text = conn_search.getText().toString().toLowerCase(Locale.getDefault());
					conn_adapter.filter(text);
				}else{
					Toast.makeText(getApplicationContext(), "Connections Not Found", Toast.LENGTH_SHORT).show();
					//msg.setVisibility(View.VISIBLE);
				}

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
			}
		});

		//pagination for connection search

		listconn.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0)
				{
					if(flag_loading == false)
					{
						flag_loading = true;
						ConnectionDetector conn = new ConnectionDetector(Select_Connections.this);
						if(conn.isConnectingToInternet()){
							new ConnectionLoadMoreAsyncClass().execute();
						}else{
							alert.showAlertDialog(Select_Connections.this, "Connection Error", "Please check your internet connection", false);
						}
					}
				}
			}
		});
	}
	
	
	
	private static String removeLastChar(String str) {
		return str.substring(1,str.length()-1);
	}
	@Override
	public boolean onCreateOptionsMenu (Menu menu) {
		menuInstance = menu;
		getMenuInflater().inflate(R.menu.menu_select_contact, menu);
		return super.onCreateOptionsMenu(menu);

	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.members_add: 
			listconn.setEnabled(false);
			if(conn_adapter!=null){
			int size = conn_adapter.getCount();
			

			if(size>0)
			{
				
				for(int i=0;i<size;i++){
					ConnectionsModel rowItem = (ConnectionsModel) conn_adapter.getItem(i);
					if(rowItem.isSelected()){

						checkedUserName.add(rowItem.getFirstname().trim());
						checkedUserID.add(rowItem.getUser_id().trim());
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


		case R.id.members_exit: 
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

	private class ConnectionsAsyncClass extends AsyncTask<Void, Void, ArrayList<ConnectionsModel>>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			// Showing progress dialog before sending http request
			pDialog = new ProgressDialog(Select_Connections.this);
			pDialog.setMessage("Loading ...");
			pDialog.setIndeterminate(true);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected ArrayList<ConnectionsModel> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONArray connectionsResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(Util.ConnectionAPI+Util.USER_ID);
			if(jsonObjectRecived != null){

				try{
					connectionsResponse = jsonObjectRecived.getJSONArray("connections");
				}catch(JSONException e){
					e.printStackTrace();
				}
				for(int i = 0; i< connectionsResponse.length();i++){
					ConnectionsModel connectionData = new ConnectionsModel();
					JSONObject connectionsDetails;
					try{
						connectionsDetails = connectionsResponse.getJSONObject(i);
						connectionData.setUser_id(connectionsDetails.getString("user_id"));
						connectionData.setFirstname(connectionsDetails.getString("firstname"));
						connectionData.setLastname(connectionsDetails.getString("lastname"));
						connectionData.setMiddlename(connectionsDetails.getString("middlename"));
						connectionData.setUsername(connectionsDetails.getString("username"));
						connectionData.setEmail(connectionsDetails.getString("email"));
						connectionData.setMobile(connectionsDetails.getString("mobile"));
						connectionData.setDob(connectionsDetails.getString("dob"));
						connectionData.setHome_city_name(connectionsDetails.getString("home_city_name"));
						connectionData.setCurr_city_name(connectionsDetails.getString("curr_city_name"));
						connectionData.setGendername(connectionsDetails.getString("gendername"));
						connectionData.setUser_profile_photo(connectionsDetails.getString("user_profile_photo"));
						//connectionData.setUser_cover_photo(connectionsDetails.getString("user_cover_photo"));
						connectionData.setMessaging_status(connectionsDetails.getString("messaging_status"));
						connectionData.setFriend(connectionsDetails.getString("friend"));
						connectionData.setCreated_date(connectionsDetails.getString("created_date"));
						pagination_Date_String =connectionsDetails.getString("created_date");
						connectionssearch.add(connectionData);
					}catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return connectionssearch;
			}else{
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<ConnectionsModel> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if(result == null){
			}else if(result.size()==0){
				
			}else{
				conn_adapter = new ConnectionAdapter_Select_Connection(Select_Connections.this, result);
				
				listconn.setAdapter(conn_adapter);
				if(result.size() < 40){
					flag_loading = true;
				}else{
					flag_loading = false;
				}
			}

		}

	}
	private class ConnectionsSearchAsyncClass extends AsyncTask<Void, Void, ArrayList<ConnectionsModel>>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			// Showing progress dialog before sending http request
			pDialog = new ProgressDialog(Select_Connections.this);
			pDialog.setMessage("Loading ...");
			pDialog.setIndeterminate(true);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected ArrayList<ConnectionsModel> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONArray connectionsResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(Util.ConnectionAPI+Util.USER_ID+"&search_text="+conn_search.getText().toString().toLowerCase(Locale.getDefault()) );
			if(jsonObjectRecived != null){

				try{
					connectionsResponse = jsonObjectRecived.getJSONArray("connections");
				}catch(JSONException e){
					e.printStackTrace();
				}
				for(int i = 0; i< connectionsResponse.length();i++){
					ConnectionsModel connectionData = new ConnectionsModel();
					JSONObject connectionsDetails;
					try{
						connectionsDetails = connectionsResponse.getJSONObject(i);
						connectionData.setUser_id(connectionsDetails.getString("user_id"));
						connectionData.setFirstname(connectionsDetails.getString("firstname"));
						connectionData.setLastname(connectionsDetails.getString("lastname"));
						connectionData.setMiddlename(connectionsDetails.getString("middlename"));
						connectionData.setUsername(connectionsDetails.getString("username"));
						connectionData.setEmail(connectionsDetails.getString("email"));
						connectionData.setMobile(connectionsDetails.getString("mobile"));
						connectionData.setDob(connectionsDetails.getString("dob"));
						connectionData.setHome_city_name(connectionsDetails.getString("home_city_name"));
						connectionData.setCurr_city_name(connectionsDetails.getString("curr_city_name"));
						connectionData.setGendername(connectionsDetails.getString("gendername"));
						connectionData.setUser_profile_photo(connectionsDetails.getString("user_profile_photo"));
						//connectionData.setUser_cover_photo(connectionsDetails.getString("user_cover_photo"));
						connectionData.setMessaging_status(connectionsDetails.getString("messaging_status"));
						connectionData.setFriend(connectionsDetails.getString("friend"));
						connectionData.setCreated_date(connectionsDetails.getString("created_date"));
						pagination_Date_String =connectionsDetails.getString("created_date");
						connectionssearch.add(connectionData);
					}catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return connectionssearch;
			}else{
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<ConnectionsModel> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if(result == null){
			}else if(result.size()==0){
				   
			}else{
				conn_adapter.notifyDataSetInvalidated();
				conn_adapter = new ConnectionAdapter_Select_Connection(Select_Connections.this, result);
				listconn.setAdapter(conn_adapter);
				if(result.size() < 40){
					flag_loading = true;
				}else{
					flag_loading = false;
				}
			}

		}




	}
	//load more for connection in messages
	private class ConnectionLoadMoreAsyncClass extends AsyncTask<Void, Void, ArrayList<ConnectionsModel>>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			// Showing progress dialog before sending http request
			if(pDialog == null){
				pDialog = Util.createProgressDialog(Select_Connections.this);
				pDialog.setCancelable(false);
				pDialog.show();}
			else{
				pDialog.setCancelable(false);
				pDialog.show();
			}}

		@Override
		protected ArrayList<ConnectionsModel> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONArray connectionsResponse = null;
			JSONObject jsonObjectRecived=null;
			//	JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(Util.ConnectionAPI+Util.USER_ID +"&last_date=" + pagination_Date_String.replaceAll(" ", "%20"));
			if(searchPaginationFlag==false)
			{
				jsonObjectRecived = HttpGetClient.sendHttpPost(Util.ConnectionAPI+Util.USER_ID +"&last_date=" + pagination_Date_String.replaceAll(" ", "%20"));
			}
			else
			{
				// url for the pagination in search
				jsonObjectRecived = HttpGetClient.sendHttpPost(Util.ConnectionAPI+Util.USER_ID +"?search_text="+conn_search.getText().toString().toLowerCase(Locale.getDefault()) + "&last_date=" + pagination_Date_String.replaceAll(" ", "%20"));
			}
			if(jsonObjectRecived != null){

				try{
					connectionsResponse = jsonObjectRecived.getJSONArray("connections");
				}catch(JSONException e){
					e.printStackTrace();
				}
				if(connectionsResponse.length() > 0){
					for(int i = 0; i< connectionsResponse.length();i++){
						ConnectionsModel connectionData = new ConnectionsModel();
						JSONObject connectionsDetails;
						try{
							connectionsDetails = connectionsResponse.getJSONObject(i);
							connectionData.setUser_id(connectionsDetails.getString("user_id"));
							connectionData.setFirstname(connectionsDetails.getString("firstname"));
							connectionData.setCreated_date(connectionsDetails.getString("created_date"));
							connectionData.setUser_profile_photo_thumb1(connectionsDetails.getString("user_profile_photo_thumb1"));
							connectionData.setJid(connectionsDetails.getString("jid"));
							pagination_Date_String = connectionsDetails.getString("created_date");
							connectionssearch.add(connectionData);
						}catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					return connectionssearch;
				}else{
					return null;
				}
			}else{
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<ConnectionsModel> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if(result == null){
				if(conn_adapter.isEmpty()){
					//NoMembers.setVisibility(View.VISIBLE);
					flag_loading = false;
				}
				flag_loading = true;
				Toast.makeText(Select_Connections.this, "No More Connections to Load", Toast.LENGTH_SHORT).show();
			}else if(result.size()==0){
				if(conn_adapter.isEmpty()){
					//NoMembers.setVisibility(View.VISIBLE);
				}
				flag_loading = true;
				Toast.makeText(Select_Connections.this, "No More Connections to Load", Toast.LENGTH_SHORT).show();
			    } else {
				conn_adapter = new ConnectionAdapter_Select_Connection(Select_Connections.this, result);
				conn_adapter.notifyDataSetChanged();
				flag_loading = false;
				pageCount = pageCount +40;
				listconn.setSelection(pageCount);
			}

		}


	}

}
