package com.nxgenminds.eduminds.ju.cms.events;

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
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class UserFragmentEvents extends Fragment{

	Button currentEvents,upComingEvents,archivedEvents;
	public static ImageButton addEvent;
	ImageButton chatButton;
	public static View v_current;
	public static View v_upcoming;
	public static View v_archived;
	static LinearLayout buttons;
	public static String flag="0";


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
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
		Util.eventFlag=true;
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		addEvent.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.create));
		chatButton.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.timelinechat));
		// by default
		v_upcoming.setVisibility(View.VISIBLE);
		UserFragmentEventUpComing upComing = new UserFragmentEventUpComing();
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.content_frame_user_events,upComing);
		fragmentTransaction.commit();
          //end
		
		currentEvents.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v_current.setVisibility(View.VISIBLE);
				v_archived.setVisibility(View.INVISIBLE);
				v_upcoming.setVisibility(View.INVISIBLE);	
				UserFragmentEventCurrent current = new UserFragmentEventCurrent();
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.content_frame_user_events,current);
				fragmentTransaction.commit();

			}
		});

		upComingEvents.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				v_upcoming.setVisibility(View.VISIBLE);
				v_current.setVisibility(View.INVISIBLE);
				v_archived.setVisibility(View.INVISIBLE);
				UserFragmentEventUpComing upComing = new UserFragmentEventUpComing();
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.content_frame_user_events,upComing);
				fragmentTransaction.commit();

			}
		});

		archivedEvents.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				v_archived.setVisibility(View.VISIBLE);
				v_upcoming.setVisibility(View.INVISIBLE);
				v_current.setVisibility(View.INVISIBLE);			
				UserFragmentEventArchived archived = new UserFragmentEventArchived();
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
				addEvent.putExtra("Group", "User");
				startActivity(addEvent);
			}
		});
	}
}

