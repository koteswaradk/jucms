package com.nxgenminds.eduminds.ju.cms.classSchedule;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.adapters.ClassScheduleAdapter;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.models.ClassScheduleModel;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class ClassScheduleDetailActivity extends Activity {
	private GridView mClassScheduleGrid;
	private TextView day,noData;
	private ArrayList<ClassScheduleModel> mClassScheduleArrayList = new ArrayList<ClassScheduleModel>();
	private ClassScheduleAdapter  mClassScheduleAdapter;
	private ProgressDialog mPDialog;
	private AlertDialogManager alert = new AlertDialogManager();
	String nowAsString,day_title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_class_schedule_detail);

		Bundle bundle = getIntent().getExtras();
		assert bundle != null;

		day_title = bundle.getString("Day");
		nowAsString = bundle.getString("Date");
		noData=(TextView)findViewById(R.id.noData);
		mClassScheduleGrid = (GridView) findViewById(R.id.class_schedule_timetable_detail);
		day=(TextView)findViewById(R.id.day_title);
		day.setText(day_title);
		
		ConnectionDetector conn = new ConnectionDetector(ClassScheduleDetailActivity.this);
        if(conn.isConnectingToInternet()){
        	new TodayClassSchedule().execute();
        } else{
        	alert.showAlertDialog(ClassScheduleDetailActivity.this,"Connection Error","Check your Internet Connection",false);
        }

		
	}
	class TodayClassSchedule extends AsyncTask<Void,Void,ArrayList<ClassScheduleModel>>{
		@Override
		protected void onPreExecute(){
			if(mPDialog==null){
				mPDialog = Util.createProgressDialog(ClassScheduleDetailActivity.this);
				mPDialog.show();
			} else mPDialog.show();
		}

		@Override
		protected ArrayList<ClassScheduleModel> doInBackground(Void... params) {
			JSONObject receivedTimeTable  = HttpGetClient.sendHttpPost(Util.API+"timetable/"+nowAsString);
			if(receivedTimeTable!=null){
				try{
					JSONArray  timeTableSlots =  receivedTimeTable.getJSONArray("timetable_slots");
					if(timeTableSlots.length()>0)
					{
						for(int i=0;i<timeTableSlots.length();i++){
							JSONObject slot = timeTableSlots.getJSONObject(i);
							ClassScheduleModel scheduleModel = new ClassScheduleModel();
							//scheduleModel.setTimetable_slot_id(slot.getString("timetable_slot_id"));
							//scheduleModel.setSlot_name(slot.getString("slot_name"));
							scheduleModel.setSlot_start_time(slot.getString("slot_start_time"));
							scheduleModel.setSlot_end_time(slot.getString("slot_end_time"));
							
							scheduleModel.setSubject_name(slot.getString("subject_name"));
							scheduleModel.setFullname(slot.getString("fullname"));
							scheduleModel.setIs_sprit_45(slot.getString("is_sprit_45"));
							scheduleModel.setSprit_45_subject_name(slot.getString("sprit_45_subject_name"));
							scheduleModel.setSprit_45_teacher_name(slot.getString("sprit_45_teacher_name"));
							scheduleModel.setTeacher_profile_photo(slot.getString("teacher_profile_photo"));
							scheduleModel.setStream_name(slot.getString("stream_name"));
							scheduleModel.setSection_name(slot.getString("section_name"));
							scheduleModel.setClass_room(slot.getString("class_room"));
							scheduleModel.setSemester(slot.getString("semester"));
							mClassScheduleArrayList.add(scheduleModel);
						}
					}
				} catch(JSONException e){}
			}
			return mClassScheduleArrayList;
		}
		@Override
		protected void onPostExecute(ArrayList<ClassScheduleModel> result){
			mPDialog.dismiss();
			if(result==null){
				//Toast.makeText(ClassScheduleDetailActivity.this, "No Class Schedule Found !", Toast.LENGTH_SHORT).show();
				noData.setVisibility(View.VISIBLE);

			} else if(result.size()==0){
				//Toast.makeText(ClassScheduleDetailActivity.this, "No Class Schedule Found !", Toast.LENGTH_SHORT).show();
				noData.setVisibility(View.VISIBLE);
			}else{
				noData.setVisibility(View.GONE);
				mClassScheduleAdapter = new ClassScheduleAdapter(ClassScheduleDetailActivity.this,result);
				mClassScheduleGrid.setAdapter(mClassScheduleAdapter);
			}
		}
	}
}
