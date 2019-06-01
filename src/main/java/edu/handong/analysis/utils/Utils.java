package edu.handong.analysis.utils;

import edu.handong.analysis.*;

import java.util.ArrayList;
import java.io.BufferedReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class Utils {

	public static ArrayList<String> getLines(String dataPath, boolean b) {

		ArrayList<String> lines = new ArrayList<String>();

		try {

			BufferedReader reader = Files.newBufferedReader(Paths.get(dataPath));
			CSVParser csvParser = new CSVParser(reader,
					CSVFormat.DEFAULT.withHeader("StudentID", "YearMonthGraduated", "FirstMajor", "SecondMajor",
							"CourseCode", "CourseName", "CourseCredit", "YearTaken", "SemesterTaken"));

			if (b == true)
				b = false;

			for (CSVRecord csvRecord : csvParser) {

				if (b) {
					String StudentID = csvRecord.get("StudentID");
					String YearMonthGraduated = csvRecord.get("YearMonthGraduated");
					String FirstMajor = csvRecord.get("FirstMajor");
					String SecondMajor = csvRecord.get("SecondMajor");
					String CourseCode = csvRecord.get("CourseCode");
					String CourseName = csvRecord.get("CourseName");
					String CourseCredit = csvRecord.get("CourseCredit");
					String YearTaken = csvRecord.get("YearTaken");
					String SemesterTaken = csvRecord.get("SemesterTaken");

					String aline = StudentID + ", " + YearMonthGraduated + ", " + FirstMajor + ", " + SecondMajor + ", "
							+ CourseCode + ", " + CourseName + ", " + CourseCredit + ", " + YearTaken + ", "
							+ SemesterTaken;
					lines.add(aline);
				} else
					b = true;
			}

			csvParser.close();
		} // end of try

		catch (IOException e) {
			System.out.println("Cannot find file " + dataPath);
		} // end of catch

		return lines;
	}// end of getlines function

	public static void writeAFile(ArrayList<String> linesToBeSaved, String resultPath) {
		// TODO Auto-generated method stub
		try {
			File f = new File(resultPath);
			File dir = new File(f.getParentFile().getAbsolutePath());
			dir.mkdirs();
			FileWriter wrt = new FileWriter(f, true);

			wrt.write("StudentID, TotalNumberOfSemestersRegistered, Semester, NumCoursesTakenInTheSemester\n");

			for (String line : linesToBeSaved) {
				wrt.write(line + "\n");
				wrt.flush();
			}
			wrt.close();

		} catch (FileNotFoundException e) {

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}



	public static void writeAFileB(ArrayList<String> linesToBeSaved, String resultPath) {
		// TODO Auto-generated method stub
		try {
			File f = new File(resultPath);
			File dir = new File(f.getParentFile().getAbsolutePath());
			dir.mkdirs();
			FileWriter wrt = new FileWriter(f, true);

			wrt.write("Year,Semester,CourseCode,CourseName,TotalStudents,StudentsTaken,Rate\n");

			for (String line : linesToBeSaved) {
				wrt.write(line + "\n");
				wrt.flush();
			}
			wrt.close();

		} catch (FileNotFoundException e) {

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}

