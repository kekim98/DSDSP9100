package com.dignsys.dsdsp.dsdsp_9100.ui.config;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dignsys.dsdsp.dsdsp_9100.Definer;
import com.dignsys.dsdsp.dsdsp_9100.R;
import com.dignsys.dsdsp.dsdsp_9100.ui.dialog.DlgConfirm;
import com.dignsys.dsdsp.dsdsp_9100.ui.dialog.DlgFilelist;
import com.dignsys.dsdsp.dsdsp_9100.ui.dialog.DlgSelectFW;
import com.dignsys.dsdsp.dsdsp_9100.ui.dialog.DlgSyncUMS;
import com.dignsys.dsdsp.dsdsp_9100.util.DaulUtils;
import com.dignsys.dsdsp.dsdsp_9100.viewmodel.ConfigHelper;
import com.dignsys.dsdsp.dsdsp_9100.viewmodel.MainViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link GeneralFragmentCfg#newInstance} factory method to
 * create an instance of this fragment.
 */


public class GeneralFragmentCfg extends Fragment implements View.OnClickListener, DialogInterface.OnDismissListener{
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PANE_NUM = "pane_num";
    private static final String TAG = GeneralFragmentCfg.class.getSimpleName();
    private ConfigHelper DSLibIF ;
    private View mView;
    private String m_strVersion;
    private MainViewModel mViewModel;


    public GeneralFragmentCfg() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pane_num Parameter test.
     * @return A new instance of fragment VideoFragmentMain.
     */

    static GeneralFragmentCfg newInstance(int pane_num) {
        GeneralFragmentCfg fragment = new GeneralFragmentCfg();
        Bundle args = new Bundle();
        args.putInt(PANE_NUM, pane_num);
    //    fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        DSLibIF = mViewModel.getConfigHelper();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.cfg_general, container, false);

        mView.findViewById(R.id.cfgAG_btnUpgrade).setOnClickListener(this);
        mView.findViewById(R.id.cfgAG_btnSDFormat).setOnClickListener(this);
        mView.findViewById(R.id.cfgAG_btnAllDelete).setOnClickListener(this);
        mView.findViewById(R.id.cfgAG_btnUSBCopy).setOnClickListener(this);
        mView.findViewById(R.id.cfgAG_btnUSBSync).setOnClickListener(this);
        mView.findViewById(R.id.cfgAG_btnView).setOnClickListener(this);


        return mView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


       // String strDefaultFWVer 	= DSLibIF.getFirmwareVersion();
        String strBoardID 		= Definer.DEF_BOARD_ID;
        String strModelID 		= Definer.DEF_MODEL_ID;
        String m_strCompanyID 	= Definer.DEF_COMPANY_ID;
        String strVersion 		= Definer.DEF_VERSION;

      //  m_strCompanyID 	= DSFrameworkIF.getVendorCode(this);


        m_strVersion 	= strBoardID + strModelID + m_strCompanyID + "_" + strVersion;

        ((TextView)mView.findViewById(R.id.cfgValue_tvFV)).setText(m_strVersion);
        ((EditText)mView.findViewById(R.id.cfgValue_etDevice_ID)).setText(DSLibIF.getDeviceID());


        // TODO: Rename and change types and number of view

    }

    @Override
    public void onClick(View v)	{

        int nID = v.getId();

        if (nID == R.id.cfgAG_btnUpgrade)	{
            DlgSelectFW dlg = new DlgSelectFW(this.getContext(), this, Definer.DEF_UMS_PATH, m_strVersion);
            dlg.show();
        }

        if (nID == R.id.cfgAG_btnSDFormat)	{
			/*Intent intent= new Intent("android.settings.MEMORY_CARD_SETTINGS");
			startActivity(intent);*/
                DlgConfirm dlg = new DlgConfirm(this.getContext(), this, getString(R.string.msg_confirm_format_sdcard), Definer.DEF_CONFIRM_ID_FORMAT_SDCARD);
                dlg.show();

        }

        if (nID == R.id.cfgAG_btnView)	{
            DlgFilelist dlg = new DlgFilelist(this.getContext(), Definer.DEF_ROOT_PATH);
            dlg.show();
        }

        if (nID == R.id.cfgAG_btnAllDelete)	{

            DlgConfirm dlg = new DlgConfirm(this.getContext(), this, getString(R.string.msg_confirm_delete_all), Definer.DEF_CONFIRM_ID_DELETE_ALL);

            dlg.show();
        }

        if (nID == R.id.cfgAG_btnUSBSync)	{
            DlgConfirm dlg = new DlgConfirm(this.getContext(), this, getString(R.string.msg_confirm_usb_sync), Definer.DEF_CONFIRM_ID_USB_SYNC);

            dlg.show();
        }

        if (nID == R.id.cfgAG_btnUSBCopy)	{
            DlgConfirm dlg = new DlgConfirm(this.getContext(), this, getString(R.string.msg_confirm_usb_copy), Definer.DEF_CONFIRM_ID_USB_COPY);

            dlg.show();
        }


       /* if (nID == R.id.cfgAG_btnApply)	{
            applyGeneralConfig();
        }*/
    }

    private void applyGeneralConfig()
    {
        DSLibIF.setDeviceID(((EditText)mView.findViewById(R.id.cfgValue_etDevice_ID)).getText().toString());
     //   DSLibIF.applyConfig();

        DaulUtils.showMessageBox(this.getContext(), getString(R.string.ca_msg_apply_config), getString(R.string.def_yes));
    }


    @Override
    public void onDismiss(DialogInterface dialog) {

        String strClassName = dialog.getClass().getSimpleName().trim();

        if(strClassName.equals("DlgConfirm"))	{

            DlgConfirm dlg = (DlgConfirm)dialog;

            dlg.dismiss();

            if(dlg.getConfirmID() == Definer.DEF_CONFIRM_ID_DELETE_ALL)	{

                /*if(DSLibIF.getServerMode() == Definer.DEF_CFG_SERVER_MODE_INTERNET)	{
                    mServiceBinder.setWaitFlagForDownloader(true);
                    mServiceBinder.stopDownload();
                    DaulUtils.deleteFilesInDirectory(m_strRootPath);
                    mServiceBinder.startDownload(m_strDownladPath);
                }
                else DaulUtils.deleteFilesInDirectory(m_strRootPath);*/


                Toast.makeText(this.getContext(), this.getString(R.string.msg_completed_delete_all), Toast.LENGTH_SHORT).show();
            }


            if(dlg.getConfirmID() == Definer.DEF_CONFIRM_ID_USB_SYNC)	{

                /*if(DaulUtils.isUMSConnected(DSLibIF.getUMSPath()))	{

                    if(DSLibIF.getServerMode() == Definer.DEF_CFG_SERVER_MODE_INTERNET) mServiceBinder.stopDownload();

                    DlgSyncUMS dlgUMS = new DlgSyncUMS(this, this, Definer.DEF_USM_SYNC_OP_ID_SYNC);
                    dlgUMS.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
                    dlgUMS.setOnShowListener(new DialogInterface.OnShowListener() {

                        @Override
                        public void onShow(DialogInterface dialog) {
                            ((DlgSyncUMS)dialog).startSync();
                        }

                    });

                    dlgUMS.show();
                }
                else {
                    Toast.makeText(this.getContext(), this.getString(R.string.msg_not_detected_ums), Toast.LENGTH_SHORT).show();
                }*/
                Toast.makeText(this.getContext(), this.getString(R.string.msg_not_detected_ums), Toast.LENGTH_SHORT).show();

            }

            if(dlg.getConfirmID() == Definer.DEF_CONFIRM_ID_USB_COPY)	{


              /*  if(DaulUtils.isUMSConnected(DSLibIF.getUMSPath()))	{

                    if(DSLibIF.getServerMode() == Definer.DEF_CFG_SERVER_MODE_INTERNET) {
                        mServiceBinder.setWaitFlagForDownloader(true);
                        mServiceBinder.stopDownload();
                    }

                    DlgSyncUMS dlgUMS = new DlgSyncUMS(this.getContext(), this, Definer.DEF_USM_SYNC_OP_ID_COPY);
                dlgUMS.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
                dlgUMS.setOnShowListener(new DialogInterface.OnShowListener() {

                    @Override
                    public void onShow(DialogInterface dialog) {
                        ((DlgSyncUMS)dialog).startSync();
                    }

                });

                dlgUMS.show();
                }
                else {
                    Toast.makeText(this.getContext(), this.getString(R.string.msg_not_detected_ums), Toast.LENGTH_SHORT).show();
                }*/


                Toast.makeText(this.getContext(), this.getString(R.string.msg_not_detected_ums), Toast.LENGTH_SHORT).show();



            }



            if(dlg.getConfirmID() == Definer.DEF_CONFIRM_ID_FORMAT_SDCARD)	{
               /* mServiceBinder.setWaitFlagForDownloader(true);
                mServiceBinder.stopDownload();
                mServiceBinder.formatSDCard();
                mServiceBinder.startDownload(m_strDownladPath);*/
                Toast.makeText(this.getContext(), "format sd-card", Toast.LENGTH_SHORT).show();

            }


        }

        if(strClassName.equals("DlgSyncUMS"))	{
            DlgSyncUMS dlg = (DlgSyncUMS)dialog;

            dlg.dismiss();
/*
            DaulUtils.createFolder(m_strRootPath);
            DaulUtils.createFolder(m_strDownladPath);

            if(DSLibIF.getServerMode() == Definer.DEF_CFG_SERVER_MODE_INTERNET) mServiceBinder.startDownload(m_strDownladPath);*/

            Toast.makeText(this.getContext(), "DlgSyncUMS", Toast.LENGTH_SHORT).show();


        }

        if(strClassName.equals("DlgSelectFW"))	{

            DlgSelectFW dlg = (DlgSelectFW)dialog;

           /* File fileFW = new File(dlg.getFWFilePath());


            if(fileFW.exists()){

                String strDestFilePath = m_strRootPath + File.separator + DaulUtils.stripExtension(fileFW.getName()) + ".apk";

                DaulUtils.copyFile(fileFW , strDestFilePath);


                Intent update = new Intent(DS7000ServiceManager. ACTION_DS7000_INSTALL_REQ_PLAYER);
                update.putExtra(DS7000ServiceManager. EXTRA_PLAYER_INSTALL_PATH, strDestFilePath);
                sendBroadcast(update);

            }*/

            Toast.makeText(this.getContext(), "DlgSelectFW", Toast.LENGTH_SHORT).show();
            dlg.dismiss();
        }

    }


}
