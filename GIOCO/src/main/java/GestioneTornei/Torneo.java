package GestioneTornei;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import GestioneGioco.Bot;
import GestioneGioco.Giocatore;
import GestionePartite.Partita;
import GestioneUtenti.Utente;

public class Torneo {

    public enum Stato {
        Pronto, Sospeso, Terminato;
    }

    private String nomeTorneo;
    private int numGiocatori;
    private int codice;
    private Stato statoTorneo;
    private Partita[] partite;
    private Partita daGiocare;
    private ArrayList<String> accessi;
    private ArrayList<Giocatore> partecipanti;
    private ArrayList<Giocatore> vincenti;
    private ArrayList<Giocatore> perdenti;

    private List<Giocatore> giocatoriDaFile;
    private File fileGiocatori;
    private ObjectMapper objectMapper;

    public Torneo(String nomeTorneo, int numGiocatori, int codice) {
        this.nomeTorneo = nomeTorneo;
        this.numGiocatori = numGiocatori;
        this.codice = codice;

        this.accessi = new ArrayList<>();
        this.partecipanti = new ArrayList<>();
        this.vincenti = new ArrayList<>();
        this.perdenti = new ArrayList<>();
        this.statoTorneo = Stato.Pronto;

        this.giocatoriDaFile = new ArrayList<>();
        this.fileGiocatori = new File("src/main/resources/FileJson/giocatori.json");
        this.objectMapper = new ObjectMapper();

        if (numGiocatori == 4 || numGiocatori == 6 || numGiocatori == 8)
            partite = new Partita[3];
        else
            partite = new Partita[7];
    }

    public Torneo() {
    }

    public void aggiungiGiocatore(Utente u) {
        scaricaGiocatoriDaFile();

        boolean trovato = false;

        for (Giocatore g : giocatoriDaFile) {
            if (g.getNome().equals(u.getNome())) {
                partecipanti.add(g);
                accessi.add(g.getNome());
                trovato = true;
                break;
            }
        }

        if (!trovato) {
            Giocatore g = new Giocatore(u.getNome());
            partecipanti.add(g);
            giocatoriDaFile.add(g);
            accessi.add(g.getNome());
            caricaGiocatoriSuFile();
        }
    }

    public void aggiungiBot(int numeroBot) {
        Bot b = new Bot("Bot-" + numeroBot);
        partecipanti.add(b);
    }

    public void sorteggiaPartite() {
        Random random = new Random();

        if (partite.length == 3) {
            for (int i = 0; i < partite.length - 1; i++) {
                partite[i] = new Partita("Partita" + i, numGiocatori / 2, codice);
                for (int j = 0; j < numGiocatori / 2; j++) {
                    int indiceCasuale = random.nextInt(partecipanti.size());
                    partite[i].aggiungiGiocatorePartitaToreno(partecipanti.get(indiceCasuale));
                    partecipanti.remove(indiceCasuale);
                }
            }
        }else{
            for (int i = 0; i < partite.length - 3; i++) {
                partite[i] = new Partita("Partita" + i, numGiocatori / 4, codice);
                for (int j = 0; j < numGiocatori / 4; j++) {
                    int indiceCasuale = random.nextInt(partecipanti.size());
                    partite[i].aggiungiGiocatorePartitaToreno(partecipanti.get(indiceCasuale));
                    partecipanti.remove(indiceCasuale);
                }
            }
        }

    }

    public Partita selezionaPartita() {
        for (int i = 0; i < partite.length; i++) {
            if (partite[i].getStatoPartita() != Partita.Stato.Terminata) {
                daGiocare = partite[i];
                break;
            }
        }
        return daGiocare;
    }

    // --------------------------- Metodi per UtenteBoard
    public int ottieniAccessi() {
        return accessi.size();
    }

    public void effettuaAccesso(String nome) {
        for (int i = 0; i < accessi.size(); i++) {
            if (accessi.get(i).equals(nome)) {
                accessi.remove(i);
                break;
            }
        }
    }

    public boolean controlloUtente(String nome) {
        for (int i = 0; i < accessi.size(); i++) {
            if (accessi.get(i).equals(nome)) {
                return true;
            }
        }
        return false;
    }

    public boolean pronto() {
        if (accessi.isEmpty())
            return true;

        return false;
    }

    public void ripristinaAccessi(ArrayList<String> accessiBackup) {
        this.accessi = new ArrayList<>(accessiBackup);
    }

    // ------------------------------ Metodi get e set
    public String getNomeTorneo() {
        return nomeTorneo;
    }

    public int getNumGiocatori() {
        return numGiocatori;
    }

    public int getCodice() {
        return codice;
    }

    public Stato getStatoTorneo() {
        return statoTorneo;
    }

    public ArrayList<String> getAccessi() {
        return accessi;
    }

    public ArrayList<Giocatore> getPartecipanti() {
        return partecipanti;
    }

    public void setStatoTorneo(Stato nuovoStato) {
        statoTorneo = nuovoStato;
    }

    private void scaricaGiocatoriDaFile() {
        try {
            // Se il file esiste lo leggiamo
            if (fileGiocatori.exists()) {
                System.out.println("Il file giocatori.json esiste già.");

                // Se il file non è vuoto lo leggiamo
                if (fileGiocatori.length() == 0) {
                    System.out.println("Il file giocatori.json è vuoto.");
                    return;
                } else {
                    giocatoriDaFile = objectMapper.readValue(fileGiocatori, new TypeReference<List<Giocatore>>() {
                    });
                }

            } else {
                // Se il file non esiste, lo creiamo ma non lo leggiamo
                try {
                    fileGiocatori.createNewFile();
                    System.out.println("Il file giocatori.json è stato creato con successo.");
                } catch (Exception e) {
                    // Alert impossibile creare il file(?)
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            // Alert impossibile scaricare il file(?)
            e.printStackTrace();
        }
    }

    private void caricaGiocatoriSuFile() {
        try {
            objectMapper.writeValue(fileGiocatori, giocatoriDaFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Partita[] getPartite() {
        return partite;
    }

    public ArrayList<Giocatore> getVincenti() {
        return vincenti;
    }

    public ArrayList<Giocatore> getPerdenti() {
        return perdenti;
    }
}
