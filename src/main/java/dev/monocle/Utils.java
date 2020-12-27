package dev.monocle;

import org.web3j.protocol.websocket.events.Notification;
import org.web3j.protocol.websocket.events.NotificationParams;

public final class Utils {

    private Utils() {}

    /**
     * Extracts a {@link Notification} result.
     *
     * @param notification the notification to extract the result from.
     * @param <T> the generic type of the result.
     * @return the extracted result.
     */
    public static <T> T extractResult(Notification<T> notification) {
        NotificationParams<T> params = notification.getParams();
        return params.getResult();
    }
}
