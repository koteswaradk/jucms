package com.nxgenminds.eduminds.ju.cms.classSchedule;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost.OnTabChangeListener;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.adapters.ClassScheduleTabHostAdapter;


public class ClassScheduleTabHostFragment extends Fragment{

	private FragmentTabHost tabhost;
	private ViewPager pager;
	private ClassScheduleTabHostAdapter adapter;
	private ImageView create;

	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
	{
		
		View view = inflater.inflate(R.layout.events_tabhost_fragment, container, false);
		tabhost = (FragmentTabHost) view.findViewById(android.R.id.tabhost);
		tabhost.setup(getActivity(),getChildFragmentManager(),android.R.id.tabcontent);

		tabhost.addTab(tabhost.newTabSpec("Today's").setIndicator("Today's"),TodayClassScheduleFragment.class,null);
		tabhost.addTab(tabhost.newTabSpec("This Week's").setIndicator("This Week's"),WeeksClassScheduleFragment.class,null);

		pager = (ViewPager) view.findViewById(R.id.eventsviewpager);
		adapter = new ClassScheduleTabHostAdapter(getChildFragmentManager());

		create=(ImageView)view.findViewById(R.id.event_tabhost_CreateEvent);
		create.setVisibility(View.GONE);
		//pager.setOffscreenPageLimit(2);
		pager.setAdapter(adapter);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		
		super.onActivityCreated(savedInstanceState);

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

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageSelected(int position) {
				tabhost.setCurrentTab(position);
			}

		});
	}

}


