package com.bookmap.test.service.impl;

import com.bookmap.test.model.Operation;
import com.bookmap.test.service.OperationManager;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;

public class OperationManagerImpl implements OperationManager {
    private final Map<Integer, Operation> operationBook;

    public OperationManagerImpl() {
        operationBook = new HashMap<>();
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
        boolean seen = false;
        Operation acc = null;
        BinaryOperator<Operation> accumulator = BinaryOperator.maxBy(Comparator.comparing(Operation::getPrice));
        for (Operation operation : operationBook.values()) {
            if (operation.getType().equals(Operation.Type.BID)
                    && operation.getSize() > 0) {
                if (!seen) {
                    seen = true;
                    acc = operation;
                } else {
                    acc = accumulator.apply(acc, operation);
                }
            }
        }
        if (seen) {
            return acc;
        } else {
            boolean seen1 = false;
            Operation result = null;
            BinaryOperator<Operation> accumulator1 = BinaryOperator.maxBy(Comparator.comparing(Operation::getPrice));
            for (Operation o : operationBook.values()) {
                if (o.getType().equals(Operation.Type.BID)) {
                    if (!seen1) {
                        seen1 = true;
                        result = o;
                    } else {
                        result = accumulator1.apply(result, o);
                    }
                }
            }
            return (seen1 ? Optional.of(result) : Optional.<Operation>empty())
                    .orElseThrow(() -> new RuntimeException("Can't find any bid"));
        }
    }

    @Override
    public Operation getBestAsk() {
        boolean seen = false;
        Operation acc = null;
        BinaryOperator<Operation> accumulator = BinaryOperator.minBy(Comparator.comparing(Operation::getPrice));
        for (Operation operation : operationBook.values()) {
            if (operation.getType().equals(Operation.Type.ASK)
                    && operation.getSize() > 0) {
                if (!seen) {
                    seen = true;
                    acc = operation;
                } else {
                    acc = accumulator.apply(acc, operation);
                }
            }
        }
        if (seen) {
            return acc;
        } else {
            boolean seen1 = false;
            Operation result = null;
            BinaryOperator<Operation> accumulator1 = BinaryOperator.minBy(Comparator.comparing(Operation::getPrice));
            for (Operation o : operationBook.values()) {
                if (o.getType().equals(Operation.Type.ASK)) {
                    if (!seen1) {
                        seen1 = true;
                        result = o;
                    } else {
                        result = accumulator1.apply(result, o);
                    }
                }
            }
            return (seen1 ? Optional.of(result) : Optional.<Operation>empty())
                    .orElseThrow(() -> new RuntimeException("Can't find any ask"));
        }
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
