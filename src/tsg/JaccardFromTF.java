package tsg;

import general.CSVParser;
import general.Parameters;

import java.io.*;
import java.util.*;

public class JaccardFromTF {

	
	/*
	 * Takes a sortedScores/TF file (or any other score file) and converts it to a 'J' file by
	 * computing Jaccard distances. 
	 * 
	 * CAVEAT: could be missing data because of errors made in previous score file
	 */
	static String prefix="/host/heteroDatasets/journal/used_finally/Restaurants/";
	static String file1=prefix+"file1.csv";
	static String file2=prefix+"file2.csv";
	static String tffile=prefix+"sortedScores/TF";
	static String jaccardfile=prefix+"sortedScores/J";
	
	static String schemafile=prefix+"schema_ours";
	
	public static void main(String[] args)throws IOException{
				nonSchemaJaccard();
	}
	
	public static void nonSchemaJaccard()throws IOException{
		HashMap<Integer,HashSet<String>> f1=convertFileToMap(file1);
		HashMap<Integer,HashSet<String>> f2=convertFileToMap(file2);
		HashMap<Double, HashSet<String>> scores=new HashMap<Double,HashSet<String>>();
		Scanner in=new Scanner(new FileReader(tffile));
		while(in.hasNextLine()){
			String[] line=in.nextLine().split("\t");
			int i1=Integer.parseInt(line[0].split(" ")[0]);
			int i2=Integer.parseInt(line[0].split(" ")[1]);
			double score=computeJaccardIndex(f1.get(i1),f2.get(i2));
			if(!scores.containsKey(score))
				scores.put(score, new HashSet<String>());
			scores.get(score).add(line[0]);
		}
		
		
		
		in.close();
		
		ArrayList<Double> s=new ArrayList<Double>(scores.keySet());
		Collections.sort(s);
		PrintWriter out=new PrintWriter(new File(jaccardfile));
		for(int i=s.size()-1; i>=0; i--)
			for(String m:scores.get(s.get(i)))
				out.println(m+"\t"+s.get(i));
		out.close();
	
	}
	
	public static void schemaJaccard()throws IOException{
		
		ArrayList<Integer> schema1=getSchemaIndices(true,schemafile);
		ArrayList<Integer> schema2=getSchemaIndices(false,schemafile);
		HashMap<Integer,HashSet<String>> f1=convertFileToMap(file1,schema1);
		HashMap<Integer,HashSet<String>> f2=convertFileToMap(file2,schema2);
		
		
		HashMap<Double, HashSet<String>> scores=new HashMap<Double,HashSet<String>>();
		Scanner in=new Scanner(new FileReader(tffile));
		while(in.hasNextLine()){
			String[] line=in.nextLine().split("\t");
			int i1=Integer.parseInt(line[0].split(" ")[0]);
			int i2=Integer.parseInt(line[0].split(" ")[1]);
			double score=computeJaccardIndex(f1.get(i1),f2.get(i2));
			if(!scores.containsKey(score))
				scores.put(score, new HashSet<String>());
			scores.get(score).add(line[0]);
		}
		
		
		
		in.close();
		
		ArrayList<Double> s=new ArrayList<Double>(scores.keySet());
		Collections.sort(s);
		PrintWriter out=new PrintWriter(new File(jaccardfile));
		for(int i=s.size()-1; i>=0; i--)
			for(String m:scores.get(s.get(i)))
				out.println(m+"\t"+s.get(i));
		out.close();
	
	}

	/*
	 * if file1 then first column is parsed, otherwise second
	 * file is schema file
	 */
	private static ArrayList<Integer> getSchemaIndices(boolean file1, String file)throws IOException{
		Scanner in=new Scanner(new FileReader(file));
		ArrayList<Integer> res=new ArrayList<Integer>();
		while(in.hasNextLine()){
			String[] line=in.nextLine().split(" ");
			if(file1)
				res.add(Integer.parseInt(line[0]));
			else
				res.add(Integer.parseInt(line[1]));	
		}
		in.close();
		return res;
	}
	
	//input should be file1 or file2
	private static HashMap<Integer,HashSet<String>> convertFileToMap(String file)throws IOException{
		HashMap<Integer,HashSet<String>> result=new HashMap<Integer,HashSet<String>>();
		Scanner in=new Scanner(new FileReader(file));
		int count=0;
		while(in.hasNextLine()){
			String[] line=in.nextLine().split(Parameters.splitstring);
			HashSet<String> p=new HashSet<String>();
			for(String a: line)
				if(a.trim().length()!=0)
					p.add(a.trim().toLowerCase());
			result.put(count, p);
			count++;
		}
		in.close();
		return result;
	}
	
	//input should be file1 or file2
	private static HashMap<Integer,HashSet<String>> convertFileToMap(String file, ArrayList<Integer> schema)throws IOException{
		HashMap<Integer,HashSet<String>> result=new HashMap<Integer,HashSet<String>>();
		Scanner in=new Scanner(new FileReader(file));
		int count=0;
		while(in.hasNextLine()){
			String[] l=(new CSVParser()).parseLine(in.nextLine());
			HashSet<String> p=new HashSet<String>();
			
			for(int i:schema){
			
			String[] line=l[i].split(Parameters.splitstring);
			
			for(String a: line)
				if(a.trim().length()!=0)
					p.add(a.trim().toLowerCase());
			}
			result.put(count, p);
			
			count++;
		}
		in.close();
		return result;
	}

	//if both sets are empty, returns 0; for null, return 0 as well
	public static double computeJaccardIndex(HashSet<String> a, HashSet<String> b){
		if(a==null||b==null)
			return 0;
		else if(a.size()==0&& b.size()==0)
			return 0;
		else if(a.size()==0|| b.size()==0)
			return 0;
		
		return 1.0*intersect(a,b).size()/union(a,b).size();
		
		
	}
	
	
	public static HashSet<String> union(HashSet<String> a, HashSet<String> b){
		if(a==null||b==null)
			return null;
		HashSet<String> result=new HashSet<String>(a);
		for(String c:b)
			result.add(c);
		
		return result;
	}
	
	public static HashSet<String> intersect(HashSet<String> a, HashSet<String> b){
		if(a==null||b==null)
			return null;
		HashSet<String> result=new HashSet<String>();
		for(String c:a)
			if(b.contains(c))
				result.add(c);
				
		return result;
	}
}
