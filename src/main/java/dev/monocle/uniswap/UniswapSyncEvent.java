package dev.monocle.uniswap;

import com.google.common.collect.Lists;
import dev.monocle.event.EventDecoderException;
import dev.monocle.event.EventValues;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.generated.Uint112;
import org.web3j.protocol.websocket.events.Log;

import java.math.BigInteger;

public final class UniswapSyncEvent {

    public static final Event DEFINITION = new Event(
        "Sync",
        Lists.newArrayList(
            new TypeReference<Uint112>() {},
            new TypeReference<Uint112>() {}
        )
    );

    public static final String ENCODED_SIGNATURE = EventEncoder.encode(DEFINITION);

    private final BigInteger reserve0;
    private final BigInteger reserve1;

    private UniswapSyncEvent(EventValues values) {
        this.reserve0 = values.expectUint112(0);
        this.reserve1 = values.expectUint112(1);
    }

    public static UniswapSyncEvent decode(Log log) throws EventDecoderException {
        return new UniswapSyncEvent(EventValues.decode(DEFINITION, log));
    }

    public BigInteger getReserve0() {
        return reserve0;
    }

    public BigInteger getReserve1() {
        return reserve1;
    }
}
