package com.tims.model;

public class Message {
	private String mes_content;
	private String mes_date;
	
	public Message(String content, String date){
		mes_content = content;
		mes_date = date;
	}
	public String getContent(){
		return mes_content;
	}
	
	public String getDate()
	{
		return mes_date;
	}
}
