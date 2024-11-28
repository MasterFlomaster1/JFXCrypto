package dev.masterflomaster1.jfxc.core.passwords;

public interface PasswordEvaluator {

    int getStrengthScore(String password);
    PasswordEvaluatorFeedback getStrengthReport(String password);

}
