package es.unex.giiis.aos.grpc.server;

import com.google.rpc.Code;
import com.google.rpc.ErrorInfo;
import es.unex.giiis.aos.grpc.generated.Chat;
import es.unex.giiis.aos.grpc.server.utils.ErrorUtils;
import io.grpc.StatusException;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

import java.util.*;

public class ChatServerService {
    private static ChatServerService instance = null;

    private final Map<String, StreamObserver<Chat.ReceivedChatMessage>> subscriptions = new HashMap<>();

    private ChatServerService() {}

    public static ChatServerService get() {
        if(ChatServerService.instance == null) {
            ChatServerService.instance = new ChatServerService();
        }
        return ChatServerService.instance;
    }

    public List<String> getSubscribers() {
        return new ArrayList<>(subscriptions.keySet());
    }

    public void send(String key, String message) throws StatusException {
        if (key.isBlank()) {
            final ErrorInfo errorInfo = ErrorInfo.newBuilder()
                    .setReason("Invalid username")
                    .build();
            throw ErrorUtils.buildError("Invalid username", Code.INVALID_ARGUMENT, errorInfo);
        } else if (!subscriptions.containsKey(key)) {
            final ErrorInfo errorInfo = ErrorInfo.newBuilder()
                    .setReason("Username not found")
                    .build();
            throw ErrorUtils.buildError("Username not found", Code.NOT_FOUND, errorInfo);
        }

        final Chat.ReceivedChatMessage validatedMessage = validateAndTransformSendEvent(key, message);

        final Set<String> disconnectedUsers = new HashSet<>();
        for (final Map.Entry<String, StreamObserver<Chat.ReceivedChatMessage>> entry : subscriptions.entrySet()) {
            try {
                entry.getValue().onNext(validatedMessage);
            } catch (StatusRuntimeException error) {
                disconnectedUsers.add(entry.getKey());
            }
        }
        disconnectedUsers.forEach(subscriptions::remove);
    }

    public void subscribe(String key, StreamObserver<Chat.ReceivedChatMessage> handler) throws StatusException {
        if (key.isBlank()) {
            final ErrorInfo errorInfo = ErrorInfo.newBuilder()
                    .setReason("Invalid username")
                    .build();
            throw ErrorUtils.buildError("Invalid username", Code.INVALID_ARGUMENT, errorInfo);
        } else if (subscriptions.containsKey(key)) {
            final ErrorInfo errorInfo = ErrorInfo.newBuilder()
                    .setReason("Duplicated username")
                    .build();
            throw  ErrorUtils.buildError("Duplicated username", Code.ALREADY_EXISTS, errorInfo);
        }
        subscriptions.put(key, handler);
    }
    public StreamObserver<Chat.ReceivedChatMessage> unsubscribe(String key) throws StatusException {
        if (!subscriptions.containsKey(key)) {
            final ErrorInfo errorInfo = ErrorInfo.newBuilder()
                    .setReason("Username not found")
                    .build();
            throw ErrorUtils.buildError("Username not found", Code.NOT_FOUND, errorInfo);
        }
        return subscriptions.remove(key);
    }

    private Chat.ReceivedChatMessage validateAndTransformSendEvent(String key, String message) throws StatusException {
        if(message.isBlank()) {
            final ErrorInfo errorInfo = ErrorInfo.newBuilder()
                    .setReason("Invalid message")
                    .build();
            throw ErrorUtils.buildError("Invalid message", Code.INVALID_ARGUMENT, errorInfo);
        }

        return Chat.ReceivedChatMessage.newBuilder()
                .setUser(key)
                .setMessage(message)
                .setTimestamp(new Date().getTime())
                .build();
    }
}
