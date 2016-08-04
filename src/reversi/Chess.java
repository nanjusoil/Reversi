package reversi;

import java.awt.Color;
import java.io.Serializable;
import java.util.Timer;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Chess implements Cloneable , Serializable{

	JButton jButton =  new JButton();
	
	static final int BLANK = 0;
	static final int BLACK = 1;
	static final int WHITE = 2;
	
	boolean blackCanClick = false;
	boolean whiteCanClick = false;
	private int state;
	@Override
	 public Object clone() {
	  try {
			return super.clone();
	} catch (CloneNotSupportedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  		return this;
	}
	 
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

	public void setState(int state){
		this.state = state;
	}
}
