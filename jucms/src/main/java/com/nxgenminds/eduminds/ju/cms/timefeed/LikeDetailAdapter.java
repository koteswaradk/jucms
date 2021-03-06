package com.nxgenminds.eduminds.ju.cms.timefeed;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
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


public class LikeDetailAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<LikeDetailModel> mLikeArrayList;
	private ImageLoader imageLoader;
	private DisplayImageOptions profile_options;


	public LikeDetailAdapter(Context c,ArrayList<LikeDetailModel> list){
		context = c;
		mLikeArrayList = list;
		imageLoader = ImageLoader.getInstance();

		profile_options = new DisplayImageOptions.Builder()
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
		// TODO Auto-generated method stub
		return mLikeArrayList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mLikeArrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView =  inflater.inflate(R.layout.custom_like_detail,parent,false);
			holder.profileImage = (ImageView)convertView.findViewById(R.id.custom_like_detail_user_image);
			holder.userName = (TextView)convertView.findViewById(R.id.custom_like_detail_user_name);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}

		LikeDetailModel model = (LikeDetailModel)mLikeArrayList.get(position);
		Typeface typeFace=Typeface.createFromAsset(context.getAssets(),"fonts/BentonSans-Book.otf");
		/*String profileName = model.getFirstname();
		if(!(model.getLastname()==null || model.getLastname().equalsIgnoreCase("null"))){
			profileName = profileName+" "+model.getLastname();
		}*/
		holder.userName.setText(model.getFullname());
		holder.userName.setTypeface(typeFace);
		String imageUrl = model.getProfile_photo();

		if(imageUrl!=null){
			imageLoader.displayImage(imageUrl, holder.profileImage, profile_options, new SimpleImageLoadingListener() {
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
		}
		return convertView;
	}
	class ViewHolder{
		ImageView profileImage;
		TextView userName;
	}
}
