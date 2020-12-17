package com.nxgenminds.eduminds.ju.cms.adapters;
//changed as backup on 25.03.2014
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nxgenminds.eduminds.ju.cms.R;
import com.nxgenminds.eduminds.ju.cms.models.PollingModel;
import com.nxgenminds.eduminds.ju.cms.models.PollingOptionsModel;
import com.nxgenminds.eduminds.ju.cms.proxy.HttpPostClient;
import com.nxgenminds.eduminds.ju.cms.utils.Util;


public class PollingAdapter extends BaseAdapter  {
	private Context context;
	private ArrayList<PollingModel> arrayList_poll;
	private int optionVoteCount[] = new int[10];
	private String pollVotesURL = Util.API + "poll_stats";
	private int selectedIndex =-1;
	public PollingAdapter(Context c,ArrayList<PollingModel> pollData)
	{	
		this.context = c;
		this.arrayList_poll = pollData;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrayList_poll.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return arrayList_poll.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v=convertView;
		Holder holder = new Holder();
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if(convertView == null)
		{    
			v = inflater.inflate(R.layout.polling_list,null);
			TextView pq = (TextView) v.findViewById(R.id.textView_poll_question);
			LinearLayout layout = (LinearLayout)v.findViewById(R.id.linearLayout_pollOptions);
			holder.txtview_pollquestion = pq;
			holder.linearLayout_options = layout; 
			v.setTag(holder);
		}
		else
		{ 
			holder = (Holder)v.getTag();
		}

		final PollingModel   pollDetail = (PollingModel) arrayList_poll.get(position);
		final int optioncount = pollDetail.getOptionCount();
		final int pollVoted = pollDetail.getIsPollVoted(); 
		final int optionVoted = pollDetail.getOptionVoted();
		holder.linearLayout_options.removeAllViewsInLayout();
		final TextView textView_option[] = new TextView[10];
		final TextView textView_vote[] = new TextView[10];
		final CheckBox checkBox_choice[] = new CheckBox[10];
		ArrayList<PollingOptionsModel> arrayList_pollOptions = pollDetail.getPollOptions();
		for(int i=0;i<optioncount;i++)
		{   

			final int j=i;
			final PollingOptionsModel pOptions = arrayList_pollOptions.get(i);
			String sOption = pollDetail.getOption(i);

			final View view;
			view = LayoutInflater.from(context).inflate(R.layout.polling_list_options,null);

			textView_option[i] = (TextView) view.findViewById(R.id.textView_option);
			textView_vote[i] = (TextView)view.findViewById(R.id.textView_VoteCount);
			checkBox_choice[i] = (CheckBox)view.findViewById(R.id.radio_Optionvote);
			checkBox_choice[i].setId(i);

			holder.linearLayout_options.addView(view);
			textView_option[i].setText(sOption);
			textView_vote[i].setText(String.valueOf(pOptions.getVoteCount()).concat("  votes "));
			if(pollVoted ==1)
			{
				textView_vote[i].setVisibility(View.VISIBLE);
				checkBox_choice[i].setVisibility(View.GONE);

				if(i == (optionVoted-1))
				{
					textView_option[i].setBackgroundColor(Color.rgb(07, 138, 75));
				}
			}
			else
			{
				textView_vote[i].setVisibility(View.INVISIBLE);
			}
			checkBox_choice[i].setOnCheckedChangeListener(new OnCheckedChangeListener()
			{
				@Override
				public void onCheckedChanged(CompoundButton button, boolean isChecked) {
					// TODO Auto-generated method stub


					if(isChecked){   
						checkBox_choice[j].setVisibility(View.GONE);
						textView_option[j].setBackgroundColor(Color.rgb(07, 138, 75));

						switch(button.getId())
						{
						case 0:
							executeLoop(1,optioncount,checkBox_choice,textView_vote);
							/*for(int i=1;i<optioncount;i++)
								{
									checkBox_choice[i].setChecked(false);
								}*/
							break;
						case 1:
							checkBox_choice[0].setVisibility(View.GONE);
							textView_vote[0].setVisibility(View.VISIBLE);
							executeLoop(2,optioncount,checkBox_choice,textView_vote);
							/*for(int i=2;i<optioncount;i++)
								{
									checkBox_choice[i].setChecked(false);
								}*/
							break;
						case 2:
							executeLoop(0,2,checkBox_choice,textView_vote);
							executeLoop(3,optioncount,checkBox_choice,textView_vote);
							/*for(int i=0;i<2;i++)
								{
								checkBox_choice[i].setChecked(false);
								}
								for(int i=3;i<optioncount;i++)
								{
									checkBox_choice[i].setChecked(false);
								}*/
							break;
						case 3:
							executeLoop(0,3,checkBox_choice,textView_vote);
							executeLoop(4,optioncount,checkBox_choice,textView_vote);
							/*for(int i=0;i<3;i++)
								{
								checkBox_choice[i].setChecked(false);
								}
								for(int i=4;i<optioncount;i++)
								{
									checkBox_choice[i].setChecked(false);
								}*/
							break;
						case 4:
							executeLoop(0,4,checkBox_choice,textView_vote);
							executeLoop(5,optioncount,checkBox_choice,textView_vote);
							/*for(int i=0;i<4;i++)
								{
								checkBox_choice[i].setChecked(false);
								}
								for(int i=5;i<optioncount;i++)
								{
									checkBox_choice[i].setChecked(false);
								}*/
							break;
						case 5:
							executeLoop(0,5,checkBox_choice,textView_vote);
							executeLoop(6,optioncount,checkBox_choice,textView_vote);
							/*for(int i=0;i<5;i++)
								{
								checkBox_choice[i].setChecked(false);
								}
								for(int i=6;i<optioncount;i++)
								{
									checkBox_choice[i].setChecked(false);
								}*/
							break;
						case 6:
							executeLoop(0,6,checkBox_choice,textView_vote);
							executeLoop(7,optioncount,checkBox_choice,textView_vote);
							/*for(int i=0;i<6;i++)
								{
								checkBox_choice[i].setChecked(false);
								}
								for(int i=7;i<optioncount;i++)
								{
									checkBox_choice[i].setChecked(false);
								}*/
							break;
						case 7:
							executeLoop(0,7,checkBox_choice,textView_vote);

							/*for(int i=0;i<7;i++)
								{
								checkBox_choice[i].setChecked(false);
								}*/


							break;
						case 8:
							executeLoop(0,8,checkBox_choice,textView_vote);
							/*for(int i=0;i<8;i++)
								{
									checkBox_choice[i].setChecked(false);
								}*/
							break;
						case 9:
							executeLoop(0,9,checkBox_choice,textView_vote);
							/*for(int i=0;i<9;i++)
								{
									checkBox_choice[i].setChecked(false);
								}*/
							break;


						}

						int count = pOptions.getVoteCount();
						System.out.println("Vote Count before:"+count);
						final int pollId = pollDetail.getPollId();

						count = count+1;
						System.out.println("Vote Count after:"+count);
						textView_vote[j].setVisibility(View.VISIBLE);
						textView_vote[j].setText(String.valueOf(count).concat(" votes  "));
						pOptions.setVoteCount(count);
						pollDetail.setOptionVoted(j+1);
						pollDetail.setIsPollVoted(1);


						class PollVoteAsyncClass extends AsyncTask<Void, Void, Void>{

							@Override
							protected Void doInBackground(Void... params) {
								// TODO Auto-generated method stub
								JSONArray connectionsResponse = null;
								JSONObject PollVotesJsonObject = new JSONObject();
								String optionKey;
								try
								{
									System.out.println(j);
									int k = j+1;
									PollVotesJsonObject.put("poll_id",pollId);
									optionKey = "poll_option_".concat(String.valueOf(k));
									PollVotesJsonObject.put(optionKey, 1);
								}

								catch(JSONException e)
								{
									e.printStackTrace();
								}
								System.out.println("<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>"+PollVotesJsonObject.toString());
								JSONObject jsonObjectRecived = HttpPostClient.sendHttpPost(pollVotesURL,PollVotesJsonObject);
								return null;

							}

							@Override
							protected void onPostExecute(Void result)
							{
								System.out.println("Post Execute");
							}
						}	
						new PollVoteAsyncClass().execute();
					}
				}});

		}

		holder.txtview_pollquestion.setText(pollDetail.getPollQuestion());

		return v;
	}

	public void executeLoop(int start,int n,CheckBox[] check,TextView[] textView)
	{
		for(int i=start;i<n;i++)
		{
			check[i].setVisibility(View.GONE);
			textView[i].setVisibility(View.VISIBLE);

		}
	}
}
class Holder
{
	TextView txtview_pollquestion;
	LinearLayout linearLayout_options;
}
class ChildHolder
{
	TextView txtview_pollOption;
	TextView txtview_pollVoteCount;
	CheckBox checkBox_Vote;
}




