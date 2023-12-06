package es.unex.giiis.aos.grpc.server;

import es.unex.giiis.aos.grpc.generated.Chat.*;
import es.unex.giiis.aos.grpc.generated.ChatServiceGrpc;
import io.grpc.StatusException;
import io.grpc.stub.StreamObserver;

import java.util.HashMap;
import java.util.Map;

public class ChatServerImpl extends ChatServiceGrpc.ChatServiceImplBase {

    private final ChatServerService service = ChatServerService.get();
    private final Map<String, StreamObserver<ReceivedChatMessage>> subscriptions = new HashMap<>();

    @Override
    public void ping(EmptyMessage request, StreamObserver<PongMessage> responseObserver) {
        final PongMessage pong = PongMessage.newBuilder().build();
        responseObserver.onNext(pong);
        responseObserver.onCompleted();
    }

    @Override
    public void getUsers(EmptyMessage request, StreamObserver<UsersResponse> responseObserver) {
        final UsersResponse usersResponse = UsersResponse.newBuilder()
                .addAllUsers(service.getSubscribers())
                .build();
        responseObserver.onNext(usersResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void sendMessage(SentChatMessage request, StreamObserver<EmptyMessage> responseObserver) {
        final String username = request.getUser();
        final String message = request.getMessage();

        try {
            service.send(username, message);

            responseObserver.onNext(EmptyMessage.newBuilder().build());
            responseObserver.onCompleted();
        } catch (StatusException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void subscribe(UsernameMessage request, StreamObserver<ReceivedChatMessage> responseObserver) {
        final String username = request.getUsername();
        try {
            service.subscribe(username, responseObserver);
        } catch (StatusException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void unsubscribe(UsernameMessage request, StreamObserver<EmptyMessage> responseObserver) {
        final String username = request.getUsername();
        try {
            service.unsubscribe(username).onCompleted();
            responseObserver.onNext(EmptyMessage.newBuilder().build());
            responseObserver.onCompleted();
        } catch (StatusException e) {
            responseObserver.onError(e);
        }
    }
}
