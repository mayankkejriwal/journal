package goldStandard;

import java.io.*;
import java.util.Scanner;

public class GenerateSchemaFile {

	/*
	 * This file takes a 'property file' or RDB file as input and reads in the first line,
	 * does a comma separation and publishes a tab-delimited 2-column schema file with format
	 * 0	field_name1
	 * 1	field_name2
	 * ...
	 * 
	 * CAVEAT: The file will not make checks/assertions. If the input is non-conforming, results
	 * will be undefined
	 */
	
	static String infile="/host/heteroDatasets/journal/used_finally/Restaurants/archive/restaurant1.csv";
	static String outfile="/host/heteroDatasets/journal/used_finally/Restaurants/archive/schema1.txt";
	
	public static void main(String[] args){
		Scanner in=null;
		String[] fields=null;
		int count=0;
		PrintWriter out=null;
		try{
			in=new Scanner(new FileReader(infile));
			if(in.hasNextLine())
				fields=in.nextLine().split(",");
			out=new PrintWriter(new File(outfile));
			for(String f: fields){
				out.println(count+"\t"+f);
				count++;
			}
		}catch(IOException E){
			System.out.println("Error!");
		}finally{
			in.close();
			out.close();
		}
		
		
	}
}
