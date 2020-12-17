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



public class UserFragmentEventUpComing extends Fragment{

	private SwipeRefreshLayout mSwipeLayout;
	private ListView mEventUpcomingList;
	private TextView noEvents;
	
	private static String UPCOMING_EVENT_API = Util.API+"event?type=upcoming";
	private ArrayList<EventListGetModel> eventssearch = new ArrayList<EventListGetModel>();
	
	private ProgressDialog pDialog;
	private static int pageCount = 0;
	private static String pagination_Date_String;
	
	private boolean flag_loading = false;
	private boolean flag_refresh = false;
	private static String refresh_Date_String;
	
	private EventListAdapter adapter;
	AlertDialogManager alert = new AlertDialogManager();
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		
		
		
		View rootView = inflater.inflate(R.layout.events_current, container, false);
		mSwipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container_events);
		mEventUpcomingList = (ListView) rootView.findViewById(R.id.eventscurrentlistview);
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
			new UpcomingEventsAsyncClass().execute();
		}else{
			alert.showAlertDialog(getActivity(), "Connection Error", "Please check your internet connection", false);
		}

		mEventUpcomingList.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

				boolean enable = false;
				if(mEventUpcomingList != null && mEventUpcomingList.getChildCount() > 0){
					boolean firstItemVisible = mEventUpcomingList.getFirstVisiblePosition() == 0;
					boolean topOfFirstItemVisible = mEventUpcomingList.getChildAt(0).getTop() == 0;
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
							new UpcomingEventsLoadMoreAsyncClass().execute();
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
					new CurrentEventsRefreshAsyncClass().execute();
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


		mEventUpcomingList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Object object = mEventUpcomingList.getItemAtPosition(position);
				EventListGetModel event_data = (EventListGetModel)  object;
				Intent intent = new Intent(getActivity(),EventDetailActivtiy.class);
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
				
				getParentFragment().startActivityForResult(intent,Util.REQUEST_CODE_FOR_UPCOMING_EVENTS);

			}
		});

	}


	private class UpcomingEventsAsyncClass extends AsyncTask<Void, Void, ArrayList<EventListGetModel>>{
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
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(UPCOMING_EVENT_API);
			if(jsonObjectRecived != null){

				try{
					connectionsResponse = jsonObjectRecived.getJSONArray("events");
				}catch(JSONException e){
					e.printStackTrace();
				}
				for(int i = 0; i< connectionsResponse.length();i++){
					EventListGetModel upComingEventsData = new EventListGetModel();
					JSONObject eventsDetails;
					try{
						eventsDetails = connectionsResponse.getJSONObject(i);
						
						upComingEventsData.setLogin_user_feedback(eventsDetails.getString("login_user_feedback"));
						upComingEventsData.setEvent_theme_id(eventsDetails.getString("event_theme_id"));
						upComingEventsData.setParticipants(eventsDetails.getString("participants"));
						upComingEventsData.setCreated_by(eventsDetails.getString("created_by"));
						upComingEventsData.setEvent_id(eventsDetails.getString("event_id"));
						upComingEventsData.setFirstname(eventsDetails.getString("firstname"));
						upComingEventsData.setLastname(eventsDetails.getString("lastname"));
						upComingEventsData.setLocation_name(eventsDetails.getString("location_name"));
						upComingEventsData.setEmail(eventsDetails.getString("email"));
						upComingEventsData.setEvent_photo_path(eventsDetails.getString("event_photo_path"));
						upComingEventsData.setUser_profile_picture(eventsDetails.getString("user_profile_picture"));
						upComingEventsData.setCreated_date(eventsDetails.getString("created_date"));
						pagination_Date_String = eventsDetails.getString("created_date");
						if(!flag_refresh){
							flag_refresh = true;
							refresh_Date_String = eventsDetails.getString("created_date");
						}
						upComingEventsData.setEvent_name(eventsDetails.getString("event_name"));
						upComingEventsData.setEvent_desc(eventsDetails.getString("event_desc"));
						upComingEventsData.setEvent_latitude(eventsDetails.getString("event_latitude"));
						upComingEventsData.setEvent_longitude(eventsDetails.getString("event_longitude"));
						upComingEventsData.setEvent_altitude(eventsDetails.getString("event_altitude"));
						upComingEventsData.setEvent_start_date(eventsDetails.getString("event_start_date"));
						upComingEventsData.setEvent_start_time(eventsDetails.getString("event_start_time"));
						upComingEventsData.setCity_name(eventsDetails.getString("city_name"));
						upComingEventsData.setEvent_end_date(eventsDetails.getString("event_end_date"));
						upComingEventsData.setEvent_end_time(eventsDetails.getString("event_end_time"));
						upComingEventsData.setEvent_privacy(eventsDetails.getString("event_privacy"));
						upComingEventsData.setEvent_type_name(eventsDetails.getString("event_type_name"));
						upComingEventsData.setLogin_user_participating(eventsDetails.getString("login_user_participating"));
						upComingEventsData.setLogin_user_feedback(eventsDetails.getString("login_user_feedback"));
						upComingEventsData.setEvent_photo_path_thumb1(eventsDetails.getString("event_photo_path_thumb1"));
						upComingEventsData.setEvent_photo_path_thumb2(eventsDetails.getString("event_photo_path_thumb2"));
						upComingEventsData.setEvent_photo_path_thumb3(eventsDetails.getString("event_photo_path_thumb3"));
						upComingEventsData.setPublic(eventsDetails.getString("public"));
						upComingEventsData.setCustom(eventsDetails.getString("custom"));
						upComingEventsData.setOrg_event(eventsDetails.getString("org_event"));
						upComingEventsData.setEvent_type_name(eventsDetails.getString("event_type_name"));
						upComingEventsData.setRole(eventsDetails.getString("role"));
						eventssearch.add(upComingEventsData);
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
				
				System.out.println("Result size is:"+ result.size());
				adapter = new EventListAdapter(getActivity(), result);
				mEventUpcomingList.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				if(result.size() < 10){
					flag_loading = true;
				}else{
					flag_loading = false;
				}
			}
		}
	}

	private class UpcomingEventsLoadMoreAsyncClass extends AsyncTask<Void, Void, ArrayList<EventListGetModel>>{
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
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(UPCOMING_EVENT_API +"&last_date="+ pagination_Date_String.replaceAll(" ", "%20"));
			if(jsonObjectRecived != null){

				try{
					connectionsResponse = jsonObjectRecived.getJSONArray("events");
				}catch(JSONException e){
					e.printStackTrace();
				}
				if(connectionsResponse.length() > 0){
					for(int i = 0; i< connectionsResponse.length();i++){
						EventListGetModel upComingEventsData = new EventListGetModel();
						JSONObject eventsDetails;
						try{
							eventsDetails = connectionsResponse.getJSONObject(i);
							upComingEventsData.setLogin_user_feedback(eventsDetails.getString("login_user_feedback"));
							upComingEventsData.setEvent_theme_id(eventsDetails.getString("event_theme_id"));
							upComingEventsData.setParticipants(eventsDetails.getString("participants"));
							upComingEventsData.setCreated_by(eventsDetails.getString("created_by"));
							upComingEventsData.setEvent_id(eventsDetails.getString("event_id"));
							upComingEventsData.setFirstname(eventsDetails.getString("firstname"));
							upComingEventsData.setLastname(eventsDetails.getString("lastname"));
							upComingEventsData.setEmail(eventsDetails.getString("email"));
							upComingEventsData.setEvent_photo_path(eventsDetails.getString("event_photo_path"));
							upComingEventsData.setUser_profile_picture(eventsDetails.getString("user_profile_picture"));
							upComingEventsData.setCreated_date(eventsDetails.getString("created_date"));
							pagination_Date_String = eventsDetails.getString("created_date");
							upComingEventsData.setEvent_name(eventsDetails.getString("event_name"));
							upComingEventsData.setEvent_desc(eventsDetails.getString("event_desc"));
							upComingEventsData.setEvent_latitude(eventsDetails.getString("event_latitude"));
							upComingEventsData.setEvent_longitude(eventsDetails.getString("event_longitude"));
							upComingEventsData.setEvent_altitude(eventsDetails.getString("event_altitude"));
							upComingEventsData.setEvent_start_date(eventsDetails.getString("event_start_date"));
							upComingEventsData.setEvent_start_time(eventsDetails.getString("event_start_time"));
							upComingEventsData.setCity_name(eventsDetails.getString("city_name"));
							upComingEventsData.setEvent_end_date(eventsDetails.getString("event_end_date"));
							upComingEventsData.setEvent_end_time(eventsDetails.getString("event_end_time"));
							upComingEventsData.setEvent_privacy(eventsDetails.getString("event_privacy"));
							upComingEventsData.setLogin_user_participating(eventsDetails.getString("login_user_participating"));
							upComingEventsData.setLogin_user_feedback(eventsDetails.getString("login_user_feedback"));
							upComingEventsData.setEvent_photo_path_thumb1(eventsDetails.getString("event_photo_path_thumb1"));
							upComingEventsData.setEvent_photo_path_thumb2(eventsDetails.getString("event_photo_path_thumb2"));
							upComingEventsData.setEvent_photo_path_thumb3(eventsDetails.getString("event_photo_path_thumb3"));
							upComingEventsData.setPublic(eventsDetails.getString("public"));
							upComingEventsData.setCustom(eventsDetails.getString("custom"));
							upComingEventsData.setOrg_event(eventsDetails.getString("org_event"));
							upComingEventsData.setEvent_type_name(eventsDetails.getString("event_type_name"));
							upComingEventsData.setRole(eventsDetails.getString("role"));
							eventssearch.add(upComingEventsData);
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
				Toast.makeText(getActivity(), "End of event list.", Toast.LENGTH_SHORT).show();
			}else if(result.size()==0){
				if(adapter.isEmpty()){
					noEvents.setVisibility(View.VISIBLE);
				}
				flag_loading = true;
				Toast.makeText(getActivity(), "End of event list.", Toast.LENGTH_SHORT).show();
			} else{
				adapter = new EventListAdapter(getActivity(), result);
				adapter.notifyDataSetChanged();
				flag_loading = false;
				pageCount = pageCount +10;
				mEventUpcomingList.setSelection(pageCount);
			}
		}
	}

	private class CurrentEventsRefreshAsyncClass extends AsyncTask<Void, Void, ArrayList<EventListGetModel>>{
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
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(UPCOMING_EVENT_API +"&first_date="+ refresh_Date_String.replaceAll(" ", "%20"));
			if(jsonObjectRecived != null){

				try{
					connectionsResponse = jsonObjectRecived.getJSONArray("events");
				}catch(JSONException e){
					e.printStackTrace();
				}
				if(connectionsResponse.length() > 0){
					for(int i = 0; i< connectionsResponse.length();i++){
						EventListGetModel upComingEventsData = new EventListGetModel();
						JSONObject eventsDetails;
						try{
							eventsDetails = connectionsResponse.getJSONObject(i);
							upComingEventsData.setLogin_user_feedback(eventsDetails.getString("login_user_feedback"));
							upComingEventsData.setEvent_theme_id(eventsDetails.getString("event_theme_id"));
							upComingEventsData.setParticipants(eventsDetails.getString("participants"));
							upComingEventsData.setCreated_by(eventsDetails.getString("created_by"));
							upComingEventsData.setEvent_id(eventsDetails.getString("event_id"));
							upComingEventsData.setFirstname(eventsDetails.getString("firstname"));
							upComingEventsData.setLastname(eventsDetails.getString("lastname"));
							upComingEventsData.setEmail(eventsDetails.getString("email"));
							upComingEventsData.setEvent_photo_path(eventsDetails.getString("event_photo_path"));
							upComingEventsData.setUser_profile_picture(eventsDetails.getString("user_profile_picture"));
							upComingEventsData.setCreated_date(eventsDetails.getString("created_date"));
							if(!flag_refresh){
								flag_refresh = true;
								refresh_Date_String = eventsDetails.getString("created_date");
							}
							upComingEventsData.setEvent_name(eventsDetails.getString("event_name"));
							upComingEventsData.setEvent_desc(eventsDetails.getString("event_desc"));
							upComingEventsData.setEvent_latitude(eventsDetails.getString("event_latitude"));
							upComingEventsData.setEvent_longitude(eventsDetails.getString("event_longitude"));
							upComingEventsData.setEvent_altitude(eventsDetails.getString("event_altitude"));
							upComingEventsData.setEvent_start_date(eventsDetails.getString("event_start_date"));
							upComingEventsData.setEvent_start_time(eventsDetails.getString("event_start_time"));
							upComingEventsData.setCity_name(eventsDetails.getString("city_name"));
							upComingEventsData.setEvent_end_date(eventsDetails.getString("event_end_date"));
							upComingEventsData.setEvent_end_time(eventsDetails.getString("event_end_time"));
							upComingEventsData.setEvent_privacy(eventsDetails.getString("event_privacy"));
							upComingEventsData.setLogin_user_participating(eventsDetails.getString("login_user_participating"));
							upComingEventsData.setLogin_user_feedback(eventsDetails.getString("login_user_feedback"));
							upComingEventsData.setEvent_photo_path_thumb1(eventsDetails.getString("event_photo_path_thumb1"));
							upComingEventsData.setEvent_photo_path_thumb2(eventsDetails.getString("event_photo_path_thumb2"));
							upComingEventsData.setEvent_photo_path_thumb3(eventsDetails.getString("event_photo_path_thumb3"));
							upComingEventsData.setPublic(eventsDetails.getString("public"));
							upComingEventsData.setCustom(eventsDetails.getString("custom"));
							upComingEventsData.setOrg_event(eventsDetails.getString("org_event"));
							upComingEventsData.setEvent_type_name(eventsDetails.getString("event_type_name"));
							upComingEventsData.setRole(eventsDetails.getString("role"));
							eventssearch.add(0,upComingEventsData);
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
			Util.FLAG_ARCHIVE_EVENT = 1;
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
				adapter = new EventListAdapter(getActivity(), result);
				adapter.notifyDataSetChanged();
				flag_loading = false;
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);	
		if(resultCode == Activity.RESULT_OK){
			
		if (requestCode == Util.REQUEST_CODE_FOR_UPCOMING_EVENTS || requestCode == Util.REQUEST_CODE_FOR_CREATE_EVENT 
				|| requestCode == Util.REQUEST_CODE_FOR_UPDATE_EVENT && data!=null) {
			
			String refresh_check=data.getStringExtra("Refresh");
			if(refresh_check.equalsIgnoreCase("true")){
             eventssearch.clear();		
             ConnectionDetector conn = new ConnectionDetector(getActivity());
				if(conn.isConnectingToInternet()){ 
					System.out.println("In User UpComing Async on activity result:");
					new UpcomingEventsAsyncClass().execute();
				}
			 else {
				alert.showAlertDialog(getActivity(), "Connection Error","Check your Internet Connection", false);
			}
			}	
		 }
	   }
	}
 }
