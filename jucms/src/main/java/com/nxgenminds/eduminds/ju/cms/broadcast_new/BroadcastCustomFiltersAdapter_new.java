package com.nxgenminds.eduminds.ju.cms.broadcast_new;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nxgenminds.eduminds.ju.cms.R;



public class BroadcastCustomFiltersAdapter_new extends BaseAdapter{

	public List<BroadcastCustomFiltersModel_new > listData;
	private ArrayList<BroadcastCustomFiltersModel_new > arraylist;
	private LayoutInflater layoutInflater;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private Context prova;
	String[] sections;
	private boolean state;
	Activity contex_a;

	public BroadcastCustomFiltersAdapter_new(Activity contex_a,List<BroadcastCustomFiltersModel_new > listData, ArrayList<BroadcastCustomFiltersModel_new> result){
		this.listData = listData;
		this.arraylist = new ArrayList<BroadcastCustomFiltersModel_new >();
		this.arraylist.addAll(listData);
		//layoutInflater = LayoutInflater.from(context);
		imageLoader = ImageLoader.getInstance();
		this.contex_a = contex_a;
		prova=contex_a;

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
		final ViewHolder holder;
		BroadcastCustomFiltersModel_new  connectionsItem = (BroadcastCustomFiltersModel_new ) listData.get(position);
		LayoutInflater mInflater = (LayoutInflater) contex_a.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

		int count = BroadcastCustomFiltersListActivity_new.myList.size();
		if(count>0)
		{
			for(int i=0;i<count;i++)
			{

				if(BroadcastCustomFiltersListActivity_new.myList.get(i).trim().equalsIgnoreCase(connectionsItem.getName()))
				{
					connectionsItem.setSelected(true);
				}
			}
		}



		if(convertView == null){
			convertView = mInflater.inflate(R.layout.new_create_broadcast_add_members_custom, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.broad_name);
			holder.checkBox=(CheckBox)convertView.findViewById(R.id.broad_checkBox_i);
			holder.checkBox.setOnClickListener( new View.OnClickListener() { 
				public void onClick(View v) { 
					CheckBox cb = (CheckBox) v ; 
					BroadcastCustomFiltersModel_new  connItem = (BroadcastCustomFiltersModel_new ) cb.getTag();
					connItem.setSelected(cb.isChecked());

				} 
			}); 

			convertView.setTag(holder);		

		}else{
			holder = (ViewHolder) convertView.getTag();
		}

		// addind Font 
		Typeface typeFace=Typeface.createFromAsset(prova.getAssets(),"fonts/BentonSans-Regular.otf");
		holder.name.setText(connectionsItem.getValue());
		holder.name.setTypeface(typeFace);

		holder.checkBox.setChecked(connectionsItem.isSelected());
		holder.checkBox.setTag(connectionsItem);


		return convertView;
	} 

	static class ViewHolder{

		TextView name;
		CheckBox checkBox;

	}

	public boolean getCheckBoxSelectedState(){
		return state;
	}

	public void setSelectStateForAll(){
		if(state)
			state = false;
		else
			state = true;
		for(BroadcastCustomFiltersModel_new  r : listData)
			r.setSelected(state);
	}

	// Filter Class
	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		listData.clear();    
		if (charText.length() == 0) {
			listData.addAll(arraylist);
		} 
		else 
		{
			for (BroadcastCustomFiltersModel_new  wp : arraylist) 
			{
				if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) 
				{
					listData.add(wp);
				}
			}
		}
		notifyDataSetChanged();
	}



}
