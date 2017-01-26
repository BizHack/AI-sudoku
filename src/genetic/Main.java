package genetic;

import java.util.Scanner;

public class Main {
	static int SIZE = 9;
	static Genetic myGen;
	
	static void generateSuduko(){
		long before = System.currentTimeMillis();
		myGen.readyForGenerate();
		Board res = myGen.geneticResult();
		if (res != null){
			res.print();
			long after = System.currentTimeMillis();
			System.out.println("Time: " + (after - before) + " ms");
		} else {
			System.out.println("I can't find the Suduko :(");
		}
	}
	
	static void solveSuduko(){
		Scanner sc = new Scanner(System.in);
		int input[][] = new int[SIZE][SIZE];
		for (int i = 0;i < SIZE;++i){
			for (int j = 0;j < SIZE;++j){
				input[i][j] = sc.nextInt();
				input[i][j]--;
				if (input[i][j] != -1){
					Board.CAN[i][j] = false;
				}
			}
		}
		
		long before = System.currentTimeMillis();
		myGen.readyForSolve(input);
		Board res = myGen.geneticResult();
		if (res != null){
			res.print();
			long after = System.currentTimeMillis();
			System.out.println("Time: " + (after - before) + " ms");
		} else {
			System.out.println("I can't find the Suduko :(");
		}
	}
	
	public static void main(String args[]){
		myGen = new Genetic();
		new GUI();
	}
	
	/*
	5 3 0 0 7 0 0 0 0
	6 0 0 1 9 5 0 0 0
	0 9 8 0 0 0 0 6 0
	8 0 0 0 6 0 0 0 3
	4 0 0 8 0 3 0 0 1
	7 0 0 0 2 0 0 0 6
	0 6 0 0 0 0 2 8 0
	0 0 0 4 1 9 0 0 5
	0 0 0 0 8 0 0 7 9
	 */
}
