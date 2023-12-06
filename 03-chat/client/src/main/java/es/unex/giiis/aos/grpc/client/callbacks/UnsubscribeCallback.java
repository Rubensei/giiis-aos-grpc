package es.unex.giiis.aos.grpc.client.callbacks;

import es.unex.giiis.aos.grpc.generated.Chat;
import io.grpc.stub.StreamObserver;

public class UnsubscribeCallback implements StreamObserver<Chat.EmptyMessage> {
    private final ValueCallback<Boolean> handler;

    public UnsubscribeCallback(ValueCallback<Boolean> handler) {
        this.handler = handler;
    }

    @Override
    public void onNext(Chat.EmptyMessage value) {}

    @Override
    public void onError(Throwable t) {
        handler.onValue(false);
    }

    @Override
    public void onCompleted() {
        handler.onValue(true);
    }
}
