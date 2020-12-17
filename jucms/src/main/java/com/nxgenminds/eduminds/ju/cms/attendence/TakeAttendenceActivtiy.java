package com.nxgenminds.eduminds.ju.cms.attendence;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpPostClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class TakeAttendenceActivtiy extends Activity{

	private String SectionID;
	private String SemesterID;
	private String StreamID;
	//private String TimeSlotID;
	private String SubjectID;
	private String SlotTimeStart;
	private String SlotTimeEnd;
	private String TimeTableDate;

	private Button submit;
	private Spinner selectClassSpinner;
	private ListView studentsListView;
	private TextView displayTimeslot,noData;
	private ArrayList<AttendenceStudentsModel> students = new ArrayList<AttendenceStudentsModel>();
	private TakeAttendenceAdapter adapter;
	private String classType; 
	private ProgressDialog pDialog;
	private JSONObject jsonObjRecv;

	private ArrayList<String> presentStudents = new ArrayList<String>();
	private ArrayList<String> abcentStudents = new ArrayList<String>();
	private String presentsIds;
	private String abcentIds;
	
	private AlertDialogManager alert = new AlertDialogManager();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_take_attendence);
		Bundle extras = getIntent().getExtras();
		SectionID = extras.getString("SectionID");
		SemesterID = extras.getString("SemesterID");
		StreamID = extras.getString("StreamID");
		SlotTimeStart = extras.getString("SlotTimeStart");
		SlotTimeEnd = extras.getString("SlotTimeEnd");
		//TimeSlotID = extras.getString("TimeSlotID");
		SubjectID = extras.getString("SubjectID");

	
		List<String> list = new ArrayList<String>();
		list.add("Regular Class");
		list.add("Extra Class 1");
		list.add("Extra Class 2");
		list.add("Extra Class 3");
		list.add("Extra Class 4");
		list.add("Extra Class 5");

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,R.layout.custom_spinner_item,list);

		TimeTableDate = extras.getString("TimeTableDate");
		submit = (Button) findViewById(R.id.submitAttendence);
		selectClassSpinner = (Spinner) findViewById(R.id.classtypeSpinner);
		displayTimeslot = (TextView) findViewById(R.id.timeslotdisplay);
		studentsListView = (ListView) findViewById(R.id.studentsListview);
		noData=(TextView)findViewById(R.id.atten_noData);
		displayTimeslot.setText(SlotTimeStart + "-" + SlotTimeEnd);

		selectClassSpinner.setAdapter(dataAdapter);

		ConnectionDetector conn = new ConnectionDetector(TakeAttendenceActivtiy.this);
        if(conn.isConnectingToInternet()){
        	new GetStudentAttendenceAsync().execute();
        } else{
        	alert.showAlertDialog(TakeAttendenceActivtiy.this,"Connection Error","Check your Internet Connection",false);
        }
		
		

		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for(AttendenceStudentsModel student : students){
					if(student.getPresentStatus().equalsIgnoreCase("1")){
						presentStudents.add(student.getUser_id().trim());
					}else{
						abcentStudents.add(student.getUser_id().trim());
					}
				}
				presentsIds = removeLastChar(presentStudents.toString().replaceAll("\\s+", ""));
				abcentIds = removeLastChar(abcentStudents.toString().replaceAll("\\s+", ""));
                
				ConnectionDetector conn = new ConnectionDetector(TakeAttendenceActivtiy.this);
		        if(conn.isConnectingToInternet()){
		           new SendAttendenceAsync().execute();
		        } else{
		        	alert.showAlertDialog(TakeAttendenceActivtiy.this,"Connection Error","Check your Internet Connection",false);
		        }  
			}
		});

		selectClassSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				classType = parent.getItemAtPosition(position).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	private class GetStudentAttendenceAsync extends AsyncTask<Void, Void, ArrayList<AttendenceStudentsModel>>{

		@Override
		protected void onPreExecute() {
			if(pDialog == null){
				pDialog = Util.createProgressDialog(TakeAttendenceActivtiy.this);
				pDialog.setCancelable(false);
				pDialog.show();}
			else{
				pDialog.setCancelable(false);
				pDialog.show();
			}}

		@Override
		protected ArrayList<AttendenceStudentsModel> doInBackground(Void... params) {
			JSONArray topicsResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(Util.API+"student_section/" +SectionID+"?stream_id="+StreamID+"&semester_id="+SemesterID);
			
			if(jsonObjectRecived != null){
				try{
					topicsResponse = jsonObjectRecived.getJSONArray("students");
				}catch(JSONException e){
					e.printStackTrace();
				}
				if(topicsResponse.length() > 0){
					for(int i = 0; i< topicsResponse.length();i++){
						AttendenceStudentsModel studentData = new AttendenceStudentsModel();
						JSONObject studentDetails;
						try{
							studentDetails = topicsResponse.getJSONObject(i);
							studentData.setStream_id(studentDetails.getString("stream_id"));
							studentData.setSemester_id(studentDetails.getString("semester_id"));
							studentData.setSection_id(studentDetails.getString("section_id"));
							studentData.setUser_id(studentDetails.getString("user_id"));
							studentData.setFirstname(studentDetails.getString("firstname"));
							studentData.setLastname(studentDetails.getString("lastname"));
							studentData.setMobile(studentDetails.getString("mobile"));
							studentData.setProfile_photo(studentDetails.getString("profile_photo"));
							studentData.setPresentStatus("1");
							students.add(studentData);
						}catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					return students;
				}else{
					return null;
				}
			}else{
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<AttendenceStudentsModel> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if(result == null ||result.size()==0){
				noData.setVisibility(View.VISIBLE);
			}else{
				noData.setVisibility(View.GONE);
				adapter = new TakeAttendenceAdapter(TakeAttendenceActivtiy.this, result);
				studentsListView.setAdapter(adapter);
			}
		}
	}





	private class SendAttendenceAsync extends AsyncTask<Void, Void, Integer>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			// Showing progress dialog before sending http request
			if(pDialog == null){
				pDialog = Util.createProgressDialog(TakeAttendenceActivtiy.this);
				pDialog.setCancelable(false);
				pDialog.show();}
			else{
				pDialog.setCancelable(false);
				pDialog.show();
			}}

		@Override
		protected Integer doInBackground(Void... params) {
			// TODO Auto-generated method stub
			// JSON object to hold the information, which is sent to the server
			JSONObject jsonObjSend = new JSONObject();

			try {
				jsonObjSend.put("presents",presentsIds);
				jsonObjSend.put("abcenties",abcentIds);
				//jsonObjSend.put("timetable_slot_id",TimeSlotID);
				jsonObjSend.put("slot_start_time",SlotTimeStart);
				jsonObjSend.put("slot_end_time",SlotTimeEnd);
				
				jsonObjSend.put("stream_id",StreamID);
				jsonObjSend.put("semester_id",SemesterID);
				jsonObjSend.put("section_id",SectionID);
				jsonObjSend.put("subject_id",SubjectID);
				jsonObjSend.put("attendence_date",TimeTableDate);
				jsonObjSend.put("class_type",classType);

				
			} 
			catch (JSONException e) {
				e.printStackTrace();
			}

			jsonObjRecv = HttpPostClient.sendHttpPost(Util.API+"attendence", jsonObjSend);
			
			try {
				if(jsonObjRecv.getString("error").equalsIgnoreCase("false")){
					if(jsonObjRecv.getString("status").equalsIgnoreCase("0")){
						return 0;
					}else{
						return 1;
					}
				}
			}catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return 2;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if(result == 1){
				try {
					Toast.makeText(TakeAttendenceActivtiy.this, jsonObjRecv.getString("message"), Toast.LENGTH_SHORT).show();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}else if(result == 0){
				try {
					Toast.makeText(TakeAttendenceActivtiy.this, jsonObjRecv.getString("errorMessages"), Toast.LENGTH_SHORT).show();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}else{
				Toast.makeText(TakeAttendenceActivtiy.this, "Please try again", Toast.LENGTH_SHORT).show();
			}

		}
	}

	private static String removeLastChar(String str) {
		//return str.substring(0,str.length()-1);
		return str.substring(1,str.length()-1);

	}
}
