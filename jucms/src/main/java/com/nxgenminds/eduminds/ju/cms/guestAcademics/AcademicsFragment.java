package com.nxgenminds.eduminds.ju.cms.guestAcademics;

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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.guestAdmissions.AdmissionsModel;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.noticeboard.NoticeBoardModel;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;



public class AcademicsFragment extends Fragment {

	private ListView acdemicsListView;
	private SwipeRefreshLayout mSwipeRefreshLayout;
	JSONArray acdemicsResponse;
	private TextView noacdemics;
	private static int pageCount = 0;
	private String AcdamicsURL = Util.API + "academicslist";
	private ArrayList<AcademicsModel> acdemicmodel = new ArrayList<AcademicsModel>();
	private ProgressDialog pDialog;
	private static String pagination_Date_String;
	private boolean flag_loading = false;
	AlertDialogManager alert = new AlertDialogManager();
	private boolean flag_refresh = false;
	private static String refresh_Date_String;
	private AcademicsAdapter adapter;
	LinearLayout ll;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_academics, container, false);
		
		acdemicsListView = (ListView) rootView.findViewById(R.id.academicslistview);
		mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.academic_swipe_container);
		mSwipeRefreshLayout.setEnabled(false);
		noacdemics = (TextView) rootView.findViewById(R.id.noAcdemicslist);
		noacdemics.setGravity(Gravity.CENTER);
		
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		acdemicmodel.clear();
		pageCount = 0;
		adapter = new AcademicsAdapter(getActivity(),acdemicmodel);
		
		//swipeLayout.setColorScheme(android.R.color.holo_blue_bright,android.R.color.holo_green_light, android.R.color.holo_orange_light,android.R.color.holo_red_light);
		ConnectionDetector conn = new ConnectionDetector(getActivity());
		if(conn.isConnectingToInternet()){
			new GetAcadamicsAsync().execute();
		}else{
			alert.showAlertDialog(getActivity(), "Connection Error", "Please check your internet connection", false);
		}
		acdemicsListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				boolean enable = false;
				if(acdemicsListView != null && acdemicsListView.getChildCount() > 0){

					boolean firstItemVisible = acdemicsListView.getFirstVisiblePosition() == 0;
					boolean topOfFirstItemVisible = acdemicsListView.getChildAt(0).getTop() == 0;
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
							new GetAcadamicsLoadMoreAsync().execute();
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
					new GeAcadamicsRefreshAsyncClass().execute();
				}else{
					alert.showAlertDialog(getActivity(),"Connection Error","Check your Internet Connection",false);		
				}

			}
		});
		
		acdemicsListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Object object = acdemicsListView.getItemAtPosition(position);
				AcademicsModel event_data = (AcademicsModel)  object;
				Intent intent = new Intent(getActivity(),AcademicsDeatailActivity.class);
			
				intent.putExtra("title", event_data.getTitle());
				intent.putExtra("desc", event_data.getDescription());
				intent.putExtra("pdfilename",event_data.getDownload_file_name());
				intent.putExtra("pdflink",event_data.getDownload_file_path());
				startActivity(intent);
			}
		});
		
	}
	public class GeAcadamicsRefreshAsyncClass extends AsyncTask<Void,Void,Void>{

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			mSwipeRefreshLayout.setRefreshing(false);

			if(HttpGetClient.statuscode == 200)
			{
				if(acdemicmodel == null){
					Toast.makeText(getActivity(), "No Data to load", Toast.LENGTH_SHORT).show();
				} 
				else if(acdemicmodel.size()==0){
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
			
			JSONObject jsonObjectReceived = HttpGetClient.sendHttpPost(AcdamicsURL+"?first_date="+refresh_Date_String.replaceAll(" ", "%20"));
			//JSONObject jsonObjectReceived = HttpGetClient.sendHttpPost(NoticeBoardURL+"?first_date="+refresh_Date_String.replaceAll(" ", "%20"));
			if (jsonObjectReceived != null) {
				try{
					if(jsonObjectReceived.getString("error").equalsIgnoreCase("false")){
						JSONArray broadcastResponse = jsonObjectReceived.getJSONArray("noticeboard");
						for(int i = 0; i< broadcastResponse.length();i++){
							AcademicsModel academicsData = new AcademicsModel();
							JSONObject admissionDetails;
							admissionDetails = broadcastResponse.getJSONObject(i);
							
							academicsData.setStatus(admissionDetails.getString("status"));
							academicsData.setTitle(admissionDetails.getString("title"));
							academicsData.setAcademic_id(admissionDetails.getString("academic_id"));
							academicsData.setDescription(admissionDetails.getString("description"));
							academicsData.setDownload_file_name(admissionDetails.getString("download_file_name"));
							academicsData.setDownload_file_path(admissionDetails.getString("download_file_path"));
							academicsData.setCreated_date(admissionDetails.getString("created_date"));
							academicsData.setCreated_by(admissionDetails.getString("created_by"));
							academicsData.setModified_date(admissionDetails.getString("modified_date"));
							academicsData.setModified_by(admissionDetails.getString("modified_by"));
							
							pagination_Date_String = admissionDetails.getString("created_date");
							
							if(!flag_refresh){
							
								flag_refresh = true;
							
								refresh_Date_String = admissionDetails.getString("created_date");
								
							}

							//broadcastData.setFrom_user_profile_pic(broadcastDetails.getString("from_user_profile_pic"));

							acdemicmodel.add(academicsData);
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
	
	public class GetAcadamicsLoadMoreAsync extends AsyncTask<Void, Void, ArrayList<AcademicsModel>>{

		@Override
		protected ArrayList<AcademicsModel> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONArray connectionsResponse = null;

			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(AcdamicsURL + "?last_date=" + pagination_Date_String.replaceAll(" ", "%20"));

			if(jsonObjectRecived != null){
				try{
					connectionsResponse = jsonObjectRecived.getJSONArray("academics");
				}catch (JSONException e) {
					e.printStackTrace();
				}
				if(connectionsResponse.length() > 0){
					for(int i = 0; i< connectionsResponse.length();i++){
						AcademicsModel academicsData = new AcademicsModel();
						JSONObject admissionDetails;

						try {
							admissionDetails = connectionsResponse.getJSONObject(i);
							academicsData.setStatus(admissionDetails.getString("status"));
							academicsData.setTitle(admissionDetails.getString("title"));
							academicsData.setAcademic_id(admissionDetails.getString("academic_id"));
							academicsData.setDescription(admissionDetails.getString("description"));
							academicsData.setDownload_file_name(admissionDetails.getString("download_file_name"));
							academicsData.setDownload_file_path(admissionDetails.getString("download_file_path"));
							academicsData.setCreated_date(admissionDetails.getString("created_date"));
							academicsData.setCreated_by(admissionDetails.getString("created_by"));
							academicsData.setModified_date(admissionDetails.getString("modified_date"));
							academicsData.setModified_by(admissionDetails.getString("modified_by"));
							
							pagination_Date_String = admissionDetails.getString("created_date");

							
							if(!flag_refresh){
								flag_refresh = true;
								refresh_Date_String = admissionDetails.getString("created_date");
							}

							acdemicmodel.add(0,academicsData);
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
					   
					return acdemicmodel;
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
		protected void onPostExecute(ArrayList<AcademicsModel> result) {
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
				acdemicsListView.setSelection(pageCount);
			}
		}

	}
	public class GetAcadamicsAsync extends AsyncTask<Void, Void, ArrayList<AcademicsModel>>{

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
		protected ArrayList<AcademicsModel> doInBackground(Void... params) {
			JSONArray connectionsResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(AcdamicsURL);
			if(jsonObjectRecived != null){

				try{
					connectionsResponse = jsonObjectRecived.getJSONArray("academics");
					for(int i = 0; i< connectionsResponse.length();i++){
						AcademicsModel academicsData = new AcademicsModel();
						JSONObject admissionDetails;
						try{
							admissionDetails = connectionsResponse.getJSONObject(i);
							academicsData.setStatus(admissionDetails.getString("status"));
							academicsData.setTitle(admissionDetails.getString("title"));
							academicsData.setAcademic_id(admissionDetails.getString("academic_id"));
							academicsData.setDescription(admissionDetails.getString("description"));
							academicsData.setDownload_file_name(admissionDetails.getString("download_file_name"));
							academicsData.setDownload_file_path(admissionDetails.getString("download_file_path"));
							academicsData.setCreated_date(admissionDetails.getString("created_date"));
							academicsData.setCreated_by(admissionDetails.getString("created_by"));
							academicsData.setModified_date(admissionDetails.getString("modified_date"));
							academicsData.setModified_by(admissionDetails.getString("modified_by"));
							pagination_Date_String = admissionDetails.getString("created_date");

							if(!flag_refresh){
								flag_refresh = true;
								refresh_Date_String = admissionDetails.getString("created_date");
							}
							acdemicmodel.add(0,academicsData);
							
						}catch (Exception e) {
							e.printStackTrace();
						}
					}
				}catch(JSONException e){
					e.printStackTrace();
				}

				return acdemicmodel;
			}else{
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<AcademicsModel> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if(result == null){
				noacdemics.setVisibility(View.VISIBLE);
				
				Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_SHORT).show();
			}else if(result.size()==0){
				noacdemics.setVisibility(View.VISIBLE);
			}  else{
				//adapter = new BroadcastListAdapter_new(getActivity(), result);
				acdemicsListView.setAdapter(adapter);

				if(result.size() < 20){
					flag_loading = true;
				}else{
					flag_loading = false;
				}
			}
		}

	}

}


