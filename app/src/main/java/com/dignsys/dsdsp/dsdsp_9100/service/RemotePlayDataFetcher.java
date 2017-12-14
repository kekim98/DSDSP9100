package com.dignsys.dsdsp.dsdsp_9100.service;

import android.content.Context;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.dignsys.dsdsp.dsdsp_9100.Definer;
import com.dignsys.dsdsp.dsdsp_9100.util.HashUtils;
import com.dignsys.dsdsp.dsdsp_9100.util.IOUtils;
import com.dignsys.dsdsp.dsdsp_9100.util.TimeUtils;
import com.turbomanage.httpclient.BasicHttpClient;
import com.turbomanage.httpclient.ConsoleRequestLogger;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.RequestLogger;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashSet;
import java.util.List;

import static com.dignsys.dsdsp.dsdsp_9100.service.PlayDataHandler.DATA_KEYS_IN_ORDER;


/**
 * Helper class that fetches conference data from the remote server.
 */
public class RemotePlayDataFetcher {
    private static final String TAG = RemotePlayDataFetcher.class.getSimpleName();

    // The directory under which we cache our downloaded files
    private static String CACHE_DIR = "data_cache";

    private Context mContext = null;

    // the set of cache files we have used -- we use this for cache cleanup.
    private HashSet<String> mCacheFilesToKeep = new HashSet<>();

    // total # of bytes downloaded (approximate)
    private long mBytesDownloaded = 0;

    // total # of bytes read from cache hits (approximate)
    private long mBytesReadFromCache = 0;

    // Shared settings_prefs key under which we store the timestamp that corresponds to
    // the data we currently have in our content provider.
    private static final String SP_KEY_DATA_TIMESTAMP = "data_timestamp";

    // symbolic timestamp to use when we are missing timestamp data (which means our data is
    // really old or nonexistent)
    private static final String DEFAULT_TIMESTAMP = "Sat, 1 Jan 2000 00:00:00 GMT";


    private static final String[] URL_KEYS_IN_ORDER = {
            Definer.TEST_PLAYLIST_URL,
            Definer.TEST_FORMAT_URL,
            Definer.TEST_RSS_URL,
            Definer.TEST_CONFIG_URL,
            Definer.TEST_COMMAND_URL
    };


    public String[] mManifestUrl = new String[DATA_KEYS_IN_ORDER.length];
    public String[] mManifestLTimeStamp = new String[DATA_KEYS_IN_ORDER.length]; //local timestamp
    public String[] mManifestSTimeStamp = new String[DATA_KEYS_IN_ORDER.length]; // server timestamp


    public RemotePlayDataFetcher(Context context) {
        mContext = context;

        for (int i = 0; i < DATA_KEYS_IN_ORDER.length; i++) {
            mManifestUrl[i] = URL_KEYS_IN_ORDER[i];
            mManifestLTimeStamp[i] = getPlayTimestamp(context, i);
        }

    }

    /**
     * Fetches data from the remote server.
     *
     * @param idx model index for in this function.
     * @return The data downloaded, or null if there is no data to download
     * @throws IOException if an error occurred during download.
     */
    public String fetchPlayDataIfNewer(int idx) throws IOException {
        if (TextUtils.isEmpty(mManifestUrl[idx])) {
            Log.w(TAG, "Manifest URL is empty (remote sync disabled!).");
            return null;
        }

        BasicHttpClient httpClient = new BasicHttpClient();
        httpClient.setRequestLogger(mQuietLogger);

        // IOUtils.authorizeHttpClient(mContext, httpClient);

        // Only download if data is newer than localTimestamp
        // WE Server is very picky with the If-Modified-Since format. If it's in a wrong
        // format, it refuses to serve the file, returning 304 HTTP error. So, if the
        // localTimestamp is in a wrong format, we simply ignore it. But pay attention to this
        // warning in the log, because it might mean unnecessary data is being downloaded.
        if (!TextUtils.isEmpty(mManifestLTimeStamp[idx])) {
            if (TimeUtils.isValidFormatForIfModifiedSinceHeader(mManifestLTimeStamp[idx])) {
                httpClient.addHeader("If-Modified-Since", mManifestLTimeStamp[idx]);
            } else {
                Log.w(TAG, "Could not set If-Modified-Since HTTP header. Potentially downloading " +
                        "unnecessary data. Invalid format of localTimestamp argument: " +
                        mManifestLTimeStamp[idx]);
            }
        }

        HttpResponse response = httpClient.get(mManifestUrl[idx], null);
        if (response == null) {
            Log.e(TAG, "Request for manifest returned null response.");
            throw new IOException("Request for data manifest returned null response.");
        }

        int status = response.getStatus();
        if (status == HttpURLConnection.HTTP_OK) {
            Log.d(TAG, "Server returned HTTP_OK, so new data is available.");
            mManifestSTimeStamp[idx] = getLastModified(response);
            Log.d(TAG, "Server timestamp for new data is: " + mManifestSTimeStamp[idx]);
            //String body = response.getBodyAsString();
            byte[] body = response.getBody();
            String encodeBody = IOUtils.universalDetector(body);
            //   String encodeBody = new String(body, "EUC-KR");
            if (TextUtils.isEmpty(encodeBody)) {
                Log.e(TAG, "Request for manifest returned empty data.");
        //        throw new IOException("Error fetching conference data manifest: no data.");
            }
            Log.d(TAG, "Manifest " + mManifestUrl[idx] + " read, contents: " + body);
            mBytesDownloaded += encodeBody.getBytes().length;
            return encodeBody;
        } else if (status == HttpURLConnection.HTTP_NOT_MODIFIED) {
            // data on the server is not newer than our data
            Log.d(TAG, "HTTP_NOT_MODIFIED: data has not changed since " + mManifestLTimeStamp[idx]);
            return null;
        } else {
            Log.e(TAG, "Error fetching play data: HTTP status " + status + " and manifest " +
                    mManifestUrl[idx]);
            return null;
     //       throw new IOException("Error fetching conference data: HTTP status " + status);
        }
    }

    // Returns the timestamp of the data downloaded from the server
    public String getServerDataTimestamp(int idx) {
        return mManifestSTimeStamp[idx];
    }


    /**
     * Fetches a file from the cache/network, from an absolute or relative URL. If the file is
     * available in our cache, we read it from there; if not, we will download it from the network
     * and cache it.
     *
     * @param url The URL to fetch the file from. The URL may be absolute or relative; if relative,
     *            it will be considered to be relative to the manifest URL.
     * @return The contents of the file.
     * @throws IOException If an error occurs.
     */
    private String fetchFile(String url) throws IOException {
        // If this is a relative url, consider it relative to the manifest URL
        if (!url.contains("://")) {
            if (TextUtils.isEmpty(url) || !url.contains("/")) {
                Log.e(TAG, "Could not build relative URL based on manifest URL.");
                return null;
            }
            int i = url.lastIndexOf('/');
            url = url.substring(0, i) + "/" + url;
        }

        Log.d(TAG, "Attempting to fetch: " + sanitizeUrl(url));

        // Check if we have it in our cache first
        String body;
        try {
            body = loadFromCache(url);
            if (!TextUtils.isEmpty(body)) {
                // cache hit
                mBytesReadFromCache += body.getBytes().length;
                mCacheFilesToKeep.add(getCacheKey(url));
                return body;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            Log.e(TAG, "IOException getting file from cache.");
            // proceed anyway to attempt to download it from the network
        }

        BasicHttpClient client = new BasicHttpClient();
        // IOUtils.authorizeHttpClient(mContext, client);
        client.setRequestLogger(mQuietLogger);

        // We don't have the file on cache, so download it
        Log.d(TAG, "Cache miss. Downloading from network: " + sanitizeUrl(url));
        HttpResponse response = client.get(url, null);

        if (response == null) {
            throw new IOException(
                    "Request for URL " + sanitizeUrl(url) + " returned null response.");
        }

        Log.d(TAG, "HTTP response " + response.getStatus());
        if (response.getStatus() == HttpURLConnection.HTTP_OK) {
            body = response.getBodyAsString();
            if (TextUtils.isEmpty(body)) {
                throw new IOException("Got empty response when attempting to fetch " +
                        sanitizeUrl(url) + url);
            }
            Log.d(TAG, "Successfully downloaded from network: " + sanitizeUrl(url));
            mBytesDownloaded += body.getBytes().length;
            writeToCache(url, body);
            mCacheFilesToKeep.add(getCacheKey(url));
            return body;
        } else {
            Log.e(TAG, "Failed to fetch from network: " + sanitizeUrl(url));
            throw new IOException("Request for URL " + sanitizeUrl(url) +
                    " failed with HTTP error " + response.getStatus());
        }
    }

    /**
     * Returns the cache file where we store our cache of the response of the given URL.
     *
     * @param url The URL for which to return the cache file.
     * @return The cache file.
     */
    private File getCacheFile(String url) {
        String cacheKey = getCacheKey(url);
        return new File(mContext.getCacheDir() + File.separator + CACHE_DIR + File.separator +
                cacheKey);
    }

    // Creates the cache directory, if it doesn't exist yet
    private void createCacheDir() throws IOException {
        File dir = new File(mContext.getCacheDir() + File.separator + CACHE_DIR);
        if (!dir.exists() && !dir.mkdir()) {
            throw new IOException("Failed to mkdir: " + dir);
        }
    }


    /**
     * Loads our cached content corresponding to the given URL.
     *
     * @param url The URL for which to load the cached response.
     * @return The cached response corresponding to the URL; or null if the given URL does not exist
     * in our cache.
     * @throws IOException If there is an error reading the cache.
     */
    private String loadFromCache(String url) throws IOException {
        String cacheKey = getCacheKey(url);
        File cacheFile = getCacheFile(url);
        if (cacheFile.exists()) {
            Log.d(TAG, "Cache hit " + cacheKey + " for " + sanitizeUrl(url));
            return IOUtils.readFileAsString(cacheFile);
        } else {
            Log.d(TAG, "Cache miss " + cacheKey + " for " + sanitizeUrl(url));
            return null;
        }
    }

    /**
     * Writes a file to the cache.
     *
     * @param url  The URL from which the contents were retrieved.
     * @param body The contents retrieved from the given URL.
     * @throws IOException If there is a problem writing the file.
     */
    private void writeToCache(String url, String body) throws IOException {
        String cacheKey = getCacheKey(url);
        File cacheFile = getCacheFile(url);
        createCacheDir();
        IOUtils.writeToFile(body, cacheFile);
        Log.d(TAG, "Wrote to cache " + cacheKey + " --> " + sanitizeUrl(url));
    }

    /**
     * Returns the cache key to be used to store the given URL. The cache key is the file name under
     * which the contents of the URL are stored.
     *
     * @param url The URL.
     * @return The cache key (guaranteed to be a valid filename)
     */
    private String getCacheKey(String url) {
        return HashUtils.computeWeakHash(url.trim()) + String.format("%04x", url.length());
    }

    // Sanitize a URL for logging purposes (only the last component is left visible).
    private String sanitizeUrl(String url) {
        int i = url.lastIndexOf('/');
        if (i >= 0 && i < url.length()) {
            return url.substring(0, i).replaceAll("[A-za-z]", "*") +
                    url.substring(i);
        } else {
            return url.replaceAll("[A-za-z]", "*");
        }
    }

    private static final String MANIFEST_FORMAT = "iosched-json-v1";

    /**
     * Process the data manifest and download data files referenced from it.
     *
     * @return The contents of the set of files referenced from the manifest, or null if none could
     * be retrieved.
     * @throws IOException If an error occurs while retrieving information.
     */
    public String[] processManifest() throws IOException {

        Log.d(TAG, "Manifest lists " + mManifestUrl.length + " data files.");
        String[] bodys = new String[mManifestUrl.length];
        for (int i = 0; i < mManifestUrl.length; i++) {
            String url = mManifestUrl[i];
            Log.d(TAG, "Processing data file: " + url);
            //  bodys[i] = fetchFile(url);
            bodys[i] = fetchPlayDataIfNewer(i);
            if (TextUtils.isEmpty(bodys[i])) {
                Log.w(TAG, "Failed to fetch data file: " + sanitizeUrl(url));
             //   throw new IOException("Failed to fetch data file " + sanitizeUrl(url));
            }
        }

        Log.d(TAG, "Got " + bodys.length + " data files.");
        //cleanUpCache();
        //  IOUtils.convertToUTF8(bodys);
        return bodys;

    }

    public boolean downloadContents() throws IOException {


        return true;
    }

    public boolean moveContents() throws IOException {

        return true;
    }

    // Delete unnecessary files from our cache
    private void cleanUpCache() {
        Log.d(TAG, "Starting cache cleanup, " + mCacheFilesToKeep.size() + " URLs to keep.");
        File dir = new File(mContext.getCacheDir() + File.separator + CACHE_DIR);
        if (!dir.exists()) {
            Log.d(TAG, "Cleanup complete (there is no cache).");
            return;
        }

        int deleted = 0, kept = 0;
        for (File file : dir.listFiles()) {
            if (mCacheFilesToKeep.contains(file.getName())) {
                Log.d(TAG, "Cache cleanup: KEEEPING " + file.getName());
                ++kept;
            } else {
                Log.d(TAG, "Cache cleanup: DELETING " + file.getName());
                file.delete();
                ++deleted;
            }
        }

        Log.d(TAG, "End of cache cleanup. " + kept + " files kept, " + deleted + " deleted.");
    }

    public long getTotalBytesDownloaded() {
        return mBytesDownloaded;
    }

    public long getTotalBytesReadFromCache() {
        return mBytesReadFromCache;
    }

    private String getLastModified(HttpResponse resp) {
        if (!resp.getHeaders().containsKey("Last-Modified")) {
            return "";
        }

        List<String> s = resp.getHeaders().get("Last-Modified");
        return s.isEmpty() ? "" : s.get(0);
    }

    /**
     * A type of ConsoleRequestLogger that does not log requests and responses.
     */
    private RequestLogger mQuietLogger = new ConsoleRequestLogger() {
        @Override
        public void logRequest(HttpURLConnection uc, Object content) throws IOException {
        }

        @Override
        public void logResponse(HttpResponse res) {
        }
    };


    public void updatePlayDataTimestamp(int idx) {
        String ts;

        ts = getServerDataTimestamp(idx);
        setDataTimestamp(mContext, ts, idx);

    }

    // Returns the timestamp of the data we have in the content provider.
    public static String getPlayTimestamp(Context ctx, int idx) {


        String key = DATA_KEYS_IN_ORDER[idx];

        return PreferenceManager.getDefaultSharedPreferences(ctx).getString(
                SP_KEY_DATA_TIMESTAMP + "_" + key, DEFAULT_TIMESTAMP);
    }


    // Sets the timestamp of the data we have in the content provider.
    public static void setDataTimestamp(Context ctx, String timestamp, int idx) {

        String key = DATA_KEYS_IN_ORDER[idx];

        Log.d(TAG, "Setting data timestamp to: " + timestamp);
        PreferenceManager.getDefaultSharedPreferences(ctx).edit().putString(
                SP_KEY_DATA_TIMESTAMP + "_" + key, timestamp).apply();
    }

    // Reset the timestamp of the data we have in the content provider
    public static void resetDataTimestamp(final Context context, int idx) {
        Log.d(TAG, "Resetting data timestamp to default (to invalidate our synced data)");

        String key = DATA_KEYS_IN_ORDER[idx];

        PreferenceManager.getDefaultSharedPreferences(context).edit().remove(
                SP_KEY_DATA_TIMESTAMP + "_" + key).apply();
    }
}
