/*
 * Copyright (c) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.dignsys.dsdsp.dsdsp_9100.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

// TODO (b/36980611): move sync util code from SettingsUtils here.

public class SyncUtils {
    public static final String SERVER_TIME_OFFSET_PATH = ".info/serverTimeOffset";

    private static final String PREF_SERVER_TIME_OFFSET = "pref_server_time_offset";

    private static final String PREF_SERVER_TIME_OFFSET_SET_AT =
            "pref_server_time_offset_set_at";

    /**
     * Long indicating when a sync was last ATTEMPTED (not necessarily succeeded).
     */
    private static final String PREF_LAST_SYNC_ATTEMPTED = "pref_last_sync_attempted";
    /**
     * Long indicating when a sync last SUCCEEDED.
     */
    private static final String PREF_LAST_SYNC_SUCCEEDED = "pref_last_sync_succeeded";

    private static final long NEVER_SET = -1L;

    /**
     * Minimum interval for calculating the server time offset.
     */
    public static final long SERVER_TIME_OFFSET_INTERVAL = 3 * 60 * 60 * 1000L; // 3 hours.

    /**
     * Returns the server time offset reported by Firebase RTDB.
     *
     * @param context Context to be used to lookup the {@link SharedPreferences}.
     */
    public static int getServerTimeOffset(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(PREF_SERVER_TIME_OFFSET, 0);
    }

    /**
     * Saves the server time offset reported by Firebase RTDB.
     *
     * @param context The {@link Context}.
     * @param offset  The server time offset.
     */
    public static void setServerTimeOffset(final Context context, int offset) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit()
          .putInt(PREF_SERVER_TIME_OFFSET, offset)
          .putLong(PREF_SERVER_TIME_OFFSET_SET_AT, System.currentTimeMillis())
          .apply();
    }

    /**
     * Returns the last time server time offset was obtained from Firebase RTDB.
     *
     * @param context The {@link Context}.
     */
    public static long getServerTimeOffsetSetAt(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getLong(PREF_SERVER_TIME_OFFSET_SET_AT, NEVER_SET);
    }

    /**
     * Returns true if the server time offset was never set, otherwise false.
     *
     * @param context The {@link Context}.
     */
    public static boolean serverTimeOffsetNeverSet(final Context context) {
        return getServerTimeOffsetSetAt(context) == NEVER_SET;
    }

    /**
     * Mark that a sync succeeded (stores current time as 'last sync succeeded' preference).
     *
     * @param context Context to be used to edit the {@link android.content.SharedPreferences}.
     */
    public static void markSyncSucceededNow(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putLong(PREF_LAST_SYNC_SUCCEEDED, TimeUtils.getCurrentTime(context)).apply();
    }

    /**
     * Mark a sync was attempted (stores current time as 'last sync attempted' preference).
     *
     * @param context Context to be used to edit the {@link android.content.SharedPreferences}.
     */
    public static void markSyncAttemptedNow(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putLong(PREF_LAST_SYNC_ATTEMPTED, TimeUtils.getCurrentTime(context)).apply();
    }
}
