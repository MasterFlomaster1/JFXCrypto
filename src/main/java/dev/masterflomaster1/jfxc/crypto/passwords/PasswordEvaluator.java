package dev.masterflomaster1.jfxc.crypto.passwords;

public interface PasswordEvaluator {

    int getStrengthScore(String password);
    PasswordEvaluatorFeedback getStrengthReport(String password);

}
