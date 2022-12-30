package com.bookmap.test.service.impl;

import com.bookmap.test.model.Operation;
import com.bookmap.test.service.OperationManager;

import java.util.Comparator;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.BinaryOperator;

public class OperationManagerImpl implements OperationManager {
    private final TreeMap<Integer, Operation> bidTree;
    private final TreeMap<Integer, Operation> askTree;

    public OperationManagerImpl() {
        bidTree = new TreeMap<>();
        askTree = new TreeMap<>();
    }

    @Override
    public void updateBid(int price, int size) {
        bidTree.put(price, new Operation(price, size, Operation.Type.BID));
        askTree.remove(price);
    }

    @Override
    public void updateAsk(int price, int size) {
        askTree.put(price, new Operation(price, size, Operation.Type.ASK));
        bidTree.remove(price);
    }

    @Override
    public Operation getBestBid() {
        boolean seen = false;
        Operation result = null;
        BinaryOperator<Operation> accumulator = BinaryOperator.maxBy(Comparator.comparing(Operation::getPrice));

        for (Operation operation : bidTree.values()) {
            if (operation.getType().equals(Operation.Type.BID)
                    && operation.getSize() > 0) {
                if (!seen) {
                    seen = true;
                    result = operation;
                } else {
                    result = accumulator.apply(result, operation);
                }
            }
        }
        if (seen) {
            return result;
        } else {
            for (Operation o : bidTree.values()) {
                if (o.getType().equals(Operation.Type.BID)) {
                    if (!seen) {
                        seen = true;
                        result = o;
                    } else {
                        result = accumulator.apply(result, o);
                    }
                }
            }
            return (seen ? Optional.of(result) : Optional.<Operation>empty())
                    .orElseThrow(() -> new RuntimeException("Can't find any bid"));
        }
    }

    @Override
    public Operation getBestAsk() {
        boolean seen = false;
        Operation result = null;
        BinaryOperator<Operation> accumulator = BinaryOperator.minBy(Comparator.comparing(Operation::getPrice));
        for (Operation operation : askTree.values()) {
            if (operation.getType().equals(Operation.Type.ASK)
                    && operation.getSize() > 0) {
                if (!seen) {
                    seen = true;
                    result = operation;
                } else {
                    result = accumulator.apply(result, operation);
                }
            }
        }
        if (seen) {
            return result;
        } else {
            for (Operation o : askTree.values()) {
                if (o.getType().equals(Operation.Type.ASK)) {
                    if (!seen) {
                        seen = true;
                        result = o;
                    } else {
                        result = accumulator.apply(result, o);
                    }
                }
            }
            if (seen) {
                return result;
            } else {
                throw new RuntimeException("Can't find best ask");
            }
        }
    }

    @Override
    public Operation getOperationWithPrice(int price) {
        Operation operation1 = askTree.get(price);
        Operation operation2 = bidTree.get(price);
        if (operation1 != null) {
            return operation1;
        } else if (operation2 != null) {
            return operation2;
        } else {
            return new Operation(price, 0, Operation.Type.SPREAD);
        }


//        Operation operation = operationBook.get(price);
//        if (operation == null) {
//            operation =  new Operation(price, 0, Operation.Type.SPREAD);
//        }
//        return operation;
    }

    @Override
    public void buyOrder(int size) {
        Operation bestAsk = getBestAsk();
        while (size != 0) {
            int newSize = bestAsk.getSize() - size;
            if (newSize <= 0) {
                size = Math.abs(newSize);
                askTree.remove(bestAsk.getPrice());
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
                bidTree.remove(bestBid.getPrice());
                bestBid = getBestBid();
            } else {
                size = 0;
                bestBid.setSize(newSize);
            }
        }
    }
}
