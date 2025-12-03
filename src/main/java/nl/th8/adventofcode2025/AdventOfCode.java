package nl.th8.adventofcode2025;

import org.springframework.util.StopWatch;

import java.util.List;

public class AdventOfCode {

    private static final List<Day> days = List.of(new DayOne(), new DayTwo(), new DayThree());

    public static void main(String[] args) {
        StopWatch stopWatch = new StopWatch("Advent of Code 2024");

        for(Day day : days) {
            System.out.printf("----- Day %d -----%n", day.getDayNumber());

            stopWatch.start(String.format("Day %d.1", day.getDayNumber()));
            System.out.printf("Day %d.1's solution is: %d%n", day.getDayNumber(), day.solvePartOne());
            stopWatch.stop();

            stopWatch.start(String.format("Day %d.2", day.getDayNumber()));
            System.out.printf("Day %d.2's solution is: %d%n", day.getDayNumber(), day.solvePartTwo());
            stopWatch.stop();

            System.out.println();
        }

        /* Motivation for me to optimise runtime */
        System.out.println(stopWatch.prettyPrint());
    }
}
