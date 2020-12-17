package com.nxgenminds.eduminds.ju.cms.classSchedule;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.models.ClassScheduleModel;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpPostClient;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class TodayClassScheduleAdapter extends BaseAdapter{
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private ArrayList<ClassScheduleModel> mClassScheduleArrayList;
	private Context context;
	private ProgressDialog mPDialog;
	RadioButton good,average,bore,ugly;
	RadioGroup  select; 


	public TodayClassScheduleAdapter(Context c,ArrayList<ClassScheduleModel> scheduleList){
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
		final ViewHolder holder;
		if(convertView == null){
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.custom_class_schedule,parent,false);
			holder = new ViewHolder();
			holder.slotName = (TextView) convertView.findViewById(R.id.custom_class_schedule_slot_name);
			holder.slotStartTime = (TextView)convertView.findViewById(R.id.custom_class_schedule_slot_start_time);
			holder.slotEndTime =(TextView)convertView.findViewById(R.id.custom_class_schedule_slot_end_time);
			holder.teacherName = (TextView) convertView.findViewById(R.id.custom_class_schedule_teacher_name);
			holder.teacherProfileImage = (ImageView) convertView.findViewById(R.id.custom_class_schedule_teacher_profile_image);
			holder.rate = (ImageView) convertView.findViewById(R.id.rate);
			holder.roomNo = (TextView) convertView.findViewById(R.id.custom_class_schedule_room);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		final ClassScheduleModel schedule = (ClassScheduleModel)mClassScheduleArrayList.get(position);
		String sprit=schedule.getIs_sprit_45();
		System.out.println(sprit);


		if(Util.ROLE.equalsIgnoreCase("student") || Util.ROLE.equalsIgnoreCase("class monitor"))
		{
			if(sprit.equalsIgnoreCase("1"))
			{ 
				System.out.println("yes");

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
			}else{
				//System.out.println("classschedule" + holder.slotStartTime + holder.slotEndTime);

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
			}

			if(schedule.getClass_room()==null ||schedule.getClass_room().equalsIgnoreCase("")|| schedule.getClass_room().equalsIgnoreCase("null") )
			{
				holder.roomNo.setVisibility(View.GONE);

			}else
			{
				holder.roomNo.setVisibility(View.VISIBLE);
				holder.roomNo.setText(schedule.getClass_room());

			}
			// rate visibility
			if(schedule.getSession_rating_id_flag().equalsIgnoreCase("1"))
			{
				holder.rate.setVisibility(View.VISIBLE);
				holder.rate.setImageResource(R.drawable.ic_rate_star);
			}
			else
			{
				holder.rate.setVisibility(View.GONE);
			}

		}
		else if(Util.ROLE.equalsIgnoreCase("teacher"))
		{

			holder.teacherName.setText(schedule.getStream_name() +","+schedule.getSemester() +","+schedule.getSection_name());
			holder.slotName.setText(schedule.getSubject_name());
			holder.slotStartTime.setText(schedule.getSlot_start_time());
			holder.slotEndTime.setText(schedule.getSlot_end_time());
			holder.teacherProfileImage.setVisibility(View.GONE);
			if(schedule.getClass_room()==null ||schedule.getClass_room().equalsIgnoreCase("")|| schedule.getClass_room().equalsIgnoreCase("null") )
			{
				holder.roomNo.setVisibility(View.GONE);

			}else
			{
				holder.roomNo.setVisibility(View.VISIBLE);
				holder.roomNo.setText(schedule.getClass_room());

			}

			// rate
			holder.rate.setVisibility(View.GONE);
		}

		// submit the ratings
		holder.rate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final Dialog dialog = new Dialog(context);  
				dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);  
				dialog.setContentView(R.layout.dialog_submit_rating);  
				dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
				dialog.setTitle("Rate");
				dialog.show(); 
				select=(RadioGroup)dialog.findViewById(R.id.select_ratings);
				good =(RadioButton)dialog.findViewById(R.id.rate_good);
				good.setChecked(false);

				average =(RadioButton)dialog.findViewById(R.id.rate_average);
				average.setChecked(false);

				bore =(RadioButton)dialog.findViewById(R.id.rate_boring);
				bore.setChecked(false);

				ugly =(RadioButton)dialog.findViewById(R.id.rate_ugly);
				ugly.setChecked(false);

				good.setOnClickListener(new Button.OnClickListener(){

					@Override
					public void onClick(View v) {
						dialog.dismiss();  
						//	Toast.makeText(context, "Thank You  For Rating", Toast.LENGTH_SHORT).show();
						System.out.println("GOOD");
						System.out.println(schedule.getTimetable_id());
						new SubmitRating().execute("good",schedule.getTimetable_id());
						schedule.setSession_rating_id_flag("0");
						holder.rate.setVisibility(View.GONE);
					}});

				average.setOnClickListener(new Button.OnClickListener(){

					@Override
					public void onClick(View v) {
						dialog.dismiss();  
						//Toast.makeText(context, "Thank You  For Rating", Toast.LENGTH_SHORT).show();
						System.out.println("AVERAGE");

						System.out.println(schedule.getTimetable_id());
						new SubmitRating().execute("average",schedule.getTimetable_id());
						schedule.setSession_rating_id_flag("0");
						holder.rate.setVisibility(View.GONE);

					}});

				bore.setOnClickListener(new Button.OnClickListener(){

					@Override
					public void onClick(View v) {
						dialog.dismiss();  
						//Toast.makeText(context, "Thank You  For Rating", Toast.LENGTH_SHORT).show();
						System.out.println("BORING");

						System.out.println(schedule.getTimetable_id());
						new SubmitRating().execute("boring",schedule.getTimetable_id());
						schedule.setSession_rating_id_flag("0");
						holder.rate.setVisibility(View.GONE);

					}});

				ugly.setOnClickListener(new Button.OnClickListener(){

					@Override
					public void onClick(View v) {
						dialog.dismiss();  
						//Toast.makeText(context, "Thank You  For Rating", Toast.LENGTH_SHORT).show();
						System.out.println("UGLY");

						System.out.println(schedule.getTimetable_id());
						new SubmitRating().execute("ugly",schedule.getTimetable_id());
						schedule.setSession_rating_id_flag("0");
						holder.rate.setVisibility(View.GONE);

					}});

			}
		});
		//end
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

	class SubmitRating extends AsyncTask<String,Void,JSONObject>{
		@Override
		protected void onPreExecute(){
			if(mPDialog==null){
				mPDialog = Util.createProgressDialog(context);
				mPDialog.show();
			} else {
				mPDialog.show();
			}
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			String student_rating = params[0];
			String timetable_id = params[1];
			JSONObject receivedJSONResponse = null;
			JSONObject rateObj = new JSONObject();
			try {
				rateObj.put("student_rating",student_rating);	
				rateObj.put("timetable_id",timetable_id);
				rateObj.put("student_id",Util.USER_ID);

				receivedJSONResponse = HttpPostClient.sendHttpPost(Util.API+"session_rating", rateObj);
				System.out.println("URL__________________________"+Util.API+"session_rating");
				System.out.println("student_rating--------------"+student_rating);
				System.out.println("timetable_id------------------"+timetable_id);
				System.out.println("Util.USER_ID--------------------"+Util.USER_ID);

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return receivedJSONResponse;
		}

		@Override
		protected void onPostExecute(JSONObject result){
			super.onPostExecute(result);
			mPDialog.dismiss();
			if(result!=null){
				try {
					if(result.getString("status").equalsIgnoreCase("1")){
						Toast.makeText(context, result.getString("message"),Toast.LENGTH_LONG).show();	

					} else {
						Toast.makeText(context, result.getString("message"),Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
