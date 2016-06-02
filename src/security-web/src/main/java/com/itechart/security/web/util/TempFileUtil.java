package com.itechart.security.web.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;

import java.io.*;

public class TempFileUtil {

    private static final Logger logger = LoggerFactory.getLogger(TempFileUtil.class);

    public static <T extends Closeable> void closeStream(T stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                logger.error("can't close stream", e);
            }
        }
    }

    public static String saveTempFile(InputStream fileInputStream) throws IOException {
        File tempPath = File.createTempFile("crm_project", ".tmp");
        logger.debug("saving file to temp path: {}", tempPath);
        BufferedOutputStream outputStream = null;
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(tempPath));
            FileCopyUtils.copy(fileInputStream, outputStream);
        } finally {
            closeStream(fileInputStream);
            closeStream(outputStream);
        }
        return tempPath.getAbsolutePath();
    }
}
