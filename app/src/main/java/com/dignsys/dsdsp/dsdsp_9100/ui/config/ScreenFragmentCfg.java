package com.dignsys.dsdsp.dsdsp_9100.ui.config;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ScreenFragmentCfg#newInstance} factory method to
 * create an instance of this fragment.
 */


public class ScreenFragmentCfg extends Fragment  {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PANE_NUM = "pane_num";
    private static final String TAG = ScreenFragmentCfg.class.getSimpleName();
    private View mView;
    private MainViewModel mViewModel;
    private ConfigHelper DSLibIF;
    private CommandHelper DSCommanIF;


    public ScreenFragmentCfg() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pane_num Parameter test.
     * @return A new instance of fragment VideoFragmentMain.
     */


    static ScreenFragmentCfg newInstance(int pane_num) {
        ScreenFragmentCfg fragment = new ScreenFragmentCfg();
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

        mView = inflater.inflate(R.layout.cfg_screen, container, false);
        return mView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<String> listFonts = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.spinnerCapFont)));

        if(!TextUtils.isEmpty(DSCommanIF.getUserFont(1))) listFonts.add(DSCommanIF.getUserFont(1));
        if(!TextUtils.isEmpty(DSCommanIF.getUserFont(2))) listFonts.add(DSCommanIF.getUserFont(2));
        if(!TextUtils.isEmpty(DSCommanIF.getUserFont(3))) listFonts.add(DSCommanIF.getUserFont(3));

        ArrayAdapter<String> adpCapFont = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, listFonts);
        ArrayAdapter<CharSequence> adpCapRSSMode	= ArrayAdapter.createFromResource(getContext(), R.array.spinnerCapRSSMode, android.R.layout.simple_spinner_item);

        final Spinner spScreenRotation= mView.findViewById(R.id.cfgAS_spScreenRotation);
        final Spinner spPicEffect		= mView.findViewById(R.id.cfgAS_spPicEffect);
        final Spinner spResizeMovie 	= mView.findViewById(R.id.cfgAS_spResizeMovie);
        final Spinner spResizePic 	= mView.findViewById(R.id.cfgAS_spResizePic);
        final Spinner spCapMode		= mView.findViewById(R.id.cfgAS_spCapMode);
        final Spinner spCapPosition	= mView.findViewById(R.id.cfgAS_spCapPosition);
        final Spinner spCapColor		= mView.findViewById(R.id.cfgAS_spCapColor);
        final Spinner spCapBGColor	= mView.findViewById(R.id.cfgAS_spCapBGColor);
        final Spinner spCapSpeed		= mView.findViewById(R.id.cfgAS_spCapSpeed);
        final Spinner spCapFont		= mView.findViewById(R.id.cfgAS_spCapFont);
        final Spinner spCapRSSMode	= mView.findViewById(R.id.cfgAS_spCapRSSMode);

        ArrayAdapter<CharSequence> adpScreenRotation 	= ArrayAdapter.createFromResource(getContext(), R.array.spinnerScreenRotation, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adpPicEffect 	= ArrayAdapter.createFromResource(getContext(), R.array.spinnerPicEffect, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adpResizeMovie 	= ArrayAdapter.createFromResource(getContext(), R.array.spinnerDefUseNotUse, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adpResizePic 	= ArrayAdapter.createFromResource(getContext(), R.array.spinnerDefUseNotUse, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adpCapMode		= ArrayAdapter.createFromResource(getContext(), R.array.spinnerCapMode, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adpCapPosition	= ArrayAdapter.createFromResource(getContext(), R.array.spinnerCapPosition, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adpCapColor		= ArrayAdapter.createFromResource(getContext(), R.array.spinnerCapColor, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adpCapBGColor	= ArrayAdapter.createFromResource(getContext(), R.array.spinnerCapBGColor, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adpCapSpeed		= ArrayAdapter.createFromResource(getContext(), R.array.spinnerCapSpeed, android.R.layout.simple_spinner_item);

        adpScreenRotation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adpPicEffect.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adpResizeMovie.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adpResizePic.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adpCapMode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adpCapPosition.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adpCapColor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adpCapBGColor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adpCapSpeed.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adpCapFont.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adpCapRSSMode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spScreenRotation.setAdapter(adpScreenRotation);
        spPicEffect.setAdapter(adpPicEffect);
        spResizeMovie.setAdapter(adpResizeMovie);
        spResizePic.setAdapter(adpResizePic);
        spCapMode.setAdapter(adpCapMode);
        spCapPosition.setAdapter(adpCapPosition);
        spCapColor.setAdapter(adpCapColor);
        spCapBGColor.setAdapter(adpCapBGColor);
        spCapSpeed.setAdapter(adpCapSpeed);
        spCapFont.setAdapter(adpCapFont);
        spCapRSSMode.setAdapter(adpCapRSSMode);


        Display display = ((WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        spScreenRotation.setSelection(display.getRotation());
        spPicEffect.setSelection(DSLibIF.getPicChangeEffect());
        spResizeMovie.setSelection(DSLibIF.getResizeMovie());
        spResizePic.setSelection(DSLibIF.getResizePic());
        spCapMode.setSelection(DSLibIF.getCapMode());
        spCapPosition.setSelection(DSLibIF.getCapPosition());
        spCapColor.setSelection(DSLibIF.getCapColor());
        spCapBGColor.setSelection(DSLibIF.getCapBGColor());
        spCapSpeed.setSelection(DSLibIF.getCapSpeed());
        spCapFont.setSelection(DSLibIF.getCapFont());
        spCapRSSMode.setSelection(DSLibIF.getCapRSSMode());

        ((EditText)mView.findViewById(R.id.cfgValue_etScreenPicTime)).setText(String.valueOf(DSLibIF.getPicChangeTime()));
        ((EditText)mView.findViewById(R.id.cfgValue_etScreenCapFontSize)).setText(String.valueOf(DSLibIF.getCapSize()));
        ((EditText)mView.findViewById(R.id.cfgValue_etScreenCapRSSUpdateTime)).setText(String.valueOf(DSLibIF.getCapRSSUpdateTime()));
        ((EditText)mView.findViewById(R.id.cfgValue_etScreenCapRSSAddress)).setText(DSLibIF.getCapRSSAddress());

        Button btnApply = mView.findViewById(R.id.cfgAS_btnApply);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int nFontSize = Integer.parseInt(((EditText)mView.findViewById(R.id.cfgValue_etScreenCapFontSize)).getText().toString());

                if(nFontSize < 4 || nFontSize > 256)	{
                    DaulUtils.showMessageBox(ScreenFragmentCfg.this.getContext(), getString(R.string.ca_msg_invalid_fontsize), getString(R.string.def_yes));
                    return;
                }

                int screenRotation = ((Spinner)mView.findViewById(R.id.cfgAS_spScreenRotation)).getSelectedItemPosition();
                Log.d(TAG, "applyScreenConfig: " + String.valueOf(screenRotation));

                DSCommanIF.setScreenRotation(screenRotation);


                DSLibIF.setPicChangeTime(Integer.parseInt(((EditText)mView.findViewById(R.id.cfgValue_etScreenPicTime)).getText().toString()));
                DSLibIF.setCapFontSize(Integer.parseInt(((EditText)mView.findViewById(R.id.cfgValue_etScreenCapFontSize)).getText().toString()));
                DSLibIF.setCapRSSUpdateTime(Integer.parseInt(((EditText)mView.findViewById(R.id.cfgValue_etScreenCapRSSUpdateTime)).getText().toString()));
                DSLibIF.setCapRSSAddress(((EditText)mView.findViewById(R.id.cfgValue_etScreenCapRSSAddress)).getText().toString());

                DSLibIF.setPicChangeEffect(((Spinner)mView.findViewById(R.id.cfgAS_spPicEffect)).getSelectedItemPosition());
                DSLibIF.setResizeMovie(((Spinner)mView.findViewById(R.id.cfgAS_spResizeMovie)).getSelectedItemPosition());
                DSLibIF.setResizePic(((Spinner)mView.findViewById(R.id.cfgAS_spResizePic)).getSelectedItemPosition());
                DSLibIF.setCapMode(((Spinner)mView.findViewById(R.id.cfgAS_spCapMode)).getSelectedItemPosition());
                DSLibIF.setCapPosition(((Spinner)mView.findViewById(R.id.cfgAS_spCapPosition)).getSelectedItemPosition());
                DSLibIF.setCapColor(((Spinner)mView.findViewById(R.id.cfgAS_spCapColor)).getSelectedItemPosition());
                DSLibIF.setCapBGColor(((Spinner)mView.findViewById(R.id.cfgAS_spCapBGColor)).getSelectedItemPosition());
                DSLibIF.setCapSpeed(((Spinner)mView.findViewById(R.id.cfgAS_spCapSpeed)).getSelectedItemPosition());
                DSLibIF.setCapFont(((Spinner)mView.findViewById(R.id.cfgAS_spCapFont)).getSelectedItemPosition());
                DSLibIF.setCapRSSMode(((Spinner)mView.findViewById(R.id.cfgAS_spCapRSSMode)).getSelectedItemPosition());

                Toast.makeText(ScreenFragmentCfg.this.getContext(), R.string.ca_msg_apply_config, Toast.LENGTH_SHORT).show();
                ConfigActivity.hideKeyboard(getActivity());
            }
        });

    }

}
