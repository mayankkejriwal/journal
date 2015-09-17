package goldStandard;


import java.io.*;
import java.util.*;

public class GenerateSVMSupFiles {

	
	static String prefix="/host/heteroDatasets/journal/used_finally/Restaurants/";
	static int percent=50;
	static String recPrec=prefix+"sortedScores/svm";
	static String gold=prefix+"goldStandard";
	static String output=prefix+"svmSup"+percent;
	static String outputND=prefix+"svmSupNonDup"+percent;
	
	
	static int num=50;	//number of samples to extract as duplicates from svm file
	static String outputIter=prefix+"svmIter"+num;
	static String outputNDIter=prefix+"svmNonDupIter"+num;
	
	public static void main(String[] args)throws IOException{
		generateIterFiles();
		//generateSVMSupFiles();
	}
	
	public static void generateIterFiles()throws IOException{
		Scanner in=new Scanner(new FileReader(recPrec));
		PrintWriter out=new PrintWriter(new File(outputIter));
		HashSet<String> chosen=new HashSet<String>();
		int count=0;
		while(in.hasNextLine() && count<num){
			String line=in.nextLine().split("\t")[0];
			chosen.add(line);
			out.println(line);
			count++;
		}
		in.close();
		out.close();
		
		Random k=new Random(System.currentTimeMillis()*3);
		ArrayList<String> o=new ArrayList<String>(chosen);
		HashSet<String> chosenND=new HashSet<String>();
		count=0;
		while(count<num){
			int m=k.nextInt(o.size());
			int n=k.nextInt(o.size());
			String c=o.get(m).split(" ")[0]+" "+o.get(n).split(" ")[1];
			if(chosen.contains(c))
				continue;
			else{
				chosenND.add(c);
				count++;
			}
		}
		
				
		out=new PrintWriter(new File(outputNDIter));
		for(String line:chosenND)
			out.println(line);
		out.close();
	}
	
	public static void generateSVMSupFiles()throws IOException{
		Scanner in=new Scanner(new FileReader(gold));
		ArrayList<HashSet<String>> p=new ArrayList<HashSet<String>>();
		HashSet<String> g=new HashSet<String>();
		ArrayList<Integer> a=new ArrayList<Integer>();
		ArrayList<Integer> b=new ArrayList<Integer>();
		
		for(int i=0; i<100/percent; i++)
			p.add(new HashSet<String>());
		Random k=new Random(System.currentTimeMillis()*5);
		
		while(in.hasNextLine()){
			String line=in.nextLine();
			g.add(line);
			a.add(Integer.parseInt(line.split(" ")[0]));
			b.add(Integer.parseInt(line.split(" ")[1]));
			int h=k.nextInt(p.size());
			p.get(h).add(line);
			
		}
		
		in.close();
		
		k=new Random(System.currentTimeMillis()*3);
		HashSet<String> chosen=p.get(k.nextInt(p.size()));
		HashSet<String> chosenND=new HashSet<String>();
		int count=0;
		while(count<chosen.size()){
			int m=k.nextInt(a.size());
			int n=k.nextInt(b.size());
			String c=a.get(m)+" "+b.get(n);
			if(g.contains(c))
				continue;
			else{
				chosenND.add(c);
				count++;
			}
		}
		
		
		PrintWriter out=new PrintWriter(new File(output));
		for(String line:chosen)
			out.println(line);
		out.close();
		
		out=new PrintWriter(new File(outputND));
		for(String line:chosenND)
			out.println(line);
		out.close();
		
	}
}
