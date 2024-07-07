package dev.masterflomaster1.sjc.crypto.passwords;

public class PasswordEvaluatorService {

    private static PasswordEvaluatorService instance;
    private final ZxcvbnPasswordEvaluator checker = new ZxcvbnPasswordEvaluator();

    private PasswordEvaluatorService() {
    }

    public static PasswordEvaluatorService getInstance() {
        if (instance == null)
            instance = new PasswordEvaluatorService();

        return instance;
    }

    public ZxcvbnPasswordEvaluator getChecker() {
        return checker;
    }

}
