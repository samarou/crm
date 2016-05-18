package com.itechart.security.web.util;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;

import java.io.*;

public class FileUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    private static File uploadDir;

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
        File temp = File.createTempFile("crm_project", ".tmp");
        saveFile(fileInputStream, temp);
        return temp.getAbsolutePath();
    }

    public static void saveFile(InputStream fileInputStream, File path) throws IOException {
        logger.debug("saving file to path: {}", path);
        BufferedOutputStream outputStream = null;
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(path));
            FileCopyUtils.copy(fileInputStream, outputStream);
        } finally {
            closeStream(fileInputStream);
            closeStream(outputStream);
        }
    }

    public static File getAttachment(Long contactId, Long attachmentId) {
        return FileUtils.getFile(getAttachmentPath(contactId, attachmentId));
    }

    public static File getAttachmentPath(Long contactId, Long attachmentId) {
        File contactFilesDir = getContactDirectory(contactId);
        return new File(contactFilesDir, attachmentId.toString());
    }

    private static File getContactDirectory(Long contactId) {
        File contactFilesDir = new File(getContactsDirectory(), contactId.toString());
        createDirectoryIfNotExists(contactFilesDir);
        return contactFilesDir;
    }

    private static File getContactsDirectory() {
        File contactsDir = new File(uploadDir, "contacts");
        createDirectoryIfNotExists(contactsDir);
        return contactsDir;
    }

    public void setUploadDir(String uploadDir) {
        FileUtil.uploadDir = new File(uploadDir);
        createDirectoryIfNotExists(FileUtil.uploadDir);
    }

    private static void createDirectoryIfNotExists(File directory) {
        if (!directory.exists()) {
            createDirectory(directory);
        }
    }

    private static void createDirectory(File directory) {
        if (directory.mkdirs()) {
            logger.debug("directory created: {}", directory);
        } else {
            if (!directory.exists()) {
                logger.debug("can't create directory: {}", directory);
                throw new RuntimeException("problem with file system on server occurred");
            }
        }
    }
}
