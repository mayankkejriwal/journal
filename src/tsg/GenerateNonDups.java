package tsg;

import java.io.*;
import java.util.*;

public class GenerateNonDups {

	//takes a score file from sortedScores/ and generates non-duplicates from the top n
	//non-dups file will not have appended scores
	
	static String prefix="/host/heteroDatasets/journal/used_finally/Restaurants/";
	
	public static void main(String[] args)throws IOException{
		
		genNonDups(prefix+"sortedScores/J",prefix+"sortedScores/J_nonDups",500);
		//evaluateNonDupsPrecision(prefix+"sortedScores/TF_nonDups",prefix+"goldStandard");
	}
	
	//will randomly generate n nonduplicates, by permuting top n duplicates
	public static void genNonDups(String infile, String outfile, int topn)throws IOException{
		Scanner in=new Scanner(new FileReader(infile));
		ArrayList<Integer> a1=new ArrayList<Integer>(topn);
		ArrayList<Integer> a2=new ArrayList<Integer>(topn);
		HashSet<String> origLines=new HashSet<String>(topn);
		int count=0;
		while(in.hasNextLine()&&count<topn){
			String line=in.nextLine();
			origLines.add(line);
			a1.add(Integer.parseInt(line.split("\t")[0].split(" ")[0]));
			a2.add(Integer.parseInt(line.split("\t")[0].split(" ")[1]));
			count++;
		}
		in.close();
		
		HashSet<String> newLines=new HashSet<String>(topn);
		Random p=new Random(System.currentTimeMillis());
		Random q=new Random(System.currentTimeMillis()+7);
		count=0;
		while(count<topn){
			int m=p.nextInt(topn);
			int n=q.nextInt(topn);
			String b=a1.get(m)+" "+a2.get(n);
			if(origLines.contains(b)||newLines.contains(b))
				continue;
			else{
				newLines.add(b);
				count++;
			}
		}
		
		PrintWriter out=new PrintWriter(new File(outfile));
		for(String b:newLines)
			out.println(b);
		out.close();
		
	}
	
	//will print precision of non-dups; recall does not apply
	public static void evaluateNonDupsPrecision(String file, String gold)throws IOException{
		Scanner in=new Scanner(new FileReader(gold));
		HashSet<String> g=new HashSet<String>();
		while(in.hasNextLine())
			g.add(in.nextLine());
		in.close();
		in=new Scanner(new FileReader(file));
		int correct=0;
		int total=0;
		while(in.hasNextLine()){
			String p=in.nextLine();
			if(!g.contains(p))
				correct++;
			total++;
		}
		in.close();
		System.out.println(1.0*correct/total);
		
	}
	
}
