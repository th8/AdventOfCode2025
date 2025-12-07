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
class Day07Test {

    private final PuzzleInputParser puzzleInputParser = mock(PuzzleInputParser.class);

    private Day day;

    @BeforeEach
    void beforeEach() {
        when(puzzleInputParser.getInputAs3DCharArray()).thenReturn(new char[][]{
                        ".......S.......".toCharArray(),
                        "...............".toCharArray(),
                        ".......^.......".toCharArray(),
                        "...............".toCharArray(),
                        "......^.^......".toCharArray(),
                        "...............".toCharArray(),
                        ".....^.^.^.....".toCharArray(),
                        "...............".toCharArray(),
                        "....^.^...^....".toCharArray(),
                        "...............".toCharArray(),
                        "...^.^...^.^...".toCharArray(),
                        "...............".toCharArray(),
                        "..^...^.....^..".toCharArray(),
                        "...............".toCharArray(),
                        ".^.^.^.^.^...^.".toCharArray(),
                        "...............".toCharArray()
                });

        day = new Day07(puzzleInputParser);
    }

    @Test
    void solvePart1() {
        assertEquals(21, day.solvePartOne());
    }

    @Test
    void solvePart2() {
        assertEquals(40, day.solvePartTwo());
    }
}