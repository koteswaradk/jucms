package com.nxgenminds.eduminds.ju.cms.fragments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.adapters.MemberAdapter_Select_Member;
import com.nxgenminds.eduminds.ju.cms.adapters.select_connection_adapter;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.models.ConnectionsModel;
import com.nxgenminds.eduminds.ju.cms.models.MembersModel;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class SelectConnection_contacts extends Fragment {
	private ListView listconn;
	private TextView NoMembers;
	private String MembersURL = Util.API+ "user";
	public static Button add;
	private Button exit;
	private ArrayList<ConnectionsModel> connectionssearch = new ArrayList<ConnectionsModel>();
	private ArrayList<MembersModel> memberssearch = new ArrayList<MembersModel>();
	private EditText conn_search;
	StringBuffer response_uname,response_uID;
	String custom_users_name=" ";
	String custom_users_id=" ";
	select_connection_adapter conn_adapter;
	MemberAdapter_Select_Member  adapter;
	private ProgressDialog pDialog;
	AlertDialogManager alert = new AlertDialogManager();

	ArrayList <String> checkedUserName=new ArrayList<String>();
	ArrayList <String> checkedUserID=new ArrayList<String>();
	String received_userID;
	String trimeduid="";
	//public static ArrayList<String> myList ;
	private static String pagination_Date_String = "";
	private boolean flag_loading = false;
	public static boolean searchPaginationFlag=false;
	private static int pageCount = 0;

	private static String pagination_Date_String_mem = "";
	private boolean flag_loading_mem= false;
	public static boolean searchPaginationFlag_mem=false;
	private static int pageCount_mem = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Get the view from fragmenttab2.xml
		
		View view = inflater.inflate(R.layout.select_contact, container, false);
		Typeface typeFace=Typeface.createFromAsset(getActivity().getAssets(),"fonts/BentonSans-Regular.otf");
		pageCount=0;
		pageCount_mem=0;
		conn_search = (EditText) view.findViewById(R.id.connection_search);
		conn_search.setTypeface(typeFace);
		listconn = (ListView)view.findViewById(R.id.list_connections);

		NoMembers = (TextView) view.findViewById(R.id.NoMembers);
		NoMembers.setTypeface(typeFace);

		add=(Button)view.findViewById(R.id.add);
		add.setTypeface(typeFace);

		exit=(Button)view.findViewById(R.id.exit);
		exit.setTypeface(typeFace);

		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		// get the intent
		Bundle bundle = getActivity().getIntent().getExtras();
		received_userID = bundle.getString("usercheckedid");
		
		if(received_userID.equalsIgnoreCase(" "))
		{
			
			Util.myList = new ArrayList<String>(Arrays.asList(received_userID.split(",")));
		}
		else
		{
			
			trimeduid=removeLastChar(received_userID);
			Util.myList = new ArrayList<String>(Arrays.asList(trimeduid.split(",")));
		}


		for(int id=0;id<Util.myList.size();id++)
		{
			 
		}


		//end


		if(Util.ADMIN.equalsIgnoreCase("0")){
			connectionssearch.clear();
			ConnectionDetector conn = new ConnectionDetector(getActivity());
            if(conn.isConnectingToInternet()){
            	new ConnectionsAsyncClass().execute();
            } else{
            	alert.showAlertDialog(getActivity(),"Connection Error","Check your Internet Connection",false);
            }
			
		}
		else
		{
			memberssearch.clear();
			ConnectionDetector conn = new ConnectionDetector(getActivity());
			if(conn.isConnectingToInternet()){
				new MembersAsyncClass().execute();
            } else{
            	alert.showAlertDialog(getActivity(),"Connection Error","Check your Internet Connection",false);
            }
				
		}


		conn_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					if(Util.ADMIN.equalsIgnoreCase("0")){
						searchPaginationFlag=true;
						pageCount = 0;
						connectionssearch.clear();
						conn_adapter.notifyDataSetInvalidated();
						if(conn_adapter != null){
							ConnectionDetector conn = new ConnectionDetector(getActivity());
							if(conn.isConnectingToInternet()){
								if(conn_search.getText().toString().trim().length()>0){
									new ConnectionsSearchAsyncClass().execute();
								}
								else
								{
									Toast.makeText(getActivity(), "Add text to search contacts", Toast.LENGTH_SHORT).show();
								}
							}else{
								alert.showAlertDialog(getActivity(), "Connection Error", "Please check your internet connection", false);
							}
						}else{
							Toast.makeText(getActivity(), "Connections Not Found", Toast.LENGTH_SHORT).show();
						}
						return true;
					}
					else{
						///for member
						searchPaginationFlag_mem=true;
						pageCount_mem = 0;
						memberssearch.clear();
						adapter.notifyDataSetInvalidated();
						if(adapter != null){
							ConnectionDetector conn = new ConnectionDetector(getActivity());
							if(conn.isConnectingToInternet()){
								if(conn_search.getText().toString().trim().length()>0){
									new MemberSearchAsyncClass().execute();
								}
								else
								{
									Toast.makeText(getActivity(), "Add text to search contacts", Toast.LENGTH_SHORT).show();
								}

							}else{
								alert.showAlertDialog(getActivity(), "Connection Error", "Please check your internet connection", false);
							}
						}else{
							Toast.makeText(getActivity(), "Members Not Found", Toast.LENGTH_SHORT).show();
						}
						return true;

					}
				}
				return false;
			}


		});

		// Capture Text in EditText
		conn_search.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {  
				if(Util.ADMIN.equalsIgnoreCase("0"))
				{
					if(conn_adapter != null){
						String text = conn_search.getText().toString().toLowerCase(Locale.getDefault());
						conn_adapter.filter(text);

					}else{
						Toast.makeText(getActivity(), "Connections Not Found", Toast.LENGTH_SHORT).show();
					}
				}
				else
				{

					if(adapter != null){
						String text = conn_search.getText().toString().toLowerCase(Locale.getDefault());
						adapter.filter(text);

					}else{
						Toast.makeText(getActivity(), "Members Not Found", Toast.LENGTH_SHORT).show();
					}

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

		//pagination for connection search & member search

		listconn.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if(Util.ADMIN.equalsIgnoreCase("0")){
					// TODO Auto-generated method stub
					if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0)
					{
						if(flag_loading == false)
						{
							flag_loading = true;
							ConnectionDetector conn = new ConnectionDetector(getActivity());
							if(conn.isConnectingToInternet()){
								new ConnectionLoadMoreAsyncClass().execute();
							}else{
								alert.showAlertDialog(getActivity(), "Connection Error", "Please check your internet connection", false);
							}
						}
					}
				}
				else
				{

					// TODO Auto-generated method stub
					if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0)
					{
						if(flag_loading_mem == false)
						{
							flag_loading_mem = true;
							ConnectionDetector conn = new ConnectionDetector(getActivity());
							if(conn.isConnectingToInternet()){
								new MembersLoadMoreAsyncClass().execute();
							}else{
								alert.showAlertDialog(getActivity(), "Connection Error", "Please check your internet connection", false);
							}
						}
					}
				
				}
			}
		});

		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(Util.ADMIN.equalsIgnoreCase("0"))
				{
					listconn.setEnabled(false);
					int size = conn_adapter.getCount();

					for(int i=0;i<size;i++){
						ConnectionsModel rowItem = (ConnectionsModel) conn_adapter.getItem(i);
						if(rowItem.isSelected()){
							checkedUserName.add(rowItem.getFirstname().trim());
							checkedUserID.add(rowItem.getUser_id().trim());
						}

					}
					custom_users_name=checkedUserName.toString();
					custom_users_id=checkedUserID.toString();

					sendData();

					getActivity().finish();
					listconn.setEnabled(true);
				}
				else{
					listconn.setEnabled(false);
					int size = adapter.getCount();

					for(int i=0;i<size;i++){
						MembersModel rowItem = (MembersModel) adapter.getItem(i);
						if(rowItem.isSelected()){
							checkedUserName.add(rowItem.getFirstname().trim());
							checkedUserID.add(rowItem.getUser_id().trim());
						}

					}
					custom_users_name=checkedUserName.toString();
					custom_users_id=checkedUserID.toString();

					sendData();
					getActivity().finish();
					listconn.setEnabled(true);

				}
			}
		});

		exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub exit 
				getActivity().finish();
			}
		} );
	}
	private static String removeLastChar(String str) {
		return str.substring(1,str.length()-1);
	}
	public void sendData() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra("CustomUserName", custom_users_name);
		intent.putExtra("CustomUserID", custom_users_id);

		getActivity().setResult(getActivity().RESULT_OK, intent);

	}

	private class ConnectionsAsyncClass extends AsyncTask<Void, Void, ArrayList<ConnectionsModel>>{

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
				NoMembers.setVisibility(View.VISIBLE);
				Toast.makeText(getActivity(), "No Members Found", Toast.LENGTH_SHORT).show();
			}else if(result.size()==0){
				NoMembers.setVisibility(View.VISIBLE);
			}  else{
				conn_adapter = new select_connection_adapter(getActivity(), result);
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
			if(pDialog == null){
				pDialog = Util.createProgressDialog(getActivity());
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
				conn_adapter = new select_connection_adapter(getActivity(), result);
				listconn.setAdapter(conn_adapter);
				if(result.size() < 40){
					flag_loading = true;
				}else{
					flag_loading = false;
				}
			}

		}
	}
	private class MembersAsyncClass extends AsyncTask<Void, Void, ArrayList<MembersModel>>{

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
						pagination_Date_String_mem= membersDetails.getString("joined_date");
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
						memberssearch.add(membersData);
					}catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return memberssearch;
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
				NoMembers.setVisibility(View.VISIBLE);
				Toast.makeText(getActivity(), "No Members Found", Toast.LENGTH_SHORT).show();
			}else if(result.size()==0){
				NoMembers.setVisibility(View.VISIBLE);
			}  else{
				adapter = new MemberAdapter_Select_Member(getActivity(), result);
				listconn.setAdapter(adapter);
				if(result.size() < 40){
					flag_loading_mem = true;
				}else{
					flag_loading_mem = false;
				}
			}
		}
	}
	private class MemberSearchAsyncClass extends AsyncTask<Void, Void, ArrayList<MembersModel>>{

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
						pagination_Date_String_mem = membersDetails.getString("joined_date");
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
						memberssearch.add(membersData);
					}catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return memberssearch;
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
				NoMembers.setVisibility(View.VISIBLE);
				Toast.makeText(getActivity(), "No Members Found", Toast.LENGTH_SHORT).show();
			}else if(result.size()==0){
				NoMembers.setVisibility(View.VISIBLE);
			}  else{
				adapter = new MemberAdapter_Select_Member(getActivity(), result);
				listconn.setAdapter(adapter);
				if(result.size() < 40){
					flag_loading_mem = true;
				}else{
					flag_loading_mem = false;
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
				pDialog = Util.createProgressDialog(getActivity());
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
				Toast.makeText(getActivity(), "No More Connections to Load", Toast.LENGTH_SHORT).show();
			}else if(result.size()==0){
				if(conn_adapter.isEmpty()){
					//NoMembers.setVisibility(View.VISIBLE);
				}
				flag_loading = true;
				Toast.makeText(getActivity(), "No More Connections to Load", Toast.LENGTH_SHORT).show();
				
			}    else{
				conn_adapter = new select_connection_adapter(getActivity(), result);
				conn_adapter.notifyDataSetChanged();
				flag_loading = false;
				pageCount = pageCount +40;
				listconn.setSelection(pageCount);
			}

		}

	}
	private class MembersLoadMoreAsyncClass extends AsyncTask<Void, Void, ArrayList<MembersModel>>{

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
		protected ArrayList<MembersModel> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONArray membersResponse = null;
			JSONObject jsonObjectRecived=null;
			if(searchPaginationFlag_mem==false)
			{
				jsonObjectRecived = HttpGetClient.sendHttpPost(MembersURL + "?last_date=" + pagination_Date_String_mem.replaceAll(" ", "%20"));
			}
			else
			{
				// url for the pagination in search
				jsonObjectRecived = HttpGetClient.sendHttpPost(MembersURL+"?search_text="+conn_search.getText().toString().toLowerCase(Locale.getDefault()) + "&last_date=" + pagination_Date_String_mem.replaceAll(" ", "%20"));
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
							pagination_Date_String_mem = membersDetails.getString("joined_date");
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
							memberssearch.add(membersData);
						}catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}else{
					return null;
				}
				return memberssearch;
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
				if(adapter.isEmpty()){
					//NoMembers.setVisibility(View.VISIBLE);
					flag_loading_mem = false;
				}
				flag_loading_mem = true;
				Toast.makeText(getActivity(), "No More Members to Load", Toast.LENGTH_SHORT).show();
			}else if(result.size()==0){
				if(adapter.isEmpty()){
					//NoMembers.setVisibility(View.VISIBLE);
				}
				flag_loading_mem = true;
				Toast.makeText(getActivity(), "No More Members to Load", Toast.LENGTH_SHORT).show();
				
			}    else{
				adapter = new MemberAdapter_Select_Member(getActivity(), result);
				adapter.notifyDataSetChanged();
				flag_loading_mem = false;
				pageCount_mem = pageCount_mem +40;
				listconn.setSelection(pageCount_mem);
			}

		}


	}
}		




