package reversi;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

public class SocketServer implements Runnable {
	private String address = "127.0.0.1";
	private int port = 8765;
	private ServerSocket server;
    ObjectOutputStream out;
    ObjectInputStream in;
    ChessBoardPanel chessBoardPanel;

	public SocketServer(ChessBoardPanel chessBoardPanel){
		this.chessBoardPanel = chessBoardPanel;
		try
		{
			server = new ServerSocket(port);
			
		}
		catch(java.io.IOException e)
		{
		}
	}
	@Override
	public void run() {
		Socket socket ;
		System.out.println("Server Started!");
		SocketData socketData;
		while(true)
		{
			socket = null;
			try
			{
				socket = server.accept();
				System.out.println("Server Accepted");
				chessBoardPanel.seconds = chessBoardPanel.counDownTime;

				in = new ObjectInputStream(socket.getInputStream());
				out = new ObjectOutputStream(socket.getOutputStream());
				
				while((socketData=(SocketData)in.readObject())!=null){
					if(!socketData.message.isEmpty()){
						chessBoardPanel.chatTextArea.setText(chessBoardPanel.chatTextArea.getText() + "\n Opponent:" + socketData.message);
					}
					else if(socketData.state != 0){
						chessBoardPanel.undo(socketData.state);
					}
					else{
					chessBoardPanel.isMyTurn = true;
					chessBoardPanel.dropChess(socketData.x, socketData.y, false);
					chessBoardPanel.seconds = chessBoardPanel.counDownTime;
					chessBoardPanel.updateCanClick();
					//System.out.println(socketData.x + " " + socketData.y);
					}
				}
			}
			catch(IOException e)
			{
				JOptionPane.showMessageDialog(null , "Connection Lost!");
				System.exit(0);
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
