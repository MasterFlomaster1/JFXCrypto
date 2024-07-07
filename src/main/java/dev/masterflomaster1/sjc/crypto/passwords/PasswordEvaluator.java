package dev.masterflomaster1.sjc.crypto.passwords;

public interface PasswordEvaluator {

    int getStrengthScore(String password);
    PasswordEvaluatorFeedback getStrengthReport(String password);

}
