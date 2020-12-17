package com.nxgenminds.eduminds.ju.cms.models;

public class DiscussionForumReplyModel {

	private String forum_reply_id;
	private String forum_reply_content;
	private String forum_topic_id;
	private String created_date;
	private String firstname;
	private String lastname;
	private String profile_photo ;

	public String getForum_reply_id() {
		return forum_reply_id;
	}
	public void setForum_reply_id(String forum_reply_id) {
		this.forum_reply_id = forum_reply_id;
	}
	public String getForum_reply_content() {
		return forum_reply_content;
	}
	public void setForum_reply_content(String forum_reply_content) {
		this.forum_reply_content = forum_reply_content;
	}
	public String getForum_topic_id() {
		return forum_topic_id;
	}
	public void setForum_topic_id(String forum_topic_id) {
		this.forum_topic_id = forum_topic_id;
	}
	public String getCreated_date() {
		return created_date;
	}
	public void setCreated_date(String created_date) {
		this.created_date = created_date;
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
