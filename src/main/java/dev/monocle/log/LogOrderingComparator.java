package dev.monocle.log;

import dev.monocle.Web3jUtils;
import org.web3j.protocol.websocket.events.Log;

import java.util.Comparator;

public class LogOrderingComparator implements Comparator<Log> {
    @Override
    public int compare(Log left, Log right) {
        int leftBlockNumber = Web3jUtils.hexToInteger(left.getBlockNumber());
        int rightBlockNumber = Web3jUtils.hexToInteger(right.getBlockNumber());

        if (leftBlockNumber != rightBlockNumber) {
            return Integer.compare(leftBlockNumber, rightBlockNumber);
        }

        int leftIndex = Web3jUtils.hexToInteger(left.getLogIndex());
        int rightIndex = Web3jUtils.hexToInteger(right.getLogIndex());

        return Integer.compare(leftIndex, rightIndex);
    }
}
