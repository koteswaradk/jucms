package com.nxgenminds.eduminds.ju.cms.thirdpartyprofile;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.adapters.ThirdTabsPagerAdapter;
import com.nxgenminds.eduminds.ju.cms.utils.Util;



public class ThirdPartyTabMenuActivity extends ActionBarActivity  implements
ActionBar.TabListener { 

	private ViewPager viewPager;
	private ThirdTabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	public String FriendID;
	public String FriendName;
	public String FriendRole;
	// Tab titles
	private String[] tabs ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_tab_menu_activity);

		Bundle extras = getIntent().getExtras();
		FriendID = extras.getString("UserID");
		FriendRole=extras.getString("ThirdPartyRole");
		FriendName = Util.THIRD_PARTY_NAME;
		Util.THIRD_PARTY_ROLE=FriendRole;
		// creating the tabs according to the user role

		if((Util.ROLE.equalsIgnoreCase("class monitor") || Util.ROLE.equalsIgnoreCase("student") ) && FriendRole.contains("admin"))
		{
			tabs=new String[]{ FriendName, "Timelog", "Gallery"};
		}
		else if((Util.ROLE.equalsIgnoreCase("class monitor") || Util.ROLE.equalsIgnoreCase("student") ) && FriendRole.equalsIgnoreCase("student"))
		{
			tabs=new String[]{ FriendName};
		}
		else if((Util.ROLE.equalsIgnoreCase("class monitor") || Util.ROLE.equalsIgnoreCase("student")) && FriendRole.equalsIgnoreCase("teacher"))
		{
			tabs=new String[]{ FriendName,"Timelog", "Gallery"};
		}
		// same as student
		else if((Util.ROLE.equalsIgnoreCase("class monitor") || Util.ROLE.equalsIgnoreCase("student")) && FriendRole.equalsIgnoreCase("class monitor"))
		{
			tabs=new String[]{ FriendName};
		}

		// non restricted view
		else if(Util.ROLE.contains("admin") && FriendRole.equalsIgnoreCase("class monitor"))
		{
			tabs=new String[]{ FriendName,"Timelog", "Attendence","Intership"};
		}
		else if(Util.ROLE.contains("admin")&& FriendRole.equalsIgnoreCase("student"))
		{
			tabs=new String[]{ FriendName,"Attendence","Intership"};
		}
		else if(Util.ROLE.contains("admin")&& FriendRole.contains("admin"))
		{
			tabs=new String[]{ FriendName,"Timelog", "Gallery"};
		}

		else if(Util.ROLE.equalsIgnoreCase("teacher") && FriendRole.equalsIgnoreCase("class monitor")){
			tabs=new String[]{ FriendName,"Timelog", "Attendence","Intership"};
		}
		else if(Util.ROLE.equalsIgnoreCase("teacher") && FriendRole.equalsIgnoreCase("student")){
			tabs=new String[]{ FriendName,"Attendence","Intership"};
		}
		else if(Util.ROLE.equalsIgnoreCase("teacher") && FriendRole.equalsIgnoreCase("teacher")){
			tabs=new String[]{ FriendName,"Timelog", "Gallery"};
		}
		else if(Util.ROLE.contains("admin")&& FriendRole.equalsIgnoreCase("teacher"))
		{
			tabs=new String[]{ FriendName,"Timelog", "Gallery"};
		}
		else if(Util.ROLE.contains("teacher")&& FriendRole.contains("admin"))
		{
			tabs=new String[]{ FriendName,"Timelog", "Gallery"};
		}
		///////////////////////new ///////////////////////
		else if(Util.ROLE.equalsIgnoreCase("alumni") && FriendRole.equalsIgnoreCase("admin") ){
			tabs=new String[]{ FriendName, "Timelog", "Gallery"};
		}
		else if(Util.ROLE.equalsIgnoreCase("alumni") && FriendRole.equalsIgnoreCase("alumni") ){  //new 
			tabs=new String[]{ FriendName};
		}
		else if(Util.ROLE.equalsIgnoreCase("admin") && FriendRole.equalsIgnoreCase("alumni") ) //modified
		{
			tabs=new String[]{ Util.user_firstname};
		}
		else{
			tabs=new String[]{ Util.user_firstname};
		}

		// Initilization  
		viewPager = (ViewPager) findViewById(R.id.user_pager);
		actionBar = getSupportActionBar();	
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#40be7f")));
		//getSupportActionBar().hide();

		mAdapter = new ThirdTabsPagerAdapter(getSupportFragmentManager());
		viewPager.setOffscreenPageLimit(4);
		viewPager.setAdapter(mAdapter);
		//actionBar.setHomeButtonEnabled(true);

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
	public void finish() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();

		intent.putExtra("refresh_request", "refresh_request");
		setResult(RESULT_OK,intent);


		super.finish();
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
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
		MenuInflater inflater = getMenuInflater();
		//inflater.inflate(R.menu.main_activity, menu);
		return super.onCreateOptionsMenu(menu);
	}
}