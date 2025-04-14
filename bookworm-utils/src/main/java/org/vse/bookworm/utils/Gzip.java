package org.vse.bookworm.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.zip.GZIPOutputStream;

public final class Gzip {
    private Gzip() {
    }

    public static String compressToBase64(String source) {
        if (source == null || source.isEmpty()) {
            return source;
        }

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             GZIPOutputStream gzip = new GZIPOutputStream(out);) {
            gzip.write(source.getBytes());
            gzip.close();
            return Base64.getEncoder().encodeToString(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
