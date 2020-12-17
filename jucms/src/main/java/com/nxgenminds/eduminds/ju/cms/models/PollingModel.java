package com.nxgenminds.eduminds.ju.cms.models;

import java.util.ArrayList;

public class PollingModel {

	private int poll_count;
	private String poll_question;
	private int option_count;
	private int checkedOptionIndex;
	private String options[] = new String[10];
	private int vote[] = new int[10];
	private int pollId;
	private int flag_pollVoted;
	private int optionVoted;
	private ArrayList<PollingOptionsModel>  pollOptions = new ArrayList<PollingOptionsModel>(); 

	public void setOption(int index,String Option)
	{
		this.options[index] = Option;
	}

	public String getOption(int index)
	{
		return this.options[index];
	}
	public void setPollCount(int pcount)
	{
		this.poll_count = pcount;	
	}
	public void setPollQuestion(String pollquestion)
	{
		this.poll_question = pollquestion;
	}
	public void setOptionCount(int optionCount)
	{
		this.option_count = optionCount;
	}
	public int getPollCount()
	{
		return this.poll_count;
	}
	public String getPollQuestion()
	{
		return this.poll_question;
	}
	public int getOptionCount()
	{
		return this.option_count;
	}
	public ArrayList<PollingOptionsModel> getPollOptions() {
		return pollOptions;
	}
	public void setPollOptions(ArrayList<PollingOptionsModel> pollOptions) {
		this.pollOptions = pollOptions;
	}

	public void setVoteCount(int index,int vote)
	{
		this.vote[index] = vote;
	}
	public int getVoteCount(int index)
	{
		return this.vote[index];
	}
	public void setCheckedOptionIndex(int index)
	{
		this.checkedOptionIndex = index;
	}
	public int getCheckedOptionIndex()
	{
		return this.checkedOptionIndex;
	}
	public void setPollId(int id)
	{
		this.pollId = id;
	}
	public int getPollId()
	{
		return this.pollId;
	}
	public void setIsPollVoted(int i)
	{
		this.flag_pollVoted=i;	
	}
	public int getIsPollVoted()
	{
		return this.flag_pollVoted;
	}
	public void setOptionVoted(int index)
	{
		this.optionVoted=index;
	}
	public int getOptionVoted()
	{
		return this.optionVoted;

	}
}