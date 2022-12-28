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
                .orElse(operationBook.values().stream()
                        .filter(o -> o.getType().equals(Operation.Type.BID))
                        .max(Comparator.comparing(Operation::getPrice))
                        .orElseThrow(() -> new RuntimeException("Can't find any bid")));
    }

    @Override
    public Operation getBestAsk() {
        return operationBook.values().stream()
                .filter(o -> o.getType().equals(Operation.Type.ASK)
                        && o.getSize() > 0)
                .min(Comparator.comparing(Operation::getPrice))
                .orElse(operationBook.values().stream()
                        .filter(o -> o.getType().equals(Operation.Type.ASK))
                        .min(Comparator.comparing(Operation::getPrice))
                        .orElseThrow(() -> new RuntimeException("Can't find any ask")));
    }

    @Override
    public Operation getOperationWithSize(Integer price) {
        return operationBook.values().stream()
                .filter(o -> o.getPrice() == price)
                .findAny().orElse(new Operation(price, 0, Operation.Type.SPREAD));
    }

    @Override
    public void buyOrder(Integer size) {
        while (size != 0) {
            Operation bestAsk = getBestAsk();
            int newSize = bestAsk.getSize() - size;
            if (newSize < 0) {
                size = Math.abs(newSize);
                newSize = 0;
            } else {
                size = 0;
            }
            bestAsk.setSize(newSize);
        }
    }

    @Override
    public void sellOrder(Integer size) {
        while (size != 0) {
            Operation bestBid = getBestBid();
            int newSize = bestBid.getSize() - size;
            if (newSize < 0) {
                size = Math.abs(newSize);
                newSize = 0;
            } else {
                size = 0;
            }
            bestBid.setSize(newSize);
        }
    }
}
