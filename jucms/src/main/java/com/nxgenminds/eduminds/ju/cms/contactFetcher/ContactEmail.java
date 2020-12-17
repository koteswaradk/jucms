package com.nxgenminds.eduminds.ju.cms.contactFetcher;

/**
 * Email for contact <br>
 *
 * TYPE_CUSTOM  value 0 <br>
 * TYPE_HOME	value 1 <br> 
 * TYPE_WORK	value 2 <br>
 * TYPE_OTHER	value 3 <br>
 * TYPE_MOBILE	value 4 <br>
 * 
 */ 
public class ContactEmail {

	public static final String TYPE_CUSTOM = "0";
	public static final String TYPE_HOME = "1";
	public static final String TYPE_WORK = "2";
	public static final String TYPE_OTHER = "3";
	public static final String TYPE_MOBILE = "4";

	public String email;

	public String type;

	public ContactEmail(String email, String type) {
		this.email = email;
		this.type = type;
	}
}
