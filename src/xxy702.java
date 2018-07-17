import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class xxy702 {
	
	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		if(Integer.parseInt(args[1]) == 1) {
			String clause = "";
			String assignment ="";
			for(int i = 0; i < args.length; i++) {
				if(args[i].equals("-clause") ) {
					 clause = args[i+1];
				}else if(args[i].equals("-assignment") ) {
					 assignment = args[i+1];
				}
			}
			System.out.println(exercise1(clause, assignment));
		}
		if(Integer.parseInt(args[1]) == 2) {
			String name = "";
			String assignment = "";
			for(int i = 0; i < args.length; i++) {
				if(args[i].equals("-wdimacs") ){
					name = args[i+1];
				}else if(args[i].equals("-assignment")) {
					assignment = args[i+1];
				}
			}
			System.out.println(exercise2(name,assignment));
		}
		if(Integer.parseInt(args[1]) == 3) {
			
			String name = "";
			double time_budget= 0;
			int repetitions = 0;
			for(int i = 0; i < args.length; i++) {
				if(args[i].equals("-wdimacs")) {
					name = args[i+1];
				}else if(args[i].equals("-time_budget")) {
					time_budget = Double.parseDouble(args[i+1]);
				}else if(args[i].equals("-repetitions")) {
					repetitions = Integer.parseInt(args[i+1]);
				}
			}
			for(int i =0; i< repetitions; i++) {
				exercise3(name, time_budget, 100, 0.6);
			}
		}
	}
	
	public static void exercise3(String name,double b, int lambda, double chi) throws IOException {
		File dir= new File(".");
		File fin = new File(dir.getCanonicalPath() + File.separator + name);
		int num = 0;// number of lines
		String line = null;  
		int nsat = 0;// number of satisfied items
		List<String> list = new ArrayList<String>();
		int n = 0, m = 0, p =0;
		int generation = 0;
		String xbest = "";
		
		// read lines in the file
		BufferedReader br = new BufferedReader(new FileReader(fin));   
		while ((line = br.readLine()) != null) {  
		    list.add(line);
		    num++;
		}  
		String[] array = list.toArray(new String[num]);
		
		br.close();
		
		//GET n and m
		for(int i =0; i<array.length;i++) {
			if(array[i].startsWith("p")) {
				String[] subpara = array[i].split(" ");
				n = Integer.parseInt(subpara[2]);
				m = Integer.parseInt(subpara[3]);
				p = i;
			}
		}

		//Generate solution
		String[] solution = new String[lambda];
		solution = initialize(n, lambda);
		int maxsat =0;
		int ssat = 0;
		String assignment = "";
		// match
		long start = System.currentTimeMillis() ;
		while(System.currentTimeMillis() < start+b*1000) {
			generation++;
			
			Random rn = new Random();
			int c = rn.nextInt(lambda);
			assignment = solution[c];
			nsat = 0;// reset sat_num	
			for(int i = p+1; i<num; i++) {
				String clause = array[i];
				int sat = exercise1(clause, assignment);
				//System.out.println(t);
				if(sat == 1) {
					nsat++;
				}
				if(System.currentTimeMillis() >= start+b*1000) {
					break;
				}
			}
			if(nsat > maxsat) {
				maxsat = nsat;
				xbest = solution[c];
			}
			if(nsat == m) {
				break;
			}
			solution = evolve( xbest, assignment, lambda, chi); //mutate solution
		}
		
		int t = generation * lambda;
		System.out.println(t+"	"+ maxsat+ "	"+xbest);
	}
	
	public static int exercise2(String name, String assignment) throws IOException {
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
		int m = str_integer.length-2;
		int[] integer= new int[m];
		
		for(int i = 0; i < m; i++) {
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
		for(int i = 0; i < m; i++) {
			int a = Math.abs(integer[i])-1;
			if(integer[i]>0 && literal[a] ==1 || integer[i] < 0 && literal[a] == 0) {
				output = 1;
				break;
			}
		}
		
		return output;
	} 
	
	public static String[] initialize(int n, int lambda) {
		String[] population = new String[lambda];
		int[] chrome = new int[n];
		String individual;
		
		for(int i=0; i < lambda; i++) {
			for(int r = 0; r < n; r++) {
				Random rn = new Random();
				int c = rn.nextInt(2);
				if(c == 1) {
					chrome[r] = 0;
				}else {
					chrome[r] = 1;
				}
			}
			
			StringBuffer sb = new StringBuffer();
			for(int t = 0; t < n ;t++) {
				sb.append(chrome[t]);
			}
			individual = sb.toString();
			population[i] = individual;
		}
		
		return population;
	}

	public static String mutation(String bits_x, double chi) {
		int n = bits_x.length();
		int[] genes = new int[n];
		String output = "";
		String input = bits_x.toString();
		double rate = chi /n;
		
		for(int i = 0; i< n; i++) {
			Character ch = input.charAt(i);
			genes[i] = Integer.parseInt(ch.toString());
		}
		
		for(int i = 0; i<n; i++) {
			if(Math.random()<= rate) {
				
				if(genes[i] == 0) {
					genes[i] = 1;
				}else {
					genes[i] = 0;
				}
			}
		}
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < n ;i++) {
			sb.append(genes[i]);
		}
		output = sb.toString();
			//System.out.println(output);
		return output;
	}
	
	public static String crossover(String bits_x, String bits_y) {
		Random rn = new Random();
		int n = bits_x.length();
		int m = bits_y.length();
		int[] genesx = new int[n];
		int[] genesy = new int[n];
		int[] genesz = new int[n];
		String output = "";
		String inputx = bits_x.toString();
		String inputy = bits_y.toString();
		
		for(int i = 0; i< n; i++) {
			Character x = inputx.charAt(i);
			Character y = inputy.charAt(i);
			genesx[i] = Integer.parseInt(x.toString());
			genesy[i] = Integer.parseInt(y.toString());
		}
		
		for(int i = 0; i <n; i++) {
			if(genesx[i]!=genesy[i]) {
				int c = rn.nextInt(2);
				if (c == 1) {
					genesz[i] = 1;
				}else {
					genesz[i] = 0;
				}
			}else {
				genesz[i] = genesx[i];
			}
		}
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < n ;i++) {
			sb.append(genesz[i]);
		}
		output = sb.toString();
		//System.out.println(output);
	
		return output;
	}

	public static String[] evolve(String x, String y, int lambda, double chi) {
			String mutation_x, mutation_y;
			String[] gbest = new String[lambda];
			
			//One generation
			for(int i= 0; i < lambda; i++) {
				
				//Mutation
				mutation_x = mutation(x, chi);
				mutation_y = mutation(y, chi);
			
				//Crossover
				gbest[i] = crossover(mutation_x, mutation_y);	
			}	
		return gbest;
	}
}



