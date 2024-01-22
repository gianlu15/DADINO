module gioco {
    requires javafx.controls;
    requires javafx.fxml;

    opens gioco to javafx.fxml;
    exports gioco;
}
