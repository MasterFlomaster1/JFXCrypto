package dev.masterflomaster1.sjc.crypto.impl;

/**
 * <a href="https://en.wikipedia.org/wiki/Hill_cipher">Hill cipher</a>
 */
public final class HillCipherImpl {

    private static final int ALPHABET_SIZE = 26;

    private HillCipherImpl() { }

    public static String encrypt(String plaintext, int[][] key) {
        plaintext = plaintext.toUpperCase().replaceAll("[^A-Z]", "");
        StringBuilder ciphertext = new StringBuilder();

        int blockSize = key.length;

        // Pad plaintext if needed
        while (plaintext.length() % blockSize != 0) {
            plaintext += 'X';
        }

        // Encrypt each block
        for (int i = 0; i < plaintext.length(); i += blockSize) {
            String block = plaintext.substring(i, i + blockSize);
            int[] blockVector = new int[blockSize];
            for (int j = 0; j < blockSize; j++) {
                blockVector[j] = block.charAt(j) - 'A';
            }
            int[] encryptedBlockVector = multiplyMatrixVector(key, blockVector);
            for (int value : encryptedBlockVector) {
                char encryptedChar = (char) ('A' + value % ALPHABET_SIZE);
                ciphertext.append(encryptedChar);
            }
        }

        return ciphertext.toString();
    }

    public static String decrypt(String ciphertext, int[][] key) {
        int determinant = getDeterminant(key);
        int inverseDeterminant = modInverse(determinant, ALPHABET_SIZE);
        int[][] adjugate = getAdjugate(key);

        int[][] inverseKey = multiplyMatrixScalar(adjugate, inverseDeterminant);
        for (int i = 0; i < inverseKey.length; i++) {
            for (int j = 0; j < inverseKey[i].length; j++) {
                inverseKey[i][j] = (inverseKey[i][j] % ALPHABET_SIZE + ALPHABET_SIZE) % ALPHABET_SIZE; // Ensure positive values
            }
        }

        return encrypt(ciphertext, inverseKey);
    }

    private static int[] multiplyMatrixVector(int[][] matrix, int[] vector) {
        int[] result = new int[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            int sum = 0;
            for (int j = 0; j < matrix[i].length; j++) {
                sum += matrix[i][j] * vector[j];
            }
            result[i] = sum;
        }
        return result;
    }

    private static int[][] multiplyMatrixScalar(int[][] matrix, int scalar) {
        int[][] result = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                result[i][j] = matrix[i][j] * scalar;
            }
        }
        return result;
    }

    private static int getDeterminant(int[][] matrix) {
        if (matrix.length != matrix[0].length) {
            throw new IllegalArgumentException("Matrix is not square.");
        }
        if (matrix.length == 1) {
            return matrix[0][0];
        }
        if (matrix.length == 2) {
            return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
        }
        int determinant = 0;
        for (int i = 0; i < matrix.length; i++) {
            determinant += matrix[0][i] * getCofactor(matrix, 0, i);
        }
        return determinant;
    }

    private static int getCofactor(int[][] matrix, int row, int col) {
        return (int) Math.pow(-1, row + col) * getMinor(matrix, row, col);
    }

    private static int getMinor(int[][] matrix, int row, int col) {
        int[][] minor = new int[matrix.length - 1][matrix.length - 1];
        int minorRow = 0;
        for (int i = 0; i < matrix.length; i++) {
            if (i == row) {
                continue;
            }
            int minorCol = 0;
            for (int j = 0; j < matrix[i].length; j++) {
                if (j == col) {
                    continue;
                }
                minor[minorRow][minorCol] = matrix[i][j];
                minorCol++;
            }
            minorRow++;
        }
        return getDeterminant(minor);
    }

    private static int[][] getAdjugate(int[][] matrix) {
        int[][] adjugate = new int[matrix.length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                adjugate[j][i] = getCofactor(matrix, i, j);
            }
        }
        return adjugate;
    }

    private static int modInverse(int a, int m) {
        a = a % m;
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) {
                return x;
            }
        }
        return 1;
    }

}
