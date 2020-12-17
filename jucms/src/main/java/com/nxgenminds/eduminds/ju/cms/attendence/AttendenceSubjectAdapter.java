package com.nxgenminds.eduminds.ju.cms.attendence;

import java.util.ArrayList;

import com.nxgenminds.eduminds.ju.cms.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;



public class AttendenceSubjectAdapter extends BaseAdapter{

	private Context context;
	private LayoutInflater layoutInflater;
	private ArrayList<AttendenceSubjectModel> listData;
	public AttendenceSubjectAdapter(Context context , ArrayList<AttendenceSubjectModel> listData) {
		// TODO Auto-generated constructor stub
		this.listData = listData;
		this.context = context;
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
			layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(convertView == null){
			convertView = layoutInflater.inflate(R.layout.custom_attendence_subjects, null);
			holder = new ViewHolder();
			holder.tvSubjectName = (TextView) convertView.findViewById(R.id.subjectName);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		AttendenceSubjectModel topicsItem = (AttendenceSubjectModel) listData.get(position);

		holder.tvSubjectName.setText(topicsItem.getStream_name()+", " + topicsItem.getSemester() +
				", " + topicsItem.getSection_name() + "-" + topicsItem.getSubject_name());

		return convertView;
	}

	static class ViewHolder{

		TextView tvSubjectName;

	}

}
