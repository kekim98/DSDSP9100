
package com.dignsys.dsdsp.dsdsp_9100.service.handler;

import android.content.Context;

import com.dignsys.dsdsp.dsdsp_9100.Definer;
import com.dignsys.dsdsp.dsdsp_9100.db.AppDatabase;
import com.dignsys.dsdsp.dsdsp_9100.db.DatabaseCreator;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ConfigEntity;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class ConfigHandler extends BasicHandler {
    private static final String TAG = ConfigHandler.class.getSimpleName();

    private ConfigEntity mConfig = null;
   

    public ConfigHandler(Context context) {
        super(context);
    }


    @Override
    public void process(String body) {

        AppDatabase db = DatabaseCreator.getInstance(mContext);
        mConfig = db.configDao().loadConfigSync();

        String strLine = "";
        // convert String into InputStream
        InputStream is = new ByteArrayInputStream(body.getBytes());

        BufferedReader br = new BufferedReader(new InputStreamReader(is));

       try {
           while ((strLine = br.readLine()) != null) {

               if(strLine.length() == 0 ) continue;

               if(strLine.startsWith("set dsp"))	{

                   String[] saConfig = new String(strLine).split(" ");

                   if(saConfig.length > 4)	{

                       String strItem 		= saConfig[2];
                       String strSubItem 	= saConfig[3];
                       String strValue 	= saConfig[4];

                       // set dsp yday 1217 off
                       if(strItem.equals("yday"))	{
                           if(strValue.equals("off") )	mConfig.setOffDay(Integer.valueOf(strSubItem));
                       }

                       // set dsp wday fri off
                       if(strItem.equals("wday"))	{

                           final String ow = "1111111";
                           char[] cb = ow.toCharArray();
                           String val = "";

                           if(strSubItem.equals("mon") ) cb[0] = '0';
                           if(strSubItem.equals("tue") ) cb[1] = '0';
                           if(strSubItem.equals("wed") ) cb[2] = '0';
                           if(strSubItem.equals("thu") ) cb[3] = '0';
                           if(strSubItem.equals("fri") ) cb[4] = '0';
                           if(strSubItem.equals("sat") ) cb[5] = '0';
                           if(strSubItem.equals("sun") ) cb[6] = '0';

                           for(int nCnt=0 ; nCnt < cb.length ; nCnt++)	{
                               val += Character.toString(cb[nCnt]);
                           }
                           mConfig.setOffWeek(val);
                       }

                       // set dsp reboot time
                       if(strItem.equals("reboot"))	{
                           if(strSubItem.equals("time")) {
                               mConfig.setRebootTime(Integer.valueOf(strValue.replace(":", "")));
                           }
                       }

                       if(strItem.equals("download"))	{
                           if(strSubItem.equals("start")) {

                               int nCurrVal = Integer.valueOf(strValue.replace(":", ""));

                               mConfig.setDownloadStartTime(nCurrVal);

                           }

                           if(strSubItem.equals("end")) {

                               int nCurrVal = Integer.valueOf(strValue.replace(":", ""));

                               mConfig.setDownloadEndTime(nCurrVal);
                           }
                       }

                       if(strItem.equals("contents"))	{
                           if(strSubItem.equals("up"))	{

                               int nCurrVal = Integer.valueOf(strValue);

                               mConfig.setServerSyncInterval(nCurrVal);
                           }
                       }

                       if(strItem.equals("log"))	{

                           if(strSubItem.equals("status") )	{

                               int nCurrVal = 1;	// if(strValue.equals("on"))

                               if(strValue.equals("off")) nCurrVal = 0;

                               mConfig.setEnableLogUpload(nCurrVal);

                           }

                           if(strSubItem.equals("up") )	{

                               int nCurrVal = Integer.valueOf(strValue);
                               mConfig.setIntervalLog(nCurrVal);

                           }
                       }

                       if(strItem.equals("live"))	{

                           if(strSubItem.equals("status") )	{

                               int nCurrVal = 1;	// if(strValue.equals("on"))

                               if(strValue.equals("off")) nCurrVal = 0;

                               mConfig.setEnableLiveUpload(nCurrVal);

                           }

                           if(strSubItem.equals("up") )	{

                               int nCurrVal = Integer.valueOf(strValue);

                               mConfig.setIntervalLive(nCurrVal);
                           }
                       }

                       if(strItem.equals("capture"))	{

                           if(strSubItem.equals("status") )	{

                               int nCurrVal = 1;	// if(strValue.equals("on"))

                               if(strValue.equals("off")) nCurrVal = 0;

                               mConfig.setEnableCaptureUpload(nCurrVal);

                           }

                           if(strSubItem.equals("up") )			{

                               int nCurrVal = Integer.valueOf(strValue);

                               mConfig.setIntervalCapture(nCurrVal);
                           }
                           if(strSubItem.equals("percentage") )	{

                               int nCurrVal = Integer.valueOf(strValue);

                               mConfig.setCaptureScale(nCurrVal);

                           }
                       }

                       if(strItem.equals("basic"))	{
                           // set dsp basic deviceid "xxxxxxxx"
                           if(strSubItem.equals("deviceid")) {

                               String strCurrVal = strValue.replace("\"", "");
                               mConfig.setDeviceId(strCurrVal);
                           }

                           // set dsp basic dspmode standard | set dsp basic dspmode interactive
                           if(strSubItem.equals("dspmode"))	{

                               int nCurrVal = Definer.DEF_CFG_ITEM_VALUE_STANDARD;		// strValue.equals("standard")

                               if(strValue.equals("interactive")) nCurrVal = Definer.DEF_CFG_ITEM_VALUE_INTERACTIVE;

                               mConfig.setDSPMode(nCurrVal);
                           }
                       }

                       if(strItem.equals("video"))	{

                           // set dsp video backlight xx
                           if(strSubItem.equals("backlight")) {

                               int nCurrVal = Integer.valueOf(strValue);

                               mConfig.setBacklight(nCurrVal);
                           }

                           // set dsp video tvmode pal || set dsp video tvmode ntsc
                           if(strSubItem.equals("tvmode"))	{

                               int nCurrVal = Definer.DEF_CFG_ITEM_VALUE_PAL;		// strValue.equals("pal")

                               if(strValue.equals("ntsc")) nCurrVal = Definer.DEF_CFG_ITEM_VALUE_NTSC;

                               mConfig.setTVMode(nCurrVal);

                           }

                           // set dsp video output hdmi_720p | set dsp video output hdmi_1080p
                           if(strSubItem.equals("output"))	{

                               int nCurrVal = Definer.DEF_CFG_ITEM_VALUE_720P;		// strValue.equals("hdmi_720p")

                               if(strValue.equals("hdmi_1080p")) nCurrVal = Definer.DEF_CFG_ITEM_VALUE_1080P;

                               mConfig.setVideoOutput(nCurrVal);
                           }

                       }

                       if(strItem.equals("audio"))	{

                           // set dsp audio vol xx
                           if(strSubItem.equals("vol")) {

                               int nCurrVal = Integer.valueOf(strValue);

                               mConfig.setAudioVolume(nCurrVal);
                           }

                           // set dsp video output hdmi_720p | set dsp video output hdmi_1080p
                           if(strSubItem.equals("output"))	{

                               int nCurrVal = Definer.DEF_CFG_ITEM_VALUE_STEREO;		// strValue.equals("stereo")

                               if(strValue.equals("surroun")) nCurrVal = Definer.DEF_CFG_ITEM_VALUE_SURROUN;


                               mConfig.setAudioMode(nCurrVal);

                           }
                       }

                       if(strItem.equals("server"))	{

                           // set dsp server mode standalone | set dsp server mode internet
                           if(strSubItem.equals("mode"))	{

                               int nCurrVal = Definer.DEF_CFG_ITEM_VALUE_STANDALONE;	// strValue.equals("standalone")

                               if(strValue.equals("internet")) nCurrVal = Definer.DEF_CFG_ITEM_VALUE_INTERNET;

                               mConfig.setServerMode(nCurrVal);
                           }

                           // set dsp server ip "192.168.0.1"
                           if(strSubItem.equals("ip")) {
                               String strCurrVal = strValue.replace("\"", "");
                               mConfig.setServerAddress(strCurrVal);
                           }

                           // set dsp server url "http://serverurl.com"
                           if(strSubItem.equals("url")) {

                               String strCurrVal = strValue.replace("\"", "");

                               mConfig.setServerAddress(strCurrVal);

                           }

                           // set dsp server port 8089
                           if(strSubItem.equals("port")) {
                               int nCurrVal = Integer.valueOf(strValue);
                               mConfig.setServerPort(nCurrVal);
                           }

                           // set dsp server folder "site/o/s2"
                           if(strSubItem.equals("folder")) {

                               String strCurrVal = strValue.replace("\"", "");

                               mConfig.setServerFolder(strCurrVal);
                           }

                           // set dsp server id "id_111"
                           if(strSubItem.equals("id")) {

                               String strCurrVal = strValue.replace("\"", "");

                               mConfig.setServerId(strCurrVal);
                           }

                           // set dsp server password "pwd"
                           if(strSubItem.equals("password")) {

                               String strCurrVal = strValue.replace("\"", "");

                               mConfig.setServerPwd(strCurrVal);
                           }

                           // set dsp server interval 30
                           if(strSubItem.equals("interval")) {
                               int nCurrVal = Integer.valueOf(strValue);

                               mConfig.setServerSyncInterval(nCurrVal);
                           }
                       }

                       if(strItem.equals("lan"))	{

                           // set dsp lan support enable | set dsp lan support disable
                           //if(strSubItem.equals("support"))	{
                           //	if(strValue.equals("enable")) 			DSLibIF.setLanUse(Definer.DEF_CFG_ITEM_VALUE_USE);
                           //	else if(strValue.equals("disable")) 	DSLibIF.setLanUse(Definer.DEF_CFG_ITEM_VALUE_NOT_USE);
                           //}

                           // set dsp lan ip auto | set dsp lan ip fix
                           // set dsp lan ipaddress "192.168.0.1"
                           // set dsp lan subnet "255.255.255.0"
                           // set dsp lan gateway "192.168.0.1"
                           // set dsp lan dns fix | set dsp lan dns auto
                           // set dsp lan dnsaddress "111.222.333.444"

                         //TODO : not yet implemented
                       }

                       if(strItem.equals("wlan"))	{

                           //set dsp wlan support enable | set dsp wlan support disable
                           //set dsp wlan ssid "SSIDVALUE"
                           //set dsp wlan dnsaddress "xxx.xxx.xxx.xxx"
                           //set dsp wlan dns auto | set dsp wlan dns fix
                           //set dsp wlan gateway "xxx.xxx.xxx.xxx"
                           //set dsp wlan subnet "xxx.xxx.xxx.xxx"
                           //set dsp wlan ipaddress "xxx.xxx.xxx.xxx"
                           //set dsp wlan ip auto | set dsp wlan ip fix
                           //set dsp wlan wepkey "wepkey_value"

                           //TODO : not yet implemented
                       }

                       if(strItem.equals("screen"))	{

//								set dsp screen slide downward | increase | upward | leftward | rightward | crossfade | random | disable
                           if(strSubItem.equals("slide") || strSubItem.equals("downward") || strSubItem.equals("upward")
                                   || strSubItem.equals("leftward") || strSubItem.equals("rightward")
                                   || strSubItem.equals("crossfade") || strSubItem.equals("random") || strSubItem.equals("disable") ) {

                               mConfig.setImageChangeEffect(Integer.valueOf(strValue));
                           }

//								set dsp screen transitiontime 999
                           if(strSubItem.equals("transitiontime")) {

                               int nCurrVal = Integer.valueOf(strValue);

                               mConfig.setImageChangeInterval(nCurrVal);
                           }


//								set dsp screen caption textonly
//								set dsp screen caption textrss
                           if(strSubItem.equals("caption")) {

                               int nCurrVal = Definer.DEF_CFG_ITEM_VALUE_CAP_TEXT;	// strValue.equals("textonly")

                               if(strValue.equals("textrss")) nCurrVal = Definer.DEF_CFG_ITEM_VALUE_CAP_TEXT_RSS;

                               mConfig.setCapMode(nCurrVal);
                           }

//								set dsp screen captionposition top | bottom | left | right
                           if(strSubItem.equals("captionposition")) {

                               int nCurrVal = Integer.valueOf(strValue);
                               mConfig.setCapPosition(nCurrVal);
                           }

//								set dsp screen captioncolor red | white ...
                           if(strSubItem.equals("captioncolor")) {

                               int nCurrVal = Integer.valueOf(strValue);
                               mConfig.setCapColor(nCurrVal);
                           }

//								set dsp screen captionbgcolor red | transparent ...
                           if(strSubItem.equals("captionbgcolor")) {

                               int nCurrVal = Integer.valueOf(strValue);
                               mConfig.setCapBackColor(nCurrVal);
                           }

//								set dsp screen captionscroll quick | normal |slow
                           if(strSubItem.equals("captionscroll")) {

                               int nCurrVal = Integer.valueOf(strValue);
                               mConfig.setCapSpeed(nCurrVal);
                           }

//								set dsp screen autoscaling enable
                           if(strSubItem.equals("autoscaling")) {

                               int nCurrVal = 0;	// strValue.equals("enable")

                               if(strValue.equals("disable")) nCurrVal = 1;

                               mConfig.setUseAutoResizeMovie(nCurrVal);
                           }

//								set dsp screen autoscalingpicture enable
                           if(strSubItem.equals("autoscalingpicture")) {

                               int nCurrVal = 0;	// strValue.equals("enable")

                               if(strValue.equals("disable")) nCurrVal = 1;

                               mConfig.setUseAutoResizeImage(nCurrVal);
                           }

//								set dsp screen captionfont dotum
                           if(strSubItem.equals("captionfont")) {

                               int nCurrVal = Integer.valueOf(strValue);
                               mConfig.setCapFont(nCurrVal);
                           }

//								set dsp screen captionuserfont "gulim"
                           if(strSubItem.equals("captionuserfont")) {

                               String t = strValue.replace("\"", "");
                               int nCurrVal = Integer.valueOf(t);
                               mConfig.setCapFont(nCurrVal);
                           }

//								set dsp screen captionfontsize 99
                           if(strSubItem.equals("captionfontsize")) {

                               int nCurrVal = Integer.valueOf(strValue);
                               mConfig.setCapSize(nCurrVal);
                           }

                       }

                       if(strItem.equals("rss"))	{

                           //set dsp rss url "http://rss.com"
                           if(strSubItem.equals("url")) {

                               String strCurrVal = strValue.replace("\"", "");

                               mConfig.setRSSAddress(strCurrVal);
                           }

                           //set dsp rss mode all
                           if(strSubItem.equals("mode") )	{

                               int nCurrVal = 0;	// if(strValue.equals("description"))

                               if(strValue.equals("title")) 	nCurrVal = 1;
                               if(strValue.equals("all")) 		nCurrVal = 2;

                               mConfig.setRSSMode(nCurrVal);
                           }

                           //set dsp rss interval 999
                           if(strSubItem.equals("interval")) {

                               int nCurrVal = Integer.valueOf(strValue);
                               mConfig.setRSSUpdateInterval(nCurrVal);
                           }

                           //set dsp rss captionmode scroll | wrapdownwardstop
                           if(strSubItem.equals("captionmode")) {


                               int nCurrVal = Integer.valueOf(strValue);
                               mConfig.setRssCaptionMode(nCurrVal);
                           }

                           //set dsp rss captioncontinuetime 999
                           if(strSubItem.equals("captioncontinuetime")) {

                               int nCurrVal = Integer.valueOf(strValue);

                               mConfig.setRssContinueTime(nCurrVal);
                           }
                       }


                       if(strItem.equals("time"))	{

                           //set dsp time fontsize 999
                           if(strSubItem.equals("fontsize")) {

                               int nCurrVal = Integer.valueOf(strValue);

                               mConfig.setTimeDisplayFontSize(nCurrVal);
                           }

                           //set dsp time font dotum
                           if(strSubItem.equals("font")) {

                               int nCurrVal = Integer.valueOf(strValue);
                               mConfig.setTimeDisplayFont(nCurrVal);
                           }

                           //set dsp time userfont gulim
                           if(strSubItem.equals("userfont")) {

                               int nCurrVal = Integer.valueOf(strValue);
                               mConfig.setTimeDisplayFont(nCurrVal);
                           }

                           //set dsp time color orange
                           if(strSubItem.equals("color")) {

                               int nCurrVal = Integer.valueOf(strValue);

                               mConfig.setTimeDisplayColor(nCurrVal);
                           }

                           //set dsp time bgcolor deepblue
                           if(strSubItem.equals("bgcolor")) {


                               int nCurrVal = Integer.valueOf(strValue);
                               mConfig.setTimeDisplayBackColor(nCurrVal);
                           }

                           //set dsp time display lefttop
                           if(strSubItem.equals("display")) {

                               int nCurrVal = Integer.valueOf(strValue);
                               mConfig.setTimeDisplayPosition(nCurrVal);
                           }

                           //set dsp time zone -9
                           if(strSubItem.equals("zone")) {
                               // PASS DSPCONFIG!.
                           }

                           //set dsp time ntpinterval 999
                           if(strSubItem.equals("ntpinterval")) {
                               // PASS DSPCONFIG!.
                           }

                           //set dsp time ntpserver server
                           if(strSubItem.equals("ntpserver")) {
                               // PASS DSPCONFIG!.
                           }

                           //set dsp time ntpserver disable
                           if(strSubItem.equals("ntpserver")) {
                               // PASS DSPCONFIG!.
                           }

                           if(strSubItem.equals("status") )	{

                               int nCurrVal = 0;	// if(strValue.equals("on"))

                               if(strValue.equals("off")) nCurrVal = 1;

                               mConfig.setAutoOnOffMode(nCurrVal);
                           }

                           if(strSubItem.equals("off") ) 	{

                               String strCurrVal = strValue.replace(":", "");

                               mConfig.setOffTime(strCurrVal);

                           }

                           if(strSubItem.equals("on") ) 	{

                               String strCurrVal = strValue.replace(":", "");

                               mConfig.setOnTime(strCurrVal);
                           }

                       }

                       if(strItem.equals("adv"))	{

                           //set dsp adv language korean
                           if(strSubItem.equals("language")) {
                               // PASS DSPCONFIG!.
                           }
                       }

                       if(strItem.equals("interactive"))	{
                           //set dsp interactive iamode standby
                           if(strSubItem.equals("iamode")) {

                           }
                           // PASS DSPCONFIG!.
                       }

                       if(strItem.equals("sync"))	{
                           //set dsp sync status on
                           if(strSubItem.equals("status")) {

                           }
                           // PASS DSPCONFIG!.
                       }

                       if(strItem.equals("dtv"))	{
                           //set dsp dtv type 8vsb
                           if(strSubItem.equals("type")) {

                           }
                           // PASS DSPCONFIG!.
                       }

                   }

               }

           }

           br.close();

       } catch (IOException e) {
            e.printStackTrace();
       }


    }


    public ConfigEntity getConfig() {
        return mConfig;
    }
}
