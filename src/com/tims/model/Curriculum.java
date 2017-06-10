package com.tims.model;

import java.io.Serializable;

public class Curriculum implements Serializable{  
		  
	    /** 
	     *  课表信息实体
	     */  
	    private static final long serialVersionUID = 2074656067805712769L;  

	    /** 课程名称  */  
	    private String cur_coursename;
	    /** 教师姓名  */  
	    private String cur_teachername;
	    /** 开始周次 */  
	    private int cur_startweek;  
	    /** 结束周次 */  
	    private int cur_endweek;  
	    /** 单双周 **/  
	    private int cur_oddoreven;
	    /** 周一-周日 */  
	    private int cur_weeknum;  
	    /** 开始节次 */  
	    private int cur_startsection;  
	    /** 节数 */  
	    private int cur_sectionnums;
	    /** 上课教室 */  
	    private String cur_classroom; 
	      
	    
	    public Curriculum(String coursename, String teachername, int startweek, int endweek, 
	    		int oddoreven, int weeknum, int startsection, int sectionnums, String classroom){
	    	cur_coursename = coursename;
	    	cur_teachername = teachername;
	    	cur_startweek = startweek;
	    	cur_endweek = endweek;
	    	cur_oddoreven = oddoreven;
	    	cur_weeknum = weeknum;
	    	cur_startsection = startsection;
	    	cur_sectionnums = sectionnums;
	    	cur_classroom = classroom;
	    }
	    public String getCourseName() {  
	        return cur_coursename;  
	    }
	    public String getTeacherName() {  
	        return cur_teachername;  
	    }  
	    public int getStartWeek() {  
	        return cur_startweek;  
	    }  
	    public int getEndWeek() {  
	        return cur_endweek;  
	    } 
	    public int getOddOrEven() {  
	        return cur_oddoreven;  
	    } 
	    public int getWeekNum() {  
	        return cur_weeknum;  
	    } 
	    public int getStartSection() {  
	        return cur_startsection;  
	    }  
	    public int getSectionNums() {  
	        return cur_sectionnums;  
	    }

	    public String getClassRoom() {  
	        return cur_classroom;  
	    }
}
