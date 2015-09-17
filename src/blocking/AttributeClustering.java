package blocking;

import general.CSVParser;
import general.Parameters;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

import tsg.JaccardFromTF;

public class AttributeClustering {

	String file1;
	String file2;
	ArrayList<HashSet<String>> set1;
	ArrayList<HashSet<String>> set2;
	
	double[][] matrix;
	
	double[][] transpose;
	
	public AttributeClustering(String prefix)throws IOException{
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
		transpose=new double[set2.size()][set1.size()];
		populateMatricesFromFiles();
	}
	
	private void populateMatricesFromFiles()throws IOException{
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
		convertSetsToTrigrams();
		for(int i=0; i<matrix.length; i++)
			for(int j=0; j<matrix[i].length; j++){
				matrix[i][j]=JaccardFromTF.computeJaccardIndex(set1.get(i), set2.get(j));
				transpose[j][i]=matrix[i][j];
			}
				
	}
	
	//form trigram reps. of strings in set1 and set2 and replace the sets with new sets
	private void convertSetsToTrigrams(){
		ArrayList<HashSet<String>> set1rep=new ArrayList<HashSet<String>>();
		for(int i=0; i<set1.size(); i++){
			HashSet<String> orig=set1.get(i);
			HashSet<String> q=new HashSet<String>();
			for(String a:orig)
				q=tsg.JaccardFromTF.union(q,convertStringToTrigram(a));
			
			set1rep.add(q);
		}
		
		set1=null;
		set1=set1rep;
		
		ArrayList<HashSet<String>> set2rep=new ArrayList<HashSet<String>>();
		for(int i=0; i<set2.size(); i++){
			HashSet<String> orig=set2.get(i);
			HashSet<String> q=new HashSet<String>();
			for(String a:orig)
				q=tsg.JaccardFromTF.union(q,convertStringToTrigram(a));
			
			set2rep.add(q);
		}
		
		set2=null;
		set2=set2rep;
		
	}
	
	private HashSet<String> convertStringToTrigram(String a){
		HashSet<String> res=new HashSet<String>();
		if(a.length()<=3)
		{
			res.add(a);
			return res;
		}
		else{
			for(int i=3; i<=a.length() ;i++)
				res.add(a.substring(i-3,i));
			return res;
		}
	}
	
	//find index j such that m[j] has maximum of all m[*]
	public static int findMaxInArray(double[] m){
		int res=-1;
		double max=-1;
		for(int i=0; i<m.length; i++)
			if(m[i]>=max){
				max=m[i];
				res=i;
			}
		
		
		return res;
	}
	
	public void execute(String outfile)throws IOException{
		HashSet<String> results=new HashSet<String>();
		
		for(int i=0; i<matrix.length; i++)
			if(matrix[i][findMaxInArray(matrix[i])]>0)
				results.add(i+" "+findMaxInArray(matrix[i]));
		for(int j=0; j<transpose.length; j++)
			if(transpose[j][findMaxInArray(transpose[j])]>0)
				results.add(findMaxInArray(transpose[j])+" "+j);
		PrintWriter out=new PrintWriter(new File(outfile));
		for(String a: results)
			out.println(a);
		out.close();
	}
	
	public static void main(String[] args)throws IOException{
		String prefix="/host/heteroDatasets/hyperparam-optim/d6/";
		//String prefix="/host/heteroDatasets/journal/used_finally/Restaurants/";
		AttributeClustering c=new AttributeClustering(prefix);
		c.execute(prefix+"ac");
		convertACToBK(prefix);
	}
	
	public static void convertACToBK(String prefix)throws IOException{
		Scanner in=new Scanner(new FileReader(prefix+"ac"));
		ArrayList<String> result=new ArrayList<String>();
		while(in.hasNextLine())
			result.add("2 "+in.nextLine());
		
		in.close();
		PrintWriter out=new PrintWriter(new File(prefix+"BK_ac"));
		for(int i=0; i<result.size()-1; i++)
			out.print(result.get(i)+"\t");
		out.print(result.get(result.size()-1));
		out.close();
			
	}
	
	
}
