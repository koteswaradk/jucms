package com.nxgenminds.eduminds.ju.cms.fragments;

import com.nxgenminds.eduminds.ju.cms.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class Landing_Fragment extends Fragment{

	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container,  Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_landing, container, false);
		return rootView;
	}



}
