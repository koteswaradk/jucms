package com.nxgenminds.eduminds.ju.cms.spirit;

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
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;



public class SpritListFragment_new extends Fragment {

	private ListView msprit45List;
	private TextView mNosprit45Message;
	private static String GET_sprit45_LIST_URL = Util.API + "sprit45";
	private ArrayList<SpritListModel_new> mArrayListsprit45 = new ArrayList<SpritListModel_new>();
	private SpritListAdapter_new adapter;
	private static int pageCount = 0;
	private String pagination_Date_String;
	private boolean flag_loading = false;
	private AlertDialogManager alert = new AlertDialogManager();
	private ProgressDialog pDialog;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.new_fragment_sprit_list, container, false);
		msprit45List = (ListView) rootView.findViewById(R.id.sprit_fragment_sprit_list);
		mNosprit45Message = (TextView) rootView.findViewById(R.id.sprit_fragment_sprit_noData);
		return rootView;
	}



	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mArrayListsprit45.clear();
		pageCount = 0;
		adapter = new SpritListAdapter_new(getActivity(), mArrayListsprit45);
		ConnectionDetector conn = new ConnectionDetector(getActivity());
		if(conn.isConnectingToInternet()){
			new  Getsprit45Async().execute();
		}else{
			alert.showAlertDialog(getActivity(), "Connection Error", "Please check your internet connection", false);
		}

		msprit45List.setOnScrollListener(new OnScrollListener() {

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
							new Getsprit45AsyncLoadMore().execute();
						}else{
							alert.showAlertDialog(getActivity(), "Connection Error", "Please check your internet connection", false);
						}
					}
				}
			}
		});

	}


	private class Getsprit45Async extends AsyncTask<Void, Void, ArrayList<SpritListModel_new>>{

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
		protected ArrayList<SpritListModel_new> doInBackground(Void... params) {
			JSONArray sprit45Response = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(GET_sprit45_LIST_URL);

			if(jsonObjectRecived != null){

				try{
					sprit45Response = jsonObjectRecived.getJSONArray("sprite45timetable");
				}catch(JSONException e){
					e.printStackTrace();
				}
				if(sprit45Response.length()>0){
					for(int i = 0; i< sprit45Response.length();i++){
						SpritListModel_new sprit45Data = new SpritListModel_new();
						JSONObject sprit45Details;

						try {
							sprit45Details = sprit45Response.getJSONObject(i);
							sprit45Data.setSprit_45_teacher_name(sprit45Details.getString("sprit_45_teacher_name"));
							sprit45Data.setTimetable_id(sprit45Details.getString("timetable_id"));
							sprit45Data.setTimetable_date(sprit45Details.getString("timetable_date"));
							pagination_Date_String = sprit45Details.getString("timetable_date");
							sprit45Data.setSprit_45_subject_name(sprit45Details.getString("sprit_45_subject_name"));
							sprit45Data.setIs_sprit_45(sprit45Details.getString("is_sprit_45"));
							//sprit45Data.setTimetable_slot_id(sprit45Details.getString("timetable_slot_id"));
							//sprit45Data.setSlot_name(sprit45Details.getString("slot_name"));
							sprit45Data.setSlot_start_time(sprit45Details.getString("slot_start_time"));
							sprit45Data.setSlot_end_time(sprit45Details.getString("slot_end_time"));
							sprit45Data.setStream_id(sprit45Details.getString("stream_id"));
							sprit45Data.setStream_name(sprit45Details.getString("stream_name"));
							sprit45Data.setStream_duration(sprit45Details.getString("stream_duration"));
							sprit45Data.setStream_stream_description(sprit45Details.getString("stream_description"));
							sprit45Data.setSemester_id(sprit45Details.getString("semester_id"));
							sprit45Data.setSemester(sprit45Details.getString("semester"));
							sprit45Data.setSection_id(sprit45Details.getString("section_id"));
							sprit45Data.setSection_name(sprit45Details.getString("section_name"));
							sprit45Data.setSection_description(sprit45Details.getString("section_description"));
							mArrayListsprit45.add(sprit45Data);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					return mArrayListsprit45;
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
		protected void onPostExecute(ArrayList<SpritListModel_new> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if(result == null){
				mNosprit45Message.setVisibility(View.VISIBLE);
				Toast.makeText(getActivity(), "No spri45 data Found", Toast.LENGTH_SHORT).show();
			}else if(result.size()==0){
				mNosprit45Message.setVisibility(View.VISIBLE);
			}  else{
				mNosprit45Message.setVisibility(View.GONE);
				msprit45List.setAdapter(adapter);
				if(result.size() < 20){
					flag_loading = true;
				}else{
					flag_loading = false;
				}
			}
		}
	}

	private class Getsprit45AsyncLoadMore extends AsyncTask<Void, Void, ArrayList<SpritListModel_new>>{

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
		protected ArrayList<SpritListModel_new> doInBackground(
				Void... params) {
			// TODO Auto-generated method stub
			JSONArray sprit45Response = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(GET_sprit45_LIST_URL+"?last_date="+pagination_Date_String.replaceAll(" ", "%20"));

			if(jsonObjectRecived != null){

				try{
					sprit45Response = jsonObjectRecived.getJSONArray("sprite45timetable");
				}catch(JSONException e){
					e.printStackTrace();
				}
				if(sprit45Response.length()>0){
					for(int i = 0; i< sprit45Response.length();i++){
						SpritListModel_new sprit45Data = new SpritListModel_new();
						JSONObject sprit45Details;

						try {
							sprit45Details = sprit45Response.getJSONObject(i);
							sprit45Data.setSprit_45_teacher_name(sprit45Details.getString("sprit_45_teacher_name"));
							sprit45Data.setTimetable_id(sprit45Details.getString("timetable_id"));
							sprit45Data.setTimetable_date(sprit45Details.getString("timetable_date"));
							pagination_Date_String = sprit45Details.getString("timetable_date");
							sprit45Data.setSprit_45_subject_name(sprit45Details.getString("sprit_45_subject_name"));
							sprit45Data.setIs_sprit_45(sprit45Details.getString("is_sprit_45"));
							//sprit45Data.setTimetable_slot_id(sprit45Details.getString("timetable_slot_id"));
							//sprit45Data.setSlot_name(sprit45Details.getString("slot_name"));
							sprit45Data.setSlot_start_time(sprit45Details.getString("slot_start_time"));
							sprit45Data.setSlot_end_time(sprit45Details.getString("slot_end_time"));
							sprit45Data.setStream_id(sprit45Details.getString("stream_id"));
							sprit45Data.setStream_name(sprit45Details.getString("stream_name"));
							sprit45Data.setStream_duration(sprit45Details.getString("stream_duration"));
							sprit45Data.setStream_stream_description(sprit45Details.getString("stream_description"));
							sprit45Data.setSemester_id(sprit45Details.getString("semester_id"));
							sprit45Data.setSemester(sprit45Details.getString("semester"));
							sprit45Data.setSection_id(sprit45Details.getString("section_id"));
							sprit45Data.setSection_name(sprit45Details.getString("section_name"));
							sprit45Data.setSection_description(sprit45Details.getString("section_description"));
							mArrayListsprit45.add(sprit45Data);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					return mArrayListsprit45;
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
		protected void onPostExecute(ArrayList<SpritListModel_new> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if(result == null){
				Toast.makeText(getActivity(), "No more data to load", Toast.LENGTH_SHORT).show();
				flag_loading = true;

			}else if(result.size()==0){
				flag_loading = true;

			}  else{
				adapter.notifyDataSetChanged();
				flag_loading = false;
				pageCount = pageCount +20;
				msprit45List.setSelection(pageCount);
			}
		}
	}

}
