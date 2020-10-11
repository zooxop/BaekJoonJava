package baekjoonAlgorithmStudy;

import java.util.Scanner;

public class Proplem_2588 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Scanner in = new Scanner(System.in); 
		
		int a = in.nextInt();
		int b = in.nextInt();
		
		in.close();
		
		System.out.println(a * (b%10)); //385 중 5
		System.out.println(a * (b%100/10)); //385 % 100 => 85. 85 / 10 => 8 
		System.out.println(a * (b/100));
		
		System.out.println(a*b);
	}

}







