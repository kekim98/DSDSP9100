package com.dignsys.dsdsp.dsdsp_9100.util;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StatFs;
import android.util.Log;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.channels.FileLock;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint({ "DefaultLocale", "NewApi", "SimpleDateFormat" })
public class DaulUtils {

	private static String m_strMacAddress = getLocalMACAddress();
	
	public static void showMessageBox(Context context, String strMsg, String strPositiveButtonText)
	{
		AlertDialog.Builder adb = new AlertDialog.Builder(context);
		adb.setPositiveButton(strPositiveButtonText, new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		    dialog.dismiss();  
		    }
		});
		adb.setMessage(strMsg);
		final AlertDialog ad = adb.create();
		
		ad.setOnShowListener(new DialogInterface.OnShowListener(){

	        @Override
	        public void onShow(DialogInterface dialog) {

	            Button btn = ad.getButton(AlertDialog.BUTTON_POSITIVE);
	            btn.setFocusable(true);
	            btn.setFocusableInTouchMode(true);
	            btn.requestFocus();
	        }
	    });
		
		ad.show();
	}
	

	public static boolean isUMSConnected(String strUMSPath)
	{
		File f = new File(strUMSPath);
		
		int nFileCnt = (int) f.length();
		
		if(nFileCnt > 0) return true;
		
		return false;
	}

	public static String getLocalMACAddress()
	{
		String strAddress 	= "";
	    StringBuffer sb 			= new StringBuffer(1000);
	    
		try {
	        char[] szBuffer = new char[1024];
	        int nReadCount 	= 0;
			
	        BufferedReader br = new BufferedReader(new FileReader("/sys/class/net/eth0/address"));
				        
	        while ((nReadCount = br.read(szBuffer)) != -1) {
	            String readData = String.valueOf(szBuffer, 0, nReadCount);
	            sb.append(readData);
	        }
	        
	        br.close();
	        strAddress = sb.toString().replace("\n", "");
		} 
		catch (IOException e) {
			e.printStackTrace();
		}	        
	    
	    return strAddress.replace(":", "").toUpperCase();
	}
	

	public static String getExtension(String strFileName){
		return strFileName.substring(strFileName.lastIndexOf(".")+1,strFileName.length());
	}
	
	
	public static void createFolder(String strPath)
	{
    	File fPath 		= new File( strPath );
    	
    	if( !fPath.isDirectory() )	{	
    		fPath.mkdirs();
        }
    			
	}
	
	public static boolean copyFile(File file , String save_file)	{
		
        boolean result;
        
        if(file!=null&&file.exists())	{
        	
            try {
                int 	readcount	= 0;
                
                byte[] 	buffer 		= new byte[4096];
            	
                FileInputStream fis 	= new FileInputStream(file);
                FileOutputStream newfos = new FileOutputStream(save_file);
                
                while((readcount = fis.read(buffer,0,4096))!= -1)	{
                    newfos.write(buffer,0,readcount);
                }
                
                newfos.close();
                fis.close();
            } 
            catch (Exception e) {
				Log.e("BLEUAILE", "@@@@@ ERROR @@@@@ Utils:copyFile() # " + e.getMessage());
                e.printStackTrace();
            }
            result = true;
            
        }
        else	{
            result = false;
        }
        
        return result;
    }
	
	public static boolean moveFile(String strSrcPath, String strDescPath)
	{
		File fileSrc= new File(strSrcPath);
		
		if(fileSrc.exists())	{
			
			File fileDest = new File(strDescPath);
			
			if(!fileSrc.renameTo(fileDest)) 	{
				Log.e("BLEUAILE", "@@@@@ ERROR @@@@@ Utils:moveFile() : move fail - " + fileSrc.getName());
				return false;
			}
		}	
		else return false;
		
		return true;
	}
	
	public static boolean moveFiles(String strSrc, String strDesc)
	{
		boolean bResult = false;

		File f = new File(strSrc);
		
		if(f.isDirectory())	{
			if (f.list().length > 0) {
				
				String files[] = f.list();
				
				for (String temp: files) {
					
					File fileSrc= new File(f, temp);
					
					if(!fileSrc.isDirectory())	{
						
						String strDestPath = strDesc + File.separator + fileSrc.getName();
						File fileDest = new File(strDestPath);
						//bResult = copyFile(fileSrc, strDescPath);	
						bResult = fileSrc.renameTo(fileDest);
						if(!bResult) 	{		
							Log.e("BLEUAILE", "@@@@@ ERROR @@@@@ Utils:moveFiles() # move fail. " + fileSrc.getName());
							return bResult;						
						}
					}					
				}
			}
		}
		
		return bResult;
	}
	
	public static boolean deleteFilesInDirectory(String strPath)
	{
		boolean bResult = false;
		
		File f = new File(strPath);
		
		if(f.isDirectory())	{
			if (f.list().length > 0) {
				
				String files[] = f.list();
				
				for (String temp: files) {
					File fileDelete = new File(f, temp);
					
					if(!fileDelete.isDirectory())	{
						bResult = fileDelete.delete();
						if(!bResult) return bResult;
					}					
				}
			}
		}
		
		return bResult;
	}
	
	public static boolean deleteDirectory(File file) {
		
		boolean bResult = false;
		
		if (file.isDirectory()) {
			
			if (file.list().length == 0) {
				bResult = file.delete();
				
				if(!bResult) return bResult;
			} 
			else {
				
				String files[] = file.list();
				
				for (String temp: files) {
					File fileDelete = new File(file, temp);
					bResult = deleteDirectory(fileDelete);
					if(!bResult) return bResult;
				}
				
				if (file.list().length == 0) {
					bResult = file.delete();
					if(!bResult) return bResult;
				}
				
			}
			
		} 
		else {
			bResult = file.delete();
		}
		
		return bResult;
	}
	
	static public void writeLog(String strPath, String strLine, boolean bStatus)
	{
		
		if(strLine.contains("playlist.txt")) return;
		if(strLine.contains("format.txt")) return;
		if(strLine.contains("command.txt")) return;
			
		SimpleDateFormat sdfDate 		= new SimpleDateFormat("yyyy_MM_dd");
		SimpleDateFormat sdfDateTime 	= new SimpleDateFormat("yyyy.MM.dd\tHH:mm:ss\t");
		String strFilePath 				= "";
		String strToday 				= sdfDate.format(new Date());
		String strCurrent 				= sdfDateTime.format(new Date());
		
		sdfDate 	= null;
		sdfDateTime = null;
		
		if(bStatus) strFilePath = String.format("%s/%s_%s_filestatus.txt", strPath , m_strMacAddress, strToday);
		else 		strFilePath = String.format("%s/%s_%s.txt", strPath, m_strMacAddress, strToday);
		
		
		File f = new File(strFilePath);
		
		try {
			
			//String strLineEncoding = new String(strLine.toString().getBytes(), "MS949");
				
			FileOutputStream fos = new FileOutputStream(f, true);
			FileLock fl = fos.getChannel().lock();
			
			/*
			FileWriter writer = new FileWriter(f, true);
            writer.write(strCurrent + strLineEncoding + "\n");
            writer.flush();
            writer.close();
            */			

			FileOutputStream fileOutputStream = new FileOutputStream(f, true);
			OutputStreamWriter OutputStreamWriter = new OutputStreamWriter(fileOutputStream, "MS949");
			BufferedWriter bufferedWriter = new BufferedWriter(OutputStreamWriter);
			
			bufferedWriter.write(strCurrent + strLine + "\n");
			
			bufferedWriter.flush();
			bufferedWriter.close();
			
			OutputStreamWriter.close();
			
			fileOutputStream.close();
			
			fl.release();
			fos.flush();
            fos.close();
            
            bufferedWriter = null;
            OutputStreamWriter = null;
            fileOutputStream = null;
            fos = null;            
		} 
		catch (IOException e) {
			e.printStackTrace();		
		}		
		
		f = null;
		
	}
		
	public static String encodeURL(String strURL) {
		
		if (strURL.length() == 0) return strURL;
		
		Pattern patternHangul = Pattern.compile("[ㄱ-ㅎㅏ-ㅣ가-힣]");
	
	    Matcher matcher = patternHangul.matcher(strURL);
	    
	    while(matcher.find()) {
	      String group = matcher.group();
	      
	      try {
	    	  //strURL = strURL.replace(group, URLEncoder.encode(group, "UTF-8"));		// Windows OK. Linux Faile.
	    	  strURL = strURL.replace(group, URLEncoder.encode(group, "EUC-KR"));		// Windows/Linux OK.
	      } 
	      catch (UnsupportedEncodingException ignore) {
	      }
	    }
	
	    return strURL;
	}
	
	static public String getSizeString(long l)
	{  
		
		int nUnit = 1024;
		
		if (l < nUnit) return l + " B";
		
		int exp = (int) (Math.log(l) / Math.log(nUnit));
		
		String pre = "KMGTPE".charAt(exp-1)+"";
		
		return String.format("%.2f %sB", l / Math.pow(nUnit, exp), pre);
	}

	static public long getTotalSDSize(String strPath)
	{
        File f = new File(strPath);

		if(!f.exists()){
			return 0;
		}
        StatFs s = new StatFs(f.getPath());
        
        long lBlockSize 	= s.getBlockSizeLong();
        long lTotalBlocks 	= s.getBlockCountLong();  
        
        return lTotalBlocks * lBlockSize; 
    }
	
	static public long getAvailableSDSize(String strPath)
	{
		File f = new File(strPath);

		//bawoori : fix exception occur once sdcard not exist
		if(!f.exists()){
			return 0;
		}
		StatFs s = new StatFs(f.getPath());
		
		long lBlockSize 		= s.getBlockSizeLong();  
		long lAvailableBlocks 	= s.getAvailableBlocksLong();  
		
		return lAvailableBlocks * lBlockSize;  
    }
	
	static public String stripExtension (String str)
	{

        if (str == null) return null;

        int nPos = str.lastIndexOf(".");

        if (nPos == -1) return str;

        return str.substring(0, nPos);
    }
	
	
}
