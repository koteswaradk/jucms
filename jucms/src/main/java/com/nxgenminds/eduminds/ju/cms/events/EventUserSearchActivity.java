package com.nxgenminds.eduminds.ju.cms.events;

import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class  EventUserSearchActivity extends ActionBarActivity  {
	//search
	private static final int LIST_PIC_SCREEN = 0;
	private static final int VIEW_PIC_SCREEN = 1;
	public static String EVENT_USER_SEARCH_API = Util.API+"event_user_search/?search_text=";
	//end search
	
	public static ArrayList<String> mCustomUserId = new ArrayList<String>();
	public static ArrayList<String> mCustomUserName = new ArrayList<String>();
	
	private String mSelectedUserId="",
			       mSelectedUserName="";
	
	public Menu menuInstance;
    private ListView mEventSearchList;
    
	private ArrayList<EventUserSearchModel> mArrayListEventUserSearch = new ArrayList<EventUserSearchModel>();
	public static ArrayList<EventSelectedUsersModel> mArrayListSelectedUsers = new ArrayList<EventSelectedUsersModel>();
	
	private EditText conn_search;
	StringBuffer response_uname,response_uID;
	String custom_users_name=" ";
	String custom_users_id=" ";
	private EventUserSearchAdapter event_user_adapter;
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
	private String mEventId;
	private int firstSearch;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.create_broadcast_add_members);
		// get the intent
		Bundle bundle = getIntent().getExtras();
		if(bundle !=null){
		
		received_userID = bundle.getString("usercheckedid");
		mEventId = bundle.getString("event_id");
		firstSearch = bundle.getInt("first_search");
		}
		
		
		mEventSearchList = (ListView)findViewById(R.id.list_connections);
		conn_search=(EditText)findViewById(R.id.connection_search);

		mArrayListEventUserSearch.clear();
		//new EventAsyncClass().execute();
		
		conn_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					searchPaginationFlag=true;
					pageCount = 0;
				
				mArrayListEventUserSearch.clear();
				String text = conn_search.getText().toString().toLowerCase(Locale.getDefault());
				ConnectionDetector conn = new ConnectionDetector(EventUserSearchActivity.this);
					if(text.length()>0){
						if(conn.isConnectingToInternet()){
							mArrayListEventUserSearch.clear();
							mEventSearchList.setVisibility(View.VISIBLE);
							new EventUserSearchAsyncClass().execute();
						}else{
							alert.showAlertDialog(EventUserSearchActivity.this, "Connection Error", "Please check your internet connection", false);
						}
					}
					else{
						Toast.makeText(EventUserSearchActivity.this,"Enter the text to search",Toast.LENGTH_SHORT).show();
						mEventSearchList.setVisibility(View.GONE);
				
					}
				}
				
				return false;
		   }
      });

		
				//pagination for connection search

		mEventSearchList.setOnScrollListener(new OnScrollListener() {

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
						ConnectionDetector conn = new ConnectionDetector(EventUserSearchActivity.this);
						if(conn.isConnectingToInternet()){
							new EventUserSearchLoadMoreAsyncClass().execute();
						}else{
							alert.showAlertDialog(EventUserSearchActivity.this, "Connection Error", "Please check your internet connection", false);
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

			mEventSearchList.setEnabled(false);
			addCustomUser(mArrayListEventUserSearch);
			
			  if(mSelectedUserName.length()>0){
            	  int len = mSelectedUserName.length();
            	  int len2 = mSelectedUserId.length();
            	  mSelectedUserName = mSelectedUserName.substring(0,(len-1));
            	  mSelectedUserId = mSelectedUserId.substring(0,(len2-1));
              }
                
				
				Intent intent = new Intent();
				intent.putExtra("CustomUserName",mSelectedUserName);
				intent.putExtra("CustomUserID",mSelectedUserId);
				setResult(RESULT_OK,intent);

				mEventSearchList.setEnabled(true);
				
				finish();
			
			
			return true;


		case R.id.members_exit: 
			//callAlertForExit();
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
	
	private void addCustomUser(ArrayList<EventUserSearchModel> arrayList){
		
		
		if(mArrayListEventUserSearch.size()>0){		
					
			for(int i=0;i<mArrayListEventUserSearch.size();i++){
           	 EventUserSearchModel model = arrayList.get(i);
           	 int searchCount =0;
           	 if(model.isSelected()){
           		 
           		 	if(mArrayListSelectedUsers.size()>0){
           			 
           		 		for(int j=0;j<mArrayListSelectedUsers.size();j++){
           			    EventSelectedUsersModel selectedModel = mArrayListSelectedUsers.get(j);
           		 			if(model.getUser_id().equalsIgnoreCase(selectedModel.getUserId())){
           		 				 searchCount++;
           		 			} 
           		 		  }
           		 		   if(searchCount ==0){
           		 			addSelectedUsers(model);  
           		 		   }
           		 		  
           		 		} else{
           		 		   // add new
           		 		addSelectedUsers(model);
           		 	}	
           		 
           		  	
           		}
			}
			
			
		} else{
			Toast.makeText(getApplicationContext(), "Users Not Found", Toast.LENGTH_SHORT).show();
		}
		
		if(mArrayListSelectedUsers.size()>0){
			for(int i=0;i<mArrayListSelectedUsers.size();i++){
			  EventSelectedUsersModel model = mArrayListSelectedUsers.get(i);
				mSelectedUserId = mSelectedUserId+model.getUserId()+",";
				mSelectedUserName = mSelectedUserName+model.getFirstname()+",";
			}
		}	
		
		
	}
	
	private void addSelectedUsers(EventUserSearchModel model){
		
		     EventSelectedUsersModel userSelected = new EventSelectedUsersModel();
			 userSelected.setUserId(model.getUser_id());
			 userSelected.setFirstname(model.getFirstname());
			 userSelected.setLastname(model.getLastname());
			 mArrayListSelectedUsers.add(userSelected);
		
	}
	

	private class EventAsyncClass extends AsyncTask<Void, Void, ArrayList<EventUserSearchModel>>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			// Showing progress dialog before sending http request
			pDialog = new ProgressDialog(EventUserSearchActivity.this);
			pDialog.setMessage("Loading ...");
			pDialog.setIndeterminate(true);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected ArrayList<EventUserSearchModel> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONArray connectionsResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(Util.ConnectionAPI+Util.USER_ID);
			if(jsonObjectRecived != null){

				try{
					connectionsResponse = jsonObjectRecived.getJSONArray("user_list");
				}catch(JSONException e){
					e.printStackTrace();
				}
				for(int i = 0; i< connectionsResponse.length();i++){
					EventUserSearchModel eventUserData = new EventUserSearchModel();
					JSONObject eventUserDetails;
					try{
						eventUserDetails = connectionsResponse.getJSONObject(i);
						eventUserData.setUser_id(eventUserDetails.getString("user_id"));
						eventUserData.setFirstname(eventUserDetails.getString("firstname"));
						eventUserData.setLastname(eventUserDetails.getString("lastname"));
						eventUserData.setFullname(eventUserDetails.getString("fullname"));
						eventUserData.setEmail(eventUserDetails.getString("email"));
						eventUserData.setRole(eventUserDetails.getString("role"));
						eventUserData.setStream_name(eventUserDetails.getString("stream_name"));
						eventUserData.setStream_id(eventUserDetails.getString("stream_id"));
						eventUserData.setSemester(eventUserDetails.getString("semester"));
						eventUserData.setSemester_id(eventUserDetails.getString("semester_id"));
						eventUserData.setSection_name(eventUserDetails.getString("section_name"));
						eventUserData.setSection_id(eventUserDetails.getString("section_id"));
						eventUserData.setProfile_photo(eventUserDetails.getString("profile_photo"));
						
						pagination_Date_String =eventUserDetails.getString("created_date");
						mArrayListEventUserSearch.add(eventUserData);
					}catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return mArrayListEventUserSearch;
			}else{
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<EventUserSearchModel> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if(result == null){
			}else if(result.size()==0){
				
			}else{
				event_user_adapter = new EventUserSearchAdapter(EventUserSearchActivity.this,result);
				mEventSearchList.setAdapter(event_user_adapter);
				if(result.size() < 40){
					flag_loading = true;
				}else{
					flag_loading = false;
				}
			}

		}

	}
	private class  EventUserSearchAsyncClass extends AsyncTask<Void, Void, ArrayList<EventUserSearchModel>>{
       private String status;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			// Showing progress dialog before sending http request
			pDialog = new ProgressDialog(EventUserSearchActivity.this);
			pDialog.setMessage("Loading ...");
			pDialog.setIndeterminate(true);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected ArrayList<EventUserSearchModel> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONArray connectionsResponse = null;
			JSONObject jsonObjectRecived = null;
			
			if(Util.EVENT_UPDATE_FLAG ==1){
				jsonObjectRecived = HttpGetClient.sendHttpPost(EVENT_USER_SEARCH_API+conn_search.getText().toString().toLowerCase(Locale.getDefault()).trim()+"&event_id="+mEventId);
			} else{
				jsonObjectRecived = HttpGetClient.sendHttpPost(EVENT_USER_SEARCH_API+conn_search.getText().toString().toLowerCase(Locale.getDefault()).trim() );
			}
			if(jsonObjectRecived != null ){

				try{
					status = jsonObjectRecived.getString("status");
					if(jsonObjectRecived.getString("status").equalsIgnoreCase("1")){
					connectionsResponse = jsonObjectRecived.getJSONArray("user_list");
				
						if(connectionsResponse.length()>0){
							for(int i = 0; i< connectionsResponse.length();i++){
								EventUserSearchModel eventUserData = new EventUserSearchModel();
								JSONObject eventUserDetails;
					
						eventUserDetails = connectionsResponse.getJSONObject(i);
					
					
						eventUserData.setUser_id(eventUserDetails.getString("user_id"));
						eventUserData.setFirstname(eventUserDetails.getString("firstname"));
						eventUserData.setLastname(eventUserDetails.getString("lastname"));
						eventUserData.setFullname(eventUserDetails.getString("fullname"));
						eventUserData.setEmail(eventUserDetails.getString("email"));
						eventUserData.setRole(eventUserDetails.getString("role"));
						eventUserData.setStream_name(eventUserDetails.getString("stream_name"));
						eventUserData.setStream_id(eventUserDetails.getString("stream_id"));
						eventUserData.setSemester(eventUserDetails.getString("semester"));
						eventUserData.setSemester_id(eventUserDetails.getString("semester_id"));
						eventUserData.setSection_name(eventUserDetails.getString("section_name"));
						eventUserData.setSection_id(eventUserDetails.getString("section_id"));
						eventUserData.setProfile_photo(eventUserDetails.getString("profile_photo"));
						
						pagination_Date_String =eventUserDetails.getString("created_date");
						if(mArrayListSelectedUsers.size()>0){
						for(int m=0;m<mArrayListSelectedUsers.size();m++){
						      EventSelectedUsersModel model = mArrayListSelectedUsers.get(m);
						      if(model.getUserId().equalsIgnoreCase(eventUserData.getUser_id())){
						    	  	eventUserData.setSelected(true);
						     }
						 }
						}	
						mArrayListEventUserSearch.add(eventUserData);
						
				 }
				 return mArrayListEventUserSearch;		
				}else {
					return null;
				}
				
			}else{
				return null;
			}
		} catch(JSONException e){
			
		}
		  return mArrayListEventUserSearch;		
		 }else{
			 return null;
		 }
		}

		@Override
		protected void onPostExecute(ArrayList<EventUserSearchModel> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if(status.equalsIgnoreCase("0")){
				mEventSearchList.setVisibility(View.GONE);
				Toast.makeText(EventUserSearchActivity.this, "Users Not found",Toast.LENGTH_LONG).show();
			}else {
			if(result == null){
			}else if(result.size()==0){
				   
			}else{
				
				if(event_user_adapter!=null){
				event_user_adapter.notifyDataSetInvalidated();
				}
				event_user_adapter = new EventUserSearchAdapter(EventUserSearchActivity.this, result);
				mEventSearchList.setAdapter(event_user_adapter);
				mEventSearchList.setVisibility(View.VISIBLE);
				if(result.size() < 40){
					flag_loading = true;
				}else{
					flag_loading = false;
				}
			}
		 }
		}




	}
	//load more for connection in messages
	private class EventUserSearchLoadMoreAsyncClass extends AsyncTask<Void, Void, ArrayList<EventUserSearchModel>>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			// Showing progress dialog before sending http request
			if(pDialog == null){
				pDialog = Util.createProgressDialog(EventUserSearchActivity.this);
				pDialog.setCancelable(false);
				pDialog.show();}
			else{
				pDialog.setCancelable(false);
				pDialog.show();
			}}

		@Override
		protected ArrayList<EventUserSearchModel> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONArray connectionsResponse = null;
			JSONObject jsonObjectRecived=null;
			//	JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(Util.ConnectionAPI+Util.USER_ID +"&last_date=" + pagination_Date_String.replaceAll(" ", "%20"));
			if(searchPaginationFlag==false)
			{
				jsonObjectRecived = HttpGetClient.sendHttpPost(EVENT_USER_SEARCH_API+conn_search.getText().toString().toLowerCase(Locale.getDefault()).trim()+"&last_date=" + pagination_Date_String.replaceAll(" ", "%20"));
			}
			else
			{
				// url for the pagination in search
				jsonObjectRecived = HttpGetClient.sendHttpPost(EVENT_USER_SEARCH_API+conn_search.getText().toString().toLowerCase(Locale.getDefault()).trim() + "&last_date=" + pagination_Date_String.replaceAll(" ", "%20"));
			}
			
			if(jsonObjectRecived != null ) 
			{
				try{
					if(jsonObjectRecived.getString("status").equalsIgnoreCase("1")){
                          connectionsResponse = jsonObjectRecived.getJSONArray("user_list");
				    if(connectionsResponse.length() > 0){
					for(int i = 0; i< connectionsResponse.length();i++){
						EventUserSearchModel eventUserData = new EventUserSearchModel();
						JSONObject eventUserDetails;
						
							eventUserDetails = connectionsResponse.getJSONObject(i);
							eventUserData.setUser_id(eventUserDetails.getString("user_id"));
							eventUserData.setFirstname(eventUserDetails.getString("firstname"));
							eventUserData.setCreated_date(eventUserDetails.getString("created_date"));
							eventUserData.setProfile_photo(eventUserDetails.getString("profile_photo"));
							
							pagination_Date_String = eventUserDetails.getString("created_date");
							
							if(mArrayListSelectedUsers.size()>0){
								for(int m=0;m<mArrayListSelectedUsers.size();m++){
								      EventSelectedUsersModel model = mArrayListSelectedUsers.get(m);
								      if(model.getUserId().equalsIgnoreCase(eventUserData.getUser_id())){
								    	  
								    	  eventUserData.setSelected(true);
								    	  
								    	  
								    	  
								    	   
								      }
								 }
								}
							mArrayListEventUserSearch.add(eventUserData);
						
					}
					return mArrayListEventUserSearch;
				}else{
					return null;
				}
				   
			} else{
				return null;
			} 
			} catch(JSONException e){}
				return mArrayListEventUserSearch;
			} else {
				return null;
			}
			
		}
		
		

		@Override
		protected void onPostExecute(ArrayList<EventUserSearchModel> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if(result == null){
				if(event_user_adapter.isEmpty()){
					//NoMembers.setVisibility(View.VISIBLE);
					flag_loading = false;
				}
				flag_loading = true;
				Toast.makeText(EventUserSearchActivity.this, "No More Users to Load", Toast.LENGTH_SHORT).show();
			}else if(result.size()==0){
				if(event_user_adapter.isEmpty()){
					//NoMembers.setVisibility(View.VISIBLE);
				}
				flag_loading = true;
				Toast.makeText(EventUserSearchActivity.this, "No More Users to Load", Toast.LENGTH_SHORT).show();
				
			}    else{
				event_user_adapter = new EventUserSearchAdapter(EventUserSearchActivity.this, result);
				event_user_adapter.notifyDataSetChanged();
				flag_loading = false;
				pageCount = pageCount +40;
				mEventSearchList.setSelection(pageCount);
			}

		}


	}

}
