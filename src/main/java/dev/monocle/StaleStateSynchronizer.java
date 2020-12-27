package dev.monocle;

import com.google.common.collect.Lists;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthLog;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

public final class StaleStateSynchronizer {

    private final Web3j client;

    public StaleStateSynchronizer(Web3j client) {
        this.client = client;
    }

    @SuppressWarnings("unchecked")
    public void replay(
        String address,
        long startingBlockNumber,
        long endingBlockNumber,
        long maximumSpan
    ) throws IOException {
        long currentBlockNumber = startingBlockNumber;

        while (currentBlockNumber != endingBlockNumber) {
            long span = maximumSpan;
            if (currentBlockNumber + maximumSpan > endingBlockNumber) {
                span = endingBlockNumber - currentBlockNumber;
            }

            long fromBlockNumber = currentBlockNumber;
            long toBlockNumber = fromBlockNumber + span;

            currentBlockNumber = currentBlockNumber + span

            EthLog logsResponse = client
                .ethGetLogs(
                    new EthFilter(
                        DefaultBlockParameter.valueOf(BigInteger.valueOf(fromBlockNumber)),
                        DefaultBlockParameter.valueOf(BigInteger.valueOf(toBlockNumber)),
                        Lists.newArrayList(address)
                    )
                )
                .send();

            List<EthLog.LogResult> logs = logsResponse.getLogs();
            if (logs == null) {
                continue;
            }

            for (EthLog.LogResult<EthLog.LogObject> result : logs) {
                EthLog.LogObject log = result.get();
                System.out.println(log.getBlockNumber());
            }
        }
    }
}
