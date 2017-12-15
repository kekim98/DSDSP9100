package com.dignsys.dsdsp.dsdsp_9100.viewmodel;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.dignsys.dsdsp.dsdsp_9100.Definer;
import com.dignsys.dsdsp.dsdsp_9100.db.AppDatabase;
import com.dignsys.dsdsp.dsdsp_9100.db.DatabaseCreator;
import com.dignsys.dsdsp.dsdsp_9100.db.DatabaseInitUtil;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ConfigEntity;
import com.dignsys.dsdsp.dsdsp_9100.service.AlarmReceiver;
import com.dignsys.dsdsp.dsdsp_9100.util.ImageUtil;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by bawoori on 17. 11. 24.
 */

public class ConfigHelper {

    private static final String TAG = ConfigHelper.class.getSimpleName();
    private static ConfigHelper sInstance;
    private final CommandHelper mCommand;

    private AppDatabase mDB;

    // private static final MutableLiveData ABSENT = new MutableLiveData();
    private final LiveData<ConfigEntity> mConfig;
    private PendingIntent mOnTimePI;
    private AlarmManager mOnTimeAlarm;
    private AlarmManager mOffTimeAlarm;
    private PendingIntent mOffTimePI;

    public LiveData<ConfigEntity> getConfig() {
        return mConfig;
    }

    private ConfigEntity _mConfig;


    private Context _context;

    // For Singleton instantiation
    private static final Object LOCK = new Object();

    private ConfigHelper(Context context) {
        _context = context;

        mDB = DatabaseCreator.getInstance(context);
        mConfig = mDB.configDao().loadConfig();

        mCommand = CommandHelper.getInstance(context);
       


        // Create the observer which updates the schedule list .
        final Observer<ConfigEntity> configObserver = new Observer<ConfigEntity>() {
            @Override
            public void onChanged(@Nullable final ConfigEntity config) {
                if (config != null ) {
                    _mConfig = config;
                    applyConfigs();
                }
            }
        };
        mConfig.observeForever(configObserver);
    }

    private void applyConfigs() {
        //apply config item only to system configuration(ex, timezone)

        applyTimezone();
        applyOnOffTime();


    }

    private void applyOnOffTime() {

        applyOffTime();
        applyOnTime();

    }

    private void applyOffTime() {
        if(_mConfig.getAutoOnOffMode() == 1) return;

        if (mOffTimeAlarm != null) {
            mOffTimeAlarm.cancel(mOffTimePI);
        }

        regOffTimeAlarm( ); //0=ON, 1=OFF

    }

    private void applyOnTime() {

        if(_mConfig.getAutoOnOffMode() == 1) return;

        if (mOnTimeAlarm != null) {
            mOnTimeAlarm.cancel(mOnTimePI);
        }

        regOnTimeAlarm(); //0=ON, 1=OFF

    }

    private void regOffTimeAlarm() {
        String time = _mConfig.getOffTime();

        if(TextUtils.isEmpty(time)) return;

        Log.d(TAG, "applyOnOffTime: onTime=" + time);

        Pattern pattern = Pattern.compile("^(\\d{2})(\\d{2})$");
        Matcher matcher = pattern.matcher(time);

        int hour=0;
        int minute = 0;
        while (matcher.find()) {
            hour = Integer.valueOf(matcher.group(1));
            minute = Integer.valueOf(matcher.group(2));
        }

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, hour); // For 24
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        Intent intent = new Intent(_context, AlarmReceiver.class);

        intent.setAction(Definer.DEF_OFF_TIME_ACTION);

        mOffTimePI = PendingIntent.getBroadcast(_context, 1,
                intent,PendingIntent.FLAG_UPDATE_CURRENT);

        mOffTimeAlarm = (AlarmManager) _context.getSystemService(Context.ALARM_SERVICE);
        mOffTimeAlarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), mOffTimePI);
    }

    private void regOnTimeAlarm() {
        String time = _mConfig.getOnTime();

        if(TextUtils.isEmpty(time)) return;

        Log.d(TAG, "applyOnOffTime: onTime=" + time);

        Pattern pattern = Pattern.compile("^(\\d{2})(\\d{2})$");
        Matcher matcher = pattern.matcher(time);

        int hour=0;
        int minute = 0;
        while (matcher.find()) {
            hour = Integer.valueOf(matcher.group(1));
            minute = Integer.valueOf(matcher.group(2));
        }

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, hour); // For 24
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        Intent intent = new Intent(_context, AlarmReceiver.class);

        intent.setAction(Definer.DEF_ON_TIME_ACTION);

        mOnTimePI = PendingIntent.getBroadcast(_context, 0,
                intent,PendingIntent.FLAG_UPDATE_CURRENT);

        mOnTimeAlarm = (AlarmManager) _context.getSystemService(Context.ALARM_SERVICE);
        mOnTimeAlarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), mOnTimePI);
    }



    private void applyTimezone() {
        String timezone = _mConfig.getTimezone(); //get rawoffset
        String temp = "GMT"+timezone;

        TimeZone tz = TimeZone.getTimeZone(temp);
        int offset = tz.getRawOffset();

        TimeZone searchTimeZone = null;

        Log.d(TAG, "applyConfigs: offset=" + tz.getRawOffset());

        for (String id : TimeZone.getAvailableIDs()) {
            TimeZone tt = TimeZone.getTimeZone(id);
            if (tt.getRawOffset() == offset) {
                searchTimeZone = tt;
                break;

            }
        }

        if (searchTimeZone != null) {
            Log.d(TAG, "applyConfigs: searchTimeZone.id=" + searchTimeZone.getID());
            mCommand.setTimeZone(searchTimeZone.getID());
        }
    }


    public synchronized static ConfigHelper getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = new ConfigHelper(context);
                }
            }
        }
        return sInstance;
    }


     String m_strCompanyID		= "000";

     String m_strUserFontPath1	= "";
     String m_strUserFontPath2	= "";
     String m_strUserFontPath3	= "";


     int m_nDownStartTime		= 0;
     int m_nDownEndTime		= 0;

     int m_nRSSCaptionMode 	= Definer.DEF_MESSAGE_TYPE_SCROLL;
     int m_nRSSContinueTime	= 0;

     //boolean m_bCommandOff		= false;

    @SuppressLint("StaticFieldLeak")
    private void updateConfig() {
        new AsyncTask<Context, Void, Void>() {
            @Override
            protected Void doInBackground(Context... contexts) {
                mDB.configDao().update(_mConfig);
                return null;
            }
        }.execute(_context);
    }

    public   void setDeviceID(String str){
        _mConfig.setDeviceId(str);
        updateConfig();
    }

    public   void setServerAddr(String str){
        _mConfig.setServerAddress(str);
        updateConfig();
    }
    public   void setServerFolder(String str){
        _mConfig.setServerFolder(str);
        updateConfig();
    }

    public   void setServerID(String str){
        _mConfig.setServerId(str);
        updateConfig();
    }

    public   void setServerPWD(String str){
        _mConfig.setServerPwd(str);
        updateConfig();
    }

    public   void setLanIP(String str){
        _mConfig.setLAN_IP(str);
        updateConfig();
    }

    public   void setLanSubnet(String str){
        _mConfig.setLAN_Subnet(str);
        updateConfig();
    }

    public   void setLanGateway(String str){
        _mConfig.setLAN_Gateway(str);
        updateConfig();
    }

    public   void setLanDNSServer(String str){
        _mConfig.setLAN_DNS(str);
        updateConfig();
    }

    public   void setWifiIP(String str){
        _mConfig.setWifi_IP(str);
        updateConfig();
    }

    public   void setWifiSubnet(String str){
        _mConfig.setWifi_Subnet(str);
        updateConfig();
    }

    public   void setWifiGateway(String str){
        _mConfig.setWifi_Gateway(str);
        updateConfig();
    }

    public   void setWifiDNSServer(String str){
        _mConfig.setWifi_DNS(str);
        updateConfig();
    }

    public   void setWifiESSID(String str){
        _mConfig.setWifi_ESSID(str);
        updateConfig();
    }

    public   void setWifiPWD(String str){
        _mConfig.setWifi_PWD(str);
        updateConfig();
    }

    public   void setCapRSSAddress(String str){
        _mConfig.setRSSAddress(str);
        updateConfig();
    }

    public   void setAutoOnTime(String str){
        _mConfig.setOnTime(str);
        updateConfig();
    }

    public   void setAutoOffTime(String str){
        _mConfig.setOffTime(str);
        updateConfig();
    }

    public   void setIANetIP(String str){
        _mConfig.setIA_NetIP(str);
        updateConfig();
    }

    public   void setMSServerIP(String str){
        _mConfig.setMS_ServerIP(str);
        updateConfig();
    }

    public   void setDTVChannel(String str){
        _mConfig.setDTV_Channel(str);
        updateConfig();
    }


    public   void setDSPMode(int n){
        _mConfig.setDSPMode(n);
        updateConfig();
    }

    public   void setBacklight(int n){
        _mConfig.setBacklight(n);
        updateConfig();
    }

    public   void setVolume(int n){
        _mConfig.setAudioVolume(n);
        updateConfig();
    }

    public   void setTVMode(int n){
        _mConfig.setTVMode(n);
        updateConfig();
    }

    public   void setVideoOutput(int n){
        _mConfig.setVideoOutput(n);
        updateConfig();
    }

    public   void setAudioMode(int n){
        _mConfig.setAudioMode(n);
        updateConfig();
    }

    public   void setServerMode(int n){
        _mConfig.setServerMode(n);
        updateConfig();
    }

    public   void setServerPort(String n){
        _mConfig.setServerPort(n);
        updateConfig();
    }

    public   void setServerSyncInterval(int n){
        _mConfig.setServerSyncInterval(n);
        updateConfig();
    }

    public   void setLanUse(int n){
        _mConfig.setUseLAN(n);
        updateConfig();
    }

    public   void setLanUseDHCP(int n){
        _mConfig.setUseLAN_DHCP(n);
        updateConfig();
    }

    public   void setLanDNS(int n){
        _mConfig.setLAN_DNS(String.valueOf(n));
        updateConfig();
    }

    public   void setWifiUse(int n){
        _mConfig.setUseWifi(n);
        updateConfig();
    }

    public   void setWifiUseDHCP(int n){
        _mConfig.setUseWifi_DHCP(n);
        updateConfig();
    }

    public   void setWifiDNS(int n){
        _mConfig.setWifi_DNS(String.valueOf(n));
        updateConfig();
    }

    public   void setWifiAuthMode(int n){
        _mConfig.setWifi_AuthMode(n);
        updateConfig();
    }

    public   void setPicChangeEffect(int n){
        _mConfig.setImageChangeEffect(n);
        updateConfig();
    }

    public   void setPicChangeTime(int n){
        _mConfig.setImageChangeInterval(n);
        updateConfig();
    }

    public   void setResizeMovie(int n){
        _mConfig.setUseAutoResizeMovie(n);
        updateConfig();
    }

    public   void setResizePic(int n){
        _mConfig.setUseAutoResizeImage(n);
        updateConfig();
    }

    public   void setCapMode(int n){
        _mConfig.setCapMode(n);
        updateConfig();
    }

    public   void setCapPosition(int n){
        _mConfig.setCapPosition(n);
        updateConfig();
    }

    public   void setCapColor(int n) {
        _mConfig.setCapColor(n);
        updateConfig();
    }

    public   void setCapBGColor(int n){
        _mConfig.setCapBackColor(n);
        updateConfig();
    }

    public   void setCapSpeed(int n) {
        _mConfig.setCapSpeed(n);
        updateConfig();
    }

    public   void setCapFont(int n){
        _mConfig.setCapFont(n);
        updateConfig();
    }

    public   void setCapFontSize(int n){
        _mConfig.setCapSize(n);
        updateConfig();
    }

    public   void setCapRSSMode(int n){
        _mConfig.setRSSMode(n);
        updateConfig();
    }

    public   void setCapRSSUpdateTime(int n){
        _mConfig.setRSSUpdateInterval(n);
        updateConfig();
    }

    public   void setLanguage(int n){
        //TODO : not implemented
    }

    public   void setTimePosition(int n) {
   /*     _mConfig.setTimeDisplayPosition(n);
        updateConfig();*/
    }
    public   void setTimeColor(int n) {
        _mConfig.setTimeDisplayColor(n);
        updateConfig();
    }

    public   void setTimeBGColor(int n){
        _mConfig.setTimeDisplayBackColor(n);
        updateConfig();
    }

    public   void setTimeFont(int n) {
        _mConfig.setTimeDisplayFont(n);
        updateConfig();
    }

    public   void setTimeFontSize(int n) {
        _mConfig.setTimeDisplayFontSize(n);
        updateConfig();
    }

    public   void setAutoOnOffMode(int n) {
        _mConfig.setAutoOnOffMode(n);
        updateConfig();
    }

    public   void setIAMode(int n) {
        _mConfig.setIA_Mode(n);
        updateConfig();
    }

    public   void setIAReq(int n) {
        _mConfig.setIA_ReqMeans(n);
        updateConfig();
    }

    public   void setIABaud(int n) {
        _mConfig.setIA_SerialBaud(n);
        updateConfig();
    }

    public   void setIADataBit(int n) {
        _mConfig.setIA_SerialData(n);
        updateConfig();
    }

    public   void setIAParityBit(int n) {
        _mConfig.setIA_SerialParity(n);
        updateConfig();
    }

    public   void setIAStopBit(int n) {
        _mConfig.setIA_SerialStop(n);
        updateConfig();
    }

    public   void setIANetPort(int n) {
        _mConfig.setIA_NetPort(n);
        updateConfig();
    }

    public   void setMSMode(int n) {
        _mConfig.setMS_Mode(n);
        updateConfig();
    }

    public   void setMSUserCnt(int n) {
        _mConfig.setMS_UserCount(n);
        updateConfig();
    }

    public   void setDTVModulation(int n) {
        _mConfig.setDTV_Modulation(n);
        updateConfig();
    }

    public   void setDTVAutoRange(int n) {
        _mConfig.setDTV_AutoSearchRange(n);
        updateConfig();
    }

    public   void setSOSD(int n) {
        //TODO : not implemented

    }

    public   void setSOLogSave(int n) {
        _mConfig.setUseSDLogSave(n);
        updateConfig();
    }

    public   void setSOLogSaveDay(int n) {
        _mConfig.setLogFileSaveDay(n);
        updateConfig();
    }

    public   void setEnableLive(int n) {
        _mConfig.setEnableLiveUpload(n);
        updateConfig();
    }

    public   void setEnableLog(int n) {
        _mConfig.setEnableLogUpload(n);
        updateConfig();
    }

    public   void setEnableCapture(int n) {
        _mConfig.setEnableCaptureUpload(n);
        updateConfig();
    }

    public   void setIntervalLive(int n) {
        _mConfig.setIntervalLive(n);
        updateConfig();
    }

    public   void setIntervalLog(int n) {
        _mConfig.setIntervalLog(n);
        updateConfig();
    }

    public   void setIntervalCapture(int n) {
        _mConfig.setIntervalCapture(n);
        updateConfig();
    }

    public   void setCaptureScale(int n) {
        _mConfig.setCaptureScale(n);
        updateConfig();
    }

    public   void setTimezone(String n) {
        _mConfig.setTimezone(n);
        updateConfig();
    }


    public   String getTimezone(){
        return _mConfig.getTimezone();
    }

    public   int getDSPMode(){
        return _mConfig.getDSPMode();
    }

    public   int getBacklight(){
        return _mConfig.getBacklight();
    }

    public   int getVolume(){
        return _mConfig.getAudioVolume();
    }

    public   int getTVMode(){
        return _mConfig.getTVMode();
    }

    public   int getVideoOutput(){
        return _mConfig.getVideoOutput();
    }

    public   int getAudioMode(){
        return _mConfig.getAudioMode();
    }

    public   int getServerMode(){
        return _mConfig.getServerMode();
    }

    public   String getServerPort(){
        return _mConfig.getServerPort();
    }

    public   int getServerSyncInterval(){
        return _mConfig.getServerSyncInterval();
    }

    public   int getLanUse(){
        return _mConfig.getUseLAN();
    }

    public   int getLanUseDHCP(){
        return _mConfig.getUseLAN_DHCP();
    }

    public   int getLanDNS(){
        return _mConfig.getLAN_AutoDNS();
    }

    public   int getWifiUse(){
        return _mConfig.getUseWifi();
    }

    public   int getWifiUseDHCP(){
        return _mConfig.getUseWifi_DHCP();
    }

    public   int getWifiDNS(){
        return _mConfig.getWifi_AutoDNS();
    }

    public   int getWifiAuthMode(){
        return _mConfig.getWifi_AuthMode();
    }

    public   int getPicChangeEffect(){
        return _mConfig.getImageChangeEffect();
    }

    public   int getPicChangeTime(){
        return _mConfig.getImageChangeInterval();
    }

    public   int getResizeMovie(){
        return _mConfig.getUseAutoResizeMovie();
    }

    public   int getResizePic(){
        return _mConfig.getUseAutoResizeImage();
    }

    public   int getCapMode(){
        return _mConfig.getCapMode();
    }

    public   int getCapPosition(){
        return _mConfig.getCapPosition();
    }

    public   int getCapColor(){
        return _mConfig.getCapColor();
    }

    public   int getCapBGColor(){
        return _mConfig.getCapBackColor();
    }

    public   int getCapSpeed(){
        return _mConfig.getCapSpeed();
    }

    public   int getCapFont(){
        return _mConfig.getCapFont();
    }

    public   int getCapFontSize(){
        return _mConfig.getTimeDisplayFontSize();
    }

    public   int getCapRSSMode(){
        return _mConfig.getRSSMode();
    }

    public   int getCapRSSUpdateTime(){
        return _mConfig.getRSSUpdateInterval();
    }
    public   int getLanguage(){
        //TODO: not implementation
        return 0;
    }

    public   int getTimePosition(){
        //TODO : must change
        return 0;
       // return _mConfig.getTimeDisplayPosition();
    }

    public   int getTimeColor(){
        return _mConfig.getTimeDisplayColor();
    }

    public   int getTimeBGColor(){
        return _mConfig.getTimeDisplayBackColor();
    }

    public   int getTimeFont(){
        return _mConfig.getTimeDisplayFont();
    }

    public   int getTimeFontSize(){
        return _mConfig.getTimeDisplayFontSize();
    }

    public   int getAutoOnOffMode(){
        return _mConfig.getAutoOnOffMode();
    }

    public   int getIAMode(){
        return _mConfig.getIA_Mode();
    }

    public   int getIAReq(){
        return _mConfig.getIA_ReqMeans();
    }

    public   int getIABaud(){
        return _mConfig.getIA_SerialBaud();
    }

    public   int getIADataBit(){
        return _mConfig.getIA_SerialData();
    }

    public   int getIAParityBit(){
        return _mConfig.getIA_SerialParity();
    }

    public   int getIAStopBit(){
        return _mConfig.getIA_SerialStop();
    }

    public   int getIANetPort(){
        return _mConfig.getIA_NetPort();
    }

    public   int getMSMode(){
        return _mConfig.getMS_Mode();
    }

    public   int getMSUserCnt(){
        return _mConfig.getMS_UserCount();
    }

    public   int getDTVModulation(){
        return _mConfig.getDTV_Modulation();
    }

    public   int getDTVAutoRange(){
        return _mConfig.getDTV_AutoSearchRange();
    }

    public   int getSOSD(){
        //TODO:not implemented
        return 0;
    }
    public   int getSOLogSave(){
        return _mConfig.getUseSDLogSave();
    }
    public   int getSOLogSaveDay(){
        return _mConfig.getLogFileSaveDay();
    }

    public   int getEnableLive(){
        return _mConfig.getEnableLiveUpload();
    }

    public   int getEnableLog(){
        return _mConfig.getEnableLogUpload();
    }

    public   int getEnableCapture(){
        return _mConfig.getEnableCaptureUpload();
    }

    public   int getIntervalLive(){
        return _mConfig.getIntervalLive();
    }

    public   int getIntervalLog(){
        return _mConfig.getIntervalLog();
    }

    public   int getIntervalCapture(){
        return _mConfig.getIntervalCapture();
    }

    public   int getCaptureScale(){
        return _mConfig.getCaptureScale();
    }



    public   String getFirmwareVersion(){
        return _mConfig.getFirmwareVersion();
    }

    public   String getDeviceID(){
        return _mConfig.getDeviceId();
    }

    public   String getServerAddr(){
        return _mConfig.getServerAddress();
    }

    public   String getServerFolder(){
        return _mConfig.getServerFolder();
    }

    public   String getServerID(){
        return _mConfig.getServerId();
    }

    public   String getServerPWD(){
        return _mConfig.getServerPwd();
    }

    public   String getLanIP(){
        return _mConfig.getLAN_IP();
    }

    public   String getLanSubnet(){
        return _mConfig.getLAN_Subnet();
    }

    public   String getLanGateway(){
        return _mConfig.getLAN_Gateway();
    }

    public   String getLanDNSServer(){
        return _mConfig.getLAN_DNS();
    }

    public   String getWifiIP(){
        return _mConfig.getWifi_IP();
    }

    public   String getWifiSubnet(){
        return _mConfig.getWifi_Subnet();
    }

    public   String getWifiGateway(){
        return _mConfig.getWifi_Gateway();
    }

    public   String getWifiDNSServer(){
        return _mConfig.getWifi_DNS();
    }

    public   String getWifiESSID(){
        return _mConfig.getWifi_ESSID();
    }

    public   String getWifiPWD(){
        return _mConfig.getWifi_PWD();
    }

    public   String getCapRSSAddress(){
        return _mConfig.getRSSAddress();
    }

    public   String getAutoOnTime(){
        return _mConfig.getOnTime();
    }

    public   String getAutoOffTime(){
        return _mConfig.getOffTime();
    }

    public   int getOffDay(){
        return _mConfig.getOffDay();
    }

    public String getOffWeek(){ return _mConfig.getOffWeek();}

    public   String getIANetIP(){
        return _mConfig.getIA_NetIP();
    }

    public   String getMSServerIP(){
        return _mConfig.getMS_ServerIP();
    }

    public   String getDTVChannel(){
        return _mConfig.getDTV_Channel();
    }

    public   String getUMSPath(){
        //TODO : not implemented
        return null;
    }

    public   String getRootPath(){
        //TODO : not implemented
        return null;
    }
	



    public  void setUserFont(int n, String strPath)	{

        switch(n)	{
            case 1:
                m_strUserFontPath1 = strPath;
                break;
            case 2:
                m_strUserFontPath2 = strPath;
                break;
            case 3:
                m_strUserFontPath3 = strPath;
                break;
        }
    }



    public  int getUserFontID(String strFontName)	{

        int n = 0;

        if(!m_strUserFontPath1.isEmpty())	{
            if(m_strUserFontPath1.equals(strFontName)) n = 2 + 1;
        }

        if(!m_strUserFontPath2.isEmpty())	{
            if(m_strUserFontPath2.equals(strFontName)) n = 2 + 2;
        }

        if(!m_strUserFontPath3.isEmpty())	{
            if(m_strUserFontPath3.equals(strFontName)) n = 2 + 3;
        }


        return n;
    }

   /* public  boolean getCommandOff()			{ return m_bCommandOff;			}
    public  void setCommandOff(boolean b)		{ m_bCommandOff = b;			}*/

    public  void setDownStartTime(int n)		{ m_nDownStartTime 	= n;		}
    public  void setDownEndTime(int n)		{ m_nDownEndTime 	= n;		}
    public  int  getDownStartTime()			{ return m_nDownStartTime;		}
    public  int  getDownEndTime()				{ return m_nDownEndTime;		}


    public  void setCompanyID(String str)		{ m_strCompanyID 	= str;		}
    public  String  getCompanyID()			{ return m_strCompanyID;		}


    public  void setRSSCaptionMode(int n)		{ m_nRSSCaptionMode = n;		}
    public  int  getRSSCaptionMode()			{ return m_nRSSCaptionMode;		}

    public  void setRSSContinueTime(int n)	{ m_nRSSContinueTime = n;		}
    public  int  getRSSContinueTime()			{ return m_nRSSContinueTime;	}

    public  int getPicChangeEffectValue(String str)
    {
        int n = 0;

        if(str.equals("disable")) 	n = 0;
        if(str.equals("random")) 	n = 1;
        if(str.equals("crossfade")) n = 2;
        if(str.equals("leftward")) 	n = 3;
        if(str.equals("rightward")) n = 4;
        if(str.equals("upward")) 	n = 5;
        if(str.equals("increase")) 	n = 6;
        if(str.equals("downward")) 	n = 7;

        return n;

    }

    public  int getCapPositionValue(String str)
    {
        int n = 0;

        if(str.equals("top")) 		n = 0;
        if(str.equals("bottom")) 	n = 1;
        if(str.equals("left")) 		n = 2;
        if(str.equals("right")) 	n = 3;

        return n;
    }

    public  int getTimePositionValue(String str)
    {
        int n = 0;

        if(str.equals("disable")) 		n = 0;
        if(str.equals("lefttop")) 		n = 1;
        if(str.equals("righttop")) 		n = 2;
        if(str.equals("leftbottom")) 	n = 3;
        if(str.equals("rightbottom")) 	n = 4;

        return n;
    }

    public  int getCapSpeedValue(String str)
    {

        int n = 0;

        if(str.equals("quick")) 	n = 0;
        if(str.equals("normal")) 	n = 1;
        if(str.equals("slow")) 		n = 2;

        return n;
    }

    public  int getRSSCaptionModeValue(String str)
    {

        int n = 0;

        if(str.equals("scroll")) 			n = Definer.DEF_MESSAGE_TYPE_SCROLL;
        if(str.equals("left")) 		n = Definer.DEF_MESSAGE_TYPE_STATIC_LEFT;
        if(str.equals("right")) 		n = Definer.DEF_MESSAGE_TYPE_STATIC_RIGHT;
        if(str.equals("center")) 		n = Definer.DEF_MESSAGE_TYPE_STATIC_MIDDLE;
        if(str.equals("wrapupward")) 		n = Definer.DEF_MESSAGE_TYPE_WRAP_UP;
        if(str.equals("wrapdownward")) 		n = Definer.DEF_MESSAGE_TYPE_WRAP_DOWN;
        if(str.equals("wrapupwardstop")) 	n = Definer.DEF_MESSAGE_TYPE_WRAP_STOP_UP;
        if(str.equals("wrapdownwardstop")) 	n = Definer.DEF_MESSAGE_TYPE_WRAP_STOP_DOWN;

        return n;
    }

    public  int getFontValue(String str)
    {
        int n = 0;

        if(str.equals("NanumGothic"))	n = 1;
        if(str.equals("NanumMyeongjo")) n = 2;

        return n;
    }

    public  int getColorValue(String str)
    {
        int n = 0;

        if(str.equals("red")) 			n = 0;
        if(str.equals("orange")) 		n = 1;
        if(str.equals("yellow")) 		n = 2;
        if(str.equals("green")) 		n = 3;
        if(str.equals("blue")) 			n = 4;
        if(str.equals("deepblue")) 		n = 5;
        if(str.equals("voilet")) 		n = 6;
        if(str.equals("black")) 		n = 7;
        if(str.equals("white")) 		n = 8;
        if(str.equals("transparent")) 	n = 9;

        return n;
    }

    @SuppressLint("StaticFieldLeak")
    public void initConfigs() {

        new AsyncTask<Context, Void, Void>() {

            @Override
            protected Void doInBackground(Context... params) {
                final AppDatabase db = DatabaseCreator.getInstance(_context);
                if(db.configDao().configCount() == 0) DatabaseInitUtil.initializeDb(db);
                return null;
            }

        }.execute(_context);
    }

    public void removeObservers() {

    }
}

    