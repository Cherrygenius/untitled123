package Lr1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class KMeans<Point extends DataPoint> {

    public class Cluster {
        public List<Point> points;
        public DataPoint centroid;

        public Cluster(List<Point> points, DataPoint randPoint) {
            this.points = points;
            this.centroid = randPoint;
        }
    }

    private List<Point> points;
    public List<Cluster> clusters;

    public KMeans(int k, List<Point> points) {
        if (k < 1) { // can't have negative or zero clusters
            throw new IllegalArgumentException("k must be >= 1");
        }
        this.points = points;
        zScoreNormalize();
        // initialize empty clusters with random centroids
        clusters = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            DataPoint randPoint = randomPoint();
            Cluster cluster = new Cluster(new ArrayList<Point>(), randPoint);
            clusters.add(cluster);
        }
    }

    private List<DataPoint> centroids() {
        return clusters.stream().map(cluster -> cluster.centroid)
                .collect(Collectors.toList());
    }

    private List<Double> dimensionSlice(int dimension) {
        return points.stream().map(x -> x.dimensions.get(dimension))
                .collect(Collectors.toList());
    }

    private void zScoreNormalize() {
        List<List<Double>> zscored = new ArrayList<>();
        for (Point point : points) {
            zscored.add(new ArrayList<Double>());
        }
        for (int dimension = 0; dimension < points.get(0).numDimensions; dimension++) {
            List<Double> dimensionSlice = dimensionSlice(dimension);
            Statistics stats = new Statistics(dimensionSlice);
            List<Double> zscores = stats.zscored();
            for (int index = 0; index < zscores.size(); index++) {
                zscored.get(index).add(zscores.get(index));
            }
        }
        for (int i = 0; i < points.size(); i++) {
            points.get(i).dimensions = zscored.get(i);
        }
    }

    private DataPoint randomPoint() {
        List<Double> randDimensions = new ArrayList<>();
        Random random = new Random();
        for (int dimension = 0; dimension < points.get(0).numDimensions; dimension++) {
            List<Double> values = dimensionSlice(dimension);
            Statistics stats = new Statistics(values);
            Double randValue = random.doubles(stats.min(), stats.max()).findFirst().getAsDouble();
            randDimensions.add(randValue);
        }
        return new DataPoint(randDimensions);
    }

    // Find the closest cluster centroid to each point and assign the point
    // to that cluster
    private void assignClusters() {
        for (Point point : points) {
            double lowestDistance = Double.MAX_VALUE;
            Cluster closestCluster = clusters.get(0);
            for (Cluster cluster : clusters) {
                double centroidDistance = point.distance(cluster.centroid);
                if (centroidDistance < lowestDistance) {
                    lowestDistance = centroidDistance;
                    closestCluster = cluster;
                }
            }
            closestCluster.points.add(point);
        }
    }

    // Find the center of each cluster and move the centroid to there
    private void generateCentroids() {
        for (Cluster cluster : clusters) {
            // Ignore if the cluster is empty
            if (cluster.points.isEmpty()) {
                continue;
            }
            List<Double> means = new ArrayList<>();
            for (int i = 0; i < cluster.points.get(0).numDimensions; i++) {
                int dimension = i; // needed to use in scope of closure
                Double dimensionMean = cluster.points.stream()
                        .mapToDouble(x -> x.dimensions.get(dimension)).average().getAsDouble();
                means.add(dimensionMean);
            }
            cluster.centroid = new DataPoint(means);
        }
    }

    // Check if two Lists of DataPoints are of equivalent DataPoints
    private boolean listsEqual(List<DataPoint> first, List<DataPoint> second) {
        if (first.size() != second.size()) {
            return false;
        }
        for (int i = 0; i < first.size(); i++) {
            for (int j = 0; j < first.get(0).numDimensions; j++) {
                if (first.get(i).dimensions.get(j).doubleValue() != second.get(i).dimensions.get(j).doubleValue()) {
                    return false;
                }
            }
        }
        return true;
    }

    public List<Cluster> run(int maxIterations) {
        for (int iteration = 0; iteration < maxIterations; iteration++) {
            for (Cluster cluster : clusters) { // clear all clusters
                cluster.points.clear();
            }
            assignClusters();
            List<DataPoint> oldCentroids = new ArrayList<>(centroids());
            generateCentroids(); // find new centroids
            if (listsEqual(oldCentroids, centroids())) {
                System.out.println("Converged after " + iteration + " iterations.");
                return clusters;
            }
        }
        return clusters;
    }
}