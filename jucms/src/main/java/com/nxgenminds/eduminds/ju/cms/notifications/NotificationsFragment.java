package com.nxgenminds.eduminds.ju.cms.notifications;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.adapters.NotificationAdapter;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.models.NotificationsModel;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpPostClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;



public class NotificationsFragment extends Fragment {

	private ListView notificationsListView;
	private SwipeRefreshLayout swipeLayout;
	JSONArray notificationsResponse;
	private TextView noNotifications;
	private static int pageCount = 0;
	private String NotificationURL = Util.API + "notification";
	private ArrayList<NotificationsModel> notificationSearch = new ArrayList<NotificationsModel>();
	private ProgressDialog pDialog;
	private static String pagination_Date_String;
	private boolean flag_loading = false;
	AlertDialogManager alert = new AlertDialogManager();
	private boolean flag_refresh = false;
	private static String refresh_Date_String;
	private NotificationAdapter adapter;


	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.notifications, container, false);
		notificationsListView = (ListView) rootView.findViewById(R.id.Notificationslistview);
		swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container_noti);
		noNotifications = (TextView) rootView.findViewById(R.id.noNotifications);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		notificationSearch.clear();
		pageCount = 0;
		adapter = new NotificationAdapter(getActivity(), notificationSearch);
		swipeLayout.setColorScheme(android.R.color.holo_blue_bright,android.R.color.holo_green_light, android.R.color.holo_orange_light,android.R.color.holo_red_light);
		ConnectionDetector conn = new ConnectionDetector(getActivity());
		if(conn.isConnectingToInternet()){
			new GetNotificationAsyncClass().execute();
		}else{
			alert.showAlertDialog(getActivity(), "Connection Error", "Please check your internet connection", false);
		}

		notificationsListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				boolean enable = false;
				if(notificationsListView != null && notificationsListView.getChildCount() > 0){
					boolean firstItemVisible = notificationsListView.getFirstVisiblePosition() == 0;
					boolean topOfFirstItemVisible = notificationsListView.getChildAt(0).getTop() == 0;
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
							new GetNotificationLoadMoreAsyncClass().execute();
						}else{
							//Crouton.makeText(getActivity(), getString(R.string.crouton_message), Style.ALERT).show();
						}
					}
				}
			}
		});


		notificationsListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
				AlertDialog.Builder confirmAlert = new AlertDialog.Builder(getActivity());
				confirmAlert.setTitle("Delete Notification");
				confirmAlert.setMessage("Are you sure you want to delete this notification ! ");
				confirmAlert.setCancelable(true);

				confirmAlert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Object object = notificationsListView.getItemAtPosition(position);
						
						final NotificationsModel notiData = (NotificationsModel) object;
						
						ConnectionDetector conn = new ConnectionDetector(getActivity());
			            if(conn.isConnectingToInternet()){
			            	new DeleteNotification().execute(notiData.getNotification_id());
			            	notificationSearch.remove(object);
			            	adapter.notifyDataSetChanged();
			            } else{
			            	alert.showAlertDialog(getActivity(),"Connection Error","Check your Internet Connection",false);
			            }

						
						
						//adapter = new NotificationAdapter(getActivity(), notificationSearch);
						//notificationsListView.setAdapter(adapter);
						
						dialog.dismiss();
					}
				});

				confirmAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				confirmAlert.create().show();





				return true;
			}
		});


		swipeLayout.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {

				swipeLayout.setRefreshing(true);
				flag_refresh = false;
				ConnectionDetector conn = new ConnectionDetector(getActivity());
				if(conn.isConnectingToInternet()){
					swipeLayout.setRefreshing(false);
					new GetNotificationRefreshAsyncClass().execute();
				}else{
					//Crouton.makeText(getActivity(), getString(R.string.crouton_message), Style.ALERT).show();
				}
			}
		});
	}

	private class GetNotificationAsyncClass extends AsyncTask<Void, Void, ArrayList<NotificationsModel>>{

		@Override
		protected void onPreExecute() {
			if(pDialog == null){
				pDialog = Util.createProgressDialog(getActivity());
				pDialog.setCancelable(false);
				pDialog.show();}
			else{
				pDialog.setCancelable(false);
				pDialog.show();
			}}

		@Override
		protected ArrayList<NotificationsModel> doInBackground(Void... params) {
			JSONArray connectionsResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(NotificationURL);
			if(jsonObjectRecived != null){

				try{
					connectionsResponse = jsonObjectRecived.getJSONArray("notifications");
					for(int i = 0; i< connectionsResponse.length();i++){
						NotificationsModel notificationData = new NotificationsModel();
						JSONObject notificationDetails;
						try{
							notificationDetails = connectionsResponse.getJSONObject(i);
							notificationData.setStatus(notificationDetails.getString("status"));
							notificationData.setNotification_id(notificationDetails.getString("notification_id"));
							notificationData.setNotification_type(notificationDetails.getString("notification_type"));
							notificationData.setIs_seen(notificationDetails.getString("is_seen"));
							notificationData.setSeen_on(notificationDetails.getString("seen_on"));
							notificationData.setFrom_user_id(notificationDetails.getString("from_user_id"));
							notificationData.setTable_name(notificationDetails.getString("table_name"));
							notificationData.setPk_value(notificationDetails.getString("pk_value"));
							notificationData.setCreated_by(notificationDetails.getString("created_by"));
							notificationData.setCreated_date(notificationDetails.getString("created_date"));
							notificationData.setRole(notificationDetails.getString("role"));
							pagination_Date_String = notificationDetails.getString("created_date");
							if(!flag_refresh){
								flag_refresh = true;
								refresh_Date_String = notificationDetails.getString("created_date");
							}
							notificationData.setFirstname(notificationDetails.getString("firstname"));
							notificationData.setLastname(notificationDetails.getString("lastname"));
							notificationData.setActual_photo_path(notificationDetails.getString("actual_photo_path"));
							notificationData.setNotification_text(notificationDetails.getString("notification_text"));
							notificationData.setPost_photo_path(notificationDetails.getString("post_photo_path"));
							notificationData.setPost_id(notificationDetails.getString("post_id"));
							notificationData.setPost_owner_username(notificationDetails.getString("post_owner_username"));
							notificationData.setPost_text(notificationDetails.getString("post_text"));
							notificationData.setNotification_text(notificationDetails.getString("notification_text"));
							notificationData.setPost_owner_fullname(notificationDetails.getString("post_owner_fullname"));
							notificationData.setFullname(notificationDetails.getString("fullname"));
							notificationSearch.add(notificationData);
						}catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}catch(JSONException e){
					e.printStackTrace();
				}

				return notificationSearch;
			}else{
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<NotificationsModel> result) {
			super.onPostExecute(result);
			if(swipeLayout.isRefreshing())
			{
				swipeLayout.setRefreshing(false);
			}
			pDialog.dismiss();
			if(result == null){
				noNotifications.setVisibility(View.VISIBLE);
			}else if(result.size()==0){
				noNotifications.setVisibility(View.VISIBLE);
			}        else{
				notificationsListView.setAdapter(adapter);
				if(result.size() < 20){
					flag_loading = true;
				}else{
					flag_loading = false;
				}

			}

		}

	}


	private class GetNotificationRefreshAsyncClass extends AsyncTask<Void, Void, ArrayList<NotificationsModel>>{

		@Override
		protected ArrayList<NotificationsModel> doInBackground(Void... params) {
			JSONArray connectionsResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(NotificationURL+"?first_date="+ refresh_Date_String.replaceAll(" ", "%20"));
			if(jsonObjectRecived != null){

				try{
					connectionsResponse = jsonObjectRecived.getJSONArray("notifications");
					for(int i = 0; i< connectionsResponse.length();i++){
						NotificationsModel notificationData = new NotificationsModel();
						JSONObject notificationDetails;
						try{
							notificationDetails = connectionsResponse.getJSONObject(i);
							notificationData.setStatus(notificationDetails.getString("status"));
							notificationData.setNotification_id(notificationDetails.getString("notification_id"));
							notificationData.setNotification_type(notificationDetails.getString("notification_type"));
							notificationData.setIs_seen(notificationDetails.getString("is_seen"));
							notificationData.setSeen_on(notificationDetails.getString("seen_on"));
							notificationData.setFrom_user_id(notificationDetails.getString("from_user_id"));
							notificationData.setTable_name(notificationDetails.getString("table_name"));
							notificationData.setPk_value(notificationDetails.getString("pk_value"));
							notificationData.setCreated_by(notificationDetails.getString("created_by"));
							notificationData.setCreated_date(notificationDetails.getString("created_date"));
							notificationData.setRole(notificationDetails.getString("role"));
							if(!flag_refresh){
								flag_refresh = true;
								refresh_Date_String = notificationDetails.getString("created_date");
							}
							notificationData.setFirstname(notificationDetails.getString("firstname"));
							notificationData.setLastname(notificationDetails.getString("lastname"));
							notificationData.setActual_photo_path(notificationDetails.getString("actual_photo_path"));
							notificationData.setNotification_text(notificationDetails.getString("notification_text"));
							notificationData.setPost_photo_path(notificationDetails.getString("post_photo_path"));
							notificationData.setPost_id(notificationDetails.getString("post_id"));
							notificationData.setPost_owner_username(notificationDetails.getString("post_owner_username"));
							notificationData.setPost_text(notificationDetails.getString("post_text"));
							notificationData.setNotification_text(notificationDetails.getString("notification_text"));
							notificationData.setPost_owner_fullname(notificationDetails.getString("post_owner_fullname"));
							notificationData.setFullname(notificationDetails.getString("fullname"));
							notificationSearch.add(0,notificationData);
						}catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}catch(JSONException e){
					e.printStackTrace();
				}


				return notificationSearch;
			}else{
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<NotificationsModel> result) {
			super.onPostExecute(result);
			swipeLayout.setRefreshing(false);
			if(result == null){
				//noNotifications.setVisibility(View.VISIBLE);
			}else if(result.size()==0){
				//noNotifications.setVisibility(View.VISIBLE);
			}        else{
				adapter.notifyDataSetChanged();
			}

		}
	}

	private class GetNotificationLoadMoreAsyncClass extends AsyncTask<Void, Void, ArrayList<NotificationsModel>>{

		@Override
		protected void onPreExecute() {
			if(pDialog == null){
				pDialog = Util.createProgressDialog(getActivity());
				pDialog.setCancelable(false);
				pDialog.show();}
			else{
				pDialog.setCancelable(false);
				pDialog.show();
			}}

		@Override
		protected ArrayList<NotificationsModel> doInBackground(Void... params) {
			JSONArray connectionsResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(NotificationURL+"?last_date="+ pagination_Date_String.replaceAll(" ", "%20"));
			if(jsonObjectRecived != null){

				try{
					connectionsResponse = jsonObjectRecived.getJSONArray("notifications");
				}catch(JSONException e){
					e.printStackTrace();
				}
				if(connectionsResponse.length() > 0){
					for(int i = 0; i< connectionsResponse.length();i++){
						NotificationsModel notificationData = new NotificationsModel();
						JSONObject notificationDetails;
						try{
							notificationDetails = connectionsResponse.getJSONObject(i);
							notificationData.setStatus(notificationDetails.getString("status"));
							notificationData.setNotification_id(notificationDetails.getString("notification_id"));
							notificationData.setNotification_type(notificationDetails.getString("notification_type"));
							notificationData.setIs_seen(notificationDetails.getString("is_seen"));
							notificationData.setSeen_on(notificationDetails.getString("seen_on"));
							notificationData.setFrom_user_id(notificationDetails.getString("from_user_id"));
							notificationData.setTable_name(notificationDetails.getString("table_name"));
							notificationData.setPk_value(notificationDetails.getString("pk_value"));
							notificationData.setCreated_by(notificationDetails.getString("created_by"));
							notificationData.setCreated_date(notificationDetails.getString("created_date"));
							pagination_Date_String = notificationDetails.getString("created_date");
							notificationData.setFirstname(notificationDetails.getString("firstname"));
							notificationData.setLastname(notificationDetails.getString("lastname"));
							notificationData.setActual_photo_path(notificationDetails.getString("actual_photo_path"));
							notificationData.setNotification_text(notificationDetails.getString("notification_text"));
							notificationData.setPost_photo_path(notificationDetails.getString("post_photo_path"));
							notificationData.setPost_id(notificationDetails.getString("post_id"));
							notificationData.setPost_owner_username(notificationDetails.getString("post_owner_username"));
							notificationData.setPost_text(notificationDetails.getString("post_text"));
							notificationData.setNotification_text(notificationDetails.getString("notification_text"));
							notificationData.setPost_owner_fullname(notificationDetails.getString("post_owner_fullname"));
							notificationData.setFullname(notificationDetails.getString("fullname"));
							notificationData.setRole(notificationDetails.getString("role"));
							notificationSearch.add(notificationData);
						}catch (JSONException e) {
							e.printStackTrace();
						}
					}
					return notificationSearch;
				}else{
					return null;
				}
			}else{
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<NotificationsModel> result) {
			super.onPostExecute(result);
			pDialog.dismiss();
			if(result == null){
				//noNotifications.setVisibility(View.VISIBLE);

				flag_loading = true;
			}else if(result.size()==0){
				//noNotifications.setVisibility(View.VISIBLE);

				flag_loading = true;
			}      else{
				adapter.notifyDataSetChanged();
				flag_loading = false;
				pageCount = pageCount +20;
				notificationsListView.setSelection(pageCount);

			}

		}

	}


	private class DeleteNotification extends AsyncTask<String, Void, JSONObject>{

		@Override
		protected void onPreExecute() {
			if(pDialog == null){
				pDialog = Util.createProgressDialog(getActivity());
				pDialog.setCancelable(false);
				pDialog.show();}
			else{
				pDialog.setCancelable(false);
				pDialog.show();
			}}


		@Override
		protected JSONObject doInBackground(String... params) {
			JSONArray membersResponse = null;
			JSONObject send = new JSONObject();
			String NotificationId = params[0];
			try {
				send.put("notification_id", NotificationId);

			} catch (JSONException e) {
				e.printStackTrace();
			}
			JSONObject jsonObjectRecived = HttpPostClient.sendHttpPost(Util.API+"delete_notification",send);
			return jsonObjectRecived;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			pDialog.dismiss();
			try {
				if(result.getString("status").equalsIgnoreCase("1"))
				{

					Toast.makeText(getActivity(), "Notification deleted succesfully !", Toast.LENGTH_SHORT).show();

				}
				else
				{
					Toast.makeText(getActivity(), "Something went wrong !", Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}}


