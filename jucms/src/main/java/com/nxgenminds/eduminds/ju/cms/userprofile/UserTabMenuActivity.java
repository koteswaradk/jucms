package com.nxgenminds.eduminds.ju.cms.userprofile;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.adapters.UserTabsPagerAdapter;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class UserTabMenuActivity extends ActionBarActivity  implements
ActionBar.TabListener { 

	private ViewPager viewPager;
	private UserTabsPagerAdapter mAdapter;
	private ActionBar actionBar;

	// Tab titles
	//private String[] tabs = { Util.user_firstname, "Timelog", "Connections" ,"Gallery"};

	private String[] tabs;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_tab_menu_activity);
		Log.i("user tab menu activity", "inside on create");
		// depending upon the user role , name the tabs. (except user role:"alumni")
		if(Util.ROLE.equalsIgnoreCase("class monitor"))
		{
			tabs=new String[]{ Util.user_firstname, "Timelog", "Attendance" ,"Internship"};
		}
		else if(Util.ROLE.equalsIgnoreCase("alumni")){
			tabs=new String[]{ Util.user_firstname};
		}
		else if(Util.ROLE.equalsIgnoreCase("attendence admin")){
			tabs=new String[]{ Util.user_firstname, "Timelog","Gallery"};
		}
		else if(Util.ROLE.equalsIgnoreCase("alumni admin")){
			tabs=new String[]{ Util.user_firstname, "Timelog","Gallery"};
		}
		else if(Util.ROLE.equalsIgnoreCase("feedback admin")){
			tabs=new String[]{ Util.user_firstname, "Timelog","Gallery"};
		}
		else if(Util.ROLE.equalsIgnoreCase("internship admin")){
			tabs=new String[]{ Util.user_firstname, "Timelog","Gallery"};
		}
		else if(Util.ROLE.equalsIgnoreCase("admin")){
			tabs=new String[]{ Util.user_firstname, "Timelog","Gallery"};
		}
		else if(Util.ROLE.equalsIgnoreCase("sprit45 admin")){
			tabs=new String[]{ Util.user_firstname, "Timelog","Gallery"};
		}
		else if(Util.ROLE.equalsIgnoreCase("student")){
			tabs=new String[]{ Util.user_firstname, "Attendance" ,"Internship"};
		}
		else if(Util.ROLE.equalsIgnoreCase("teacher")){
			tabs=new String[]{ Util.user_firstname, "Timelog","Gallery"};
		}
		else if(Util.ROLE.equalsIgnoreCase("timetable admin")){
			tabs=new String[]{ Util.user_firstname, "Timelog","Gallery"};
		}

		else if(Util.ROLE.equalsIgnoreCase("parent")){
			tabs=new String[]{ Util.user_firstname, "Attendance" ,"Internship"};
		}
		else if(Util.ROLE.equalsIgnoreCase("guest")){
			tabs=new String[]{ Util.user_firstname};
		}
		else if(Util.ROLE.equalsIgnoreCase("company admin")){
			tabs=new String[]{ Util.user_firstname};
		}
		else if(Util.ROLE.equalsIgnoreCase("office staff")){
			tabs=new String[]{ Util.user_firstname};
		}
		else if(Util.ROLE.equalsIgnoreCase("office admin")){
			tabs=new String[]{ Util.user_firstname, "Timelog","Gallery"};
		}

		// Initilization  
		viewPager = (ViewPager) findViewById(R.id.user_pager);
		actionBar = getSupportActionBar();	
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);

		mAdapter = new UserTabsPagerAdapter(getSupportFragmentManager());
		viewPager.setOffscreenPageLimit(3);
		viewPager.setAdapter(mAdapter);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#40be7f")));

		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS); 
		//actionBar.setSplitBackgroundDrawable(new ColorDrawable(Color.parseColor("#330000ff")));

		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}
		//set pager position
		/**
		 * on swiping the viewpager make respective tab selected
		 * */

		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		//MenuInflater inflater = getMenuInflater();
		//inflater.inflate(R.menu.main_activity, menu);
		return super.onCreateOptionsMenu(menu);
	}
}