package goldStandard;

import java.io.*;
import java.util.*;

import general.CSVParser;

public class GenerateGoldStandards {

	static String prefix="/host/heteroDatasets/eswc1/Restaurants/";
	public static void main(String[] args)throws IOException{
		generatePersons2();
		//printNumTypes(6,6);
	}
	
	//enter the column number index corresponding to 'type' column
	public static void printNumTypes(int type1, int type2)throws IOException{
		HashSet<String> class1=new HashSet<String>();
		HashSet<String> class2=new HashSet<String>();
		
		Scanner in=new Scanner(new FileReader(prefix+"file1.csv"));
		while(in.hasNextLine()){
			class1.add((new CSVParser()).parseLine(in.nextLine())[type1]);
		}
		in.close();
		
		in=new Scanner(new FileReader(prefix+"file2.csv"));
		while(in.hasNextLine()){
			class2.add((new CSVParser()).parseLine(in.nextLine())[type2]);
		}
		in.close();
		
		System.out.println("Number of classes in file1 "+class1.size());
		System.out.println(class1);
		
		System.out.println("Number of classes in file2 "+class2.size());
		System.out.println(class2);
	}
	
	
	public static void verifyLength(int length1, int length2)throws IOException{
		Scanner in=new Scanner(new FileReader(prefix+"file1.csv"));
		while(in.hasNextLine()){
			String[] c=(new CSVParser()).parseLine(in.nextLine());
			if(c.length!=length1)
				System.out.println("file1\t"+c.length+"    "+c[0]+"\t"+c[1]);
			
		}
		in.close();
		
		in=new Scanner(new FileReader(prefix+"file2.csv"));
		while(in.hasNextLine()){
			String[] c=(new CSVParser()).parseLine(in.nextLine());
			if(c.length!=length2)
				System.out.println(c.length+"    "+c[0]+"\t"+c[1]);
			
		}
		in.close();
	}
	
	public static void generateIMGS() throws IOException{
		Scanner in=new Scanner(new FileReader(prefix+"sortedScores/TF"));
		while(in.hasNextLine()){
			System.out.println(in.nextLine().split("\t")[0]);
		}
		in.close();
	}
	
	public static void generateRexaEprintsGS()throws IOException{
		
		HashMap<String, Integer> rexa=new HashMap<String,Integer>();
		HashMap<String, Integer> eprints=new HashMap<String,Integer>();
		Scanner in=new Scanner(new FileReader(prefix+"rexaProp.csv"));
		int count=0;
		while(in.hasNextLine()){
			String line=in.nextLine();
			CSVParser p=new CSVParser();
			String[] q=p.parseLine(line);
			rexa.put(q[0],count);
			count++;
		}
		in.close();
		
		in=new Scanner(new FileReader(prefix+"eprintsProp.csv"));
		count=0;
		while(in.hasNextLine()){
			String line=in.nextLine();
			CSVParser p=new CSVParser();
			String[] q=p.parseLine(line);
			eprints.put(q[0],count);
			count++;
		}
		in.close();
		
		HashMap<String, String> entity1=new HashMap<String,String>();
		HashMap<String, String> entity2=new HashMap<String,String>();
		in=new Scanner(new FileReader(prefix+"eprints_rexa_goldStandard.csv"));
		while(in.hasNextLine()){
			String[] q=(new CSVParser()).parseLine(in.nextLine());
			if(q[1].contains("entity1"))
				entity1.put(q[0], q[2]);
			else if(q[1].contains("entity2"))
				entity2.put(q[0], q[2]);
		}
		in.close();
		
		PrintWriter out=new PrintWriter(new File(prefix+"goldStandard"));
		for(String a:entity1.keySet())
			if(entity2.containsKey(a))
				if(eprints.get(entity1.get(a))!=null && rexa.get(entity2.get(a))!=null)
				out.println(eprints.get(entity1.get(a))+" "+rexa.get(entity2.get(a)));
		out.close();
	}

	public static void generateIIMBGS()throws IOException{
		
		HashMap<String, Integer> f1=new HashMap<String,Integer>();
		HashMap<String, Integer> f2=new HashMap<String,Integer>();
		Scanner in=new Scanner(new FileReader(prefix+"file1Prop.csv"));
		int count=0;
		while(in.hasNextLine()){
			String line=in.nextLine();
			CSVParser p=new CSVParser();
			String[] q=p.parseLine(line);
			f1.put(q[0],count);
			count++;
		}
		in.close();
		
		in=new Scanner(new FileReader(prefix+"file2Prop.csv"));
		count=0;
		while(in.hasNextLine()){
			String line=in.nextLine();
			CSVParser p=new CSVParser();
			String[] q=p.parseLine(line);
			f2.put(q[0],count);
			count++;
		}
		in.close();
		
		HashMap<String, String> entity1=new HashMap<String,String>();
		HashMap<String, String> entity2=new HashMap<String,String>();
		in=new Scanner(new FileReader(prefix+"refalign.csv"));
		while(in.hasNextLine()){
			String[] q=(new CSVParser()).parseLine(in.nextLine());
			if(q[1].contains("entity1"))
				entity1.put(q[0], q[2]);
			else if(q[1].contains("entity2"))
				entity2.put(q[0], q[2]);
		}
		in.close();
		
		PrintWriter out=new PrintWriter(new File(prefix+"goldStandard"));
		for(String a:entity1.keySet())
			if(entity2.containsKey(a))
				if(f1.get(entity1.get(a))!=null && f2.get(entity2.get(a))!=null)
				out.println(f1.get(entity1.get(a))+" "+f2.get(entity2.get(a)));
		out.close();
	}
	
	//for persons2, we first generated the property table version, which makes everything easier
	public static void generatePersons2()throws IOException{
		Scanner in=new Scanner(new FileReader(prefix+"file1.csv"));
		HashMap<String, Integer> map1=new HashMap<String,Integer>();
		int count=0;
		while(in.hasNextLine()){
			String element=(new CSVParser()).parseLine(in.nextLine())[0];
			map1.put(element,count);
			count++;
			
		}
		in.close();
		
		in=new Scanner(new FileReader(prefix+"file2.csv"));
		HashMap<String, Integer> map2=new HashMap<String,Integer>();
		count=0;
		while(in.hasNextLine()){
			String element=(new CSVParser()).parseLine(in.nextLine())[0];
			map2.put(element,count);
			count++;
			
		}
		in.close();
		
		PrintWriter out=new PrintWriter(new File(prefix+"goldStandard"));
		in=new Scanner(new FileReader(prefix+"goldstandardprop.csv"));
		while(in.hasNextLine()){
			String[] line=(new CSVParser()).parseLine(in.nextLine());
			if(line[1].equals("null")||line[2].equals("null")||(!line[6].equals("1.0"))||(!line[9].equals("=")))
				continue;
			if(map1.containsKey(line[1])&&map2.containsKey(line[2]))
				out.println(map1.get(line[1])+" "+map2.get(line[2]));
		}
		
		out.close();
		in.close();
		
		
	}
	
	
	//Find identical tuples in both files
	public static void refineIIMBGS()throws IOException{
		HashMap<String, Integer> f1=new HashMap<String,Integer>();
		Scanner in=new Scanner(new FileReader(prefix+"file1.csv"));
		int count=0;
		while(in.hasNextLine()){
			String[] line=(new CSVParser()).parseLine(in.nextLine());
			String a="";
			for(String l:line)
				if(l.length()!=0)
					a=a+l+" ";
			f1.put(a.substring(0,a.length()-1),count);
			count++;
		}
		in.close();
		in=new Scanner(new FileReader(prefix+"file2.csv"));
		count=0;
		while(in.hasNextLine()){
			String[] line=(new CSVParser()).parseLine(in.nextLine());
			String a="";
			for(String l:line)
				if(l.length()!=0)
					a=a+l+" ";
			a=(a.substring(0,a.length()-1));
			if(f1.containsKey(a))
				System.out.println(f1.get(a)+" "+count);
			count++;
		}
		in.close();
	}
	

	
}
