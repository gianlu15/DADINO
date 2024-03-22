package com.example.GestionePartite;

import com.example.GestisciFile;
import com.example.GestioneGiocatori.Bot;
import com.example.GestioneGiocatori.Giocatore;
import com.example.GestioneUtenti.Utente;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Partita {

    public enum Stato {
        Pronta, Sospesa, Terminata;
    }

    private String nome;
    private int numGiocatori;
    private int codice;
    private Giocatore vincitore;
    private Stato statoPartita;
    private ArrayList<String> accessi;
    private ArrayList<Giocatore> partecipanti;

    private List<Giocatore> giocatoriDaFile;
    private File fileGiocatori;
    private ObjectMapper objectMapper;

    public Partita(String nome, int numGiocatori, int codice) throws URISyntaxException {
        this.nome = nome;
        this.numGiocatori = numGiocatori;
        this.codice = codice;

        this.accessi = new ArrayList<>();
        this.partecipanti = new ArrayList<>();
        this.statoPartita = Stato.Pronta;

        this.giocatoriDaFile = new ArrayList<>();

        String path = GestisciFile.ottieniDirectory();

        this.fileGiocatori = new File(path, "giocatori.json");
        this.objectMapper = new ObjectMapper();
    }

    public Partita() {
    }

    public void aggiungiGiocatore(Utente u) {

        boolean trovato = false;

        scaricaGiocatoriDaFile();
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

    // --------------------------- Metodi torneo
    public void aggiungiGiocatorePartitaToreno(Giocatore g) {
        partecipanti.add(g);
    }

    public Giocatore ottieniGiocatore(int indice) {
        return partecipanti.get(indice);
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

    public boolean pronta() {
        if (accessi.isEmpty())
            return true;

        return false;
    }

    public void ripristinaAccessi(ArrayList<String> accessiBackup) {
        this.accessi = new ArrayList<>(accessiBackup);
    }

    // ------------------------------ Metodi get e set
    public String getNome() {
        return nome;
    }

    public int getNumGiocatori() {
        return numGiocatori;
    }

    public int getCodice() {
        return codice;
    }

    public Stato getStatoPartita() {
        return statoPartita;
    }

    public ArrayList<String> getAccessi() {
        return accessi;
    }

    public ArrayList<Giocatore> getPartecipanti() {
        return partecipanti;
    }

    public void setStatoPartita(Stato nuovoStato) {
        statoPartita = nuovoStato;
    }

    public void setVincitore(Giocatore vincitore) {
        this.vincitore = vincitore;
    }

    public Giocatore getVincitore() {
        return vincitore;
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

}
