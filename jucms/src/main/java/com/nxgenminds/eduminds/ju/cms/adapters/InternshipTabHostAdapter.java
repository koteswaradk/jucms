package com.nxgenminds.eduminds.ju.cms.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nxgenminds.eduminds.ju.cms.fragments.StudentWorkedAtFragment;
import com.nxgenminds.eduminds.ju.cms.fragments.StudentWorkingAtFragment;

public class InternshipTabHostAdapter extends FragmentPagerAdapter{

	private String titles[] = {"Working at","Worked at"};

	public InternshipTabHostAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub

		switch(position)
		{
		case 0:
			return new StudentWorkingAtFragment();
		case 1:
			return new StudentWorkedAtFragment();
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
