package com.nxgenminds.eduminds.ju.cms.adapters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nxgenminds.eduminds.ju.cms.LearningOutcomeStudentActivity;
import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.models.LearningOutComeStudentModel;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpPutClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class LearningOutcomesStudentAdapter extends BaseAdapter{

	
	private ArrayList<LearningOutComeStudentModel> listData;
	private LayoutInflater layoutInflater;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private ProgressDialog mPDialog;
	private Context prova;
	private SimpleDateFormat df;
	SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
	AlertDialogManager alert = new AlertDialogManager();


	
	public LearningOutcomesStudentAdapter(Context context,ArrayList<LearningOutComeStudentModel> listData){
		this.listData = listData;
		imageLoader = ImageLoader.getInstance();
		prova = context;
		Log.i("LearningOutcomesStudentAdapter","inside LearningOutcomesStudentAdapter");
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.placeholder)
		.showImageForEmptyUri(R.drawable.placeholder) 
		.showImageOnFail(R.drawable.placeholder)
		.cacheInMemory(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		Log.i("LearningOutcomesStudentAdapter","Aft build");

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
		final ViewHolder holder;
		if (layoutInflater == null)
			layoutInflater = (LayoutInflater) prova.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		if(convertView == null){
			convertView = layoutInflater.inflate(R.layout.custom_student_outcomes, null);
			Log.i("LearningOutcomesStudentAdapter","Aft inflating layout");
			holder = new ViewHolder();
			holder.fromDate = (TextView) convertView.findViewById(R.id.custom__outcome_fromDate);
			holder.toDate = (TextView) convertView.findViewById(R.id.custom__outcome_toDate);
			holder.TextViewText = (EditText) convertView.findViewById(R.id.custom_outcome_type);
			holder.status=(ImageView)convertView.findViewById(R.id.outcome_edit);
			holder.remark=(TextView)convertView.findViewById(R.id.custom_outcome_remark);
			Log.i("LearningOutcomesStudentAdapter","Aft holder adding");
			if(Util.intership_flag)		
			{
				holder.status.setVisibility(View.GONE);
				Log.i("LearningOutcomesStudentAdapter","1");
			}else
			{
				holder.status.setVisibility(View.VISIBLE);
				Log.i("LearningOutcomesStudentAdapter","2");

			}
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		final LearningOutComeStudentModel outcomes = (LearningOutComeStudentModel) listData.get(position);


		try {

			df = new SimpleDateFormat("dd MMM");

			holder.fromDate.setText(df.format(input.parse(outcomes.getFrom_date())));
			Log.i("LearningOutcomesStudentAdapter","3");
			holder.toDate.setText(df.format(input.parse(outcomes.getTo_date())));
			Log.i("LearningOutcomesStudentAdapter","4");


		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}

		holder.TextViewText.setText(outcomes.getComment());
		Log.i("LearningOutcomesStudentAdapter","5");
		if(outcomes.getIs_approved().equalsIgnoreCase("0"))
		{
			Log.i("LearningOutcomesStudentAdapter","6");
			holder.status.setBackgroundResource(R.drawable.ic_edit_profile);
			Log.i("LearningOutcomesStudentAdapter","7");
		}else{
			holder.status.setBackgroundResource(R.drawable.ic_tick_green);
			Log.i("LearningOutcomesStudentAdapter","8");
		}

		if(outcomes.getIs_approval_remarks().equalsIgnoreCase("1"))
		{
			Log.i("LearningOutcomesStudentAdapter","9");
			holder.remark.setVisibility(View.VISIBLE);
			Log.i("LearningOutcomesStudentAdapter","10");
			holder.remark.setText(outcomes.getApproval_remarks());
			Log.i("LearningOutcomesStudentAdapter","11");

		}else{
			Log.i("LearningOutcomesStudentAdapter","12");
			holder.remark.setVisibility(View.INVISIBLE);
			Log.i("LearningOutcomesStudentAdapter","13");
			
		
		}
		
		/*if ((outcomes.getIs_approved().equalsIgnoreCase("0"))&&((outcomes.getIs_approval_remarks().equalsIgnoreCase("0")))) {
			holder.status.setBackgroundResource(R.drawable.ic_edit_profile);
			Log.i("LearningOutcomesStudentAdapter","not both  remark and aprove");
			holder.remark.setText(outcomes.getApproval_remarks());
			holder.remark.setVisibility(View.INVISIBLE);
			
		}
		
		 if ((outcomes.getIs_approved().equalsIgnoreCase("1"))&&((outcomes.getIs_approval_remarks().equalsIgnoreCase("0")))) {
			holder.status.setBackgroundResource(R.drawable.ic_tick_green);
			Log.i("LearningOutcomesStudentAdapter","only aprove");
			holder.remark.setText(outcomes.getApproval_remarks());
			holder.remark.setVisibility(View.INVISIBLE);
		}
		
			if ((outcomes.getIs_approved().equalsIgnoreCase("0"))&&((outcomes.getIs_approval_remarks().equalsIgnoreCase("1")))) {
				holder.status.setBackgroundResource(R.drawable.ic_edit_profile);
				Log.i("LearningOutcomesStudentAdapter","only remark ");
				holder.remark.setText(outcomes.getApproval_remarks());
				holder.remark.setVisibility(View.VISIBLE);
				
			}if((outcomes.getIs_approved().equalsIgnoreCase("1"))&&((outcomes.getIs_approval_remarks().equalsIgnoreCase("1")))){
				holder.status.setBackgroundResource(R.drawable.ic_tick_green);
				Log.i("LearningOutcomesStudentAdapter"," both aprove and remarks");
				holder.remark.setText(outcomes.getApproval_remarks());
				holder.remark.setVisibility(View.VISIBLE);
			}*/
			
			
			
			
		
	
		holder.status.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if(outcomes.getIs_approved().equalsIgnoreCase("0"))
				{
					Log.i("LearningOutcomesStudentAdapter","14");
					holder.TextViewText.setEnabled(true);
					Log.i("LearningOutcomesStudentAdapter","15");
					holder.TextViewText.setFocusable(true);
					Log.i("LearningOutcomesStudentAdapter","16");
					holder.TextViewText.setFocusableInTouchMode(true);
					
					Log.i("LearningOutcomesStudentAdapter","17");
				}
				else
				{
					Log.i("LearningOutcomesStudentAdapter","18");
					Toast.makeText(prova, "Outcome cannot be edited,  Its Approved !",Toast.LENGTH_SHORT).show();
				}
				

			}
		});

		holder.TextViewText.setOnEditorActionListener(
				new EditText.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_SEARCH ||actionId == EditorInfo.IME_ACTION_DONE ||event.getAction() == KeyEvent.ACTION_DOWN &&
								event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
							//	if (!event.isShiftPressed()) {
							Log.i("LearningOutcomesStudentAdapter","19");
							ConnectionDetector conn = new ConnectionDetector(prova);
							Log.i("LearningOutcomesStudentAdapter","19");
							if(conn.isConnectingToInternet()){
								Log.i("LearningOutcomesStudentAdapter","20");
								if(holder.TextViewText.getText().toString().trim().length()>3)
								{
									Log.i("LearningOutcomesStudentAdapter","21");
									new EditAsync().execute(holder.TextViewText.getText().toString().trim(),outcomes.getFrom_date(),outcomes.getTo_date(),outcomes.getLearning_outcome_id());
									Log.i("LearningOutcomesStudentAdapter","22");
									outcomes.setComment(holder.TextViewText.getText().toString().trim());
									Log.i("LearningOutcomesStudentAdapter","23");
									holder.TextViewText.setText(outcomes.getComment());
									Log.i("LearningOutcomesStudentAdapter","24");
									holder.TextViewText.setEnabled(false);
									Log.i("LearningOutcomesStudentAdapter","25");
								}else
								{
									Log.i("LearningOutcomesStudentAdapter","26");
									alert.showAlertDialog(prova, "Connection Error", "Outcome cannot be left blank !", false);
								}
							}else{
								Log.i("LearningOutcomesStudentAdapter","27");
								alert.showAlertDialog(prova, "Connection Error", "Please check your internet connection", false);
							}


							return true; // consume.
							//	}                
						}
						return false; // pass on to other listeners. 
					}
				});

		return convertView;
	}

	static class ViewHolder{
		TextView fromDate;
		TextView toDate,remark;
		EditText TextViewText;
		ImageView status;
	}
	private class EditAsync extends AsyncTask<String, Void, JSONObject>{
		@Override
		protected void onPreExecute(){
			Log.i("LearningOutcomesStudentAdapter","28");
			if(mPDialog == null){
				Log.i("LearningOutcomesStudentAdapter","29");
				mPDialog = Util.createProgressDialog(prova);
				mPDialog.setTitle("please wait..");
				Log.i("LearningOutcomesStudentAdapter","30");
				mPDialog.show();
				Log.i("LearningOutcomesStudentAdapter","31");
			} else {
				Log.i("LearningOutcomesStudentAdapter","32");
				mPDialog.setTitle("please wait...");
				Log.i("LearningOutcomesStudentAdapter","33");
				mPDialog.show();
				Log.i("LearningOutcomesStudentAdapter","34");
			}
		}
		@Override
		protected JSONObject doInBackground(String... params) {
			JSONObject jsonObjSend = new JSONObject();
			Log.i("LearningOutcomesStudentAdapter","35");
			String Comment = params[0];
			String from_date=params[1];
			String to_date=params[2];
			String outcome_id=params[3];
			Log.i("LearningOutcomesStudentAdapter","36");
			try {
				Log.i("LearningOutcomesStudentAdapter","37");
				jsonObjSend.put("comment",Comment);
				Log.i("LearningOutcomesStudentAdapter","38");
				jsonObjSend.put("from_date",from_date);
				Log.i("LearningOutcomesStudentAdapter","39");
				jsonObjSend.put("to_date",to_date);
				Log.i("LearningOutcomesStudentAdapter","40");
			} 
			catch (JSONException e) {
				e.printStackTrace();
			}
			Log.i("LearningOutcomesStudentAdapter","41");
			JSONObject jsonObjRecv = HttpPutClient.sendHttpPost(Util.API+"learning_outcome/"+outcome_id, jsonObjSend);
			Log.i("LearningOutcomesStudentAdapter","42");
			Log.i("jsonObjRecv",jsonObjRecv.toString());
			Log.i("LearningOutcomesStudentAdapter","43");
			return jsonObjRecv;
		}
		@Override
		protected void onPostExecute(JSONObject result){
			super.onPostExecute(result);
			mPDialog.dismiss();
			try {
				if(result.getString("status").equalsIgnoreCase("1"))
				{

					Log.i("LearningOutcomesStudentAdapter","38");
					/*Intent intent= new Intent(prova,LearningOutcomeStudentActivity.class); 
					prova.startActivity(intent);
					((Activity) prova).finish();*/
				}
				else
				{
					
					Log.i("LearningOutcomesStudentAdapter","39");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

	}
}
