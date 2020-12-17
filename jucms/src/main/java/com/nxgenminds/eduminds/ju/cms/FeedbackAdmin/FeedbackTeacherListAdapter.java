package com.nxgenminds.eduminds.ju.cms.FeedbackAdmin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.nxgenminds.eduminds.ju.cms.thirdpartyprofile.ThirdPartyTabMenuActivity;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class FeedbackTeacherListAdapter extends BaseAdapter{
	
	private ArrayList<FeedBackTeacherModel> mArrayListFeedBackTeacher;
    private Context mContext;
    
    private ImageLoader imageLoader;
	private DisplayImageOptions options;
	
    
    
    public FeedbackTeacherListAdapter(Context c,ArrayList<FeedBackTeacherModel>  arrayList){
    	this.mContext = c;
    	this.mArrayListFeedBackTeacher = arrayList;
    	
    	imageLoader = ImageLoader.getInstance();
    	
    	options = new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.showImageOnLoading(R.drawable.placeholder)
		.showImageForEmptyUri(R.drawable.placeholder) 
		.showImageOnFail(R.drawable.placeholder)
		.build();

    }

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mArrayListFeedBackTeacher.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mArrayListFeedBackTeacher.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		final FeedBackTeacherModel teacherData = mArrayListFeedBackTeacher.get(position);
		ViewHolder holder;
		
		LayoutInflater inflater = LayoutInflater.from(mContext);
		if(convertView == null){
			convertView = inflater.inflate(R.layout.custom_view_feedback_teacher_list,parent,false);
			holder =  new ViewHolder();
			
			holder.teacherImage =  (ImageView) convertView.findViewById(R.id.custom_view_feedback_teacher_profile);
			holder.teacherName = (TextView) convertView.findViewById(R.id.custom_view_feedback_teacher_name);
			holder.subjectName = (TextView) convertView.findViewById(R.id.custom_view_feedback_teacher_subject_name);
			holder.streamName = (TextView) convertView.findViewById(R.id.custom_view_feedback_teacher_stream_name);
			holder.teacherPercent = (Button) convertView.findViewById(R.id.custom_view_feedback_teacher_subject_percent);
			
			convertView.setTag(holder);
		} else{
			holder = (ViewHolder) convertView.getTag();
		}
		
	   
		holder.teacherName.setText(teacherData.getFullname());
		holder.subjectName.setText(teacherData.getSubject_name());
		holder.streamName.setText(teacherData.getStream_name()+","+teacherData.getSemester()+","+teacherData.getSection_name());
		
		double percentage = Double.parseDouble(teacherData.getAnswer_percentage());
		long percent = Math.round(percentage);
		
		holder.teacherPercent.setText(String.valueOf(percent)+"%");
		GradientDrawable shape = (GradientDrawable) mContext.getResources().getDrawable(R.drawable.circle_transparent);

		String str = teacherData.getRgb();
		List<String> elephantList = Arrays.asList(str.split(","));
		//colors codes
		/*int r=Integer.parseInt(elephantList.get(0));
		int g=Integer.parseInt(elephantList.get(1));
		int b=Integer.parseInt(elephantList.get(2));
		int color = Color.argb(255, r, g, b);
		shape.setColor(color);*/
		
		holder.teacherPercent.setBackground(shape);
	
		imageLoader.displayImage(teacherData.getProfile_photo(), holder.teacherImage, options, new SimpleImageLoadingListener() {
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
	
		holder.teacherImage.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,ThirdPartyTabMenuActivity.class);
				intent.putExtra("UserID", teacherData.getUser_id());
				intent.putExtra("ThirdPartyRole","teacher" );
				Util.THIRD_PARTY_NAME  = teacherData.getFirstname();
				Util.THIRD_PARTY_ID= teacherData.getUser_id();
				mContext.startActivity(intent);
				Util.intership_flag=true;
				
			}
			
		});
		
        return convertView;
		
	}
	
	class ViewHolder{
		
	TextView teacherName,subjectName,streamName;
	Button teacherPercent;
	ImageView teacherImage;
	
	}
	
	

}
