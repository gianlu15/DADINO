package com.example;

import java.io.File;
import java.net.URISyntaxException;

public class GestisciFile {

    public static String ottieniDirectory() throws URISyntaxException {
        String jarPath = new File(MainClass.class.getProtectionDomain().getCodeSource().getLocation().toURI())
                .getPath();

        String jarDir = new File(jarPath).getParent();
        String directoryPath = jarDir + File.separator + "File_di_gioco";

        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdir();
        }

        return directoryPath;
    }
}
