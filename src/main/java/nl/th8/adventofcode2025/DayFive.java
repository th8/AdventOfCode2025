package nl.th8.adventofcode2025;

import nl.th8.adventofcode2025.utils.PuzzleInputParser;

import java.nio.file.Path;
import java.util.*;

public class DayFive implements Day {
    private final PuzzleInputParser puzzleInputParser;

    //For actual use
    public DayFive() {
        puzzleInputParser = new PuzzleInputParser(Path.of("src", "main", "resources", "input", "5.txt"));
    }

    //For Unittesting
    public DayFive(PuzzleInputParser puzzleInputParser) {
        this.puzzleInputParser = puzzleInputParser;
    }


    /**
     * To solve part one we create a HashMap with all ranges, see {@link this#indexFreshIngredients(String, Map)}.
     * Following the format key = start of range, value: a set of endpoints for the range.
     * We then check each ingredient against the key's and values of this map to see if they're within the given range.
     *
     * @return the amount of ingredients that were in one or more of the ranges.
     */
    public long solvePartOne() {
        List<String> database = puzzleInputParser.getInputAsStringList();
        Map<Long, Set<Long>> freshIngredients = new HashMap<>();
        int amountOfFreshIngredients = 0;

        for(String range : database) {
            if(range.isEmpty()) {
                break;
            }
            indexFreshIngredients(range, freshIngredients);
        }

        int startOfAvailableIngredients = database.indexOf("") + 1;

        for(int i = startOfAvailableIngredients; i < database.size(); i++) {
            long ingredientToCheck = Long.parseLong(database.get(i));
            for(var entry :  freshIngredients.entrySet()) {
                if(entry.getKey() <= ingredientToCheck && entry.getValue().stream().anyMatch(v -> v >= ingredientToCheck)) {
                    amountOfFreshIngredients++;
                    break;
                }
            }
        }


        return amountOfFreshIngredients;
    }

    /**
     * Create a HashMap of all fresh ingredient ranges using this format:</br>
     * ["beginOfRange" : ["endOfRange", "endOfRange1", "endOfRangeN"]]</br>
     * <p>
     * So ranges: 2-8, 2-12, 3-10 would look like this:</br>
     * ["2" : [8, 12]],  </br>
     * ["3" : [10]]
     *
     * @param range to index
     * @param freshIngredients map to put ranges in
     */
    private void indexFreshIngredients(String range, Map<Long, Set<Long>> freshIngredients) {
        String[] rangeSplit = range.split("-");
        long begin = Long.parseLong(rangeSplit[0].trim());
        long end = Long.parseLong(rangeSplit[1].trim());
        freshIngredients.compute(begin, (k, v) -> v == null ? new HashSet<>() : v).add(end);
    }

    /**
     * To solve part two we create a HashMap with all ranges, whilst merging any overlaps in ranges during creation.
     * Following the format key = start of range, value = end of range.
     * We then subtract the end of the range from the start of the range + 1 to get the amount of ids within the range.
     *
     * @return the total amount of ids in all ranges, without duplicates.
     */
    public long solvePartTwo() {
        List<String> database = puzzleInputParser.getInputAsStringList();
        Map<Long, Long> freshIngredients = new HashMap<>();
        long amountOfFreshIngredients = 0;

        for(String range : database) {
            if(range.isEmpty()) {
                break;
            }
            indexFreshIngredients2(range, freshIngredients);
        }

        for(var entry : freshIngredients.entrySet()) {
            amountOfFreshIngredients += entry.getValue() + 1 - entry.getKey();
        }

        return amountOfFreshIngredients;
    }

    /**
     * Create a HashMap of all fresh ingredient ranges merging with any overlapping ranges, see {@link this#mergeIfOverlapping(long, long, Map)}</br>
     * Using this format:</br>
     * ["beginOfRange" : "endOfRange"]</br>
     *
     * @param range to merge or index
     * @param freshIngredients
     */
    private void indexFreshIngredients2(String range, Map<Long, Long> freshIngredients) {
        String[] rangeSplit = range.split("-");
        long begin = Long.parseLong(rangeSplit[0].trim());
        long end = Long.parseLong(rangeSplit[1].trim());
        if(!mergeIfOverlapping(begin, end, freshIngredients))
            freshIngredients.compute(begin, (k, v) -> v == null || v < end ? Long.valueOf(end) : v);
    }


    /**
     * Check all current ranges in the HashMap for overlap with the given begin-end of a range. If there is any
     * overlap we merge the ranges to make sure there are no overlapping ranges in the map.
     *
     * @param begin of the range to check and merge
     * @param end of the range to check and merge
     * @param freshIngredients map of currently indexed ranges
     *
     * @return whether a merge has taken place
     */
    private boolean mergeIfOverlapping(long begin, long end, Map<Long, Long> freshIngredients) {
        long keyToRemove = -1L;
        long beginToSet = -1;
        long endToSet = -1;

        for (var entry : freshIngredients.entrySet()) {
            long newBegin;
            long newEnd;
            //If the beginning of a range falls within another range, possibly extend its end (e.g. begin: 5 end : 12 on key: 1 value: 10 merges to 1 - 12)
            if (entry.getKey() <= begin && entry.getValue() >= begin) {
                newBegin = entry.getKey();
                newEnd = Math.max(entry.getValue(), end);
            }
            //If the end of a range falls within another range, possibly move it's begin point (e.g. begin: 1 end: 10 on key: 5 value: 12 merges to 1-12)
            else if (entry.getKey() <= end && entry.getValue() >= end) {
                newBegin = Math.min(entry.getKey(), begin);
                newEnd = entry.getValue();
                keyToRemove = entry.getKey();
            }
            //If the range completely includes the range to check (e.g. begin: 1 end: 10 on key: 2 value: 9 replaces 2-9 with 1-10.
            else if(entry.getKey() >= begin && entry.getValue() <= end) {
                newBegin = begin;
                newEnd = end;
                keyToRemove = entry.getKey();
            }
            else {
                continue;
            }
            //If this range forms a bridge between two existing ranges, we need to merge all three together.
            //(e.g. begin: 5 end: 7 on key1: 1, value1: 5 and key2: 7 value2: 9 merges to 1-9)
            beginToSet = beginToSet > 0 ? Math.min(beginToSet, newBegin) : newBegin;
            endToSet = endToSet > 0 ? Math.max(endToSet, newEnd) : newEnd;
        }

        //Perform the actual merge after looping through the entire map to prevent ConcurrentModificationExceptions and to make sure 3-way merges are processed correctly.
        if(beginToSet > 0 && endToSet > 0) {
            if(keyToRemove > 0) {
                freshIngredients.put(beginToSet, endToSet);
                freshIngredients.remove(keyToRemove);
            } else {
                freshIngredients.replace(beginToSet, endToSet);
            }
            return true;
        }
        return false;
    }

    public int getDayNumber() {
        return 5;
    }
}
