package com.im.test.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

/**
 * @description:
 * @author XiaHui
 * @date 2014-06-09 14:22:34
 * @version 1.0.0
 */
public class HttpUtil {

    public static String post(String url) {
        Map<String, String> dataMap = new HashMap<String, String>();
        return post(url, dataMap, 3000, null, null, false);
    }

    public static String post(String url, Map<String, String> dataMap) {
        return post(url, dataMap, 3000, null, null, false);
    }

    public static String post(String httpUrl, Map<String, String> dataMap, int timeOut, Charset sendCharset, Charset getCharset, boolean urlCharset) {
        if (urlCharset) {
            int lastIndex = httpUrl.lastIndexOf("?");
            if (lastIndex != -1) {
                String temp = httpUrl.substring(lastIndex + 1, httpUrl.length());
                dataMap.putAll(getDataMap(temp));
                httpUrl = httpUrl.substring(0, lastIndex);
            }
        }
//        try {
//            httpUrl = getUrl(httpUrl, dataMap, sendCharset);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        //获得响应状态
        StringBuilder text = new StringBuilder();
        InputStream in = null;
        BufferedReader reader = null;
        PrintWriter out = null;

        try {

            //建立连接
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //设置参数
            connection.setDoOutput(true);   //需要输出
            connection.setDoInput(true);   //需要输入
            connection.setUseCaches(false);  //不允许缓存
            connection.setRequestMethod("POST");   //设置POST方式连接
            //设置请求属性
            connection.setConnectTimeout(timeOut);
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + (null == sendCharset ? "UTF-8" : sendCharset.name()));

            connection.setRequestProperty("Charset", "UTF-8");
            //连接,也可以不用明文connect，使用下面的httpConn.getOutputStream()会自动connect
            connection.connect();
            //建立输入流，向指向的URL传入参数
            DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
            if (null != dataMap && !dataMap.isEmpty()) {
                Set<String> keySet = dataMap.keySet();
                for (String key : keySet) {
                    dos.writeBytes(key + "=" + dataMap.get(key));
                }
            }
            dos.flush();
            dos.close();

            int resultCode = connection.getResponseCode();
            if (HttpURLConnection.HTTP_OK == resultCode) {

                in = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader((in), (null == getCharset ? "UTF-8" : getCharset.name())));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    text.append(line);
                }
            }
            connection.disconnect();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (null != out) {
                    out.close();
                }
                if (null != in) {
                    in.close();
                }
                if (null != reader) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return text.toString();
    }

    public static String get(String httpUrl, int timeOut, Charset sendCharset, Charset getCharset) {

        StringBuilder text = new StringBuilder();
        InputStream in = null;
        BufferedReader reader = null;
        URL url;
        try {
            url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setConnectTimeout(timeOut);
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setRequestProperty("Content-Type", "text/html; charset=" + sendCharset.name());
            //connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + sendCharset.name());

            connection.connect();
            in = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader((in), getCharset));
            String line = "";
            while ((line = reader.readLine()) != null) {
                text.append(line);
            }
            connection.disconnect();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (null != in) {
                    in.close();
                }
                if (null != reader) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return text.toString();
    }

    public static String getUrl(String url, Map<String, String> dataMap, Charset charset) throws UnsupportedEncodingException {
        StringBuilder value = new StringBuilder(url);
        if (StringUtils.isNotBlank(url)) {
            boolean isFirst = false;
            if (null != dataMap && !dataMap.isEmpty()) {
                int index = url.indexOf("?");
                int lastIndex = url.lastIndexOf("?");
                if (index == -1) {
                    value.append("?");
                    isFirst = true;
                }
                if (lastIndex == (url.length() - 1)) {
                    isFirst = true;
                }
            }

            Set<String> keySet = dataMap.keySet();
            for (String key : keySet) {
                String data = dataMap.get(key);
                if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(data)) {
                    if (!isFirst) {
                        value.append("&");
                    }
                    value.append(key);
                    value.append("=");
                    value.append((null == charset) ? data : URLEncoder.encode(data, charset.name()));
                    isFirst = false;
                }
            }
        }
        return value.toString();
    }

    public static Map<String, String> getDataMap(String urlValue) {
        Map<String, String> dataMap = new HashMap<String, String>();
        if (StringUtils.isNotBlank(urlValue)) {
            String[] values = urlValue.split("&");
            if (values.length > 0) {
                for (String d : values) {
                    String[] dataArray = d.split("=");
                    if (dataArray.length == 2) {
                        String name = (dataArray[0]);
                        String value = (dataArray[1]);
                        dataMap.put(name, value);
                    }
                }
            }
        }
        return dataMap;
    }

    public static void main(String a[]) {
        String auth = "{\"id\":\"own-000001\",\"key\":\"kkkyyyttt\"}";
        String userId = "B14487B135F4760115E9846397378890";
        String url = "http://192.168.1.155:9010/auth/getToken.do";//?auth={\"id\":\"own-000001\",\"key\":\"kkkyyyttt\"}&userId=0A1AB42B0E15448F550803EC8BE5C651";
        Map<String, String> dataMap = new HashMap<String, String>();
        dataMap.put("userId", userId);
        dataMap.put("auth", auth);
        //dataMap.put("time", (1000*60*3)+"");
       // url = "http://192.168.1.155:8800/ban/banChat.do";
        System.out.println(post(url, dataMap));
    }
}
