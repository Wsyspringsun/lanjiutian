package com.wyw.ljtsp.biz.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2017/7/27.
 */

public class HttpRequestUtil {

    private String path;

    private String json;

    public HttpRequestUtil(String path, String json){
        this.path = path;
        this.json = json;
    }

    public String doRequest(){
        URL url = null;
        StringBuffer sb = new StringBuffer();
        try {
            Log.e("path",path);
            url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
//            conn.setReadTimeout(5000);
            conn.setRequestProperty("Content-Type",
                    "application/json; charset=UTF-8");
            conn.setDoInput(true);// 表示从服务器获取数据
            conn.setDoOutput(true);// 表示向服务器写数据

            Log.e("conn",""+conn.getResponseCode());
//            conn.connect();

            if(HttpURLConnection.HTTP_OK==conn.getResponseCode()){
                Log.e("status","true");
            }
            Log.e("111111","111111111"+conn.getOutputStream());
            OutputStream os = conn.getOutputStream();
            Log.e("111",path);
            os.write(json.getBytes());
            Log.e("111",path);
            if(conn.getResponseCode()==200){
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String str;
                while((str=reader.readLine())!=null){
                    sb.append(str);
                }
            }
            Log.e("sb--------",sb.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("sb",sb.toString());
        return sb.toString();
    }
}
