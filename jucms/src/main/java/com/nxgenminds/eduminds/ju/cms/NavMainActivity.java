package com.nxgenminds.eduminds.ju.cms;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nxgenminds.eduminds.ju.cms.DiscussionForum.DiscussionForumFragment;
import com.nxgenminds.eduminds.ju.cms.Feedback.FeedBackSystemFragment;
import com.nxgenminds.eduminds.ju.cms.FeedbackAdmin.FeedBackAdminFragment;
import com.nxgenminds.eduminds.ju.cms.Internship.AdminInternship;
import com.nxgenminds.eduminds.ju.cms.Internship.CompanyAdmin;
import com.nxgenminds.eduminds.ju.cms.adapters.NavListAdapter;
import com.nxgenminds.eduminds.ju.cms.admin.Admin;
import com.nxgenminds.eduminds.ju.cms.alumni.Alumni;
import com.nxgenminds.eduminds.ju.cms.attendence.AttendenceFragment;
import com.nxgenminds.eduminds.ju.cms.broadcast_new.BroadcastListFragment_new;
import com.nxgenminds.eduminds.ju.cms.classSchedule.ClassScheduleAdminFragment;
import com.nxgenminds.eduminds.ju.cms.classSchedule.ClassScheduleTabHostFragment;
import com.nxgenminds.eduminds.ju.cms.events.EventsTabHostFragment;
import com.nxgenminds.eduminds.ju.cms.fragments.ConnectionsFragment;
import com.nxgenminds.eduminds.ju.cms.fragments.Landing_Fragment;
import com.nxgenminds.eduminds.ju.cms.fragments.MarksFragment;
import com.nxgenminds.eduminds.ju.cms.fragments.ModulesFragment;
import com.nxgenminds.eduminds.ju.cms.guestAcademics.AcademicsFragment;
import com.nxgenminds.eduminds.ju.cms.guestAdmissions.AdmissionsFragment;
import com.nxgenminds.eduminds.ju.cms.guestEntrnaceTest.EntranceTestResultFragment;
import com.nxgenminds.eduminds.ju.cms.help.HelpTabHost;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.jgigroup.GroupTabMenuActivity;
import com.nxgenminds.eduminds.ju.cms.messages.UserFragmentMessage;
import com.nxgenminds.eduminds.ju.cms.models.NavListModel;
import com.nxgenminds.eduminds.ju.cms.noticeboard.NoticeBoardFragment;
import com.nxgenminds.eduminds.ju.cms.notifications.NotificationsFragment;
import com.nxgenminds.eduminds.ju.cms.placementsboard.PlacementsBoardFragment;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpPostClient;
import com.nxgenminds.eduminds.ju.cms.settings.SettingsFragment;
import com.nxgenminds.eduminds.ju.cms.spirit.SpritListFragment_new;
import com.nxgenminds.eduminds.ju.cms.student.StudentsFragment;
import com.nxgenminds.eduminds.ju.cms.teacher.TeachersFragment;
import com.nxgenminds.eduminds.ju.cms.timefeed.UserFragmentTimeFeed;
import com.nxgenminds.eduminds.ju.cms.userprofile.UserTabMenuActivity;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.NewIteamsChecking;
import com.nxgenminds.eduminds.ju.cms.utils.SessionManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;

import androidx.appcompat.app.AppCompatActivity;

public class NavMainActivity extends AppCompatActivity {
	private float lastTranslate = 0.0f;// for
										// sliding......................................
	private FrameLayout frame;// for
								// sliding......................................
	SessionManager session;
	private TextView nav_user_name;
	public static ImageView nav_user_profile_path;
	private ProgressDialog pDialog;
	// Declare Variables
	DrawerLayout mDrawerLayout;
	ListView mDrawerList;
	ActionBarDrawerToggle mDrawerToggle;
	public List<String> list_menu_name = new ArrayList<String>();
	String navname = "";
	String userID = "";
	String _admin_spirit45 = "admin_spirit45";
	String _admin_timetable = "admin_timetable";
	String _admin_attendence = "admin_attendence";

	private View header;
	private static final String SETTINGS_LOGOUT_APT = Util.API + "logout";
	public static ArrayList<NavListModel> navListModel = new ArrayList<NavListModel>();
	NewIteamsChecking checkingiteams;

	Fragment connectionsFragments = new ConnectionsFragment();
	Fragment notificationsFragments = new NotificationsFragment();
	Fragment messagesFragments = new UserFragmentMessage();
	Fragment broadcastFragments = new BroadcastListFragment_new();
	Fragment settingsFragments = new SettingsFragment();
	Fragment userTimeFeed = new UserFragmentTimeFeed();
	Fragment userEventFragment = new EventsTabHostFragment();
	Fragment feedbacksystemfragment = new FeedBackSystemFragment();
	Fragment marksFragment = new MarksFragment();
	Fragment classScheduleFragment = new ClassScheduleTabHostFragment();
	Fragment modulesFragment = new ModulesFragment();
	Fragment attendenceFragment = new AttendenceFragment();
	Fragment internshipFragment = new AdminInternship();
	Fragment cmpny_internshipFragment = new CompanyAdmin();
	Fragment discussionforumFragment = new DiscussionForumFragment();
	Fragment studentsFragment = new StudentsFragment();
	Fragment teachersFragment = new TeachersFragment();
	// new added fragments
	Fragment adminFragment = new Admin();
	Fragment alumniFragment = new Alumni();
	Fragment classScheduleAdmin = new ClassScheduleAdminFragment();
	Fragment landing = new Landing_Fragment();

	Fragment feedbackAdmin = new FeedBackAdminFragment();
	Fragment help = new HelpTabHost();
	Fragment spirit = new SpritListFragment_new();

	// 3 menus by satish
	Fragment academicsFragment = new AcademicsFragment();
	Fragment admissionsFragment = new AdmissionsFragment();
	Fragment entrancetestFragment = new EntranceTestResultFragment();
	Fragment classScheduleTabHostFragment = new ClassScheduleTabHostFragment();
	Fragment noticeboardFragment=new NoticeBoardFragment();
	Fragment placementsboardFragment=new PlacementsBoardFragment();

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private static CharSequence title;

	// /
	NavListAdapter mMenuAdapter;
	// String title[];
	int[] icon;
	String newtitle[];
	int pos;
	static String test[];
	//
	public ImageLoader imageLoader;
	DisplayImageOptions options;

	private AlertDialogManager alert = new AlertDialogManager();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drawer_main);
		session = new SessionManager(NavMainActivity.this);
		checkingiteams = new NewIteamsChecking(getBaseContext());
		// get intent result
		Intent in = getIntent();
		userID = in.getStringExtra("loginID");
		Log.i("intent user id", userID);

		nav_user_name = (TextView) findViewById(R.id.nav_user_name);
		navListModel.clear();
		initView();
		imageLoader = imageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.placeholder)
				.showImageForEmptyUri(R.drawable.placeholder)
				.showImageOnFail(R.drawable.placeholder).cacheInMemory(true)
				.considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
				.build();
		mTitle = mDrawerTitle = getTitle();

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		mDrawerList = (ListView) findViewById(R.id.listview_drawer);
		frame = (FrameLayout) findViewById(R.id.content_frame);// for
																// sliding......................................
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		mDrawerList.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (visibleItemCount == 0)
					return;
				if (firstVisibleItem != 0)
					return;
				int currentapiVersion = android.os.Build.VERSION.SDK_INT;
				if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {

					nav_user_profile_path.setTranslationY(-mDrawerList
							.getChildAt(0).getTop() / 2);

				}
			}
		});

		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		// Enable ActionBar app icon to behave as action to toggle nav drawer
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setBackgroundDrawable(null);
		getSupportActionBar().setDisplayShowTitleEnabled(true);
		getSupportActionBar().setDisplayUseLogoEnabled(false);
		getSupportActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#000000")));

		getSupportActionBar().setTitle(
				Html.fromHtml("<font color=\"#ffffff\">"
						+ getString(R.string.app_name) + "</font>"));

		// adding header
		header = View.inflate(this, R.layout.header, null);

		// setting the username in the nav item for first row
		nav_user_name = (TextView) header.findViewById(R.id.nav_user_name);
		nav_user_profile_path = (ImageView) header
				.findViewById(R.id.image_header);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			@SuppressLint("NewApi")
			public void onDrawerSlide(View drawerView, float slideOffset) {
				mMenuAdapter.notifyDataSetChanged();
				float moveFactor = (mDrawerList.getWidth() * slideOffset);

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					frame.setTranslationX(moveFactor);
				} else {
					TranslateAnimation anim = new TranslateAnimation(
							lastTranslate, moveFactor, 0.0f, 0.0f);
					anim.setDuration(0);
					anim.setFillAfter(true);
					frame.startAnimation(anim);
					lastTranslate = moveFactor;
				}
			}

			// /// for
			// sliding......................................end.........................................
			public void onDrawerClosed(View view) {
				if (userTimeFeed.isVisible()) {
					getSupportActionBar().setTitle(
							Html.fromHtml("<font color=\"#ffffff\">"
									+ "Timefeed" + "</font>"));
				} else if (connectionsFragments.isVisible()) {
					getSupportActionBar().setTitle(
							Html.fromHtml("<font color=\"#ffffff\">"
									+ "Connections" + "</font>"));
				} else if (notificationsFragments.isVisible()) {
					getSupportActionBar().setTitle(
							Html.fromHtml("<font color=\"#ffffff\">"
									+ "Notifications" + "</font>"));
				} else if (messagesFragments.isVisible()) {
					getSupportActionBar().setTitle(
							Html.fromHtml("<font color=\"#ffffff\">"
									+ "Messages" + "</font>"));
				} else if (broadcastFragments.isVisible()) {
					getSupportActionBar().setTitle(
							Html.fromHtml("<font color=\"#ffffff\">"
									+ "Broadcast" + "</font>"));
				} else if (settingsFragments.isVisible()) {
					getSupportActionBar().setTitle(
							Html.fromHtml("<font color=\"#ffffff\">"
									+ "Settings" + "</font>"));
				} else if (userEventFragment.isVisible()) {
					getSupportActionBar().setTitle(
							Html.fromHtml("<font color=\"#ffffff\">" + "Events"
									+ "</font>"));
				} else if (feedbacksystemfragment.isVisible()) {
					getSupportActionBar().setTitle(
							Html.fromHtml("<font color=\"#ffffff\">"
									+ "Feedback" + "</font>"));
				} else if (marksFragment.isVisible()) {
					getSupportActionBar().setTitle(
							Html.fromHtml("<font color=\"#ffffff\">" + "Marks "
									+ "</font>"));
				} else if (classScheduleFragment.isVisible()) {
					getSupportActionBar().setTitle(
							Html.fromHtml("<font color=\"#ffffff\">"
									+ "Class Schedule" + "</font>"));
				} else if (modulesFragment.isVisible()) {
					getSupportActionBar().setTitle(
							Html.fromHtml("<font color=\"#ffffff\">"
									+ "Modules " + "</font>"));
				} else if (attendenceFragment.isVisible()) {
					getSupportActionBar().setTitle(
							Html.fromHtml("<font color=\"#ffffff\">"
									+ "Attendence " + "</font>"));
				} else if (internshipFragment.isVisible()) {
					getSupportActionBar().setTitle(
							Html.fromHtml("<font color=\"#ffffff\">"
									+ "Internship " + "</font>"));
				} else if (cmpny_internshipFragment.isVisible()) {
					getSupportActionBar().setTitle(
							Html.fromHtml("<font color=\"#ffffff\">"
									+ "Internship " + "</font>"));
				} else if (discussionforumFragment.isVisible()) {
					getSupportActionBar().setTitle(
							Html.fromHtml("<font color=\"#ffffff\">"
									+ "Discussion Forum " + "</font>"));
				} else if (teachersFragment.isVisible()) {
					getSupportActionBar().setTitle(
							Html.fromHtml("<font color=\"#ffffff\">"
									+ "Teachers " + "</font>"));
				} else if (studentsFragment.isVisible()) {
					getSupportActionBar().setTitle(
							Html.fromHtml("<font color=\"#ffffff\">"
									+ "Students " + "</font>"));
				} else if (adminFragment.isVisible()) {
					getSupportActionBar().setTitle(
							Html.fromHtml("<font color=\"#ffffff\">" + "Admin "
									+ "</font>"));
				} else if (classScheduleAdmin.isVisible()) {
					getSupportActionBar().setTitle(
							Html.fromHtml("<font color=\"#ffffff\">"
									+ "Class Schedule" + "</font>"));
				} else if (alumniFragment.isVisible()) {
					getSupportActionBar().setTitle(
							Html.fromHtml("<font color=\"#ffffff\">"
									+ "Alumni " + "</font>"));
				} else if (landing.isVisible()) {
					getSupportActionBar().setTitle(
							Html.fromHtml("<font color=\"#ffffff\">"
									+ "Welcome To JU CMS" + "</font>"));
				} else if (help.isVisible()) {
					getSupportActionBar().setTitle(
							Html.fromHtml("<font color=\"#ffffff\">" + "Help"
									+ "</font>"));
				} else if (spirit.isVisible()) {
					getSupportActionBar().setTitle(
							Html.fromHtml("<font color=\"#ffffff\">"
									+ "Spirit of 45" + "</font>"));
				} else if (feedbackAdmin.isVisible()) {
					getSupportActionBar().setTitle(
							Html.fromHtml("<font color=\"#ffffff\">"
									+ "Feedback" + "</font>"));
				}

				else if (admissionsFragment.isVisible()) {
					getSupportActionBar().setTitle(
							Html.fromHtml("<font color=\"#ffffff\">"
									+ "Admissions" + "</font>"));
				} else if (academicsFragment.isVisible()) {
					getSupportActionBar().setTitle(
							Html.fromHtml("<font color=\"#ffffff\">"
									+ "Academics" + "</font>"));
				} else if (entrancetestFragment.isVisible()) {
					getSupportActionBar().setTitle(
							Html.fromHtml("<font color=\"#ffffff\">"
									+ "Entrance Test Results" + "</font>"));
				} 
				else if (noticeboardFragment.isVisible()) {
					getSupportActionBar().setTitle(
							Html.fromHtml("<font color=\"#ffffff\">"
									+ "Notice Board" + "</font>"));
				}
				else if (placementsboardFragment.isVisible()) {
					getSupportActionBar().setTitle(
							Html.fromHtml("<font color=\"#ffffff\">"
									+ "Placements Board" + "</font>"));
				} else {
					getSupportActionBar().setTitle(
							Html.fromHtml("<font color=\"#ffffff\">"
									+ "JU CMS" + "</font>"));
				}
				super.onDrawerClosed(view);

			}

			public void onDrawerOpened(View drawerView) {
				mDrawerLayout.requestFocus();
				mMenuAdapter.notifyDataSetChanged();

				InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
				inputMethodManager.hideSoftInputFromWindow(getCurrentFocus()
						.getWindowToken(), 0);
				getSupportActionBar().setTitle(
						Html.fromHtml("<font color=\"#ffffff\">" + mDrawerTitle
								+ "</font>"));

				super.onDrawerOpened(drawerView);
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			if (Util.ROLE.equalsIgnoreCase("alumni")
					|| Util.ROLE.equalsIgnoreCase("company admin")
					|| Util.ROLE.equalsIgnoreCase("parent")
					|| Util.ROLE.equalsIgnoreCase("alumni")
					|| Util.ROLE.equalsIgnoreCase("guest")) {
				selectItem("Welcome", 1);
			} else {
				selectItem("Timefeed", 1);
			}
		}

	}// //end oncreate

	@SuppressLint("InlinedApi")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {

			if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
				mDrawerLayout.closeDrawer(mDrawerList);
			} else {
				mDrawerLayout.openDrawer(mDrawerList);
			}
		}

		return super.onOptionsItemSelected(item);
	}

	// ListView click listener in the navigation drawer
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (position > 0) {
				Object object = parent.getItemAtPosition(position);
				NavListModel nav_data = (NavListModel) object;

				navname = nav_data.getMenu_item();
				if (!nav_data.getIs_heading().equalsIgnoreCase("1")) {

					selectItem(nav_data.getMenu_item(), position);
					setTitle(nav_data.getMenu_item());
				}
			} else {
				if (Util.ROLE.equalsIgnoreCase("guest")
						|| Util.ROLE.equalsIgnoreCase("company admin")) {

				} else {
					Util.intership_flag = false;
					Intent intent = new Intent(NavMainActivity.this,
							UserTabMenuActivity.class);
					startActivity(intent);
				}
			}

		}
	}

	private void selectItem(String navname, int position) {

		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
		if (navname.contains("JU CMS")) {
			checkingiteams.clickedpushNotificationNew("group");
			Intent intent = new Intent(NavMainActivity.this,
					GroupTabMenuActivity.class);
			startActivity(intent);
		} else if (navname.contains("Connections")) {
			fragmentTransaction.replace(R.id.content_frame,
					connectionsFragments);
			Util.msgFlag = false;
			Util.eventFlag = false;
			Util.broadcastFlag = false;
		}
		// public notice board
		else if (navname.contains("Notice Board")) {
			
			fragmentTransaction.replace(R.id.content_frame, noticeboardFragment);
			Util.msgFlag = false;
			Util.eventFlag = false;
			Util.broadcastFlag = false;
		}
		// placements notice board
		else if (navname.contains("Placements Board")) {
			
			fragmentTransaction.replace(R.id.content_frame, placementsboardFragment);
			Util.msgFlag = false;
			Util.eventFlag = false;
			Util.broadcastFlag = false;
		}
		// Academics
		else if (navname.contains("Academics")) {
			fragmentTransaction.replace(R.id.content_frame, academicsFragment);
			Util.msgFlag = false;
			Util.eventFlag = false;
			Util.broadcastFlag = false;
		}
		// Admissions
		else if (navname.contains("Admissions")) {
			fragmentTransaction.replace(R.id.content_frame, admissionsFragment);
			Util.msgFlag = false;
			Util.eventFlag = false;
			Util.broadcastFlag = false;
		}
		// Entrance Test Results
		else if (navname.contains("Entrance Test Results")) {
			fragmentTransaction.replace(R.id.content_frame,
					entrancetestFragment);
			Util.msgFlag = false;
			Util.eventFlag = false;
			Util.broadcastFlag = false;
		}

		else if (navname.contains("Notifications")) {
			checkingiteams.clickedpushNotificationNew("notification");
			fragmentTransaction.replace(R.id.content_frame,
					notificationsFragments);
			Util.msgFlag = false;
			Util.eventFlag = false;
			Util.broadcastFlag = false;
		} else if (navname.contains("Admin")) {
			fragmentTransaction.replace(R.id.content_frame, adminFragment);
			Util.msgFlag = false;
			Util.eventFlag = false;
			Util.broadcastFlag = false;
		} else if (navname.contains("Welcome")) {
			fragmentTransaction.replace(R.id.content_frame, landing);
			Util.msgFlag = false;
			Util.eventFlag = false;
			Util.broadcastFlag = false;
		} else if (navname.contains("Alumni")) {
			fragmentTransaction.replace(R.id.content_frame, alumniFragment);
			Util.msgFlag = false;
			Util.eventFlag = false;
			Util.broadcastFlag = false;
		}

		else if (navname.contains("Messages") && Util.msgFlag == false) {
			checkingiteams.clickedpushNotificationNew("message");
			fragmentTransaction.replace(R.id.content_frame, messagesFragments);
			Util.eventFlag = false;
			Util.broadcastFlag = false;
		} else if (navname.contains("Messages") && Util.msgFlag == true) {
			checkingiteams.clickedpushNotificationNew("message");
			Util.eventFlag = false;
			Util.broadcastFlag = false;
		} else if (navname.contains("Broadcast") && Util.broadcastFlag == false) {
			checkingiteams.clickedpushNotificationNew("broadcast");
			fragmentTransaction.replace(R.id.content_frame, broadcastFragments);
			Util.msgFlag = false;
			Util.eventFlag = false;
		} else if (navname.contains("Broadcast") && Util.broadcastFlag == true) {
			checkingiteams.clickedpushNotificationNew("broadcast");
			Util.msgFlag = false;
			Util.eventFlag = false;
		}

		else if (navname.contains("Settings")) {
			fragmentTransaction.replace(R.id.content_frame, settingsFragments);
			Util.msgFlag = false;
			Util.eventFlag = false;
			Util.broadcastFlag = false;
		} else if (navname.contains("Timefeed")) {
			fragmentTransaction.replace(R.id.content_frame, userTimeFeed);
			Util.msgFlag = false;
			Util.eventFlag = false;
			Util.broadcastFlag = false;
		} else if (navname.contains("Events") && Util.eventFlag == false) {
			checkingiteams.clickedpushNotificationNew("event");
			fragmentTransaction.replace(R.id.content_frame, userEventFragment);
			Util.msgFlag = false;
			Util.broadcastFlag = false;
		} else if (navname.contains("Events") && Util.eventFlag == true) {
			checkingiteams.clickedpushNotificationNew("event");
			Util.msgFlag = false;
			Util.broadcastFlag = false;
		} else if (navname.contains("Feedback")) {
			fragmentTransaction.replace(R.id.content_frame, feedbackAdmin);
			Util.msgFlag = false;
			Util.eventFlag = false;
			Util.broadcastFlag = false;
		} else if (navname.contains("Spirit of 45")) {
			fragmentTransaction.replace(R.id.content_frame, spirit);
			Util.msgFlag = false;
			Util.eventFlag = false;
			Util.broadcastFlag = false;
		} else if (navname.contains("Sign Out")) {
			// fragmentTransaction.replace(R.id.content_frame,
			// feedbacksystemfragment); need to change
			// fragmentTransaction.replace(R.id.content_frame,
			// myGroupsFragment);
			Util.msgFlag = false;
			Util.eventFlag = false;
			Util.broadcastFlag = false;
			// new GetLogoutAsync().execute();

			ConnectionDetector conn = new ConnectionDetector(
					NavMainActivity.this);
			if (conn.isConnectingToInternet()) {
				new GCMUnregister().execute();
			} else {
				alert.showAlertDialog(NavMainActivity.this, "Connection Error",
						"Check your Internet Connection", false);
			}
		} else if (navname.contains("Marks")) {
			fragmentTransaction.replace(R.id.content_frame, marksFragment);
			Util.msgFlag = false;
			Util.eventFlag = false;
			Util.broadcastFlag = false;
		} else if (navname.contains("Class Schedule")) {
			Log.e("Navigation main class shedule", Util.ROLE.toString());
			if (Util.ROLE.equalsIgnoreCase("admin")
					|| Util.ROLE.equalsIgnoreCase("sprit45 admin")
					|| Util.ROLE.equalsIgnoreCase("timetable admin")
					|| Util.ROLE.equalsIgnoreCase("attendence admin")) {
				Util.msgFlag = false;
				Util.eventFlag = false;
				Util.broadcastFlag = false;
				// Toast.makeText(getApplicationContext(),
				// "Work in progress for admin", Toast.LENGTH_SHORT).show();
				fragmentTransaction.replace(R.id.content_frame,
						classScheduleAdmin);
			} else {
				fragmentTransaction.replace(R.id.content_frame,
						classScheduleFragment);// classScheduleTabHostFragment
				Util.msgFlag = false;
				Util.eventFlag = false;
				Util.broadcastFlag = false;
			}
		} else if (navname.contains("Modules")) {
			fragmentTransaction.replace(R.id.content_frame, modulesFragment);
			Util.msgFlag = false;
			Util.eventFlag = false;
			Util.broadcastFlag = false;
		} else if (navname.contains("Attendence")) {
			fragmentTransaction.replace(R.id.content_frame, attendenceFragment);
			Util.msgFlag = false;
			Util.eventFlag = false;
			Util.broadcastFlag = false;
		} else if (navname.contains("Internship")) {
			if (Util.ROLE.equalsIgnoreCase("admin")) {
				fragmentTransaction.replace(R.id.content_frame,
						internshipFragment);
				Util.msgFlag = false;
				Util.eventFlag = false;
				Util.broadcastFlag = false;
			} else if (Util.ROLE.equalsIgnoreCase("internship admin")) {
				fragmentTransaction.replace(R.id.content_frame,
						internshipFragment);
				Util.msgFlag = false;
				Util.eventFlag = false;
				Util.broadcastFlag = false;
			} else if (Util.ROLE.equalsIgnoreCase("company admin")) {
				fragmentTransaction.replace(R.id.content_frame,
						cmpny_internshipFragment);
				Util.msgFlag = false;
				Util.eventFlag = false;
				Util.broadcastFlag = false;
			} else {
				Util.msgFlag = false;
				Util.eventFlag = false;
				Util.broadcastFlag = false;
			}
		} else if (navname.contains("Discussion Forum")) {
			fragmentTransaction.replace(R.id.content_frame,
					discussionforumFragment);
			Util.msgFlag = false;
			Util.eventFlag = false;
			Util.broadcastFlag = false;
		}

		else if (navname.contains("Teachers")) {
			fragmentTransaction.replace(R.id.content_frame, teachersFragment);
			Util.msgFlag = false;
			Util.eventFlag = false;
			Util.broadcastFlag = false;
		} else if (navname.contains("Students")) {
			fragmentTransaction.replace(R.id.content_frame, studentsFragment);
			Util.msgFlag = false;
			Util.eventFlag = false;
			Util.broadcastFlag = false;
		} else if (navname.contains("Help")) {
			fragmentTransaction.replace(R.id.content_frame, help);
			Util.msgFlag = false;
			Util.eventFlag = false;
			Util.broadcastFlag = false;
		} else {

		}
		fragmentTransaction.commit();
		mDrawerList.setItemChecked(position, true);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(
				Html.fromHtml("<font color=\"#ffffff\">" + mTitle + "</font>"));
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onBackPressed() {

		if (userTimeFeed.isVisible()) {
			AlertDialog alert_back = new AlertDialog.Builder(this).create();
			alert_back.setTitle("Exit?");
			alert_back.setMessage("Are you sure want to exit?");

			alert_back.setButton("No", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});

			alert_back.setButton2("Yes", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					NavMainActivity.this.finish();
				}
			});
			alert_back.show();

		}

		else if (Util.ROLE.equalsIgnoreCase("alumni")
				|| Util.ROLE.equalsIgnoreCase("company admin")
				|| Util.ROLE.equalsIgnoreCase("parent")
				|| Util.ROLE.equalsIgnoreCase("alumni")
				|| Util.ROLE.equalsIgnoreCase("guest")) {
			selectItem("Welcome", 1);
			setTitle("Welcome To JU CMS");
		} else {
			selectItem("Timefeed", 1);
			setTitle("Timefeed");
		}

	}

	private void initView() {

		ConnectionDetector conn = new ConnectionDetector(NavMainActivity.this);
		if (conn.isConnectingToInternet()) {

			new GettingMenuItemsAsync().execute();

		} else {
			alert.showAlertDialog(NavMainActivity.this, "Connection Error",
					"Check your Internet Connection", false);
		}

	}

	private class GettingMenuItemsAsync extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			if (pDialog == null) {
				pDialog = Util.createProgressDialog(NavMainActivity.this);
				pDialog.setCancelable(false);
				pDialog.show();
			} else {
				pDialog.setCancelable(false);
				pDialog.show();
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			JSONObject menuObject;
			JSONArray menuResponse = null;
			JSONArray userResponse = null;
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(Util.API
					+ "get_user_info");
			if (jsonObjectRecived != null) {

				try {
					userResponse = jsonObjectRecived.getJSONArray("user_info");
					menuResponse = jsonObjectRecived.getJSONArray("menu_items");
					JSONObject userDetails = userResponse.getJSONObject(0);
					Util.user_firstname = userDetails.getString("firstname");
					Util.user_profile_pic = userDetails
							.getString("user_profile_photo");
					Util.USER_ID = userDetails.getString("user_id");

					for (int i = 0; i < menuResponse.length(); i++) {

						NavListModel list = new NavListModel();
						JSONObject menuDetails = menuResponse.getJSONObject(i);
						list.setMenu_item(menuDetails.getString("menu_item"));
						list.setIs_heading(menuDetails.getString("is_heading"));
						navListModel.add(list);
						list_menu_name.add(menuDetails.getString("menu_item"));

					}

					Util.stringArray_Menu_name = list_menu_name
							.toArray(new String[list_menu_name.size()]);
					list_menu_name.clear();// ==================================================================================clear
											// the array for nav list items
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Toast.makeText(getBaseContext(), "server error",
						Toast.LENGTH_SHORT).show();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			pDialog.dismiss();

			nav_user_name.setText(Util.user_firstname);

			imageLoader.displayImage(Util.user_profile_pic,
					nav_user_profile_path, options,
					new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {

						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {

						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {

						}
					}, new ImageLoadingProgressListener() {
						@Override
						public void onProgressUpdate(String imageUri,
								View view, int current, int total) {

						}
					});

			mMenuAdapter = new NavListAdapter(NavMainActivity.this,
					navListModel);

			// Opening the Navlist if Notifications avaiable
			HashMap<String, String> checknav = checkingiteams
					.checknavlistiteams();
			if (checknav.size() > 0) {
				String newnotification = checknav
						.get(NewIteamsChecking.NOTIFICATIONS_PUSH);
				String newmessages = checknav
						.get(NewIteamsChecking.NOTIFICATIONS_MESSAGES);
				String newevents = checknav
						.get(NewIteamsChecking.NOTIFICATIONS_EVENTS);
				String newbroadcast = checknav
						.get(NewIteamsChecking.NOTIFICATIONS_BROADCAST);
				String newgroup = checknav
						.get(NewIteamsChecking.NOTIFICATIONS_GROUP);

				if (newnotification.equalsIgnoreCase("new")
						|| newmessages.equalsIgnoreCase("new")
						|| newevents.equalsIgnoreCase("new")
						|| newbroadcast.equalsIgnoreCase("new")
						|| newgroup.equalsIgnoreCase("new")) {
					mDrawerLayout.openDrawer(mDrawerList);
				}
			}
			mDrawerList.addHeaderView(header);

			mDrawerList.setAdapter(mMenuAdapter);
		}
	}

	class GetLogoutAsync extends AsyncTask<Void, Void, JSONObject> {
		@Override
		protected void onPreExecute() {
			if (pDialog == null) {
				pDialog = Util.createProgressDialog(NavMainActivity.this);
				pDialog.show();
			} else {
				pDialog.show();
			}
		}

		@Override
		protected JSONObject doInBackground(Void... params) {
			JSONObject receivedJsonObject = HttpGetClient
					.sendHttpPost(SETTINGS_LOGOUT_APT);
			return receivedJsonObject;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			pDialog.dismiss();
			if (result != null) {
				try {
					Toast.makeText(NavMainActivity.this,
							result.getString("message"), Toast.LENGTH_LONG)
							.show();
					session.logoutUser();
					clearApplicationData();
					NavMainActivity.this.finish();

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

		public void clearApplicationData() {
			File cache = NavMainActivity.this.getCacheDir();
			File appDir = new File(cache.getParent());
			if (appDir.exists()) {
				String[] children = appDir.list();
				for (String s : children) {
					if (!s.equals("lib")) {
						deleteDir(new File(appDir, s));
					}
				}
			}
		}

		public boolean deleteDir(File dir) {
			if (dir != null && dir.isDirectory()) {
				String[] children = dir.list();
				for (int i = 0; i < children.length; i++) {
					boolean success = deleteDir(new File(dir, children[i]));
					if (!success) {
						return false;
					}
				}
			}

			return dir.delete();
		}
	}

	private class GCMUnregister extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			JSONObject jsonObjSend = new JSONObject();

			try {
				jsonObjSend.put("token", Util.GCM_KEY_STORE);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			HttpPostClient.sendHttpPost(Util.API_PUSH_URL + "/unsubscribe",
					jsonObjSend);

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			ConnectionDetector conn = new ConnectionDetector(
					NavMainActivity.this);
			if (conn.isConnectingToInternet()) {
				new GetLogoutAsync().execute();
			} else {
				alert.showAlertDialog(NavMainActivity.this, "Connection Error",
						"Check your Internet Connection", false);
			}

		}

	}
}
