package com.nxgenminds.eduminds.ju.cms.spirit;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nxgenminds.eduminds.ju.cms.R;



public class SpritListAdapter_new extends BaseAdapter{

	private ArrayList<SpritListModel_new> listData;
	private LayoutInflater layoutInflater;
	private ImageLoader imageLoader;
	private DisplayImageOptions options,dialog_options;
	private Context prova;
	private ProgressDialog pDialog;

	private TextView dialog_fullname,dialog_message_text;
	private ImageView dialog_user_image;
	private Button dialog_cancel;


	public SpritListAdapter_new(Context context,ArrayList<SpritListModel_new> listData){
		this.listData = listData;
		imageLoader = ImageLoader.getInstance();
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
			convertView = layoutInflater.inflate(R.layout.new_custom_view_sprit, null);
			holder = new ViewHolder();
			holder.sprit_teachername = (TextView) convertView.findViewById(R.id.sprit_teacher_name);
			holder.sprit_subjcet = (TextView) convertView.findViewById(R.id.sprit__subject);
			holder.stream= (TextView) convertView.findViewById(R.id.sprit_stream_name);
			holder.sprit_stardate = (TextView)convertView.findViewById(R.id.sprit_start_date);
			holder.sprint_enddate=(TextView)convertView.findViewById(R.id.sprit_end_date);
			holder.date=(TextView)convertView.findViewById(R.id.spirit_date);

			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		final SpritListModel_new sprit45Item = (SpritListModel_new) listData.get(position);

		holder.sprit_teachername.setText(sprit45Item.getSprit_45_teacher_name());
		holder.sprit_subjcet.setText(sprit45Item.getSprit_45_subject_name());
		holder.stream.setText(sprit45Item.getStream_name()+ ","+sprit45Item.getSemester()+","+sprit45Item.getSection_name());
		holder.sprit_stardate.setText(sprit45Item.getSlot_start_time());
		holder.sprint_enddate.setText(sprit45Item.getSlot_end_time());
		holder.date.setText(sprit45Item.getTimetable_date());
		return convertView;
	}

	static class ViewHolder{
		TextView sprit_teachername;
		TextView sprit_subjcet,stream;
		TextView sprit_stardate,sprint_enddate,date;

	}

}
