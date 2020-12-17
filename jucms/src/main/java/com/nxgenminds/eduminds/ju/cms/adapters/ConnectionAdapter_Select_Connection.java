package com.nxgenminds.eduminds.ju.cms.adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.Select_Connections;
import com.nxgenminds.eduminds.ju.cms.models.ConnectionsModel;


public class ConnectionAdapter_Select_Connection extends BaseAdapter{

	public List<ConnectionsModel> listData;
	private ArrayList<ConnectionsModel> arraylist;
	private LayoutInflater layoutInflater;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private Context prova;
	String[] sections;
	private boolean state;
	Activity contex_a;

	public ConnectionAdapter_Select_Connection(Activity contex_a,List<ConnectionsModel> listData){
		this.listData = listData;
		this.arraylist = new ArrayList<ConnectionsModel>();
		this.arraylist.addAll(listData);
		//layoutInflater = LayoutInflater.from(context);
		imageLoader = ImageLoader.getInstance();
		this.contex_a = contex_a;
		prova=contex_a;

		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.placeholder)
		.showImageForEmptyUri(R.drawable.placeholder) 
		.showImageOnFail(R.drawable.placeholder)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();

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
		ConnectionsModel connectionsItem = (ConnectionsModel) listData.get(position);
		LayoutInflater mInflater = (LayoutInflater) contex_a.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

		int count = Select_Connections.myList.size();
		if(count>0)
		{
			for(int i=0;i<count;i++)
			{

				if(Select_Connections.myList.get(i).trim().equalsIgnoreCase(connectionsItem.getUser_id()))
				{
					connectionsItem.setSelected(true);
				}
			}
		}



		if(convertView == null){
			convertView = mInflater.inflate(R.layout.create_broadcast_add_members_custom, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.checkBox=(CheckBox)convertView.findViewById(R.id.checkBox_i);
			holder.checkBox.setOnClickListener( new View.OnClickListener() { 
				public void onClick(View v) { 
					CheckBox cb = (CheckBox) v ; 
					ConnectionsModel connItem = (ConnectionsModel) cb.getTag();
					connItem.setSelected(cb.isChecked());
			/*		if(cb.isChecked())
					{
						connItem.setSelected(true);
					}
					else 
					{
						connItem.setSelected(false); 
						Select_Connections.myList.remove(cb.getTag());
					}*/
				} 
			}); 
	
			convertView.setTag(holder);		
			
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		// addind Font 
		Typeface typeFace=Typeface.createFromAsset(prova.getAssets(),"fonts/BentonSans-Regular.otf");
		holder.name.setText(connectionsItem.getFirstname());
		holder.name.setTypeface(typeFace);

		holder.checkBox.setChecked(connectionsItem.isSelected());
		holder.checkBox.setTag(connectionsItem);
		String imageUrl = connectionsItem.getUser_profile_photo();

		imageLoader.displayImage(imageUrl, holder.image, options, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
			}

			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

			}
		}, new ImageLoadingProgressListener() {
			@Override
			public void onProgressUpdate(String imageUri, View view, int current,
					int total) {

			}
		}
				);


		return convertView;
	} 

	static class ViewHolder{
		ImageView image;
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
		for(ConnectionsModel r : listData)
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
			for (ConnectionsModel wp : arraylist) 
			{
				if (wp.getFirstname().toLowerCase(Locale.getDefault()).contains(charText)) 
				{
					listData.add(wp);
				}
			}
		}
		notifyDataSetChanged();
	}



}
