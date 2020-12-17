package com.nxgenminds.eduminds.ju.cms.fragments;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nxgenminds.eduminds.ju.cms.LearningOutcomeStudentActivity;
import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.models.StudentWorkingAtModel;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class StudentWorkingAtFragment extends Fragment{

	private ProgressDialog pDialog;
	private ImageView company_logo;
	private TextView companyName,companyCityName,companyWebsite,designation,department,from,to;
	private TextView mentorName,MentorDesignation,mentorEmail,mentorPhNo;
	public ImageLoader imageLoader;
	public DisplayImageOptions options;
	private Button learningOutcome;
	private String internshipID;
	private TextView desig_lbl,depart_lbl,frm_lbl,to_lbl,men_lbl,mdesig_lbl,email_lbl,phn_lbl,noData;
	private LinearLayout one,two,three;
	private JSONObject jsonObjectRecived;
	public String FriendID;

	private AlertDialogManager alert = new AlertDialogManager();
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container,  Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.studentworking_fragment, container , false);
		company_logo = (ImageView) rootView.findViewById(R.id.internship_student_view_company_image);
		companyName = (TextView) rootView.findViewById(R.id.internship_student_view_company_name);
		companyCityName = (TextView) rootView.findViewById(R.id.internship_student_view_company_city);
		companyWebsite = (TextView) rootView.findViewById(R.id.internship_student_view_company_website);
		designation = (TextView) rootView.findViewById(R.id.internship_student_view_designation);
		department = (TextView) rootView.findViewById(R.id.internship_student_view_department);
		from = (TextView) rootView.findViewById(R.id.internship_student_view_from);
		to = (TextView) rootView.findViewById(R.id.internship_student_view_to);
		mentorName = (TextView) rootView.findViewById(R.id.internship_student_view_mentor);
		MentorDesignation = (TextView) rootView.findViewById(R.id.internship_student_view_mentor_desgn);
		mentorEmail = (TextView) rootView.findViewById(R.id.internship_student_view_mentor_email);
		mentorPhNo = (TextView) rootView.findViewById(R.id.internship_student_view_mentor_phone);
		learningOutcome = (Button) rootView.findViewById(R.id.internship_student_view_learning_outcome);

		desig_lbl=(TextView)rootView.findViewById(R.id.internship_student_view_designationlbl);
		depart_lbl=(TextView)rootView.findViewById(R.id.internship_student_view_departmentlbl);
		frm_lbl=(TextView)rootView.findViewById(R.id.internship_student_view_fromlbl);
		to_lbl=(TextView)rootView.findViewById(R.id.internship_student_view_tolbl);
		men_lbl=(TextView)rootView.findViewById(R.id.internship_student_view_mentorlbl);
		mdesig_lbl=(TextView)rootView.findViewById(R.id.internship_student_view_mentor_desgn_lbl);
		email_lbl=(TextView)rootView.findViewById(R.id.internship_student_view_emaillbl);
		phn_lbl=(TextView)rootView.findViewById(R.id.internship_student_view_phonelbl);
		noData=(TextView)rootView.findViewById(R.id.no_working_data);

		one =(LinearLayout)rootView.findViewById(R.id.container_1);
		two =(LinearLayout)rootView.findViewById(R.id.container_2);
		three =(LinearLayout)rootView.findViewById(R.id.container_3);
		return rootView;
	}

	@Override
	public void onActivityCreated( Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		FriendID = Util.THIRD_PARTY_ID;
		// setting of universal loader
		imageLoader =imageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.placeholder)
		.showImageForEmptyUri(R.drawable.placeholder) 
		.showImageOnFail(R.drawable.placeholder)
		.cacheInMemory(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		
		ConnectionDetector conn = new ConnectionDetector(getActivity());
        if(conn.isConnectingToInternet()){
        	new StudentWorkingAsync().execute();
        } else{
        	alert.showAlertDialog(getActivity(),"Connection Error","Check your Internet Connection",false);
        }
		

		learningOutcome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),LearningOutcomeStudentActivity.class);
				intent.putExtra("internshipID", internshipID);
				startActivity(intent);
			}
		});

	}

	private class StudentWorkingAsync extends AsyncTask<Void, Void, JSONObject>{

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
		protected JSONObject doInBackground(Void... params) {
			JSONArray UserProfileInfoModelResponse = null;
			if(Util.intership_flag){
				jsonObjectRecived = HttpGetClient.sendHttpPost(Util.API+"internship?student_user_id="+FriendID+"&currently_working=1");
			}
			else
			{
				jsonObjectRecived = HttpGetClient.sendHttpPost(Util.API+"internship?student_user_id="+Util.USER_ID+"&currently_working=1");
			}
			if(jsonObjectRecived != null){

				try{
					UserProfileInfoModelResponse = jsonObjectRecived.getJSONArray("internships");   
				}catch(JSONException e){
					e.printStackTrace();
				}
				StudentWorkingAtModel workingInfoModelData = new StudentWorkingAtModel();
				JSONObject companyprofile_info_Details;
				if(UserProfileInfoModelResponse.length() > 0){
					try{
						companyprofile_info_Details = UserProfileInfoModelResponse.getJSONObject(0);
						workingInfoModelData.setInternship_id(companyprofile_info_Details.getString("internship_id"));
						workingInfoModelData.setStudent_user_id(companyprofile_info_Details.getString("student_user_id"));
						workingInfoModelData.setCompany_id(companyprofile_info_Details.getString("company_id"));
						workingInfoModelData.setDepartment(companyprofile_info_Details.getString("department"));
						workingInfoModelData.setDesignation(companyprofile_info_Details.getString("designation"));
						workingInfoModelData.setFrom_date(companyprofile_info_Details.getString("from_date"));
						workingInfoModelData.setTo_date(companyprofile_info_Details.getString("to_date"));
						workingInfoModelData.setMentor_name(companyprofile_info_Details.getString("mentor_name"));
						workingInfoModelData.setMentor_designation(companyprofile_info_Details.getString("mentor_designation"));
						
						 /*if(workingInfoModelData.getMentor_designation().equals(null));
							{
								workingInfoModelData.setMentor_designation("NA");
							}*/
						workingInfoModelData.setMentor_email(companyprofile_info_Details.getString("mentor_email"));
						workingInfoModelData.setMentor_phone(companyprofile_info_Details.getString("mentor_phone"));
						workingInfoModelData.setCurrently_working(companyprofile_info_Details.getString("currently_working"));
						workingInfoModelData.setCompany_name(companyprofile_info_Details.getString("company_name"));
						workingInfoModelData.setCompany_desc(companyprofile_info_Details.getString("company_desc"));
						workingInfoModelData.setCompany_website(companyprofile_info_Details.getString("company_website"));
						workingInfoModelData.setCompany_logo_path(companyprofile_info_Details.getString("company_logo_path"));
						workingInfoModelData.setCity_name(companyprofile_info_Details.getString("city_name"));
						//workingInfoModelData.setCity_id(companyprofile_info_Details.getString("city_id"));
					}catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
			return jsonObjectRecived;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			pDialog.dismiss();
			super.onPostExecute(result);
			try {
				if(result.getJSONArray("internships")==null){
					company_logo.setVisibility(View.GONE);
					companyName.setVisibility(View.GONE);
					companyCityName.setVisibility(View.GONE);
					companyWebsite.setVisibility(View.GONE);
					designation.setVisibility(View.GONE);
					department.setVisibility(View.GONE);
					from.setVisibility(View.GONE);
					to.setVisibility(View.GONE);
					mentorName.setVisibility(View.GONE);
					MentorDesignation.setVisibility(View.GONE);
					mentorEmail.setVisibility(View.GONE);
					mentorPhNo.setVisibility(View.GONE);
					learningOutcome.setVisibility(View.GONE);
					one.setVisibility(View.GONE);
					two.setVisibility(View.GONE);
					three.setVisibility(View.GONE);
					desig_lbl.setVisibility(View.GONE);
					depart_lbl.setVisibility(View.GONE);
					frm_lbl.setVisibility(View.GONE);
					to_lbl.setVisibility(View.GONE);
					men_lbl.setVisibility(View.GONE);
					mdesig_lbl.setVisibility(View.GONE);
					email_lbl.setVisibility(View.GONE);
					phn_lbl.setVisibility(View.GONE);
					noData.setVisibility(View.VISIBLE);

				}else if(result.getJSONArray("internships").length()<=0){
					company_logo.setVisibility(View.GONE);
					companyName.setVisibility(View.GONE);
					companyCityName.setVisibility(View.GONE);
					companyWebsite.setVisibility(View.GONE);
					designation.setVisibility(View.GONE);
					department.setVisibility(View.GONE);
					from.setVisibility(View.GONE);
					to.setVisibility(View.GONE);
					mentorName.setVisibility(View.GONE);
					MentorDesignation.setVisibility(View.GONE);
					mentorEmail.setVisibility(View.GONE);
					mentorPhNo.setVisibility(View.GONE);
					learningOutcome.setVisibility(View.GONE);
					one.setVisibility(View.GONE);
					two.setVisibility(View.GONE);
					three.setVisibility(View.GONE);
					desig_lbl.setVisibility(View.GONE);
					depart_lbl.setVisibility(View.GONE);
					frm_lbl.setVisibility(View.GONE);
					to_lbl.setVisibility(View.GONE);
					men_lbl.setVisibility(View.GONE);
					mdesig_lbl.setVisibility(View.GONE);
					email_lbl.setVisibility(View.GONE);
					phn_lbl.setVisibility(View.GONE);
					noData.setVisibility(View.VISIBLE);
				}
				else
				{
					company_logo.setVisibility(View.VISIBLE);
					companyName.setVisibility(View.VISIBLE);
					companyCityName.setVisibility(View.VISIBLE);
					companyWebsite.setVisibility(View.VISIBLE);
					designation.setVisibility(View.VISIBLE);
					department.setVisibility(View.VISIBLE);
					from.setVisibility(View.VISIBLE);
					to.setVisibility(View.VISIBLE);
					mentorName.setVisibility(View.VISIBLE);
					MentorDesignation.setVisibility(View.VISIBLE);
					mentorEmail.setVisibility(View.VISIBLE);
					mentorPhNo.setVisibility(View.VISIBLE);
					if(Util.ROLE.equalsIgnoreCase("parent"))
					{
						learningOutcome.setVisibility(View.GONE);
					}
					else
					{
						learningOutcome.setVisibility(View.VISIBLE);
					}
					one.setVisibility(View.VISIBLE);
					two.setVisibility(View.VISIBLE);
					three.setVisibility(View.VISIBLE);
					desig_lbl.setVisibility(View.VISIBLE);
					depart_lbl.setVisibility(View.VISIBLE);
					frm_lbl.setVisibility(View.VISIBLE);
					to_lbl.setVisibility(View.VISIBLE);
					men_lbl.setVisibility(View.VISIBLE);
					mdesig_lbl.setVisibility(View.VISIBLE);
					email_lbl.setVisibility(View.VISIBLE);
					phn_lbl.setVisibility(View.VISIBLE);
					noData.setVisibility(View.GONE);

					JSONArray companyDet = new JSONArray();
					JSONObject cmp_obj= new JSONObject();
					companyDet=result.getJSONArray("internships");
					cmp_obj=companyDet.getJSONObject(0);
					internshipID = cmp_obj.getString("internship_id");

					companyName.setText(cmp_obj.getString("company_name"));
					companyCityName.setText(cmp_obj.getString("city_name"));
					companyWebsite.setText(cmp_obj.getString("company_website"));
					designation.setText(cmp_obj.getString("designation"));
					department.setText(cmp_obj.getString("department"));
					from.setText(cmp_obj.getString("from_date"));
					to.setText(cmp_obj.getString("to_date"));
					mentorName.setText(cmp_obj.getString("mentor_name"));
					MentorDesignation.setText(cmp_obj.getString("mentor_designation"));
					/* if(MentorDesignation.getText().equals(null))
						{
							MentorDesignation.setText("NA");
						}*/
						
					mentorEmail.setText(cmp_obj.getString("mentor_email"));
					mentorPhNo.setText(cmp_obj.getString("mentor_phone"));

					//setting proflie pic
					imageLoader.displayImage(cmp_obj.getString("company_logo_path"), company_logo, options, new SimpleImageLoadingListener() {
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
							);// end setting profile pic

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

}
