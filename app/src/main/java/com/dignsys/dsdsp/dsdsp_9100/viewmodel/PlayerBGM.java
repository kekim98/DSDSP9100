package com.dignsys.dsdsp.dsdsp_9100.viewmodel;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;

import com.dignsys.dsdsp.dsdsp_9100.db.entity.ContentEntity;
import com.dignsys.dsdsp.dsdsp_9100.util.IOUtils;

import java.io.IOException;
import java.util.List;

public class PlayerBGM implements OnPreparedListener, OnCompletionListener, OnErrorListener {


	private final Context mCtx;
	private final boolean mIsBGMMode;
	private List<ContentEntity> mContentsList;
	
	private MediaPlayer mPlayer			= null;
	
	private boolean						mBGMMode		= false;
	
	private int							m_nIndex		= 0;
	
	public PlayerBGM(Context ctx, List<ContentEntity> cil, boolean mode) {
		super();

		mCtx = ctx;

		mPlayer = new MediaPlayer();
		mPlayer.setOnCompletionListener(this);
		mPlayer.setOnPreparedListener(this);

		mContentsList = cil;

		mIsBGMMode = mode;

	}	
	

	public void releasePlayer()
	{
		if (mPlayer != null) {
			mPlayer.release();
			mPlayer = null;
		}

	}
	
	public boolean isPlaying()
	{
		return mPlayer != null?true:false;
	}
	

	
	public void playBGM()
	{
		if(mContentsList.size() > 0)	{
			
			if(m_nIndex >= mContentsList.size() ) m_nIndex = 0;

			ContentEntity ci = mContentsList.get(m_nIndex);
			String path;
			if (mIsBGMMode) {
				path = IOUtils.getDspPlayContent(mCtx, ci.getFilePath());
			} else {
				path = IOUtils.getDspPlayContent(mCtx, ci.getOpBGMFile());
			}

			
			playBGM(path, ci.getOpRunTime());
		}
	}

	public void playBGM(String strPath, int nPlayTime)
	{
		try {
			mPlayer.reset();
			mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mPlayer.setDataSource(strPath);
			mPlayer.prepareAsync();
		}
		catch (IllegalArgumentException e) {
			e.printStackTrace();
		} 
		catch (SecurityException e) {
			e.printStackTrace();
		} 
		catch (IllegalStateException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		mp.start();		
	}
	
	@Override
	public void onCompletion(MediaPlayer mp) {
		
		if(mBGMMode){
			m_nIndex++;
			playBGM();
		}
		else {
			
		}
		
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		return false;
	}


}
