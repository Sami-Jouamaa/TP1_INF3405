package Server;
import Verificateur.Verificateur;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.*;
import java.io.File;
import java.io.FileWriter;

public class Server extends Verificateur {
	public static int queueSize = 15;
	public static ServerSocket Listener;
	public static String serverAddress ;
	public static int serverPort;
	public static Map <String, String> utilisateurs = new TreeMap<String, String>();
	public static LimitedSizeQueue<String> messages = new LimitedSizeQueue(queueSize);
	public static ArrayList<DataOutputStream> dataOutputStreamsList = new ArrayList<DataOutputStream>();
	public static FileWriter writerUtilisateurs;
	
	//Application Serveur
	public static void main(String[] args) throws Exception {
		int clientNumber = 0;
		//Création des fichiers de sauvegarde s'ils n'existent pas
		createFile("sauvegarde.txt");
		createFile("utilisateurs.txt");		
		
		//Morceaux de code exécuté à la fermeture du serveur
		Runtime.getRuntime().addShutdownHook(new Thread(){
				public void run()
				{
					System.out.println("Shutdown hook is running");
					try {
						FileWriter writerUtilisateurs = new FileWriter("utilisateurs.txt");
						FileWriter writerMessages = new FileWriter("sauvegarde.txt");
						
						//Enregistrement des utilisateurs
						for (String key : utilisateurs.keySet())
						{
							writerUtilisateurs.write(key + '\t' + utilisateurs.get(key) + '\n');
						}
						
						//Enregistrement des messages
						for (int i = 0; i < messages.size(); i++) 
						{
							writerMessages.write(messages.get(i) + '\n');
						}
						
						writerUtilisateurs.close();
						writerMessages.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			});
		
		//Demande et vérification adresse et port du serveur 
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
	
	
	public static void createFile(String name)
	{
		//Méthode qui crée un fichier, à partir du nom entré, s'il n'existe pas
		try
		{
			File f = new File(name);
			if (f.createNewFile())
			{
				System.out.println("File created");
			}
			else
			{
				System.out.println("File not created");
			}
		}
		catch(IOException e)
		{
			System.out.println("Error occurred");
		}
		
	}
}

