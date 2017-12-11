package com.dignsys.dsdsp.dsdsp_9100.ui.config;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.dignsys.dsdsp.dsdsp_9100.Definer;
import com.dignsys.dsdsp.dsdsp_9100.R;
import com.dignsys.dsdsp.dsdsp_9100.ui.dialog.DlgConfirm;


public class ConfigActivity extends AppCompatActivity implements DialogInterface.OnDismissListener{

    private int mFragmentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        GeneralFragmentCfg generalFragmentCfg =
                GeneralFragmentCfg.newInstance(0);

        replaceFragment(generalFragmentCfg);

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void onMenuItemClick(View v)
    {
        if(mFragmentID == v.getId()) return;

        mFragmentID = v.getId();

        if (mFragmentID == R.id.cfgBtnMI_General)	{

            GeneralFragmentCfg generalFragmentCfg =
                    GeneralFragmentCfg.newInstance(0);

            replaceFragment(generalFragmentCfg);
        }

        /*if (mFragmentID == R.id.cfgBtnMI_AV)	{
            Toast.makeText(this, "Advanced Fragment", Toast.LENGTH_SHORT).show();
        }*/

        if (mFragmentID == R.id.cfgBtnMI_Server)	{
          //  Toast.makeText(this, "Server Fragment", Toast.LENGTH_SHORT).show();

            ServerFragmentCfg serverFragmentCfg =
                    ServerFragmentCfg.newInstance(0);

            replaceFragment(serverFragmentCfg);
        }

        if (mFragmentID == R.id.cfgBtnMI_Lan)	{
            Toast.makeText(this, "Sorry LAN Config not yet implemented...", Toast.LENGTH_SHORT).show();

           /* Intent intent = new Intent();

            intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.SubSettings"));
            intent.putExtra(":settings:show_fragment", "com.android.settings.EthernetSettings");

            startActivity(intent);*/

        }

        if (mFragmentID == R.id.cfgBtnMI_Wifi)	{
            Intent intent= new Intent("android.settings.WIFI_SETTINGS");
            startActivity(intent);
        }

        if (mFragmentID == R.id.cfgBtnMI_Screen)	{
            //Toast.makeText(this, "Screen Fragment", Toast.LENGTH_SHORT).show();
            ScreenFragmentCfg screenFragmentCfg =
                    ScreenFragmentCfg.newInstance(0);

            replaceFragment(screenFragmentCfg);

        }

        if (mFragmentID == R.id.cfgBtnMI_Time)	{
           // Toast.makeText(this, "Time Fragment", Toast.LENGTH_SHORT).show();
            TimeFragmentCfg timeFragmentCfg =
                    TimeFragmentCfg.newInstance(0);

            replaceFragment(timeFragmentCfg);

        }

        if (mFragmentID == R.id.cfgBtnMI_Optional)	{
         //   Toast.makeText(this, "Optional Fragment", Toast.LENGTH_SHORT).show();
            OptionalFragmentCfg optionalFragmentCfg =
                    OptionalFragmentCfg.newInstance(0);

            replaceFragment(optionalFragmentCfg);
        }

       /* if (mFragmentID == R.id.cfgBtnMI_ChangeInitConfig)	{
            Toast.makeText(this, "ChangeInit Fragment", Toast.LENGTH_SHORT).show();

        }*/

        if (mFragmentID == R.id.cfgBtnMI_Reset)	{

            Toast.makeText(this, "Reset Fragment", Toast.LENGTH_SHORT).show();


            DlgConfirm dlg = new DlgConfirm(this, this, getString(R.string.msg_confirm_reset_config), Definer.DEF_CONFIRM_ID_RESET_CONFIG);

            dlg.show();

        }

        if (mFragmentID == R.id.cfgBtnMI_Exit)	{
            this.finish();
        }

    }

    private void replaceFragment(Fragment fragment) {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container,
                        fragment, null)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {

    }

    public static final void startConfigActivity(Context context)
    {

        Intent intentConfigActivity = new Intent(context, ConfigActivity.class);
        context.startActivity(intentConfigActivity);

    }
}
