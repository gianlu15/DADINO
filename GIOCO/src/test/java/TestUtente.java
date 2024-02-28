import java.io.*;
import java.util.List;

import GestioneUtenti.Utente;

import java.util.ArrayList;

public class TestUtente {

    public static void main(String[] args) {

       
        List<Utente> utenti = new ArrayList<>();

        // FASE 4 [test]: "scarico" la lista aggiornata e visualizzo tutti gli utenti
        // presenti
        try (ObjectInputStream inputStream = new ObjectInputStream(
                new FileInputStream("src/main/resources/com/example/utenti.ser"))) {
            utenti = (List<Utente>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Visualizza gli utenti
        for (Utente utente : utenti) {
            System.out.println("Nome: " + utente.getNome() + ", ID: " + utente.getId());
        }

    }
}
