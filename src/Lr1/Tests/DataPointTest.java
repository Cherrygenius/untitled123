package Lr1.Tests;

import Lr1.DataPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DataPointTest
{
    private DataPoint point1;
    private DataPoint point2;

    @BeforeEach
    public void setUp() {
        point1 = new DataPoint(List.of(1.0, 2.0, 3.0));
        point2 = new DataPoint(List.of(4.0, 5.0, 6.0));
    }

    @Test
    public void testDistance() {
        double distance = point1.distance(point2);
        assertEquals(Math.sqrt(27), distance, 0.0001); // Проверяем, что расстояние корректно
    }

    @Test
    public void testToString() {
        assertEquals("[1.0, 2.0, 3.0]", point1.toString()); // Проверяем строковое представление
    }
}
