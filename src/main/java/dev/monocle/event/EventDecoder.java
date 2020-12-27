package dev.monocle.event;

import org.web3j.protocol.websocket.events.Log;

import java.util.function.Function;

public interface EventDecoder<T extends AbstractEvent> extends Function<Log, T> {

    /**
     * Decodes an event from a {@link Log}.
     *
     * @param log the log to decode into an event.
     * @return the decoded event.
     * @throws EventDecoderException
     */
    T decode(Log log) throws EventDecoderException;
}
