package com.nxgenminds.eduminds.ju.cms;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;

import com.nxgenminds.eduminds.ju.cms.adapters.SelectTabsPagerAdapter;


public class SelectConnectionTabActivity extends ActionBarActivity  implements
ActionBar.TabListener { 
	public Menu menuInstance;
	private ViewPager viewPager;
	private SelectTabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	// Tab titles
	private String[] tabs = { "Contacts" ,"Group"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_connection_tab_activity_pager);

		// Initilization  
		viewPager = (ViewPager) findViewById(R.id.select_pager);

		actionBar = getSupportActionBar();	
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);

		mAdapter = new SelectTabsPagerAdapter(getSupportFragmentManager());
		viewPager.setOffscreenPageLimit(2);
		viewPager.setAdapter(mAdapter);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#40be7f")));

		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS); 

		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}

		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);

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
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
}