package dev.monocle;

import org.web3j.protocol.websocket.events.Notification;
import org.web3j.protocol.websocket.events.NotificationParams;

import java.math.BigInteger;

public final class Web3jUtils {

    private Web3jUtils() {}

    public static int hexToInteger(String hex) {
        if (!hex.startsWith("0x")) {
            throw new IllegalArgumentException("Expected hex string to start with '0x' prefix");
        }
        String suffix = hex.substring(2);
        return Integer.parseInt(suffix, 16);
    }

    public static BigInteger hexToBigInteger(String hex) {
        if (!hex.startsWith("0x")) {
            throw new IllegalArgumentException("Expected hex string to start with '0x' prefix");
        }
        String suffix = hex.substring(2);
        return new BigInteger(suffix, 16);
    }

    /**
     * Extracts a {@link Notification} result.
     *
     * @param notification the notification to extract the result from.
     * @param <T> the generic type of the result.
     * @return the extracted result.
     */
    public static <T> T extractNotificationResult(Notification<T> notification) {
        NotificationParams<T> params = notification.getParams();
        return params.getResult();
    }
}
