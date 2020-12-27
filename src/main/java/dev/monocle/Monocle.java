package dev.monocle;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.websocket.WebSocketClient;
import org.web3j.protocol.websocket.WebSocketService;

import java.math.BigInteger;
import java.net.URI;

public final class Monocle {

    private static final String PROJECT_ID = "f4d83078681743bc9bd2d60877454b53";
    private static final String UNISWAP_FACTORY_ADDRESS = "0x5C69bEe701ef814a2B6a3EDD4B1652CB9cc5aA6f";
    private static final String UNISWAP_DAI_ETH_ADDRESS = "0xa478c2975ab1ea89e8196811f51a7b7ade33eb11";

    public static void main(String... args) throws Throwable {
        HttpService httpService = new HttpService(
            String.format("https://mainnet.infura.io/v3/%s", PROJECT_ID)
        );

        WebSocketClient webSocketClient = new WebSocketClient(
            URI.create(String.format("wss://mainnet.infura.io/ws/v3/%s", PROJECT_ID))
        );

        WebSocketService webSocketService = new WebSocketService(webSocketClient, false);
        webSocketService.connect();

        Web3j httpClient = Web3j.build(httpService);
        Web3j socketClient = Web3j.build(webSocketService);

        EthBlockNumber response = httpClient.ethBlockNumber().send();
        BigInteger currentBlockNumber = response.getBlockNumber();

        StaleStateSynchronizer synchronizer = new StaleStateSynchronizer(httpClient);

        synchronizer.replay(
            UNISWAP_FACTORY_ADDRESS,
            BigInteger.valueOf(10_008_355),
            currentBlockNumber,
            BigInteger.valueOf(100_000)
        );
    }
}

