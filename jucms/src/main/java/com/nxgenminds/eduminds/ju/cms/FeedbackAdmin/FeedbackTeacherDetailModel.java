package com.nxgenminds.eduminds.ju.cms.FeedbackAdmin;

import java.util.ArrayList;

public class FeedbackTeacherDetailModel {
	
	String  total_students,
			total_students_given_the_answer,
			answer_percentage,
			user_id,
			firstname,
			lastname,
			username,
			teacher_profile_photo,
			stream_name,
			semester,
			section_name,
			subject_name,
			feedback_colorcode_grade;
	
	ArrayList<FeedbackQuestionsResultModel> arrayListFeedBackQuestionsResultModel = new ArrayList<FeedbackQuestionsResultModel>();

	public ArrayList<FeedbackQuestionsResultModel> getArrayListFeedBackQuestionsResultModel() {
		return arrayListFeedBackQuestionsResultModel;
	}

	public void setArrayListFeedBackQuestionsResultModel(
			ArrayList<FeedbackQuestionsResultModel> arrayListFeedBackQuestionsResultModel) {
		this.arrayListFeedBackQuestionsResultModel = arrayListFeedBackQuestionsResultModel;
	}

	public String getTotal_students() {
		return total_students;
	}

	public void setTotal_students(String total_students) {
		this.total_students = total_students;
	}

	public String getTotal_students_given_the_answer() {
		return total_students_given_the_answer;
	}

	public void setTotal_students_given_the_answer(
			String total_students_given_the_answer) {
		this.total_students_given_the_answer = total_students_given_the_answer;
	}

	public String getAnswer_percentage() {
		return answer_percentage;
	}

	public void setAnswer_percentage(String answer_percentage) {
		this.answer_percentage = answer_percentage;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTeacher_profile_photo() {
		return teacher_profile_photo;
	}

	public void setTeacher_profile_photo(String teacher_profile_photo) {
		this.teacher_profile_photo = teacher_profile_photo;
	}

	public String getStream_name() {
		return stream_name;
	}

	public void setStream_name(String stream_name) {
		this.stream_name = stream_name;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public String getSection_name() {
		return section_name;
	}

	public void setSection_name(String section_name) {
		this.section_name = section_name;
	}

	public String getSubject_name() {
		return subject_name;
	}

	public void setSubject_name(String subject_name) {
		this.subject_name = subject_name;
	}

	public String getFeedback_colorcode_grade() {
		return feedback_colorcode_grade;
	}

	public void setFeedback_colorcode_grade(String feedback_colorcode_grade) {
		this.feedback_colorcode_grade = feedback_colorcode_grade;
	}
	
	
}
