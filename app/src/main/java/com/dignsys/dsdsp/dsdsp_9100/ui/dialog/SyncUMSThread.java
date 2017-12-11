package com.dignsys.dsdsp.dsdsp_9100.ui.dialog;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


import com.dignsys.dsdsp.dsdsp_9100.Definer;

import java.io.File;

public class SyncUMSThread extends Thread {

	private Handler mHandler 		= null;

	private boolean 	mRunFlag 		= false;

	private int	m_nOperationID			= 0;
	
	public SyncUMSThread(Handler h, int nOperationID)
	{
	/*	mHandler 		= h;
		
		m_nOperationID 	= nOperationID;*/
	}
	
	public void run() 
	{
		/*if(m_nOperationID == Definer.DEF_USM_SYNC_OP_ID_SYNC) procSync();
		if(m_nOperationID == Definer.DEF_USM_SYNC_OP_ID_COPY) procCopy();*/
		
	}
	
	private void procSync()
	{
/*
		mRunFlag = true;
		
		while (mRunFlag) {

			try {
				
				File fRoot	= new File(DSLibIF.getRootPath());
				File fUMS	= new File(DSLibIF.getUMSPath());

				String strSRCs[] = fUMS.list();
										
				sendMessage(Definer.DEF_MSG_ID_UMS_SYNC_DELETE, "");	
				
				DaulUtils.deleteDirectory(fRoot);	
				fRoot.mkdirs();
				
				for (String strSRC: strSRCs) {
					
					File fSRC = new File(fUMS, strSRC);
					
					if(!fSRC.isDirectory())	{

						String strDest = fRoot.getAbsolutePath() + File.separator + fSRC.getName();
						
						sendMessage(Definer.DEF_MSG_ID_UMS_SYNC_RUNNING, fSRC.getName());
						
						DaulUtils.copyFile(fSRC, strDest);	

						Thread.sleep(100);
					}
									
				}
				
				sendMessage(Definer.DEF_MSG_ID_UMS_SYNC_COMPLETED, "");				
				
				mRunFlag = false;

				Thread.sleep(100);
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
*/
	}
	
	private void procCopy()
	{
/*
		mRunFlag = true;
		
		while (mRunFlag) {

			try {
				
				File fRoot	= new File(DSLibIF.getRootPath());
				File fUMS	= new File(DSLibIF.getUMSPath());

				String strSRCs[] = fUMS.list();
				
				for (String strSRC: strSRCs) {
					
					File fSRC = new File(fUMS, strSRC);
					
					if(!fSRC.isDirectory())	{

						String strDest = fRoot.getAbsolutePath() + File.separator + fSRC.getName();
						
						sendMessage(Definer.DEF_MSG_ID_UMS_SYNC_RUNNING, fSRC.getName());

						//TODO : DaulUtils
						//DaulUtils.copyFile(fSRC, strDest);

						Thread.sleep(100);
					}
									
				}
				
				sendMessage(Definer.DEF_MSG_ID_UMS_SYNC_COMPLETED, "");				
				
				mRunFlag = false;

				Thread.sleep(100);
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
*/
	}
	
	private void sendMessage(int nWhat, String strData)
	{
/*
		Message msg = mHandler.obtainMessage();

		msg.what = nWhat;
		
		if(nWhat == Definer.DEF_MSG_ID_UMS_SYNC_RUNNING)	{
			Bundle b 	= new Bundle();
			b.putString("FileName", strData);
			msg.setData(b);			
			mHandler.sendMessage(msg);	
		}
		else mHandler.sendMessage(msg); 
*/

	}

}
