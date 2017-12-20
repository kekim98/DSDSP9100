package com.dignsys.dsdsp.dsdsp_9100.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.dignsys.dsdsp.dsdsp_9100.R;
import com.dignsys.dsdsp.dsdsp_9100.util.DaulUtils;

import java.io.File;
import java.util.Arrays;

public class DlgFilelist extends Dialog implements View.OnClickListener {

	private Context mContext	= null;
	
	private String m_strPath 	= "";
	
	public DlgFilelist(Context context, String strPath) {
		super(context);
		
		mContext	= context;
		m_strPath	= strPath;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setCanceledOnTouchOutside(false);
		
		setContentView(R.layout.dlg_filelist);
		
		((Button)findViewById(R.id.dlgFileList_btnOK)).setOnClickListener(this);

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
		ListView lv = findViewById(R.id.dlgFileList_lvFiles);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1);
		
		File f = new File(m_strPath);

		/**
		 * 2017-06-21 by BLEUAILE.
		 * SD Format 후 진입하면 dsdsp folder가 존재 하지 않으므로 Error 발생.
		 * 따라서 Folder 가 존재 하지 않으면 다시 생성해주는 부분 추가.
		 */
		if(!f.exists())	{
			//TODO : create folder
			DaulUtils.createFolder(m_strPath);
		}
		
        File[] files = f.listFiles();
        Arrays.sort(files);
        
        // Only Directory
        for (int i = 0; i < files.length; i++) {
        	
            File file = files[i];
            
            try  {
            	String str = new String(file.getName());
            	
            	if (file.isDirectory()) adapter.add("/" + str);

			} 
            catch (Exception e)  {
				e.printStackTrace();
			}

        } 
        
        for (int i = 0; i < files.length; i++) {
        	
            File file = files[i];
            
            try  {
            	String str = new String(file.getName());
            	
            	if (!file.isDirectory()) adapter.add(str);

			} 
            catch (Exception e)  {
				e.printStackTrace();
			}

        }
        
        lv.setAdapter(adapter);
		
	}

}
