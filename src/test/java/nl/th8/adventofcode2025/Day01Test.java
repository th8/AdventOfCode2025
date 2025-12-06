package nl.th8.adventofcode2025;

import nl.th8.adventofcode2025.utils.PuzzleInputParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class Day01Test {

    private final PuzzleInputParser puzzleInputParser = mock(PuzzleInputParser.class);

    private Day01 day01;

    @BeforeEach
    public void beforeEach() {
        when(puzzleInputParser.getInputAsStringList()).thenReturn(Arrays.asList("L68",
                "L30",
                "R48",
                "R301",
                "L401",
                "L5",
                "R60",
                "L55",
                "L1",
                "L99",
                "R14",
                "L82"));

        day01 = new Day01(puzzleInputParser);
    }

    @Test
    void solvePart1() {
        assertEquals(4, day01.solvePartOne());
    }

    @Test
    void solvePart2() {
        assertEquals(14, day01.solvePartTwo());

        when(puzzleInputParser.getInputAsStringList()).thenReturn(Arrays.asList("L68",
                "L30",
                "R48",
                "L5",
                "R60",
                "L55",
                "L1",
                "L99",
                "R14",
                "L82"));
        assertEquals(6, day01.solvePartTwo());

        when(puzzleInputParser.getInputAsStringList()).thenReturn(Arrays.asList("L68",
                "L30",
                "R48",
                "R300",
                "L5",
                "R60",
                "L55",
                "L1",
                "L99",
                "R14",
                "L82"));
        assertEquals(9, day01.solvePartTwo());

        when(puzzleInputParser.getInputAsStringList()).thenReturn(Arrays.asList("L250", "L1", "R2"));
        assertEquals(4, day01.solvePartTwo());
    }
}