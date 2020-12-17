package com.nxgenminds.eduminds.ju.cms.guestAdmissions;

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
import android.text.Layout;
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
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;



public class AdmissionsFragment extends Fragment {



	private ListView admissionsListView;
	private SwipeRefreshLayout mSwipeRefreshLayout;
	JSONArray acdemicsResponse;
	private TextView noacdemics;
	private static int pageCount = 0;
	private String AdmissionURL = Util.API + "admissionslist";
	private ArrayList<AdmissionsModel> admisionsmodel = new ArrayList<AdmissionsModel>();
	private ProgressDialog pDialog;
	private static String pagination_Date_String;
	private boolean flag_loading = false;
	AlertDialogManager alert = new AlertDialogManager();
	private boolean flag_refresh = false;
	private static String refresh_Date_String;
	private AdmissionsAdapter adapter;


	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_admissions, container, false);
		admissionsListView = (ListView) rootView.findViewById(R.id.admissionlistview);
		mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.admission_swipe_container);
		mSwipeRefreshLayout.setEnabled(false);
		noacdemics = (TextView) rootView.findViewById(R.id.noAdmissionslist);
		noacdemics.setGravity(Gravity.CENTER);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		admisionsmodel.clear();
		pageCount = 0;
		adapter = new AdmissionsAdapter(getActivity(),admisionsmodel);
		
		//swipeLayout.setColorScheme(android.R.color.holo_blue_bright,android.R.color.holo_green_light, android.R.color.holo_orange_light,android.R.color.holo_red_light);
		ConnectionDetector conn = new ConnectionDetector(getActivity());
		if(conn.isConnectingToInternet()){
			new GetAdmissionsAsync().execute();
		}else{
			alert.showAlertDialog(getActivity(), "Connection Error", "Please check your internet connection", false);
		}
		admissionsListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				boolean enable = false;
				if(admissionsListView != null && admissionsListView.getChildCount() > 0){

					boolean firstItemVisible = admissionsListView.getFirstVisiblePosition() == 0;
					boolean topOfFirstItemVisible = admissionsListView.getChildAt(0).getTop() == 0;
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
							new GetAdmissionLoadMoreAsync().execute();
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
					new GetAdmissionRefreshAsyncClass().execute();
				}else{
					alert.showAlertDialog(getActivity(),"Connection Error","Check your Internet Connection",false);		
				}

			}
		});

	admissionsListView.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			Object object = admissionsListView.getItemAtPosition(position);
			AdmissionsModel event_data = (AdmissionsModel)  object;
			Intent intent = new Intent(getActivity(),AdmissionDeatailActivity.class);
		
			intent.putExtra("title", event_data.getTitle());
			intent.putExtra("desc", event_data.getDescription());
			intent.putExtra("pdfilename",event_data.getDownload_file_name());
			intent.putExtra("pdflink_adm",event_data.getDownload_file_path());
			
			startActivity(intent);
		}
	});
	
}
	public class GetAdmissionRefreshAsyncClass extends AsyncTask<Void,Void,Void>{

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			mSwipeRefreshLayout.setRefreshing(false);

			if(HttpGetClient.statuscode == 200)
			{
				if(admisionsmodel == null){
					Toast.makeText(getActivity(), "No Data to load", Toast.LENGTH_SHORT).show();
				} 
				else if(admisionsmodel.size()==0){
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
			
			JSONObject jsonObjectReceived = HttpGetClient.sendHttpPost(AdmissionURL+"?first_date="+refresh_Date_String.replaceAll(" ", "%20"));
			if (jsonObjectReceived != null) {
				try{
					if(jsonObjectReceived.getString("error").equalsIgnoreCase("false")){
						JSONArray broadcastResponse = jsonObjectReceived.getJSONArray("noticeboard");
						for(int i = 0; i< broadcastResponse.length();i++){
							AdmissionsModel admissionData = new AdmissionsModel();
							JSONObject admissionDetails;
							admissionDetails = broadcastResponse.getJSONObject(i);
		/*	if (jsonObjectReceived != null) {
				try{
					if(jsonObjectReceived.getString("error").equalsIgnoreCase("false")){
						JSONArray broadcastResponse = jsonObjectReceived.getJSONArray("admissions");
						for(int i = 0; i< broadcastResponse.length();i++){

							AdmissionsModel admissionData = new AdmissionsModel();
							JSONObject admissionDetails;
							
							*/
							admissionDetails = broadcastResponse.getJSONObject(i);
							admissionData.setStatus(admissionDetails.getString("status"));
							admissionData.setTitle(admissionDetails.getString("title"));
							admissionData.setAdmission_id(admissionDetails.getString("admission_id"));
							admissionData.setDescription(admissionDetails.getString("description"));
							admissionData.setDownload_file_name(admissionDetails.getString("download_file_name"));
							admissionData.setDownload_file_path(admissionDetails.getString("download_file_path"));
							admissionData.setCreated_date(admissionDetails.getString("created_date"));
							admissionData.setCreated_by(admissionDetails.getString("created_by"));
							admissionData.setModified_date(admissionDetails.getString("modified_date"));
							admissionData.setModified_by(admissionDetails.getString("modified_by"));
							pagination_Date_String = admissionDetails.getString("created_date");
						
							if(!flag_refresh){
								flag_refresh = true;
								refresh_Date_String = admissionDetails.getString("created_date");
							}

							//broadcastData.setFrom_user_profile_pic(broadcastDetails.getString("from_user_profile_pic"));

							admisionsmodel.add(0,admissionData);
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
	public class GetAdmissionLoadMoreAsync extends AsyncTask<Void, Void, ArrayList<AdmissionsModel>>
	{

		@Override
		protected ArrayList<AdmissionsModel> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONArray connectionsResponse = null;

			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(AdmissionURL + "?last_date=" + pagination_Date_String.replaceAll(" ", "%20"));

			if(jsonObjectRecived != null){
				try{
					connectionsResponse = jsonObjectRecived.getJSONArray("admissions");
				}catch (JSONException e) {
					e.printStackTrace();
				}
				if(connectionsResponse.length() > 0){
					for(int i = 0; i< connectionsResponse.length();i++){
						AdmissionsModel admissionData = new AdmissionsModel();
						JSONObject admissionDetails;

						try {
							admissionDetails = connectionsResponse.getJSONObject(i);
							admissionData.setStatus(admissionDetails.getString("status"));
							admissionData.setTitle(admissionDetails.getString("title"));
							admissionData.setAdmission_id(admissionDetails.getString("admission_id"));
							admissionData.setDescription(admissionDetails.getString("description"));
							admissionData.setDownload_file_name(admissionDetails.getString("download_file_name"));
							admissionData.setDownload_file_path(admissionDetails.getString("download_file_path"));
							admissionData.setCreated_date(admissionDetails.getString("created_date"));
							admissionData.setCreated_by(admissionDetails.getString("created_by"));
							admissionData.setModified_date(admissionDetails.getString("modified_date"));
							admissionData.setModified_by(admissionDetails.getString("modified_by"));
							pagination_Date_String = admissionDetails.getString("created_date");

						
							if(!flag_refresh){
								flag_refresh = true;
								refresh_Date_String = admissionDetails.getString("created_date");
							}

							admisionsmodel.add(0,admissionData);
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
					   
					return admisionsmodel;
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
		protected void onPostExecute(ArrayList<AdmissionsModel> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if(result == null){
				flag_loading = true;
				Toast.makeText(getActivity(),"No more data to load",Toast.LENGTH_SHORT).show();
			} 
			else if(result.size() == 0){

				flag_loading = true;
				Toast.makeText(getActivity(),"No more data to load",Toast.LENGTH_SHORT).show();
			}
			else{	
				adapter.notifyDataSetChanged();
				flag_loading = false;
				pageCount = pageCount +20;
				admissionsListView.setSelection(pageCount);
			}
		}

	}

	
	private class GetAdmissionsAsync extends AsyncTask<Void, Void, ArrayList<AdmissionsModel>>{

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
		protected ArrayList<AdmissionsModel> doInBackground(Void... params) {
			JSONArray connectionsResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(AdmissionURL);
			if(jsonObjectRecived != null){

				try{
					connectionsResponse = jsonObjectRecived.getJSONArray("admissions");
					for(int i = 0; i< connectionsResponse.length();i++){
						AdmissionsModel admissionData = new AdmissionsModel();
						JSONObject admissionDetails;
						try{
							admissionDetails = connectionsResponse.getJSONObject(i);
							admissionData.setStatus(admissionDetails.getString("status"));
							admissionData.setTitle(admissionDetails.getString("title"));
							admissionData.setAdmission_id(admissionDetails.getString("admission_id"));
							admissionData.setDescription(admissionDetails.getString("description"));
							admissionData.setDownload_file_name(admissionDetails.getString("download_file_name"));
							admissionData.setDownload_file_path(admissionDetails.getString("download_file_path"));
							admissionData.setCreated_date(admissionDetails.getString("created_date"));
							admissionData.setCreated_by(admissionDetails.getString("created_by"));
							admissionData.setModified_date(admissionDetails.getString("modified_date"));
							admissionData.setModified_by(admissionDetails.getString("modified_by"));
							pagination_Date_String = admissionDetails.getString("created_date");

							if(!flag_refresh){
								flag_refresh = true;
								refresh_Date_String = admissionDetails.getString("created_date");
							}
							admisionsmodel.add(0,admissionData);
						}catch (Exception e) {
							e.printStackTrace();
						}
					}
				}catch(JSONException e){
					e.printStackTrace();
				}

				return admisionsmodel;
			}else{
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<AdmissionsModel> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if(result == null){
				noacdemics.setGravity(Gravity.CENTER_VERTICAL);
				//noacdemics.setLayout_Gravity(Gravity.CENTER_VERTICAL);
				noacdemics.setVisibility(View.VISIBLE);
				Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_SHORT).show();
			}else if(result.size()==0){
				noacdemics.setVisibility(View.VISIBLE);
			}  else{
				//adapter = new BroadcastListAdapter_new(getActivity(), result);
				admissionsListView.setAdapter(adapter);

				if(result.size() < 20){
					flag_loading = true;
				}else{
					flag_loading = false;
				}
			}
		}

	}

}


