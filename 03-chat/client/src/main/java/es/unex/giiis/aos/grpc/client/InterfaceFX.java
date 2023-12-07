package es.unex.giiis.aos.grpc.client;

import es.unex.giiis.aos.grpc.client.utils.AlertUtils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Date;

public class InterfaceFX extends Application {
    private final ClientController clientController = ClientController.getClientController();
    private String username = "";

    Stage window;
    //Scenes
    Scene welcomeScene;
    Scene chatScene;
    //Widgets
    Label welcomeText = new Label("WELCOME TO THE CHAT, TELL US YOUR NAME");
    TextField usernameInput = new TextField();
    Button joinButton = new Button("Join");
    TextArea messagesArea = new TextArea();
    Button send = new Button();
    Button users = new Button();
    TextField message = new TextField();

    @Override
    public void start(Stage stage) throws Exception {
        window = stage;
        //Creating welcome page
        joinButton.setOnAction(this::onJoin);
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        usernameInput.setPromptText("username");
        GridPane.setConstraints(welcomeText, 0, 0);
        GridPane.setConstraints(usernameInput, 0, 1);
        GridPane.setConstraints(joinButton, 1, 1);
        grid.getChildren().addAll(welcomeText, usernameInput, joinButton);

        welcomeScene = new Scene(grid, 350, 100);

        //Creating chat page
        chatScene = createChatScene();

        window.setScene(welcomeScene);
        window.setTitle("CHAT");
        window.show();
        window.setOnCloseRequest((request) -> {
            if (!username.isBlank()) {
                request.consume();
                clientController.unsubscribe(username, (data) -> {
                    Platform.runLater(window::close);
                });
            }
        });
    }

    private void onJoin(ActionEvent actionEvent) {
        final String username = usernameInput.getText();
        if (username.isBlank()) {
            System.out.println("INVALID USERNAME");
            return;
        }
        clientController.ping((connected) -> {
            if (connected) {
                subscribe();
                Platform.runLater(() -> {
                    this.username = username;
                    window.setScene(chatScene);
                });
            } else {
               Platform.runLater(() -> {
                   AlertUtils.showAlert("HA OCURRIDO UN ERROR CONECTANDO AL SERVIDOR");
               });
            }
        });
    }

    public void subscribe() {
        clientController.subscribe(usernameInput.getText(), (receivedChatMessage) -> {
            final Date date = new Date(receivedChatMessage.getTimestamp());
            messagesArea.appendText(String.format("%s - %s: %s\n", date, receivedChatMessage.getUser(), receivedChatMessage.getMessage()));
        });
    }

    private void onSend(ActionEvent actionEvent) {
        clientController.sendMessage(username, message.getText(), (data) -> {
            if (data) {
                message.setText("");
            }
        });
    }

    private void onShowUsers(ActionEvent actionEvent) {
        clientController.getUsers(data -> {
            if (!data.isEmpty()) {
                Platform.runLater(() -> AlertUtils.showUsers(data));
            }
        });
    }


    private Scene createChatScene() {
        //Panel Dividido
        SplitPane splitPane = new SplitPane();
        splitPane.setOrientation(Orientation.VERTICAL);
        splitPane.setPrefHeight(400.0);
        splitPane.setPrefWidth(600.0);
        splitPane.setDividerPositions(0.8065326633165829);
        //Sección Arriba (Texto y Area de mensajes)
        AnchorPane messagesSection = new AnchorPane();
        messagesSection.setMinHeight(0);
        messagesSection.setMinWidth(0);
        messagesSection.setPrefHeight(100);
        messagesSection.setPrefWidth(160);
        //Area de mensajes
        messagesArea.setLayoutX(11);
        messagesArea.setLayoutY(40);
        messagesArea.setEditable(false);
        messagesArea.setPrefHeight(263);
        messagesArea.setPrefWidth(573);
        //Texto
        Text chatText = new Text();
        chatText.setLayoutX(279);
        chatText.setLayoutY(30);
        chatText.setStrokeType(StrokeType.OUTSIDE);
        chatText.setStrokeWidth(0);
        chatText.setText("CHAT");
        //Rellenar Sección Arriba
        messagesSection.getChildren().addAll(messagesArea, chatText);
        //Sección Abajo (Texto, Input de mensajes y Botón Enviar)
        AnchorPane writeSection = new AnchorPane();
        writeSection.setMinHeight(0);
        writeSection.setMinWidth(0);
        writeSection.setPrefHeight(100);
        writeSection.setPrefWidth(160);
        //Botón Enviar
        send.setLayoutX(511);
        send.setLayoutY(16);
        send.setMnemonicParsing(false);
        send.setText("Send");
        send.setOnAction(this::onSend);
        //Botón Usuarios
        users.setLayoutX(511);
        users.setLayoutY(46);
        users.setMnemonicParsing(false);
        users.setText("Show Users");
        users.setOnAction(this::onShowUsers);
        //Input de mensajes
        message.setLayoutX(128);
        message.setLayoutY(21);
        message.setPrefHeight(31);
        message.setPrefWidth(370);
        //Texto
        Text writeSomething = new Text();
        writeSomething.setLayoutX(14);
        writeSomething.setLayoutY(42);
        writeSomething.setStrokeType(StrokeType.OUTSIDE);
        writeSomething.setStrokeWidth(0);
        writeSomething.setText("write something:");
        //Rellenar Sección Abajo
        writeSection.getChildren().addAll(send, message, writeSomething, users);
        //Fusion todos
        splitPane.getItems().addAll(messagesSection, writeSection);
        return new Scene(splitPane);
    }
}
