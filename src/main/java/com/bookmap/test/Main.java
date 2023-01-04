package com.bookmap.test;

import com.bookmap.test.service.StrategyManager;
import com.bookmap.test.service.impl.OperationManagerImpl;
import com.bookmap.test.service.impl.OutputServiceImpl;
import com.bookmap.test.service.impl.StrategyManagerImpl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.TreeMap;

public class Main {
    private static final String INPUT_FILE_PATH = "input.txt";
    private static final String OUTPUT_FILE_PATH = "output.txt";

    public static void main(String[] args) {
        prepareOutputFile(OUTPUT_FILE_PATH);
        readAllLineByLine();
    }

    //better perfomance for huge files
    private static void readAllLineByLine() {
        String line;
        try (BufferedReader bufferedReader = Files.newBufferedReader(Path.of(INPUT_FILE_PATH));
             BufferedWriter bufferedWriter = Files.newBufferedWriter(Path.of(OUTPUT_FILE_PATH))) {
            StrategyManager strategyManager = new StrategyManagerImpl(
                    new OperationManagerImpl(),
                    new OutputServiceImpl(bufferedWriter));
            while ((line = bufferedReader.readLine()) != null) {
                strategyManager.lineStrategy(line); // need change StrategyManager parameters to String
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


    // better perfomance in small files
//    // need change StrategyManager parameters to StringBuilder
//    private static void readAllInMemory() {
//        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(Path.of(OUTPUT_FILE_PATH))) {
//            StrategyManager strategyManager = new StrategyManagerImpl(
//                    new OperationManagerImpl(),
//                    new OutputServiceImpl(bufferedWriter));
//        byte[] bytes =  Files.readAllBytes(Path.of(INPUT_FILE_PATH));
//
//        StringBuilder stringBuilder = new StringBuilder();
//            for (int i = 0; i < bytes.length; i++) {
//                stringBuilder.setLength(0);
//                while (bytes[i] != System.lineSeparator().charAt(0)) {
//                    stringBuilder.append((char) bytes[i]);
//                    i++;
//                    if (i >= bytes.length) {
//                        break;
//                    }
//                }
//                strategyManager.lineStrategy(stringBuilder);
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
