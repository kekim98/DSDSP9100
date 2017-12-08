
package com.dignsys.dsdsp.dsdsp_9100.service.handler;

import android.content.Context;

import com.dignsys.dsdsp.dsdsp_9100.db.AppDatabase;
import com.dignsys.dsdsp.dsdsp_9100.db.DatabaseCreator;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.CommandEntity;
import com.dignsys.dsdsp.dsdsp_9100.model.PlayContent;
import com.dignsys.dsdsp.dsdsp_9100.util.IOUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class CommandHandler extends BasicHandler {
    private static final String TAG = CommandHandler.class.getSimpleName();

    private CommandEntity mCommand = new CommandEntity();



    public CommandHandler(Context context) {
        super(context);
    }


    @Override
    public void process(String body) {


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

                       mCommand.setFontFilePath(strFontFileName);

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

        String url;
        String filename;
        final String hostAddr = IOUtils.getHostAddress(mContext);

        PlayContent content = new PlayContent();

        if (!mCommand.getFwFilePath().isEmpty()) {

            if (mCommand.getFwFilePath().startsWith("/")) {
                url = hostAddr + mCommand.getFwFilePath();
            }else{
                url =mCommand.getFwFilePath();
            }

            filename = IOUtils.getFilename(url);

            content.filename = filename;
            content.url = url;
            contents.add(content);
        }

        if (!mCommand.getFontFilePath().isEmpty()) {

            if (mCommand.getFontFilePath().startsWith("/")) {
                url = hostAddr + mCommand.getFontFilePath();
            }else{
                url =mCommand.getFontFilePath();
            }

            filename = IOUtils.getFilename(url);

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
