package Client;
import Verificateur.Verificateur;
import java.io.DataInputStream;
import java.util.Scanner;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Client extends Verificateur {
	private static Socket socket;
	private static String serverAddress = "127.0.0.1";
	private static int serverPort = 5000;
	private static ArrayList<String> infoConnections = new ArrayList<>();
	
	public static void main(String[] args) throws Exception{
		//Adresse et port du serveur

		serverAddress = Verificateur.askStartAddress();
		serverPort = Verificateur.askStartPort();
		socket = new Socket(serverAddress, serverPort);
		
		infoConnections = Verificateur.askLoginInfo();
		
		DataOutputStream out = new DataOutputStream(socket.getOutputStream()); //Création de canal d'envoi
		DataInputStream in = new DataInputStream(socket.getInputStream());
		
		out.writeUTF(infoConnections.get(0));
		out.writeUTF(infoConnections.get(1));
		
		//Création d'une nouvelle connexion avec le serveur
		
		System.out.format("Serveur lancé sur [%s:%d] \n", serverAddress, serverPort);
		
		String helloMessageFromServer = in.readUTF();
		System.out.println(helloMessageFromServer);
		
		Scanner scanner = new Scanner(System.in);
		
		System.out.println(in.readUTF());
		//do {
			//tempMessage = in.readUTF();
			//System.out.println(tempMessage);
		//}while(tempMessage != "");
		
		System.out.println("Type EXIT to leave the chatroom");
		System.out.println("Enter the message you want to send to the server: \n");
		String userMessage = "";
		
		new ServerHandler(in).start();
		
		while (true)
		{
			try
			{
				userMessage = scanner.nextLine();
				if (userMessage.equals("EXIT"))
				{
					socket.close();
					scanner.close();
					System.exit(0);
					break;
				}
				else
				{
					out.writeUTF(userMessage); //Envoi de message

				}
			}
			catch(IOException e)
			{
				System.out.println("Connection terminated");
			}
		}
	}
}
