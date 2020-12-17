package com.nxgenminds.eduminds.ju.cms.classSchedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.models.WeekScheduleModel;


public class WeekClassScheduleAdapter extends BaseAdapter{
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat newformat = new SimpleDateFormat("dd/MM/yyyy");
	private ArrayList<WeekScheduleModel> mClassScheduleArrayList;
	private Context context;
	Date date;


	public WeekClassScheduleAdapter(Context c,ArrayList<WeekScheduleModel> scheduleList){
		mClassScheduleArrayList = scheduleList;
		this.context = c;
		imageLoader = ImageLoader.getInstance();
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
			convertView = inflater.inflate(R.layout.custom_week_schedule,parent,false);
			holder = new ViewHolder();
			holder.slotDay = (TextView)convertView.findViewById(R.id.custom_week_schedule_slot_day);
			holder.slotDate= (TextView) convertView.findViewById(R.id.custom_week_schedule_date);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		final WeekScheduleModel schedule = (WeekScheduleModel)mClassScheduleArrayList.get(position);
		holder.slotDay.setText(schedule.getDay());

		try {
			date = format.parse(schedule.getDate());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		holder.slotDate.setText(newformat.format(date));

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
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, ClassScheduleDetailActivity.class);
				intent.putExtra("Day",schedule.getDay());
				intent.putExtra("Date",schedule.getDate());
				context.startActivity(intent); 				
			}
		});

		return convertView;
	}

	class ViewHolder {
		TextView slotDay,slotDate;
	}
}
