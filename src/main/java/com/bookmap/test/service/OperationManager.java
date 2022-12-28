package com.bookmap.test.service;

import com.bookmap.test.model.Operation;

public interface OperationManager {
    void updateBid(Integer price, Integer size);

    void updateAsk(Integer price, Integer size);

    Operation getBestBid();

    Operation getBestAsk();

    Operation getOperationWithSize(Integer price);

    void buyOrder(Integer size);

    void sellOrder(Integer size);
}
