package com.nxgenminds.eduminds.ju.cms.classSchedule;

import java.util.ArrayList;

import com.nxgenminds.eduminds.ju.cms.R;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;



public class SemesterAdapter extends BaseAdapter{

	private ArrayList<Semester> listData;
	private LayoutInflater layoutInflater;
	private Context prova;

	public SemesterAdapter(Context context,ArrayList<Semester> listData){
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
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (layoutInflater == null)
			layoutInflater = (LayoutInflater) prova.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(convertView == null){
			convertView = layoutInflater.inflate(R.layout.custom_simple_list, null);
			holder = new ViewHolder();
			holder.list_name = (TextView) convertView.findViewById(R.id.custom_simple_list);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		Semester semItem = (Semester) listData.get(position);
		
		Typeface typeFace=Typeface.createFromAsset(prova.getAssets(),"fonts/BentonSans-Regular.otf");
		holder.list_name.setText(semItem.getSemester());
		return convertView;
	}

	static class ViewHolder{
		TextView list_name;

	}

}