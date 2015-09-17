package goldStandard;


import java.io.*;
import java.util.*;

import tsg.JaccardFromTF;

public class EvaluateSchema {

	static String prefix="/host/heteroDatasets/journal/used_finally/Restaurants/";
	public static void main(String[] args)throws IOException{
		printPrecRec(prefix+"schema_dumas",prefix+"goldStandard_schema");
	}
	
	public static double[] printPrecRec(String file, String gold)throws IOException{
		Scanner in=new Scanner(new FileReader(gold));
		HashSet<String> g=new HashSet<String>();
		while(in.hasNextLine())
			g.add(in.nextLine());
		in.close();
		in=new Scanner(new FileReader(file));
		HashSet<String> f=new HashSet<String>();
		while(in.hasNextLine()){
			f.add(in.nextLine());
		}
		in.close();
		int correct=JaccardFromTF.intersect(g,f).size();
		double[] res=new double[3];
		res[0]=100.0*correct/f.size();
		res[1]=100.0*correct/g.size();
		if(res[0]+res[1]==0)
			res[2]=0;
		else
			res[2]=2.0*res[0]*res[1]/(res[0]+res[1]);
		System.out.println("Precision \t Recall \t FM");
		System.out.println(res[0]+"\t "+res[1]+"\t"+res[2]);
		return res;
	}
}
