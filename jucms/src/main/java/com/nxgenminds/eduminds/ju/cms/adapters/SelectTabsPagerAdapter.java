package com.nxgenminds.eduminds.ju.cms.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nxgenminds.eduminds.ju.cms.fragments.SelectConnection_contacts;

public class SelectTabsPagerAdapter extends FragmentPagerAdapter {
	public SelectTabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}
	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			return new SelectConnection_contacts();
		/*case 1:
			// timelog fragment activity
			return new Select_connection_group();*/
	
		}
		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 1;
	}
}
