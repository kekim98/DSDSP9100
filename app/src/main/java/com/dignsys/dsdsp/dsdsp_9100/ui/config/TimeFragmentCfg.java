package com.dignsys.dsdsp.dsdsp_9100.ui.config;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.dignsys.dsdsp.dsdsp_9100.R;
import com.dignsys.dsdsp.dsdsp_9100.util.DaulUtils;
import com.dignsys.dsdsp.dsdsp_9100.viewmodel.CommandHelper;
import com.dignsys.dsdsp.dsdsp_9100.viewmodel.ConfigHelper;
import com.dignsys.dsdsp.dsdsp_9100.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link TimeFragmentCfg#newInstance} factory method to
 * create an instance of this fragment.
 */


public class TimeFragmentCfg extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PANE_NUM = "pane_num";
    private static final String TAG = TimeFragmentCfg.class.getSimpleName();

    private View mView;
    private MainViewModel mViewModel;
    private ConfigHelper DSLibIF;
    private CommandHelper DSCommanIF;


    public TimeFragmentCfg() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pane_num Parameter test.
     * @return A new instance of fragment VideoFragmentMain.
     */

    static TimeFragmentCfg newInstance(int pane_num) {
        TimeFragmentCfg fragment = new TimeFragmentCfg();
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
        DSCommanIF = mViewModel.getCommandHelper();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.cfg_time, container, false);
        return mView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((EditText)mView.findViewById(R.id.cfgValue_etTimeFontSize)).setText(String.valueOf(DSLibIF.getTimeFontSize()));
        ((EditText)mView.findViewById(R.id.cfgValue_etAutoOnTime)).setText(DSLibIF.getAutoOnTime());
        ((EditText)mView.findViewById(R.id.cfgValue_etAutoOffTime)).setText(DSLibIF.getAutoOffTime());

        Spinner spTimeZone		= mView.findViewById(R.id.cfgAT_spTimeZone);
        Spinner spTimePosition	= mView.findViewById(R.id.cfgAT_spTimePosition);
        Spinner spTimeColor		= mView.findViewById(R.id.cfgAT_spTimeColor);
        Spinner spTimeBGColor	= mView.findViewById(R.id.cfgAT_spTimeBGColor);
        Spinner spTimeFont		= mView.findViewById(R.id.cfgAT_spTimeFont);
        Spinner spAutoOnOffMode	= mView.findViewById(R.id.cfgAT_spAutoOnOffMode);


        List<String> listFonts = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.spinnerCapFont)));

        if(!DSLibIF.getUserFont(1).isEmpty()) listFonts.add(DSLibIF.getUserFont(1));
        if(!DSLibIF.getUserFont(2).isEmpty()) listFonts.add(DSLibIF.getUserFont(2));
        if(!DSLibIF.getUserFont(3).isEmpty()) listFonts.add(DSLibIF.getUserFont(3));



        String[] idArray = TimeZone.getAvailableIDs();
        ArrayAdapter<String> adpTimeZone	    =  new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, idArray);
        adpTimeZone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTimeZone.setAdapter(adpTimeZone);
        String currTimeZone = TimeZone.getDefault().getID();
        for(int i = 0; i < idArray.length; i++) {
            if(idArray[i].equals(currTimeZone)) {
                spTimeZone.setSelection(i);
            }
        }


        ArrayAdapter<CharSequence> adpTimePosition 	= ArrayAdapter.createFromResource(this.getContext(), R.array.spinnerTimePosition, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adpTimeColor 	= ArrayAdapter.createFromResource(this.getContext(), R.array.spinnerTimeColor, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adpTimeBGColor 	= ArrayAdapter.createFromResource(this.getContext(), R.array.spinnerTimeBGColor, android.R.layout.simple_spinner_item);

        ArrayAdapter<String> adpTimeFont = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, listFonts);

        ArrayAdapter<CharSequence> adpAutoOnOffMode = ArrayAdapter.createFromResource(this.getContext(), R.array.spinnerDefUseNotUse, android.R.layout.simple_spinner_item);

        spTimePosition.setAdapter(adpTimePosition);
        spTimeColor.setAdapter(adpTimeColor);
        spTimeBGColor.setAdapter(adpTimeBGColor);
        spTimeFont.setAdapter(adpTimeFont);
        spAutoOnOffMode.setAdapter(adpAutoOnOffMode);


        spTimePosition.setSelection(DSLibIF.getTimePosition());
        spTimeColor.setSelection(DSLibIF.getTimeColor());
        spTimeBGColor.setSelection(DSLibIF.getTimeBGColor());
        spTimeFont.setSelection(DSLibIF.getTimeFont());
        spAutoOnOffMode.setSelection(DSLibIF.getAutoOnOffMode());

        mView.findViewById(R.id.cfgAT_btnSetTime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent("android.settings.DATE_SETTINGS");
                startActivity(intent);
            }
        });

        Button btnApply = mView.findViewById(R.id.cfgAT_btnApply);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int nFontSize = Integer.parseInt(((EditText)mView.findViewById(R.id.cfgValue_etTimeFontSize)).getText().toString());

                if(nFontSize < 4 || nFontSize > 256)	{
                    DaulUtils.showMessageBox(TimeFragmentCfg.this.getContext(), getString(R.string.ca_msg_invalid_fontsize), getString(R.string.def_yes));
                    return;
                }

                String timeZone = ((Spinner)mView.findViewById(R.id.cfgAT_spTimeZone)).getSelectedItem().toString();
                Log.d(TAG, "applyTimeConfig: " + timeZone);


                DSCommanIF.setTimeZone(timeZone);


                DSLibIF.setTimeFontSize(Integer.parseInt(((EditText)mView.findViewById(R.id.cfgValue_etTimeFontSize)).getText().toString()));
                DSLibIF.setAutoOnTime(((EditText)mView.findViewById(R.id.cfgValue_etAutoOnTime)).getText().toString());
                DSLibIF.setAutoOffTime(((EditText)mView.findViewById(R.id.cfgValue_etAutoOffTime)).getText().toString());

                DSLibIF.setTimePosition(((Spinner)mView.findViewById(R.id.cfgAT_spTimePosition)).getSelectedItemPosition());
                DSLibIF.setTimeColor(((Spinner)mView.findViewById(R.id.cfgAT_spTimeColor)).getSelectedItemPosition());
                DSLibIF.setTimeBGColor(((Spinner)mView.findViewById(R.id.cfgAT_spTimeBGColor)).getSelectedItemPosition());
                DSLibIF.setTimeFont(((Spinner)mView.findViewById(R.id.cfgAT_spTimeFont)).getSelectedItemPosition());
                DSLibIF.setAutoOnOffMode(((Spinner)mView.findViewById(R.id.cfgAT_spAutoOnOffMode)).getSelectedItemPosition());

                Toast.makeText(TimeFragmentCfg.this.getContext(), R.string.ca_msg_apply_config, Toast.LENGTH_SHORT).show();
            }
        });

    }



}
