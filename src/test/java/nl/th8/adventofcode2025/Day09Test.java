package nl.th8.adventofcode2025;

import nl.th8.adventofcode2025.utils.PuzzleInputParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class Day09Test {

    private final PuzzleInputParser puzzleInputParser = mock(PuzzleInputParser.class);

    private Day day;

    @BeforeEach
    void beforeEach() {
        when(puzzleInputParser.getInputAsStringList()).thenReturn(Arrays.asList(
                "7,1",
                "11,1",
                "11,7",
                "9,7",
                "9,5",
                "2,5",
                "2,3",
                "7,3"));

        day = new Day09(puzzleInputParser);
    }

    @Test
    void solvePart1() {
        assertEquals(50, day.solvePartOne());
    }

    @Test
    void solvePart2() {
        assertEquals(24, day.solvePartTwo());
    }
}