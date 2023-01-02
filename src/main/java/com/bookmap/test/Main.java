package com.bookmap.test;

import com.bookmap.test.service.StrategyManager;
import com.bookmap.test.service.impl.OperationManagerImpl;
import com.bookmap.test.service.impl.OutputServiceImpl;
import com.bookmap.test.service.impl.StrategyManagerImpl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.TreeMap;

public class Main {
    private static final String INPUT_FILE_PATH = "input.txt";
    private static final String OUTPUT_FILE_PATH = "output.txt";

    public static void main(String[] args) {
        prepareOutputFile(OUTPUT_FILE_PATH);
        String line;
        try (BufferedReader bufferedReader = Files.newBufferedReader(Path.of(INPUT_FILE_PATH));
             BufferedWriter bufferedWriter = Files.newBufferedWriter(Path.of(OUTPUT_FILE_PATH))) {
            StrategyManager strategyManager = new StrategyManagerImpl(
                    new OperationManagerImpl(),
                    new OutputServiceImpl(bufferedWriter));
            int count = 0;
            while ((line = bufferedReader.readLine()) != null) {
                strategyManager.lineStrategy(line);
                count++;
                System.gc();
            }

//            byte[] bytes = Files.readAllBytes(Path.of(INPUT_FILE_PATH));
//            bufferedReader.close();
//
//            StringBuilder stringBuilder;
//            for (int i = 0; i < bytes.length; i++) {
//                stringBuilder = new StringBuilder();
//                while ((char) bytes[i] != System.lineSeparator().charAt(0)) {
//                    stringBuilder.append((char) bytes[i]);
//                    i++;
//                    if (i >= bytes.length) {
//                        break;
//                    }
//                }
//                strategyManager.lineStrategy(stringBuilder.toString());
//            }
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
