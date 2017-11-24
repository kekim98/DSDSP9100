
package com.dignsys.dsdsp.dsdsp_9100.util;

import android.content.Context;

import com.turbomanage.httpclient.BasicHttpClient;

import org.mozilla.universalchardet.UniversalDetector;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Utility methods and constants used for writing and reading to from streams and files.
 */
public class IOUtils {

    public static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final boolean AUTHORIZATION_TO_BACKEND_REQUIRED = false;

    /**
     * Writes the given string to a {@link File}.
     *
     * @param data The data to be written to the File.
     * @param file The File to write to.
     * @throws IOException
     */
    public static void writeToFile(String data, File file) throws IOException {
        writeToFile(data.getBytes(CHARSET_UTF8), file);
    }

    /**
     * Write the given bytes to a {@link File}.
     *
     * @param data The bytes to be written to the File.
     * @param file The {@link File} to be used for writing the data.
     * @throws IOException
     */
    public static void writeToFile(byte[] data, File file) throws IOException {
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            os.write(data);
            os.flush();
            // Perform an fsync on the FileOutputStream.
            os.getFD().sync();
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }

    /**
     * Write the given content to an {@link OutputStream}
     * <p/>
     * Note: This method closes the given OutputStream.
     *
     * @param content The String content to write to the OutputStream.
     * @param os      The OutputStream to which the content should be written.
     * @throws IOException
     */
    public static void writeToStream(String content, OutputStream os) throws IOException {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(os, CHARSET_UTF8));
            writer.write(content);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * Reads a {@link File} as a String
     *
     * @param file The file to be read in.
     * @return Returns the contents of the File as a String.
     * @throws IOException
     */
    public static String readFileAsString(File file) throws IOException {
        return readAsString(new FileInputStream(file));
    }

    /**
     * Reads an {@link InputStream} into a String using the UTF-8 encoding.
     * Note that this method closes the InputStream passed to it.
     *
     * @param is The InputStream to be read.
     * @return The contents of the InputStream as a String.
     * @throws IOException
     */
    public static String readAsString(InputStream is) throws IOException {
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            String line;
            reader = new BufferedReader(new InputStreamReader(is, CHARSET_UTF8));
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return sb.toString();
    }

    public static String getFilename(String path) {

        return new File(path).getName();
    }

    public static File getContentFile(Context context, String filename) {
        File folder = new File(context.getFilesDir(), getContentFilePath());
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return new File(folder, filename);
    }

    public static String getContentFilePath() {

        return "test-content";
    }


    public static void removeUnusedContents(Context mContext, final ArrayList<String> usedContents) {
        // remove all files are stored in the content path but are not used
        File folder = new File(mContext.getFilesDir(), getContentFilePath());
        File[] unused = folder.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String filename) {
                return !usedContents.contains(filename);
            }
        });

        if (unused != null) {
            for (File f : unused) {
                f.delete();
            }
        }
    }

    public static boolean hasContent(Context mContext, String filename) {
        return getContentFile(mContext, filename).exists();
    }

    /**
     * If {@code AUTHORIZATION_TO_BACKEND_REQUIRED} is true add an authentication header to the
     * given request. The currently signed in user is used to retrieve the auth token.
     *
     * @param context         Context used to retrieve auth token from SharedPreferences.
     * @param basicHttpClient HTTP client to which the authorization header will be added.
     */
    public static void authorizeHttpClient(Context context, BasicHttpClient basicHttpClient) {
      /*  if (basicHttpClient == null || AccountUtils.getAuthToken(context) == null) {
            return;
        }
        if (AUTHORIZATION_TO_BACKEND_REQUIRED) {
            basicHttpClient.addHeader(AUTHORIZATION_HEADER,
                    BEARER_PREFIX + AccountUtils.getAuthToken(context));
        }*/
    }

    public static String getHostAddress(Context mContext) {
        //TODO : for only testing address
        String addr = "http://192.168.1.132";
        return addr;
    }



    public static String universalDetector(final byte[] args) {

        String result = null;
        UniversalDetector detector = new UniversalDetector(null);

        detector.handleData(args, 0, args.length);
        detector.dataEnd();

        String encoding = detector.getDetectedCharset();

        detector.reset();

        if (encoding != null) {
            System.out.println("Detected encoding = " + encoding);
            try {
                result = new String(args, encoding);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No encoding detected.");
            result = new String(args);
        }

        return result;
    }

}
