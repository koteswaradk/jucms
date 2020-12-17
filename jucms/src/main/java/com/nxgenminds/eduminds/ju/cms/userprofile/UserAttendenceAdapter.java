package com.nxgenminds.eduminds.ju.cms.userprofile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.models.UserAttendenceModel;


public class UserAttendenceAdapter extends BaseAdapter{

	private ArrayList<UserAttendenceModel> listData;
	private LayoutInflater layoutInflater;
	private Context prova;

	public UserAttendenceAdapter(Context context,ArrayList<UserAttendenceModel> listData){
		this.listData = listData;
		prova = context;
	}



	@Override
	public int getCount() {
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position,  View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (layoutInflater == null)
			layoutInflater = (LayoutInflater) prova.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(convertView == null){
			convertView = layoutInflater.inflate(R.layout.custom_user_attendence, null);
			holder = new ViewHolder();
			holder.subname = (TextView) convertView.findViewById(R.id.user_sub_name);
			holder.attend = (TextView) convertView.findViewById(R.id.user_attend);
			holder.taken= (TextView) convertView.findViewById(R.id.user_taken);
			holder.percentage= (Button) convertView.findViewById(R.id.user_attendence_per);

			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		final UserAttendenceModel attendItem = (UserAttendenceModel) listData.get(position);

		holder.subname.setText(attendItem.getSubject_name());
		holder.attend.setText("Class Attended : " +attendItem.getTotal_presents());
		holder.taken.setText("Class Taken     : " +attendItem.getTotal_classes());
		holder.percentage.setText(attendItem.getAttendence_percentage()+"%");
		//holder.percentage.setBackgroundColor(Color.parseColor(attendItem.getHex_colorcode()));

		GradientDrawable shape = (GradientDrawable) prova.getResources().getDrawable(R.drawable.circle_transparent);

		String str = attendItem.getRgb_colorcode();
		List<String> elephantList = Arrays.asList(str.split(","));
	/*	int r=Integer.parseInt(elephantList.get(0));
		int g=Integer.parseInt(elephantList.get(1));
		int b=Integer.parseInt(elephantList.get(2));

		//	int argb = 0xff00ff00;
		int color = Color.argb(255, r, g, b);
		shape.setColor(color);*/
		holder.percentage.setBackground(shape);
		return convertView;
	}

	static class ViewHolder{
		TextView subname;
		TextView attend;
		TextView taken;
		Button percentage;
	}



}
