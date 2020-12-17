package com.nxgenminds.eduminds.ju.cms.DiscussionForum;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.adapters.DiscussionReplysAdapter;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.models.DiscussionForumReplyModel;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpPostClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class DiscussionsFrmReplysActivity extends Activity {

	private String categoryID;
	private String categoryName;
	private String topicID;
	private String topicQuestion;
	private String replyCount;
	private String timeAgo;
	private String userName;
	private String userImage;


	private ListView replyListView;
	private TextView topic_Question;
	private TextView topic_replys_count;
	private TextView topic_user_Name;
	private TextView topic_time_ago;
	private ImageView topic_user_Image;
	private ImageButton add_answer;

	public Dialog replyDialog;
	private EditText replyEditText;
	private TextView replyQuestion,replyUserName;
	private ImageView replyUserImage;
	private Button replySubmit;

	private ArrayList<DiscussionForumReplyModel> forumReplys = new ArrayList<DiscussionForumReplyModel>();
	private DiscussionReplysAdapter adapter;

	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	
	private AlertDialogManager alert = new AlertDialogManager();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_discussion_replys);
		Bundle extras = getIntent().getExtras();

		categoryID = extras.getString("CategoryID");
		categoryName = extras.getString("CategoryName");
		topicID = extras.getString("TopicID");
		topicQuestion = extras.getString("QuestionName");
		replyCount = extras.getString("ReplyCount");
		timeAgo = extras.getString("TimeAgo");
		userName = extras.getString("UserName");
		userImage = extras.getString("UserImage");

		replyListView = (ListView) findViewById(R.id.replysListView);
		topic_Question = (TextView) findViewById(R.id.detailed_question);
		topic_replys_count = (TextView) findViewById(R.id.forum_detailed_no);
		topic_user_Image = (ImageView) findViewById(R.id.forum_userImage);
		topic_user_Name = (TextView) findViewById(R.id.forum_userName);
		add_answer = (ImageButton) findViewById(R.id.addAnswer);
		topic_time_ago = (TextView) findViewById(R.id.detailed_time_when);

		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.placeholder)
		.showImageForEmptyUri(R.drawable.placeholder) 
		.showImageOnFail(R.drawable.placeholder)
		.cacheInMemory(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		
		
		ConnectionDetector conn = new ConnectionDetector(DiscussionsFrmReplysActivity.this);
        if(conn.isConnectingToInternet()){
        	new GetTopicReplysAsync().execute();
        } else{
        	alert.showAlertDialog(DiscussionsFrmReplysActivity.this,"Connection Error","Check your Internet Connection",false);
        }

		
		
		topic_Question.setText(topicQuestion);
		topic_replys_count.setText(replyCount);
		topic_time_ago.setText(timeAgo);
		topic_user_Name.setText(userName);

		imageLoader.displayImage(userImage, topic_user_Image, options, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
			}

			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			}
		}, new ImageLoadingProgressListener() {
			@Override
			public void onProgressUpdate(String imageUri, View view, int current,
					int total) {

			}
		});		


		add_answer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				replyDialog = new Dialog(DiscussionsFrmReplysActivity.this);
				replyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				replyDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
				replyDialog.setContentView(R.layout.custom_topicquestion_reply);
				replyDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

				/*DisplayMetrics metrics = prova.getResources().getDisplayMetrics();
				int screenWidth = (int) (metrics.widthPixels * 0.80);
				replyDialog.getWindow().setLayout(screenWidth, LayoutParams.WRAP_CONTENT); //set below the setContentview
				 */

				replyQuestion = (TextView)replyDialog.findViewById(R.id.user_question);
				replyUserImage = (ImageView)replyDialog.findViewById(R.id.question_userImage);
				replyEditText = (EditText)replyDialog.findViewById(R.id.replyMessage);
				replyUserName = (TextView) replyDialog.findViewById(R.id.question_userName);
				replySubmit = (Button) replyDialog.findViewById(R.id.sendReply);
				replyQuestion.setText(topicQuestion);
				replyUserName.setText(userName);

				replySubmit.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(replyEditText.getText().toString().length() > 0){
							
							ConnectionDetector conn = new ConnectionDetector(DiscussionsFrmReplysActivity.this);
				            if(conn.isConnectingToInternet()){
				            	new SendReplyAsync().execute(topicID);
				            } else{
				            	alert.showAlertDialog(DiscussionsFrmReplysActivity.this,"Connection Error","Check your Internet Connection",false);
				            }						
						}else{
							Toast.makeText(DiscussionsFrmReplysActivity.this, "Please Enter Reply", Toast.LENGTH_SHORT).show();
						}
					}
				});
				
				imageLoader.displayImage(userImage, replyUserImage, options, new SimpleImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
					}

					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					}
				}, new ImageLoadingProgressListener() {
					@Override
					public void onProgressUpdate(String imageUri, View view, int current,
							int total) {

					}
				}
						);		
				replyDialog.show();
			}
		});
	}

	private class GetTopicReplysAsync extends AsyncTask<Void, Void, ArrayList<DiscussionForumReplyModel>>{

		@Override
		protected ArrayList<DiscussionForumReplyModel> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONArray topicsResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(Util.API+"forum_reply?forum_topic_id=" + topicID);
			if(jsonObjectRecived != null){
				try{
					topicsResponse = jsonObjectRecived.getJSONArray("forum_replies");
				}catch(JSONException e){
					e.printStackTrace();
				}
				if(topicsResponse.length() > 0){
					for(int i = 0; i< topicsResponse.length();i++){
						DiscussionForumReplyModel topicsData = new DiscussionForumReplyModel();
						JSONObject topicsDetails;
						try{
							topicsDetails = topicsResponse.getJSONObject(i);
							topicsData.setForum_reply_id(topicsDetails.getString("forum_reply_id"));
							topicsData.setForum_reply_content(topicsDetails.getString("forum_reply_content"));
							topicsData.setForum_topic_id(topicsDetails.getString("forum_topic_id"));
							topicsData.setCreated_date(topicsDetails.getString("created_date"));
							topicsData.setFirstname(topicsDetails.getString("firstname"));
							topicsData.setLastname(topicsDetails.getString("lastname"));
							topicsData.setProfile_photo(topicsDetails.getString("profile_photo"));
							forumReplys.add(topicsData);
						}catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					return forumReplys;
				}else{
					return null;
				}
			}else{
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<DiscussionForumReplyModel> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if(result == null ||result.size()==0){
			}else{
				adapter = new DiscussionReplysAdapter(DiscussionsFrmReplysActivity.this, result);
				replyListView.setAdapter(adapter);
			}
		}
	}

	private class SendReplyAsync extends AsyncTask<String, Void, Integer>{

		@Override
		protected Integer doInBackground(String... params) {
			// TODO Auto-generated method stub
			// JSON object to hold the information, which is sent to the server
			JSONObject jsonObjRecv;
			JSONObject jsonObjSend = new JSONObject();
			String topic_id = params[0];
			try {

				jsonObjSend.put("forum_topic_id",topic_id);
				jsonObjSend.put("forum_reply_content",replyEditText.getText().toString());

			} 
			catch (JSONException e) {
				e.printStackTrace();
			}

			jsonObjRecv = HttpPostClient.sendHttpPost(Util.API + "forum_reply", jsonObjSend);
			return null;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			replyDialog.dismiss();
		}


	}


}
