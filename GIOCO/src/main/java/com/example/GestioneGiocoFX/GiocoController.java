package com.example.GestioneGiocoFX;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.example.GestisciFile;
import com.example.GestioneCarte.Carta;
import com.example.GestioneGiocatori.Bot;
import com.example.GestioneGiocatori.Giocatore;
import com.example.GestioneGioco.Esecuzione;
import com.example.GestioneGioco.Tavolo;
import com.example.GestioneGiocoTorneoFX.VisualizzaTorneo3Controller;
import com.example.GestioneGiocoTorneoFX.VisualizzaTorneo7Controller;
import com.example.GestioneLogin.HomeLogin;
import com.example.GestionePartite.Partita;
import com.example.GestionePartite.Partita.Stato;
import com.example.GestioneTornei.Torneo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;

import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GiocoController {

    @FXML
    private Label Giocatore0;

    @FXML
    private Label punteggioGiocatore0;

    @FXML
    private Label Giocatore1;

    @FXML
    private Label punteggioGiocatore1;

    @FXML
    private Label Giocatore2;

    @FXML
    private Label punteggioGiocatore2;

    @FXML
    private Label Giocatore3;

    @FXML
    private Label punteggioGiocatore3;

    @FXML
    private Button BottonePesca;

    @FXML
    private Label fraseTurno;

    @FXML
    private Label giocatoreTurno;

    @FXML
    private ImageView CartaCoperta;

    @FXML
    private ImageView CartaScoperta;

    @FXML
    private Button BottoneFermati;

    @FXML
    private Label PunteggioParziale;

    @FXML
    private Node rootNode;

    private Tavolo tavoloPartita;
    private Esecuzione esecuzione;
    private ArrayList<Giocatore> giocatori;
    private ArrayList<Integer> punti;
    private Partita partitaAttiva;
    private Torneo torneoAttivo;
    private boolean partitaTorneo;

    private List<Partita> partiteSalvate;
    private List<Esecuzione> esecuzioniSalvate;
    private List<Giocatore> giocatoriSalvati;
    private List<Torneo> torneiSalvati;
    private File partiteFile;
    private File esecuzioniFile;
    private File giocatoriFile;
    private File torneiFile;
    private ObjectMapper objectMapper;
    private ObjectMapper partitaMapper;
    private ObjectMapper torneiMapper;;
    private Thread partitaThread;
    private boolean partitaInterrotta;

    Stage stage;

    @FXML
    public void initialize() throws URISyntaxException {
        String pathName = "CarteImmagini/Retro.png";

        CartaCoperta.setImage(new Image(Carta.class.getResourceAsStream(pathName)));

        disabilitaPulsanti();
        PunteggioParziale.setVisible(false);
        fraseTurno.setVisible(false);
        giocatoreTurno.setVisible(false);

        this.torneiSalvati = new ArrayList<>();
        this.giocatori = new ArrayList<>();
        this.punti = new ArrayList<>();

        this.objectMapper = new ObjectMapper();
        this.partitaMapper = new ObjectMapper();
        this.torneiMapper = new ObjectMapper();
        this.partiteSalvate = new ArrayList<>();
        this.esecuzioniSalvate = new ArrayList<>();
        this.giocatoriSalvati = new ArrayList<>();
        this.partitaInterrotta = false;
        this.partitaTorneo = false;

        String path = GestisciFile.ottieniDirectory();

        this.torneiFile = new File(path, "tornei.json");
        this.partiteFile = new File(path, "partite.json");
        this.giocatoriFile = new File(path, "giocatori.json");
        this.esecuzioniFile = new File(path, "esecuzioni.json");

        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType("com.example")
                .allowIfSubType("java.util.ArrayList")
                .allowIfBaseType("java.util.List<com.example.GestionePartite.Partita>")
                .build();
        partitaMapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL);

        PolymorphicTypeValidator ptt = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType("com.example")
                .allowIfSubType("java.util.ArrayList")
                .allowIfSubType("[Lcom.example.GestionePartite.Partita")
                .allowIfBaseType("java.util.List<com.example.GestioneTornei.Torneo>")
                .build();
        torneiMapper.activateDefaultTyping(ptt, ObjectMapper.DefaultTyping.NON_FINAL);
    }

    // #2-a
    public void creaNuovoTavolo(Partita partitaAttiva, Torneo torneoAttivo) {

        tavoloPartita = new Tavolo(partitaAttiva.getCodice());

        for (Giocatore g : partitaAttiva.getPartecipanti())
            if (g instanceof Bot) {
                Bot b = (Bot) g;
                giocatori.add(b);
            } else {
                giocatori.add(g);
            }

        tavoloPartita.assegnaGiocatori(giocatori);

        esecuzione = new Esecuzione(tavoloPartita, giocatori);
        esecuzione.setController(this);

        this.partitaAttiva = partitaAttiva;

        if (torneoAttivo != null) {
            this.torneoAttivo = torneoAttivo;
            this.partitaTorneo = true;
        } else
            this.partitaTorneo = false;

        setLeaderboard();
    }

    // #2-b
    public void reimpostaTavolo(Partita partitaAttiva, Torneo torneoAttivo) {

        for (Giocatore g : partitaAttiva.getPartecipanti())
            if (g instanceof Bot) {
                Bot b = (Bot) g;
                giocatori.add(b);
            } else {
                giocatori.add(g);
            }

        scaricaEsecuzioniDaFile();
        for (Esecuzione e : esecuzioniSalvate) {
            if (e.getCodice() == partitaAttiva.getCodice())
                esecuzione = e;
        }

        tavoloPartita = esecuzione.getTavolo();
        esecuzione.setGiocatori(giocatori);
        punti = tavoloPartita.getListaPunteggi();
        tavoloPartita.assegnaGiocatoriPunti(giocatori, punti);

        esecuzione.setController(this);
        this.partitaAttiva = partitaAttiva;

        if (torneoAttivo != null) {
            this.torneoAttivo = torneoAttivo;
            this.partitaTorneo = true;
        } else
            this.partitaTorneo = false;

        setLeaderboard();
    }

    // #3
    public void setStage(Stage primaryStage) {
        stage = primaryStage;
    }

    // #4
    public void esegui() {
        partitaThread = new Thread(() -> {
            esecuzione.eseguiPartita();

        });

        partitaThread.start();
    }

    // Richiamata dall'esecuzione
    public void inizioTurno(int punteggioParziale, Giocatore giocatore) {
        CartaScoperta.setImage(null);
        fraseTurno.setVisible(true);
        giocatoreTurno.setVisible(true);
        PunteggioParziale.setVisible(true);
        Platform.runLater(() -> {
            PunteggioParziale.setText(Integer.toString(punteggioParziale));
            giocatoreTurno.setText(giocatore.getNome());
        });
    }

    // Servono per la decisione obbligata
    public void disabilitaPesca() {
        BottonePesca.setDisable(true);
    }

    public void abilitaPesca() {
        BottonePesca.setDisable(false);
    }

    public CompletableFuture<Boolean> decisioneGiocatore() {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        ScheduledFuture<?> scheduledFuture = executor.scheduleAtFixedRate(() -> {
            if (partitaInterrotta) {
                System.out.println(("#4.5 partitaThread interrotta"));
                future.complete(false);
                executor.shutdown();
            }
        }, 0, 3, TimeUnit.SECONDS);

        BottonePesca.setOnAction(event -> {
            future.complete(true);
            scheduledFuture.cancel(true);
            executor.shutdown();
        });

        BottoneFermati.setOnAction(event -> {
            future.complete(false);
            scheduledFuture.cancel(true);
            executor.shutdown();
        });

        if (!partitaInterrotta)
            esecuzione.attendi();

        return future;
    }

    public CompletableFuture<Boolean> decisioneObbligataGiocatore() {

        CompletableFuture<Boolean> future = new CompletableFuture<>();

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        ScheduledFuture<?> scheduledFuture = executor.scheduleAtFixedRate(() -> {
            if (partitaInterrotta) {
                System.out.println(("#4.5 partitaThread interrotta"));
                future.complete(false); // Completa il futuro con un valore predefinito se il thread è stato interrotto
                executor.shutdown(); // Chiudi l'ExecutorService
            }
        }, 0, 3, TimeUnit.SECONDS); // Controlla ogni secondo se il thread è stato interrotto

        BottonePesca.setOnAction(event -> {
            future.complete(true);
            scheduledFuture.cancel(true); // Interrompe il controllo periodico quando viene fatta una scelta
            executor.shutdown(); // Chiudi l'ExecutorService
        });

        if (!partitaInterrotta)
            esecuzione.attendi();

        return future;
    }

    public void setImmagine(Image immagine) {
        CartaScoperta.setImage(immagine);
        RotateTransition rotate = new RotateTransition();
        rotate.setNode(CartaScoperta);
        rotate.setDuration(Duration.millis(500));
        rotate.setByAngle(360);
        rotate.play();
    }

    // disabilita la vista del punteggio parziale
    public void disabilitaPunteggio() {
        PunteggioParziale.setVisible(false);
    }

    // aggiorna la vista del punteggio parziale
    public void aggiornaVistaPunteggioParziale(int punteggioParziale) {
        Platform.runLater(() -> {
            PunteggioParziale.setText(Integer.toString(punteggioParziale));
        });
    }

    // aggiorna la leaderboard
    public void aggiornaVistaPunteggio(Giocatore giocatoreCorrente, int punteggioTurno) {
        int indice = giocatori.indexOf(giocatoreCorrente);

        switch (indice) {
            case 0:
                Platform.runLater(() -> {
                    punteggioGiocatore0.setText(Integer.toString(punteggioTurno));
                });
                break;

            case 1:
                Platform.runLater(() -> {
                    punteggioGiocatore1.setText(Integer.toString(punteggioTurno));
                });
                break;
            case 2:
                Platform.runLater(() -> {
                    punteggioGiocatore2.setText(Integer.toString(punteggioTurno));
                });
                break;
            case 3:
                Platform.runLater(() -> {
                    punteggioGiocatore3.setText(Integer.toString(punteggioTurno));
                });
                break;
            default:
                System.err.println("Giocatore non trovato nella leaderboard");
                break;
        }
    }

    @FXML
    public void alertVittoria(Giocatore vincitore) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Vittoria!");
            alert.setContentText("Il giocatore " + vincitore.getNome() + " ha vinto!");

            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets()
                    .add(getClass().getResource("/com/example/Styles/alertStyle.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");

            alert.getButtonTypes().clear();

            if (partitaTorneo) {
                if (torneoAttivo.getPartite()[torneoAttivo.getPartite().length - 1] == partitaAttiva) {
                    alert.setTitle("Torneo terminato");
                    ButtonType home = new ButtonType("Torna alla home");
                    alert.getButtonTypes().addAll(home);
                    alert.setOnCloseRequest(event -> handleTournamentFinish(home, alert));
                } else {
                    alert.setTitle("Partita terminata");
                    ButtonType prosegui = new ButtonType("Prosegui torneo");
                    alert.getButtonTypes().addAll(prosegui);
                    alert.setOnCloseRequest(event -> {
                        try {
                            handleContinueTournament(prosegui, alert);
                        } catch (URISyntaxException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    });
                }
            } else {
                alert.setTitle("Partita terminata");
                ButtonType homeButton = new ButtonType("Torna alla Home");
                ButtonType statsButton = new ButtonType("Statistiche Partita");
                alert.getButtonTypes().addAll(homeButton, statsButton);
                alert.setOnCloseRequest(event -> handleGameFinish(homeButton, statsButton, alert));
            }

            alert.showAndWait();
        });
    }

    private void handleTournamentFinish(ButtonType home, Alert alert) {
        if (alert.getResult() == home) {
            try {
                navigateToHome();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        interruptAndClose();
    }

    private void handleContinueTournament(ButtonType prosegui, Alert alert) throws URISyntaxException {
        if (alert.getResult() == prosegui) {
            try {
                loadTournamentView();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        interruptAndClose();
    }

    private void handleGameFinish(ButtonType homeButton, ButtonType statsButton, Alert alert) {
        try {
            if (alert.getResult() == homeButton) {
                navigateToHome();
            } else if (alert.getResult() == statsButton) {
                showGameStatistics();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        interruptAndClose();
    }

    private void navigateToHome() throws Exception {
        HomeLogin hl = new HomeLogin();
        hl.start(stage);
    }

    private void loadTournamentView() throws IOException, URISyntaxException {
        FXMLLoader loader;
        Parent root;
        if (torneoAttivo.getNumGiocatori() < 10) {
            loader = new FXMLLoader(
                    getClass().getResource("/com/example/GestioneGiocoTorneoFX/visualizzazioneTornei3.fxml"));
            root = loader.load();
            VisualizzaTorneo3Controller vtc = loader.getController();
            vtc.setTorneo(torneoAttivo);
        } else {
            loader = new FXMLLoader(
                    getClass().getResource("/com/example/GestioneGiocoTorneoFX/visualizzazioneTornei7.fxml"));
            root = loader.load();
            VisualizzaTorneo7Controller vtc = loader.getController();
            vtc.setTorneo(torneoAttivo);
        }
        Stage nuovoStage = new Stage();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/com/example/Styles/StyleTorneoView.css").toExternalForm());
        nuovoStage.setScene(scene);
        nuovoStage.show();
    }

    private void showGameStatistics() throws IOException {
        StatistichePostPartita spp = new StatistichePostPartita(tavoloPartita, giocatori);
        spp.start(stage);
    }

    private void interruptAndClose() {
        partitaThread.interrupt();
        stage.close();
    }

    public void setLeaderboard() {

        Giocatore0.setText(giocatori.get(0).getNome());
        punteggioGiocatore0.setText(Integer.toString(tavoloPartita.getPunteggi().get(giocatori.get(0))));

        System.out.println(giocatori.get(0).getNome());
        Giocatore1.setText(giocatori.get(1).getNome());
        punteggioGiocatore1.setText(Integer.toString(tavoloPartita.getPunteggi().get(giocatori.get(1))));

        Giocatore2.setText("");
        punteggioGiocatore2.setText("");

        Giocatore3.setText("");
        punteggioGiocatore3.setText("");

        if (giocatori.size() == 3) {
            Giocatore2.setText(giocatori.get(2).getNome());
            punteggioGiocatore2.setText(Integer.toString(tavoloPartita.getPunteggi().get(giocatori.get(2))));
        }

        if (giocatori.size() == 4) {
            Giocatore2.setText(giocatori.get(2).getNome());
            punteggioGiocatore2.setText(Integer.toString(tavoloPartita.getPunteggi().get(giocatori.get(2))));

            Giocatore3.setText(giocatori.get(3).getNome());
            punteggioGiocatore3.setText(Integer.toString(tavoloPartita.getPunteggi().get(giocatori.get(3))));
        }
    }

    public void disabilitaPulsanti() {
        BottonePesca.setDisable(true);
        BottoneFermati.setDisable(true);
    }

    public void abilitaPulsanti() {
        BottonePesca.setDisable(false);
        BottoneFermati.setDisable(false);
    }

    public void interrompiPartita() {

        tavoloPartita.trasformaPunteggiAsArrayList();

        scaricaPartiteDaFile();
        scaricaEsecuzioniDaFile();

        for (int i = 0; i < partiteSalvate.size(); i++) {
            Partita p = partiteSalvate.get(i);
            if (p.getCodice() == partitaAttiva.getCodice()) {
                partiteSalvate.set(i, partitaAttiva);
                System.out.println("#3 Partita rimpiazzata");
                break;
            }
        }

        if (partitaTorneo) {
            scaricaTorneiDaFile();
            torneoAttivo.setStatoTorneo(Torneo.Stato.Sospeso);
            for (int i = 0; i < partiteSalvate.size(); i++) {
                Torneo t = torneiSalvati.get(i);
                if (t.getCodice() == torneoAttivo.getCodice()) {
                    torneiSalvati.set(i, torneoAttivo);
                    System.out.println("#3 Torneo rimpiazzata");
                    break;
                }
            }
            caricaTorneiSuFile();
        }

        boolean esecuzioneTrovata = false;

        for (int i = 0; i < esecuzioniSalvate.size(); i++) {
            Esecuzione e = esecuzioniSalvate.get(i);
            if (e.getCodice() == partitaAttiva.getCodice()) {
                esecuzioniSalvate.set(i, esecuzione);
                esecuzioneTrovata = true;
                break;
            }
        }

        if (!esecuzioneTrovata) {
            esecuzioniSalvate.add(esecuzione);
        }

        System.out.println("#4 Salvato tutto correttamente");

        caricaPartiteSuFile();
        caricaEsecuzioniSuFile();

        partitaInterrotta = true;
        partitaThread.interrupt();
    }

    private void scaricaPartiteDaFile() {
        try {
            // Se il file esiste lo leggiamo
            if (partiteFile.exists()) {
                System.out.println("#1 Il file partite esiste già.");

                // Se il file non è vuoto lo leggiamo
                if (partiteFile.length() == 0) {
                    System.out.println("Il file partite JSON è vuoto.");
                    return;
                } else {
                    partiteSalvate = partitaMapper.readValue(partiteFile, new TypeReference<List<Partita>>() {
                    });
                }

            } else {
                System.out.println("Il file partite non esiste");
                // Alert nessuna partita creata!
            }
        } catch (IOException e) {
            // Alert impossibile scaricare il file(?)
            e.printStackTrace();
        }
    }

    private void scaricaEsecuzioniDaFile() {
        try {
            // Se il file esiste lo leggiamo
            if (esecuzioniFile.exists()) {
                System.out.println("#2 Il file esecuzioni esiste già.");

                // Se il file non è vuoto lo leggiamo
                if (esecuzioniFile.length() == 0) {
                    System.out.println("Il file esecuzioni JSON è vuoto.");
                    return;
                } else {
                    esecuzioniSalvate = objectMapper.readValue(esecuzioniFile, new TypeReference<List<Esecuzione>>() {
                    });
                }

            } else {
                System.out.println("Il file esecuzioni non esiste");
                // Alert nessuna partita creata!
            }
        } catch (IOException e) {
            // Alert impossibile scaricare il file(?)
            e.printStackTrace();
        }
    }

    private void scaricaTorneiDaFile() {
        try {
            // Se il file esiste lo leggiamo
            if (torneiFile.exists()) {
                System.out.println("#1 Il file tornei esiste già.");

                // Se il file non è vuoto lo leggiamo
                if (torneiFile.length() == 0) {
                    System.out.println("Il file tornei JSON è vuoto.");
                    return;
                } else {
                    torneiSalvati = torneiMapper.readValue(torneiFile, new TypeReference<List<Torneo>>() {
                    });
                }

            } else {
                System.out.println("Il file tornei non esiste");
                // Alert nessuna partita creata!
            }
        } catch (IOException e) {
            // Alert impossibile scaricare il file(?)
            e.printStackTrace();
        }
    }

    private void caricaPartiteSuFile() {
        try {
            partitaMapper.writeValue(partiteFile, partiteSalvate);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void caricaEsecuzioniSuFile() {
        try {
            objectMapper.writeValue(esecuzioniFile, esecuzioniSalvate);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void caricaTorneiSuFile() {
        try {
            torneiMapper.writeValue(torneiFile, torneiSalvati);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void scaricaGiocatorDaFile() {
        try {
            // Se il file esiste lo leggiamo
            if (giocatoriFile.exists()) {
                System.out.println("#2 Il file esecuzioni esiste già.");

                // Se il file non è vuoto lo leggiamo
                if (giocatoriFile.length() == 0) {
                    System.out.println("Il file esecuzioni JSON è vuoto.");
                    return;
                } else {
                    giocatoriSalvati = objectMapper.readValue(giocatoriFile, new TypeReference<List<Giocatore>>() {
                    });
                }

            } else {
                System.out.println("Il file esecuzioni non esiste");
                // Alert nessuna partita creata!
            }
        } catch (IOException e) {
            // Alert impossibile scaricare il file(?)
            e.printStackTrace();
        }
    }

    private void caricaGiocatoriDaFile() {
        try {
            objectMapper.writeValue(giocatoriFile, giocatoriSalvati);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void terminaPartita(Giocatore vincitore) {

        scaricaPartiteDaFile();
        partitaAttiva.setStatoPartita(Stato.Terminata);

        for (int i = 0; i < partiteSalvate.size(); i++) {
            Partita p = partiteSalvate.get(i);
            if (p.getCodice() == partitaAttiva.getCodice()) {
                partiteSalvate.set(i, partitaAttiva);
                break;
            }
        }
        caricaPartiteSuFile();

        scaricaGiocatorDaFile();
        vincitore.aumentaVittorie();

        if (partitaTorneo && torneoAttivo.controlloUltimaPartita()) {
            vincitore.aumentaVittorieTorneo();
            torneoAttivo.setStatoTorneo(Torneo.Stato.Terminato);
        }

        for (int i = 0; i < giocatori.size(); i++) {
            giocatori.get(i).aumentaPartiteGiocate();
            giocatori.get(i).setBombettePescate(tavoloPartita.getBombePescate()[i]);
            giocatori.get(i).setCarteTotaliPescate(tavoloPartita.getCartePescate()[i]);
            giocatori.get(i).setPuntiTotaliFatti(tavoloPartita.getPuntiTotali()[i]);
        }

        for (Giocatore g : giocatoriSalvati) {
            for (Giocatore giocatore : giocatori) {
                if (g.getNome().equals(giocatore.getNome())) {
                    giocatoriSalvati.set(giocatoriSalvati.indexOf(g), giocatore);
                    break;
                }
            }
        }
        caricaGiocatoriDaFile();

        scaricaEsecuzioniDaFile();
        esecuzioniSalvate.remove(esecuzione);
        caricaEsecuzioniSuFile();

        if (partitaTorneo) {
            partitaAttiva.setVincitore(vincitore);
            torneoAttivo.aggiungiVincitore(vincitore);

            giocatori.remove(vincitore);
            for (Giocatore g : giocatori) {
                torneoAttivo.aggiungiPerdente(g);
            }

            scaricaTorneiDaFile();
            for (int i = 0; i < torneiSalvati.size(); i++) {
                Torneo t = torneiSalvati.get(i);
                if (t.getCodice() == torneoAttivo.getCodice()) {
                    torneiSalvati.set(i, torneoAttivo);
                    break;
                }
            }
            caricaTorneiSuFile();
        }
    }

}
