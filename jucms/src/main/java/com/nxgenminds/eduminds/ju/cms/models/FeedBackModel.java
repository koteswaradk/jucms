package com.nxgenminds.eduminds.ju.cms.models;

import java.util.ArrayList;

public class FeedBackModel {

	private ArrayList<FeedLecturerModel> feedbacklecturerList;
	private ArrayList<FeedbackQuestModel> feedbackquestionsList;

	public ArrayList<FeedbackQuestModel> getFeedbackquestionsList() {
		return feedbackquestionsList;
	}
	public void setFeedbackquestionsList(
			ArrayList<FeedbackQuestModel> feedbackquestionsList) {
		this.feedbackquestionsList = feedbackquestionsList;
	}
	public ArrayList<FeedLecturerModel> getFeedbacklecturerList() {
		return feedbacklecturerList;
	}
	public void setFeedbacklecturerList(
			ArrayList<FeedLecturerModel> feedbacklecturerList) {
		this.feedbacklecturerList = feedbacklecturerList;
	}

}
