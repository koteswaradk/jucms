package com.nxgenminds.eduminds.ju.cms.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
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
import com.nxgenminds.eduminds.ju.cms.models.DiscussionForumReplyModel;


public class DiscussionReplysAdapter extends BaseAdapter{

	private ArrayList<DiscussionForumReplyModel> listData;
	private LayoutInflater layoutInflater;
	private Context prova;

	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	public DiscussionReplysAdapter(Context context,ArrayList<DiscussionForumReplyModel> listData){
		this.listData = listData;
		prova = context;
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.placeholder)
		.showImageForEmptyUri(R.drawable.placeholder) 
		.showImageOnFail(R.drawable.placeholder)
		.cacheInMemory(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
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
		ViewHolder holder;
		if (layoutInflater == null)
			layoutInflater = (LayoutInflater) prova.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(convertView == null){
			convertView = layoutInflater.inflate(R.layout.custom_discussion_reply, null);
			holder = new ViewHolder();
			holder.userImageiv = (ImageView) convertView.findViewById(R.id.userImage);
			holder.userNametv = (TextView) convertView.findViewById(R.id.userName);
			holder.replyAnswer = (TextView) convertView.findViewById(R.id.answer);
			holder.timeEgotv = (TextView) convertView.findViewById(R.id.time_ago);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		final DiscussionForumReplyModel topicsItem = (DiscussionForumReplyModel) listData.get(position);

		holder.userNametv.setText(topicsItem.getFirstname());
		holder.replyAnswer.setText(topicsItem.getForum_reply_content());
		holder.timeEgotv.setText(topicsItem.getCreated_date());


		imageLoader.displayImage(topicsItem.getProfile_photo(), holder.userImageiv, options, new SimpleImageLoadingListener() {
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


		return convertView;
	}

	static class ViewHolder{

		ImageView userImageiv;
		TextView userNametv;
		TextView replyAnswer;
		TextView timeEgotv;

	}
}
