package dev.masterflomaster1.sjc.crypto.passwords;

public interface StrengthChecker {

    int getStrengthScore(String password);
    CheckReport getStrengthReport(String password);

}
