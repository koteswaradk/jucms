package com.nxgenminds.eduminds.ju.cms.Internship;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.models.UserProfileInfoModel;
import com.nxgenminds.eduminds.ju.cms.models.Users;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class CompanyDetailUserDet extends Fragment{
	private TextView user_studied,user_email,user_phone,edu_det,user_bio,user_name,user_address,user_role,user_blog,user_teacher_name,user_parent_name;
	private ImageView userCoverPic,userProfilePic,user_teacher_image,user_parent_image,message;
	public ImageLoader imageLoader;
	private ScrollView scrollViewpage;
	DisplayImageOptions profile_options,cover_option;
	private ProgressDialog pDialog;
	public static final String TAG = "UserFragmentTabUserdetails";    
	private JSONObject jsonObjRecv;
	public int flag;
	private RelativeLayout RelativeL_image_container;
	private LinearLayout LL_titleTop,contact_container,education_container,bio_container,user_parent_container,user_teacher_container;
	UserProfileInfoModel user_model;
	private String error_status;
	private JSONArray user_array;
	private JSONObject user_object;
	ArrayAdapter<String> adapter ;
	private String friedID;
	
	private AlertDialogManager alert = new AlertDialogManager();
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container,  Bundle savedInstanceState) {
		View rooView = inflater.inflate(R.layout.activity_company_admin_user_view, container , false);
		View rootView = inflater.inflate(R.layout.thirdpartyprofileview, container, false);
		CompanyAdminDetailView thirdParty = (CompanyAdminDetailView) getActivity();
		friedID = thirdParty.FriendID;
		Log.i("CompanyDetailUserDet", "inside oncreate");
		return rooView;
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

			}
		});


		userCoverPic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

	}

	private void initView() {

		
		LL_titleTop = (LinearLayout) getView().findViewById(R.id.company_titleTop);
		contact_container= (LinearLayout) getView().findViewById(R.id.company_user_contact_container);
		education_container= (LinearLayout) getView().findViewById(R.id.company_user_edu_container);
		bio_container= (LinearLayout) getView().findViewById(R.id.company_user_bio_container);
		user_parent_container= (LinearLayout) getView().findViewById(R.id.company_user_parent_container);
		user_teacher_container= (LinearLayout) getView().findViewById(R.id.company_user_teacher_container);
		RelativeL_image_container = (RelativeLayout) getView().findViewById(R.id.company_image_container);
		scrollViewpage = (ScrollView) getView().findViewById(R.id.company_userdetailsscrollViewpage);
		user_name = (TextView) getView().findViewById(R.id.company_userName);
		user_role = (TextView) getView().findViewById(R.id.company_user_role);
		user_blog = (TextView) getView().findViewById(R.id.company_userblog);
		user_address = (TextView) getView().findViewById(R.id.company_useraddress);
		user_studied = (TextView)getView().findViewById(R.id.company_userstudied);
		user_email = (TextView)getView().findViewById(R.id.company_useremail);
		user_phone = (TextView)getView().findViewById(R.id.company_userphone);
		user_bio = (TextView)getView().findViewById(R.id.company_user_bio_content);
		user_teacher_name = (TextView)getView().findViewById(R.id.company_user_class_teacher_name);
		user_parent_name = (TextView)getView().findViewById(R.id.company_user_parent_name);
		userProfilePic = (ImageView)getView().findViewById(R.id.company_profilepic);
		userCoverPic=(ImageView)getView().findViewById(R.id.company_user_cover_pic);
		user_parent_image=(ImageView)getView().findViewById(R.id.company_user_parent_image);
		user_teacher_image=(ImageView)getView().findViewById(R.id.company_user_class_teacher_image);
		edu_det=(TextView)getView().findViewById(R.id.company_user_edu_det);
		message = (ImageView)getView().findViewById(R.id.company_msg);

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
			

			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(Util.API+"user/"+friedID);
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
					});

				}


			}

		}


	}
}
