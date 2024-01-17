package Server;
import Verificateur.Verificateur;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.Scanner;

public class Server {
	private static ServerSocket Listener;
	private static String serverAddress = "127.0.0.1";
	private static int serverPort = 5000;
	
	//Application Serveur
	public static void main(String[] args) throws Exception {
		//Compteur incrémenté à chaque connexion d'un client
		int clientNumber = 0;
		
		//Adresse et port du serveur 
		
		serverAddress = Verificateur.askStartAddress();
		serverPort = Verificateur.askStartPort();
		
		//Création de la connexion pour communiquer avec les clients
		Listener = new ServerSocket();
		Listener.setReuseAddress(true);
		InetAddress serverIP = InetAddress.getByName(serverAddress);
		
		//Association de l'adresse et du port à la connexion
		Listener.bind(new InetSocketAddress(serverIP, serverPort));
		System.out.format("The server is running on %s:%d%n", serverAddress, serverPort);
		
		try {
			//À chaque fois qu'un nouveau client se connecte, on exécute la fonction
			//run() de l'objet ClientHandler
			while(true) {
				//Important: la fonction accept() est bloquante: on attend qu'un prochain client se connecte
				//On incrémente clientNumber à chaque nouvelle connexion
				
				new ClientHandler(Listener.accept(), clientNumber++).start();
				
			}
		}
		finally {
			//Fermeture de la connexion
			Listener.close();
		}
	}
}
