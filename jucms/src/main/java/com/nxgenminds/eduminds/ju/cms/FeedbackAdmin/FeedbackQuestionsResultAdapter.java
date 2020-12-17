package com.nxgenminds.eduminds.ju.cms.FeedbackAdmin;

import java.util.ArrayList;

import com.nxgenminds.eduminds.ju.cms.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;



public class FeedbackQuestionsResultAdapter extends BaseAdapter {
	
	private ArrayList<FeedbackQuestionsResultModel> mArrayListFeedBackQuestionsResult;
    private Context mContext;
    
    
    public FeedbackQuestionsResultAdapter(Context c,ArrayList<FeedbackQuestionsResultModel>  arrayList){
    	this.mContext = c;
    	this.mArrayListFeedBackQuestionsResult = arrayList;
    }
   
	@Override
	public int getCount() {
		
		return mArrayListFeedBackQuestionsResult.size();
	}
	@Override
	public Object getItem(int position) {
		
		return mArrayListFeedBackQuestionsResult.get(position);
	}
	@Override
	public long getItemId(int position) {
		
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		
		FeedbackQuestionsResultModel questionResultData = mArrayListFeedBackQuestionsResult.get(position);
		ViewHolder holder;
		
		LayoutInflater inflater = LayoutInflater.from(mContext);
		if(convertView == null){
			
			convertView = inflater.inflate(R.layout.custom_view_feedback_teacher_detail_questions,parent,false);
			holder =  new ViewHolder();
					
			holder.questionImage =  (ImageView) convertView.findViewById(R.id.custom_view_feedback_teacher_detail_question_image);
			holder.questionName = (TextView) convertView.findViewById(R.id.custom_view_feedback_teacher_detail_question);
			
			convertView.setTag(holder);
			
		} else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		int  pos = position+1; 
		holder.questionName.setText("Question "+String.valueOf(pos));
		
		int answer = Integer.parseInt(questionResultData.getAnswer_avg());
		
		switch(answer){
		case 1:
			holder.questionImage.setBackground(mContext.getResources().getDrawable(R.drawable.one));
			break;
		case 2:
			holder.questionImage.setBackground(mContext.getResources().getDrawable(R.drawable.two));
			break;
		case 3:
			holder.questionImage.setBackground(mContext.getResources().getDrawable(R.drawable.three));
			break;
		case 4:
			holder.questionImage.setBackground(mContext.getResources().getDrawable(R.drawable.four));
			break;
		case 5:
			holder.questionImage.setBackground(mContext.getResources().getDrawable(R.drawable.five));
			break;
		case 6:
			holder.questionImage.setBackground(mContext.getResources().getDrawable(R.drawable.six));
			break;
		case 7:
			holder.questionImage.setBackground(mContext.getResources().getDrawable(R.drawable.seven));
			break;
		default:
			break;
		}
		
		return convertView;
	}
	
	class ViewHolder{
		
		TextView questionName;
		ImageView questionImage;
		
		}
    

}
