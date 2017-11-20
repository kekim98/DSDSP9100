/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dignsys.dsdsp.dsdsp_9100.service;

import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dignsys.dsdsp.dsdsp_9100.util.ConnectivityUtils;
import com.turbomanage.httpclient.BasicHttpClient;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.RequestLogger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;



/**
 * A helper class for dealing with conference data synchronization. All operations occur on the
 * thread they're called from, so it's best to wrap calls in an {@link android.os.AsyncTask}, or
 * better yet, a {@link android.app.Service}.
 */
public class SyncHelper {

    private static final String TAG = SyncHelper.class.getSimpleName();

    private Context mContext;

    private ConferenceDataHandler mConferenceDataHandler;

    private RemoteConferenceDataFetcher mRemoteDataFetcher;

    private BasicHttpClient mHttpClient;

    /**
     *
     * @param context Can be Application, Activity or Service context.
     */
    public SyncHelper(Context context) {
        mContext = context;
        mConferenceDataHandler = new ConferenceDataHandler(mContext);
        mRemoteDataFetcher = new RemoteConferenceDataFetcher(mContext);
        mHttpClient = new BasicHttpClient();
    }

   

    /**
     * Attempts to perform data synchronization. There are 3 types of data: conference, user
     * schedule and user feedback.
     * <p />
     * The conference data sync is handled by {@link RemoteConferenceDataFetcher}. For more details
     * about conference data, refer to the documentation at
     * https://github.com/google/iosched/blob/master/doc/SYNC.md. The user schedule data sync is
     * handled by AbstractUserDataSyncHelper.
     *
     *
     * @param syncResult The sync result object to update with statistics.
     * @param extras Specifies additional information about the sync. This must contain key
     *               {@code SyncAdapter.EXTRA_SYNC_USER_DATA_ONLY} with boolean value
     * @return true if the sync changed the data.
     */
    public boolean performSync(@Nullable SyncResult syncResult, Bundle extras) {

        boolean dataChanged = false;

      //  SettingsUtils.markSyncAttemptedNow(mContext);
        long opStart;
        long syncDuration, choresDuration;

        opStart = System.currentTimeMillis();

        // Sync consists of 1 or more of these operations. We try them one by one and tolerate
        // individual failures on each.
        final int OP_CONFERENCE_DATA_SYNC = 0;
        final int OP_USER_SCHEDULE_DATA_SYNC = 1;
        final int OP_USER_FEEDBACK_DATA_SYNC = 2;

        int[] opsToPerform = 
                new int[]{OP_CONFERENCE_DATA_SYNC, OP_USER_SCHEDULE_DATA_SYNC,
                        OP_USER_FEEDBACK_DATA_SYNC};

        for (int op : opsToPerform) {
            try {
                switch (op) {
                    case OP_CONFERENCE_DATA_SYNC:
                        dataChanged |= doConferenceDataSync();
                        break;
                    case OP_USER_SCHEDULE_DATA_SYNC:
                        dataChanged |= doUserDataSync(syncResult);
                        break;
                    case OP_USER_FEEDBACK_DATA_SYNC:
                        // User feedback data sync is an outgoing sync only so not affecting
                        // {@code dataChanged} value.
                        doUserFeedbackDataSync();
                        break;
                }

            } catch (Throwable throwable) {
                throwable.printStackTrace();
                Log.e(TAG, "Error performing remote sync.");
                increaseIoExceptions(syncResult);
            }
        }
        syncDuration = System.currentTimeMillis() - opStart;

        // If data has changed, there are a few chores we have to do.
        opStart = System.currentTimeMillis();
        if (dataChanged) {
            try {
                performPostSyncChores(mContext);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                Log.e(TAG, "Error performing post sync chores.");
            }
        }
        choresDuration = System.currentTimeMillis() - opStart;

        int operations = mConferenceDataHandler.getContentProviderOperationsDone();
        if (syncResult != null && syncResult.stats != null) {
            syncResult.stats.numEntries += operations;
            syncResult.stats.numUpdates += operations;
        }

        if (dataChanged) {
            long totalDuration = choresDuration + syncDuration;
            Log.d(TAG, "SYNC STATS:\n" +
                    " *  Content provider operations: " + operations + "\n" +
                    " *  Sync took: " + syncDuration + "ms\n" +
                    " *  Post-sync chores took: " + choresDuration + "ms\n" +
                    " *  Total time: " + totalDuration + "ms\n" +
                    " *  Total data read from cache: \n" +
                    (mRemoteDataFetcher.getTotalBytesReadFromCache() / 1024) + "kB\n" +
                    " *  Total data downloaded: \n" +
                    (mRemoteDataFetcher.getTotalBytesDownloaded() / 1024) + "kB");
        }

        Log.i(TAG, "End of sync (" + (dataChanged ? "data changed" : "no data change") + ")");


        return dataChanged;
    }

    public static void performPostSyncChores(final Context context) {
        // Update search index.

    }

    private static void syncCalendar(Context context) {
       /* Intent intent = new Intent(SessionCalendarService.ACTION_UPDATE_ALL_SESSIONS_CALENDAR);
        intent.setClass(context, SessionCalendarService.class);
        context.startService(intent);*/
    }

    private void doUserFeedbackDataSync() {
       /* Log.d(TAG, "Syncing feedback");
        new FeedbackSyncHelper(mContext, new FeedbackApiHelper(mHttpClient,
                BuildConfig.FEEDBACK_API_ENDPOINT)).sync();*/
    }

    /**
     * Checks if the remote server has new conference data that we need to import. If so, download
     * the new data and import it into the database.
     *
     * @return Whether or not data was changed.
     * @throws IOException if there is a problem downloading or importing the data.
     */
    private boolean doConferenceDataSync() throws IOException {
        if (!ConnectivityUtils.isConnected(mContext)) {
            Log.d(TAG, "Not attempting remote sync because device is OFFLINE");
            return false;
        }

        Log.d(TAG, "Starting remote sync.");

        // Fetch the remote data files via RemoteConferenceDataFetcher.
        String[] dataFiles = mRemoteDataFetcher.fetchConferenceDataIfNewer(
                mConferenceDataHandler.getDataTimestamp());

        if (dataFiles != null) {
            Log.i(TAG, "Applying remote data.");
            // Save the remote data to the database.
            mConferenceDataHandler.applyConferenceData(dataFiles,
                    mRemoteDataFetcher.getServerDataTimestamp(), true);
            Log.i(TAG, "Done applying remote data.");

            // Mark that conference data sync has succeeded.
          //  SettingsUtils.markSyncSucceededNow(mContext);
            return true;
        } else {
            // No data to process (everything is up to date).
            // Mark that conference data sync succeeded.
            // SettingsUtils.markSyncSucceededNow(mContext);
            return false;
        }
    }

    /**
     * Checks if there are changes on User's Data to sync with/from remote AppData folder.
     *
     * @return Whether or not data was changed.
     * @throws IOException if there is a problem uploading the data.
     */
    private boolean doUserDataSync(SyncResult syncResult) throws IOException {
        if (!ConnectivityUtils.isConnected(mContext)) {
            Log.d(TAG, "Not attempting userdata sync because device is OFFLINE");
            return false;
        }

        return false;
    }

    private void increaseIoExceptions(SyncResult syncResult) {
        if (syncResult != null && syncResult.stats != null) {
            ++syncResult.stats.numIoExceptions;
        }
    }

    public static class AuthException extends RuntimeException {

    }


    public static class MinimalRequestLogger implements RequestLogger {

        @Override
        public boolean isLoggingEnabled() {
            return true;
        }

        @Override
        public void log(final String s) { }

        @Override
        public void logRequest(final HttpURLConnection urlConnection, final Object o)
                throws IOException {
            try {
                URL url = urlConnection.getURL();
                Log.w(TAG, "HTTPRequest to " + url.getHost());
            } catch (Throwable e) {
                Log.i(TAG, "Exception while logging http request.");
            }

        }

        @Override
        public void logResponse(final HttpResponse httpResponse) {
            try {
                URL url = new URL(httpResponse.getUrl());
                Log.w(TAG, "HTTPResponse from " + url.getHost() + " had return status " + httpResponse.getStatus());
            } catch (Throwable e) {
                Log.i(TAG, "Exception while logging http response.");
            }
        }
    }
}
