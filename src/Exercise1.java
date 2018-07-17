import java.util.Scanner;

public class Exercise1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner cin = new Scanner(System.in);
		String a = cin.nextLine();
		String b = cin.next();
		System.out.println(exercise1(a, b));
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
		
		for(int i = 0; i < n; i++) {
			int a = Math.abs(integer[i])-1;
			if(integer[i]>0 && literal[a] ==1 || integer[i] < 0 && literal[a] == 0) {
				output = 1;
				break;
			}
		}
		
		return output;
	} 
}
