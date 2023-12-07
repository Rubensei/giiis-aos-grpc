package es.unex.giiis.aos.grpc.client.callbacks;

import es.unex.giiis.aos.grpc.client.utils.AlertUtils;
import es.unex.giiis.aos.grpc.generated.Chat;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import javafx.application.Platform;

public class SubscribeCallback implements StreamObserver<Chat.ReceivedChatMessage> {
    private final ValueCallback<Chat.ReceivedChatMessage> handler;

    public SubscribeCallback(ValueCallback<Chat.ReceivedChatMessage> handler) {
        this.handler = handler;
    }

    @Override
    public void onNext(Chat.ReceivedChatMessage value) {
        handler.onValue(value);
        System.out.println(" ONNEXT SUBSCRIBE");
    }

    @Override
    public void onError(Throwable t) {
        System.out.println(" ERROR ON SUBSCRIBE");
        if(t instanceof StatusRuntimeException) {
            Platform.runLater(() -> {
                AlertUtils.showAlert(((StatusRuntimeException) t).getStatus().getDescription());
            });
        }
    }

    @Override
    public void onCompleted() {
        System.out.println(" COMPLETED ON SUBSCRIBE ");
    }
}
