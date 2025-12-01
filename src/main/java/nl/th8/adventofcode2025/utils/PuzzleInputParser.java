package nl.th8.adventofcode2025.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Util to parse .txt files which are used by AdventOfCode as inputs for puzzles.
 */
public class PuzzleInputParser {

    private final Path puzzleInputPath;

    public PuzzleInputParser(Path inputPath) {
        puzzleInputPath = inputPath;
    }

    /**
     * @return a list of integers for each line of the file. Using null for empty lines.
     */
    public List<Integer> getInputAsIntegerListWithNullSkips() {
        try (var fileStream = Files.lines(puzzleInputPath)) {
            List<Integer> returnValue = new ArrayList<>();

            fileStream
                .forEachOrdered(line -> {
                    if (Objects.equals(line, ""))
                        returnValue.add(null);
                    else
                        returnValue.add(Integer.valueOf(line));
                });

            return returnValue;
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new IllegalArgumentException("Unable to read input file, I can't solve puzzles like this! Bye.");
        }
    }

    /**
     * @return a list of Strings for each line of the file.
     */
    public List<String> getInputAsStringList() {
        try(var fileStream = Files.lines(puzzleInputPath)) {
            return fileStream.toList();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new IllegalArgumentException("Unable to read input file, I can't solve puzzles like this! Bye.");
        }
    }

    public String getInputAsString() {
        try {
            return Files.readString(puzzleInputPath);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new IllegalArgumentException("Unable to read input file, I can't solve puzzles like this! Bye.");
        }
    }
}
