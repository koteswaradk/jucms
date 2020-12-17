package com.nxgenminds.eduminds.ju.cms.classSchedule;

import java.util.ArrayList;

import org.w3c.dom.Text;

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
import com.nxgenminds.eduminds.ju.cms.models.ClassScheduleModel;


public class AdminFilterAdapter extends BaseAdapter{
	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	private ArrayList<ClassScheduleModel> mClassScheduleArrayList;
	private Context context;


	public AdminFilterAdapter(Context c,ArrayList<ClassScheduleModel> scheduleList){
		mClassScheduleArrayList = scheduleList;
		this.context = c;
		imageLoader = ImageLoader.getInstance();

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
		return mClassScheduleArrayList.size();
	}

	@Override
	public Object getItem(int position) {
		return mClassScheduleArrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.custom_class_schedule,parent,false);
			holder = new ViewHolder();
			holder.slotName = (TextView) convertView.findViewById(R.id.custom_class_schedule_slot_name);
			holder.slotStartTime = (TextView)convertView.findViewById(R.id.custom_class_schedule_slot_start_time);
			holder.slotEndTime =(TextView)convertView.findViewById(R.id.custom_class_schedule_slot_end_time);
			holder.teacherName = (TextView) convertView.findViewById(R.id.custom_class_schedule_teacher_name);
			holder.teacherProfileImage = (ImageView) convertView.findViewById(R.id.custom_class_schedule_teacher_profile_image);
			holder.roomNo = (TextView) convertView.findViewById(R.id.custom_class_schedule_room);
			holder.rate = (ImageView) convertView.findViewById(R.id.rate);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		ClassScheduleModel schedule = (ClassScheduleModel)mClassScheduleArrayList.get(position);
		String sprit=schedule.getIs_sprit_45();

		holder.rate.setVisibility(View.GONE);

		if(sprit.equalsIgnoreCase("1"))
		{ 
			holder.teacherName.setText(schedule.getSprit_45_teacher_name());
			holder.slotName.setText(schedule.getSprit_45_subject_name());
			holder.slotStartTime.setText(schedule.getSlot_start_time());
			holder.slotEndTime.setText(schedule.getSlot_end_time());
			imageLoader.displayImage(schedule.getTeacher_profile_photo(), holder.teacherProfileImage, options, new SimpleImageLoadingListener() {
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
			if(schedule.getClass_room()==null ||schedule.getClass_room().equalsIgnoreCase("")|| schedule.getClass_room().equalsIgnoreCase("null") )
			{
				holder.roomNo.setVisibility(View.GONE);

			}else
			{
				holder.roomNo.setVisibility(View.VISIBLE);
				holder.roomNo.setText(schedule.getClass_room());

			}
		}else{
			System.out.println("classschedule" + schedule.getSlot_start_time() + schedule.getSlot_end_time());

			holder.teacherName.setText(schedule.getFullname());
			holder.slotName.setText(schedule.getSubject_name());
			holder.slotStartTime.setText(schedule.getSlot_start_time());
			holder.slotEndTime.setText(schedule.getSlot_end_time());
			imageLoader.displayImage(schedule.getTeacher_profile_photo(), holder.teacherProfileImage, options, new SimpleImageLoadingListener() {
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
			if(schedule.getClass_room()==null ||schedule.getClass_room().equalsIgnoreCase("")|| schedule.getClass_room().equalsIgnoreCase("null") )
			{
				holder.roomNo.setVisibility(View.GONE);

			}else
			{
				holder.roomNo.setVisibility(View.VISIBLE);
				holder.roomNo.setText(schedule.getClass_room());

			}
		}

		if(position%4==0)
		{
			convertView.setBackgroundResource(R.color.cs_yellow);
		}else if(position%4==1)
		{
			convertView.setBackgroundResource(R.color.cs_red);

		}
		else if(position%4==2)
		{
			convertView.setBackgroundResource(R.color.cs_blue);

		}
		else if(position%4==3)
		{
			convertView.setBackgroundResource(R.color.cs_grey);

		}

		return convertView;
	}

	class ViewHolder {
		TextView slotName,slotStartTime,slotEndTime,teacherName,roomNo;
		ImageView teacherProfileImage,rate;
	}
}
