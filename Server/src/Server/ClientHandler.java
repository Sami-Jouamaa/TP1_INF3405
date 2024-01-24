package Server;
import Verificateur.Verificateur;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
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
	
	public void mapSetter(String utilisateur, String MotDePasse)
	{
		utilisateurs.put(utilisateur, MotDePasse);
	}
	
	public void run() {
		//Création de thread qui envoit un message à un client
		try {
			utilisateurs.put("patrice", "1910");
			
			DataOutputStream out = new DataOutputStream(socket.getOutputStream()); //Création de canal d'envoi
			DataInputStream in = new DataInputStream(socket.getInputStream());
			
			String userName = in.readUTF();
			String password = in.readUTF();
			
			ArrayList<String> utilisateurMdp = Verificateur.checkLoginInfo(userName, password, utilisateurs);
			utilisateurs.put(utilisateurMdp.get(0), utilisateurMdp.get(1));
			
			System.out.println(utilisateurs.get("sami"));
			
			out.writeUTF("Hello from server - you are client#" + clientNumber); //Envoi de message
			
			String messageFromClient = in.readUTF();
			
			out.writeUTF(messageFromClient);
			
			
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
}
