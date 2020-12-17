package com.nxgenminds.eduminds.ju.cms.contactFetcher;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpPostClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;



public class Contact_Fetcher_MainActivity extends ActionBarActivity { 
	private static final String TAG = Contact_Fetcher_MainActivity.class.getName();

	private static final Uri URI = ContactsContract.Contacts.CONTENT_URI;
	private static final Uri PURI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
	private static final Uri EURI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
	private static final String ID = ContactsContract.Contacts._ID;
	private static final String DNAME = ContactsContract.Contacts.DISPLAY_NAME;
	private static final String HPN = ContactsContract.Contacts.HAS_PHONE_NUMBER;
	private static final String LOOKY = ContactsContract.Contacts.LOOKUP_KEY;
	private static final String CID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
	private static final String EID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
	private static final String PNUM = ContactsContract.CommonDataKinds.Phone.NUMBER;
	private static final String PHONETYPE = ContactsContract.CommonDataKinds.Phone.TYPE;
	private static final String EMAIL = ContactsContract.CommonDataKinds.Email.DATA;
	private static final String EMAILTYPE = ContactsContract.CommonDataKinds.Email.TYPE;
	private JSONArray allNewContactArray;
	ListView listView;
	List<RowItem> rowItems;
	private ActionBar actionBar;
	public Menu menuInstance;
	CustomListViewAdapter customListViewAdapter;
	private String UserDetails;
		
	private AlertDialogManager alert = new AlertDialogManager();
	/**
	 * Keeps contact photos cached in memory. Can also cache to disk (not in use TODO..)
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_fetcher);

		rowItems = new ArrayList<RowItem>();

		actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		getContactsDetails();
	}

	@Override
	public boolean onCreateOptionsMenu (Menu menu) {
		menuInstance = menu;
		getMenuInflater().inflate(R.menu.menu_main_list, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.menu_main_exit:
			finish();
			return false;

		case R.id.menu_main_select_all: 

			customListViewAdapter.setSelectStateForAll();
			customListViewAdapter.notifyDataSetChanged();

			if(customListViewAdapter.getCheckBoxSelectedState()){
				menuInstance.getItem(0).setVisible(true);
				menuInstance.getItem(1).setVisible(false);
				menuInstance.getItem(2).setVisible(true);
			}
			else
				menuInstance.getItem(0).setVisible(false);
			return true;

		case R.id.menu_main_unselect_all: 
			menuInstance.getItem(1).setVisible(true);
			menuInstance.getItem(2).setVisible(false);

		case R.id.menu_main_ToJson: 
			listView.setEnabled(false);
			int size = customListViewAdapter.getCount();
			allNewContactArray = new JSONArray();

			try {
				for(int i=0;i<size;i++){
					RowItem rowItem = (RowItem) customListViewAdapter.getItem(i);
					if(rowItem.isSelected()){
						JSONObject newContact = new JSONObject();
						boolean emailTypeIsHome = false;
						boolean phoneTypeIsHome = false;
						// check for email type 1
						ArrayList<ContactEmail> emailArray = rowItem.getEmails();
						if(emailArray.size() > 0){
							for (ContactEmail contactEmail : emailArray){
								//								Log.i(TAG, "EMAIL "+ contactEmail.type);
								if(contactEmail.type.equals(ContactEmail.TYPE_HOME)){
									newContact.put(RowItem.CONTACT_EMAILS, contactEmail.email);
									emailTypeIsHome = true; 
								}else if(contactEmail.type.equals(ContactEmail.TYPE_WORK)){
									newContact.put(RowItem.CONTACT_EMAILS, contactEmail.email);
									emailTypeIsHome = true; 
								}else if(contactEmail.type.equals(ContactEmail.TYPE_OTHER)){
									newContact.put(RowItem.CONTACT_EMAILS, contactEmail.email);
									emailTypeIsHome = true; 
								}
							} 
						}
						else
						{
							//System.out.println("email in else");
							newContact.put(RowItem.CONTACT_EMAILS,"");
							emailTypeIsHome = true;
						}
						// check for phone type 1
						ArrayList<ContactPhone> phoneArray = rowItem.getNumber();
						if(phoneArray.size() > 0){
							for (ContactPhone contactPhone : phoneArray){
								//								Log.i(TAG, "PHONE "+ contactPhone.type);

								if(contactPhone.type.equals(ContactPhone.TYPE_HOME)){
									newContact.put(RowItem.CONTACT_NUMBER, contactPhone.number);
									phoneTypeIsHome = true;
								}	
								else if(contactPhone.type.equals(ContactPhone.TYPE_MOBILE)){
									newContact.put(RowItem.CONTACT_NUMBER, contactPhone.number);
									phoneTypeIsHome = true;
								}	else if(contactPhone.type.equals(ContactPhone.TYPE_WORK)){
									newContact.put(RowItem.CONTACT_NUMBER, contactPhone.number);
									phoneTypeIsHome = true;
								}	
								else
								{
									//System.out.println("phone sim contact");
									newContact.put(RowItem.CONTACT_NUMBER, "0000000000");
									phoneTypeIsHome = true;
								}
							}
						}	
						else
						{
							/*newContact.put(RowItem.CONTACT_NUMBER, "Phone not provided");
							phoneTypeIsHome = true;*/
							newContact.put(RowItem.CONTACT_NUMBER, "0000000000");
							phoneTypeIsHome = true;
						}

						if(emailTypeIsHome){
							// build the new json 
							// add id
							//newContact.put(RowItem.CONTACT_ID, rowItem.getContactId());
							// add name
							newContact.put(RowItem.CONTACT_NAME, rowItem.getContactName());
							// add selected state
							//newContact.put(RowItem.CONTACT_SELECTED, rowItem.isSelected());		
							String jsonStr = newContact.toString(4);
							//Log.i(TAG, jsonStr);	

							allNewContactArray.put(newContact);
							//System.out.println(allNewContactArray.length()); 


						}else
							continue; 
					}
				}
				//print an array
				for(int i=0;i<1;i++) 
				{
					//System.out.println(allNewContactArray.toString(i));
					UserDetails=allNewContactArray.toString(i);
				}

				//System.out.println("user details : "+UserDetails);  
				
				ConnectionDetector conn = new ConnectionDetector(Contact_Fetcher_MainActivity.this);
	            if(conn.isConnectingToInternet()){
	            	new InvitePostAsync().execute(UserDetails);
	            } else{
	            	alert.showAlertDialog(Contact_Fetcher_MainActivity.this,"Connection Error","Check your Internet Connection",false);
	            }

				
				//end print an array

			} catch (JSONException e) {
				e.printStackTrace();
			}
			listView.setEnabled(true);
			return true;
		}		
		return super.onOptionsItemSelected(item);
	}

	//Method to retrieve Contact details from the phone database and display in the app in a list view
	private void getContactsDetails() {
		//A Progress dialog with a spinning wheel, to instruct the user about the app's current state
		final ProgressDialog dialog = ProgressDialog.show(Contact_Fetcher_MainActivity.this, "Please Wait", "Retrieving Contacts...", true);
		//A new worker thread is created to retrieve and display the contacts.
		new Thread(new Runnable() {
			public void run() {
				Log.w(TAG, "syncPeopleWorker has started");
				if(rowItems != null)
					rowItems.clear();
				//			   String[] projection = { ContactsContract.CommonDataKinds.Email.PHOTO_URI };
				String[] projection = new String[] {
						ContactsContract.Contacts._ID,
						ContactsContract.Contacts.DISPLAY_NAME,
						ContactsContract.Contacts.HAS_PHONE_NUMBER,
						ContactsContract.Contacts.LOOKUP_KEY};
				String sortOrder = ContactsContract.Contacts.DISPLAY_NAME;
				Cursor cursor = getContentResolver().query(
						URI, 
						projection, 
						null, 
						null, 
						sortOrder);
				ContentResolver cr = getContentResolver();
				try{
					if (cursor != null && cursor.getCount() > 0) {	
						// Loop over all contacts
						while (cursor.moveToNext()) { 
							String contactId;
							String lookupKey; // not used
							String name;
							// Get ID information (contactId, name and lookup key) for this contact. contactId is an identifier
							// number, name is the name associated with this row in the database, and
							// lookupKey is an opaque value that contains hints on how to find the contact 
							// if its row contactId changed as a result of a sync or aggregation.
							contactId = cursor.getString(cursor.getColumnIndex(ID));
							name = cursor.getString(cursor.getColumnIndex(DNAME));          
							lookupKey = cursor.getString(cursor.getColumnIndex(LOOKY));   
							RowItem rowItem = new RowItem(contactId, name);
							if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(HPN))) > 0) {	        	
								Cursor pCur = cr.query(
										PURI,  
										null, 
										CID + " = ?",  
										new String[]{contactId}, 
										null);
								while (pCur.moveToNext()) {
									rowItem.addNumber(pCur.getString(pCur.getColumnIndex(PNUM)), pCur.getString(pCur.getColumnIndex(PHONETYPE)));
								} 
								pCur.close();
							}
							// Query email addresses for this contact (may be more than one), so use a 
							// while-loop to move the cursor to the next row until moveToNext() returns
							// false, indicating no more rows. Store the results in arrays since there may
							// be more than one email address stored per contact.
							Cursor emailCur = null;
							try {
								emailCur = cr.query(
										EURI, 
										null, 
										EID + " = ?",  
										new String[]{contactId}, 
										null); 
								/*while (emailCur.moveToNext()) { 
									rowItem.addEmail(emailCur.getString(emailCur.getColumnIndex(EMAIL)), emailCur.getString(emailCur.getColumnIndex(EMAILTYPE)));
								} 
								emailCur.close();*/
								Cursor emails = getContentResolver().query(Email.CONTENT_URI, null, Email.CONTACT_ID + " = " + contactId, null, null);
								if(emails != null){
									while (emails.moveToNext()){
										rowItem.addEmail(emails.getString(emails.getColumnIndex(Email.DATA)), emails.getString(emails.getColumnIndex(Email.TYPE)));
									}
									emails.close();
								}
							}finally {
								if(emailCur != null)
									emailCur.close();
							}
							rowItem.setContactId(contactId);
							rowItem.setContactName(name);
							rowItems.add(rowItem);
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					try{
						if(cursor != null)
							cursor.close();
					}catch(Exception ex) {
						ex.printStackTrace();
					}
				}
				listView = (ListView) findViewById(R.id.contactslist);
				listView.post(new Runnable() {
					@Override
					public void run() {
						customListViewAdapter = new CustomListViewAdapter(Contact_Fetcher_MainActivity.this, R.layout.contact_fecther_list_row, rowItems);
						listView.setAdapter(customListViewAdapter);
						listView.setClickable(true);
						//Assign a click listener to the rows of the list view which runs a new activity with the contact page to make calls and send messages.
						listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

							}
						});
						listView.refreshDrawableState();
					}
				});
				dialog.dismiss();
			}
		}).start();
	}

	private class InvitePostAsync extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			// JSON object to hold the information, which is sent to the server
			JSONObject jsonObjSend = new JSONObject();
			String data = params[0];
			try {
				jsonObjSend.put("data",data);
				Log.i(TAG, data);
				//	System.out.println("user contact info Post "+ data);
			} 
			catch (JSONException e) {
				e.printStackTrace();
			}

			JSONObject jsonObjRecv = HttpPostClient.sendHttpPost(Util.API+"invite", jsonObjSend);

			if(jsonObjRecv!=null){
				try {
					if(jsonObjRecv.getJSONObject("error").toString().equalsIgnoreCase("false")){
						Log.i("jsonObjRecv",jsonObjRecv.toString());
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;
		}

	}
}