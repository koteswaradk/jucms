package com.nxgenminds.eduminds.ju.cms.FeedbackAdmin;

import java.util.ArrayList;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.internet.ConnectionDetector;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpGetClient;
import com.nxgenminds.eduminds.ju.cms.thirdpartyprofile.ThirdPartyTabMenuActivity;
import com.nxgenminds.eduminds.ju.cms.utils.AlertDialogManager;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class FeedbackTeacherDetailActivity extends ActionBarActivity{

	private TextView mTeacherName,mClassSection,mTotalStudents,
	mFeedbackGivenBy,mAcceptance,mGrade;

	private ImageView mTeacherProfileImage;

	private View mDivider;

	private GridView mGridQuestions;

	private AlertDialogManager alert = new AlertDialogManager();

	private ArrayList<FeedbackTeacherDetailModel> mArrayListFeedbackTeacherDetail = new ArrayList<FeedbackTeacherDetailModel>();

	private String mEventId,mTeacherSubjectId,mStreamId,mSemesterId,mSectionId,
	mStringTotalStudents,mStringAcceptance,mStringGrade,mStringFeedBackGivenBy,mStringClassSection,
	mStringTeacherName,mStringProfilePhotoPath,mTeacherId,mTeacherFirstName;

	private static String FEEDBACK_TEACHER_DETAIL_API=Util.API+"get_teacher_feedback_stat?event_id=+";

	private ImageLoader imageloader;
	private DisplayImageOptions options;
	private ProgressDialog mPDialog;
	private String qus1,qus2,qus3,qus4,qus5,qus6,qus7,qusId,total;
	private int qus1_per,qus2_per,qus3_per,qus4_per,qus5_per,qus6_per,qus7_per;
	TextView ques1,ques2,ques3,ques4,ques5,ques6,ques7;

	public static final int TEXT_SIZE_XHDPI = 34;
	public static final int TEXT_SIZE_HDPI = 25;
	public static final int TEXT_SIZE_MDPI = 18;
	public static final int TEXT_SIZE_LDPI = 13;

	private static int[] COLORS = new int[] {Color.argb(255, 142, 142,142),Color.argb(255, 229, 77, 67),Color.argb(255, 255, 149, 0),
		Color.argb(255, 59, 153, 216),Color.argb(255, 42, 187, 155),Color.argb(255, 240, 195, 48),Color.argb(255,238,130,238)};  
	public static CategorySeries mSeries = new CategorySeries("");  
	public static  DefaultRenderer mRenderer = new DefaultRenderer();  
	public static GraphicalView mChartView; 
	String[] NAME_LIST = new String[] { "Question 1", "Question 2","Question 3","Question 4","Question 5","Question 6","Question 7"}; 

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_feedback_teacher_detail);

		Bundle extras = getIntent().getExtras();
		if(extras!=null){

			mEventId = extras.getString("event_id");
			mTeacherSubjectId = extras.getString("teacher_subject_id");
			mStreamId = extras.getString("stream_id");
			mSemesterId = extras.getString("semester_id");
			mSectionId = extras.getString("section_id");
		}

		mTeacherName = (TextView) findViewById(R.id.activity_feedback_teacher_detail_name);
		mClassSection = (TextView)findViewById(R.id.activity_feedback_teacher_detail_class_section);

		mTotalStudents = (TextView) findViewById(R.id.activity_feedback_teacher_detail_total_students);
		mFeedbackGivenBy = (TextView) findViewById(R.id.activity_feedback_teacher_detail_feedback_given_by);

		mAcceptance = (TextView) findViewById(R.id.activity_feedback_teacher_detail_acceptance);
		mGrade = (TextView) findViewById(R.id.activity_feedback_teacher_detail_grade);

		mTeacherProfileImage = (ImageView) findViewById(R.id.activity_feedback_teacher_detail_image);
		mGridQuestions = (GridView) findViewById(R.id.activity_feedback_teacher_detail_questions_grid);

		mDivider = (View) findViewById(R.id.divider_view);

		imageloader = ImageLoader.getInstance();

		options = new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.showImageOnLoading(R.drawable.placeholder)
		.showImageForEmptyUri(R.drawable.placeholder) 
		.showImageOnFail(R.drawable.placeholder)
		.build();



		ConnectionDetector conn = new ConnectionDetector(FeedbackTeacherDetailActivity.this);
		if(conn.isConnectingToInternet()){
			new GetFeedBackTeacherDetail().execute();
		} else{
			alert.showAlertDialog(FeedbackTeacherDetailActivity.this,"Connection Error","Check your Internet Connection", false);
		}

		mGridQuestions.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {

				Object object = mGridQuestions.getItemAtPosition(position);
				FeedbackQuestionsResultModel data = (FeedbackQuestionsResultModel)  object;
				qusId=data.getFeedback_quest_id();

				ConnectionDetector conn = new ConnectionDetector(FeedbackTeacherDetailActivity.this);
				if(conn.isConnectingToInternet()){
					mSeries.clear();
					mRenderer.removeAllRenderers();
					new MemberStatsAsync().execute();
				} else{
					alert.showAlertDialog(FeedbackTeacherDetailActivity.this,"Connection Error","Check your Internet Connection", false);
				}

			}
		});

		mTeacherProfileImage.setOnClickListener(new OnClickListener() {
			
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(FeedbackTeacherDetailActivity.this,ThirdPartyTabMenuActivity.class);
				intent.putExtra("UserID",mTeacherId);
				intent.putExtra("ThirdPartyRole","teacher");
				Util.THIRD_PARTY_NAME  = mTeacherFirstName;
				Util.THIRD_PARTY_ID= mTeacherId;
				startActivity(intent);
				Util.intership_flag=true;			
			}
		});
	}


	private class GetFeedBackTeacherDetail extends AsyncTask<Void, Void, ArrayList<FeedbackTeacherDetailModel>>{

		private ArrayList<FeedbackQuestionsResultModel> mArrayListFeedbackQuestionsResult = new ArrayList<FeedbackQuestionsResultModel>();

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			// Showing progress dialog before sending http request			
			mPDialog = Util.createProgressDialog(FeedbackTeacherDetailActivity.this);
			mPDialog.setCancelable(false);
			mPDialog.show();}

		@Override
		protected ArrayList<FeedbackTeacherDetailModel> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			JSONArray connectionsResponse = null;
			JSONObject jsonObjectReceived = null;

			jsonObjectReceived = HttpGetClient.sendHttpPost(FEEDBACK_TEACHER_DETAIL_API+mEventId+
					"&teacher_subject_id="+mTeacherSubjectId+
					"&stream_id="+mStreamId+
					"&semester_id="+mSemesterId+
					"&section_id="+mSectionId);

			if(jsonObjectReceived != null){


				try{
					if(jsonObjectReceived.getString("status").equalsIgnoreCase("1")){
						JSONObject teacherDetails = jsonObjectReceived.getJSONObject("result");

						FeedbackTeacherDetailModel feedBackTeacherDetailModel = new FeedbackTeacherDetailModel();

						feedBackTeacherDetailModel.setTotal_students(teacherDetails.getString("total_students"));
						feedBackTeacherDetailModel.setTotal_students_given_the_answer(teacherDetails.getString("total_students_given_the_answer"));
						feedBackTeacherDetailModel.setStream_name(teacherDetails.getString("stream_name"));
						feedBackTeacherDetailModel.setSemester(teacherDetails.getString("semester"));
						feedBackTeacherDetailModel.setSubject_name(teacherDetails.getString("subject_name"));
						feedBackTeacherDetailModel.setSection_name(teacherDetails.getString("section_name"));
						feedBackTeacherDetailModel.setUser_id(teacherDetails.getString("user_id"));
						feedBackTeacherDetailModel.setFirstname(teacherDetails.getString("firstname"));
						
						mTeacherId = teacherDetails.getString("user_id");
						mTeacherFirstName = teacherDetails.getString("firstname");
						
						feedBackTeacherDetailModel.setLastname(teacherDetails.getString("lastname"));
						feedBackTeacherDetailModel.setUsername(teacherDetails.getString("username"));
						feedBackTeacherDetailModel.setTeacher_profile_photo(teacherDetails.getString("teacher_profile_photo"));
						feedBackTeacherDetailModel.setAnswer_percentage(teacherDetails.getString("answer_percentage"));
						feedBackTeacherDetailModel.setFeedback_colorcode_grade(teacherDetails.getString("feedback_colorcode_grade"));

						mStringTeacherName =  teacherDetails.getString("firstname")+" "+teacherDetails.getString("lastname");
						mStringTotalStudents = teacherDetails.getString("total_students");
						mStringFeedBackGivenBy = teacherDetails.getString("total_students_given_the_answer");

						total=teacherDetails.getString("total_students_given_the_answer");

						mStringClassSection = teacherDetails.getString("semester")+" - "+teacherDetails.getString("section_name")+" - "+
								teacherDetails.getString("subject_name");
						mStringGrade = teacherDetails.getString("feedback_colorcode_grade");
						mStringAcceptance = teacherDetails.getString("answer_percentage");
						mStringProfilePhotoPath = teacherDetails.getString("teacher_profile_photo");


						connectionsResponse = jsonObjectReceived.getJSONArray("feedback_questions_result");


						if(connectionsResponse!=null && connectionsResponse.length()>0){
							mArrayListFeedbackQuestionsResult.clear();
							for(int i = 0; i< connectionsResponse.length();i++){
								FeedbackQuestionsResultModel questionResultModel = new FeedbackQuestionsResultModel();
								JSONObject resultDetails;

								resultDetails = connectionsResponse.getJSONObject(i);

								questionResultModel.setFeedback_quest_id(resultDetails.getString("feedback_quest_id"));
								questionResultModel.setAnswer_avg(resultDetails.getString("answer_avg"));

								mArrayListFeedbackQuestionsResult.add(questionResultModel);
							}

							feedBackTeacherDetailModel.setArrayListFeedBackQuestionsResultModel(mArrayListFeedbackQuestionsResult);
							mArrayListFeedbackTeacherDetail.add(feedBackTeacherDetailModel);
						}


					} 
				}catch(JSONException e){
					e.printStackTrace();
				}
			}

			return mArrayListFeedbackTeacherDetail;


		}

		@Override
		protected void onPostExecute(ArrayList<FeedbackTeacherDetailModel> result) {
			// TODO Auto-generated method stub

			super.onPostExecute(result);
			mPDialog.dismiss();
			if(result == null){



			}else if(result.size()==0){


			} else{

				mTeacherName.setText(mStringTeacherName);
				mClassSection.setText(mStringClassSection);
				mTotalStudents.setText("Total Students:"+mStringTotalStudents);
				mFeedbackGivenBy.setText("Feedback Given By:"+mStringFeedBackGivenBy);

				Double roundedAcceptance = Double.parseDouble(mStringAcceptance);
				mStringAcceptance = String.valueOf(Math.round(roundedAcceptance));

				mAcceptance.setText("Acceptance:"+mStringAcceptance);
				mGrade.setText("Grade:"+mStringGrade);

				mDivider.setVisibility(View.VISIBLE);


				FeedbackQuestionsResultAdapter questionAdapter = new FeedbackQuestionsResultAdapter(FeedbackTeacherDetailActivity.this,result.get(0).getArrayListFeedBackQuestionsResultModel());
				mGridQuestions.setAdapter(questionAdapter);

				imageloader.displayImage(mStringProfilePhotoPath, mTeacherProfileImage, options, new SimpleImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
					}

					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

					}
				}, new ImageLoadingProgressListener() {
					@Override
					public void onProgressUpdate(String imageUri, View view, int current,
							int total) {

					}
				});

			}
		}
	}

	private class MemberStatsAsync extends AsyncTask<Void, Void, JSONObject>{

		@Override
		protected void onPreExecute() {
			if(mPDialog == null){
				mPDialog = Util.createProgressDialog(FeedbackTeacherDetailActivity.this);
				mPDialog.setCancelable(false);
				mPDialog.show();}
			else{
				mPDialog.setCancelable(false);
				mPDialog.show();
			}}

		@Override
		protected JSONObject doInBackground(Void... params) {
			// TODO Auto-generated method stub
			JSONObject jsonObjectRecived = HttpGetClient.sendHttpPost(Util.API+
					"get_feedback_pichart?feedback_quest_id="+qusId+"&teacher_subject_id="+mTeacherSubjectId+"&event_id="+mEventId);
			if(jsonObjectRecived != null){
				try {
					if(jsonObjectRecived.getString("error").equalsIgnoreCase("false")){
						JSONObject StatDetails = jsonObjectRecived.getJSONObject("result");

						qus1 = StatDetails.getString("answer_one_count");
						qus2 = StatDetails.getString("answer_two_count");
						qus3 = StatDetails.getString("answer_three_count");
						qus4 = StatDetails.getString("answer_four_count");
						qus5 = StatDetails.getString("answer_five_count");
						qus6 = StatDetails.getString("answer_six_count");
						qus7 = StatDetails.getString("answer_seven_count");
					}
					else
					{
						return null;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return jsonObjectRecived;
			}
			else
			{
				return null;
			}

		}

		@Override
		protected void onPostExecute(JSONObject result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			mPDialog.dismiss();

			if(result == null){
				qus1="0";
				qus2="0";
				qus3="0";
				qus4="0";
				qus5="0";
				qus6="0";
				qus7="0";
				total="0";

			}else{
				qus1_per=(Integer.parseInt(qus1)*100)/(Integer.parseInt(total));
				qus2_per=(Integer.parseInt(qus2)*100)/(Integer.parseInt(total));
				qus3_per=(Integer.parseInt(qus3)*100)/(Integer.parseInt(total));
				qus4_per=(Integer.parseInt(qus4)*100)/(Integer.parseInt(total));
				qus5_per=(Integer.parseInt(qus5)*100)/(Integer.parseInt(total));
				qus6_per=(Integer.parseInt(qus6)*100)/(Integer.parseInt(total));
				qus7_per=(Integer.parseInt(qus7)*100)/(Integer.parseInt(total));

				double[] VALUES = new double[] {qus1_per,qus2_per,qus3_per,qus4_per,qus5_per,qus6_per,qus7_per };  

				for (int i = 0; i < VALUES.length; i++) {  
					mSeries.add(NAME_LIST[i] + " " + VALUES[i], VALUES[i]);  
					SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();  
					renderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);  
					mRenderer.addSeriesRenderer(renderer); 
					//renderer.setChartValuesFormat(NumberFormat.getPercentInstance());// Setting percentage
					
				}

				///
				if (mChartView != null) {  
					mChartView.repaint();  
				}  

				final Dialog dialog = new Dialog(FeedbackTeacherDetailActivity.this);  
				dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);  
				dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
						WindowManager.LayoutParams.FLAG_FULLSCREEN);  
				dialog.setContentView(R.layout.fragment_insights);  


				mChartView = ChartFactory.getPieChartView(FeedbackTeacherDetailActivity.this,mSeries, mRenderer);

				mChartView.setOnClickListener(new View.OnClickListener() { 
					@Override
					public void onClick(View v) {
						SeriesSelection seriesSelection =mChartView.getCurrentSeriesAndPoint();
						if (seriesSelection == null) {

						} else {
							for (int i = 0; i <mSeries.getItemCount(); i++) {
								mRenderer.getSeriesRendererAt(i).setHighlighted(i == seriesSelection.getPointIndex());
							}
							mChartView.repaint();
							// Toast.makeText( getActivity(), "Chart data point index " + seriesSelection.getPointIndex() + " selected" + " point value=" + seriesSelection.getValue(), Toast.LENGTH_SHORT).show();
						}
					}
				});
				mRenderer.setClickEnabled(true);  
				mRenderer.setStartAngle(180);
				mRenderer.setDisplayValues(false);
				mRenderer.setShowLabels(false);
				mRenderer.setShowLegend(false);
				mRenderer.setZoomEnabled(false);
				mRenderer.setPanEnabled(false); 
				mRenderer.setLabelsColor(Color.argb(255, 255, 255, 255));
				switch (getResources().getDisplayMetrics().densityDpi) {
				case DisplayMetrics.DENSITY_XHIGH:
					mRenderer.setMargins(new int[] { 40, 90, 25, 10 });
					mRenderer.setChartTitleTextSize(TEXT_SIZE_XHDPI);
					mRenderer.setLabelsTextSize(TEXT_SIZE_XHDPI);

					break;
				case DisplayMetrics.DENSITY_HIGH:
					mRenderer.setMargins(new int[] { 30, 50, 20, 10 });
					mRenderer.setChartTitleTextSize(TEXT_SIZE_HDPI);
					mRenderer.setLabelsTextSize(TEXT_SIZE_HDPI);
					break;
				default:
					mRenderer.setMargins(new int[] { 20, 30, 15, 0 });
					mRenderer.setChartTitleTextSize(TEXT_SIZE_MDPI);
					mRenderer.setLabelsTextSize(TEXT_SIZE_MDPI);
					break;
				}

				dialog.show(); 

				final LinearLayout layout = (LinearLayout)dialog.findViewById(R.id.chart_container);
				layout.addView(mChartView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)); 
				ques1=(TextView)dialog.findViewById(R.id.ques1);
				ques2=(TextView)dialog.findViewById(R.id.ques2);
				ques3=(TextView)dialog.findViewById(R.id.ques3);
				ques4=(TextView)dialog.findViewById(R.id.ques4);
				ques5=(TextView)dialog.findViewById(R.id.ques5);
				ques6=(TextView)dialog.findViewById(R.id.ques6);
				ques7=(TextView)dialog.findViewById(R.id.ques7);

				ques1.setText(qus1_per+"%");
				ques2.setText(qus2_per+"%");
				ques3.setText(qus3_per+"%");
				ques4.setText(qus4_per+"%");
				ques5.setText(qus5_per+"%");
				ques6.setText(qus6_per+"%");
				ques7.setText(qus7_per+"%");
			}
		}
	}

}
