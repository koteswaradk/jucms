package com.nxgenminds.eduminds.ju.cms.jgigroup;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.adapters.UserGalleryVideosAdapter;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.models.UserVideoGalleryModel;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class GroupFragmentTabVideos extends Fragment{


	private GridView videosGridView;
	private ProgressDialog pDialog;
	private UserGalleryVideosAdapter adapter;
	private static int pageCount = 0;
	private static String pagination_Date_String;
	private boolean flag_loading = false;
	private TextView noVideos;
	private String GroupGalleryVideosURL = Util.API + "video?org_video=1";
	private ArrayList<UserVideoGalleryModel> galleryVideoSearch = new ArrayList<UserVideoGalleryModel>();
	AlertDialogManager alert = new AlertDialogManager();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Get the view from fragmenttab2.xml
		View view = inflater.inflate(R.layout.user_fragmenttab_gallery_video, container, false);
		videosGridView = (GridView) view.findViewById(R.id.userGalleryGridViewVideo);
		noVideos = (TextView) view.findViewById(R.id.noVideos);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		pageCount = 0;
		galleryVideoSearch.clear();
		ConnectionDetector conn = new ConnectionDetector(getActivity());
		if(conn.isConnectingToInternet()){
			new GroupGalleryVideoAsync().execute();
		}else{
			alert.showAlertDialog(getActivity(), "Connection Error", "Please check your internet connection", false);
		}


		videosGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				Object object = videosGridView.getItemAtPosition(position);
				UserVideoGalleryModel connection_data = (UserVideoGalleryModel)  object;
				//
				String extension = MimeTypeMap.getFileExtensionFromUrl(connection_data.getVideo_path());
				//String extension = MimeTypeMap.getFileExtensionFromUrl("http://192.168.1.9/youmobile/public/uploads/videos/diJMqUft.mp4");
				String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
				Intent mediaIntent = new Intent(Intent.ACTION_VIEW);
				mediaIntent.setDataAndType(Uri.parse(connection_data.getVideo_path()), mimeType);
				//mediaIntent.setDataAndType(Uri.parse("http://192.168.1.9/youmobile/public/uploads/videos/diJMqUft.mp4"), mimeType);
				startActivity(mediaIntent);
			}
		});

		videosGridView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
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
							new GroupGalleryVideoLoadMoreAsync().execute();
						}else{
							alert.showAlertDialog(getActivity(), "Connection Error", "Please check your internet connection", false);
						}

					}
				}
			}
		});

	}

	private class GroupGalleryVideoAsync extends AsyncTask<Void, Void, ArrayList<UserVideoGalleryModel>>{

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
		protected ArrayList<UserVideoGalleryModel> doInBackground(
				Void... params) {
			// TODO Auto-generated method stub
			JSONArray userGalleryPhotosResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(GroupGalleryVideosURL);

			if(jsonObjectRecived != null){

				try{
					userGalleryPhotosResponse = jsonObjectRecived.getJSONArray("videos");
				}catch(JSONException e){
					e.printStackTrace();
				}
				for(int i = 0; i< userGalleryPhotosResponse.length();i++){
					UserVideoGalleryModel videoData = new UserVideoGalleryModel();
					JSONObject videoDetails;
					try{
						videoDetails = userGalleryPhotosResponse.getJSONObject(i);
						videoData.setVideo_owner_id(videoDetails.getString("video_id"));
						videoData.setVideo_title(videoDetails.getString("video_title"));
						videoData.setVideo_desc(videoDetails.getString("video_desc"));
						videoData.setVideo_path(videoDetails.getString("video_path"));
						videoData.setVideo_length(videoDetails.getString("video_length"));
						videoData.setVideo_format(videoDetails.getString("video_format"));
						videoData.setCreated_date(videoDetails.getString("created_date"));
						pagination_Date_String = videoDetails.getString("created_date");
						videoData.setCity_name(videoDetails.getString("city_name"));
						videoData.setVideo_thumb_img_path_1(videoDetails.getString("video_thumb_img_path_1"));
						galleryVideoSearch.add(videoData);
					}catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return galleryVideoSearch;
			}else{
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<UserVideoGalleryModel> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if(result == null){
				noVideos.setVisibility(View.VISIBLE);
				Toast.makeText(getActivity(), "No Videos Fund", Toast.LENGTH_SHORT).show();
			}else if(result.size()==0){
				noVideos.setVisibility(View.VISIBLE);
			}  else{
				noVideos.setVisibility(View.GONE);
				adapter = new UserGalleryVideosAdapter(getActivity(), result);
				videosGridView.setAdapter(adapter);

				if(result.size() < 20){
					flag_loading = true;
				}else{
					flag_loading = false;
				}
			}

		}

	}

	private class GroupGalleryVideoLoadMoreAsync extends AsyncTask<Void, Void, ArrayList<UserVideoGalleryModel>>{

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
		protected ArrayList<UserVideoGalleryModel> doInBackground(
				Void... params) {
			// TODO Auto-generated method stub
			JSONArray userGalleryPhotosResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(GroupGalleryVideosURL + "&last_date=" + pagination_Date_String.replaceAll(" ", "%20"));

			if(jsonObjectRecived != null){

				try{
					userGalleryPhotosResponse = jsonObjectRecived.getJSONArray("videos");
				}catch(JSONException e){
					e.printStackTrace();
				}
				if(userGalleryPhotosResponse.length() > 0){
					for(int i = 0; i < userGalleryPhotosResponse.length();i++){
						UserVideoGalleryModel videoData = new UserVideoGalleryModel();
						JSONObject videoDetails;
						try{
							videoDetails = userGalleryPhotosResponse.getJSONObject(i);
							videoData.setVideo_owner_id(videoDetails.getString("video_id"));
							videoData.setVideo_title(videoDetails.getString("video_title"));
							videoData.setVideo_desc(videoDetails.getString("video_desc"));
							videoData.setVideo_path(videoDetails.getString("video_path"));
							videoData.setVideo_length(videoDetails.getString("video_length"));
							videoData.setVideo_format(videoDetails.getString("video_format"));
							pagination_Date_String = videoDetails.getString("created_date");
							videoData.setCreated_date(videoDetails.getString("created_date"));
							videoData.setCity_name(videoDetails.getString("city_name"));
							videoData.setVideo_thumb_img_path_1(videoDetails.getString("video_thumb_img_path_1"));
							galleryVideoSearch.add(videoData);
						}catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					return galleryVideoSearch;
				}else{
					return null;
				}

			}else{
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<UserVideoGalleryModel> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if(result == null){
				flag_loading = true;
				Toast.makeText(getActivity(), "No More Videos to Load", Toast.LENGTH_SHORT).show();
			}else if(result.size()==0){
				flag_loading = true;
				Toast.makeText(getActivity(), "No More Videos to Load", Toast.LENGTH_SHORT).show();
				
			}        else{
				adapter = new UserGalleryVideosAdapter(getActivity(), result);
				adapter.notifyDataSetChanged();
				flag_loading = false;
				pageCount = pageCount +20;
				videosGridView.setSelection(pageCount);
			}

		}

	}

}