package com.nxgenminds.eduminds.ju.cms.admin;

import java.util.ArrayList;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnTouchListener;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.adapters.UsersAdapter;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.models.UsersModel;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.thirdpartyprofile.ThirdPartyTabMenuActivity;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class Admin extends Fragment{
	private GridView mAdminGridView;
	private ArrayList<UsersModel> mAdminArrayList = new ArrayList<UsersModel>();
	private static int pageCount = 0;
	private static String pagination_Date_String = "";
	private boolean flag_loading = false;
	private boolean pagination_date_flag=true;
	private ProgressDialog pDialog;
	private UsersAdapter adapter;
	private TextView NoAdmin;
	private EditText mSearchAdmin;
	AlertDialogManager alert = new AlertDialogManager();

	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container,  Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_admin, container,false);
		mAdminGridView = (GridView) rootView.findViewById(R.id.admingridview);
		NoAdmin = (TextView)rootView.findViewById(R.id.noAdmin);
		mSearchAdmin = (EditText)rootView.findViewById(R.id.search_admin);
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mAdminArrayList.clear();
		pageCount = 0;
		adapter = new UsersAdapter(getActivity(), mAdminArrayList);
		ConnectionDetector conn = new ConnectionDetector(getActivity());
		if(conn.isConnectingToInternet()){
			new AdminAsyncClass().execute();
		}else{
			alert.showAlertDialog(getActivity(), "Connection Error", "Please check your internet connection", false);
		}
		
		
		mSearchAdmin.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					//searchPaginationFlag=true;
					pageCount = 0;
				ConnectionDetector conn = new ConnectionDetector(getActivity());
				if(conn.isConnectingToInternet()){
					mAdminArrayList.clear();
					new AdminSearchAsyncClass().execute();
				}else{
					alert.showAlertDialog(getActivity(), "Connection Error", "Please check your internet connection", false);
				}
				} 
				
				return false;
			}
		});
		mSearchAdmin.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_UP) {
	             
                    mSearchAdmin.setText("");
					Log.d("Teacher Fragment", "inside setOnTouchListener,on touch befor false");
	                    return false;
	                    
	                }
	            
	         
			return false;
			}
		});



		mAdminGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
				Object object = mAdminGridView.getItemAtPosition(position);
				UsersModel admin_data = (UsersModel)  object;
				Intent intent = new Intent(getActivity(),ThirdPartyTabMenuActivity.class);
				intent.putExtra("UserID", admin_data.getUser_id());
				intent.putExtra("ThirdPartyRole", admin_data.getRole());
				Util.THIRD_PARTY_NAME  = admin_data.getFirstname();
				Util.THIRD_PARTY_ID=admin_data.getUser_id();
				Util.intership_flag=true;
				startActivity(intent);
			}

		});

		mAdminGridView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

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
							new AdminLoadMoreAsyncClass().execute();
						}else{
							alert.showAlertDialog(getActivity(), "Connection Error", "Please check your internet connection", false);
						}
					}
				}

			}
		});


	}

	private class AdminAsyncClass extends AsyncTask<Void, Void, ArrayList<UsersModel>>{

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
		protected  ArrayList<UsersModel> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONArray mStudentsResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(Util.API+"user?user_role=admin");
			if(jsonObjectRecived != null){

				try{
					mStudentsResponse = jsonObjectRecived.getJSONArray("users");
				}catch(JSONException e){
					e.printStackTrace();
				}
				if(mStudentsResponse.length()>0){
					for(int i = 0; i<mStudentsResponse.length();i++){
						UsersModel mStudentData = new UsersModel();
						JSONObject mStudentDetails;
						try{
							mStudentDetails = mStudentsResponse.getJSONObject(i);
							mStudentData.setUser_id(mStudentDetails.getString("user_id"));
							mStudentData.setFirstname(mStudentDetails.getString("firstname"));
							mStudentData.setLastname(mStudentDetails.getString("lastname"));
							mStudentData.setMiddlename(mStudentDetails.getString("middlename"));
							mStudentData.setEmail(mStudentDetails.getString("email"));
							mStudentData.setDob(mStudentDetails.getString("dob"));
							mStudentData.setBio(mStudentDetails.getString("bio"));
							mStudentData.setWebsite_blog(mStudentDetails.getString("website_blog"));
							mStudentData.setCurrent_address(mStudentDetails.getString("current_address"));
							mStudentData.setGuardian_name(mStudentDetails.getString("guardian_name"));
							mStudentData.setMother_name(mStudentDetails.getString("mother_name"));
							mStudentData.setMother_contact_no(mStudentDetails.getString("mother_contact_no"));
							mStudentData.setFather_name(mStudentDetails.getString("father_name"));
							mStudentData.setFather_contact_no(mStudentDetails.getString("father_contact_no"));
							mStudentData.setPincode(mStudentDetails.getString("current_pincode"));
							mStudentData.setLanguages_known(mStudentDetails.getString("languages_known"));
							mStudentData.setEdu(mStudentDetails.getString("edu"));
							mStudentData.setWork(mStudentDetails.getString("work"));
							mStudentData.setAre_you_blood_donor(mStudentDetails.getString("are_you_blood_donor"));
							mStudentData.setGendername(mStudentDetails.getString("gendername"));
							mStudentData.setHome_city_name(mStudentDetails.getString("home_city_name"));
							mStudentData.setRelationship_name(mStudentDetails.getString("relationship_name"));
							mStudentData.setBlood_group_name(mStudentDetails.getString("blood_group_name"));
							mStudentData.setProfile_photo(mStudentDetails.getString("profile_photo"));
							mStudentData.setCover_photo(mStudentDetails.getString("cover_photo"));
							mStudentData.setRole(mStudentDetails.getString("role"));
							mStudentData.setStream_name(mStudentDetails.getString("stream_name"));
							mStudentData.setStream_id(mStudentDetails.getString("stream_id"));
							mStudentData.setSemester(mStudentDetails.getString("semester"));
							mStudentData.setSemester_id(mStudentDetails.getString("semester_id"));
							mStudentData.setSection_name(mStudentDetails.getString("section_name"));
							mStudentData.setSection_id(mStudentDetails.getString("section_id"));
							mStudentData.setCreated_date(mStudentDetails.getString("created_date"));
							pagination_Date_String = mStudentDetails.getString("user_id");
							mAdminArrayList.add(mStudentData);
						}catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					return mAdminArrayList;
				}else
				{
					return null;
				}

			}else{
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<UsersModel> result) {
			super.onPostExecute(result);
			pDialog.dismiss();

			if(result == null){
				NoAdmin.setVisibility(View.VISIBLE);
				NoAdmin.setText("No data found !");
			} 
			else if(result.size()<=0){
				NoAdmin.setVisibility(View.VISIBLE);
				NoAdmin.setText("No data found !");
			}	
			else{
				//adapter = new UsersAdapter(getActivity(), result);
				mAdminGridView.setAdapter(adapter);
				if(result.size() < 20){
					flag_loading = true;
				}else{
					flag_loading = false;
					pagination_date_flag=true;
				}
			}

		}
	}
	
	
	private class AdminSearchAsyncClass extends AsyncTask<Void, Void, ArrayList<UsersModel>>{

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
		protected  ArrayList<UsersModel> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONArray mStudentsResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(Util.API+"user?user_role=admin&search_text="+mSearchAdmin.getText().toString().trim());
			if(jsonObjectRecived != null){

				try{
					mStudentsResponse = jsonObjectRecived.getJSONArray("users");
				}catch(JSONException e){
					e.printStackTrace();
				}
				if(mStudentsResponse.length()>0){
					for(int i = 0; i<mStudentsResponse.length();i++){
						UsersModel mStudentData = new UsersModel();
						JSONObject mStudentDetails;
						try{
							mStudentDetails = mStudentsResponse.getJSONObject(i);
							mStudentData.setUser_id(mStudentDetails.getString("user_id"));
							mStudentData.setFirstname(mStudentDetails.getString("firstname"));
							mStudentData.setLastname(mStudentDetails.getString("lastname"));
							mStudentData.setMiddlename(mStudentDetails.getString("middlename"));
							mStudentData.setEmail(mStudentDetails.getString("email"));
							mStudentData.setDob(mStudentDetails.getString("dob"));
							mStudentData.setBio(mStudentDetails.getString("bio"));
							mStudentData.setWebsite_blog(mStudentDetails.getString("website_blog"));
							mStudentData.setCurrent_address(mStudentDetails.getString("current_address"));
							mStudentData.setGuardian_name(mStudentDetails.getString("guardian_name"));
							mStudentData.setMother_name(mStudentDetails.getString("mother_name"));
							mStudentData.setMother_contact_no(mStudentDetails.getString("mother_contact_no"));
							mStudentData.setFather_name(mStudentDetails.getString("father_name"));
							mStudentData.setFather_contact_no(mStudentDetails.getString("father_contact_no"));
							mStudentData.setPincode(mStudentDetails.getString("current_pincode"));
							mStudentData.setLanguages_known(mStudentDetails.getString("languages_known"));
							mStudentData.setEdu(mStudentDetails.getString("edu"));
							mStudentData.setWork(mStudentDetails.getString("work"));
							mStudentData.setAre_you_blood_donor(mStudentDetails.getString("are_you_blood_donor"));
							mStudentData.setGendername(mStudentDetails.getString("gendername"));
							mStudentData.setHome_city_name(mStudentDetails.getString("home_city_name"));
							mStudentData.setRelationship_name(mStudentDetails.getString("relationship_name"));
							mStudentData.setBlood_group_name(mStudentDetails.getString("blood_group_name"));
							mStudentData.setProfile_photo(mStudentDetails.getString("profile_photo"));
							mStudentData.setCover_photo(mStudentDetails.getString("cover_photo"));
							mStudentData.setRole(mStudentDetails.getString("role"));
							mStudentData.setStream_name(mStudentDetails.getString("stream_name"));
							mStudentData.setStream_id(mStudentDetails.getString("stream_id"));
							mStudentData.setSemester(mStudentDetails.getString("semester"));
							mStudentData.setSemester_id(mStudentDetails.getString("semester_id"));
							mStudentData.setSection_name(mStudentDetails.getString("section_name"));
							mStudentData.setSection_id(mStudentDetails.getString("section_id"));
							mStudentData.setCreated_date(mStudentDetails.getString("created_date"));
							pagination_Date_String = mStudentDetails.getString("user_id");
							mAdminArrayList.add(mStudentData);
						}catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					return mAdminArrayList;
				}else
				{
					return null;
				}

			}else{
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<UsersModel> result) {
			super.onPostExecute(result);
			pDialog.dismiss();

			if(result == null){
				NoAdmin.setVisibility(View.VISIBLE);
				NoAdmin.setText("No data found !");
			} 
			else if(result.size()<=0){
				NoAdmin.setVisibility(View.VISIBLE);
				NoAdmin.setText("No data found !");
			}	
			else{
				//adapter = new UsersAdapter(getActivity(), result);
			    adapter.notifyDataSetChanged();
				if(result.size() < 20){
					flag_loading = true;
				}else{
					flag_loading = false;
					pagination_date_flag=false;
				}
			}

		}
	}



	private class AdminLoadMoreAsyncClass extends AsyncTask<Void, Void, ArrayList<UsersModel>>{

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
		protected ArrayList<UsersModel> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONArray mStudentsResponse = null;
			JSONObject jsonObjectRecived=null;
			if(pagination_date_flag == true){
				jsonObjectRecived = HttpGetClient.sendHttpPost(Util.API+"user?user_role=admin"+"&last_date="+pagination_Date_String);
			}
			if(pagination_date_flag==false){
				jsonObjectRecived = HttpGetClient.sendHttpPost(Util.API+"user?user_role=admin&search_text="+mSearchAdmin.getText().toString().trim()+"&last_date=" + pagination_Date_String);
			}

			if(jsonObjectRecived != null){

				try{
					mStudentsResponse = jsonObjectRecived.getJSONArray("users");
				}catch(JSONException e){
					e.printStackTrace();
				}
				if(mStudentsResponse.length()>0){
					for(int i = 0; i<mStudentsResponse.length();i++){
						UsersModel mStudentData = new UsersModel();
						JSONObject mStudentDetails;
						try{
							mStudentDetails = mStudentsResponse.getJSONObject(i);
							mStudentData.setUser_id(mStudentDetails.getString("user_id"));
							mStudentData.setFirstname(mStudentDetails.getString("firstname"));
							mStudentData.setLastname(mStudentDetails.getString("lastname"));
							mStudentData.setMiddlename(mStudentDetails.getString("middlename"));
							mStudentData.setEmail(mStudentDetails.getString("email"));
							mStudentData.setDob(mStudentDetails.getString("dob"));
							mStudentData.setBio(mStudentDetails.getString("bio"));
							mStudentData.setWebsite_blog(mStudentDetails.getString("website_blog"));
							mStudentData.setCurrent_address(mStudentDetails.getString("current_address"));
							mStudentData.setGuardian_name(mStudentDetails.getString("guardian_name"));
							mStudentData.setMother_name(mStudentDetails.getString("mother_name"));
							mStudentData.setMother_contact_no(mStudentDetails.getString("mother_contact_no"));
							mStudentData.setFather_name(mStudentDetails.getString("father_name"));
							mStudentData.setFather_contact_no(mStudentDetails.getString("father_contact_no"));
							mStudentData.setPincode(mStudentDetails.getString("current_pincode"));
							mStudentData.setLanguages_known(mStudentDetails.getString("languages_known"));
							mStudentData.setEdu(mStudentDetails.getString("edu"));
							mStudentData.setWork(mStudentDetails.getString("work"));
							mStudentData.setAre_you_blood_donor(mStudentDetails.getString("are_you_blood_donor"));
							mStudentData.setGendername(mStudentDetails.getString("gendername"));
							mStudentData.setHome_city_name(mStudentDetails.getString("home_city_name"));
							mStudentData.setRelationship_name(mStudentDetails.getString("relationship_name"));
							mStudentData.setBlood_group_name(mStudentDetails.getString("blood_group_name"));
							mStudentData.setProfile_photo(mStudentDetails.getString("profile_photo"));
							mStudentData.setCover_photo(mStudentDetails.getString("cover_photo"));
							mStudentData.setRole(mStudentDetails.getString("role"));
							mStudentData.setStream_name(mStudentDetails.getString("stream_name"));
							mStudentData.setStream_id(mStudentDetails.getString("stream_id"));
							mStudentData.setSemester(mStudentDetails.getString("semester"));
							mStudentData.setSemester_id(mStudentDetails.getString("semester_id"));
							mStudentData.setSection_name(mStudentDetails.getString("section_name"));
							mStudentData.setSection_id(mStudentDetails.getString("section_id"));
							mStudentData.setCreated_date(mStudentDetails.getString("created_date"));
							pagination_Date_String = mStudentDetails.getString("user_id");
							mAdminArrayList.add(mStudentData);
						}catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					return mAdminArrayList;
				}else
				{

					return null;
				}

			}else{
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<UsersModel> result) {
			super.onPostExecute(result);
			pDialog.dismiss();
			if(result == null){
				flag_loading = true;
				Toast.makeText(getActivity(), "No more data to load!", Toast.LENGTH_SHORT).show();
			} else if(result.size()<0){
				flag_loading = true;
				Toast.makeText(getActivity(), "No more data to load", Toast.LENGTH_SHORT).show();
				
			}

			else{
				//adapter = new UsersAdapter(getActivity(), result);
				adapter.notifyDataSetChanged();
				flag_loading = false;
				pageCount = pageCount +20;
				mAdminGridView.setSelection(pageCount);
			}

		}
	}
}
