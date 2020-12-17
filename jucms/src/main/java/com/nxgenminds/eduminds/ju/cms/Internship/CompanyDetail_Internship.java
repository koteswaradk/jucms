package com.nxgenminds.eduminds.ju.cms.Internship;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.TextView;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.models.LearningOutComeStudentModel;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class CompanyDetail_Internship extends Fragment{
	private ListView lrngStdOutcomeListView;
	private TextView noData;
	AlertDialogManager alert = new AlertDialogManager();
	private ProgressDialog pDialog;
	private ArrayList<LearningOutComeStudentModel> learningOutcomesArray = new ArrayList<LearningOutComeStudentModel>();
	private CompanyAdmin_InternAdapter adapter;
	private String internshipID;
	private SimpleDateFormat df,ddf;
	private static String formattedDate;
	private static String to,from;
	private static int pageCount = 0;
	private static String pagination_Date_String = "";
	private boolean flag_loading = false;
	JSONObject jsonObjectRecived;
	private final Handler handler = new Handler();
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container,  Bundle savedInstanceState) {
		View rooView = inflater.inflate(R.layout.fragment_company_internship, container , false);
		lrngStdOutcomeListView = (ListView) rooView.findViewById(R.id.company_learningOutcomesListview );
		noData=(TextView)rooView.findViewById(R.id.company_no_learning_outcome);
		CompanyAdminDetailView thirdParty = (CompanyAdminDetailView) getActivity();
		internshipID = thirdParty.InternshipId;
		Log.i("CompanyDetail_Internship", "inside of oncreate from CompanyDetail_Internship");
		return rooView;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		learningOutcomesArray.clear();
		pageCount = 0;
		adapter = new CompanyAdmin_InternAdapter(getActivity(), learningOutcomesArray);
		ConnectionDetector conn = new ConnectionDetector(getActivity());
		if(conn.isConnectingToInternet()){
			new LearningOutcomesStudent().execute();
		}else{
			alert.showAlertDialog(getActivity(), "Connection Error", "Please check your internet connection", false);
		}

		lrngStdOutcomeListView.setOnScrollListener(new OnScrollListener() {

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
							new LearningOutcomesStudentLoadMore().execute();
						}else{
							alert.showAlertDialog(getActivity(), "Connection Error", "Please check your internet connection", false);
						}
					}
				}

			}
		});
	}


	private class LearningOutcomesStudent extends AsyncTask<Void, Void, ArrayList<LearningOutComeStudentModel>>{

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
		protected ArrayList<LearningOutComeStudentModel> doInBackground(Void... params) {
			JSONArray connectionsResponse = null;
			
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(Util.API+"learning_outcome?internship_id="+internshipID);
			if(jsonObjectRecived != null){
           
				try{
					connectionsResponse = jsonObjectRecived.getJSONArray("learn_outcomes");
				}catch(JSONException e){
					e.printStackTrace();
				}
				if(connectionsResponse.length()>0){
					for(int i = 0; i< connectionsResponse.length();i++){
						LearningOutComeStudentModel outcome = new LearningOutComeStudentModel();
						JSONObject outcomeDetails;
						try{
							outcomeDetails = connectionsResponse.getJSONObject(i);
							outcome.setFrom_date(outcomeDetails.getString("from_date"));
							outcome.setTo_date(outcomeDetails.getString("to_date"));
							outcome.setComment(outcomeDetails.getString("comment"));
							outcome.setCreated_date(outcomeDetails.getString("created_date"));
							outcome.setIs_approved(outcomeDetails.getString("is_approved"));
							outcome.setApproval_remarks(outcomeDetails.getString("approval_remarks"));
							outcome.setIs_approval_remarks(outcomeDetails.getString("is_approval_remarks"));
							outcome.setLearning_outcome_id(outcomeDetails.getString("learning_outcome_id"));
							pagination_Date_String=outcomeDetails.getString("created_date");
							learningOutcomesArray.add(outcome);
						}catch (JSONException e) {
							e.printStackTrace();
						}
					}
					return learningOutcomesArray;
				}
				else
				{
					return null;
				}

			}else{
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<LearningOutComeStudentModel> result) {
			super.onPostExecute(result);
			pDialog.dismiss();

			if(result == null){
				noData.setVisibility(View.VISIBLE);

			}else if(result.size()==0){
				noData.setVisibility(View.VISIBLE);

			}  else{
				noData.setVisibility(View.GONE);
				lrngStdOutcomeListView.setAdapter(adapter);
				if(result.size() < 20){
					flag_loading = true;
				}else{
					flag_loading = false;
				}
			}
		}
	}
	private class LearningOutcomesStudentLoadMore extends AsyncTask<Void, Void, ArrayList<LearningOutComeStudentModel>>{

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
		protected ArrayList<LearningOutComeStudentModel> doInBackground(Void... params) {
			JSONArray connectionsResponse = null;
			
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(Util.API+"learning_outcome?internship_id="+internshipID+"&last_date="+ pagination_Date_String.replaceAll(" ", "%20"));
			if(jsonObjectRecived != null){

				try{
					connectionsResponse = jsonObjectRecived.getJSONArray("learn_outcomes");
				}catch(JSONException e){
					e.printStackTrace();
				}
				if(connectionsResponse.length()>0){
					for(int i = 0; i< connectionsResponse.length();i++){
						LearningOutComeStudentModel outcome = new LearningOutComeStudentModel();
						JSONObject outcomeDetails;
						try{
							outcomeDetails = connectionsResponse.getJSONObject(i);
							outcome.setFrom_date(outcomeDetails.getString("from_date"));
							outcome.setTo_date(outcomeDetails.getString("to_date"));
							outcome.setComment(outcomeDetails.getString("comment"));
							outcome.setCreated_date(outcomeDetails.getString("created_date"));
							outcome.setIs_approved(outcomeDetails.getString("is_approved"));
							outcome.setApproval_remarks(outcomeDetails.getString("approval_remarks"));
							outcome.setIs_approval_remarks(outcomeDetails.getString("is_approval_remarks"));
							outcome.setLearning_outcome_id(outcomeDetails.getString("learning_outcome_id"));
							pagination_Date_String=outcomeDetails.getString("created_date");
							learningOutcomesArray.add(outcome);
						}catch (JSONException e) {
							e.printStackTrace();
						}
						
					}
					return learningOutcomesArray;
				}
				else
				{
					return null;
				}

			}else{
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<LearningOutComeStudentModel> result) {
			super.onPostExecute(result);
			pDialog.dismiss();

			if(result == null){
			}else if(result.size()==0){
			}  else{
				adapter.notifyDataSetChanged();
				flag_loading = false;
				pageCount = pageCount +20;
				lrngStdOutcomeListView.setSelection(pageCount);}
		}
	}
}
