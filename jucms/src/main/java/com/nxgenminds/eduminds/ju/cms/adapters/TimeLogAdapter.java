package com.nxgenminds.eduminds.ju.cms.adapters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.ViewProfilePhoto;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.models.Posts;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpPostClient;
import com.nxgenminds.eduminds.ju.cms.thirdpartyprofile.ThirdPartyTabMenuActivity;
import com.nxgenminds.eduminds.ju.cms.timefeed.LikeDetailActivity;
import com.nxgenminds.eduminds.ju.cms.userprofile.UserTabMenuActivity;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class TimeLogAdapter extends BaseAdapter{

	private ArrayList<Posts> listData;
	private LayoutInflater layoutInflater;
	private ImageLoader imageLoader;
	private DisplayImageOptions options,flatoptions;
	private Context prova;
	private int lastPosition = -1;
	AlertDialogManager alert = new AlertDialogManager();
	private ProgressDialog mPDialog;

	public TimeLogAdapter(Context context,ArrayList<Posts> listData){
		this.listData = listData;
		imageLoader = ImageLoader.getInstance();
		prova = context;

		options = new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.showImageOnLoading(R.drawable.placeholder)
		.showImageForEmptyUri(R.drawable.placeholder) 
		.showImageOnFail(R.drawable.placeholder)
		.build();

		flatoptions = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.placeholder_timefeed)
		.showImageForEmptyUri(R.drawable.placeholder_timefeed) 
		.showImageOnFail(R.drawable.placeholder_timefeed)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();

	}



	@Override
	public int getCount() {
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getViewTypeCount() {
		return super.getViewTypeCount();
	}

	@Override
	public int getItemViewType(int position) {
		return super.getItemViewType(position);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		final Posts feedItem = (Posts) listData.get(position);
		if (layoutInflater == null)
			layoutInflater = (LayoutInflater) prova.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(convertView == null){
			convertView = layoutInflater.inflate(R.layout.custom_post_text_and_image, null);
			holder = new ViewHolder();
			holder.postUserName = (TextView) convertView.findViewById(R.id.postUserName);
			holder.postUserText = (TextView) convertView.findViewById(R.id.postText);
			holder.postLikes = (TextView) convertView.findViewById(R.id.postTotalLikes);
			//	holder.postedDate = (TextView) convertView.findViewById(R.id.postedDate);
			holder.PostlikeButton = (ImageButton) convertView.findViewById(R.id.postlikeButton);
			holder.postUserImage = (ImageView) convertView.findViewById(R.id.postOwnerImage);
			holder.postImage = (ImageView) convertView.findViewById(R.id.postImage);
			holder.postImageOnTop = (ImageView) convertView.findViewById(R.id.postImageOnTop);
			holder.postDateUnderName = (TextView) convertView.findViewById(R.id.postdateundername);
			holder.postOptionsButton = (ImageView) convertView.findViewById(R.id.postOptionsButton);
			convertView.setTag(holder);	
		}else{
			holder = (ViewHolder) convertView.getTag();
		}

		//Adding Animations

		//Animation animation = AnimationUtils.loadAnimation(prova, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
		/*Animation animation = AnimationUtils.loadAnimation(prova, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.test_from_top);
		convertView.startAnimation(animation);*/
		lastPosition = position;

		// Adding Animations

		if(feedItem.getUser_liked().equalsIgnoreCase("1")){
			holder.PostlikeButton.setBackgroundResource(R.drawable.like_blue);
		}else{
			holder.PostlikeButton.setBackgroundResource(R.drawable.like_white);
		}

		if(feedItem.getPost_owner_id().equalsIgnoreCase(Util.USER_ID) || Util.ROLE.equalsIgnoreCase("admin")){
			holder.postOptionsButton.setVisibility(View.VISIBLE);
		}else{
			holder.postOptionsButton.setVisibility(View.GONE);
		}

		// Text
		if(feedItem.getPost_type().equalsIgnoreCase("1")){
			holder.postUserText.setVisibility(View.VISIBLE);
			String timestr = feedItem.getCreated_date();
			String[] datestr = timestr.split(" "); 

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			try {

				Date date = formatter.parse(datestr[0]);
				
				SimpleDateFormat formater = new SimpleDateFormat("MMM dd,yyyy");
				
				holder.postDateUnderName.setText(formater.format(date));

			} catch (ParseException e) {
				e.printStackTrace();
			}

			if(feedItem.getLastname().equalsIgnoreCase("null")){
				holder.postUserName.setText(feedItem.getFirstname());
			}else{
				holder.postUserName.setText(feedItem.getFirstname() + " " +feedItem.getLastname());
			}
			holder.postUserText.setText(feedItem.getStatus_update_text());

			if(feedItem.getPost_like_count().equalsIgnoreCase("1")){
				holder.postLikes.setText(feedItem.getPost_like_count() + " Like");
			}
			else if(feedItem.getPost_like_count().equalsIgnoreCase("0")){
				holder.postLikes.setText(feedItem.getPost_like_count() + " Like");
			}
			else{
				holder.postLikes.setText(feedItem.getPost_like_count() + " Likes");
			}
			//holder.postedDate.setText(feedItem.getCreated_date());
			holder.postImage.setVisibility(View.GONE);
			holder.postImageOnTop.setVisibility(View.GONE);
			imageLoader.displayImage(feedItem.getUser_profile_photo(), holder.postUserImage, options, new SimpleImageLoadingListener() {
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
		}

		// Text with Image

		if(feedItem.getPost_type().equalsIgnoreCase("2")){

			String timestr = feedItem.getCreated_date();
			String[] datestr = timestr.split(" "); 

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			try {

				Date date = formatter.parse(datestr[0]);
				
				SimpleDateFormat formater = new SimpleDateFormat("MMM dd,yyyy");
				
				holder.postDateUnderName.setText(formater.format(date));

			} catch (ParseException e) {
				e.printStackTrace();
			}

			if(feedItem.getLastname().equalsIgnoreCase("null")){
				holder.postUserName.setText(feedItem.getFirstname());
			}else{
				holder.postUserName.setText(feedItem.getFirstname() + " " +feedItem.getLastname());
			}		
			holder.postUserText.setText(feedItem.getStatus_update_text());

			if(feedItem.getPost_like_count().equalsIgnoreCase("1")){
				holder.postLikes.setText(feedItem.getPost_like_count() + " Like");
			}else{
				holder.postLikes.setText(feedItem.getPost_like_count() + " Likes");
			}
			//	holder.postedDate.setText(feedItem.getCreated_date());
			holder.postUserText.setVisibility(View.VISIBLE);
			holder.postImage.setVisibility(View.VISIBLE);
			holder.postImageOnTop.setVisibility(View.GONE);
			imageLoader.displayImage(feedItem.getUser_profile_photo(), holder.postUserImage, options, new SimpleImageLoadingListener() {
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
			imageLoader.displayImage(feedItem.getPost_photo_path(), holder.postImage, flatoptions, new SimpleImageLoadingListener() {
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
		}

		// text and video

		if(feedItem.getPost_type().equalsIgnoreCase("3")){

			String timestr = feedItem.getCreated_date();
			String[] datestr = timestr.split(" "); 

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			try {

				Date date = formatter.parse(datestr[0]);
				
				SimpleDateFormat formater = new SimpleDateFormat("MMM dd,yyyy");
				
				holder.postDateUnderName.setText(formater.format(date));

			} catch (ParseException e) {
				e.printStackTrace();
			}

			if(feedItem.getLastname().equalsIgnoreCase("null")){
				holder.postUserName.setText(feedItem.getFirstname());
			}else{
				holder.postUserName.setText(feedItem.getFirstname() + " " +feedItem.getLastname());
			}		
			holder.postUserText.setText(feedItem.getStatus_update_text());
			if(feedItem.getPost_like_count().equalsIgnoreCase("1")){
				holder.postLikes.setText(feedItem.getPost_like_count() + " Like");
			}else{
				holder.postLikes.setText(feedItem.getPost_like_count() + " Likes");
			}
			//holder.postedDate.setText(feedItem.getCreated_date());
			//			holder.postUserImage.setText(feedItem.getPost_id());
			holder.postUserText.setVisibility(View.VISIBLE);
			holder.postImage.setVisibility(View.VISIBLE);
			holder.postImageOnTop.setVisibility(View.VISIBLE);
			imageLoader.displayImage(feedItem.getUser_profile_photo(), holder.postUserImage, options, new SimpleImageLoadingListener() {
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
			imageLoader.displayImage(feedItem.getVideo_thumb_img_path_3(), holder.postImage, flatoptions, new SimpleImageLoadingListener() {
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
		}

		//Image
		if(feedItem.getPost_type().equalsIgnoreCase("4")){

			String timestr = feedItem.getCreated_date();
			String[] datestr = timestr.split(" "); 

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			try {

				Date date = formatter.parse(datestr[0]);
				SimpleDateFormat formater = new SimpleDateFormat("MMM dd,yyyy");
				holder.postDateUnderName.setText(formater.format(date));

			} catch (ParseException e) {
				e.printStackTrace();
			}

			if(feedItem.getLastname().equalsIgnoreCase("null")){
				holder.postUserName.setText(feedItem.getFirstname());
			}else{
				holder.postUserName.setText(feedItem.getFirstname() + " " +feedItem.getLastname());
			}	

			if(feedItem.getPost_like_count().equalsIgnoreCase("1")){
				holder.postLikes.setText(feedItem.getPost_like_count() + " Like");
			}else{
				holder.postLikes.setText(feedItem.getPost_like_count() + " Likes");
			}
			//holder.postedDate.setText(feedItem.getCreated_date());
			holder.postUserText.setVisibility(View.GONE);
			holder.postImage.setVisibility(View.VISIBLE);
			holder.postImageOnTop.setVisibility(View.GONE);
			imageLoader.displayImage(feedItem.getUser_profile_photo(), holder.postUserImage, options, new SimpleImageLoadingListener() {
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
			imageLoader.displayImage(feedItem.getPost_photo_path(), holder.postImage, flatoptions, new SimpleImageLoadingListener() {
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
		}

		//Video

		if(feedItem.getPost_type().equalsIgnoreCase("5")){

			String timestr = feedItem.getCreated_date();
			String[] datestr = timestr.split(" "); 

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			try {

				Date date = formatter.parse(datestr[0]);
				
				SimpleDateFormat formater = new SimpleDateFormat("MMM dd,yyyy");
				
				holder.postDateUnderName.setText(formater.format(date));

			} catch (ParseException e) {
				e.printStackTrace();
			}

			if(feedItem.getLastname().equalsIgnoreCase("null")){
				holder.postUserName.setText(feedItem.getFirstname());
			}else{
				holder.postUserName.setText(feedItem.getFirstname() + " " +feedItem.getLastname());
			}
			if(feedItem.getPost_like_count().equalsIgnoreCase("1")){
				holder.postLikes.setText(feedItem.getPost_like_count() + " Like");
			}
			else if(feedItem.getPost_like_count().equalsIgnoreCase("0")){
				holder.postLikes.setText(feedItem.getPost_like_count() + " Like");
			}
			else{
				holder.postLikes.setText(feedItem.getPost_like_count() + " Likes");
			}
			//holder.postedDate.setText(feedItem.getCreated_date());
			holder.postUserText.setVisibility(View.GONE);
			//			holder.postUserImage.setText(feedItem.getPost_id());
			holder.postImage.setVisibility(View.VISIBLE);
			holder.postImageOnTop.setVisibility(View.VISIBLE);
			imageLoader.displayImage(feedItem.getUser_profile_photo(), holder.postUserImage, options, new SimpleImageLoadingListener() {
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

			imageLoader.displayImage(feedItem.getVideo_thumb_img_path_3(), holder.postImage, flatoptions, new SimpleImageLoadingListener() {
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
		}

		holder.PostlikeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(feedItem.getUser_liked().equalsIgnoreCase("1")){
				}else{
					/*Animation animFadeOut = AnimationUtils.loadAnimation(prova, R.anim.fade_out);
					holder.PostlikeButton.startAnimation(animFadeOut);*/

					//krunal Animation for Like
					Animation animFadeOut = AnimationUtils.loadAnimation(prova, R.anim.fade_out);
					animFadeOut.setAnimationListener(new Animation.AnimationListener(){
						@Override
						public void onAnimationStart(Animation arg0) {
						}           
						@Override
						public void onAnimationRepeat(Animation arg0) {
						}           
						@Override
						public void onAnimationEnd(Animation arg0) {
							Animation animZoomOut = AnimationUtils.loadAnimation(prova, R.anim.zoom_out);
							holder.PostlikeButton.startAnimation(animZoomOut);
						}
					});
					holder.PostlikeButton.startAnimation(animFadeOut);
					
					
					ConnectionDetector conn = new ConnectionDetector(prova);
		            if(conn.isConnectingToInternet()){
		            	new LikePostAsync().execute(feedItem.getPost_id());
		            } else{
		            	alert.showAlertDialog(prova,"Connection Error","Check your Internet Connection",false);
		            }

					

					String like_count = null;
					if(holder.postLikes.getText().toString().contains(" ")){
						like_count= holder.postLikes.getText().toString().substring(0, holder.postLikes.getText().toString().indexOf(" ")); 
					}

					int temp_count=Integer.parseInt(like_count)+1;
					feedItem.setPost_like_count(String.valueOf(temp_count));
					feedItem.setUser_liked("1");
					//	holder.postLikes.setText(String.valueOf(temp_count));

					// Incresing the Like Count
					if(feedItem.getPost_like_count().equalsIgnoreCase("1")){
						holder.postLikes.setText(String.valueOf(temp_count)+ " Like");
						holder.PostlikeButton.setBackgroundResource(R.drawable.like_blue);
					}else{
						holder.postLikes.setText(String.valueOf(temp_count)+ " Likes");
						holder.PostlikeButton.setBackgroundResource(R.drawable.like_blue);
					}

				}

			}
		});

		/*		holder.postUserImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(feedItem.getPost_owner_id().equalsIgnoreCase(Util.USER_ID)){
					Intent intent = new Intent(prova,UserTabMenuActivity.class);
					prova.startActivity(intent);
				}else{
					Intent intent = new Intent(prova,ThirdPartyTabMenuActivity.class);
					intent.putExtra("UserID", feedItem.getPost_owner_id());
					Util.THIRD_PARTY_NAME  = feedItem.getFirstname();
					prova.startActivity(intent);
				}

			}
		});*/

		holder.postImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(feedItem.getPost_type().equalsIgnoreCase("5") || feedItem.getPost_type().equalsIgnoreCase("3")){
					String extension = MimeTypeMap.getFileExtensionFromUrl(feedItem.getPost_video_path());
					//String extension = MimeTypeMap.getFileExtensionFromUrl("http://192.168.1.9/youmobile/public/uploads/videos/diJMqUft.mp4");
					String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
					Intent mediaIntent = new Intent(Intent.ACTION_VIEW);
					mediaIntent.setDataAndType(Uri.parse(feedItem.getPost_video_path()), mimeType);
					//mediaIntent.setDataAndType(Uri.parse("http://192.168.1.9/youmobile/public/uploads/videos/diJMqUft.mp4"), mimeType);
					prova.startActivity(mediaIntent);
				}else if(feedItem.getPost_type().equalsIgnoreCase("2") || feedItem.getPost_type().equalsIgnoreCase("4")){
					Intent intent = new Intent(prova, ViewProfilePhoto.class);
					intent.putExtra("ImagePath", feedItem.getPost_photo_path() );
					prova.startActivity(intent); 
				}else{
					// Do Nothing
				}
			}
		});

		holder.postUserImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if(feedItem.getPost_owner_id().equalsIgnoreCase(Util.USER_ID)){
					Intent intent = new Intent(prova,UserTabMenuActivity.class);
					Util.intership_flag=false;
					prova.startActivity(intent);
				}else{
					Intent intent = new Intent(prova,ThirdPartyTabMenuActivity.class);
					intent.putExtra("UserID", feedItem.getPost_owner_id());
					intent.putExtra("ThirdPartyRole", feedItem.getRole());
					Util.THIRD_PARTY_NAME  = feedItem.getFirstname();
					Util.THIRD_PARTY_ID=feedItem.getPost_owner_id();
					Util.intership_flag=true;
					prova.startActivity(intent);

				}
			}
		});

		holder.postLikes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent postDetail= new Intent(prova,LikeDetailActivity.class);
				postDetail.putExtra("post_id", feedItem.getPost_id());
				prova.startActivity(postDetail);
			}
		});

		holder.postOptionsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder confirmAlert = new AlertDialog.Builder(prova);
				confirmAlert.setTitle("Delete Post");
				confirmAlert.setMessage("Are you sure you want to delete this post?");
				confirmAlert.setCancelable(true);
				confirmAlert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						
						ConnectionDetector conn = new ConnectionDetector(prova);
			            if(conn.isConnectingToInternet()){
			            	new DeletePostAsync().execute(feedItem.getPost_id());
			            } else{
			            	alert.showAlertDialog(prova,"Connection Error","Check your Internet Connection",false);
			            }
											
						listData.remove(position);
						notifyDataSetChanged();
					}
				});

				confirmAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				confirmAlert.create().show();


			}
		});

		return convertView;

	}

	static class ViewHolder{
		ImageView postUserImage;
		TextView postUserName;
		TextView postUserText;
		TextView postDateUnderName;
		TextView postLikes;
		//TextView postedDate;
		ImageButton PostlikeButton;
		ImageView postImage;
		ImageView postImageOnTop;
		ImageView postOptionsButton;
	}

	private class LikePostAsync extends AsyncTask<String, Void, JSONObject>{
		@Override
		protected void onPreExecute(){
			if(mPDialog == null){
				mPDialog = Util.createProgressDialog(prova);
				mPDialog.show();
			} else {
				mPDialog.show();
			}
		}
		@Override
		protected JSONObject doInBackground(String... params) {
			// TODO Auto-generated method stub
			// JSON object to hold the information, which is sent to the server
			JSONObject jsonObjSend = new JSONObject();
			String PostId = params[0];

			try {
				jsonObjSend.put("post_id",PostId);
			} 
			catch (JSONException e) {
				e.printStackTrace();
			}

			JSONObject jsonObjRecv = HttpPostClient.sendHttpPost(Util.API+"like", jsonObjSend);
			Log.i("jsonObjRecv",jsonObjRecv.toString());
			return jsonObjRecv;
		}
		@Override
		protected void onPostExecute(JSONObject result){
			super.onPostExecute(result);
			mPDialog.dismiss();
			try {
				if(result.getString("status").equalsIgnoreCase("1"))
				{
					if(result.getString("message").equalsIgnoreCase("Like inserted successfully"))
					{
						//listData.get(pos).setUser_liked(result.getString("1"));
						//listData.get(pos).setPost_like_count((Integer.parseInt(listData.get(pos).getPost_like_count())+1).);

					}
					else
					{
						//listData.get(pos).setUser_liked("0");

					}
				}
				else
				{
					// dont do any thing
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

	}


	private class DeletePostAsync extends AsyncTask<String, Void, Integer>{
		@Override
		protected void onPreExecute(){
			if(mPDialog == null){
				mPDialog = Util.createProgressDialog(prova);
				mPDialog.show();
			} else {
				mPDialog.show();
			}
		}
		@Override
		protected Integer doInBackground(String... params) {
			JSONObject jsonObjSend = new JSONObject();
			String PostId = params[0];

			try {
				jsonObjSend.put("post_id",PostId);
				JSONObject jsonObjRecv = HttpPostClient.sendHttpPost(Util.API+"delete_post",jsonObjSend);
				
			} 
			catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Integer result){
			super.onPostExecute(result);
			mPDialog.dismiss();
		}

	}
}
