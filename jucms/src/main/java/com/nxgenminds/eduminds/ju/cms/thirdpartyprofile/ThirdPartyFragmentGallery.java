package com.nxgenminds.eduminds.ju.cms.thirdpartyprofile;

import com.nxgenminds.eduminds.ju.cms.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;



public class ThirdPartyFragmentGallery  extends Fragment{

	private Button viewVideos,viewPhotos;
	private ImageButton addphotosvideos;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.user_fragmenttab_gallery, container, false);
		viewPhotos = (Button) view.findViewById(R.id.userPhotos);
		viewVideos = (Button) view.findViewById(R.id.userVideos);
		addphotosvideos = (ImageButton) view.findViewById(R.id.userAddPhotoVideo);
		viewPhotos.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_button));
		viewVideos.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_button_gray));
		addphotosvideos.setVisibility(View.GONE);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		// by default
		ThirdPartyFragmentTabPhotos photos = new  ThirdPartyFragmentTabPhotos();
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.content_frame_photos,photos);
		//fragmentTransaction.addToBackStack(null);	
		//getChildFragmentManager().popBackStack();
		fragmentTransaction.commit();
		//end
		
		viewVideos.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				viewVideos.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_button));
				viewPhotos.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_button_gray));
				// TODO Auto-generated method stub
				ThirdPartyFragmentTabVideos videosfragment = new  ThirdPartyFragmentTabVideos();
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.content_frame_photos, videosfragment);
				getChildFragmentManager().popBackStack();
				fragmentTransaction.commit();

			}
		});

		viewPhotos.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewPhotos.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_button));
				viewVideos.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_button_gray));
				ThirdPartyFragmentTabPhotos photosfragment = new  ThirdPartyFragmentTabPhotos();
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.content_frame_photos, photosfragment);
				getChildFragmentManager().popBackStack();
				fragmentTransaction.commit();

			}
		});

	}

}
