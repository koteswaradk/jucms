package com.nxgenminds.eduminds.ju.cms.jgigroup;

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
import com.nxgenminds.eduminds.ju.cms.events.EventDetailActivtiy;
import com.nxgenminds.eduminds.ju.cms.events.EventListArchiveAdapter;
import com.nxgenminds.eduminds.ju.cms.events.EventListGetModel;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class GroupFragmentEventArchived extends Fragment {

	private SwipeRefreshLayout swipeLayout;
	private ListView eventsArchivedListview;
	private TextView noEvents;
	private ProgressDialog pDialog;
	
	private static String GroupEventsAPI = Util.API+"event?org_event=1&type=archived";
	private ArrayList<EventListGetModel> eventssearch = new ArrayList<EventListGetModel>();
	private EventListArchiveAdapter adapter;
	
	private static int pageCount = 0;
	private static String pagination_Date_String;
	private boolean flag_loading = false;
	private boolean flag_refresh = false;
	private static String refresh_Date_String;
	public static final int group_archived=444;
	
	AlertDialogManager alert = new AlertDialogManager();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.events_current, container, false);
		
		swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container_events);
		eventsArchivedListview = (ListView) rootView.findViewById(R.id.eventscurrentlistview);
		noEvents = (TextView) rootView.findViewById(R.id.Noevents);
		
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		ConnectionDetector conn = new ConnectionDetector(getActivity());
		if(conn.isConnectingToInternet()){
			new ArchivedEventsAsyncClass().execute();
		}else{
			alert.showAlertDialog(getActivity(), "Connection Error", "Please check your internet connection", false);
		}

		eventsArchivedListview.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
				boolean enable = false;
				if(eventsArchivedListview != null && eventsArchivedListview.getChildCount() > 0){
					boolean firstItemVisible = eventsArchivedListview.getFirstVisiblePosition() == 0;
					boolean topOfFirstItemVisible = eventsArchivedListview.getChildAt(0).getTop() == 0;
					enable = firstItemVisible && topOfFirstItemVisible;
				}
				swipeLayout.setEnabled(enable);	


				if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0)
				{
					if(flag_loading == false)
					{
						flag_loading = true;
						ConnectionDetector conn = new ConnectionDetector(getActivity());
						if(conn.isConnectingToInternet()){
							new ArchivedEventsLoadMoreAsyncClass().execute();
						}else{
							alert.showAlertDialog(getActivity(), "Connection Error", "Please check your internet connection", false);
						}

					}
				}

			}
		});

		swipeLayout.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				swipeLayout.setRefreshing(true);
				flag_refresh = false;
				ConnectionDetector conn = new ConnectionDetector(getActivity());
				if(conn.isConnectingToInternet()){
					if(eventssearch.size() > 0){
						new ArchivedEventsRefreshAsyncClass().execute();
					}else{
						swipeLayout.setRefreshing(false);
					}
				}else{
					alert.showAlertDialog(getActivity(), "Connection Error", "Please check your internet connection", false);
				}
			}
		});

		eventsArchivedListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Object object = eventsArchivedListview.getItemAtPosition(position);
				EventListGetModel event_data = (EventListGetModel)  object;
				Intent intent = new Intent(getActivity(),EventDetailActivtiy.class);
				intent.putExtra("Archive","Archive");	
				intent.putExtra("EventId", event_data.getEvent_id());
				intent.putExtra("LoginUserFeedback",event_data.getLogin_user_feedback());
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
				intent.putExtra("EventLoginUserParticating", event_data.getLogin_user_participating());
				intent.putExtra("EventPrivacy", event_data.getEvent_privacy());
				intent.putExtra("EventThemeId", event_data.getEvent_theme_id());
				intent.putExtra("EventCreatedUser",event_data.getFirstname());
				startActivityForResult(intent, group_archived);

			}
		});

	}


	private class ArchivedEventsAsyncClass extends AsyncTask<Void, Void, ArrayList<EventListGetModel>>{
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
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(GroupEventsAPI);
			if(jsonObjectRecived != null){

				try{
					connectionsResponse = jsonObjectRecived.getJSONArray("events");
				}catch(JSONException e){
					e.printStackTrace();
				}
				for(int i = 0; i< connectionsResponse.length();i++){
					EventListGetModel currentEventsData = new EventListGetModel();
					JSONObject eventsDetails;
					try{
						eventsDetails = connectionsResponse.getJSONObject(i);
						currentEventsData.setLogin_user_feedback(eventsDetails.getString("login_user_feedback"));
						currentEventsData.setEvent_theme_id(eventsDetails.getString("event_theme_id"));
						currentEventsData.setParticipants(eventsDetails.getString("participants"));
						currentEventsData.setCreated_by(eventsDetails.getString("created_by"));
						currentEventsData.setEvent_id(eventsDetails.getString("event_id"));
						currentEventsData.setFirstname(eventsDetails.getString("firstname"));
						currentEventsData.setLastname(eventsDetails.getString("lastname"));
						currentEventsData.setEmail(eventsDetails.getString("email"));
						currentEventsData.setEvent_photo_path(eventsDetails.getString("event_photo_path"));
						currentEventsData.setUser_profile_picture(eventsDetails.getString("user_profile_picture"));
						currentEventsData.setCreated_date(eventsDetails.getString("created_date"));
						pagination_Date_String = eventsDetails.getString("created_date");
						if(!flag_refresh){
							flag_refresh = true;
							refresh_Date_String = eventsDetails.getString("created_date");
						}
						currentEventsData.setEvent_name(eventsDetails.getString("event_name"));
						currentEventsData.setEvent_desc(eventsDetails.getString("event_desc"));
						currentEventsData.setEvent_latitude(eventsDetails.getString("event_latitude"));
						currentEventsData.setEvent_longitude(eventsDetails.getString("event_longitude"));
						currentEventsData.setEvent_altitude(eventsDetails.getString("event_altitude"));
						currentEventsData.setEvent_start_date(eventsDetails.getString("event_start_date"));
						currentEventsData.setEvent_start_time(eventsDetails.getString("event_start_time"));
						currentEventsData.setCity_name(eventsDetails.getString("city_name"));
						currentEventsData.setEvent_end_date(eventsDetails.getString("event_end_date"));
						currentEventsData.setEvent_end_time(eventsDetails.getString("event_end_time"));
						currentEventsData.setEvent_privacy(eventsDetails.getString("event_privacy"));
						currentEventsData.setEvent_type_name(eventsDetails.getString("event_type_name"));
						currentEventsData.setLogin_user_participating(eventsDetails.getString("login_user_participating"));
						currentEventsData.setEvent_photo_path_thumb1(eventsDetails.getString("event_photo_path_thumb1"));
						currentEventsData.setEvent_photo_path_thumb2(eventsDetails.getString("event_photo_path_thumb2"));
						currentEventsData.setEvent_photo_path_thumb3(eventsDetails.getString("event_photo_path_thumb3"));
						currentEventsData.setEvent_type_name(eventsDetails.getString("event_type_name"));
						currentEventsData.setPublic(eventsDetails.getString("public"));
						currentEventsData.setCustom(eventsDetails.getString("custom"));
						currentEventsData.setOrg_event(eventsDetails.getString("org_event"));
						currentEventsData.setLocation_name(eventsDetails.getString("location_name"));
						

						eventssearch.add(currentEventsData);
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
				eventsArchivedListview.setAdapter(adapter);
				if(result.size() < 10){
					flag_loading = true;
				}else{
					flag_loading = false;
				}
			}
		}
	}

	private class ArchivedEventsLoadMoreAsyncClass extends AsyncTask<Void, Void, ArrayList<EventListGetModel>>{
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
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(GroupEventsAPI +"&last_date="+ pagination_Date_String.replaceAll(" ", "%20"));
			if(jsonObjectRecived != null){

				try{
					connectionsResponse = jsonObjectRecived.getJSONArray("events");
				}catch(JSONException e){
					e.printStackTrace();
				}
				if(connectionsResponse.length() > 0){
					for(int i = 0; i< connectionsResponse.length();i++){
						EventListGetModel currentEventsData = new EventListGetModel();
						JSONObject eventsDetails;
						try{
							eventsDetails = connectionsResponse.getJSONObject(i);
							currentEventsData.setLogin_user_feedback(eventsDetails.getString("login_user_feedback"));
							currentEventsData.setEvent_theme_id(eventsDetails.getString("event_theme_id"));
							currentEventsData.setParticipants(eventsDetails.getString("participants"));
							currentEventsData.setCreated_by(eventsDetails.getString("created_by"));
							currentEventsData.setEvent_id(eventsDetails.getString("event_id"));
							currentEventsData.setFirstname(eventsDetails.getString("firstname"));
							currentEventsData.setLastname(eventsDetails.getString("lastname"));
							currentEventsData.setEmail(eventsDetails.getString("email"));
							currentEventsData.setEvent_photo_path(eventsDetails.getString("event_photo_path"));
							currentEventsData.setUser_profile_picture(eventsDetails.getString("user_profile_picture"));
							currentEventsData.setCreated_date(eventsDetails.getString("created_date"));
							pagination_Date_String = eventsDetails.getString("created_date");
							currentEventsData.setEvent_name(eventsDetails.getString("event_name"));
							currentEventsData.setEvent_desc(eventsDetails.getString("event_desc"));
							currentEventsData.setEvent_latitude(eventsDetails.getString("event_latitude"));
							currentEventsData.setEvent_longitude(eventsDetails.getString("event_longitude"));
							currentEventsData.setEvent_altitude(eventsDetails.getString("event_altitude"));
							currentEventsData.setEvent_start_date(eventsDetails.getString("event_start_date"));
							currentEventsData.setEvent_start_time(eventsDetails.getString("event_start_time"));
							currentEventsData.setCity_name(eventsDetails.getString("city_name"));
							currentEventsData.setEvent_end_date(eventsDetails.getString("event_end_date"));
							currentEventsData.setEvent_end_time(eventsDetails.getString("event_end_time"));
							currentEventsData.setEvent_privacy(eventsDetails.getString("event_privacy"));
							currentEventsData.setLogin_user_participating(eventsDetails.getString("login_user_participating"));
							currentEventsData.setEvent_photo_path_thumb1(eventsDetails.getString("event_photo_path_thumb1"));
							currentEventsData.setEvent_photo_path_thumb2(eventsDetails.getString("event_photo_path_thumb2"));
							currentEventsData.setEvent_photo_path_thumb3(eventsDetails.getString("event_photo_path_thumb3"));
							currentEventsData.setEvent_type_name(eventsDetails.getString("event_type_name"));
							currentEventsData.setPublic(eventsDetails.getString("public"));
							currentEventsData.setCustom(eventsDetails.getString("custom"));
							currentEventsData.setOrg_event(eventsDetails.getString("org_event"));
							currentEventsData.setLocation_name(eventsDetails.getString("location_name"));

							eventssearch.add(currentEventsData);
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
				eventsArchivedListview.setSelection(pageCount);
			}
		}
	}

	private class ArchivedEventsRefreshAsyncClass extends AsyncTask<Void, Void, ArrayList<EventListGetModel>>{
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
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(GroupEventsAPI +"&first_date="+ refresh_Date_String.replaceAll(" ", "%20"));
			if(jsonObjectRecived != null){

				try{
					connectionsResponse = jsonObjectRecived.getJSONArray("events");
				}catch(JSONException e){
					e.printStackTrace();
				}
				if(connectionsResponse.length() > 0){
					for(int i = 0; i< connectionsResponse.length();i++){
						EventListGetModel currentEventsData = new EventListGetModel();
						JSONObject eventsDetails;
						try{
							eventsDetails = connectionsResponse.getJSONObject(i);
							currentEventsData.setLogin_user_feedback(eventsDetails.getString("login_user_feedback"));
							currentEventsData.setEvent_theme_id(eventsDetails.getString("event_theme_id"));
							currentEventsData.setParticipants(eventsDetails.getString("participants"));
							currentEventsData.setCreated_by(eventsDetails.getString("created_by"));
							currentEventsData.setEvent_id(eventsDetails.getString("event_id"));
							currentEventsData.setFirstname(eventsDetails.getString("firstname"));
							currentEventsData.setLastname(eventsDetails.getString("lastname"));
							currentEventsData.setEmail(eventsDetails.getString("email"));
							currentEventsData.setEvent_photo_path(eventsDetails.getString("event_photo_path"));
							currentEventsData.setUser_profile_picture(eventsDetails.getString("user_profile_picture"));
							currentEventsData.setCreated_date(eventsDetails.getString("created_date"));
							if(!flag_refresh){
								flag_refresh = true;
								refresh_Date_String = eventsDetails.getString("created_date");
							}
							currentEventsData.setEvent_name(eventsDetails.getString("event_name"));
							currentEventsData.setEvent_desc(eventsDetails.getString("event_desc"));
							currentEventsData.setEvent_latitude(eventsDetails.getString("event_latitude"));
							currentEventsData.setEvent_longitude(eventsDetails.getString("event_longitude"));
							currentEventsData.setEvent_altitude(eventsDetails.getString("event_altitude"));
							currentEventsData.setEvent_start_date(eventsDetails.getString("event_start_date"));
							currentEventsData.setEvent_start_time(eventsDetails.getString("event_start_time"));
							currentEventsData.setCity_name(eventsDetails.getString("city_name"));
							currentEventsData.setEvent_end_date(eventsDetails.getString("event_end_date"));
							currentEventsData.setEvent_end_time(eventsDetails.getString("event_end_time"));
							currentEventsData.setEvent_privacy(eventsDetails.getString("event_privacy"));
							currentEventsData.setLogin_user_participating(eventsDetails.getString("login_user_participating"));
							currentEventsData.setEvent_photo_path_thumb1(eventsDetails.getString("event_photo_path_thumb1"));
							currentEventsData.setEvent_photo_path_thumb2(eventsDetails.getString("event_photo_path_thumb2"));
							currentEventsData.setEvent_photo_path_thumb3(eventsDetails.getString("event_photo_path_thumb3"));
							currentEventsData.setEvent_type_name(eventsDetails.getString("event_type_name"));
							currentEventsData.setPublic(eventsDetails.getString("public"));
							currentEventsData.setCustom(eventsDetails.getString("custom"));
							currentEventsData.setOrg_event(eventsDetails.getString("org_event"));
							currentEventsData.setLocation_name(eventsDetails.getString("location_name"));

							eventssearch.add(0,currentEventsData);
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
			swipeLayout.setRefreshing(false);
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
			}
		}
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode != getActivity().RESULT_OK) {

			return;
		}

		if (requestCode == group_archived
				&& resultCode == Activity.RESULT_OK && null != data)  
		{
			String refresh_check=data.getStringExtra("Refresh");
			if(refresh_check.equalsIgnoreCase("true"))
			{

				eventssearch.clear();		

				ConnectionDetector conn = new ConnectionDetector(getActivity());
				if(conn.isConnectingToInternet()){ 
					new ArchivedEventsAsyncClass().execute();
				}

			}
			else
			{

			}
		}

		super.onActivityResult(requestCode, resultCode, data); 
	}


}
