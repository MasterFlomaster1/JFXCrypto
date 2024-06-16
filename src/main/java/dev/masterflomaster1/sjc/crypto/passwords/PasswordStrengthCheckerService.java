package dev.masterflomaster1.sjc.crypto.passwords;

public class PasswordStrengthCheckerService {

    private static PasswordStrengthCheckerService instance;
    private final ZxcvbnStrengthChecker checker = new ZxcvbnStrengthChecker();

    private PasswordStrengthCheckerService() {
    }

    public static PasswordStrengthCheckerService getInstance() {
        if (instance == null)
            instance = new PasswordStrengthCheckerService();

        return instance;
    }

    public ZxcvbnStrengthChecker getChecker() {
        return checker;
    }

}
