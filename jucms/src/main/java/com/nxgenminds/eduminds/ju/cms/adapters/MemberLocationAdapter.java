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
import com.nxgenminds.eduminds.ju.cms.models.MemberLocationModel;


public class MemberLocationAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<MemberLocationModel> locationModel;

	public MemberLocationAdapter(Context context,ArrayList<MemberLocationModel> location) 
	{
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

		MemberLocationModel location = (MemberLocationModel ) locationModel.get(position);
		Typeface typeFace = Typeface.createFromAsset(context.getAssets(),
				"fonts/BentonSans-Regular.otf");

		LocationHolderNew holder;
		TextView text_location;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.userprofile_adapter_layout,parent, false);
			holder = new LocationHolderNew();
			holder.text_location =  (TextView)convertView.findViewById(R.id.textView_userprofileUpdate_Adpater);
			holder.text_location.setTypeface(typeFace);
			convertView.setTag(holder);

		}
		else
		{
			holder = (LocationHolderNew) convertView.getTag();
		}
		holder.text_location.setTypeface(typeFace);
		holder.text_location.setText(location.getCity());

		System.out.println(location.getCity());
		return convertView;
	}
}
class LocationHolderNew
{
	TextView text_location;
}