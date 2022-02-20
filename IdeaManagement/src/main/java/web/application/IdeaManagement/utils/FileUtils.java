package web.application.IdeaManagement.utils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {
    public static Path getAttachmentFilePath(String type, String fileName) {
        Path path = Paths.get(OSUtils.getApplicationDataFolder()
                + File.separator + type
                + File.separator + fileName);
        return path;
    }
}
