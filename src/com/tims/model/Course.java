package com.tims.model;

public class Course {

	private int cou_id;
	private String cou_name;
	private int cou_score;
	
	public Course(int id, String name){
		cou_id = id;
		cou_name = name;
	}
	public void setId(int id){
		cou_id = id;
	}
	public int getId(){
		return cou_id;
	}
	public void setName(String name){
		cou_name = name;
	}
	public String getName(){
		return cou_name;
	}
	public void  setScore(int score){
		cou_score = score;
	}
	public int getScore(){
		return cou_score;
	}
}
