package com.dignsys.dsdsp.dsdsp_9100.viewmodel;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.content.Context;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dignsys.dsdsp.dsdsp_9100.Definer;
import com.dignsys.dsdsp.dsdsp_9100.db.AppDatabase;
import com.dignsys.dsdsp.dsdsp_9100.db.DatabaseCreator;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ContentEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.PaneEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.SceneEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ScheduleEntity;
import com.dignsys.dsdsp.dsdsp_9100.model.ContentSchedule;
import com.dignsys.dsdsp.dsdsp_9100.model.SceneSchedule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by bawoori on 17. 11. 24.
 */

public class ScheduleHelper {

    private static ScheduleHelper sInstance;

    // private static final MutableLiveData ABSENT = new MutableLiveData();
    private final LiveData<List<ContentEntity>> mContentList;
  //  private final LiveData<ConfigEntity> mConfig;
    private final ConfigHelper mConfig;
    private long mTickCount = 0;
    private static final String TAG = ScheduleHelper.class.getSimpleName();
    private int mPaneListSyncDone = 0;
    private int mContentListSyncDone = 0;
    private final int[] mBgmType = {Definer.DEF_CONTENTS_TYPE_IMAGE, Definer.DEF_CONTENTS_TYPE_TEXT,
            Definer.DEF_CONTENTS_TYPE_RSS, Definer.DEF_CONTENTS_TYPE_BGM};
    private final int[] mNormalType = {Definer.DEF_CONTENTS_TYPE_IMAGE, Definer.DEF_CONTENTS_TYPE_TEXT,
            Definer.DEF_CONTENTS_TYPE_RSS, Definer.DEF_CONTENTS_TYPE_BGM, Definer.DEF_CONTENTS_TYPE_VIDEO};

/*

    {
        //noinspection unchecked
        ABSENT.setValue(null);
    }
*/

    //private final MutableLiveData<Integer> mPlayCommand = new MutableLiveData<>();
    private final MutableLiveData<Integer> mPlayCommand;
    private final MutableLiveData<Integer> mScheduleId = new MutableLiveData<>();
    private final MutableLiveData<Integer> mSceneId = new MutableLiveData<>();
    // private final MutableLiveData<Integer> mScheduleDone = new MutableLiveData<>();
    private final MutableLiveData<Integer> mContentPlayDone = new MutableLiveData<>();
    //TODO : doing scene object control .....
    private final MutableLiveData<SceneEntity> mScene = new MutableLiveData<>();
    private final LiveData<List<ScheduleEntity>> mScheduleList;
    private final LiveData<List<PaneEntity>> mPaneList;
    private final List<ContentSchedule> mContentSchedule = new ArrayList<>();
    private final SceneSchedule mSceneSchedule = new SceneSchedule();


    private LiveData<List<SceneEntity>> mSceneList;
    private Context _context;

    // For Singleton instantiation
    private static final Object LOCK = new Object();

    private ScheduleHelper(Context context) {
        _context = context;

        AppDatabase db = DatabaseCreator.getInstance(context);
       // mConfig = db.configDao().loadConfig();
        mConfig = ConfigHelper.getInstance(_context);
        mScheduleList = db.scheduleDao().loadAllSchedule();

        mPlayCommand = CommandHelper.getInstance(_context).getPlayCommand();


        // Create the observer which updates the schedule list .
        final Observer<Integer> configObserver = new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable final Integer command) {
                if (command == Definer.DEF_PLAY_STOP_COMMAND
                        || command == Definer.DEF_PLAY_IDLE_COMMAND) {
                        mScheduleId.setValue(0);
                        mSceneId.setValue(0);
                        mContentPlayDone.setValue(0);
                    }
            }
        };
        mPlayCommand.observeForever(configObserver);


        // Create the observer which updates the schedule list .
        final Observer<List<ScheduleEntity>> scheduleObserver = new Observer<List<ScheduleEntity>>() {
            @Override
            public void onChanged(@Nullable final List<ScheduleEntity> schedules) {
                if (schedules.size() > 0) {
                    mScheduleId.setValue(0);
                }
            }
        };
        mScheduleList.observeForever(scheduleObserver);


        mSceneList = Transformations.switchMap(mScheduleId,
                new Function<Integer, LiveData<List<SceneEntity>>>() {

                    @Override
                    public LiveData<List<SceneEntity>> apply(Integer mScheduleId) {
                        if (mScheduleId > 0) {
                            //noinspection ConstantConditions
                            return DatabaseCreator.getInstance(_context).sceneDao().loadSceneByScheduleId(mScheduleId);

                        }
                        return null;
                    }
                });

        Observer<List<SceneEntity>> sceneListObserver = new Observer<List<SceneEntity>>() {
            @Override
            public void onChanged(@Nullable List<SceneEntity> sceneEntities) {
                if (sceneEntities != null && sceneEntities.size() > 0) {
                    mSceneSchedule.idx = 0;
                    mSceneSchedule.count = sceneEntities.size();
                    mSceneSchedule.scene = sceneEntities.get(0);
                    mSceneId.setValue(mSceneSchedule.scene.getId());
                }
            }
        };
        mSceneList.observeForever(sceneListObserver);

        //getNextSceneId() will change mSceneId and MainActivity
        Observer<Integer> sceneIdObserver = new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable final Integer sceneId) {
                if (sceneId > 0 && mSceneList.getValue() != null) {
                    //  Log.d(TAG, "onChanged: mSceneId= " + mSceneId.getValue());

                    SceneEntity se = mSceneSchedule.scene;

                    initBGM(se);

                    if (se.getOpPlayTime().isEmpty()) {
                        mSceneSchedule.expire_time = 0;
                    } else {
                        mSceneSchedule.expire_time = Long.valueOf(se.getOpPlayTime()) + mTickCount;
                    }

                    mScene.setValue(se);
                }
            }
        };
        mSceneId.observeForever(sceneIdObserver);

        //MainActivity will make observer
        mPaneList = Transformations.switchMap(mSceneId,
                new Function<Integer, LiveData<List<PaneEntity>>>() {

                    @Override
                    public LiveData<List<PaneEntity>> apply(Integer mSceneId) {
                        if (mSceneId > 0) {
                            //noinspection unchecked

                            return DatabaseCreator.getInstance(_context).paneDao().loadPaneListById(mSceneId);

                        }
                        //noinspection unchecked
                        return null;
                    }
                });
        Observer<List<PaneEntity>> paneListObserver = new Observer<List<PaneEntity>>() {
            @Override
            public void onChanged(@Nullable List<PaneEntity> paneEntityList) {
                Log.d(TAG, "onChanged: paneListObserver");
                mPaneListSyncDone = 1;
                if (mContentListSyncDone == 1) {
                    makeContentSchedule();
                }
            }
        };
        mPaneList.observeForever(paneListObserver);


        mContentList = Transformations.switchMap(mSceneId,
                new Function<Integer, LiveData<List<ContentEntity>>>() {

                    @Override
                    public LiveData<List<ContentEntity>> apply(Integer mSceneId) {
                        if (mSceneId > 0) {

                            final int[] types = mSceneSchedule.isBGMMode ? mBgmType : mNormalType;
                            //noinspection unchecked
                            return DatabaseCreator.getInstance(_context).contentDao().loadContentListById(mSceneId, types);
                        }
                        //noinspection unchecked
                        return null;
                    }
                });
        Observer<List<ContentEntity>> contentListObserver = new Observer<List<ContentEntity>>() {
            @Override
            public void onChanged(@Nullable List<ContentEntity> contentEntities) {
                Log.d(TAG, "onChanged: contentListObserver");
                mContentListSyncDone = 1;
                if (mPaneListSyncDone == 1) {
                    makeContentSchedule();
                }
            }
        };
        mContentList.observeForever(contentListObserver);

    }

    public ContentEntity getContent(int paneNum) {

        if (mContentList.getValue() == null) return null;

        if (mContentList != null) {

            if (mContentSchedule.size() <= 0) return null;

            for (ContentSchedule cs : mContentSchedule) {

                if (cs.pane_num == paneNum) {
                    if (cs.count == cs.idx && cs.isMain == 1) {
                        cs.idx = 0;
                        if (requestNextScene()) {
                            return null;
                        }
                    }
                    Log.d(TAG, "getContent:bawoori- paneNum=" + String.valueOf(paneNum));

                    if (cs.count == 0) return null;
                    ContentEntity ce = cs.contents.get(cs.idx % cs.count);

                    cs.opRunTimeTick = 0;
                    cs.opMSGPlayTick = 0;

                    if (ce.getOpRunTime() != 0) {
                        cs.opRunTimeTick = ce.getOpRunTime() + mTickCount;
                    } else {
                        if (ce.getFileType() == Definer.DEF_CONTENTS_TYPE_IMAGE) {
                            cs.opRunTimeTick = mConfig.getPicChangeTime() + mTickCount;
                            Log.d(TAG, "getContent bawoori: content=" + ce.getFilePath());
                        }
                    }

                    if (ce.getOpMSGPlayTime() != 0) {
                        cs.opMSGPlayTick = ce.getOpMSGPlayTime() + mTickCount;
                    }

                    if (!ce.getOpBGMFile().isEmpty() && !mSceneSchedule.isBGMMode) {
                        playBGM(cs, ce);
                    }

                    cs.idx++;

                    if (!checkDTime(ce)) {
                        continue;
                    }

                    return ce;
                }
            }
        }

        return null;
    }

    private boolean requestNextScene() {

        if (mSceneList.getValue() == null) return false;

        synchronized (LOCK) {
            if (mSceneSchedule.count == 1) {
                Log.d(TAG, "getContent:bawoori1 requestNextScene = false");

                return false;
            }


            mSceneSchedule.idx++;
            mSceneSchedule.scene = mSceneList.getValue()
                    .get(mSceneSchedule.idx % mSceneSchedule.count);

            final int nextSceneId = mSceneSchedule.scene.getId();

            Log.d(TAG, "requestNextScene: bawoori1- sceneId=" + nextSceneId);

            if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
                mSceneId.setValue(nextSceneId);
            } else {
                mSceneId.postValue(nextSceneId);
            }

        }

        return true;
    }


    private void makeContentSchedule() {

        // Log.d(TAG, "makeContentSchedule: runtimeTick=0");
        initContentSchedule();

        findMainPane();



        if (mSceneSchedule.isBGMMode) {
            playSceneBGM();
        }

        mPaneListSyncDone = mContentListSyncDone = 0;

        Log.d(TAG, "makeContentSchedule: bawoori1- make done");
        mContentPlayDone.setValue(0);
        mPlayCommand.setValue(Definer.DEF_PLAY_START_COMMAND);
    }

    private void findMainPane() {
        final String[] priority = {"I", "D", "V", "P", "T"};

        for (String type : priority) {
            for (ContentSchedule cs : mContentSchedule) {
                if (type.equals(cs.pane_type)) {
                    cs.isMain = 1;
                    return;
                }
            }
        }
    }

    private void initContentSchedule() {
        mContentSchedule.clear();

        for (PaneEntity pe : mPaneList.getValue()) {

            if (pe.getPaneType().equals("S")) continue;

            ContentSchedule cs = new ContentSchedule();

            cs.pane_num = pe.getPane_id();
            cs.pane_type = pe.getPaneType();
            cs.idx = 0;
            cs.opRunTimeTick = 0;
            cs.opMSGPlayTick = 0;
            mContentSchedule.add(cs);
        }

        for (ContentSchedule cs : mContentSchedule) {
            for (ContentEntity ce : mContentList.getValue()) {
                if (cs.pane_num == ce.getPane_id()) {

                    if (ce.getFileType() == Definer.DEF_CONTENTS_TYPE_BGM) {
                        cs.bgm_count++;
                        cs.bgms.add(ce);
                    } else {
                        cs.count++; //to find total content count
                        cs.contents.add(ce);
                    }

                }
            }
        }
    }


    public synchronized static ScheduleHelper getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = new ScheduleHelper(context);
                }
            }
        }
        return sInstance;
    }


    private void initBGM(SceneEntity se) {
        if (mSceneSchedule.BGMPlayer != null) {
            mSceneSchedule.BGMPlayer.releasePlayer();
            mSceneSchedule.BGMPlayer = null;
        }

        if (se.isOpBGMMode()) {
            mSceneSchedule.isBGMMode = true;
        } else {
            mSceneSchedule.isBGMMode = false;
        }
    }

    private void playSceneBGM() {
        for (ContentSchedule cs : mContentSchedule) {
            if (cs.pane_type.equals("V") && cs.bgm_count > 0) {
                // cs.is_bgm_runnig = true;
                mSceneSchedule.BGMPlayer = new PlayerBGM(_context, cs.bgms, true);
                mSceneSchedule.BGMPlayer.playBGM();
                break;
            }
        }
    }

    private void playBGM(ContentSchedule cs, ContentEntity ce) {
        cs.is_bgm_runnig = true;
        List<ContentEntity> bgms = new ArrayList<>();
        bgms.add(ce);

        mSceneSchedule.BGMPlayer = new PlayerBGM(_context, bgms, false);
        mSceneSchedule.BGMPlayer.playBGM();
    }

    private void stopBGM(ContentSchedule cs) {
        mSceneSchedule.BGMPlayer.releasePlayer();
        mSceneSchedule.BGMPlayer = null;
        cs.is_bgm_runnig = false;
    }



    // called per 1 second tick
    public void updateScheduleTick() {
        mTickCount++;

        scheduleScheduler();
        sceneScheduler();
        contentScheduler();

    }

    public void sceneScheduler() {
        if (mScene.getValue() == null) return;

        if (mSceneSchedule.expire_time != 0) {
            if (mTickCount > mSceneSchedule.expire_time) {
                Log.d(TAG, "sceneScheduler: bawoori1......");
                requestNextScene();
            }
        }
    }

    private void contentScheduler() {

        if (mContentSchedule == null || mContentSchedule.size() <= 0) return;

        for (ContentSchedule cs : mContentSchedule) {
            //  Log.d(TAG, "bawoori: runtimeTick=" + String.valueOf(cs.opRunTimeTick ));

            if (cs.opRunTimeTick > 0 && cs.opRunTimeTick <= mTickCount) {
                Log.d(TAG, "contentScheduler bawoori1: runtimeTick=" + String.valueOf(cs.opRunTimeTick) + "  pane_num=" + cs.pane_num);
                if (mSceneSchedule.BGMPlayer != null && cs.is_bgm_runnig) {
                    stopBGM(cs);
                }
                mContentPlayDone.postValue(cs.pane_num);
            }
            if (cs.opMSGPlayTick > 0 && cs.opMSGPlayTick <= mTickCount) {
                mContentPlayDone.postValue(cs.pane_num);
            }
        }

    }


    private void scheduleScheduler() {
        if (mScheduleList.getValue() == null) return;
        if (mScheduleId.getValue() == null) return;


        Calendar cal = Calendar.getInstance();

        int nDoM = cal.get(Calendar.DAY_OF_MONTH);
        int nDoW = cal.get(Calendar.DAY_OF_WEEK) - 2;    // Sunday = 1;
        // Because WE start Monday.
        if (nDoW == -1) nDoW = 6;

        int nDate = Integer.valueOf(String.format("%04d%02d%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, nDoM));
        int nTime = Integer.valueOf(String.format("%02d%02d", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE)));


        for (ScheduleEntity se : mScheduleList.getValue()) {
            if (checkOpS(nDoM, se.getOpS())
                    && checkOpDate(nDate, se.getOpStartDate(), se.getOpEndDate())
                    && checkOpW(nDoW, se.getOpW())
                    && checkOpTime(nTime, se.getOpStartTime(), se.getOpEndTime())) {
                int sid = mScheduleId.getValue();
                if (sid != se.getId()) {
                    sid = se.getId();
                    mScheduleId.postValue(sid);
                }

                break;
            }
        }

    }

    private boolean checkDTime(ContentEntity ce) {

        if (!ce.getOpStartDT().isEmpty()) {

            Calendar calendar = Calendar.getInstance();
            String currTime = new SimpleDateFormat("yyyyMMddHHmm").format(calendar.getTime());
            long lCurrTime = Long.valueOf(currTime);
            long lOpStartDT = Long.valueOf(ce.getOpStartDT());

            if (lCurrTime > lOpStartDT) return false;
        }

        if (!ce.getOpEndDT().isEmpty()) {

            Calendar calendar = Calendar.getInstance();
            String currTime = new SimpleDateFormat("yyyyMMddHHmm").format(calendar.getTime());
            long lCurrTime = Long.valueOf(currTime);
            long lOpEndtDT = Long.valueOf(ce.getOpEndDT());

            if (lCurrTime < lOpEndtDT) return false;
        }

        return true;

    }

    private boolean checkOpS(int n, int opS) {

        boolean nResult = false;

        if (opS == n || opS == 0) nResult = true;

        return nResult;
    }

    private boolean checkOpW(int n, String opW) {

        boolean nResult = false;

        if (opW.isEmpty()) nResult = true;
        else {
            char[] cbOpW = opW.toCharArray();

            if (cbOpW[n] == '1') nResult = true;

        }

        return nResult;
    }


    private boolean checkOpDate(int n, String startData, String endDate) {

        boolean nResult = false;

        if (startData.isEmpty() && endDate.isEmpty()) nResult = true;
        else {
            long lSDate = Long.valueOf(startData);
            long lEDate = Long.valueOf(endDate);

            if (lSDate <= n && n <= lEDate) nResult = true;
        }

        return nResult;
    }

    private boolean checkOpTime(int n, String startTime, String endTime) {

        boolean nResult = false;

        if (startTime.isEmpty() && endTime.isEmpty()) nResult = true;
        else {
            int nSTime = Integer.valueOf(startTime);
            int nETime = Integer.valueOf(endTime);

            if (nSTime < n && n < nETime) nResult = true;
        }

        return nResult;
    }


    //public MutableLiveData<Integer> getPlayCommand() { return mPlayCommand; }

    // public MutableLiveData<Integer> getScheduleDone() { return mScheduleDone; }

    public MutableLiveData<Integer> getContentPlayDone() {
        return mContentPlayDone;
    }

    public List<PaneEntity> getPaneList() {
        return mPaneList.getValue();
    }

    //public LiveData<SceneEntity> getScene() { return mScene; }

    // public LiveData<List<ContentEntity>> getContentList() { return mContentList; }

    // public LiveData<ConfigEntity> getConfig() { return mConfig; }

    public void removeObservers() {

    }
}
