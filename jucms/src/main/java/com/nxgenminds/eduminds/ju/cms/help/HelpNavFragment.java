package com.nxgenminds.eduminds.ju.cms.help;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class HelpNavFragment extends Fragment{
	private View rooView;
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container,  Bundle savedInstanceState) {
		if(Util.ROLE.equalsIgnoreCase("admin"))
		{
			rooView = inflater.inflate(R.layout.activity_help_admin, container , false);
		}
		else if(Util.ROLE.equalsIgnoreCase("alumni admin"))
		{
			rooView = inflater.inflate(R.layout.activity_help_alumni_admin, container , false);
		}
		else if(Util.ROLE.equalsIgnoreCase("alumni"))
		{
			rooView = inflater.inflate(R.layout.activity_help_alumni, container , false);
		}
		else if(Util.ROLE.equalsIgnoreCase("attendence admin"))
		{
			rooView = inflater.inflate(R.layout.activity_help_attendance_admin, container , false);
		}
		else if(Util.ROLE.equalsIgnoreCase("class monitor"))
		{
			rooView = inflater.inflate(R.layout.activity_help_class_monitor, container , false);
		}
		else if(Util.ROLE.equalsIgnoreCase("feedback admin"))
		{
			rooView = inflater.inflate(R.layout.activity_help_feedback_admin, container , false);
		}
		else if(Util.ROLE.equalsIgnoreCase("internship admin"))
		{
			rooView = inflater.inflate(R.layout.activity_help_internship_admin, container , false);
		}
		else if(Util.ROLE.equalsIgnoreCase("module admin"))
		{
			rooView = inflater.inflate(R.layout.activity_help_module_admin, container , false);
		}
		else if(Util.ROLE.equalsIgnoreCase("parent"))
		{
			rooView = inflater.inflate(R.layout.activity_help_parent, container , false);
		}
		else if(Util.ROLE.equalsIgnoreCase("student"))
		{
			rooView = inflater.inflate(R.layout.activity_help_student, container , false);
		}
		else if(Util.ROLE.equalsIgnoreCase("teacher"))
		{
			rooView = inflater.inflate(R.layout.activity_help_teacher, container , false);
		}
		else if(Util.ROLE.equalsIgnoreCase("sprit45 admin"))
		{
			rooView = inflater.inflate(R.layout.activity_spirit_45_admin, container , false);
		}
		else if(Util.ROLE.equalsIgnoreCase("guest"))
		{
			rooView = inflater.inflate(R.layout.activity_help_guest, container , false);
		}
		else
		{
			rooView = inflater.inflate(R.layout.activity_help_guest, container , false);

		}
		return rooView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

}
