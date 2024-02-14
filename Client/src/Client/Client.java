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
		//On demande l'adresse et le port du serveur, ensuite, on les vérifie. Après la vérification, on connecte le client
		serverAddress = Verificateur.askStartAddress();
		serverPort = Verificateur.askStartPort();
		socket = new Socket(serverAddress, serverPort);
		
		//Récupère le nom d'utilisateur et le mot de passe dans une arrayList de string
		infoConnections = Verificateur.askLoginInfo();
		
		DataOutputStream out = new DataOutputStream(socket.getOutputStream()); //Création de canal d'envoi
		DataInputStream in = new DataInputStream(socket.getInputStream());
		
		//Envoi des informations de l'utilisateur au Serveur pour qu'il nous laisse nous connecter à la salle de clavardage
		out.writeUTF(infoConnections.get(0));
		out.writeUTF(infoConnections.get(1));
		
		//Création d'une nouvelle connexion avec le serveur
		System.out.format("Serveur lancé sur [%s:%d] \n", serverAddress, serverPort);
		
		//Message de bienvenue du serveur
		String helloMessageFromServer = in.readUTF();
		System.out.println(helloMessageFromServer);
		
		System.out.println(in.readUTF());
		
		System.out.println("Type EXIT to leave the chatroom");
		System.out.println("Enter the message you want to send to the server: \n");
		
		String userMessage = "";
		Scanner scanner = new Scanner(System.in);
		
		//Instanciation d'un objet pour la récupération des messages des autres utilisateurs
		new ServerHandler(in).start();
		
		while (true)
		{
			try
			{
				userMessage = scanner.nextLine();
				if (userMessage.equals("EXIT"))
				{
					///Fermeture de la connexion du client au serveur
					socket.close();
					scanner.close();
					System.exit(0);
					break;
				}
				else
				{
					out.writeUTF(userMessage); //Envoi de message en boucle tant que le client en écrit
				}
			}
			catch(IOException e)
			{
				System.out.println("Connection terminated");
			}
		}
	}
}
