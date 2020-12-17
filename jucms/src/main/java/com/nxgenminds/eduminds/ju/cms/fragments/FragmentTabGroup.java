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
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.ViewProfilePhoto;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.models.GroupInfoModel;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class FragmentTabGroup extends Fragment {

	private TextView founded_on,website,keymembers,vision,mission,bio,noData;
	private ImageView userCoverPic,userProfilePic;
	private ScrollView dataContainer;
	public ImageLoader imageLoader;
	DisplayImageOptions profile_options,cover_option;
	private ProgressDialog pDialog;
	private GroupInfoModel groupinfoData;

	private AlertDialogManager alert = new AlertDialogManager();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_group_jucms, container, false);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
		userCoverPic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), ViewProfilePhoto.class);
				intent.putExtra("ImagePath", groupinfoData.getOrg_cover_photo_path());
				startActivity(intent); 
			}
		});

		userProfilePic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(getActivity(), ViewProfilePhoto.class);
				intent.putExtra("ImagePath", groupinfoData.getOrg_profile_photo_path());
				startActivity(intent); 
			}
		});
	}

	@SuppressWarnings("static-access")
	private void initView() {

		founded_on= (TextView)getView().findViewById(R.id.founderDate);
		website = (TextView)getView().findViewById(R.id.websiteID);
		//keymembers = (TextView)getView().findViewById(R.id.keymembers_content);
		vision = (TextView)getView().findViewById(R.id.vision_content);
		mission = (TextView)getView().findViewById(R.id.mission_content);
		bio = (TextView)getView().findViewById(R.id.bio_content);
		noData = (TextView)getView().findViewById(R.id.noData);
		dataContainer = (ScrollView) getView().findViewById(R.id.groupDataContainerScroll);

		userProfilePic = (ImageView)getView().findViewById(R.id.profilepic);
		userCoverPic=(ImageView)getView().findViewById(R.id.user_cover_pic);
		// setting of universal loader
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
			new GroupInfoFragmentAsync().execute();
		} else{
			alert.showAlertDialog(getActivity(),"Connection Error","Check your Internet Connection",false);
		}


	}

	private class GroupInfoFragmentAsync extends AsyncTask<Void, Void, GroupInfoModel>{

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
		protected GroupInfoModel doInBackground(Void... params) {
			JSONArray GroupInfoResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(Util.API+"group_info/1");
			if(jsonObjectRecived != null){

				try{
					GroupInfoResponse = jsonObjectRecived.getJSONArray("group_info");
				}catch(JSONException e){
					e.printStackTrace();
				}
				groupinfoData = new GroupInfoModel();
				JSONObject group_info_Details;
				try{
					group_info_Details = GroupInfoResponse.getJSONObject(0);
					groupinfoData.setOrg_id(group_info_Details.getString("org_id"));
					groupinfoData.setOrg_name(group_info_Details.getString("org_name"));
					groupinfoData.setOrg_founded_on(group_info_Details.getString("org_founded_on"));
					groupinfoData.setOrg_history(group_info_Details.getString("org_history"));
					groupinfoData.setOrg_website(group_info_Details.getString("org_website"));
					groupinfoData.setOrg_blog(group_info_Details.getString("org_blog"));
					groupinfoData.setOrg_address(group_info_Details.getString("org_address"));
					groupinfoData.setOrg_contact_no(group_info_Details.getString("org_contact_no"));
					groupinfoData.setOrg_vision(group_info_Details.getString("org_vision"));
					groupinfoData.setOrg_mission(group_info_Details.getString("org_mission"));
					groupinfoData.setOrg_email(group_info_Details.getString("org_email"));
					groupinfoData.setOrg_city_id(group_info_Details.getString("org_city_id"));
					groupinfoData.setOrg_cover_photo_path(group_info_Details.getString("org_cover_photo_path"));
					groupinfoData.setOrg_profile_photo_path(group_info_Details.getString("org_profile_photo_path"));
					groupinfoData.setCreated_by(group_info_Details.getString("created_by"));
					groupinfoData.setCreated_date(group_info_Details.getString("created_date"));
					groupinfoData.setGroup_owners_name(group_info_Details.getString("group_owners_name"));
					groupinfoData.setGroup_owners_id(group_info_Details.getString("group_owners_id"));

					return groupinfoData;
				}catch (JSONException e) {
					e.printStackTrace();
				}
			}else{
				return null;
			}
			return null;
		}

		@Override
		protected void onPostExecute(GroupInfoModel app) {
			super.onPostExecute(app);
			pDialog.dismiss();
			if(app == null){
				noData.setVisibility(View.VISIBLE);	
				dataContainer.setVisibility(View.GONE);
			}else{

				noData.setVisibility(View.GONE);	
				dataContainer.setVisibility(View.VISIBLE);

				String Founded[] = app.getOrg_founded_on().split(" ");
				founded_on.setText("Founded in : " +Founded[0]);
				website.setText("Website : " + app.getOrg_website());
				//keymembers.setText("Key Members : " +app.getGroup_owners_name());
				if(app.getOrg_vision()==null ||app.getOrg_vision().equalsIgnoreCase("null")||app.getOrg_vision().equalsIgnoreCase(" "))
				{
					vision.setText("No Data");

				}
				else
				{
					vision.setText(app.getOrg_vision());

				}
				if(app.getOrg_mission()==null ||app.getOrg_mission().equalsIgnoreCase("null")||app.getOrg_mission().equalsIgnoreCase(" "))
				{
					mission.setText("No Data");

				}
				else
				{
					mission.setText(app.getOrg_mission());

				}
				if(app.getOrg_history()==null ||app.getOrg_history().equalsIgnoreCase("null")||app.getOrg_history().equalsIgnoreCase(" "))
				{
					bio.setText("No Data");

				}
				else
				{
					bio.setText(app.getOrg_history());

				}

				//setting proflie pic
				imageLoader.displayImage(app.getOrg_profile_photo_path(), userProfilePic, profile_options, new SimpleImageLoadingListener() {
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
				imageLoader.displayImage(app.getOrg_cover_photo_path(), userCoverPic, cover_option, new SimpleImageLoadingListener() {
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
						);////end setting cover pic
			}

		}

	}

}
