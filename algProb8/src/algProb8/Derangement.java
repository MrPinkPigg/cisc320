package algProb8;

import java.util.*;
import java.io.*;

public class Derangement {
	public static void main(String[] args) {
		/*
		int[] output = new int[5];
		output = derange(3, 1, output);
		for(int i : output) {
			System.out.print(i + " ");
		}*/
		
		ArrayList<Integer[]> output2 = new ArrayList<>();
		output2 = derange2(output2, 4, 1);
		for(Integer[] a : output2) {
			for(Integer i : a) {
				System.out.print(i + " ");
			}
		}
	}
	
	public static int[] derange(int n, int count, int[] output) {
		while(count <= n) {
			output[count - 1] = count;
			count++;
			output = derange(n, count, output);
		}
		
		return output;
	}
	
	public static ArrayList<Integer[]> derange2(ArrayList<Integer[]> output2, int n, int count){
		while(count <= n) {
			Integer[] der = new Integer[n];
			int c =  0;
			while(c < der.length) { der[c] = 0; c++; }
			System.out.println("here");
			
			for(int i = 3; i < n; i++) {
				der[i] = (i - 1) * (der[i - 1] + der [i - 2]);
			}
			
			output2.add(der);
			count++;
			output2 = derange2(output2, n, count);
		}
		
		return output2;
	}
}
