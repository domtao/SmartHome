package com.zunder.smart.tools;

import android.util.Log;
import android.util.Xml;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Exchanger;

/**
 * Created by hectoraguilar on 15/01/16.
 */
public class HttpUrlPostJson{
    public static InputStream is = null;
    public static String json = "";
//    public static HttpURLConnection urlConnection;
   private  static HttpUrlPostJson instance;

    public static HttpUrlPostJson getInstance() {
        if(instance==null){
            instance=new HttpUrlPostJson();
        }
        return instance;
    }

    public String getConnectionJson(String urlStr, HashMap data){
        String result="[]";
        try{

            URL url = new URL(urlStr);
            HttpURLConnection   urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(100000);
            urlConnection.setConnectTimeout(150000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getQueryData(data));
            writer.flush();
            writer.close();
            urlConnection.connect();
            is = urlConnection.getInputStream();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is,"UTF-8");
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if ("string".equals(parser.getName())) {
                        result=  parser.nextText();
                        }
                        break;
                }
                event = parser.next();
            }
            os.close();
            is.close();
            urlConnection.disconnect();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public String getQueryData(HashMap<Object, Object> data) throws UnsupportedEncodingException {
        if(data==null){
            return "";
        }
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for(Map.Entry<Object,Object> entry:data.entrySet()){
            if(first)
                first=false;
            else
                result.append("&");
            result.append(URLEncoder.encode(entry.getKey().toString(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));

        }
        return result.toString();
    }
}
