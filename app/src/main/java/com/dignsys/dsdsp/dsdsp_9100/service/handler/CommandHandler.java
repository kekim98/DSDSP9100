
package com.dignsys.dsdsp.dsdsp_9100.service.handler;

import android.content.Context;
import android.text.TextUtils;

import com.dignsys.dsdsp.dsdsp_9100.db.AppDatabase;
import com.dignsys.dsdsp.dsdsp_9100.db.DatabaseCreator;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.CommandEntity;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.ConfigEntity;
import com.dignsys.dsdsp.dsdsp_9100.model.PlayContent;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class CommandHandler extends BasicHandler {
    private static final String TAG = CommandHandler.class.getSimpleName();

    private CommandEntity mCommand ;



    public CommandHandler(Context context) {
        super(context);
    }


    @Override
    public void process(String body) {

        mCommand =  new CommandEntity();

        String strLine = "";
        // convert String into InputStream
        InputStream is = new ByteArrayInputStream(body.getBytes());

        BufferedReader br = new BufferedReader(new InputStreamReader(is));

       try {
           while ((strLine = br.readLine()) != null) {

               if(strLine.length() == 0 ) continue;

               if(strLine.startsWith("upgrade"))	{


                       String strFWFileName = strLine.substring(strLine.indexOf(" ")).trim();

                       mCommand.setFwFilePath(strFWFileName);

               }
               else {

                   if(strLine.startsWith("addfont"))	{

                       String strFontFileName = strLine.substring(strLine.indexOf(" ")).trim() + ".ttf";
                       int n = Integer.valueOf(strLine.substring(7, 8));

                       switch (n) {
                           case 1:
                               mCommand.setFontFilePath_1(strFontFileName);
                               break;
                           case 2:
                               mCommand.setFontFilePath_2(strFontFileName);
                               break;
                           case 3:
                               mCommand.setFontFilePath_3(strFontFileName);
                               break;
                       }


                   }

                   if(strLine.startsWith("poweroff"))	{


                       mCommand.setPowerOff(true);

                   }

                   if(strLine.startsWith("poweron"))	{

                       mCommand.setPowerOn(true);

                   }

                   if(strLine.startsWith("reboot"))	{

                       mCommand.setReboot(true);
                   }
               }

           }
           br.close();



       } catch (IOException e) {
            e.printStackTrace();
       }

    }

    public ArrayList<PlayContent> getContentFiles() {

        ArrayList<PlayContent> contents = new ArrayList<>();

        if (mCommand == null) {
            return contents;
        }

        String url;
        String filename;

        AppDatabase db = DatabaseCreator.getInstance(mContext);
        ConfigEntity config = db.configDao().loadConfigSync();

        final String hostAddr = config.getServerAddress();
        final String port = config.getServerPort();
        final String folder = config.getServerFolder();




        if (!TextUtils.isEmpty(mCommand.getFwFilePath())) {

            PlayContent content = new PlayContent();
            filename =mCommand.getFwFilePath();
            url = String.format("http://%s:%s/%s/%s", hostAddr, port, folder, filename);

            content.filename = filename;
            content.url = url;
            contents.add(content);
        }

        if (!TextUtils.isEmpty(mCommand.getFontFilePath_1())) {
            PlayContent content = new PlayContent();

            filename = mCommand.getFontFilePath_1();
            url = String.format("http://%s:%s/%s/%s", hostAddr, port, folder, filename);

            content.filename = filename;
            content.url = url;
            contents.add(content);

        }

        if (!TextUtils.isEmpty(mCommand.getFontFilePath_2())) {
            PlayContent content = new PlayContent();

            filename = mCommand.getFontFilePath_2();
            url = String.format("http://%s:%s/%s/%s", hostAddr, port, folder, filename);

            content.filename = filename;
            content.url = url;
            contents.add(content);

        }

        if (!TextUtils.isEmpty(mCommand.getFontFilePath_3())) {
            PlayContent content = new PlayContent();

            filename = mCommand.getFontFilePath_3();
            url = String.format("http://%s:%s/%s/%s", hostAddr, port, folder, filename);

            content.filename = filename;
            content.url = url;
            contents.add(content);

        }

        return contents;
    }

    public CommandEntity getCommand() {
        return mCommand;
    }
}
