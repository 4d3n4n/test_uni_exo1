package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordValidatorTest {

    private final PasswordValidator validator = new PasswordValidator();

    // --- Tests classiques ---

    @Test
    void shouldBeValidForPassword1() {
        assertTrue(validator.isValid("Password1!"));
    }

    @Test
    void shouldBeValidForAdmin2024() {
        assertTrue(validator.isValid("Admin2024@"));
    }

    @Test
    void shouldBeInvalidForShortPassword() {
        assertFalse(validator.isValid("short1!"));
    }

    @Test
    void shouldBeInvalidWhenNoLowercase() {
        assertFalse(validator.isValid("PASSWORD1!"));
    }

    @Test
    void shouldBeInvalidWhenNoUppercase() {
        assertFalse(validator.isValid("password1!"));
    }

    @Test
    void shouldBeInvalidWhenNoDigit() {
        assertFalse(validator.isValid("Password!"));
    }

    @Test
    void shouldBeInvalidWhenNoSpecialChar() {
        assertFalse(validator.isValid("Password1"));
    }

    @Test
    void shouldBeInvalidForNull() {
        assertFalse(validator.isValid(null));
    }

    // --- Tests paramétrés @CsvSource ---

    @ParameterizedTest
    @CsvSource({
        "Password1!, true",
        "Admin2024@, true",
        "short1!, false",
        "PASSWORD1!, false",
        "password1!, false",
        "Password!, false",
        "Password1, false"
    })
    void shouldValidatePasswordWithCsvSource(String password, boolean expected) {
        assertEquals(expected, validator.isValid(password));
    }

    // --- Test @ValueSource ---

    @ParameterizedTest
    @ValueSource(strings = {"Password1!", "Admin2024@"})
    void shouldBeValidForValidPasswords(String password) {
        assertTrue(validator.isValid(password));
    }

    // --- Test @MethodSource ---

    static Stream<String> invalidPasswords() {
        return Stream.of("short1!", "PASSWORD1!", "password1!", "Password!", "Password1");
    }

    @ParameterizedTest
    @MethodSource("invalidPasswords")
    void shouldBeInvalidForInvalidPasswords(String password) {
        assertFalse(validator.isValid(password));
    }

    // --- Vérification des messages d'erreur ---

    @Test
    void shouldReturnNullMessage() {
        assertEquals("Password must not be null", validator.getErrorMessage(null));
    }

    @Test
    void shouldReturnTooShortMessage() {
        assertEquals("Password must contain at least 8 characters", validator.getErrorMessage("Sh1!"));
    }

    @Test
    void shouldReturnNoLowercaseMessage() {
        assertEquals("Password must contain at least one lowercase letter", validator.getErrorMessage("PASSWORD1!"));
    }

    @Test
    void shouldReturnNoUppercaseMessage() {
        assertEquals("Password must contain at least one uppercase letter", validator.getErrorMessage("password1!"));
    }

    @Test
    void shouldReturnNoDigitMessage() {
        assertEquals("Password must contain at least one digit", validator.getErrorMessage("Password!"));
    }

    @Test
    void shouldReturnNoSpecialCharMessage() {
        assertEquals("Password must contain at least one special character", validator.getErrorMessage("Password1"));
    }

    @Test
    void shouldReturnValidMessage() {
        assertEquals("Password is valid", validator.getErrorMessage("Password1!"));
    }

    // --- Bonus : @NullAndEmptySource ---

    @ParameterizedTest
    @NullAndEmptySource
    void shouldBeInvalidForNullAndEmpty(String password) {
        assertFalse(validator.isValid(password));
    }
}
