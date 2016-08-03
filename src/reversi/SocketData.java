package reversi;

import java.io.Serializable;

public class SocketData implements Serializable{
	int x;
	int y;
	boolean confirm;
	SocketData(int x, int y , boolean confirm){
		this.x = x;
		this.y = y;
		this.confirm = confirm;
	}
	
	SocketData(int x, int y){
		this.x = x;
		this.y = y;
	}
}
