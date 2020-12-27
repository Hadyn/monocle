package dev.monocle.event;

import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;
import org.web3j.protocol.websocket.events.Log;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public final class EventValues {

    private final List<Type> values;

    public EventValues(List<Type> values) {
        this.values = values;
    }

    public static EventValues decode(Event event, Log log) throws EventDecoderException {
        return new EventValues(extractEventValues(event, log));
    }

    private static List<Type> extractEventValues(Event event, Log log) throws EventDecoderException {
        List<String> topics = log.getTopics();

        if (topics == null || topics.size() == 0) {
            throw new EventDecoderException("Missing encoded signature");
        }

        String expectedEventSignature = EventEncoder.encode(event);
        String actualEventSignature = topics.get(0);

        if (!expectedEventSignature.equals(actualEventSignature)) {
            throw new EventDecoderException("Signature mismatch");
        }

        List<Type> indexedValues = new ArrayList<>();

        List<TypeReference<Type>> indexedParameters = event.getIndexedParameters();
        for (int i = 0; i < indexedParameters.size(); i++) {
            Type value = FunctionReturnDecoder.decodeIndexedValue(
                topics.get(i + 1),
                indexedParameters.get(i)
            );
            indexedValues.add(value);
        }

        List<Type> nonIndexedValues = FunctionReturnDecoder.decode(
            log.getData(),
            event.getNonIndexedParameters()
        );

        List<Type> extractedValues = new ArrayList<>();

        int indexedOffset = 0;
        int nonIndexedOffset = 0;

        for (TypeReference<Type> reference : event.getParameters()) {
            if (reference.isIndexed()) {
                extractedValues.add(indexedValues.get(indexedOffset++));
            } else {
                extractedValues.add(nonIndexedValues.get(nonIndexedOffset++));
            }
        }

        return extractedValues;
    }

    public Address expectAddress(int index) {
        Type type = values.get(index);
        if(!type.getTypeAsString().equals("address")) {
            throw new IllegalStateException("Expected type to be an Address");
        }
        return (Address) type;
    }

    public BigInteger expectUint112(int index) {
        Type type = values.get(index);
        if(!type.getTypeAsString().equals("uint112")) {
            throw new IllegalStateException("Expected type to be a Uint112");
        }

        Uint casted = (Uint) type;
        return casted.getValue();
    }

    public BigInteger expectUint(int index) {
        Type type = values.get(index);
        if(!type.getTypeAsString().equals("uint")) {
            throw new IllegalStateException("Expected type to be a Uint");
        }

        Uint casted = (Uint) type;
        return casted.getValue();
    }
}
