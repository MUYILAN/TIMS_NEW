package com.tims.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tims.model.Curriculum;
import com.tims.util.ContextUtil;
import com.tims.util.HttpCallbackListener;
import com.tims.util.HttpUtil;
import com.tims.util.LogUtil;
import com.tims.util.Utility;

public class MainFragment02 extends Fragment {

	/** 选择周次的下拉列表	 */
	private Spinner spinner;
	/**	当前周次 */
	private int currentWeek = 1;
	/** 第一个无内容的格子 */
	protected TextView empty;
	/** 星期一的格子 */
	protected TextView monColum;
	/** 星期二的格子 */
	protected TextView tueColum;
	/** 星期三的格子 */
	protected TextView wedColum;
	/** 星期四的格子 */
	protected TextView thrusColum;
	/** 星期五的格子 */
	protected TextView friColum;
	/** 星期六的格子 */
	protected TextView satColum;
	/** 星期日的格子 */
	protected TextView sunColum;
	/** 课程表body部分布局 */
	protected RelativeLayout course_table_layout;
	/** 屏幕宽度 **/
	protected int screenWidth;
	/** 课程格子平均宽度 **/
	protected int aveWidth;
	/** 课程格子平均高度 **/
	protected int aveHeight;
	int[] background = {R.drawable.course_info_light_grey,R.drawable.course_info_blue};
			
	
	private Map<String, List<Curriculum>> curriculumMap;
	private Map<TextView, List<Curriculum>> curriculumTextViewMap = new HashMap<TextView, List<Curriculum>>();
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_main02,container, false);
		spinner = (Spinner) view.findViewById(R.id.spinner_mainfrag02);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			//监听事件
						 @Override
						public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
						 // TODO Auto-generated method stub
							 currentWeek = arg2+1;
							 modifyWeek();	
			 }
						
						 @Override
						 public void onNothingSelected(AdapterView<?> arg0) {
						 // TODO Auto-generated method stub
										
						 }
					});

				empty = (TextView) view.findViewById(R.id.test_empty);
				monColum = (TextView) view.findViewById(R.id.test_monday_course);
				tueColum = (TextView) view.findViewById(R.id.test_tuesday_course);
				wedColum = (TextView) view.findViewById(R.id.test_wednesday_course);
				thrusColum = (TextView) view.findViewById(R.id.test_thursday_course);
				friColum = (TextView) view.findViewById(R.id.test_friday_course);
				satColum  = (TextView) view.findViewById(R.id.test_saturday_course);
				sunColum = (TextView) view.findViewById(R.id.test_sunday_course);
				course_table_layout = (RelativeLayout) view.findViewById(R.id.test_course_rl);
				DisplayMetrics dm = new DisplayMetrics();  
				getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
				//屏幕宽度
				int width = dm.widthPixels;
				int height = dm.heightPixels;
				//平均宽度
				int aveWidth = width / 8;
				int aveHeight = height / 12;
				//第一个空白格子设置为25宽
				empty.setWidth(aveWidth);
				monColum.setWidth(aveWidth);
				tueColum.setWidth(aveWidth);
				wedColum.setWidth(aveWidth);
				thrusColum.setWidth(aveWidth);
				friColum.setWidth(aveWidth);
				satColum.setWidth(aveWidth);
				sunColum.setWidth(aveWidth);
				this.screenWidth = width;
				this.aveWidth = aveWidth;
				this.aveHeight = aveHeight;
				
				initCurriculum();
				queryCurriculumInfo();
				return view;
			}
	protected void modifyWeek() {
		// TODO Auto-generated method stub
		for(Map.Entry<TextView, List<Curriculum>> entry: curriculumTextViewMap.entrySet())  
        {
			TextView tv = entry.getKey();
			List<Curriculum> list = new ArrayList<Curriculum>(entry.getValue());
            Curriculum upperCourse = null;  
            Iterator<Curriculum> iter = list.iterator();
            while(iter.hasNext())  
            {  
                Curriculum c = iter.next();
                if((((c.getStartWeek()) <= currentWeek && (c.getEndWeek()) >= currentWeek)))  
                {  
                	if(c.getOddOrEven() == 2 ||  
                      (c.getOddOrEven() == 0 && currentWeek % 2 == 0) ||  
                      (c.getOddOrEven() == 1 && currentWeek % 2 != 0) )  
                    {    
                        iter.remove();  
                        upperCourse = c;  
                        String oddOrEven = "";
            			switch(upperCourse.getOddOrEven()){
            			case 0:
            				oddOrEven = "[双周]";
            				break;
            			case 1:
            				oddOrEven = "[单周]";
            				break;
            			default:
            			}
            			tv.setText(upperCourse.getCourseName()+"\n@"+upperCourse.getTeacherName()+"\n"
            			+upperCourse.getStartWeek()+"-"+upperCourse.getEndWeek()+"周"+oddOrEven+"\n@"+upperCourse.getClassRoom());
            			tv.setTextColor(Color.WHITE);
            			tv.setBackgroundResource(background[1]);
            			tv.getBackground().setAlpha(222);
                        break;  
                    }
                }
            }  
            
            if(upperCourse == null){
            	upperCourse = list.get(0);
            	 String oddOrEven = "";
     			switch(upperCourse.getOddOrEven()){
     			case 0:
     				oddOrEven = "[双周]";
     				break;
     			case 1:
     				oddOrEven = "[单周]";
     				break;
     			default:
     			}
     			tv.setText(upperCourse.getCourseName()+"\n@"+upperCourse.getTeacherName()+"\n"
     			+upperCourse.getStartWeek()+"-"+upperCourse.getEndWeek()+"周"+oddOrEven+"\n@"+upperCourse.getClassRoom());
     			tv.setBackgroundResource(background[0]);
     			tv.setTextColor(R.color.deep_gray);
     			tv.getBackground().setAlpha(222);
            }
             
        }
	}
	private void initCurriculum() {
		// TODO Auto-generated method stub
		for(int i = 1; i <= 12; i ++){
			
			for(int j = 1; j <= 8; j ++){
				
				TextView tx = new TextView(getActivity());
				tx.setId((i - 1) * 8  + j);
				if(j < 8)
					tx.setBackgroundResource(R.drawable.course_text_view_bg);
				else
					tx.setBackgroundResource(R.drawable.course_table_last_colum);
				RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(aveWidth,aveHeight);
				tx.setGravity(Gravity.CENTER);
				tx.setTextAppearance(R.style.courseTableText);
				if(j == 1)
				{
					tx.setText(String.valueOf(i));
//					tx.setBackgroundColor(R.drawable.light_gray);
					if(i == 1)
						rp.addRule(RelativeLayout.BELOW, empty.getId());
					else
						rp.addRule(RelativeLayout.BELOW, (i - 1) * 8);
				}
				else
				{
					rp.addRule(RelativeLayout.RIGHT_OF, (i - 1) * 8  + j - 1);
					rp.addRule(RelativeLayout.ALIGN_TOP, (i - 1) * 8  + j - 1);
					tx.setText("");
				}
					
				tx.setLayoutParams(rp);
				course_table_layout.addView(tx);
			}
		}

	}
	private void queryCurriculumInfo() {
		// TODO Auto-generated method stub
		String url = HttpUtil.BASE_URL + "CurriculumsServlet";
		
		SharedPreferences pref=ContextUtil.getContext().getSharedPreferences("user",0);
		String type = pref.getString("type", "");
		String typeId;
		if(type.equals("teacher")){
			typeId = pref.getString("acc", "");
		}else{
			typeId = pref.getString("classid", "");
		} 
		String year = "2016-2017-spring";
		
		String message = "type="+type+"&typeid="+typeId+"&year="+year;
		HttpUtil.sendHttpRequest(url, message, new HttpCallbackListener() {
			@Override
			public void onFinish(final String response) {
				LogUtil.d("课表的返回信息", response);	
				if (response.equals("FALSE")) {
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(ContextUtil.getContext(),"获取课表失败!", Toast.LENGTH_SHORT).show();
						}
					});
				}else if(response.equals("NULL")){
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							//lvReply.setVisibility(View.GONE);
						}
					});
					
				}else{
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							curriculumMap = Utility.handleCurriculumResponse(getActivity(), response);
							showCurriculumInfo();
						}
					});
				}
			}
			@Override
			public void onError(Exception e) {

				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(ContextUtil.getContext(),"连接服务器失败!", Toast.LENGTH_SHORT).show();
					}
				});
				
			}
		});

	}
	private void showCurriculumInfo() {
		// TODO Auto-generated method stub
		for(Map.Entry<String, List<Curriculum>> entry: curriculumMap.entrySet())  
        {
            Curriculum upperCourse = null;  
             List<Curriculum> list = new ArrayList<Curriculum>(entry.getValue());
           /* Collections.sort(list, new Comparator<Curriculum>(){
                @Override  
                public int compare(Curriculum arg0, Curriculum arg1) {  
                	LogUtil.d("排序", "开始");
                    if(arg0.getStartSection() < arg1.getStartSection())  
                        return -1;  
                    else  
                        return 1;  
                }  
                  
            });*/    
            do {  
                Iterator<Curriculum> iter = list.iterator();  
              if(iter.hasNext()){
                upperCourse = iter.next();
                iter.remove(); 
              }
              
                if(upperCourse != null)  
                {  
                    List<Curriculum> curriculumList = new ArrayList<Curriculum>();  
                    curriculumList.add(upperCourse);  
                    iter = list.iterator();   
                    while(iter.hasNext())  
                    {  
                        Curriculum c = iter.next();   
                        if((c.getStartSection() <= upperCourse.getStartSection()
                        	&&upperCourse.getStartSection() < (c.getStartSection() + c.getSectionNums()))||
                            (c.getStartSection() < (upperCourse.getStartSection() + upperCourse.getSectionNums())
                            &&(upperCourse.getStartSection() + upperCourse.getSectionNums()) <= (c.getStartSection() + c.getSectionNums())))    
                        {  
                            curriculumList.add(c);  
                            iter.remove();  
                            //在判断哪个跨度大，跨度大的为顶层课程信息  
                            /*if(c.getSectionNums() > upperCourse.getSectionNums()
                                && ((c.getStartWeek() <= currentWeek && c.getEndWeek() >= currentWeek)))  
                            {  
                                upperCourse = c;  
                                index ++;  
                            }  */
                              
                        }  
                          
                    }  
                    TextView courseInfo = new TextView(getActivity());  
                    courseInfo.setId(100 + upperCourse.getWeekNum() * 10 + upperCourse.getStartSection());  
                    curriculumTextViewMap.put(courseInfo, curriculumList);
                    
        			RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(aveWidth-2,(aveHeight) * (upperCourse.getSectionNums())-2);
        			rlp.topMargin = 1;
        			rlp.leftMargin = 1;
        			rlp.addRule(RelativeLayout.BELOW, (upperCourse.getStartSection()-2) * 8 + upperCourse.getWeekNum()+1);
        			rlp.addRule(RelativeLayout.RIGHT_OF, (upperCourse.getStartSection()-1) * 8 + upperCourse.getWeekNum());
//        			courseInfo.setPadding(1, 1, 1, 1);
        			courseInfo.setGravity(Gravity.CENTER);
        			courseInfo.setTextSize(12);
        			courseInfo.setLayoutParams(rlp);
        			course_table_layout.addView(courseInfo);  
                    upperCourse = null;  
                }  
            } while(list.size() != 0);  
            
            modifyWeek();
            
        }
    }  
}
