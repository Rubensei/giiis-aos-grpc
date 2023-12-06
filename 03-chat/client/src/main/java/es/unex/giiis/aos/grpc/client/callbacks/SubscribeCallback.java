package es.unex.giiis.aos.grpc.client.callbacks;

import es.unex.giiis.aos.grpc.generated.Chat;
import io.grpc.stub.StreamObserver;

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
        t.printStackTrace();
    }

    @Override
    public void onCompleted() {
        System.out.println(" COMPLETED ON SUBSCRIBE ");
    }
}
