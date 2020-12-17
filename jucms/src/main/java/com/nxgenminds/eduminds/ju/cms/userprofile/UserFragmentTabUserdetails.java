package com.nxgenminds.eduminds.ju.cms.userprofile;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.ViewProfilePhoto;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.models.UserProfileInfoModel;
import com.nxgenminds.eduminds.ju.cms.models.Users;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.userprofileupdate.UserProfileUpdate;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.ListUtility;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class UserFragmentTabUserdetails extends Fragment {

	private TextView user_studied,user_work,user_email,user_phone,edu_det,user_bio,user_name,user_address,user_role,user_blog,user_teacher_name,user_parent_name;
	private ImageView userCoverPic,userProfilePic,user_teacher_image,user_parent_image,profile_update;
	public ImageLoader imageLoader;
	private ScrollView scrollViewpage;
	DisplayImageOptions profile_options,cover_option;
	private ProgressDialog pDialog;
	public static final String TAG = "UserFragmentTabUserdetails";    
	private JSONObject jsonObjRecv;
	public int flag;
	private RelativeLayout RelativeL_image_container;
	private LinearLayout LL_titleTop,contact_container,education_container,work_container,bio_container,user_parent_container,user_teacher_container,specialization_container;
	UserProfileInfoModel user_model;
	private String error_status;
	private JSONArray user_array;
	private JSONObject user_object;
	private ListView specialList;
	ArrayAdapter<String> adapter ;
	private String user_profile_pic,user_cover_pic;
	private static final int UPDATE_RESULT = 0x4;

	private AlertDialogManager alert = new AlertDialogManager();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.user_fragmenttab_userdetails, container, false);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
		final ViewTreeObserver.OnScrollChangedListener onScrollChangedListener = new
				ViewTreeObserver.OnScrollChangedListener() {

			@Override
			public void onScrollChanged() {
			}
		};

		scrollViewpage.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				ViewTreeObserver observer = scrollViewpage.getViewTreeObserver();
				observer.addOnScrollChangedListener(onScrollChangedListener);

				int currentapiVersion = android.os.Build.VERSION.SDK_INT;
				if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB){

					userCoverPic.setTranslationY(-LL_titleTop.getChildAt(0).getTop() / 2);

				} 

				return false;
			}
		});




		userProfilePic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), ViewProfilePhoto.class);
				intent.putExtra("ImagePath",user_profile_pic);
				getActivity().startActivity(intent); 
			}
		});


		userCoverPic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), ViewProfilePhoto.class);
				intent.putExtra("ImagePath", user_cover_pic);
				getActivity().startActivity(intent); 
			}
		});

		profile_update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent profile_update = new Intent(getActivity(),UserProfileUpdate.class);
				startActivityForResult(profile_update,UPDATE_RESULT);	
			}
		});
	}

	private void initView() {

		LL_titleTop = (LinearLayout) getView().findViewById(R.id.titleTop);
		contact_container= (LinearLayout) getView().findViewById(R.id.user_contact_container);
		education_container= (LinearLayout) getView().findViewById(R.id.user_edu_container);
		work_container= (LinearLayout) getView().findViewById(R.id.user_work_container);
		bio_container= (LinearLayout) getView().findViewById(R.id.user_bio_container);
		user_parent_container= (LinearLayout) getView().findViewById(R.id.user_parent_container);
		user_teacher_container= (LinearLayout) getView().findViewById(R.id.user_teacher_container);
		RelativeL_image_container = (RelativeLayout) getView().findViewById(R.id.image_container);
		scrollViewpage = (ScrollView) getView().findViewById(R.id.userdetailsscrollViewpage);
		user_name = (TextView) getView().findViewById(R.id.userName);
		user_role = (TextView) getView().findViewById(R.id.user_role);
		user_blog = (TextView) getView().findViewById(R.id.userblog);
		user_address = (TextView) getView().findViewById(R.id.useraddress);
		user_studied = (TextView)getView().findViewById(R.id.userstudied);
		user_work = (TextView)getView().findViewById(R.id.userwork);
		user_email = (TextView)getView().findViewById(R.id.useremail);
		user_phone = (TextView)getView().findViewById(R.id.userphone);
		user_bio = (TextView)getView().findViewById(R.id.user_bio_content);
		user_teacher_name = (TextView)getView().findViewById(R.id.user_class_teacher_name);
		user_parent_name = (TextView)getView().findViewById(R.id.user_parent_name);
		userProfilePic = (ImageView)getView().findViewById(R.id.profilepic);
		userCoverPic=(ImageView)getView().findViewById(R.id.user_cover_pic);
		user_parent_image=(ImageView)getView().findViewById(R.id.user_parent_image);
		user_teacher_image=(ImageView)getView().findViewById(R.id.user_class_teacher_image);
		specialization_container= (LinearLayout) getView().findViewById(R.id.user_specialization_container);
		specialList=(ListView)getView().findViewById(R.id.specialization);
		edu_det=(TextView)getView().findViewById(R.id.user_edu_det);
		profile_update = (ImageView)getView().findViewById(R.id.user_profile_update);

		imageLoader =imageLoader.getInstance();
		profile_options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.placeholder)
		.showImageForEmptyUri(R.drawable.placeholder) 
		.showImageOnFail(R.drawable.placeholder)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();

		cover_option = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.placeholder_timefeed)
		.showImageForEmptyUri(R.drawable.placeholder_timefeed) 
		.showImageOnFail(R.drawable.placeholder_timefeed)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();

		// according to user role show the informations

		if(Util.ROLE.equalsIgnoreCase("class monitor"))
		{
			//	profile_update.setVisibility(View.GONE);
			edu_det.setVisibility(View.VISIBLE);
			contact_container.setVisibility(View.VISIBLE);
			education_container.setVisibility(View.GONE);
			bio_container.setVisibility(View.GONE);
			work_container.setVisibility(View.GONE);
			user_parent_container.setVisibility(View.VISIBLE);
			user_teacher_container.setVisibility(View.VISIBLE);
			specialization_container.setVisibility(View.GONE);
		}
		else if(Util.ROLE.equalsIgnoreCase("teacher")){
			//	profile_update.setVisibility(View.VISIBLE);
			edu_det.setVisibility(View.GONE);
			contact_container.setVisibility(View.VISIBLE);
			education_container.setVisibility(View.VISIBLE);
			bio_container.setVisibility(View.VISIBLE);
			work_container.setVisibility(View.GONE);
			user_parent_container.setVisibility(View.GONE);
			user_teacher_container.setVisibility(View.GONE);
			specialization_container.setVisibility(View.VISIBLE);
			// need to add specialist
		}
		else if(Util.ROLE.equalsIgnoreCase("alumni")){
			//profile_update.setVisibility(View.VISIBLE);
			edu_det.setVisibility(View.GONE);
			contact_container.setVisibility(View.VISIBLE);
			education_container.setVisibility(View.VISIBLE);
			bio_container.setVisibility(View.VISIBLE);
			work_container.setVisibility(View.VISIBLE);
			user_parent_container.setVisibility(View.GONE);
			user_teacher_container.setVisibility(View.GONE);
			specialization_container.setVisibility(View.GONE);
		}
		else if(Util.ROLE.equalsIgnoreCase("attendence admin") ||Util.ROLE.equalsIgnoreCase("feedback admin") ||
				Util.ROLE.equalsIgnoreCase("internship admin") || Util.ROLE.equalsIgnoreCase("admin") || Util.ROLE.equalsIgnoreCase("sprit45 admin")
				|| Util.ROLE.equalsIgnoreCase("timetable admin")|| Util.ROLE.equalsIgnoreCase("alumni admin")){
			//profile_update.setVisibility(View.VISIBLE);
			contact_container.setVisibility(View.VISIBLE);
			edu_det.setVisibility(View.GONE);
			education_container.setVisibility(View.VISIBLE);
			bio_container.setVisibility(View.VISIBLE);
			work_container.setVisibility(View.VISIBLE);
			user_parent_container.setVisibility(View.GONE);
			user_teacher_container.setVisibility(View.GONE);
			specialization_container.setVisibility(View.GONE);
		}
		else if(Util.ROLE.equalsIgnoreCase("student") ||Util.ROLE.equalsIgnoreCase("parent") ){
			//profile_update.setVisibility(View.GONE);
			edu_det.setVisibility(View.VISIBLE);
			contact_container.setVisibility(View.VISIBLE);
			education_container.setVisibility(View.VISIBLE);
			bio_container.setVisibility(View.VISIBLE);
			work_container.setVisibility(View.GONE);
			user_parent_container.setVisibility(View.VISIBLE);
			user_teacher_container.setVisibility(View.VISIBLE);
			specialization_container.setVisibility(View.GONE);
		}

		else if(Util.ROLE.equalsIgnoreCase("office admin") ||Util.ROLE.equalsIgnoreCase("office staff") ){
			//profile_update.setVisibility(View.GONE);
			edu_det.setVisibility(View.VISIBLE);
			contact_container.setVisibility(View.VISIBLE);
			education_container.setVisibility(View.VISIBLE);
			bio_container.setVisibility(View.VISIBLE);
			work_container.setVisibility(View.VISIBLE);
			user_parent_container.setVisibility(View.GONE);
			user_teacher_container.setVisibility(View.GONE);
			specialization_container.setVisibility(View.GONE);
		}

		ConnectionDetector conn = new ConnectionDetector(getActivity());
		if(conn.isConnectingToInternet()){
			new UserProfileInfoModelFragmentAsync().execute();
		} else{
			alert.showAlertDialog(getActivity(),"Connection Error","Check your Internet Connection",false);
		}

	}

	private class UserProfileInfoModelFragmentAsync extends AsyncTask<Void, Void, UserProfileInfoModel>{

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
		protected UserProfileInfoModel doInBackground(Void... params) {

			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(Util.API+"user/"+Util.USER_ID);
			user_model=new Gson().fromJson(jsonObjectRecived.toString(), UserProfileInfoModel.class);
			return user_model;
		}

		@Override
		protected void onPostExecute(UserProfileInfoModel user_info_model) {
			super.onPostExecute(user_info_model);
			pDialog.dismiss();
			if(user_info_model != null){

				error_status=user_info_model.getStatus();
				if(error_status.equalsIgnoreCase("1"))
				{
					Users user_info = new Users();
					user_info = user_info_model.getUsers()[0];

					if(user_info.getLastname()==null||user_info.getLastname().equalsIgnoreCase("null") || user_info.getLastname().equalsIgnoreCase("")){
						user_name.setText(user_info.getFirstname());}
					else{user_name.setText(user_info.getFirstname()+" "+user_info.getLastname());}

					if(user_info.getRole()==null||user_info.getRole().equalsIgnoreCase("null") ||user_info.getRole().equalsIgnoreCase("")){
						user_role.setText("NA");}
					else{user_role.setText(user_info.getRole());}

					if(user_info.getMobile()==null||user_info.getMobile().equalsIgnoreCase("null") || user_info.getMobile().equalsIgnoreCase("")){
						user_phone.setText("NA");}
					else{user_phone.setText(user_info.getMobile());}

					if(user_info.getEdu()==null||user_info.getEdu().equalsIgnoreCase("null") || user_info.getEdu().equalsIgnoreCase("")){
						user_studied.setText("NA");}
					else{user_studied.setText(user_info.getEdu());}

					if(user_info.getWork()==null||user_info.getWork().equalsIgnoreCase("null") || user_info.getWork().equalsIgnoreCase("")){
						user_work.setText("NA");}
					else{user_work.setText(user_info.getWork());}

					if(user_info.getEmail()==null||user_info.getEmail().equalsIgnoreCase("null") ||user_info.getEmail().equalsIgnoreCase("")){
						user_email.setText("NA");}
					else{user_email.setText(user_info.getEmail());}

					if(user_info.getBio()==null||user_info.getBio().equalsIgnoreCase("null") || user_info.getBio().equalsIgnoreCase("")){
						user_bio.setText("NA");}
					else{user_bio.setText(user_info.getBio());}

					if(user_info.getCurrent_city_name()==null||user_info.getCurrent_city_name().equalsIgnoreCase("null") || user_info.getCurrent_city_name().equalsIgnoreCase("")){
						user_address.setText("NA");}
					else{user_address.setText(user_info.getCurrent_city_name());}

					if(user_info.getWebsite_blog()==null||user_info.getWebsite_blog().equalsIgnoreCase("null") || user_info.getWebsite_blog().equalsIgnoreCase("")){
						user_blog.setText("NA");}
					else{user_blog.setText(user_info.getWebsite_blog());}

					if(user_info.getClass_teacher_lastname()==null||user_info.getClass_teacher_lastname().equalsIgnoreCase("null") || user_info.getClass_teacher_lastname().equalsIgnoreCase("")){
						user_teacher_name.setText(user_info.getClass_teacher_firstname());}
					else{user_teacher_name.setText(user_info.getClass_teacher_firstname()+" "+user_info.getClass_teacher_lastname());}

					if(user_info.getGuardian_name()==null||user_info.getGuardian_name().equalsIgnoreCase("null") || user_info.getGuardian_name().equalsIgnoreCase("")){
						user_parent_name.setText("NA");}
					else{user_parent_name.setText(user_info.getGuardian_name());}

					if(user_info.getJoined_year()==null||user_info.getJoined_year().equalsIgnoreCase("null") || user_info.getJoined_year().equalsIgnoreCase("")){
						edu_det.setText("NA");
					}else{
						edu_det.setText(user_info.getJoined_year()+ " "+ "Batch," +user_info.getStudent_stream_name() +
								","+ user_info.getStudent_semester() + "," +user_info.getStudent_section_name());
					}
					user_profile_pic=user_info.getProfile_photo();
					user_cover_pic=user_info.getCover_photo();

					// specilization list for subjects for teachers
					adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,android.R.id.text1,user_info.getTeacher_taking_subjects());
					specialList.setAdapter(adapter);
					ListUtility.setListViewHeightBasedOnChildren(specialList);

					//setting proflie pic
					imageLoader.displayImage(user_info.getProfile_photo(), userProfilePic, profile_options, new SimpleImageLoadingListener() {
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
					//setting cover pic
					imageLoader.displayImage(user_info.getCover_photo(), userCoverPic, cover_option, new SimpleImageLoadingListener() {
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
					//setting proflie pic for parent
					imageLoader.displayImage(user_info.getGaurdian_profile_photo(), user_parent_image, profile_options, new SimpleImageLoadingListener() {
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
					//setting proflie pic for teacher
					imageLoader.displayImage(user_info.getClass_teacher_profile_photo(), user_teacher_image, profile_options, new SimpleImageLoadingListener() {
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


			}

		}


	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode !=getActivity().RESULT_OK) {

			return;
		}

		switch (requestCode) {

		case UPDATE_RESULT:
			if(resultCode == getActivity().RESULT_OK && null != data)
			{
				if(data.getStringExtra("update").equalsIgnoreCase("update"))
				{
					new  UserProfileInfoModelFragmentAsync().execute();
				}
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data); 
	}
}