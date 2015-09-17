package blocking;

import baseline.CanopyClustering;
import christen.Parameters;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import evaluation.ImportGoldStandard;


/*
 * We use christen.Parameters, not general.Parameters. Since splitstring is the same, this is not a problem;
 * Must set parameters in main file before calling canopies code
 * Modeled after the entry files in HeteroBlocking.entry
 */
public class EntryCanopies {

	static String prefix="/host/heteroDatasets/journal/used_finally/Restaurants/";
	static String file1=prefix+"file1.csv";
	static String file2=prefix+"file2.csv";
	static String goldStandard=prefix+"goldStandard";
	static String canopyResults=prefix+"canopyResults.csv";//at present, not used
	
	public static void main(String[] args)throws IOException{
		Parameters.maxpairs=100;
		Parameters.maxtokentuples=100;
		Parameters.ut=0.00001;
		/*
		double[] rr={
				//0.99,0.95,0.90,
				0.85,0.80,0.75,
				0.70,0.65,0.60,
				//0.55,0.50,0.40,
				//0.30,0.20,0.10,
				0.05,0.01
				};*/
		canopy();
		//printCanopyToFile(rr,10);
	}
	
	
	//will print results to outfile in CSV format
	//will collect results and print each rr to console in case of sudden termination
	//at the end, will print all collected results to file
	public static void printCanopyToFile(double[] rr, int iter)throws IOException{
		ArrayList<String> results=new ArrayList<String>();
		results.add("PC,RR");
		System.out.println("PC,RR");
		for(double r:rr){
			double[] res=canopy(r,iter);
			System.out.println(res[0]+","+res[1]);
			results.add(res[0]+","+res[1]);
		}
	/*	PrintWriter out=new PrintWriter(new File(outfile));
		for(String r:results)
			out.println(r);
		out.close();*/
	}
	
	public static void canopy()throws IOException{
		
		ImportGoldStandard gold=new ImportGoldStandard(file1,file2,goldStandard);
		CanopyClustering can=new CanopyClustering(file1,file2);
		
		
			
			can.printPCandRR(gold);
		
	}
	
	//this is a randomized algorithm
	public static double[] canopy(double rr, int iter)throws IOException{
		
		ImportGoldStandard gold=new ImportGoldStandard(file1,file2,goldStandard);
		CanopyClustering can=new CanopyClustering(file1,file2);
		double[] pcrr=new double[2]; 
		for(int i=0; i<iter; i++){
			
			double[] q=can.getPCRR(rr,(int) (System.currentTimeMillis()+i),gold);
			
			pcrr[0]+=q[0];
			pcrr[1]+=q[1];
	
		}
		pcrr[0]/=iter;
		pcrr[1]/=iter;
		System.out.println("Final (average) PC, RR canopy: \n"+pcrr[0]+","+pcrr[1]);
		return pcrr;
	}
}
