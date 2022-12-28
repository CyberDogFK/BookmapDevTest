package com.bookmap.test.service;

import com.bookmap.test.model.Operation;

public interface OutputService {
    void saveOperationToFile(Operation operation);

    void saveValueToFile(Integer value);
}
