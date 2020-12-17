package com.nxgenminds.eduminds.ju.cms.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.Feedback.FeedBackQuestionModel;


public class FeedBackQuestionsAdapter extends BaseAdapter{

	private ArrayList<FeedBackQuestionModel> listData;
	private LayoutInflater layoutInflater;
	private Context prova;
     
	
	
	public FeedBackQuestionsAdapter(Context context,ArrayList<FeedBackQuestionModel> listData){
		this.listData = listData;
		prova = context;
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		
		final FeedBackQuestionModel question = (FeedBackQuestionModel) listData.get(position);
		
		if (layoutInflater == null)
		    layoutInflater = (LayoutInflater) prova.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(convertView == null){		
			convertView = layoutInflater.inflate(R.layout.custom_feedback_questions, null);
			holder = new ViewHolder();
			holder.feedBackQuestionNo = (TextView) convertView.findViewById(R.id.custom_feedback_Question_no);
			holder.feedBackQuestion = (TextView) convertView.findViewById(R.id.custom_feedback_Question);
			holder.mark= (TextView) convertView.findViewById(R.id.custom_feedback_seek_mark);
			holder.feedSeek = (SeekBar) convertView.findViewById(R.id.custom_feedback_seekbar);
			
			
			convertView.setTag(holder);
			
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.feedBackQuestionNo.setText(String.valueOf(position+1+". "));
		holder.feedBackQuestion.setText(question.getFeedback_question());
		
		
		holder.mark.setText(question.getFeedback_answer());
		int mark= Integer.parseInt(question.getFeedback_answer());
		holder.feedSeek.setProgress(mark);
		
		
		
		holder.feedSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
				// TODO Auto-generated method stub
				if(progress == 0){
					Toast.makeText(prova, "Give a valid Feedback", Toast.LENGTH_LONG).show();
					holder.mark.setVisibility(View.INVISIBLE);
				}
				else{
					
				holder.mark.setText(String.valueOf(progress));
				holder.mark.setVisibility(View.VISIBLE);
				question.setFeedback_answer(String.valueOf(progress));
				}
				
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
		}); 
		
		
		return convertView;
	}

	static class ViewHolder{
		TextView feedBackQuestion,feedBackQuestionNo,mark;
		SeekBar feedSeek;
	}

}
