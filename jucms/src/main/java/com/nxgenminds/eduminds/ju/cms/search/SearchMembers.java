package com.nxgenminds.eduminds.ju.cms.search;


import java.util.ArrayList;
import android.util.Log;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.adapters.MemberSearchAdapter;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.models.MembersModel;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.thirdpartyprofile.ThirdPartyTabMenuActivity;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


import android.view.View.OnFocusChangeListener;

public class SearchMembers extends Activity {

	String MyActivity="SearchMembers";
	public Menu menuInstance;
	private ListView listconn;
	private ArrayList<MembersModel> connectionssearch = new ArrayList<MembersModel>();
	private EditText conn_search;
	StringBuffer response_uname,response_uID;
	String custom_users_name=" ";
	String custom_users_id=" ";
	MemberSearchAdapter conn_adapter;
	private ProgressDialog pDialog;
	AlertDialogManager alert = new AlertDialogManager();
	private String MembersURL = Util.API+ "user";
	private static int pageCount = 0;
	private static String pagination_Date_String = "";
	private boolean flag_loading = false;
	private boolean flag_refresh=false;
	public static final int refresh = 000;
	public static final int no_refresh = 222;
	public static boolean searchPaginationFlag=false;
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        
		setContentView(R.layout.create_broadcast_add_members);
		listconn = (ListView)findViewById(R.id.list_connections);
		Log.d(MyActivity, "Before conn_search");
		conn_search=(EditText)findViewById(R.id.connection_search);
		Log.d(MyActivity, "After conn_search");
		connectionssearch.clear();
		Log.d(MyActivity, "After clear");
		
		ConnectionDetector conn = new ConnectionDetector(SearchMembers.this);
        if(conn.isConnectingToInternet()){
        	new MembersAsyncClass().execute();
        } else{
        	alert.showAlertDialog(SearchMembers.this,"Connection Error","Check your Internet Connection",false);
        }

		
		conn_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					//flag for pagination in search
					searchPaginationFlag=true;
					connectionssearch.clear();
					pageCount = 0;
					conn_adapter.notifyDataSetInvalidated();
					if(conn_adapter != null){
						ConnectionDetector conn = new ConnectionDetector(SearchMembers.this);
						if(conn.isConnectingToInternet()){

							if(conn_search.getText().toString().trim().length()>0){
								new MemberSearchAsyncClass().execute();
							}
							else
							{
								Toast.makeText(getApplicationContext(), "Add text to search contacts", Toast.LENGTH_SHORT).show();
							}
						}else{
							alert.showAlertDialog(SearchMembers.this, "Connection Error", "Please check your internet connection", false);
						}
					}else{
						Toast.makeText(getApplicationContext(), "Connections Not Found", Toast.LENGTH_SHORT).show();
					}
					return true;
				}


				return false;
			}


		});
		

		/*conn_search.setOnFocusChangeListener(new OnFocusChangeListener(){
			  public void onFocus(){
				  conn_search.setHint("");
			  }

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				 conn_search.setHint("");
				 Log.v("inside on focus", " conn_search.setHint");
			}
			});*/
		// Capture Text in EditText
		conn_search.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {  
				// TODO Auto-generated method stub
				if(conn_adapter != null){
					String text = conn_search.getText().toString().toLowerCase(Locale.getDefault());
					conn_adapter.filter(text);
				}else{
					Toast.makeText(getApplicationContext(), "Connections Not Found", Toast.LENGTH_SHORT).show();
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

		//pagination for member search

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
						ConnectionDetector conn = new ConnectionDetector(SearchMembers.this);
						if(conn.isConnectingToInternet()){
							new MembersLoadMoreAsyncClass().execute();
						}else{
							alert.showAlertDialog(SearchMembers.this, "Connection Error", "Please check your internet connection", false);
						}
					}
				}
			}
		});

		listconn.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				flag_refresh=true;
				Object object = listconn.getItemAtPosition(position);
				MembersModel members_data = (MembersModel)  object;
				Toast.makeText(getApplicationContext(), members_data.getUser_id(), Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(SearchMembers.this,ThirdPartyTabMenuActivity.class);
				intent.putExtra("UserID", members_data.getUser_id());
				Util.THIRD_PARTY_NAME  = members_data.getFirstname();
				startActivity(intent);

			}
		});

	}



	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
	}



	private class MembersAsyncClass extends AsyncTask<Void, Void, ArrayList<MembersModel>>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			// Showing progress dialog before sending http request
			if(pDialog == null){
				pDialog = Util.createProgressDialog(SearchMembers.this);
				pDialog.setCancelable(false);
				pDialog.show();}
			else{
				pDialog.setCancelable(false);
				pDialog.show();
			}}


		@Override
		protected ArrayList<MembersModel> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONArray membersResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(MembersURL);
			if(jsonObjectRecived != null){

				try{
					membersResponse = jsonObjectRecived.getJSONArray("users");
				}catch(JSONException e){
					e.printStackTrace();
				}
				for(int i = 0; i< membersResponse.length();i++){
					MembersModel membersData = new MembersModel();
					JSONObject membersDetails;
					try{
						membersDetails = membersResponse.getJSONObject(i);
						membersData.setUser_id(membersDetails.getString("user_id"));
						membersData.setFirstname(membersDetails.getString("firstname"));
						membersData.setLastname(membersDetails.getString("lastname"));
						membersData.setMiddlename(membersDetails.getString("middlename"));
						membersData.setUsername(membersDetails.getString("username"));
						membersData.setEmail(membersDetails.getString("email"));
						membersData.setMobile(membersDetails.getString("mobile"));
						membersData.setDob(membersDetails.getString("dob"));
						membersData.setJoined_date(membersDetails.getString("joined_date"));
						pagination_Date_String = membersDetails.getString("joined_date");
						membersData.setGendername(membersDetails.getString("gendername"));
						membersData.setUser_profile_photo(membersDetails.getString("user_profile_photo"));
						membersData.setUser_cover_photo(membersDetails.getString("user_cover_photo"));
						membersData.setMessaging_status(membersDetails.getString("messaging_status"));
						membersData.setFriend(membersDetails.getString("friend"));
						membersData.setCurr_city_name(membersDetails.getString("curr_city_name"));
						membersData.setHome_city_name(membersDetails.getString("home_city_name"));
						membersData.setPending_req_status(membersDetails.getString("pending_req_status"));
						membersData.setAccept_req_status(membersDetails.getString("accept_req_status"));
						membersData.setCover_thumb1(membersDetails.getString("cover_thumb1"));
						membersData.setCover_thumb2(membersDetails.getString("cover_thumb2"));
						membersData.setCover_thumb3(membersDetails.getString("cover_thumb3"));
						membersData.setProfile_thumb1(membersDetails.getString("profile_thumb1"));
						membersData.setProfile_thumb2(membersDetails.getString("profile_thumb2"));
						membersData.setProfile_thumb3(membersDetails.getString("profile_thumb3"));
						connectionssearch.add(membersData);
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
		protected void onPostExecute(ArrayList<MembersModel> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if(result == null){
				//NoMembers.setVisibility(View.VISIBLE);
				Toast.makeText(SearchMembers.this, "No Members Found", Toast.LENGTH_SHORT).show();
			}else if(result.size()==0){
				//NoMembers.setVisibility(View.VISIBLE);
			}  else{
				conn_adapter = new MemberSearchAdapter(SearchMembers.this, result);
				listconn.setAdapter(conn_adapter);
				if(result.size() < 40){
					flag_loading = true;
				}else{
					flag_loading = false;
				}
			}
		}
	}
	private class MembersLoadMoreAsyncClass extends AsyncTask<Void, Void, ArrayList<MembersModel>>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			// Showing progress dialog before sending http request
			if(pDialog == null){
				pDialog = Util.createProgressDialog(SearchMembers.this);
				pDialog.setCancelable(false);
				pDialog.show();}
			else{
				pDialog.setCancelable(false);
				pDialog.show();
			}}
		@Override
		protected ArrayList<MembersModel> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONArray membersResponse = null;
			JSONObject jsonObjectRecived=null;
			if(searchPaginationFlag==false)
			{
				jsonObjectRecived = HttpGetClient.sendHttpPost(MembersURL + "?last_date=" + pagination_Date_String.replaceAll(" ", "%20"));
			}
			else
			{
				// url for the pagination in search
				jsonObjectRecived = HttpGetClient.sendHttpPost(MembersURL+"?search_text="+conn_search.getText().toString().toLowerCase(Locale.getDefault()) + "&last_date=" + pagination_Date_String.replaceAll(" ", "%20"));
			}
			if(jsonObjectRecived != null){

				try{
					membersResponse = jsonObjectRecived.getJSONArray("users");
				}catch(JSONException e){
					e.printStackTrace();
				}
				if(membersResponse.length() > 0){
					for(int i = 0; i< membersResponse.length();i++){
						MembersModel membersData = new MembersModel();
						JSONObject membersDetails;
						try{
							membersDetails = membersResponse.getJSONObject(i);
							membersData.setUser_id(membersDetails.getString("user_id"));
							membersData.setFirstname(membersDetails.getString("firstname"));
							membersData.setLastname(membersDetails.getString("lastname"));
							membersData.setMiddlename(membersDetails.getString("middlename"));
							membersData.setUsername(membersDetails.getString("username"));
							membersData.setEmail(membersDetails.getString("email"));
							membersData.setMobile(membersDetails.getString("mobile"));
							membersData.setDob(membersDetails.getString("dob"));
							membersData.setJoined_date(membersDetails.getString("joined_date"));
							pagination_Date_String = membersDetails.getString("joined_date");
							membersData.setGendername(membersDetails.getString("gendername"));
							membersData.setUser_profile_photo(membersDetails.getString("user_profile_photo"));
							membersData.setUser_cover_photo(membersDetails.getString("user_cover_photo"));
							membersData.setMessaging_status(membersDetails.getString("messaging_status"));
							membersData.setFriend(membersDetails.getString("friend"));
							membersData.setCurr_city_name(membersDetails.getString("curr_city_name"));
							membersData.setHome_city_name(membersDetails.getString("home_city_name"));
							membersData.setPending_req_status(membersDetails.getString("pending_req_status"));
							membersData.setAccept_req_status(membersDetails.getString("accept_req_status"));
							membersData.setCover_thumb1(membersDetails.getString("cover_thumb1"));
							membersData.setCover_thumb2(membersDetails.getString("cover_thumb2"));
							membersData.setCover_thumb3(membersDetails.getString("cover_thumb3"));
							membersData.setProfile_thumb1(membersDetails.getString("profile_thumb1"));
							membersData.setProfile_thumb2(membersDetails.getString("profile_thumb2"));
							membersData.setProfile_thumb3(membersDetails.getString("profile_thumb3"));
							connectionssearch.add(membersData);
						}catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}else{
					return null;
				}
				return connectionssearch;
			}else{
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<MembersModel> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if(result == null){
				if(conn_adapter.isEmpty()){
					//NoMembers.setVisibility(View.VISIBLE);
					flag_loading = false;
				}
				flag_loading = true;
				Toast.makeText(SearchMembers.this, "No More Members to Load", Toast.LENGTH_SHORT).show();
			}else if(result.size()==0){
				if(conn_adapter.isEmpty()){
					//NoMembers.setVisibility(View.VISIBLE);
				}
				flag_loading = true;
				Toast.makeText(getApplicationContext(), "No More Members to Load", Toast.LENGTH_SHORT).show();
				}   else{
				conn_adapter = new MemberSearchAdapter(SearchMembers.this, result);
				conn_adapter.notifyDataSetChanged();
				flag_loading = false;
				pageCount = pageCount +40;
				listconn.setSelection(pageCount);
			}

		}


	}
	// pagination for search

	private class MemberSearchAsyncClass extends AsyncTask<Void, Void, ArrayList<MembersModel>>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			// Showing progress dialog before sending http request
			if(pDialog == null){
				pDialog = Util.createProgressDialog(SearchMembers.this);
				pDialog.setCancelable(false);
				pDialog.show();}
			else{
				pDialog.setCancelable(false);
				pDialog.show();
			}}


		@Override
		protected ArrayList<MembersModel> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONArray membersResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(MembersURL+"?search_text="+conn_search.getText().toString().toLowerCase(Locale.getDefault()) );
			if(jsonObjectRecived != null){

				try{
					membersResponse = jsonObjectRecived.getJSONArray("users");
				}catch(JSONException e){
					e.printStackTrace();
				}
				for(int i = 0; i< membersResponse.length();i++){
					MembersModel membersData = new MembersModel();
					JSONObject membersDetails;
					try{
						membersDetails = membersResponse.getJSONObject(i);
						membersData.setUser_id(membersDetails.getString("user_id"));
						membersData.setFirstname(membersDetails.getString("firstname"));
						membersData.setLastname(membersDetails.getString("lastname"));
						membersData.setMiddlename(membersDetails.getString("middlename"));
						membersData.setUsername(membersDetails.getString("username"));
						membersData.setEmail(membersDetails.getString("email"));
						membersData.setMobile(membersDetails.getString("mobile"));
						membersData.setDob(membersDetails.getString("dob"));
						membersData.setJoined_date(membersDetails.getString("joined_date"));
						pagination_Date_String = membersDetails.getString("joined_date");
						membersData.setGendername(membersDetails.getString("gendername"));
						membersData.setUser_profile_photo(membersDetails.getString("user_profile_photo"));
						membersData.setUser_cover_photo(membersDetails.getString("user_cover_photo"));
						membersData.setMessaging_status(membersDetails.getString("messaging_status"));
						membersData.setFriend(membersDetails.getString("friend"));
						membersData.setCurr_city_name(membersDetails.getString("curr_city_name"));
						membersData.setHome_city_name(membersDetails.getString("home_city_name"));
						membersData.setPending_req_status(membersDetails.getString("pending_req_status"));
						membersData.setAccept_req_status(membersDetails.getString("accept_req_status"));
						membersData.setCover_thumb1(membersDetails.getString("cover_thumb1"));
						membersData.setCover_thumb2(membersDetails.getString("cover_thumb2"));
						membersData.setCover_thumb3(membersDetails.getString("cover_thumb3"));
						membersData.setProfile_thumb1(membersDetails.getString("profile_thumb1"));
						membersData.setProfile_thumb2(membersDetails.getString("profile_thumb2"));
						membersData.setProfile_thumb3(membersDetails.getString("profile_thumb3"));
						connectionssearch.add(membersData);
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
		protected void onPostExecute(ArrayList<MembersModel> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if(result == null){
				//NoMembers.setVisibility(View.VISIBLE);
				Toast.makeText(SearchMembers.this, "No Members Found", Toast.LENGTH_SHORT).show();
			}else if(result.size()==0){
				//NoMembers.setVisibility(View.VISIBLE);
			}  else{
				conn_adapter.notifyDataSetInvalidated();
				conn_adapter = new MemberSearchAdapter(SearchMembers.this, result);
				listconn.setAdapter(conn_adapter);
				if(result.size() < 40){
					flag_loading = true;
				}else{
					flag_loading = false;
				}

			}
		}
	}
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		if(flag_refresh==true)
		{
			intent.putExtra("refresh", "refresh");
			setResult(RESULT_OK,intent);

		}
		else if (flag_refresh==false) {
			intent.putExtra("refresh", "no_refresh");
			setResult(RESULT_OK,intent);
		}
		else
		{
			intent.putExtra("refresh", "no_refresh");
			setResult(RESULT_OK,intent);
		}
		super.finish();
	}



}

