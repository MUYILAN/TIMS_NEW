package com.tims.model;

public class Attendance {

//	private int att_id;
	private int att_courseid;
	private String att_classid;
	private String att_date;
	private String att_studentid;
	private String att_studentname;
	private int att_ispresent;
	
	public Attendance(int courseid, String classid, String date, String studentid, String studentname, int ispresent){
//		att_id = id;
		att_courseid = courseid;
		att_classid = classid;
		att_date = date;		
		att_studentid = studentid;
		att_studentname = studentname;
		att_ispresent = ispresent;
	}
	public Attendance(String studentId, String studentName){
		att_studentid = studentId;
		att_studentname = studentName;
		att_ispresent = 2;
	}
	
	/*public int getId(){
		return att_id;
	}*/
	public int getCourseId(){
		return att_courseid;
	}
	public String getClassId(){
		return att_classid;
	}
	public String getDate(){
		return att_date;
	}
	public void setDate(String date){
		att_date = date;
	}
	public String getStudentId(){
		return att_studentid;
	}
	public String getStudentName(){
		return att_studentname;
	}
	public int getIsPresent(){
		return att_ispresent;
	}
	public void setIsPresent(int option){
		att_ispresent = option;
	}
	
}
