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

    public static void saveFile(InputStream fileInputStream, File path) throws IOException {
            BufferedOutputStream outputStream = null;
            try {
                outputStream = new BufferedOutputStream(new FileOutputStream(path));
                FileCopyUtils.copy(fileInputStream, outputStream);
            } finally {
                closeStream(fileInputStream);
                closeStream(outputStream);
            }
    }

    public static File getAttachmentPath(Long contactId, Long attachmentId) {
        File contactFilesDir = getContactUploadDirectory(contactId);
        return new File(contactFilesDir, attachmentId.toString());
    }

    public static File getContactUploadDirectory(Long contactId){
        File contactFilesDir = new File(uploadDir, contactId.toString());
        if (contactFilesDir.mkdirs()) {
            logger.debug("directory for contact created: {}", contactFilesDir);
        } else {
            logger.debug("can't create directory for contact: {}", contactFilesDir);
            // todo: throw exception
        }
        return contactFilesDir;
    }

    public static File getAttachment(Long contactId, Long attachmentId) {
        return  FileUtils.getFile(uploadDir, contactId.toString(), attachmentId.toString());
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = new File(uploadDir);
        if (this.uploadDir.mkdirs()) {
            logger.debug("directory created: {}", this.uploadDir);
        } else {
            logger.debug("can't create directory {}", this.uploadDir);
            // todo: throw exception
        }
    }
}
