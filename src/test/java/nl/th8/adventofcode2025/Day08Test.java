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
class Day08Test {

    private final PuzzleInputParser puzzleInputParser = mock(PuzzleInputParser.class);

    private Day day;

    @BeforeEach
    void beforeEach() {
        when(puzzleInputParser.getInputAsStringList()).thenReturn(Arrays.asList(
                "162,817,812",
                "57,618,57",
                "906,360,560",
                "592,479,940",
                "352,342,300",
                "466,668,158",
                "542,29,236",
                "431,825,988",
                "739,650,466",
                "52,470,668",
                "216,146,977",
                "819,987,18",
                "117,168,530",
                "805,96,715",
                "346,949,466",
                "970,615,88",
                "941,993,340",
                "862,61,35",
                "984,92,344",
                "425,690,689"));

        day = new Day08(puzzleInputParser);
    }

    @Test
    void solvePart1() {
        assertEquals(40, day.solvePartOne());
    }

    @Test
    void solvePart2() {
        assertEquals(25272, day.solvePartTwo());
    }
}