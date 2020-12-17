package com.nxgenminds.eduminds.ju.cms.FeedbackAdmin;

import java.util.ArrayList;

import com.nxgenminds.eduminds.ju.cms.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;



public class FeedBackListAdminAdapter extends BaseAdapter {
	
	private ArrayList<FeedBackListModel> mArrayListFeedBack;
    private Context mContext;
    
    
    public FeedBackListAdminAdapter(Context c,ArrayList<FeedBackListModel>  arrayList){
    	this.mContext = c;
    	this.mArrayListFeedBack = arrayList;
    }
	
    
    @Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mArrayListFeedBack.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mArrayListFeedBack.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		FeedBackListModel feedBackData = mArrayListFeedBack.get(position);
		ViewHolder holder;
		
		LayoutInflater inflater = LayoutInflater.from(mContext);
		if(convertView == null){
			convertView = inflater.inflate(R.layout.custom_view_feedback_admin_list,parent,false);
			holder =  new ViewHolder();
			holder.feedBackName = (TextView) convertView.findViewById(R.id.custom_view_feedback_admin_feedbackName);
			holder.startDate = (TextView) convertView.findViewById(R.id.custom_view_feedback_startdate);
			holder.startTime = (TextView) convertView.findViewById(R.id.custom_view_feedback_starttime);
			holder.endDate = (TextView) convertView.findViewById(R.id.custom_view_feedback_enddate);
			holder.endTime = (TextView) convertView.findViewById(R.id.custom_view_feedback_endtime);
			convertView.setTag(holder);
		} else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.feedBackName.setText(feedBackData.getEvent_name());
		holder.startDate.setText(feedBackData.getEvent_start_date());
	    holder.endDate.setText(feedBackData.getEvent_end_date());
	    
	    
	    if(feedBackData.getEvent_start_time()==null || feedBackData.getEvent_start_time().equalsIgnoreCase("null") || feedBackData.getEvent_start_time().equalsIgnoreCase("")){
		holder.startTime.setText("");
	    } else{
	    	holder.startTime.setText(feedBackData.getEvent_start_time());
	    }
	    
	    if(feedBackData.getEvent_end_time()==null || feedBackData.getEvent_end_time().equalsIgnoreCase("null") || feedBackData.getEvent_end_time().equalsIgnoreCase("") ){
			holder.endTime.setText("");
		    } else{
		    	holder.endTime.setText(feedBackData.getEvent_end_time());
		    }
	    
		return convertView;
	}
	
	
	class ViewHolder{
		
		TextView feedBackName,
		         startDate,startTime,
		         endDate,endTime;
	}

}
