package nl.th8.adventofcode2025;

import nl.th8.adventofcode2025.utils.PuzzleInputParser;

import java.nio.file.Path;
import java.util.*;

public class DayFour implements Day {
    private final PuzzleInputParser puzzleInputParser;

    //For actual use
    public DayFour() {
        puzzleInputParser = new PuzzleInputParser(Path.of("src", "main", "resources", "input", "4.txt"));
    }

    //For Unittesting
    public DayFour(PuzzleInputParser puzzleInputParser) {
        this.puzzleInputParser = puzzleInputParser;
    }

    /**
     * To solve part one we create a Hashmap of all coordinates in the warehouse, and set a count of neighboring paper
     * rolls on each coordinate. Afterward we count the amount of spaces with 3 or fewer neighbors.
     *
     * @return the amount of rolls with 3 or fewer neighbors.
     */
    public long solvePartOne() {
        List<String> mapOfPaper = puzzleInputParser.getInputAsStringList();
        Map<String, Integer> adjacencyMap = initialiseAdjacencyMap(mapOfPaper.getFirst().length(), mapOfPaper.size());

        //Fill or clear the adjacencyMap for each coordinate
        fillAdjacencyMap(mapOfPaper, adjacencyMap);

        int accessiblePaperRolls = 0;
        for(Integer place : adjacencyMap.values()) {
            if(place != null && place <= 4)
                accessiblePaperRolls++;
        }

        return accessiblePaperRolls;
    }

    /**
     * Create a map of all coordinates in the paper warehouse. And initialise their neighbor count as 0.
     *
     * @param xSize horizontal width of the paper warehouse
     * @param ySize vertical length of the paper warehouse
     * @return a map containing all possible coordinates as key, and 0 neighbors as value.
     */
    private Map<String, Integer> initialiseAdjacencyMap(int xSize, int ySize) {
        Map<String, Integer> adjacencyMap = new HashMap<>();
        for(int i = 0; i < xSize; i++) {
            for(int j = 0; j < ySize; j++) {
                adjacencyMap.put(createCoordinate(i, j), 0);
            }
        }
        return adjacencyMap;
    }

    /**
     * Fill the previously initialised map with neighbor counts (includes itself). For each spot with a paper roll (e.g. @)
     * we take all it's neighbors (so x +/- 1 and y +/- 1) an up their neighbor count by one. If a spot does not have
     * a paper roll on it, we set its neighbor count to null.
     *
     * @param mapOfPaper
     * @param adjacencyMap
     */
    private void fillAdjacencyMap(List<String> mapOfPaper, Map<String, Integer> adjacencyMap) {
        for(int i = 0; i < mapOfPaper.size(); i++) {
            char[] warehouseRow = mapOfPaper.get(i).toCharArray();
            for(int j = 0; j < warehouseRow.length; j++) {
                if(warehouseRow[j] == '@')
                    setAdjacency(j, i, adjacencyMap);
                else
                    adjacencyMap.put(createCoordinate(j, i), null);
            }
        }
    }

    /**
     * See {@link this#fillAdjacencyMap(List, Map)} for explanation
     * @param xCoord
     * @param yCoord
     * @param adjacencyMap
     */
    private void setAdjacency(int xCoord, int yCoord, Map<String, Integer> adjacencyMap) {
        for(int i = Math.max(0, xCoord - 1); i <= xCoord + 1; i++) {
            for(int j = Math.max(0, yCoord - 1); j <= yCoord + 1; j++) {
                String key = createCoordinate(i, j);
                adjacencyMap.compute(key, (k, v) -> v == null ? null : v + 1);
            }
        }
    }

    /**
     * To solve part two we create a HashMap of all coordinates in the warehouse, and set a count of neighboring paper
     * rolls on each coordinate. Afterward we count the amount of rolls with 3 or fewer neighbors, and remove these rolls
     * from the HashMap, and update the neighbor count for each removal, a 'sweep' of the warehouse.
     * We repeat the counting and removing until no more rolls are removed on a sweep.
     *
     * @return the total amount of rolls removed of all sweeps of the warehouse.
     */
    public long solvePartTwo() {
        List<String> mapOfPaper = new ArrayList<>(puzzleInputParser.getInputAsStringList());
        Map<String, Integer> adjacencyMap = initialiseAdjacencyMap(mapOfPaper.getFirst().length(), mapOfPaper.size());

        //Fill or clear the adjacencyMap for each coordinate
        fillAdjacencyMap(mapOfPaper, adjacencyMap);

        int accessiblePaperRolls = 0;
        int paperRollsRemoved = sweepWarehouse(adjacencyMap);
        while (paperRollsRemoved > 0) {
            accessiblePaperRolls += paperRollsRemoved;
            paperRollsRemoved = sweepWarehouse(adjacencyMap);
        }

        return accessiblePaperRolls;
    }

    /**
     * Check the neighbor count of all coordinates in the warehouse and remove paper rolls from these spaces.
     * @param adjacencyMap
     * @return the amount of rolls that were removed
     */
    private int sweepWarehouse(Map<String, Integer> adjacencyMap) {
        int accessiblePaperRolls = 0;
        Set<String> clearedPaper = new HashSet<>();
        for(Map.Entry<String, Integer> place : adjacencyMap.entrySet()) {
            if(place.getValue() != null && place.getValue() <= 4) {
                accessiblePaperRolls++;
                clearedPaper.add(place.getKey());
            }
        }
        for(String paper : clearedPaper) {
            removePaperRoll(paper, adjacencyMap);
        }
        return accessiblePaperRolls;
    }

    /**
     * Update the adjacency map after a paper roll has been moved away. We set the neighbor count of the roll to remove
     * to null, and subtract one from the neighbor count of all adjacent places.
     *
     * @param coordinate
     * @param adjacencyMap
     */
    private void removePaperRoll(String coordinate, Map<String, Integer> adjacencyMap) {
        adjacencyMap.replace(coordinate, null);
        removeAdjacency(getCoordinateX(coordinate), getCoordinateY(coordinate), adjacencyMap);
    }

    /**
     * See {@link this#removePaperRoll(String, Map)}
     * @param xCoord
     * @param yCoord
     * @param adjacencyMap
     */
    private void removeAdjacency(int xCoord, int yCoord, Map<String, Integer> adjacencyMap) {
        for(int i = Math.max(0, xCoord - 1); i <= xCoord + 1; i++) {
            for(int j = Math.max(0, yCoord - 1); j <= yCoord + 1; j++) {
                String key = createCoordinate(i, j);
                adjacencyMap.compute(key, (k, v) -> v == null ? null : v + -1);
            }
        }
    }

    /**
     * Transforms coordinates into a Stringified coordinate that can be used as a Hashmap key.
     * @param x coordinate
     * @param y coordinate
     * @return stringified coordinates (e.g. x = 5, y = 5 -> "x5y5"
     */
    private static String createCoordinate(int x, int y) {
        return "x%dy%d".formatted(x, y);
    }

    /**
     * Return the x-coordinate value from a stringified coordinate key.
     * @param coordinate
     * @return
     */
    private static int getCoordinateX(String coordinate) {
        return Integer.parseInt(coordinate.split("[xy]")[1]);
    }

    /**
     * Return the y-coordinate value from a stringified coordinate key.
     * @param coordinate
     * @return
     */
    private static int getCoordinateY(String coordinate) {
        return Integer.parseInt(coordinate.split("[xy]")[2]);
    }

    public int getDayNumber() {
        return 4;
    }
}
