package com.nxgenminds.eduminds.ju.cms.FeedbackAdmin;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class FeedBackAdminFilterActivity extends ActionBarActivity {
	
	private SwipeRefreshLayout mSwipeLayout;
	private ListView mFeedBackFilterList;
	private TextView noFeedBackFilter;
	
    private String is_public,mEventId;
    
	private static String FEEDBACK_FILTER_LIST_API = Util.API+"feedback_filter_list?is_public=";
	private ArrayList<FeedBackFilterModel> mArrayListFeedBackFilter = new ArrayList<FeedBackFilterModel>();
	
	private ProgressDialog mPDialog;
	private static int pageCount = 0;
	private static String pagination_Date_String;
	
	private boolean flag_loading = false;
	private boolean flag_refresh = false;
	private static String refresh_Date_String;
	
	private FeedBackFilterListAdapter mFeedBackFilterAdapter;
	AlertDialogManager alert = new AlertDialogManager();
	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
	     super.onCreate(savedInstanceState)	;
	     setContentView(R.layout.activity_feedback_admin_filter);
	     
	        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container_feedback_admin_filters);
	        mSwipeLayout.setEnabled(false);
	        mSwipeLayout.setRefreshing(false);
	        
			mFeedBackFilterList = (ListView) findViewById(R.id.activity_feedback_admin_filter_list);
			noFeedBackFilter = (TextView) findViewById(R.id.NoFeedBackAdminFilter);
			
			getSupportActionBar().setTitle("Feedback");
			
			Bundle extras = getIntent().getExtras();
			
			if(extras!=null){
				
				is_public = extras.getString("is_public");
				mEventId = extras.getString("event_id");
				
			}
			
			ConnectionDetector conn = new ConnectionDetector(FeedBackAdminFilterActivity.this);
			if(conn.isConnectingToInternet()){
				new GetFeedBackFilterList().execute();
			}else{
				alert.showAlertDialog(FeedBackAdminFilterActivity.this, "Connection Error", "Please check your internet connection", false);
			}
			
			mFeedBackFilterList.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					FeedBackFilterModel filterModel = mArrayListFeedBackFilter.get(position);
					Intent teacherListIntent = new Intent(FeedBackAdminFilterActivity.this,FeedBackAdminTeacherListingActivity.class);
					teacherListIntent.putExtra("event_id",mEventId);
					teacherListIntent.putExtra("class_section",filterModel.getValue());
					startActivity(teacherListIntent);
					
				}
				
			});
			
	     
	}
	
	
	private class GetFeedBackFilterList extends AsyncTask<Void, Void, ArrayList<FeedBackFilterModel>>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			// Showing progress dialog before sending http request
			if(mPDialog == null){
				mPDialog = Util.createProgressDialog(FeedBackAdminFilterActivity.this);
				mPDialog.setCancelable(false);
				mPDialog.show();}
			else{
				mPDialog.setCancelable(false);
				mPDialog.show();
			}}
		@Override
		protected ArrayList<FeedBackFilterModel> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONArray connectionsResponse = null;		
			JSONObject jsonObjectReceived = HttpGetClient.sendHttpPost(FEEDBACK_FILTER_LIST_API+is_public+"&event_id="+mEventId);
			if(jsonObjectReceived != null){

				try{
					if(jsonObjectReceived.getString("status").equalsIgnoreCase("1")){
					connectionsResponse = jsonObjectReceived.getJSONArray("custom_filters");
					}
				}catch(JSONException e){
					e.printStackTrace();
				}
				if(connectionsResponse!=null && connectionsResponse.length()>0){
				for(int i = 0; i< connectionsResponse.length();i++){
					FeedBackFilterModel feedBackFilterModel = new FeedBackFilterModel();
					JSONObject filterDetails;
					try{
						filterDetails = connectionsResponse.getJSONObject(i);
					    feedBackFilterModel.setValue(filterDetails.getString("value"));
					    mArrayListFeedBackFilter.add(feedBackFilterModel);
					}catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				return mArrayListFeedBackFilter;
				} else{
					return null;
				}
			}else{
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<FeedBackFilterModel> result) {
			// TODO Auto-generated method stub
			
			super.onPostExecute(result);
			mPDialog.dismiss();
			if(result == null){
				noFeedBackFilter.setVisibility(View.VISIBLE);
				/*noFeedBackFilter.setText("No Events.When you have events you'll see them here.");*/
			}else if(result.size()==0){
				noFeedBackFilter.setVisibility(View.VISIBLE);
				/*noFeedBackFilter.setText("No Events.When you have events you'll see them here.");*/
			} else{
							
				mFeedBackFilterAdapter = new FeedBackFilterListAdapter(FeedBackAdminFilterActivity.this, result);
				mFeedBackFilterList.setAdapter(mFeedBackFilterAdapter);
				mFeedBackFilterAdapter.notifyDataSetChanged();
				/*if(result.size() < 20){
					flag_loading = true;
				}else{
					flag_loading = false;
				}*/
			}
		}
	}


}
