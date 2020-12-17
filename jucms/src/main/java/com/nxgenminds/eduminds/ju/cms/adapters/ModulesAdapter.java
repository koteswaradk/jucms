package com.nxgenminds.eduminds.ju.cms.adapters;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.models.ModulesModule;


public class ModulesAdapter extends BaseAdapter{

	private ArrayList<ModulesModule> listData;
	private LayoutInflater layoutInflater;
	private Context prova;
	private ProgressDialog mProgressDialog;

	public ModulesAdapter(Context context,ArrayList<ModulesModule> listData){
		this.listData = listData;
		prova = context;
	}

	@Override
	public int getCount() {
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (layoutInflater == null)
			layoutInflater = (LayoutInflater) prova.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(convertView == null){
			convertView = layoutInflater.inflate(R.layout.custom_module_view, null);
			holder = new ViewHolder();
			holder.textViewName = (TextView) convertView.findViewById(R.id.subjectName);
			holder.topicButtonCount = (Button) convertView.findViewById(R.id.subjectDownload);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}

		final ModulesModule moduleItem = (ModulesModule) listData.get(position);
		holder.textViewName.setText(moduleItem.getSubject_name());

		holder.topicButtonCount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder confirmAlert = new AlertDialog.Builder(prova);
				confirmAlert.setTitle("Download File...");
				confirmAlert.setMessage("Are you sure you want to download this file?");
				confirmAlert.setCancelable(true);

				confirmAlert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						new DownloadSubjectFile().execute(moduleItem.getDownload_file_path(),moduleItem.getDownload_file_name());
					}
				});

				confirmAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				confirmAlert.create().show();
			}
		});

		return convertView;
	}

	static class ViewHolder{

		Button topicButtonCount;
		TextView textViewName;

	}

	private class DownloadSubjectFile extends AsyncTask<String, Integer, String>{

		private PowerManager.WakeLock mWakeLock;


		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// take CPU lock to prevent CPU from going off if the user 
			// presses the power button during download
			// instantiate it within the onCreate method
			mProgressDialog = new ProgressDialog(prova);
			mProgressDialog.setMessage("Downloading File...");
			mProgressDialog.setIndeterminate(true);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mProgressDialog.setCancelable(false);
			PowerManager pm = (PowerManager) prova.getSystemService(Context.POWER_SERVICE);
			mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
					getClass().getName());
			mWakeLock.acquire();
			mProgressDialog.show();
		}


		@SuppressWarnings("resource")
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			InputStream input = null;
			OutputStream output = null;
			HttpURLConnection connection = null;
			try {
				URL url = new URL(params[0]);
				connection = (HttpURLConnection) url.openConnection();
				connection.connect();

				// expect HTTP 200 OK, so we don't mistakenly save error report
				// instead of the file
				if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
					return "Server returned HTTP " + connection.getResponseCode()
							+ " " + connection.getResponseMessage();
				}

				// this will be useful to display download percentage
				// might be -1: server did not report the length
				int fileLength = connection.getContentLength();

				// download the file
				input = connection.getInputStream();

				File folder = new File("/sdcard", "jucms");
				boolean check = folder.mkdirs();

				File myFile = new File("/sdcard/jucms/" + params[1].replace(" ", "")+".pdf");
				output = new FileOutputStream(myFile);

				byte data[] = new byte[4096];
				long total = 0;
				int count;
				while ((count = input.read(data)) != -1) {
					// allow canceling with back button
					if (isCancelled()) {
						input.close();
						return null;
					}
					total += count;
					// publishing the progress....
					if (fileLength > 0) // only if total length is known
						publishProgress((int) (total * 100 / fileLength));
					output.write(data, 0, count);
				}
			} catch (Exception e) {
				return e.toString();
			} finally {
				try {
					if (output != null)
						output.close();
					if (input != null)
						input.close();
				} catch (IOException ignored) {
				}

				if (connection != null)
					connection.disconnect();
			}
			return null;
		}


		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			mProgressDialog.setIndeterminate(false);
			mProgressDialog.setMax(100);
			mProgressDialog.setProgress(values[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			mWakeLock.release();
			mProgressDialog.dismiss();
			if (result != null)
				Toast.makeText(prova,"No file to Download", Toast.LENGTH_LONG).show();
			else
				Toast.makeText(prova,"File downloaded", Toast.LENGTH_SHORT).show();

		}
	}


}
