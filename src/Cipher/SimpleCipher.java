package Cipher;

public class SimpleCipher {

    private static String allChars = ("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890 ,.!?:;()*&^%$#@-=+_");
    private static String combination = null;
    private static String key = null;


    public SimpleCipher() {
        updateCombination();
    }

    public static String encryption(String text) {
        StringBuilder encrypted = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            int index = 0;
            char a = text.charAt(i);

            for (int j = 0; j < allChars.length(); j++) {
                if (a == allChars.charAt(j)) {
                    index = j;
                    break;
                } else {
                    index++;
                }
            }
            encrypted.append(combination.charAt(index));
        }
        return encrypted.toString();
    }

    public static String decryption(String text) {
        StringBuilder decrypted = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            int index = 0;
            char a = text.charAt(i);

            for (int j = 0; j < combination.length(); j++) {
                if (a == combination.charAt(j)) {
                    index = j;
                    break;
                } else
                    index++;
            }
            decrypted.append(allChars.charAt(index));
        }
        return decrypted.toString();
    }

    public static void updateCombination() {

        int[] arr = new int[allChars.length()];
        randomSort(arr);
        String temp = "";
        for (int value : arr) {
            temp = temp.concat(String.valueOf(combination.charAt(value - 1)));
        }
        combination = temp;
    }

    public static void setKey(String key) {

    }

    public static String getKey() {
        return key;
    }

    private static int[] getCombFromKey() {
        StringBuilder num = new StringBuilder();
        int[] mixPos = new int[allChars.length()];
        int index = 0;
        for (int i = 0; i < combination.length(); i++) {
            if (Character.isDigit(combination.charAt(i))) {
                num.append(combination.charAt(i));
            } else {
                mixPos[index] = Integer.parseInt(num.toString());
                index++;
                num = new StringBuilder();
            }
        }
        return mixPos;
    }

    private static void randomSort(int[] inputArray){
        int count = 0;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < inputArray.length; i++){
            int randomNumber = (int)(1+(Math.random()*inputArray.length));
            inputArray[i] = randomNumber;
            count++;
            for (int q = 0; q < count; q++){
                if (q==i) {
                    builder.append(randomNumber).append(allChars.charAt((int)(Math.random()*56+1)));
                    break;
                }

                if (randomNumber == inputArray[q]){
                    i--;
                    count--;
                    break;
                }
            }
        }
        key = builder.toString();
    }

    private static void randomSort() {

    }

}
