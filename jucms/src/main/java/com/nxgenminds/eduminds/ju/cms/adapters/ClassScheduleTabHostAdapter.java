package com.nxgenminds.eduminds.ju.cms.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nxgenminds.eduminds.ju.cms.classSchedule.TodayClassScheduleFragment;
import com.nxgenminds.eduminds.ju.cms.classSchedule.WeeksClassScheduleFragment;

public class ClassScheduleTabHostAdapter extends FragmentPagerAdapter{

	private String titles[] = {"Today's","This Week's"};

	public ClassScheduleTabHostAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub

		switch(position)
		{
		case 0:
			return new TodayClassScheduleFragment();
		case 1:
			return new WeeksClassScheduleFragment();

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
		return 2;
	}

}
