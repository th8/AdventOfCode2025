package nl.th8.adventofcode2025;

import nl.th8.adventofcode2025.utils.PuzzleInputParser;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Day06 implements Day {
    private static final String WHITESPACE_REGEX = "\\s+";
    private static final String DECIMAL_REGEX = "\\d+";
    private static final String OPERATOR_SUM = "+";
    private static final String OPERATOR_MULTIPLY = "*";

    private final PuzzleInputParser puzzleInputParser;

    //For actual use
    public Day06() {
        puzzleInputParser = new PuzzleInputParser(Path.of("src", "main", "resources", "input", "6.txt"));
    }

    //For Unittesting
    public Day06(PuzzleInputParser puzzleInputParser) {
        this.puzzleInputParser = puzzleInputParser;
    }


    /**
     * To solve part one we create a list of lists containing all the numbers in a column.
     * <<p>
     * We then run through all operators and apply them to all the numbers in the corresponding column.
     * @return the sum of the results of all operations.
     */
    public long solvePartOne() {
        List<String> workSheet = puzzleInputParser.getInputAsStringList();
        int amountOfProblems = workSheet.getFirst().split(WHITESPACE_REGEX).length;
        List<List<Long>> operands = initEmptyLists(amountOfProblems);

        for(var row : workSheet) {
            parseRow(row, operands);
        }

        long totalSum = 0;
        String[] operators = workSheet.getLast().split(WHITESPACE_REGEX);
        for(int i = 0;  i < operators.length; i++) {
            if(operators[i].equals(OPERATOR_SUM))
                totalSum += operands.get(i).stream()
                        .mapToLong(Long::longValue)
                        .sum();
            else if(operators[i].equals(OPERATOR_MULTIPLY))
                totalSum += operands.get(i).stream()
                        .mapToLong(Long::longValue)
                        .reduce(1, (a, b) -> a * b);
        }

        return totalSum;
    }

    /**
     * Initioalise our list of lists by putting an empty list at as many indexes as we have operations to perform.
     * @param size amount of lists to initialise
     *
     * @return the initialised list
     */
    private List<List<Long>> initEmptyLists(int size) {
        List<List<Long>> result = new ArrayList<>();
        for(int i = 0; i < size; i++) {
            result.add(new ArrayList<>());
        }
        return result;
    }

    /**
     * Parse a row of input, and put all numbers into the corresponding lists.
     *
     * @param row to parse
     * @param operands to put the numbers into
     */
    private void parseRow(String row, List<List<Long>> operands) {
        String[] rowSplit = row.trim().split(WHITESPACE_REGEX);
        for(int i = 0; i < rowSplit.length; i++) {
            if(rowSplit[i].matches(DECIMAL_REGEX)) {
                operands.get(i).add(Long.parseLong(rowSplit[i]));
            }
        }
    }

    /**
     * To solve part two we create a list of lists containing all the numbers in a column. Making sure to convert from
     * right-to-left & top-to-bottom to an ordered list.
     * <<p>
     * We then run through all operators and apply them to all the numbers in the corresponding column.
     * @return the sum of the results of all operations.
     */
    public long solvePartTwo() {
        List<String> workSheet = puzzleInputParser.getInputAsStringList();
        int amountOfProblems = workSheet.getFirst().split(WHITESPACE_REGEX).length;
        List<List<Long>> operands = initEmptyLists(amountOfProblems);

        parseColumns(workSheet, operands, amountOfProblems);

        long totalSum = 0;
        String[] operators = workSheet.getLast().split(WHITESPACE_REGEX);
        for(int i = 0;  i < operators.length; i++) {
            if(operators[i].equals(OPERATOR_SUM))
                totalSum += operands.get(i).stream()
                        .mapToLong(Long::longValue)
                        .sum();
            else if(operators[i].equals(OPERATOR_MULTIPLY))
                totalSum += operands.get(i).stream()
                        .mapToLong(Long::longValue)
                        .reduce(1, (a, b) -> a * b);
        }

        return totalSum;
    }

    /**
     * Run through all columns in the worksheet and convert them into numbers needed.
     * See {@link this#constructNumbers(List, List, int, int, int)}
     *
     * @param workSheet to convert
     * @param operands to convert into
     */
    private void parseColumns(List<String> workSheet, List<List<Long>> operands, int amountOfColumns) {
        int currentPosition = 0;
        for(int i = 0; i < amountOfColumns; i++) {
            int columnSize = workSheet.getLast().split("[+*]")[i+1].length()+1;
            constructNumbers(workSheet, operands, i, currentPosition, columnSize);
            currentPosition += columnSize;
        }
    }

    /**
     * Parses the numbers for one column from right-to-left. By iterating through the column size in reverse.
     * Taking the value in the column from each row for the current iteration, and sticking them together to create the
     * required number.
     *
     * @param workSheet the worksheet input
     * @param operands to put the constructed numbers into
     * @param columnIndex current column being parsed
     * @param columnPosition starting index of the column in the rows of the worksheet.
     * @param columnSize size of the column including the whitespace behind the numbers.
     */
    private void constructNumbers(List<String> workSheet, List<List<Long>> operands, int columnIndex, int columnPosition, int columnSize) {
        //Iterate through the column in reverse. (Subtract one from its size to convert to its index)
        for(int indexInColumn = columnSize - 1; indexInColumn >= 0; indexInColumn--) {
            StringBuilder number = new StringBuilder();
            //Get the character at the current indexInColumn from each row excluding the last one which contains the operators.
            for(int rowIndex = 0; rowIndex < workSheet.size() - 1; rowIndex++) {
                number.append(workSheet.get(rowIndex).charAt(columnPosition + indexInColumn));
            }

            if(!number.toString().trim().isEmpty())
                operands.get(columnIndex).add(Long.parseLong(number.toString().trim()));
        }
    }

    public int getDayNumber() {
        return 6;
    }
}
