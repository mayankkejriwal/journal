package goldStandard;

import java.io.*;
import java.util.*;

public class PrintLines {

	
	static String prefix="/host/heteroDatasets/journal/used_finally/im-similarity/";
	static String file1=prefix+"file1.csv";
	static String file2=prefix+"file2.csv";
	static String score=prefix+"sortedScores/svm";
	
	public static void main(String[] args)throws IOException{
		printLines(87);
	
	}
	
		
	public static void printLines(int n)throws IOException{
		Scanner in=new Scanner(new FileReader(file1));
		ArrayList<String> records1=new ArrayList<String>();
		while(in.hasNextLine())
			records1.add(in.nextLine());
		in.close();
		
		in=new Scanner(new FileReader(file2));
		ArrayList<String> records2=new ArrayList<String>();
		while(in.hasNextLine())
			records2.add(in.nextLine());
		in.close();
		
		in=new Scanner(new FileReader(score));
		int count=0;
		while(in.hasNextLine() && count<n){
			String l=in.nextLine();
			System.out.println(l);
			int i=Integer.parseInt(l.split("\t")[0].split(" ")[0]);
			int j=Integer.parseInt(l.split("\t")[0].split(" ")[1]);
			System.out.println(records1.get(i));
			System.out.println(records2.get(j));
			System.out.println();
			count++;
		}
		in.close();
	}
	
	public static void printLinesWithoutTab(String file, int n) throws FileNotFoundException{
		Scanner in=new Scanner(new FileReader(file));
		int count=0;
		while(in.hasNextLine() && count<n){
			System.out.println(in.nextLine().split("\t")[0]);
			count++;
		}
		
		in.close();
	}
	
	
}
