package com.nxgenminds.eduminds.ju.cms.guestAcademics;

import java.util.ArrayList;

import com.nxgenminds.eduminds.ju.cms.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;




public class AcademicsAdapter extends BaseAdapter{

	private ArrayList<AcademicsModel> listData;
	private LayoutInflater layoutInflater;
	
	private Context prova;
	


	public AcademicsAdapter(Context context,ArrayList<AcademicsModel> listData){
		this.listData = listData;
		//layoutInflater = LayoutInflater.from(context);
		
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
			convertView = layoutInflater.inflate(R.layout.custom_guest_admissions, null);
			holder = new ViewHolder();
			holder.textTitle = (TextView) convertView.findViewById(R.id.admiss_title_name);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		final AcademicsModel broadcastItem = (AcademicsModel) listData.get(position);



		holder.textTitle.setText(broadcastItem.getTitle());
		

	
		return convertView;
	}

	static class ViewHolder{
		TextView textTitle;
		
	}

}
