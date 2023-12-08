package es.unex.giiis.aos.grpc.client.ui.chat;

import es.unex.giiis.aos.grpc.client.ui.home.HomeScreen;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class ChatScreen extends Scene {
    private static final FXMLLoader loader = new FXMLLoader(HomeScreen.class.getClassLoader().getResource("chat.fxml"));

    public ChatScreen(String username) throws IOException {
        super(loader.load());
        ChatController controller = loader.getController();
        controller.username = username;
        controller.subscribe(username);
    }
}
