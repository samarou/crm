package com.itechart.security.web.util;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.util.FileCopyUtils;

import javax.net.ssl.*;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.X509Certificate;

import static com.itechart.security.web.util.TempFileUtil.closeStream;

public class FileUtil {

    public static void copyFileToResponse(File file, HttpServletResponse response) throws IOException {
        BufferedInputStream inputStream = null;
        ServletOutputStream outputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(file));
            outputStream = response.getOutputStream();
            FileCopyUtils.copy(inputStream, outputStream);
            outputStream.flush();
        } finally {
            closeStream(inputStream);
            closeStream(outputStream);
        }
    }

    public static void copyImageToResponse(URL url, HttpServletResponse response) throws IOException{
        BufferedInputStream inputStream = null;
        ServletOutputStream outputStream = null;
        try{
            URLConnection conn = url.openConnection();
            inputStream = new BufferedInputStream(conn.getInputStream());
            outputStream = response.getOutputStream();
            FileCopyUtils.copy(inputStream, outputStream);
            outputStream.flush();
        }finally {
            closeStream(inputStream);
            closeStream(outputStream);
        }
    }

    public static void fixSSLHandshakeException(){
        /**
         *  fix for
         *    Exception in thread "main" javax.net.ssl.SSLHandshakeException:
         *       sun.security.validator.ValidatorException:
         *           PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException:
         *               unable to find valid certification path to requested target
         */
        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {  }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {  }

                }
        };
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        }catch(Exception e){
            e.printStackTrace();
        }

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        /**
         * end of the fix
         */
    }
}
