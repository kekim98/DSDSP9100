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

package com.dignsys.dsdsp.dsdsp_9100.service.handler;

import android.content.ContentProviderOperation;
import android.content.Context;

import com.dignsys.dsdsp.dsdsp_9100.Definer;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ContentEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.Format;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.PaneEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.SceneEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ScheduleEntity;
import com.dignsys.dsdsp.dsdsp_9100.util.DaulUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FormatHandler extends BasicHandler {
    private static final String TAG = FormatHandler.class.getSimpleName();

    private final List<String> mVideoExtension = Arrays.asList("avi", "mp4", "wmv", "stream");
    private final List<String> mImageExtension = Arrays.asList("jpg", "png", "bmp");
    private final List<String> mTextExtension = Arrays.asList("txt");
    private final List<String> mMusicExtension = Arrays.asList("mp3");

    private ArrayList<Format> mFormat = new ArrayList<>();
    private ArrayList<PaneEntity> mPane = new ArrayList<>();


    public FormatHandler(Context context) {
        super(context);
    }

    @Override
    public void process(String body) {

        String strLine = "";
        // convert String into InputStream
        InputStream is = new ByteArrayInputStream(body.getBytes());

        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        int sceneId = 0;
        int sceneType = Definer.DEF_PANEL_TYPE_LANDSCAPE;
        int screenWidth = 1920;
        int screenHeight = 1080;

        try {
            while ((strLine = br.readLine()) != null) {

                if (strLine.length() == 0) continue;

                if (strLine.startsWith("<FORMAT")) {

                    Format format = new Format();

                    sceneId = Integer.valueOf(strLine.substring(7, strLine.lastIndexOf(">")));
                    format.setScene_id(sceneId);

                    if ((strLine = br.readLine()) != null && strLine.length() > 0) {
                        if (strLine.startsWith("#")) {

                            String[] saPanelInfor = new String(strLine).split("\\|");

                            if (saPanelInfor.length == 3) {

                                if (saPanelInfor[0].equals("#PT1"))
                                    sceneType = Definer.DEF_PANEL_TYPE_LANDSCAPE;
                                else if (saPanelInfor[0].equals("#PT2"))
                                    sceneType = Definer.DEF_PANEL_TYPE_PORTRAIT;

                                screenWidth = Integer.valueOf(saPanelInfor[1]);
                                screenHeight = Integer.valueOf(saPanelInfor[2]);
                            }
                        }
                    }
                    format.setSceneType(sceneType);
                    format.setSceneWidth(screenWidth);
                    format.setSceneHeight(screenHeight);

                    mFormat.add(format);
                } else {
                    String[] saAreaInfor = new String(strLine).split("\\|");

                    if (saAreaInfor.length == 3) {

                        PaneEntity paneEntity = new PaneEntity();

                        paneEntity.setScene_id(sceneId);
                        paneEntity.setPane_id(Integer.valueOf(saAreaInfor[0]));
                        paneEntity.setPaneType(saAreaInfor[1]);

                        String[] saAreaInforDetail = saAreaInfor[2].split(",");

                        if (saAreaInforDetail.length >= 4) {

                            if (sceneType == Definer.DEF_PANEL_TYPE_PORTRAIT) {

                                int nY = Integer.valueOf(saAreaInforDetail[0]);
                                int nW = Integer.valueOf(saAreaInforDetail[3]);
                                int nH = Integer.valueOf(saAreaInforDetail[2]);
                                int nX = screenHeight - nW - Integer.valueOf(saAreaInforDetail[1]);

                                paneEntity.setPaneX(nX);
                                paneEntity.setPaneY(nY);
                                paneEntity.setPaneWidth(nW);
                                paneEntity.setPaneHeight(nH);

                            } else {
                                paneEntity.setPaneX(Integer.valueOf(saAreaInforDetail[0]));
                                paneEntity.setPaneY(Integer.valueOf(saAreaInforDetail[1]));
                                paneEntity.setPaneWidth(Integer.valueOf(saAreaInforDetail[2]));
                                paneEntity.setPaneHeight(Integer.valueOf(saAreaInforDetail[3]));
                            }

                            if (paneEntity.getPaneType().equals("V") || paneEntity.getPaneType().equals("P")
                                    || paneEntity.getPaneType().equals("I")) {
                                paneEntity.setOpTransparency(Integer.valueOf(saAreaInforDetail[4]));
                            }
                            if (paneEntity.getPaneType().equals("M") || paneEntity.getPaneType().equals("C")) {
                                paneEntity.setOpDirectionMode(Integer.valueOf(saAreaInforDetail[4]));
                                paneEntity.setOpDirection(Integer.valueOf(saAreaInforDetail[5]));
                                paneEntity.setOpFontSize(Integer.valueOf(saAreaInforDetail[6]));
                                paneEntity.setOpMsgSpeed(Integer.valueOf(saAreaInforDetail[7]));

                                paneEntity.setOpFontColor(Integer.valueOf(saAreaInforDetail[8]));
                                paneEntity.setOpBackColor(Integer.valueOf(saAreaInforDetail[9]));

                                if (paneEntity.getPaneType().equals("C")) {
                                    paneEntity.setOpTimeFormat(saAreaInforDetail[10]);
                                    paneEntity.setOpTransparency(Integer.valueOf(saAreaInforDetail[11]));
                                } else {
                                    paneEntity.setOpTransparency(Integer.valueOf(saAreaInforDetail[10]));
                                }
                            }

                            if (paneEntity.getPaneType().equals("D")) {
                                paneEntity.setOpModulationType(Integer.valueOf(saAreaInforDetail[4]));
                                paneEntity.setOpReceiveType(Integer.valueOf(saAreaInforDetail[5]));
                                paneEntity.setOpChannelNum(saAreaInforDetail[6]);
                            }

                        }
                        mPane.add(paneEntity);
                    }
                }

            }
            br.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void makeContentProviderOperations(ArrayList<ContentProviderOperation> list) {

    }

    private void buildDeleteOperation(String sessionId, List<ContentProviderOperation> list) {
       /* Uri sessionUri = ScheduleContractHelper.setUriAsCalledFromSyncAdapter(
                ScheduleContract.Sessions.buildSessionUri(sessionId));
        list.add(ContentProviderOperation.newDelete(sessionUri).build());*/
    }

   /* private HashMap<String, String> loadSessionHashCodes() {
        Uri uri = ScheduleContractHelper.setUriAsCalledFromSyncAdapter(
                ScheduleContract.Sessions.CONTENT_URI);
        Log.d(TAG, "Loading session hashcodes for session import optimization.");
        Cursor cursor = null;
        try {
            cursor = mContext.getContentResolver().query(uri, SessionHashcodeQuery.PROJECTION,
                    null, null, null);
            if (cursor == null || cursor.getCount() < 1) {
                Log.w(TAG, "Warning: failed to load session hashcodes. Not optimizing session import.");
                return null;
            }
            HashMap<String, String> hashcodeMap = new HashMap<>();
            if (cursor.moveToFirst()) {
                do {
                    String sessionId = cursor.getString(SessionHashcodeQuery.SESSION_ID);
                    String hashcode = cursor.getString(SessionHashcodeQuery.SESSION_IMPORT_HASHCODE);
                    hashcodeMap.put(sessionId, hashcode == null ? "" : hashcode);
                } while (cursor.moveToNext());
            }
            Log.d(TAG, "Session hashcodes loaded for " + hashcodeMap.size() + " sessions.");
            return hashcodeMap;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }*/

    StringBuilder mStringBuilder = new StringBuilder();

  /*  private void buildSession(boolean isInsert,
                              Session session, ArrayList<ContentProviderOperation> list) {
        ContentProviderOperation.Builder builder;
        Uri allSessionsUri = ScheduleContractHelper
                .setUriAsCalledFromSyncAdapter(ScheduleContract.Sessions.CONTENT_URI);
        Uri thisSessionUri = ScheduleContractHelper
                .setUriAsCalledFromSyncAdapter(ScheduleContract.Sessions.buildSessionUri(
                        session.id));

        if (isInsert) {
            builder = ContentProviderOperation.newInsert(allSessionsUri);
        } else {
            builder = ContentProviderOperation.newUpdate(thisSessionUri);
        }

        String speakerNames = "";
        if (mSpeakerMap != null) {
            // build human-readable list of speakers
            mStringBuilder.setLength(0);
            if (session.speakers != null) {
                for (int i = 0; i < session.speakers.length; ++i) {
                    if (mSpeakerMap.containsKey(session.speakers[i])) {
                        mStringBuilder
                                .append(i == 0 ? "" :
                                        i == session.speakers.length - 1 ? " and " : ", ")
                                .append(mSpeakerMap.get(session.speakers[i]).name.trim());
                    } else {
                        Log.w(TAG, "Unknown speaker ID " + session.speakers[i] + " in session " +
                                session.id);
                    }
                }
            }
            speakerNames = mStringBuilder.toString();
        } else {
            Log.e(TAG, "Can't build speaker names -- speaker map is null.");
        }

        int color = mDefaultSessionColor;
        try {
            if (!TextUtils.isEmpty(session.color)) {
                color = Color.parseColor(session.color);
            }
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "Ignoring invalid formatted session color: "+session.color);
        }

        builder.withValue(ScheduleContract.SyncColumns.UPDATED, System.currentTimeMillis())
                .withValue(ScheduleContract.Sessions.SESSION_ID, session.id)
                .withValue(ScheduleContract.Sessions.SESSION_LEVEL, null)            // Not available
                .withValue(ScheduleContract.Sessions.SESSION_TITLE, session.title)
                .withValue(ScheduleContract.Sessions.SESSION_ABSTRACT, session.description)
                .withValue(ScheduleContract.Sessions.SESSION_HASHTAG, session.hashtag)
                .withValue(ScheduleContract.Sessions.SESSION_START, TimeUtils.timestampToMillis(session.startTimestamp, 0))
                .withValue(ScheduleContract.Sessions.SESSION_END, TimeUtils.timestampToMillis(session.endTimestamp, 0))
                .withValue(ScheduleContract.Sessions.SESSION_TAGS, session.makeTagsList())
                        // Note: we store this comma-separated list of tags IN ADDITION
                        // to storing the tags in proper relational format (in the sessions_tags
                        // relationship table). This is because when querying for sessions,
                        // we don't want to incur the performance penalty of having to do a
                        // subquery for every record to figure out the list of tags of each session.
                .withValue(ScheduleContract.Sessions.SESSION_SPEAKER_NAMES, speakerNames)
                        // Note: we store the human-readable list of speakers (which is redundant
                        // with the sessions_speakers relationship table) so that we can
                        // display it easily in lists without having to make an additional DB query
                        // (or another join) for each record.
                .withValue(ScheduleContract.Sessions.SESSION_KEYWORDS, null)             // Not available
                .withValue(ScheduleContract.Sessions.SESSION_URL, session.url)
                .withValue(ScheduleContract.Sessions.SESSION_LIVESTREAM_ID,
                        session.isLivestream ? session.youtubeUrl : null)
                .withValue(ScheduleContract.Sessions.SESSION_MODERATOR_URL, null)    // Not available
                .withValue(ScheduleContract.Sessions.SESSION_REQUIREMENTS, null)     // Not available
                .withValue(ScheduleContract.Sessions.SESSION_YOUTUBE_URL,
                        session.isLivestream ? null : session.youtubeUrl)
                .withValue(ScheduleContract.Sessions.SESSION_PDF_URL, null)          // Not available
                .withValue(ScheduleContract.Sessions.SESSION_NOTES_URL, null)        // Not available
                .withValue(ScheduleContract.Sessions.ROOM_ID, session.room)
                .withValue(ScheduleContract.Sessions.SESSION_GROUPING_ORDER, session.groupingOrder)
                .withValue(ScheduleContract.Sessions.SESSION_IMPORT_HASHCODE,
                        session.getImportHashCode())
                .withValue(ScheduleContract.Sessions.SESSION_MAIN_TAG, session.mainTag)
                .withValue(ScheduleContract.Sessions.SESSION_CAPTIONS_URL, session.captionsUrl)
                .withValue(ScheduleContract.Sessions.SESSION_PHOTO_URL, session.photoUrl)
                // Disabled since this isn't being used by this app.
                // .withValue(ScheduleContract.Sessions.SESSION_RELATED_CONTENT, session.relatedContent)
                .withValue(ScheduleContract.Sessions.SESSION_COLOR, color);
        list.add(builder.build());
    }

    // The type order of a session is the order# (in its category) of the tag that indicates
    // its type. So if we sort sessions by type order, they will be neatly grouped by type,
    // with the types appearing in the order given by the tag category that represents the
    // concept of session type.
    private int computeTypeOrder(Session session) {
        int order = Integer.MAX_VALUE;
        int keynoteOrder = -1;
        if (mTagMap == null) {
            throw new IllegalStateException("Attempt to compute type order without tag map.");
        }

        if (session.tags != null) {
            for (String tagId : session.tags) {
                if (Config.Tags.SPECIAL_KEYNOTE.equals(tagId)) {
                    return keynoteOrder;
                }
                Tag tag = mTagMap.get(tagId);
                if (tag != null && Config.Tags.SESSION_GROUPING_TAG_CATEGORY.equals(tag.category)) {
                    if (tag.order_in_category < order) {
                        order = tag.order_in_category;
                    }
                }
            }
        }
        return order;
    }

    private void buildSessionSpeakerMapping(Session session,
                                            ArrayList<ContentProviderOperation> list) {
        final Uri uri = ScheduleContractHelper.setUriAsCalledFromSyncAdapter(
                ScheduleContract.Sessions.buildSpeakersDirUri(session.id));

        // delete any existing relationship between this session and speakers
        list.add(ContentProviderOperation.newDelete(uri).build());

        // add relationship records to indicate the speakers for this session
        if (session.speakers != null) {
            for (String speakerId : session.speakers) {
                list.add(ContentProviderOperation.newInsert(uri)
                        .withValue(ScheduleDatabase.SessionsSpeakers.SESSION_ID, session.id)
                        .withValue(ScheduleDatabase.SessionsSpeakers.SPEAKER_ID, speakerId)
                        .build());
            }
        }
    }

    private void buildTagsMapping(Session session, ArrayList<ContentProviderOperation> list) {
        final Uri uri = ScheduleContractHelper.setUriAsCalledFromSyncAdapter(
                ScheduleContract.Sessions.buildTagsDirUri(session.id));

        // delete any existing mappings
        list.add(ContentProviderOperation.newDelete(uri).build());

        // add a mapping (a session+tag tuple) for each tag in the session
        if (session.tags != null) {
            for (String tag : session.tags) {
                list.add(ContentProviderOperation.newInsert(uri)
                        .withValue(ScheduleDatabase.SessionsTags.SESSION_ID, session.id)
                        .withValue(ScheduleDatabase.SessionsTags.TAG_ID, tag)
                        .build());
            }
        }
    }

    private void buildRelatedSessionsMapping(Session session,
            ArrayList<ContentProviderOperation> list) {
        final Uri uri = ScheduleContractHelper.setUriAsCalledFromSyncAdapter(
                ScheduleContract.Sessions.buildRelatedSessionsDirUri(session.id));

        // delete existing mappings
        list.add(ContentProviderOperation.newDelete(uri).build());

        // add a mapping for each related session id
        if (session.relatedSessionIds != null) {
            for (String id : session.relatedSessionIds) {
                list.add(ContentProviderOperation.newInsert(uri)
                        .withValue(ScheduleContract.Sessions.RELATED_SESSION_ID, id)
                        .build());
            }
        }
    }

    public void setTagMap(HashMap<String, Tag> tagMap) {
        mTagMap = tagMap;
    }
    public void setSpeakerMap(HashMap<String, Speaker> speakerMap) {
        mSpeakerMap = speakerMap;
    }

    private interface SessionHashcodeQuery {
        String[] PROJECTION = {
                BaseColumns._ID,
                ScheduleContract.Sessions.SESSION_ID,
                ScheduleContract.Sessions.SESSION_IMPORT_HASHCODE
        };
        int _ID = 0;
        int SESSION_ID = 1;
        int SESSION_IMPORT_HASHCODE = 2;
    };*/
}
