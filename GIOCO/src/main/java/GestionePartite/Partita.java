package GestionePartite;

import GestioneGioco.Bot;
import GestioneGioco.Giocatore;
import GestioneGioco.GiocoController;
import GestioneGioco.Tavolo;
import GestioneUtenti.Utente;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Partita implements Serializable {

    public enum Stato {
        Pronta, Sospesa, Terminata;
    }

    String nomePartita;
    int numGiocatori;
    int codice;
    Stato statoPartita;

    public List<Giocatore> accessi = new ArrayList();

    static Tavolo t;

    public Partita(String nomePartita, int numGiocatori, int codice) {
        t = new Tavolo();
        this.nomePartita = nomePartita;
        this.numGiocatori = numGiocatori;
        this.codice = codice;
        statoPartita = Stato.Pronta;
    }

    public String getNome() {
        return nomePartita;
    }

    public int getNumGiocatori() {
        return numGiocatori;
    }

    public int getCodice() {
        return codice;
    }

    public Stato getStato() {
        return statoPartita;
    }

    public Tavolo getTavolo(){
        return t;
    }

    public void aggiungiGiocatore(Utente u) {
        Giocatore g = new Giocatore(u.getNome());
        t.nuovoPunteggio(g);
        accessi.add(g);
    }

    public void aggiungiBot(int numeroBot) {
        Bot b = new Bot("Bot-" + numeroBot);
        t.nuovoPunteggio(b);
    }

    public void effettuaAccesso(String nome) {
        for (int i = 0; i < accessi.size(); i++) {
            Giocatore giocatoreCorrente = accessi.get(i);
            if (giocatoreCorrente.getNome().equals(nome)) {
                accessi.remove(i);
                break;
            }
        }
    }

    public boolean controlloUtente(String nome) {
        for (Giocatore g : accessi) {
            if (g.getNome().equals(nome))
                return true;
        }
        return false;
    }

    public boolean pronta() {
        if (accessi.isEmpty())
            return true;

        return false;
    }

    public int getGiocatoriAcceduti() {
        return accessi.size();
    }
}
