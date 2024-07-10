package dev.masterflomaster1.sjc.crypto.enigma;

public class Reflector {

    protected int[] forwardWiring;

    public Reflector(String encoding) {
        this.forwardWiring = decodeWiring(encoding);
    }

    public static Reflector create(String name) {
        return switch (name) {
            case "B" -> new Reflector("YRUHQSLDPXNGOKMIEBFZCWVJAT");
            case "C" -> new Reflector("FVPJIAOYEDRZXWGCTKUQSBNMHL");
            default -> new Reflector("ZYXWVUTSRQPONMLKJIHGFEDCBA");
        };
    }

    protected static int[] decodeWiring(String encoding) {
        char[] charWiring = encoding.toCharArray();
        int[] wiring = new int[charWiring.length];
        for (int i = 0; i < charWiring.length; i++) {
            wiring[i] = charWiring[i] - 65;
        }
        return wiring;
    }

    public int forward(int c) {
        return this.forwardWiring[c];
    }

}
