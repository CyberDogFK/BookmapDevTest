package com.bookmap.test.service.impl;

import com.bookmap.test.service.OperationManager;
import com.bookmap.test.service.OutputService;
import com.bookmap.test.service.StrategyManager;

public class StrategyManagerImpl implements StrategyManager {
    private final OperationManager operationManager;
    private final OutputService outputService;

    public StrategyManagerImpl(OperationManager operationManager, OutputService outputService) {
        this.operationManager = operationManager;
        this.outputService = outputService;
    }

    @Override
    public void lineStrategy(String line) {
        char ch = line.charAt(0);
        switch (ch) {
            case 'u':
                updateStrategy(line);
                return;
            case 'q':
                queriesStrategy(line);
                return;
            case 'o':
                orderStrategy(line);
                return;
            default:
                throw new RuntimeException("Wrong line format: " + line);
        }
    }

    private void orderStrategy(String line) {
        if (line.charAt(2) == 'b') {
            operationManager.buyOrder(Integer.parseInt(line.substring(6)));
        } else if (line.charAt(2) == 's') {
            operationManager.sellOrder(Integer.parseInt(line.substring(7)));
        }
    }

    private void queriesStrategy(String line) {
        if (line.charAt(7) == 'b') {
            outputService.saveOperationToFile(operationManager.getBestBid());
        } else if (line.charAt(7) == 'a') {
            outputService.saveOperationToFile(operationManager.getBestAsk());
        } else if (line.charAt(2) == 's') {
            outputService.saveValueToFile(operationManager
                    .getOperationWithSize(Integer.parseInt(line.substring(7))).getSize());
        }
    }

    private void updateStrategy(String line) {
        int lastIndex = line.lastIndexOf(",");
        String substring = line.substring(line.indexOf(",") + 1, lastIndex);
        int index = substring.indexOf(",");
        int firstInt = Integer.parseInt(substring, 0, index, 10);
        int secondInt = Integer.parseInt(substring, index + 1, substring.length(), 10);
        switch (line.substring(lastIndex + 1).charAt(0)) {
            case 'b':
                operationManager.updateBid(firstInt,
                        secondInt);
                return;
            case 'a':
                operationManager.updateAsk(firstInt,
                        secondInt);
        }
    }
}
