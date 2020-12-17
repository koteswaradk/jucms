package com.nxgenminds.eduminds.ju.cms.attendence;

public class AttendenceStudentsModel {

	private String stream_id;
	private String semester_id;
	private String section_id;
	private String user_id;
	private String firstname;
	private String lastname;
	private String mobile;
	private String profile_photo;
	private String PresentStatus;

	public String getStream_id() {
		return stream_id;
	}
	public void setStream_id(String stream_id) {
		this.stream_id = stream_id;
	}
	public String getSemester_id() {
		return semester_id;
	}
	public void setSemester_id(String semester_id) {
		this.semester_id = semester_id;
	}
	public String getSection_id() {
		return section_id;
	}
	public void setSection_id(String section_id) {
		this.section_id = section_id;
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
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getProfile_photo() {
		return profile_photo;
	}
	public void setProfile_photo(String profile_photo) {
		this.profile_photo = profile_photo;
	}
	public String getPresentStatus() {
		return PresentStatus;
	}
	public void setPresentStatus(String presentStatus) {
		PresentStatus = presentStatus;
	}


}
