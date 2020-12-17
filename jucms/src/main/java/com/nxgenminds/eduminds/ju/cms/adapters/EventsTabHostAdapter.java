package com.nxgenminds.eduminds.ju.cms.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nxgenminds.eduminds.ju.cms.events.UserFragmentEventArchived;
import com.nxgenminds.eduminds.ju.cms.events.UserFragmentEventCurrent;
import com.nxgenminds.eduminds.ju.cms.events.UserFragmentEventUpComing;

public class EventsTabHostAdapter extends FragmentPagerAdapter{

	private String titles[] = {"Upcoming","Current","Archived"};

	public EventsTabHostAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub

		switch(position)
		{
		case 0:
			return new UserFragmentEventUpComing();
		case 1:
			return new UserFragmentEventCurrent();
		case 2:
			return new UserFragmentEventArchived();
		default:
			return null;
		}

	}
	@Override
	public CharSequence getPageTitle(int position)
	{
		return titles[position];
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3;
	}

}
