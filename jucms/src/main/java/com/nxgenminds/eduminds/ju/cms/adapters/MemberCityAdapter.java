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
import com.nxgenminds.eduminds.ju.cms.models.CityModel;


public class MemberCityAdapter extends BaseAdapter{

	private ArrayList<CityModel> listData;
	private LayoutInflater layoutInflater;
	private Context prova;

	public MemberCityAdapter(Context context,ArrayList<CityModel> listData){
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
			convertView = layoutInflater.inflate(R.layout.custom_location, null);
			holder = new ViewHolder();
			holder.textViewLoc= (TextView) convertView.findViewById(R.id.customloc);
				convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		CityModel city_Item = (CityModel) listData.get(position);

		Typeface typeFace=Typeface.createFromAsset(prova.getAssets(),"fonts/BentonSans-Regular.otf");
		// addind Font 
		if(position == 0)
		{			
			holder.textViewLoc.setText("Select City");		
			holder.textViewLoc.setTypeface(typeFace);
		}
		else{		
			holder.textViewLoc.setText(city_Item.getCity_name());	
			holder.textViewLoc.setTypeface(typeFace);
		}
	
		
		return convertView;
	}

	static class ViewHolder{
	
		TextView textViewLoc;
	

	}

}

