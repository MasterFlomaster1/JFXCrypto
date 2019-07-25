package Util;

public class SystemInfo {

    public static boolean isWindows = false;
    public static boolean isMacOs = false;
    public static boolean isLinux = false;

    public static void operatingSystem() {
        String operatingSystem = System.getProperty("os.name");
        if (operatingSystem.toLowerCase().contains("win")) {
            isWindows = true;
        } else if (operatingSystem.toLowerCase().contains("osx")) {
            isMacOs = true;
        } else if (operatingSystem.toLowerCase().contains("nix") || operatingSystem.contains("aix") || operatingSystem.contains("nux")) {
            isLinux = true;
        }
    }

}
