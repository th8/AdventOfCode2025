package nl.th8.adventofcode2025;

import nl.th8.adventofcode2025.utils.PuzzleInputParser;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DayTwo implements Day {
    private final PuzzleInputParser puzzleInputParser;

    //For actual use
    public DayTwo() {
        puzzleInputParser = new PuzzleInputParser(Path.of("src", "main", "resources", "input", "2.txt"));
    }

    //For Unittesting
    public DayTwo(PuzzleInputParser puzzleInputParser) {
        this.puzzleInputParser = puzzleInputParser;
    }

    /**
     * To solve part one of the puzzle we turn the L/R instructions into negative or positive numbers to add to the current position.
     * After adding the negative or positive number to our current position we modulo it by 100 to get the actual position on the dial,
     * and check if this would be position 0 on the dial.
     *
     * @return The amount of times the dial was on position 0.
     */
    public long solvePartOne() {
        String inputString = puzzleInputParser.getInputAsString();
        List<String> idRanges = Arrays.stream(inputString.split(",")).toList();
        long allInvalidIds = 0;

        for(String range : idRanges) {
            String[] rangeSplit = range.split("-");
            long begin = Long.parseLong(rangeSplit[0].trim());
            long end = Long.parseLong(rangeSplit[1].trim());
            List<Long> invalidIds = findInvalidIds(begin, end);

            for(long id : invalidIds) {
                allInvalidIds += id;
            }

        }
        return allInvalidIds;
    }

    private List<Long> findInvalidIds(long begin, long end) {
        List<Long> invalidIds = new ArrayList<>();
        for(long id = begin; id <= end; id++) {
            String idAsString = String.valueOf(id);
            if(isInvalid(idAsString)) {
                invalidIds.add(id);
            }
        }
        return invalidIds;
    }

    private boolean isInvalid(String idAsString) {
        //A String of uneven length cannot be invalid
        if(idAsString.length() % 2 != 0)
            return false;

        int halfLength = idAsString.length() / 2;

        for(int i = 0; i < halfLength; i++) {
            if(idAsString.charAt(i) != idAsString.charAt(halfLength + i)) {
                return false;
            }
        }
        return true;
    }

    public long solvePartTwo() {
        String inputString = puzzleInputParser.getInputAsString();
        List<String> idRanges = Arrays.stream(inputString.split(",")).toList();
        long allInvalidIds = 0;

        for(String range : idRanges) {
            String[] rangeSplit = range.split("-");
            long begin = Long.parseLong(rangeSplit[0].trim());
            long end = Long.parseLong(rangeSplit[1].trim());
            List<Long> invalidIds = findInvalidIdsTwo(begin, end);

            for(long id : invalidIds) {
                allInvalidIds += id;
            }

        }
        return allInvalidIds;
    }

    private List<Long> findInvalidIdsTwo(long begin, long end) {
        List<Long> invalidIds = new ArrayList<>();
        for(long id = begin; id <= end; id++) {
            String idAsString = String.valueOf(id);
            if(isInvalidTwo(idAsString, 2)) {
                invalidIds.add(id);
            }
        }
        return invalidIds;
    }

    private boolean isInvalidTwo(String idAsString, int splitSize) {
        if(splitSize > idAsString.length()) {
            return false;
        }
        //A String of undividable (at least cleanly) length cannot be invalid
        if(idAsString.length() % splitSize != 0)
            return isInvalidTwo(idAsString, splitSize + 1);

        int splitLength = idAsString.length() / splitSize;


        for(int i = 0; i < splitLength; i++) {
            for(int j = 1; j < splitSize; j++) {
                if(idAsString.charAt(i) != idAsString.charAt(splitLength * j + i)) {
                    return isInvalidTwo(idAsString, splitSize + 1);
                }
            }
        }
        return true;
    }

    public int getDayNumber() {
        return 2;
    }
}
