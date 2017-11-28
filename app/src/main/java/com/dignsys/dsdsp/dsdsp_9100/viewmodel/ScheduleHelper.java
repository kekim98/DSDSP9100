package com.dignsys.dsdsp.dsdsp_9100.viewmodel;

import android.annotation.SuppressLint;
import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.dignsys.dsdsp.dsdsp_9100.db.AppDatabase;
import com.dignsys.dsdsp.dsdsp_9100.db.DatabaseCreator;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ContentEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.PaneEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.SceneEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ScheduleEntity;
import com.dignsys.dsdsp.dsdsp_9100.model.ContentSchedule;

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
    private int mScenePlayTime = 0;
    private long mTickCount = 0;

    {
        //noinspection unchecked
        ABSENT.setValue(null);
    }

    private final MutableLiveData<Integer> mScheduleId = new MutableLiveData<>();
    private final MutableLiveData<Integer> mSceneId = new MutableLiveData<>();
    private final MutableLiveData<Integer> mScheduleDone = new MutableLiveData<>();
    //TODO : doing scene object control .....
    private final MutableLiveData<SceneEntity> mScene = new MutableLiveData<>();
    private final LiveData<List<ScheduleEntity>> mScheduleList;
    private LiveData<List<PaneEntity>> mPaneList;
    private final List<ContentSchedule> mContentSchedule = new ArrayList<ContentSchedule>();


    private LiveData<List<SceneEntity>> mSceneList;
    //private int _scheduleId; //only to be used internally
    private Context _context;

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    //private int _scenePlayTime;

    private ScheduleHelper(Context context) {
        _context = context;



        AppDatabase db = DatabaseCreator.getInstance(context);
        mScheduleList = db.scheduleDao().loadAllSchedule();


        // Create the observer which updates the schedule list .
        final Observer<List<ScheduleEntity>> scheduleObserver = new Observer<List<ScheduleEntity>>() {
            @Override
            public void onChanged(@Nullable final List<ScheduleEntity> schedules) {
                if(schedules.size() > 0){

                    mScheduleId.setValue(1);

                }
            }
        };
        mScheduleList.observeForever(scheduleObserver);

        //getNextScheduleId() will change mScheduleId
        /*Observer<Integer> scheduleIdObserver  = new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable final Integer scheduleId) {
                if(scheduleId > 0){
                  //  mSceneId.setValue(1);
                }
            }
        };
        mScheduleId.observeForever(scheduleIdObserver);*/

        mSceneList = Transformations.switchMap(mScheduleId,
                new Function<Integer, LiveData<List<SceneEntity>>>() {

                    @Override
                    public LiveData<List<SceneEntity>> apply(Integer mScheduleId) {
                        if (mScheduleId < 0) {
                            //noinspection unchecked
                            return null;
                        } else {
                            //noinspection ConstantConditions
                            return DatabaseCreator.getInstance(_context).sceneDao().loadSceneByScheduleId(mScheduleId);
                        }
                    }
                });

        Observer<List<SceneEntity>> sceneListObserver = new Observer<List<SceneEntity>>() {
            @Override
            public void onChanged(@Nullable List<SceneEntity> sceneEntities) {
                if(sceneEntities != null && sceneEntities.size() > 0){
                    mSceneId.setValue(1);
                }
            }
        };
        mSceneList.observeForever(sceneListObserver);

                //getNextSceneId() will change mSceneId and MainActivity
        Observer<Integer> sceneIdObserver = new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable final Integer sceneId) {
                if(sceneId > 0){
                    int sceneIdx = sceneId-1;
                    SceneEntity se = mSceneList.getValue().get(sceneIdx);
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
                        if (mSceneId == 0) {
                            //noinspection unchecked
                            return null;
                        } else {
                            //noinspection ConstantConditions
                            return DatabaseCreator.getInstance(_context).paneDao().loadPaneListById(mSceneId);
                        }
                    }
                });
        Observer<List<PaneEntity>> paneListObserver = new Observer<List<PaneEntity>>() {
            @Override
            public void onChanged(@Nullable List<PaneEntity> paneEntityList) {
                if(paneEntityList != null && paneEntityList.size() > 0){
                   mContentSchedule.clear();
                    for (PaneEntity pe : paneEntityList) {
                        ContentSchedule cs = new ContentSchedule();
                        cs.pane_num = pe.getPane_id();
                        cs.idx = 0;
                        mContentSchedule.add(cs);
                    }
                }
            }
        };
        mPaneList.observeForever(paneListObserver);


        mContentList = Transformations.switchMap(mSceneId,
                new Function<Integer, LiveData<List<ContentEntity>>>() {

                    @Override
                    public LiveData<List<ContentEntity>> apply(Integer mSceneId) {
                        if (mSceneId == 0) {
                            //noinspection unchecked
                            return null;
                        } else {
                            //noinspection ConstantConditions
                            return DatabaseCreator.getInstance(_context).contentDao().loadContentListById(mSceneId);
                        }
                    }
                });


        Observer<List<ContentEntity>> contentListObserver = new Observer<List<ContentEntity>>() {
            @Override
            public void onChanged(@Nullable List<ContentEntity> contentEntities) {
                if(contentEntities != null && contentEntities.size() > 0){
                    // mSceneList =  contentEntities;
                    //mSceneId.setValue(1);
                }
            }
        };
        mContentList.observeForever(contentListObserver);

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

   /* public LiveData<Integer> getCurrSchduleId() {
        return mScheduleId;
    }
*/

    public void getNextScene() {

      /*  ////
        //for control scene play time
        int playTime = mScene.getValue().getOpPlayTime();

        if(_scenePlayTime != 0){
            if(--_scenePlayTime==0){

               // getNextSceneId();
            };

        }*/
    }

    public MutableLiveData<Integer> getScheduleDone() { return  mScheduleDone;}

    public LiveData<List<PaneEntity>> getPaneList() {
        return mPaneList;
    }

    public LiveData<SceneEntity> getScene(){
        return mScene;
    }

    public LiveData<List<ContentEntity>> getContentList() {
        return mContentList;
    }



    public void updateScheduleTick() {
        mTickCount++;

        if(mScheduleList.getValue() == null) return;

        getNextSchedule();
        getNextScene();

        //for control schedule change

    }

    @SuppressLint("StaticFieldLeak")
    private void getNextSchedule() {
        if(mScheduleList.getValue() == null) return;

        new AsyncTask<Context, Void, Void>() {


            @Override
            protected Void doInBackground(Context... params) {
                Calendar cal = Calendar.getInstance();

                int nDoM 	= cal.get(Calendar.DAY_OF_MONTH);
                int nDoW 	= cal.get(Calendar.DAY_OF_WEEK)-2;	// Sunday = 1;
                // Because WE start Monday.
                if(nDoW == -1)	nDoW = 6;

                int nDate	= Integer.valueOf(String.format("%04d%02d%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, nDoM));
                int nTime	= Integer.valueOf(String.format("%02d%02d", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE)));


                for(ScheduleEntity se : mScheduleList.getValue())	{
                    if(checkOpS(nDoM, se.getOpS())
                            && checkOpDate(nDate,se.getOpStartDate(), se.getOpEndDate())
                            && checkOpW(nDoW, se.getOpW())
                            && checkOpTime(nTime, se.getOpStartTime(), se.getOpEndTime()))
                    {
                        int sid = mScheduleId.getValue();
                        if(sid != se.getId()){
                            sid = se.getId();
                            mScheduleId.postValue(sid);
                        }

                        break;
                    }
                }
                return null;
            }


        }.execute(_context.getApplicationContext());
    }

    private boolean checkOpS(int n, int opS)	{

        boolean nResult = false;

        if(opS == n || opS == 0) nResult = true;

        return nResult;
    }

    private boolean checkOpW(int n, String opW)	{

        boolean nResult = false;

        if(opW.isEmpty()) nResult = true;
        else {
            char[] cbOpW = opW.toCharArray();

            if(cbOpW[n] == '1') nResult = true;

        }

        return nResult;
    }


    private boolean checkOpDate(int n, String startData, String endDate)	{

        boolean nResult = false;

        if(startData.isEmpty() && endDate.isEmpty() ) nResult = true;
        else {
            long lSDate	= Long.valueOf(startData);
            long lEDate	= Long.valueOf(endDate);

            if(lSDate <= n && n <= lEDate) nResult = true;
        }

        return nResult;
    }

    private boolean checkOpTime(int n, String startTime, String endTime)	{

        boolean nResult = false;

        if(startTime.isEmpty() && endTime.isEmpty() ) nResult = true;
        else {
            int nSTime	= Integer.valueOf(startTime);
            int nETime	= Integer.valueOf(endTime);

            if(nSTime < n && n < nETime) nResult = true;
        }

        return nResult;
    }


    public ContentEntity  getContent(int paneNum) {

        if (mContentList != null) {
            /*ContentSchedule cs = CS_hasPaneNum(paneNum);
            if (cs == null) {
                cs = new ContentSchedule();
                cs.pane_num = paneNum;
                mContentSchedule.add(cs);
            }*/

            if(mContentSchedule.size() <= 0) return null;

            int _idx = 0;
            for (ContentSchedule cs : mContentSchedule) {
                for (ContentEntity ce : mContentList.getValue()) {
                    if (ce.getPane_id() == paneNum) {

                        if (cs.idx == _idx) {
                            cs.idx++;
                            return ce;
                        }
                        _idx++;
                    }

                }

            }
        }
        return null;
    }

    private ContentSchedule CS_hasPaneNum(int paneNum) {
        for (ContentSchedule cs : mContentSchedule) {
            if(cs.pane_num == paneNum) return cs;
        }

        return null;
    }
}
