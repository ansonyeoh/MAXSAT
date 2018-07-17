import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Exercise2 {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Scanner cin = new Scanner(System.in);
		String name = cin.nextLine();
		String assignment = cin.nextLine();
		
		Exercise2(name, assignment);
	}
	
	
	public static int Exercise2(String name, String assignment) throws IOException {
		File dir= new File(".");
		File fin = new File(dir.getCanonicalPath() + File.separator + name);
		int output = 0;
		int n = 0;// number of lines
		String line = null;  
		List<String> list = new ArrayList<String>();
		String[] lines = new String[100];
		
		// read lines in the file
		BufferedReader br = new BufferedReader(new FileReader(fin));   
		while ((line = br.readLine()) != null) {  
		    list.add(line);
		    n++;
		}  
		br.close();
		
		for(int i = 2; i<n;i++) {
			String clause = list.get(i);
			int t = exercise1(clause, assignment);
			//System.out.println(t);
			if(t == 1) {
				output++;
			}
		}
		System.out.println(output);
		return output;
	}
	
	public static int exercise1(String clause, String assignment) {
		int output = 0;
		//Clause split
		String[] str_integer = clause.split(" ");
		int m = str_integer.length;
		int[] integer= new int[m-2];
	
		for(int i = 0; i < m-2; i++) {
			String t = str_integer[i+1].toString();
			integer[i] = Integer.parseInt(t);
		}
		
		//Assignment analysis
		int n = assignment.length();
		int[] literal = new int[n];
		String assign = assignment.toString();
		
		for(int i = 0; i < n; i++) {
			Character ch = assign.charAt(i);
			literal[i] = Integer.parseInt(ch.toString());
		}
		
		//Compare/check
		
		for(int i = 0; i < n; i++) {
			if(integer[i]>0 && literal[i] ==1 || integer[i] < 0 && literal[i] == 0) {
				output = 1;
			}
		}
		
		return output;
	} 
}
