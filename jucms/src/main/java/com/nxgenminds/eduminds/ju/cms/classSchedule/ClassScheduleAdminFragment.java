package com.nxgenminds.eduminds.ju.cms.classSchedule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class ClassScheduleAdminFragment extends Fragment{
	private Spinner  stream,section,semester;
	private TextView date;
	StreamAdapter stream_adapter ;
	SectionAdapter section_adapter ;
	SemesterAdapter semester_adapter ;
	private ProgressDialog pDialog;
	ClassScheduleAdminModel class_model;
	private String error_status;
	private ArrayList<Streams> stream_search = new ArrayList<Streams>();
	private ArrayList<Sections> section_search = new ArrayList<Sections>();
	private ArrayList<Semester> semester_search = new ArrayList<Semester>();
	private Button getTimeSlot;
	private DatePickerDialog.OnDateSetListener date_d;
	private Calendar calendar;
	private String stream_id,section_id,semester_id;
	
	private AlertDialogManager alert = new AlertDialogManager();

	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,  Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_class_schedule_admin, container,false);
		stream = (Spinner) rootView.findViewById(R.id.class_sc_stream);
		section = (Spinner) rootView.findViewById(R.id.class_sc_section);
		semester = (Spinner) rootView.findViewById(R.id.class_sc_semester);
		date=(TextView)rootView.findViewById(R.id.class_sc_date);
		getTimeSlot=(Button)rootView.findViewById(R.id.class_sc_get_timeslot);
		return rootView;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		semester_search.clear();
		section_search.clear();
		stream_search.clear();
		//setting the hint for  spinner

		Semester semData = new Semester();
		semData.setSemester_id("0");
		semData.setSemester("Select Semester");
		semester_search.add(semData);

		Sections secData = new Sections();
		secData.setSection_id("0");
		secData.setSection_name("Select Section");
		section_search.add(secData);

		Streams streamData = new Streams();
		streamData.setStream_id("0");
		streamData.setStream_name("Select Stream");
		stream_search.add(streamData);

		calendar = Calendar.getInstance();
		stream_adapter = new StreamAdapter(getActivity(),stream_search );
		section_adapter = new SectionAdapter(getActivity(),section_search );
		semester_adapter = new SemesterAdapter(getActivity(),semester_search);
		
		
		ConnectionDetector conn = new ConnectionDetector(getActivity());
        if(conn.isConnectingToInternet()){
        	new FilterAsync().execute();
        } else{
        	alert.showAlertDialog(getActivity(),"Connection Error","Check your Internet Connection",false);
        }

		
		

		section.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent,
					View view, int position, long id) {
				Sections section_item = (Sections) section.getSelectedItem();
				/*if (position == 0) {} 
				else
				{*/
				section_id=section_item.getSection_id();
				//}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}

		});

		stream.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent,
					View view, int position, long id) {
				Streams stream_item = (Streams) stream.getSelectedItem();
				/*	if (position == 0) {} 
				else
				{
				 */	stream_id=stream_item.getStream_id();

				 //}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}

		});

		semester.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent,
					View view, int position, long id) {
				Semester sem_item = (Semester) semester.getSelectedItem();
				/*	if (position == 0) {} 
				else
				{*/
				semester_id=sem_item.getSemester_id();
				
				//}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}

		});

		date.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				new DatePickerDialog(getActivity(), 2, date_d, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

			}
		});

		date_d = new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {

				calendar.set(Calendar.YEAR, year);
				calendar.set(Calendar.MONTH, monthOfYear);
				calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				String date_format = "yyyy-MM-dd";
				SimpleDateFormat sdf = new SimpleDateFormat(date_format);
				date.setText(sdf.format(calendar.getTime()).toString()); 

			}
		};

		getTimeSlot.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(semester_id.equalsIgnoreCase("0") || stream_id.equalsIgnoreCase("0") || section_id.equalsIgnoreCase("0"))
				{
					// select the all the specified filters
					Toast.makeText(getActivity(), "please select  all the specified filters", Toast.LENGTH_SHORT).show();
				}
				else{
					if(date.getText().toString().trim().length()>0)
					{
						// run the intent & pass the data  
						Intent adminDetail = new Intent(getActivity(),AdminFilterView.class);
						adminDetail.putExtra("semID", semester_id);
						adminDetail.putExtra("secID", section_id);
						adminDetail.putExtra("StrmID", stream_id);
						adminDetail.putExtra("date", date.getText().toString().trim());
						startActivity(adminDetail);

						semester.setSelection(0);
						stream.setSelection(0);
						section.setSelection(0);
						semester_id="0";
						section_id="0";
						stream_id="0";
						date.setText("");
						date.setHint("Select Date");
					}
					else
					{
						// please select the date
						Toast.makeText(getActivity(), "please select the date", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}

	private class FilterAsync extends AsyncTask<Void, Void, JSONObject>{

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
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(Util.API+"get_stream_semester_sectionlist");
			class_model=new Gson().fromJson(jsonObjectRecived.toString(), ClassScheduleAdminModel.class);
			if(class_model.getStatus().equalsIgnoreCase("1"))
			{
				for(int i=0 ; i < class_model.getStreams().length ; i++)
				{
					Streams stream_model=new Streams();
					stream_model=class_model.getStreams()[i];
					stream_search.add(stream_model);
				}
				for(int i=0 ; i < class_model.getSections().length ; i++)
				{
					Sections section_model = new Sections();
					section_model=class_model.getSections()[i];
					section_search.add(section_model);
				}
				for(int i=0 ; i < class_model.getSemester().length ; i++)
				{
					Semester  semester_model =new Semester();
					semester_model=class_model.getSemester()[i];
					semester_search.add(semester_model);
				}


			}
			return jsonObjectRecived;
		}

		@Override
		protected void onPostExecute(JSONObject filter_info) {
			super.onPostExecute(filter_info);
			pDialog.dismiss();
			if(filter_info != null){

				try {
					error_status=filter_info.getString("status");
					if(error_status.equalsIgnoreCase("1"))
					{
						stream.setAdapter(stream_adapter);
						section.setAdapter(section_adapter);
						semester.setAdapter(semester_adapter);

					}
					else{
						
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}



			}

		}


	}// end of async


}
