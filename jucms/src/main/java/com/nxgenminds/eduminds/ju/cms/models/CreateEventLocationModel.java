package com.nxgenminds.eduminds.ju.cms.models;

public class CreateEventLocationModel {
	
	int city_id, state_id, country_id;
	String city,state,country;
	
	public void setCity(String city){
		this.city = city;
	}
	public void setState(String state){
		this.state = state;
	}
	public void setCountry(String country){
		this.country = country;
	}
	public void setCityId(int cityId){
		this.city_id = cityId;
	}
	public void setCountryId(int countryId){
		this.country_id = countryId;
	}
	public void setStateId(int stateId){
		this.state_id = stateId;
	}
	public String getCity(){
		return this.city;
	}
	public String getState()
	{
	 return this.state;	
	}
	public String getCountry()
	{
		return this.country;
	}
}
