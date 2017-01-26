package genetic;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class GUI extends JFrame implements ActionListener{
	public static int height = Toolkit.getDefaultToolkit().getScreenSize().height;
    public static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	public static int H = 500, W = 650;
	public static int SIZE = Main.SIZE; 
	
	private JTextField jtf[][];
	private JButton clear, solve, generate;
	
	public GUI(){
		this.setTitle("Suduko, enjoy it!");
		this.setSize(W, H);
		this.setLocation((width - W) / 2, (height - H) / 2);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLayout(null);
		
		jtf = new JTextField[SIZE][SIZE];
		for (int i = 0;i < SIZE;++i){
			for (int j = 0;j < SIZE;++j){
				jtf[i][j] = new JTextField();
				Font font = new Font(jtf[i][j].getFont().getName(), Font.BOLD, 52);
				jtf[i][j].setFont(font);
				jtf[i][j].setEditable(true);
				jtf[i][j].setSize(50, 50);
				jtf[i][j].setLocation(i * 50, j * 50);
				jtf[i][j].setForeground(Color.BLUE);
				this.add(jtf[i][j]);
			}
		}
		
		clear = new JButton();
		clear.setText("Clear Table!");
		clear.setSize(140, 50);
		clear.setLocation(475, 50);
		clear.addActionListener(this);
		this.add(clear);
		
		
		solve = new JButton();
		solve.setText("Solve Table!");
		solve.setSize(140, 50);
		solve.setLocation(475, 200);
		solve.addActionListener(this);
		this.add(solve);
		
		generate = new JButton();
		generate.setText("Generate A Suduko!");
		generate.setSize(140, 50);
		generate.setLocation(475, 350);
		generate.addActionListener(this);
		this.add(generate);
		
		this.setVisible(true);
	}
	
	private void setAll(boolean state){
		generate.setEnabled(state);
		solve.setEnabled(state);
		clear.setEnabled(state);
		for (int i = 0;i < SIZE;++i)
			for (int j = 0;j < SIZE;++j){
				jtf[i][j].setEditable(state);
				jtf[i][j].setForeground(Color.BLUE);
			}

			
	}
	
	private void clearAll(){
		for (int i = 0;i < SIZE;++i)
			for (int j = 0;j < SIZE;++j){
				jtf[i][j].setText("");
				jtf[i][j].setForeground(Color.BLUE);
			}
	}
	
	private Board solveTable(){
		for (int i = 0;i < SIZE;++i)
			for (int j = 0;j < SIZE;++j)
				Board.CAN[i][j] = true;
		
		int input[][] = new int[SIZE][SIZE];
		boolean err = false;
		for (int i = 0;i < SIZE;++i){
			for (int j = 0;j < SIZE;++j){
				String str = jtf[i][j].getText();
				int temp = 0;
				for (int k = 0;k < str.length();++k)
					if (str.charAt(k) > '9' || str.charAt(k) < '0')
						temp = -2;
				if (str.length() > 0 && temp == 0)
					temp = Integer.parseInt(jtf[i][j].getText());
				input[i][j] = temp - 1;
				if (input[i][j] > 8 || input[i][j] < -1) err = true;			  
				else if (input[i][j] != -1) Board.CAN[i][j] = false;
			}
		}
		if (err){
			JOptionPane.showMessageDialog(this, "Please Correct your input", "Error!", JOptionPane.ERROR_MESSAGE);
			return null;
		} else {
			long before = System.currentTimeMillis();
			Main.myGen.readyForSolve(input);
			Board res = Main.myGen.geneticResult();
			if (res != null){
				res.print();
				long after = System.currentTimeMillis();
				System.out.println("Time: " + (after - before) + " ms");
				return res;
			} else {
				System.out.println("I can't find the Suduko :(");
				return null;
			}
		}
	}
	
	private Board generateTable(){
		for (int i = 0;i < SIZE;++i)
			for (int j = 0;j < SIZE;++j)
				Board.CAN[i][j] = true;
		
		long before = System.currentTimeMillis();
		Main.myGen.readyForGenerate();
		Board res = Main.myGen.geneticResult();
		if (res != null){
			res.print();
			long after = System.currentTimeMillis();
			System.out.println("Time: " + (after - before) + " ms");
			return res;
		} else {
			System.out.println("I can't find the Suduko :(");
			return null;
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == clear)	clearAll();
		else {
			Board b = null;
			if (e.getSource() == solve){
				setAll(false);
				b = solveTable();
				setAll(true);
			}
			if (e.getSource() == generate){
				setAll(false);
				b = generateTable();
				setAll(true);
			}
			if (b != null){
				for (int i = 0;i < SIZE;++i){
					for (int j = 0;j < SIZE;++j){
						jtf[i][j].setText(Integer.toString(b.sdk[i][j] + 1));
						if (Board.CAN[i][j] == false)
							jtf[i][j].setForeground(Color.RED);
						else jtf[i][j].setForeground(Color.BLUE);
					}
				}
			} else {
				JOptionPane.showMessageDialog(this, "I can't find any suduko :(", "Shame!", JOptionPane.NO_OPTION);
			}
		}
	}
}
