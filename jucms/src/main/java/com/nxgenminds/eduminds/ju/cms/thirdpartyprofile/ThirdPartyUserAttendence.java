package com.nxgenminds.eduminds.ju.cms.thirdpartyprofile;

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
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.models.UserAttendenceModel;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.userprofile.UserAttendenceAdapter;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class ThirdPartyUserAttendence extends Fragment {

	private ListView attendenceListView;
	private TextView noData;
	private static int pageCount = 0;
	private String NotificationURL = Util.API + "attendence?student_id=";
	private ArrayList<UserAttendenceModel> attendenceSearch = new ArrayList<UserAttendenceModel>();
	private ProgressDialog pDialog;
	AlertDialogManager alert = new AlertDialogManager();
	private UserAttendenceAdapter adapter;
	JSONObject jsonObjectRecived;
	private String friedID;


	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_user_attendence, container, false);
		attendenceListView = (ListView) rootView.findViewById(R.id.userAttendencelistview);
		noData = (TextView) rootView.findViewById(R.id.noAttendence);
		ThirdPartyTabMenuActivity thirdParty = (ThirdPartyTabMenuActivity) getActivity();
		friedID = thirdParty.FriendID;
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		attendenceSearch.clear();
		pageCount = 0;
		adapter = new UserAttendenceAdapter(getActivity(), attendenceSearch);
		ConnectionDetector conn = new ConnectionDetector(getActivity());
		if(conn.isConnectingToInternet()){
			new GetAttendenceAsyncClass().execute();
		}else{
			alert.showAlertDialog(getActivity(), "Connection Error", "Please check your internet connection", false);
		}


	}

	private class GetAttendenceAsyncClass extends AsyncTask<Void, Void, ArrayList<UserAttendenceModel>>{

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
		protected ArrayList<UserAttendenceModel> doInBackground(Void... params) {
			JSONArray attendenceResponse = null;
			jsonObjectRecived = HttpGetClient.sendHttpPost(NotificationURL+friedID);

			if(jsonObjectRecived != null){

				try{
					attendenceResponse = jsonObjectRecived.getJSONArray("attendenceDet");
					for(int i = 0; i< attendenceResponse.length();i++){
						UserAttendenceModel attendenceData = new UserAttendenceModel();
						JSONObject attendenceDetails;
						try{
							attendenceDetails = attendenceResponse.getJSONObject(i);
							attendenceData.setHex_colorcode(attendenceDetails.getString("hex_colorcode"));
							attendenceData.setAttendence_percentage(attendenceDetails.getString("attendence_percentage"));
							attendenceData.setGrade(attendenceDetails.getString("grade"));
							attendenceData.setSubject_name(attendenceDetails.getString("subject_name"));
							attendenceData.setTotal_classes(attendenceDetails.getString("total_classes"));
							attendenceData.setTotal_presents(attendenceDetails.getString("total_presents"));
							attendenceData.setRgb_colorcode(attendenceDetails.getString("rgb_colorcode"));
							attendenceSearch.add(attendenceData);
						}catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}catch(JSONException e){
					e.printStackTrace();
				}

				return attendenceSearch;
			}else{
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<UserAttendenceModel> result) {
			super.onPostExecute(result);
			pDialog.dismiss();
			if(result == null){
				noData.setVisibility(View.VISIBLE);
			}else if(result.size()==0){
				noData.setVisibility(View.VISIBLE);
			}        else{
				attendenceListView.setAdapter(adapter);

			}

		}

	}


}
