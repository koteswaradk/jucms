package com.nxgenminds.eduminds.ju.cms.noticeboard;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.broadcast_new.BroadcastListModel_new;
import com.nxgenminds.eduminds.ju.cms.guestAcademics.AcademicsAdapter;
import com.nxgenminds.eduminds.ju.cms.guestAcademics.AcademicsDeatailActivity;
import com.nxgenminds.eduminds.ju.cms.guestAcademics.AcademicsModel;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class NoticeBoardFragment extends Fragment{
	

	private SwipeRefreshLayout mSwipeRefreshLayout;
    private GridView notiboardGridView;
	//static Bitmap bitmap_img;

	private TextView nonoticeboard;
	private static int pageCount = 0;
	private String NoticeBoardURL = Util.API + "noticeboard";
	private ArrayList<NoticeBoardModel> noticeboardmodel = new ArrayList<NoticeBoardModel>();
	private ProgressDialog pDialog;
	private static String pagination_Date_String;
	
	AlertDialogManager alert = new AlertDialogManager();
	private boolean flag_loading = false;
	private boolean flag_refresh = false;
	private static String refresh_Date_String;
	private NoticeBoardAdapter adapter;
	 ImageView bmImage;
	 NoticeBoardImageDisplayActivity noticeboardimagedisplay;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_noticeboard, container, false);
		notiboardGridView = (GridView) rootView.findViewById(R.id.noticegridview);
		mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.noticeboard_swipe_container);
		mSwipeRefreshLayout.setEnabled(false);
		
		nonoticeboard = (TextView) rootView.findViewById(R.id.frag_notice);
		nonoticeboard.setGravity(Gravity.CENTER);
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		noticeboardmodel.clear();
		pageCount = 0;
		adapter = new NoticeBoardAdapter(getActivity(),noticeboardmodel);
		
		//swipeLayout.setColorScheme(android.R.color.holo_blue_bright,android.R.color.holo_green_light, android.R.color.holo_orange_light,android.R.color.holo_red_light);
		ConnectionDetector conn = new ConnectionDetector(getActivity());
		if(conn.isConnectingToInternet()){
			new GetNoticeBoardAsync().execute();
		}else{
			alert.showAlertDialog(getActivity(), "Connection Error", "Please check your internet connection", false);
		}
		notiboardGridView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				boolean enable = false;
				if(notiboardGridView != null && notiboardGridView.getChildCount() > 0){

					boolean firstItemVisible = notiboardGridView.getFirstVisiblePosition() == 0;
					boolean topOfFirstItemVisible = notiboardGridView.getChildAt(0).getTop() == 0;
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
		
		notiboardGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				//Log.i("i12222", "888888"+position);
				
				Object object = notiboardGridView.getItemAtPosition(position);
				NoticeBoardModel notice_data = (NoticeBoardModel)object;
				
				notice_data.getDownload_file_path();
				notice_data.getTitle();
				notice_data.getDownload_file_type();
			/*	Log.i(notice_data.getTitle(), "tittle the file for display");
				Log.i(notice_data.getDownload_file_path(), "path of the file for pdf");
				Log.i(notice_data.getDownload_file_type(),"download file type");*/
				if (notice_data.getDownload_file_type().contains("jpg")||notice_data.getDownload_file_type().contains("gif")||notice_data.getDownload_file_type().contains("jpeg")||notice_data.getDownload_file_type().contains("png")) {
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
				if(noticeboardmodel == null){
					Toast.makeText(getActivity(), "No more broadcast to load", Toast.LENGTH_SHORT).show();
				} 
				else if(noticeboardmodel.size()==0){
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
			JSONObject jsonObjectReceived = HttpGetClient.sendHttpPost(NoticeBoardURL+"?first_date="+refresh_Date_String.replaceAll(" ", "%20"));
			if (jsonObjectReceived != null) {
				try{
					if(jsonObjectReceived.getString("error").equalsIgnoreCase("false")){
						JSONArray broadcastResponse = jsonObjectReceived.getJSONArray("noticeboard");
						for(int i = 0; i< broadcastResponse.length();i++){
							NoticeBoardModel noticeBoardData = new NoticeBoardModel();
							JSONObject noticeBoardDetails;
							noticeBoardDetails = broadcastResponse.getJSONObject(i);
							

							noticeBoardData.setTitle(noticeBoardDetails.getString("notice"));
							Log.i(noticeBoardDetails.getString("notice"), "getnotce details");
							
							noticeBoardData.setNoticeboard_id(noticeBoardDetails.getString("noticeboard_id"));
							
							Log.i(noticeBoardDetails.getString("noticeboard_id"), "getnotce noticeboard_id");
							
							/*academicsData.setDescription(acdamicsDetails.getString("description"));*/
							noticeBoardData.setDownload_file_type(noticeBoardDetails.getString("file_type"));
							
							noticeBoardData.setDownload_file_path(noticeBoardDetails.getString("download_file_path"));
						
							//downloadImageFromPath(noticeBoardDetails.getString("download_file_path"));

						//	noticeBoardData.setBit_image(bitmap_img);
							Log.i(noticeBoardDetails.getString("download_file_path"), "download_file_path");
							
							noticeBoardData.setCreated_date(noticeBoardDetails.getString("created_date"));
							Log.i(noticeBoardDetails.getString("created_date"), "created_date");

							pagination_Date_String = noticeBoardDetails.getString("created_date");
						
							if(!flag_refresh){
								flag_refresh = true;
								refresh_Date_String = noticeBoardDetails.getString("created_date");
							}

							//broadcastData.setFrom_user_profile_pic(broadcastDetails.getString("from_user_profile_pic"));

							noticeboardmodel.add(noticeBoardData);
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

			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(NoticeBoardURL + "?last_date=" + pagination_Date_String.replaceAll(" ", "%20"));

			if(jsonObjectRecived != null){
				try{
					broadcastResponse = jsonObjectRecived.getJSONArray("noticeboard");
				}catch (JSONException e) {
					e.printStackTrace();
				}
				if(broadcastResponse.length() > 0){
					for(int i = 0; i< broadcastResponse.length();i++){
						NoticeBoardModel noticeBoardData = new NoticeBoardModel();
						JSONObject noticeBoardDetails;

						try {
							noticeBoardDetails = broadcastResponse.getJSONObject(i);
							
							noticeBoardData.setTitle(noticeBoardDetails.getString("notice"));
							Log.i(noticeBoardDetails.getString("notice"), "getnotice details");
							
							noticeBoardData.setNoticeboard_id(noticeBoardDetails.getString("noticeboard_id"));
							
							Log.i(noticeBoardDetails.getString("noticeboard_id"), "getnotice noticeboard_id");
							
							/*academicsData.setDescription(acdamicsDetails.getString("description"));*/
							noticeBoardData.setDownload_file_type(noticeBoardDetails.getString("file_type"));
							
							noticeBoardData.setDownload_file_path(noticeBoardDetails.getString("download_file_path"));
						
							//downloadImageFromPath(noticeBoardDetails.getString("download_file_path"));

							//noticeBoardData.setBit_image(bitmap_img);
							Log.i(noticeBoardDetails.getString("download_file_path"), "download_file_path");
							
							noticeBoardData.setCreated_date(noticeBoardDetails.getString("created_date"));
							Log.i(noticeBoardDetails.getString("created_date"), "created_date");

							pagination_Date_String = noticeBoardDetails.getString("created_date");
							if(!flag_refresh){
								flag_refresh = true;
								refresh_Date_String = noticeBoardDetails.getString("created_date");
							}

							noticeboardmodel.add(noticeBoardData);
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
					   
					return noticeboardmodel;
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
				notiboardGridView.setSelection(pageCount);
			}
		}
	}
	
	/*public Bitmap downloadImageFromPath(String path){
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
    }	*/
	
	public class GetNoticeBoardAsync extends AsyncTask<Void, Void, ArrayList<NoticeBoardModel>>{

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
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(NoticeBoardURL);
			if(jsonObjectRecived != null){

				try{
					connectionsResponse = jsonObjectRecived.getJSONArray("noticeboard");
					for(int i = 0; i< connectionsResponse.length();i++){
						NoticeBoardModel noticeBoardData = new NoticeBoardModel();
						JSONObject noticeBoardDetails;
						try{
							noticeBoardDetails = connectionsResponse.getJSONObject(i);
						/*	academicsData.setStatus(acdamicsDetails.getString("status"));*/
							noticeBoardData.setTitle(noticeBoardDetails.getString("notice"));
							Log.i(noticeBoardDetails.getString("notice"), "getnotce details");
							
							noticeBoardData.setNoticeboard_id(noticeBoardDetails.getString("noticeboard_id"));
							
							Log.i(noticeBoardDetails.getString("noticeboard_id"), "getnotce noticeboard_id");
							
							/*academicsData.setDescription(acdamicsDetails.getString("description"));*/
							noticeBoardData.setDownload_file_type(noticeBoardDetails.getString("file_type"));
							
							noticeBoardData.setDownload_file_path(noticeBoardDetails.getString("download_file_path"));
						
							//downloadImageFromPath(noticeBoardDetails.getString("download_file_path"));

							//noticeBoardData.setBit_image(bitmap_img);
							Log.i(noticeBoardDetails.getString("download_file_path"), "download_file_path");
							
							noticeBoardData.setCreated_date(noticeBoardDetails.getString("created_date"));
							Log.i(noticeBoardDetails.getString("created_date"), "created_date");
							pagination_Date_String = noticeBoardDetails.getString("created_date");
							/*academicsData.setCreated_by(acdamicsDetails.getString("created_by"));
							academicsData.setModified_date(acdamicsDetails.getString("modified_date"));
							academicsData.setModified_by(acdamicsDetails.getString("modified_by"));*/
							
							
							if(!flag_refresh){
								flag_refresh = true;
								//refresh_Date_String = noticeBoardDetails.getString("created_date");
							}
							noticeboardmodel.add(noticeBoardData);
						}catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}catch(JSONException e){
					e.printStackTrace();
				}

				return noticeboardmodel;
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
				nonoticeboard.setVisibility(View.VISIBLE);
				Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_SHORT).show();
			}else if(result.size()==0){
				nonoticeboard.setVisibility(View.VISIBLE);
			}  else{
				//adapter = new BroadcastListAdapter_new(getActivity(), result);
				notiboardGridView.setAdapter(adapter);

				if(result.size() < 20){
					flag_loading = true;
				}else{
					flag_loading = false;
				}
			}
		}

	}
	

}
