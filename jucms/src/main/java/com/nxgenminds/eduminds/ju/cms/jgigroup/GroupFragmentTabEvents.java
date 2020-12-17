package com.nxgenminds.eduminds.ju.cms.jgigroup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.events.UserCreateEvent;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class GroupFragmentTabEvents extends Fragment{

	Button currentEvents,upComingEvents,archivedEvents;
	public static ImageButton addEvent;
	private ImageButton chatButton;
	public static View v_current;
	public static View v_upcoming;
	public static View v_archived;
	static LinearLayout buttons;
	public static String flag="0";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.userfragmentevents, container, false);
		currentEvents = (Button) rootView.findViewById(R.id.currentEvents);
		upComingEvents = (Button) rootView.findViewById(R.id.upcomingEvents);
		archivedEvents = (Button) rootView.findViewById(R.id.archivedEvents);
		addEvent = (ImageButton) rootView.findViewById(R.id.addEvents);
		chatButton = (ImageButton) rootView.findViewById(R.id.chatbutton_userevents);
		v_current=(View)rootView.findViewById(R.id.h_current);
		v_archived=(View)rootView.findViewById(R.id.h_archived);
		v_upcoming=(View)rootView.findViewById(R.id.h_upcoming);
		buttons=(LinearLayout)rootView.findViewById(R.id.event_c);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		addEvent.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.create));
		chatButton.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.timelinechat));
		// by default
		chatButton.setVisibility(View.GONE);
		v_current.setVisibility(View.VISIBLE);

		GroupFragmentEventCurrent current = new GroupFragmentEventCurrent();
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.content_frame_user_events,current);
		fragmentTransaction.commit();
		//end

		if(!Util.ADMIN.equalsIgnoreCase("1")){
			addEvent.setVisibility(View.GONE);
		}
		
		if(Util.ROLE.equalsIgnoreCase("admin")){
			addEvent.setVisibility(View.VISIBLE);
		}

		currentEvents.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// by default
				v_current.setVisibility(View.VISIBLE);
				v_archived.setVisibility(View.INVISIBLE);
				v_upcoming.setVisibility(View.INVISIBLE);	
				GroupFragmentEventCurrent current = new GroupFragmentEventCurrent();
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.content_frame_user_events,current);
				fragmentTransaction.commit();
				//end
			}
		});

		upComingEvents.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// by default
				v_upcoming.setVisibility(View.VISIBLE);
				v_current.setVisibility(View.INVISIBLE);
				v_archived.setVisibility(View.INVISIBLE);
				GroupFragmentEventUpComing upComing = new GroupFragmentEventUpComing();
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.content_frame_user_events,upComing);
				fragmentTransaction.commit();
				//end
			}
		});

		archivedEvents.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// by default
				v_archived.setVisibility(View.VISIBLE);
				v_upcoming.setVisibility(View.INVISIBLE);
				v_current.setVisibility(View.INVISIBLE);
				GroupFragmentEventArchived archived = new GroupFragmentEventArchived();
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.content_frame_user_events,archived);
				fragmentTransaction.commit();
				//end
			}
		});

		addEvent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent addEvent= new Intent(getActivity(),UserCreateEvent.class);
				addEvent.putExtra("Group", "Group");
				startActivity(addEvent);
			}
		});
	}
}
