package com.nxgenminds.eduminds.ju.cms.models;

public class MemberGenderModel {
	
	public int gender_id;
	public String gender_name;
	
public void setGenderName(String genderName)
{
	this.gender_name = genderName;
}
public void setGenderId(int genderId)
{
	this.gender_id = genderId;
}
public String getGenderName()
{
	return this.gender_name;
}
public int getGenderId()
{
	return this.gender_id;
}
}
