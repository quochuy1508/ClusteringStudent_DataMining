import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class KMeans {

    private KMeans() {
        throw new IllegalAccessError("You shouldn't call this constructor");
    }


//     Will be used to generate random numbers.
    private static final Random random = new Random();

    public static Map<Centroid, List<Record>> fit(List<Record> records, int k, Distance distance, int maxIterations) {
        applyPreconditions(records, k, distance, maxIterations);

        List<Centroid> centroids = randomCentroids(records, k);

        Map<Centroid, List<Record>> clusters = new HashMap<>();
        Map<Centroid, List<Record>> lastState = new HashMap<>();

        // iterate for a pre-defined number of times
        for (int i = 0; i < maxIterations; i++) {
            boolean isLastIteration = i == maxIterations - 1;
//            if (centroids.size() != k) {
//                System.out.println("Controid length: " + centroids.size());
//            }
            // in each iteration we should find the nearest centroid for each record
            for (Record record : records) {
                Centroid centroid = nearestCentroid(record, centroids, distance);
                assignToCluster(clusters, record, centroid);
            }
            if (clusters.size() != k) {
                return clusters;
            }
                // if the assignment does not change, then the algorithm terminates
            boolean shouldTerminate = isLastIteration || clusters.equals(lastState);
            lastState = clusters;

            if (shouldTerminate) {
                break;
            }

            // at the end of each iteration we should relocate the centroids
            centroids = relocateCentroids(clusters); // done
            clusters = new HashMap<>();
        }

        return lastState;
    }

    private static List<Centroid> relocateCentroids(Map<Centroid, List<Record>> clusters) {
        return clusters
                .entrySet()
                .stream()
                .map(e -> average(e.getKey(), e.getValue()))
                .collect(toList());
    }

    private static Centroid average(Centroid centroid, List<Record> records) {
        // if this cluster is empty, then we shouldn't move the centroid
        if (records == null || records.isEmpty()) {
            return centroid;
        }

        // Since some records don't have all possible attributes, we initialize
        // average coordinates equal to current centroid coordinates
        Map<String, Double> average = centroid.getCoordinates();

        // The average function works correctly if we clear all coordinates corresponding
        // to present record attributes
        records
                .stream()
                .flatMap(e -> e
                        .getFeatures()
                        .keySet()
                        .stream())
                .forEach(k -> average.put(k, 0.0));

        for (Record record : records) {
            record
                    .getFeatures()
                    .forEach((k, v) -> average.compute(k, (k1, currentValue) -> v + currentValue));
        }

        average.forEach((k, v) -> average.put(k, v / records.size()));

        return new Centroid(average);
    }

    private static void assignToCluster(Map<Centroid, List<Record>> clusters, Record record, Centroid centroid) {
        clusters.compute(centroid, (key, list) -> {
            if (list == null) {
                list = new ArrayList<>();
            }

            list.add(record);
            return list;
        });
    }
    private static Centroid nearestCentroid(Record record, List<Centroid> centroids, Distance distance) {
        double minimumDistance = Double.MAX_VALUE;
        Centroid nearest = null;

        for (Centroid centroid : centroids) {
            double currentDistance = distance.calculate(record.getFeatures(), centroid.getCoordinates());

            if (currentDistance < minimumDistance) {
                minimumDistance = currentDistance;
                nearest = centroid;
            }
        }
        return nearest;
    }

    private static List<Centroid> randomCentroids(List<Record> records, int k) {
        List<Centroid> centroids = new ArrayList<>();
        Map<String, Double> maxs = new HashMap<>();
        Map<String, Double> mins = new HashMap<>();

        for (Record record : records) {
            record
                    .getFeatures()
                    .forEach((key, value) -> {
                        // compares the value with the current max and choose the bigger value between them
                        maxs.compute(key, (k1, max) -> max == null || value > max ? value : max);

                        // compare the value with the current min and choose the smaller value between them
                        mins.compute(key, (k1, min) -> min == null || value < min ? value : min);
                    });
        }

        Set<String> attributes = records
                .stream()
                .flatMap(e -> e
                        .getFeatures()
                        .keySet()
                        .stream())
                .collect(toSet());
        for (int i = 0; i < k; i++) {
            Map<String, Double> coordinates = new HashMap<>();
            for (String attribute : attributes) {
                double max = maxs.get(attribute);
                double min = mins.get(attribute);
                coordinates.put(attribute, random.nextDouble() * (max - min) + min);
            }

            centroids.add(new Centroid(coordinates));
        }

        return centroids;
    }

    private static void applyPreconditions(List<Record> records, int k, Distance distance, int maxIterations) {
        if (records == null || records.isEmpty()) {
            throw new IllegalArgumentException("The dataset can't be empty");
        }

        if (k < 1) {
            throw new IllegalArgumentException("It doesn't make sense to have less than or equal to 1 cluster");
        }

        if (distance == null) {
            throw new IllegalArgumentException("The distance calculator is required");
        }

        if (maxIterations <= 0) {
            throw new IllegalArgumentException("Max iterations should be a positive number");
        }
    }
}