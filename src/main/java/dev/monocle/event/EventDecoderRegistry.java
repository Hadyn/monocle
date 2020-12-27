package dev.monocle.event;

import org.web3j.protocol.websocket.events.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class EventDecoderRegistry {

    private final Map<String, Map<String, EventDecoder>> decoders = new HashMap<>();

    public AbstractEvent decode(Log log) {
        Map<String, EventDecoder> contractEventDecoders = decoders.get(log.getAddress());
        if (contractEventDecoders == null) {
            return null;
        }

        List<String> topics = log.getTopics();

        EventDecoder decoder = contractEventDecoders.get(topics.get(0));
        if (decoder == null) {
            return null;
        }

        return null;
    }
}
