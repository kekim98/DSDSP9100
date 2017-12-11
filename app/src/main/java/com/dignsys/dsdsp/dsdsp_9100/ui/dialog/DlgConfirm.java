package com.dignsys.dsdsp.dsdsp_9100.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.dignsys.dsdsp.dsdsp_9100.R;


public class DlgConfirm extends Dialog implements View.OnClickListener {
	
	private OnDismissListener mDismissListener	= null;

	private TextView mTVMessage	= null;
	
	private String m_strMessage 	= "";
	private int			m_nID			= 0;
	
	public DlgConfirm(Context context, OnDismissListener listener, String strMessage, int nID) {
		super(context);
		
		mDismissListener 	= listener;
		m_strMessage		= strMessage;
		m_nID				= nID;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.dlg_confirm);
		
		mTVMessage	= (TextView)findViewById(R.id.dlgConfirm_tvMSG);
		
		mTVMessage.setText(m_strMessage);
		
		((Button)findViewById(R.id.dlgConfirm_btnOK)).setOnClickListener(this);
		((Button)findViewById(R.id.dlgConfirm_btnCancel)).setOnClickListener(this);
				
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		
		int nID = v.getId();
		
		if(nID == R.id.dlgConfirm_btnOK) 		mDismissListener.onDismiss(this);
		if(nID == R.id.dlgConfirm_btnCancel) 	this.dismiss();
		
	}
	
	public int getConfirmID()	{ return m_nID;	}
	
}
