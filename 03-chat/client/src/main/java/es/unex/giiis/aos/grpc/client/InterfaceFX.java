package es.unex.giiis.aos.grpc.client;

import es.unex.giiis.aos.grpc.client.ui.chat.ChatScreen;
import es.unex.giiis.aos.grpc.client.ui.home.HomeScreen;
import es.unex.giiis.aos.grpc.client.utils.AlertUtils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Date;

public class InterfaceFX extends Application {
    private final ClientController clientController = ClientController.getClientController();

    private Stage window;

    private ChatScreen chatScreen;

    @Override
    public void start(Stage stage) throws IOException {
        window = stage;
        HomeScreen homeScreen = new HomeScreen(this::onJoin);

        window.setScene(homeScreen);
        window.show();
    }

    private void onJoin(String username) {
        Platform.runLater(() -> {
            try {
                chatScreen = new ChatScreen(username);
                window.setScene(chatScreen);

                window.setOnCloseRequest((request) -> {
                    if (!username.isBlank()) {
                        request.consume();
                        clientController.unsubscribe(username, (data) -> {
                            Platform.runLater(window::close);
                        });
                    }
                });

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
