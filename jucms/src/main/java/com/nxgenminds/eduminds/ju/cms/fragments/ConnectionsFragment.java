package com.nxgenminds.eduminds.ju.cms.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.adapters.ConnectionAdapter;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.models.ConnectionsModel;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.thirdpartyprofile.ThirdPartyTabMenuActivity;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class ConnectionsFragment extends Fragment {

	private GridView connectionGridView;
	private ArrayList<ConnectionsModel> connectionssearch = new ArrayList<ConnectionsModel>();
	private static int pageCount = 0;
	private static String pagination_Date_String = "";
	private boolean flag_loading = false;
	private ProgressDialog pDialog;
	private ConnectionAdapter adapter;
	private TextView NoConnections;
	AlertDialogManager alert = new AlertDialogManager();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.connections, container, false);
		connectionGridView = (GridView) rootView.findViewById(R.id.connectionsgridview);
		NoConnections = (TextView) rootView.findViewById(R.id.NoConnections);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		connectionssearch.clear();
		pageCount = 0;
		ConnectionDetector conn = new ConnectionDetector(getActivity());
		if(conn.isConnectingToInternet()){
			new ConnectionsAsyncClass().execute();

		}else{
			alert.showAlertDialog(getActivity(), "Connection Error", "Please check your internet connection", false);
		}


		connectionGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				Object object = connectionGridView.getItemAtPosition(position);
				ConnectionsModel connection_data = (ConnectionsModel)  object;
				Toast.makeText(getActivity(), connection_data.getUser_id(), Toast.LENGTH_SHORT).show();
				//intent.putExtra("UserID", connection_data.getUser_id());
				Intent intent = new Intent(getActivity(),ThirdPartyTabMenuActivity.class);
				intent.putExtra("UserID", connection_data.getUser_id());
				Util.THIRD_PARTY_NAME  = connection_data.getFirstname();
				startActivity(intent);
			}

		});

		connectionGridView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {	

				if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0)
				{
					if(flag_loading == false)
					{
						flag_loading = true;
						ConnectionDetector conn = new ConnectionDetector(getActivity());
						if(conn.isConnectingToInternet()){
							new ConnectionsLoadMoreAsyncClass().execute();
						}else{
							alert.showAlertDialog(getActivity(), "Connection Error", "Please check your internet connection", false);
						}
					}
				}

			}
		});


	}

	private class ConnectionsAsyncClass extends AsyncTask<Void, Void, ArrayList<ConnectionsModel>>{

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
		protected ArrayList<ConnectionsModel> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONArray connectionsResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(Util.ConnectionAPI+Util.USER_ID);
			if(jsonObjectRecived != null){

				try{
					connectionsResponse = jsonObjectRecived.getJSONArray("connections");
				}catch(JSONException e){
					e.printStackTrace();
				}
				for(int i = 0; i< connectionsResponse.length();i++){
					ConnectionsModel connectionData = new ConnectionsModel();
					JSONObject connectionsDetails;
					try{
						connectionsDetails = connectionsResponse.getJSONObject(i);
						connectionData.setUser_id(connectionsDetails.getString("user_id"));
						connectionData.setFirstname(connectionsDetails.getString("firstname"));
						connectionData.setUser_profile_photo_thumb1(connectionsDetails.getString("user_profile_photo_thumb1"));
						connectionData.setCreated_date(connectionsDetails.getString("created_date"));
						connectionData.setJid(connectionsDetails.getString("jid"));
						pagination_Date_String = connectionsDetails.getString("created_date");
						connectionssearch.add(connectionData);
					}catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return connectionssearch;
			}else{
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<ConnectionsModel> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();

			if(result == null){
				NoConnections.setVisibility(View.VISIBLE);
				NoConnections.setText("You haven't connected to anyone yet!");
			}else if(result.size()==0){
				NoConnections.setVisibility(View.VISIBLE);
				NoConnections.setText("You haven't connected to anyone yet!");
			}  else{
				adapter = new ConnectionAdapter(getActivity(), result);
				connectionGridView.setAdapter(adapter);
				if(result.size() < 40){
					flag_loading = true;
				}else{
					flag_loading = false;
				}
			}
		}
	}


	private class ConnectionsLoadMoreAsyncClass extends AsyncTask<Void, Void, ArrayList<ConnectionsModel>>{

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
		protected ArrayList<ConnectionsModel> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONArray connectionsResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(Util.ConnectionAPI+Util.USER_ID +"&last_date=" + pagination_Date_String.replaceAll(" ", "%20"));
			if(jsonObjectRecived != null){

				try{
					connectionsResponse = jsonObjectRecived.getJSONArray("connections");
				}catch(JSONException e){
					e.printStackTrace();
				}
				if(connectionsResponse.length() > 0){
					for(int i = 0; i< connectionsResponse.length();i++){
						ConnectionsModel connectionData = new ConnectionsModel();
						JSONObject connectionsDetails;
						try{
							connectionsDetails = connectionsResponse.getJSONObject(i);
							connectionData.setUser_id(connectionsDetails.getString("user_id"));
							connectionData.setFirstname(connectionsDetails.getString("firstname"));
							connectionData.setCreated_date(connectionsDetails.getString("created_date"));
							connectionData.setUser_profile_photo_thumb1(connectionsDetails.getString("user_profile_photo_thumb1"));
							connectionData.setJid(connectionsDetails.getString("jid"));
							pagination_Date_String = connectionsDetails.getString("created_date");
							connectionssearch.add(connectionData);
						}catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					return connectionssearch;
				}else{
					return null;
				}
			}else{
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<ConnectionsModel> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if(result == null){
				if(adapter.isEmpty()){
					NoConnections.setVisibility(View.VISIBLE);
					flag_loading = false;
				}
				flag_loading = true;
				Toast.makeText(getActivity(), "No more connections", Toast.LENGTH_SHORT).show();
			}else if(result.size()==0){
				if(adapter.isEmpty()){
					NoConnections.setVisibility(View.VISIBLE);
				}
				flag_loading = true;
				Toast.makeText(getActivity(), "No more connections", Toast.LENGTH_SHORT).show();
				System.out.println("No MoreConnections ");
			}      else{
				adapter = new ConnectionAdapter(getActivity(), result);
				adapter.notifyDataSetChanged();
				flag_loading = false;
				pageCount = pageCount +40;
				connectionGridView.setSelection(pageCount);
			}

		}


	}


}


