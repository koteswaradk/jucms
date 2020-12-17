package com.nxgenminds.eduminds.ju.cms.contactFetcher;

import java.util.List;

import com.nxgenminds.eduminds.ju.cms.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;



//An adapter to display the data in list view.
public class CustomListViewAdapter extends BaseAdapter {

	Activity context;
	private List<RowItem> rowItems;
	String[] sections;
	private boolean state;
	private Context prova;

	public CustomListViewAdapter(Activity context, int resourceId, List<RowItem> items) {
		this.rowItems = items;
		this.context = context;
		prova = context;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		RowItem rowItem = rowItems.get(position);

		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.contact_fecther_list_row, null);
			holder = new ViewHolder();
			holder.txtNumber = (TextView) convertView.findViewById(R.id.number);
			holder.txtName = (TextView) convertView.findViewById(R.id.name);
			holder.txtEmail = (TextView) convertView.findViewById(R.id.email);

			holder.chkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
			holder.chkBox.setOnClickListener( new View.OnClickListener() { 
				public void onClick(View v) { 
					CheckBox cb = (CheckBox) v ; 
					RowItem rowItem = (RowItem) cb.getTag(); 
					//					Toast.makeText(context, "Clicked on Checkbox: " + cb.getText() + " is " + cb.isChecked(), Toast.LENGTH_LONG).show();
					rowItem.setSelected(cb.isChecked());
					boolean selected = false;
					for(int i = 0 ; i < getCount() ; i++)
						if(((RowItem) getItem(i)).isSelected()){
							selected = true;
							break;
						}
					if(selected)
						((Contact_Fetcher_MainActivity)context).menuInstance.getItem(0).setVisible(true);
					else
						((Contact_Fetcher_MainActivity)context).menuInstance.getItem(0).setVisible(false);
				} 
			}); 
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();
		Typeface typeFace=Typeface.createFromAsset(prova.getAssets(),"fonts/BentonSans-Regular.otf");
		holder.txtName.setText(rowItem.getContactName());
		holder.txtName.setTypeface(typeFace);
		if(rowItem.getNumber().size() == 0)
		{
			holder.txtNumber.setText("Phone number missing");
			holder.txtNumber.setTypeface(typeFace);
		}
		else{
			holder.txtNumber.setText(rowItem.getNumber().get(0).number);
			holder.txtNumber.setTypeface(typeFace);
		}

		if(rowItem.getEmails().size() == 0)
		{
			holder.txtEmail.setText("email missing");
			holder.txtEmail.setTypeface(typeFace);
		}
		else
		{
			holder.txtEmail.setText(rowItem.getEmails().get(0).email);
			holder.txtEmail.setTypeface(typeFace);
		}

		holder.chkBox.setChecked(rowItem.isSelected());



		//Log.i("tag", "email " + holder.txtEmail.getText());
		holder.chkBox.setTag(rowItem);
		return convertView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return rowItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return rowItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	//private view holder class
	private class ViewHolder {

		TextView txtName;
		TextView txtEmail;
		TextView txtNumber;
		CheckBox chkBox;
	}

	/**
	 * Return true if all CheckBoxs are selected.
	 * Return false if all CheckBoxs are deselected.
	 */
	public boolean getCheckBoxSelectedState(){
		return state;
	}

	public void setSelectStateForAll(){
		if(state)
			state = false;
		else
			state = true;
		for(RowItem r : rowItems)
			r.setSelected(state);
	}


}