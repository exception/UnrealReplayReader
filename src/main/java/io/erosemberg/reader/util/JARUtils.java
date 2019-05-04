package io.erosemberg.reader.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class JARUtils {

    private JARUtils() {
        // no op
    }

    public static void exportAndLoadResource(Class fromClass, String resourceName, String exportPath) {
        File temp = new File(exportPath);
        if (temp.exists()) {
            System.load(temp.getAbsolutePath());
            System.out.println("Already exported and loaded");
            return;
        }
        InputStream in = null;
        OutputStream out = null;
        try {
            in = fromClass.getResourceAsStream(resourceName);
            if (in == null) {
                throw new IOException("Cannot get resource \"" + resourceName + "\" from jar.");
            }

            int readBytes;
            byte[] buffer = new byte[4096];
            out = new FileOutputStream(exportPath);
            while ((readBytes = in.read(buffer)) > 0) {
                out.write(buffer, 0, readBytes);
            }

            System.load(new File(exportPath).getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
