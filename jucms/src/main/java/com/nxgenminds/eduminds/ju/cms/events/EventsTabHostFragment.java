package com.nxgenminds.eduminds.ju.cms.events;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost.OnTabChangeListener;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class EventsTabHostFragment extends Fragment{

	private FragmentTabHost tabhost;
	private ViewPager pager;
	private EventsTabHostAdapter adapter;
	private RelativeLayout mCreateEventLayout;
	private ImageView mImageCreateEvent;
	
	

	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		
		View view = inflater.inflate(R.layout.events_tabhost_fragment, container, false);
		tabhost = (FragmentTabHost) view.findViewById(android.R.id.tabhost);
		tabhost.setup(getActivity(),getChildFragmentManager(),android.R.id.tabcontent);

		tabhost.addTab(tabhost.newTabSpec("Upcoming").setIndicator("Upcoming"),UserFragmentEventUpComing.class,null);
		tabhost.addTab(tabhost.newTabSpec("Current").setIndicator("Current"),UserFragmentEventCurrent.class,null);
		tabhost.addTab(tabhost.newTabSpec("Archived").setIndicator("Archived"),UserFragmentEventArchived.class,null);
		
		
        
		mCreateEventLayout = (RelativeLayout) view.findViewById(R.id.event_tabhost_layout_CreateEvent);
		mImageCreateEvent = (ImageView) view.findViewById(R.id.event_tabhost_CreateEvent);
		pager = (ViewPager) view.findViewById(R.id.eventsviewpager);
		adapter = new EventsTabHostAdapter(getChildFragmentManager());
		//pager.setOffscreenPageLimit(2);
		pager.setAdapter(adapter);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		if(Util.ROLE.equalsIgnoreCase("class monitor") || Util.ROLE.equalsIgnoreCase("attendence admin") || Util.ROLE.equalsIgnoreCase("feedback admin")
				|| Util.ROLE.equalsIgnoreCase("internship admin")||Util.ROLE.equalsIgnoreCase("sprit45 admin")||Util.ROLE.equalsIgnoreCase("timetable admin")||
				Util.ROLE.equalsIgnoreCase("teacher")||Util.ROLE.equalsIgnoreCase("admin") ||	Util.ROLE.equalsIgnoreCase("alumni admin")){
			mCreateEventLayout.setVisibility(View.VISIBLE);
		}
		else{
			mCreateEventLayout.setVisibility(View.GONE);
		}
        
		
		
		mImageCreateEvent .setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent create_event_intent =  new Intent(getActivity(),UserCreateEvent.class);
				startActivityForResult(create_event_intent,Util.REQUEST_CODE_FOR_CREATE_EVENT);
			}
			
		});
		tabhost.setOnTabChangedListener(new OnTabChangeListener(){

			@Override
			public void onTabChanged(String tabId) {
				
				
				pager.setCurrentItem(tabhost.getCurrentTab());

			}

		});
		pager.setOnPageChangeListener(new OnPageChangeListener()
		{

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageSelected(int position) {
				
				// TODO Auto-generated method stub
				
				tabhost.setCurrentTab(position);
			}

		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		     
			 Fragment fragment = getChildFragmentManager().findFragmentById(android.R.id.tabcontent);
			 fragment.onActivityResult(requestCode, resultCode, data);
			 //super.onActivityResult(requestCode, resultCode, data);
			 
			 
		   /*if(resultCode == Activity.RESULT_OK && requestCode == Util.REQUEST_CODE_FOR_CREATE_EVENT){
			pager.setCurrentItem(0);
			tabhost.setCurrentTab(0);
		 }*/
		/*Fragment fragment = new ChatFragmentInbox();
		fragment.onActivityResult(requestCode, resultCode, data);*/
	}

}


