package com.tims.model;

import java.io.Serializable;

public class Subject implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int sub_id;
	private String sub_studentid;
	private String sub_studentname;
	private String sub_content;
	private String sub_date;
	private int sub_reply;
	
	public Subject(int id, String studentid, String content, String date, int reply, String studentname){
		sub_id = id;
		sub_studentid = studentid;
		sub_content = content;
		sub_date = date;
		sub_reply = reply;
		sub_studentname = studentname;
	}
	
	public int getId(){
		return sub_id;
	}
	public String getStudentId(){
		return sub_studentid;
	}
	public String getStudentName(){
		return sub_studentname;
	}
	public String getContent(){
		return sub_content;
	}
	public String getDate(){
		return sub_date;
	}
	public int getReply(){
		return sub_reply;
	}
}
