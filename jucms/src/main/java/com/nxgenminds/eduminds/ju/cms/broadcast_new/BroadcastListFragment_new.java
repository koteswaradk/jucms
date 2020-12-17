package com.nxgenminds.eduminds.ju.cms.broadcast_new;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;




public class BroadcastListFragment_new extends Fragment {

	private SwipeRefreshLayout mSwipeRefreshLayout;
	private ListView mBroadcastList;
	private TextView mNoBroadcastMessage;
	private ImageButton mCreate_Broadcast;
	private RelativeLayout mBottom_layout;
	private static String GET_BROADCAST_LIST_URL = Util.API + "broadcast";
	private RelativeLayout bottom_Buttons_Layout;
	private ArrayList<BroadcastListModel_new> mArrayListBroadcast = new ArrayList<BroadcastListModel_new>();
	private BroadcastListAdapter_new adapter;
	private static int pageCount = 0;
	private String pagination_Date_String;
	private boolean flag_loading = false;
	private boolean flag_refresh = false;
	private static String refresh_Date_String;
	private AlertDialogManager alert = new AlertDialogManager();
	private ProgressDialog pDialog;
	private static final int UPDATE_RESULT = 0x19;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.new_fragment_broadcast_list, container, false);
		Util.broadcastFlag = true;
		mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.broad_fragment_broadcast_swipe_container);
		mSwipeRefreshLayout.setEnabled(false);
		mBottom_layout=(RelativeLayout)rootView.findViewById(R.id.broad_buttons_layout_broadcast_list);
		mCreate_Broadcast=(ImageButton)rootView.findViewById(R.id.broad_button_createBroadcast);
		mBroadcastList = (ListView) rootView.findViewById(R.id.broad_fragment_broadcast_list);
		mNoBroadcastMessage = (TextView) rootView.findViewById(R.id.broad_fragment_broadcast_noData);
		bottom_Buttons_Layout  = (RelativeLayout) rootView.findViewById(R.id.broad_buttons_layout_broadcast_list);
		return rootView;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mArrayListBroadcast.clear();
		adapter = new BroadcastListAdapter_new(getActivity(), mArrayListBroadcast);
		ConnectionDetector conn = new ConnectionDetector(getActivity());
		if (conn.isConnectingToInternet()) {
			new  GetBroadcastAsync().execute();
		} else {
			alert.showAlertDialog(getActivity(), "Connection Error","Please check your internet connection", false);
		}
		if(Util.ROLE.equalsIgnoreCase("class monitor") || Util.ROLE.equalsIgnoreCase("attendence admin") || Util.ROLE.equalsIgnoreCase("feedback admin")
				|| Util.ROLE.equalsIgnoreCase("internship admin")||Util.ROLE.equalsIgnoreCase("sprit45 admin")||Util.ROLE.equalsIgnoreCase("timetable admin")||
				Util.ROLE.equalsIgnoreCase("teacher")||Util.ROLE.equalsIgnoreCase("admin") ||	Util.ROLE.equalsIgnoreCase("alumni admin")) 
		{
			bottom_Buttons_Layout.setVisibility(View.VISIBLE);
		}
		else
		{
			bottom_Buttons_Layout.setVisibility(View.GONE);
		}


		mBroadcastList.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
				boolean enable = false;

				if(mBroadcastList != null && mBroadcastList.getChildCount() > 0){

					boolean firstItemVisible = mBroadcastList.getFirstVisiblePosition() == 0;
					boolean topOfFirstItemVisible = mBroadcastList.getChildAt(0).getTop() == 0;
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
							new GetBroadcastLoadMoreAsync().execute();
						}else{
							alert.showAlertDialog(getActivity(),"Connection Error","Check your Internet Connection",false);		
						}

					}

				}
			}
		});

		mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				mSwipeRefreshLayout.setRefreshing(true);
				flag_refresh = false;
				ConnectionDetector conn = new ConnectionDetector(getActivity());
				if(conn.isConnectingToInternet()){
					mSwipeRefreshLayout.setRefreshing(false);
					new GetBroadcastRefreshAsyncClass().execute();
				}else{
					alert.showAlertDialog(getActivity(),"Connection Error","Check your Internet Connection",false);		
				}

			}
		});

		mCreate_Broadcast.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),BroadcastCreateActivity_new.class);
				startActivityForResult(intent,UPDATE_RESULT);
			}
		});

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode !=getActivity().RESULT_OK) {
			return;
		}

		Bitmap bitmap;
		switch (requestCode) {

		case UPDATE_RESULT:
			if(resultCode == getActivity().RESULT_OK && null != data)
			{
				
				if(data.getStringExtra("Refresh").equalsIgnoreCase("true"))
				{
					ConnectionDetector conn = new ConnectionDetector(getActivity());
					if(conn.isConnectingToInternet()){
						mArrayListBroadcast.clear();
						pageCount = 0;
						new GetBroadcastAsync().execute();
					}else{
						alert.showAlertDialog(getActivity(), "Connection Error", "Please check your internet connection", false);
					}
				}
				else
				{
					
				}
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data); 
	}
	public class GetBroadcastAsync extends AsyncTask<Void, Void, ArrayList<BroadcastListModel_new>>{

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
		protected ArrayList<BroadcastListModel_new> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONArray broadcastResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(GET_BROADCAST_LIST_URL);

			if(jsonObjectRecived != null){
				//mArrayListBroadcast= new ArrayList<BroadcastListModel_new>();
				try{
					broadcastResponse = jsonObjectRecived.getJSONArray("broadcasts");
				} catch(JSONException e){

				}
				if(broadcastResponse.length()>0)
				{
					for(int i = 0; i< broadcastResponse.length();i++){
						BroadcastListModel_new broadcastData = new BroadcastListModel_new();
						JSONObject broadcastDetails;

						try {
							broadcastDetails = broadcastResponse.getJSONObject(i);
							broadcastData.setBroadcast_id(broadcastDetails.getString("broadcast_id"));
							broadcastData.setBroadcast_user_id(broadcastDetails.getString("broadcast_user_id"));
							broadcastData.setBroadcast_text(broadcastDetails.getString("broadcast_text"));
							broadcastData.setFirstname(broadcastDetails.getString("firstname"));
							broadcastData.setLastname(broadcastDetails.getString("lastname"));
							broadcastData.setRole(broadcastDetails.getString("role"));
							broadcastData.setCreated_by(broadcastDetails.getString("created_by"));
							broadcastData.setCreated_date(broadcastDetails.getString("created_date"));
							pagination_Date_String = broadcastDetails.getString("created_date");
							if(!flag_refresh){
								flag_refresh = true;
								refresh_Date_String = broadcastDetails.getString("created_date");
							}

							broadcastData.setFrom_user_profile_pic(broadcastDetails.getString("from_user_profile_pic"));

							mArrayListBroadcast.add(broadcastData);
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
					return mArrayListBroadcast;
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
		protected void onPostExecute(ArrayList<BroadcastListModel_new> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if(result == null){
				mNoBroadcastMessage.setVisibility(View.VISIBLE);
				Toast.makeText(getActivity(), "No Broadcast Found", Toast.LENGTH_SHORT).show();
			}else if(result.size()==0){
				mNoBroadcastMessage.setVisibility(View.VISIBLE);
			}  else{
				//adapter = new BroadcastListAdapter_new(getActivity(), result);
				mBroadcastList.setAdapter(adapter);

				if(result.size() < 20){
					flag_loading = true;
				}else{
					flag_loading = false;
				}
			}
		}
	}

	private class GetBroadcastLoadMoreAsync extends AsyncTask<Void, Void, ArrayList<BroadcastListModel_new>>{

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
		protected ArrayList<BroadcastListModel_new> doInBackground(Void... params) {
			JSONArray broadcastResponse = null;

			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(GET_BROADCAST_LIST_URL + "?last_date=" + pagination_Date_String.replaceAll(" ", "%20"));

			if(jsonObjectRecived != null){
				try{
					broadcastResponse = jsonObjectRecived.getJSONArray("broadcasts");
				}catch (JSONException e) {
					e.printStackTrace();
				}
				if(broadcastResponse.length() > 0){
					for(int i = 0; i< broadcastResponse.length();i++){
						BroadcastListModel_new broadcastData = new BroadcastListModel_new();
						JSONObject broadcastDetails;

						try {
							broadcastDetails = broadcastResponse.getJSONObject(i);
							broadcastData.setBroadcast_id(broadcastDetails.getString("broadcast_id"));
							broadcastData.setBroadcast_user_id(broadcastDetails.getString("broadcast_user_id"));
							broadcastData.setBroadcast_text(broadcastDetails.getString("broadcast_text"));
							broadcastData.setFirstname(broadcastDetails.getString("firstname"));
							broadcastData.setLastname(broadcastDetails.getString("lastname"));
							broadcastData.setRole(broadcastDetails.getString("role"));
							broadcastData.setCreated_by(broadcastDetails.getString("created_by"));
							broadcastData.setCreated_date(broadcastDetails.getString("created_date"));
							broadcastData.setFrom_user_profile_pic(broadcastDetails.getString("from_user_profile_pic"));

							pagination_Date_String = broadcastDetails.getString("created_date");
							if(!flag_refresh){
								flag_refresh = true;
								refresh_Date_String = broadcastDetails.getString("created_date");
							}

							mArrayListBroadcast.add(broadcastData);
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
					   
					return mArrayListBroadcast;
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

		@SuppressWarnings("unused")
		@Override
		protected void onPostExecute(ArrayList<BroadcastListModel_new> result) {
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
				mBroadcastList.setSelection(pageCount);
			}

		}

	}

	//async for broadcast refresh

	private class GetBroadcastRefreshAsyncClass extends AsyncTask<Void,Void,Void>{

		/*   	 @Override
		 		protected void onPreExecute() {
		 			if(mPDialog == null){
		 				mPDialog = Util.createProgressDialog(getActivity());
		 				mPDialog.setCancelable(false);
		 				mPDialog.show();}
		 			else{
		 				mPDialog.setCancelable(false);
		 				mPDialog.show();
		 			}}*/

		@Override
		protected Void doInBackground(Void... params) {
				 
			JSONObject jsonObjectReceived = HttpGetClient.sendHttpPost(GET_BROADCAST_LIST_URL+"?first_date="+refresh_Date_String.replaceAll(" ", "%20"));
			if(jsonObjectReceived != null){		
				try{
					if(jsonObjectReceived.getString("error").equalsIgnoreCase("false")){
						JSONArray broadcastResponse = jsonObjectReceived.getJSONArray("broadcasts");
						for(int i = 0; i< broadcastResponse.length();i++){
							BroadcastListModel_new broadcastData = new BroadcastListModel_new();
							JSONObject broadcastDetails;

							broadcastDetails = broadcastResponse.getJSONObject(i);
							broadcastData.setBroadcast_id(broadcastDetails.getString("broadcast_id"));
							broadcastData.setBroadcast_user_id(broadcastDetails.getString("broadcast_user_id"));
							broadcastData.setBroadcast_text(broadcastDetails.getString("broadcast_text"));
							broadcastData.setFirstname(broadcastDetails.getString("firstname"));
							broadcastData.setLastname(broadcastDetails.getString("lastname"));
							broadcastData.setRole(broadcastDetails.getString("role"));
							broadcastData.setCreated_by(broadcastDetails.getString("created_by"));
							broadcastData.setCreated_date(broadcastDetails.getString("created_date"));
							pagination_Date_String = broadcastDetails.getString("created_date");
							if(!flag_refresh){
								flag_refresh = true;
								refresh_Date_String = broadcastDetails.getString("created_date");
							}

							broadcastData.setFrom_user_profile_pic(broadcastDetails.getString("from_user_profile_pic"));

							mArrayListBroadcast.add(0,broadcastData);
						}
					}
				}	catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) 
		{
			super.onPostExecute(result);
			//mPDialog.dismiss();
			mSwipeRefreshLayout.setRefreshing(false);

			if(HttpGetClient.statuscode == 200)
			{
				if(mArrayListBroadcast == null){
					Toast.makeText(getActivity(), "No more broadcast to load", Toast.LENGTH_SHORT).show();
				} 
				else if(mArrayListBroadcast.size()==0){
					Toast.makeText(getActivity(), "No more broadcast to load", Toast.LENGTH_SHORT).show();
				}	   
				else{
					//adapter = new BroadcastListAdapter_new(getActivity(),mArrayListBroadcast);
					adapter.notifyDataSetChanged();		     
				}
			}
		}
	}
}


