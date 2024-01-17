package Client;
import Verificateur.Verificateur;
import java.io.DataInputStream;
import java.util.Scanner;
import java.io.DataOutputStream;
import java.net.Socket;

public class Client {
	private static Socket socket;
	private static String serverAddress = "127.0.0.1";
	private static int serverPort = 5000;
	
	public static void main(String[] args) throws Exception{
		//Adresse et port du serveur

		serverAddress = Verificateur.askStartAddress();
		serverPort = Verificateur.askStartPort();
		
		//Création d'une nouvelle connexion avec le serveur
		socket = new Socket(serverAddress, port);
		System.out.format("Serveur lancé sur [%s:%d]", serverAddress, port);
		
		//Création d'un canal entrant pour recevoir les messages envoyés par le serveur
		DataInputStream in = new DataInputStream(socket.getInputStream());
		
		DataOutputStream out = new DataOutputStream(socket.getOutputStream()); //Création de canal d'envoi
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the message you want to send to the server:");
		String userMessage = scanner.nextLine();
		
		out.writeUTF(userMessage); //Envoi de message
		
		//Attente de la réception d'un message envoyé par le serveur sur le canal
		String helloMessageFromServer = in.readUTF();
		System.out.println(helloMessageFromServer);
		
		//fermeture de la connexion avec le serveur
		socket.close();
		scanner.close();
	}
}
