package com.nxgenminds.eduminds.ju.cms.DiscussionForum;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.adapters.TopicsAdapter;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.models.TopicsModel;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpPostClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class DiscussionForumFragment extends Fragment{

	private ListView topicsListView;
	private EditText topicName;
	//private ImageButton addTopicButton;
	private ArrayList<TopicsModel> topics = new ArrayList<TopicsModel>();
	private TopicsAdapter adapter;
	private ProgressDialog pDialog;

	private AlertDialogManager alert = new AlertDialogManager();

	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,  Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.discussion_forum_fragment, container , false);

		topicsListView = (ListView) rootView.findViewById(R.id.topicsListView);
		topicName = (EditText) rootView.findViewById(R.id.topicName);
		//addTopicButton = (ImageButton) rootView.findViewById(R.id.createTopicButton);
		topics.clear();

		return rootView;
	}

	@Override
	public void onActivityCreated( Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		ConnectionDetector conn = new ConnectionDetector(getActivity());
		if(conn.isConnectingToInternet()){
			new GetTopicsAsync().execute();
		} else{
			alert.showAlertDialog(getActivity(),"Connection Error","Check your Internet Connection",false);
		}


		/*		addTopicButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(topicName.getText().toString().trim().length() > 0 ){

					ConnectionDetector conn = new ConnectionDetector(getActivity());
		            if(conn.isConnectingToInternet()){
		            	new AddLearningOutcomeStudent().execute();
		            } else{
		            	alert.showAlertDialog(getActivity(),"Connection Error","Check your Internet Connection",false);
		            }


				}else{
					// Error Message to Enter the Topic Name
					Toast.makeText(getActivity(), "Please Enter Topic Name", Toast.LENGTH_SHORT).show();
				}
			}
		});*/

		topicName.setOnEditorActionListener(
				new EditText.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_SEARCH ||
								actionId == EditorInfo.IME_ACTION_DONE ||
								event.getAction() == KeyEvent.ACTION_DOWN &&
								event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

							// TODO Auto-generated method stub
							if(topicName.getText().toString().trim().length() > 0 ){

								ConnectionDetector conn = new ConnectionDetector(getActivity());
								if(conn.isConnectingToInternet()){
									new AddLearningOutcomeStudent().execute();
								} else{
									alert.showAlertDialog(getActivity(),"Connection Error","Check your Internet Connection",false);
								}


							}else{
								// Error Message to Enter the Topic Name
								Toast.makeText(getActivity(), "Please Enter Topic Name", Toast.LENGTH_SHORT).show();
							}


							return true;
						}
						return false;
					}
				});

		topicsListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Object object = topicsListView.getItemAtPosition(position);
				TopicsModel topicsItem = (TopicsModel) object;
				// call a intent
				Intent intent = new Intent(getActivity(),DiscussionsForumQuestionsActivity.class);
				intent.putExtra("CategoryName", topicsItem.getForum_category_name());
				intent.putExtra("CategoryID", topicsItem.getForum_category_id());
				startActivity(intent);

			}
		});
	}


	private class GetTopicsAsync extends AsyncTask<Void, Void, ArrayList<TopicsModel>>{

		@Override
		protected ArrayList<TopicsModel> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONArray topicsResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(Util.API+"forum_category");
			if(jsonObjectRecived != null){
				try{
					topicsResponse = jsonObjectRecived.getJSONArray("forum_categories");
				}catch(JSONException e){
					e.printStackTrace();
				}
				if(topicsResponse.length() > 0){
					for(int i = 0; i< topicsResponse.length();i++){
						TopicsModel topicsData = new TopicsModel();
						JSONObject topicsDetails;
						try{
							topicsDetails = topicsResponse.getJSONObject(i);
							topicsData.setForum_category_id(topicsDetails.getString("forum_category_id"));
							topicsData.setForum_category_name(topicsDetails.getString("forum_category_name"));
							topicsData.setForum_category_desc(topicsDetails.getString("forum_category_desc"));
							topicsData.setCreated_date(topicsDetails.getString("created_date"));
							topicsData.setForum_topics_count(topicsDetails.getString("forum_topics_count"));
							topicsData.setFirstname(topicsDetails.getString("firstname"));
							topicsData.setLastname(topicsDetails.getString("lastname"));
							topicsData.setProfile_photo(topicsDetails.getString("profile_photo"));
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
		protected void onPostExecute(ArrayList<TopicsModel> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if(result == null ||result.size()==0){
			}else{
				adapter = new TopicsAdapter(getActivity(), result);
				topicsListView.setAdapter(adapter);
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
				pDialog = Util.createProgressDialog(getActivity());
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
				jsonObjSend.put("forum_category_name",topicName.getText().toString());
				jsonObjRecv = HttpPostClient.sendHttpPost(Util.API+"forum_category",jsonObjSend);
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
						Toast.makeText(getActivity(),jsonObjRecv.getString("errorMessages"),Toast.LENGTH_LONG).show();
					} catch (JSONException e) {e.printStackTrace();}	
				}
				else
				{
					topics.clear();
					topicName.setText("");
					ConnectionDetector conn = new ConnectionDetector(getActivity());
					if(conn.isConnectingToInternet()){
						new GetTopicsAsync().execute();
					} else{
						alert.showAlertDialog(getActivity(),"Connection Error","Check your Internet Connection",false);
					}

				}
			}
		}
	}
}
