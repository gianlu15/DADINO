package GestioneGioco;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.annotation.JsonIgnore;

import GestioneCarte.Mazzo;

public class Tavolo {

    Mazzo mazzoDiGioco;
    private int codice;
    public Map<Giocatore, Integer> punteggi; // Memorizza l'associazione Giocatore-punteggio

    private int[] cartePescate;
    private int[] bombePescate;
    private int[] puntiTotali;
    private int turniTotali;

    private ArrayList<Integer> listaPunteggi;

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

    public void assegnaGiocatori(ArrayList<Giocatore> partecipanti) {
        for (Giocatore g : partecipanti) {
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

    public ArrayList<Integer> getListaPunteggi() {
        return listaPunteggi;
    }

    public void assegnaGiocatoriPunti(ArrayList<Giocatore> partecipanti, ArrayList<Integer> punti) {
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

    public int[] getCartePescate() {
        return cartePescate;
    }

    public int[] getBombePescate() {
        return bombePescate;
    }

    public int[] getPuntiTotali() {
        return puntiTotali;
    }

    public int getTurniTotali() {
        return turniTotali;
    }

    public void aggiornaCartePescate(int indice) {
        this.cartePescate[indice]++;
    }

    public void aggiornaBombePescate(int indice) {
        this.bombePescate[indice]++;
    }

    public void aggiornaPuntiTotali(int indice, int punteggio) {
        this.puntiTotali[indice] += punteggio;
    }

    public void aggiornaTurniTotali() {
        this.turniTotali++;
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

}
