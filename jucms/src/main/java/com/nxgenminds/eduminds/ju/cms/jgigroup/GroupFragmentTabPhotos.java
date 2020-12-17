package com.nxgenminds.eduminds.ju.cms.jgigroup;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.nxgenminds.eduminds.ju.cms.ImagePagerActivity;
import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.adapters.UserGalleryPhotosAdapter;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.models.UserPhotosGalleryModel;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;
import com.nxgenminds.eduminds.ju.cms.utils.Util.Extra;


public class GroupFragmentTabPhotos extends Fragment{
	//org_post
	private GridView photoGridView;
	private TextView noPhotos;
	private String UserGalleryPhotosURL = Util.API+ "photo?org_photo=1";
	private ArrayList<UserPhotosGalleryModel> galleryPhotoSearch = new ArrayList<UserPhotosGalleryModel>();
	private ProgressDialog pDialog;
	private UserGalleryPhotosAdapter adapter;
	private static int pageCount = 0;
	private static String pagination_Date_String = "";
	private boolean flag_loading = false;
	AlertDialogManager alert = new AlertDialogManager();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Get the view from fragmenttab2.xml
		View view = inflater.inflate(R.layout.user_fragment_tab_gallery_photos, container, false);
		photoGridView = (GridView) view.findViewById(R.id.userGalleryGridViewPhoto);
		noPhotos = (TextView) view.findViewById(R.id.Nophotos);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		galleryPhotoSearch.clear();
		pagination_Date_String = "";
		ConnectionDetector conn = new ConnectionDetector(getActivity());
		if(conn.isConnectingToInternet()){
			new GroupGalleryPhotoAsync().execute();
		}else{
			alert.showAlertDialog(getActivity(), "Connection Error", "Please check your internet connection", false);
		}


		photoGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				startImagePagerActivity(position);
			}
		});

		photoGridView.setOnScrollListener(new OnScrollListener() {
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
							new GroupGalleryPhotoLoadMoreAsync().execute();
						}else{
							alert.showAlertDialog(getActivity(), "Connection Error", "Please check your internet connection", false);
						}

					}
				}
			}
		});


	}

	private void startImagePagerActivity(int position) {
		Intent intent = new Intent(getActivity(), ImagePagerActivity.class);

		//intent.putExtra(Extra.IMAGES, imageUrls);
		intent.putExtra(Extra.IMAGE_POSITION, position);
		intent.putExtra("Activity", "Group");
		
		startActivity(intent); 
	}

	private class GroupGalleryPhotoAsync extends AsyncTask<Void, Void, ArrayList<UserPhotosGalleryModel>>{

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
		protected ArrayList<UserPhotosGalleryModel> doInBackground(
				Void... params) {
			// TODO Auto-generated method stub
			JSONArray userGalleryPhotosResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(UserGalleryPhotosURL);

			if(jsonObjectRecived != null){

				try{
					userGalleryPhotosResponse = jsonObjectRecived.getJSONArray("photos");
				}catch(JSONException e){
					e.printStackTrace();
				}
				for(int i = 0; i< userGalleryPhotosResponse.length();i++){
					UserPhotosGalleryModel photosData = new UserPhotosGalleryModel();
					JSONObject photoDetails;
					try{
						photoDetails = userGalleryPhotosResponse.getJSONObject(i);
						photosData.setPhoto_owner_id(photoDetails.getString("photo_id"));
						photosData.setActual_photo_path(photoDetails.getString("photo_path"));
						photosData.setPhoto_name(photoDetails.getString("photo_name"));
						photosData.setPhoto_desc(photoDetails.getString("photo_desc"));
						photosData.setOriginal_dimensions(photoDetails.getString("original_dimensions"));
						photosData.setCity_name(photoDetails.getString("city_name"));
						pagination_Date_String = photoDetails.getString("uploaded_date");
						galleryPhotoSearch.add(photosData);
					}catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return galleryPhotoSearch;
			}else{
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<UserPhotosGalleryModel> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();

			if(result == null){
				noPhotos.setVisibility(View.VISIBLE);
				Toast.makeText(getActivity(), "No Photos Fund", Toast.LENGTH_SHORT).show();
			}else if(result.size()==0){
				noPhotos.setVisibility(View.VISIBLE);
			}  else{
				adapter = new UserGalleryPhotosAdapter(getActivity(), result);
				photoGridView.setAdapter(adapter);

				if(result.size() < 20){
					flag_loading = true;
				}else{
					flag_loading = false;
				}
			}
		}
	}

	private class GroupGalleryPhotoLoadMoreAsync extends AsyncTask<Void, Void, ArrayList<UserPhotosGalleryModel>>{


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
		protected ArrayList<UserPhotosGalleryModel> doInBackground(
				Void... params) {
			// TODO Auto-generated method stub
			JSONArray userGalleryPhotosResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(UserGalleryPhotosURL + "&last_date=" + pagination_Date_String.replaceAll(" ", "%20"));

			if(jsonObjectRecived != null){
				try{
					userGalleryPhotosResponse = jsonObjectRecived.getJSONArray("photos");
				}catch(JSONException e){
					e.printStackTrace();
				}
				if(userGalleryPhotosResponse.length() > 0){
					for(int i = 0; i< userGalleryPhotosResponse.length();i++){
						UserPhotosGalleryModel photosData = new UserPhotosGalleryModel();
						JSONObject photoDetails;
						try{
							photoDetails = userGalleryPhotosResponse.getJSONObject(i);
							photosData.setPhoto_owner_id(photoDetails.getString("photo_id"));
							photosData.setActual_photo_path(photoDetails.getString("photo_path"));
							photosData.setPhoto_name(photoDetails.getString("photo_name"));
							photosData.setPhoto_desc(photoDetails.getString("photo_desc"));
							photosData.setOriginal_dimensions(photoDetails.getString("original_dimensions"));
							photosData.setCity_name(photoDetails.getString("city_name"));
							pagination_Date_String = photoDetails.getString("uploaded_date");
							galleryPhotoSearch.add(photosData);
						}catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					return galleryPhotoSearch;
				}else{
					return null;
				}
			}else{
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<UserPhotosGalleryModel> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if(result == null){
				if(adapter.isEmpty()){
					noPhotos.setVisibility(View.VISIBLE);
					flag_loading = false;
				}
				flag_loading = true;
				Toast.makeText(getActivity(), "No More Photos to Load", Toast.LENGTH_SHORT).show();
			}else if(result.size()==0){
				if(adapter.isEmpty()){
					noPhotos.setVisibility(View.VISIBLE);
				}
				flag_loading = true;
				Toast.makeText(getActivity(), "No More Photos to Load", Toast.LENGTH_SHORT).show();
				
			}        else{
				adapter = new UserGalleryPhotosAdapter(getActivity(), result);
				adapter.notifyDataSetChanged();
				flag_loading = false;
				pageCount = pageCount +20;
				photoGridView.setSelection(pageCount);
			}

		}

	}





}
