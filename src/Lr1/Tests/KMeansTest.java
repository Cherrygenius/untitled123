package Lr1.Tests;

import Lr1.Governor;
import Lr1.KMeans;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class KMeansTest {
    private List<Governor> governors;


    @BeforeEach
    public void setUp() {
        governors = new ArrayList<>();
        governors.add(new Governor(-86.79113, 72, "Alabama"));
        governors.add(new Governor(-152.404419, 66, "Alaska"));
        governors.add(new Governor(-111.431221, 53, "Arizona"));
        governors.add(new Governor(-92.373123, 66, "Arkansas"));
        // Добавьте больше губернаторов, если нужно
    }

    @Test
    public void testKMeansInitialization() {
        KMeans<Governor> kmeans = new KMeans<>(2, governors);
        assertEquals(2, kmeans.clusters.size()); // Проверяем, что количество кластеров равно 2
    }

    @Test
    public void testRunMethodConvergence() {
        KMeans<Governor> kmeans = new KMeans<>(2, governors);
        List<KMeans<Governor>.Cluster> clusters = kmeans.run(100);
        assertNotNull(clusters); // Проверяем, что метод run возвращает не null
        assertEquals(2, clusters.size()); // Проверяем, что количество кластеров равно 2
    }

    @Test
    public void testClusterAssignment() {
        KMeans<Governor> kmeans = new KMeans<>(2, governors);
        kmeans.run(100);
        for (KMeans<Governor>.Cluster cluster : kmeans.clusters) {
            assertFalse(cluster.points.isEmpty(), "Cluster should not be empty");
        }
    }

    @Test
    public void testGenerateCentroids() {
        KMeans<Governor> kmeans = new KMeans<>(2, governors);
        kmeans.run(100);
        for (KMeans<Governor>.Cluster cluster : kmeans.clusters) {
            if (!cluster.points.isEmpty()) {
                assertNotNull(cluster.centroid); // Проверяем, что центроид не null
            }
        }
    }
}
