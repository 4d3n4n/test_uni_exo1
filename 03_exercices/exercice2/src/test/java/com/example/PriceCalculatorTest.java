package com.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PriceCalculatorTest {

    // --- Cas nominaux ---

    @Test
    void shouldCalculateTotalPrice() {
        // Arrange
        PriceCalculator calculator = new PriceCalculator();

        // Act
        double result = calculator.calculateTotalPrice(10.0, 3);

        // Assert
        assertEquals(30.0, result);
    }

    @Test
    void shouldApplyDiscount() {
        // Arrange
        PriceCalculator calculator = new PriceCalculator();

        // Act
        double result = calculator.applyDiscount(100.0, 0.20);

        // Assert
        assertEquals(80.0, result);
    }

    @Test
    void shouldCalculateVat() {
        // Arrange
        PriceCalculator calculator = new PriceCalculator();

        // Act
        double result = calculator.calculateVat(100.0, 0.20);

        // Assert
        assertEquals(20.0, result);
    }

    @Test
    void shouldCalculatePriceWithVat() {
        // Arrange
        PriceCalculator calculator = new PriceCalculator();

        // Act
        double result = calculator.calculatePriceWithVat(100.0, 0.20);

        // Assert
        assertEquals(120.0, result);
    }

    // --- Cas d'erreur ---

    @Test
    void shouldThrowExceptionWhenUnitPriceIsNegative() {
        // Arrange
        PriceCalculator calculator = new PriceCalculator();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> calculator.calculateTotalPrice(-10.0, 3)
        );
        assertEquals("Le prix unitaire ne doit pas être négatif.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenQuantityIsNegative() {
        // Arrange
        PriceCalculator calculator = new PriceCalculator();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> calculator.calculateTotalPrice(10.0, -3)
        );
        assertEquals("La quantité ne doit pas être négative.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenDiscountRateIsNegative() {
        // Arrange
        PriceCalculator calculator = new PriceCalculator();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> calculator.applyDiscount(100.0, -0.20)
        );
        assertEquals("Le taux de remise ne doit pas être négatif.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenVatRateIsNegativeInCalculateVat() {
        // Arrange
        PriceCalculator calculator = new PriceCalculator();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> calculator.calculateVat(100.0, -0.20)
        );
        assertEquals("Le taux de TVA ne doit pas être négatif.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenVatRateIsNegativeInCalculatePriceWithVat() {
        // Arrange
        PriceCalculator calculator = new PriceCalculator();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> calculator.calculatePriceWithVat(100.0, -0.20)
        );
        assertEquals("Le taux de TVA ne doit pas être négatif.", exception.getMessage());
    }
}
