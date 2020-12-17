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
import com.nxgenminds.eduminds.ju.cms.models.StudentSubjects;


public class SubjectsAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<StudentSubjects> subjects;

	public SubjectsAdapter (Context context,ArrayList<StudentSubjects> subjects) 
	{
		this.context = context;
		this.subjects = subjects;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return subjects.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return subjects.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		StudentSubjects subjectsModel = (StudentSubjects ) subjects.get(position);
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
		holder.text_subjects.setText(subjectsModel.getSubject_name());

		System.out.println(subjectsModel.getSubject_name());
		return convertView;
	}

	class LocationHolderNew
	{
		TextView text_subjects;
	}

}
