package es.unex.giiis.aos.grpc.client.ui.home;

import es.unex.giiis.aos.grpc.client.ClientController;
import es.unex.giiis.aos.grpc.client.callbacks.ValueCallback;
import es.unex.giiis.aos.grpc.client.utils.AlertUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class HomeController {
    private final ClientController clientController = ClientController.getClientController();
    public TextField usernameInput;
    public Button joinButton;
    private ValueCallback<String> joinHandler = null;

    public void setOnJoinHandler(ValueCallback<String> handler) {
        joinHandler = handler;
    }

    @FXML
    protected void handleJoinClick() {
        final String username = usernameInput.getText();
        if(username.isBlank()) return;
        clientController.ping(connected -> {
            if(!connected) {
                Platform.runLater(() -> AlertUtils.showAlert("Server connection issue"));
                return;
            }

            if(joinHandler != null) joinHandler.onValue(username);
        });
    }
}
