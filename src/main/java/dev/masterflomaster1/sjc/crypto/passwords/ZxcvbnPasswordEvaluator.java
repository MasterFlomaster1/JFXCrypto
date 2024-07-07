package dev.masterflomaster1.sjc.crypto.passwords;

import com.nulabinc.zxcvbn.Zxcvbn;

public class ZxcvbnPasswordEvaluator implements PasswordEvaluator {

    private final Zxcvbn zxcvbn = new Zxcvbn();

    @Override
    public int getStrengthScore(String password) {
        return (int) ((zxcvbn.measure(password).getScore() / 4F) * 100);
    }

    @Override
    public PasswordEvaluatorFeedback getStrengthReport(String password) {
        return new ZxcvbnPasswordEvaluatorFeedback(zxcvbn.measure(password).getFeedback());
    }

}
