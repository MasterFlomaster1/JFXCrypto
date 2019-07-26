package Cipher;

public class SimpleCipher {

    private static String allChars = ("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890 ,.!?:;()*&^%$#@-=+_");
    private static String mixedChars = ("E0&NqO^LaP9JzH%D GxA$Ts3.w)eM=Xd:c6v#f2,8*r5Vtg-b7nIhQy?WuRjU!mS1Yk(iZ;BColp@F+4K_");

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
            encrypted.append(mixedChars.charAt(index));
        }
        return encrypted.toString();
    }

    public static String decryption(String text) {
        StringBuilder decrypted = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            int index = 0;
            char a = text.charAt(i);

            for (int j = 0; j < mixedChars.length(); j++) {
                if (a == mixedChars.charAt(j)) {
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
            temp = temp.concat(String.valueOf(mixedChars.charAt(value - 1)));
        }
        mixedChars = temp;
    }

    private static void randomSort(int[] inputArray){
        int count = 0;

        for (int i = 0; i < inputArray.length; i++){
            int randomNumber = (int)(1+(Math.random()*inputArray.length));
            inputArray[i] = randomNumber;
            count++;

            for (int q = 0; q < count; q++){
                if (randomNumber==inputArray[q] && q != i){
                    i--;
                    count--;
                    break;
                }
            }
        }
    }

}
