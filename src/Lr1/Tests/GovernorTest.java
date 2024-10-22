package Lr1.Tests;

import Lr1.Governor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GovernorTest {
    private Governor governor;

    @BeforeEach
    public void setUp() {
        governor = new Governor(-86.79113, 72, "Alabama");
    }

    @Test
    public void testToString() {
        assertEquals("Alabama: (longitude: -86.79113, age: 72.0)", governor.toString()); // Проверяем строковое представление
    }
}
