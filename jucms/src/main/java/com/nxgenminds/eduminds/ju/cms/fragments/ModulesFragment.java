package com.nxgenminds.eduminds.ju.cms.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.adapters.ModulesAdapter;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.models.ModulesModule;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class ModulesFragment extends Fragment{

	private ListView modulesListView;
	private ArrayList<ModulesModule> modulesArrayList = new ArrayList<ModulesModule>();
	private ModulesAdapter adapter;
	private ProgressDialog pDialog;
	private TextView noData;
	
	private AlertDialogManager alert = new AlertDialogManager();
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container,  Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.modules_fragment, container , false);
		modulesListView = (ListView) rootView.findViewById(R.id.modulesListView);
		noData=(TextView)rootView.findViewById(R.id.noModuleData);
		return rootView;
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		modulesArrayList.clear();
		
		ConnectionDetector conn = new ConnectionDetector(getActivity());
        if(conn.isConnectingToInternet()){
        	new GetModulesAsync().execute();
        } else{
        	alert.showAlertDialog(getActivity(),"Connection Error","Check your Internet Connection",false);
        }
		
	}


	private class GetModulesAsync extends AsyncTask<Void, Void, ArrayList<ModulesModule>>{
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
		protected ArrayList<ModulesModule> doInBackground(Void... params) {
			JSONArray topicsResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(Util.API+"module_admin?stream_id=" + Util.STREAM_ID 
					+ "&semester_id=" + Util.SEMESTER_ID + "&section_id=" +Util.SECTION_ID);
			if(jsonObjectRecived != null){
				try{
					topicsResponse = jsonObjectRecived.getJSONArray("subjects");
				}catch(JSONException e){
					e.printStackTrace();
				}
				if(topicsResponse.length() > 0){
					for(int i = 0; i< topicsResponse.length();i++){
						ModulesModule modulesData = new ModulesModule();
						JSONObject topicsDetails;
						try{
							topicsDetails = topicsResponse.getJSONObject(i);
							modulesData.setSubject_name(topicsDetails.getString("subject_name"));
							modulesData.setSubject_description(topicsDetails.getString("subject_description"));
							modulesData.setDownload_file_name(topicsDetails.getString("download_file_name"));
							modulesData.setDownload_file_path(topicsDetails.getString("download_file_path"));
							modulesArrayList.add(modulesData);
						}catch (JSONException e) {
							e.printStackTrace();
						}
					}
					return modulesArrayList;
				}else{
					return null;
				}
			}else{
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<ModulesModule> result) {
			super.onPostExecute(result);
			pDialog.dismiss();
			if(result == null ||result.size()==0){
				noData.setVisibility(View.VISIBLE);
			}else{
				noData.setVisibility(View.GONE);
				adapter = new ModulesAdapter(getActivity(), result);
				modulesListView.setAdapter(adapter);
			}
		}
	}



}
