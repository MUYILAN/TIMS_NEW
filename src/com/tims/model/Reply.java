package com.tims.model;

public class Reply {

	private String rep_type;
	private String rep_typeid;
	private String rep_content;
	private String rep_date;
	private String rep_typename;
	
	public Reply(String type, String typeid, String content, String date, String typename){
		rep_type = type;
		rep_typeid = typeid;
		rep_content = content;
		rep_date = date;
		rep_typename = typename;
	}
	
	public String getType(){
		return rep_type;
	}
	public String getTypeId(){
		return rep_typeid;
	}
	public String getContent(){
		return rep_content;
	}
	public String getDate(){
		return rep_date;
	}
	public String getTypeName(){
		return rep_typename;
	}
}
