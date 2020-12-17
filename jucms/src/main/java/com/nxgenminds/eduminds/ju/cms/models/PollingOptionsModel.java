package com.nxgenminds.eduminds.ju.cms.models;

public class PollingOptionsModel {

	private int voteCount;
	private String option;


	public void setOption(String option)
	{
		this.option = option;
	}
	public String getOption()
	{
		return this.option;
	}

	public void setVoteCount(int voteCount)
	{
		this.voteCount=voteCount;
	}
	public int getVoteCount()
	{
		return this.voteCount;
	}

}
