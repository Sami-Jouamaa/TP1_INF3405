package Client;

import java.io.DataInputStream;
import java.io.IOException;

public class ServerHandler extends Thread {
	private DataInputStream in;
	
	public ServerHandler(DataInputStream in)
	{
		this.in = in;
	}
	
	public void run()
	{
		while(true)
		{
			try {
				String formattedMessage = in.readUTF();
				System.out.println(formattedMessage);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
