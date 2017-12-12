package com.dignsys.dsdsp.dsdsp_9100.ui.config;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.dignsys.dsdsp.dsdsp_9100.R;
import com.dignsys.dsdsp.dsdsp_9100.util.DaulUtils;
import com.dignsys.dsdsp.dsdsp_9100.viewmodel.ConfigHelper;
import com.dignsys.dsdsp.dsdsp_9100.viewmodel.MainViewModel;

import static android.widget.Toast.LENGTH_SHORT;
import static com.dignsys.dsdsp.dsdsp_9100.R.string.ca_msg_apply_config;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ServerFragmentCfg#newInstance} factory method to
 * create an instance of this fragment.
 */


public class ServerFragmentCfg extends Fragment  {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PANE_NUM = "pane_num";
    private static final String TAG = ServerFragmentCfg.class.getSimpleName();
    private MainViewModel mViewModel;
    private ConfigHelper DSLibIF;
    private View mView;


    public ServerFragmentCfg() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pane_num Parameter test.
     * @return A new instance of fragment VideoFragmentMain.
     */

    static ServerFragmentCfg newInstance(int pane_num) {
        ServerFragmentCfg fragment = new ServerFragmentCfg();
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

        mView = inflater.inflate(R.layout.cfg_server, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final EditText serverAddr = mView.findViewById(R.id.cfgValue_etServerAddr);
        serverAddr.setText(DSLibIF.getServerAddr());

        final EditText serverFolder = mView.findViewById(R.id.cfgValue_etServerFolder);
        serverFolder.setText(DSLibIF.getServerFolder());

        final EditText serverPort = mView.findViewById(R.id.cfgValue_etServerPort);
        serverPort.setText(String.valueOf(DSLibIF.getServerPort()));

        final EditText serverSI = mView.findViewById(R.id.cfgValue_etServerSI);
        serverSI.setText(String.valueOf(DSLibIF.getServerSyncInterval()));

        final Spinner spServerMode = mView.findViewById(R.id.cfgASRV_spServerMode);
        ArrayAdapter<CharSequence> adpServerMode = ArrayAdapter.createFromResource(getContext(), R.array.spinnerServerMode, android.R.layout.simple_spinner_item);
        adpServerMode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spServerMode.setAdapter(adpServerMode);
        spServerMode.setSelection(DSLibIF.getServerMode());

        Button btnApply = mView.findViewById(R.id.cfgASRV_btnApply);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DSLibIF.setServerAddr(serverAddr.getText().toString());
                DSLibIF.setServerFolder(serverFolder.getText().toString());

                DSLibIF.setServerPort(serverPort.getText().toString());
                DSLibIF.setServerSyncInterval(Integer.valueOf(serverSI.getText().toString()));

                DSLibIF.setServerMode(spServerMode.getSelectedItemPosition());

                Toast.makeText(ServerFragmentCfg.this.getContext(), R.string.ca_msg_apply_config, Toast.LENGTH_SHORT).show();
            }
        });

    }


}
