package com.bookmap.test;

import com.bookmap.test.service.StrategyManager;
import com.bookmap.test.service.impl.OperationManagerImpl;
import com.bookmap.test.service.impl.OutputServiceImpl;
import com.bookmap.test.service.impl.StrategyManagerImpl;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class Main {
    private static final String INPUT_FILE_PATH = "input.txt";
    private static final String OUTPUT_FILE_PATH = "output.txt";

    public static void main(String[] args) {
        List<String> strings = readFile(INPUT_FILE_PATH);
        prepareOutputFile(OUTPUT_FILE_PATH);

        StrategyManager lineStrategy = new StrategyManagerImpl(
                new OperationManagerImpl(),
                new OutputServiceImpl(OUTPUT_FILE_PATH));
        strings.forEach(lineStrategy::lineStrategy);
    }

    private static List<String> readFile(String filePath) {
        File file = new File(filePath);
        try {
            return Files.readAllLines(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException("Can't read input file " + INPUT_FILE_PATH, e);
        }
    }

    private static File prepareOutputFile(String filePath) {
        File outputFile = new File(filePath);
        try {
            if (!outputFile.exists()) {
                outputFile.createNewFile();
            } else {
                outputFile.delete();
                outputFile.createNewFile();
            }
        } catch (IOException e) {
            throw new RuntimeException("Can't create output file", e);
        }
        return outputFile;
    }
}
