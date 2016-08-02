package reversi;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer implements Runnable {
	private String address = "127.0.0.1";
	private int port = 8765;
	private ServerSocket server;
    ObjectOutputStream out;
    ObjectInputStream in;

	public SocketServer(){
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
		//Data data;
		while(true)
		{
			socket = null;
			try
			{
				socket = server.accept();
				System.out.println("Server Accepted");

				in = new ObjectInputStream(socket.getInputStream());
				out = new ObjectOutputStream(socket.getOutputStream());
				/*
				while((data=(Data)in.readObject())!=null){
					this.canClick = true;
					chesspanel.chess[data.getX()/100][data.getY()/100] = 1;
					chesspanel.repaint();
					System.out.println(""+data.getX());
					System.out.println(""+data.getY());
				}*/
			}
			catch(java.io.IOException e)
			{
			}
		}
	}
}
