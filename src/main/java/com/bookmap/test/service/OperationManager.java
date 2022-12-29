package com.bookmap.test.service;

import com.bookmap.test.model.Operation;

public interface OperationManager {
    void updateBid(int price, int size);

    void updateAsk(int price, int size);

    Operation getBestBid();

    Operation getBestAsk();

    Operation getOperationWithSize(int price);

    void buyOrder(int size);

    void sellOrder(int size);
}
