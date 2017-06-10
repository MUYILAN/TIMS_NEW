package com.tims.model;

public class Source {
	private int sou_id;
	private int sou_courseid;
	private String sou_teachername;
	private String sou_filename;
	private String sou_date;
	
	public Source(int id, int courseid, String teachername, String filename, String date){
		sou_id = id;
		sou_courseid = courseid;
		sou_teachername = teachername;
		sou_filename = filename;
		sou_date = date;
	}
	
	public int getId(){
		return sou_id;
	}
	
	public int getCourseId(){
		return sou_courseid;
	}
	
	public String getTeacherName(){
		return sou_teachername;
	}
	
	public String getFileName(){
		return sou_filename;
	}
	public String getDate()
	{
		return sou_date;
	}
}
