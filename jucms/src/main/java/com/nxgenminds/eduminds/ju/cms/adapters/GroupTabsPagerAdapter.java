package com.nxgenminds.eduminds.ju.cms.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nxgenminds.eduminds.ju.cms.fragments.FragmentTabGroup;
import com.nxgenminds.eduminds.ju.cms.jgigroup.GroupFragmentTabEvents;
import com.nxgenminds.eduminds.ju.cms.jgigroup.GroupFragmentTabGallery;
import com.nxgenminds.eduminds.ju.cms.jgigroup.GroupFragmentTabGroupFeed;

public class GroupTabsPagerAdapter extends FragmentPagerAdapter{

	public GroupTabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			//  fragment activity
			return new FragmentTabGroup();
		case 1:
			// GroupFeed fragment activity
			return new GroupFragmentTabGroupFeed();
		case 2:
			// Events fragment activity
			return new GroupFragmentTabEvents();
		case 3:
			// Gallery fragment activity
			return new GroupFragmentTabGallery();
		}
		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 4;
	}

}
