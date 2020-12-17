package com.nxgenminds.eduminds.ju.cms.jgigroup;


import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpPostClientImageUpdate;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class GroupFragmentTabGallery extends Fragment{

	private Button viewVideos,viewPhotos;
	private ImageButton addphotosvideos;
	RadioButton photos_add,videos_add;
	RadioGroup  select_upload; 

	private ProgressDialog pDialog;
	private File picfile,videoFile;
	private static final int SELECT_PICTURE = 1;
	private static final int SELECT_VIDEO = 2;
	
	private AlertDialogManager alert = new AlertDialogManager();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.user_fragmenttab_gallery, container, false);
		viewPhotos = (Button) view.findViewById(R.id.userPhotos);
		addphotosvideos = (ImageButton) view.findViewById(R.id.userAddPhotoVideo);
		addphotosvideos.setVisibility(View.GONE);
		viewVideos = (Button) view.findViewById(R.id.userVideos);
		//adding active color 
		viewPhotos.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_button));
		viewVideos.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_button_gray));
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		// by default
		GroupFragmentTabPhotos photos = new  GroupFragmentTabPhotos();
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.content_frame_photos,photos);
		//fragmentTransaction.addToBackStack(null);	
		//getChildFragmentManager().popBackStack();
		fragmentTransaction.commit();
		//end
		/*if(!Util.ADMIN.equalsIgnoreCase("1")){
			addphotosvideos.setVisibility(View.GONE);
		}else{
			addphotosvideos.setVisibility(View.VISIBLE);
		}
*/
		viewVideos.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewVideos.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_button));
				viewPhotos.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_button_gray));
				GroupFragmentTabVideos videosfragment = new  GroupFragmentTabVideos();
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.content_frame_photos, videosfragment);
				getChildFragmentManager().popBackStack();
				fragmentTransaction.commit();

			}
		});

		viewPhotos.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewPhotos.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_button));
				viewVideos.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_button_gray));
				GroupFragmentTabPhotos photosfragment = new  GroupFragmentTabPhotos();
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.content_frame_photos, photosfragment);
				getChildFragmentManager().popBackStack();
				fragmentTransaction.commit();
			}
		});

		addphotosvideos.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*	UserFragmentTabUploads uploadfragment = new  UserFragmentTabUploads();
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.content_frame_photos, uploadfragment);
				getChildFragmentManager().popBackStack();
				fragmentTransaction.commit();*/

				final Dialog dialog = new Dialog(getActivity());  
				dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);  
				dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
						WindowManager.LayoutParams.FLAG_FULLSCREEN);  
				dialog.setContentView(R.layout.dialog_upload);  
				//dialog.getWindow().setBackgroundDrawable(  
				//new ColorDrawable(Color.argb(255, 204, 255, 229))); 

				dialog.show(); 

				select_upload=(RadioGroup)dialog.findViewById(R.id.select_upload);
				photos_add =(RadioButton)dialog.findViewById(R.id.photos_upload);
				photos_add.setChecked(false);
				videos_add =(RadioButton)dialog.findViewById(R.id.videos_upload);
				videos_add.setChecked(false);

				// view profile pic				
				photos_add.setOnClickListener(new Button.OnClickListener(){

					@Override
					public void onClick(View v) {
						//upload photos
						dialog.dismiss();
						Toast.makeText(getActivity(), "Select image to upload ", Toast.LENGTH_SHORT).show();

						Intent intent = new Intent(
								Intent.ACTION_PICK,
								android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						startActivityForResult(Intent.createChooser(intent,
								"Select Picture"), SELECT_PICTURE);

					}});
				videos_add.setOnClickListener(new Button.OnClickListener(){

					@Override
					public void onClick(View v) {
						//upload videos
						dialog.dismiss();
						Toast.makeText(getActivity(), "Select video to upload ", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(
								Intent.ACTION_PICK,
								android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
						startActivityForResult(Intent.createChooser(intent,
								"Select Video"), SELECT_VIDEO);
					}});

			}
		});

	}
	private class PicUploadAsync extends AsyncTask<Void, Void, String>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			// Showing progress dialog before sending http request
			if(pDialog == null){
				pDialog = Util.createProgressDialog(getActivity());
				pDialog.setCancelable(false);
				pDialog.show();}
			else{
				pDialog.setCancelable(false);
				pDialog.show();
			}}


		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			//	MyHttpClientImageUpload.sendHttpPostImage(Util.API+"photo", picfile);
			String error = null;

			JSONObject jsonObjectReceived = HttpPostClientImageUpdate.sendHttpPostImage(Util.API+"photo",picfile,3);
			try
			{
				error = jsonObjectReceived.getString("error");
				
			} 
			catch(JSONException e){}

			return error;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if(result.equalsIgnoreCase("false")){
				Toast.makeText(getActivity(), "Image uploaded successfully",Toast.LENGTH_SHORT).show();
			}
			// by default
			GroupFragmentTabPhotos photos = new  GroupFragmentTabPhotos();
			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.content_frame_photos,photos);
			fragmentTransaction.commit();
			//end

		}

	}

	private class VideoUploadAsync extends AsyncTask<Void, Void, String>{

		@Override
		protected void onPreExecute() {

			// TODO Auto-generated method stub
			// Showing progress dialog before sending http request
			if(pDialog == null){
				pDialog = Util.createProgressDialog(getActivity());
				pDialog.setCancelable(false);
				pDialog.show();}
			else{
				pDialog.setCancelable(false);
				pDialog.show();
			}}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			//	MyHttpClientImageUpload.sendHttpPostImage(Util.API+"video", videoFile);
			String error = null;

			JSONObject jsonObjectReceived = HttpPostClientImageUpdate.sendHttpPostImage(Util.API+"video",videoFile,4);
			try
			{
				error = jsonObjectReceived.getString("error");
				
			} 
			catch(JSONException e){}

			return error;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if(result.equalsIgnoreCase("false")){
				Toast.makeText(getActivity(), "Video uploaded successfully",Toast.LENGTH_SHORT).show();
			}
			GroupFragmentTabVideos videosfragment = new  GroupFragmentTabVideos();
			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.content_frame_photos, videosfragment);
			getChildFragmentManager().popBackStack();
			fragmentTransaction.commit();
		}

	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK && null != data) {

				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };
				Cursor cursor = getActivity().getContentResolver().query(selectedImage,filePathColumn, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String picturePath = cursor.getString(columnIndex);
				cursor.close();
				picfile = new File(picturePath);
				
				ConnectionDetector conn = new ConnectionDetector(getActivity());
	            if(conn.isConnectingToInternet()){
	            	new PicUploadAsync().execute();
	            } else{
	            	alert.showAlertDialog(getActivity(),"Connection Error","Check your Internet Connection",false);
	            }


				


			}

			if (requestCode == SELECT_VIDEO && resultCode == Activity.RESULT_OK && null != data) {

				Uri selectedVideo = data.getData();
				String[] filePathColumn = { MediaStore.Video.Media.DATA };
				Cursor cursor = getActivity().getContentResolver().query(selectedVideo,filePathColumn, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String videoPath = cursor.getString(columnIndex);
				cursor.close();
				videoFile = new File(videoPath);
				
				ConnectionDetector conn = new ConnectionDetector(getActivity());
	            if(conn.isConnectingToInternet()){
	            	new VideoUploadAsync().execute();
	            } else{
	            	alert.showAlertDialog(getActivity(),"Connection Error","Check your Internet Connection",false);
	            }
	           } 

		}
	}

}
