/**
 * Created by bawoori on 17. 11. 16.
 */

package com.dignsys.dsdsp.dsdsp_9100.db;



import com.dignsys.dsdsp.dsdsp_9100.db.entity.ConfigEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.DspPlayListEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.DspFormatEntity;
import com.dignsys.dsdsp.dsdsp_9100.model.DspFormat;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.dignsys.dsdsp.dsdsp_9100.Definer.DEF_BOARD_ID;
import static com.dignsys.dsdsp.dsdsp_9100.Definer.DEF_COMPANY_ID;
import static com.dignsys.dsdsp.dsdsp_9100.Definer.DEF_CONFIG_VERSION;
import static com.dignsys.dsdsp.dsdsp_9100.Definer.DEF_MODEL_ID;
import static com.dignsys.dsdsp.dsdsp_9100.Definer.DEF_VERSION;

/** Generates dummy data and inserts them into the database */
class DatabaseInitUtil {

    private static final String[] FIRST = new String[]{
            "Special edition", "New", "Cheap", "Quality", "Used"};
    private static final String[] SECOND = new String[]{
            "Three-headed Monkey", "Rubber Chicken", "Pint of Grog", "Monocle"};
    private static final String[] DESCRIPTION = new String[]{
            "is finally here", "is recommended by Stan S. Stanman",
            "is the best sold product on Mêlée Island", "is \uD83D\uDCAF", "is ❤️", "is fine"};
    private static final String[] COMMENTS = new String[]{
            "Comment 1", "Comment 2", "Comment 3", "Comment 4", "Comment 5", "Comment 6",
    };

    static void initializeDb(AppDatabase db) {
       /* List<DspFormatEntity> products = new ArrayList<>(FIRST.length * SECOND.length);
        List<DspPlayListEntity> comments = new ArrayList<>();

        generateData(products, comments);

        insertData(db, products, comments);*/

        ConfigEntity configEntity = new ConfigEntity();

        generateConfig(configEntity);
        insertConfigData(db, configEntity);
       
    }

    private static void insertConfigData(AppDatabase db, ConfigEntity configEntity) {
        db.beginTransaction();
        try {
            db.configDao().insertOne(configEntity);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private static void generateConfig(ConfigEntity configEntity) {
        configEntity.setConfigVersion(DEF_CONFIG_VERSION);
        //configEntity.FirmwareVersion, DEF_FW_VERSION;
        configEntity.setFirmwareVersion(DEF_BOARD_ID + DEF_MODEL_ID + DEF_COMPANY_ID + "_" + DEF_VERSION);
        configEntity.setDeviceId("00000000");

        configEntity.setDSPMode(0);

        configEntity.setBacklight(16);
        configEntity.setVideoOutput(2);	// Digital A/V 720p
        configEntity.setAudioVolume(16);

        configEntity.setServerAddress("211.33.40.40");
        configEntity.setServerFolder("site/");

        configEntity.setServerMode(0);
        configEntity.setServerPort(80);
        configEntity.setServerSyncInterval(60);

        configEntity.setUseLAN(1);
        configEntity.setUseLAN_DHCP(1);

        configEntity.setLAN_IP("192.168.1.71");
        configEntity.setLAN_Subnet("255.255.255.0");
        configEntity.setLAN_Gateway("192.168.1.1");
        configEntity.setLAN_DNS("192.168.1.1");

        configEntity.setUseWifi_DHCP(1);

        configEntity.setWifi_IP("192.168.1.71");
        configEntity.setWifi_Subnet("255.255.255.0");
        configEntity.setWifi_Gateway("192.168.1.1");
        configEntity.setWifi_DNS("192.168.1.1");
        configEntity.setWifi_ESSID("dsp");
        configEntity.setWifi_PWD("1234");

        configEntity.setImageChangeEffect(1);	// 0: Random, 1: Not used.
        configEntity.setImageChangeInterval(3);
        configEntity.setUseAutoResizeMovie(1);
        configEntity.setUseAutoResizeImage(1);

        configEntity.setCapMode(0);
        configEntity.setCapPosition(1);
        configEntity.setCapColor(1);
        configEntity.setCapBackColor(6);
        configEntity.setCapSpeed(1);
        configEntity.setCapFont(0);
        configEntity.setCapSize(50);
        configEntity.setRSSMode(1);
        configEntity.setRSSUpdateInterval(1800);

        configEntity.setRSSAddress("http://rss.com");

        configEntity.setTimezone(9);
        configEntity.setTimeDisplayPosition(0);
        configEntity.setTimeDisplayColor(7);
        configEntity.setTimeDisplayBackColor(2);
        configEntity.setTimeDisplayFont(0);
        configEntity.setTimeDisplayFontSize(20);

        configEntity.setOnTime("0900");
        configEntity.setOffTime("1800");

        configEntity.setIA_SerialBaud(0);
        configEntity.setIA_SerialData(3);
        configEntity.setIA_SerialParity(0);
        configEntity.setIA_SerialStop(0);

        configEntity.setIA_NetIP("192.168.1.73");
        configEntity.setIA_NetPort(9300);
        configEntity.setMS_ServerIP("192.168.1.72");
        configEntity.setMS_UserCount(2);


        configEntity.setDTV_Channel("14");

        configEntity.setAutoOnOffMode(1);

        configEntity.setUseDefaultContents(0);
        configEntity.setUseSDLogSave(0);
        configEntity.setLogFileSaveDay(0);

        configEntity.setEnableLiveUpload(1);
        configEntity.setEnableLogUpload(1);
        configEntity.setEnableCaptureUpload(1);
        configEntity.setIntervalLive(60);
        configEntity.setIntervalLog(60);
        configEntity.setIntervalCapture(60);

        configEntity.setCaptureScale(30);
    }

    private static void generateData(List<DspFormatEntity> products, List<DspPlayListEntity> comments) {
        Random rnd = new Random();
        for (int i = 0; i < FIRST.length; i++) {
            for (int j = 0; j < SECOND.length; j++) {
                DspFormatEntity product = new DspFormatEntity();
                product.setName(FIRST[i] + " " + SECOND[j]);
                product.setDescription(product.getName() + " " + DESCRIPTION[j]);
                product.setPrice(rnd.nextInt(240));
                product.setId(FIRST.length * i + j + 1);
                products.add(product);
            }
        }

        for (DspFormat dspFormat : products) {
            int commentsNumber = rnd.nextInt(5) + 1;
            for (int i = 0; i < commentsNumber; i++) {
                DspPlayListEntity comment = new DspPlayListEntity();
                comment.setProductId(dspFormat.getId());
                comment.setText(COMMENTS[i] + " for " + dspFormat.getName());
                comment.setPostedAt(new Date(System.currentTimeMillis()
                        - TimeUnit.DAYS.toMillis(commentsNumber - i) + TimeUnit.HOURS.toMillis(i)));
                comments.add(comment);
            }
        }
    }

    private static void insertData(AppDatabase db, List<DspFormatEntity> products, List<DspPlayListEntity> comments) {
        db.beginTransaction();
        try {
            db.dspFormatDao().insertAll(products);
            db.dspPlayListDao().insertAll(comments);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
}
