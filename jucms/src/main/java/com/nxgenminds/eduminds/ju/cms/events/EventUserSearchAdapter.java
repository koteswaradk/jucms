package com.nxgenminds.eduminds.ju.cms.events;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
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




public class EventUserSearchAdapter extends BaseAdapter{

	private ArrayList<EventUserSearchModel> arraylist;
	
	private LayoutInflater layoutInflater;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private Context prova;
	
	String[] sections;
	private boolean state;
	

	public EventUserSearchAdapter(Context contex_a,ArrayList<EventUserSearchModel> result){
		
		this.arraylist = result;
		
		
		imageLoader = ImageLoader.getInstance();
		
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
		return arraylist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return arraylist.get(position);
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
		final EventUserSearchModel eventSearchItem = (EventUserSearchModel) arraylist.get(position);
		LayoutInflater mInflater = (LayoutInflater) prova.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
           
		if(convertView == null){
		    convertView = mInflater.inflate(R.layout.create_broadcast_add_members_custom,parent,false);
			holder = new ViewHolder();
		    holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.checkBox=(CheckBox)convertView.findViewById(R.id.checkBox_i);
			holder.checkBox.setTag(eventSearchItem);
			convertView.setTag(holder);		
		} else{
			holder =(ViewHolder)convertView.getTag();
			//holder.checkBox.setTag(eventSearchItem);
		}
		
				
		// addind Font
		
		
		
		holder.name.setText(eventSearchItem.getFirstname());
		holder.checkBox.setChecked(eventSearchItem.isSelected());	
         
		holder.checkBox.setOnClickListener( new View.OnClickListener() { 
			public void onClick(View v) { 
				 if(holder.checkBox.isChecked()){
					 eventSearchItem.setSelected(true);
				
				 } else{
	            	 eventSearchItem.setSelected(false);
	            	 
	            	 if(EventUserSearchActivity.mArrayListSelectedUsers.size()>0){
	           			 
	           		 		for(int j=0;j<EventUserSearchActivity.mArrayListSelectedUsers.size();j++){
	           			    
	           		 			EventSelectedUsersModel selectedModel = EventUserSearchActivity.mArrayListSelectedUsers.get(j);
	           		 			if(eventSearchItem.getUser_id().equalsIgnoreCase(selectedModel.getUserId())){
	           		 	                   EventUserSearchActivity.mArrayListSelectedUsers.remove(j);
	           		 			}
	           		 		}
	            	 }	
	            	 
				 }
			} 
		}); 
		
        
		String imageUrl = eventSearchItem.getProfile_photo();
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
		for(EventUserSearchModel r : arraylist){
			//r.setSelected(state);
	}
	}
	// Filter Class
	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		arraylist.clear();    
		if (charText.length() == 0) {
			arraylist.addAll(arraylist);
		} 
		else 
		{
			for (EventUserSearchModel wp : arraylist) 
			{
				if (wp.getFirstname().toLowerCase(Locale.getDefault()).contains(charText)) 
				{
					arraylist.add(wp);
				}
			}
		}
		notifyDataSetChanged();
	}



}
