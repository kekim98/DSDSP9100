package com.dignsys.dsdsp.dsdsp_9100.viewmodel;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dignsys.dsdsp.dsdsp_9100.Definer;
import com.dignsys.dsdsp.dsdsp_9100.db.AppDatabase;
import com.dignsys.dsdsp.dsdsp_9100.db.DatabaseCreator;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ConfigEntity;
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

    private static final MutableLiveData ABSENT = new MutableLiveData();
    private final LiveData<List<ContentEntity>> mContentList;
    private final LiveData<ConfigEntity> mConfig;
    private long mTickCount = 0;
    private static final String TAG = ScheduleHelper.class.getSimpleName();
    private int mPaneListSyncDone = 0;
    private int mContentListSyncDone = 0;


    {
        //noinspection unchecked
        ABSENT.setValue(null);
    }

    private final MutableLiveData<Integer> mPlayStart = new MutableLiveData<>();
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
    //private int _scheduleId; //only to be used internally
    private Context _context;

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    //private int _scenePlayTime;

    private ScheduleHelper(Context context) {
        _context = context;

        AppDatabase db = DatabaseCreator.getInstance(context);
        mConfig = db.configDao().loadConfig();
        mScheduleList = db.scheduleDao().loadAllSchedule();


        // Create the observer which updates the schedule list .
        final Observer<ConfigEntity> configObserver = new Observer<ConfigEntity>() {
            @Override
            public void onChanged(@Nullable final ConfigEntity config) {
                if (config != null) {
                    if (config.getIsDBEnable() == 0) {
                        mScheduleId.setValue(0);
                        mSceneId.setValue(0);
                        mPlayStart.setValue(0);
                        //mScheduleDone.setValue(0);
                        mContentPlayDone.setValue(0);
                    }
                }
            }
        };
        mConfig.observeForever(configObserver);


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
                if (sceneEntities != null) {
                    mSceneSchedule.idx = 0;
                    mSceneSchedule.count = sceneEntities.size();
                    mSceneSchedule.scene_id =1;
                    mSceneId.setValue(1);
                }
            }
        };
        mSceneList.observeForever(sceneListObserver);

        //getNextSceneId() will change mSceneId and MainActivity
        Observer<Integer> sceneIdObserver = new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable final Integer sceneId) {
                if (sceneId > 0 && mSceneList.getValue() != null) {
                    Log.d(TAG, "onChanged: mSceneId= " + mSceneId.getValue());
                    SceneEntity se = mSceneList.getValue().get(sceneId - 1);

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
                            //noinspection unchecked
                            return DatabaseCreator.getInstance(_context).contentDao().loadContentListById(mSceneId);
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

    private boolean requestNextScene() {

        if (mSceneList.getValue() == null) return false;

        final int nextScene = (mSceneSchedule.idx % mSceneSchedule.count) + 1;

        if (nextScene == mSceneSchedule.scene_id) {
            Log.d(TAG, "getContent: requestNextScene = false");

            return false;
        }
        mSceneSchedule.scene_id = nextScene;
        mSceneSchedule.idx++;

        //  mSceneId.setValue(nextScene);
        Log.d(TAG, "requestNextScene=true: bawoori");
        mSceneId.setValue(nextScene);
        return true;
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

    private void makeContentSchedule() {

        mContentSchedule.clear();
        // Log.d(TAG, "makeContentSchedule: runtimeTick=0");
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
                    cs.count++; //to find total content count
                    cs.contents.add(ce);
                }
            }
        }
        findMainPane();

        mPaneListSyncDone = mContentListSyncDone = 0;

        Log.d(TAG, "makeContentSchedule: bawoori- make done");
        mContentPlayDone.setValue(0);
        mPlayStart.setValue(1);
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

/*

    public MutableLiveData<Integer> getScheduleDone() {
        return mScheduleDone;
    }
*/

    public MutableLiveData<Integer> getContentPlayDone() {
        return mContentPlayDone;
    }

    public List<PaneEntity> getPaneList() {
        return mPaneList.getValue();
    }

    public LiveData<SceneEntity> getScene() {
        return mScene;
    }

    public LiveData<List<ContentEntity>> getContentList() {
        return mContentList;
    }

    public LiveData<ConfigEntity> getConfig() {
        return mConfig;
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

                    ContentEntity ce = cs.contents.get(cs.idx % cs.count);

                    cs.opRunTimeTick = 0;

                    if (ce.getOpRunTime() != 0) {
                        cs.opRunTimeTick += mTickCount;
                    } else {
                        if (ce.getFileType() == Definer.DEF_CONTENTS_TYPE_IMAGE) {
                             cs.opRunTimeTick = mConfig.getValue().getImageChangeInterval() + mTickCount;
                            Log.d(TAG, "getContent bawoori: content=" + ce.getFilePath());
                        }
                    }
                    if (ce.getOpMSGPlayTime() != 0) {
                        cs.opMSGPlayTick += mTickCount;
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
               requestNextScene();
            }
        }
    }

    private void contentScheduler() {

        if (mContentSchedule == null || mContentSchedule.size() <= 0) return;

        for (ContentSchedule cs : mContentSchedule) {
            //  Log.d(TAG, "bawoori: runtimeTick=" + String.valueOf(cs.opRunTimeTick ));

            if (cs.opRunTimeTick > 0 && cs.opRunTimeTick <= mTickCount) {
                Log.d(TAG, "contentScheduler bawoori: runtimeTick=" + String.valueOf(cs.opRunTimeTick) +"  pane_num=" + cs.pane_num);
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


    public MutableLiveData<Integer> getPlayStart() {
        return mPlayStart;
    }
}
