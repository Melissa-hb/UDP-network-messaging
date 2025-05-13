package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import util.UDPconnection;

public class PeerC extends Application {
    private TextArea messageArea;
    private TextField ipField;
    private TextField portField;
    private TextField messageField;
    private UDPconnection udpConnection;

    @Override
    public void start(Stage primaryStage) {
        udpConnection = UDPconnection.getInstance();
        udpConnection.setPort(5002);
        udpConnection.setOnMessageReceived(this::displayMessage);
        udpConnection.start();

        messageArea = new TextArea();
        messageArea.setEditable(false);

        ipField = new TextField("127.0.0.1");
        portField = new TextField("5001");
        messageField = new TextField();

        Button sendButton = new Button("Enviar");
        sendButton.setOnAction(e -> {
            String ip = ipField.getText();
            int port = Integer.parseInt(portField.getText());
            String message = "[A]: " + messageField.getText();
            udpConnection.sendAsyncMessage(message, ip, port);
            messageArea.appendText(message + "\n");
            messageField.clear();
        });

        VBox layout = new VBox(10,
            new Label("Chat:"), messageArea,
            new HBox(5, new Label("IP:"), ipField, new Label("Puerto:"), portField),
            new HBox(5, new Label("Mensaje:"), messageField, sendButton)
        );
        layout.setStyle("-fx-padding: 10;");

        Scene scene = new Scene(layout, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Peer C");
        primaryStage.show();
    }

    private void displayMessage(String message) {
        // Asegura que se actualiza en el hilo de JavaFX
        javafx.application.Platform.runLater(() -> {
            messageArea.appendText(message + "\n");
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
