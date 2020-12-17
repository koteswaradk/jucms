package com.nxgenminds.eduminds.ju.cms.guestEntrnaceTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;

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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.guestAcademics.AcademicsModel;
import com.nxgenminds.eduminds.ju.cms.guestAdmissions.AdmissionDeatailActivity;
import com.nxgenminds.eduminds.ju.cms.guestAdmissions.AdmissionsModel;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.noticeboard.NoticeBoardModel;
import com.nxgenminds.eduminds.ju.cms.noticeboard.NoticeBoardFragment.GetNoticeBoarLoadMoreAsync;
import com.nxgenminds.eduminds.ju.cms.noticeboard.NoticeBoardFragment.GetNoticeBoardRefreshAsyncClass;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;



public class EntranceTestResultFragment extends Fragment {

	private ListView resultListView;
	private SwipeRefreshLayout mSwipeRefreshLayout;
	JSONArray acdemicsResponse;
	private TextView noacdemics;
	private static int pageCount = 0;
	private String testresultURL = Util.API + "entrancetestresultlist";
	private ArrayList<EntranceTestResultModel> testmodel = new ArrayList<EntranceTestResultModel>();
	private ProgressDialog pDialog;
	private static String pagination_Date_String;
	private boolean flag_loading = false;
	AlertDialogManager alert = new AlertDialogManager();
	private boolean flag_refresh = false;
	private static String refresh_Date_String;
	private EntranceTestReslutAdapter adapter;


	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_entrancetest, container, false);
		resultListView = (ListView) rootView.findViewById(R.id.testresultlistview);
		mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.entrance_test_swipe_container);
		mSwipeRefreshLayout.setEnabled(false);
		noacdemics = (TextView) rootView.findViewById(R.id.notestresultlist);
		noacdemics.setGravity(Gravity.CENTER);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		testmodel.clear();
		pageCount = 0;
		adapter = new EntranceTestReslutAdapter(getActivity(),testmodel);
		
		//swipeLayout.setColorScheme(android.R.color.holo_blue_bright,android.R.color.holo_green_light, android.R.color.holo_orange_light,android.R.color.holo_red_light);
		ConnectionDetector conn = new ConnectionDetector(getActivity());
		if(conn.isConnectingToInternet()){
			new GetAdmissionsAsync().execute();
		}else{
			alert.showAlertDialog(getActivity(), "Connection Error", "Please check your internet connection", false);
		}
		
		resultListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				boolean enable = false;
				if(resultListView != null && resultListView.getChildCount() > 0){

					boolean firstItemVisible = resultListView.getFirstVisiblePosition() == 0;
					boolean topOfFirstItemVisible = resultListView.getChildAt(0).getTop() == 0;
					enable = firstItemVisible && topOfFirstItemVisible;
				}
				mSwipeRefreshLayout.setEnabled(enable);	
				if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0)
				{
					if(flag_loading == false)
					{ 
						flag_loading = true;
						ConnectionDetector conn = new ConnectionDetector(getActivity());
						if(conn.isConnectingToInternet()){
							new GetEntranceResultLoadMoreAsync().execute();
						}else{
							alert.showAlertDialog(getActivity(),"Connection Error","Check your Internet Connection",false);		
						}

					}
				}
				
			}
		});
		mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				mSwipeRefreshLayout.setRefreshing(true);
				flag_refresh = false;
				ConnectionDetector conn = new ConnectionDetector(getActivity());
				if(conn.isConnectingToInternet()){
					mSwipeRefreshLayout.setRefreshing(false);
				//	new GetEntranceResultsRefreshAsyncClass().execute();
				}else{
					alert.showAlertDialog(getActivity(),"Connection Error","Check your Internet Connection",false);		
				}

			}
		});
		

		resultListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Object object = resultListView.getItemAtPosition(position);
				EntranceTestResultModel event_data = (EntranceTestResultModel)  object;
				Intent intent = new Intent(getActivity(),EntranceTestResultDeatailActivity.class);
			
				intent.putExtra("title", event_data.getTitle());
				intent.putExtra("desc", event_data.getDescription());
				intent.putExtra("pdfilename",event_data.getDownload_file_name());
				intent.putExtra("pdflink",event_data.getDownload_file_path());
				
				startActivity(intent);
			}
		});
		
	}
		
	public class GetEntranceResultLoadMoreAsync extends AsyncTask<Void, Void, ArrayList<EntranceTestResultModel>>{

		@Override
		protected ArrayList<EntranceTestResultModel> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			//JSONArray broadcastResponse = null;
			JSONArray connectionsResponse = null;
			Log.i("before hit jsonObjectRecived", "display");
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(testresultURL + "?last_date=" + pagination_Date_String.replaceAll(" ", "%20"));
			Log.i("before hit jsonObjectRecived", "display");
			if(jsonObjectRecived != null){
				try{
					connectionsResponse = jsonObjectRecived.getJSONArray("entrance_test_results");
				}catch (JSONException e) {
					e.printStackTrace();
				}
				if(connectionsResponse.length() > 0){
					for(int i = 0; i< connectionsResponse.length();i++){
						EntranceTestResultModel entranceResultData = new EntranceTestResultModel();
						JSONObject noticeBoardDetails;

						try {
							Log.i("before hit jsonObjectRecived1", "display");
							noticeBoardDetails = connectionsResponse.getJSONObject(i);
							Log.i("before hit jsonObjectRecived2", "display");
						entranceResultData.setStatus(noticeBoardDetails.getString("status"));
						entranceResultData.setTitle(noticeBoardDetails.getString("title"));
						entranceResultData.setEntrance_test_result_id(noticeBoardDetails.getString("entrance_test_result_id"));
						entranceResultData.setDescription(noticeBoardDetails.getString("description"));
						entranceResultData.setDownload_file_name(noticeBoardDetails.getString("download_file_name"));
						entranceResultData.setDownload_file_path(noticeBoardDetails.getString("download_file_path"));
						entranceResultData.setCreated_date(noticeBoardDetails.getString("created_date"));
						entranceResultData.setCreated_by(noticeBoardDetails.getString("created_by"));
						entranceResultData.setModified_date(noticeBoardDetails.getString("modified_date"));
						entranceResultData.setModified_by(noticeBoardDetails.getString("modified_by"));
						pagination_Date_String = noticeBoardDetails.getString("created_date");
						Log.i("before hit jsonObjectRecived3", "display"+pagination_Date_String);


						if(!flag_refresh){
							flag_refresh = true;
							Log.i("before hit jsonObjectRecived4", "display");
							refresh_Date_String = noticeBoardDetails.getString("created_date");
						}

						testmodel.add(entranceResultData);
						Log.i("before hit jsonObjectRecived5", "display");
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				   
				return testmodel;
			}
			else
			{
				return null;
			}
		}
		else
		{
			return null;
		}
		
		}
		
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
		protected void onPostExecute(ArrayList<EntranceTestResultModel> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if(result == null){
				flag_loading = true;
				Toast.makeText(getActivity(),"No data to load",Toast.LENGTH_SHORT).show();
			} 
			else if(result.size() == 0){

				flag_loading = true;
				Toast.makeText(getActivity(),"Nodata to load",Toast.LENGTH_SHORT).show();
			}
			else{	
				adapter.notifyDataSetChanged();
				flag_loading = false;
				pageCount = pageCount +20;
				resultListView.setSelection(pageCount);
			}
		}

	}
	public class GetEntranceResultsRefreshAsyncClass extends AsyncTask<Void,Void,Void>{
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			mSwipeRefreshLayout.setRefreshing(false);

			if(HttpGetClient.statuscode == 200)
			{
				if(testmodel == null){
					Toast.makeText(getActivity(), "No Data to load", Toast.LENGTH_SHORT).show();
				} 
				else if(testmodel.size()==0){
					Toast.makeText(getActivity(), "No Data to load", Toast.LENGTH_SHORT).show();
				}	   
				else{
					//adapter = new BroadcastListAdapter_new(getActivity(),mArrayListBroadcast);
					adapter.notifyDataSetChanged();		     
				}
			}
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONObject jsonObjectReceived = HttpGetClient.sendHttpPost(testresultURL+"?first_date="+refresh_Date_String.replaceAll(" ", "%20"));
			//JSONObject jsonObjectReceived = HttpGetClient.sendHttpPost(NoticeBoardURL+"?first_date="+refresh_Date_String.replaceAll(" ", "%20"));
			if (jsonObjectReceived != null) {
				try{
					if(jsonObjectReceived.getString("error").equalsIgnoreCase("false")){
						JSONArray broadcastResponse = jsonObjectReceived.getJSONArray("entrance_test_results");
						for(int i = 0; i< broadcastResponse.length();i++){
							EntranceTestResultModel academicsData = new EntranceTestResultModel();
							JSONObject entranceDetails;
							entranceDetails = broadcastResponse.getJSONObject(i);
							academicsData.setStatus(entranceDetails.getString("status"));
							academicsData.setTitle(entranceDetails.getString("title"));
							academicsData.setEntrance_test_result_id(entranceDetails.getString("entrance_test_result_id"));
							academicsData.setDescription(entranceDetails.getString("description"));
							academicsData.setDownload_file_name(entranceDetails.getString("download_file_name"));
							academicsData.setDownload_file_path(entranceDetails.getString("download_file_path"));
							academicsData.setCreated_date(entranceDetails.getString("created_date"));
							academicsData.setCreated_by(entranceDetails.getString("created_by"));
							academicsData.setModified_date(entranceDetails.getString("modified_date"));
							academicsData.setModified_by(entranceDetails.getString("modified_by"));
							Log.i("1", "1");
							pagination_Date_String = entranceDetails.getString("created_date");
							Log.i("2", "2");
							if(!flag_refresh){
								
								flag_refresh = true;
							
								refresh_Date_String = entranceDetails.getString("created_date");
								
							}

							//broadcastData.setFrom_user_profile_pic(broadcastDetails.getString("from_user_profile_pic"));
							testmodel.add(academicsData);
							Log.i("6", "6");
						}
					}
				}	catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			return null;
		}

	}
	
	private class GetAdmissionsAsync extends AsyncTask<Void, Void, ArrayList<EntranceTestResultModel>>{

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
		protected ArrayList<EntranceTestResultModel> doInBackground(Void... params) {
			JSONArray connectionsResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(testresultURL);
			if(jsonObjectRecived != null){

				try{
					connectionsResponse = jsonObjectRecived.getJSONArray("entrance_test_results");
					for(int i = 0; i< connectionsResponse.length();i++){
						EntranceTestResultModel academicsData1 = new EntranceTestResultModel();
						JSONObject noticeBoardDetails1;
						try{
							noticeBoardDetails1 = connectionsResponse.getJSONObject(i);
							academicsData1.setStatus(noticeBoardDetails1.getString("status"));
							academicsData1.setTitle(noticeBoardDetails1.getString("title"));
							academicsData1.setEntrance_test_result_id(noticeBoardDetails1.getString("entrance_test_result_id"));
							academicsData1.setDescription(noticeBoardDetails1.getString("description"));
							academicsData1.setDownload_file_name(noticeBoardDetails1.getString("download_file_name"));
							academicsData1.setDownload_file_path(noticeBoardDetails1.getString("download_file_path"));
							academicsData1.setCreated_date(noticeBoardDetails1.getString("created_date"));
							academicsData1.setCreated_by(noticeBoardDetails1.getString("created_by"));
							academicsData1.setModified_date(noticeBoardDetails1.getString("modified_date"));
							academicsData1.setModified_by(noticeBoardDetails1.getString("modified_by"));
							
							pagination_Date_String = noticeBoardDetails1.getString("created_date");
						
							if(!flag_refresh){
								flag_refresh = true;
								refresh_Date_String = noticeBoardDetails1.getString("created_date");
							}
							testmodel.add(academicsData1);
						}catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}catch(JSONException e){
					e.printStackTrace();
				}

				return testmodel;
			}else{
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<EntranceTestResultModel> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if(result == null){
				noacdemics.setVisibility(View.VISIBLE);
				Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_SHORT).show();
			}else if(result.size()==0){				
					noacdemics.setText("No Data found");
					noacdemics.setVisibility(View.VISIBLE);
					
			}  else{
				//adapter = new BroadcastListAdapter_new(getActivity(), result);
				
				resultListView.setAdapter(adapter);

				if(result.size() < 20){
					flag_loading = true;
				}else{
					flag_loading = false;
				}
			}
		}

	}

}


