package genetic;

import java.util.Arrays;

public class Board {
	public static int SIZE = Main.SIZE;
	public static int SQRS = (int)Math.sqrt((double)Main.SIZE);
	public static int coef[] = {1, 1, 1};//Weight of every Heureistic
	public static boolean[][] CAN = new boolean[SIZE][SIZE];//Set or not set
	
	public int[][] sdk;//Suduko Board
	public int huerValue;
	
	Board(){
		huerValue = -1;
		sdk = new int[SIZE][SIZE];
	}
	
	Board(int [][] boardData){
		huerValue = -1;
		sdk = new int[SIZE][SIZE];
		setBoard(boardData);
	}
	
	void setBoard(int [][] boardData){
		for (int i = 0;i < SIZE;++i)
			for (int j = 0;j < SIZE;++j)
				sdk[i][j] = boardData[i][j];
	}
	
	void setGenerateMode(){
		for (int i = 0;i < SIZE;++i){
			boolean mark[] = new boolean[SIZE];
			Arrays.fill(mark, false);
			for (int j = 0;j < SIZE;++j){
				int newNum = getRandomNumber(SIZE);
				while (mark[newNum] == true)
					newNum = getRandomNumber(SIZE);
				mark[newNum] = true;
				sdk[i][j] = newNum;
			}
		}
		huerValue = -1;
	}
	
	void setSolveMode(){
		for (int i = 0;i < SIZE;++i)
			for (int j = 0;j < SIZE;++j) 
				if (CAN[i][j])
					sdk[i][j] = -1;
			
		for (int i = 0;i < SIZE;++i){
			boolean mark[] = new boolean[SIZE];
			Arrays.fill(mark, false);
			for (int j = 0;j < SIZE;++j)
				if (sdk[i][j] > -1)
					mark[sdk[i][j]] = true;
			//System.out.println();
			for (int j = 0;j < SIZE;++j) if (sdk[i][j] == -1){
				int newNum = getRandomNumber(SIZE);
				while (mark[newNum] == true)
					newNum = getRandomNumber(SIZE);
				mark[newNum] = true;
				sdk[i][j] = newNum;
			}
		}
		huerValue = -1;
	}
	
	private int repeatRow(){
		int cnt = 0;
		for (int i = 0;i < SIZE;++i){
			boolean mark[] = new boolean[SIZE];
			Arrays.fill(mark, false);
			for (int j = 0;j < SIZE;++j){
				if (mark[sdk[i][j]] == true)
					++cnt;
				mark[sdk[i][j]] = true;
			}
		}
		return cnt;
	}
	
	private int repeatColumn(){
		int cnt = 0;
		for (int i = 0;i < SIZE;++i){
			boolean mark[] = new boolean[SIZE];
			Arrays.fill(mark, false);
			for (int j = 0;j < SIZE;++j){
				if (mark[sdk[j][i]] == true)
					++cnt;
				mark[sdk[j][i]] = true;
			}
		}
		return cnt;
	}
	
	private int repeatSquare(){ 
		int cnt = 0;
		for (int i = 0;i < SIZE;++i){
			boolean mark[] = new boolean[SIZE];
			Arrays.fill(mark, false);
			int x = (i / SQRS) * SQRS , y = (i % SQRS) * SQRS; 
			for (int j = 0;j < SQRS;++j){
				for (int k = 0;k < SQRS;++k){
					if (mark[sdk[x + j][y + k]] == true)
						++cnt;
					mark[sdk[x + j][y + k]] = true;
				}
			}
		}
		return cnt;
	}
	
	public int Heuristic(){
		int rowRep = repeatRow();
		int colRep = repeatColumn();
		int squareRep = repeatSquare();
		//System.out.println(rowRep + " " + colRep + " " + squareRep);
		huerValue = rowRep * coef[0] + colRep * coef[1] + squareRep * coef[2];
		return huerValue;
	}
	
	public int getHuerisitic(){
		if (huerValue == -1) {
			huerValue = Heuristic();
			return huerValue;
		}
		return huerValue;
	}
	
	public void setHueristic(){
		huerValue = -1;
	}
	
	public void mutation(){
		if (Genetic.DEBUG) System.out.println("Mutatings!");
		for (int i = 0;i < SIZE;++i){
			
			int CANSwaps = 0;
			for (int j = 0;j < SIZE;++j)
				if (CAN[i][j] == true)
					++CANSwaps;
			
			int select = 0;
			double temp = Math.random();
			if (temp < Genetic.PM){
				temp = Math.random();
				if (temp < 0.2 && CANSwaps >= 4) select = 2;
				else if (temp < 0.5 && CANSwaps >= 3) select = 1;
				else if (CANSwaps >= 2) select = 0;
				else continue;
				
			} else continue;
			
			int a, b, c, t1;
			//Swap two positions...
			if (select == 0){
				a = getRandomNumber(SIZE);
				while (CAN[i][a] == false) 
					a = getRandomNumber(SIZE);
				
				b = getRandomNumber(SIZE);
				while (a == b || CAN[i][b] == false) 
					b = getRandomNumber(SIZE);
				
				t1 = sdk[i][a];
				sdk[i][a] = sdk[i][b];
				sdk[i][b] = t1;
			}
			
			//Swap three positions...
			if (select == 1){	
				a = getRandomNumber(SIZE);
				while (CAN[i][a] == false) 
					a = getRandomNumber(SIZE);
				
				b = getRandomNumber(SIZE);
				while (a == b || CAN[i][b] == false) 
					b = getRandomNumber(SIZE);
				
				c = getRandomNumber(SIZE);
				while (a == c || b == c || CAN[i][c] == false) 
					c = getRandomNumber(SIZE);
				
				t1 = sdk[i][a];
				sdk[i][a] = sdk[i][b];
				sdk[i][b] = sdk[i][c];
				sdk[i][c] = t1;
				
			}
			//Shift all valid positions!...
			if (select == 2){
				int p1 = 0, lp = 0;
				while (CAN[i][p1] == false) p1++;
				t1 = sdk[i][p1];
				while (p1 < SIZE){
					lp = p1;
					p1 = p1 + 1;
					while (p1 < SIZE && CAN[i][p1] == false) p1++;
					if (p1 < SIZE){
						sdk[i][lp] = sdk[i][p1];
						lp = p1;
					}
				}
				sdk[i][lp] = t1;
			}
		}
		setHueristic();
	}
	
	int getRandomNumber(int number){
		return (int)(Math.random() * number);
	}
	
	void copyBoard(Board myBoard){
		for (int i = 0;i < SIZE;++i)
			for (int j = 0;j < SIZE;++j)
				this.sdk[i][j] = myBoard.sdk[i][j];

		setHueristic();
		myBoard.setHueristic();
	}
	
	public void print(){
		for (int i = 0;i < SIZE;++i){
			for (int j = 0;j < SIZE;++j)
				System.out.print((sdk[i][j] + 1) + " ");
			System.out.println();
		}
		System.out.println();
		
		/*for (int i = 0;i < SIZE;++i){
			for (int j = 0;j < SIZE;++j)
				System.out.print((CAN[i][j]?1:0) + " ");
			System.out.println();
		}
		System.out.println();*/
	}
	
	public boolean equalBoard(Board myBoard){
		for (int i = 0;i < SIZE;++i)
			for (int j = 0;j < SIZE;++j)
				if (myBoard.sdk[i][j] != this.sdk[i][j])
					return false;
		return true;
	}
	
}
