package main.java.expenseTracker.UI;

import javafx.application.Application;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

public class ExpenseTrackerUI extends Application {

    @Override
    public void start(Stage stage) {
        WebView webView = new WebView();
        WebEngine engine = webView.getEngine();

        JavaBridge bridge = new JavaBridge();

        // Înregistrează bridge-ul după ce pagina s-a încărcat
        engine.getLoadWorker().stateProperty().addListener((obs, old, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) engine.executeScript("window");
                window.setMember("java", bridge);
                System.out.println("Bridge conectat!");
            }
        });
// Activează alert() din JavaScript
        engine.setOnAlert(event -> {
            javafx.scene.control.Alert alert =
                    new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
            alert.setTitle("Sprout");
            alert.setHeaderText(null);
            alert.setContentText(event.getData());
            alert.showAndWait();
        });
        // Activează prompt() din JavaScript
        engine.setPromptHandler(event -> {
            javafx.scene.control.TextInputDialog dialog = new javafx.scene.control.TextInputDialog();
            dialog.setTitle("Sprout");
            dialog.setHeaderText(null);
            dialog.setContentText(event.getMessage());
            return dialog.showAndWait().orElse(null);
        });
        engine.load(new java.io.File("sprout_app.html").toURI().toString());

        StackPane root = new StackPane(webView);
        Scene scene = new Scene(root, 420, 800);
        stage.setTitle("Sprout");
        stage.setScene(scene);
        stage.show();
    }

}