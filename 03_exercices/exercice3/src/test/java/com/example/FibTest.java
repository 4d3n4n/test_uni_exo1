package com.example;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FibTest {

    // --- Range = 1 ---

    @Test
    void shouldReturnNonEmptyListWhenRangeIs1() {
        Fib fib = new Fib(1);
        List<Integer> result = fib.getFibSeries();
        assertFalse(result.isEmpty());
    }

    @Test
    void shouldReturnListContainingZeroWhenRangeIs1() {
        Fib fib = new Fib(1);
        List<Integer> result = fib.getFibSeries();
        assertEquals(List.of(0), result);
    }

    // --- Range = 6 ---

    @Test
    void shouldContain3WhenRangeIs6() {
        Fib fib = new Fib(6);
        List<Integer> result = fib.getFibSeries();
        assertTrue(result.contains(3));
    }

    @Test
    void shouldContain6ElementsWhenRangeIs6() {
        Fib fib = new Fib(6);
        List<Integer> result = fib.getFibSeries();
        assertEquals(6, result.size());
    }

    @Test
    void shouldNotContain4WhenRangeIs6() {
        Fib fib = new Fib(6);
        List<Integer> result = fib.getFibSeries();
        assertFalse(result.contains(4));
    }

    @Test
    void shouldReturnCorrectSequenceWhenRangeIs6() {
        Fib fib = new Fib(6);
        List<Integer> result = fib.getFibSeries();
        assertEquals(List.of(0, 1, 1, 2, 3, 5), result);
    }

    @Test
    void shouldBeSortedAscendingWhenRangeIs6() {
        Fib fib = new Fib(6);
        List<Integer> result = fib.getFibSeries();
        for (int i = 0; i < result.size() - 1; i++) {
            assertTrue(result.get(i) <= result.get(i + 1));
        }
    }
}
