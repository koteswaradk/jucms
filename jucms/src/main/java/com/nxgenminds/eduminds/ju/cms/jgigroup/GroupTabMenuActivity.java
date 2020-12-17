package com.nxgenminds.eduminds.ju.cms.jgigroup;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.adapters.GroupTabsPagerAdapter;


public class GroupTabMenuActivity extends ActionBarActivity  implements
ActionBar.TabListener {

	private ViewPager viewPager;
	private GroupTabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	// Tab titles
	private String[] tabs = { "JU CMS", "Group Feed", "Events" ,"Gallery"};
	//private String pos = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_menu_activity);


		// Initilization  
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getSupportActionBar();	
		mAdapter = new GroupTabsPagerAdapter(getSupportFragmentManager());
		viewPager.setOffscreenPageLimit(4);
		viewPager.setAdapter(mAdapter);

		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#40be7f")));
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS); 

		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}


		/**
		 * on swiping the viewpager make respective tab selected
		 * */

		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);

				/*if(position==0)
				{
					
					finish();
				}*/
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

	}

	@Override
	public void onTabReselected(Tab arg0,
			FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab arg0,
			FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		viewPager.setCurrentItem(arg0.getPosition());


	}

	@Override
	public void onTabUnselected(Tab arg0,
			FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		/*MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity, menu);*/
		return super.onCreateOptionsMenu(menu);
	}
}