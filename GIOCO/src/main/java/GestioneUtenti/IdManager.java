package GestioneUtenti;

import java.io.*;

public class IdManager {

    private static final String FILE_PATH = "src/main/resources/GestioneFileUtenti/ultimoId.txt";
    private static int lastUserId = 0;

    static {
        // Carica l'ID più recente dal file all'avvio del programma
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            lastUserId = Integer.parseInt(reader.readLine());
        } catch (FileNotFoundException e) {
            // Se il file non esiste, crea un nuovo file e inizializza l'ID a 1
            System.out.println("Il file dell'ultimo ID non esiste. Creazione in corso...");
            try {
                File file = new File(FILE_PATH);
                if (file.createNewFile()) {
                    System.out.println("Il file dell'ultimo ID è stato creato con successo.");
                    lastUserId = 0; // Inizializza l'ID a 1
                } else {
                    System.out.println("Impossibile creare il file dell'ultimo ID.");
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public static int getProssimoId() {
        return ++lastUserId;
    }

    public static void salvaProssimoId() {
        // Salva l'ID più recente sul file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write(String.valueOf(lastUserId));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
