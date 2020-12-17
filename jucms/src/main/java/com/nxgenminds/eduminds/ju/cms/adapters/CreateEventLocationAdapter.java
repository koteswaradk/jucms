package com.nxgenminds.eduminds.ju.cms.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.models.CreateEventLocationModel;


public class CreateEventLocationAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<CreateEventLocationModel> locationModel;

	public CreateEventLocationAdapter(Context context,
			ArrayList<CreateEventLocationModel> location) {
		this.context = context;
		this.locationModel = location;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return locationModel.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return locationModel.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		CreateEventLocationModel location = (CreateEventLocationModel ) locationModel.get(position);
		Typeface typeFace = Typeface.createFromAsset(context.getAssets(),
				"fonts/BentonSans-Thin.otf");
		 
		LocationHolder holder;
	     TextView text_location;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.create_event_location,parent,false);
			holder = new LocationHolder();
		    holder.text_location =  (TextView)convertView.findViewById(R.id.textView_createEvent_location);
			holder.text_location.setTypeface(typeFace);
			convertView.setTag(holder);
			
		}
		else
		{
			holder = (LocationHolder) convertView.getTag();
		}
		if(position == 0)
		{
		holder.text_location.setText("Select the location");
		}
		else{
			holder.text_location.setText(location.getCity());
		}
		
		return convertView;
	}
}
class LocationHolder
{
	TextView text_location;
}