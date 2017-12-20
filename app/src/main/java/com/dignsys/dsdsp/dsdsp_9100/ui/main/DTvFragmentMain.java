package com.dignsys.dsdsp.dsdsp_9100.ui.main;

import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.dignsys.dsdsp.dsdsp_9100.R;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ConfigEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ContentEntity;

@SuppressWarnings("deprecation")
public class DTvFragmentMain extends MainBaseFragment implements SurfaceHolder.Callback {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PANE_NUM = "pane_num";
    private static final String TAG = DTvFragmentMain.class.getSimpleName();


    private ContentEntity mContent;
    private SurfaceView mSurfaceView;

    private Camera mCamera;
    private SurfaceHolder mHolder;


    public DTvFragmentMain() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pane_num Parameter test.
     * @return A new instance of fragment VideoFragmentMain.
     */
    public static DTvFragmentMain newInstance(int pane_num) {
        DTvFragmentMain fragment = new DTvFragmentMain();
        Bundle args = new Bundle();
        args.putInt(PANE_NUM, pane_num);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dtv, container, false);

        makeLayout(view);
        mSurfaceView = view.findViewById(R.id.surfaceView);

        // mSurfaceView.setZOrderMediaOverlay(true);
        mSurfaceView.setZOrderOnTop(true);

        mHolder = mSurfaceView.getHolder();
        mHolder.setFormat(PixelFormat.TRANSLUCENT);
        mHolder.addCallback(this);
        mHolder.setSizeFromLayout();

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        run();

    }

    @Override
    public void onResume() {
        super.onResume();

        // Use mCurrentCamera to select the camera desired to safely restore
        // the fragment after the camera has been changed
        mCamera = Camera.open();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mCamera != null)
        {
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        stop();

        try {
            this.finalize();
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    void stop() {
        //TODO:......
        if (mCamera != null)
        {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    protected void applyConfig(ConfigEntity config) {

    }

    @Override
    void run() {


    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

        if (mSurfaceView == null || mSurfaceView.getHolder() == null) return;

        if (mSurfaceView.getHolder().getSurface() == null) {
            // preview surface does not exist
            return;
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here
        Camera.Parameters param = mCamera.getParameters();

        param.setPreviewSize(1920, 1080);
        mCamera.setParameters(param);

        // start preview with new settings
        try {
            mCamera.setPreviewCallback(new Camera.PreviewCallback() {

                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {
                    // TODO Auto-generated method stub

                }
            });
            mCamera.setPreviewDisplay(mSurfaceView.getHolder());
            mCamera.startPreview();

        } catch (Exception e) {
            Log.d("camera",
                    "Error starting camera preview: " + e.getMessage());
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (mCamera != null)
        {
            mCamera.stopPreview();
            mCamera.release();
        }

    }
}
