package Utils;

public class RepairString {

    public static String repairString(String input) {
        int index = 0;
        for (int i = input.length()-1; i >= 0; i--) {
            if (!Character.isWhitespace(input.charAt(i))) {
                index = i;
                break;
            }
        }
        index++;
        return input.substring(0, index);
    }

}
