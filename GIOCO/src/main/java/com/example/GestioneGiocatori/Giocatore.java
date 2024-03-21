package com.example.GestioneGiocatori;

public class Giocatore {

    private String nome;

    private int carteTotaliPescate;
    private int puntiTotaliFatti;
    private int bombettePescate;
    private int partiteGiocate;
    private int vittorie;
    private int vittorieTorneo;
    private Boolean vincitore;

    public Giocatore(String nome) {
        this.nome = nome;
    }

    public Giocatore() {
    }

    public String getNome() {
        return nome;
    }

    public int getVittorieTorneo() {
        return vittorieTorneo;
    }

    public int getCarteTotaliPescate() {
        return carteTotaliPescate;
    }

    public void aumentaVittorieTorneo() {
        this.vittorieTorneo++;
    }

    public void setCarteTotaliPescate(int carteTotaliPescate) {
        this.carteTotaliPescate += carteTotaliPescate;
    }

    public int getPuntiTotaliFatti() {
        return puntiTotaliFatti;
    }

    public void setPuntiTotaliFatti(int puntiTotaliFatti) {
        this.puntiTotaliFatti += puntiTotaliFatti;
    }

    public int getBombettePescate() {
        return bombettePescate;
    }

    public void setBombettePescate(int bombettePescate) {
        this.bombettePescate += bombettePescate;
    }

    public int getPartiteGiocate() {
        return partiteGiocate;
    }

    public void aumentaPartiteGiocate() {
        this.partiteGiocate++;
    }

    public int getVittorie() {
        return vittorie;
    }

    public void aumentaVittorie() {
        this.vittorie++;
    }

    public void vinto(){
        this.vincitore = true;
    }

    public void perso(){
        this.vincitore = false;
    }

    public Boolean getVincitore(){
        return vincitore;
    }
}
