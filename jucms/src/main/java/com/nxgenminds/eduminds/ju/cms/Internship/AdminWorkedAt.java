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
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.thirdpartyprofile.ThirdPartyTabMenuActivity;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class AdminWorkedAt extends Fragment{
	private GridView mStudentsGridView;
	private ArrayList<ModelCompanyAdmin> mStudentsArrayList = new ArrayList<ModelCompanyAdmin>();
	private static int pageCount = 0;
	private static String pagination_Date_String = "";
	private boolean flag_loading = false;
	private ProgressDialog pDialog;
	private TextView NoStudents;
	AlertDialogManager alert = new AlertDialogManager();
	CompanyAdminAdapter adapter;
	private String companyID;
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,  Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_company_admin, container , false);
		mStudentsGridView = (GridView) rootView.findViewById(R.id.Intern_gridview);
		NoStudents = (TextView)rootView.findViewById(R.id.noIntern);
		AdminDetailView admin = (AdminDetailView) getActivity();
		companyID = admin.companyId;
		return rootView;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mStudentsArrayList.clear();
		pageCount = 0;
		adapter = new CompanyAdminAdapter(getActivity(), mStudentsArrayList);
		ConnectionDetector conn = new ConnectionDetector(getActivity());
		if(conn.isConnectingToInternet()){
			new StudentsAsyncClass().execute();
		}else{
			alert.showAlertDialog(getActivity(), "Connection Error", "Please check your internet connection", false);
		}


		mStudentsGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,long id) {/*
				Object object = mStudentsGridView.getItemAtPosition(position);
				ModelCompanyAdmin cm_admin_data = (ModelCompanyAdmin)  object;
				Intent intent = new Intent(getActivity(),CompanyAdminDetailView.class);
				intent.putExtra("studentId", cm_admin_data.getStudent_user_id());
				intent.putExtra("studentName", cm_admin_data.getFirstname());
				intent.putExtra("internshipId", cm_admin_data.getInternship_id());
				startActivity(intent);
			*/	
				ModelCompanyAdmin studentData = mStudentsArrayList.get(position);
				Intent intent = new Intent(getActivity(),ThirdPartyTabMenuActivity.class);
				intent.putExtra("UserID",studentData.getStudent_user_id());
				intent.putExtra("ThirdPartyRole","student");
				Util.THIRD_PARTY_NAME  = studentData.getFirstname();
				Util.THIRD_PARTY_ID = studentData.getStudent_user_id();
				startActivity(intent);
				Util.intership_flag=true;
				
				
			}

		});

		mStudentsGridView.setOnScrollListener(new OnScrollListener() {

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
							new StudentsLoadMoreAsyncClass().execute();
						}else{
							alert.showAlertDialog(getActivity(), "Connection Error", "Please check your internet connection", false);
						}
					}
				}

			}
		});
	}
	private class StudentsAsyncClass extends AsyncTask<Void, Void, ArrayList<ModelCompanyAdmin>>{

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
		protected  ArrayList<ModelCompanyAdmin> doInBackground(Void... params) {
			JSONArray mStudentsResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(Util.API+"worked_at?company_id="+companyID+"&currently_working=0");

			if(jsonObjectRecived != null){

				try{
					mStudentsResponse = jsonObjectRecived.getJSONArray("currently_working");
				}catch(JSONException e){
					e.printStackTrace();
				}
				if(mStudentsResponse.length()>0){
					for(int i = 0; i<mStudentsResponse.length();i++){
						ModelCompanyAdmin mStudentData = new ModelCompanyAdmin();
						JSONObject mStudentDetails;
						try{
							mStudentDetails = mStudentsResponse.getJSONObject(i);
							mStudentData.setInternship_id(mStudentDetails.getString("internship_id"));
							mStudentData.setFirstname(mStudentDetails.getString("firstname"));
							mStudentData.setLastname(mStudentDetails.getString("lastname"));
							mStudentData.setCompany_id(mStudentDetails.getString("company_id"));
							mStudentData.setDepartment(mStudentDetails.getString("department"));
							mStudentData.setStudent_user_id(mStudentDetails.getString("student_user_id"));
							mStudentData.setDesignation(mStudentDetails.getString("designation"));
							mStudentData.setFrom_date(mStudentDetails.getString("from_date"));
							mStudentData.setTo_date(mStudentDetails.getString("to_date"));
							mStudentData.setMentor_name(mStudentDetails.getString("mentor_name"));
							mStudentData.setMentor_designation(mStudentDetails.getString("mentor_designation"));
							mStudentData.setMentor_email(mStudentDetails.getString("mentor_email"));
							mStudentData.setMentor_phone(mStudentDetails.getString("mentor_phone"));
							mStudentData.setCurrently_working(mStudentDetails.getString("currently_working"));
							mStudentData.setCompany_name(mStudentDetails.getString("company_name"));
							mStudentData.setCompany_logo_path(mStudentDetails.getString("company_logo_path"));
							mStudentData.setCompany_desc(mStudentDetails.getString("company_desc"));
							mStudentData.setProfile_photo(mStudentDetails.getString("profile_photo"));
							pagination_Date_String = mStudentDetails.getString("internship_id");
							mStudentsArrayList.add(mStudentData);
						}catch (JSONException e) {
							e.printStackTrace();
						}
					}
					return mStudentsArrayList;
				}else
				{
					return null;
				}

			}else{
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<ModelCompanyAdmin> result) {
			super.onPostExecute(result);
			pDialog.dismiss();

			if(result == null){
				NoStudents.setVisibility(View.VISIBLE);
				NoStudents.setText("No data found !");
			} 
			else if(result.size()<=0){
				NoStudents.setVisibility(View.VISIBLE);
				NoStudents.setText("No data found !");
			}	
			else{
				NoStudents.setVisibility(View.GONE);
				mStudentsGridView.setAdapter(adapter);
				if(result.size() < 20){
					flag_loading = true;
				}else{
					flag_loading = false;
				}
			}

		}
	}


	private class StudentsLoadMoreAsyncClass extends AsyncTask<Void, Void, ArrayList<ModelCompanyAdmin>>{

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
		protected ArrayList<ModelCompanyAdmin> doInBackground(Void... params) {
			JSONArray mStudentsResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(Util.API+"worked_at?company_id="+companyID+"&currently_working=0"+"&last_date=" + pagination_Date_String.replaceAll(" ", "%20"));
			if(jsonObjectRecived != null){

				try{
					mStudentsResponse = jsonObjectRecived.getJSONArray("currently_working");
				}catch(JSONException e){
					e.printStackTrace();
				}
				if(mStudentsResponse.length()>0){
					for(int i = 0; i<mStudentsResponse.length();i++){
						ModelCompanyAdmin mStudentData = new ModelCompanyAdmin();
						JSONObject mStudentDetails;
						try{
							mStudentDetails = mStudentsResponse.getJSONObject(i);
							mStudentData.setInternship_id(mStudentDetails.getString("internship_id"));
							mStudentData.setFirstname(mStudentDetails.getString("firstname"));
							mStudentData.setLastname(mStudentDetails.getString("lastname"));
							mStudentData.setCompany_id(mStudentDetails.getString("company_id"));
							mStudentData.setDepartment(mStudentDetails.getString("department"));
							mStudentData.setStudent_user_id(mStudentDetails.getString("student_user_id"));
							mStudentData.setDesignation(mStudentDetails.getString("designation"));
							mStudentData.setFrom_date(mStudentDetails.getString("from_date"));
							mStudentData.setTo_date(mStudentDetails.getString("to_date"));
							mStudentData.setMentor_name(mStudentDetails.getString("mentor_name"));
							mStudentData.setMentor_designation(mStudentDetails.getString("mentor_designation"));
							mStudentData.setMentor_email(mStudentDetails.getString("mentor_email"));
							mStudentData.setMentor_phone(mStudentDetails.getString("mentor_phone"));
							mStudentData.setCurrently_working(mStudentDetails.getString("currently_working"));
							mStudentData.setCompany_name(mStudentDetails.getString("company_name"));
							mStudentData.setCompany_logo_path(mStudentDetails.getString("company_logo_path"));
							mStudentData.setCompany_desc(mStudentDetails.getString("company_desc"));
							mStudentData.setProfile_photo(mStudentDetails.getString("profile_photo"));
							pagination_Date_String = mStudentDetails.getString("internship_id");
							mStudentsArrayList.add(mStudentData);
						}catch (JSONException e) {
							e.printStackTrace();
						}
					}
					return mStudentsArrayList;
				}else
				{
					return null;
				}

			}else{
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<ModelCompanyAdmin> result) {
			super.onPostExecute(result);
			pDialog.dismiss();
			if(result == null){
				flag_loading = true;
				Toast.makeText(getActivity(), "No more data to load!", Toast.LENGTH_SHORT).show();
			} else if(result.size()<0){
				flag_loading = true;
				Toast.makeText(getActivity(), "No more data to load", Toast.LENGTH_SHORT).show();
				
			}

			else{
				adapter.notifyDataSetChanged();
				flag_loading = false;
				pageCount = pageCount +20;
				mStudentsGridView.setSelection(pageCount);
			}

		}
	}
}
