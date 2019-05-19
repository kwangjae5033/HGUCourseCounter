package edu.handong.analysis.datamodel;

import java.util.ArrayList;
import java.util.HashMap;

public class Student {
	private String studentId;
	private ArrayList<Course> courseTaken;
	private HashMap<String, Integer> semestersByYearAndSemester; 
	
	public Student(String studentId) {
		
	}
	
	public void addCourse(Course newRecord) {
	//put course instance that read the line to 
	//ArrayList<Course> courseTaken
	}
	
	public HashMap<String, Integer> getSemestersByYearAndSemester(){
	//by using year and semester info 
	//to count up the number of semester taken by the student accordingly
	//instantiate HashMap
	}
	
	public int getNumCourseInNthSemestersByYearAndSemesters() {
	//return how many courses the student have taken 
	//according to the "순차적 학기 번호"
	//what to instantiate 
		
		return 0; //change it later 
	}
	
	
}
