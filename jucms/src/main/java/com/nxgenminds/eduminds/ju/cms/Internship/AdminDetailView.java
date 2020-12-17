package com.nxgenminds.eduminds.ju.cms.Internship;

import com.nxgenminds.eduminds.ju.cms.R;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;



public class AdminDetailView extends ActionBarActivity  implements
ActionBar.TabListener { 

	private ViewPager viewPager;
	private AdminDetailViewAdapter mAdapter;
	private ActionBar actionBar;
	private String[] tabs;
	public String companyId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_tab_menu_activity);
		Bundle extras = getIntent().getExtras();
		companyId = extras.getString("companyId");
		tabs=new String[]{ "Working at","Worked at"};

		// Initilization  
		viewPager = (ViewPager) findViewById(R.id.user_pager);
		actionBar = getSupportActionBar();	
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);

		mAdapter = new AdminDetailViewAdapter(getSupportFragmentManager());
		viewPager.setOffscreenPageLimit(2);
		viewPager.setAdapter(mAdapter);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#40be7f")));
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS); 
		//actionBar.setSplitBackgroundDrawable(new ColorDrawable(Color.parseColor("#330000ff")));

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
	}

	@Override
	public void onTabSelected(Tab arg0,
			FragmentTransaction arg1) {
		viewPager.setCurrentItem(arg0.getPosition());

	}

	@Override
	public void onTabUnselected(Tab arg0,
			FragmentTransaction arg1) {
	} 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}
}