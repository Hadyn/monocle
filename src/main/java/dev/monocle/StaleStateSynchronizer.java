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
        BigInteger startingBlockNumber,
        BigInteger endingBlockNumber,
        BigInteger maximumSpan
    ) throws IOException {
        BigInteger currentBlockNumber = new BigInteger(startingBlockNumber.toByteArray());

        while (currentBlockNumber.compareTo(endingBlockNumber) != 0) {
            BigInteger span = maximumSpan;
            if (endingBlockNumber.compareTo(currentBlockNumber.add(maximumSpan)) < 0) {
                span = endingBlockNumber.subtract(currentBlockNumber);
            }

            BigInteger fromBlockNumber = currentBlockNumber;
            BigInteger toBlockNumber = fromBlockNumber.add(span);

            EthLog logsResponse = client
                .ethGetLogs(
                    new EthFilter(
                        DefaultBlockParameter.valueOf(fromBlockNumber),
                        DefaultBlockParameter.valueOf(toBlockNumber),
                        Lists.newArrayList(address)
                    )
                )
                .send();

            currentBlockNumber = currentBlockNumber.add(span);

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
