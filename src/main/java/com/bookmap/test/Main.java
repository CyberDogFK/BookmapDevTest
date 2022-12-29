package com.bookmap.test;

import com.bookmap.test.service.StrategyManager;
import com.bookmap.test.service.impl.OperationManagerImpl;
import com.bookmap.test.service.impl.OutputServiceImpl;
import com.bookmap.test.service.impl.StrategyManagerImpl;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    private static final String INPUT_FILE_PATH = "input.txt";
    private static final String OUTPUT_FILE_PATH = "output.txt";

    public static void main(String[] args) {
        prepareOutputFile(OUTPUT_FILE_PATH);
        String line;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(INPUT_FILE_PATH), 8192 * 1280);
             BufferedWriter bufferedWriter = Files.newBufferedWriter(Path.of(OUTPUT_FILE_PATH));) {
            StrategyManager strategyManager = new StrategyManagerImpl(
                    new OperationManagerImpl(),
                    new OutputServiceImpl(bufferedWriter));
            while ((line = bufferedReader.readLine()) != null) {
                strategyManager.lineStrategy(line);
            }
        } catch (IOException e) {
            throw new RuntimeException("Can't read file", e);
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
