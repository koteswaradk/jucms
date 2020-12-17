package com.nxgenminds.eduminds.ju.cms.events;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.adapters.ThemesAdapter;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;



public class EventThemes extends Activity {

	ListView themes;
	private ArrayList<EventThemesModel> themes_search = new ArrayList<EventThemesModel>();
	private ProgressDialog pDialog;
	private ThemesAdapter adapter;
	AlertDialogManager alert = new AlertDialogManager();

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_themes);

		themes=(ListView)findViewById(R.id.themesLists);

		ConnectionDetector conn = new ConnectionDetector(EventThemes.this);
		if(conn.isConnectingToInternet()){
			new ThemesAsyncClass().execute();

		}else{
			alert.showAlertDialog(getApplicationContext(), "Connection Error", "Please check your internet connection", false);
		}

		themes.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Object object=themes.getItemAtPosition(arg2);
				EventThemesModel theme_item = (EventThemesModel) object;
				Intent intent = new Intent();
				intent.putExtra("themeImage", theme_item.getActual_theme_path());
				intent.putExtra("theme_id", theme_item.getEvent_theme_id());
				setResult(RESULT_OK,intent);
				finish();		
			}
		});
	}
	private class ThemesAsyncClass extends AsyncTask<Void, Void, ArrayList<EventThemesModel>>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			// Showing progress dialog before sending http request
			if(pDialog == null){
				pDialog = Util.createProgressDialog(EventThemes.this);
				pDialog.setCancelable(false);
				pDialog.show();}
			else{
				pDialog.setCancelable(false);
				pDialog.show();
			}}


		@Override
		protected  ArrayList<EventThemesModel> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONArray themeResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(Util.API+"event_theme");
			if(jsonObjectRecived != null){

				try{
					themeResponse = jsonObjectRecived.getJSONArray("event_theme");
				}catch(JSONException e){
					e.printStackTrace();
				}
				for(int i = 0; i< themeResponse.length();i++){
					EventThemesModel themeData = new EventThemesModel();
					JSONObject themeDetails;
					try{
						themeDetails = themeResponse.getJSONObject(i);
						themeData.setEvent_theme_id(themeDetails.getString("event_theme_id"));
						themeData.setEvent_theme_name(themeDetails.getString("event_theme_name"));
						themeData.setEvent_theme_size(themeDetails.getString("event_theme_size"));
						themeData.setEvent_theme_dimensions(themeDetails.getString("event_theme_dimensions"));
						themeData.setActual_theme_path(themeDetails.getString("actual_theme_path"));
						themeData.setActual_theme_base_name(themeDetails.getString("actual_theme_base_name"));
						themeData.setUploaded_by(themeDetails.getString("uploaded_by"));
						themeData.setUploaded_date(themeDetails.getString("uploaded_date"));
						
						themes_search.add(themeData);
					}catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return themes_search;
			}else{
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<EventThemesModel> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();

			if(result == null){
				Toast.makeText(getApplicationContext(), "No images Found", Toast.LENGTH_SHORT).show();
			}else if(result.size()==0){
			}  else{
				adapter = new ThemesAdapter(getApplicationContext(), result);
				themes.setAdapter(adapter);

			}
		}

	}



}

