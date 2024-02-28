package GestioneUtenti;

import java.io.Serializable;

public class Utente implements Serializable {

    private String nome;
    private int id;

    public Utente(String nome) {
        this.nome = nome;
        this.id = IdManager.getProssimoId();
        IdManager.salvaProssimoId();
    }

    public String getNome() {
        return nome;
    }

    public int getId() {
        return id;
    }
}
