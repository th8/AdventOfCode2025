package nl.th8.adventofcode2025;

import nl.th8.adventofcode2025.utils.PuzzleInputParser;

import java.nio.file.Path;

public class DayOne implements Day {
    private final PuzzleInputParser puzzleInputParser;

    //For actual use
    public DayOne() {
        puzzleInputParser = new PuzzleInputParser(Path.of("src", "main", "resources", "input", "1.txt"));
    }

    //For Unittesting
    public DayOne(PuzzleInputParser puzzleInputParser) {
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
        int currentPosition = 50;
        int timesAtZero = 0;
        for(var rotation : puzzleInputParser.getInputAsStringList()) {
            int rotationAdjustment = rotation.charAt(0) == 'L' ?
                    Integer.parseInt("-" + rotation.substring(1)):
                    Integer.parseInt(rotation.substring(1));

            currentPosition += rotationAdjustment;

            if(currentPosition % 100 == 0)
                timesAtZero++;
        }
        return timesAtZero;
    }

    public long solvePartTwo() {
        int currentPosition = 50;
        int previousPosition = currentPosition;
        int timesAtZero = 0;
        for(var rotation : puzzleInputParser.getInputAsStringList()) {
            int rotationAdjustment = rotation.charAt(0) == 'L' ?
                    Integer.parseInt("-" + rotation.substring(1)):
                    Integer.parseInt(rotation.substring(1));

            currentPosition += rotationAdjustment;

            //Divide and round down to get the amount of full spins made before we apply a modulo to get our dial back into bounds.
            //This includes the dial landing on zero, so we have to exclude it when checking later. (e.g. prevPos: 0, rotation: R200, will result in 2 here so we need to remove a duplicate when checking if the dial is at 0 later.)
            int fullSpins = Math.abs(Math.floorDiv(currentPosition, 100));
            //One exception to the previous statement is values landing on -100/-200/etc. (e.g. prevPos: 50, rotation: L150, takes us past zero and lands on it as well)
            //for which Math#floorDiv outputs -1, so the dial landing on zero is not included.
            boolean isBoundaryIssue = currentPosition < 0 && currentPosition % 100 == 0;
            //Edge case: if we came from 0 and we're rotating left, we need to reduce the result of Math#floorDiv by one. (e.g. prevPos: 0, rotation L5 does not take us past zero)
            if(fullSpins > 0 && previousPosition == 0 && rotationAdjustment < 0)
                fullSpins--;

            //Now that we know the amount of full rotations, put the dial back into it's bounds by modulo 100
            //We use Math#floorMod instead of %= because of its more applicable handling of negative values. (floorMod(-1, 100) -> 99 whereas -1 %= 100 -> -1)
            currentPosition = Math.floorMod(currentPosition, 100);

            timesAtZero += fullSpins;
            if((fullSpins == 0 || isBoundaryIssue) && currentPosition == 0)
                timesAtZero++;

            previousPosition = currentPosition;
        }
        return timesAtZero;
    }

    public int getDayNumber() {
        return 1;
    }
}
