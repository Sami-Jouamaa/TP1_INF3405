package Server;
import Verificateur.Verificateur;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.*;
import java.io.File;
import java.io.FileWriter;

public class Server extends Verificateur {
	public static int queueSize = 15;
	public static ServerSocket Listener;
	public static String serverAddress = "127.0.0.1";
	public static int serverPort = 5000;
	public static Map <String, String> utilisateurs = new TreeMap<String, String>();
	public static LimitedSizeQueue<String> messages = new LimitedSizeQueue(queueSize);
	public static ArrayList<DataOutputStream> tempSocketsList = new ArrayList<DataOutputStream>();
	public static FileWriter writerUtilisateurs;
	
	//Application Serveur
	public static void main(String[] args) throws Exception {
		int clientNumber = 0;
		createFile("sauvegarde.txt");
		createFile("utilisateurs.txt");		
		
		//Compteur incrémenté à chaque connexion d'un client
		
		//messages.add("anisdfnisadngfidng");
		//messages.add("salsatidkgmjfkgmnfknhfkhnfkhnkhnfkh");
		//utilisateurs.put("Patrice",  "1234");
		
		Runtime.getRuntime().addShutdownHook(new Thread(){
				public void run()
				{
					System.out.println("Shutdown hook is running");
					try {
						FileWriter writerUtilisateurs = new FileWriter("utilisateurs.txt");
						FileWriter writerMessages = new FileWriter("sauvegarde.txt");
						
						for (String key : utilisateurs.keySet())
						{
							writerUtilisateurs.write(key + '\t' + utilisateurs.get(key) + '\n');
						}
						
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
	
	public static void createFile(String name)
	{
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
	
	public static void addToQueue(String message)
	{
		messages.add(message);
	}
	
	public static void addUserInfoToTree(String username, String password)
	{
		utilisateurs.put(username, password);
	}
}

