package genetic;

public class Genetic {
	public static boolean DEBUG = false;
	public static boolean FAST = true;
	
	public static double PC = 0.8;
	public static double PM = 0.2;
	
	static int PARENTS = 10;
	static int OFFSPRING = 10;
	static int STEPS = 1000000;
	
	private Board parent[], child[];
	
	public Genetic() {
		// TODO Auto-generated constructor stub
		parent = new Board[PARENTS];
		child = new Board[OFFSPRING];		
	}
	
	int getRandomNumber(int number){
		return (int)(Math.random() * number);
	}
	
	void crossOver3(int idx, int a, int b, int c){
		if (DEBUG) System.out.println("Crossover 3!");
		for (int i = 0;i < Main.SIZE;++i){
			int wh = getRandomNumber(3);
			for (int j = 0;j < Main.SIZE;j++){
				int temp = 0;
				if (wh == 0) temp = parent[a].sdk[i][j];
				if (wh == 1) temp = parent[b].sdk[i][j];
				else temp = parent[c].sdk[i][j];
				
				child[idx].sdk[i][j] = temp;
			}
		}
		child[idx].setHueristic();
	}
	
	void crossOverAll(int idx){
		if (DEBUG) System.out.println("Crossover All!");
		for (int i = 0;i < Main.SIZE;++i){
			int wh = getRandomNumber(PARENTS);
			for (int j = 0;j < Main.SIZE;j++){
				child[idx].sdk[i][j] = parent[wh].sdk[i][j];
			}
		}
		child[idx].setHueristic();
	}
	
	public void readyForGenerate(){
		for (int i = 0;i < PARENTS ;++i){
			parent[i] = new Board();
			parent[i].setGenerateMode();
		}
	}
	
	public void readyForSolve(int [][] input){
		for (int i = 0;i < PARENTS ;++i){
			parent[i] = new Board();
			parent[i].setBoard(input);
			parent[i].setSolveMode();
		}
	}
	
	public Board geneticResult(){
		//Make and Sort Parents...
		if (FAST == false) System.out.println("Initializing...");
		for (int i = 0;i < PARENTS ;++i){
			for (int j = i;j > 0;--j){
				if (parent[j].getHuerisitic() < parent[j - 1].getHuerisitic()){
					swapBoards(parent[j - 1], parent[j]);
				}
			}
		}
		
		for (int i = 0;i < PARENTS ;++i){
			if (FAST == false) System.out.println(parent[i].getHuerisitic());
			if (DEBUG) parent[i].print();
		}
		
		for (int i = 0;i < OFFSPRING ;++i)
			child[i] = new Board();
		if (FAST == false) System.out.println("Ended.");
		
		//Genetic...
		for (int st = 0;st < STEPS;++st){
			if (FAST == false) System.out.println("Step " + st + " started...");
			for (int i = 0;i < OFFSPRING;i++){
				
				int a = getRandomNumber(PARENTS), 
					b = getRandomNumber(PARENTS),
					c = getRandomNumber(PARENTS);
				
				double temp = Math.random();
				if (temp > PC){
					child[i].copyBoard(parent[a]);
					continue;
				}
				
				if (PARENTS < 3){
					child[i].copyBoard(parent[b]);
					continue;
				}
				
				while (a == b) b = getRandomNumber(PARENTS);
				while (c == a || c == b) c = getRandomNumber(PARENTS);
				
				//Changeable Parameter...
				if (Math.random() > 0.3)
					crossOver3(i, a, b, c);
				else crossOverAll(i);
			}
			
			for (int i = 0;i < OFFSPRING;i++){
				child[i].mutation();
				/*boolean rep = false;
				for (int j = 0;j < i;++j){
					if (child[i].equalBoard(child[j])){
						rep = true;
						break;
					}
				}
				if (rep == true){
					i--;
					PM += 0.005;
					if (PM > 0.8) PM = 0.8;
					continue;
				}*/
			}
			
			
			if (FAST == false) System.out.println("Parents: ");
			for (int i = 0;i < PARENTS;++i){
				if (FAST == false) System.out.println(parent[i].getHuerisitic() + " ");
				if (DEBUG) parent[i].print();
			}
			if (FAST == false) System.out.println("Childs: ");
			for (int i = 0;i < OFFSPRING;++i){
				if (FAST == false)System.out.println(child[i].getHuerisitic() + " ");
				if (DEBUG) child[i].print();
			}
			
			//Select bests from parents and child...
			for (int i = 0;i < OFFSPRING;++i){
				int lastParent = PARENTS - 1;
				if (child[i].getHuerisitic() < parent[lastParent].getHuerisitic()){
					parent[lastParent].copyBoard(child[i]);
					for (int j = lastParent;j > 0;--j){
						if (parent[j].getHuerisitic() < parent[j - 1].getHuerisitic()){
							swapBoards(parent[j - 1], parent[j]);
						}
					}
				}
			}
			
			if (parent[0].getHuerisitic() == 0){
				System.out.println("Solved after " + st + " steps!");
				return parent[0];
			}
			
			if (st % 50000 == 0){
				System.out.println("Step " + st + " done!");
				System.out.println("Best parent heurisitic: " + parent[0].getHuerisitic());
				System.out.println("Worst parent heurisitic: " + parent[PARENTS - 1].getHuerisitic());
			}
			//Increase the PM
			if (st % 100000 == 0) 
				PM += 0.05;
			
			if (parent[PARENTS - 1].getHuerisitic() < 5){
				for (int i = 1;i < PARENTS;++i)
					parent[i].setSolveMode();
				for (int i = 1;i < PARENTS ;++i){
					for (int j = i;j > 0;--j){
						if (parent[j].getHuerisitic() < parent[j - 1].getHuerisitic()){
							swapBoards(parent[j - 1], parent[j]);
						}
					}
				}
			}
		}
		return null;
	}
	
	void swapBoards(Board a, Board b){
		int temp[][] = new int[Main.SIZE][Main.SIZE];
		for (int i = 0;i < Main.SIZE;++i)
			for (int j = 0;j < Main.SIZE;++j)
				temp[i][j] = a.sdk[i][j];
		
		a.copyBoard(b);
		b.setBoard(temp);
	}
}
