package com.nxgenminds.eduminds.ju.cms.FeedbackAdmin;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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


public class FeedBackAdminFragment extends Fragment{
	
	private SwipeRefreshLayout mSwipeLayout;
	private ListView mFeedBackList;
	private TextView noFeedBack;
	
	private static String FEEDBACK_LIST_API = Util.API+"feedbackList";
	private ArrayList<FeedBackListModel> mArrayListFeedBack = new ArrayList<FeedBackListModel>();
	
	private ProgressDialog mPDialog;
	private static int pageCount = 0;
	private static String pagination_Date_String;
	
	private boolean flag_loading = false;
	private boolean flag_refresh = false;
	private static String refresh_Date_String;
	
	private FeedBackListAdminAdapter mFeedBackAdapter;
	AlertDialogManager alert = new AlertDialogManager();
	

	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,  Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_feedback_admin, container , false);
		
	    mSwipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container_feedback_admin);
		mFeedBackList = (ListView) rootView.findViewById(R.id.fragment_feedback_list_admin);
		noFeedBack = (TextView) rootView.findViewById(R.id.NoFeedBackList);
		
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		mArrayListFeedBack.clear();
		
	    ConnectionDetector conn = new ConnectionDetector(getActivity());
		if(conn.isConnectingToInternet()){
			new GetFeedBackList().execute();
		}else{
			alert.showAlertDialog(getActivity(), "Connection Error", "Please check your internet connection", false);
		}

		mFeedBackList.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

				boolean enable = false;
				if(mFeedBackList!= null && mFeedBackList.getChildCount() > 0){
					boolean firstItemVisible = mFeedBackList.getFirstVisiblePosition() == 0;
					boolean topOfFirstItemVisible = mFeedBackList.getChildAt(0).getTop() == 0;
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
							new GetFeedBackListLoadMoreAsyncClass().execute();
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
					new GetFeedBackListRefreshAsyncClass().execute();
				}else{
					alert.showAlertDialog(getActivity(), "Connection Error", "Please check your internet connection", false);
				}
			}
		});

		mFeedBackList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
				
				FeedBackListModel model = mArrayListFeedBack.get(position);	
				Intent feedBackFilterIntent = new Intent(getActivity(),FeedBackAdminFilterActivity.class);
				feedBackFilterIntent.putExtra("event_id",model.getEvent_id());
				feedBackFilterIntent.putExtra("is_public",model.getIs_public());
				startActivity(feedBackFilterIntent);
			}
    	  
		});
      
      
	  }
	
	private class GetFeedBackList extends AsyncTask<Void, Void, ArrayList<FeedBackListModel>>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			// Showing progress dialog before sending http request
			if(mPDialog == null){
				mPDialog = Util.createProgressDialog(getActivity());
				mPDialog.setCancelable(false);
				mPDialog.show();}
			else{
				mPDialog.setCancelable(false);
				mPDialog.show();
			}}
		@Override
		protected ArrayList<FeedBackListModel> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONArray connectionsResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(FEEDBACK_LIST_API);
			if(jsonObjectRecived != null){

				try{
					connectionsResponse = jsonObjectRecived.getJSONArray("feedbackList");
				}catch(JSONException e){
					e.printStackTrace();
				}
				for(int i = 0; i< connectionsResponse.length();i++){
					FeedBackListModel feedBackModel = new FeedBackListModel();
					JSONObject eventsDetails;
					try{
						eventsDetails = connectionsResponse.getJSONObject(i);
						
						feedBackModel.setEvent_id(eventsDetails.getString("event_id"));
						feedBackModel.setEvent_type_id(eventsDetails.getString("event_type_id"));
						feedBackModel.setEvent_photo_path(eventsDetails.getString("event_photo_path"));
					    feedBackModel.setCreated_date(eventsDetails.getString("created_date"));
						pagination_Date_String = eventsDetails.getString("created_date");
						if(!flag_refresh){
							flag_refresh = true;
							refresh_Date_String = eventsDetails.getString("created_date");
						}
						feedBackModel.setEvent_name(eventsDetails.getString("event_name"));
						feedBackModel.setEvent_start_date(eventsDetails.getString("event_start_date"));
						feedBackModel.setEvent_start_time(eventsDetails.getString("event_start_time"));
						feedBackModel.setEvent_end_date(eventsDetails.getString("event_end_date"));
						feedBackModel.setEvent_end_time(eventsDetails.getString("event_end_time"));
						
						feedBackModel.setPublic(eventsDetails.getString("public"));
						feedBackModel.setCustom(eventsDetails.getString("custom"));
						feedBackModel.setOrg_event(eventsDetails.getString("org_event"));
						feedBackModel.setIs_public(eventsDetails.getString("is_public"));
						
						mArrayListFeedBack.add(feedBackModel);
					}catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				return mArrayListFeedBack;
			}else{
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<FeedBackListModel> result) {
			// TODO Auto-generated method stub
			
			super.onPostExecute(result);
			mPDialog.dismiss();
			if(result == null){
				noFeedBack.setVisibility(View.VISIBLE);
				noFeedBack.setText("No Feedback.When you have feedback you'll see them here.");
			}else if(result.size()==0){
				noFeedBack.setVisibility(View.VISIBLE);
				noFeedBack.setText("No Feedback.When you have feedback you'll see them here.");
			} else{
				
				System.out.println("Result size is:"+ result.size());
				mFeedBackAdapter = new FeedBackListAdminAdapter(getActivity(), result);
				mFeedBackList.setAdapter(mFeedBackAdapter);
				mFeedBackAdapter.notifyDataSetChanged();
				if(result.size() < 20){
					flag_loading = true;
				}else{
					flag_loading = false;
				}
			}
		}
	}
	
	
	//load more async class
	private class GetFeedBackListLoadMoreAsyncClass extends AsyncTask<Void, Void, ArrayList<FeedBackListModel>>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			// Showing progress dialog before sending http request
			if(mPDialog == null){
				mPDialog = Util.createProgressDialog(getActivity());
				mPDialog.setCancelable(false);
				mPDialog.show();}
			else{
				mPDialog.setCancelable(false);
				mPDialog.show();
			}}
		@Override
		protected ArrayList<FeedBackListModel> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONArray connectionsResponse = null;
			
			
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(FEEDBACK_LIST_API +"?last_date="+ pagination_Date_String.replaceAll(" ", "%20"));
			if(jsonObjectRecived != null){

				try{
					connectionsResponse = jsonObjectRecived.getJSONArray("feedbackList");
				}catch(JSONException e){
					e.printStackTrace();
				}
				if(connectionsResponse.length() > 0){
					for(int i = 0; i< connectionsResponse.length();i++){
						FeedBackListModel feedBackModel = new FeedBackListModel();
						JSONObject eventsDetails;
						try{
							eventsDetails = connectionsResponse.getJSONObject(i);
							
							feedBackModel.setEvent_id(eventsDetails.getString("event_id"));
							feedBackModel.setEvent_type_id(eventsDetails.getString("event_type_id"));
							feedBackModel.setEvent_photo_path(eventsDetails.getString("event_photo_path"));
						    feedBackModel.setCreated_date(eventsDetails.getString("created_date"));
							pagination_Date_String = eventsDetails.getString("created_date");
							
							feedBackModel.setEvent_name(eventsDetails.getString("event_name"));
							feedBackModel.setEvent_start_date(eventsDetails.getString("event_start_date"));
							feedBackModel.setEvent_start_time(eventsDetails.getString("event_start_time"));
							feedBackModel.setEvent_end_date(eventsDetails.getString("event_end_date"));
							feedBackModel.setEvent_end_time(eventsDetails.getString("event_end_time"));
							
							feedBackModel.setPublic(eventsDetails.getString("public"));
							feedBackModel.setCustom(eventsDetails.getString("custom"));
							feedBackModel.setOrg_event(eventsDetails.getString("org_event"));
							feedBackModel.setIs_public(eventsDetails.getString("is_public"));
					
							mArrayListFeedBack.add(feedBackModel);
						}catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}else{
					return null;
				}
				
				return mArrayListFeedBack;
			}else{
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<FeedBackListModel> result) {
			// TODO Auto-generated method stub
			
			super.onPostExecute(result);
			mPDialog.dismiss();
			if(result == null){
				if(mFeedBackAdapter.isEmpty()){
					noFeedBack.setVisibility(View.VISIBLE);
					flag_loading = false;
				}
				flag_loading = true;
				Toast.makeText(getActivity(), "End of event list.", Toast.LENGTH_SHORT).show();
			}else if(result.size()==0){
				if(mFeedBackAdapter.isEmpty()){
					noFeedBack.setVisibility(View.VISIBLE);
				}
				flag_loading = true;
				Toast.makeText(getActivity(), "End of event list.", Toast.LENGTH_SHORT).show();
			} else{
				mFeedBackAdapter= new FeedBackListAdminAdapter(getActivity(), result);
				mFeedBackAdapter.notifyDataSetChanged();
				flag_loading = false;
				pageCount = pageCount +20;
				mFeedBackList.setSelection(pageCount);
			}
		}
	}
	
	
	
	// refresh aysnc class
	private class GetFeedBackListRefreshAsyncClass extends AsyncTask<Void, Void, ArrayList<FeedBackListModel>>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			// Showing progress dialog before sending http request
			if(mPDialog == null){
				mPDialog = Util.createProgressDialog(getActivity());
				mPDialog.setCancelable(false);
				mPDialog.show();}
			else{
				mPDialog.setCancelable(false);
				mPDialog.show();
			}}
		@Override
		protected ArrayList<FeedBackListModel> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONArray connectionsResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(FEEDBACK_LIST_API +"&first_date="+ refresh_Date_String.replaceAll(" ", "%20"));
			if(jsonObjectRecived != null){

				try{
					
					connectionsResponse = jsonObjectRecived.getJSONArray("feedbackList");
				}catch(JSONException e){
					e.printStackTrace();
				}
				if(connectionsResponse.length() > 0){
					for(int i = 0; i< connectionsResponse.length();i++){
						FeedBackListModel feedBackModel = new FeedBackListModel();	
						JSONObject eventsDetails;
						try{
							eventsDetails = connectionsResponse.getJSONObject(i);
							
							feedBackModel.setEvent_id(eventsDetails.getString("event_id"));
							feedBackModel.setEvent_type_id(eventsDetails.getString("event_type_id"));
							feedBackModel.setEvent_photo_path(eventsDetails.getString("event_photo_path"));
						    feedBackModel.setCreated_date(eventsDetails.getString("created_date"));
							pagination_Date_String = eventsDetails.getString("created_date");
							
							feedBackModel.setEvent_name(eventsDetails.getString("event_name"));
							feedBackModel.setEvent_start_date(eventsDetails.getString("event_start_date"));
							feedBackModel.setEvent_start_time(eventsDetails.getString("event_start_time"));
							feedBackModel.setEvent_end_date(eventsDetails.getString("event_end_date"));
							feedBackModel.setEvent_end_time(eventsDetails.getString("event_end_time"));
							
							feedBackModel.setPublic(eventsDetails.getString("public"));
							feedBackModel.setCustom(eventsDetails.getString("custom"));
							feedBackModel.setOrg_event(eventsDetails.getString("org_event"));
							feedBackModel.setIs_public(eventsDetails.getString("is_public"));
					
							
							if(!flag_refresh){
								flag_refresh = true;
								refresh_Date_String = eventsDetails.getString("created_date");
							}
							mArrayListFeedBack.add(0,feedBackModel);
						}catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}else{
					return null;
				}
				
				return mArrayListFeedBack;
			}else{
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<FeedBackListModel> result) {
			// TODO Auto-generated method stub
			
			super.onPostExecute(result);
			mSwipeLayout.setRefreshing(false);
			mPDialog.dismiss();
			if(result == null){
				if(mFeedBackAdapter.isEmpty()){
					noFeedBack.setVisibility(View.VISIBLE);
					flag_loading = false;
				}
				flag_loading = true;
			}else if(result.size()==0){
				if(mFeedBackAdapter.isEmpty()){
					noFeedBack.setVisibility(View.VISIBLE);
				}
				flag_loading = true;
			} else{
				mFeedBackAdapter = new FeedBackListAdminAdapter(getActivity(), result);
				mFeedBackAdapter.notifyDataSetChanged();
				flag_loading = false;
			}
		}
	}



	  
	}
