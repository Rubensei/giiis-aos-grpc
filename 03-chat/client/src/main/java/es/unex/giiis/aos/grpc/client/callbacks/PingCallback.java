package es.unex.giiis.aos.grpc.client.callbacks;

import es.unex.giiis.aos.grpc.generated.Chat;
import io.grpc.stub.StreamObserver;

public class PingCallback implements StreamObserver<Chat.PongMessage> {
    final ValueCallback<Boolean> handler;

    public PingCallback(ValueCallback<Boolean> handler) {
        this.handler = handler;
    }

    @Override
    public void onNext(Chat.PongMessage value) {
        System.out.println("PONG RECEIVED");
    }

    @Override
    public void onError(Throwable t) {
        System.out.println("PING ERROR");
        handler.onValue(false);
    }

    @Override
    public void onCompleted() {
        System.out.println("PING OK");
        handler.onValue(true);
    }
}
