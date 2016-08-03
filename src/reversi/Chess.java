package reversi;

import java.awt.Color;
import java.util.Timer;

import javax.swing.ImageIcon;
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
	
	void dropCanClick(){
		this.jButton.setBackground(Color.lightGray);
	}
	
	void initButton(){
		this.jButton.setBackground(Color.ORANGE);
	}
	
	public int getState(){
		return state;
	}
}
