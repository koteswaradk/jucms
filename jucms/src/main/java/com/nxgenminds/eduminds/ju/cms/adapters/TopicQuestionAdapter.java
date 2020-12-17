package com.nxgenminds.eduminds.ju.cms.adapters;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.models.TopicQuestionsModel;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpPostClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class TopicQuestionAdapter extends BaseAdapter{

	private ArrayList<TopicQuestionsModel> listData;
	private LayoutInflater layoutInflater;
	private Context prova;

	private EditText replyEditText;
	private TextView replyQuestion,replyUserName;
	private ImageView replyUserImage;
	private Button replySubmit;

	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	public static Dialog replyDialog;
	
	private AlertDialogManager alert = new AlertDialogManager();

	public TopicQuestionAdapter(Context context,ArrayList<TopicQuestionsModel> listData){
		this.listData = listData;
		prova = context;
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.placeholder)
		.showImageForEmptyUri(R.drawable.placeholder) 
		.showImageOnFail(R.drawable.placeholder)
		.cacheInMemory(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (layoutInflater == null)
			layoutInflater = (LayoutInflater) prova.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(convertView == null){
			convertView = layoutInflater.inflate(R.layout.custom_topics_questions_listview, null);
			holder = new ViewHolder();
			holder.userImageiv = (ImageView) convertView.findViewById(R.id.custom_view_discussion_forum_userImage);
			holder.userNametv = (TextView) convertView.findViewById(R.id.custom_view_discussion_forum_userName);
			holder.questionstv = (TextView) convertView.findViewById(R.id.custom_view_discussion_forum_detailed_question);
			holder.reply1tv = (TextView) convertView.findViewById(R.id.custom_view_discussion_forum_detailed_lastreply1);
			holder.reply2tv = (TextView) convertView.findViewById(R.id.custom_view_discussion_forum_detailed_lastreply2);
			holder.timeEgotv = (TextView) convertView.findViewById(R.id.custom_view_discussion_forum_detailed_time_when);
			holder.replyButton = (ImageButton) convertView.findViewById(R.id.addAnswer);
			holder.replyCounttv = (TextView) convertView.findViewById(R.id.custom_view_discussion_forum_detailed_no);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		final TopicQuestionsModel topicsItem = (TopicQuestionsModel) listData.get(position);

		holder.userNametv.setText(topicsItem.getFirstname());
		holder.questionstv.setText(topicsItem.getForum_topic_subject());
		//if(topicsItem.getReply_content1()!= "")
			if(topicsItem.getReply_content1() != null && !topicsItem.getReply_content1().isEmpty())
		{
			holder.reply1tv.setText("1. " + topicsItem.getReply_content1());
		}
			if(topicsItem.getReply_content2() != null && !topicsItem.getReply_content2().isEmpty()){

			holder.reply2tv.setText("2. " + topicsItem.getReply_content2());
		}
		
		holder.replyCounttv.setText(topicsItem.getForum_replies_count());
		holder.timeEgotv.setText(topicsItem.getReply_date1());

		holder.replyButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				replyDialog = new Dialog(prova);
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
				replyQuestion.setText(topicsItem.getForum_topic_subject());
				replyUserName.setText(topicsItem.getFirstname());

				replySubmit.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(replyEditText.getText().toString().length() > 0){
							ConnectionDetector conn = new ConnectionDetector(prova);
				            if(conn.isConnectingToInternet()){
				            	new SendReplyAsync().execute(topicsItem.getForum_topic_id());
				            } else{
				            	alert.showAlertDialog(prova,"Connection Error","Check your Internet Connection",false);
				            }
							
						}else{
							Toast.makeText(prova, "Please Enter Reply", Toast.LENGTH_SHORT).show();
						}
					}
				});

				imageLoader.displayImage(topicsItem.getProfile_photo(), replyUserImage, options, new SimpleImageLoadingListener() {
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

		imageLoader.displayImage(topicsItem.getProfile_photo(), holder.userImageiv, options, new SimpleImageLoadingListener() {
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
		
		holder.userImageiv.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				}
			
		});
		return convertView;
	}

	static class ViewHolder{

		ImageView userImageiv;
		TextView userNametv;
		TextView questionstv;
		TextView reply1tv;
		TextView reply2tv;
		ImageButton replyButton;
		TextView replyCounttv;
		TextView timeEgotv;

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
