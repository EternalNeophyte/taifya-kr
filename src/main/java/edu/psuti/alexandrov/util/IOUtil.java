package edu.psuti.alexandrov.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class IOUtil {

    public static final String ROOT_PATH = "src\\main\\resources\\";
    public static final String DELIMITER = "\\";
    public static final String TXT = ".txt";

    public static String readTxt(String... relativePathFragments) {
        return readTxt(String.join(DELIMITER, relativePathFragments));
    }

    public static String readTxt(String relativePath) {
        try {
            return Files.readString(Path.of(ROOT_PATH + relativePath + TXT));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage readImage(String relativePath) {
        try {
            return ImageIO.read(new File(ROOT_PATH + relativePath));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
