package com.nxgenminds.eduminds.ju.cms.Feedback;
//pojo class for teacher
public class TeacherSubjectGetModel {
	
	private String teacher_id,subject_id,teacher_subject_id,
	               firstname,lastname,subject_name,
	               profile_photo;

	public String getTeacher_subject_id() {
		return teacher_subject_id;
	}

	public void setTeacher_subject_id(String teacher_subject_id) {
		this.teacher_subject_id = teacher_subject_id;
	}

	public String getSubject_name() {
		return subject_name;
	}

	public void setSubject_name(String subject_name) {
		this.subject_name = subject_name;
	}

	public String getTeacher_id() {
		return teacher_id;
	}

	public void setTeacher_id(String teacher_id) {
		this.teacher_id = teacher_id;
	}

	public String getSubject_id() {
		return subject_id;
	}

	public void setSubject_id(String subject_id) {
		this.subject_id = subject_id;
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
