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
        int highestJoltage = 0;

        if (index != -1 && index + 1 < bank.length()) {
            String secondBattery = String.valueOf(findHighestSecondBattery(bank.substring(index + 1), 9));
            int foundJoltage = Integer.parseInt(nrToFind + secondBattery);
            if(foundJoltage > highestJoltage) {
                highestJoltage = foundJoltage;
            }
        }
        if(highestJoltage == 0) {
            return findHighestJoltage(bank, nrToFind-1);
        }
        return highestJoltage;
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

    public long solvePartTwo() {
        List<String> batteryBanks = puzzleInputParser.getInputAsStringList();
        long totalJoltage = 0;
        for(String bank : batteryBanks) {
            totalJoltage += findHighestJoltageTwo(bank, 12, 9);
        }

        return totalJoltage;
    }

    // 234234234234278
    private long findHighestJoltageTwo(String bank, int amountOfBatteries, int nrToFind) {
        if(amountOfBatteries < 1)
            throw new IllegalArgumentException("Amount of batteries may not be less than 1");
        if(nrToFind < 1)
            return 0;

        int index = bank.indexOf(Character.forDigit(nrToFind, 10));
        long highestJoltage = 0;

        if(index != -1 && index + amountOfBatteries <= bank.length()) {
            if(amountOfBatteries == 1) {
                return nrToFind;
            }
            long subBattery = findHighestJoltageTwo(bank.substring(index + 1), amountOfBatteries - 1, 9);
            long foundJoltage = Long.parseLong(nrToFind + String.valueOf(subBattery));
            if(foundJoltage > highestJoltage) {
                highestJoltage = foundJoltage;
            }
        }
        if(highestJoltage == 0) {
            return findHighestJoltageTwo(bank, amountOfBatteries, nrToFind-1);
        }
        return highestJoltage;
    }

    public int getDayNumber() {
        return 3;
    }
}
