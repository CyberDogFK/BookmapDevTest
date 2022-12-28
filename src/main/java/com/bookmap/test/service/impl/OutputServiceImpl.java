package com.bookmap.test.service.impl;

import com.bookmap.test.model.Operation;
import com.bookmap.test.service.OutputService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class OutputServiceImpl implements OutputService {
    private final String fileName;

    public OutputServiceImpl(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void saveOperationToFile(Operation operation) {
        String price = String.valueOf(operation.getPrice());
        String size = String.valueOf(operation.getSize());
        String toFileFormat = price + "," + size + "\n";
        try {
            Files.write(Path.of(fileName), toFileFormat.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveValueToFile(Integer value) {
        try {
            Files.write(Path.of(fileName), String.valueOf(value).getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
