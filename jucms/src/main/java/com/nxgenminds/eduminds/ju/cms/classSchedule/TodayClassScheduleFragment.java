package com.nxgenminds.eduminds.ju.cms.classSchedule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
import android.widget.GridView;
import android.widget.TextView;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.models.ClassScheduleModel;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class TodayClassScheduleFragment extends Fragment{
	private TextView noData;
	private GridView mClassScheduleGrid;
	private ArrayList<ClassScheduleModel> mClassScheduleArrayList = new ArrayList<ClassScheduleModel>();
	private TodayClassScheduleAdapter  mClassScheduleAdapter;
	private ProgressDialog mPDialog;
	private AlertDialogManager alert = new AlertDialogManager();
	Date now = new Date();
	Date alsoNow = Calendar.getInstance().getTime();
	String nowAsString = new SimpleDateFormat("yyyy-MM-dd").format(now);

	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,  Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.class_schedule_fragment, container,false);
		mClassScheduleGrid = (GridView) rootView.findViewById(R.id.class_schedule_timetable);
		noData=(TextView)rootView.findViewById(R.id.noData_today);
		return rootView;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);

		ConnectionDetector conn = new ConnectionDetector(getActivity());
		if(conn.isConnectingToInternet()){
			new TodayClassSchedule().execute();
			System.out.println("async");
		} else{
			alert.showAlertDialog(getActivity(),"Connection Error","Check your Internet Connection",false);
		}


	}
	class TodayClassSchedule extends AsyncTask<Void,Void,ArrayList<ClassScheduleModel>>{
		@Override
		protected void onPreExecute(){
			if(mPDialog==null){
				mPDialog = Util.createProgressDialog(getActivity());
				mPDialog.show();
			} else
			{
				mPDialog.show();
			}
		}

		@Override
		protected ArrayList<ClassScheduleModel> doInBackground(Void... params) {
			JSONObject receivedTimeTable  = HttpGetClient.sendHttpPost(Util.API+"timetable/"+"todayDate");
			if(receivedTimeTable!=null){
				try{
					JSONArray  timeTableSlots =  receivedTimeTable.getJSONArray("timetable_slots");
					if(timeTableSlots.length()>0)
					{
						for(int i=0;i<timeTableSlots.length();i++){
							System.out.println("in do in ");

							JSONObject slot = timeTableSlots.getJSONObject(i);
							ClassScheduleModel scheduleModel = new ClassScheduleModel();
							//scheduleModel.setTimetable_slot_id(slot.getString("timetable_slot_id"));
							//scheduleModel.setSlot_name(slot.getString("slot_name"));
							scheduleModel.setSlot_start_time(slot.getString("slot_start_time"));
							scheduleModel.setSlot_end_time(slot.getString("slot_end_time"));
							System.out.println("classscheduleloop" +scheduleModel.getSlot_start_time() + scheduleModel.getSlot_end_time());

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
							scheduleModel.setTimetable_id(slot.getString("timetable_id"));
							scheduleModel.setSession_rating_id_flag(slot.getString("session_rating_id_flag"));
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
			System.out.println(result.toString());
			if(result==null){
				//Toast.makeText(getActivity(), "No Class Schedule Found !", Toast.LENGTH_SHORT).show();
				noData.setVisibility(View.VISIBLE);
				System.out.println("null");

			} else if(result.size()==0){
				//Toast.makeText(getActivity(), "No Class Schedule Found !", Toast.LENGTH_SHORT).show();
				noData.setVisibility(View.VISIBLE);
				System.out.println("==0");
			}else{
				System.out.println("Done");
				noData.setVisibility(View.GONE);
				mClassScheduleAdapter = new TodayClassScheduleAdapter(getActivity(),result);
				mClassScheduleGrid.setAdapter(mClassScheduleAdapter);
			}
		}
	}
}
