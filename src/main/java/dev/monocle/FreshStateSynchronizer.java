package dev.monocle;

import com.google.common.collect.Lists;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.websocket.WebSocketClient;
import org.web3j.protocol.websocket.events.NewHead;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

public final class FreshStateSynchronizer {

    private final Web3j httpClient;
    private final Web3j socketClient;

    public FreshStateSynchronizer(Web3j httpClient, Web3j socketClient) {
        this.httpClient = httpClient;
        this.socketClient = socketClient;
    }

    @SuppressWarnings("unchecked")
    public void replay() throws IOException {

        socketClient.newHeadsNotifications().map(Web3jUtils::extractNotificationResult).subscribe(this::logAndSave);
 
    }

    public void logAndSave(NewHead head){

        String num = head.getNumber();

        BigInteger fromBlockNumber = Web3jUtils.hexToBigInteger(num);

        try{
            EthLog logsResponse = httpClient
            .ethGetLogs(
                new EthFilter(
                    DefaultBlockParameter.valueOf(fromBlockNumber),
                    DefaultBlockParameter.valueOf(fromBlockNumber),
                    Lists.newArrayList()
                )
            )
            .send();

            List<EthLog.LogResult> logs = logsResponse.getLogs();
            if (logs == null) {
                return;
            }
    
            for (EthLog.LogResult<EthLog.LogObject> result : logs) {
                EthLog.LogObject log = result.get();
                System.out.println(log.getBlockNumber());
            }
        } catch (Exception e) {
            
        }

    }
}
