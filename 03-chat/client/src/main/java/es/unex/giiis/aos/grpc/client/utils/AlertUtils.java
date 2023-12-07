package es.unex.giiis.aos.grpc.client.utils;

import javafx.scene.control.Alert;

import java.util.List;

public class AlertUtils {
    public static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(message);
        alert.show();
    }

    public static void showUsers(List<String> users) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        String text = "USUARIOS:\n" + String.join(",\n", users);
        alert.setHeaderText(text);
        alert.show();
    }
}
