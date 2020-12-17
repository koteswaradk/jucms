package com.nxgenminds.eduminds.ju.cms.models;

public class MemberBloodGroupModel {
	int bloodGroup_id;
	String bloodGroup;
	
	public void setBloodGroupId(int id)
	{
		this.bloodGroup_id =id;
	}
	public int getBloodGroupId()
	{
		return this.bloodGroup_id;
	}
	public void setBloodGroup(String bloodGroup)
	{
		this.bloodGroup = bloodGroup;
	}
	public String getBloodGroup(){
		return this.bloodGroup;
	}

}
