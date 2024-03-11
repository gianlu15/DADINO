package GestioneGioco;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import GestioneCarte.Mazzo;
import GestionePartite.Partita;
import GestionePartite.Partita.Stato;

public class Tavolo {

    Mazzo mazzoDiGioco;
    private int codice;
    public Map<Giocatore, Integer> punteggi; // Memorizza l'associazione Giocatore-punteggio



    private int[] cartePescate;
    private int[] bombePescate;
    private int[] puntiTotali;
    private int turniTotali;
    private ArrayList<Integer> listaPunteggi;

    private List<Partita> partiteDaFile;
    private File filePartite;
    private ObjectMapper objectMapper;

    // Costruttore
    public Tavolo(int codice) {
        this.codice = codice;
        this.punteggi = new LinkedHashMap<>();
        this.mazzoDiGioco = new Mazzo();
        this.mazzoDiGioco.inizializzaMazzo();
        this.mazzoDiGioco.mescolaMazzo();
        this.listaPunteggi = new ArrayList<>();
    }

    public Tavolo() {
    }


    public void assegnaGiocatori(ArrayList<Giocatore> partecipanti){
        for(Giocatore g : partecipanti){
            punteggi.put(g, 0);
        }
    }

    public void aggiornaPunteggio(Giocatore giocatore, int punteggioTurno) {
        punteggi.put(giocatore, punteggioTurno);
    }

    public Mazzo getMazzoDiGioco() {
        return mazzoDiGioco;
    }

    public int getCodice() {
        return codice;
    }

    @JsonIgnore
    public Map<Giocatore, Integer> getPunteggi() {
        return punteggi;
    }

    @JsonIgnore
    public ArrayList<Integer> trasformaPunteggiAsArrayList() {
        listaPunteggi.clear();
        for (Integer punteggio : punteggi.values()) {
            listaPunteggi.add(punteggio);
        }
        return listaPunteggi;
    }

    public ArrayList<Integer> getListaPunteggi(){
        return listaPunteggi;
    }

    public void assegnaGiocatoriPunti(ArrayList<Giocatore> partecipanti, ArrayList<Integer> punti){
        this.punteggi = new LinkedHashMap<>();
        for (int i = 0; i < partecipanti.size(); i++) {
            System.out.println("Partecipante " + partecipanti.get(i) + "punti " + punti.get(i));
            punteggi.put(partecipanti.get(i), punti.get(i));
        }
    }

    public void setStats(int grandezza) {
        cartePescate = new int[grandezza];
        bombePescate = new int[grandezza];
        puntiTotali = new int[grandezza];
        turniTotali = 0;
    }

    // ------------------- test
    public void mostraStats() {
        System.out.println("Statistiche:");
        System.out.println("Turni totali: " + turniTotali);
        System.out.println("Giocatori\tCarte Pescate\tBombe Pescate\tPunti Totali");

        for (int i = 0; i < cartePescate.length; i++) {
            System.out.print("Giocatore " + (i + 1) + "\t\t");
            System.out.print(cartePescate[i] + "\t\t");
            System.out.print(bombePescate[i] + "\t\t");
            System.out.println(puntiTotali[i]);
        }
    }

    // ------------------- test
    public void stampaOrdine() {
        for (Entry<Giocatore, Integer> entry : punteggi.entrySet()) {
            System.out.println("Giocatore: " + entry.getKey());
        }
    }

    // Metodo per cambiare lo stato della partita a "Terminata"
    public void finePartita() {
        this.partiteDaFile = new ArrayList<>();
        this.objectMapper = new ObjectMapper();
        this.filePartite = new File("src/main/resources/FileJson/partite.json");
        scaricaPartiteDaFile();
        for (Partita p : partiteDaFile) {
            if (p.getCodice() == codice) {
                p.setStatoPartita(Stato.Terminata);
                caricaPartiteSuFile();
                return;
            }
        }
    }

    // ------------------- mi servono per cambiare lo stato della partita a cui
    // appartiene il tavolo
    private void scaricaPartiteDaFile() {
        try {
            partiteDaFile = objectMapper.readValue(filePartite, new TypeReference<List<Partita>>() {
            });
        } catch (IOException e) {
            // Alert impossibile scaricare il file(?)
            e.printStackTrace();
        }
    }

    private void caricaPartiteSuFile() {
        try {
            objectMapper.writeValue(filePartite, partiteDaFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
