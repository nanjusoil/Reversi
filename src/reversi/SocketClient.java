package reversi;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketClient implements Runnable{
	private String address = "127.0.0.1";
	private int port = 8765;
	static boolean canClick = false;
    ObjectOutputStream out;
    ObjectInputStream in;
    ChessBoardPanel chessBoardPanel;
    
	public SocketClient(ChessBoardPanel chessBoardPanel){
		Socket client = new Socket() ;
		this.chessBoardPanel = chessBoardPanel;
	    InetSocketAddress isa = new InetSocketAddress(this.address,this.port);
	    try
	    {
	    	client.connect(isa,10000);
			chessBoardPanel.seconds = chessBoardPanel.counDownTime;
	    	out = new ObjectOutputStream(client.getOutputStream());
	    	in = new ObjectInputStream(client.getInputStream());
	    	
	    }
	    catch(java.io.IOException e)
	    {
	    	e.printStackTrace();
	    }
	}
	
	@Override
	public void run() {
		SocketData socketData;
		try {
			while((socketData=(SocketData)in.readObject())!=null){
				if(socketData.x == -1 && socketData.y ==-1){
					if(socketData.confirm){
						chessBoardPanel.undo(true , 1);
					}else{
						chessBoardPanel.undo(true , 0);
					}
				}
				else{
					chessBoardPanel.isMyTurn = true;
					chessBoardPanel.checkOrFlip(socketData.x , socketData.y, true);
					chessBoardPanel.chess[socketData.x][socketData.y].dropBlack();
					chessBoardPanel.seconds = chessBoardPanel.counDownTime;
					chessBoardPanel.updateCanClick();
					System.out.println(socketData.x + " " + socketData.y);
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
