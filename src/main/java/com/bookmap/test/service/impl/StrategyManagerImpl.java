package com.bookmap.test.service.impl;

import com.bookmap.test.service.OperationManager;
import com.bookmap.test.service.OutputService;
import com.bookmap.test.service.StrategyManager;

public class StrategyManagerImpl implements StrategyManager {
    private OperationManager operationManager;
    private OutputService outputService;

    public StrategyManagerImpl(OperationManager operationManager, OutputService outputService) {
        this.operationManager = operationManager;
        this.outputService = outputService;
    }

    @Override
    public void lineStrategy(String line) {
        String[] split = line.split(",");
        switch (split[0]) {
            case "u":
                updateStrategy(split);
                return;
            case "q":
                queriesStrategy(split);
                return;
            case "o":
                orderStrategy(split);
                return;
            default:
                throw new RuntimeException("Wrong line format: " + line);
        }
    }

    private void orderStrategy(String[] line) {
        switch (line[1]) {
            case "buy":
                operationManager.buyOrder(Integer.parseInt(line[2]));
                return;
            case "sell":
                operationManager.sellOrder(Integer.parseInt(line[2]));
        }
    }

    private void queriesStrategy(String[] line) {
        switch (line[1]) {
            case "best_bid":
                outputService.saveOperationToFile(operationManager.getBestBid());
                return;
            case "best_ask":
                outputService.saveOperationToFile(operationManager.getBestAsk());
                return;
            case "size":
                outputService.saveValueToFile(operationManager.getOperationWithSize(Integer.parseInt(line[2])).getSize());
        }
    }

    private void updateStrategy(String[] line) {
        switch (line[3]) {
            case "bid":
                operationManager.updateBid(Integer.parseInt(line[1]),
                        Integer.parseInt(line[2]));
                return;
            case "ask":
                operationManager.updateAsk(Integer.parseInt(line[1]),
                        Integer.parseInt(line[2]));
        }
    }
}
