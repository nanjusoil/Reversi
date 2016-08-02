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
	public SocketClient(){
		Socket client = new Socket() ;
	    InetSocketAddress isa = new InetSocketAddress(this.address,this.port);
	    try
	    {
	    	client.connect(isa,10000);
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
		
	}
}
