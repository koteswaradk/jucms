package com.nxgenminds.eduminds.ju.cms.messages;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nxgenminds.eduminds.ju.cms.R;


public class MessagesAdapter extends BaseAdapter{

	private ArrayList<MessageModel> listData;
	private LayoutInflater layoutInflater;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private Context prova;
	// initialization for dialog box
	private TextView dialog_username,dialog_message_text;
	private ImageView dialog_user_image;
	private Button dialog_cancel;
	private ProgressDialog pDialog;

	public MessagesAdapter(Context context,ArrayList<MessageModel> listData){
		this.listData = listData;
		imageLoader = ImageLoader.getInstance();
		prova = context;
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.placeholder)
		.showImageForEmptyUri(R.drawable.placeholder) 
		.showImageOnFail(R.drawable.placeholder)
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
	public View getView(int position,  View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (layoutInflater == null)
			layoutInflater = (LayoutInflater) prova.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(convertView == null){
			convertView = layoutInflater.inflate(R.layout.custom_messages, null);
			holder = new ViewHolder();
			holder.messageFriendName = (TextView) convertView.findViewById(R.id.messages_username);
			holder.messageFriendMessage = (TextView) convertView.findViewById(R.id.messages_usermessage);
			holder.messageFriendDate= (TextView) convertView.findViewById(R.id.messages_usertimestamp);
			holder.messageFriendPhoto= (ImageView) convertView.findViewById(R.id.messages_userProfile);

			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		final MessageModel messagesItem = (MessageModel) listData.get(position);

		Typeface typeFace=Typeface.createFromAsset(prova.getAssets(),"fonts/BentonSans-Regular.otf");
		Typeface typeFace_thin=Typeface.createFromAsset(prova.getAssets(),"fonts/BentonSans-Book.otf");
		// adding Font 

		if(messagesItem.getLastname()==null ||messagesItem.getLastname().equalsIgnoreCase("null")||messagesItem.getLastname().equalsIgnoreCase(" ") )
		{
			holder.messageFriendName.setText(messagesItem.getFirstname());
		}else{
			holder.messageFriendName.setText(messagesItem.getFirstname()+" "+messagesItem.getLastname());
		}

		holder.messageFriendMessage.setText(messagesItem.getBody());
		holder.messageFriendDate.setText(messagesItem.getCreated_date());



		String imageUrl = messagesItem.getProfile_photo();

		imageLoader.displayImage(imageUrl, holder.messageFriendPhoto, options, new SimpleImageLoadingListener() {
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
		});

		return convertView;
	}

	static class ViewHolder{
		ImageView messageFriendPhoto;
		TextView messageFriendName;
		TextView messageFriendMessage;
		TextView messageFriendDate;
	}



}
