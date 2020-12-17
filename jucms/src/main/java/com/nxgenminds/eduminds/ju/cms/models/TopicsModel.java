package com.nxgenminds.eduminds.ju.cms.models;

public class TopicsModel {

	private String forum_category_id;
	private String forum_category_name;
	private String forum_category_desc;
	private String created_date;
	private String forum_topics_count;
	private String firstname;
	private String lastname;
	private String profile_photo;

	public String getForum_category_id() {
		return forum_category_id;
	}
	public void setForum_category_id(String forum_category_id) {
		this.forum_category_id = forum_category_id;
	}
	public String getForum_category_name() {
		return forum_category_name;
	}
	public void setForum_category_name(String forum_category_name) {
		this.forum_category_name = forum_category_name;
	}
	public String getForum_category_desc() {
		return forum_category_desc;
	}
	public void setForum_category_desc(String forum_category_desc) {
		this.forum_category_desc = forum_category_desc;
	}
	public String getCreated_date() {
		return created_date;
	}
	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}
	public String getForum_topics_count() {
		return forum_topics_count;
	}
	public void setForum_topics_count(String forum_topics_count) {
		this.forum_topics_count = forum_topics_count;
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
	public String getProfile_photo() {
		return profile_photo;
	}
	public void setProfile_photo(String profile_photo) {
		this.profile_photo = profile_photo;
	}

}
