package com.nxgenminds.eduminds.ju.cms.messages;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpPostClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class UserFragmentMessage  extends Fragment{

	private ImageButton  add_users;
	private ListView msg_list;
	private RelativeLayout bottom_layout;
	private TextView noMsg;
	private static final int UPDATE_RESULT = 0x1;
	private MessagesAdapter adapter;
	private static int pageCount = 0;
	private static String pagination_Date_String;
	private boolean flag_loading = false;
	AlertDialogManager alert = new AlertDialogManager();
	private AlertDialog.Builder mBuilder;
	private ProgressDialog pDialog;

	private String UserTimeFeedURL = Util.API+ "message";
	private ArrayList<MessageModel>messageSearch = new ArrayList<MessageModel>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.user_fragment_messages, container, false);
		Util.msgFlag = true;
		add_users=(ImageButton)view.findViewById(R.id.useraddmessage);
		msg_list=(ListView)view.findViewById(R.id.MessageListview);
		bottom_layout=(RelativeLayout)view.findViewById(R.id.buttom_buttons_layout_msg_list);
		noMsg=(TextView)view.findViewById(R.id.noMessageList);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//adapter = new MessagesAdapter(getActivity(), messageSearch);
		messageSearch.clear();
		pageCount = 0;
		adapter = new MessagesAdapter(getActivity(), messageSearch);
		ConnectionDetector conn = new ConnectionDetector(getActivity());
		if(conn.isConnectingToInternet()){
			new GetMsgAsyncClass().execute();
		}else{
			alert.showAlertDialog(getActivity(), "Connection Error", "Please check your internet connection", false);
		}



		msg_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Object object = msg_list.getItemAtPosition(position);
				MessageModel message_data = (MessageModel)  object;
				Intent ChatDetail = new Intent(getActivity(),ChatDetailActivity.class);
				ChatDetail.putExtra("username", message_data.getMessageusers());
				ChatDetail.putExtra("userprofile", message_data.getProfile_photo());
				ChatDetail.putExtra("Firstname", message_data.getFirstname());
				ChatDetail.putExtra("user_id", message_data.getUser_id());
				ChatDetail.putExtra("user_role", message_data.getRole());
				startActivityForResult(ChatDetail, UPDATE_RESULT);

			}
		});
		msg_list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int position, long id) {
				//	Toast.makeText(getActivity(), "long press", Toast.LENGTH_SHORT).show();

				Object object = msg_list.getItemAtPosition(position);
				final MessageModel message_data = (MessageModel)  object;
				AlertDialog.Builder confirmAlert = new AlertDialog.Builder(getActivity());
				confirmAlert.setTitle("Delete Message");
				confirmAlert.setMessage("Are you Sure you want to Delete this Message..! ");
				confirmAlert.setCancelable(true);

				confirmAlert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						ConnectionDetector conn = new ConnectionDetector(getActivity());
						if(conn.isConnectingToInternet()){

							new DeleteMessage().execute(message_data.getMessageusers());
							messageSearch.remove(position);
							adapter.notifyDataSetChanged();

						} else{
							alert.showAlertDialog(getActivity(),"Connection Error","Check your Internet Connection",false);
						}


						//dialog.dismiss();
					}
				});

				confirmAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
					}
				});
				confirmAlert.create().show();
				return false;
			}
		});


		msg_list.setOnScrollListener(new OnScrollListener() {

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
						ConnectionDetector conn = new ConnectionDetector(getActivity());
						if(conn.isConnectingToInternet()){
							new GetMsgLoadMoreAsyncClass().execute();
						}else{
							//Crouton.makeText(getActivity(), getString(R.string.crouton_message), Style.ALERT).show();
						}
					}
				}
			}
		});




		add_users.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent add_user = new Intent(getActivity(),SearchUserMessages.class);
				startActivityForResult(add_user, UPDATE_RESULT);
			}
		});
	}

	private class GetMsgAsyncClass extends AsyncTask<Void, Void, ArrayList<MessageModel>>{

		@Override
		protected void onPreExecute() {
			if(pDialog == null){
				pDialog = Util.createProgressDialog(getActivity());
				pDialog.setCancelable(false);
				pDialog.show();}
			else{
				pDialog.setCancelable(false);
				pDialog.show();
			}}

		@Override
		protected ArrayList<MessageModel> doInBackground(Void... params) {
			JSONArray msgListResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(UserTimeFeedURL);


			if(jsonObjectRecived != null){

				try{
					msgListResponse = jsonObjectRecived.getJSONArray("last_conversations");
				}catch(JSONException e){
					e.printStackTrace();
				}
				if(msgListResponse.length()>0){
					for(int i = 0; i<msgListResponse.length();i++){
						MessageModel mMessagetData = new MessageModel();
						JSONObject mMessageDetails;
						try{
							mMessageDetails = msgListResponse.getJSONObject(i);
							mMessagetData.setMessageusers(mMessageDetails.getString("messageusers"));
							mMessagetData.setConvid(mMessageDetails.getString("convid"));
							mMessagetData.setTime(mMessageDetails.getString("time"));
							mMessagetData.setUser_direction(mMessageDetails.getString("user_direction"));
							mMessagetData.setBody(mMessageDetails.getString("body"));
							mMessagetData.setRecord_display(mMessageDetails.getString("record_display"));
							mMessagetData.setProfile_photo(mMessageDetails.getString("profile_photo"));
							mMessagetData.setFirstname(mMessageDetails.getString("firstname"));
							mMessagetData.setLastname(mMessageDetails.getString("lastname"));
							mMessagetData.setBio(mMessageDetails.getString("bio"));
							mMessagetData.setJid(mMessageDetails.getString("jid"));
							mMessagetData.setCreated_date(mMessageDetails.getString("created_date"));
							mMessagetData.setUser_id(mMessageDetails.getString("user_id"));
							mMessagetData.setRole(mMessageDetails.getString("role"));
							pagination_Date_String = mMessageDetails.getString("convid");
							messageSearch.add(mMessagetData);
						}catch (JSONException e) {
							e.printStackTrace();
						}
					}
					return messageSearch;
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
		protected void onPostExecute(ArrayList<MessageModel> result) {
			super.onPostExecute(result);

			pDialog.dismiss();
			if(HttpGetClient.statuscode == 500)
			{
				//Crouton.makeText(getActivity(), getString(R.string.crouton_message_serverError), Style.ALERT).show();
				//reload.setVisibility(View.VISIBLE);
			}
			else if(HttpGetClient.statuscode == 504)
			{
				//Crouton.makeText(getActivity(), getString(R.string.crouton_message_serverGatewayError), Style.ALERT).show();
				//reload.setVisibility(View.VISIBLE);
			}
			else if(HttpGetClient.statuscode==404)
			{
				//Crouton.makeText(getActivity(), getString(R.string.crouton_message_auth), Style.ALERT).show();
				//reload.setVisibility(View.VISIBLE);

			}
			else if(HttpGetClient.statuscode == 200)
			{

				if(result== null)
				{
					noMsg.setVisibility(View.VISIBLE);
					//reload.setVisibility(View.VISIBLE);

				} 
				else if(result.size()<=0)
				{
					noMsg.setVisibility(View.VISIBLE);
					//reload.setVisibility(View.GONE);
				}      
				else
				{
					noMsg.setVisibility(View.GONE);
					//reload.setVisibility(View.GONE);
					msg_list.setAdapter(adapter);		
					if(messageSearch.size() < 20)
					{
						flag_loading = true;
					} else{
						flag_loading = false;
					}

				}

			}
		}

	}


	private class GetMsgLoadMoreAsyncClass extends AsyncTask<Void, Void, ArrayList<MessageModel>>{

		@Override
		protected void onPreExecute() {
			if(pDialog == null){
				pDialog = Util.createProgressDialog(getActivity());
				pDialog.setCancelable(false);
				pDialog.show();}
			else{
				pDialog.setCancelable(false);
				pDialog.show();
			}}

		@Override
		protected ArrayList<MessageModel> doInBackground(Void... params) {
			JSONArray msgListResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(UserTimeFeedURL+"?last_conv_id="+pagination_Date_String);
			if(jsonObjectRecived != null){

				try{
					msgListResponse = jsonObjectRecived.getJSONArray("last_conversations");
				}catch(JSONException e){
					e.printStackTrace();
				}
				if(msgListResponse.length()>0){
					for(int i = 0; i<msgListResponse.length();i++){
						MessageModel mMessagetData = new MessageModel();
						JSONObject mMessageDetails;
						try{
							mMessageDetails = msgListResponse.getJSONObject(i);
							mMessagetData.setMessageusers(mMessageDetails.getString("messageusers"));
							mMessagetData.setConvid(mMessageDetails.getString("convid"));
							mMessagetData.setTime(mMessageDetails.getString("time"));
							mMessagetData.setUser_direction(mMessageDetails.getString("user_direction"));
							mMessagetData.setBody(mMessageDetails.getString("body"));
							mMessagetData.setRecord_display(mMessageDetails.getString("record_display"));
							mMessagetData.setProfile_photo(mMessageDetails.getString("profile_photo"));
							mMessagetData.setFirstname(mMessageDetails.getString("firstname"));
							mMessagetData.setLastname(mMessageDetails.getString("lastname"));
							mMessagetData.setBio(mMessageDetails.getString("bio"));
							mMessagetData.setJid(mMessageDetails.getString("jid"));
							mMessagetData.setUser_id(mMessageDetails.getString("user_id"));
							mMessagetData.setRole(mMessageDetails.getString("role"));
							mMessagetData.setCreated_date(mMessageDetails.getString("created_date"));
							pagination_Date_String = mMessageDetails.getString("convid");

							messageSearch.add(mMessagetData);
						}catch (JSONException e) {
							e.printStackTrace();
						}
					}
					return messageSearch;
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
		protected void onPostExecute(ArrayList<MessageModel> result) {
			super.onPostExecute(result);
			pDialog.dismiss();
			if(HttpGetClient.statuscode == 500)
			{
				//Crouton.makeText(getActivity(), getString(R.string.crouton_message_serverError), Style.ALERT).show();
			}
			else if(HttpGetClient.statuscode == 504)
			{
				//Crouton.makeText(getActivity(), getString(R.string.crouton_message_serverGatewayError), Style.ALERT).show();
			}
			else if(HttpGetClient.statuscode==401)
			{
				//Crouton.makeText(getActivity(), getString(R.string.crouton_message_auth), Style.ALERT).show();
			}
			else if(HttpGetClient.statuscode == 200)
			{
				if(result == null)
				{
					flag_loading = true;
					Toast.makeText(getActivity(), "No more data to load !", Toast.LENGTH_SHORT).show();
				} else if(result.size()<=0)
				{
					flag_loading = true;
					Toast.makeText(getActivity(), "No more data to load !", Toast.LENGTH_SHORT).show();
				}			
				else
				{
					adapter.notifyDataSetChanged();
					flag_loading = true;
					pageCount = pageCount +20;
					msg_list.setSelection(pageCount);
				}	      


			}
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode !=getActivity().RESULT_OK) {
			return;
		}

		Bitmap bitmap;
		switch (requestCode) {

		case UPDATE_RESULT:
			if(resultCode == getActivity().RESULT_OK && null != data)
			{
				if(data.getStringExtra("update").equalsIgnoreCase("true"))
				{
					ConnectionDetector conn = new ConnectionDetector(getActivity());
					if(conn.isConnectingToInternet()){
						messageSearch.clear();
						pageCount = 0;
						new GetMsgAsyncClass().execute();
					}else{
						alert.showAlertDialog(getActivity(), "Connection Error", "Please check your internet connection", false);
					}
				}
				else
				{

				}
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data); 
	}

	private class DeleteMessage extends AsyncTask<String, Void, JSONObject>{

		@Override
		protected void onPreExecute() {
			if(pDialog == null){
				pDialog = Util.createProgressDialog(getActivity());
				pDialog.setCancelable(false);
				pDialog.show();}
			else{
				pDialog.setCancelable(false);
				pDialog.show();
			}}


		@Override
		protected JSONObject doInBackground(String... params) {
			JSONArray membersResponse = null;
			JSONObject send = new JSONObject();
			String uname= params[0];
			try {
				send.put("to_username", uname);

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

					}
					else
					{
						Toast.makeText(getActivity(), "Something went wrong !", Toast.LENGTH_SHORT).show();
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}}
