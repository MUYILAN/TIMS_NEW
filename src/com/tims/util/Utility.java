package com.tims.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tims.model.Attendance;
import com.tims.model.Curriculum;
import com.tims.model.Message;
import com.tims.model.Reply;
import com.tims.model.Source;
import com.tims.model.Subject;

import android.content.Context;


public class Utility {

	/*public synchronized static List<Course> handleCoursesResponse(Context context, String response){
		String[] allCourses = response.split(",");
		courses = new Course[allCourses.length];
		name = new String[allCourses.length];
		for(int i = 0; i < allCourses.length; i++ ){
			String[] array = allCourses[i].split("\\|");
			LogUtil.d("array0", array[0]);
			LogUtil.d("array1", array[1]);
			courses[i] = new Course(Integer.parseInt(array[0]),array[1]);
			name[i] =array[1];
		}
		return false;
	}*/
	/**
	* 
	*/
	public static List<Subject> handleSubjectResponse(Context context, String response) {
		List<Subject> subjectList = new ArrayList<Subject>();
		try {
			JSONArray jsonArray = new JSONArray(response.toString());
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject j = jsonArray.getJSONObject(i);
				Subject sub = new Subject(Integer.parseInt(j.getString("id")), j.getString("studentid"), j.getString("content"), j.getString("date"), Integer.parseInt(j.getString("reply")), j.getString("studentname"));
				subjectList.add(sub);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return subjectList;
	}
	public static List<Reply> handleReplyResponse(Context context, String response) {
		List<Reply> replyList = new ArrayList<Reply>();
		try {
			JSONArray jsonArray = new JSONArray(response.toString());
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject j = jsonArray.getJSONObject(i);
				Reply rep = new Reply(j.getString("type"), j.getString("typeid"), j.getString("content"), j.getString("date"), j.getString("typename"));
				replyList.add(rep);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return replyList;
	}
	public static Map<String,List<Curriculum>> handleCurriculumResponse(Context context, String response) {
		List<Curriculum> monList = new ArrayList<Curriculum>();
		List<Curriculum> tueList = new ArrayList<Curriculum>();
		List<Curriculum> wenList = new ArrayList<Curriculum>();
		List<Curriculum> thuList = new ArrayList<Curriculum>();
		List<Curriculum> friList = new ArrayList<Curriculum>();
		List<Curriculum> staList = new ArrayList<Curriculum>();
		List<Curriculum> sunList = new ArrayList<Curriculum>();
		Map<String,List<Curriculum>> courseInfoMap = new HashMap<String,List<Curriculum>>();
		try {
			JSONArray jsonArray = new JSONArray(response.toString());
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject j = jsonArray.getJSONObject(i);
				Curriculum cur = new Curriculum(j.getString("coursename"), j.getString("teachername"), j.getInt("startweek"), j.getInt("endweek"),
						j.getInt("oddoreven"), j.getInt("weeknum"), j.getInt("startsection"), j.getInt("sectionnums"), j.getString("classroom"));
				
				switch(cur.getWeekNum()){
				case 1:
					monList.add(cur);
					break;
				case 2:
					tueList.add(cur);
					break;
				case 3:
					wenList.add(cur);
					break;
				case 4:
					thuList.add(cur);
					break;
				case 5:
					friList.add(cur);
					break;
				case 6:
					staList.add(cur);
					break;
				case 7:
					sunList.add(cur);
					break;
				default:
					break;
				}
			}
			courseInfoMap.put("mon", monList);
			courseInfoMap.put("tue", tueList);
			courseInfoMap.put("wen", wenList);
			courseInfoMap.put("thu", thuList);
			courseInfoMap.put("fri", friList);
			courseInfoMap.put("sta", staList);
			courseInfoMap.put("sun", sunList);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return courseInfoMap;
	}
	public static List<Attendance> handleAttendResponse(Context context, String response) {
		// TODO Auto-generated method stub
		List<Attendance> attList = new ArrayList<Attendance>();
		try {
			JSONArray jsonArray = new JSONArray(response.toString());
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject j = jsonArray.getJSONObject(i);
				Attendance att = new Attendance(j.getInt("courseid"), j.getString("classid"), j.getString("date"), j.getString("studentid"), j.getString("studentname"), j.getInt("ispresent"));;
				attList.add(att);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return attList;
	}
	public static List<Source> handleSourceResponse(Context context, String response) {
		// TODO Auto-generated method stub
		List<Source> souList = new ArrayList<Source>();
		try {
			JSONArray jsonArray = new JSONArray(response.toString());
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject j = jsonArray.getJSONObject(i);
				Source sou = new Source(j.getInt("id"), j.getInt("courseid"), j.getString("teachername"), j.getString("filename"), j.getString("date"));;
				souList.add(sou);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return souList;
	}
	public static List<Message> handleMessagesResponse(
			Context context, String response) {
		// TODO Auto-generated method stub
		List<Message> mesList = new ArrayList<Message>();
		try {
			JSONArray jsonArray = new JSONArray(response.toString());
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject j = jsonArray.getJSONObject(i);
				Message mes = new Message(j.getString("content"), j.getString("date"));;
				mesList.add(mes);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mesList;
	}
	public static List<Attendance> handleStudentResponse(
			Context context, String response) {
		// TODO Auto-generated method stub
		List<Attendance> attList = new ArrayList<Attendance>();
		try {
			JSONArray jsonArray = new JSONArray(response.toString());
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject j = jsonArray.getJSONObject(i);
				Attendance att = new Attendance(j.getString("studentid"), j.getString("studentname"));;
				attList.add(att);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return attList;
	}
	
	/*public static Subject handleReplySubjectResponse(Context context, String response) {
		// TODO Auto-generated method stub
		Subject sub = null;
		try { 
			JSONObject j = new JSONObject(response.toString());
			 sub = new Subject(Integer.parseInt(j.getString("id")), j.getString("studentid"), j.getString("content"), j.getString("date"), Integer.parseInt(j.getString("reply")), j.getString("studentname"));
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sub;
	}*/
}


