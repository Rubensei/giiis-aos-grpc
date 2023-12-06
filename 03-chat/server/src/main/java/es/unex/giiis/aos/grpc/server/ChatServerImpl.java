package es.unex.giiis.aos.grpc.server;

import com.google.rpc.Code;
import com.google.rpc.ErrorInfo;
import es.unex.giiis.aos.grpc.generated.Chat.*;
import es.unex.giiis.aos.grpc.generated.ChatServiceGrpc;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

import java.util.*;

public class ChatServerImpl extends ChatServiceGrpc.ChatServiceImplBase {

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
                .addAllUsers(subscriptions.keySet())
                .build();
        responseObserver.onNext(usersResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void sendMessage(SentChatMessage request, StreamObserver<EmptyMessage> responseObserver) {
        final String username = request.getUser();
        final String message = request.getMessage();

        if (username.isBlank()) {
            final ErrorInfo errorInfo = ErrorInfo.newBuilder()
                    .setReason("Invalid username")
                    .build();
            final Throwable error = ErrorUtils.buildError("Invalid username", Code.INVALID_ARGUMENT, errorInfo);
            responseObserver.onError(error);
            return;
        } else if (!subscriptions.containsKey(username)) {
            final ErrorInfo errorInfo = ErrorInfo.newBuilder()
                    .setReason("Username not found")
                    .build();
            final Throwable error = ErrorUtils.buildError("Username not found", Code.NOT_FOUND, errorInfo);
            responseObserver.onError(error);
            return;
        }

        if (message.isBlank()) {
            final ErrorInfo errorInfo = ErrorInfo.newBuilder()
                    .setReason("Invalid message")
                    .build();
            final Throwable error = ErrorUtils.buildError("Invalid message", Code.INVALID_ARGUMENT, errorInfo);
            responseObserver.onError(error);
            return;
        }

        final ReceivedChatMessage messageResponse = ReceivedChatMessage.newBuilder()
                .setUser(username)
                .setMessage(message)
                .setTimestamp(new Date().getTime())
                .build();

        final Set<String> disconnectedUsers = new HashSet<>();
        for (final Map.Entry<String, StreamObserver<ReceivedChatMessage>> entry : subscriptions.entrySet()) {
            try {
                entry.getValue().onNext(messageResponse);
            } catch (StatusRuntimeException error) {
                disconnectedUsers.add(entry.getKey());
            }
        }
        disconnectedUsers.forEach(subscriptions::remove);

        responseObserver.onNext(EmptyMessage.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void subscribe(UsernameMessage request, StreamObserver<ReceivedChatMessage> responseObserver) {
        final String username = request.getUsername();
        if (username.isBlank()) {
            final ErrorInfo errorInfo = ErrorInfo.newBuilder()
                    .setReason("Invalid username")
                    .build();
            final Throwable error = ErrorUtils.buildError("Invalid username", Code.INVALID_ARGUMENT, errorInfo);
            responseObserver.onError(error);
            return;
        } else if (subscriptions.containsKey(username)) {
            final ErrorInfo errorInfo = ErrorInfo.newBuilder()
                    .setReason("Duplicated username")
                    .build();
            final Throwable error = ErrorUtils.buildError("Duplicated username", Code.ALREADY_EXISTS, errorInfo);
            responseObserver.onError(error);
            return;
        }
        subscriptions.put(username, responseObserver);
    }

    @Override
    public void unsubscribe(UsernameMessage request, StreamObserver<EmptyMessage> responseObserver) {
        final String username = request.getUsername();
        if (!subscriptions.containsKey(username)) {
            final ErrorInfo errorInfo = ErrorInfo.newBuilder()
                    .setReason("Username not found")
                    .build();
            final Throwable error = ErrorUtils.buildError("Username not found", Code.NOT_FOUND, errorInfo);
            responseObserver.onError(error);
            return;
        }
        subscriptions.remove(username).onCompleted();
        responseObserver.onNext(EmptyMessage.newBuilder().build());
        responseObserver.onCompleted();
    }
}
