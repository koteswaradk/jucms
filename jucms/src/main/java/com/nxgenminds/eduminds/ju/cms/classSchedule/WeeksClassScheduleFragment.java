package com.nxgenminds.eduminds.ju.cms.classSchedule;

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
import android.widget.GridView;
import android.widget.TextView;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.models.WeekScheduleModel;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class WeeksClassScheduleFragment extends Fragment{
	TextView noData;
	private GridView mClassScheduleGrid;
	private ArrayList<WeekScheduleModel> mWeekScheduleArrayList = new ArrayList<WeekScheduleModel>();
	private WeekClassScheduleAdapter  mWeekScheduleAdapter;
	private ProgressDialog mPDialog;
	private AlertDialogManager alert = new AlertDialogManager();
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
        	new WeekClassSchedule().execute();
        } else{
        	alert.showAlertDialog(getActivity(),"Connection Error","Check your Internet Connection",false);
        }
	}
	
	class WeekClassSchedule extends AsyncTask<Void,Void,ArrayList<WeekScheduleModel>>{
		@Override
		protected void onPreExecute(){
			if(mPDialog==null){
				mPDialog = Util.createProgressDialog(getActivity());
				mPDialog.show();
			} else mPDialog.show();
		}

		@Override
		protected ArrayList<WeekScheduleModel> doInBackground(Void... params) {
			JSONObject receivedTimeTable  = HttpGetClient.sendHttpPost(Util.API+"get_weekly_details");
			if(receivedTimeTable!=null){
				try{
					JSONArray  ScheduleSlots =  receivedTimeTable.getJSONArray("weeks");
					if(ScheduleSlots.length()>0)
					{
						for(int i=0;i<ScheduleSlots.length();i++){
							JSONObject slot = ScheduleSlots.getJSONObject(i);
							WeekScheduleModel scheduleModel = new WeekScheduleModel();
							scheduleModel.setDay(slot.getString("day"));
							scheduleModel.setDate(slot.getString("date"));
							mWeekScheduleArrayList.add(scheduleModel);
						}
					}
				} catch(JSONException e){}
			}
			return mWeekScheduleArrayList;
		}
		@Override
		protected void onPostExecute(ArrayList<WeekScheduleModel> result){
			mPDialog.dismiss();
			if(result==null){
				//Toast.makeText(getActivity(), "No Class Schedule Found !", Toast.LENGTH_SHORT).show();
				noData.setVisibility(View.VISIBLE);

			} else if(result.size()==0){
				//Toast.makeText(getActivity(), "No Class Schedule Found !", Toast.LENGTH_SHORT).show();
				noData.setVisibility(View.VISIBLE);
			}else{
				noData.setVisibility(View.GONE);
				mWeekScheduleAdapter = new WeekClassScheduleAdapter(getActivity(),result);
				mClassScheduleGrid.setAdapter(mWeekScheduleAdapter);
			}
		}
	}
}
