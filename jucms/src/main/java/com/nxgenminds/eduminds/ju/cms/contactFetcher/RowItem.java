package com.nxgenminds.eduminds.ju.cms.contactFetcher;

import java.util.ArrayList;

import android.net.Uri;

/**
 * A custom RowItem class that manages 
 * the contact details shown in the list view
 *
 */
public class RowItem {
	
	public static final String CONTACT_ID = "contactId";
	public static final String CONTACT_NAME = "ContactName";
	public static final String CONTACT_EMAILS = "email";
	public static final String CONTACT_NUMBER = "Number";
	public static final String CONTACT_SELECTED = "selected";

	/**
	 * Contact's id in Android contact list.
	 */
	private String contactId;

	private String contactName;

	private ArrayList<ContactEmail> emails;

	private ArrayList<ContactPhone> numbers;

	private Uri imageUri;
	
	private boolean selected;

	/**
	 * @param id
	 * @param name
	 */
	public RowItem(String contactId, String name) {
		this.contactId = contactId;
		this.contactName = name;
	}

	/**
	 * @param contactId the contactId to set
	 */
	public void setContactId(String contactId) {
		this.contactId = contactId;
	}
	/**
	 * Contact's id in Android contact list.
	 * 
	 * @return contactId
	 */
	public String getContactId() {
		return contactId;
	}

	/**
	 * @return the name
	 */
	public String getContactName() {
		return contactName;
	}

	/**
	 * @param name the name to set
	 */
	public void setContactName(String name) {
		this.contactName = name;
	}

	/**
	 * @return the emails
	 */
	public ArrayList<ContactEmail> getEmails() {
		if(emails == null)
			emails = new ArrayList<ContactEmail>();		
		return emails;
	}
	
	public void addEmail(String address, String type) {
		if(emails == null)
			emails = new ArrayList<ContactEmail>();
		emails.add(new ContactEmail(address, type));
	}

	public void addNumber(String number, String type) {
		if(numbers == null)
			numbers = new ArrayList<ContactPhone>();
		numbers.add(new ContactPhone(number, type));
	}

	public ArrayList<ContactPhone> getNumber() {
		if(numbers == null)
			numbers = new ArrayList<ContactPhone>();
		return numbers;
	}
	
	/**
	 * @return the imageUri
	 */
	public Uri getImageUri() {
		return imageUri;
	}

	/**
	 * @param imageUri the imageUri to set
	 */
	public void setImageUri(Uri image) {
		this.imageUri = image;
	}
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
