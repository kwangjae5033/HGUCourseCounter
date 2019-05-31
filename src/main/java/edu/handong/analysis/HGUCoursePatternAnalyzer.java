package edu.handong.analysis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import edu.handong.analysis.datamodel.Course;
import edu.handong.analysis.datamodel.Student;
import edu.handong.analysis.utils.NotEnoughArgumentException;
import edu.handong.analysis.utils.Utils;

public class HGUCoursePatternAnalyzer {

	private HashMap<String,Student> students;
	public String input;
	public String output; 
	public String coursecode; 
	public String startyear;
	public String endyear;
	public String analysis; 
	boolean help;
	
	/**
	 * This method runs our analysis logic to save the number courses taken by each student per semester in a result file.
	 * Run method must not be changed!!
	 * @param args
	 * @throws IOException 
	 */
	public void run(String[] args) {
		
		Options options = createOptions();
	      
	    if(parseOptions(options, args)){
	         if (help){
	            printHelp(options);
	            return;
	         }
	         
	         String dataPath = input; 
	         String resultPath = output; 
	         
	         if (Integer.parseInt(analysis) == 1) {
	        	 //"a -1"
	        	 ArrayList<String> lines = Utils.getLines(dataPath, true);				 
	        	 
	        	 students = loadStudentCourseRecords(lines);

	        	 // To sort HashMap entries by key values so that we can save the results by student ids in ascending order.
	     		 Map<String, Student> sortedStudents = new TreeMap<String,Student>(students); 
	     		
	     		 // Generate result lines to be saved.
	     		 ArrayList<String> linesToBeSaved = countNumberOfCoursesTakenInEachSemester(sortedStudents);
	     		
	     		 // Write a file (named like the value of resultPath) with linesTobeSaved.
	     		 Utils.writeAFile(linesToBeSaved, resultPath);
	         
	         }else {
	        	 //"-a 2"
	        	 if(coursecode.isEmpty()) { 
	        		 System.out.println("You have to type option -c with -a 2"); 
	        	 	 System.exit(0);
	        	 }
	        	 
	        	 ArrayList<String> lines = Utils.getLines(dataPath, true);
	        	 
	             HashMap<String,ArrayList<Course>> students = new HashMap<String,ArrayList<Course>>(); 
	        	 
	             for(String aline : lines) {
	        		
	        		 if(aline.contains(coursecode)) {
	 
	        			 
						 Course newCourse = new Course(aline);
	        			 if(students.get(Integer.toString(newCourse.getYearTaken())+Integer.toString(newCourse.getSemesterCourseTaken()))==null) {
	        				 ArrayList<Course> studentInSameYearNSemester = new ArrayList<Course>(); 
	        				 studentInSameYearNSemester.add(newCourse); 
	        				 students.put(Integer.toString(newCourse.getYearTaken())+Integer.toString(newCourse.getSemesterCourseTaken()),studentInSameYearNSemester);
	        			 }
	        			 else {
	        				 students.get(Integer.toString(newCourse.getYearTaken())+Integer.toString(newCourse.getSemesterCourseTaken())).add(newCourse); 
	        			 }
	        			
	        		 }//stored students that took the course at same year and same semester
	        		 
	        	 }//end of for-each loop 
	        	 
	             //year range filter//
	        	 
	         }//end of else 
	         
	         ////.....
	         
	    }//end of if(parseOptions...
	
	}//end of run function 
	

	/**
	 * This method create HashMap<String,Student> from the data csv file. Key is a student id and the corresponding object is an instance of Student.
	 * The Student instance have all the Course instances taken by the student.
	 * @param lines
	 * @return
	 */
	private HashMap <String,Student> loadStudentCourseRecords(ArrayList<String> lines) {
		// TODO: Implement this method
		HashMap<String,Student> courseRecords = new HashMap<String, Student>(); 
		
		//instantiate Student and put it into HashMap 
		int numberOfStudent = Integer.parseInt(lines.get(lines.size()-1).split(",")[0]);
		
		for(int i=1; i <= numberOfStudent; i++) {
			Student newStudent = new Student(String.format("%04d", i)); 
			courseRecords.put(newStudent.getStudentId(), newStudent);
		}
		
		//call key value and add courseTaken 
		for(String aline:lines) {
			Course newCourse = new Course(aline);
			courseRecords.get(newCourse.getStudentId()).addCourse(newCourse);; 
		}
	
		return courseRecords; 
	}

	/**
	 * This method generate the number of courses taken by a student in each semester. The result file look like this:
	 * StudentID, TotalNumberOfSemestersRegistered, Semester, NumCoursesTakenInTheSemester
	 * 0001,14,1,9
     * 0001,14,2,8
	 * ....
	 * 
	 * 0001,14,1,9 => this means, 0001 student registered 14 semesters in total. In the first semester (1), the student took 9 courses.
	 * 
	 * 
	 * @param sortedStudents
	 * @return
	 */
	private ArrayList<String> countNumberOfCoursesTakenInEachSemester(Map<String, Student> sortedStudents) {
		
		// TODO: Implement this method
		ArrayList<String> linesToBeSaved = new ArrayList<String>();
		int numOfStudents = sortedStudents.size();
		for(int i=1; i <= numOfStudents; i++) {
			int numOfSemesters = sortedStudents.get(String.format("%04d", i)).getSemestersByYearAndSemester().get(Integer.toString(sortedStudents.get(String.format("%04d", i)).getCourseTaken().get(sortedStudents.get(String.format("%04d", i)).getCourseTaken().size()-1).getYearTaken())+"-"+Integer.toString(sortedStudents.get(String.format("%04d", i)).getCourseTaken().get(sortedStudents.get(String.format("%04d", i)).getCourseTaken().size()-1).getSemesterCourseTaken()));
			//print and check if the result gives correct semester 		
			for(int j=1; j <= numOfSemesters; j++) { 
				linesToBeSaved.add(String.format("%04d", i) + "," + Integer.toString(numOfSemesters) + "," + Integer.toString(j) + "," + Integer.toString(sortedStudents.get(String.format("%04d", i)).getNumCourseInNthSemestersByYearAndSemesters(j)));
			}
		}

		return linesToBeSaved;
		
	}//end of countNumberOfCoursesTakenInEachSemester
	
	 
	private boolean parseOptions(Options options, String[] args) {
		
		CommandLineParser parser = new DefaultParser();
		
		try {
			
			CommandLine cmd = parser.parse(options, args);
		
		    input = cmd.getOptionValue("i");
	        output = cmd.getOptionValue("o");
	        coursecode = cmd.getOptionValue("c");
	        startyear = cmd.getOptionValue("s");
	        endyear = cmd.getOptionValue("e");
	        analysis = cmd.getOptionValue("a");
	        help = cmd.hasOption("h");

	      } catch (Exception e) {
	        printHelp(options);
	        return false;
	      }

	      return true;

	}
	
	
	// Definition Stage
	private Options createOptions() {
		
		Options options = new Options();
		
		// add options by using OptionBuilder
	    options.addOption(Option.builder("i").longOpt("input")
	           .desc("Set an input file path")
	           .hasArg()
	           .argName("Path name to display")
	           .required()
	           .build());
	    
	    options.addOption(Option.builder("o").longOpt("output")
		           .desc("Set an output file path")
		           .hasArg()
		           .argName("Path name to display")
		           .required()
		           .build());
		    
	    //two separate options? Or in one?
	    options.addOption(Option.builder("a").longOpt("analysis")
	           .desc("")
	           .hasArg()     // this option is intended not to have an option value but just an option
	           .argName("")
	           .required()   
	           .build());

	    options.addOption(Option.builder("c").longOpt("coursecode")
	           .desc("Course code for '-a 2' option")
               .hasArg()
               .argName("") 
	    	   .build());
	      
	    options.addOption(Option.builder("s").longOpt("startyear")
	           .desc("Set the start year for analysis")
	           .hasArg()
	           .argName("")
	           .build());

	    options.addOption(Option.builder("e").longOpt("endyear")
	           .desc("Set the end year for analysis")
	           .hasArg()
	           .argName("")
	           .build());

	    return options;
	
	}//end createOptions function
	
	 
	private void printHelp(Options options) {
		// automatically generate the help statement
	    HelpFormatter formatter = new HelpFormatter();
	    String header = "HGU Course Analyzer";
	    String footer ="";
	    formatter.printHelp("CLIExample", header, options, footer, true);
	
	}

}//end of class HGUCourseCounter 

