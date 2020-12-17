package com.nxgenminds.eduminds.ju.cms.Feedback;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.adapters.FeedBackQuestionsAdapter;
import com.nxgenminds.eduminds.ju.cms.events.EventsTabHostFragment;
import com.nxgenminds.eduminds.ju.cms.events.FeedBackAnswerModel;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpPostClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.ListUtility;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class FeedBackSystemFragment extends Fragment {

	private TextView mTextFeedBackName,mTextFeedBackDesc,mTextSubjectName,mTextTeacherName;
	private ListView mFeedbackListView;
	private ScrollView mScrollView;
	private ProgressDialog pDialog;
	private Button mFeedBackSubmit;
	private String lecturerID = null;
	
	private int mNo_Of_Subjects=1;
	private int subjectIndex=0;

	private ArrayList<TeacherSubjectGetModel> mArrayListSubjectsTeacher = new ArrayList<TeacherSubjectGetModel>();
	private ArrayList<ArrayList<FeedBackQuestionModel>> mArrayListAllquestions = new ArrayList<ArrayList<FeedBackQuestionModel>>();
	
	private JSONArray mFeedBackRootArray = new JSONArray();
	
	private static String GET_FEEDACK_URL =Util.API+"feedback";
	private FeedBackQuestionsAdapter mQuestionsadapter,mFinalAdapter;
	private AlertDialogManager alert = new AlertDialogManager();
	
   
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.feedbackfragment, container, false);
		mTextFeedBackName = (TextView)rootView.findViewById(R.id.feedback_name);
		mTextFeedBackDesc = (TextView)rootView.findViewById(R.id.feedback_info);
		mTextSubjectName = (TextView)rootView.findViewById(R.id.feedback_subject_name);
		mTextTeacherName = (TextView)rootView.findViewById(R.id.feedback_teacher_name);
		mFeedbackListView = (ListView)rootView.findViewById(R.id.feedbackListView);
		mScrollView = (ScrollView)rootView.findViewById(R.id.parentScrollLayout);
		mFeedBackSubmit = (Button)rootView.findViewById(R.id.feedBackSubmit);
		mFeedBackSubmit.setText("NEXT");
			
		Util.FEEDBACK_ANSWER_ARRAYLIST = new ArrayList<FeedBackAnswerModel>();
		ConnectionDetector conn = new ConnectionDetector(getActivity());
		   if(conn.isConnectingToInternet()){
				new GetFeedBackAsync().execute();
		   } else {
			   alert.showAlertDialog(getActivity(), "Connection Error"," Check your Internet Connection",false);
		   }
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onActivityCreated(savedInstanceState);
		          
		mTextFeedBackName.setText(Util.FEEDBACK_EVENT_NAME);
		if(Util.FEEDBACK_EVENT_DESC == null || Util.FEEDBACK_EVENT_DESC.equalsIgnoreCase("null") || Util.FEEDBACK_EVENT_DESC.length()==0){
			mTextFeedBackDesc.setText("FeedBack Description Not given");
	    } else{
		mTextFeedBackDesc.setText(Util.FEEDBACK_EVENT_DESC);  
	    }
			
        
		mFeedBackSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			if(mFeedBackSubmit.getText().toString().trim().equalsIgnoreCase("SUBMIT")){
				
				addFeedBackDataToJSONArray();
				ConnectionDetector conn = new ConnectionDetector(getActivity());
				   if(conn.isConnectingToInternet()){
					  new SendResult().execute();
				  } else {
					  alert.showAlertDialog(getActivity(), "Connection Error"," Check your Internet Connection",false);
				  }
				
			    
				
			} else {
				
				subjectIndex = subjectIndex+1;
			  
			  if(subjectIndex == (mArrayListSubjectsTeacher.size())-1){
				 mFeedBackSubmit.setText("SUBMIT");	
			  }
			  
			  TeacherSubjectGetModel model = mArrayListSubjectsTeacher.get(subjectIndex);
			  if(model.getLastname().equals(null));
				{
					 mTextTeacherName.setText(model.getFirstname());
				}
			 // mTextTeacherName.setText(model.getFirstname() +" " + model.getLastname());
			  mTextSubjectName.setText(model.getSubject_name());
			  
			  
			  mQuestionsadapter = new FeedBackQuestionsAdapter(getActivity(), mArrayListAllquestions.get(subjectIndex));
			  mQuestionsadapter.notifyDataSetChanged();
			  mFeedbackListView.setAdapter(mQuestionsadapter);
			  mScrollView.smoothScrollTo(0, 0);
			  
			}
			}
		});
				 		
	}		
				 
	protected void addFeedBackDataToUtils(int position) {
		
		FeedBackAnswerModel model = new FeedBackAnswerModel();
		
		TeacherSubjectGetModel subjectModel = mArrayListSubjectsTeacher.get(position);
		
		model.setTeacher_id(subjectModel.getTeacher_id());
		model.setSubject_id(subjectModel.getSubject_id());
		model.setTeacher_subject_id(subjectModel.getTeacher_subject_id());
		model.setQuestion_array_list(Util.FEEDBACK_QUESTION_ARRAYLIST);
	    Util.FEEDBACK_ANSWER_ARRAYLIST.add(position,model);
	    
	}

	protected void addFeedBackDataToJSONArray() {
		
		try{
		
			  for(int i=0;i<mArrayListSubjectsTeacher.size();i++)
			  {
					
				  TeacherSubjectGetModel subjectModel = mArrayListSubjectsTeacher.get(i);
				  
					    JSONObject jsonSubjectItem = new JSONObject();
			
				         jsonSubjectItem.put("teacher_id",subjectModel.getTeacher_id());
				         jsonSubjectItem.put("subject_id", subjectModel.getSubject_id());
				         jsonSubjectItem.put("teacher_subject_id",subjectModel.getTeacher_subject_id());
						         
				         JSONArray arrayFeedBackItem	 = new JSONArray();
				         ArrayList<FeedBackQuestionModel> arrayListQuestion = mArrayListAllquestions.get(i);
			         
				         for(int j=0;j<arrayListQuestion.size();j++){
				
				        	 JSONObject object = new JSONObject();
				        	 FeedBackQuestionModel model = arrayListQuestion.get(j);
				
				        	 object.put("feedback_quest_id",model.getFeedback_quest_id());
				        	 object.put("feedback_answer",model.getFeedback_answer());
				        	 arrayFeedBackItem.put(j,object);
				
				        	 
			            }
			
			jsonSubjectItem.put("feedback_stat",arrayFeedBackItem);
			mFeedBackRootArray.put(jsonSubjectItem);
			
			 }
	}catch(JSONException e){
	}
	
		
	}

	
	private class GetFeedBackAsync extends AsyncTask<String, Void, ArrayList<FeedBackQuestionModel>>{

		@Override
		protected ArrayList<FeedBackQuestionModel> doInBackground(String... params) {
			// TODO Auto-generated method stub
			// JSON object to hold the information, which is sent to the server
			JSONArray subjectsTeacherResponse = null;
			JSONArray feedBackResponse =null;
			JSONObject jsonObjRecv = HttpGetClient.sendHttpPost(GET_FEEDACK_URL);
			
			if(jsonObjRecv!=null ){               
				try{
					
					if(jsonObjRecv.getString("status").equalsIgnoreCase("1")){
					
					// getting the subjects and teachers
					subjectsTeacherResponse = jsonObjRecv.getJSONArray("teacher_subjects");
					if(subjectsTeacherResponse.length() > 0){
						mNo_Of_Subjects = subjectsTeacherResponse.length();
						for(int i = 0; i< subjectsTeacherResponse.length();i++){
							TeacherSubjectGetModel tsModel = new TeacherSubjectGetModel();
							JSONObject tsObject = subjectsTeacherResponse.getJSONObject(i);
							tsModel.setFirstname(tsObject.getString("firstname"));
							tsModel.setLastname(tsObject.getString("lastname"));
							tsModel.setTeacher_subject_id(tsObject.getString("teacher_subject_id"));
							tsModel.setTeacher_id(tsObject.getString("teacher_id"));
							tsModel.setSubject_id(tsObject.getString("subject_id"));
							tsModel.setSubject_name(tsObject.getString("subject_name"));
							tsModel.setProfile_photo(tsObject.getString("profile_photo"));
							mArrayListSubjectsTeacher.add(tsModel);
						}
					}
					
					// getting the feedBack
					feedBackResponse = jsonObjRecv.getJSONArray("feedback_questions");
					if(feedBackResponse.length() > 0){
							for(int i=0;i<subjectsTeacherResponse.length();i++){
								ArrayList<FeedBackQuestionModel> arrayListModel = new ArrayList<FeedBackQuestionModel>();
								for(int j = 0; j< feedBackResponse.length();j++){
									FeedBackQuestionModel feedBackquestionModel = new FeedBackQuestionModel();
				
									JSONObject feedBackObject = feedBackResponse.getJSONObject(j);
									feedBackquestionModel.setFeedback_quest_id(feedBackObject.getString("feedback_quest_id"));
									feedBackquestionModel.setFeedback_question(feedBackObject.getString("feedback_question"));
									feedBackquestionModel.setFeedback_answer("5");
									arrayListModel.add(feedBackquestionModel);
								}
						mArrayListAllquestions.add(arrayListModel);
				
					 }
						
					}
				 }
				}catch(JSONException e){
					e.printStackTrace();
				}
			}
			return mArrayListAllquestions.get(0);
		}

		@Override
		protected void onPostExecute(ArrayList<FeedBackQuestionModel> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if(result!=null){
				 TeacherSubjectGetModel model = mArrayListSubjectsTeacher.get(0);
			     mTextTeacherName.setText(model.getFirstname() +" " + model.getLastname());
				 mTextSubjectName.setText(model.getSubject_name());
				 	 
				 mQuestionsadapter = new FeedBackQuestionsAdapter(getActivity(), result);
			     mFeedbackListView.setAdapter(mQuestionsadapter);
			     ListUtility.setListViewHeightBasedOnChildren(mFeedbackListView);
			     mFeedBackSubmit.setVisibility(View.VISIBLE);
			     }
			}
	}

	
 class SendResult extends AsyncTask<Void, Void, String>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			// Showing progress dialog before sending http request
			if(pDialog == null){
				pDialog = Util.createProgressDialog(getActivity());
				pDialog.setCancelable(false);
				pDialog.show();}
			else{
				pDialog.setCancelable(false);
				pDialog.show();
			}}

		@Override
		protected String doInBackground(Void... params) {

			// TODO Auto-generated method stub
			String status =null;
			
			try{
					
			JSONObject sendFeedBackJSON = new JSONObject();
			sendFeedBackJSON.put("event_id",Util.FEED_BACK_EVENT_ID);
			sendFeedBackJSON.put("feedback_stat",mFeedBackRootArray.toString());
		    
			JSONObject jsonObjRecv = HttpPostClient.sendHttpPost(GET_FEEDACK_URL,sendFeedBackJSON);
			
			if(jsonObjRecv!=null){
				if(jsonObjRecv.getString("status").equalsIgnoreCase("1")){
                        status= jsonObjRecv.getString("status");
					}
			}
			
			} catch (JSONException e) {
				  e.printStackTrace();
			}
			
			return status;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			
			if(result !=null){
			if(result.equalsIgnoreCase("1")){
				AlertDialog.Builder confirmAlert = new AlertDialog.Builder(getActivity());
				confirmAlert.setTitle("Feedback Complected");
				confirmAlert.setMessage("Feedback successfully Uploaded");
				confirmAlert.setCancelable(true);
                confirmAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						replaceFragment(new EventsTabHostFragment());
								}
				});
				confirmAlert.create().show();

			}else{
				Toast.makeText(getActivity(), "There is some problem,Please re-submit feedback  ", Toast.LENGTH_SHORT).show();
			}
		}
	}
 }	
 
 private void replaceFragment(Fragment f){
	 FragmentManager fm = getActivity().getSupportFragmentManager();
	 FragmentTransaction ft = fm.beginTransaction();
	 ft.replace(R.id.content_frame,f);
	 ft.commit();
	 }


 
 
@Override
public void onResume() {

    super.onResume();

    getView().setFocusableInTouchMode(true);
    getView().requestFocus();
    getView().setOnKeyListener(new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
        	
            if (event.getAction() ==  KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK){

                // handle back button
            	
            	if(subjectIndex>0){
            		
            		subjectIndex = subjectIndex-1;
            		TeacherSubjectGetModel subject = mArrayListSubjectsTeacher.get(subjectIndex);
            		
            		mTextSubjectName.setText(subject.getSubject_name());
            		mTextTeacherName.setText(subject.getFirstname()+" "+subject.getLastname());
            		mFeedBackSubmit.setText("NEXT");
            		mScrollView.smoothScrollTo(0, 0);
            	   
            		mQuestionsadapter = new FeedBackQuestionsAdapter(getActivity(), mArrayListAllquestions.get(subjectIndex));
            		mQuestionsadapter.notifyDataSetChanged();
            		mFeedbackListView.setAdapter(mQuestionsadapter);	
					
				
             } else {
            		if(subjectIndex==0){
            		 replaceFragment(new EventsTabHostFragment());
            		}
            	}
            	
            	return true;

            }
            
            return false;
        }
    });
}

 
}
		


