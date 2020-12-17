package com.nxgenminds.eduminds.ju.cms.events;

import java.util.ArrayList;

import com.nxgenminds.eduminds.ju.cms.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;



public class EventTypesAdapter extends BaseAdapter{

	private ArrayList<EventTypesModel> listData;
	private LayoutInflater layoutInflater;
	private Context prova;

	public EventTypesAdapter(Context context,ArrayList<EventTypesModel> listData){
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
			convertView = layoutInflater.inflate(R.layout.custom_event_types, null);
			holder = new ViewHolder();
			holder.textViewtype= (TextView) convertView.findViewById(R.id.customEventType);
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder) convertView.getTag();
		}
		EventTypesModel gender_Item = (EventTypesModel) listData.get(position);
		
		 

		if(gender_Item.getEvent_type_name().equalsIgnoreCase("Select Event Type")){
			holder.textViewtype.setTextColor(Color.rgb(128, 128, 128));
		}
		holder.textViewtype.setText(gender_Item.getEvent_type_name());	
		
		return convertView;
	}	

	static class ViewHolder{
		TextView textViewtype;
	}
}

