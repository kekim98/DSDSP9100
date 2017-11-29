package com.dignsys.dsdsp.dsdsp_9100.ui.main;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import com.dignsys.dsdsp.dsdsp_9100.R;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ContentEntity;
import com.dignsys.dsdsp.dsdsp_9100.viewmodel.ScheduleViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VideoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VideoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PANE_NUM = "pane_num";
    private static final String TAG = VideoFragment.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private int mPaneNum;

    private OnFragmentInteractionListener mListener;
    private VideoView mVideoView;
    private ViewDataBinding mBinding;
    private ContentEntity mContent;
    private ScheduleViewModel viewModel;

    public VideoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pane_num Parameter test.
     * @return A new instance of fragment VideoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VideoFragment newInstance(int pane_num) {
        VideoFragment fragment = new VideoFragment();
        Bundle args = new Bundle();
        args.putInt(PANE_NUM, pane_num);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPaneNum = getArguments().getInt(PANE_NUM);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate this data binding layout
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_video, container, false);

        // Create and set the adapter for the RecyclerView.
     //   mCommentAdapter = new CommentAdapter(mCommentClickCallback);
     //   mBinding.commentList.setAdapter(mCommentAdapter);
        return mBinding.getRoot();


        // Inflate the layout for this fragment
      //  return inflater.inflate(R.layout.fragment_video, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       /* *//*ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = 960;
        layoutParams.height = 1080;

        view.setLayoutParams(layoutParams);
        view.setX(mPaneNum);
        view.setY(mIsMain);*//*

        mVideoView = view.findViewById(R.id.videoView);
        String UrlPath="android.resource://"+getActivity().getPackageName()+"/"+R.raw.kkk;
        mVideoView.setVideoURI(Uri.parse(UrlPath));
        mVideoView.start();*/


       /* ProductViewModel.Factory factory = new ProductViewModel.Factory(
                getActivity().getApplication(), getArguments().getInt(KEY_PRODUCT_ID));*/


        /*final ScheduleViewModel */viewModel =
                ViewModelProviders.of(this).get(ScheduleViewModel.class);

        subscribe(viewModel);
    }

    private void subscribe(ScheduleViewModel viewModel) {
        // Update the list when the data changes

        mContent = viewModel.getContent(mPaneNum); //for first content

        viewModel.getContentPlayDone().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer pane_num) {
                Log.d(TAG, "onChanged: getScheduleDone pane_num ="  + Integer.valueOf(pane_num));

                if (pane_num == mPaneNum) {
                    mContent = VideoFragment.this.viewModel.getContent(mPaneNum);

                    /*if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                        ((MainActivity) getActivity()).paneScheduleDone(mPaneNum);
                    }*/
                }
            }
        });

    }




    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

   /* @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
