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
import com.nxgenminds.eduminds.ju.cms.adapters.StudentWorkedAtAdapter;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.models.StudentInternshipModel;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class StudentWorkedAtFragment extends Fragment{

	private ListView compinesListview;
	private ProgressDialog pDialog;
	private ArrayList<StudentInternshipModel> internCompaniessearch = new ArrayList<StudentInternshipModel>();
	private StudentWorkedAtAdapter adapter;
	private TextView noData;
	public String FriendID;
	JSONObject jsonObjectRecived;

	private AlertDialogManager alert = new AlertDialogManager();
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.studentworked_fragment, container, false);
		compinesListview = (ListView) rootView.findViewById(R.id.studentworkedlistview);
		noData=(TextView)rootView.findViewById(R.id.no_worked_data);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		FriendID = Util.THIRD_PARTY_ID;
		
		ConnectionDetector conn = new ConnectionDetector(getActivity());
        if(conn.isConnectingToInternet()){
        	new InternCopaniesAsyncClass().execute();
        } else{
        	alert.showAlertDialog(getActivity(),"Connection Error","Check your Internet Connection",false);
        }
		
	}



	private class InternCopaniesAsyncClass extends AsyncTask<Void, Void, ArrayList<StudentInternshipModel>>{

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
		protected ArrayList<StudentInternshipModel> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONArray connectionsResponse = null;
			if(Util.intership_flag)
			{
				jsonObjectRecived = HttpGetClient.sendHttpPost(Util.API+"internship?student_user_id="+FriendID+"&currently_working=0");
			}
			else
			{
				jsonObjectRecived = HttpGetClient.sendHttpPost(Util.API+"internship?student_user_id="+Util.USER_ID+"&currently_working=0");

			}
			if(jsonObjectRecived != null){

				try{
					connectionsResponse = jsonObjectRecived.getJSONArray("internships");
				}catch(JSONException e){
					e.printStackTrace();
				}
				if(connectionsResponse.length() > 0){
					for(int i = 0; i< connectionsResponse.length();i++){
						StudentInternshipModel internCompaniesData = new StudentInternshipModel();
						JSONObject comapniesDatiles;
						try{
							comapniesDatiles = connectionsResponse.getJSONObject(i);

							internCompaniesData.setCompany_name(comapniesDatiles.getString("company_name"));
							internCompaniesData.setCompany_logo_path(comapniesDatiles.getString("company_logo_path"));
							internCompaniesData.setDesignation(comapniesDatiles.getString("designation"));
							internCompaniesData.setDepartment(comapniesDatiles.getString("department"));
							internCompaniesData.setFrom_date(comapniesDatiles.getString("from_date"));
							internCompaniesData.setTo_date(comapniesDatiles.getString("to_date"));
							internCompaniessearch.add(internCompaniesData);
						}catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					return internCompaniessearch;
				}

			}
			return null;

		}

		@Override
		protected void onPostExecute(ArrayList<StudentInternshipModel> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();

			if(result == null){
				noData.setVisibility(View.VISIBLE);
				//NoConnections.setText("You haven't connected to anyone yet!");
			}else if(result.size()<=0){
				noData.setVisibility(View.VISIBLE);
				//NoConnections.setText("You haven't connected to anyone yet!");
			}  else{
				noData.setVisibility(View.GONE);

				adapter = new StudentWorkedAtAdapter(getActivity(), result);
				compinesListview.setAdapter(adapter);
			}
		}
	}


}
