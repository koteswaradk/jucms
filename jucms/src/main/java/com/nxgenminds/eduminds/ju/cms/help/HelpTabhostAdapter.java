package com.nxgenminds.eduminds.ju.cms.help;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class HelpTabhostAdapter extends FragmentPagerAdapter{

	private String titles[] = {"Upcoming","Current","Archived"};

	public HelpTabhostAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int position) {

		switch(position)
		{
		case 0:

			return new HelpFaqFragment();
		case 1:

			return new HelpNavFragment();
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
		return 2;
	}

}
