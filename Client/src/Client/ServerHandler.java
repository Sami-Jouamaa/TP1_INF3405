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
		//Chaque client imprime les messages de tous les autres clients au fur et à mesure qu'ils en écrivent
		while(true)
		{
			try {
				String formattedMessage = in.readUTF();
				System.out.println(formattedMessage);
			} catch (IOException e) {
				continue;
			}
		}
	}
}
