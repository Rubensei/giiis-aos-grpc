package es.unex.giiis.aos.grpc.client.callbacks;

import es.unex.giiis.aos.grpc.generated.Chat;
import io.grpc.stub.StreamObserver;

import java.util.List;

public class GetUsersCallback implements StreamObserver<Chat.UsersResponse> {
    final ValueCallback<List<String>> handler;

    public GetUsersCallback(ValueCallback<List<String>> handler) {
        this.handler = handler;
    }

    @Override
    public void onNext(Chat.UsersResponse value) {
        handler.onValue(value.getUsersList().subList(0, value.getUsersCount()));
    }

    @Override
    public void onError(Throwable t) {
        System.out.println("GETUSERS ERROR");
    }

    @Override
    public void onCompleted() {
        System.out.println("GETUSERS OK");
    }
}