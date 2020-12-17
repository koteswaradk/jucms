package com.nxgenminds.eduminds.ju.cms.Internship;

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
import android.widget.ListView;
import android.widget.TextView;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.models.ComanyModel;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class AdminInternship extends Fragment{
	private ListView internListView;
	JSONArray notificationsResponse;
	private TextView noData;
	private static int pageCount = 0;
	private String internURL = Util.API + "company";
	private ArrayList<ComanyModel> companySearch = new ArrayList<ComanyModel>();
	private ProgressDialog pDialog;
	private static String pagination_Date_String;
	private boolean flag_loading = false;
	AlertDialogManager alert = new AlertDialogManager();
	private AdminIntershipAdapter adapter;
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container,  Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_admin_internship, container , false);
		internListView = (ListView) rootView.findViewById(R.id.adminIntern_listview);
		noData = (TextView) rootView.findViewById(R.id.noData_adminIntern);
		return rootView;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		companySearch.clear();
		pageCount = 0;
		adapter = new AdminIntershipAdapter(getActivity(), companySearch);
		ConnectionDetector conn = new ConnectionDetector(getActivity());
		if(conn.isConnectingToInternet()){
			new GetCompanyAsyncClass().execute();
		}else{
			alert.showAlertDialog(getActivity(), "Connection Error", "Please check your internet connection", false);
		}

		internListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

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
							new GetCompanyLoadMoreAsyncClass().execute();
						}else{
							//Crouton.makeText(getActivity(), getString(R.string.crouton_message), Style.ALERT).show();
						}
					}
				}
			}
		});

		internListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
				Object object = internListView.getItemAtPosition(position);
				ComanyModel admin_data = (ComanyModel)  object;
				Intent intent = new Intent(getActivity(),AdminDetailView.class);
				intent.putExtra("companyId",admin_data.getCompany_id());
				startActivity(intent);
			}

		});
	}

	private class GetCompanyAsyncClass extends AsyncTask<Void, Void, ArrayList<ComanyModel>>{

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
		protected ArrayList<ComanyModel> doInBackground(Void... params) {
			JSONArray connectionsResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(internURL);
			if(jsonObjectRecived != null){

				try{
					connectionsResponse = jsonObjectRecived.getJSONArray("companies");
					for(int i = 0; i< connectionsResponse.length();i++){
						ComanyModel companyData = new ComanyModel();
						JSONObject companyDetails;
						try{
							companyDetails = connectionsResponse.getJSONObject(i);
							companyData.setCity_id(companyDetails.getString("city_id"));
							companyData.setCity_name(companyDetails.getString("city_name"));
							companyData.setCompany_desc(companyDetails.getString("company_desc"));
							companyData.setCompany_id(companyDetails.getString("company_id"));
							companyData.setCompany_logo_path(companyDetails.getString("company_logo_path"));
							companyData.setCompany_name(companyDetails.getString("company_name"));
							companyData.setCompany_url(companyDetails.getString("company_url"));
							companyData.setCompany_website(companyDetails.getString("company_website"));
							companyData.setCreated_date(companyDetails.getString("created_date"));
							pagination_Date_String = companyDetails.getString("created_date");
							companySearch.add(companyData);
						}catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}catch(JSONException e){
					e.printStackTrace();
				}

				return companySearch;
			}else{
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<ComanyModel> result) {
			super.onPostExecute(result);

			pDialog.dismiss();
			if(result == null){
				noData.setVisibility(View.VISIBLE);
			}else if(result.size()==0){
				noData.setVisibility(View.VISIBLE);
			}        else{
				internListView.setAdapter(adapter);
				if(result.size() < 20){
					flag_loading = true;
				}else{
					flag_loading = false;
				}

			}

		}

	}



	private class GetCompanyLoadMoreAsyncClass extends AsyncTask<Void, Void, ArrayList<ComanyModel>>{

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
		protected ArrayList<ComanyModel> doInBackground(Void... params) {
			JSONArray connectionsResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(internURL+"?last_date="+ pagination_Date_String.replaceAll(" ", "%20"));
			if(jsonObjectRecived != null){

				try{
					connectionsResponse = jsonObjectRecived.getJSONArray("companies");
				}catch(JSONException e){
					e.printStackTrace();
				}
				if(connectionsResponse.length() > 0){
					for(int i = 0; i< connectionsResponse.length();i++){
						ComanyModel companyData = new ComanyModel();
						JSONObject companyDetails;
						try{
							companyDetails = connectionsResponse.getJSONObject(i);
							companyData.setCity_id(companyDetails.getString("city_id"));
							companyData.setCity_name(companyDetails.getString("city_name"));
							companyData.setCompany_desc(companyDetails.getString("company_desc"));
							companyData.setCompany_id(companyDetails.getString("company_id"));
							companyData.setCompany_logo_path(companyDetails.getString("company_logo_path"));
							companyData.setCompany_name(companyDetails.getString("company_name"));
							companyData.setCompany_url(companyDetails.getString("company_url"));
							companyData.setCompany_website(companyDetails.getString("company_website"));
							companyData.setCreated_date(companyDetails.getString("created_date"));
							pagination_Date_String = companyDetails.getString("created_date");
							companySearch.add(companyData);
						}catch (JSONException e) {
							e.printStackTrace();
						}
					}
					return companySearch;
				}else{
					return null;
				}
			}else{
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<ComanyModel> result) {
			super.onPostExecute(result);
			pDialog.dismiss();
			if(result == null){

				flag_loading = true;
			}else if(result.size()==0){

				flag_loading = true;
			}      else{
				adapter.notifyDataSetChanged();
				flag_loading = false;
				pageCount = pageCount +20;
				internListView.setSelection(pageCount);

			}

		}

	}

}
