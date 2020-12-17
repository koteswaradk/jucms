package com.nxgenminds.eduminds.ju.cms.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.models.StudentInternshipModel;


public class StudentWorkedAtAdapter extends BaseAdapter{

	private ArrayList<StudentInternshipModel> listData;
	private LayoutInflater layoutInflater;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private Context prova;

	public StudentWorkedAtAdapter(Context context,ArrayList<StudentInternshipModel> listData){
		this.listData = listData;
		imageLoader = ImageLoader.getInstance();
		prova = context;

		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.placeholder)
		.showImageForEmptyUri(R.drawable.placeholder) 
		.showImageOnFail(R.drawable.placeholder)
		.cacheInMemory(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();

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
		final ViewHolder holder;
		if (layoutInflater == null)
			layoutInflater = (LayoutInflater) prova.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(convertView == null){
			convertView = layoutInflater.inflate(R.layout.custom_student_workedat, null);
			holder = new ViewHolder();
			holder.companyLogo = (ImageView) convertView.findViewById(R.id.companyIcon);
			holder.companyName = (TextView) convertView.findViewById(R.id.companyName);
			holder.designation = (TextView) convertView.findViewById(R.id.designation);
			holder.department = (TextView) convertView.findViewById(R.id.department);
			holder.from = (TextView) convertView.findViewById(R.id.from);
			holder.to = (TextView) convertView.findViewById(R.id.to);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		final StudentInternshipModel companyItem = (StudentInternshipModel) listData.get(position);

		Typeface typeFace=Typeface.createFromAsset(prova.getAssets(),"fonts/BentonSans-Regular.otf");
		// addind Font 

		holder.companyName.setText(companyItem.getCompany_name());
		holder.designation.setText(companyItem.getDesignation());
		holder.department.setText(companyItem.getDepartment());
		holder.from.setText(companyItem.getFrom_date());
		holder.to.setText(companyItem.getTo_date());

		holder.companyName.setTypeface(typeFace);
		String imageUrl = companyItem.getCompany_logo_path();

		imageLoader.displayImage(imageUrl, holder.companyLogo, options, new SimpleImageLoadingListener() {
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
		ImageView companyLogo;
		TextView companyName;
		TextView designation;
		TextView department;
		TextView from;
		TextView to;
	}

}