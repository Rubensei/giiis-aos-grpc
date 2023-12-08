package es.unex.giiis.aos.grpc.client.ui.home;

import es.unex.giiis.aos.grpc.client.ClientController;
import es.unex.giiis.aos.grpc.client.callbacks.ValueCallback;
import es.unex.giiis.aos.grpc.client.utils.AlertUtils;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class HomeScreen extends Scene {
    private static final FXMLLoader loader = new FXMLLoader(HomeScreen.class.getClassLoader().getResource("home.fxml"));

    public HomeScreen(ValueCallback<String> onJoinCompleted) throws IOException {
        super(loader.load());
        HomeController controller = loader.getController();
        controller.setOnJoinHandler(onJoinCompleted);
    }
}
