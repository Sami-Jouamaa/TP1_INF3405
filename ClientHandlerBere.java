package Server;
import Verificateur.Verificateur;
import java.time.format.DateTimeFormatter; 
import java.time.LocalDateTime;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ClientHandler extends Thread{
	private Socket socket;
	private int clientNumber;
	private Map<String, String> utilisateurs;
	private int tailleHistorique;
	private Queue<String> historiqueMessages; 
	public ClientHandler(Socket socket, int clientNumber) {
		this.socket = socket;
		this.clientNumber = clientNumber;
		this.utilisateurs = new TreeMap<String, String>();
		this.tailleHistorique = 15;
		this.historiqueMessages = recupererHistorique();
		System.out.println("New connection with client#" + clientNumber + "at" + socket);
		
	}
	
	public void run() {
		//Création de thread qui envoit un message à un client
		try {
			DataOutputStream out = new DataOutputStream(socket.getOutputStream()); //Création de canal d'envoi
			DataInputStream in = new DataInputStream(socket.getInputStream());
			
			String userName = in.readUTF();
			String password = in.readUTF();
			Server.sauvegarde(userName+"\t"+password, Server.writerUtilisateurs);
			
			utilisateurs.put(userName, password);
			
			out.writeUTF("Hello from server - you are client#" + clientNumber); //Envoi de message
			
			//AFFICHER ICI L'HISTORIQUE DES MESSAGES
			afficherHistorique(out, this.historiqueMessages);
			
			
			String adresseClient = socket.getInetAddress().toString();
			String messageFromClient = in.readUTF();
			int portClient = socket.getPort();
			
			out.writeUTF(assemblerMessage(userName, adresseClient, portClient, messageFromClient));
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
        LocalDateTime d = LocalDateTime.now();
        
        DateTimeFormatter formateur = DateTimeFormatter.ofPattern("dd-MM-yyyy'@'HH:mm:ss");

        String dateFormatee = d.format(formateur);

        return dateFormatee;

        //adapte de https://www.w3schools.com/java/java_date.asp
    }


	public static String assemblerMessage(String utilisateur, String adresse, int port, String message)
	{
	    String portString = String.valueOf(port);
	    String date = getDate();
	    String messageComplet = "[" + utilisateur+"-"+adresse+":"+portString+"-"+date+"]:"+message;
		historiqueMessages.add(messageComplet);
	    return messageComplet;
	}
	public static void afficherHistorique(DataOutputStream out, SizeLimitedQueue<String> historique)
	{
		for(i=0;i<historique.size();i++)
		{
			out.writeUTF(historique[i]);
		}
	}
	public static SizeLimitedQueue<String> recupererHistorique()
	{
		///LIGNE POUR LIRE DANS UN FICHIER
	}

	public static class SizeLimitedQueue<E> //classe reprise de https://www.geeksforgeeks.org/how-to-implement-size-limited-queue-that-holds-last-n-elements-in-java/
    extends LinkedList<E> { 
  
		// Variable which store the 
		// SizeLimitOfQueue of the queue 
		private int SizeLimitOfQueue; 
	  
		// Constructor method for initializing 
		// the SizeLimitOfQueue variable 
		public SizeLimitedQueue(int SizeLimitOfQueue) 
		{ 
			this.SizeLimitOfQueue = SizeLimitOfQueue; 
		} 
	  
		// Override the method add() available 
		// in LinkedList class so that it allow 
		// addition  of element in queue till 
		// queue size is less than 
		// SizeLimitOfQueue otherwise it remove 
		// the front element of queue and add 
		// new element 
		@Override
		public boolean add(E o) 
		{ 
			while (this.size() == SizeLimitOfQueue) { 
				super.remove(); 
			} 
			super.add(o); 
			return true; 
		} 
}