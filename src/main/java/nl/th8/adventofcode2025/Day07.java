package nl.th8.adventofcode2025;

import nl.th8.adventofcode2025.utils.PuzzleInputParser;

import java.nio.file.Path;
import java.util.*;

public class Day07 implements Day {
    private static final char SOURCE = 'S';
    private static final char SPLITTER = '^';
    private static final char BEAM = '|';
    private static final char EMPTY = '.';
    
    private final Map<String, Long> timelineCache = new HashMap<>();
    
    private final PuzzleInputParser puzzleInputParser;
    //For actual use
    public Day07() {
        puzzleInputParser = new PuzzleInputParser(Path.of("src", "main", "resources", "input", "7.txt"));}

    //For Unittesting
    public Day07(PuzzleInputParser puzzleInputParser) {
        this.puzzleInputParser = puzzleInputParser;
    }

    /**
     * To solve part one we build our beam row for row in the input. If we hit a splitter we put a beam on
     * each side of it and add one to our split count.
     * 
     * @return the amount of splits made.
     */
    public long solvePartOne() {
        char[][] manifold = puzzleInputParser.getInputAs3DCharArray();
        return traceTachyonBeam(manifold);
    }

    /**
     * Traces the beam from it's source to the end. Expanding the beam down row for row, and continuing in on each side
     * of a splitter if it hit's one of those. 
     * 
     * @param manifold to trace the beam in.
     * @return the amount of splitters hit whilst tracing the beam.
     */
    private int traceTachyonBeam(char[][] manifold) {
        int amountOfSplits = 0;
        for(int y = 0; y < manifold.length; ++y) {
            for(int x = 0; x < manifold[y].length; ++x) {
                if (manifold[y][x] == SOURCE)
                    manifold[y + 1][x] = BEAM;
                else if (manifold[y][x] == BEAM && y != manifold.length - 1) {
                    if (manifold[y + 1][x] == SPLITTER) {
                        manifold[y + 1][x - 1] = BEAM;
                        manifold[y + 1][x + 1] = BEAM;
                        amountOfSplits++;
                    } else {
                        manifold[y + 1][x] = BEAM;
                    }
                }
            }
        }
        return amountOfSplits;
    }

    /**
     * To solve part two we first solve part one again, so we can use it's tracing of the beam to trace each beam backwards
     * to the source. For each path that reaches the source we add one timeline. This requires caching of any split points
     * as the runtime far exceeds my runtime goals for Advent of Code.
     * 
     * @return the amount of timelines created.
     */
    public long solvePartTwo() {
        char[][] manifold = puzzleInputParser.getInputAs3DCharArray();
        //Solve part one so we can trace back along all paths
        traceTachyonBeam(manifold);
        
        //Trace each possible path back to its origin point. (e.g. all beams on the last row of the manifold)
        long timelines = 0;
        for(int x = 0; x < manifold[0].length; ++x) {
            long timelinesFromBeam = backTraceBeam(manifold, x,manifold.length - 1);
            timelines += timelinesFromBeam;
            
        }
        return timelines;
    }

    /**
     * Traces a beam in reverse, going up one row each time until it hits the source, or ends in a dead end. Adding 
     * one timeline to the count when it hits the source. If a splitter is next to the beam at any point we recursively
     * trace back the beam that hit that splitter (if any), and add it's timeline count to ours.
     * <p>
     * We cache any paths from recursive calls, and always check if we've already cached the current path already.
     * 
     * @param manifold that's been traced out so we can reverse through all paths
     * @param x coordinate of the beam to start tracing
     * @param startY y coordinate to start tracing at, moving up (so counting down) from there.
     *               
     * @return the amount of timelines traced back to the source from the given x and startY point.
     */
    private long backTraceBeam(char[][] manifold, int x, int startY) {
        //Cache any fully traced out splitter, otherwise we'll be here for quite a while.
        if(timelineCache.containsKey(createTimelineCoordinate(x, startY))) {
            return timelineCache.get(createTimelineCoordinate(x, startY));
        }
        
        long timelines = 0;
        int y = startY;
        while(y >= 0) {
            //We've traced this path back to the source, add one timeline
            if(manifold[y][x] == SOURCE) {
                return 1;
            }
            //This path ends here, we needn't trace it further.
            if(manifold[y][x] == SPLITTER || manifold[y][x] == EMPTY) {
                return timelines;
            }
            
            //Follow the beam up the tree, creating new traces for all splitters next to the beam.
            //If the beam never hits that splitter (so it couldn't be split into the beam we're trace) it will not be traced back to the source, 
            //so it will not be counted.
            if(manifold[y][x] == BEAM) {
                if(x - 1 >= 0 && manifold[y][x - 1] == SPLITTER) {
                    long timelinesFound = backTraceBeam(manifold, x - 1, y -1);
                    timelineCache.put(createTimelineCoordinate(x - 1, y - 1), timelinesFound);
                    timelines += timelinesFound;
                }
                if(x + 1 < manifold[0].length &&  manifold[y][x + 1] == SPLITTER) {
                    long timelinesFound = backTraceBeam(manifold, x + 1, y -1);
                    timelineCache.put(createTimelineCoordinate(x + 1, y - 1), timelinesFound);
                    timelines += timelinesFound;
                }
                y--;
            }
        }
        return timelines;
    }
    
    private static String createTimelineCoordinate(int x, int y) {
        return "x%dy%d".formatted(x, y);
    }

    public int getDayNumber() {
        return 7;
    }
}
