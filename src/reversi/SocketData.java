package reversi;

import java.io.Serializable;

public class SocketData implements Serializable{
	public static final int COMFIRM = 1 , WAITFORCOMFIRM = 2 , DENIED = 3;
	int x;
	int y;
	int state;
	String message = "";
	
	SocketData(String message){
		this.message = message;
	}
	
	SocketData(int state){
		this.state = state;
	}
	
	SocketData(int x, int y){
		this.x = x;
		this.y = y;
	}
}
