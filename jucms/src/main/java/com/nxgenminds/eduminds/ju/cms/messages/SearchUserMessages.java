package com.nxgenminds.eduminds.ju.cms.messages;

import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
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
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;



public class SearchUserMessages extends Activity {

	public Menu menuInstance;
	private ListView listconn;
	private ArrayList<SearchUserMessageModel> connectionssearch = new ArrayList<SearchUserMessageModel>();
	private EditText conn_search;
	SearchUserMessagesAdapter conn_adapter;
	private ProgressDialog pDialog;
	AlertDialogManager alert = new AlertDialogManager();
	private String MembersURL = Util.API+ "userlist";
	private static int pageCount = 0;
	private static String pagination_Date_String = "";
	private boolean flag_loading = false;
	public static boolean searchPaginationFlag=false;
	private static final int UPDATE_RESULT = 0x1;
	private static String update="false";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.create_broadcast_add_members);
		listconn = (ListView)findViewById(R.id.list_connections);
		conn_search=(EditText)findViewById(R.id.connection_search);
		connectionssearch.clear();

		ConnectionDetector conn = new ConnectionDetector(SearchUserMessages.this);
		if(conn.isConnectingToInternet()){
			new MembersAsyncClass().execute();
		}else{
			alert.showAlertDialog(SearchUserMessages.this, "Connection Error", "Please check your internet connection", false);
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
						ConnectionDetector conn = new ConnectionDetector(SearchUserMessages.this);
						if(conn.isConnectingToInternet()){
							
							// searching users from list

							if(conn_search.getText().toString().trim().length()>=0){
								new MemberSearchAsyncClass().execute();
								
							}
							else
							{
								Toast.makeText(getApplicationContext(), "Add text to search contacts", Toast.LENGTH_SHORT).show();
							}
						}else{
							alert.showAlertDialog(SearchUserMessages.this, "Connection Error", "Please check your internet connection", false);
						}
					}else{
						Toast.makeText(getApplicationContext(), "Users Not Found", Toast.LENGTH_SHORT).show();
					}
					return true;
				}


				return false;
			}


		});
		conn_search.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_UP) {
	               /* int leftEdgeOfRightDrawable = mQueryEditText.getRight() 
	                      - mQueryEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width();
	                // when EditBox has padding, adjust leftEdge like
	                // leftEdgeOfRightDrawable -= getResources().getDimension(R.dimen.edittext_padding_left_right);
	                if (event.getRawX() >= leftEdgeOfRightDrawable) {
	                    // clicked on clear icon
*/	                    conn_search.setText("");
					//Log.d("Teacher Fragment", "inside setOnTouchListener,on touch befor false");
	                    return false;
	                    
	                }
	            
	           /*Log.d("Teacher Fragment", "inside setOnTouchListener,on touch outside false");*/
				/*adapter = new UsersAdapter(getActivity(), mTeachersArrayList);
				ConnectionDetector conn = new ConnectionDetector(getActivity());
				if(conn.isConnectingToInternet()){
					new TeachersAsyncClass().execute();
				}else{
					alert.showAlertDialog(getActivity(), "Connection Error", "Please check your internet connection", false);
				}*/
			return false;
			}
		});
		//pagination for member search
		listconn.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0)
				{
					if(flag_loading == false)
					{
						flag_loading = true;
						ConnectionDetector conn = new ConnectionDetector(SearchUserMessages.this);
						if(conn.isConnectingToInternet()){
							new MembersLoadMoreAsyncClass().execute();
						}else{
							alert.showAlertDialog(SearchUserMessages.this, "Connection Error", "Please check your internet connection", false);
						}
					}
				}
			}
		});

		listconn.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Object object = listconn.getItemAtPosition(position);
				SearchUserMessageModel members_data = (SearchUserMessageModel)  object;

				Intent ChatDetail = new Intent(SearchUserMessages.this,ChatDetailActivity.class);
				ChatDetail.putExtra("username", members_data.getMessageusers());
				ChatDetail.putExtra("userprofile", members_data.getProfile_photo());
				ChatDetail.putExtra("Firstname", members_data.getFirstname());
				ChatDetail.putExtra("user_id", members_data.getUser_id());
				ChatDetail.putExtra("user_role", members_data.getRole());
				startActivityForResult(ChatDetail, UPDATE_RESULT);

			}
		});

	}



	@Override
	public void onBackPressed() {
		finish();
	}



	private class MembersAsyncClass extends AsyncTask<Void, Void, ArrayList<SearchUserMessageModel>>{

		@Override
		protected void onPreExecute() {
			if(pDialog == null){
				pDialog = Util.createProgressDialog(SearchUserMessages.this);
				pDialog.setCancelable(false);
				pDialog.show();}
			else{
				pDialog.setCancelable(false);
				pDialog.show();
			}}


		@Override
		protected ArrayList<SearchUserMessageModel> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONArray membersResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(MembersURL);
			if(jsonObjectRecived != null){

				try{
					membersResponse = jsonObjectRecived.getJSONArray("user_list");
				}catch(JSONException e){
					e.printStackTrace();
				}
				if(membersResponse.length()>0){
					for(int i = 0; i< membersResponse.length();i++){
						SearchUserMessageModel membersData = new SearchUserMessageModel();
						JSONObject membersDetails;
						try{
							membersDetails = membersResponse.getJSONObject(i);
							membersData.setUser_id(membersDetails.getString("user_id"));
							membersData.setFirstname(membersDetails.getString("firstname"));
							membersData.setLastname(membersDetails.getString("lastname"));
							membersData.setUsername(membersDetails.getString("username"));
							membersData.setEmail(membersDetails.getString("email"));
							membersData.setMessageusers(membersDetails.getString("messageusers"));
							membersData.setCreated_date(membersDetails.getString("created_date"));
							pagination_Date_String = membersDetails.getString("created_date");
							membersData.setStream_name(membersDetails.getString("stream_name"));
							membersData.setStream_id(membersDetails.getString("stream_id"));
							membersData.setSection_id(membersDetails.getString("section_id"));
							membersData.setSection_name(membersDetails.getString("section_name"));
							membersData.setSemester(membersDetails.getString("semester"));
							membersData.setSemester_id(membersDetails.getString("semester_id"));
							membersData.setRole(membersDetails.getString("role"));
							membersData.setProfile_photo(membersDetails.getString("profile_photo"));
							membersData.setFullname(membersDetails.getString("fullname"));
							connectionssearch.add(membersData);
						}catch (JSONException e) {
							e.printStackTrace();
						}
					}
					return connectionssearch;
				} else
				{
					return null;
				}

			}else{
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<SearchUserMessageModel> result) {
			super.onPostExecute(result);
			pDialog.dismiss();
			if(result == null){
				Toast.makeText(SearchUserMessages.this, "No Users Found", Toast.LENGTH_SHORT).show();
			}else if(result.size()==0){
				//NoMembers.setVisibility(View.VISIBLE);
			}  else{
				conn_adapter = new SearchUserMessagesAdapter(SearchUserMessages.this, result);
				listconn.setAdapter(conn_adapter);
				if(result.size() < 20){
					flag_loading = true;
				}else{
					flag_loading = false;
				}
			}
		}
	}
	private class MembersLoadMoreAsyncClass extends AsyncTask<Void, Void, ArrayList<SearchUserMessageModel>>{

		@Override
		protected void onPreExecute() {
			if(pDialog == null){
				pDialog = Util.createProgressDialog(SearchUserMessages.this);
				pDialog.setCancelable(false);
				pDialog.show();}
			else{
				pDialog.setCancelable(false);
				pDialog.show();
			}}
		@Override
		protected ArrayList<SearchUserMessageModel> doInBackground(Void... params) {
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
					membersResponse = jsonObjectRecived.getJSONArray("user_list");
				}catch(JSONException e){
					e.printStackTrace();
				}
				if(membersResponse.length() > 0){
					for(int i = 0; i< membersResponse.length();i++){
						SearchUserMessageModel membersData = new SearchUserMessageModel();
						JSONObject membersDetails;
						try{
							membersDetails = membersResponse.getJSONObject(i);
							membersData.setUser_id(membersDetails.getString("user_id"));
							membersData.setFirstname(membersDetails.getString("firstname"));
							membersData.setLastname(membersDetails.getString("lastname"));
							membersData.setUsername(membersDetails.getString("username"));
							membersData.setEmail(membersDetails.getString("email"));
							membersData.setMessageusers(membersDetails.getString("messageusers"));
							membersData.setCreated_date(membersDetails.getString("created_date"));
							pagination_Date_String = membersDetails.getString("created_date");
							membersData.setStream_name(membersDetails.getString("stream_name"));
							membersData.setStream_id(membersDetails.getString("stream_id"));
							membersData.setSection_id(membersDetails.getString("section_id"));
							membersData.setSection_name(membersDetails.getString("section_name"));
							membersData.setSemester(membersDetails.getString("semester"));
							membersData.setSemester_id(membersDetails.getString("semester_id"));
							membersData.setRole(membersDetails.getString("role"));
							membersData.setProfile_photo(membersDetails.getString("profile_photo"));
							membersData.setFullname(membersDetails.getString("fullname"));
							connectionssearch.add(membersData);
						}catch (JSONException e) {
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
		protected void onPostExecute(ArrayList<SearchUserMessageModel> result) {
			super.onPostExecute(result);
			pDialog.dismiss();
			if(result == null){
				if(conn_adapter.isEmpty()){
					flag_loading = false;
				}
				flag_loading = true;
				Toast.makeText(SearchUserMessages.this, "No More Users to Load", Toast.LENGTH_SHORT).show();
			}else if(result.size()==0){
				if(conn_adapter.isEmpty()){
					//NoMembers.setVisibility(View.VISIBLE);
				}
				flag_loading = true;
				Toast.makeText(getApplicationContext(), "No More Users to Load", Toast.LENGTH_SHORT).show();
				System.out.println("No Members");
			}    else{
				conn_adapter = new SearchUserMessagesAdapter(SearchUserMessages.this, result);
				conn_adapter.notifyDataSetChanged();
				flag_loading = false;
				pageCount = pageCount +20;
				listconn.setSelection(pageCount);
			}

		}


	}

	private class MemberSearchAsyncClass extends AsyncTask<Void, Void, ArrayList<SearchUserMessageModel>>{

		@Override
		protected void onPreExecute() {
			if(pDialog == null){
				pDialog = Util.createProgressDialog(SearchUserMessages.this);
				pDialog.setCancelable(false);
				pDialog.show();}
			else{
				pDialog.setCancelable(false);
				pDialog.show();
			}}


		@Override
		protected ArrayList<SearchUserMessageModel> doInBackground(Void... params) {
			JSONArray membersResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(MembersURL+"?search_text="+conn_search.getText().toString().trim());
			if(jsonObjectRecived != null){

				try{
					membersResponse = jsonObjectRecived.getJSONArray("user_list");
				}catch(JSONException e){
					e.printStackTrace();
				}
				for(int i = 0; i< membersResponse.length();i++){
					SearchUserMessageModel membersData = new SearchUserMessageModel();
					JSONObject membersDetails;
					try{
						membersDetails = membersResponse.getJSONObject(i);
						membersData.setUser_id(membersDetails.getString("user_id"));
						membersData.setFirstname(membersDetails.getString("firstname"));
						membersData.setLastname(membersDetails.getString("lastname"));
						membersData.setUsername(membersDetails.getString("username"));
						membersData.setEmail(membersDetails.getString("email"));
						membersData.setMessageusers(membersDetails.getString("messageusers"));
						membersData.setCreated_date(membersDetails.getString("created_date"));
						pagination_Date_String = membersDetails.getString("created_date");
						membersData.setStream_name(membersDetails.getString("stream_name"));
						membersData.setStream_id(membersDetails.getString("stream_id"));
						membersData.setSection_id(membersDetails.getString("section_id"));
						membersData.setSection_name(membersDetails.getString("section_name"));
						membersData.setSemester(membersDetails.getString("semester"));
						membersData.setSemester_id(membersDetails.getString("semester_id"));
						membersData.setRole(membersDetails.getString("role"));
						membersData.setProfile_photo(membersDetails.getString("profile_photo"));
						membersData.setFullname(membersDetails.getString("fullname"));
						connectionssearch.add(membersData);
					}catch (JSONException e) {
						e.printStackTrace();
					}
				}
				return connectionssearch;
			}else{
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<SearchUserMessageModel> result) {
			super.onPostExecute(result);
			pDialog.dismiss();
			if(result == null){
				//NoMembers.setVisibility(View.VISIBLE);
				Toast.makeText(SearchUserMessages.this, "No Users Found", Toast.LENGTH_SHORT).show();
			}else if(result.size()==0){
				//NoMembers.setVisibility(View.VISIBLE);
			}  else{
				conn_adapter.notifyDataSetInvalidated();
				conn_adapter = new SearchUserMessagesAdapter(SearchUserMessages.this, result);
				listconn.setAdapter(conn_adapter);
				if(result.size() < 20){
					flag_loading = true;
				}else{
					flag_loading = false;
				}

			}
		}
	}
	@Override
	public void finish() {
		Intent intent = new Intent();
		if(update.equalsIgnoreCase("true"))
		{
			intent.putExtra("update", "true");
		}
		else
		{
			intent.putExtra("update", "false");
		}
		setResult(RESULT_OK,intent);
		super.finish();
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode !=SearchUserMessages.this.RESULT_OK) {
			return;
		}

		Bitmap bitmap;
		switch (requestCode) {

		case UPDATE_RESULT:
			if(resultCode == SearchUserMessages.this.RESULT_OK && null != data)
			{
				if(data.getStringExtra("update").equalsIgnoreCase("true"))
				{
					update="true";
				}
				else
				{
					update="false";
				}
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data); 
	}
}

