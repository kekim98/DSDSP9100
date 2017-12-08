
package com.dignsys.dsdsp.dsdsp_9100.service.handler;

import android.content.Context;

import com.dignsys.dsdsp.dsdsp_9100.Definer;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ContentEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.Format;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.SceneEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ScheduleEntity;
import com.dignsys.dsdsp.dsdsp_9100.model.PlayContent;
import com.dignsys.dsdsp.dsdsp_9100.util.DaulUtils;
import com.dignsys.dsdsp.dsdsp_9100.util.IOUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class PlayListHandler extends BasicHandler {
    private static final String TAG = PlayListHandler.class.getSimpleName();

    private ArrayList<ScheduleEntity> mSchedule = new ArrayList<>();
    private ArrayList<SceneEntity> mScene = new ArrayList<>();
    private ArrayList<ContentEntity> mContent = new ArrayList<>();


    public PlayListHandler(Context context) {
        super(context);
    }

    public ArrayList<ScheduleEntity> getScheduleList() {
        return mSchedule;
    }

    public ArrayList<SceneEntity> getSceneList() {
        return mScene;
    }

    public ArrayList<ContentEntity> getContentList() {
        return mContent;
    }

    @Override
    public void process(String body) {
        final List<String> mVideoExtension = Arrays.asList("avi", "mp4", "wmv", "stream");
        final List<String> mImageExtension = Arrays.asList("jpg", "png", "bmp");
        final List<String> mTextExtension = Arrays.asList("txt");
        final List<String> mMusicExtension = Arrays.asList("mp3");

        String strLine = "";
        // convert String into InputStream
        InputStream is = new ByteArrayInputStream(body.getBytes());

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        try {

            while ((strLine = br.readLine()) != null) {

                if (strLine.length() == 0) continue;

                if (strLine.startsWith("<SCENE")) {

                    boolean isNFormat = false;

                    strLine = strLine.replace("><", "|");

                    String[] saSceneInfor = new String(strLine).split("\\|");

                    String strSceneNumber = "";
                  //  String strFormatNumber = "";
                    String strPlayTime = "";
                    boolean bBGMMode = false;

                    if (saSceneInfor.length > 1) {

                        strSceneNumber = saSceneInfor[0].substring(6, saSceneInfor[0].length());

                        for (int nCnt = 1; nCnt < saSceneInfor.length; nCnt++) {
                            if (saSceneInfor[nCnt].startsWith("BGM")) bBGMMode = true;
                            if (saSceneInfor[nCnt].startsWith("PT")) {
                                strPlayTime = saSceneInfor[nCnt].substring(saSceneInfor[nCnt].indexOf(":") + 1, saSceneInfor[nCnt].length() - 1);
                            }
                        }
                    }

                    SceneEntity si = new SceneEntity();

                    si.setId(Integer.valueOf(strSceneNumber));
                    si.setOpBGMMode(bBGMMode);
                    si.setOpPlayTime(strPlayTime);
                    mScene.add(si);

                    while ((strLine = br.readLine()).length() != 0) {

                        String[] saContentsInfor = new String(strLine).split("\\|");

                        if (saContentsInfor.length > 1) {

                            int nContentsType = Definer.DEF_CONTENTS_TYPE_UNKNOWN;
                            String strContentsPath = saContentsInfor[0];


                            if (mVideoExtension.contains(DaulUtils.getExtension(strContentsPath)))
                                nContentsType = Definer.DEF_CONTENTS_TYPE_VIDEO;
                            else if (mImageExtension.contains(DaulUtils.getExtension(strContentsPath)))
                                nContentsType = Definer.DEF_CONTENTS_TYPE_IMAGE;
                            else if (mTextExtension.contains(DaulUtils.getExtension(strContentsPath)))
                                nContentsType = Definer.DEF_CONTENTS_TYPE_TEXT;
                            else if (mMusicExtension.contains(DaulUtils.getExtension(strContentsPath)))
                                nContentsType = Definer.DEF_CONTENTS_TYPE_BGM;


                            ContentEntity contentEntity = new ContentEntity();

                            contentEntity.setScene_id(Integer.valueOf(strSceneNumber));
                            contentEntity.setFileType(nContentsType); contentEntity.setFilePath(strContentsPath);

                            for (int nCnt = 1; nCnt < saContentsInfor.length; nCnt++) {
                                String strOp = saContentsInfor[nCnt].substring(0, saContentsInfor[nCnt].indexOf(":"));
                                String strValue = saContentsInfor[nCnt].substring(saContentsInfor[nCnt].indexOf(":") + 1);

                                if (strOp.equals("001") && strValue.length() > 0)
                                contentEntity.setOpStartDT(strValue);
                                if (strOp.equals("002") && strValue.length() > 0) contentEntity.setOpEndDT(strValue);
                                if (strOp.equals("003") && strValue.length() > 0)
                                    contentEntity.setOpRunTime(Integer.valueOf(strValue));
                                if (strOp.equals("004") && strValue.length() > 0) {
                                    contentEntity.setOpBGMFile(strValue);
                                }
                                if (strOp.equals("005") && strValue.length() > 0)
                                    contentEntity.setOpNotPlay(Integer.valueOf(strValue));
                                if (strOp.equals("006") && strValue.length() > 0)
                                    contentEntity.setOpAutoResize(Integer.valueOf(strValue));
                                if (strOp.equals("007") && strValue.length() > 0)
                                    contentEntity.setOpEffect(Integer.valueOf(strValue) - 1);
                                if (strOp.equals("008") && strValue.length() > 0) {
                                    contentEntity.setPane_id(Integer.valueOf(strValue));
                                }
                                if (strOp.equals("009") && strValue.length() > 0) {
                                    contentEntity.setOpBGMFile(strValue);
                                }
                                if (strOp.equals("010") && strValue.length() > 0) {
                                    String[] saPosition = new String(strValue).split(",");
                                    if (saPosition.length == 4) {
                                        contentEntity.setOpStartX(Integer.valueOf(saPosition[0]));  contentEntity.setOpStartY(Integer.valueOf(saPosition[1]));
                                        contentEntity.setOpWidth(Integer.valueOf(saPosition[3]));contentEntity.setOpHeight(Integer.valueOf(saPosition[4]));
                                    }
                                }
                                if (strOp.equals("011") && strValue.length() > 0)
                                    contentEntity.setOpMSGType(Integer.valueOf(strValue));
                                if (strOp.equals("012") && strValue.length() > 0)
                                    contentEntity.setOpMSGPlayTime(Integer.valueOf(strValue));
                                if (strOp.equals("030") && strValue.length() > 0)
                                    contentEntity.setOpNotDownload(Integer.valueOf(strValue));
                            }

                            mContent.add(contentEntity);
                        }

                    } // while ((strLine = br.readLine()).length() != 0)
                } // if(strLine.startsWith("<SCENE"))
                else if (strLine.startsWith("<SPY")) {

                    ScheduleEntity scheduleEntity = new ScheduleEntity();
                    scheduleEntity.setId(mSchedule.size()+1);

                    strLine = strLine.replace("><", "|");

                    String[] saScheduleInfor = new String(strLine).split("\\|");

                    if (saScheduleInfor.length > 1) {

                        for (int nCnt = 1; nCnt < saScheduleInfor.length; nCnt++) {

                            saScheduleInfor[nCnt] = saScheduleInfor[nCnt].replace(">", "");

                            if (saScheduleInfor[nCnt].startsWith("S:")) {
                                scheduleEntity.setOpS(Integer.valueOf(saScheduleInfor[nCnt].substring(saScheduleInfor[nCnt].indexOf(":") + 1)));
                            }
                            if (saScheduleInfor[nCnt].startsWith("W:")) {
                                scheduleEntity.setOpW(saScheduleInfor[nCnt].substring(saScheduleInfor[nCnt].indexOf(":") + 1));
                            }
                            if (saScheduleInfor[nCnt].startsWith("D:")) {
                                scheduleEntity.setOpDate(saScheduleInfor[nCnt].substring(saScheduleInfor[nCnt].indexOf(":") + 1, saScheduleInfor[nCnt].length()));
                            }
                        }
                    }

                    while ((strLine = br.readLine()) != null && strLine.length() != 0) {

                        if (strLine.startsWith("<")) {
                            scheduleEntity.setOpTime(strLine.substring(1, strLine.indexOf(">")));
                        } else if (strLine.startsWith(":")) {
                            int nSceneNumber = Integer.valueOf(strLine.substring(6, strLine.length()));

                            for (SceneEntity sceneEntity : mScene) {
                                if (sceneEntity.getId() == nSceneNumber) {
                                    sceneEntity.setSchedule_id(scheduleEntity.getId());
                                    break;
                                }
                            }
                        }
                    }
                    mSchedule.add(scheduleEntity);

                } else if (strLine.startsWith("<ANY")) {

                    ScheduleEntity scheduleEntity = new ScheduleEntity();
                    scheduleEntity.setId(mSchedule.size()+1);

                    while ((strLine = br.readLine()) != null && strLine.length() != 0) {
                        if (!strLine.startsWith("<DEF")) {
                            if (strLine.startsWith(":")) {
                                int nSceneNumber = Integer.valueOf(strLine.substring(6, strLine.length()));

                                for (SceneEntity sceneEntity : mScene) {
                                    if (sceneEntity.getId() == nSceneNumber) {
                                        sceneEntity.setSchedule_id(scheduleEntity.getId());
                                        break;
                                    }
                                }
                            }
                        }
                    } // while ((strLine = br.readLine()).length() != 0)

                    mSchedule.add(scheduleEntity);
                } // else if(strLine.startsWith("<ANY"))
            }    // while ((strLine = br.readLine()) != null)

            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void setFormat(ArrayList<Format> formatArrayList) {
        for (SceneEntity scene : mScene) {
            for (Format format : formatArrayList) {
                if (scene.getId() == format.getScene_id()) {
                    scene.setFormat(format);
                }
            }
        }
    }


    public ArrayList<PlayContent> getContentFiles() {
        ArrayList<PlayContent> contents = new ArrayList<>();

        String url;
        String filename;
        final String hostAddr = IOUtils.getHostAddress(mContext);

        for (ContentEntity c : mContent) {
            PlayContent content = new PlayContent();

            if(c.getOpNotDownload() ==1) continue;

            if (!c.getFilePath().isEmpty()) {
                if (c.getFilePath().startsWith("/")) {
                    url = hostAddr + c.getFilePath();
                }else{
                    url =c.getFilePath();
                }

                filename = IOUtils.getFilename(url);

                content.filename = filename;
                content.url = url;
                contents.add(content);
            }

            if (!c.getOpBGMFile().isEmpty()) {
                if (c.getOpBGMFile().startsWith("/")) {
                    url = hostAddr + c.getOpBGMFile();
                }else{
                    url =c.getFilePath();
                }
                filename = IOUtils.getFilename(url);

                content.filename = filename;
                content.url = url;
                contents.add(content);
            }

            if (!c.getOpMSGFile().isEmpty()) {
                if (c.getOpMSGFile().startsWith("/")) {
                    url = hostAddr + c.getOpMSGFile();
                }else{
                    url =c.getFilePath();
                }
                filename = IOUtils.getFilename(url);

                content.filename = filename;
                content.url = url;
                contents.add(content);
            }
        }
        return contents;
    }
}
