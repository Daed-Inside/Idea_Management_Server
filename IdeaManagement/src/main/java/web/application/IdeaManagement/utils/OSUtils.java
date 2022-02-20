package web.application.IdeaManagement.utils;

import java.io.File;

public class OSUtils {

    private static boolean isMacOS() {
        return getOs() != null && getOs().contains("mac os");
    }

    private static boolean isWindows() {
        return getOs() != null && getOs().contains("windows");
    }

    private static boolean isLinux() {
        return getOs() != null && getOs().contains("linux");
    }

    private static String getOs() {
        String os = System.getProperty("os.name");

        if (os != null)
            os = os.toLowerCase();

        return os;
    }

    private static String getHelperDirectory() {
        if (isMacOS()) {
            return "./";
        }
        if (isLinux()) {
            return "/doc/";
        }
        if (isWindows()) {
            return "C:/";
        }
        return null;
    }

    public static File getApplicationDataFolder() {
        String rootFolder = getHelperDirectory();

        String dataFolder = (rootFolder != null ? rootFolder : "") + File.separator + "spro4";

        File folder = new File(dataFolder);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String applicationDataFolder = dataFolder + File.separator + File.separator  + "application_data";
        folder = new File(applicationDataFolder);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        return folder;
    }

}
