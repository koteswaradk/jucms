package com.nxgenminds.eduminds.ju.cms.adapters;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.models.MembersModel;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpPostClient;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class MembersAdapter extends BaseAdapter{

	private ArrayList<MembersModel> listData;
	private LayoutInflater layoutInflater;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private Context prova;

	public MembersAdapter(Context context,ArrayList<MembersModel> listData){
		this.listData = listData;
		imageLoader = ImageLoader.getInstance();
		prova = context;

		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.placeholder)
		.showImageForEmptyUri(R.drawable.placeholder) 
		.showImageOnFail(R.drawable.placeholder)
		.cacheInMemory(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		//.displayer(new RoundedBitmapDisplayer(70))//////
		.build();

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if (layoutInflater == null)
			layoutInflater = (LayoutInflater) prova.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(convertView == null){
			convertView = layoutInflater.inflate(R.layout.custom_members, null);
			holder = new ViewHolder();
			holder.textViewName = (TextView) convertView.findViewById(R.id.membersname);
			holder.imageViewPhoto = (ImageView) convertView.findViewById(R.id.membersroundedimage);
			holder.checkFriendRelations = (ImageView) convertView.findViewById(R.id.membersconnectionstatus);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		final MembersModel membersItem = (MembersModel) listData.get(position);

		Typeface typeFace=Typeface.createFromAsset(prova.getAssets(),"fonts/BentonSans-Regular.otf");
		// addind Font 

		holder.textViewName.setText(membersItem.getFirstname());

		if(membersItem.getFriend().equalsIgnoreCase("1")){
			holder.checkFriendRelations.setImageDrawable(prova.getResources().getDrawable(R.drawable.friends_connected));
		}else if(membersItem.getPending_req_status().equalsIgnoreCase("1") || membersItem.getAccept_req_status().equalsIgnoreCase("1")){
			holder.checkFriendRelations.setImageDrawable(prova.getResources().getDrawable(R.drawable.friends_pending));
		}else{
			holder.checkFriendRelations.setImageDrawable(prova.getResources().getDrawable(R.drawable.friends_add));
		}
		holder.textViewName.setTypeface(typeFace);

		String imageUrl = membersItem.getProfile_thumb1();

		imageLoader.displayImage(imageUrl, holder.imageViewPhoto, options, new SimpleImageLoadingListener() {
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
		holder.checkFriendRelations.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!membersItem.getFriend().equalsIgnoreCase("1")){
					if(!membersItem.getPending_req_status().equalsIgnoreCase("1")){
						if(!membersItem.getAccept_req_status().equalsIgnoreCase("1")){
							membersItem.setPending_req_status("1");
							holder.checkFriendRelations.setImageDrawable(prova.getResources().getDrawable(R.drawable.friends_pending));
							new AddFriendAsync().execute(membersItem.getUser_id());
						}
					}
				}
			}
		});
		return convertView;
	}

	static class ViewHolder{
		ImageView imageViewPhoto;
		ImageView checkFriendRelations;
		TextView textViewName;
	}

	private class AddFriendAsync extends AsyncTask<String, Void, Integer>{

		@Override
		protected Integer doInBackground(String... params) {
			// TODO Auto-generated method stub
			// JSON object to hold the information, which is sent to the server
			JSONObject jsonObjSend = new JSONObject();
			String UserId = params[0];

			try {
				jsonObjSend.put("user_two",UserId);
				System.out.println("RAJESH FRIENDS IDS"+ UserId);
			} 
			catch (JSONException e) {
				e.printStackTrace();
			}

			JSONObject jsonObjRecv = HttpPostClient.sendHttpPost(Util.API+"friend_request", jsonObjSend);
			Log.i("jsonObjRecv",jsonObjRecv.toString());
			return null;
		}

	}

}
