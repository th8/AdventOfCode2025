package nl.th8.adventofcode2025;

import nl.th8.adventofcode2025.utils.PuzzleInputParser;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DayThree implements Day {
    private final PuzzleInputParser puzzleInputParser;

    //For actual use
    public DayThree() {
        puzzleInputParser = new PuzzleInputParser(Path.of("src", "main", "resources", "input", "3.txt"));
    }

    //For Unittesting
    public DayThree(PuzzleInputParser puzzleInputParser) {
        this.puzzleInputParser = puzzleInputParser;
    }

    /**
     * To solve part one we search for the first {@link String#indexOf(String)} the highest battery joltage (e.g. 9).
     * If we do not find any occurences we lower the joltage we're looking for by one and try again. After finding
     * the first battery we repeat this step to find the second battery in a substring of our batteryBank starting past
     * the index of the first battery.
     *
     * @return the sum of all combined joltages found
     */
    public long solvePartOne() {
        List<String> batteryBanks = puzzleInputParser.getInputAsStringList();
        long totalJoltage = 0;
        for(String bank : batteryBanks) {
            totalJoltage += findHighestJoltage(bank, 9);
        }

        return totalJoltage;
    }

    private int findHighestJoltage(String bank, int nrToFind) {
        if(nrToFind < 1)
            throw new RuntimeException("Didn't find any first battery before hitting 0");

        int index = bank.indexOf(Character.forDigit(nrToFind, 10));

        if (index != -1 && index + 1 < bank.length()) {
            String secondBattery = String.valueOf(findHighestSecondBattery(bank.substring(index + 1), 9));
            return Integer.parseInt(nrToFind + secondBattery);
        } else {
            return findHighestJoltage(bank, nrToFind-1);
        }
    }

    private char findHighestSecondBattery(String subBank, int nrToFind) {
        if(nrToFind < 1)
            throw new RuntimeException("Didn't find any second battery before hitting 0");

        int index = subBank.indexOf(Character.forDigit(nrToFind, 10));
        if(index != -1) {
            return subBank.charAt(index);
        }
        return findHighestSecondBattery(subBank, nrToFind-1);
    }

    /**
     * To solve part two we take the same strategy of using {@link String#indexOf(String)} starting with the highest possible
     * joltage and reducing it until we've found a battery. We repeat this search recursively keeping track of the amount of batteries
     * left to find and limiting our search area between the index of the previous number
     * and the end of the batteryBank - the amount of batteries left to find,
     * so we do not run out of the bank before finding all batteries.
     *
     * @return the sum of the higest joltages found in each bank, using 12 batteries.
     */
    public long solvePartTwo() {
        List<String> batteryBanks = puzzleInputParser.getInputAsStringList();
        long totalJoltage = 0;
        for(String bank : batteryBanks) {
            totalJoltage += findHighestJoltageTwo(bank, 12, 9);
        }

        return totalJoltage;
    }

    private long findHighestJoltageTwo(String bank, int amountOfBatteries, int nrToFind) {
        if(amountOfBatteries < 1)
            throw new IllegalArgumentException("Amount of batteries may not be less than 1");
        if(nrToFind < 1)
            return 0;

        int index = bank.indexOf(Character.forDigit(nrToFind, 10));

        if(index != -1 && index + amountOfBatteries <= bank.length()) {
            if(amountOfBatteries == 1) {
                return nrToFind;
            }
            long subBattery = findHighestJoltageTwo(bank.substring(index + 1), amountOfBatteries - 1, 9);
            return Long.parseLong(nrToFind + String.valueOf(subBattery));
        } else {
            return findHighestJoltageTwo(bank, amountOfBatteries, nrToFind-1);
        }
    }

    public int getDayNumber() {
        return 3;
    }
}
