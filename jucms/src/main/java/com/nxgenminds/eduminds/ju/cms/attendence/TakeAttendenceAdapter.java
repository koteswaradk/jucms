package com.nxgenminds.eduminds.ju.cms.attendence;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nxgenminds.eduminds.ju.cms.R;


public class TakeAttendenceAdapter extends BaseAdapter{

	private Context context;
	private LayoutInflater layoutInflater;
	private ArrayList<AttendenceStudentsModel> listData;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	public TakeAttendenceAdapter(Context context, ArrayList<AttendenceStudentsModel> listData) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.listData = listData;

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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (layoutInflater == null)
			layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(convertView == null){
			convertView = layoutInflater.inflate(R.layout.custom_take_attendence_activity, null);
			holder = new ViewHolder();
			holder.studentImage = (ImageView) convertView.findViewById(R.id.studentImage);
			holder.tvStudentName = (TextView) convertView.findViewById(R.id.studentName);
			holder.attendenceStatusToggle = (ToggleButton) convertView.findViewById(R.id.attendenceToggle);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		final AttendenceStudentsModel topicsItem = (AttendenceStudentsModel) listData.get(position);

		if(topicsItem.getLastname()==null || topicsItem.getLastname().equalsIgnoreCase("") || topicsItem.getLastname().equalsIgnoreCase("null"))
		{
			holder.tvStudentName.setText(topicsItem.getFirstname());

		}else
		{
			holder.tvStudentName.setText(topicsItem.getFirstname() + " " + topicsItem.getLastname());

		}

		if(topicsItem.getPresentStatus().equalsIgnoreCase("1")){
			holder.attendenceStatusToggle.setChecked(true);

		}else{
			holder.attendenceStatusToggle.setChecked(false); 
		}

		/*	if(holder.attendenceStatusToggle.isChecked()){
			topicsItem.setPresentStatus("1");
			
		}else
		{
			topicsItem.setPresentStatus("0");
			
		}*/

		holder.attendenceStatusToggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					// The toggle is enabled
					topicsItem.setPresentStatus("1");
					
				} else {
					// The toggle is disabled
					topicsItem.setPresentStatus("0");
					
				}
			}
		});


		imageLoader.displayImage(topicsItem.getProfile_photo(), holder.studentImage, options, new SimpleImageLoadingListener() {
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

		ImageView studentImage;
		TextView tvStudentName;
		ToggleButton attendenceStatusToggle;

	}
}