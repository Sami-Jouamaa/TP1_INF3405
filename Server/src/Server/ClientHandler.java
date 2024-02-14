package Server;
import Verificateur.Verificateur;
import java.time.format.DateTimeFormatter; 
import java.time.LocalDateTime;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.*;

public class ClientHandler extends Thread{
	private Socket socket;
	private int clientNumber;
	private Map<String, String> utilisateurs;
	private static LimitedSizeQueue<String> messages;
	private static int queueSize = 15;
	
	public ClientHandler(Socket socket, int clientNumber) {
		this.socket = socket;
		this.clientNumber = clientNumber;
		System.out.println("New connection with client#" + clientNumber + "at" + socket);
		
		//récupération des données sauvegardées de la dernière session
		this.messages = recupererHistorique();
		Server.utilisateurs = recupererUtilisateurs();
	}
	
	public void run() {
		//Création de thread qui envoit un message à un client
		try {
			DataOutputStream out = new DataOutputStream(socket.getOutputStream()); //Création de canal d'envoi
			DataInputStream in = new DataInputStream(socket.getInputStream());//Création de canal de réception
			
			//Lecture du nom d'utilisateur et de son mot de passe envoyé par le client
			String userName = in.readUTF();
			String password = in.readUTF();
			
			//Map temporaire pour le remplacement de l'attribut utilisateurs du Serveur si l'utilisateur se connecte bien
			Map <String, String> tempMapCheckInfo = new TreeMap<String, String>();
			tempMapCheckInfo = Verificateur.checkLoginInfo(userName, password, Server.utilisateurs);
			
			if(tempMapCheckInfo.size() > 0)//Début de l'accès à la salle de clavardage pour le client
			{
				//Nouvel utilisateur apparaît alors dans les données du serveur. S'il n'y avait pas de changement, c'est la même map qui se fait "remplacer"
				Server.utilisateurs = tempMapCheckInfo;
				
				Server.dataOutputStreamsList.add(out);
				
				out.writeUTF("Hello from server - you are client#" + clientNumber + '\n'); //Envoi de message de connexion réussie
				
				afficherHistorique(out);//Affichage des 15 derniers messages
				
				//Stockage des informations du client
				String adresseClient = socket.getInetAddress().toString();
				int portClient = socket.getPort();
				String nouveauMessageFromClient = "";
				
				//Début du clavardage
				do {
					nouveauMessageFromClient = in.readUTF();
					if (nouveauMessageFromClient.equals("EXIT"))
					{
						socket.close();
						break;
					}
					//Formattage des messages avec le nom, date et adresse de l'utilisateur
					String messageFormatte = assemblerMessage(userName, adresseClient, portClient, nouveauMessageFromClient);
					broadcastMessages(messageFormatte); //Répéte le même message à tous les utilisateurs connectés
					
				}while(true);
			}
		}
		catch(IOException e){
			System.out.println("Error handling client#" + clientNumber + ":" + e);
		}
		finally {
			try {
				socket.close();
			}
			catch(IOException e) {
				System.out.println("Couldn't close a socket, what's going on?");
			}
			System.out.println("Connection with client#" + clientNumber + "closed");
		}
	}
	
	public static String getDate(){
		//Prend la date du système de l'utilisateur pour le formattage des messages. La formatte aussi
        LocalDateTime d = LocalDateTime.now();
        
        DateTimeFormatter formateur = DateTimeFormatter.ofPattern("dd-MM-yyyy'@'HH:mm:ss");//adapte de https://www.w3schools.com/java/java_date.asp
        String dateFormatee = d.format(formateur);
      
        return dateFormatee;   
    }


	public static String assemblerMessage(String utilisateur, String adresse, int port, String message)
	{
		//Formattage des messages avant l'envoi aux autres clients
	    String portString = String.valueOf(port);
	    String date = getDate();
	    String messageComplet = "[" + utilisateur+"-"+adresse+":"+portString+"-"+date+"]:"+message.substring(0, 200);
		Server.messages.add(messageComplet);
	    return messageComplet;
	}
	
	public LimitedSizeQueue<String> recupererHistorique()
	{
		//Specifier le chemin au fichier
        String filePath = "./sauvegarde.txt"; // mettre chemin du fichier où nous sauvegardons les messages
        
        LimitedSizeQueue<String> messages = new LimitedSizeQueue<>(15); //File où mettre les messages que nous lisons du fichier sauvegarde.txt
        
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
            	messages.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return messages;
	}
	
	public void afficherHistorique(DataOutputStream out)
	{
		//Regroupement de tous les messages dans une même string à envoyée au client dans un writeUTF().
		try {
			String quinzeDerniersMessages = "";
			Server.messages = recupererHistorique();
			for (int i = 0; i < Server.messages.size(); i++)
			{
				quinzeDerniersMessages += Server.messages.get(i) + '\n';
			}
			out.writeUTF(quinzeDerniersMessages);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public void broadcastMessages(String messageFormatte)
	{
		//Sur la liste de tous les outputstream des clients, on writeUTF() le message que le dernier client a envoyé (fait un broadcast, au final).
		for (DataOutputStream out: Server.dataOutputStreamsList)
		{
			try {
				out.writeUTF(messageFormatte);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Map <String, String> recupererUtilisateurs()
	{
		String filePath = "./utilisateurs.txt"; // mettre chemin du fichier où nous sauvegardons les utilisateurs et leur mot de passe
        
		//Map pour mettre les utilisateurs et leur mot de passe lus dans le fichier utilisateurs.txt
        Map <String, String> utilisateursMdp = new TreeMap<String, String>();
        
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
            	String [] lineSplit = line.split("\t", 2); //Chaque ligne est séparée par une tabulation, nom à gauche et mot de passe à droite
            	utilisateursMdp.put(lineSplit[0], lineSplit[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return utilisateursMdp;
	}
}

