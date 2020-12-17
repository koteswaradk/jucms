package com.nxgenminds.eduminds.ju.cms.events;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class UserFragmentEventArchived  extends Fragment {

	private SwipeRefreshLayout mSwipeLayout;
	private ListView mEventsArchivelistview;
	private TextView noEvents;
	
	private static String ARCHIVE_EVENTS_API = Util.API+"event?type=archived";
	private ArrayList<EventListGetModel> eventssearch = new ArrayList<EventListGetModel>();
	private ProgressDialog pDialog;
	
	private static int pageCount = 0;
	private static String pagination_Date_String;
	private boolean flag_loading = false;
	private boolean flag_refresh = false;
	private static String refresh_Date_String;
	
	private EventListArchiveAdapter adapter;
	AlertDialogManager alert = new AlertDialogManager();
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		
		
		View rootView = inflater.inflate(R.layout.events_current, container, false);
		mSwipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container_events);
		mEventsArchivelistview = (ListView) rootView.findViewById(R.id.eventscurrentlistview);
		noEvents = (TextView) rootView.findViewById(R.id.Noevents);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		eventssearch.clear();
		
		ConnectionDetector conn = new ConnectionDetector(getActivity());
		if(conn.isConnectingToInternet()){
			
			new ArchiveEventsAsyncClass().execute();
		}else{
			alert.showAlertDialog(getActivity(), "Connection Error", "Please check your internet connection", false);
		}
		
		mEventsArchivelistview.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				boolean enable = false;
				if(mEventsArchivelistview != null && mEventsArchivelistview.getChildCount() > 0){
					boolean firstItemVisible = mEventsArchivelistview.getFirstVisiblePosition() == 0;
					boolean topOfFirstItemVisible = mEventsArchivelistview.getChildAt(0).getTop() == 0;
					enable = firstItemVisible && topOfFirstItemVisible;
				}
				mSwipeLayout.setEnabled(enable);	

				if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0)
				{
					if(flag_loading == false)
					{
						flag_loading = true;
						ConnectionDetector conn = new ConnectionDetector(getActivity());
						if(conn.isConnectingToInternet()){
							new ArchiveEventsLoadMoreAsyncClass().execute();
						}else{
							alert.showAlertDialog(getActivity(), "Connection Error", "Please check your internet connection", false);
						}

					}
				}

			}
		});
		
		mSwipeLayout.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				mSwipeLayout.setRefreshing(true);
				flag_refresh = false;
				ConnectionDetector conn = new ConnectionDetector(getActivity());
				if(conn.isConnectingToInternet()){
					new ArchiveEventsRefreshAsyncClass().execute();
				}else{
					alert.showAlertDialog(getActivity(), "Connection Error", "Please check your internet connection", false);
				}

			}
		});

		/*eventscurrentlistview.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				flag_refresh = false;
				ConnectionDetector conn = new ConnectionDetector(getActivity());
				if(conn.isConnectingToInternet()){
					new CurrentEventsRefreshAsyncClass().execute();
				}else{
					alert.showAlertDialog(getActivity(), "Connection Error", "Please check your internet connection", false);
				}

			}
		});*/

		mEventsArchivelistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Object object = mEventsArchivelistview.getItemAtPosition(position);
				EventListGetModel event_data = (EventListGetModel)  object;
				Intent intent = new Intent(getActivity(),EventDetailActivtiy.class);
				intent.putExtra("Archive","Archive");
				intent.putExtra("EventId", event_data.getEvent_id());
				intent.putExtra("EventUserId", event_data.getCreated_by());
				intent.putExtra("EventName", event_data.getEvent_name());
				intent.putExtra("EventDec", event_data.getEvent_desc());
				intent.putExtra("EventPlace", event_data.getCity_name());
				intent.putExtra("EventDate", event_data.getEvent_start_date() + " " + event_data.getEvent_start_time());
				intent.putExtra("EventStartDate", event_data.getEvent_start_date() );
				intent.putExtra("EventEndDate", event_data.getEvent_end_date());
				intent.putExtra("EventStartTime", event_data.getEvent_start_time());
				intent.putExtra("EventEndTime", event_data.getEvent_end_time());
				intent.putExtra("EventImageUrl", event_data.getEvent_photo_path());
				intent.putExtra("EventUserUrl", event_data.getUser_profile_picture());
				intent.putExtra("EventParticipants", event_data.getParticipants());
				intent.putExtra("LoginUserFeedback",event_data.getLogin_user_feedback());
				intent.putExtra("EventLoginUserParticating", event_data.getLogin_user_participating());
				intent.putExtra("EventPrivacy", event_data.getEvent_privacy());
				intent.putExtra("EventThemeId", event_data.getEvent_theme_id());
				intent.putExtra("EventCreatedUser",event_data.getFirstname());
				getParentFragment().startActivityForResult(intent,Util.REQUEST_CODE_FOR_ARCHIVE_EVENTS);
			}
		});

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);	
		if(resultCode == Activity.RESULT_OK){
			
		if (requestCode == Util.REQUEST_CODE_FOR_ARCHIVE_EVENTS ||requestCode == Util.REQUEST_CODE_FOR_CREATE_EVENT 
				||requestCode == Util.REQUEST_CODE_FOR_UPDATE_EVENT && data!=null) {
			
			String refresh_check=data.getStringExtra("Refresh");
			if(refresh_check.equalsIgnoreCase("true")){
             eventssearch.clear();		
             ConnectionDetector conn = new ConnectionDetector(getActivity());
				if(conn.isConnectingToInternet()){ 
					new ArchiveEventsAsyncClass().execute();
				}
			 else {
				alert.showAlertDialog(getActivity(), "Connection Error","Check your Internet Connection", false);
			}
			}
		  }
	   }
		 
	}
	private class ArchiveEventsAsyncClass extends AsyncTask<Void, Void, ArrayList<EventListGetModel>>{
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
		protected ArrayList<EventListGetModel> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONArray connectionsResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(ARCHIVE_EVENTS_API);
			if(jsonObjectRecived != null){

				try{
					connectionsResponse = jsonObjectRecived.getJSONArray("events");
				}catch(JSONException e){
					e.printStackTrace();
				}
				for(int i = 0; i< connectionsResponse.length();i++){
					EventListGetModel archiveEventsData = new EventListGetModel();
					JSONObject eventsDetails;
					try{
						eventsDetails = connectionsResponse.getJSONObject(i);
						
						archiveEventsData.setLogin_user_feedback(eventsDetails.getString("login_user_feedback"));
						archiveEventsData.setEvent_theme_id(eventsDetails.getString("event_theme_id"));
						archiveEventsData.setParticipants(eventsDetails.getString("participants"));
						archiveEventsData.setCreated_by(eventsDetails.getString("created_by"));
						archiveEventsData.setEvent_id(eventsDetails.getString("event_id"));
						archiveEventsData.setFirstname(eventsDetails.getString("firstname"));
						archiveEventsData.setLastname(eventsDetails.getString("lastname"));
						archiveEventsData.setEmail(eventsDetails.getString("email"));
						archiveEventsData.setEvent_photo_path(eventsDetails.getString("event_photo_path"));
						archiveEventsData.setUser_profile_picture(eventsDetails.getString("user_profile_picture"));
						archiveEventsData.setCreated_date(eventsDetails.getString("created_date"));
						pagination_Date_String = eventsDetails.getString("created_date");
						if(!flag_refresh){
							flag_refresh = true;
							refresh_Date_String = eventsDetails.getString("created_date");
						}
						archiveEventsData.setEvent_name(eventsDetails.getString("event_name"));
						archiveEventsData.setEvent_desc(eventsDetails.getString("event_desc"));
						archiveEventsData.setEvent_latitude(eventsDetails.getString("event_latitude"));
						archiveEventsData.setEvent_longitude(eventsDetails.getString("event_longitude"));
						archiveEventsData.setEvent_altitude(eventsDetails.getString("event_altitude"));
						archiveEventsData.setEvent_start_date(eventsDetails.getString("event_start_date"));
						archiveEventsData.setEvent_start_time(eventsDetails.getString("event_start_time"));
						archiveEventsData.setCity_name(eventsDetails.getString("city_name"));
						archiveEventsData.setEvent_end_date(eventsDetails.getString("event_end_date"));
						archiveEventsData.setEvent_end_time(eventsDetails.getString("event_end_time"));
						archiveEventsData.setEvent_privacy(eventsDetails.getString("event_privacy"));
						archiveEventsData.setLogin_user_participating(eventsDetails.getString("login_user_participating"));
						archiveEventsData.setLogin_user_feedback(eventsDetails.getString("login_user_feedback"));
						/*archiveEventsData.setUser_profile_picture_thumb1(eventsDetails.getString("user_profile_picture_thumb1"));
						archiveEventsData.setUser_profile_picture_thumb2(eventsDetails.getString("user_profile_picture_thumb2"));
						archiveEventsData.setUser_profile_picture_thumb3(eventsDetails.getString("user_profile_picture_thumb3"));*/
						archiveEventsData.setEvent_photo_path_thumb1(eventsDetails.getString("event_photo_path_thumb1"));
						archiveEventsData.setEvent_photo_path_thumb2(eventsDetails.getString("event_photo_path_thumb2"));
						archiveEventsData.setEvent_photo_path_thumb3(eventsDetails.getString("event_photo_path_thumb3"));
						archiveEventsData.setPublic(eventsDetails.getString("public"));
						archiveEventsData.setCustom(eventsDetails.getString("custom"));
						archiveEventsData.setOrg_event(eventsDetails.getString("org_event"));
						archiveEventsData.setLocation_name(eventsDetails.getString("location_name"));
						archiveEventsData.setEvent_type_name(eventsDetails.getString("event_type_name"));
						archiveEventsData.setRole(eventsDetails.getString("role"));
						eventssearch.add(archiveEventsData);
						
					}catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				return eventssearch;
			}else{
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<EventListGetModel> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			pDialog.dismiss();
			if(result == null){
				noEvents.setVisibility(View.VISIBLE);
				noEvents.setText("No Events.When you have events you'll see them here.");
			}else if(result.size()==0){
				noEvents.setVisibility(View.VISIBLE);
				noEvents.setText("No Events.When you have events you'll see them here.");
			} else{
				adapter = new EventListArchiveAdapter(getActivity(), result);
				mEventsArchivelistview.setAdapter(adapter);
				if(result.size() < 10){
					flag_loading = true;
				}else{
					flag_loading = false;
				}
			}
		}
	}

	private class ArchiveEventsLoadMoreAsyncClass extends AsyncTask<Void, Void, ArrayList<EventListGetModel>>{
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
		protected ArrayList<EventListGetModel> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONArray connectionsResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(ARCHIVE_EVENTS_API +"&last_date="+ pagination_Date_String.replaceAll(" ", "%20"));
			if(jsonObjectRecived != null){

				try{
					connectionsResponse = jsonObjectRecived.getJSONArray("events");
				}catch(JSONException e){
					e.printStackTrace();
				}
				if(connectionsResponse.length() > 0){
					for(int i = 0; i< connectionsResponse.length();i++){
						EventListGetModel archiveEventsData = new EventListGetModel();
						JSONObject eventsDetails;
						try{
							eventsDetails = connectionsResponse.getJSONObject(i);
							archiveEventsData.setLogin_user_feedback(eventsDetails.getString("login_user_feedback"));
							archiveEventsData.setEvent_theme_id(eventsDetails.getString("event_theme_id"));
							archiveEventsData.setParticipants(eventsDetails.getString("participants"));
							archiveEventsData.setCreated_by(eventsDetails.getString("created_by"));
							archiveEventsData.setEvent_id(eventsDetails.getString("event_id"));
							archiveEventsData.setFirstname(eventsDetails.getString("firstname"));
							archiveEventsData.setLastname(eventsDetails.getString("lastname"));
							archiveEventsData.setEmail(eventsDetails.getString("email"));
							archiveEventsData.setEvent_photo_path(eventsDetails.getString("event_photo_path"));
							archiveEventsData.setUser_profile_picture(eventsDetails.getString("user_profile_picture"));
							archiveEventsData.setCreated_date(eventsDetails.getString("created_date"));
							pagination_Date_String = eventsDetails.getString("created_date");
							archiveEventsData.setEvent_name(eventsDetails.getString("event_name"));
							archiveEventsData.setEvent_desc(eventsDetails.getString("event_desc"));
							archiveEventsData.setEvent_latitude(eventsDetails.getString("event_latitude"));
							archiveEventsData.setEvent_longitude(eventsDetails.getString("event_longitude"));
							archiveEventsData.setEvent_altitude(eventsDetails.getString("event_altitude"));
							archiveEventsData.setEvent_start_date(eventsDetails.getString("event_start_date"));
							archiveEventsData.setEvent_start_time(eventsDetails.getString("event_start_time"));
							archiveEventsData.setCity_name(eventsDetails.getString("city_name"));
							archiveEventsData.setEvent_end_date(eventsDetails.getString("event_end_date"));
							archiveEventsData.setEvent_end_time(eventsDetails.getString("event_end_time"));
							archiveEventsData.setEvent_privacy(eventsDetails.getString("event_privacy"));
							archiveEventsData.setLogin_user_participating(eventsDetails.getString("login_user_participating"));
							archiveEventsData.setLogin_user_feedback(eventsDetails.getString("login_user_feedback"));
							/*archiveEventsData.setUser_profile_picture_thumb1(eventsDetails.getString("user_profile_picture_thumb1"));
							archiveEventsData.setUser_profile_picture_thumb2(eventsDetails.getString("user_profile_picture_thumb2"));
							archiveEventsData.setUser_profile_picture_thumb3(eventsDetails.getString("user_profile_picture_thumb3"));
							*/archiveEventsData.setEvent_photo_path_thumb1(eventsDetails.getString("event_photo_path_thumb1"));
							archiveEventsData.setEvent_photo_path_thumb2(eventsDetails.getString("event_photo_path_thumb2"));
							archiveEventsData.setEvent_photo_path_thumb3(eventsDetails.getString("event_photo_path_thumb3"));
							archiveEventsData.setPublic(eventsDetails.getString("public"));
							archiveEventsData.setCustom(eventsDetails.getString("custom"));
							archiveEventsData.setOrg_event(eventsDetails.getString("org_event"));
							archiveEventsData.setLocation_name(eventsDetails.getString("location_name"));
							archiveEventsData.setEvent_type_name(eventsDetails.getString("event_type_name"));
							archiveEventsData.setRole(eventsDetails.getString("role"));
							eventssearch.add(archiveEventsData);
						}catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}else{
					return null;
				}
				
				return eventssearch;
			}else{
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<EventListGetModel> result) {
			// TODO Auto-generated method stub
			
			super.onPostExecute(result);
			pDialog.dismiss();
			if(result == null){
				if(adapter.isEmpty()){
					noEvents.setVisibility(View.VISIBLE);
					flag_loading = false;
				}
				flag_loading = true;
				Toast.makeText(getActivity(), "No More Events to Load", Toast.LENGTH_SHORT).show();
			}else if(result.size()==0){
				if(adapter.isEmpty()){
					noEvents.setVisibility(View.VISIBLE);
				}
				flag_loading = true;
				Toast.makeText(getActivity(), "No More Events to Load", Toast.LENGTH_SHORT).show();
			} else{
				adapter = new EventListArchiveAdapter(getActivity(), result);
				adapter.notifyDataSetChanged();
				flag_loading = false;
				pageCount = pageCount +10;
				mEventsArchivelistview.setSelection(pageCount);
			}
		}
	}

	private class ArchiveEventsRefreshAsyncClass extends AsyncTask<Void, Void, ArrayList<EventListGetModel>>{
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
		protected ArrayList<EventListGetModel> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONArray connectionsResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(ARCHIVE_EVENTS_API +"&first_date="+ refresh_Date_String.replaceAll(" ", "%20"));
			if(jsonObjectRecived != null){

				try{
					connectionsResponse = jsonObjectRecived.getJSONArray("events");
				}catch(JSONException e){
					e.printStackTrace();
				}
				if(connectionsResponse.length() > 0){
					for(int i = 0; i< connectionsResponse.length();i++){
						EventListGetModel archiveEventsData = new EventListGetModel();
						JSONObject eventsDetails;
						try{
							eventsDetails = connectionsResponse.getJSONObject(i);
							archiveEventsData.setLogin_user_feedback(eventsDetails.getString("login_user_feedback"));
							archiveEventsData.setEvent_theme_id(eventsDetails.getString("event_theme_id"));
							archiveEventsData.setParticipants(eventsDetails.getString("participants"));
							archiveEventsData.setCreated_by(eventsDetails.getString("created_by"));
							archiveEventsData.setEvent_id(eventsDetails.getString("event_id"));
							archiveEventsData.setFirstname(eventsDetails.getString("firstname"));
							archiveEventsData.setLastname(eventsDetails.getString("lastname"));
							archiveEventsData.setEmail(eventsDetails.getString("email"));
							archiveEventsData.setEvent_photo_path(eventsDetails.getString("event_photo_path"));
							archiveEventsData.setUser_profile_picture(eventsDetails.getString("user_profile_picture"));
							archiveEventsData.setCreated_date(eventsDetails.getString("created_date"));
							if(!flag_refresh){
								flag_refresh = true;
								refresh_Date_String = eventsDetails.getString("created_date");
							}
							archiveEventsData.setEvent_name(eventsDetails.getString("event_name"));
							archiveEventsData.setEvent_desc(eventsDetails.getString("event_desc"));
							archiveEventsData.setEvent_latitude(eventsDetails.getString("event_latitude"));
							archiveEventsData.setEvent_longitude(eventsDetails.getString("event_longitude"));
							archiveEventsData.setEvent_altitude(eventsDetails.getString("event_altitude"));
							archiveEventsData.setEvent_start_date(eventsDetails.getString("event_start_date"));
							archiveEventsData.setEvent_start_time(eventsDetails.getString("event_start_time"));
							archiveEventsData.setCity_name(eventsDetails.getString("city_name"));
							archiveEventsData.setEvent_end_date(eventsDetails.getString("event_end_date"));
							archiveEventsData.setEvent_end_time(eventsDetails.getString("event_end_time"));
							archiveEventsData.setEvent_privacy(eventsDetails.getString("event_privacy"));
							archiveEventsData.setLogin_user_participating(eventsDetails.getString("login_user_participating"));
							archiveEventsData.setLogin_user_feedback(eventsDetails.getString("login_user_feedback"));
							/*archiveEventsData.setUser_profile_picture_thumb1(eventsDetails.getString("user_profile_picture_thumb1"));
							archiveEventsData.setUser_profile_picture_thumb2(eventsDetails.getString("user_profile_picture_thumb2"));
							archiveEventsData.setUser_profile_picture_thumb3(eventsDetails.getString("user_profile_picture_thumb3"));
							*/archiveEventsData.setEvent_photo_path_thumb1(eventsDetails.getString("event_photo_path_thumb1"));
							archiveEventsData.setEvent_photo_path_thumb2(eventsDetails.getString("event_photo_path_thumb2"));
							archiveEventsData.setEvent_photo_path_thumb3(eventsDetails.getString("event_photo_path_thumb3"));
							archiveEventsData.setPublic(eventsDetails.getString("public"));
							archiveEventsData.setCustom(eventsDetails.getString("custom"));
							archiveEventsData.setOrg_event(eventsDetails.getString("org_event"));
							archiveEventsData.setLocation_name(eventsDetails.getString("location_name"));
							archiveEventsData.setEvent_type_name(eventsDetails.getString("event_type_name"));
							archiveEventsData.setRole(eventsDetails.getString("role"));
							eventssearch.add(0,archiveEventsData);
						}catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}else{
					return null;
				}
				
				return eventssearch;
			}else{
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<EventListGetModel> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			mSwipeLayout.setRefreshing(false);
			pDialog.dismiss();
			if(result == null){
				if(adapter.isEmpty()){
					noEvents.setVisibility(View.VISIBLE);
					flag_loading = false;
				}
				flag_loading = true;
			}else if(result.size()==0){
				if(adapter.isEmpty()){
					noEvents.setVisibility(View.VISIBLE);
				}
				flag_loading = true;
			} else{
				adapter = new EventListArchiveAdapter(getActivity(), result);
				adapter.notifyDataSetChanged();
				flag_loading = false;
			}
		}
	}
}
