package com.nxgenminds.eduminds.ju.cms.Internship;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AdminDetailViewAdapter extends FragmentPagerAdapter {
	public AdminDetailViewAdapter(FragmentManager fm) {
		super(fm);
	}
	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			return new AdminWorkingAt();
		case 1:
			return new AdminWorkedAt();
		}
		return null;
	}

	@Override
	public int getCount() {
		return 2;
	}
}
