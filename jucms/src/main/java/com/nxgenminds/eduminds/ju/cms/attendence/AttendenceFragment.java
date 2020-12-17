package com.nxgenminds.eduminds.ju.cms.attendence;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class AttendenceFragment extends Fragment{

	private ListView attendenceListView;
	private TextView noSubjects;
	private ProgressDialog pDialog;
	private ArrayList<AttendenceSubjectModel> subjects = new ArrayList<AttendenceSubjectModel>();
	private AttendenceSubjectAdapter adapter;
	
	private AlertDialogManager alert = new AlertDialogManager();

	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container,  Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.attendence_fragment, container , false);
		attendenceListView = (ListView) rootView.findViewById(R.id.attendenceListView);
		noSubjects = (TextView) rootView.findViewById(R.id.NoSubjectsText);
		return rootView;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		subjects.clear();
		
		ConnectionDetector conn = new ConnectionDetector(getActivity());
        if(conn.isConnectingToInternet()){
        	new GetSubjectsAsync().execute();
        } else{
        	alert.showAlertDialog(getActivity(),"Connection Error","Check your Internet Connection",false);
        }
		
		attendenceListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Object object = attendenceListView.getItemAtPosition(position);
				AttendenceSubjectModel subjectsItem = (AttendenceSubjectModel) object;
				// call a intent
				Intent intent = new Intent(getActivity(),TakeAttendenceActivtiy.class);
				intent.putExtra("SectionID", subjectsItem.getSection_id());
				intent.putExtra("SemesterID", subjectsItem.getSemester_id());
				intent.putExtra("StreamID", subjectsItem.getStream_id());
				intent.putExtra("SlotTimeStart", subjectsItem.getSlot_start_time());
				intent.putExtra("SlotTimeEnd", subjectsItem.getSlot_end_time());
				intent.putExtra("TimeSlotID", subjectsItem.getTimetable_slot_id());
				intent.putExtra("SubjectID", subjectsItem.getSubject_id());
				intent.putExtra("TimeTableDate", subjectsItem.getTimetable_date());

				startActivity(intent);

			}
		});
	}



	private class GetSubjectsAsync extends AsyncTask<Void, Void, ArrayList<AttendenceSubjectModel>>{

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
		protected ArrayList<AttendenceSubjectModel> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONArray topicsResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(Util.API+"class_attendence/" +Util.USER_ID);
			if(jsonObjectRecived != null){
				try{
					topicsResponse = jsonObjectRecived.getJSONArray("teacher_subjects");
				}catch(JSONException e){
					e.printStackTrace();
				}
				if(topicsResponse.length() > 0){
					for(int i = 0; i< topicsResponse.length();i++){
						AttendenceSubjectModel subjectsData = new AttendenceSubjectModel();
						JSONObject subjectDetails;
						try{
							subjectDetails = topicsResponse.getJSONObject(i);
							//	XsubjectsData.setTimetable_slot_id(subjectDetails.getString("timetable_slot_id"));
							subjectsData.setTimetable_date(subjectDetails.getString("timetable_date"));
							subjectsData.setStream_name(subjectDetails.getString("stream_name"));
							subjectsData.setSemester(subjectDetails.getString("semester"));
							subjectsData.setSection_name(subjectDetails.getString("section_name"));
							subjectsData.setStream_id(subjectDetails.getString("stream_id"));
							subjectsData.setSemester_id(subjectDetails.getString("semester_id"));
							subjectsData.setSection_id(subjectDetails.getString("section_id"));
							subjectsData.setSubject_name(subjectDetails.getString("subject_name"));
							subjectsData.setSubject_id(subjectDetails.getString("subject_id"));
							subjectsData.setSlot_start_time(subjectDetails.getString("slot_start_time"));
							subjectsData.setSlot_end_time(subjectDetails.getString("slot_end_time"));
							subjects.add(subjectsData);
						}catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					return subjects;
				}else{
					return null;
				}
			}else{
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<AttendenceSubjectModel> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if(result == null || result.size()==0){
				noSubjects.setVisibility(View.VISIBLE);
			}else{
				noSubjects.setVisibility(View.GONE);
				adapter = new AttendenceSubjectAdapter(getActivity(), result);
				attendenceListView.setAdapter(adapter);
			}
		}
	}

}
