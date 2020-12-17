package com.nxgenminds.eduminds.ju.cms.placementsboard;

import android.graphics.Bitmap;

public class PlacementsBoardModel {

	private String noticeboard_id;
	
	private String title;
	private String description;
	private String download_file_type;
	private String download_file_path;
	private String created_date;
	private String created_by;
	private String modified_date;
	private String modified_by;
	private String status;
	private Bitmap bit_image;
	
	public String getNoticeboard_id() {
		return noticeboard_id;
	}
	public void setNoticeboard_id(String noticeboard_id) {
		this.noticeboard_id = noticeboard_id;
	}
	public Bitmap getBit_image() {
		return bit_image;
	}
	public void setBit_image(Bitmap bit_image) {
		this.bit_image = bit_image;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDownload_file_type() {
		return download_file_type;
	}
	public void setDownload_file_type(String download_file_type) {
		this.download_file_type = download_file_type;
	}
	public String getDownload_file_path() {
		return download_file_path;
	}
	public void setDownload_file_path(String download_file_path) {
		this.download_file_path = download_file_path;
	}
	public String getCreated_date() {
		return created_date;
	}
	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}
	public String getCreated_by() {
		return created_by;
	}
	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}
	public String getModified_date() {
		return modified_date;
	}
	public void setModified_date(String modified_date) {
		this.modified_date = modified_date;
	}
	public String getModified_by() {
		return modified_by;
	}
	public void setModified_by(String modified_by) {
		this.modified_by = modified_by;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
