package com.bookmap.test.service.impl;

import com.bookmap.test.model.Operation;
import com.bookmap.test.service.OperationManager;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class OperationManagerImpl implements OperationManager {
    private Map<Integer, Operation> operationBook;

    public OperationManagerImpl() {
        operationBook = new HashMap<>();
    }

    @Override
    public void updateBid(Integer price, Integer size) {
        operationBook.put(price, new Operation(price, size, Operation.Type.BID));
    }

    @Override
    public void updateAsk(Integer price, Integer size) {
        operationBook.put(price, new Operation(price, size, Operation.Type.ASK));
    }

    @Override
    public Operation getBestBid() {
        return operationBook.values().stream()
                .filter(o -> o.getType().equals(Operation.Type.BID)
                        && o.getSize() > 0)
                .max(Comparator.comparing(Operation::getPrice))
                .orElseThrow(() -> new RuntimeException("Can't find best bid"));
    }

    @Override
    public Operation getBestAsk() {
        return operationBook.values().stream()
                .filter(o -> o.getType().equals(Operation.Type.ASK)
                        && o.getSize() > 0)
                .min(Comparator.comparing(Operation::getPrice))
                .orElseThrow(() -> new RuntimeException("Can't find best ask"));
    }

    @Override
    public Operation getOperationWithSize(Integer price) {
        return operationBook.values().stream()
                .filter(o -> o.getPrice() == price)
                .findAny().orElseThrow(() -> new RuntimeException("Can't find size of price " + price));
    }

    @Override
    public void buyOrder(Integer size) {
        Operation bestAsk = getBestAsk();
        bestAsk.setSize(bestAsk.getSize() - size);
    }

    @Override
    public void sellOrder(Integer size) {
        Operation bestBid = getBestBid();
        bestBid.setSize(bestBid.getSize() - size);
    }
}
