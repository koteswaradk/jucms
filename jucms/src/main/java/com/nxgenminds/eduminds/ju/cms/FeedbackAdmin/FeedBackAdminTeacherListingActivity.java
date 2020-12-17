package com.nxgenminds.eduminds.ju.cms.FeedbackAdmin;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class FeedBackAdminTeacherListingActivity extends ActionBarActivity {
	
	private ListView mFeedBackTeacherList;
	private TextView mNoTeacher;
	private FeedbackTeacherListAdapter mFeedbackTeacherAdapter;
	private ArrayList<FeedBackTeacherModel> mArrayListFeedbackTeacher = new ArrayList<FeedBackTeacherModel>();
	
	private static String FEEDBACK_TEACHER_LIST_API = Util.API+"feedbackteacherList?class_section=";
	
	private String mClassSection,mEventId;
	private AlertDialogManager alert = new AlertDialogManager();
	
		
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_feedback_teacher_list);
		mFeedBackTeacherList = (ListView) findViewById(R.id.activity_feedback_admin_teacher_list);
		mNoTeacher = (TextView)findViewById(R.id.activity_feedback_admin_no_teacher);
		
		Bundle extras = getIntent().getExtras();
		if(extras!=null){
			
			mClassSection = extras.getString("class_section");
			mEventId = extras.getString("event_id");
			
		}
		
		ConnectionDetector conn = new ConnectionDetector(FeedBackAdminTeacherListingActivity.this);
		if(conn.isConnectingToInternet()){
			new GetFeedBackTeacherList().execute();
	    } else{
			alert.showAlertDialog(FeedBackAdminTeacherListingActivity.this,"Connection Error","Check your Internet Connection", false);
		}
		
		mFeedBackTeacherList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				FeedBackTeacherModel teacherModel =  mArrayListFeedbackTeacher.get(position);
				Intent teacherDetailIntent = new Intent(FeedBackAdminTeacherListingActivity.this,FeedbackTeacherDetailActivity.class);
				teacherDetailIntent.putExtra("event_id",teacherModel.getEvent_id());
				teacherDetailIntent.putExtra("teacher_subject_id",teacherModel.getTeacher_subject_id());
				teacherDetailIntent.putExtra("stream_id",teacherModel.getStream_id());
				teacherDetailIntent.putExtra("semester_id",teacherModel.getSemester_id());
				teacherDetailIntent.putExtra("section_id", teacherModel.getSection_id());
				startActivity(teacherDetailIntent);
			}
			
		});
		
		
		
		
	}
	
	
	private class GetFeedBackTeacherList extends AsyncTask<Void, Void, ArrayList<FeedBackTeacherModel>>{
		
		private ProgressDialog mPDialog;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			// Showing progress dialog before sending http request			
				mPDialog = Util.createProgressDialog(FeedBackAdminTeacherListingActivity.this);
				mPDialog.setCancelable(false);
				mPDialog.show();}
			
		@Override
		protected ArrayList<FeedBackTeacherModel> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONArray connectionsResponse = null;
			
			
			JSONObject jsonObjectReceived = null;
			try {
				jsonObjectReceived = HttpGetClient.sendHttpPost(FEEDBACK_TEACHER_LIST_API+URLEncoder.encode(mClassSection,"UTF-8")+"&event_id="+mEventId);
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(jsonObjectReceived != null){

				try{
					if(jsonObjectReceived.getString("status").equalsIgnoreCase("1")){
					connectionsResponse = jsonObjectReceived.getJSONArray("teacherList");
					}
				}catch(JSONException e){
					e.printStackTrace();
				}
				if(connectionsResponse!=null && connectionsResponse.length()>0){
				for(int i = 0; i< connectionsResponse.length();i++){
					FeedBackTeacherModel feedBackTeacherModel = new FeedBackTeacherModel();
					JSONObject teacherDetails;
					try{
						teacherDetails = connectionsResponse.getJSONObject(i);
						
				        feedBackTeacherModel.setTeacher_subject_id(teacherDetails.getString("teacher_subject_id"));
				        feedBackTeacherModel.setCreated_date(teacherDetails.getString("created_date"));
				        
				        feedBackTeacherModel.setStream_id(teacherDetails.getString("stream_id"));
				        feedBackTeacherModel.setStream_name(teacherDetails.getString("stream_name"));
				        feedBackTeacherModel.setStream_duration(teacherDetails.getString("stream_duration"));
				        feedBackTeacherModel.setStream_description(teacherDetails.getString("stream_description"));
				        
				        feedBackTeacherModel.setSemester(teacherDetails.getString("semester"));
				        feedBackTeacherModel.setSemester_id(teacherDetails.getString("semester_id"));
				        
				        feedBackTeacherModel.setSubject_id(teacherDetails.getString("subject_id"));
				        feedBackTeacherModel.setSubject_name(teacherDetails.getString("subject_name"));
				        feedBackTeacherModel.setSubject_description(teacherDetails.getString("subject_description"));
				        
				        feedBackTeacherModel.setSection_id(teacherDetails.getString("section_id"));
				        feedBackTeacherModel.setSection_name(teacherDetails.getString("section_name"));
				        feedBackTeacherModel.setSection_description(teacherDetails.getString("section_description"));
				        
				        feedBackTeacherModel.setUser_id(teacherDetails.getString("user_id"));
				        feedBackTeacherModel.setFirstname(teacherDetails.getString("firstname"));
				        feedBackTeacherModel.setLastname(teacherDetails.getString("lastname"));
				        feedBackTeacherModel.setFullname(teacherDetails.getString("fullname"));
				        feedBackTeacherModel.setProfile_photo(teacherDetails.getString("profile_photo"));
				        
				        feedBackTeacherModel.setAnswer_percentage(teacherDetails.getString("answer_percentage"));
				        feedBackTeacherModel.setFeedback_colorcode(teacherDetails.getString("feedback_colorcode"));
				        feedBackTeacherModel.setGrade(teacherDetails.getString("grade")); 
				        feedBackTeacherModel.setRgb(teacherDetails.getString("rgb"));
				        feedBackTeacherModel.setEvent_id(teacherDetails.getString("event_id"));
				        
				        
				        mArrayListFeedbackTeacher.add(feedBackTeacherModel);
					}catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				return mArrayListFeedbackTeacher ;
				} else{
					return null;
				}
			}else{
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<FeedBackTeacherModel> result) {
			// TODO Auto-generated method stub
			
			super.onPostExecute(result);
			mPDialog.dismiss();
			if(result == null){
				
				mNoTeacher.setVisibility(View.VISIBLE);
				mFeedBackTeacherList.setVisibility(View.GONE);
				
			}else if(result.size()==0){
				
				mNoTeacher.setVisibility(View.VISIBLE);
				mFeedBackTeacherList.setVisibility(View.GONE);
				
			} else{
				
				mNoTeacher.setVisibility(View.GONE);
				mFeedBackTeacherList.setVisibility(View.VISIBLE);
				mFeedbackTeacherAdapter = new FeedbackTeacherListAdapter(FeedBackAdminTeacherListingActivity.this, result);
				mFeedBackTeacherList.setAdapter(mFeedbackTeacherAdapter);
				mFeedbackTeacherAdapter .notifyDataSetChanged();
				/*if(result.size() < 20){
					flag_loading = true;
				}else{
					flag_loading = false;
				}*/
			}
		}
	}


}
