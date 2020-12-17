package com.nxgenminds.eduminds.ju.cms.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.models.TopicsModel;


public class TopicsAdapter extends BaseAdapter{

	private ArrayList<TopicsModel> listData;
	private LayoutInflater layoutInflater;
	private Context prova;

	public TopicsAdapter(Context context,ArrayList<TopicsModel> listData){
		this.listData = listData;
		prova = context;
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
			convertView = layoutInflater.inflate(R.layout.custom_topics_listview, null);
			holder = new ViewHolder();
			holder.topicButtonCount = (Button) convertView.findViewById(R.id.questiosCount);
			holder.textViewName = (TextView) convertView.findViewById(R.id.topicName);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		TopicsModel topicsItem = (TopicsModel) listData.get(position);

		holder.topicButtonCount.setText(topicsItem.getForum_topics_count());
		int i = position%4;
		if(i == 0){
			holder.topicButtonCount.setBackgroundResource(R.drawable.circle_red);
		}
		if(i == 1){
			holder.topicButtonCount.setBackgroundResource(R.drawable.circle_green);
		}
		if(i == 2){
			holder.topicButtonCount.setBackgroundResource(R.drawable.circle_yellow);
		}
		if(i == 3){
			holder.topicButtonCount.setBackgroundResource(R.drawable.circle_gray);
		}
		holder.textViewName.setText(topicsItem.getForum_category_name());

		return convertView;
	}

	static class ViewHolder{

		Button topicButtonCount;
		TextView textViewName;

	}

}
