package dev.masterflomaster1.sjc.crypto.passwords;

public class PasswordEvaluatorService {

    private static PasswordEvaluatorService instance;
    private final ZxcvbnPasswordEvaluator checker = new ZxcvbnPasswordEvaluator();

    private PasswordEvaluatorService() {
    }

    public static PasswordEvaluatorService of() {
        if (instance == null)
            instance = new PasswordEvaluatorService();

        return instance;
    }

    public ZxcvbnPasswordEvaluator getZxcvbn() {
        return checker;
    }

}
