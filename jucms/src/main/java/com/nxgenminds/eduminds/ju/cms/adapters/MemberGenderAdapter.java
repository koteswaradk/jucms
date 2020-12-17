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
import com.nxgenminds.eduminds.ju.cms.models.MemberGenderModel;


public class MemberGenderAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<MemberGenderModel> arrayList_gender;

	public MemberGenderAdapter(Context c,ArrayList<MemberGenderModel> arrayList_gender)
	{
		this.context =c;
		this.arrayList_gender = arrayList_gender;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrayList_gender.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return arrayList_gender.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Typeface typeFace = Typeface.createFromAsset(context.getAssets(),
				"fonts/BentonSans-Regular.otf");
		MemberGenderModel genderData = (MemberGenderModel) arrayList_gender.get(position);
		GenderHolder holder;
		TextView text_location;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.userprofile_adapter_layout,parent, false);
			holder = new GenderHolder();
			holder.text_gender =  (TextView)convertView.findViewById(R.id.textView_userprofileUpdate_Adpater);
			convertView.setTag(holder);

		}
		else
		{
			holder = (GenderHolder) convertView.getTag();
		}
		holder.text_gender.setTypeface(typeFace);
		holder.text_gender.setText(genderData.getGenderName());
		return convertView;
	}
}
class GenderHolder
{
	TextView text_gender;
}




