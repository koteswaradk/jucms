package com.nxgenminds.eduminds.ju.cms.Internship;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
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
import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.models.LearningOutComeStudentModel;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpPostClient;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class CompanyAdmin_InternAdapter extends BaseAdapter{

	private ArrayList<LearningOutComeStudentModel> listData;
	private LayoutInflater layoutInflater;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private ProgressDialog mPDialog;
	private Context prova;
	private SimpleDateFormat df;
	SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
	AlertDialogManager alert = new AlertDialogManager();
	private String student_id;

	public CompanyAdmin_InternAdapter(Context context,ArrayList<LearningOutComeStudentModel> listData){
		this.listData = listData;
		imageLoader = ImageLoader.getInstance();
		prova = context;

		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.placeholder)
		.showImageForEmptyUri(R.drawable.placeholder) 
		.showImageOnFail(R.drawable.placeholder)
		.cacheInMemory(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();

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
			convertView = layoutInflater.inflate(R.layout.custom_company_admin_learning_outcome, null);
			holder = new ViewHolder();
			holder.fromDate = (TextView) convertView.findViewById(R.id.company_custom__outcome_fromDate);
			holder.toDate = (TextView) convertView.findViewById(R.id.company_custom__outcome_toDate);
			holder.TextViewText = (TextView) convertView.findViewById(R.id.company_custom_outcome_type);
			holder.approved=(ImageView)convertView.findViewById(R.id.company_outcome_approve);
			holder.remark=(EditText)convertView.findViewById(R.id.company_custom_outcome_remark);

			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		final LearningOutComeStudentModel outcomes = (LearningOutComeStudentModel) listData.get(position);
		CompanyAdminDetailView thirdParty = (CompanyAdminDetailView) prova;
		student_id = thirdParty.FriendID;

		try {

			df = new SimpleDateFormat("dd MMM");

			holder.fromDate.setText(df.format(input.parse(outcomes.getFrom_date())));

			holder.toDate.setText(df.format(input.parse(outcomes.getTo_date())));



		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}

		holder.TextViewText.setText(outcomes.getComment());

		if(outcomes.getIs_approved().equalsIgnoreCase("0"))
		{
			holder.approved.setImageResource(R.drawable.ic_approve);
			if(outcomes.getIs_approval_remarks().equalsIgnoreCase("1"))
			{
				holder.remark.setText(outcomes.getApproval_remarks());
				holder.remark.setVisibility(View.VISIBLE);
				holder.remark.setEnabled(true);

			}else{
				holder.remark.setVisibility(View.VISIBLE);
				holder.remark.setEnabled(true);

			}

		}else{
			holder.approved.setImageResource(R.drawable.ic_tick_green);//// change with approve icon
			if(outcomes.getIs_approval_remarks().equalsIgnoreCase("1"))
			{
				holder.remark.setText(outcomes.getApproval_remarks());
				holder.remark.setVisibility(View.VISIBLE);
				holder.remark.setEnabled(false);

			}else{
				holder.remark.setVisibility(View.INVISIBLE);
				holder.remark.setEnabled(false);

			}
		}

		holder.approved.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(outcomes.getIs_approved().equalsIgnoreCase("0")){
					ConnectionDetector conn = new ConnectionDetector(prova);
					if(conn.isConnectingToInternet()){
						if(holder.TextViewText.getText().toString().trim().length()>3)
						{
							new ApproveAsync().execute(outcomes.getLearning_outcome_id());
							outcomes.setIs_approved("1");
							holder.approved.setImageResource(R.drawable.ic_tick_green);
							holder.remark.setEnabled(false);
						}else
						{
							alert.showAlertDialog(prova, "Connection Error", "Remark cannot be left blank !", false);
						}
					}else{
						alert.showAlertDialog(prova, "Connection Error", "Please check your internet connection", false);
					}
				}}
		});
		holder.remark.setOnEditorActionListener(
				new EditText.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_SEARCH ||actionId == EditorInfo.IME_ACTION_DONE ||event.getAction() == KeyEvent.ACTION_DOWN &&
								event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
							//	if (!event.isShiftPressed()) {
							ConnectionDetector conn = new ConnectionDetector(prova);
							if(conn.isConnectingToInternet()){
								if(holder.TextViewText.getText().toString().trim().length()>3)
								{
									new AddRemarkAsync().execute(outcomes.getLearning_outcome_id(),holder.remark.getText().toString().trim());
									outcomes.setApproval_remarks(holder.remark.getText().toString().trim());
									outcomes.setIs_approved("0");
									holder.remark.setText(outcomes.getApproval_remarks());
									holder.remark.setEnabled(true);
								}else
								{
									alert.showAlertDialog(prova, "Connection Error", "Remark cannot be left blank !", false);
								}
							}else{
								alert.showAlertDialog(prova, "Connection Error", "Please check your internet connection", false);
							}return true; 	              
						}
						return false;
					}
				});

		return convertView;
	}

	static class ViewHolder{
		TextView fromDate;
		TextView toDate,TextViewText;
		EditText remark;
		ImageView approved;
	}
	private class AddRemarkAsync extends AsyncTask<String, Void, JSONObject>{
		@Override
		protected void onPreExecute(){
			if(mPDialog == null){
				mPDialog = Util.createProgressDialog(prova);
				mPDialog.show();
			} else {
				mPDialog.show();
			}
		}
		@Override
		protected JSONObject doInBackground(String... params) {
			JSONObject jsonObjSend = new JSONObject();
			String learning_outcome_id = params[0];
			String remark_add=params[1];

			try {
				jsonObjSend.put("learning_outcome_id",learning_outcome_id);
				jsonObjSend.put("is_approved","0");
				jsonObjSend.put("approval_remarks",remark_add);
				jsonObjSend.put("student_id",student_id);
			} 
			catch (JSONException e) {
				e.printStackTrace();
			}

			JSONObject jsonObjRecv = HttpPostClient.sendHttpPost(Util.API+"learning_outcome_response", jsonObjSend);
			return jsonObjRecv;
		}
		@Override
		protected void onPostExecute(JSONObject result){
			super.onPostExecute(result);
			mPDialog.dismiss();
			try {
				if(result.getString("status").equalsIgnoreCase("1"))
				{
					Toast.makeText(prova, result.getString("message"), Toast.LENGTH_SHORT).show();
					
				}
				else
				{
					Toast.makeText(prova, "Something went wrong !", Toast.LENGTH_SHORT).show();

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

	}
	private class ApproveAsync extends AsyncTask<String, Void, JSONObject>{
		@Override
		protected void onPreExecute(){
			if(mPDialog == null){
				mPDialog = Util.createProgressDialog(prova);
				mPDialog.show();
			} else {
				mPDialog.show();
			}
		}
		@Override
		protected JSONObject doInBackground(String... params) {
			JSONObject jsonObjSend = new JSONObject();
			String learning_outcome_id = params[0];

			try {
				jsonObjSend.put("learning_outcome_id",learning_outcome_id);
				jsonObjSend.put("is_approved","1");
				jsonObjSend.put("student_id",student_id);
			} 
			catch (JSONException e) {
				e.printStackTrace();
			}

			JSONObject jsonObjRecv = HttpPostClient.sendHttpPost(Util.API+"learning_outcome_response", jsonObjSend);
			
			return jsonObjRecv;
		}
		@Override
		protected void onPostExecute(JSONObject result){
			super.onPostExecute(result);
			mPDialog.dismiss();
			try {
				if(result.getString("status").equalsIgnoreCase("1"))
				{
					Toast.makeText(prova, result.getString("message"), Toast.LENGTH_SHORT).show();
				}
				else
				{
					Toast.makeText(prova, "Something went wrong !", Toast.LENGTH_SHORT).show();

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

	}
}
