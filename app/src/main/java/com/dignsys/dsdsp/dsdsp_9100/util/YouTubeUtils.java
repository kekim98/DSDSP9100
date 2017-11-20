/*
 * Copyright 2015 Google Inc. All rights reserved.
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

package com.dignsys.dsdsp.dsdsp_9100.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Utility class to deal with YouTube urls and videos.
 */
public class YouTubeUtils {
    private static final String TAG = YouTubeUtils.class.getSimpleName();

    /**
     * Builds and returns the youTube video ID for a session. For livestreamed sessions, uses the
     * livestream ID only if a youTube ID isn't yet available.
     *
     * @param youTubeUrl   The ID for the youTube link to the session video.
     * @param liveStreamId The ID for the liveStream link.
     * @return The ID used for the video link for the session.
     */
    public static String getVideoIdFromSessionData(String youTubeUrl, String liveStreamId) {
        if (youTubeUrl != null) {
            Uri youTubeUri = Uri.parse(youTubeUrl);

            String youTubeVideoId = youTubeUri.getQueryParameter("v");
            if (youTubeVideoId != null) {
                return youTubeVideoId;
            }
        }

        if (liveStreamId != null) {
            Uri liveStreamUri = Uri.parse(liveStreamId);
            return liveStreamUri.getQueryParameter("v");
        }

        return null;
    }

    public static void showYouTubeVideo(String videoId, Activity activity) {
        // We aren't embedding a youTube video in 2017. Instead we'll just send
        // users to the youTube app or, failing that, to Chrome.
        if (!TextUtils.isEmpty(videoId)) {
            try {
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" +
                        videoId));
                activity.startActivity(appIntent);
            } catch (ActivityNotFoundException ex) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.youtube.com/watch?v=" + videoId));
                activity.startActivity(webIntent);
            }
        } else {
            Toast.makeText(activity, "explore_io_video_id_not_valid",
                    Toast.LENGTH_LONG).show();
        }
    }
}
