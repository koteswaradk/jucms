package com.nxgenminds.eduminds.ju.cms.jgigroup;


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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.models.Posts;
import com.nxgenminds.eduminds.ju.cms.models.Timefeed;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.statusUpdate.UserPostCreateAcitivty;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class GroupFragmentTabGroupFeed extends Fragment{
	private SwipeRefreshLayout swipeLayout;
	private ListView timelogListView;
	private ProgressDialog pDialog;
	private TextView noFeed;
	private String GroupTimeFeedURL = Util.API+ "post?org_post=1";
	private ArrayList<Posts> timeLogSearch = new ArrayList<Posts>();
	private static int pageCount;
	private GroupTimefeedAdapter adapter;
	private ImageButton statusPost;
	private static String pagination_Date_String;
	private boolean flag_loading = false;
	private boolean flag_refresh = false;
	private static String refresh_Date_String;
	AlertDialogManager alert = new AlertDialogManager();
	private RelativeLayout bottom_Buttons_Layout;
	Timefeed feed;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.timefeed, container, false);
		timelogListView = (ListView) rootView.findViewById(R.id.user_timeloglistView);
		noFeed = (TextView) rootView.findViewById(R.id.noFeed);
		statusPost = (ImageButton)rootView.findViewById(R.id.statusbutton);
		swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container_timelogfeed);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		adapter = new GroupTimefeedAdapter(getActivity(), timeLogSearch);
		timeLogSearch.clear();
		pageCount = 0;
		swipeLayout.setColorScheme(android.R.color.holo_blue_bright,android.R.color.holo_green_light, android.R.color.holo_orange_light,android.R.color.holo_red_light);
		ConnectionDetector conn = new ConnectionDetector(getActivity());
		if(conn.isConnectingToInternet()){
			new GetFeedAsyncClass().execute();
		}else{
			alert.showAlertDialog(getActivity(), "Connection Error", "Please check your internet connection", false);
		}

		bottom_Buttons_Layout  = (RelativeLayout) getView().findViewById(R.id.buttom_buttons_layout);
		if(Util.ROLE.equalsIgnoreCase("admin"))
		{
			bottom_Buttons_Layout.setVisibility(View.VISIBLE);
		}
		else
		{
			bottom_Buttons_Layout.setVisibility(View.GONE);
		}
		swipeLayout.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {

				swipeLayout.setRefreshing(true);
				flag_refresh = false;
				ConnectionDetector conn = new ConnectionDetector(getActivity());
				if(conn.isConnectingToInternet()){

					swipeLayout.setRefreshing(false);
					new GetFeedRefreshAsyncClass().execute();
				}else{
					//Crouton.makeText(getActivity(), getString(R.string.crouton_message), Style.ALERT).show();
				}
			}
		});

		timelogListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				boolean enable = false;
				if(timelogListView != null && timelogListView.getChildCount() > 0){
					boolean firstItemVisible = timelogListView.getFirstVisiblePosition() == 0;
					boolean topOfFirstItemVisible = timelogListView.getChildAt(0).getTop() == 0;
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
							new GetFeedLoadMoreAsyncClass().execute();
						}else{
							//Crouton.makeText(getActivity(), getString(R.string.crouton_message), Style.ALERT).show();
						}
					}
				}
			}
		});

		statusPost.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),UserPostCreateAcitivty.class);
				intent.putExtra("Group", "Group");
				startActivity(intent);
			}
		});

	}

	private class GetFeedAsyncClass extends AsyncTask<Void, Void, JSONObject>{

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
		protected JSONObject doInBackground(Void... params) {
			JSONArray feedResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(GroupTimeFeedURL);


			if(jsonObjectRecived != null){
				feed=new Gson().fromJson(jsonObjectRecived.toString(), Timefeed.class);
				for(int i=0 ; i < feed.getPosts().length ; i++)
				{
					Posts postmodel = new Posts();
					postmodel = feed.getPosts()[i];
					pagination_Date_String=feed.getPosts()[i].getCreated_date();
					timeLogSearch.add(postmodel);
					if(!flag_refresh){
						flag_refresh = true;
						refresh_Date_String =feed.getPosts()[i].getCreated_date();
					}
				}
			}
			return jsonObjectRecived;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);

			if(swipeLayout.isRefreshing())
			{
				swipeLayout.setRefreshing(false);
			}
			pDialog.dismiss();
			if(HttpGetClient.statuscode == 500)
			{
				//Crouton.makeText(getActivity(), getString(R.string.crouton_message_serverError), Style.ALERT).show();
				noFeed.setVisibility(View.VISIBLE);
				noFeed.setText("Something went wrong!");

			}
			else if(HttpGetClient.statuscode == 504)
			{
				//Crouton.makeText(getActivity(), getString(R.string.crouton_message_serverGatewayError), Style.ALERT).show();
				noFeed.setVisibility(View.VISIBLE);
				noFeed.setText("Something went wrong!");

			}
			else if(HttpGetClient.statuscode==404)
			{
				//Crouton.makeText(getActivity(), getString(R.string.crouton_message_auth), Style.ALERT).show();
				noFeed.setVisibility(View.VISIBLE);
				noFeed.setText("Something went wrong!");


			}
			else if(HttpGetClient.statuscode == 200)
			{
				try {
					if(result.getJSONArray("posts")== null)
					{
						noFeed.setVisibility(View.VISIBLE);
						noFeed.setText("Check all recent updates from the origanization right here! ");

						swipeLayout.setVisibility(View.GONE);
						//reload.setVisibility(View.VISIBLE);

					} 
					else if(result.getJSONArray("posts").length()<=0)
					{
						noFeed.setVisibility(View.VISIBLE);
						swipeLayout.setVisibility(View.VISIBLE);
						noFeed.setText("Check all recent updates from the origanization right here! ");

						//reload.setVisibility(View.GONE);
					}      
					else
					{
						noFeed.setVisibility(View.GONE);
						timelogListView.setAdapter(adapter);		
						if(timeLogSearch.size() < 20)
						{
							flag_loading = true;
						} else{
							flag_loading = false;
						}

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

	}

	private class GetFeedRefreshAsyncClass extends AsyncTask<Void, Void, ArrayList<Posts>>{

		@Override
		protected ArrayList<Posts> doInBackground(Void... params) {
			JSONArray feedResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(GroupTimeFeedURL+"&first_date="+ refresh_Date_String.replaceAll(" ", "%20"));

			if(jsonObjectRecived != null){
				feed=new Gson().fromJson(jsonObjectRecived.toString(), Timefeed.class);
				for(int i=0 ; i < feed.getPosts().length ; i++)
				{ 
					Posts postmodel = new Posts();
					postmodel = feed.getPosts()[i];
					timeLogSearch.add(postmodel);
					if(!flag_refresh){
						flag_refresh = true;
						refresh_Date_String =feed.getPosts()[i].getCreated_date();
					}
					timeLogSearch.add(0,postmodel);
				}
			}
			return timeLogSearch;
		}

		@Override
		protected void onPostExecute(ArrayList<Posts> result) {
			super.onPostExecute(result);
			swipeLayout.setRefreshing(false);
			if(HttpGetClient.statuscode == 500)
			{
				//Crouton.makeText(getActivity(), getString(R.string.crouton_message_serverError), Style.ALERT).show();
			}
			else if(HttpGetClient.statuscode == 504)
			{
				//Crouton.makeText(getActivity(), getString(R.string.crouton_message_serverGatewayError), Style.ALERT).show();
			}
			else if(HttpGetClient.statuscode==401)
			{
				//Crouton.makeText(getActivity(), getString(R.string.crouton_message_auth), Style.ALERT).show();
			}
			else if(HttpGetClient.statuscode == 200)
			{
				if(result == null)
				{
					Toast.makeText(getActivity(), "No more feeds to load", Toast.LENGTH_SHORT).show();
				} 
				else if(result.size()==0)
				{
					Toast.makeText(getActivity(), "No more feeds to load", Toast.LENGTH_SHORT).show();
				}      
				else
				{
					adapter.notifyDataSetChanged();
				}
			}
		}
	}

	private class GetFeedLoadMoreAsyncClass extends AsyncTask<Void, Void, ArrayList<Posts>>{

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
		protected ArrayList<Posts> doInBackground(Void... params) {
			JSONArray feedResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(GroupTimeFeedURL+"&last_date="+ pagination_Date_String.replaceAll(" ", "%20"));

			if(jsonObjectRecived != null){
				feed=new Gson().fromJson(jsonObjectRecived.toString(), Timefeed.class);
				if(feed.getPosts().length > 0){
					for(int i=0 ; i < feed.getPosts().length ; i++)
					{
						Posts postmodel = new Posts();
						postmodel = feed.getPosts()[i];
						pagination_Date_String=feed.getPosts()[i].getCreated_date();
						timeLogSearch.add(postmodel);

					}
					return timeLogSearch;
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
		protected void onPostExecute(ArrayList<Posts> result) {
			super.onPostExecute(result);
			pDialog.dismiss();
			if(HttpGetClient.statuscode == 500)
			{
				//Crouton.makeText(getActivity(), getString(R.string.crouton_message_serverError), Style.ALERT).show();
			}
			else if(HttpGetClient.statuscode == 504)
			{
				//Crouton.makeText(getActivity(), getString(R.string.crouton_message_serverGatewayError), Style.ALERT).show();
			}
			else if(HttpGetClient.statuscode==401)
			{
				//Crouton.makeText(getActivity(), getString(R.string.crouton_message_auth), Style.ALERT).show();
			}
			else if(HttpGetClient.statuscode == 200)
			{
				if(result == null)
				{
					if(adapter.isEmpty()){
						flag_loading = false;
					}
					flag_loading = true;
				} 
				else if(result.size()==0)
				{
					if(adapter.isEmpty()){
					}
					flag_loading = true;
				}      
				else
				{
					adapter.notifyDataSetChanged();
					flag_loading = false;
					pageCount = pageCount +20;
					timelogListView.setSelection(pageCount-2);
				}
			}
		}

	}
}
