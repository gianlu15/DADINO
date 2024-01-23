package com.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.shape.Circle;

/*
 * Il file controller è una classe Java che funge da intermediario 
 * tra l'interfaccia utente definita in un file FXML e la logica 
 * dell'applicazione. Questa classe rappresenta la logica 
 * dietro l'interfaccia utente e gestisce gli eventi e le azioni dell'utente. 
 */

public class Lezione04Controller {

    /* L'annotazione "@FXML" qua sotto è IMPORTANTISSIMA! Serve ad associare
     * gli elementi dell'interfaccia utente definiti in un file FXML con 
     * i campi o i metodi nella classe Java associata.
     */
    @FXML
    private Circle mioCerchio;
    private double x;
    private double y;


    public void Su(ActionEvent e) {
        System.out.println("SU");
        mioCerchio.setCenterY(y = y - 10);       //metodi per muovere il cerchio (aggiungiamo o togliamo 10 alle coordinate)
    }

    public void Giu(ActionEvent e) {
        System.out.println("GIU");
        mioCerchio.setCenterY(y = y + 10);
    }

    public void Destra(ActionEvent e) {
        System.out.println("DESTRA");
        mioCerchio.setCenterX(x = x + 10);
    }

    public void Sinistra(ActionEvent e) {
        System.out.println("SINISTRA");
        mioCerchio.setCenterX(x = x - 10);
    }

    /*
     * Ricorda di associare questa classe al file FXML attraverso lo Scene Builder.
     * Occorre andare in basso a sinistra ("Controller") e selezionarla, se non compare
     * prova a riaprire tutto o a ricreare il file FXML.
     * Assoccia successivamente ogni bottone al proprio metodo nella sezione "Code" a destra.
     * Dopo aver creato anche il cerchio oltre i bottoni, associa il cerchio di questo codice
     * a quello attraverso l'uso di fx:id nella sezione "Code" del cerchio in cui occorre mettere
     * il nome di questo cerchio (quindi "mioCerchio").
     */
}
