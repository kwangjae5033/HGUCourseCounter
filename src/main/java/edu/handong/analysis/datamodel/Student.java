package edu.handong.analysis.datamodel;

import java.util.ArrayList;
import java.util.HashMap;

public class Student {
	private String studentId;
	private ArrayList<Course> courseTaken;
	private HashMap<String, Integer> semestersByYearAndSemester; 
	
	//constructor
	public Student(String studentId) {
		this.studentId = studentId;
		this.courseTaken = new ArrayList<Course>();
		this.semestersByYearAndSemester = new HashMap<String, Integer>();
	}
	
	
	public void addCourse(Course newRecord) {
		courseTaken.add(newRecord); 
	}
	
	
	public HashMap<String, Integer> getSemestersByYearAndSemester(){
	//by using year and semester info 
	//to count up the number of semester taken by the student accordingly 
		String yearTaken1 = "";
		String yearTaken2 = "";
		String semesterTaken1 = "";
		String semesterTaken2 = "";
		int semester = 1;
		
		this.semestersByYearAndSemester.put(Integer.toString(this.courseTaken.get(0).getYearTaken())+"-"
										   +Integer.toString(this.courseTaken.get(0).getSemesterCourseTaken()), semester);
		
		for(int i=0; i<this.courseTaken.size()-1; i++) {
			
			yearTaken1 = Integer.toString(this.courseTaken.get(i).getYearTaken());
			semesterTaken1 = Integer.toString(this.courseTaken.get(i).getSemesterCourseTaken());
			yearTaken2 = Integer.toString(this.courseTaken.get(i+1).getYearTaken());
			semesterTaken2 = Integer.toString(this.courseTaken.get(i+1).getSemesterCourseTaken());
			
			if(!yearTaken1.equals(yearTaken2) || !semesterTaken1.equals(semesterTaken2)) {
				semester++;
				this.semestersByYearAndSemester.put(yearTaken2+"-"+semesterTaken2, semester);
			}
		}
		
		return this.semestersByYearAndSemester;
	}
	
	
	public int getNumCourseInNthSemestersByYearAndSemesters(int semester) {
		
		int countNumOfCourse = 1;
		int semesterCounter = 1;
		for(int i = 0; i < this.courseTaken.size()-1; i++) {
			
			if(semesterCounter <= semester) {
				
				int yearTakenFirstIdx = this.courseTaken.get(i).getYearTaken();
				int yearTakenSecondIdx = this.courseTaken.get(i+1).getYearTaken();
				int semesterTakenFirstIdx = this.courseTaken.get(i).getSemesterCourseTaken();
				int semesterTakenSecondIdx = this.courseTaken.get(i+1).getSemesterCourseTaken();
				
				if((yearTakenFirstIdx == yearTakenSecondIdx) && (semesterTakenFirstIdx == semesterTakenSecondIdx))
					countNumOfCourse++;
			
				else {
					// as semester changed 
					semesterCounter++; 
					if(semesterCounter <= semester) countNumOfCourse = 1;
				}
		
			}else break; 
		}// end of for loop 
		return countNumOfCourse;
	}// end of getNumCourseInNthSemestersByYearAndSemesters function	
	
	
	//getters and setters
	public String getStudentId() {
		return this.studentId; 
	}
	
	
	public ArrayList<Course> getCourseTaken(){
		return this.courseTaken; 
	}
	
	
}
