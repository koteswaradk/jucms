package com.nxgenminds.eduminds.ju.cms.placementsboard;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Random;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.ViewProfilePhoto;
import com.nxgenminds.eduminds.ju.cms.noticeboard.TouchImageView.OnTouchImageViewListener;
import com.polites.android.GestureImageView;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.RectF;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class PlacementsBoardImageDisplayActivity extends FragmentActivity{
	private String imagePath,title;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private Bitmap showedImgae;
	public Menu menuInstance;
	private GestureImageView imageView;
	private MediaScannerConnection msConn = null;
	TextView textview;
	@SuppressWarnings("static-access")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.noticeboarddisplayimage);

		Bundle bundle = getIntent().getExtras();
		//imagelink_url=bundle.getString("noticeimagelink");
		assert bundle != null;
		imagePath = bundle.getString("noticeimagelink");
		title=bundle.getString("noticetitle");
		imageView = (GestureImageView)findViewById(R.id.image);
		textview=(TextView) findViewById(R.id.textdisplay);
		textview.setText(title);
		final ProgressBar spinner = (ProgressBar)findViewById(R.id.loading);

		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.ic_empty)
		.showImageOnFail(R.drawable.ic_error)
		.resetViewBeforeLoading(true)
		.cacheOnDisc(true)
		.imageScaleType(ImageScaleType.EXACTLY)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.considerExifParams(true)
		.displayer(new FadeInBitmapDisplayer(300))
		.build();

		imageLoader.getInstance().displayImage(imagePath, imageView, options, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				spinner.setVisibility(View.VISIBLE);
			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				String message = null;
				switch (failReason.getType()) {
				case IO_ERROR:
					message = "Input/Output error";
					break;
				case DECODING_ERROR:
					message = "Image can't be decoded";
					break;
				case NETWORK_DENIED:
					message = "Downloads are denied";
					break;
				case OUT_OF_MEMORY:
					message = "Out Of Memory error";
					break;
				case UNKNOWN:
					message = "Unknown error";
					break;
				}

				spinner.setVisibility(View.GONE);
			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

				showedImgae = loadedImage;
				PlacementsBoardModel noticemodel=new PlacementsBoardModel();

				noticemodel.setBit_image(loadedImage);
				spinner.setVisibility(View.GONE);
			}
		});
	}

	public void downloadImage(){

		String root = Environment.getExternalStorageDirectory().toString();
		File myDir = new File(root + "/DCIM/JUCMS");    
		myDir.mkdirs();
		Random generator = new Random();
		int n = 10000;
		n = generator.nextInt(n);
		String fname = "jucms-"+ n +".jpg";
		File file = new File (myDir, fname);
		if (file.exists ()) file.delete (); 
		try {
			FileOutputStream out = new FileOutputStream(file);
			showedImgae.compress(Bitmap.CompressFormat.JPEG, 100, out);
			Toast.makeText(PlacementsBoardImageDisplayActivity.this, "Image Saved", Toast.LENGTH_SHORT).show();
			out.flush();
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		Uri contentUri = Uri.fromFile(file);
		mediaScanIntent.setData(contentUri);
		getApplicationContext().sendBroadcast(mediaScanIntent);
	}

	@Override
	public boolean onCreateOptionsMenu (Menu menu) {
		menuInstance = menu;
		getMenuInflater().inflate(R.menu.save_image, menu);
		return super.onCreateOptionsMenu(menu);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.saveImage: 
			downloadImage();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	/*TouchImageView img;
    Bitmap bitmap;
    ProgressDialog pDialog;
	String imagelink_url;
	TextView _noticetitledisplay;
	private DecimalFormat df;
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	    	setContentView(R.layout.imagedisplay);
	    	df = new DecimalFormat("#.##");
	    	Bundle extras = getIntent().getExtras();
	    	 img = (TouchImageView)findViewById(R.id.img);
		    _noticetitledisplay= (TextView) findViewById(R.id.noticetitledisplay);
		    
		    
	    	if (extras==null) {
	    		_noticetitledisplay.setText("sorry no data to display");
			}else{
	    	imagelink_url=extras.getString("noticeimagelink");
			 bitmap = (Bitmap) extras.getParcelable("BitmapImage");
	    	_noticetitledisplay.setText(extras.getString("noticetitle"));
	    	Log.i("path", imagelink_url);
	       
	        new LoadImage().execute(imagelink_url);
	 
			}
	    	img.setOnTouchImageViewListener(new OnTouchImageViewListener() {
				
				@Override
				public void onMove() {
					// TODO Auto-generated method stub
					PointF point = img.getScrollPosition();
					RectF rect = img.getZoomedRect();
					float currentZoom = img.getCurrentZoom();
					boolean isZoomed = img.isZoomed();
					scrollPositionTextView.setText("x: " + df.format(point.x) + " y: " + df.format(point.y));
					zoomedRectTextView.setText("left: " + df.format(rect.left) + " top: " + df.format(rect.top)
							+ "\nright: " + df.format(rect.right) + " bottom: " + df.format(rect.bottom));
					currentZoomTextView.setText("getCurrentZoom(): " + currentZoom + " isZoomed(): " + isZoomed);
				}
			});	
	    	
	    }
  
	    
	    public class LoadImage extends AsyncTask<String, String, Bitmap> {
	        @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            pDialog = new ProgressDialog(NoticeBoardImageDisplayActivity.this);
	            pDialog.setMessage("Loading Image ....");
	            pDialog.show();
	 
	        }
	        
	         protected Bitmap doInBackground(String... args) {
	             try {
	            	 
	            	 
	                   bitmap = BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());
	 
	            } catch (Exception e) {
	                  e.printStackTrace();
	            }
	            return bitmap;
	         }
	 
	         protected void onPostExecute(Bitmap image) {
	 
	             if(image != null){
	             img.setImageBitmap(image);
	             pDialog.dismiss();
	 
	             }else{
	 
	             pDialog.dismiss();
	             Toast.makeText(NoticeBoardImageDisplayActivity.this, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();
	 
	             }
	             
	         }
	         
	         
	     }*/
	    
	
}
