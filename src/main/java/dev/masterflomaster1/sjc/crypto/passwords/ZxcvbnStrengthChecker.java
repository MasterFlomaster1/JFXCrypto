package dev.masterflomaster1.sjc.crypto.passwords;

import com.nulabinc.zxcvbn.Zxcvbn;

public class ZxcvbnStrengthChecker implements StrengthChecker {

    private final Zxcvbn zxcvbn = new Zxcvbn();

    @Override
    public int getStrengthScore(String password) {
        return (int) ((zxcvbn.measure(password).getScore() / 4F) * 100);
    }

    @Override
    public CheckReport getStrengthReport(String password) {
        return null;
    }

}
