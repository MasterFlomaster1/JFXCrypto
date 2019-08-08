package Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

public class PathCutter {

    public static String cutPath(String path) {
        if (path.length() <= 62) return path;
        String pattern = Pattern.quote(System.getProperty("file.separator"));
        ArrayList<String> subStr = new ArrayList<>(Arrays.asList(path.split(pattern)));
        int size = path.length();
        int prev = 0;
        while (size>62) {
            if (prev!=0) {
                subStr.remove(prev);
            }
            int index = subStr.size()/2;
            size-=subStr.get(index).length();
            subStr.set(index, "...");
            prev = index;
        }
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < subStr.size(); i++) {
            str.append(subStr.get(i));
            if (i<subStr.size()-1) {
                str.append(File.separator);
            }
        }
        return str.toString();
    }

}
