package dev.masterflomaster1.jfxc.crypto.enigma;

public class Enigma {

    public Rotor leftRotor;
    public Rotor middleRotor;
    public Rotor rightRotor;

    public Reflector reflector;

    public Plugboard plugboard;

    public Enigma(String[] rotors, String reflector, int[] rotorPositions, int[] ringSettings, String plugboardConnections) {
        this.leftRotor = Rotor.create(rotors[0], rotorPositions[0], ringSettings[0]);
        this.middleRotor = Rotor.create(rotors[1], rotorPositions[1], ringSettings[1]);
        this.rightRotor = Rotor.create(rotors[2], rotorPositions[2], ringSettings[2]);
        this.reflector = Reflector.create(reflector);
        this.plugboard = new Plugboard(plugboardConnections);
    }

    public Enigma(EnigmaKey key) {
        this(key.rotors, "B", key.indicators, key.rings, key.plugboard);
    }

    public void rotate() {
        // If middle rotor notch - double-stepping
        if (middleRotor.isAtNotch()) {
            middleRotor.turnover();
            leftRotor.turnover();
        } else if (rightRotor.isAtNotch()) {
            // If left-rotor notch
            middleRotor.turnover();
        }

        // Increment right-most rotor
        rightRotor.turnover();
    }

    public int encrypt(int c) {
        rotate();

        // Plugboard in
        c = this.plugboard.forward(c);

        // Right to left
        int c1 = rightRotor.forward(c);
        int c2 = middleRotor.forward(c1);
        int c3 = leftRotor.forward(c2);

        // Reflector
        int c4 = reflector.forward(c3);

        // Left to right
        int c5 = leftRotor.backward(c4);
        int c6 = middleRotor.backward(c5);
        int c7 = rightRotor.backward(c6);

        // Plugboard out
        c7 = plugboard.forward(c7);

        return c7;
    }

    public char encrypt(char c) {
        return (char) (this.encrypt(c - 65) + 65);
    }

    public char[] encrypt(char[] input) {
        char[] output = new char[input.length];
        for (int i = 0; i < input.length; i++) {
            output[i] = this.encrypt(input[i]);
        }
        return output;
    }

    public char[] encrypt(String input) {
        input = input.toUpperCase().replace(" ", "");

        char[] output = new char[input.length()];
        for (int i = 0; i < input.length(); i++) {
            output[i] = this.encrypt(input.charAt(i));
        }
        return output;
    }

}
