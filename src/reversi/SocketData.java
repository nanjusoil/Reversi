package reversi;

import java.io.Serializable;

public class SocketData implements Serializable{
	int x;
	int y;
	
	SocketData(int x, int y){
		this.x = x;
		this.y = y;
	}
}
