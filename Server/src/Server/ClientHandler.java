package Server;
import Verificateur.Verificateur;
import java.time.format.DateTimeFormatter; 
import java.time.LocalDateTime;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class ClientHandler extends Thread{
	private Socket socket;
	private int clientNumber;
	private Map<String, String> utilisateurs;
	private static LimitedSizeQueue<String> messages;
	private static int queueSize = 15;
	
	public ClientHandler(Socket socket, int clientNumber) {
		this.socket = socket;
		this.clientNumber = clientNumber;
		this.utilisateurs = new TreeMap<String, String>();
		this.messages = recupererHistorique();
		System.out.println("New connection with client#" + clientNumber + "at" + socket);
	}
	
	public void run() {
		//Création de thread qui envoit un message à un client
		try {
			DataOutputStream out = new DataOutputStream(socket.getOutputStream()); //Création de canal d'envoi
			DataInputStream in = new DataInputStream(socket.getInputStream());
			
			
			String userName = in.readUTF();
			String password = in.readUTF();
			System.out.println(password);
			
			Map <String, String> tempMapCheckInfo = new TreeMap<String, String>();
			tempMapCheckInfo = Verificateur.checkLoginInfo(userName, password, utilisateurs);
			
			if(tempMapCheckInfo.size() > 0)
			{
				utilisateurs = tempMapCheckInfo;
				
				System.out.println(Verificateur.checkLoginInfo(userName, password, utilisateurs));
				Server.addUserInfoToTree(userName, password);
				
				out.writeUTF("Hello from server - you are client#" + clientNumber); //Envoi de message
				//out.writeUTF();
				
				String adresseClient = socket.getInetAddress().toString();
				String messageFromClient = in.readUTF();
				int portClient = socket.getPort();
				
				out.writeUTF(assemblerMessage(userName, adresseClient, portClient, messageFromClient));
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
        LocalDateTime d = LocalDateTime.now();
        
        DateTimeFormatter formateur = DateTimeFormatter.ofPattern("dd-MM-yyyy'@'HH:mm:ss");
        String dateFormatee = d.format(formateur);
      //adapte de https://www.w3schools.com/java/java_date.asp
        return dateFormatee;   
    }


	public static String assemblerMessage(String utilisateur, String adresse, int port, String message)
	{
	    String portString = String.valueOf(port);
	    String date = getDate();
	    String messageComplet = "[" + utilisateur+"-"+adresse+":"+portString+"-"+date+"]:"+message;
		Server.addToQueue(messageComplet);
	    return messageComplet;
	}
	
	public LimitedSizeQueue<String> recupererHistorique()
	{
		  // Specifier le chemin au fichier
        String filePath = "./sauvegarde.txt"; // mettre chemin du fichier
        
        LimitedSizeQueue<String> messages = new LimitedSizeQueue<>(15);
        
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
}


