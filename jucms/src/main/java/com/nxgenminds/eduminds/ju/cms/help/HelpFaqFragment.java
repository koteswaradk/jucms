package com.nxgenminds.eduminds.ju.cms.help;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class HelpFaqFragment extends Fragment{
	FaqAdapter listAdapter;
	ExpandableListView expListView;
	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild;
	TextView noData;
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container,  Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_faq, container, false);
		// get the listview
		expListView = (ExpandableListView) rootView.findViewById(R.id.faq_list);
		noData=(TextView)rootView.findViewById(R.id.noFaq);
		// preparing list data
		if(Util.ROLE.equalsIgnoreCase("admin"))
		{
			prepareListDataAdmin();
			noData.setVisibility(View.GONE);
			listAdapter = new FaqAdapter(getActivity(), listDataHeader, listDataChild);
			// setting list adapter
			expListView.setAdapter(listAdapter);
		}
		else if(Util.ROLE.equalsIgnoreCase("alumni admin"))
		{
			prepareListData_AluminiAdmin();
			noData.setVisibility(View.GONE);
			listAdapter = new FaqAdapter(getActivity(), listDataHeader, listDataChild);
			// setting list adapter
			expListView.setAdapter(listAdapter);

		}
		else if(Util.ROLE.equalsIgnoreCase("internship admin"))
		{
			prepareListData_InternshipAdmin();
			noData.setVisibility(View.GONE);
			listAdapter = new FaqAdapter(getActivity(), listDataHeader, listDataChild);
			// setting list adapter
			expListView.setAdapter(listAdapter);

		}
		else if(Util.ROLE.equalsIgnoreCase("attendance"))
		{
			prepareListData_AttendanceAdmin();
			noData.setVisibility(View.GONE);
			listAdapter = new FaqAdapter(getActivity(), listDataHeader, listDataChild);
			// setting list adapter
			expListView.setAdapter(listAdapter);

		}
		else if(Util.ROLE.equalsIgnoreCase("feedback admin"))
		{
			FeedBack();
			noData.setVisibility(View.GONE);
			listAdapter = new FaqAdapter(getActivity(), listDataHeader, listDataChild);
			// setting list adapter
			expListView.setAdapter(listAdapter);

		}
		else if(Util.ROLE.equalsIgnoreCase("sprit45 admin"))
		{
			SpiritOf45();
			noData.setVisibility(View.GONE);
			listAdapter = new FaqAdapter(getActivity(), listDataHeader, listDataChild);
			// setting list adapter
			expListView.setAdapter(listAdapter);

		}
		else if(Util.ROLE.equalsIgnoreCase("teacher"))
		{
			teacher();
			noData.setVisibility(View.GONE);
			listAdapter = new FaqAdapter(getActivity(), listDataHeader, listDataChild);
			// setting list adapter
			expListView.setAdapter(listAdapter);

		}
		else if(Util.ROLE.equalsIgnoreCase("class monitor"))
		{
			ClassMonitor();
			noData.setVisibility(View.GONE);
			listAdapter = new FaqAdapter(getActivity(), listDataHeader, listDataChild);
			// setting list adapter
			expListView.setAdapter(listAdapter);

		}
		else if(Util.ROLE.equalsIgnoreCase("student"))
		{
			Student();
			noData.setVisibility(View.GONE);
			listAdapter = new FaqAdapter(getActivity(), listDataHeader, listDataChild);
			// setting list adapter
			expListView.setAdapter(listAdapter);

		}
		else if(Util.ROLE.equalsIgnoreCase("alumni"))
		{
			Alumni();	
			noData.setVisibility(View.GONE);
			listAdapter = new FaqAdapter(getActivity(), listDataHeader, listDataChild);
			// setting list adapter
			expListView.setAdapter(listAdapter);

		}
		else if(Util.ROLE.equalsIgnoreCase("company admin"))
		{
			CompanyAdmin();
			noData.setVisibility(View.GONE);
			listAdapter = new FaqAdapter(getActivity(), listDataHeader, listDataChild);
			// setting list adapter
			expListView.setAdapter(listAdapter);

		}
		else if(Util.ROLE.equalsIgnoreCase("parent"))
		{
			Parent();
			noData.setVisibility(View.GONE);
			listAdapter = new FaqAdapter(getActivity(), listDataHeader, listDataChild);
			// setting list adapter
			expListView.setAdapter(listAdapter);

		}
		else if(Util.ROLE.equalsIgnoreCase("guest"))
		{
			noData.setVisibility(View.VISIBLE);
		}
		else
		{
			prepareListData_Common();
			noData.setVisibility(View.GONE);
			listAdapter = new FaqAdapter(getActivity(), listDataHeader, listDataChild);
			// setting list adapter
			expListView.setAdapter(listAdapter);

		}

		return rootView;
	}
	/*
	 * Preparing the list data
	 */
	private void prepareListData_Common() {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();

		// Adding child data
		listDataHeader.add("Who can use the JU CMS App?");
		listDataHeader.add("What is Timefeed?");
		listDataHeader.add("How do I create an Event? ");

		listDataHeader.add("Can I change my profile Photo? ");
		listDataHeader.add("How do I change my profile photo?");
		listDataHeader.add("What type of content can I post on the Timefeed?");

		listDataHeader.add("Can I delete my posts? ");
		listDataHeader.add("Can I change my email address?");
		listDataHeader.add("How do I change my email address?");

		listDataHeader.add("What is broadcast? ");
		listDataHeader.add("What is organisation profile? ");
		listDataHeader.add("What is Timelog? ");

		listDataHeader.add("What is Gallery?");
		listDataHeader.add("What is notification?");
		listDataHeader.add("What is discussion forum?");

		listDataHeader.add("What are photos & videos? ");

		// Adding child data
		List<String> a= new ArrayList<String>();
		a.add("All the users with user id and guest login credentials can use the JU CMS.");

		List<String> b= new ArrayList<String>();
		b.add("Timefeed is the centralized feed. Posts from all the JU CMS App users (who have posting option) will appear here.");

		List<String> c= new ArrayList<String>();
		c.add(" *Applicable only for  users with login credentials permissible for event creation. To create an event, select the 'Events' option in the side menu.  Add Event opens. Fill the details and click on 'Save'. Now your event is created.");

		List<String> d= new ArrayList<String>();
		d.add("Yes you can change your profile photo.");

		List<String> e= new ArrayList<String>();
		e.add("Select the profile photo on the side menu. You will be directed to your own profile.Now select the edit icon.Users can select from Gallery or take a click through Camera. If you select Gallery you will be redirected to your phone's album, select desired photo and click save. If you select Camera your phone camera opens, click a photo and save. Once you save your new photo is reflected in the profile. Finally click on Done. ");

		List<String> f= new ArrayList<String>();
		f.add("If you are an user with credentials that allows you to post, then you can post photos, videos and text updates on Timefeed. ");

		List<String> g= new ArrayList<String>();
		g.add("Yes, you can only delete the post that you have made. You can either go to Timelog or Timefeed and delete the posts made by you. ");

		List<String> h= new ArrayList<String>();
		h.add("Yes you can change your email address. ");

		List<String> i= new ArrayList<String>();
		i.add("Select the profile photo from side menu. You will be redirected to your own profile. Select the edit button, edit your email address and click on Done to save the changes.  ");

		List<String> j= new ArrayList<String>();
		j.add(" *Applicable only for  users with login credentials permissible for broadcast creation. It is a listing of broadcast messages.Broadcast is a way to send single message to multiple people at one go.");

		List<String> k= new ArrayList<String>();
		k.add("An organisation profile is the profile of JU CMS. It has all the details of the institution including the contact information. ");

		List<String> l= new ArrayList<String>();
		l.add("Timelog is a repository of all your previous posts. ");

		List<String> m= new ArrayList<String>();
		m.add("Gallery is a repository of videos and photos you posted on Timefeed. ");

		List<String> n= new ArrayList<String>();
		n.add("Notifications are any information received by you from another user of the app or the app itself. ");

		List<String> o= new ArrayList<String>();
		o.add("Discussion Forum is a place where you can create topics related to academics, current affairs etc. Also, you can post questions regarding them. You can even post answers on questions other people have posted. ");

		List<String> p= new ArrayList<String>();
		p.add("Photos and videos are part of Gallery. All the photos and videos posted by you on timefeed will be reposited here. ");


		listDataChild.put(listDataHeader.get(0), a); 
		listDataChild.put(listDataHeader.get(1), b);
		listDataChild.put(listDataHeader.get(2), c);

		listDataChild.put(listDataHeader.get(3), d); 
		listDataChild.put(listDataHeader.get(4), e);
		listDataChild.put(listDataHeader.get(5), f);

		listDataChild.put(listDataHeader.get(6), g); 
		listDataChild.put(listDataHeader.get(7), h);
		listDataChild.put(listDataHeader.get(8), i);

		listDataChild.put(listDataHeader.get(9), j); 
		listDataChild.put(listDataHeader.get(10), k);
		listDataChild.put(listDataHeader.get(11), l);

		listDataChild.put(listDataHeader.get(12), m); 
		listDataChild.put(listDataHeader.get(13), n);
		listDataChild.put(listDataHeader.get(14), o);

		listDataChild.put(listDataHeader.get(15), p); 
	}

	private void prepareListDataAdmin() {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();

		// Adding child data
		listDataHeader.add("Who can use the JU CMS App?");
		listDataHeader.add("What is Timefeed?");
		listDataHeader.add("How do I create an Event? ");

		listDataHeader.add("Can I change my profile Photo? ");
		listDataHeader.add("How do I change my profile photo?");
		listDataHeader.add("What type of content can I post on the Timefeed?");

		listDataHeader.add("Can I delete my posts? ");
		listDataHeader.add("Can I change my email address?");
		listDataHeader.add("How do I change my email address?");

		listDataHeader.add("What is broadcast? ");
		listDataHeader.add("What is organisation profile? ");
		listDataHeader.add("What is Timelog? ");

		listDataHeader.add("What is Gallery?");
		listDataHeader.add("What is notification?");
		listDataHeader.add("What is discussion forum?");
		listDataHeader.add("What are photos & videos? ");
		//admin
		listDataHeader.add("Whom can I send messages? ");
		listDataHeader.add("What is Teachers?");
		listDataHeader.add("What is Students ?");
		listDataHeader.add("What is Admin?");
		listDataHeader.add("What is Alumni?");
		listDataHeader.add("What can I do in the Class Schedule section? ");
		listDataHeader.add("What is feedback? ");
		listDataHeader.add("What is internship? ");
		listDataHeader.add("What is Spirit of 45? ");
		listDataHeader.add("How do I send a broadcast? ");





		// Adding child data
		List<String> a= new ArrayList<String>();
		a.add("All the users with user id and guest login credentials can use the JU CMS app.");

		List<String> b= new ArrayList<String>();
		b.add("Timefeed is the centralized feed. Posts from all the JU CMS App users (who have posting option) will appear here.");

		List<String> c= new ArrayList<String>();
		c.add(" *Applicable only for  users with login credentials permissible for event creation. To create an event, select the 'Events' option in the side menu.  Add Event opens. Fill the details and click on 'Save'. Now your event is created.");

		List<String> d= new ArrayList<String>();
		d.add("Yes you can change your profile photo.");

		List<String> e= new ArrayList<String>();
		e.add("Select the profile photo on the side menu. You will be directed to your own profile.Now select the edit icon.Users can select from Gallery or take a click through Camera. If you select Gallery you will be redirected to your phone's album, select desired photo and click save. If you select Camera your phone camera opens, click a photo and save. Once you save your new photo is reflected in the profile. Finally click on Done. ");

		List<String> f= new ArrayList<String>();
		f.add("If you are an user with credentials that allows you to post, then you can post photos, videos and text updates on Timefeed. ");

		List<String> g= new ArrayList<String>();
		g.add("Yes, you can only delete the post that you have made. You can either go to Timelog or Timefeed and delete the posts made by you. ");

		List<String> h= new ArrayList<String>();
		h.add("Yes you can change your email address. ");

		List<String> i= new ArrayList<String>();
		i.add("Select the profile photo from side menu. You will be redirected to your own profile. Select the edit button, edit your email address and click on Done to save the changes.  ");

		List<String> j= new ArrayList<String>();
		j.add(" *Applicable only for  users with login credentials permissible for broadcast creation. It is a listing of broadcast messages.Broadcast is a way to send single message to multiple people at one go.");

		List<String> k= new ArrayList<String>();
		k.add("An organisation profile is the profile of JU CMS. It has all the details of the institution including the contact information. ");

		List<String> l= new ArrayList<String>();
		l.add("Timelog is a repository of all your previous posts. ");

		List<String> m= new ArrayList<String>();
		m.add("Gallery is a repository of videos and photos you posted on Timefeed. ");

		List<String> n= new ArrayList<String>();
		n.add("Notifications are any information received by you from another user of the app or the app itself. ");

		List<String> o= new ArrayList<String>();
		o.add("Discussion Forum is a place where you can create topics related to academics, current affairs etc. Also, you can post questions regarding them. You can even post answers on questions other people have posted. ");

		List<String> p= new ArrayList<String>();
		p.add("Photos and videos are part of Gallery. All the photos and videos posted by you on timefeed will be reposited here. ");
		// admin
		List<String> q= new ArrayList<String>();
		q.add("As a main admin you can send messages to all other admins, all teachers, students and class monitors.");

		List<String> r= new ArrayList<String>();
		r.add("It is a listing of all the Teachers in the institution (JU CMS)");

		List<String> s= new ArrayList<String>();
		s.add("It is a listing of all the Students in the institution (JU CMS).");

		List<String> t= new ArrayList<String>();
		t.add("It is a listing of all the Admins in the institution (JU CMS).");

		List<String> u= new ArrayList<String>();
		u.add("It is a listing of all the Alumni of the institution (JU CMS).");

		List<String> v= new ArrayList<String>();
		v.add("To view Class Schedule, select particular stream, semester, section and date. ");

		List<String> w= new ArrayList<String>();
		w.add("Under Feedback is the list of previously provided feedbacks. Click on individual feeback to view stream, semester, section for which feedback is provided. Click on individual stream, sem & section to view the teacher, subject and percentage is shown. Once you click on individual teacher, each feedback question with the grade is displayed. Click on individual circle to find a pop up with the stats for particular question. ");

		List<String> x= new ArrayList<String>();
		x.add("Once yoe select Internship you can view list of companies offering internship. When you click on individual company name then you can view student currently working at and who have previous worked in that company. ");

		List<String> y= new ArrayList<String>();
		y.add("Here you can view various Spirit of 45 class schedules. ");

		List<String> z= new ArrayList<String>();
		z.add("Select Broadcast option in the side menu, then select the edit icon on left corner.Create broadcast page opens. In this form, first select if you want the message to be custom sent or public, write your message and  click on Broadcast button.");




		listDataChild.put(listDataHeader.get(0), a); 
		listDataChild.put(listDataHeader.get(1), b);
		listDataChild.put(listDataHeader.get(2), c);

		listDataChild.put(listDataHeader.get(3), d); 
		listDataChild.put(listDataHeader.get(4), e);
		listDataChild.put(listDataHeader.get(5), f);

		listDataChild.put(listDataHeader.get(6), g); 
		listDataChild.put(listDataHeader.get(7), h);
		listDataChild.put(listDataHeader.get(8), i);

		listDataChild.put(listDataHeader.get(9), j); 
		listDataChild.put(listDataHeader.get(10), k);
		listDataChild.put(listDataHeader.get(11), l);

		listDataChild.put(listDataHeader.get(12), m); 
		listDataChild.put(listDataHeader.get(13), n);
		listDataChild.put(listDataHeader.get(14), o);

		listDataChild.put(listDataHeader.get(15), p); 
		//admin
		listDataChild.put(listDataHeader.get(16), q); 
		listDataChild.put(listDataHeader.get(17), r); 
		listDataChild.put(listDataHeader.get(18), s); 
		listDataChild.put(listDataHeader.get(19), t); 
		listDataChild.put(listDataHeader.get(20), u); 
		listDataChild.put(listDataHeader.get(21), v); 
		listDataChild.put(listDataHeader.get(22), w); 
		listDataChild.put(listDataHeader.get(23), x); 
		listDataChild.put(listDataHeader.get(24), y); 
		listDataChild.put(listDataHeader.get(25), z); 
	}

	private void prepareListData_AluminiAdmin() {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();

		// Adding child data
		listDataHeader.add("Who can use the JU CMS App?");
		listDataHeader.add("What is Timefeed?");
		listDataHeader.add("How do I create an Event? ");

		listDataHeader.add("Can I change my profile Photo? ");
		listDataHeader.add("How do I change my profile photo?");
		listDataHeader.add("What type of content can I post on the Timefeed?");

		listDataHeader.add("Can I delete my posts? ");
		listDataHeader.add("Can I change my email address?");
		listDataHeader.add("How do I change my email address?");

		listDataHeader.add("What is broadcast? ");
		listDataHeader.add("What is organisation profile? ");
		listDataHeader.add("What is Timelog? ");

		listDataHeader.add("What is Gallery?");
		listDataHeader.add("What is notification?");
		listDataHeader.add("What is discussion forum?");

		listDataHeader.add("What are photos & videos? ");

		//alumni admin
		listDataHeader.add("Whom can I send messages? ");
		listDataHeader.add("What is Teachers?");
		listDataHeader.add("What is Students ?");
		listDataHeader.add("What is Admin?");
		listDataHeader.add("What is Alumni?");


		// Adding child data
		List<String> a= new ArrayList<String>();
		a.add("All the users with user id and guest login credentials can use the JU CMS app.");

		List<String> b= new ArrayList<String>();
		b.add("Timefeed is the centralized feed. Posts from all the JU CMS App users (who have posting option) will appear here.");

		List<String> c= new ArrayList<String>();
		c.add(" *Applicable only for  users with login credentials permissible for event creation. To create an event, select the 'Events' option in the side menu.  Add Event opens. Fill the details and click on 'Save'. Now your event is created.");

		List<String> d= new ArrayList<String>();
		d.add("Yes you can change your profile photo.");

		List<String> e= new ArrayList<String>();
		e.add("Select the profile photo on the side menu. You will be directed to your own profile.Now select the edit icon.Users can select from Gallery or take a click through Camera. If you select Gallery you will be redirected to your phone's album, select desired photo and click save. If you select Camera your phone camera opens, click a photo and save. Once you save your new photo is reflected in the profile. Finally click on Done. ");

		List<String> f= new ArrayList<String>();
		f.add("If you are an user with credentials that allows you to post, then you can post photos, videos and text updates on Timefeed. ");

		List<String> g= new ArrayList<String>();
		g.add("Yes, you can only delete the post that you have made. You can either go to Timelog or Timefeed and delete the posts made by you. ");

		List<String> h= new ArrayList<String>();
		h.add("Yes you can change your email address. ");

		List<String> i= new ArrayList<String>();
		i.add("Select the profile photo from side menu. You will be redirected to your own profile. Select the edit button, edit your email address and click on Done to save the changes.  ");

		List<String> j= new ArrayList<String>();
		j.add(" *Applicable only for  users with login credentials permissible for broadcast creation. It is a listing of broadcast messages.Broadcast is a way to send single message to multiple people at one go.");

		List<String> k= new ArrayList<String>();
		k.add("An organisation profile is the profile of JU CMS. It has all the details of the institution including the contact information. ");

		List<String> l= new ArrayList<String>();
		l.add("Timelog is a repository of all your previous posts. ");

		List<String> m= new ArrayList<String>();
		m.add("Gallery is a repository of videos and photos you posted on Timefeed. ");

		List<String> n= new ArrayList<String>();
		n.add("Notifications are any information received by you from another user of the app or the app itself. ");

		List<String> o= new ArrayList<String>();
		o.add("Discussion Forum is a place where you can create topics related to academics, current affairs etc. Also, you can post questions regarding them. You can even post answers on questions other people have posted. ");

		List<String> p= new ArrayList<String>();
		p.add("Photos and videos are part of Gallery. All the photos and videos posted by you on timefeed will be reposited here. ");

		//alumni admin

		List<String> q= new ArrayList<String>();
		q.add("As an alumni admin you can send messages to all other admins, all teachers, students, class monitors and alumni. ");

		List<String> r= new ArrayList<String>();
		r.add("It is a listing of all the Teachers in the institution (JU CMS)");

		List<String> s= new ArrayList<String>();
		s.add("It is a listing of all the Students in the institution (JU CMS).");

		List<String> t= new ArrayList<String>();
		t.add("It is a listing of all the Admins in the institution (JU CMS).");

		List<String> u= new ArrayList<String>();
		u.add("It is a listing of all the Alumni of the institution (JU CMS).");

		listDataChild.put(listDataHeader.get(0), a); 
		listDataChild.put(listDataHeader.get(1), b);
		listDataChild.put(listDataHeader.get(2), c);

		listDataChild.put(listDataHeader.get(3), d); 
		listDataChild.put(listDataHeader.get(4), e);
		listDataChild.put(listDataHeader.get(5), f);

		listDataChild.put(listDataHeader.get(6), g); 
		listDataChild.put(listDataHeader.get(7), h);
		listDataChild.put(listDataHeader.get(8), i);

		listDataChild.put(listDataHeader.get(9), j); 
		listDataChild.put(listDataHeader.get(10), k);
		listDataChild.put(listDataHeader.get(11), l);

		listDataChild.put(listDataHeader.get(12), m); 
		listDataChild.put(listDataHeader.get(13), n);
		listDataChild.put(listDataHeader.get(14), o);

		listDataChild.put(listDataHeader.get(15), p); 

		listDataChild.put(listDataHeader.get(16), q); 
		listDataChild.put(listDataHeader.get(17), r); 
		listDataChild.put(listDataHeader.get(18), s); 
		listDataChild.put(listDataHeader.get(19), t); 
		listDataChild.put(listDataHeader.get(20), u); 

	}

	private void prepareListData_InternshipAdmin() {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();

		// Adding child data
		listDataHeader.add("Who can use the JU CMS App?");
		listDataHeader.add("What is Timefeed?");
		listDataHeader.add("How do I create an Event? ");

		listDataHeader.add("Can I change my profile Photo? ");
		listDataHeader.add("How do I change my profile photo?");
		listDataHeader.add("What type of content can I post on the Timefeed?");

		listDataHeader.add("Can I delete my posts? ");
		listDataHeader.add("Can I change my email address?");
		listDataHeader.add("How do I change my email address?");

		listDataHeader.add("What is broadcast? ");
		listDataHeader.add("What is organisation profile? ");
		listDataHeader.add("What is Timelog? ");

		listDataHeader.add("What is Gallery?");
		listDataHeader.add("What is notification?");
		listDataHeader.add("What is discussion forum?");

		listDataHeader.add("What are photos & videos? ");

		//internship admin
		listDataHeader.add("Whom can I send messages? ");
		listDataHeader.add("What is Teachers?");
		listDataHeader.add("What is Students ?");
		listDataHeader.add("What is Admin?");


		listDataHeader.add("Who is Company Admin?");
		listDataHeader.add("What is Class Schedule? ");



		// Adding child data
		List<String> a= new ArrayList<String>();
		a.add("All the users with user id and guest login credentials can use the JU CMS app.");

		List<String> b= new ArrayList<String>();
		b.add("Timefeed is the centralized feed. Posts from all the JU CMS App users (who have posting option) will appear here.");

		List<String> c= new ArrayList<String>();
		c.add(" *Applicable only for  users with login credentials permissible for event creation. To create an event, select the 'Events' option in the side menu.  Add Event opens. Fill the details and click on 'Save'. Now your event is created.");

		List<String> d= new ArrayList<String>();
		d.add("Yes you can change your profile photo.");

		List<String> e= new ArrayList<String>();
		e.add("Select the profile photo on the side menu. You will be directed to your own profile.Now select the edit icon.Users can select from Gallery or take a click through Camera. If you select Gallery you will be redirected to your phone's album, select desired photo and click save. If you select Camera your phone camera opens, click a photo and save. Once you save your new photo is reflected in the profile. Finally click on Done. ");

		List<String> f= new ArrayList<String>();
		f.add("If you are an user with credentials that allows you to post, then you can post photos, videos and text updates on Timefeed. ");

		List<String> g= new ArrayList<String>();
		g.add("Yes, you can only delete the post that you have made. You can either go to Timelog or Timefeed and delete the posts made by you. ");

		List<String> h= new ArrayList<String>();
		h.add("Yes you can change your email address. ");

		List<String> i= new ArrayList<String>();
		i.add("Select the profile photo from side menu. You will be redirected to your own profile. Select the edit button, edit your email address and click on Done to save the changes.  ");

		List<String> j= new ArrayList<String>();
		j.add(" *Applicable only for  users with login credentials permissible for broadcast creation. It is a listing of broadcast messages.Broadcast is a way to send single message to multiple people at one go.");

		List<String> k= new ArrayList<String>();
		k.add("An organisation profile is the profile of JU CMS. It has all the details of the institution including the contact information. ");

		List<String> l= new ArrayList<String>();
		l.add("Timelog is a repository of all your previous posts. ");

		List<String> m= new ArrayList<String>();
		m.add("Gallery is a repository of videos and photos you posted on Timefeed. ");

		List<String> n= new ArrayList<String>();
		n.add("Notifications are any information received by you from another user of the app or the app itself. ");

		List<String> o= new ArrayList<String>();
		o.add("Discussion Forum is a place where you can create topics related to academics, current affairs etc. Also, you can post questions regarding them. You can even post answers on questions other people have posted. ");

		List<String> p= new ArrayList<String>();
		p.add("Photos and videos are part of Gallery. All the photos and videos posted by you on timefeed will be reposited here. ");

		//internship admin

		List<String> q= new ArrayList<String>();
		q.add("As an internship admin you can send messages to all other admins, all teachers, students, class monitors and company admin. ");

		List<String> r= new ArrayList<String>();
		r.add("It is a listing of all the Teachers in the institution (JU CMS)");

		List<String> s= new ArrayList<String>();
		s.add("It is a listing of all the Students in the institution (JU CMS).");

		List<String> t= new ArrayList<String>();
		t.add("It is a listing of all the Admins in the institution (JU CMS).");



		List<String> u= new ArrayList<String>();
		u.add("An admin incharge of all the students for an internship from the company side is called Company Admin. The Company Admin can check the learning outcome and marks for the students. ");

		List<String> v= new ArrayList<String>();
		v.add("Here you can view current day's as well as current week's schedule.");

		listDataChild.put(listDataHeader.get(0), a); 
		listDataChild.put(listDataHeader.get(1), b);
		listDataChild.put(listDataHeader.get(2), c);

		listDataChild.put(listDataHeader.get(3), d); 
		listDataChild.put(listDataHeader.get(4), e);
		listDataChild.put(listDataHeader.get(5), f);

		listDataChild.put(listDataHeader.get(6), g); 
		listDataChild.put(listDataHeader.get(7), h);
		listDataChild.put(listDataHeader.get(8), i);

		listDataChild.put(listDataHeader.get(9), j); 
		listDataChild.put(listDataHeader.get(10), k);
		listDataChild.put(listDataHeader.get(11), l);

		listDataChild.put(listDataHeader.get(12), m); 
		listDataChild.put(listDataHeader.get(13), n);
		listDataChild.put(listDataHeader.get(14), o);

		listDataChild.put(listDataHeader.get(15), p); 

		listDataChild.put(listDataHeader.get(16), q); 
		listDataChild.put(listDataHeader.get(17), r); 
		listDataChild.put(listDataHeader.get(18), s); 
		listDataChild.put(listDataHeader.get(19), t); 
		listDataChild.put(listDataHeader.get(20), u); 
		listDataChild.put(listDataHeader.get(21), v); 

	}

	private void prepareListData_AttendanceAdmin() {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();

		// Adding child data
		listDataHeader.add("Who can use the JU CMS App?");
		listDataHeader.add("What is Timefeed?");
		listDataHeader.add("How do I create an Event? ");

		listDataHeader.add("Can I change my profile Photo? ");
		listDataHeader.add("How do I change my profile photo?");
		listDataHeader.add("What type of content can I post on the Timefeed?");

		listDataHeader.add("Can I delete my posts? ");
		listDataHeader.add("Can I change my email address?");
		listDataHeader.add("How do I change my email address?");

		listDataHeader.add("What is broadcast? ");
		listDataHeader.add("What is organisation profile? ");
		listDataHeader.add("What is Timelog? ");

		listDataHeader.add("What is Gallery?");
		listDataHeader.add("What is notification?");
		listDataHeader.add("What is discussion forum?");

		listDataHeader.add("What are photos & videos? ");

		//attendance admin
		listDataHeader.add("Whom can I send messages? ");
		listDataHeader.add("What is Teachers?");
		listDataHeader.add("What is Students ?");
		listDataHeader.add("What is Admin?");
		listDataHeader.add("What is Class Schedule? ");



		// Adding child data
		List<String> a= new ArrayList<String>();
		a.add("All the users with user id and guest login credentials can use the JU CMS app.");

		List<String> b= new ArrayList<String>();
		b.add("Timefeed is the centralized feed. Posts from all the JU CMS App users (who have posting option) will appear here.");

		List<String> c= new ArrayList<String>();
		c.add(" *Applicable only for  users with login credentials permissible for event creation. To create an event, select the 'Events' option in the side menu.  Add Event opens. Fill the details and click on 'Save'. Now your event is created.");

		List<String> d= new ArrayList<String>();
		d.add("Yes you can change your profile photo.");

		List<String> e= new ArrayList<String>();
		e.add("Select the profile photo on the side menu. You will be directed to your own profile.Now select the edit icon.Users can select from Gallery or take a click through Camera. If you select Gallery you will be redirected to your phone's album, select desired photo and click save. If you select Camera your phone camera opens, click a photo and save. Once you save your new photo is reflected in the profile. Finally click on Done. ");

		List<String> f= new ArrayList<String>();
		f.add("If you are an user with credentials that allows you to post, then you can post photos, videos and text updates on Timefeed. ");

		List<String> g= new ArrayList<String>();
		g.add("Yes, you can only delete the post that you have made. You can either go to Timelog or Timefeed and delete the posts made by you. ");

		List<String> h= new ArrayList<String>();
		h.add("Yes you can change your email address. ");

		List<String> i= new ArrayList<String>();
		i.add("Select the profile photo from side menu. You will be redirected to your own profile. Select the edit button, edit your email address and click on Done to save the changes.  ");

		List<String> j= new ArrayList<String>();
		j.add(" *Applicable only for  users with login credentials permissible for broadcast creation. It is a listing of broadcast messages.Broadcast is a way to send single message to multiple people at one go.");

		List<String> k= new ArrayList<String>();
		k.add("An organisation profile is the profile of JU CMS. It has all the details of the institution including the contact information. ");

		List<String> l= new ArrayList<String>();
		l.add("Timelog is a repository of all your previous posts. ");

		List<String> m= new ArrayList<String>();
		m.add("Gallery is a repository of videos and photos you posted on Timefeed. ");

		List<String> n= new ArrayList<String>();
		n.add("Notifications are any information received by you from another user of the app or the app itself. ");

		List<String> o= new ArrayList<String>();
		o.add("Discussion Forum is a place where you can create topics related to academics, current affairs etc. Also, you can post questions regarding them. You can even post answers on questions other people have posted. ");

		List<String> p= new ArrayList<String>();
		p.add("Photos and videos are part of Gallery. All the photos and videos posted by you on timefeed will be reposited here. ");

		//attendance admin

		List<String> q= new ArrayList<String>();
		q.add("As an attendance admin you can send messages to all other admins, all teachers, students and class monitors.");

		List<String> r= new ArrayList<String>();
		r.add("It is a listing of all the Teachers in the institution (JU CMS)");

		List<String> s= new ArrayList<String>();
		s.add("It is a listing of all the Students in the institution (JU CMS).");

		List<String> t= new ArrayList<String>();
		t.add("It is a listing of all the Admins in the institution (JU CMS).");


		List<String> u= new ArrayList<String>();
		u.add("Here you can view current day's as well as current week's class schedule.");


		listDataChild.put(listDataHeader.get(0), a); 
		listDataChild.put(listDataHeader.get(1), b);
		listDataChild.put(listDataHeader.get(2), c);

		listDataChild.put(listDataHeader.get(3), d); 
		listDataChild.put(listDataHeader.get(4), e);
		listDataChild.put(listDataHeader.get(5), f);

		listDataChild.put(listDataHeader.get(6), g); 
		listDataChild.put(listDataHeader.get(7), h);
		listDataChild.put(listDataHeader.get(8), i);

		listDataChild.put(listDataHeader.get(9), j); 
		listDataChild.put(listDataHeader.get(10), k);
		listDataChild.put(listDataHeader.get(11), l);

		listDataChild.put(listDataHeader.get(12), m); 
		listDataChild.put(listDataHeader.get(13), n);
		listDataChild.put(listDataHeader.get(14), o);

		listDataChild.put(listDataHeader.get(15), p); 

		listDataChild.put(listDataHeader.get(16), q); 
		listDataChild.put(listDataHeader.get(17), r); 
		listDataChild.put(listDataHeader.get(18), s); 
		listDataChild.put(listDataHeader.get(19), t); 
		listDataChild.put(listDataHeader.get(20), u); 

	}

	private void SpiritOf45() {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();

		// Adding child data
		listDataHeader.add("Who can use the JU CMS App?");
		listDataHeader.add("What is Timefeed?");
		listDataHeader.add("How do I create an Event? ");

		listDataHeader.add("Can I change my profile Photo? ");
		listDataHeader.add("How do I change my profile photo?");
		listDataHeader.add("What type of content can I post on the Timefeed?");

		listDataHeader.add("Can I delete my posts? ");
		listDataHeader.add("Can I change my email address?");
		listDataHeader.add("How do I change my email address?");

		listDataHeader.add("What is broadcast? ");
		listDataHeader.add("What is organisation profile? ");
		listDataHeader.add("What is Timelog? ");

		listDataHeader.add("What is Gallery?");
		listDataHeader.add("What is notification?");
		listDataHeader.add("What is discussion forum?");

		listDataHeader.add("What are photos & videos? ");

		//attendance admin
		listDataHeader.add("Whom can I send messages? ");
		listDataHeader.add("What is Teachers?");
		listDataHeader.add("What is Students ?");
		listDataHeader.add("What is Admin?");
		listDataHeader.add("What is Class Schedule? ");



		// Adding child data
		List<String> a= new ArrayList<String>();
		a.add("All the users with user id and guest login credentials can use the JU CMS app.");

		List<String> b= new ArrayList<String>();
		b.add("Timefeed is the centralized feed. Posts from all the JU CMS App users (who have posting option) will appear here.");

		List<String> c= new ArrayList<String>();
		c.add(" *Applicable only for  users with login credentials permissible for event creation. To create an event, select the 'Events' option in the side menu.  Add Event opens. Fill the details and click on 'Save'. Now your event is created.");

		List<String> d= new ArrayList<String>();
		d.add("Yes you can change your profile photo.");

		List<String> e= new ArrayList<String>();
		e.add("Select the profile photo on the side menu. You will be directed to your own profile.Now select the edit icon.Users can select from Gallery or take a click through Camera. If you select Gallery you will be redirected to your phone's album, select desired photo and click save. If you select Camera your phone camera opens, click a photo and save. Once you save your new photo is reflected in the profile. Finally click on Done. ");

		List<String> f= new ArrayList<String>();
		f.add("If you are an user with credentials that allows you to post, then you can post photos, videos and text updates on Timefeed. ");

		List<String> g= new ArrayList<String>();
		g.add("Yes, you can only delete the post that you have made. You can either go to Timelog or Timefeed and delete the posts made by you. ");

		List<String> h= new ArrayList<String>();
		h.add("Yes you can change your email address. ");

		List<String> i= new ArrayList<String>();
		i.add("Select the profile photo from side menu. You will be redirected to your own profile. Select the edit button, edit your email address and click on Done to save the changes.  ");

		List<String> j= new ArrayList<String>();
		j.add(" *Applicable only for  users with login credentials permissible for broadcast creation. It is a listing of broadcast messages.Broadcast is a way to send single message to multiple people at one go.");

		List<String> k= new ArrayList<String>();
		k.add("An organisation profile is the profile of JU CMS. It has all the details of the institution including the contact information. ");

		List<String> l= new ArrayList<String>();
		l.add("Timelog is a repository of all your previous posts. ");

		List<String> m= new ArrayList<String>();
		m.add("Gallery is a repository of videos and photos you posted on Timefeed. ");

		List<String> n= new ArrayList<String>();
		n.add("Notifications are any information received by you from another user of the app or the app itself. ");

		List<String> o= new ArrayList<String>();
		o.add("Discussion Forum is a place where you can create topics related to academics, current affairs etc. Also, you can post questions regarding them. You can even post answers on questions other people have posted. ");

		List<String> p= new ArrayList<String>();
		p.add("Photos and videos are part of Gallery. All the photos and videos posted by you on timefeed will be reposited here. ");

		//attendance admin

		List<String> q= new ArrayList<String>();
		q.add("As a Spirit of 45 admin you can send messages to all other admins, all teachers, students and class monitor");

		List<String> r= new ArrayList<String>();
		r.add("It is a listing of all the Teachers in the institution (JU CMS)");

		List<String> s= new ArrayList<String>();
		s.add("It is a listing of all the Students in the institution (JU CMS).");

		List<String> t= new ArrayList<String>();
		t.add("It is a listing of all the Admins in the institution (JU CMS).");

		List<String> u= new ArrayList<String>();
		u.add("Here you can view current day's as well as current week's class schedule.");


		listDataChild.put(listDataHeader.get(0), a); 
		listDataChild.put(listDataHeader.get(1), b);
		listDataChild.put(listDataHeader.get(2), c);

		listDataChild.put(listDataHeader.get(3), d); 
		listDataChild.put(listDataHeader.get(4), e);
		listDataChild.put(listDataHeader.get(5), f);

		listDataChild.put(listDataHeader.get(6), g); 
		listDataChild.put(listDataHeader.get(7), h);
		listDataChild.put(listDataHeader.get(8), i);

		listDataChild.put(listDataHeader.get(9), j); 
		listDataChild.put(listDataHeader.get(10), k);
		listDataChild.put(listDataHeader.get(11), l);

		listDataChild.put(listDataHeader.get(12), m); 
		listDataChild.put(listDataHeader.get(13), n);
		listDataChild.put(listDataHeader.get(14), o);

		listDataChild.put(listDataHeader.get(15), p); 

		listDataChild.put(listDataHeader.get(16), q); 
		listDataChild.put(listDataHeader.get(17), r); 
		listDataChild.put(listDataHeader.get(18), s); 
		listDataChild.put(listDataHeader.get(19), t); 
		listDataChild.put(listDataHeader.get(20), u); 

	}

	private void FeedBack() {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();

		// Adding child data
		listDataHeader.add("Who can use the JU CMS App?");
		listDataHeader.add("What is Timefeed?");
		listDataHeader.add("How do I create an Event? ");

		listDataHeader.add("Can I change my profile Photo? ");
		listDataHeader.add("How do I change my profile photo?");
		listDataHeader.add("What type of content can I post on the Timefeed?");

		listDataHeader.add("Can I delete my posts? ");
		listDataHeader.add("Can I change my email address?");
		listDataHeader.add("How do I change my email address?");

		listDataHeader.add("What is broadcast? ");
		listDataHeader.add("What is organisation profile? ");
		listDataHeader.add("What is Timelog? ");

		listDataHeader.add("What is Gallery?");
		listDataHeader.add("What is notification?");
		listDataHeader.add("What is discussion forum?");

		listDataHeader.add("What are photos & videos? ");

		//attendance admin
		listDataHeader.add("Whom can I send messages? ");
		listDataHeader.add("What is Teachers?");
		listDataHeader.add("What is Students ?");
		listDataHeader.add("What is Admin?");
		listDataHeader.add("What is feedback? ");



		// Adding child data
		List<String> a= new ArrayList<String>();
		a.add("All the users with user id and guest login credentials can use the JU CMS app.");

		List<String> b= new ArrayList<String>();
		b.add("Timefeed is the centralized feed. Posts from all the JU CMS App users (who have posting option) will appear here.");

		List<String> c= new ArrayList<String>();
		c.add(" *Applicable only for  users with login credentials permissible for event creation. To create an event, select the 'Events' option in the side menu.  Add Event opens. Fill the details and click on 'Save'. Now your event is created.");

		List<String> d= new ArrayList<String>();
		d.add("Yes you can change your profile photo.");

		List<String> e= new ArrayList<String>();
		e.add("Select the profile photo on the side menu. You will be directed to your own profile.Now select the edit icon.Users can select from Gallery or take a click through Camera. If you select Gallery you will be redirected to your phone's album, select desired photo and click save. If you select Camera your phone camera opens, click a photo and save. Once you save your new photo is reflected in the profile. Finally click on Done. ");

		List<String> f= new ArrayList<String>();
		f.add("If you are an user with credentials that allows you to post, then you can post photos, videos and text updates on Timefeed. ");

		List<String> g= new ArrayList<String>();
		g.add("Yes, you can only delete the post that you have made. You can either go to Timelog or Timefeed and delete the posts made by you. ");

		List<String> h= new ArrayList<String>();
		h.add("Yes you can change your email address. ");

		List<String> i= new ArrayList<String>();
		i.add("Select the profile photo from side menu. You will be redirected to your own profile. Select the edit button, edit your email address and click on Done to save the changes.  ");

		List<String> j= new ArrayList<String>();
		j.add(" *Applicable only for  users with login credentials permissible for broadcast creation. It is a listing of broadcast messages.Broadcast is a way to send single message to multiple people at one go.");

		List<String> k= new ArrayList<String>();
		k.add("An organisation profile is the profile of JU CMS. It has all the details of the institution including the contact information. ");

		List<String> l= new ArrayList<String>();
		l.add("Timelog is a repository of all your previous posts. ");

		List<String> m= new ArrayList<String>();
		m.add("Gallery is a repository of videos and photos you posted on Timefeed. ");

		List<String> n= new ArrayList<String>();
		n.add("Notifications are any information received by you from another user of the app or the app itself. ");

		List<String> o= new ArrayList<String>();
		o.add("Discussion Forum is a place where you can create topics related to academics, current affairs etc. Also, you can post questions regarding them. You can even post answers on questions other people have posted. ");

		List<String> p= new ArrayList<String>();
		p.add("Photos and videos are part of Gallery. All the photos and videos posted by you on timefeed will be reposited here. ");

		//attendance admin

		List<String> q= new ArrayList<String>();
		q.add("As a feedback admin you can send messages to all other admins, all teachers, students and class monitors. ");

		List<String> r= new ArrayList<String>();
		r.add("It is a listing of all the Teachers in the institution (JU CMS)");

		List<String> s= new ArrayList<String>();
		s.add("It is a listing of all the Students in the institution (JU CMS).");

		List<String> t= new ArrayList<String>();
		t.add("It is a listing of all the Admins in the institution (JU CMS).");

		List<String> u= new ArrayList<String>();
		u.add("As an admin you can view feedback provided by students for a particular teacher and for a particular subject. ");


		listDataChild.put(listDataHeader.get(0), a); 
		listDataChild.put(listDataHeader.get(1), b);
		listDataChild.put(listDataHeader.get(2), c);

		listDataChild.put(listDataHeader.get(3), d); 
		listDataChild.put(listDataHeader.get(4), e);
		listDataChild.put(listDataHeader.get(5), f);

		listDataChild.put(listDataHeader.get(6), g); 
		listDataChild.put(listDataHeader.get(7), h);
		listDataChild.put(listDataHeader.get(8), i);

		listDataChild.put(listDataHeader.get(9), j); 
		listDataChild.put(listDataHeader.get(10), k);
		listDataChild.put(listDataHeader.get(11), l);

		listDataChild.put(listDataHeader.get(12), m); 
		listDataChild.put(listDataHeader.get(13), n);
		listDataChild.put(listDataHeader.get(14), o);

		listDataChild.put(listDataHeader.get(15), p); 

		listDataChild.put(listDataHeader.get(16), q); 
		listDataChild.put(listDataHeader.get(17), r); 
		listDataChild.put(listDataHeader.get(18), s); 
		listDataChild.put(listDataHeader.get(19), t); 
		listDataChild.put(listDataHeader.get(20), u); 

	}
	private void teacher() {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();

		// Adding child data
		listDataHeader.add("Who can use the JU CMS App?");
		listDataHeader.add("What is Timefeed?");
		listDataHeader.add("How do I create an Event? ");

		listDataHeader.add("Can I change my profile Photo? ");
		listDataHeader.add("How do I change my profile photo?");
		listDataHeader.add("What type of content can I post on the Timefeed?");

		listDataHeader.add("Can I delete my posts? ");
		listDataHeader.add("Can I change my email address?");
		listDataHeader.add("How do I change my email address?");

		listDataHeader.add("What is broadcast? ");
		listDataHeader.add("What is organisation profile? ");
		listDataHeader.add("What is Timelog? ");

		listDataHeader.add("What is Gallery?");
		listDataHeader.add("What is notification?");
		listDataHeader.add("What is discussion forum?");

		listDataHeader.add("What are photos & videos? ");

		//attendance admin
		listDataHeader.add("Whom can I send messages? ");
		listDataHeader.add("What is Teachers?");
		listDataHeader.add("What is Students ?");
		listDataHeader.add("What is Admin?");

		listDataHeader.add("What is Class Schedule? ");
		listDataHeader.add("What is Modules?  ");


		// Adding child data
		List<String> a= new ArrayList<String>();
		a.add("All the users with user id and guest login credentials can use the JU CMS app.");

		List<String> b= new ArrayList<String>();
		b.add("Timefeed is the centralized feed. Posts from all the JU CMS App users (who have posting option) will appear here.");

		List<String> c= new ArrayList<String>();
		c.add(" *Applicable only for  users with login credentials permissible for event creation. To create an event, select the 'Events' option in the side menu.  Add Event opens. Fill the details and click on 'Save'. Now your event is created.");

		List<String> d= new ArrayList<String>();
		d.add("Yes you can change your profile photo.");

		List<String> e= new ArrayList<String>();
		e.add("Select the profile photo on the side menu. You will be directed to your own profile.Now select the edit icon.Users can select from Gallery or take a click through Camera. If you select Gallery you will be redirected to your phone's album, select desired photo and click save. If you select Camera your phone camera opens, click a photo and save. Once you save your new photo is reflected in the profile. Finally click on Done. ");

		List<String> f= new ArrayList<String>();
		f.add("If you are an user with credentials that allows you to post, then you can post photos, videos and text updates on Timefeed. ");

		List<String> g= new ArrayList<String>();
		g.add("Yes, you can only delete the post that you have made. You can either go to Timelog or Timefeed and delete the posts made by you. ");

		List<String> h= new ArrayList<String>();
		h.add("Yes you can change your email address. ");

		List<String> i= new ArrayList<String>();
		i.add("Select the profile photo from side menu. You will be redirected to your own profile. Select the edit button, edit your email address and click on Done to save the changes.  ");

		List<String> j= new ArrayList<String>();
		j.add(" *Applicable only for  users with login credentials permissible for broadcast creation. It is a listing of broadcast messages.Broadcast is a way to send single message to multiple people at one go.");

		List<String> k= new ArrayList<String>();
		k.add("An organisation profile is the profile of JU CMS. It has all the details of the institution including the contact information. ");

		List<String> l= new ArrayList<String>();
		l.add("Timelog is a repository of all your previous posts. ");

		List<String> m= new ArrayList<String>();
		m.add("Gallery is a repository of videos and photos you posted on Timefeed. ");

		List<String> n= new ArrayList<String>();
		n.add("Notifications are any information received by you from another user of the app or the app itself. ");

		List<String> o= new ArrayList<String>();
		o.add("Discussion Forum is a place where you can create topics related to academics, current affairs etc. Also, you can post questions regarding them. You can even post answers on questions other people have posted. ");

		List<String> p= new ArrayList<String>();
		p.add("Photos and videos are part of Gallery. All the photos and videos posted by you on timefeed will be reposited here. ");

		//attendance admin

		List<String> q= new ArrayList<String>();
		q.add("As a Teachers admin you can send messages to all other admins, all teachers, students and class monitors.");

		List<String> r= new ArrayList<String>();
		r.add("It is a listing of all the Teachers in the institution (JU CMS)");

		List<String> s= new ArrayList<String>();
		s.add("It is a listing of all the Students in the institution (JU CMS).");

		List<String> t= new ArrayList<String>();
		t.add("It is a listing of all the Admins in the institution (JU CMS).");

		List<String> u= new ArrayList<String>();
		u.add("Here you can view current day's as well as current week's class schedule.");

		List<String> v= new ArrayList<String>();
		v.add("Modules are the documents related to subjects uploaded by the Module Admin. ");

		listDataChild.put(listDataHeader.get(0), a); 
		listDataChild.put(listDataHeader.get(1), b);
		listDataChild.put(listDataHeader.get(2), c);

		listDataChild.put(listDataHeader.get(3), d); 
		listDataChild.put(listDataHeader.get(4), e);
		listDataChild.put(listDataHeader.get(5), f);

		listDataChild.put(listDataHeader.get(6), g); 
		listDataChild.put(listDataHeader.get(7), h);
		listDataChild.put(listDataHeader.get(8), i);

		listDataChild.put(listDataHeader.get(9), j); 
		listDataChild.put(listDataHeader.get(10), k);
		listDataChild.put(listDataHeader.get(11), l);

		listDataChild.put(listDataHeader.get(12), m); 
		listDataChild.put(listDataHeader.get(13), n);
		listDataChild.put(listDataHeader.get(14), o);

		listDataChild.put(listDataHeader.get(15), p); 

		listDataChild.put(listDataHeader.get(16), q); 
		listDataChild.put(listDataHeader.get(17), r); 
		listDataChild.put(listDataHeader.get(18), s); 
		listDataChild.put(listDataHeader.get(19), t); 
		listDataChild.put(listDataHeader.get(20), u); 

		listDataChild.put(listDataHeader.get(21), v); 

	}

	private void ClassMonitor() {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();

		// Adding child data
		listDataHeader.add("Who can use the JU CMS App?");
		listDataHeader.add("What is Timefeed?");
		listDataHeader.add("How do I create an Event? ");

		listDataHeader.add("Can I change my profile Photo? ");
		listDataHeader.add("How do I change my profile photo?");
		listDataHeader.add("What type of content can I post on the Timefeed?");

		listDataHeader.add("Can I delete my posts? ");
		listDataHeader.add("Can I change my email address?");
		listDataHeader.add("How do I change my email address?");

		listDataHeader.add("What is broadcast? ");
		listDataHeader.add("What is organisation profile? ");
		listDataHeader.add("What is Timelog? ");

		listDataHeader.add("What is Gallery?");
		listDataHeader.add("What is notification?");
		listDataHeader.add("What is discussion forum?");

		listDataHeader.add("What are photos & videos? ");

		//attendance admin
		listDataHeader.add("Whom can I send messages? ");
		listDataHeader.add("What is Teachers?");
		listDataHeader.add("What is Students ?");
		listDataHeader.add("What is Admin?");

		listDataHeader.add("What is internship?  ");
		listDataHeader.add("What is learning outcome? "); 
		listDataHeader.add(" Can I post on Timefeed?  "); 

		// Adding child data
		List<String> a= new ArrayList<String>();
		a.add("All the users with user id and guest login credentials can use the JU CMS app.");

		List<String> b= new ArrayList<String>();
		b.add("Timefeed is the centralized feed. Posts from all the JU CMS App users (who have posting option) will appear here.");

		List<String> c= new ArrayList<String>();
		c.add(" *Applicable only for  users with login credentials permissible for event creation. To create an event, select the 'Events' option in the side menu.  Add Event opens. Fill the details and click on 'Save'. Now your event is created.");

		List<String> d= new ArrayList<String>();
		d.add("Yes you can change your profile photo.");

		List<String> e= new ArrayList<String>();
		e.add("Select the profile photo on the side menu. You will be directed to your own profile.Now select the edit icon.Users can select from Gallery or take a click through Camera. If you select Gallery you will be redirected to your phone's album, select desired photo and click save. If you select Camera your phone camera opens, click a photo and save. Once you save your new photo is reflected in the profile. Finally click on Done. ");

		List<String> f= new ArrayList<String>();
		f.add("If you are an user with credentials that allows you to post, then you can post photos, videos and text updates on Timefeed. ");

		List<String> g= new ArrayList<String>();
		g.add("Yes, you can only delete the post that you have made. You can either go to Timelog or Timefeed and delete the posts made by you. ");

		List<String> h= new ArrayList<String>();
		h.add("Yes you can change your email address. ");

		List<String> i= new ArrayList<String>();
		i.add("Select the profile photo from side menu. You will be redirected to your own profile. Select the edit button, edit your email address and click on Done to save the changes.  ");

		List<String> j= new ArrayList<String>();
		j.add(" *Applicable only for  users with login credentials permissible for broadcast creation. It is a listing of broadcast messages.Broadcast is a way to send single message to multiple people at one go.");

		List<String> k= new ArrayList<String>();
		k.add("An organisation profile is the profile of JU CMS. It has all the details of the institution including the contact information. ");

		List<String> l= new ArrayList<String>();
		l.add("Timelog is a repository of all your previous posts. ");

		List<String> m= new ArrayList<String>();
		m.add("Gallery is a repository of videos and photos you posted on Timefeed. ");

		List<String> n= new ArrayList<String>();
		n.add("Notifications are any information received by you from another user of the app or the app itself. ");

		List<String> o= new ArrayList<String>();
		o.add("Discussion Forum is a place where you can create topics related to academics, current affairs etc. Also, you can post questions regarding them. You can even post answers on questions other people have posted. ");

		List<String> p= new ArrayList<String>();
		p.add("Photos and videos are part of Gallery. All the photos and videos posted by you on timefeed will be reposited here. ");

		//attendance admin

		List<String> q= new ArrayList<String>();
		q.add("As a Class monitor admin you can send messages to all other admins, all teachers, students and class monitors.");

		List<String> r= new ArrayList<String>();
		r.add("It is a listing of all the Teachers in the institution (JU CMS)");

		List<String> s= new ArrayList<String>();
		s.add("It is a listing of all the Students in the institution (JU CMS).");

		List<String> t= new ArrayList<String>();
		t.add("It is a listing of all the Admins in the institution (JU CMS).");

		List<String> u= new ArrayList<String>();
		u.add("It is a listing of your current and previous internships along with learning outcome. Your internship can be veiwed by teachers, admins and your parents. ");

		List<String> v= new ArrayList<String>();
		v.add("Learning outcome is the jobsheet for that particuar internship. ");

		List<String> w= new ArrayList<String>();
		w.add("Yes, as a class monitor you can post on timefeed");

		listDataChild.put(listDataHeader.get(0), a); 
		listDataChild.put(listDataHeader.get(1), b);
		listDataChild.put(listDataHeader.get(2), c);

		listDataChild.put(listDataHeader.get(3), d); 
		listDataChild.put(listDataHeader.get(4), e);
		listDataChild.put(listDataHeader.get(5), f);

		listDataChild.put(listDataHeader.get(6), g); 
		listDataChild.put(listDataHeader.get(7), h);
		listDataChild.put(listDataHeader.get(8), i);

		listDataChild.put(listDataHeader.get(9), j); 
		listDataChild.put(listDataHeader.get(10), k);
		listDataChild.put(listDataHeader.get(11), l);

		listDataChild.put(listDataHeader.get(12), m); 
		listDataChild.put(listDataHeader.get(13), n);
		listDataChild.put(listDataHeader.get(14), o);

		listDataChild.put(listDataHeader.get(15), p); 

		listDataChild.put(listDataHeader.get(16), q); 
		listDataChild.put(listDataHeader.get(17), r); 
		listDataChild.put(listDataHeader.get(18), s); 
		listDataChild.put(listDataHeader.get(19), t); 
		listDataChild.put(listDataHeader.get(20), u); 

		listDataChild.put(listDataHeader.get(21), v); 
		listDataChild.put(listDataHeader.get(22), w); 

	}

	private void Student() {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();

		// Adding child data
		listDataHeader.add("Who can use the JU CMS App?");
		listDataHeader.add("What is Timefeed?");
		listDataHeader.add("How do I create an Event? ");

		listDataHeader.add("Can I change my profile Photo? ");
		listDataHeader.add("How do I change my profile photo?");
		listDataHeader.add("What type of content can I post on the Timefeed?");

		listDataHeader.add("Can I delete my posts? ");
		listDataHeader.add("Can I change my email address?");
		listDataHeader.add("How do I change my email address?");

		listDataHeader.add("What is broadcast? ");
		listDataHeader.add("What is organisation profile? ");
		listDataHeader.add("What is Timelog? ");

		listDataHeader.add("What is Gallery?");
		listDataHeader.add("What is notification?");
		listDataHeader.add("What is discussion forum?");

		listDataHeader.add("What are photos & videos? ");

		//attendance admin
		listDataHeader.add("Whom can I send messages? ");
		listDataHeader.add("What is Teachers?");
		listDataHeader.add("What is Students ?");
		listDataHeader.add("What is Admin?");

		listDataHeader.add("What is internship?  ");
		listDataHeader.add("What is learning outcome? "); 
		listDataHeader.add(" Can I post on Timefeed?  "); 

		// Adding child data
		List<String> a= new ArrayList<String>();
		a.add("All the users with user id and guest login credentials can use the JU CMS app.");

		List<String> b= new ArrayList<String>();
		b.add("Timefeed is the centralized feed. Posts from all the JU CMS App users (who have posting option) will appear here.");

		List<String> c= new ArrayList<String>();
		c.add(" *Applicable only for  users with login credentials permissible for event creation. To create an event, select the 'Events' option in the side menu.  Add Event opens. Fill the details and click on 'Save'. Now your event is created.");

		List<String> d= new ArrayList<String>();
		d.add("Yes you can change your profile photo.");

		List<String> e= new ArrayList<String>();
		e.add("Select the profile photo on the side menu. You will be directed to your own profile.Now select the edit icon.Users can select from Gallery or take a click through Camera. If you select Gallery you will be redirected to your phone's album, select desired photo and click save. If you select Camera your phone camera opens, click a photo and save. Once you save your new photo is reflected in the profile. Finally click on Done. ");

		List<String> f= new ArrayList<String>();
		f.add("If you are an user with credentials that allows you to post, then you can post photos, videos and text updates on Timefeed. ");

		List<String> g= new ArrayList<String>();
		g.add("Yes, you can only delete the post that you have made. You can either go to Timelog or Timefeed and delete the posts made by you. ");

		List<String> h= new ArrayList<String>();
		h.add("Yes you can change your email address. ");

		List<String> i= new ArrayList<String>();
		i.add("Select the profile photo from side menu. You will be redirected to your own profile. Select the edit button, edit your email address and click on Done to save the changes.  ");

		List<String> j= new ArrayList<String>();
		j.add(" *Applicable only for  users with login credentials permissible for broadcast creation. It is a listing of broadcast messages.Broadcast is a way to send single message to multiple people at one go.");

		List<String> k= new ArrayList<String>();
		k.add("An organisation profile is the profile of JU CMS. It has all the details of the institution including the contact information. ");

		List<String> l= new ArrayList<String>();
		l.add("Timelog is a repository of all your previous posts. ");

		List<String> m= new ArrayList<String>();
		m.add("Gallery is a repository of videos and photos you posted on Timefeed. ");

		List<String> n= new ArrayList<String>();
		n.add("Notifications are any information received by you from another user of the app or the app itself. ");

		List<String> o= new ArrayList<String>();
		o.add("Discussion Forum is a place where you can create topics related to academics, current affairs etc. Also, you can post questions regarding them. You can even post answers on questions other people have posted. ");

		List<String> p= new ArrayList<String>();
		p.add("Photos and videos are part of Gallery. All the photos and videos posted by you on timefeed will be reposited here. ");

		//attendance admin

		List<String> q= new ArrayList<String>();
		q.add("As a students admin you can send messages to all other admins, all teachers, students and class monitors. ");

		List<String> r= new ArrayList<String>();
		r.add("It is a listing of all the Teachers in the institution (JU CMS)");

		List<String> s= new ArrayList<String>();
		s.add("It is a listing of all the Students in the institution (JU CMS).");

		List<String> t= new ArrayList<String>();
		t.add("It is a listing of all the Admins in the institution (JU CMS).");

		List<String> u= new ArrayList<String>();
		u.add("It is a listing of your current and previous internships along with learning outcome. Your internship can be veiwed by teachers, admins and your parents. ");

		List<String> v= new ArrayList<String>();
		v.add("Learning outcome is the jobsheet for that particuar internship. ");

		List<String> w= new ArrayList<String>();
		w.add("No, as a student you can't post on timefeed.   ");

		listDataChild.put(listDataHeader.get(0), a); 
		listDataChild.put(listDataHeader.get(1), b);
		listDataChild.put(listDataHeader.get(2), c);

		listDataChild.put(listDataHeader.get(3), d); 
		listDataChild.put(listDataHeader.get(4), e);
		listDataChild.put(listDataHeader.get(5), f);

		listDataChild.put(listDataHeader.get(6), g); 
		listDataChild.put(listDataHeader.get(7), h);
		listDataChild.put(listDataHeader.get(8), i);

		listDataChild.put(listDataHeader.get(9), j); 
		listDataChild.put(listDataHeader.get(10), k);
		listDataChild.put(listDataHeader.get(11), l);

		listDataChild.put(listDataHeader.get(12), m); 
		listDataChild.put(listDataHeader.get(13), n);
		listDataChild.put(listDataHeader.get(14), o);

		listDataChild.put(listDataHeader.get(15), p); 

		listDataChild.put(listDataHeader.get(16), q); 
		listDataChild.put(listDataHeader.get(17), r); 
		listDataChild.put(listDataHeader.get(18), s); 
		listDataChild.put(listDataHeader.get(19), t); 
		listDataChild.put(listDataHeader.get(20), u); 

		listDataChild.put(listDataHeader.get(21), v); 
		listDataChild.put(listDataHeader.get(22), w); 

	}

	private void Alumni() {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();

		// Adding child data
		listDataHeader.add("Who can use the JU CMS App?");
		listDataHeader.add("What is Timefeed?");
		listDataHeader.add("How do I create an Event? ");

		listDataHeader.add("Can I change my profile Photo? ");
		listDataHeader.add("How do I change my profile photo?");
		listDataHeader.add("What type of content can I post on the Timefeed?");

		listDataHeader.add("Can I delete my posts? ");
		listDataHeader.add("Can I change my email address?");
		listDataHeader.add("How do I change my email address?");

		listDataHeader.add("What is broadcast? ");
		listDataHeader.add("What is organisation profile? ");
		listDataHeader.add("What is Timelog? ");

		listDataHeader.add("What is Gallery?");
		listDataHeader.add("What is notification?");
		listDataHeader.add("What is discussion forum?");

		listDataHeader.add("What are photos & videos? ");

		//attendance admin
		listDataHeader.add("Whom can I send messages? ");
		listDataHeader.add("What is Alumni?");

		// Adding child data
		List<String> a= new ArrayList<String>();
		a.add("All the users with user id and guest login credentials can use the JU CMS app.");

		List<String> b= new ArrayList<String>();
		b.add("Timefeed is the centralized feed. Posts from all the JU CMS App users (who have posting option) will appear here.");

		List<String> c= new ArrayList<String>();
		c.add(" *Applicable only for  users with login credentials permissible for event creation. To create an event, select the 'Events' option in the side menu.  Add Event opens. Fill the details and click on 'Save'. Now your event is created.");

		List<String> d= new ArrayList<String>();
		d.add("Yes you can change your profile photo.");

		List<String> e= new ArrayList<String>();
		e.add("Select the profile photo on the side menu. You will be directed to your own profile.Now select the edit icon.Users can select from Gallery or take a click through Camera. If you select Gallery you will be redirected to your phone's album, select desired photo and click save. If you select Camera your phone camera opens, click a photo and save. Once you save your new photo is reflected in the profile. Finally click on Done. ");

		List<String> f= new ArrayList<String>();
		f.add("If you are an user with credentials that allows you to post, then you can post photos, videos and text updates on Timefeed. ");

		List<String> g= new ArrayList<String>();
		g.add("Yes, you can only delete the post that you have made. You can either go to Timelog or Timefeed and delete the posts made by you. ");

		List<String> h= new ArrayList<String>();
		h.add("Yes you can change your email address. ");

		List<String> i= new ArrayList<String>();
		i.add("Select the profile photo from side menu. You will be redirected to your own profile. Select the edit button, edit your email address and click on Done to save the changes.  ");

		List<String> j= new ArrayList<String>();
		j.add(" *Applicable only for  users with login credentials permissible for broadcast creation. It is a listing of broadcast messages.Broadcast is a way to send single message to multiple people at one go.");

		List<String> k= new ArrayList<String>();
		k.add("An organisation profile is the profile of JU CMS. It has all the details of the institution including the contact information. ");

		List<String> l= new ArrayList<String>();
		l.add("Timelog is a repository of all your previous posts. ");

		List<String> m= new ArrayList<String>();
		m.add("Gallery is a repository of videos and photos you posted on Timefeed. ");

		List<String> n= new ArrayList<String>();
		n.add("Notifications are any information received by you from another user of the app or the app itself. ");

		List<String> o= new ArrayList<String>();
		o.add("Discussion Forum is a place where you can create topics related to academics, current affairs etc. Also, you can post questions regarding them. You can even post answers on questions other people have posted. ");

		List<String> p= new ArrayList<String>();
		p.add("Photos and videos are part of Gallery. All the photos and videos posted by you on timefeed will be reposited here. ");

		//attendance admin

		List<String> q= new ArrayList<String>();
		q.add("You can send messages to all alumni, internship admin and alumni admin. ");

		List<String> r= new ArrayList<String>();
		r.add("It is a listing of all the Alumni of the institution (JU CMS).");



		listDataChild.put(listDataHeader.get(0), a); 
		listDataChild.put(listDataHeader.get(1), b);
		listDataChild.put(listDataHeader.get(2), c);

		listDataChild.put(listDataHeader.get(3), d); 
		listDataChild.put(listDataHeader.get(4), e);
		listDataChild.put(listDataHeader.get(5), f);

		listDataChild.put(listDataHeader.get(6), g); 
		listDataChild.put(listDataHeader.get(7), h);
		listDataChild.put(listDataHeader.get(8), i);

		listDataChild.put(listDataHeader.get(9), j); 
		listDataChild.put(listDataHeader.get(10), k);
		listDataChild.put(listDataHeader.get(11), l);

		listDataChild.put(listDataHeader.get(12), m); 
		listDataChild.put(listDataHeader.get(13), n);
		listDataChild.put(listDataHeader.get(14), o);

		listDataChild.put(listDataHeader.get(15), p); 

		listDataChild.put(listDataHeader.get(16), q); 
		listDataChild.put(listDataHeader.get(17), r); 

	}

	private void CompanyAdmin() {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();

		// Adding child data
		listDataHeader.add("Who can use the JU CMS App?");
		listDataHeader.add("What is Timefeed?");
		listDataHeader.add("How do I create an Event? ");

		listDataHeader.add("Can I change my profile Photo? ");
		listDataHeader.add("How do I change my profile photo?");
		listDataHeader.add("What type of content can I post on the Timefeed?");

		listDataHeader.add("Can I delete my posts? ");
		listDataHeader.add("Can I change my email address?");
		listDataHeader.add("How do I change my email address?");

		listDataHeader.add("What is broadcast? ");
		listDataHeader.add("What is organisation profile? ");
		listDataHeader.add("What is Timelog? ");

		listDataHeader.add("What is Gallery?");
		listDataHeader.add("What is notification?");
		listDataHeader.add("What is discussion forum?");

		listDataHeader.add("What are photos & videos? ");

		//attendance admin
		listDataHeader.add("Whom can I send messages? ");
		listDataHeader.add("What is Admin?");
		listDataHeader.add("What is Internship? ");


		// Adding child data
		List<String> a= new ArrayList<String>();
		a.add("All the users with user id and guest login credentials can use the JU CMS app.");

		List<String> b= new ArrayList<String>();
		b.add("Timefeed is the centralized feed. Posts from all the JU CMS App users (who have posting option) will appear here.");

		List<String> c= new ArrayList<String>();
		c.add(" *Applicable only for  users with login credentials permissible for event creation. To create an event, select the 'Events' option in the side menu.  Add Event opens. Fill the details and click on 'Save'. Now your event is created.");

		List<String> d= new ArrayList<String>();
		d.add("Yes you can change your profile photo.");

		List<String> e= new ArrayList<String>();
		e.add("Select the profile photo on the side menu. You will be directed to your own profile.Now select the edit icon.Users can select from Gallery or take a click through Camera. If you select Gallery you will be redirected to your phone's album, select desired photo and click save. If you select Camera your phone camera opens, click a photo and save. Once you save your new photo is reflected in the profile. Finally click on Done. ");

		List<String> f= new ArrayList<String>();
		f.add("If you are an user with credentials that allows you to post, then you can post photos, videos and text updates on Timefeed. ");

		List<String> g= new ArrayList<String>();
		g.add("Yes, you can only delete the post that you have made. You can either go to Timelog or Timefeed and delete the posts made by you. ");

		List<String> h= new ArrayList<String>();
		h.add("Yes you can change your email address. ");

		List<String> i= new ArrayList<String>();
		i.add("Select the profile photo from side menu. You will be redirected to your own profile. Select the edit button, edit your email address and click on Done to save the changes.  ");

		List<String> j= new ArrayList<String>();
		j.add(" *Applicable only for  users with login credentials permissible for broadcast creation. It is a listing of broadcast messages.Broadcast is a way to send single message to multiple people at one go.");

		List<String> k= new ArrayList<String>();
		k.add("An organisation profile is the profile of JU CMS. It has all the details of the institution including the contact information. ");

		List<String> l= new ArrayList<String>();
		l.add("Timelog is a repository of all your previous posts. ");

		List<String> m= new ArrayList<String>();
		m.add("Gallery is a repository of videos and photos you posted on Timefeed. ");

		List<String> n= new ArrayList<String>();
		n.add("Notifications are any information received by you from another user of the app or the app itself. ");

		List<String> o= new ArrayList<String>();
		o.add("Discussion Forum is a place where you can create topics related to academics, current affairs etc. Also, you can post questions regarding them. You can even post answers on questions other people have posted. ");

		List<String> p= new ArrayList<String>();
		p.add("Photos and videos are part of Gallery. All the photos and videos posted by you on timefeed will be reposited here. ");

		//attendance admin

		List<String> q= new ArrayList<String>();
		q.add("You can send messages to internship admin of JU CMS. ");

		List<String> r= new ArrayList<String>();
		r.add("It is a list of Internship admins of JU CMS. ");

		List<String> s= new ArrayList<String>();
		s.add("It is a list of students pursuing internship in your company. ");



		listDataChild.put(listDataHeader.get(0), a); 
		listDataChild.put(listDataHeader.get(1), b);
		listDataChild.put(listDataHeader.get(2), c);

		listDataChild.put(listDataHeader.get(3), d); 
		listDataChild.put(listDataHeader.get(4), e);
		listDataChild.put(listDataHeader.get(5), f);

		listDataChild.put(listDataHeader.get(6), g); 
		listDataChild.put(listDataHeader.get(7), h);
		listDataChild.put(listDataHeader.get(8), i);

		listDataChild.put(listDataHeader.get(9), j); 
		listDataChild.put(listDataHeader.get(10), k);
		listDataChild.put(listDataHeader.get(11), l);

		listDataChild.put(listDataHeader.get(12), m); 
		listDataChild.put(listDataHeader.get(13), n);
		listDataChild.put(listDataHeader.get(14), o);

		listDataChild.put(listDataHeader.get(15), p); 

		listDataChild.put(listDataHeader.get(16), q); 
		listDataChild.put(listDataHeader.get(17), r); 
		listDataChild.put(listDataHeader.get(18), s); 

	}

	private void Parent() {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();

		// Adding child data
		listDataHeader.add("Who can use the JU CMS App?");
		listDataHeader.add("What is Timefeed?");
		listDataHeader.add("How do I create an Event? ");

		listDataHeader.add("Can I change my profile Photo? ");
		listDataHeader.add("How do I change my profile photo?");
		listDataHeader.add("What type of content can I post on the Timefeed?");

		listDataHeader.add("Can I delete my posts? ");
		listDataHeader.add("Can I change my email address?");
		listDataHeader.add("How do I change my email address?");

		listDataHeader.add("What is broadcast? ");
		listDataHeader.add("What is organisation profile? ");
		listDataHeader.add("What is Timelog? ");

		listDataHeader.add("What is Gallery?");
		listDataHeader.add("What is notification?");
		listDataHeader.add("What is discussion forum?");

		listDataHeader.add("What are photos & videos? ");
		listDataHeader.add("What is Teachers directory?");

		// Adding child data
		List<String> a= new ArrayList<String>();
		a.add("All the users with user id and guest login credentials can use the JU CMS app.");

		List<String> b= new ArrayList<String>();
		b.add("Timefeed is the centralized feed. Posts from all the JU CMS App users (who have posting option) will appear here.");

		List<String> c= new ArrayList<String>();
		c.add(" *Applicable only for  users with login credentials permissible for event creation. To create an event, select the 'Events' option in the side menu.  Add Event opens. Fill the details and click on 'Save'. Now your event is created.");

		List<String> d= new ArrayList<String>();
		d.add("Yes you can change your profile photo.");

		List<String> e= new ArrayList<String>();
		e.add("Select the profile photo on the side menu. You will be directed to your own profile.Now select the edit icon.Users can select from Gallery or take a click through Camera. If you select Gallery you will be redirected to your phone's album, select desired photo and click save. If you select Camera your phone camera opens, click a photo and save. Once you save your new photo is reflected in the profile. Finally click on Done. ");

		List<String> f= new ArrayList<String>();
		f.add("If you are an user with credentials that allows you to post, then you can post photos, videos and text updates on Timefeed. ");

		List<String> g= new ArrayList<String>();
		g.add("Yes, you can only delete the post that you have made. You can either go to Timelog or Timefeed and delete the posts made by you. ");

		List<String> h= new ArrayList<String>();
		h.add("Yes you can change your email address. ");

		List<String> i= new ArrayList<String>();
		i.add("Select the profile photo from side menu. You will be redirected to your own profile. Select the edit button, edit your email address and click on Done to save the changes.  ");

		List<String> j= new ArrayList<String>();
		j.add(" *Applicable only for  users with login credentials permissible for broadcast creation. It is a listing of broadcast messages.Broadcast is a way to send single message to multiple people at one go.");

		List<String> k= new ArrayList<String>();
		k.add("An organisation profile is the profile of JU CMS. It has all the details of the institution including the contact information. ");

		List<String> l= new ArrayList<String>();
		l.add("Timelog is a repository of all your previous posts. ");

		List<String> m= new ArrayList<String>();
		m.add("Gallery is a repository of videos and photos you posted on Timefeed. ");

		List<String> n= new ArrayList<String>();
		n.add("Notifications are any information received by you from another user of the app or the app itself. ");

		List<String> o= new ArrayList<String>();
		o.add("Discussion Forum is a place where you can create topics related to academics, current affairs etc. Also, you can post questions regarding them. You can even post answers on questions other people have posted. ");

		List<String> p= new ArrayList<String>();
		p.add("Photos and videos are part of Gallery. All the photos and videos posted by you on timefeed will be reposited here. ");

		List<String> q= new ArrayList<String>();
		q.add("It is a listing of all the Teachers in the institution (JU CMS).");


		listDataChild.put(listDataHeader.get(0), a); 
		listDataChild.put(listDataHeader.get(1), b);
		listDataChild.put(listDataHeader.get(2), c);

		listDataChild.put(listDataHeader.get(3), d); 
		listDataChild.put(listDataHeader.get(4), e);
		listDataChild.put(listDataHeader.get(5), f);

		listDataChild.put(listDataHeader.get(6), g); 
		listDataChild.put(listDataHeader.get(7), h);
		listDataChild.put(listDataHeader.get(8), i);

		listDataChild.put(listDataHeader.get(9), j); 
		listDataChild.put(listDataHeader.get(10), k);
		listDataChild.put(listDataHeader.get(11), l);

		listDataChild.put(listDataHeader.get(12), m); 
		listDataChild.put(listDataHeader.get(13), n);
		listDataChild.put(listDataHeader.get(14), o);

		listDataChild.put(listDataHeader.get(15), p); 
		listDataChild.put(listDataHeader.get(16), q); 
	}
}
