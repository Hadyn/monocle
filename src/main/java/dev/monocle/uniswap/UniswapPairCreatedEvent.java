package dev.monocle.uniswap;

import com.google.common.collect.Lists;
import dev.monocle.event.AbstractEvent;
import dev.monocle.event.EventDecoderException;
import dev.monocle.event.EventValues;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Uint;
import org.web3j.protocol.websocket.events.Log;

import java.math.BigInteger;

public final class UniswapPairCreatedEvent extends AbstractEvent {

    public static final Event DEFINITION = new Event(
        "PairCreated",
        Lists.newArrayList(
            new TypeReference<Address>(true) {},
            new TypeReference<Address>(true) {},
            new TypeReference<Address>() {},
            new TypeReference<Uint>() {}
        )
    );

    public static final String ENCODED_SIGNATURE = EventEncoder.encode(DEFINITION);

    /**
     * The address of the first token in the pair.
     */
    private final Address token0;

    /**
     * The address of the second token in the pair.
     */
    private final Address token1;

    /**
     * The address of the pair that was created.
     */
    private final Address pair;

    /**
     * The number of pairs that existed at the point that this event was emitted.
     */
    private final BigInteger count;

    public UniswapPairCreatedEvent(Address token0, Address token1, Address pair, BigInteger count) {
        this.token0 = token0;
        this.token1 = token1;
        this.pair = pair;
        this.count = count;
    }

    private UniswapPairCreatedEvent(EventValues values) {
        this.token0 = values.expectAddress(0);
        this.token1 = values.expectAddress(1);
        this.pair = values.expectAddress(2);
        this.count = values.expectUint(3);
    }

    /**
     * Decodes a log into a {@link UniswapPairCreatedEvent}.
     *
     * @param log the log to decode.
     * @return the decoded {@link UniswapPairCreatedEvent} event.
     *
     * @throws EventDecoderException
     */
    public static UniswapPairCreatedEvent decode(Log log) throws EventDecoderException  {
        return new UniswapPairCreatedEvent(EventValues.decode(DEFINITION, log));
    }

    public Address getToken0() {
        return token0;
    }

    public Address getToken1() {
        return token1;
    }

    public Address getPair() {
        return pair;
    }

    public BigInteger getCount() {
        return count;
    }
}
