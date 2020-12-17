package com.nxgenminds.eduminds.ju.cms.models;

public class EventThemesModel {
	private String event_theme_id;

	public String getEvent_theme_id() {
		return event_theme_id;
	}

	public void setEvent_theme_id(String event_theme_id) {
		this.event_theme_id = event_theme_id;
	}

	public String getEvent_theme_name() {
		return event_theme_name;
	}

	public void setEvent_theme_name(String event_theme_name) {
		this.event_theme_name = event_theme_name;
	}

	public String getEvent_theme_size() {
		return event_theme_size;
	}

	public void setEvent_theme_size(String event_theme_size) {
		this.event_theme_size = event_theme_size;
	}

	public String getEvent_theme_dimensions() {
		return event_theme_dimensions;
	}

	public void setEvent_theme_dimensions(String event_theme_dimensions) {
		this.event_theme_dimensions = event_theme_dimensions;
	}

	public String getActual_theme_path() {
		return actual_theme_path;
	}

	public void setActual_theme_path(String actual_theme_path) {
		this.actual_theme_path = actual_theme_path;
	}

	public String getActual_theme_base_name() {
		return actual_theme_base_name;
	}

	public void setActual_theme_base_name(String actual_theme_base_name) {
		this.actual_theme_base_name = actual_theme_base_name;
	}

	private String event_theme_name;
	private String event_theme_size;
	private String event_theme_dimensions;
	private String actual_theme_path;
	private String actual_theme_base_name;

}
