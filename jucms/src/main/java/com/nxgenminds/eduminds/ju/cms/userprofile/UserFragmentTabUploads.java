package com.nxgenminds.eduminds.ju.cms.userprofile;

import java.io.File;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.proxy.MyHttpClientImageUpload;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class UserFragmentTabUploads extends Fragment {

	private Button addPhoto;
	private Button addVideo;
	private Button addCameraPhoto;
	private Button addCameraVideo;
	private ProgressDialog pDialog;
	private File picfile,videoFile;
	private static final int SELECT_PICTURE = 1;
	private static final int SELECT_VIDEO = 2;
	private static final int SELECT_CAMERA_IMAGE_REQUEST = 3;
	private static final int SELECT_CAMERA_VIDEO_REQUEST = 4;
	private String selectedImagePath,selectedVideoPath,selectedCameraPicPath,selectedCameraVideoPath;
	
	private AlertDialogManager alert = new AlertDialogManager();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.user_fragmenttab_upload, container, false);
		addPhoto = (Button) view.findViewById(R.id.userAddPhoto);
		addVideo = (Button) view.findViewById(R.id.userAddVideo);
		addCameraPhoto = (Button) view.findViewById(R.id.userCameraAddPhoto);
		addCameraVideo	= (Button) view.findViewById(R.id.userCameraAddVideo);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		addCameraPhoto.setVisibility(View.GONE);
		addCameraVideo.setVisibility(View.GONE);
		addPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(Intent.createChooser(intent,
						"Select Picture"), SELECT_PICTURE);
			}
		});


		addVideo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(Intent.createChooser(intent,
						"Select Video"), SELECT_VIDEO);
			}
		});

		/*addCameraPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
				startActivityForResult(cameraIntent, SELECT_CAMERA_IMAGE_REQUEST); 
			}
		});

		addCameraVideo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
				startActivityForResult(cameraIntent, SELECT_CAMERA_VIDEO_REQUEST); 
			}
		});*/
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
			/*if (requestCode == SELECT_CAMERA_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {  
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.ACTION_IMAGE_CAPTURE };
				Cursor cursor = getActivity().getContentResolver().query(selectedImage,filePathColumn, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String picturePath = cursor.getString(columnIndex);
				cursor.close();
				picfile = new File(picturePath);
				new PicUploadAsync().execute();
			}  
			if (requestCode == SELECT_CAMERA_VIDEO_REQUEST && resultCode == Activity.RESULT_OK) { 
				Uri selectedVideo = data.getData();
				String[] filePathColumn = { MediaStore.ACTION_VIDEO_CAPTURE };
				Cursor cursor = getActivity().getContentResolver().query(selectedVideo,filePathColumn, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String videoPath = cursor.getString(columnIndex);
				cursor.close();
				videoFile = new File(videoPath);
				new VideoUploadAsync().execute();
			}  */
		}
	}



	private class PicUploadAsync extends AsyncTask<Void, Void, Void>{

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
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			MyHttpClientImageUpload.sendHttpPostImage(Util.API+"photo", picfile);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
		}

	}

	private class VideoUploadAsync extends AsyncTask<Void, Void, Void>{

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
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			MyHttpClientImageUpload.sendHttpPostImage(Util.API+"video", videoFile);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
		}

	}





	/**
	 * helper to retrieve the path of an image URI
	 *//*
	@SuppressWarnings("deprecation")
	public String getPath(Uri uri) {
		// just some safety built in 
		if( uri == null ) {
			// TODO perform some logging or show user feedback
			return null;
		}
		// try to retrieve the image from the media store first
		// this will only work for images selected from gallery
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
		if( cursor != null ){
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		}
		return uri.getPath();
	}

	@SuppressWarnings("deprecation")
	public String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = getActivity().managedQuery(contentUri, proj, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}*/
}
