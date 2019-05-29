package edu.handong.analysis;

import java.io.File;
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
	String path;
	boolean verbose;
    boolean help;
	boolean fullpath;
	
	/**
	 * This method runs our analysis logic to save the number courses taken by each student per semester in a result file.
	 * Run method must not be changed!!
	 * @param args
	 */
	public void run(String[] args) {
		
		Options options = createOptions();
	      
	    if(parseOptions(options, args)){
	         if (help){
	            printHelp(options);
	            return;
	         }
	         // path is required (necessary) data so no need to have a branch.
	         System.out.println("You provided \"" + path + "\" as the value of the option p");
	         
	         // TODO show the number of files in the path
	        
	         File file = new File(path);
	         System.out.println(file.listFiles().length);     
	         
	         if(verbose) {
	            // TODO list all files in the path
	            File folder = new File(path);
	            listFilesForFolder(folder);
	            System.out.println("Your program is terminated. (This message is shown because you turned on -v option!");

	         }

	         if(fullpath) {
	            File file2 = new File(path);
	            System.out.println("Fullpath: " + file2.getAbsolutePath());
	         }
	         
	      }
	    // HW5 skeleton from here 
		try {
			// when there are not enough arguments from CLI, it throws the NotEnoughArgmentException which must be defined by you.
			if(args.length<2)
				throw new NotEnoughArgumentException();
		} catch (NotEnoughArgumentException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
		
		String dataPath = args[0]; // csv file to be analyzed
		String resultPath = args[1]; // the file path where the results are saved.
	
		ArrayList<String> lines = Utils.getLines(dataPath, true);
		
		students = loadStudentCourseRecords(lines);

		// To sort HashMap entries by key values so that we can save the results by student ids in ascending order.
		Map<String, Student> sortedStudents = new TreeMap<String,Student>(students); 
		
		// Generate result lines to be saved.
		ArrayList<String> linesToBeSaved = countNumberOfCoursesTakenInEachSemester(sortedStudents);
		
		// Write a file (named like the value of resultPath) with linesTobeSaved.
		Utils.writeAFile(linesToBeSaved, resultPath);
	}
	

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

	        path = cmd.getOptionValue("p");
	        verbose = cmd.hasOption("v");
	        help = cmd.hasOption("h");
	        fullpath = cmd.hasOption("f");

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
	    options.addOption(Option.builder("p").longOpt("path")
	           .desc("Set a path of a directory or a file to display")
	           .hasArg()
	           .argName("Path name to display")
	           .required()
	           .build());
	    
	    // add options by using OptionBuilder
	    options.addOption(Option.builder("v").longOpt("verbose")
	           .desc("Display detailed messages!")
	           //.hasArg()     // this option is intended not to have an option value but just an option
	           .argName("verbose option")
	           //.required() // this is an optional option. So disabled required().
	           .build());
	      
	    // add options by using OptionBuilder
	    options.addOption(Option.builder("h").longOpt("help")
	           .desc("Help")
               .build());
	      
	    // add options by using OptionBuilder
	    options.addOption(Option.builder("f").longOpt("fullpath")
	           .desc("Set a fullpath of a directory or a file to display")
	           .build());

	      return options;
	
	}//end createOptions function
	
	 
	private void printHelp(Options options) {
		// automatically generate the help statement
	    HelpFormatter formatter = new HelpFormatter();
	    String header = "CLI test program";
	    String footer ="\nPlease report issues at https://github.com/lifove/CLIExample/issues";
	    formatter.printHelp("CLIExample", header, options, footer, true);
	
	}

}//end of class HGUCourseCounter 

