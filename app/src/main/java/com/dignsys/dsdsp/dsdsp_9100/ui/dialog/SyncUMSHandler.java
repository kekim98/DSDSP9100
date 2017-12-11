package com.dignsys.dsdsp.dsdsp_9100.ui.dialog;

import android.os.Handler;
import android.os.Message;


import com.dignsys.dsdsp.dsdsp_9100.Definer;
import com.dignsys.dsdsp.dsdsp_9100.R;

import java.lang.ref.WeakReference;

public class SyncUMSHandler extends Handler {
	

	private final WeakReference<DlgSyncUMS> mParent;

	public SyncUMSHandler(DlgSyncUMS dlg)
	{
		mParent = new WeakReference<DlgSyncUMS>(dlg);
	}
	
	@Override
	public void handleMessage(Message msg)
	{
		/*DlgSyncUMS dlg = mParent.get();
		
		if(dlg != null)	{
			
			if(msg.what == Definer.DEF_MSG_ID_UMS_SYNC_DELETE)	{
				dlg.mTV.setText(dlg.mContext.getString(R.string.msg_ums_sync_dialog_delete));
			}
			
			if(msg.what == Definer.DEF_MSG_ID_UMS_SYNC_RUNNING)	{
				String strFileName 	= msg.getData().getString("FileName");
				dlg.mTV.setText(dlg.mContext.getString(R.string.msg_ums_sync_dialog_running, strFileName));				
			}
			
			if(msg.what == Definer.DEF_MSG_ID_UMS_SYNC_COMPLETED)	{
				dlg.completedSync();
			}
			
		}
		
		super.handleMessage(msg);*/
				
	}
	
	
}
