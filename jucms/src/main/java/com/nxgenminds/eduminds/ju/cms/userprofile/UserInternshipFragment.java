package com.nxgenminds.eduminds.ju.cms.userprofile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost.OnTabChangeListener;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.adapters.InternshipTabHostAdapter;
import com.nxgenminds.eduminds.ju.cms.fragments.StudentWorkedAtFragment;
import com.nxgenminds.eduminds.ju.cms.fragments.StudentWorkingAtFragment;


public class UserInternshipFragment extends Fragment{

	private FragmentTabHost tabhost;
	private ViewPager pager;
	private InternshipTabHostAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.internship_fragment, container, false);
		tabhost = (FragmentTabHost) view.findViewById(android.R.id.tabhost);
		tabhost.setup(getActivity(),getChildFragmentManager(),android.R.id.tabcontent);

		tabhost.addTab(tabhost.newTabSpec("Working at").setIndicator("Working at"),StudentWorkingAtFragment.class,null);
		tabhost.addTab(tabhost.newTabSpec("Worked at").setIndicator("Worked at"),StudentWorkedAtFragment.class,null);

		pager = (ViewPager) view.findViewById(R.id.internshipviewpager);
		adapter = new InternshipTabHostAdapter(getChildFragmentManager());
		//pager.setOffscreenPageLimit(2);
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
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				tabhost.setCurrentTab(position);
			}

		});
	}
}


