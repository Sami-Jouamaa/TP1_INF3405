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
	
	public ClientHandler(Socket socket, int clientNumber) {
		this.socket = socket;
		this.clientNumber = clientNumber;
		this.utilisateurs = new TreeMap<String, String>();
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
			
			String adresseClient = socket.getInetAddress().toString();
			String messageFromClient = in.readUTF();
			int portClient = socket.getPort();
			
			out.writeUTF(afficherMessage(userName, adresseClient, portClient, messageFromClient));
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


	public static String afficherMessage(String utilisateur, String adresse, int port, String message)
	{
	    String portString = String.valueOf(port);
	    String date = getDate();
	    String affichage = "[" + utilisateur+"-"+adresse+":"+portString+"-"+date+"]:"+message;
	    return affichage;
	    
	}
}

//public class LimitedSizeQueueExample {
//  public static void main(String[] args) {
    //int maxSize = 15;

        // Creating a limited-size queue using LinkedList
        //Queue<String> limitedQueue = new LimitedSizeQueue<>(maxSize);

        // Adding elements to the queue
        //limitedQueue.add("Element 1");
        //limitedQueue.add("Element 2");
        // Add more elements as needed

        // Printing the elements in the queue
        //System.out.println("Queue elements: " + limitedQueue);
    //}
//}
