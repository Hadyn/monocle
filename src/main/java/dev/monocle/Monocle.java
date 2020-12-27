package dev.monocle;

import com.google.common.collect.Lists;
import dev.monocle.uniswap.UniswapSyncEvent;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.websocket.WebSocketClient;
import org.web3j.protocol.websocket.WebSocketService;

import java.net.URI;

public final class Monocle {

    private static final String PROJECT_ID = "f4d83078681743bc9bd2d60877454b53";
    private static final String UNISWAP_FACTORY_ADDRESS = "0x5C69bEe701ef814a2B6a3EDD4B1652CB9cc5aA6f";
    private static final String UNISWAP_DAI_ETH_ADDRESS = "0xa478c2975ab1ea89e8196811f51a7b7ade33eb11";

    public static void main(String... args) throws Throwable {
        WebSocketClient webSocketClient = new WebSocketClient(
            URI.create(String.format("wss://mainnet.infura.io/ws/v3/%s", PROJECT_ID))
        );

        WebSocketService service = new WebSocketService(webSocketClient, false);
        service.connect();

        Web3j client = Web3j.build(service);

        client
            .newHeadsNotifications()
            .map(Utils::extractResult)
            .subscribe(head -> {

            });

        client
            .logsNotifications(
                Lists.newArrayList(UNISWAP_DAI_ETH_ADDRESS),
                Lists.newArrayList(UniswapSyncEvent.ENCODED_SIGNATURE)
            )
            .map(Utils::extractResult)
            .map(UniswapSyncEvent::decode)
            .subscribe(event -> {
                System.out.println(event.getReserve0());
                System.out.println(event.getReserve1());
            });
    }
}

