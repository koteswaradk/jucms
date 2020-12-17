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
import com.nxgenminds.eduminds.ju.cms.models.FeedLecturerModel;

public class FeedLecturerAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<FeedLecturerModel> lecturer;

	public FeedLecturerAdapter (Context context,ArrayList<FeedLecturerModel> lecturer) 
	{
		this.context = context;
		this.lecturer = lecturer;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lecturer.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return lecturer.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		FeedLecturerModel lecturerModel = (FeedLecturerModel) lecturer.get(position);
		Typeface typeFace = Typeface.createFromAsset(context.getAssets(),
				"fonts/BentonSans-Regular.otf");

		LocationHolderNew holder;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.userprofile_adapter_layout,parent, false);
			holder = new LocationHolderNew();
			holder.text_subjects =  (TextView)convertView.findViewById(R.id.textView_userprofileUpdate_Adpater);
			holder.text_subjects.setTypeface(typeFace);
			convertView.setTag(holder);

		}
		else
		{
			holder = (LocationHolderNew) convertView.getTag();
		}
		holder.text_subjects.setTypeface(typeFace);
		holder.text_subjects.setText(lecturerModel.getFirstname());

		
		return convertView;
	}

	class LocationHolderNew
	{
		TextView text_subjects;
	}

}
