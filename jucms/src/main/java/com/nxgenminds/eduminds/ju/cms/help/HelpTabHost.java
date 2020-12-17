package com.nxgenminds.eduminds.ju.cms.help;

import com.nxgenminds.eduminds.ju.cms.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost.OnTabChangeListener;



public class HelpTabHost extends Fragment{

	private FragmentTabHost tabhost;
	private ViewPager pager;
	private HelpTabhostAdapter adapter;


	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){

		View view = inflater.inflate(R.layout.help_tabhost_fragment, container, false);
		tabhost = (FragmentTabHost) view.findViewById(android.R.id.tabhost);
		tabhost.setup(getActivity(),getChildFragmentManager(),android.R.id.tabcontent);

		tabhost.addTab(tabhost.newTabSpec("FAQ").setIndicator("FAQ"),HelpFaqFragment.class,null);
		tabhost.addTab(tabhost.newTabSpec("Help List").setIndicator("Help List"),HelpNavFragment.class,null);

		pager = (ViewPager) view.findViewById(R.id.helpviewpager);
		adapter = new HelpTabhostAdapter(getChildFragmentManager());
		pager.setAdapter(adapter);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		tabhost.setOnTabChangedListener(new OnTabChangeListener(){

			@Override
			public void onTabChanged(String tabId) {


				pager.setCurrentItem(tabhost.getCurrentTab());

			}

		});
		pager.setOnPageChangeListener(new OnPageChangeListener()
		{

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageSelected(int position) {
				tabhost.setCurrentTab(position);
			}

		});
	}


}


