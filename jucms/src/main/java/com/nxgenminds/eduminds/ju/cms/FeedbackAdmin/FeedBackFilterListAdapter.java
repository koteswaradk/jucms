package com.nxgenminds.eduminds.ju.cms.FeedbackAdmin;

import java.util.ArrayList;

import com.nxgenminds.eduminds.ju.cms.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;



public class FeedBackFilterListAdapter extends BaseAdapter {

	private ArrayList<FeedBackFilterModel> mArrayListFeedBackFilter;
    private Context mContext;
    
    
    public FeedBackFilterListAdapter(Context c,ArrayList<FeedBackFilterModel>  arrayList){
    	this.mContext = c;
    	this.mArrayListFeedBackFilter = arrayList;
    }
	
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mArrayListFeedBackFilter.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mArrayListFeedBackFilter.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		FeedBackFilterModel feedBackData = mArrayListFeedBackFilter.get(position);
		ViewHolder holder;
		
		LayoutInflater inflater = LayoutInflater.from(mContext);
		if(convertView == null){
			convertView = inflater.inflate(R.layout.custom_view_feedback_filter,parent,false);
			holder =  new ViewHolder();
			holder.feedBackStreamName = (TextView) convertView.findViewById(R.id.custom_view_feedback_filter_data);
			convertView.setTag(holder);
		} else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.feedBackStreamName.setText(feedBackData.getValue());
		
		
		return convertView;
	}
	
class ViewHolder{
		
		TextView feedBackStreamName;
		         
	}

}
