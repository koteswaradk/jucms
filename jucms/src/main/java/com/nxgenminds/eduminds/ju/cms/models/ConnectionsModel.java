package com.nxgenminds.eduminds.ju.cms.models;

public class ConnectionsModel {
	private String user_id;
	private String firstname;
	private String lastname;
	private String middlename;
	private String username;
	private String email;
	private String mobile;
	private String dob;
	private String gendername;
	private String user_profile_photo;
	private String user_profile_photo_thumb1;
	private String messaging_status;
	private String friend;
	private String created_date;
	private String home_city_name;
	private String curr_city_name;
	private boolean selected;
	private String jid;

	public String getHome_city_name() {
		return home_city_name;
	}
	public void setHome_city_name(String home_city_name) {
		this.home_city_name = home_city_name;
	}
	public String getCurr_city_name() {
		return curr_city_name;
	}
	public void setCurr_city_name(String curr_city_name) {
		this.curr_city_name = curr_city_name;
	}


	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getMiddlename() {
		return middlename;
	}
	public void setMiddlename(String middlename) {
		this.middlename = middlename;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getGendername() {
		return gendername;
	}
	public void setGendername(String gendername) {
		this.gendername = gendername;
	}
	public String getUser_profile_photo() {
		return user_profile_photo;
	}
	public void setUser_profile_photo(String user_profile_photo) {
		this.user_profile_photo = user_profile_photo;
	}
	/*	public String getUser_cover_photo() {
		return user_cover_photo;
	}
	public void setUser_cover_photo(String user_cover_photo) {
		this.user_cover_photo = user_cover_photo;
	}*/
	public String getMessaging_status() {
		return messaging_status;
	}
	public void setMessaging_status(String messaging_status) {
		this.messaging_status = messaging_status;
	}
	public String getFriend() {
		return friend;
	}
	public void setFriend(String friend) {
		this.friend = friend;
	}
	public String getCreated_date() {
		return created_date;
	}
	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public String getJid() {
		return jid;
	}
	public void setJid(String jid) {
		this.jid = jid;
	}
	public String getUser_profile_photo_thumb1() {
		return user_profile_photo_thumb1;
	}
	public void setUser_profile_photo_thumb1(String user_profile_photo_thumb1) {
		this.user_profile_photo_thumb1 = user_profile_photo_thumb1;
	}


}
