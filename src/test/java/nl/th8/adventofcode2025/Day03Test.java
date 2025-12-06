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
class Day03Test {

    private final PuzzleInputParser puzzleInputParser = mock(PuzzleInputParser.class);

    private Day day;

    @BeforeEach
    void beforeEach() {
        when(puzzleInputParser.getInputAsStringList()).thenReturn(Arrays.asList(
                "987654321111111",
                "811111111111119",
                "234234234234278",
                "818181911112111"
        ));

        day = new Day03(puzzleInputParser);
    }

    @Test
    void solvePart1() {
        assertEquals(357, day.solvePartOne());
    }

    @Test
    void solvePart2() {
        assertEquals(3121910778619L, day.solvePartTwo());

    }
}