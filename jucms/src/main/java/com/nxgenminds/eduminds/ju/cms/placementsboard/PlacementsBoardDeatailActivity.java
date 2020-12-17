package com.nxgenminds.eduminds.ju.cms.placementsboard;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.nxgenminds.eduminds.ju.cms.R;

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



public class PlacementsBoardDeatailActivity extends Activity {

	private ArrayList<PlacementsBoardModel> model = new ArrayList<PlacementsBoardModel>();
	private static  TextView title;
	private static TextView desc;
	private static String sTitle;
	private static String filename;
	private static String linkpdf;

	private static String sDesc;
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
		setContentView(R.layout.activity_noticeboard_deatilview);

		pdfbutton = (Button)findViewById(R.id.acd_button_pdf_Download);

		Bundle extras = getIntent().getExtras();
		sTitle = extras.getString("title");
	/*	sDesc =extras.getString("desc");
		linkpdf =extras.getString("pdflink");*/
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

			//@Override
			//public void onClick(View v) {
				// TODO Auto-generated method stub
				//new DownloadFile().execute(linkpdf,filename); 
				
				


				/*File pdfFile = new File(Environment.getExternalStorageDirectory() + "/jucms/" + filename);  // -> filename = maven.pdf
				Uri path = Uri.fromFile(pdfFile);
				Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
				pdfIntent.setDataAndType(path, "application/pdf");
				pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

				try{
					startActivity(pdfIntent);
				}catch(ActivityNotFoundException e){
					Toast.makeText(AcademicsDeatailActivity.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
				}*/




			//}
			
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

		title = (TextView) findViewById(R.id.acd_desc_title);
		desc = (TextView) findViewById(R.id.acd_desc_detail);

	}



	


}