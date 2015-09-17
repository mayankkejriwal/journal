package propertyAligner;

import general.CSVParser;
import general.Parameters;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

import baseline.Dumas;
import tsg.JaccardFromTF;

public class ColumnMatcher {

	String file1;
	String file2;
	ArrayList<HashSet<String>> set1;
	ArrayList<HashSet<String>> set2;
	
	double[][] matrix;
	
	public static void main(String[] args)throws IOException{
		String prefix="/host/heteroDatasets/journal/used_finally/Restaurants/";
		
		double[] res=new double[3];
		
		for(double i=0.0; i<=1.0; i+=0.1){
			ColumnMatcher p=new ColumnMatcher(prefix);
			p.execute(prefix+"schema_cm",i);
			double[] res1=null;
			res1= goldStandard.EvaluateSchema.printPrecRec(prefix+"schema_cm",prefix+"goldStandard_schema");
			if(res1[2]>res[2])
				res=res1;
		}
		System.out.println("* \n * \n * \n And the highest is...");
		System.out.println("Precision \t Recall \t FM");
		System.out.println(res[0]+"\t "+res[1]+"\t"+res[2]);
		
	}
	
	
	
	public ColumnMatcher(String prefix)throws IOException{
		file1=prefix+"file1.csv";
		file2=prefix+"file2.csv";
		Scanner in=new Scanner(new FileReader(file1));
		if(in.hasNextLine()){
			int count=(new CSVParser()).parseLine(in.nextLine()).length;
			set1=new ArrayList<HashSet<String>>();
			for(int i=0; i<count; i++)
				set1.add(new HashSet<String>());
		}
		in.close();
		in=new Scanner(new FileReader(file2));
		if(in.hasNextLine()){
			int count=(new CSVParser()).parseLine(in.nextLine()).length;
			set2=new ArrayList<HashSet<String>>();
			for(int i=0; i<count; i++)
				set2.add(new HashSet<String>());
		}
		in.close();
		matrix=new double[set1.size()][set2.size()];
		populateMatrixFromFiles();
	}
	
	private void populateMatrixFromFiles()throws IOException{
		Scanner in=new Scanner(new FileReader(file1));
		while(in.hasNextLine()){
			
			String[] d=(new CSVParser()).parseLine(in.nextLine());
			for(int i=0; i<d.length; i++){
				String[] d1=d[i].split(Parameters.splitstring);
				
				for(String e:d1)
					if(e.trim().length()!=0)
						set1.get(i).add(e.trim().toLowerCase());
			}
			
		}
		in.close();
		in=new Scanner(new FileReader(file2));
		while(in.hasNextLine()){
			
			String[] d=(new CSVParser()).parseLine(in.nextLine());
			for(int i=0; i<d.length; i++){
				String[] d1=d[i].split(Parameters.splitstring);
				
				for(String e:d1)
					if(e.trim().length()!=0)
						set2.get(i).add(e.trim().toLowerCase());
			}
			
		}
		in.close();
		for(int i=0; i<matrix.length; i++)
			for(int j=0; j<matrix[i].length; j++)
				matrix[i][j]=JaccardFromTF.computeJaccardIndex(set1.get(i), set2.get(j));
				
			
	}
	
	public void execute(String outfile, double thresh) throws FileNotFoundException{
		
				
		HashSet<String> results=new HashSet<String>();
		
		for(int i=0; i<matrix.length; i++)
			for(int j=0; j<matrix[i].length; j++)
				if(matrix[i][j]>=thresh)
					results.add(i+" "+j);
		PrintWriter out=new PrintWriter(new File(outfile));
		for(String a: results)
			out.println(a);
		out.close();
		
	}
	
}
