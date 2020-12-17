package com.nxgenminds.eduminds.ju.cms.messages;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
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
import com.nxgenminds.eduminds.ju.cms.ViewProfilePhoto;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpPostClient;
import com.nxgenminds.eduminds.ju.cms.thirdpartyprofile.ThirdPartyTabMenuActivity;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class ChatDetailActivity extends ActionBarActivity{

	public Menu menuInstance;
	private ListView msg_list;
	private ArrayList<ChatDetailModel> msgsearch = new ArrayList<ChatDetailModel>();
	private EditText msg_compose;
	ChatDetailAdapter msg_adapter;
	Button send_msg;
	private ProgressDialog pDialog;
	AlertDialogManager alert = new AlertDialogManager();
	private String MessageURL = Util.API+ "message/";
	private static int pageCount = 0;
	private static String Refresh_String = "";
	private String username,userProfile,user_firstname,user_id,user_role;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private TextView actionBar_name;
	private ImageView actionBar_image;
	private SwipeRefreshLayout swipeLayout;
	private boolean flag_refresh = false;
	private String message_body;
	private int arrayListIntialSize;
	private static boolean chat_delete=false;
	private static boolean clear_flag=false;
	ConnectionDetector conn = new ConnectionDetector(ChatDetailActivity.this);


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_chat_detail);
		//	msg_adapter= new ChatDetailAdapter(ChatDetailActivity.this, msgsearch);
		msg_list=(ListView)findViewById(R.id.list_view_messages);
		msg_compose=(EditText)findViewById(R.id.inputMsg);
		send_msg=(Button)findViewById(R.id.btnSend);
		swipeLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_container_msg);
		imageLoader = ImageLoader.getInstance();
		Bundle extras = getIntent().getExtras();
		username = extras.getString("username");
		userProfile = extras.getString("userprofile");
		user_firstname=extras.getString("Firstname");
		user_id = extras.getString("user_id");
		user_role=extras.getString("user_role");

		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.placeholder)
		.showImageForEmptyUri(R.drawable.placeholder) 
		.showImageOnFail(R.drawable.placeholder)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();

		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getSupportActionBar().setCustomView(R.layout.custom_actionbar_msg);

		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		getSupportActionBar().setHomeButtonEnabled(false);
		// CUSTOM ACTIONBAR
		actionBar_name=(TextView)findViewById(R.id.custom_actionbar_msg_uname);
		actionBar_image=(ImageView)findViewById(R.id.custom_actionbar_msg_uimage);

		actionBar_name.setText(user_firstname);
		imageLoader.displayImage(userProfile, actionBar_image, options, new SimpleImageLoadingListener() {
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

		actionBar_image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ChatDetailActivity.this, ViewProfilePhoto.class);
				intent.putExtra("ImagePath",userProfile);
				startActivity(intent); 

			}
		});

		ConnectionDetector conn = new ConnectionDetector(ChatDetailActivity.this);
		if(conn.isConnectingToInternet()){
			new MessageAsyncClass().execute();
		}else{
			alert.showAlertDialog(ChatDetailActivity.this, "Connection Error", "Please check your internet connection", false);
		}

		swipeLayout.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				swipeLayout.setRefreshing(true);
				flag_refresh = false;
				ConnectionDetector conn = new ConnectionDetector(ChatDetailActivity.this);
				if(conn.isConnectingToInternet()){
					swipeLayout.setRefreshing(false);
					new MessageRefreshAsyncClass().execute();
				}else{
					//Crouton.makeText(getActivity(), getString(R.string.crouton_message), Style.ALERT).show();
				}

			}
		});

		msg_list.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				boolean enable = false;
				if(msg_list != null && msg_list.getChildCount() > 0){
					boolean firstItemVisible = msg_list.getFirstVisiblePosition() == 0;
					boolean topOfFirstItemVisible = msg_list.getChildAt(0).getTop() == 0;
					enable = firstItemVisible && topOfFirstItemVisible;
				}
				swipeLayout.setEnabled(enable);	

			}
		});

		send_msg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				message_body = msg_compose.getText().toString().trim();
				ConnectionDetector conn = new ConnectionDetector(ChatDetailActivity.this);
				if(conn.isConnectingToInternet()){
					if(message_body.length()<=0){
						Toast.makeText(ChatDetailActivity.this,"Please write some text !",Toast.LENGTH_LONG).show();
					} else {
						new PostSendMessage().execute(message_body,username);
					}
				} else {
					alert.showAlertDialog(ChatDetailActivity.this,"Connection Error","Check your internet connection", false);
				}
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu (Menu menu) {
		menuInstance = menu;
		getMenuInflater().inflate(R.menu.chat_detail_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		super.onOptionsItemSelected(item);

		switch(item.getItemId()){
		case R.id.chat_view_profile:

			if(conn.isConnectingToInternet()){
				Intent intent = new Intent(ChatDetailActivity.this,ThirdPartyTabMenuActivity.class);
				intent.putExtra("UserID", user_id);
				intent.putExtra("ThirdPartyRole", user_role);
				Util.THIRD_PARTY_NAME  = user_firstname;
				Util.THIRD_PARTY_ID=user_id;
				Util.intership_flag=true;
				startActivity(intent);
			}else{
				alert.showAlertDialog(ChatDetailActivity.this, "Connection Error", "Please check your internet connection", false);
			}
			break;

		case R.id.chat_clear:
			chat_delete=true;
			clear_flag=true;

			if(conn.isConnectingToInternet()){
				new DeleteMessage().execute();

			}else{
				alert.showAlertDialog(ChatDetailActivity.this, "Connection Error", "Please check your internet connection", false);
			}
			break;

		case R.id.chat_delete:
			//Toast.makeText(getApplication(), "delete", Toast.LENGTH_SHORT).show();
			chat_delete=true;
			clear_flag=false;

			if(conn.isConnectingToInternet()){
				new DeleteMessage().execute();
			}else{
				alert.showAlertDialog(ChatDetailActivity.this, "Connection Error", "Please check your internet connection", false);
			}

			break;

		}
		return true;		

	}


	@Override
	public void finish() {
		Intent intent = new Intent();
		if(chat_delete)
		{
			intent.putExtra("update", "true");
		}
		else
		{
			intent.putExtra("update", "false");
		}
		setResult(RESULT_OK,intent);
		super.finish();
	}

	@Override
	public void onBackPressed() {
		finish();
	}


	private class MessageAsyncClass extends AsyncTask<Void, Void, ArrayList<ChatDetailModel>>{

		@Override
		protected void onPreExecute() {
			if(pDialog == null){
				pDialog = Util.createProgressDialog(ChatDetailActivity.this);
				pDialog.setCancelable(false);
				pDialog.show();}
			else{
				pDialog.setCancelable(false);
				pDialog.show();
			}}


		@Override
		protected ArrayList<ChatDetailModel> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONArray membersResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(MessageURL+username);
			if(jsonObjectRecived != null){
				try{
					membersResponse = jsonObjectRecived.getJSONArray("recent_conversation");
				}catch(JSONException e){
					e.printStackTrace();
				}
				if(membersResponse.length()>0){
					for(int i = 0; i< membersResponse.length();i++){
						ChatDetailModel membersData = new ChatDetailModel();
						JSONObject membersDetails;
						try {
							membersDetails = membersResponse.getJSONObject(i);
							membersData.setTime(membersDetails.getString("time"));
							membersData.setBody(membersDetails.getString("body"));
							membersData.setCreated_date(membersDetails.getString("created_date"));
							membersData.setFirstname(membersDetails.getString("firstname"));
							membersData.setLastname(membersDetails.getString("lastname"));
							membersData.setFromuser(membersDetails.getString("fromuser"));
							membersData.setJid(membersDetails.getString("jid"));
							if(i==0){

								if(!flag_refresh){
									flag_refresh = true;
									Refresh_String = membersDetails.getString("time");
								}
							}
							membersData.setProfile_photo(membersDetails.getString("profile_photo"));
							membersData.setTime(membersDetails.getString("time"));
							membersData.setTouser(membersDetails.getString("touser"));
							msgsearch.add(membersData);
						} catch (JSONException e) {
							e.printStackTrace();
						}

					} 
					return msgsearch;
				}
				else
				{
					return null;
				}

			}
			else
			{
				return null;
			}

		}

		@Override
		protected void onPostExecute(ArrayList<ChatDetailModel> result) {
			super.onPostExecute(result);
			if(swipeLayout.isRefreshing())
			{
				swipeLayout.setRefreshing(false);
			}
			pDialog.dismiss();
			if(result == null){
				Toast.makeText(ChatDetailActivity.this, "No Messages Found", Toast.LENGTH_SHORT).show();
			}else if(result.size()==0){
				//NoMembers.setVisibility(View.VISIBLE);
			}  else{
				msg_adapter= new ChatDetailAdapter(ChatDetailActivity.this, msgsearch);
				msg_list.setAdapter(msg_adapter);
				msg_list.setSelection(msg_adapter.getCount());
				arrayListIntialSize = msgsearch.size();
				/*				if(result.size() < 20){
					flag_loading = true;
				}else{
					flag_loading = false;
				}*/
			}
		}
	}

	private class MessageRefreshAsyncClass extends AsyncTask<Void, Void, ArrayList<ChatDetailModel>>{

		@Override
		protected ArrayList<ChatDetailModel> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONArray membersResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(MessageURL+username+"?last_date="+Refresh_String.replaceAll(" ", "%20"));
			if(jsonObjectRecived != null){

				try{
					membersResponse = jsonObjectRecived.getJSONArray("recent_conversation");
				}catch(JSONException e){
					e.printStackTrace();
				}
				if(membersResponse.length()>0){
					for(int i = 0; i< membersResponse.length();i++){
						ChatDetailModel membersData = new ChatDetailModel();
						JSONObject membersDetails;

						try {
							membersDetails = membersResponse.getJSONObject(i);
							membersData.setTime(membersDetails.getString("time"));
							membersData.setBody(membersDetails.getString("body"));
							membersData.setCreated_date(membersDetails.getString("created_date"));
							membersData.setFirstname(membersDetails.getString("firstname"));
							membersData.setLastname(membersDetails.getString("lastname"));
							membersData.setFromuser(membersDetails.getString("fromuser"));
							membersData.setJid(membersDetails.getString("jid"));
							if(i==0){
								if(!flag_refresh){
									flag_refresh = true;
									Refresh_String = membersDetails.getString("time");
								}
							}
							membersData.setProfile_photo(membersDetails.getString("profile_photo"));
							membersData.setTime(membersDetails.getString("time"));
							membersData.setTouser(membersDetails.getString("touser"));
							msgsearch.add(i,membersData);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					return msgsearch;
				}
				else
				{
					return null;
				}

			}
			else
			{
				return null;
			}

		}

		@Override
		protected void onPostExecute(ArrayList<ChatDetailModel> result) {
			super.onPostExecute(result);
			swipeLayout.setRefreshing(false);
			if(result == null){
				Toast.makeText(ChatDetailActivity.this, "No earlier messages to load", Toast.LENGTH_SHORT).show();
			}else if (result.size()== arrayListIntialSize){	
				Toast.makeText(ChatDetailActivity.this,"No earlier messages to load",Toast.LENGTH_LONG).show();
			}else{
				msg_adapter.notifyDataSetChanged();
				//pageCount = arrayListIntialSize +20;
				msg_list.setSelection(18);
				arrayListIntialSize = result.size();
			}
		}
	}

	public class PostSendMessage extends AsyncTask<String,Void,JSONObject>{
		String responseMessage;
		@Override
		protected void onPreExecute(){
			if(pDialog == null){
				pDialog = Util.createProgressDialog(ChatDetailActivity.this);
				pDialog.show();
			} else {
				pDialog.show();
			}
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			JSONObject receivedJSONResponse = null;
			String message = params[0];
			String send_to=params[1];
			JSONObject sendMessageJSONObject = new JSONObject();
			try{
				sendMessageJSONObject.put("message_to",send_to);
				sendMessageJSONObject.put("message_text",message);
				receivedJSONResponse = HttpPostClient.sendHttpPost(Util.API+"message",sendMessageJSONObject);
				/*	if(receivedJSONResponse!=null){
					responseMessage = receivedJSONResponse.getString("message");
				}*/
			}
			catch(JSONException e){
			}
			return receivedJSONResponse;
		}
		@Override
		protected void onPostExecute(JSONObject result){
			pDialog.dismiss();
			try {
				if(result.getString("status").equalsIgnoreCase("1")){
					ChatDetailModel addLocalMessage = new ChatDetailModel();

					addLocalMessage.setFromuser(Util.msgUserName);
					addLocalMessage.setTouser(username);
					addLocalMessage.setBody(message_body);		

					String chatFormat = "yyyy-mm-dd hh:mm:ssa";
					Date currentDate;
					currentDate = Calendar.getInstance().getTime();
					addLocalMessage.setCreated_date(new SimpleDateFormat(chatFormat).format(currentDate));


					if((msg_adapter == null) || (msg_adapter.getCount()==0)){
						msgsearch.add(addLocalMessage);
						msg_adapter= new ChatDetailAdapter(ChatDetailActivity.this, msgsearch);
						msg_list.setAdapter(msg_adapter);
						arrayListIntialSize = arrayListIntialSize+1;
					} else {
						msgsearch.add(addLocalMessage);
						msg_adapter.notifyDataSetChanged();
						msg_list.setSelection(msg_adapter.getCount());
						arrayListIntialSize = arrayListIntialSize+1;
						
					}
					msg_compose.getText().clear();



				}
				else
				{
					Toast.makeText(ChatDetailActivity.this,"Something went wrong !",Toast.LENGTH_LONG).show();

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} 
	}	

	private class DeleteMessage extends AsyncTask<Void, Void, JSONObject>{

		@Override
		protected void onPreExecute() {
			if(pDialog == null){
				pDialog = Util.createProgressDialog(ChatDetailActivity.this);
				pDialog.setCancelable(false);
				pDialog.show();}
			else{
				pDialog.setCancelable(false);
				pDialog.show();
			}}


		@Override
		protected JSONObject doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONArray membersResponse = null;
			JSONObject send = new JSONObject();
			try {
				send.put("to_username", username);

			} catch (JSONException e) {
				e.printStackTrace();
			}
			JSONObject jsonObjectRecived = HttpPostClient.sendHttpPost(Util.API+"delete_message",send);
			return jsonObjectRecived;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			pDialog.dismiss();
			try {
				if(result.getString("status").equalsIgnoreCase("1"))
				{
					if(result.getString("message").equalsIgnoreCase("Deleted Successfully"))
					{
						if(clear_flag)
						{
							msgsearch.clear();
							msg_adapter.notifyDataSetChanged();
						}else{
							finish();
						}
					}
					else
					{
						Toast.makeText(ChatDetailActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}catch (NullPointerException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
}

