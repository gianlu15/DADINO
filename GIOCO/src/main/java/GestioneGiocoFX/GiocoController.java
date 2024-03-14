package GestioneGiocoFX;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import GestioneCarte.Carta;
import GestioneGioco.Bot;
import GestioneGioco.Esecuzione;
import GestioneGioco.Giocatore;
import GestioneGioco.Tavolo;
import GestioneLogin.HomeLogin;
import GestionePartite.Partita;
import GestionePartite.Partita.Stato;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GiocoController implements Initializable, Serializable {

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

    private List<Partita> partiteSalvate;
    private List<Esecuzione> esecuzioniSalvate;
    private File partiteFile;
    private File esecuzioniFile;
    private ObjectMapper objectMapper;
    private Thread partitaThread;
    private boolean partitaInterrotta;

    Stage stage;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        String pathName = "CarteImmagini/Retro.png";

        CartaCoperta.setImage(new Image(Carta.class.getResourceAsStream(pathName)));

        disabilitaPulsanti();
        PunteggioParziale.setVisible(false);
        fraseTurno.setVisible(false);
        giocatoreTurno.setVisible(false);

        this.giocatori = new ArrayList<>();
        this.punti = new ArrayList<>();

        this.objectMapper = new ObjectMapper();
        this.partiteSalvate = new ArrayList<>();
        this.esecuzioniSalvate = new ArrayList<>();
        this.partiteFile = new File("src/main/resources/FileJson/partite.json");
        this.esecuzioniFile = new File("src/main/resources/FileJson/esecuzioni.json");
        this.partitaInterrotta = false;
    }

    // #2-a
    public void creaNuovoTavolo(Partita partitaAttiva) {

        tavoloPartita = new Tavolo(partitaAttiva.getCodice());

        for (Giocatore g : partitaAttiva.getPartecipanti())
            if (g.getBot()) {
                Bot b = new Bot(g.getNome());
                giocatori.add(b);
            } else {
                giocatori.add(g);
            }

        tavoloPartita.assegnaGiocatori(giocatori);

        esecuzione = new Esecuzione(tavoloPartita, giocatori);
        esecuzione.setController(this);

        setLeaderboard();
    }

    // #2-b
    public void reimpostaTavolo(Partita partitaAttiva) {

        for (Giocatore g : partitaAttiva.getPartecipanti())
            if (g.getBot()) {
                Bot b = new Bot(g.getNome());
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
            alert.setTitle("Partita terminata");
            alert.setHeaderText("Vittoria!");
            alert.setContentText("Il giocatore " + vincitore.getNome() + " ha vinto!");

            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("/Styles/alertStyle.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");

            alert.getButtonTypes().clear();

            ButtonType homeButton = new ButtonType("Torna alla Home");
            ButtonType statsButton = new ButtonType("Statistiche Partita");

            alert.getButtonTypes().addAll(homeButton, statsButton);

            alert.showAndWait().ifPresent(buttonType -> {
                try {

                    if (buttonType == homeButton) {
                        HomeLogin hl = new HomeLogin();
                        hl.start(stage);

                    } else if (buttonType == statsButton) {
                        StatistichePostPartita spp = new StatistichePostPartita(tavoloPartita, giocatori);
                        spp.start(stage);
                    }

                    partitaThread.interrupt();
                    stage.close();

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            });
        });

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

    public void interrompiPartita(Partita partitaAttiva) {

        partitaAttiva.setStatoPartita(Stato.Sospesa);
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
                    partiteSalvate = objectMapper.readValue(partiteFile, new TypeReference<List<Partita>>() {
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

    private void caricaPartiteSuFile() {
        try {
            objectMapper.writeValue(partiteFile, partiteSalvate);
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

}
