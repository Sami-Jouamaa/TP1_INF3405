package Verificateur;

import java.util.concurrent.atomic.AtomicReference;
import java.util.*;

public class Verificateur {
	
	public static String askStartAddress()
	{
		boolean estValide = false;
		String adresseIPEntree;
		Scanner scanner = new Scanner(System.in);
		do
		{
			System.out.println("Entrez l'adresse IP du poste sur laquelle s'effectue le serveur:");
			adresseIPEntree = scanner.nextLine();
			
			int pointCounter = 0;
			int numberCounter = 1;
			if (adresseIPEntree.length() >= 7 || adresseIPEntree.length() <= 15)
			{
				for (int i = 0; i < adresseIPEntree.length(); i++)
				{
					if (adresseIPEntree.charAt(i) == '.')
					{
						pointCounter += 1;
						numberCounter += 1;
					}
					else if (adresseIPEntree.charAt(i) >= '0' && adresseIPEntree.charAt(i) <= '9')
					{
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
				estValide = false;
			}		
		}while(!(estValide));
		
		return adresseIPEntree;
		
	}
	
	public static int askStartPort()
	{
		int portServeur = 0;
		Scanner scanner = new Scanner(System.in);
		do {
			System.out.println("Entrez le port d'écoute du poste sur laquelle s'effectue le serveur:");
			
			
			String portServeurEntre = scanner.nextLine();
			
			portServeur = Integer.parseInt(portServeurEntre);
		}while(!(portServeur >= 5000 && portServeur <= 5050));
		
		System.out.println("Le port est correct");
		return portServeur;

	}
	
	public static ArrayList<String> askLoginInfo()
	{
		ArrayList<String> userNamePassword = new ArrayList<>();
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("Please enter your username: ");
		String userName;
		String password;
		
		userName = scanner.nextLine();
		System.out.println("Please enter your password: ");
		password = scanner.nextLine();
		
		userNamePassword.add(userName);
		userNamePassword.add(password);
		return userNamePassword;
	}
	
	public static Map<String, String> checkLoginInfo(String utilisateur, String mdp, Map<String, String> mapUtilisateurs)
	{
		for (String key: mapUtilisateurs.keySet())
		{
			System.out.println(mapUtilisateurs.get(key));
		}
		
		if(mapUtilisateurs.containsKey(utilisateur))
		{
			if(mapUtilisateurs.get(utilisateur) == mdp)
			{
				return mapUtilisateurs;
			}
			else
			{
				System.out.println("Le mot de passe est invalide.");
				Map<String, String> emptyMap = new TreeMap<String, String>();
				return emptyMap;
			}
		}
		else
		{
			mapUtilisateurs.put(utilisateur, mdp);
			return mapUtilisateurs;
		}
	}
}

