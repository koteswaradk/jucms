package com.nxgenminds.eduminds.ju.cms.events;

import java.util.ArrayList;

import com.nxgenminds.eduminds.ju.cms.Feedback.FeedBackQuestionModel;

public class FeedBackAnswerModel {
	
	String teacher_id,subject_id,teacher_subject_id;
	ArrayList<FeedBackQuestionModel> question_array_list ;
	
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
	public String getTeacher_subject_id() {
		return teacher_subject_id;
	}
	public void setTeacher_subject_id(String teacher_subject_id) {
		this.teacher_subject_id = teacher_subject_id;
	}
	public ArrayList<FeedBackQuestionModel> getQuestion_array_list() {
		return this.question_array_list;
	}
	public void setQuestion_array_list(ArrayList<FeedBackQuestionModel> question_array_list) {
		this.question_array_list = question_array_list;
	}
	
	       

}
