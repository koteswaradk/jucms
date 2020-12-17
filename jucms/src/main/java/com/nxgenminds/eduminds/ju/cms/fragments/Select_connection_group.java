package com.nxgenminds.eduminds.ju.cms.fragments;

import com.nxgenminds.eduminds.ju.cms.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;



public class Select_connection_group extends Fragment {
	private EditText search;
	private ListView listconn;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Get the view from fragmenttab2.xml
		View view = inflater.inflate(R.layout.create_broadcast_add_members, container, false);
		search = (EditText) view.findViewById(R.id.connection_search);
		listconn = (ListView)view.findViewById(R.id.list_connections);
		return view;
	}
}
