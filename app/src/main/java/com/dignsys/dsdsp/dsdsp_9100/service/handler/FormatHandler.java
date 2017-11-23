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


    private ArrayList<Format> mFormat = new ArrayList<>();
    private ArrayList<PaneEntity> mPane = new ArrayList<>();

    public ArrayList<Format> getFormatList() {
        return mFormat;
    }

    public ArrayList<PaneEntity> getPaneList() {
        return mPane;
    }

    public FormatHandler(Context context) {
        super(context);
    }

    @Override
    public void process(String body) {
       /*
        final List<String> mVideoExtension = Arrays.asList("avi", "mp4", "wmv", "stream");
        final List<String> mImageExtension = Arrays.asList("jpg", "png", "bmp");
        final List<String> mTextExtension = Arrays.asList("txt");
        final List<String> mMusicExtension = Arrays.asList("mp3");*/

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

}
