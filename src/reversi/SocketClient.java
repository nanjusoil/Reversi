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
				if(!socketData.message.isEmpty()){
					chessBoardPanel.chatTextArea.setText(chessBoardPanel.chatTextArea.getText() + "\n" + socketData.message);
				}
				else if(socketData.state != 0){
					chessBoardPanel.undo(socketData.state);
				}
				else{
					chessBoardPanel.isMyTurn = true;
					chessBoardPanel.dropChess(socketData.x, socketData.y, true);
					chessBoardPanel.seconds = chessBoardPanel.counDownTime;
					chessBoardPanel.updateCanClick();
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
