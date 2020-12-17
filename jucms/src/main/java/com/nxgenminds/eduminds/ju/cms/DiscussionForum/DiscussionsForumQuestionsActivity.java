package com.nxgenminds.eduminds.ju.cms.DiscussionForum;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.adapters.TopicQuestionAdapter;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.models.TopicQuestionsModel;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpPostClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class DiscussionsForumQuestionsActivity extends Activity{

	private ListView questionsListView;
	private TextView questionsTextView;
	private EditText questionEditText;
	private ProgressDialog pDialog;
	private String CategoryTopicID,CategoryName;
	private ArrayList<TopicQuestionsModel> topics = new ArrayList<TopicQuestionsModel>();
	private TopicQuestionAdapter adapter;

	private AlertDialogManager alert = new AlertDialogManager();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.discussion_topics_activity);
		Bundle extras = getIntent().getExtras();
		CategoryTopicID = extras.getString("CategoryID");
		CategoryName = extras.getString("CategoryName");
		questionsListView = (ListView) findViewById(R.id.questionsListView);
		questionsTextView = (TextView) findViewById(R.id.topicName);
		questionEditText = (EditText) findViewById(R.id.questionName);

		ConnectionDetector conn = new ConnectionDetector(DiscussionsForumQuestionsActivity.this);
		if(conn.isConnectingToInternet()){
			new GetTopicQuestionsAsync().execute();
		} else{
			alert.showAlertDialog(DiscussionsForumQuestionsActivity.this,"Connection Error","Check your Internet Connection",false);
		}

		questionsTextView.setText(CategoryName);

		/*		createQuesionButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(questionEditText.getText().toString().trim().length() > 0){
					// Create Question

					ConnectionDetector conn = new ConnectionDetector(DiscussionsForumQuestionsActivity.this);
					if(conn.isConnectingToInternet()){
						new AddLearningOutcomeStudent().execute();
					} else{
						alert.showAlertDialog(DiscussionsForumQuestionsActivity.this,"Connection Error","Check your Internet Connection",false);
					}		

				}else{
					// Please enter the questions
				}
			}
		});*/

		questionEditText.setOnEditorActionListener(
				new EditText.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_SEARCH ||
								actionId == EditorInfo.IME_ACTION_DONE ||
								event.getAction() == KeyEvent.ACTION_DOWN &&
								event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

							// TODO Auto-generated method stub
							if(questionEditText.getText().toString().trim().length() > 0){
								// Create Question

								ConnectionDetector conn = new ConnectionDetector(DiscussionsForumQuestionsActivity.this);
								if(conn.isConnectingToInternet()){
									new AddLearningOutcomeStudent().execute();
								} else{
									alert.showAlertDialog(DiscussionsForumQuestionsActivity.this,"Connection Error","Check your Internet Connection",false);
								}		

							}else{
								// Please enter the questions
							}


							return true;
						}
						return false;
					}
				});

		questionsListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				Object object = questionsListView.getItemAtPosition(position);
				TopicQuestionsModel topic = (TopicQuestionsModel) object;

				Intent intent = new Intent(DiscussionsForumQuestionsActivity.this,DiscussionsFrmReplysActivity.class);
				intent.putExtra("CategoryName", CategoryName);
				intent.putExtra("QuestionName", topic.getForum_topic_subject());
				intent.putExtra("CategoryID", CategoryTopicID);
				intent.putExtra("TopicID", topic.getForum_topic_id());
				intent.putExtra("ReplyCount", topic.getForum_replies_count());
				intent.putExtra("TimeAgo", topic.getCreated_date());
				intent.putExtra("UserName", topic.getFirstname());
				intent.putExtra("UserImage", topic.getProfile_photo());
				startActivity(intent);

			}
		});

	}


	private class GetTopicQuestionsAsync extends AsyncTask<Void, Void, ArrayList<TopicQuestionsModel>>{

		@Override
		protected ArrayList<TopicQuestionsModel> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONArray topicsResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(Util.API+"forum_topic?forum_category_id=" + CategoryTopicID);
			if(jsonObjectRecived != null){
				try{
					topicsResponse = jsonObjectRecived.getJSONArray("forum_topics");
				}catch(JSONException e){
					e.printStackTrace();
				}
				if(topicsResponse.length() > 0){
					for(int i = 0; i< topicsResponse.length();i++){
						TopicQuestionsModel topicsData = new TopicQuestionsModel();
						JSONObject topicsDetails;
						try{
							topicsDetails = topicsResponse.getJSONObject(i);
							topicsData.setForum_topic_id(topicsDetails.getString("forum_topic_id"));
							topicsData.setForum_topic_subject(topicsDetails.getString("forum_topic_subject"));
							topicsData.setForum_category_id(topicsDetails.getString("forum_category_id"));
							topicsData.setCreated_date(topicsDetails.getString("created_date"));
							topicsData.setForum_replies_count(topicsDetails.getString("forum_replies_count"));
							topicsData.setFirstname(topicsDetails.getString("firstname"));
							topicsData.setLastname(topicsDetails.getString("lastname"));
							topicsData.setProfile_photo(topicsDetails.getString("profile_photo"));
							topicsData.setReply_content1(topicsDetails.getString("reply_content1"));
							topicsData.setReply_date1(topicsDetails.getString("reply_date1"));
							topicsData.setReply_content2(topicsDetails.getString("reply_content2"));
							topicsData.setReply_date2(topicsDetails.getString("reply_date2"));
							topics.add(topicsData);
						}catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					return topics;
				}else{
					return null;
				}
			}else{
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<TopicQuestionsModel> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if(result == null ||result.size()==0){
			}else{
				adapter = new TopicQuestionAdapter(DiscussionsForumQuestionsActivity.this, result);
				questionsListView.setAdapter(adapter);
			}
		}
	}



	private class AddLearningOutcomeStudent extends AsyncTask<Void, Void, Void> {
		private JSONObject jsonObjRecv;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			// Showing progress dialog before sending http request
			if (pDialog == null) {
				pDialog = Util.createProgressDialog(DiscussionsForumQuestionsActivity.this);
				pDialog.setCancelable(false);
				pDialog.show();
			} else {
				pDialog.setCancelable(false);
				pDialog.show();
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			// JSON object to hold the information, which is sent to the server
			JSONObject jsonObjSend = new JSONObject();

			try {
				jsonObjSend.put("forum_topic_subject",questionEditText.getText().toString());
				jsonObjSend.put("forum_category_id",CategoryTopicID);
				jsonObjRecv = HttpPostClient.sendHttpPost(Util.API+"forum_topic",jsonObjSend);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if(jsonObjRecv!=null)
			{  
				if(jsonObjRecv.has("errorMessages") )
				{
					try {
						Toast.makeText(DiscussionsForumQuestionsActivity.this,jsonObjRecv.getString("errorMessages"),Toast.LENGTH_LONG).show();
					} catch (JSONException e) {e.printStackTrace();}	
				}
				else
				{
					topics.clear();
					questionEditText.setText("");

					ConnectionDetector conn = new ConnectionDetector(DiscussionsForumQuestionsActivity.this);
					if(conn.isConnectingToInternet()){
						new GetTopicQuestionsAsync().execute();
					} else{
						alert.showAlertDialog(DiscussionsForumQuestionsActivity.this,"Connection Error","Check your Internet Connection",false);
					}


				}
			}
		}
	}

}
