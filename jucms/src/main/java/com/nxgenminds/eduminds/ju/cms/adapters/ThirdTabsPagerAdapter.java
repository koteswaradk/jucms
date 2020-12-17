package com.nxgenminds.eduminds.ju.cms.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nxgenminds.eduminds.ju.cms.thirdpartyprofile.ThirdPartyFragmentGallery;
import com.nxgenminds.eduminds.ju.cms.thirdpartyprofile.ThirdPartyFragmentTimeLog;
import com.nxgenminds.eduminds.ju.cms.thirdpartyprofile.ThirdPartyProfileViewFragment;
import com.nxgenminds.eduminds.ju.cms.thirdpartyprofile.ThirdPartyUserAttendence;
import com.nxgenminds.eduminds.ju.cms.userprofile.UserInternshipFragment;
import com.nxgenminds.eduminds.ju.cms.utils.Util;

public class ThirdTabsPagerAdapter extends FragmentPagerAdapter {
	private static int tab_no;
	public ThirdTabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}
	@Override
	public Fragment getItem(int index) {
		if((Util.ROLE.equalsIgnoreCase("class monitor") || Util.ROLE.equalsIgnoreCase("student") ) && Util.THIRD_PARTY_ROLE.contains("admin"))
		{
			switch (index) {
			case 0:
				return new ThirdPartyProfileViewFragment();
			case 1:
				return new ThirdPartyFragmentTimeLog();
			case 2:
				return new ThirdPartyFragmentGallery();
			}
		}
		else if((Util.ROLE.equalsIgnoreCase("class monitor") || Util.ROLE.equalsIgnoreCase("student") ) && Util.THIRD_PARTY_ROLE.equalsIgnoreCase("student"))
		{
			switch (index) {
			case 0:
				return new ThirdPartyProfileViewFragment();
			}
		}
		else if((Util.ROLE.equalsIgnoreCase("class monitor") || Util.ROLE.equalsIgnoreCase("student")) && Util.THIRD_PARTY_ROLE.equalsIgnoreCase("teacher"))
		{
			switch (index) {
			case 0:
				return new ThirdPartyProfileViewFragment();
			case 1:
				return new ThirdPartyFragmentTimeLog();
			case 2:
				return new ThirdPartyFragmentGallery();
			}
		}
		// same as student
		else if((Util.ROLE.equalsIgnoreCase("class monitor") || Util.ROLE.equalsIgnoreCase("student") ) && Util.THIRD_PARTY_ROLE.equalsIgnoreCase("class monitor"))
		{
			switch (index) {
			case 0:
				return new ThirdPartyProfileViewFragment();
			}
		}

		// non restricted view
		else if(Util.ROLE.contains("admin") && Util.THIRD_PARTY_ROLE.equalsIgnoreCase("class monitor"))
		{
			switch (index) {
			case 0:
				return new ThirdPartyProfileViewFragment();
			case 1:
				return new ThirdPartyFragmentTimeLog();
			case 2:
				return new ThirdPartyUserAttendence();
			case 3:
				return new UserInternshipFragment();
			}

		}
		else if(Util.ROLE.contains("admin")&& Util.THIRD_PARTY_ROLE.equalsIgnoreCase("student"))
		{
			switch (index) {
			case 0:
				return new ThirdPartyProfileViewFragment();
			case 1:
				return new ThirdPartyUserAttendence();
			case 2:
				return new UserInternshipFragment();
			}
		}
		else if(Util.ROLE.contains("admin")&& Util.THIRD_PARTY_ROLE.contains("admin"))
		{
			switch (index) {
			case 0:
				return new ThirdPartyProfileViewFragment();
			case 1:
				return new ThirdPartyFragmentTimeLog();
			case 2:
				return new ThirdPartyFragmentGallery();
			}
		}

		/*		else if(Util.ROLE.equalsIgnoreCase("alumni")){
			switch (index) {
			case 0:
				return new ThirdPartyProfileViewFragment();
			}
		}*/
		else if(Util.ROLE.equalsIgnoreCase("teacher") &&  Util.THIRD_PARTY_ROLE.equalsIgnoreCase("class monitor")){
			switch (index) {
			case 0:
				return new ThirdPartyProfileViewFragment();
			case 1:
				return new ThirdPartyFragmentTimeLog();
			case 2:
				return new ThirdPartyUserAttendence();
			case 3:
				return new UserInternshipFragment();
			}
		}
		else if(Util.ROLE.equalsIgnoreCase("teacher") &&  Util.THIRD_PARTY_ROLE.equalsIgnoreCase("student")){
			switch (index) {
			case 0:
				return new ThirdPartyProfileViewFragment();
			case 1:
				return new ThirdPartyUserAttendence();
			case 2:
				return new UserInternshipFragment();
			}
		}
		else if(Util.ROLE.equalsIgnoreCase("teacher") &&  Util.THIRD_PARTY_ROLE.equalsIgnoreCase("teacher")){
			switch (index) {
			case 0:
				return new ThirdPartyProfileViewFragment();
			case 1:
				return new ThirdPartyFragmentTimeLog();
			case 2:
				return new ThirdPartyFragmentGallery();
			}
		}
		else if(Util.ROLE.contains("admin")&& Util.THIRD_PARTY_ROLE.equalsIgnoreCase("teacher"))
		{
			switch (index) {
			case 0:
				return new ThirdPartyProfileViewFragment();
			case 1:
				return new ThirdPartyFragmentTimeLog();
			case 2:
				return new ThirdPartyFragmentGallery();
			}
		}
		else if(Util.ROLE.contains("teacher")&& Util.THIRD_PARTY_ROLE.contains("admin"))
		{
			switch (index) {
			case 0:
				return new ThirdPartyProfileViewFragment();
			case 1:
				return new ThirdPartyFragmentTimeLog();
			case 2:
				return new ThirdPartyFragmentGallery();
			}
		}
		else if( Util.THIRD_PARTY_ROLE.equalsIgnoreCase("alumni"))
		{
			switch (index) {
			case 0:
				return new ThirdPartyProfileViewFragment();
			}
		}
		///////////////////////new ///////////////////////
		else if(Util.ROLE.equalsIgnoreCase("alumni") && Util.THIRD_PARTY_ROLE.equalsIgnoreCase("admin") ){
			switch (index) {
			case 0:
				return new ThirdPartyProfileViewFragment();
			case 1:
				return new ThirdPartyFragmentTimeLog();
			case 2:
				return new ThirdPartyFragmentGallery();
			}
		}
		else if(Util.ROLE.equalsIgnoreCase("alumni") && Util.THIRD_PARTY_ROLE.equalsIgnoreCase("alumni") ){
			switch (index) {
			case 0:
				return new ThirdPartyProfileViewFragment();
			}
		}
		else if(Util.ROLE.equalsIgnoreCase("admin") && Util.THIRD_PARTY_ROLE.equalsIgnoreCase("alumni") ) //modified
		{
			switch (index) {
			case 0:
				return new ThirdPartyProfileViewFragment();
			}
		}
		else
		{
			switch (index) {
			case 0:
				return new ThirdPartyProfileViewFragment();
			}
		}

		return null;
	}

	@Override
	public int getCount() {
		if((Util.ROLE.equalsIgnoreCase("class monitor") || Util.ROLE.equalsIgnoreCase("student")) && Util.THIRD_PARTY_ROLE.contains("admin"))
		{
			tab_no=3;
		}

		else if((Util.ROLE.equalsIgnoreCase("class monitor") || Util.ROLE.equalsIgnoreCase("student") ) && Util.THIRD_PARTY_ROLE.equalsIgnoreCase("student"))
		{
			tab_no=1;
		}
		else if((Util.ROLE.equalsIgnoreCase("class monitor") || Util.ROLE.equalsIgnoreCase("student")) && Util.THIRD_PARTY_ROLE.equalsIgnoreCase("teacher"))
		{
			tab_no=3;
		}
		// same as student
		else if((Util.ROLE.equalsIgnoreCase("class monitor") || Util.ROLE.equalsIgnoreCase("student")) && Util.THIRD_PARTY_ROLE.equalsIgnoreCase("class monitor"))
		{
			tab_no=1;
		}

		// non restricted view
		else if(Util.ROLE.contains("admin") && Util.THIRD_PARTY_ROLE.equalsIgnoreCase("class monitor"))
		{
			tab_no=4;

		}
		else if(Util.ROLE.contains("admin")&& Util.THIRD_PARTY_ROLE.equalsIgnoreCase("student"))
		{
			tab_no=3;
		}
		else if(Util.ROLE.contains("admin")&& Util.THIRD_PARTY_ROLE.contains("admin"))
		{
			tab_no=3;
		}

		else if(Util.ROLE.equalsIgnoreCase("alumni")){
			tab_no=1;
		}
		else if(Util.ROLE.equalsIgnoreCase("teacher") &&  Util.THIRD_PARTY_ROLE.equalsIgnoreCase("class monitor")){
			tab_no=4;
		}
		else if(Util.ROLE.equalsIgnoreCase("teacher") &&  Util.THIRD_PARTY_ROLE.equalsIgnoreCase("student")){
			tab_no=3;
		}
		else if(Util.ROLE.equalsIgnoreCase("teacher") &&  Util.THIRD_PARTY_ROLE.equalsIgnoreCase("teacher")){
			tab_no=3;
		}
		else if(Util.ROLE.contains("admin")&& Util.THIRD_PARTY_ROLE.equalsIgnoreCase("teacher"))
		{
			tab_no=3;
		}
		else if(Util.ROLE.contains("teacher")&& Util.THIRD_PARTY_ROLE.contains("admin"))
		{
			tab_no=3;
		}
		else if(Util.THIRD_PARTY_ROLE.equalsIgnoreCase("alumni"))
		{
			tab_no=1;
		}
		else
		{
			tab_no=1;
		}

		return tab_no;
	}
}
