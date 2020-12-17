package com.nxgenminds.eduminds.ju.cms.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.models.NavListModel;
import com.nxgenminds.eduminds.ju.cms.utils.NewIteamsChecking;


public class NavListAdapter extends BaseAdapter {
	Context context;
	LayoutInflater inflater;
	ArrayList<NavListModel> listData;
	NewIteamsChecking checkingiteams;
	HashMap<String, String> checknav;
	String newnotification;
	String newmessages;
	String newevents;
	String newbroadcast;
	String newgroup;

	public NavListAdapter(Context context,ArrayList<NavListModel> listData) {
		this.context = context;
		this.listData = listData;
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

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (inflater == null)
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(convertView == null){
			convertView = inflater.inflate(R.layout.drawer_list_item, null);
			holder = new ViewHolder();
			holder.navListName = (TextView) convertView.findViewById(R.id.title);
			holder.navListPhoto = (ImageView) convertView.findViewById(R.id.icon);
			holder.badge=(LinearLayout)convertView.findViewById(R.id.newcoming);

			checkingiteams = new NewIteamsChecking(context);
			checknav = checkingiteams.checknavlistiteams();
			newnotification = checknav.get(NewIteamsChecking.NOTIFICATIONS_PUSH);
			newmessages = checknav.get(NewIteamsChecking.NOTIFICATIONS_MESSAGES);
			newevents = checknav.get(NewIteamsChecking.NOTIFICATIONS_EVENTS);
			newbroadcast = checknav.get(NewIteamsChecking.NOTIFICATIONS_BROADCAST);
			newgroup = checknav.get(NewIteamsChecking.NOTIFICATIONS_GROUP);

			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}

		NavListModel navlistItem = (NavListModel) listData.get(position);

		if(navlistItem.getIs_heading().equalsIgnoreCase("1")){
			holder.navListName.setText(navlistItem.getMenu_item().toUpperCase());
			holder.navListName.setTypeface(null, Typeface.BOLD);
			holder.navListPhoto.setVisibility(View.GONE);
			holder.badge.setVisibility(View.GONE);

		}else{
			holder.navListName.setTypeface(null, Typeface.NORMAL);
			holder.navListName.setText(navlistItem.getMenu_item());
			holder.navListPhoto.setVisibility(View.VISIBLE);

			if(navlistItem.getMenu_item().equalsIgnoreCase("JU CMS")){
				holder.navListPhoto.setImageResource(R.drawable.jgiappicon);
				if(!newgroup.equalsIgnoreCase("new")){
					holder.badge.setVisibility(View.GONE);
				}else{
					holder.badge.setVisibility(View.VISIBLE);
				}
			}
			if(navlistItem.getMenu_item().equalsIgnoreCase("Welcome")){
				holder.navListPhoto.setImageResource(R.drawable.home_nav);
				holder.badge.setVisibility(View.GONE);

			}
			if(navlistItem.getMenu_item().equalsIgnoreCase("Timefeed")){
				holder.navListPhoto.setImageResource(R.drawable.timefeed);
				holder.badge.setVisibility(View.GONE);

			}
			//side menu icons..
			if(navlistItem.getMenu_item().equalsIgnoreCase("Notice Board")){
				holder.navListPhoto.setImageResource(R.drawable.help);
				holder.badge.setVisibility(View.GONE);

			}
			if(navlistItem.getMenu_item().equalsIgnoreCase("Placements Board")){
				holder.navListPhoto.setImageResource(R.drawable.help);
				holder.badge.setVisibility(View.GONE);

			}
			if(navlistItem.getMenu_item().equalsIgnoreCase("Academics")){
				holder.navListPhoto.setImageResource(R.drawable.help);
				holder.badge.setVisibility(View.GONE);

			}
			
			if(navlistItem.getMenu_item().equalsIgnoreCase("Admissions")){
				holder.navListPhoto.setImageResource(R.drawable.help);
				holder.badge.setVisibility(View.GONE);

			}
			if(navlistItem.getMenu_item().equalsIgnoreCase("Entrance Test Results")){
				holder.navListPhoto.setImageResource(R.drawable.help);
				holder.badge.setVisibility(View.GONE);

			}
			
			
			
			if(navlistItem.getMenu_item().equalsIgnoreCase("People")){

				//holder.navListPhoto.setVisibility(View.GONE);
				//holder.navListPhoto.setImageResource(R.drawable.connection);
				holder.badge.setVisibility(View.GONE);

			}
			if(navlistItem.getMenu_item().equalsIgnoreCase("Teachers")){
				holder.navListPhoto.setImageResource(R.drawable.teacher);
				holder.badge.setVisibility(View.GONE);

			}
			if(navlistItem.getMenu_item().equalsIgnoreCase("Students")){
				holder.navListPhoto.setImageResource(R.drawable.students);
				holder.badge.setVisibility(View.GONE);

			}
			if(navlistItem.getMenu_item().equalsIgnoreCase("My Groups")){
				holder.navListPhoto.setImageResource(R.drawable.mygroups);
				holder.badge.setVisibility(View.GONE);

			}
			if(navlistItem.getMenu_item().equalsIgnoreCase("Academics")){
				//holder.navListPhoto.setVisibility(View.GONE);
				holder.badge.setVisibility(View.GONE);

			}
			if(navlistItem.getMenu_item().equalsIgnoreCase("Class Schedule")){
				holder.navListPhoto.setImageResource(R.drawable.classschedule);
				holder.badge.setVisibility(View.GONE);

			}
			if(navlistItem.getMenu_item().equalsIgnoreCase("Attendence")){
				holder.navListPhoto.setImageResource(R.drawable.attendance);
				holder.badge.setVisibility(View.GONE);

			}
			if(navlistItem.getMenu_item().equalsIgnoreCase("Modules")){
				holder.navListPhoto.setImageResource(R.drawable.modules);
				holder.badge.setVisibility(View.GONE);

			}
			if(navlistItem.getMenu_item().equalsIgnoreCase("Marks")){
				holder.navListPhoto.setImageResource(R.drawable.marks);
				holder.badge.setVisibility(View.GONE);

			}

			if(navlistItem.getMenu_item().equalsIgnoreCase("Internship")){
				holder.navListPhoto.setImageResource(R.drawable.internship);
				holder.badge.setVisibility(View.GONE);

			}

			if(navlistItem.getMenu_item().equalsIgnoreCase("Discussion Forum")){
				holder.navListPhoto.setImageResource(R.drawable.discussionforum);
				holder.badge.setVisibility(View.GONE);

			}
			if(navlistItem.getMenu_item().equalsIgnoreCase("Others")){
				//holder.navListPhoto.setVisibility(View.GONE);
				holder.badge.setVisibility(View.GONE);

			}
			if(navlistItem.getMenu_item().equalsIgnoreCase("Notifications")){
				holder.navListPhoto.setImageResource(R.drawable.notification);
				if(!newnotification.equalsIgnoreCase("new")){
					holder.badge.setVisibility(View.GONE);
				}else{
					holder.badge.setVisibility(View.VISIBLE);
				}
			}

			if(navlistItem.getMenu_item().equalsIgnoreCase("Broadcast")){
				holder.navListPhoto.setImageResource(R.drawable.broadcast);
				if(!newbroadcast.equalsIgnoreCase("new")){
					holder.badge.setVisibility(View.GONE);
				}else{
					holder.badge.setVisibility(View.VISIBLE);
				}
			}
			if(navlistItem.getMenu_item().equalsIgnoreCase("Messages")){
				holder.navListPhoto.setImageResource(R.drawable.message);
				if(!newmessages.equalsIgnoreCase("new")){
					holder.badge.setVisibility(View.GONE);
				}else{
					holder.badge.setVisibility(View.VISIBLE);
				}
			}
			if(navlistItem.getMenu_item().equalsIgnoreCase("Events")){
				holder.navListPhoto.setImageResource(R.drawable.events);
				if(!newevents.equalsIgnoreCase("new")){
					holder.badge.setVisibility(View.GONE);
				}else{
					holder.badge.setVisibility(View.VISIBLE);
				}
			}

			if(navlistItem.getMenu_item().equalsIgnoreCase("Feedback")){
				holder.navListPhoto.setImageResource(R.drawable.feedback);
				holder.badge.setVisibility(View.GONE);

			}

			if(navlistItem.getMenu_item().equalsIgnoreCase("Settings")){
				holder.navListPhoto.setImageResource(R.drawable.settings);
				holder.badge.setVisibility(View.GONE);

			}
			if(navlistItem.getMenu_item().equalsIgnoreCase("Admin")){
				holder.navListPhoto.setImageResource(R.drawable.admindirectory);
				holder.badge.setVisibility(View.GONE);

			}
			if(navlistItem.getMenu_item().equalsIgnoreCase("Alumni")){
				holder.navListPhoto.setImageResource(R.drawable.alumnidirectory);
				holder.badge.setVisibility(View.GONE);

			}
			if(navlistItem.getMenu_item().equalsIgnoreCase("Sign Out")){
				holder.navListPhoto.setImageResource(R.drawable.signout);
				holder.badge.setVisibility(View.GONE);

			}
			if(navlistItem.getMenu_item().equalsIgnoreCase("Spirit of 45")){
				holder.navListPhoto.setImageResource(R.drawable.spiritof45);
				holder.badge.setVisibility(View.GONE);

			}
			if(navlistItem.getMenu_item().equalsIgnoreCase("Help")){
				holder.navListPhoto.setImageResource(R.drawable.help);
				holder.badge.setVisibility(View.GONE);

			}
		}



		return convertView;
	}

	static class ViewHolder{
		ImageView navListPhoto;
		TextView navListName;
		LinearLayout badge;
	}

}