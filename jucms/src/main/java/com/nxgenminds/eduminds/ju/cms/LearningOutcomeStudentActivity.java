package com.nxgenminds.eduminds.ju.cms;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nxgenminds.eduminds.ju.cms.adapters.LearningOutcomesStudentAdapter;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.models.LearningOutComeStudentModel;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpPostClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class LearningOutcomeStudentActivity extends Activity{

	private ListView lrngStdOutcomeListView;
	private TextView outcomeFrom,outcomeTodate,noData;
	AlertDialogManager alert = new AlertDialogManager();
	private Spinner outcomeWeek;
	private EditText learningOutcomeEditText;
	private ImageButton createLearningOutcome;
	private ProgressDialog pDialog;
	private ArrayList<LearningOutComeStudentModel> learningOutcomesArray = new ArrayList<LearningOutComeStudentModel>();
	private LearningOutcomesStudentAdapter adapter;
	private String internshipID;
	private Calendar calendar;
	private SimpleDateFormat df,ddf;
	private static String formattedDate;
	private static String to,from;
	private static int pageCount = 0;
	private static String pagination_Date_String = "";
	private boolean flag_loading = false;
	private LinearLayout main;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.learning_outcome_student);
		learningOutcomesArray.clear();
		pageCount = 0;
		adapter = new LearningOutcomesStudentAdapter(LearningOutcomeStudentActivity.this, learningOutcomesArray);
		Bundle extras = getIntent().getExtras();
		Log.d("LearningOutcomeStudentActivity", "Bundle Created");
		internshipID = extras.getString("internshipID");
		Log.d("LearningOutcomeStudentActivity", "internship id"+internshipID);
		lrngStdOutcomeListView = (ListView) findViewById(R.id.learningOutcomesListview);
		outcomeWeek = (Spinner) findViewById(R.id.internship_learning_outcome_week);
		outcomeFrom = (TextView) findViewById(R.id.internship_learning_outcome_fromDate);
		outcomeTodate = (TextView) findViewById(R.id.internship_learning_outcome_toDate);
		learningOutcomeEditText = (EditText) findViewById(R.id.internship_learning_outcome_type);
		createLearningOutcome = (ImageButton) findViewById(R.id.internship_learning_outcome_submit);
		main=(LinearLayout)findViewById(R.id.main_outcome_container);
		noData=(TextView)findViewById(R.id.no_learning_outcome);
		Log.d("LearningOutcomeStudentActivity", "variable initialisation completed");
		if(Util.intership_flag)
		{
			main.setVisibility(View.GONE);

		}else
		{
			main.setVisibility(View.VISIBLE);
		}
		List<String> list = new ArrayList<String>();
		list.add("Today");
		list.add("Yesterday");
		list.add("2 Days");
		list.add("3 Days");
		list.add("4 Days");
		list.add("5 Days");
		list.add("6 Days");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		outcomeWeek.setAdapter(dataAdapter);
		Log.d("LearningOutcomeStudentActivity", "Spinner Item is Loaded with days");

		ConnectionDetector conn = new ConnectionDetector(LearningOutcomeStudentActivity.this);
		Log.d("LearningOutcomeStudentActivity", "Connection Object Created");
		if(conn.isConnectingToInternet()){
			new LearningOutcomesStudent().execute();
			Log.d("LearningOutcomeStudentActivity", "After LearningOutcomesStudent Executed");
		}else{
			alert.showAlertDialog(LearningOutcomeStudentActivity.this, "Connection Error", "Please check your internet connection", false);
			Log.d("LearningOutcomeStudentActivity", "Connection Error checkup dialog printed");
		}


		lrngStdOutcomeListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {	

				if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0)
				{
					if(flag_loading == false)
					{
						flag_loading = true;
						ConnectionDetector conn = new ConnectionDetector(LearningOutcomeStudentActivity.this);
						if(conn.isConnectingToInternet()){
							new LearningOutcomesStudentLoadMore().execute();
							Log.d("LearningOutcomeStudentActivity", "Learning outcome Added success Fully");
						}else{
							alert.showAlertDialog(LearningOutcomeStudentActivity.this, "Connection Error", "Please check your internet connection", false);
						}
					}
				}

			}
		});

		outcomeWeek.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				if( String.valueOf(outcomeWeek.getSelectedItem()).equalsIgnoreCase("Today"))
				{
					calendar = Calendar.getInstance();
					df = new SimpleDateFormat("dd MMM");

					ddf = new SimpleDateFormat("yyyy-MM-dd");
					from=ddf.format(calendar.getTime());
					to=ddf.format(calendar.getTime());

					formattedDate = df.format(calendar.getTime());
					outcomeFrom.setText(formattedDate);
					outcomeTodate.setText(formattedDate);
				}
				else	if( String.valueOf(outcomeWeek.getSelectedItem()).equalsIgnoreCase("Yesterday"))
				{

					calendar = Calendar.getInstance();
					df = new SimpleDateFormat("dd MMM");
					formattedDate = df.format(calendar.getTime());

					ddf = new SimpleDateFormat("yyyy-MM-dd");
					to=ddf.format(calendar.getTime());

					outcomeTodate.setText(formattedDate);
					calendar.add(Calendar.DAY_OF_MONTH, -1);
					formattedDate = df.format(calendar.getTime());
					outcomeFrom.setText(formattedDate);

					from=ddf.format(calendar.getTime());

				}	
				else	if( String.valueOf(outcomeWeek.getSelectedItem()).equalsIgnoreCase("2 Days"))
				{
					calendar = Calendar.getInstance();
					df = new SimpleDateFormat("dd MMM");
					formattedDate = df.format(calendar.getTime());
					outcomeTodate.setText(formattedDate);
					ddf = new SimpleDateFormat("yyyy-MM-dd");
					to=ddf.format(calendar.getTime());

					calendar.add(Calendar.DAY_OF_MONTH, -2);
					formattedDate = df.format(calendar.getTime());
					outcomeFrom.setText(formattedDate);
					from=ddf.format(calendar.getTime());
				}
				else	if( String.valueOf(outcomeWeek.getSelectedItem()).equalsIgnoreCase("3 Days"))
				{
					calendar = Calendar.getInstance();
					df = new SimpleDateFormat("dd MMM");
					formattedDate = df.format(calendar.getTime());
					outcomeTodate.setText(formattedDate);
					ddf = new SimpleDateFormat("yyyy-MM-dd");
					to=ddf.format(calendar.getTime());

					calendar.add(Calendar.DAY_OF_MONTH, -3);
					formattedDate = df.format(calendar.getTime());
					outcomeFrom.setText(formattedDate);
					from=ddf.format(calendar.getTime());

				}
				else	if( String.valueOf(outcomeWeek.getSelectedItem()).equalsIgnoreCase("4 Days"))
				{
					calendar = Calendar.getInstance();
					df = new SimpleDateFormat("dd MMM");
					formattedDate = df.format(calendar.getTime());
					outcomeTodate.setText(formattedDate);
					ddf = new SimpleDateFormat("yyyy-MM-dd");
					to=ddf.format(calendar.getTime());
					calendar.add(Calendar.DAY_OF_MONTH, -4);
					formattedDate = df.format(calendar.getTime());
					outcomeFrom.setText(formattedDate);
					from=ddf.format(calendar.getTime());
				}
				else	if( String.valueOf(outcomeWeek.getSelectedItem()).equalsIgnoreCase("5 Days"))
				{
					calendar = Calendar.getInstance();
					df = new SimpleDateFormat("dd MMM");
					formattedDate = df.format(calendar.getTime());
					outcomeTodate.setText(formattedDate);
					ddf = new SimpleDateFormat("yyyy-MM-dd");
					to=ddf.format(calendar.getTime());
					calendar.add(Calendar.DAY_OF_MONTH, -5);
					formattedDate = df.format(calendar.getTime());
					outcomeFrom.setText(formattedDate);
					from=ddf.format(calendar.getTime());

				}
				else	if( String.valueOf(outcomeWeek.getSelectedItem()).equalsIgnoreCase("6 Days"))
				{
					calendar = Calendar.getInstance();
					df = new SimpleDateFormat("dd MMM");
					formattedDate = df.format(calendar.getTime());
					outcomeTodate.setText(formattedDate);
					ddf = new SimpleDateFormat("yyyy-MM-dd");
					to=ddf.format(calendar.getTime());
					calendar.add(Calendar.DAY_OF_MONTH, -6);
					formattedDate = df.format(calendar.getTime());
					outcomeFrom.setText(formattedDate);
					from=ddf.format(calendar.getTime());
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});


		createLearningOutcome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(learningOutcomeEditText.getText().toString().trim().length() > 3 ){
					Log.d("LearningOutcomeStudentActivity", "createLearningOutcome inside submited");
					ConnectionDetector conn = new ConnectionDetector(LearningOutcomeStudentActivity.this);
					if(conn.isConnectingToInternet()){
					new AddLearningOutcomeStudent().execute();
					Log.d("LearningOutcomeStudentActivity", "createLearningOutcome submited called function to insert");
					} else{
						alert.showAlertDialog(LearningOutcomeStudentActivity.this,"ConnectionError","Check your Internet Connection",false);
					}


				}else{
					Toast.makeText(LearningOutcomeStudentActivity.this, "Please Enter Details", Toast.LENGTH_SHORT).show();
				}
			}
		});

	}

	/*
	lrngStdOutcomeListView
	outcomeWeek
	outcomeFrom
	outcomeTodate
	createLearningOutcome*/


	private class LearningOutcomesStudent extends AsyncTask<Void, Void, ArrayList<LearningOutComeStudentModel>>{

		@Override
		protected void onPreExecute() {
			if(pDialog == null){
				pDialog = Util.createProgressDialog(LearningOutcomeStudentActivity.this);
				pDialog.setCancelable(false);
				pDialog.show();}
			else{
				pDialog.setCancelable(false);
				pDialog.show();
			}}

		@Override
		protected ArrayList<LearningOutComeStudentModel> doInBackground(Void... params) {
			JSONArray connectionsResponse = null;
			
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(Util.API+"learning_outcome?internship_id="+internshipID);
			if(jsonObjectRecived != null){

				try{
					connectionsResponse = jsonObjectRecived.getJSONArray("learn_outcomes");
				}catch(JSONException e){
					e.printStackTrace();
				}
				if(connectionsResponse.length()>0){
					for(int i = 0; i< connectionsResponse.length();i++){
						LearningOutComeStudentModel outcome = new LearningOutComeStudentModel();
						JSONObject outcomeDetails;
						try{
							outcomeDetails = connectionsResponse.getJSONObject(i);
							/*
						outcome.setPeriod(outcomeDetails.getString("period"));
						if(takePeriod){
							LastWeekPeriod = outcomeDetails.getString("period");
							takePeriod = false;
						}*/
							outcome.setFrom_date(outcomeDetails.getString("from_date"));
							outcome.setTo_date(outcomeDetails.getString("to_date"));
							outcome.setComment(outcomeDetails.getString("comment"));
							outcome.setCreated_date(outcomeDetails.getString("created_date"));
							outcome.setIs_approved(outcomeDetails.getString("is_approved"));
							outcome.setApproval_remarks(outcomeDetails.getString("approval_remarks"));
							outcome.setIs_approval_remarks(outcomeDetails.getString("is_approval_remarks"));
							outcome.setLearning_outcome_id(outcomeDetails.getString("learning_outcome_id"));
							pagination_Date_String=outcomeDetails.getString("created_date");
							learningOutcomesArray.add(outcome);
						}catch (JSONException e) {
							e.printStackTrace();
						}
					}
					return learningOutcomesArray;
				}
				else
				{
					return null;
				}

			}else{
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<LearningOutComeStudentModel> result) {
			super.onPostExecute(result);
			pDialog.dismiss();

			if(result == null){
				if(Util.intership_flag)
				{
					noData.setVisibility(View.VISIBLE);
				}
				else{
					noData.setVisibility(View.GONE);
				}
			}else if(result.size()==0){
				if(Util.intership_flag)
				{
					noData.setVisibility(View.VISIBLE);
				}
				else{
					noData.setVisibility(View.GONE);
				}
			}  else{
				noData.setVisibility(View.GONE);

				//adapter = new LearningOutcomesStudentAdapter(LearningOutcomeStudentActivity.this, result);
				lrngStdOutcomeListView.setAdapter(adapter);
				//ListUtility.setListViewHeightBasedOnChildren(lrngStdOutcomeListView);
				if(result.size() < 20){
					flag_loading = true;
				}else{
					flag_loading = false;
				}
			}
		}
	}
	private class LearningOutcomesStudentLoadMore extends AsyncTask<Void, Void, ArrayList<LearningOutComeStudentModel>>{

		@Override
		protected void onPreExecute() {
			if(pDialog == null){
				pDialog = Util.createProgressDialog(LearningOutcomeStudentActivity.this);
				pDialog.setCancelable(false);
				pDialog.show();}
			else{
				pDialog.setCancelable(false);
				pDialog.show();
			}}

		@Override
		protected ArrayList<LearningOutComeStudentModel> doInBackground(Void... params) {
			JSONArray connectionsResponse = null;
			
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(Util.API+"learning_outcome?internship_id="+internshipID+"&last_date="+ pagination_Date_String.replaceAll(" ", "%20"));
			if(jsonObjectRecived != null){

				try{
					connectionsResponse = jsonObjectRecived.getJSONArray("learn_outcomes");
				}catch(JSONException e){
					e.printStackTrace();
				}
				if(connectionsResponse.length()>0){
					for(int i = 0; i< connectionsResponse.length();i++){
						LearningOutComeStudentModel outcome = new LearningOutComeStudentModel();
						JSONObject outcomeDetails;
						try{
							outcomeDetails = connectionsResponse.getJSONObject(i);
							outcome.setFrom_date(outcomeDetails.getString("from_date"));
							outcome.setTo_date(outcomeDetails.getString("to_date"));
							outcome.setComment(outcomeDetails.getString("comment"));
							outcome.setCreated_date(outcomeDetails.getString("created_date"));
							outcome.setIs_approved(outcomeDetails.getString("is_approved"));
							outcome.setApproval_remarks(outcomeDetails.getString("approval_remarks"));
							outcome.setIs_approval_remarks(outcomeDetails.getString("is_approval_remarks"));
							outcome.setLearning_outcome_id(outcomeDetails.getString("learning_outcome_id"));
							pagination_Date_String=outcomeDetails.getString("created_date");
							learningOutcomesArray.add(outcome);
						}catch (JSONException e) {
							e.printStackTrace();
						}
					}
					return learningOutcomesArray;
				}
				else
				{
					return null;
				}

			}else{
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<LearningOutComeStudentModel> result) {
			super.onPostExecute(result);
			pDialog.dismiss();

			if(result == null){
			}else if(result.size()==0){
			}  else{
				adapter.notifyDataSetChanged();
				flag_loading = false;
				pageCount = pageCount +20;
				lrngStdOutcomeListView.setSelection(pageCount);}
		}
	}

	private class AddLearningOutcomeStudent extends AsyncTask<Void, Void, JSONObject> {
		private JSONObject jsonObjRecv;
		
		@Override
		protected void onPreExecute() {
			if (pDialog == null) {
				pDialog = Util.createProgressDialog(LearningOutcomeStudentActivity.this);
				pDialog.setCancelable(false);
				pDialog.setTitle("Please Wait....");
				pDialog.show();
			} else {
				pDialog.setCancelable(false);
				pDialog.show();
			}
		}

		@Override
		protected JSONObject doInBackground(Void... params) {
			JSONObject jsonObjSend = new JSONObject();
			Log.d("LearningOutcomeStudentActivity", "createLearningOutcome inside thread of adding outcome");
			try {
				jsonObjSend.put("internship_id",internshipID);
				jsonObjSend.put("comment",learningOutcomeEditText.getText().toString());
				jsonObjSend.put("from_date",from);
				jsonObjSend.put("to_date",to);
				
				jsonObjRecv = HttpPostClient.sendHttpPost(Util.API+"learning_outcome",jsonObjSend);
				Log.d("LearningOutcomeStudentActivity", "createLearningOutcome submited sendHttpPost");
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return jsonObjRecv;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			pDialog.dismiss();
			Log.d("LearningOutcomeStudentActivity", "inside on post execute");
			if(jsonObjRecv!=null)
			{  
				if(jsonObjRecv.has("errorMessages") )
				{
					try {
						Toast.makeText(LearningOutcomeStudentActivity.this,jsonObjRecv.getString("errorMessages"),Toast.LENGTH_LONG).show();
					} catch (JSONException e) {e.printStackTrace();}	
				}
				else
				{
					Log.d("LearningOutcomeStudentActivity", "LearningOutComeStudentModel selected");
					LearningOutComeStudentModel outcome = new LearningOutComeStudentModel();
					outcome.setFrom_date(from);
					outcome.setTo_date(to);
					outcome.setComment(learningOutcomeEditText.getText().toString());
					outcome.setApproval_remarks("");
					outcome.setIs_approval_remarks("0");
					outcome.setIs_approved("0");
					if((adapter == null) || (adapter.getCount()==0)){
						learningOutcomesArray.add(outcome);
						lrngStdOutcomeListView.setAdapter(adapter);
						if(learningOutcomesArray.size() < 20){
							flag_loading = true;
						}else{
							flag_loading = false;
						}
						Log.d("LearningOutcomeStudentActivity", "learningOutcomeEditText before settext inside on post");
						learningOutcomeEditText.setText("");
						outcomeWeek.setSelection(0);
					}	else{
						learningOutcomesArray.add(0,outcome); 
						Log.d("LearningOutcomeStudentActivity", "learningOutcomeEditText learningOutcomesArray after add to learningOutcomesArray");
						adapter.notifyDataSetChanged();
						Log.d("LearningOutcomeStudentActivity", "learningOutcomeEditText notifyDataSetChanged");
						learningOutcomeEditText.setText("");
						outcomeWeek.setSelection(0);
					}
				}
			}
		}
	}


}
