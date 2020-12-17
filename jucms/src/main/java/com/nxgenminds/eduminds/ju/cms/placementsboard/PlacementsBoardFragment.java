package com.nxgenminds.eduminds.ju.cms.placementsboard;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.guestAcademics.AcademicsAdapter;
import com.nxgenminds.eduminds.ju.cms.guestAcademics.AcademicsDeatailActivity;
import com.nxgenminds.eduminds.ju.cms.guestAcademics.AcademicsModel;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.noticeboard.NoticeBoardAdapter;
import com.nxgenminds.eduminds.ju.cms.noticeboard.NoticeBoardImageDisplayActivity;
import com.nxgenminds.eduminds.ju.cms.noticeboard.NoticeBoardModel;
import com.nxgenminds.eduminds.ju.cms.noticeboard.NoticeBoardFragment.GetNoticeBoarLoadMoreAsync;
import com.nxgenminds.eduminds.ju.cms.noticeboard.NoticeBoardFragment.GetNoticeBoardAsync;
import com.nxgenminds.eduminds.ju.cms.noticeboard.NoticeBoardFragment.GetNoticeBoardRefreshAsyncClass;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class PlacementsBoardFragment extends Fragment{

	private SwipeRefreshLayout mSwipeRefreshLayout;
    private GridView plaecementsnotiboardGridView;
	static Bitmap bitmap_img;
	private TextView noplacementsboard;
	private static int pageCount = 0;
	private String PlacementsNoticeBoardURL = Util.API + "placementsboard";
	private ArrayList<NoticeBoardModel> placementsnoticeboardmodel = new ArrayList<NoticeBoardModel>();
	private ProgressDialog pDialog;
	private static String pagination_Date_String;
	private boolean flag_loading = false;
	AlertDialogManager alert = new AlertDialogManager();
	private boolean flag_refresh = false;
	private static String refresh_Date_String;
	private NoticeBoardAdapter adapter;
	 ImageView bmImage;
	 NoticeBoardImageDisplayActivity noticeboardimagedisplay;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_noticeboard, container, false);
		plaecementsnotiboardGridView = (GridView) rootView.findViewById(R.id.noticegridview);
		mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.noticeboard_swipe_container);
		mSwipeRefreshLayout.setEnabled(false);
		
		noplacementsboard = (TextView) rootView.findViewById(R.id.frag_notice);
		noplacementsboard.setGravity(Gravity.CENTER);
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		placementsnoticeboardmodel.clear();
		pageCount = 0;
		adapter = new NoticeBoardAdapter(getActivity(),placementsnoticeboardmodel);
		
		//swipeLayout.setColorScheme(android.R.color.holo_blue_bright,android.R.color.holo_green_light, android.R.color.holo_orange_light,android.R.color.holo_red_light);
		ConnectionDetector conn = new ConnectionDetector(getActivity());
		if(conn.isConnectingToInternet()){
			new GetPlacementsNoticeBoardAsync().execute();
		}else{
			alert.showAlertDialog(getActivity(), "Connection Error", "Please check your internet connection", false);
		}
		plaecementsnotiboardGridView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				boolean enable = false;
				if(plaecementsnotiboardGridView != null && plaecementsnotiboardGridView.getChildCount() > 0){

					boolean firstItemVisible = plaecementsnotiboardGridView.getFirstVisiblePosition() == 0;
					boolean topOfFirstItemVisible = plaecementsnotiboardGridView.getChildAt(0).getTop() == 0;
					enable = firstItemVisible && topOfFirstItemVisible;
				}
				mSwipeRefreshLayout.setEnabled(enable);	
				if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0)
				{
					if(flag_loading == false)
					{ 
						flag_loading = true;
						ConnectionDetector conn = new ConnectionDetector(getActivity());
						if(conn.isConnectingToInternet()){
							new GetNoticeBoarLoadMoreAsync().execute();
						}else{
							alert.showAlertDialog(getActivity(),"Connection Error","Check your Internet Connection",false);		
						}

					}
				}
				
			}
		});
		mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				mSwipeRefreshLayout.setRefreshing(true);
				flag_refresh = false;
				ConnectionDetector conn = new ConnectionDetector(getActivity());
				if(conn.isConnectingToInternet()){
					mSwipeRefreshLayout.setRefreshing(false);
					new GetNoticeBoardRefreshAsyncClass().execute();
				}else{
					alert.showAlertDialog(getActivity(),"Connection Error","Check your Internet Connection",false);		
				}

			}
		});
		
		plaecementsnotiboardGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				//Log.i("i12222", "888888"+position);
				
				Object object = plaecementsnotiboardGridView.getItemAtPosition(position);
				NoticeBoardModel notice_data = (NoticeBoardModel)object;
				
				notice_data.getDownload_file_path();
				notice_data.getTitle();
				notice_data.getDownload_file_type();
			/*	Log.i(notice_data.getTitle(), "tittle the file for display");
				Log.i(notice_data.getDownload_file_path(), "path of the file for pdf");
				Log.i(notice_data.getDownload_file_type(),"download file type");*/
				if (notice_data.getDownload_file_type().contains("jpg")||notice_data.getDownload_file_type().contains("png")) {
					Log.i("only jpg or png", "tittle the file for display");
					Intent intent = new Intent(getActivity(),NoticeBoardImageDisplayActivity.class);
					intent.putExtra("noticeimagelink",notice_data.getDownload_file_path());
					intent.putExtra("noticetitle",notice_data.getTitle());
					startActivity(intent);
					
					
				} else {
					 Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(notice_data.getDownload_file_path()));
		                startActivity(browserIntent);
					Log.i("only pdf", "only pdf");
				}
				
				
				/*Intent intent = new Intent(getActivity(),NoticeBoardDeatailActivity.class);
			
				intent.putExtra("notice", event_data.getTitle());
				intent.putExtra("desc", event_data.getDescription());
				intent.putExtra("pdfilename",event_data.getDownload_file_name());
				intent.putExtra("pdflink",event_data.getDownload_file_path());
				startActivity(intent);*/
			}
		});
		
	}
	public class GetNoticeBoardRefreshAsyncClass extends AsyncTask<Void,Void,Void>{

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			mSwipeRefreshLayout.setRefreshing(false);

			if(HttpGetClient.statuscode == 200)
			{
				if(placementsnoticeboardmodel == null){
					Toast.makeText(getActivity(), "No more broadcast to load", Toast.LENGTH_SHORT).show();
				} 
				else if(placementsnoticeboardmodel.size()==0){
					Toast.makeText(getActivity(), "No more broadcast to load", Toast.LENGTH_SHORT).show();
				}	   
				else{
					//adapter = new BroadcastListAdapter_new(getActivity(),mArrayListBroadcast);
					adapter.notifyDataSetChanged();		     
				}
			}
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONObject jsonObjectReceived = HttpGetClient.sendHttpPost(PlacementsNoticeBoardURL+"?first_date="+refresh_Date_String.replaceAll(" ", "%20"));
			if (jsonObjectReceived != null) {
				try{
					if(jsonObjectReceived.getString("error").equalsIgnoreCase("false")){
						JSONArray broadcastResponse = jsonObjectReceived.getJSONArray("placementsboard");
						for(int i = 0; i< broadcastResponse.length();i++){
							NoticeBoardModel noticeBoardData = new NoticeBoardModel();
							JSONObject noticeBoardDetails;
							noticeBoardDetails = broadcastResponse.getJSONObject(i);
							

							noticeBoardData.setTitle(noticeBoardDetails.getString("placements_notice_board_text"));
							Log.i(noticeBoardDetails.getString("placements_notice_board_text"), "getnotce details");
							
							noticeBoardData.setNoticeboard_id(noticeBoardDetails.getString("placements_notice_board_id"));
							
							Log.i(noticeBoardDetails.getString("placements_notice_board_id"), "getnoticeboard_id");
							
							/*academicsData.setDescription(acdamicsDetails.getString("description"));*/
							noticeBoardData.setDownload_file_type(noticeBoardDetails.getString("file_type"));
							
							noticeBoardData.setDownload_file_path(noticeBoardDetails.getString("download_file_path"));
						
							downloadImageFromPath(noticeBoardDetails.getString("download_file_path"));

							noticeBoardData.setBit_image(bitmap_img);
							Log.i(noticeBoardDetails.getString("download_file_path"), "download_file_path");
							
							noticeBoardData.setCreated_date(noticeBoardDetails.getString("created_date"));
							Log.i(noticeBoardDetails.getString("created_date"), "created_date");

							pagination_Date_String = noticeBoardDetails.getString("created_date");
						
							if(!flag_refresh){
								flag_refresh = true;
								refresh_Date_String = noticeBoardDetails.getString("created_date");
							}

							//broadcastData.setFrom_user_profile_pic(broadcastDetails.getString("from_user_profile_pic"));

							placementsnoticeboardmodel.add(0,noticeBoardData);
						}
					}
				}	catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			return null;
		}

	}
	public class GetNoticeBoarLoadMoreAsync extends AsyncTask<Void, Void, ArrayList<NoticeBoardModel>>{

		@Override
		protected ArrayList<NoticeBoardModel> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONArray broadcastResponse = null;

			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(PlacementsNoticeBoardURL + "?last_date=" + pagination_Date_String.replaceAll(" ", "%20"));

			if(jsonObjectRecived != null){
				try{
					broadcastResponse = jsonObjectRecived.getJSONArray("");
				}catch (JSONException e) {
					e.printStackTrace();
				}
				if(broadcastResponse.length() > 0){
					for(int i = 0; i< broadcastResponse.length();i++){
						NoticeBoardModel noticeBoardData = new NoticeBoardModel();
						JSONObject noticeBoardDetails;

						try {
							noticeBoardDetails = broadcastResponse.getJSONObject(i);
							
							noticeBoardData.setTitle(noticeBoardDetails.getString("placements_notice_board_text"));
							Log.i(noticeBoardDetails.getString("placements_notice_board_text"), "getnotce details");
							
							noticeBoardData.setNoticeboard_id(noticeBoardDetails.getString("placements_notice_board_id"));
							
							Log.i(noticeBoardDetails.getString("placements_notice_board_id"), "getnoticeboard_id");
							
							/*academicsData.setDescription(acdamicsDetails.getString("description"));*/
							noticeBoardData.setDownload_file_type(noticeBoardDetails.getString("file_type"));
							
							noticeBoardData.setDownload_file_path(noticeBoardDetails.getString("download_file_path"));
						
							downloadImageFromPath(noticeBoardDetails.getString("download_file_path"));

							noticeBoardData.setBit_image(bitmap_img);
							Log.i(noticeBoardDetails.getString("download_file_path"), "download_file_path");
							
							noticeBoardData.setCreated_date(noticeBoardDetails.getString("created_date"));
							Log.i(noticeBoardDetails.getString("created_date"), "created_date");

							pagination_Date_String = noticeBoardDetails.getString("created_date");
							if(!flag_refresh){
								flag_refresh = true;
								refresh_Date_String = noticeBoardDetails.getString("created_date");
							}

							placementsnoticeboardmodel.add(noticeBoardData);
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
					   
					return placementsnoticeboardmodel;
				}
				else
				{
					return null;
				}
			}
			else
			{
				return null;
			}
		}
		
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
		protected void onPostExecute(ArrayList<NoticeBoardModel> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if(result == null){
				flag_loading = true;
				Toast.makeText(getActivity(),"No more data to load",Toast.LENGTH_SHORT).show();
			} 
			else if(result.size() == 0){

				flag_loading = true;
				Toast.makeText(getActivity(),"No more data to load",Toast.LENGTH_SHORT).show();
			}
			else{	
				adapter.notifyDataSetChanged();
				flag_loading = false;
				pageCount = pageCount +20;
				plaecementsnotiboardGridView.setSelection(pageCount);
			}
		}
	}
	
	public Bitmap downloadImageFromPath(String path){
        InputStream in =null;
       
         
         int responseCode = -1;
        try{

             URL url = new URL(path);//"http://192.xx.xx.xx/mypath/img1.jpg
             HttpURLConnection con = (HttpURLConnection)url.openConnection();
             con.setDoInput(true);
             con.connect();
             responseCode = con.getResponseCode();
             if(responseCode == HttpURLConnection.HTTP_OK)
             {
                 //download 
                 in = con.getInputStream();
                 bitmap_img = BitmapFactory.decodeStream(in);
                 in.close();
                
             }

        }
        catch(Exception ex){
            Log.e("Exception",ex.toString());
        }
		return bitmap_img;
    }	
	
	public class GetPlacementsNoticeBoardAsync extends AsyncTask<Void, Void, ArrayList<NoticeBoardModel>>{

		private AsyncTask<String, String, Bitmap> execute;

		@Override
		protected void onPreExecute() {
			if(pDialog == null){
				pDialog = Util.createProgressDialog(getActivity());
				pDialog.setCancelable(false);
				pDialog.setTitle("please wait..");
				pDialog.show();}
			else{
				pDialog.setCancelable(false);
				pDialog.show();
			}}


		@Override
		protected ArrayList<NoticeBoardModel> doInBackground(Void... params) {
			JSONArray connectionsResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(PlacementsNoticeBoardURL);
			if(jsonObjectRecived != null){

				try{
					connectionsResponse = jsonObjectRecived.getJSONArray("placementsboard");
					for(int i = 0; i< connectionsResponse.length();i++){
						NoticeBoardModel noticeBoardData = new NoticeBoardModel();
						JSONObject noticeBoardDetails;
						try{
							noticeBoardDetails = connectionsResponse.getJSONObject(i);
						/*	academicsData.setStatus(acdamicsDetails.getString("status"));*/
							noticeBoardData.setTitle(noticeBoardDetails.getString("placements_notice_board_text"));
							Log.i(noticeBoardDetails.getString("placements_notice_board_text"), "getnotce details");
							
							noticeBoardData.setNoticeboard_id(noticeBoardDetails.getString("placements_notice_board_id"));
							
							Log.i(noticeBoardDetails.getString("placements_notice_board_id"), "getnoticeboard_id");
							
							/*academicsData.setDescription(acdamicsDetails.getString("description"));*/
							noticeBoardData.setDownload_file_type(noticeBoardDetails.getString("file_type"));
							
							noticeBoardData.setDownload_file_path(noticeBoardDetails.getString("download_file_path"));
						
							downloadImageFromPath(noticeBoardDetails.getString("download_file_path"));

							noticeBoardData.setBit_image(bitmap_img);
							Log.i(noticeBoardDetails.getString("download_file_path"), "download_file_path");
							
							noticeBoardData.setCreated_date(noticeBoardDetails.getString("created_date"));
							Log.i(noticeBoardDetails.getString("created_date"), "created_date");
							pagination_Date_String = noticeBoardDetails.getString("created_date");
							/*academicsData.setCreated_by(acdamicsDetails.getString("created_by"));
							academicsData.setModified_date(acdamicsDetails.getString("modified_date"));
							academicsData.setModified_by(acdamicsDetails.getString("modified_by"));*/
							
							
							if(!flag_refresh){
								flag_refresh = true;
								refresh_Date_String = noticeBoardDetails.getString("created_date");
							}
							placementsnoticeboardmodel.add(noticeBoardData);
						}catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}catch(JSONException e){
					e.printStackTrace();
				}

				return placementsnoticeboardmodel;
			}else{
				return null;
			}
		}

		@Override
		protected void onPostExecute(ArrayList<NoticeBoardModel> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if(result == null){
				noplacementsboard.setVisibility(View.VISIBLE);
				
				Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_SHORT).show();
			}else if(result.size()==0){
				noplacementsboard.setVisibility(View.VISIBLE);
			}  else{
				//adapter = new BroadcastListAdapter_new(getActivity(), result);
				plaecementsnotiboardGridView.setAdapter(adapter);

				if(result.size() < 20){
					flag_loading = true;
				}else{
					flag_loading = false;
				}
			}
		}

	}
	

}

