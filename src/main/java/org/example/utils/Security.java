package org.example.utils;

import javafx.scene.control.Alert;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Security {

    /**
     * Converts a byte array to a hexadecimal string.
     * @param hash The byte array to convert.
     * @return The hexadecimal string representation of the byte array.
     */
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * Hashes a password using the SHA-3 cryptographic hash function.
     * @param password The password to hash.
     * @return The hashed password as a hexadecimal string.
     * @throws NoSuchAlgorithmException if the specified algorithm is not available.
     */
    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        final MessageDigest digest = MessageDigest.getInstance("SHA3-256");
        final byte[] hashbytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hashbytes);
    }

    /**
     * Checks if the entered password matches the stored hashed password.
     * @param enteredPassword The entered password.
     * @param storedPassword The stored hashed password.
     * @return true if the passwords match, false otherwise.
     */
    public static boolean checkPassword(String enteredPassword, String storedPassword) {
        try {
            String hashedEnteredPassword = hashPassword(enteredPassword);
            return hashedEnteredPassword.equals(storedPassword);
        } catch (NoSuchAlgorithmException e) {
            showAlert("Error hashing the password.");
            return false;
        }
    }

    private static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }
}