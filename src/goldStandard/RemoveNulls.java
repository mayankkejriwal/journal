package goldStandard;

import java.io.*;
import java.util.*;

public class RemoveNulls {
	static String prefix="/host/heteroDatasets/journal/used_finally/eprints_rexa/";
	
	public static void main(String[] args)throws IOException{
		Scanner in=new Scanner(new FileReader(prefix+"file2.csv"));
		PrintWriter out=new PrintWriter(new File(prefix+"f2.csv"));
		while(in.hasNextLine()){
			String line=in.nextLine();
			out.println(line.replaceAll("null", ""));
		}
		
		out.close();
		in.close();
	}
}
