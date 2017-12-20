package com.dignsys.dsdsp.dsdsp_9100.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dignsys.dsdsp.dsdsp_9100.R;
import com.dignsys.dsdsp.dsdsp_9100.util.DaulUtils;

import java.io.File;
import java.util.Arrays;

public class DlgSelectFW extends Dialog implements OnItemClickListener, View.OnClickListener {

	private OnDismissListener mDismissListener	= null;
	
	private Context mContext	= null;
	
	private String m_strPath 	= "";
	private String m_strFWVersion	= "";
	private String m_strFWFilePath	= "";
	private View mView;

	public DlgSelectFW(Context context, OnDismissListener listener, String strPath, String strFWVer) {
		super(context);
		
		mContext			= context;
		mDismissListener 	= listener;
		m_strPath			= strPath;
		m_strFWVersion		= strFWVer;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setCanceledOnTouchOutside(false);
		
		setContentView(R.layout.dlg_filelist);

		((Button)findViewById(R.id.dlgFileList_btnOK)).setOnClickListener(this);
		((Button)findViewById(R.id.dlgFileList_btnOK)).setText(mContext.getText(R.string.def_cancel));

		initList();

		super.onCreate(savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.dlgFileList_btnOK)	{
			this.dismiss();
		}
	}
	
	private void initList()
	{
		ListView lv = (ListView)findViewById(R.id.dlgFileList_lvFiles);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1);
		
		File f = new File(m_strPath);
        File[] files = f.listFiles();
        
        if(files != null && files.length > 0)	{
            Arrays.sort(files);
            
            for (int i = 0; i < files.length; i++) {
            	
                File file = files[i];
                
                try  {
                	String str = new String(file.getName());

                	if (!file.isDirectory() && DaulUtils.getExtension(str).equals("bin")) adapter.add(str);

    			} 
                catch (Exception e)  {
    				e.printStackTrace();
    			}

            }
            
            lv.setAdapter(adapter);
    		lv.setOnItemClickListener(this);
    	}
        	
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		
		String strFileName 	= ((TextView)v).getText().toString();
		String strFWVersion	= strFileName.substring(strFileName.indexOf("_") + 1, strFileName.lastIndexOf("."));
				
		if(!m_strFWVersion.equals(strFWVersion))	{
			m_strFWFilePath = m_strPath + File.separator + strFileName;
			mDismissListener.onDismiss(this);
		}
		else Toast.makeText( mContext, mContext.getText(R.string.msg_ugrade_same_version), Toast.LENGTH_SHORT ).show();
	}
	
	public String getFWFilePath()	{ return m_strFWFilePath;	}

}

