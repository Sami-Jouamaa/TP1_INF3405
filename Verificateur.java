package Verificateur;
import java.util.*;

public class Verificateur {
	public static String askStartAddress()
	{
		//Vérification de l'adresse aussi, que le format soit bon et qu'il n'y ait pas de symboles autres que des chiffres
		boolean estValide = false;
		String adresseIPEntree;
		Scanner scanner = new Scanner(System.in);
		do
		{
			System.out.println("Entrez l'adresse IP du poste sur laquelle s'effectue le serveur:");
			adresseIPEntree = scanner.nextLine();
			
			boolean alternancePointNumber = false;
			int pointCounter = 0;
			int numberCounter = 1;
			if (adresseIPEntree.length() >= 7 || adresseIPEntree.length() <= 15)
			{
				for (int i = 0; i < adresseIPEntree.length(); i++)
				{
					if (adresseIPEntree.charAt(i) == '.')
					{
						if (alternancePointNumber)
						{
							pointCounter += 1;
							numberCounter += 1;
							alternancePointNumber = !(alternancePointNumber);
						}
					}
					else if (adresseIPEntree.charAt(i) >= '0' && adresseIPEntree.charAt(i) <= '9')
					{
						alternancePointNumber = true;
						continue;
					}
					else
					{
						System.out.println("L'adresse IP contient des symboles non valides.");
						estValide = false;
					}
				}
			}
			else
			{
				System.out.println("La longueur de l'adresse IP n'est pas valide.");
				estValide = false;
			}
			
			if (pointCounter == 3 && numberCounter == 4)
			{
				System.out.println("L'adresse IP est valide.");
				estValide = true;
			}
			else
			{
				System.out.println("L'adresse IP n'est pas valide.");
				System.out.println(pointCounter + "" + numberCounter + '\n');
				estValide = false;
			}		
		}while(!(estValide));
		
		return adresseIPEntree;
	}
	
	public static int askStartPort()
	{
		//Vérification du numéro de port pour qu'il soit entre 5000 et 5050 et qu'il n'ait pas de symboles qui ne sont pas des chiffres
		boolean portEstValide = false;
		int portServeur = 0;
		Scanner scanner = new Scanner(System.in);
		String portServeurEntre;
		
		do {
			System.out.println("Entrez le port d'écoute du poste sur laquelle s'effectue le serveur:");
			
			portServeurEntre = scanner.nextLine();
			
			if (portServeurEntre.charAt(0) == '5' && portServeurEntre.charAt(1) == '0' && portServeurEntre.length() == 4)
			{
				if (portServeurEntre.charAt(2) == '5')
				{
					if (portServeurEntre.charAt(3) == '0')
					{
						portEstValide = true;
					}
				}
				else if (portServeurEntre.charAt(2) >= '0' && portServeurEntre.charAt(2) <= '4')
				{
					if (portServeurEntre.charAt(3) >= '0' && portServeurEntre.charAt(3) <= '9')
					{
						portEstValide = true;
					}
				}
			}
			else
			{
				System.out.println("Écrivez un nombre entre 5000 et 5050");
				portEstValide = false;
			}	
		}while(!portEstValide);
		portServeur = Integer.parseInt(portServeurEntre); //Transformation d'une string en un int pour l'attribut serverPort du client si le port est correct
		scanner.close();
		return portServeur;
	}
	
	public static ArrayList<String> askLoginInfo()
	{
		//Pas de vérification, seulement la collecte des informations d'un client
		ArrayList<String> userNamePassword = new ArrayList<>();//ArrayList que le Clienthandler recevra
		String userName;
		String password;
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Please enter your username: ");
		userName = scanner.nextLine();
		
		System.out.println("Please enter your password: ");
		password = scanner.nextLine();
		
		//Ajout à l'arrayList pour un utilisateur qui sera ajouté à la map du serveur si la connexion est bien établie
		userNamePassword.add(userName);
		userNamePassword.add(password);
		return userNamePassword;
	}
	
	public static Map<String, String> checkLoginInfo(String utilisateur, String mdp, Map<String, String> mapUtilisateurs)
	{	
		//Si le nom d'utilisateur n'existe pas, on l'ajoute à la map de serveur
		//Si le nom d'utilisateur existe mais le mot de passe est mauvais, on imprime un message et on renvoie une map vide
		if(mapUtilisateurs.containsKey(utilisateur))
		{
			if(mapUtilisateurs.get(utilisateur).equals(mdp))
			{
				return mapUtilisateurs;//bon mot de passe entré
			}
			else
			{
				System.out.println("Le mot de passe est invalide.");
				Map<String, String> emptyMap = new TreeMap<String, String>();
				return emptyMap;
			}
		}
		else//nouvel utilisateur ajouté
		{
			mapUtilisateurs.put(utilisateur, mdp);
			return mapUtilisateurs;
		}
	}
}
