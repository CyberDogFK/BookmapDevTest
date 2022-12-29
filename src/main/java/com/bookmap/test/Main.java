package com.bookmap.test;

import com.bookmap.test.service.StrategyManager;
import com.bookmap.test.service.impl.OperationManagerImpl;
import com.bookmap.test.service.impl.OutputServiceImpl;
import com.bookmap.test.service.impl.StrategyManagerImpl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    private static final String INPUT_FILE_PATH = "input.txt";
    private static final String OUTPUT_FILE_PATH = "output.txt";

    public static void main(String[] args) {
        prepareOutputFile(OUTPUT_FILE_PATH);
        String line;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(INPUT_FILE_PATH));
             BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(OUTPUT_FILE_PATH))) {
            StrategyManager lineStrategy = new StrategyManagerImpl(
                    new OperationManagerImpl(),
                    new OutputServiceImpl(bufferedWriter));
            System.out.println();
            while ((line = bufferedReader.readLine()) != null) {
                lineStrategy.lineStrategy(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
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
