package com.nxgenminds.eduminds.ju.cms.models;

public class LearningOutComeStudentModel {

	private String period;
	private String from_date;
	private String to_date;
	private String comment;
	private String created_date;
	private String learning_outcome_id;

	private String is_approved;
	public String getIs_approved() {
		return is_approved;
	}
	public void setIs_approved(String is_approved) {
		this.is_approved = is_approved;
	}
	public String getApproval_remarks() {
		return approval_remarks;
	}
	public void setApproval_remarks(String approval_remarks) {
		this.approval_remarks = approval_remarks;
	}
	public String getIs_approval_remarks() {
		return is_approval_remarks;
	}
	public void setIs_approval_remarks(String is_approval_remarks) {
		this.is_approval_remarks = is_approval_remarks;
	}
	private String approval_remarks;
	private String is_approval_remarks;

	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public String getFrom_date() {
		return from_date;
	}
	public void setFrom_date(String from_date) {
		this.from_date = from_date;
	}
	public String getTo_date() {
		return to_date;
	}
	public void setTo_date(String to_date) {
		this.to_date = to_date;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getCreated_date() {
		return created_date;
	}
	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}
	public String getLearning_outcome_id() {
		return learning_outcome_id;
	}
	public void setLearning_outcome_id(String learning_outcome_id) {
		this.learning_outcome_id = learning_outcome_id;
	}

}
