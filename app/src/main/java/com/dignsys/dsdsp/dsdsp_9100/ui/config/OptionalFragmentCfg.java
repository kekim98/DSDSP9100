package com.dignsys.dsdsp.dsdsp_9100.ui.config;

import android.arch.lifecycle.ViewModelProviders;
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

import com.dignsys.dsdsp.dsdsp_9100.Definer;
import com.dignsys.dsdsp.dsdsp_9100.R;
import com.dignsys.dsdsp.dsdsp_9100.util.DaulUtils;
import com.dignsys.dsdsp.dsdsp_9100.viewmodel.ConfigHelper;
import com.dignsys.dsdsp.dsdsp_9100.viewmodel.MainViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link OptionalFragmentCfg#newInstance} factory method to
 * create an instance of this fragment.
 */


public class OptionalFragmentCfg extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PANE_NUM = "pane_num";
    private static final String TAG = OptionalFragmentCfg.class.getSimpleName();

    private View mView;
    private MainViewModel mViewModel;
    private ConfigHelper DSLibIF;

    public OptionalFragmentCfg() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pane_num Parameter test.
     * @return A new instance of fragment VideoFragmentMain.
     */

    static OptionalFragmentCfg newInstance(int pane_num) {
        OptionalFragmentCfg fragment = new OptionalFragmentCfg();
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
      //  DSCommanIF = mViewModel.getCommandHelper();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.cfg_optional, container, false);
        return mView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((EditText)mView.findViewById(R.id.cfgValue_etSOLogSaveDay)).setText(String.valueOf(DSLibIF.getSOLogSaveDay()));

        Spinner spSOSD		= mView.findViewById(R.id.cfgAO_spSOSD);
        Spinner spSOLogSave	= mView.findViewById(R.id.cfgAO_spSOLog);

        ArrayAdapter<CharSequence> adpSOSD	 	= ArrayAdapter.createFromResource(this.getContext(), R.array.spinnerDefUseNotUse, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adpSOLogSave	= ArrayAdapter.createFromResource(this.getContext(), R.array.spinnerDefUseNotUse, android.R.layout.simple_spinner_item);

        spSOSD.setAdapter(adpSOSD);
        spSOLogSave.setAdapter(adpSOLogSave);


        spSOSD.setSelection(DSLibIF.getSOSD());
        spSOLogSave.setSelection(DSLibIF.getSOLogSave());

        Button btnApply = mView.findViewById(R.id.cfgAO_btnApply);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DSLibIF.setSOLogSaveDay(Integer.parseInt(((EditText)mView.findViewById(R.id.cfgValue_etSOLogSaveDay)).getText().toString()));
                DSLibIF.setSOSD(((Spinner)mView.findViewById(R.id.cfgAO_spSOSD)).getSelectedItemPosition());
                DSLibIF.setSOLogSave(((Spinner)mView.findViewById(R.id.cfgAO_spSOLog)).getSelectedItemPosition());

                /**
                 * 20170626. by BLEUAILE
                 * SD 저장일이 0이면 사용하지 않는 것으로 수정.
                 */
                if(DSLibIF.getSOLogSaveDay() == 0) {
                    DSLibIF.setSOLogSave(Definer.DEF_CFG_ITEM_VALUE_NOT_USE);
                    ((Spinner)mView.findViewById(R.id.cfgAO_spSOLog)).setSelection(Definer.DEF_CFG_ITEM_VALUE_NOT_USE);
                }

                Toast.makeText(OptionalFragmentCfg.this.getContext(), R.string.ca_msg_apply_config, Toast.LENGTH_SHORT).show();
            }
        });

    }



}
