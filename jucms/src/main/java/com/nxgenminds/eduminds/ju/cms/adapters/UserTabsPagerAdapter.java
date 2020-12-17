package com.nxgenminds.eduminds.ju.cms.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.nxgenminds.eduminds.ju.cms.userprofile.UserAttendence;
import com.nxgenminds.eduminds.ju.cms.userprofile.UserFragmentTabGallery;
import com.nxgenminds.eduminds.ju.cms.userprofile.UserFragmentTabTimelog;
import com.nxgenminds.eduminds.ju.cms.userprofile.UserFragmentTabUserdetails;
import com.nxgenminds.eduminds.ju.cms.userprofile.UserInternshipFragment;
import com.nxgenminds.eduminds.ju.cms.utils.Util;

public class UserTabsPagerAdapter extends FragmentPagerAdapter {
	private static  int return_value;
	public UserTabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}
	@Override
	public Fragment getItem(int index) {
		if(Util.ROLE.equalsIgnoreCase("class monitor"))
		{
			switch (index) {
			case 0:
				return new UserFragmentTabUserdetails();
			case 1:
				return new UserFragmentTabTimelog();
			case 2:
				return new UserAttendence(); 
			case 3:
				return new UserInternshipFragment();
			}
		}
		else if(Util.ROLE.equalsIgnoreCase("attendence admin")){
			switch (index) {
			case 0:
				return new UserFragmentTabUserdetails();
			case 1:
				return new UserFragmentTabTimelog();
			case 2:
				return new UserFragmentTabGallery();
			}		
		}
		else if(Util.ROLE.equalsIgnoreCase("alumni")){
			switch (index) {
			case 0:
				return new UserFragmentTabUserdetails();
			}		
		}
		else if(Util.ROLE.equalsIgnoreCase("feedback admin")){
			switch (index) {
			case 0:
				return new UserFragmentTabUserdetails();
			case 1:
				return new UserFragmentTabTimelog();
			case 2:
				return new UserFragmentTabGallery();
			}		
		}
		else if(Util.ROLE.equalsIgnoreCase("internship admin")){
			switch (index) {
			case 0:
				return new UserFragmentTabUserdetails();
			case 1:
				return new UserFragmentTabTimelog();
			case 2:
				return new UserFragmentTabGallery();
			}		
		}
		else if(Util.ROLE.equalsIgnoreCase("admin")){
			Log.i("sample inside UserTabPageradapterAdmin", "display");
			switch (index) {
			case 0:
				return new UserFragmentTabUserdetails();
			case 1:
				return new UserFragmentTabTimelog();
			case 2:
				Log.i("sample inside UserTabPagerAdminGallary", "display");
				return new UserFragmentTabGallery();
			}		
		}
		else if(Util.ROLE.equalsIgnoreCase("sprit45 admin")){
			switch (index) {
			case 0:
				return new UserFragmentTabUserdetails();
			case 1:
				return new UserFragmentTabTimelog();
			case 2:
				return new UserFragmentTabGallery();
			}		
		}
		else if(Util.ROLE.equalsIgnoreCase("student")){
			switch (index) {
			case 0:
				return new UserFragmentTabUserdetails();
			case 1:
				return new UserAttendence();
			case 2:
				return new UserInternshipFragment(); 
			}		
		}
		else if(Util.ROLE.equalsIgnoreCase("teacher")){
			switch (index) {
			case 0:
				return new UserFragmentTabUserdetails();
			case 1:
				return new UserFragmentTabTimelog();
			case 2:
				return new UserFragmentTabGallery();
			}	
		}
		else if(Util.ROLE.equalsIgnoreCase("timetable admin")){
			switch (index) {
			case 0:
				return new UserFragmentTabUserdetails();
			case 1:
				return new UserFragmentTabTimelog();
			case 2:
				return new UserFragmentTabGallery();
			}	
		}

		else if(Util.ROLE.equalsIgnoreCase("alumni admin")){
			switch (index) {
			case 0:
				return new UserFragmentTabUserdetails();
			case 1:
				return new UserFragmentTabTimelog();
			case 2:
				return new UserFragmentTabGallery();
			}		
		}

		else if(Util.ROLE.equalsIgnoreCase("parent")){

			switch (index) {
			case 0:
				return new UserFragmentTabUserdetails();
			case 1:
				return new UserAttendence();
			case 2:
				return new UserInternshipFragment(); 
			}		

		}
		else if(Util.ROLE.equalsIgnoreCase("guest")){

			switch (index) {
			case 0:
				return new UserFragmentTabUserdetails();
			}		
		}
		else if(Util.ROLE.equalsIgnoreCase("company admin")){

			switch (index) {
			case 0:
				return new UserFragmentTabUserdetails();
			}		
		}

		else if(Util.ROLE.equalsIgnoreCase("office admin")){
			switch (index) {
			case 0:
				return new UserFragmentTabUserdetails();
			case 1:
				return new UserFragmentTabTimelog();
			case 2:
				return new UserFragmentTabGallery();
			}		
		}
		else if(Util.ROLE.equalsIgnoreCase("office staff")){
			switch (index) {
			case 0:
				return new UserFragmentTabUserdetails();
			}		
		}

		return null;
	}

	@Override
	public int getCount() {
		if(Util.ROLE.equalsIgnoreCase("class monitor"))
		{
			return_value=4;
		}
		else if(Util.ROLE.equalsIgnoreCase("attendence admin")){
			return_value=3;
		}
		else if(Util.ROLE.equalsIgnoreCase("alumni")){
			return_value=1;
		}
		else if(Util.ROLE.equalsIgnoreCase("feedback admin")){
			return_value=3;
		}
		else if(Util.ROLE.equalsIgnoreCase("internship admin")){
			return_value=3;
		}
		else if(Util.ROLE.equalsIgnoreCase("admin")){
			return_value=3;
		}
		else if(Util.ROLE.equalsIgnoreCase("sprit45 admin")){
			return_value=3;
		}
		else if(Util.ROLE.equalsIgnoreCase("student")){
			return_value=3;
		}
		else if(Util.ROLE.equalsIgnoreCase("teacher")){
			return_value=3;	
		}
		else if(Util.ROLE.equalsIgnoreCase("timetable admin")){
			return_value=3;
		}
		else if(Util.ROLE.equalsIgnoreCase("alumni admin")){
			return_value=3;
		}
		else if(Util.ROLE.equalsIgnoreCase("parent")){
			return_value=3;
		}
		else if(Util.ROLE.equalsIgnoreCase("guest")){
			return_value=1;
		}
		else if(Util.ROLE.equalsIgnoreCase("company admin")){
			return_value=1;
		}
		else if(Util.ROLE.equalsIgnoreCase("office staff")){
			return_value=1;
		}
		else if(Util.ROLE.equalsIgnoreCase("office admin")){
			return_value=3;
		}
		return return_value;

	}
}
