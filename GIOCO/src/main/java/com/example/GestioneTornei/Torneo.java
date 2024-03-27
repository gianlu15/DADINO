package com.example.GestioneTornei;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Random;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.GestisciFile;
import com.example.GestioneGiocatori.Bot;
import com.example.GestioneGiocatori.Giocatore;
import com.example.GestionePartite.Partita;
import com.example.GestioneUtenti.Utente;

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

    private List<Giocatore> giocatoriDaFile;
    private File fileGiocatori;
    private ObjectMapper objectMapper;

    public Torneo(String nomeTorneo, int numGiocatori, int codice) throws URISyntaxException {
        this.nomeTorneo = nomeTorneo;
        this.numGiocatori = numGiocatori;
        this.codice = codice;

        this.accessi = new ArrayList<>();
        this.partecipanti = new ArrayList<>();
        this.statoTorneo = Stato.Pronto;
        this.giocatoriDaFile = new ArrayList<>();
        this.objectMapper = new ObjectMapper();
        String path = GestisciFile.ottieniDirectory();

        this.fileGiocatori = new File(path, "giocatori.json");
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

    public void sorteggiaPartite() throws URISyntaxException {
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
        } else {
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

    public Partita selezionaPartita() throws URISyntaxException {
        if (partite.length == 3) {
            for (int i = 0; i < partite.length - 1; i++) {
                if (partite[i].getStatoPartita() != Partita.Stato.Terminata) {
                    daGiocare = partite[i];
                    return daGiocare;
                }
            }
            if (partite[2] == null) {
                partite[2] = new Partita("Partita" + 2, 2, codice);
                partite[2].aggiungiGiocatorePartitaToreno(partite[0].getVincitore());
                partite[2].aggiungiGiocatorePartitaToreno(partite[1].getVincitore());
            }
            daGiocare = partite[2];
            return daGiocare;

        } else {
            for (int i = 0; i < partite.length - 3; i++) {
                if (partite[i].getStatoPartita() != Partita.Stato.Terminata) {
                    daGiocare = partite[i];
                    return daGiocare;
                }
            }

            if (partite[4].getStatoPartita() != Partita.Stato.Terminata) {
                if (partite[4] == null) {
                    partite[4] = new Partita("Partita" + 4, 2, codice);
                    partite[4].aggiungiGiocatorePartitaToreno(partite[0].getVincitore());
                    partite[4].aggiungiGiocatorePartitaToreno(partite[0].getVincitore());
                }
                daGiocare = partite[4];
                return daGiocare;
            }

            if (partite[5].getStatoPartita() != Partita.Stato.Terminata) {
                if (partite[5] == null) {
                    partite[5] = new Partita("Partita" + 5, 2, codice);
                    partite[5].aggiungiGiocatorePartitaToreno(partite[2].getVincitore());
                    partite[5].aggiungiGiocatorePartitaToreno(partite[3].getVincitore());
                }
                daGiocare = partite[5];
                return daGiocare;
            }

            if (partite[6] == null) {
                partite[6] = new Partita("Partita" + 6, 2, codice);
                partite[6].aggiungiGiocatorePartitaToreno(partite[4].getVincitore());
                partite[6].aggiungiGiocatorePartitaToreno(partite[5].getVincitore());
            }
            daGiocare = partite[6];
            return daGiocare;
        }
    }

    public void aggiungiVincitore(Giocatore vincitore) {
        vincitore.vinto();

        if (partite[partite.length - 1] != null
                && partite[partite.length - 1].getStatoPartita() == Partita.Stato.Terminata) {
            vincitore.aumentaVittorieTorneo();
            statoTorneo = Stato.Terminato;
        }
    }

    public boolean controlloUltimaPartita() {
        if (partite[partite.length - 1] != null
                && partite[partite.length - 1].getStatoPartita() == Partita.Stato.Terminata)
            return true;
        else
            return false;
    }

    public void aggiungiPerdente(Giocatore perdente) {
        perdente.perso();
        ;
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
                    // Impossibile creare il file
                    e.printStackTrace();
                    return;
                }
            }
        } catch (IOException e) {
            // Impossibile scaricare il file
            e.printStackTrace();
            return;
        }
    }

    private void caricaGiocatoriSuFile() {
        try {
            objectMapper.writeValue(fileGiocatori, giocatoriDaFile);
        } catch (IOException e) {
            //Impossibile caricare il file
            e.printStackTrace();
            return;
        }
    }

    public Partita[] getPartite() {
        return partite;
    }
}
