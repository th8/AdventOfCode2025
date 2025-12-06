package nl.th8.adventofcode2025;

import nl.th8.adventofcode2025.utils.PuzzleInputParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class Day02Test {

    private final PuzzleInputParser puzzleInputParser = mock(PuzzleInputParser.class);

    private Day day;

    @BeforeEach
    void beforeEach() {
        when(puzzleInputParser.getInputAsString()).thenReturn("11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,446443-446449,38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124");

        day = new Day02(puzzleInputParser);
    }

    @Test
    void solvePart1() {
        assertEquals(1227775554, day.solvePartOne());
    }

    @Test
    void solvePart2() {
        assertEquals(4174379265L, day.solvePartTwo());

    }
}