package reversi;

import java.awt.Color;

import javax.swing.JButton;

public class Chess {

	JButton jButton =  new JButton();
	
	static final int BLANK = 0;
	static final int BLACK = 1;
	static final int WHITE = 2;
	
	boolean blackCanClick = false;
	boolean whiteCanClick = false;
	private int state;
	Chess(){
		this.jButton =  new JButton();
		
		this.state = Chess.BLANK;
	}
	
	void dropBlack(){
		this.state = Chess.BLACK;
		this.jButton.setBackground(Color.BLACK);
	}
	void dropWhite(){
		this.state = Chess.WHITE;
		this.jButton.setBackground(Color.WHITE);

	}
	
	public int getState(){
		return state;
	}
}
