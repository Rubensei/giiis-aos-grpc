package es.unex.giiis.aos.grpc.client.ui.chat;

import es.unex.giiis.aos.grpc.client.ClientController;
import es.unex.giiis.aos.grpc.client.utils.AlertUtils;
import es.unex.giiis.aos.grpc.client.utils.MessageUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ChatController {
    private final ClientController clientController = ClientController.getClientController();
    public Button showUsersButton;
    public TextArea chatMessageArea;
    public TextField messageInput;
    public Button sendButton;
    public String username;

    @FXML
    public void onSendHandler() {
        clientController.sendMessage(username, messageInput.getText(), (data) -> {
            if (data) {
                messageInput.setText("");
            }
        });
    }

    @FXML
    public void onShowUsersHandler() {
        clientController.getUsers(data -> {
            if (!data.isEmpty()) {
                Platform.runLater(() -> AlertUtils.showUsers(data));
            }
        });
    }

    public void subscribe(String username) {
        clientController.subscribe(username, (receivedChatMessage) -> {
            chatMessageArea.appendText(MessageUtils.formatMessage(receivedChatMessage));
        });
    }
}
