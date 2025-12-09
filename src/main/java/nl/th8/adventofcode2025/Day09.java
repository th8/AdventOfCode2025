package nl.th8.adventofcode2025;

import nl.th8.adventofcode2025.utils.PuzzleInputParser;

import java.nio.file.Path;
import java.util.*;

/**
 * Not my proudest solution ever, but I failed to find a less computationally expensive way to check whether the rectangles were valid.
 * Not without running out of memory atleast...
 */
public class Day09 implements Day {
    
    private final PuzzleInputParser puzzleInputParser;
    //For actual use
    public Day09() {
        puzzleInputParser = new PuzzleInputParser(Path.of("src", "main", "resources", "input", "9.txt"));
    }

    //For Unittesting
    public Day09(PuzzleInputParser puzzleInputParser) {
        this.puzzleInputParser = puzzleInputParser;
    }


    /**
     * To solve part one we calculate the area of each combination of tiles, and take the highest value we found.
     * 
     * @return the highest area formed by a rectangle between two tiles.
     */
    public long solvePartOne() {
        List<Tile> redTiles = initRedTiles();

        long biggestSize = 0;
        for(Tile redTile : redTiles) {
            for(Tile otherTile : redTiles) {
                long size = (long) (Math.abs(redTile.x - otherTile.x) + 1) * (Math.abs(redTile.y - otherTile.y) + 1);
                if(size > biggestSize)
                    biggestSize = size;
            }
        }
        
        return biggestSize;
    }

    /**
     * To solve part two we create a cache (Map) containing the lowest and highest coordinates on an axis for any coordinate on the other axis.
     * We then create a TreeMap of all possible area values, to prioritise which area's to check first, as checking is very computationally expensive.
     * <p>
     * We start with the highest area and work our way down until we've found a set of tiles that is valid according to {@link this#isValid(Tile, Tile, Map)}
     * 
     * @return the highest valid area value
     */
    public long solvePartTwo() {
        List<Tile> redTiles = initRedTiles();
        Map<String, MinMax> minMaxCache = initMinMaxCoordinates(redTiles);
        TreeMap<Long, TilePair> sizeCache = new TreeMap<>();
        
        for(Tile redTile : redTiles) {
            for(Tile otherTile : redTiles) {
                long size = (long) (Math.abs(redTile.x - otherTile.x) + 1) * (Math.abs(redTile.y - otherTile.y) + 1);
                sizeCache.put(size, new TilePair(redTile, otherTile));
            }
        }
        
        while(!sizeCache.isEmpty()) {
            var entry = sizeCache.pollLastEntry();
            if(isValid(entry.getValue().tile1(), entry.getValue().tile2(), minMaxCache))
                return entry.getKey();
        }
        throw new IllegalStateException("No solution found!");
    }

    private List<Tile> initRedTiles() {
        return puzzleInputParser.getInputAsStringList().stream()
                .map(input -> {
                    String[] split = input.split(",");
                    return new Tile(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
                })
                .toList();
    }

    /**
     * Initialise a Map containing the highest x/y coordinates present on a given x or y coordinate. 
     * (so lowest and highest x for a given y coordinate or vice versa.)
     * 
     * @param redTiles
     * 
     * @return
     */
    private Map<String, MinMax> initMinMaxCoordinates(List<Tile> redTiles) {
        int sizeX = redTiles.stream().mapToInt(Tile::x).max().orElse(-1) + 1;
        int sizeY = redTiles.stream().mapToInt(Tile::y).max().orElse(-1) + 1;
        Map<String, MinMax> minMaxCache = new HashMap<>();
        
        for(int x = 0; x < sizeX; x++) {
            initVertically(redTiles, x, minMaxCache);
        }

        for(int y = 0; y < sizeY; y++) {
            initHorizontally(redTiles, y, minMaxCache);
        }
        return minMaxCache;
    }

    /**
     * Map out one horizontal line of the floor in the minMax map. Take the lowest and highest x coordinate on the given line.
     * Draw an imaginary line between those points, and update the highest/lowest y coordinates for each coordinate on the x-axes it crosses.
     * <br/>
     * Example:
     * <p>
     * .#X#.<br/>
     * .....<br/>
     * .....
     * <p>
     * We draw a line on y: 0 x: 1-3. This makes 0 the lowest and highest y coordinate for x 1-3.
     * 
     * 
     * @param redTiles to get the highest and lowest x coordinate for the current line from
     * @param y current position on the y-axes
     * @param minMaxCache to modify min/max x-axes values on.
     */
    private void initHorizontally(Collection<Tile> redTiles, int y, Map<String, MinMax> minMaxCache) {
        int minX = redTiles.stream()
                .filter(redTile -> redTile.y == y)
                .mapToInt(Tile::x).min().orElse(-1);
        int maxX = redTiles.stream()
                .filter(redTile -> redTile.y == y)
                .mapToInt(Tile::x).max().orElse(-1);

        if(minX == -1 || maxX == -1) return;
        for(int x = minX; x <= maxX; x++) {
            String key = "x%d".formatted(x);
            if(minMaxCache.containsKey(key)) {
                minMaxCache.compute(key, (k, v) -> v.min > y ? new MinMax(y, v.max) : v.max < y ? new MinMax(v.min, y) : v);
            } else {
                minMaxCache.put(key, new MinMax(y, y));
            }
        }
    }

    /**
     * Map out one vertical line of the floor in the minMax map. Take the lowest and highest y coordinate on the given line.
     * Draw an imaginary line between those points, and update the highest/lowest x coordinates for each coordinate on the y-axes it crosses.
     * <br/>
     * Example:
     * <p>
     * .#...<br/>
     * .X...<br/>
     * .#...
     * <p>
     * We draw a line on y: 0-2 x: 1. This makes 1 the lowest and highest x coordinate for y 0-2.
     *
     *
     * @param redTiles to get the highest and lowest x coordinate for the current line from
     * @param x current position on the y-axes
     * @param minMaxCache to modify min/max x-axes values on.
     */
    private void initVertically(Collection<Tile> redTiles, int x, Map<String, MinMax> minMaxCache) {
        int minY = redTiles.stream()
                .filter(redTile -> redTile.x == x)
                .mapToInt(Tile::y).min().orElse(-1);
        int maxY = redTiles.stream()
                .filter(redTile -> redTile.x == x)
                .mapToInt(Tile::y).max().orElse(-1);

        if(minY == -1 || maxY == -1) return;
        for(int y = minY; y <= maxY; y++) {
            String key = "y%d".formatted(y);
            if(minMaxCache.containsKey(key)) {
                minMaxCache.compute(key, (k, v) -> v.min > x ? new MinMax(x, v.max) : v.max < x ? new MinMax(v.min, x) : v);
            } else {
                minMaxCache.put(key, new MinMax(x, x));
            }
        }
    }

    /**
     * Checks whether a combination of tiles forms a valid rectangle. We first check if the four corners are in bounds according
     * to the minMaxCache we built earlier. If those are in bound, we check all vertices too. We do not need to check the entire
     * rectangle, as anything contained in valid vertices will also be valid.
     * 
     * @param redTile 
     * @param otherTile
     * @param minMaxCache
     * @return
     */
    private boolean isValid(Tile redTile, Tile otherTile, Map<String, MinMax> minMaxCache) {
        int xBegin = Math.min(redTile.x, otherTile.x);
        int xEnd = Math.max(redTile.x, otherTile.x);
        int yBegin = Math.min(redTile.y, otherTile.y);
        int yEnd = Math.max(redTile.y, otherTile.y);
        
        //Check the outer edges first, fail early to improve runtime
        if(checkNotInBound(minMaxCache, xBegin, yBegin))
            return false;
        if(checkNotInBound(minMaxCache, xEnd, yBegin))
            return false;
        if(checkNotInBound(minMaxCache, xBegin, yEnd))
            return false;
        if(checkNotInBound(minMaxCache, xEnd, yEnd))
            return false;
        
        //Expensive checks along all vertices of the rectangle
        if(!horizontalVertexInBounds(xBegin, xEnd, yBegin, minMaxCache))
            return false;
        if(!horizontalVertexInBounds(xBegin, xEnd, yEnd, minMaxCache))
            return false;
        if(!verticalVertexInBound(yBegin, yEnd, xBegin, minMaxCache))
            return false;
        return verticalVertexInBound(yBegin, yEnd, xEnd, minMaxCache);
    }

    private boolean horizontalVertexInBounds(int x1, int x2, int y, Map<String, MinMax> minMaxCache) {
        for(int i = x1; i <= x2; i++) {
            if(checkNotInBound(minMaxCache, i, y))
                return false;
        }
        return true;
    }

    private boolean verticalVertexInBound(int y1, int y2, int x, Map<String, MinMax> minMaxCache) {
        for(int i = y1; i <= y2; i++) {
            if(checkNotInBound(minMaxCache, x, i))
                return false;
        }
        return true;
    }

    /**
     * Get the higest and lowest tile coordinates on the x and y axes from the minMaxCache and use it to check whether 
     * the given coordinates are in bound.
     * @param minMaxCache a cache with the highest and lowest coordinate for a given x/y coordinate.
     * @param x to check bounds for
     * @param y to check bounds for
     * 
     * @return
     */
    private boolean checkNotInBound(Map<String, MinMax> minMaxCache, int x, int y) {
        MinMax minMaxX = minMaxCache.get("y%d".formatted(y));
        MinMax minMaxY = minMaxCache.get("x%d".formatted(x));
        if(minMaxX.min == -1 || minMaxY.min == -1) return true;
        return x < minMaxX.min || x > minMaxX.max || y < minMaxY.min || y > minMaxY.max;
    }

    public record Tile(int x, int y) {}
    
    public record TilePair(Tile tile1, Tile tile2) {}
    
    public record MinMax(int min,  int max) {}

    public int getDayNumber() {
        return 9;
    }
}
