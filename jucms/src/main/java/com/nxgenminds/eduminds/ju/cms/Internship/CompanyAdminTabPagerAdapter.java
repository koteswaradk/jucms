package com.nxgenminds.eduminds.ju.cms.Internship;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
public class CompanyAdminTabPagerAdapter extends FragmentPagerAdapter {
	// titles[] = {"Working at","Worked at"};
	// private String tabs[]={ "FriendName","Internship"};
	public CompanyAdminTabPagerAdapter(FragmentManager fm) {
		super(fm);
	}
	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			return new CompanyDetailUserDet();
		case 1:
			return new CompanyDetail_Internship();
		}
		return null;
	}
	/*@Override
	public CharSequence getPageTitle(int position)
	{
		return tabs[position];
	}*/

	@Override
	public int getCount() {
		return 2;
	}
}
