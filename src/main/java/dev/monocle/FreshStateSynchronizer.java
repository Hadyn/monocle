package dev.monocle;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.websocket.events.NewHead;

import java.io.IOException;
import java.util.List;

public final class FreshStateSynchronizer {

    private final Web3j httpClient;
    private final Web3j socketClient;

    public FreshStateSynchronizer(Web3j httpClient, Web3j webSocketClient) {
        this.httpClient = httpClient;
        this.socketClient = webSocketClient;
    }

    public void replay() throws IOException {
        socketClient
            .newHeadsNotifications()
            .map(Web3jUtils::extractNotificationResult)
            .subscribe(this::handleNewHead);
    }

    private void handleNewHead(NewHead head){
        try {
            EthLog logsResponse = httpClient
                .ethGetLogs(new EthFilter(head.getHash()))
                .send();

            List<EthLog.LogResult> logs = logsResponse.getLogs();
            if (logs == null) {
                return;
            }
    
            for (EthLog.LogResult<EthLog.LogObject> result : logs) {
                EthLog.LogObject log = result.get();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
