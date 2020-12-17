package com.nxgenminds.eduminds.ju.cms.statusUpdate;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpPostClient;
import com.nxgenminds.eduminds.ju.cms.proxy.MyHttpClientImageUpload;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.InternalStorageContentProvider;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class UserPostCreateAcitivty extends ActionBarActivity implements OnClickListener {

	private TextView post_text;
	private EditText postMessage;
	private ImageView image_post,image_postClose;
	private ProgressDialog pDialog;
	private File picfile, videoFile;
	private static final int SELECT_PICTURE = 1;
	private static final int SELECT_VIDEO = 2;
	private static final int SELECT_CAMERA_IMAGE_REQUEST = 3;
	private static final int SELECT_CAMERA_VIDEO_REQUEST = 4;
	private static int postFlagFileCheck = 0;
	private static int postTypeFlag = 0;
	private static String photoID;
	private static String videoID;
	private File mFileTemp, mVideoFileTemp;
	private Bitmap bitmap_postImage, scaledBitmap;
	private ThumbnailUtils video_thumbnail;
	public Menu menuInstance;
	private String selectedImagePath, selectedVideoPath, selectedCameraPicPath,
	selectedCameraVideoPath;
	public ImageLoader imageLoader;
	private String checkActivity;
	DisplayImageOptions profile_options;
	
	private AlertDialogManager alert = new AlertDialogManager();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_create_post);
		Bundle bundle = getIntent().getExtras();
		checkActivity = bundle.getString("Group");

		ActionBar actionBar = getSupportActionBar();
		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setTitle("Post Something");
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayUseLogoEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#292929")));
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setCustomView(R.layout.custom_post);
		post_text=(TextView)findViewById(R.id.post_a);

		postFlagFileCheck = 0;
		postTypeFlag = 0;

		postMessage = (EditText) findViewById(R.id.PostMessage);
		image_post = (ImageView) findViewById(R.id.post_image);
		image_postClose = (ImageView)findViewById(R.id.post_imageclose);
		image_post.setVisibility(View.INVISIBLE);
		image_postClose.setVisibility(View.INVISIBLE);

		imageLoader =imageLoader.getInstance();
		profile_options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.image_replacement)
		.showImageForEmptyUri(R.drawable.image_replacement) 
		.showImageOnFail(R.drawable.image_replacement)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();


		image_postClose.setOnClickListener(this);

		//post_text.setOnClickListener(this);
		post_text.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (postFlagFileCheck == 0) {
					if (postMessage.getText().toString().trim().length() > 0) {
						// async code 1
						postTypeFlag = 1;
						ConnectionDetector conn = new ConnectionDetector(UserPostCreateAcitivty.this);
			            if(conn.isConnectingToInternet()){
			            	new SendPostText().execute();
			            } else{
			            	alert.showAlertDialog(UserPostCreateAcitivty.this,"Connection Error","Check your Internet Connection",false);
			            }
						
					} else {
						Toast.makeText(getApplicationContext(),
								"Oops you forgot to add a post!", Toast.LENGTH_SHORT)
								.show();
					}
				}
				if (postFlagFileCheck == 1) {

					if (postMessage.getText().toString().trim().length() > 0) {
						postTypeFlag = 2;
						ConnectionDetector conn = new ConnectionDetector(UserPostCreateAcitivty.this);
			            if(conn.isConnectingToInternet()){
			            	new PostUploadFile().execute("2");
			            } else{
			            	alert.showAlertDialog(UserPostCreateAcitivty.this,"Connection Error","Check your Internet Connection",false);
			            }
						
					} else {
						// async code 4
						postTypeFlag = 4;
						ConnectionDetector conn = new ConnectionDetector(UserPostCreateAcitivty.this);
			            if(conn.isConnectingToInternet()){
			            	new PostUploadFile().execute("4");
			            } else{
			            	alert.showAlertDialog(UserPostCreateAcitivty.this,"Connection Error","Check your Internet Connection",false);
			            }
						
					}
				}

				if (postFlagFileCheck == 2) {

					if (postMessage.getText().toString().trim().length() > 0) {
						postTypeFlag = 3;
						
						ConnectionDetector conn = new ConnectionDetector(UserPostCreateAcitivty.this);
			            if(conn.isConnectingToInternet()){
			            	new PostUploadFile().execute("3");
			            } else{
			            	alert.showAlertDialog(UserPostCreateAcitivty.this,"Connection Error","Check your Internet Connection",false);
			            }
						
					} else {
						postTypeFlag = 5;
						ConnectionDetector conn = new ConnectionDetector(UserPostCreateAcitivty.this);
			            if(conn.isConnectingToInternet()){
			            	new PostUploadFile().execute("5");
			            } else{
			            	alert.showAlertDialog(UserPostCreateAcitivty.this,"Connection Error","Check your Internet Connection",false);
			            }
					}
				}

			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu (Menu menu) {
		menuInstance = menu;
		getMenuInflater().inflate(R.menu.menu_status_update, menu);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		super.onOptionsItemSelected(item);

		switch(item.getItemId()){
		case R.id.phone:

			// TODO Auto-generated method stub
			Intent intent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(
					Intent.createChooser(intent, "Select Video"),
					SELECT_VIDEO);

			break;

		case R.id.computer:

			// TODO Auto-generated method stub

			intent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(
					Intent.createChooser(intent, "Select Picture"),
					SELECT_PICTURE);

			break;     

		case R.id.camera:

			// TODO Auto-generated method stub

			String state = Environment.getExternalStorageState();
			if (Environment.MEDIA_MOUNTED.equals(state)) {
				mFileTemp = new File(Environment
						.getExternalStorageDirectory(),
						Util.TEMP_PHOTO_FILE_NAME);
			} else {
				mFileTemp = new File(getApplicationContext().getFilesDir(),
						Util.TEMP_PHOTO_FILE_NAME);
			}
			intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

			try {
				Uri mImageCaptureUri = null;
				// String state = Environment.getExternalStorageState();
				if (mFileTemp == null) {
					
				} else {
					

				}
				if (Environment.MEDIA_MOUNTED.equals(state)) {
					mImageCaptureUri = Uri.fromFile(mFileTemp);
				} else {
					mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
				}
				intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
						mImageCaptureUri);
				intent.putExtra("return-data", true);
				startActivityForResult(intent, SELECT_CAMERA_IMAGE_REQUEST);
				/*
				 * Intent cameraIntent = new
				 * Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				 * startActivityForResult(cameraIntent,
				 * SELECT_CAMERA_IMAGE_REQUEST);
				 */
			} catch (ActivityNotFoundException e) {

				
			}

			break;

		case R.id.video:

			// TODO Auto-generated method stub

			state = Environment.getExternalStorageState();
			if (Environment.MEDIA_MOUNTED.equals(state)) {
				mVideoFileTemp = new File(Environment
						.getExternalStorageDirectory(),
						Util.TEMP_VIDEO_FILE_NAME);
			} else {
				mVideoFileTemp = new File(getApplicationContext()
						.getFilesDir(), Util.TEMP_VIDEO_FILE_NAME);
			}
			intent = new Intent(
					android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
			// Intent cameraIntent = new
			// Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
			try {
				Uri mImageCaptureUri = null;
				// String state = Environment.getExternalStorageState();
				if (mVideoFileTemp == null) {
				} else {
					
				if (Environment.MEDIA_MOUNTED.equals(state)) {
					mImageCaptureUri = Uri.fromFile(mVideoFileTemp);
				} else {
					mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
				}
				intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
						mImageCaptureUri);
				intent.putExtra("return-data", true);
				startActivityForResult(intent, SELECT_CAMERA_VIDEO_REQUEST);
			} 
			}catch (ActivityNotFoundException e) {

				
			}
			/*
			 * Intent cameraIntent = new
			 * Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
			 * startActivityForResult(cameraIntent,
			 * SELECT_CAMERA_VIDEO_REQUEST);
			 */


			break;



		}
		return true;		

	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == SELECT_PICTURE
					&& resultCode == Activity.RESULT_OK && null != data) {

				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };
				Cursor cursor = getApplicationContext().getContentResolver()
						.query(selectedImage, filePathColumn, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String picturePath = cursor.getString(columnIndex);
				cursor.close();
				picfile = new File(picturePath);
				bitmap_postImage = BitmapFactory.decodeFile(picturePath);
				setImagePreview(bitmap_postImage);
				postFlagFileCheck = 1;
			}

			if (requestCode == SELECT_VIDEO && resultCode == Activity.RESULT_OK
					&& null != data) {

				Uri selectedVideo = data.getData();
				String[] filePathColumn = { MediaStore.Video.Media.DATA };
				Cursor cursor = getApplicationContext().getContentResolver()
						.query(selectedVideo, filePathColumn, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String videoPath = cursor.getString(columnIndex);
				cursor.close();
				videoFile = new File(videoPath);
				bitmap_postImage = ThumbnailUtils.createVideoThumbnail(
						videoPath, Thumbnails.MICRO_KIND);
				setImagePreview(bitmap_postImage);
				postFlagFileCheck = 2;
			}
			if (requestCode == SELECT_CAMERA_IMAGE_REQUEST
					&& resultCode == Activity.RESULT_OK) {
				picfile = mFileTemp;
				postFlagFileCheck = 1;
				bitmap_postImage = BitmapFactory.decodeFile(picfile
						.getAbsolutePath());
				setImagePreview(bitmap_postImage);
			}
			if (requestCode == SELECT_CAMERA_VIDEO_REQUEST
					&& resultCode == Activity.RESULT_OK) {

				videoFile = mVideoFileTemp;
				postFlagFileCheck = 2;
				bitmap_postImage = ThumbnailUtils.createVideoThumbnail(
						videoFile.getPath(), Thumbnails.MICRO_KIND);
				setImagePreview(bitmap_postImage);
			}
		}
	}

	private class PostUploadFile extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			// Showing progress dialog before sending http request
			pDialog = new ProgressDialog(UserPostCreateAcitivty.this);
			pDialog.setMessage("Posting Please Wait..");
			pDialog.setIndeterminate(true);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			String flag = params[0];

			if (flag.equalsIgnoreCase("2") || flag.equalsIgnoreCase("4")) {
				JSONObject jsonObj = MyHttpClientImageUpload.sendHttpPostImage(
						Util.API + "photo", picfile);
				
				if (jsonObj != null) {
					try {
						if (jsonObj.getString("error")
								.equalsIgnoreCase("false")) {
							photoID = jsonObj.getString("photo_id").toString();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			if (flag.equalsIgnoreCase("3") || flag.equalsIgnoreCase("5")) {
				JSONObject jsonObj = MyHttpClientImageUpload.sendHttpPostImage(
						Util.API + "video", videoFile);
				
				if (jsonObj != null) {
					try {
						if (jsonObj.getString("error")
								.equalsIgnoreCase("false")) {
							videoID = jsonObj.getString("video_id").toString();
						}
					} catch (JSONException e) {
		
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			ConnectionDetector conn = new ConnectionDetector(UserPostCreateAcitivty.this);
            if(conn.isConnectingToInternet()){
            	new SendPostFile().execute();
            } else{
            	alert.showAlertDialog(UserPostCreateAcitivty.this,"Connection Error","Check your Internet Connection",false);
            }
			
		}

	}

	private class SendPostFile extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONObject jsonObjSend = new JSONObject();

			if (postTypeFlag == 2) {
				// Text and Image
				try {
					jsonObjSend.put("post_type", "2");
					jsonObjSend.put("status_update_text", postMessage.getText().toString());
					if(checkActivity.equalsIgnoreCase("Group")){
						jsonObjSend.put("org_post", "1");
					}
					jsonObjSend.put("photo_id", photoID);
					JSONObject jsonObjRecv = HttpPostClient.sendHttpPost(
							Util.API + "post", jsonObjSend);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			if (postTypeFlag == 3) {
				// Text and Video

				try {
					jsonObjSend.put("post_type", "3");
					jsonObjSend.put("status_update_text", postMessage.getText().toString());
					if(checkActivity.equalsIgnoreCase("Group")){
						jsonObjSend.put("org_post", "1");
					}
					jsonObjSend.put("video_id", videoID);
					JSONObject jsonObjRecv = HttpPostClient.sendHttpPost(
							Util.API + "post", jsonObjSend);
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
			if (postTypeFlag == 4) {
				// Image
				try {
					jsonObjSend.put("post_type", "4");
					if(checkActivity.equalsIgnoreCase("Group")){
		                 jsonObjSend.put("org_post", "1");
					}
					jsonObjSend.put("photo_id", photoID);
					JSONObject jsonObjRecv = HttpPostClient.sendHttpPost(Util.API + "post", jsonObjSend);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			if (postTypeFlag == 5) {
				// Video
				try {
					jsonObjSend.put("post_type", "5");
					if(checkActivity.equalsIgnoreCase("Group")){
						jsonObjSend.put("org_post", "1");
					}
					jsonObjSend.put("video_id", videoID);
					JSONObject jsonObjRecv = HttpPostClient.sendHttpPost(
							Util.API + "post", jsonObjSend);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			finish();
		}

	}

	private class SendPostText extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			// Showing progress dialog before sending http request
			pDialog = new ProgressDialog(UserPostCreateAcitivty.this);
			pDialog.setMessage("Posting..");
			pDialog.setIndeterminate(true);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONObject jsonObjSend = new JSONObject();

			if (postTypeFlag == 1) {
				// Text and Image
				try {
					jsonObjSend.put("post_type", "1");
					if(checkActivity.equalsIgnoreCase("Group")){
						jsonObjSend.put("org_post", "1");
					}
					jsonObjSend.put("status_update_text", postMessage.getText()
							.toString());
					JSONObject jsonObjRecv = HttpPostClient.sendHttpPost(
							Util.API + "post", jsonObjSend);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			finish();
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		if(id == R.id.post_imageclose){
			image_post.setVisibility(View.INVISIBLE);
			image_postClose.setVisibility(View.INVISIBLE);
			postFlagFileCheck = 0;
			postFlagFileCheck = 0;
		}
	}

	public Bitmap setImagePreview(Bitmap bitmap_postImage)
	{
		scaledBitmap = Bitmap.createScaledBitmap(bitmap_postImage, 100,
				100, true);
		image_post.setVisibility(View.VISIBLE);
		image_post.setImageBitmap(scaledBitmap);
		image_postClose.setVisibility(View.VISIBLE);
		return scaledBitmap;
	}
	@SuppressWarnings("deprecation")
	@Override
	public void onBackPressed() {
		if (postMessage.getText().toString().trim().length() > 0)
		{

			AlertDialog alert_back = new AlertDialog.Builder(this).create();
			alert_back.setTitle("Exit?");
			alert_back.setMessage("Your post will be discarded");

			alert_back.setButton("Cancel", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});

			alert_back.setButton2("Yes", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					UserPostCreateAcitivty.this.finish();
				}
			});
			alert_back.show();

		}else
		{
			UserPostCreateAcitivty.this.finish();
		}


	}
}
