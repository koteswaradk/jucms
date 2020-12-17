package com.nxgenminds.eduminds.ju.cms.guestEntrnaceTest;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.guestAcademics.FileDownloader;


public class EntranceTestResultDeatailActivity extends Activity {

	
	private static  TextView title;
	private static TextView desc;
	private static String sTitle;
	private static String sDesc;
	

	private static String filename;
	private static String linkpdf;
	

	private Context prova;
	private ProgressDialog mProgressDialog;
	private Button pdfbutton;
	 TextView tv_loading;
	    String dest_file_path = "test.pdf";
	    int downloadedSize = 0, totalsize;
	    	    float per = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guest_testresult_deatilview);
		pdfbutton = (Button)findViewById(R.id.test_button_pdf_Download);
	    
	    
		Bundle extras = getIntent().getExtras();
		sTitle = extras.getString("title");
		sDesc =extras.getString("desc");
		linkpdf =extras.getString("pdflink");
		filename=extras.getString("pdfilename");
		
		if(filename.equalsIgnoreCase("") || filename == null || filename.equalsIgnoreCase("null"))		
		{
			pdfbutton.setVisibility(View.GONE);
		}
		else
		{
			pdfbutton.setVisibility(View.VISIBLE);
		}
		
         init();     
         
         title.setText(sTitle);
         desc.setText(sDesc);
         
         pdfbutton.setOnClickListener(new OnClickListener() {

        	 @SuppressWarnings("deprecation")
             @Override
             public void onClick(View v) {
                 // TODO Auto-generated method stub
                 //new DownloadFile().execute(linkpdf,filename); 


                           
                 Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkpdf));
                 startActivity(browserIntent);



             }
  		});
 	}

  				
  	
  		
	
	
	private void init() {
		// TODO Auto-generated method stub

		title = (TextView) findViewById(R.id.test_desc_title);
		desc = (TextView) findViewById(R.id.test_desc_detail);
	
	}
	
}
