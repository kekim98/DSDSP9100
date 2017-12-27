package com.dignsys.dsdsp.dsdsp_9100.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.os.Debug;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.bumptech.glide.request.FutureTarget;
import com.dignsys.dsdsp.dsdsp_9100.Definer;
import com.dignsys.dsdsp.dsdsp_9100.GlideApp;
import com.dignsys.dsdsp.dsdsp_9100.util.ConnectivityUtils;
import com.dignsys.dsdsp.dsdsp_9100.util.DaulUtils;
import com.dignsys.dsdsp.dsdsp_9100.viewmodel.CommandHelper;
import com.dignsys.dsdsp.dsdsp_9100.viewmodel.ConfigHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class LogService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_LOG_UPLOAD = "com.dignsys.dsdsp.service.action. ACTION_LOG_UPLOAD";
    private static final String ACTION_LIVE_STATE_UPLOAD = "com.dignsys.dsdsp.service.action.ACTION_LIVE_STATE_UPLOAD";
    private static final String ACTION_LIVE_SCREEN_UPLOAD = "com.dignsys.dsdsp.service.action.ACTION_LIVE_SCREEN_UPLOAD";



    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.dignsys.dsdsp.service.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.dignsys.dsdsp.service.extra.PARAM2";
    private static final String TAG = LogService.class.getSimpleName();

    public LogService() {
        super("SyncService");
    }

    /**
     * Starts this service to perform action DOWNLOAD with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startLogUpload(Context context, String param1, String param2) {

        if(ConfigHelper.getInstance(context).getServerMode()
                == Definer.DEF_CFG_ITEM_VALUE_STANDALONE) return;

        Log.d(TAG, "startDownload: ..............................");
        Intent intent = new Intent(context, LogService.class);
        intent.setAction(ACTION_LOG_UPLOAD);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }


    public static void startLiveStateUpload(Context context, String param1, String param2) {
        Log.d(TAG, "startDownload: ..............................");
        if(ConfigHelper.getInstance(context).getEnableLive()
                == Definer.DEF_FALSE) return;

        Intent intent = new Intent(context, LogService.class);
        intent.setAction(ACTION_LIVE_STATE_UPLOAD);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);

    }

    public static void startLiveScreenUpload(Context context, String param1, String param2) {
        Log.d(TAG, "startDownload: ..............................");
        if(ConfigHelper.getInstance(context).getEnableCapture()
                == Definer.DEF_FALSE) return;

        Intent intent = new Intent(context, LogService.class);
        intent.setAction(ACTION_LIVE_SCREEN_UPLOAD);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);

    }




    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent: xxxxxxxxxxxxxxxxxxxxxxxx");
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_LOG_UPLOAD.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionLogUpload(param1, param2);
            }
            if (ACTION_LIVE_STATE_UPLOAD.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionLiveStateUpload(param1, param2);
            }
            if (ACTION_LIVE_SCREEN_UPLOAD.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionLiveScreenUpload(param1, param2);
            }
        }
    }

    private void handleActionLiveScreenUpload(String param1, String param2) {
        Log.d(TAG, "handleActionLiveScreenUpload: ");
        if(!ConnectivityUtils.isConnected(this.getApplicationContext())) return;
        float capScale = (float) (ConfigHelper.getInstance(this.getApplicationContext()).getCaptureScale()/100.0);
        if(capScale <= 0.0) return;

        String file = "/mnt/external_sd/sc.png";
        String cmd = "screencap -p " + file;

        try {
            Process p = Runtime.getRuntime().exec(cmd);
            p.waitFor();

            if (p.exitValue() != 0) {
               return;
            }

            DisplayMetrics displayMetrics = new DisplayMetrics();
            WindowManager wm = (WindowManager) this.getApplication().getSystemService(WINDOW_SERVICE);
            wm.getDefaultDisplay().getRealMetrics(displayMetrics);

            int width = (int) (displayMetrics.widthPixels * capScale);
            int height = (int) (displayMetrics.heightPixels * capScale);

            FutureTarget<Bitmap> futureTarget =
                    GlideApp.with(this.getApplicationContext())
                            .asBitmap()
                            .load("file:///"+file)
                            .submit(width, height);

            Bitmap bitmap = futureTarget.get();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
            final byte[] bitmapdata = stream.toByteArray();

            uploadScreen(bitmapdata);

            GlideApp.with(this.getApplicationContext()).clear(futureTarget);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void uploadScreen(byte[] bitmapdata) {

        try {
            ConfigHelper DSLibIF = ConfigHelper.getInstance(this.getApplicationContext());
            String strServerForlder = DSLibIF.getServerFolder();
            if(TextUtils.isEmpty(strServerForlder)) return;


           // String strBoundary = "*****";
            String fileName = "dspcapture_" + DaulUtils.getLocalMACAddress() + ".jpg";

            String strCaptureURL = String.format("http://%s:%s/%s", DSLibIF.getServerAddr(), DSLibIF.getServerPort(), Definer.DEF_WEB_IF_CAPTURE_PATH);
            String strSiteName 		= strServerForlder.substring(strServerForlder.lastIndexOf("/") + 1);
            URL url = new URL(strCaptureURL);

            OkHttpClient client = new OkHttpClient();

            RequestBody requestBody = new  MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("Content-Disposition", "form-data; name=\"SITE_NAME\"" + strSiteName)
                    .addFormDataPart("Content-Disposition", "form-data; name=\"Filedata\";filename=\"" + fileName
                            , RequestBody.create(MediaType.parse("image/png"), bitmapdata))
                    .build();

            final Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                Log.d(TAG, "uploadScreen: fail.......");
            }
            Log.d(TAG, "uploadScreen: success....");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void handleActionLiveStateUpload(String param1, String param2) {
        Log.d(TAG, "handleActionLiveStateUpload: ");
        if(!ConnectivityUtils.isConnected(this.getApplicationContext())) return;

        try {
            ConfigHelper DSLibIF = ConfigHelper.getInstance(this.getApplicationContext());
            String strMacAddress = DaulUtils.getLocalMACAddress();
            String strFWVersion  	= DSLibIF.getFirmwareVersion().substring(DSLibIF.getFirmwareVersion().indexOf("_") + 1 );
            String strSDTotalSize		= DaulUtils.getSizeString(DaulUtils.getTotalSDSize(Definer.DEF_SD_PATH));
            String strSDAvailableSize	= DaulUtils.getSizeString(DaulUtils.getAvailableSDSize(Definer.DEF_SD_PATH));

            String strServerForlder = DSLibIF.getServerFolder();
            String strSiteName 		= strServerForlder.substring(strServerForlder.lastIndexOf("/") + 1);

            int status	= CommandHelper.getInstance(this.getApplicationContext()).get_command();
            String strDisplaySataus = "On";
            if (status == Definer.DEF_SLEEP_IN_COMMAND || status == Definer.DEF_REBOOT_COMMAND) {
                strDisplaySataus = "Off";
            }

            AudioManager am = (AudioManager)this.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            int curVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);

            String strLiveURL = String.format("http://%s:%s/%s", DSLibIF.getServerAddr(), DSLibIF.getServerPort(), Definer.DEF_WEB_IF_LIVE_PATH);
            URL url = new URL(strLiveURL);


            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .add("SITE_NAME", strSiteName)
                    .add("MAC_ADDR", strMacAddress)
                    .add("FW_VER", strFWVersion)
                    .add("TDS", strSDTotalSize)
                    .add("ADS", strSDAvailableSize)
                    .add("DS",  strDisplaySataus)
                    .add("TMS", String.valueOf(Runtime.getRuntime().maxMemory()))
                    .add("AMS", String.valueOf(Debug.getNativeHeapAllocatedSize()))
                    .add("VOLS", String.valueOf(curVolume))
                    .build();

            final Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                Log.d(TAG, "LiveStateUpload: fail.......");
            }
            Log.d(TAG, "LiveStateUpload: success....");

        }
        catch (Exception e) {
            e.printStackTrace();
        }



    }

    private void handleActionLogUpload(String param1, String param2) {
        // TODO: Handle action Download
        Log.d(TAG, "handleActionLogUpload: ");

    }



}
