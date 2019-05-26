package edu.handong.analysis.utils;

import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Scanner; 

public class Utils {

	public static ArrayList<String> getLines(String dataPath, boolean b) {
		
		String line = "";
		ArrayList<String> lines = new ArrayList<String>(); 
		
		try {
			Scanner inputStream = new Scanner(new File(dataPath));
			//if second argument is true, skip the headline
			if (b == true)
				line = inputStream.nextLine(); 
			
			while (inputStream.hasNextLine()) {
				line = inputStream.nextLine(); 
				lines.add(line); 
			}
			inputStream.close(); 
		}//end of try
		catch(FileNotFoundException e) {
			System.out.println("Cannot find file " + dataPath); 
		}//end of catch 
		
		return lines;
		
	}//end of getlines function

	
	public static void writeAFile(ArrayList<String> linesToBeSaved, String resultPath) {
		// TODO Auto-generated method stub
		try {
			File file = new File(resultPath);
			File dir = new File(file.getParentFile().getAbsolutePath());
			dir.mkdirs();
			FileWriter writer = new FileWriter(file, true);
			
			writer.write("StudentID, TotalNumberOfSemestersRegistered, Semester, NumCoursesTakenInTheSemester\n");
			
			for(String line: linesToBeSaved) {
				writer.write(line + "\n");
				writer.flush();
			}
			writer.close();
			
		} catch (FileNotFoundException e) {
			
		} catch (Exception e)	 {
			System.out.println(e.getMessage()); 
		}
	}

}
