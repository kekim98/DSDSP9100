
package com.dignsys.dsdsp.dsdsp_9100.service.handler;

import android.content.Context;
import android.text.Html;
import android.util.Log;

import com.dignsys.dsdsp.dsdsp_9100.db.AppDatabase;
import com.dignsys.dsdsp.dsdsp_9100.db.DatabaseCreator;
import com.dignsys.dsdsp.dsdsp_9100.db.entity.RssEntity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;


public class RssHandler extends BasicHandler {
    private static final String TAG = RssHandler.class.getSimpleName();

    private RssEntity mRss;


    public RssHandler(Context context) {
        super(context);
    }


    @Override
    public void process(String body) {
        AppDatabase db = DatabaseCreator.getInstance(mContext);
        mRss = db.rssDao().loadRssSync();
     //   mConfig = db.configDao().loadConfigSync();


        boolean bIsItem = false;

        String strTitle 	= "";
        String strDesc 		= "";
      //  String strRSSText 	= "";
        

        try {

            XmlPullParserFactory factory = null;

            factory = XmlPullParserFactory.newInstance();

            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput(new StringReader(body));
            
            int eventType = xpp.getEventType();
            
            while (eventType != XmlPullParser.END_DOCUMENT) {
                
                if (eventType == XmlPullParser.START_DOCUMENT) {
                    
                    Log.d(TAG,"Start document");
                    
                } else if (eventType == XmlPullParser.START_TAG) {
                    
                    Log.d(TAG,"Start tag " + xpp.getName());

                    String strStartTag = xpp.getName();


                    if(strStartTag.equals("item")) {
                        bIsItem = true;
                    }

                    if(strStartTag.equals("title")) {
                        if(bIsItem) strTitle = Html.fromHtml(xpp.nextText()).toString().trim();
                    }
                    if(strStartTag.equals("link")) {
                    }
                    if(strStartTag.equals("description")) {
                        if(bIsItem) strDesc = Html.fromHtml(xpp.nextText()).toString().trim();
                    }
                    if(strStartTag.equals("image")) {
                    }
                    
                    
                } else if (eventType == XmlPullParser.END_TAG) {
                    
                    Log.d(TAG,"End tag " + xpp.getName());

                    String strEndTag = xpp.getName();

                    if(strEndTag.equals("item")) {

                        if(!strDesc.isEmpty())	strDesc = strDesc + " | ";

                        if(!strTitle.isEmpty())	strTitle = strTitle + " ▶ ";

                        bIsItem = false;

                    }
                    
                } else if (eventType == XmlPullParser.TEXT) {
                    
                    Log.d(TAG,"Text " + xpp.getText());
                    
                }
                eventType = xpp.next();
            }
            Log.d(TAG,"End document");

            strDesc = strDesc.replace("\r", "");
            strDesc = strDesc.replace("\n", "");

            strTitle = strTitle.replace("\r", "");
            strTitle = strTitle.replace("\n", "");

            strDesc = Html.fromHtml(strDesc).toString();
            strTitle = Html.fromHtml(strTitle).toString();


            mRss.setTitle_text(strTitle);
            mRss.setDesc_text(strDesc);



        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public RssEntity getRss() {
        return mRss;
    }
}
