package es.unex.giiis.aos.grpc.client;

import es.unex.giiis.aos.grpc.client.callbacks.*;
import es.unex.giiis.aos.grpc.generated.Chat;
import es.unex.giiis.aos.grpc.generated.ChatServiceGrpc;
import io.grpc.*;

import java.util.List;

public class ClientController {
    private final ChatServiceGrpc.ChatServiceStub serviceStub;
    private static ClientController instance = null;

    private ClientController() {
        String target = "localhost:50051"; // CAMBIAR URL AQUI
        ManagedChannel channel = Grpc.newChannelBuilder(target, InsecureChannelCredentials.create())
                .build();
        this.serviceStub = ChatServiceGrpc.newStub(channel);
    }

    public static ClientController getClientController() {
        if (ClientController.instance == null) {
            ClientController.instance = new ClientController();
        }
        return ClientController.instance;
    }

    public void ping(ValueCallback<Boolean> onResult) {
        final Chat.EmptyMessage request = Chat.EmptyMessage.newBuilder().build();
        serviceStub.ping(request, new PingCallback(onResult));
    }

    public void getUsers(ValueCallback<List<String>> onResult) {
        final Chat.EmptyMessage request = Chat.EmptyMessage.newBuilder().build();
        serviceStub.getUsers(request, new GetUsersCallback(onResult));
    }

    public void sendMessage(String username, String message, ValueCallback<Boolean> onComplete) {
        final Chat.SentChatMessage sentChatMessage = Chat.SentChatMessage
                .newBuilder()
                .setUser(username)
                .setMessage(message)
                .build();
        serviceStub.sendMessage(sentChatMessage, new SendCallback(onComplete));
    }

    public void subscribe(String username, ValueCallback<Chat.ReceivedChatMessage> onValue) {
        final Chat.UsernameMessage usernameMessage = Chat.UsernameMessage.newBuilder().setUsername(username).build();
        serviceStub.subscribe(usernameMessage, new SubscribeCallback(onValue));
    }

    public void unsubscribe(String username, ValueCallback<Boolean> onValue) {
        final Chat.UsernameMessage usernameMessage = Chat.UsernameMessage.newBuilder().setUsername(username).build();
        serviceStub.unsubscribe(usernameMessage, new UnsubscribeCallback(onValue));
    }
}
