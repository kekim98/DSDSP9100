package com.dignsys.dsdsp.dsdsp_9100.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.dignsys.dsdsp.dsdsp_9100.Definer;
import com.dignsys.dsdsp.dsdsp_9100.R;


public class DlgSyncUMS extends Dialog implements View.OnClickListener {

	private SyncUMSHandler 		mHandler 		= null;
	private SyncUMSThread 		mThread 		= null;
	private OnDismissListener m_odListener	= null;

	public Context mContext 	= null;
	public TextView mTV			= null;
	public Button mBtnOK		= null;

	private int	m_nOperationID			= 0;
	
	public DlgSyncUMS(Context context, OnDismissListener listener, int nOperationID ) {
		super(context);
		
		mContext 		= context;
		m_odListener 	= listener;
		
		m_nOperationID	= nOperationID;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.dlg_sync_ums);
		
		mTV			= (TextView)findViewById(R.id.dlgSU_tvMSG);
		mBtnOK		= (Button)findViewById(R.id.dlgSU_btnOK);
		mHandler 	= new SyncUMSHandler(this);
		mThread		= new SyncUMSThread(mHandler, m_nOperationID);
		
		if(m_nOperationID == Definer.DEF_USM_SYNC_OP_ID_COPY) mTV.setText(mContext.getString(R.string.msg_ums_sync_dialog_copy));
		
		mBtnOK.setOnClickListener(this);
				
		super.onCreate(savedInstanceState);
	}
	
	public void startSync()
	{
		mThread.start();		
	}
	
	public void completedSync()
	{
		mTV.setText(mContext.getString(R.string.msg_ums_sync_dialog_completed));	
		mBtnOK.setVisibility(View.VISIBLE);
		mBtnOK.requestFocus();
	}
	
	@Override
	public void onClick(View v) {

		if(v.getId() == R.id.dlgSU_btnOK)	{
			m_odListener.onDismiss(this);
		}
	}

}
