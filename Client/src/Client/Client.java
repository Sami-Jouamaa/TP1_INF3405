package Client;
import Verificateur.Verificateur;
import java.io.DataInputStream;
import java.util.Scanner;
import java.io.DataOutputStream;
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
		
		System.out.format("Serveur lancé sur [%s:%d]", serverAddress, serverPort);
		
		String helloMessageFromServer = in.readUTF();
		System.out.println(helloMessageFromServer);
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the message you want to send to the server:");
		String userMessage = scanner.nextLine();
			
		out.writeUTF(userMessage); //Envoi de message
			
		String formattedMessage = in.readUTF();
		System.out.println(formattedMessage);
				
		//fermeture de la connexion avec le serveur
		socket.close();
		scanner.close();
		//Création d'un canal entrant pour recevoir les messages envoyés par le serveur
		
		//Attente de la réception d'un message envoyé par le serveur sur le canal
		
		
	}
}
