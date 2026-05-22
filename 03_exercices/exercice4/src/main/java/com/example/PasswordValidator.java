package com.example;

public class PasswordValidator {

    public boolean isValid(String password) {
        return "Password is valid".equals(getErrorMessage(password));
    }

    public String getErrorMessage(String password) {
        if (password == null) {
            return "Password must not be null";
        }
        if (password.length() < 8) {
            return "Password must contain at least 8 characters";
        }
        if (!password.matches(".*[a-z].*")) {
            return "Password must contain at least one lowercase letter";
        }
        if (!password.matches(".*[A-Z].*")) {
            return "Password must contain at least one uppercase letter";
        }
        if (!password.matches(".*[0-9].*")) {
            return "Password must contain at least one digit";
        }
        if (!password.matches(".*[!@#$%].*")) {
            return "Password must contain at least one special character";
        }
        return "Password is valid";
    }
}
