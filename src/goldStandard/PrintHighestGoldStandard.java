package goldStandard;

import java.io.*;
import java.util.*;

public class PrintHighestGoldStandard {

	
	static String dataset="Restaurants";
	static String prefix="/host/heteroDatasets/journal/used_finally/"+dataset+"/sortedScores/";
	
	static String[] supDatasets={prefix+"recPrec_svm10_new.csv",prefix+"recPrec_svm50_new.csv"};
	static String[] unsupDatasets={prefix+"recPrec_svm_new.csv",prefix+"recPrec_svmIter_new.csv",prefix+"recPrec_J_new.csv", prefix+"recPrec_TF_new.csv"};
	
	
	public static void main(String[] args)throws IOException{
		printBestFScores();
	}
	
	public static void printBestFScores()throws IOException{
		double[] r=new double[supDatasets.length];
		double[] p=new double[supDatasets.length];
		double[] f=new double[supDatasets.length];
		int count=-1;
		for(String sup:supDatasets){
			count++;
			Scanner in=new Scanner(new FileReader(sup));
			
			while(in.hasNextLine()){
				String[] recprec=in.nextLine().split(",");
				double rec=Double.parseDouble(recprec[0]);
				double prec=Double.parseDouble(recprec[1]);
				double fs=0;
				if(!(rec==0||prec==0))
					fs=2*rec*prec/(rec+prec);
				if(fs>f[count]){
					f[count]=fs;
					r[count]=rec;
					p[count]=prec;
				}
				
			}
			in.close();
		}
		System.out.println("Recall,Precision,FScore,System");
		for(int i=0; i<supDatasets.length; i++){
			System.out.println(r[i]+","+p[i]+","+f[i]+","+supDatasets[i]);
			
		}
		
		
		r=new double[unsupDatasets.length];
		 p=new double[unsupDatasets.length];
		f=new double[unsupDatasets.length];
		count=-1;
		for(String sup:unsupDatasets){
			count++;
			Scanner in=new Scanner(new FileReader(sup));
			
			while(in.hasNextLine()){
				String[] recprec=in.nextLine().split(",");
				double rec=Double.parseDouble(recprec[0]);
				double prec=Double.parseDouble(recprec[1]);
				double fs=0;
				if(!(rec==0||prec==0))
					fs=2*rec*prec/(rec+prec);
				if(fs>f[count]){
					f[count]=fs;
					r[count]=rec;
					p[count]=prec;
				}
				
			}
			in.close();
		}
		System.out.println();
		System.out.println("Recall,Precision,FScore,System");
		for(int i=0; i<unsupDatasets.length; i++){
			System.out.println(r[i]+","+p[i]+","+f[i]+","+unsupDatasets[i]);
			
		}
	}
}
