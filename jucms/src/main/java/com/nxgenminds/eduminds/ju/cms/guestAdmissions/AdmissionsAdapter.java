package com.nxgenminds.eduminds.ju.cms.guestAdmissions;

import java.util.ArrayList;

import com.nxgenminds.eduminds.ju.cms.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;




public class AdmissionsAdapter extends BaseAdapter{

	private ArrayList<AdmissionsModel> listData;
	private LayoutInflater layoutInflater;
	
	private Context prova;
	


	public AdmissionsAdapter(Context context,ArrayList<AdmissionsModel> listData){
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
			convertView = layoutInflater.inflate(R.layout.custom_guest_acdamics, null);
			holder = new ViewHolder();
			holder.textTitle = (TextView) convertView.findViewById(R.id.acd_title_name);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		final AdmissionsModel broadcastItem = (AdmissionsModel) listData.get(position);



		holder.textTitle.setText(broadcastItem.getTitle());
		

	
		return convertView;
	}

	static class ViewHolder{
		TextView textTitle;
		
	}

}
