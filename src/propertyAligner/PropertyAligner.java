package propertyAligner;

import java.io.*;
import java.util.*;

import tsg.JaccardFromTF;
import general.CSVParser;
import general.Parameters;

public class PropertyAligner {

	String schema1;
	String schema2;
	ArrayList<Integer> nameAlign1;
	ArrayList<Integer> nameAlign2;
	
	String file1;
	String file2;
	
	String dups;
	String nonDups;
	
	ArrayList<HashSet<String>> set1;
	ArrayList<HashSet<String>> set2;
	
	double[][] matrix;
	double[][] nonDupMatrix;
	int topn;
	ArrayList<HashSet<String>> nonDupSet1;
	ArrayList<HashSet<String>> nonDupSet2;
	
	//we'll assume standard names and file formats
	public PropertyAligner(String prefix, int topn)throws IOException{
		schema1=prefix+"archive/schema1.txt";
		schema2=prefix+"archive/schema2.txt";
		file1=prefix+"file1.csv";
		file2=prefix+"file2.csv";
		nameAlign1=new ArrayList<Integer>();
		nameAlign2=new ArrayList<Integer>();
		dups=prefix+"sortedScores/J";
		nonDups=prefix+"sortedScores/J_nonDups";
		
		HashMap<String,HashSet<Integer>> s1=new HashMap<String,HashSet<Integer>>();
		Scanner in=new Scanner(new FileReader(schema1));
		int count=0;
		while(in.hasNextLine()){
			String line=in.nextLine();
			int i=Integer.parseInt(line.split("\t")[0]);
			String p=line.split("\t")[1];
			String q=null;
			if(p.contains("#")){
				String[] j=p.split("#");
				q=j[j.length-1].toLowerCase();
			}
			else{
				String[] j=p.split("/");
				q=j[j.length-1].toLowerCase();
			}
			if(!s1.containsKey(q))
				s1.put(q, new HashSet<Integer>());
			s1.get(q).add(i);
			count++;
		}
		
		set1=new ArrayList<HashSet<String>>(count);
		nonDupSet1=new ArrayList<HashSet<String>>(count);
		for(int i=0; i<count; i++){
			set1.add(new HashSet<String>());
			nonDupSet1.add(new HashSet<String>());
		}
		
		in.close();
		
		in=new Scanner(new FileReader(schema2));
		count=0;
		while(in.hasNextLine()){
			String line=in.nextLine();
			int i=Integer.parseInt(line.split("\t")[0]);
			String p=line.split("\t")[1];
			String q=null;
			if(p.contains("#")){
				String[] j=p.split("#");
				q=j[j.length-1].toLowerCase();
			}
			else{
				String[] j=p.split("/");
				q=j[j.length-1].toLowerCase();
			}
			if(s1.containsKey(q))
				for(int k:s1.get(q)){
					nameAlign1.add(k);
					nameAlign2.add(i);
			
				}
			count++;
		}
		in.close();
		set2=new ArrayList<HashSet<String>>(count);
		nonDupSet2=new ArrayList<HashSet<String>>(count);
		for(int i=0; i<count; i++){
			set2.add(new HashSet<String>());
			nonDupSet2.add(new HashSet<String>());
		}
		matrix=new double[set1.size()][set2.size()];
		nonDupMatrix=new double[nonDupSet1.size()][nonDupSet2.size()];
		this.topn=topn;
		populateMatrices();
		
	}
	
	private void populateMatrices()throws IOException{
		populateSets();
		populateNonDupSets();
		for(int i=0; i<matrix.length; i++)
			for(int j=0; j<matrix[i].length; j++){
				matrix[i][j]=JaccardFromTF.computeJaccardIndex(set1.get(i), set2.get(j));
				nonDupMatrix[i][j]=JaccardFromTF.computeJaccardIndex(nonDupSet1.get(i), nonDupSet2.get(j));
			}
	}
	
	private void populateSets()throws IOException{
		
		ArrayList<String> a=new ArrayList<String>();
		ArrayList<String> b=new ArrayList<String>();
		Scanner in=new Scanner(new FileReader(file1));
		while(in.hasNextLine())
			a.add(in.nextLine());
		in.close();
		in=new Scanner(new FileReader(file2));
		while(in.hasNextLine())
			b.add(in.nextLine());
		in.close();
		in=new Scanner(new FileReader(dups));
		int count=0;
		while(in.hasNextLine()&&count<topn){
			String[] l=in.nextLine().split("\t")[0].split(" ");
			int c1=Integer.parseInt(l[0]);
			int c2=Integer.parseInt(l[1]);
			String[] d=(new CSVParser()).parseLine(a.get(c1));
			for(int i=0; i<d.length; i++){
				String[] d1=d[i].split(Parameters.splitstring);
				
				for(String e:d1)
					if(e.trim().length()!=0)
						set1.get(i).add(e.trim().toLowerCase());
			}
			d=(new CSVParser()).parseLine(b.get(c2));
			for(int i=0; i<d.length; i++){
				String[] d1=d[i].split(Parameters.splitstring);
				
				for(String e:d1)
					if(e.trim().length()!=0)
						set2.get(i).add(e.trim().toLowerCase());
			}
			count++;
		}
		in.close();
		
		}
	
	private void populateNonDupSets()throws IOException{
	
	ArrayList<String> a=new ArrayList<String>();
	ArrayList<String> b=new ArrayList<String>();
	Scanner in=new Scanner(new FileReader(file1));
	while(in.hasNextLine())
		a.add(in.nextLine());
	in.close();
	in=new Scanner(new FileReader(file2));
	while(in.hasNextLine())
		b.add(in.nextLine());
	in.close();
	in=new Scanner(new FileReader(nonDups));
	int count=0;
	while(in.hasNextLine()&&count<topn){
		String[] l=in.nextLine().split(" ");
		int c1=Integer.parseInt(l[0]);
		int c2=Integer.parseInt(l[1]);
		String[] d=(new CSVParser()).parseLine(a.get(c1));
		for(int i=0; i<d.length; i++){
			String[] d1=d[i].split(Parameters.splitstring);
			
			for(String e:d1)
				if(e.trim().length()!=0)
					nonDupSet1.get(i).add(e.trim().toLowerCase());
		}
		d=(new CSVParser()).parseLine(b.get(c2));
		for(int i=0; i<d.length; i++){
			String[] d1=d[i].split(Parameters.splitstring);
			
			for(String e:d1)
				if(e.trim().length()!=0)
					nonDupSet2.get(i).add(e.trim().toLowerCase());
		}
		count++;
	}
	in.close();
	
	}

	public void execute(String outfile) throws FileNotFoundException{
		double thresh=aggregateAlign();
		if(thresh==0)
			thresh=aggregateMatrix();
		
		System.out.println(thresh);
		//System.out.println(nameAlign1.size()+" "+nameAlign2.size());
		
		HashSet<String> results=new HashSet<String>();
		for(int i=0; i<nameAlign1.size(); i++)
			results.add(nameAlign1.get(i)+" "+nameAlign2.get(i));
		for(int i=0; i<matrix.length; i++)
			for(int j=0; j<matrix[i].length; j++)
				if(matrix[i][j]-nonDupMatrix[i][j]>=thresh)
					results.add(i+" "+j);
		PrintWriter out=new PrintWriter(new File(outfile));
		for(String a: results)
			out.println(a);
		out.close();
		
	}
	
	private double aggregateMatrix(){
		double b=0.0;
		for(int i=0; i<matrix.length; i++)
			for(int j=0; j<matrix[i].length; j++)
				b+=matrix[i][j];
		return b/(set1.size()*set2.size());
	}
	
	private double aggregateAlign(){
		if(nameAlign1.size()==0)
			return 0;
		else{
			double b=0.0;
			for(int i=0; i<nameAlign1.size(); i++)
				b+=(matrix[nameAlign1.get(i)][nameAlign2.get(i)]-nonDupMatrix[nameAlign1.get(i)][nameAlign2.get(i)]);
			
			return b/nameAlign1.size();
			
		}
		
	}
	
	public void printNameAlign(){
		if(nameAlign1.size()!=nameAlign2.size())
			System.out.println("Error");
		for(int i=0; i<nameAlign1.size(); i++)
			System.out.println(nameAlign1.get(i)+" "+nameAlign2.get(i));
	}
	
	public static void main(String[] args)throws IOException{
		String prefix="/host/heteroDatasets/journal/used_finally/Restaurants/";
		PropertyAligner p=new PropertyAligner(prefix,500);
		//p.printNameAlign();
		p.execute(prefix+"schema_ours");
	}
}
