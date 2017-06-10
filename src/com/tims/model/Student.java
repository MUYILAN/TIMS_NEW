package com.tims.model;

public class Student {

	private String stu_id;
	private String stu_name;
	private String stu_sex;
	private String stu_phone;
	private String stu_class;
	private String stu_passwd;
	
	public Student(String id, String name, String sex, String phone, String passwd){
		stu_id = id;
		stu_name = name;
		stu_sex = sex;
		stu_phone = phone;
		stu_passwd = passwd;
	}
	public String getId(){
		return stu_id;
	}
	public String getName(){
		return stu_name;
	}
	public String getSex(){
		return stu_sex;
	}
	public String getPhone(){
		return stu_phone;
	}
	public String getClassID(){
		return stu_class;
	}
	public String getPasswd(){
		return stu_passwd;
	}
}
