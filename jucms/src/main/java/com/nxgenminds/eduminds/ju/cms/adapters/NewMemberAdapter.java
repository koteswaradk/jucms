package com.nxgenminds.eduminds.ju.cms.adapters;

import com.nxgenminds.eduminds.ju.cms.R;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;



public class NewMemberAdapter extends BaseAdapter {
	private Context context;
	private String[] item;
	private String[] type;
	
	
	public NewMemberAdapter(Context context, String[]  listItem,String[] type)
	{
		this.context = context;
		this.item = listItem;
		this.type = type;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return (item.length)-1;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return item;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return  position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TextView txt_bloodGroup;
		
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View itemView = inflater.inflate(R.layout.newmember_bloodgroup, parent,false);
		
		if (position == (getCount()))
		{
			Typeface typeFace=Typeface.createFromAsset(context.getAssets(),"fonts/BentonSans-Thin.otf");
			txt_bloodGroup = (TextView)itemView.findViewById(R.id.view_newMemberBG);
			txt_bloodGroup.setTypeface(typeFace);
			txt_bloodGroup.setHint(item[position]);
			txt_bloodGroup.setGravity(Gravity.CENTER_VERTICAL);
			
		}
		else
		{ 
			if(position % 2 == 0)
			{
				txt_bloodGroup = (TextView)itemView.findViewById(R.id.view_newMemberBG);
				String set = item[position]+"<sup>"+type[0]+"</sup>";
				txt_bloodGroup.setText(Html.fromHtml(set));
				
			}
			else
			{
				txt_bloodGroup = (TextView)itemView.findViewById(R.id.view_newMemberBG);
				String set = item[position]+"<sup>"+type[1]+"</sup>";
				txt_bloodGroup.setText(Html.fromHtml(set));
				
			}
		}
		 return itemView;
	}

}
