package nl.th8.adventofcode2025;

import nl.th8.adventofcode2025.utils.PuzzleInputParser;

import java.nio.file.Path;
import java.util.*;

public class Day08 implements Day {
    
    private final PuzzleInputParser puzzleInputParser;
    private final int amountOfConnectionsToMake;
    //For actual use
    public Day08() {
        puzzleInputParser = new PuzzleInputParser(Path.of("src", "main", "resources", "input", "8.txt"));
        amountOfConnectionsToMake = 1000;
    }

    //For Unittesting
    public Day08(PuzzleInputParser puzzleInputParser) {
        this.puzzleInputParser = puzzleInputParser;
        amountOfConnectionsToMake = 10;
    }


    /**
     * To solve part one we create a TreeMap of all calculated Euclidean distances between the given junction boxes.
     * We connect the lowest distance box pair from this map using {@link this#connectBoxToCircuits(List, String[])} until
     * we've reached our connection goal. Then we collect the size of the three biggest circuits we've created and multiply
     * their sizes
     * 
     * @return The multiplied sizes of the three biggest circuits that were created.
     */
    public long solvePartOne() {
        List<String> junctionBoxCoordinates = puzzleInputParser.getInputAsStringList();
        Map<String, JunctionBox> junctionBoxes = initJunctionBoxes(junctionBoxCoordinates);
        TreeMap<Double, String> distanceMap = calculateDistances(junctionBoxes);
        
        List<Set<String>> circuits = new ArrayList<>();
        int junctionsConnected = 0;
        while (junctionsConnected < amountOfConnectionsToMake && !distanceMap.isEmpty()) {
            String[] pair = distanceMap.pollFirstEntry().getValue().split("-");
            if(isAlreadyConnected(circuits, pair[0], pair[1])) {
                junctionsConnected++;
                continue;
            }

            connectBoxToCircuits(circuits, pair);
            junctionsConnected++;
        }

        return circuits.stream()
                .sorted(Comparator.comparing(Set::size, Comparator.reverseOrder()))
                .limit(3)
                .mapToLong(Set::size)
                .reduce(1, (a, b) -> a * b);
    }

    /**
     * To solve part one we create a TreeMap of all calculated Euclidean distances between the given junction boxes.
     * We connect the lowest distance box pair from this map using {@link this#connectBoxToCircuits(List, String[])} until
     * a circuit containing all junction boxes has been created. We then take the pair which caused to circuit to be completed
     * and multiply their x coordinates.
     * 
     * @return the multiplied value of the x coordinates of last pair needed to complete our circuit 
     */
    public long solvePartTwo() {
        List<String> junctionBoxCoordinates = puzzleInputParser.getInputAsStringList();
        Map<String, JunctionBox> junctionBoxes = initJunctionBoxes(junctionBoxCoordinates);
        TreeMap<Double, String> distanceMap = calculateDistances(junctionBoxes);
        List<Set<String>> circuits = new ArrayList<>();
        while (!distanceMap.isEmpty()) {
            String[] pair = distanceMap.pollFirstEntry().getValue().split("-");
            if(isAlreadyConnected(circuits, pair[0], pair[1])) {
                continue;
            }

            connectBoxToCircuits(circuits, pair);
            //If the circuits list contains a single set with 1000 values, we've completed connecting the circuit together.
            if(circuits.getFirst().size() == junctionBoxes.size()) {
                return Math.multiplyExact((long) junctionBoxes.get(pair[0]).x, junctionBoxes.get(pair[1]).x);
            }
        }
        throw new IllegalArgumentException("Did not complete the circuit before running out of possible connections");
    }

    /**
     * Connects the current pair of junction boxes to the existing circuits, by creating a new circuit, adding one box to an
     * existing circuit or by merging two circuits together.
     * @param circuits that currently exist.
     * @param pair names of the junction boxes to connect together.
     */
    private void connectBoxToCircuits(List<Set<String>> circuits, String[] pair) {
        long circuitsFound = circuits.stream().filter(circuit -> circuit.contains(pair[0]) || circuit.contains(pair[1])).count();
        //If neither box is in a circuit yet, make a new circuit containing the two boxes.
        if(circuitsFound == 0) {
            Set<String> circuit = new HashSet<>();
            circuit.add(pair[0]);
            circuit.add(pair[1]);
            circuits.add(circuit);
        } 
        //If a box is part of one circuit, add the other one to that circuit as well.
        else if (circuitsFound == 1) {
            circuits.stream()
                    .filter(circuit -> circuit.contains(pair[0]) || circuit.contains(pair[1]))
                    .findFirst()
                    .ifPresent(circuit -> {
                        circuit.add(pair[0]);
                        circuit.add(pair[1]);
                    });
        } 
        //If both boxes are part of a circuit, merge the circuits together.
        else {
            Set<String> newCircuit = new  HashSet<>();
            List<Set<String>> circuitsToMerge = circuits.stream()
                    .filter(circuit -> circuit.contains(pair[0]) || circuit.contains(pair[1]))
                    .toList();
            
            for(Set<String> toMerge: circuitsToMerge) {
                circuits.remove(toMerge);
                newCircuit.addAll(toMerge);
            }
            circuits.add(newCircuit);
        }
    }


    /**
     * Turn the input coordinates into a Map of junctionBoxes, with their names as key to quickly fetch them later
     * when needed.
     * 
     * @param junctionBoxCoordinates input to create junction boxes for.
     * @return a Map of junction boxes.
     */
    private Map<String, JunctionBox> initJunctionBoxes(List<String> junctionBoxCoordinates) {
        Map<String, JunctionBox> junctionBoxes = new HashMap<>();
        for(String junctionBoxCoordinate : junctionBoxCoordinates) {
            String[] coordinates = junctionBoxCoordinate.split(",");
            var box = new JunctionBox(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]), Integer.parseInt(coordinates[2]));
            junctionBoxes.put(box.getName(), box);
        }
        return junctionBoxes;
    }

    /**
     * Create a TreeMap of the distance values between each unique pair of junction boxes. We keep track of connections
     * that have been made, so we do not calculate the connection in reverse (e.g. if box1 - box2 has been calculated, we 
     * skip calculating box2 - box1)
     * 
     * @param junctionBoxes to calculate distances between.
     *                      
     * @return a TreeMap with all calculated distances and its corresponding connection.
     */
    private TreeMap<Double, String> calculateDistances(Map<String, JunctionBox> junctionBoxes) {
        TreeMap<Double, String> distanceMap = new TreeMap<>();
        Set<String> alreadyComputed = new HashSet<>();
        for(JunctionBox junctionBox : junctionBoxes.values()){
            for(JunctionBox otherBox : junctionBoxes.values()){
                if(junctionBox.equals(otherBox) || alreadyComputed.contains(createNameForConnection(otherBox, junctionBox)))
                    continue;
                
                distanceMap.put(junctionBox.getDistance(otherBox), createNameForConnection(junctionBox, otherBox));
                alreadyComputed.add(createNameForConnection(junctionBox, otherBox));
            }
        }
        return distanceMap;
    }
    
    private static class JunctionBox {
        int x;
        int y;
        int z;
        
        public JunctionBox(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        /**
         * Wrapper for {@link this#getDistance(int, int, int)}
         * 
         * @param otherBox another junctionBox whose coordinates to use in {@link #getDistance(int, int, int)}
         *                 
         * @return calculated Euclidean distance
         */
        public double getDistance(JunctionBox otherBox) {
            return getDistance(otherBox.x, otherBox.y, otherBox.z);
        }

        /**
         * Calculate Euclidean distance between the x, y, z coordinates of this JunctionBox and the given 'other' x, y, z coordinates.
         * 
         * @param otherX 
         * @param otherY
         * @param otherZ
         * 
         * @return calculated Euclidean distance
         */
        public double getDistance(int otherX, int otherY, int otherZ) {
            return Math.sqrt(Math.pow((double) x - otherX, 2) + Math.pow((double) y - otherY, 2) + Math.pow((double) z - otherZ, 2));
        }
        
        public String getName() {
            return "x%dy%dz%d".formatted(x, y, z);
        }
    }
    
    private static String createNameForConnection(JunctionBox junctionBox, JunctionBox otherJunctionBox) {
        return "%s-%s".formatted(junctionBox.getName(), otherJunctionBox.getName());
    }
    
    private static boolean isAlreadyConnected(List<Set<String>> circuits, String box, String otherBox) {
        return circuits.stream()
                .anyMatch(circuit -> circuit.contains(box) && circuit.contains(otherBox));
    }
    

    public int getDayNumber() {
        return 8;
    }
}
