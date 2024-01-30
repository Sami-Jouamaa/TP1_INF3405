// Online Java Compiler
// Use this editor to write, compile and run your Java code online

import java.time.format.DateTimeFormatter; 
import java.time.LocalDateTime;

class HelloWorld {
    public static void main(String[] args) {

        
    }
    public static void afficherUtilisateur(){
        
    }
    public static void afficherDate(){
        LocalDateTime d = LocalDateTime.now();
        
        DateTimeFormatter formateur = DateTimeFormatter.ofPattern("dd-MM-yyyy'@'HH:mm:ss");

        String dateFormatee = d.format(formateur);

        System.out.println(dateFormatee);

        //adapte de https://www.w3schools.com/java/java_date.asp
    }
}