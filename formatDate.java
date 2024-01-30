import java.time.format.DateTimeFormatter; 
import java.time.LocalDateTime;

class Server {
    public static void main(String[] args) {
        afficherMessage("Bere","127.01.01",5050,"Hello World");
    }
    
    public static String getDate(){
        LocalDateTime d = LocalDateTime.now();
        
        DateTimeFormatter formateur = DateTimeFormatter.ofPattern("dd-MM-yyyy'@'HH:mm:ss");

        String dateFormatee = d.format(formateur);

        return dateFormatee;

        //adapte de https://www.w3schools.com/java/java_date.asp
    }
    
    public static void afficherMessage(String utilisateur, String adresse, int port, String message)
    {
        String portString = String.valueOf(port);
        String date = getDate();
        String affichage = "[" + utilisateur+"-"+adresse+":"+portString+"-"+date+"]:"+message;
        System.out.println(affichage);
        
    }
}
