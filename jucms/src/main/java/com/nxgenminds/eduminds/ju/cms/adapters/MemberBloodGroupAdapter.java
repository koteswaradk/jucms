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
import com.nxgenminds.eduminds.ju.cms.models.MemberBloodGroupModel;


public class MemberBloodGroupAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<MemberBloodGroupModel> arrayList_bloodGroup;

	public MemberBloodGroupAdapter(Context context,
			ArrayList<MemberBloodGroupModel> bloodGroup) {
		this.context = context;
		this.arrayList_bloodGroup = bloodGroup;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrayList_bloodGroup.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return arrayList_bloodGroup.get(position);
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

		Holder holder;
		if (convertView == null) {
			holder = new Holder();
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.userprofile_adapter_layout,
					parent, false);
			holder.textViewBloodGroup = (TextView) convertView
					.findViewById(R.id.textView_userprofileUpdate_Adpater);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		MemberBloodGroupModel bg = (MemberBloodGroupModel) arrayList_bloodGroup
				.get(position);
		holder.textViewBloodGroup.setTypeface(typeFace);
		holder.textViewBloodGroup.setText(bg.getBloodGroup());
		return convertView;
	}

	class Holder {
		TextView textViewBloodGroup;
	}
}
