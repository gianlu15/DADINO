import java.io.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import GestioneUtenti.Utente;


public class TestUtente {

    public static void serializza() throws JsonProcessingException {

        Utente u1 = new Utente("Mario");
        Utente u2 = new Utente("Gigi");

        String filePath = "src/main/resources/GestioneFileUtenti/utenti.json";

        File file = new File(filePath);

        ObjectMapper objectMapper = new ObjectMapper();

        String jsonUtenti = objectMapper.writeValueAsString(u2);

        //Se il file esiste, ci scriviamo sopra
        if (file.exists()) {
            System.out.println("Il file esiste già.");

            try (FileWriter fileWriter = new FileWriter(file)) {
                fileWriter.write(jsonUtenti);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {

            // Se il file non esiste, lo creiamo e ci scriviamo
            try {
                file.createNewFile();
                System.out.println("Il file è stato creato con successo.");
                try (FileWriter fileWriter = new FileWriter(file)) {
                    fileWriter.write(jsonUtenti);
                    System.out.println("Ci ho scritto sopra");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args){
        try {
            serializza();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
