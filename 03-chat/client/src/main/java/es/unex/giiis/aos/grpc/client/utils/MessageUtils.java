package es.unex.giiis.aos.grpc.client.utils;

import es.unex.giiis.aos.grpc.generated.Chat;

import java.time.LocalDateTime;

public class MessageUtils {
    public static String formatMessage(Chat.ReceivedChatMessage message) {
        return String.format("%s - %s: %s\n",
                getTime(),
                message.getUser(),
                message.getMessage()
        );
    }

    private static String getTime() {
        final LocalDateTime now = LocalDateTime.now();
        return String.format("%02d:%02d",
                now.getHour(),
                now.getMinute()
        );
    }
}
