package dev.masterflomaster1.jfxc.crypto.passwords;

import com.nulabinc.zxcvbn.Feedback;

public class ZxcvbnPasswordEvaluatorFeedback implements PasswordEvaluatorFeedback {

    private final Feedback feedback;

    public ZxcvbnPasswordEvaluatorFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    @Override
    public String getCombined() {

        String builder = "Warning: " + feedback.getWarning() + "\n\n" +
                "Suggestions: " + String.join("\n", feedback.getSuggestions());
        return builder;
    }

}
