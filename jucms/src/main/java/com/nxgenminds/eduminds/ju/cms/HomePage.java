package com.nxgenminds.eduminds.ju.cms;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.nxgenminds.eduminds.ju.cms.guestLogin.GuestLogin;
import com.nxgenminds.eduminds.ju.cms.guestLogin.GuestRegistration;
import com.nxgenminds.eduminds.ju.cms.userLogin.MemberLogin;
import com.nxgenminds.eduminds.ju.cms.utils.SessionManager;


public class HomePage extends ActionBarActivity {

	private Button memberLogin,guestLogin;
	private TextView homeNewMember,powredby;
	// Session Manager Class
	SessionManager session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		initView();

		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();

		memberLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(HomePage.this,MemberLogin.class);
				startActivity(intent);
			}
		});

		guestLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(HomePage.this,GuestLogin.class);
				startActivity(intent);
			}
		});

		homeNewMember.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomePage.this,GuestRegistration.class);
				startActivity(intent);
			}
		});

		powredby.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.yoobikwiti.com"));
				startActivity(browserIntent);
			}
		});
	}

	private void initView() {
		// TODO Auto-generated method stub
		memberLogin = (Button) findViewById(R.id.homeMemberLogin);
		guestLogin = (Button) findViewById(R.id.homeGuestLogin);
		homeNewMember = (TextView)findViewById(R.id.homeNewMember);
		powredby = (TextView) findViewById(R.id.powredby);
		Typeface typeFace=Typeface.createFromAsset(getAssets(),"fonts/BentonSans-Medium.otf");
		memberLogin.setTypeface(typeFace);
		guestLogin.setTypeface(typeFace);
		homeNewMember.setTypeface(typeFace);
	}

}
