package Cipher;

public class SimpleCipher {

    private static String allChars = ("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890 ,.!?:;()*&^%$#@-=+_");
    private static String mixedChars = ("E0&NqO^LaP9JzH%D GxA$Ts3.w)eM=Xd:c6v#f2,8*r5Vtg-b7nIhQy?WuRjU!mS1Yk(iZ;BColp@F+4K_");

    public static String crypt(boolean encrypting, String line) {
        StringBuilder encrypted = new StringBuilder();
        for (int i=0;i<line.length();i++) {
            encrypted.append(cryptChar(encrypting, line.charAt(i)));
        }
        return encrypted.toString();
    }

    private static char cryptChar(boolean encrypt, char c) {
        int index = 0;
        if (encrypt) {
            for (int i = 0; i < allChars.length(); i++) {
                if (c == allChars.charAt(i)) {
                    index = i;
                    break;
                } else {
                    index++;
                }
            }
            return mixedChars.charAt(index);
        } else {
            for (int i = 0; i < mixedChars.length(); i++) {
                if (c == mixedChars.charAt(i)) {
                    index = i;
                    break;
                } else
                    index++;
            }
            return allChars.charAt(index);
        }
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
