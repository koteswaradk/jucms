package com.nxgenminds.eduminds.ju.cms;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.nxgenminds.eduminds.ju.cms.adapters.ImagePagerAdapter;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.models.UserPhotosGalleryModel;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;
import com.nxgenminds.eduminds.ju.cms.utils.Util.Extra;


public class ImagePagerActivity extends ImagePagerBaseActivity {

	private ViewPager pager;
	private int pagerPosition;
	private String checkActivity;
	private String UserGalleryPhotosURL;
	private ArrayList<UserPhotosGalleryModel> galleryPhotoSearch = new ArrayList<UserPhotosGalleryModel>();
	private static final String STATE_POSITION = "STATE_POSITION";
	
	private AlertDialogManager alert = new AlertDialogManager();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		setContentView(R.layout.activity_image_pager);
		initView();

		Bundle bundle = getIntent().getExtras();

		assert bundle != null;
		//String[] imageUrls = bundle.getStringArray(Extra.IMAGES);
		pagerPosition = bundle.getInt(Extra.IMAGE_POSITION, 0);
		checkActivity = bundle.getString("Activity");
        
		if(checkActivity.equalsIgnoreCase("Group")){
			UserGalleryPhotosURL = Util.API +"photo?org_photo=1" +"&all=1";
		}
		if(checkActivity.equalsIgnoreCase("User")){
			UserGalleryPhotosURL = Util.API +"photo?user_id=" + Util.USER_ID +"&all=1";
		}
		if(checkActivity.equalsIgnoreCase("ThirdParty")){
			String FriendID = bundle.getString("FriendID");
			
			UserGalleryPhotosURL = Util.API +"photo?user_id=" + FriendID + "&all=1";
		}
		if(checkActivity.equalsIgnoreCase("Guest")){
			UserGalleryPhotosURL = Util.API +"guest_photo?org_photo=1" +"&all=1";
		}

		
		if (savedInstanceState != null) {
			pagerPosition = savedInstanceState.getInt(STATE_POSITION);
		}
        
		ConnectionDetector conn = new ConnectionDetector(this);
		if(conn.isConnectingToInternet()){
		new UserGalleryPhotoPagerAsync().execute();
		} else{
			alert.showAlertDialog(this,"ConnectionError","Check your Internet Connection",false);
		}
	}




	private void initView() {
		// TODO Auto-generated method stub
		pager = (ViewPager) findViewById(R.id.pager_photos);
	}


	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, pager.getCurrentItem());
	}

	public boolean isViewFromObject(View view, Object object) {
		return view.equals(object);
	}

	public void restoreState(Parcelable state, ClassLoader loader) {
	}

	public Parcelable saveState() { 
		return null;
	}




	private class UserGalleryPhotoPagerAsync extends AsyncTask<Void, Void, ArrayList<UserPhotosGalleryModel>>{

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
						/*photosData.setPhoto_owner_id(photoDetails.getString("photo_owner_id"));
						photosData.setPhoto_name(photoDetails.getString("photo_name"));
						photosData.setPhoto_desc(photoDetails.getString("photo_desc"));
						photosData.setPhoto_taken_date(photoDetails.getString("photo_taken_date"));
						photosData.setOriginal_photo_size(photoDetails.getString("original_photo_size"));
						photosData.setOriginal_dimensions(photoDetails.getString("original_dimensions"));
						photosData.setActual_photo_path(photoDetails.getString("actual_photo_path"));
						photosData.setActual_photo_base_name(photoDetails.getString("actual_photo_base_name"));
						photosData.setUploaded_by(photoDetails.getString("uploaded_by"));
						photosData.setUploaded_date(photoDetails.getString("uploaded_date"));
						photosData.setCity_name(photoDetails.getString("city_name"));
						 */
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

			if(result == null){
			}else if(result.size()==0){
				/*ImagePagerAdapter adapter = new ImagePagerAdapter(getApplicationContext(), new ArrayList<UserPhotosGalleryModel>());
				pager.setAdapter(adapter);
				pager.setCurrentItem(pagerPosition);*/
			} else{
				ImagePagerAdapter adapter = new ImagePagerAdapter(getApplicationContext(), result);
				pager.setAdapter(adapter);
				pager.setCurrentItem(pagerPosition);			}

		}

	}



}
