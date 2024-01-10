package server;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class Server {
	private static ServerSocket Listener;
	
	//Application Serveur
	public static void main(String[] args) throws Exception {
		//Compteur incr�ment� � chaque connexion d'un client
		int clientNumber = 0;
		
		//Adresse et port du serveur 
		String serverAddress = "127.0.0.1";
		int serverPort = 5000;
		
		//Cr�ation de la connexion pour communiquer avec les clients
		Listener = new ServerSocket();
		Listener.setReuseAddress(true);
		InetAddress serverIP = InetAddress.getByName(serverAddress);
		
		//Association de l'adresse et du port � la connexion
		Listener.bind(new InetSocketAddress(serverIP, serverPort));
		System.out.format("The server is running on %s:%d%n", serverAddress, serverPort);
		
		try {
			//� chaque fois qu'un nouveau client se connecte, on ex�cute la fonction
			//run() de l'objet ClientHandler
			while(true) {
				//Important: la fonction accept() est bloquante: on attend qu'un prochain client se connecte
				//On incr�mente clientNumber � chaque nouvelle connexion
				
				new ClientHandler(Listener.accept(), clientNumber++).start();
			}
		}
		finally {
			//Fermeture de la connexion
			Listener.close();
		}
	}
}
