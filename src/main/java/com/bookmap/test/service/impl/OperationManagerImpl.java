package com.bookmap.test.service.impl;

import com.bookmap.test.model.Operation;
import com.bookmap.test.service.OperationManager;

import java.util.Map;
import java.util.TreeMap;

public class OperationManagerImpl implements OperationManager {
    private final TreeMap<Integer, Operation> operationBook;

    public OperationManagerImpl() {
        operationBook = new TreeMap<>();
    }

    @Override
    public void updateBid(int price, int size) {
        operationBook.put(price, new Operation(price, size, Operation.Type.BID));
    }

    @Override
    public void updateAsk(int price, int size) {
        operationBook.put(price, new Operation(price, size, Operation.Type.ASK));
    }

    @Override
    public Operation getBestBid() {
        Map.Entry<Integer, Operation> operationEntry = operationBook.lastEntry();
        Integer key = operationEntry.getKey();
        Operation value = operationEntry.getValue();
        while (value.getSize() == 0 || value.getType().equals(Operation.Type.ASK)) {
            operationEntry = operationBook.lowerEntry(key);
            key = operationEntry.getKey();
            value = operationEntry.getValue();
        }
        return value;
    }

    @Override
    public Operation getBestAsk() {
        Map.Entry<Integer, Operation> operationEntry = operationBook.lastEntry();
        Integer key = operationEntry.getKey();
        Operation value = operationEntry.getValue();
        while (value.getSize() == 0 && value.getType().equals(Operation.Type.BID)) {
            operationEntry = operationBook.higherEntry(key);
            key = operationEntry.getKey();
            value = operationEntry.getValue();
        }
        return value;
    }

    @Override
    public Operation getOperationWithSize(int price) {
        Operation operation = operationBook.get(price);
        if (operation == null) {
            operation =  new Operation(price, 0, Operation.Type.SPREAD);
        }
        return operation;
    }

    @Override
    public void buyOrder(int size) {
        Operation bestAsk = getBestAsk();
        while (size != 0) {
            int newSize = bestAsk.getSize() - size;
            if (newSize <= 0) {
                size = Math.abs(newSize);
                operationBook.remove(bestAsk.getPrice());
                bestAsk = getBestAsk();
            } else {
                size = 0;
                bestAsk.setSize(newSize);
            }
        }
    }

    @Override
    public void sellOrder(int size) {
        Operation bestBid = getBestBid();
        while (size != 0) {
            int newSize = bestBid.getSize() - size;
            if (newSize <= 0) {
                size = Math.abs(newSize);
                operationBook.remove(bestBid.getPrice());
                bestBid = getBestBid();
            } else {
                size = 0;
                bestBid.setSize(newSize);
            }
        }
    }
}
