package Client;

import java.io.DataInputStream;
import java.util.Scanner;
import java.io.DataOutputStream;
import java.net.Socket;

public class Client {
	private static Socket socket;
	public static void main(String[] args) throws Exception{
		//Adresse et port du serveur
		String serverAddress = "127.0.0.1";
		int port = 5000;
		
		//Cr�ation d'une nouvelle connexion avec le serveur
		socket = new Socket(serverAddress, port);
		System.out.format("Serveur lanc� sur [%s:%d]", serverAddress, port);
		
		//Cr�ation d'un canal entrant pour recevoir les messages envoy�s par le serveur
		DataInputStream in = new DataInputStream(socket.getInputStream());
		
		DataOutputStream out = new DataOutputStream(socket.getOutputStream()); //Cr�ation de canal d'envoi
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the message you want to send to the server:");
		String userMessage = scanner.nextLine();
		
		out.writeUTF(userMessage); //Envoi de message
		
		//Attente de la r�ception d'un message envoy� par le serveur sur le canal
		String helloMessageFromServer = in.readUTF();
		System.out.println(helloMessageFromServer);
		
		//fermeture de la connexion avec le serveur
		socket.close();
		scanner.close();
	}
}
