package com.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GradingCalculatorTest {

    @Test
    void shouldReturnAWhenScoreIs95AndAttendanceIs90() {
        // Arrange
        GradingCalculator calculator = new GradingCalculator(95, 90);

        // Act
        char grade = calculator.getGrade();

        // Assert
        assertEquals('A', grade);
    }

    @Test
    void shouldReturnBWhenScoreIs85AndAttendanceIs90() {
        // Arrange
        GradingCalculator calculator = new GradingCalculator(85, 90);

        // Act
        char grade = calculator.getGrade();

        // Assert
        assertEquals('B', grade);
    }

    @Test
    void shouldReturnCWhenScoreIs65AndAttendanceIs90() {
        // Arrange
        GradingCalculator calculator = new GradingCalculator(65, 90);

        // Act
        char grade = calculator.getGrade();

        // Assert
        assertEquals('C', grade);
    }

    @Test
    void shouldReturnBWhenScoreIs95AndAttendanceIs65() {
        // Arrange
        GradingCalculator calculator = new GradingCalculator(95, 65);

        // Act
        char grade = calculator.getGrade();

        // Assert
        assertEquals('B', grade);
    }

    @Test
    void shouldReturnFWhenScoreIs95AndAttendanceIs55() {
        // Arrange
        GradingCalculator calculator = new GradingCalculator(95, 55);

        // Act
        char grade = calculator.getGrade();

        // Assert
        assertEquals('F', grade);
    }

    @Test
    void shouldReturnFWhenScoreIs65AndAttendanceIs55() {
        // Arrange
        GradingCalculator calculator = new GradingCalculator(65, 55);

        // Act
        char grade = calculator.getGrade();

        // Assert
        assertEquals('F', grade);
    }

    @Test
    void shouldReturnFWhenScoreIs50AndAttendanceIs90() {
        // Arrange
        GradingCalculator calculator = new GradingCalculator(50, 90);

        // Act
        char grade = calculator.getGrade();

        // Assert
        assertEquals('F', grade);
    }
}
