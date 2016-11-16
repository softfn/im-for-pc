package com.only.common.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

public class OnlyLangUtil {

    /**
     * ip string to int
     */
    public static int ipToInt(String ip) {
        try {
            InetAddress address = InetAddress.getByName(ip);
            byte[] bytes = address.getAddress();
            int a, b, c, d;
            a = byteToInt(bytes[0]);
            b = byteToInt(bytes[1]);
            c = byteToInt(bytes[2]);
            d = byteToInt(bytes[3]);
            int result = (a << 24) | (b << 16) | (c << 8) | d;
            return result;
        } catch (UnknownHostException e) {
            return 0;
        }
    }

    /**
     * byte to int
     */
    public static int byteToInt(byte b) {
        int l = b & 0x07f;
        if (b < 0) {
            l |= 0x80;
        }
        return l;
    }

    /**
     * ip to long
     */
    public static long ipToLong(String ip) {
        int ipNum = ipToInt(ip);
        return intToLong(ipNum);
    }

    /**
     * int to long
     */
    public static long intToLong(int i) {
        long l = i & 0x7fffffffL;
        if (i < 0) {
            l |= 0x080000000L;
        }
        return l;
    }

    /**
     * long to ip string
     */
    public static String longToIP(long ip) {
        int[] b = new int[4];
        b[0] = (int) ((ip >> 24) & 0xff);
        b[1] = (int) ((ip >> 16) & 0xff);
        b[2] = (int) ((ip >> 8) & 0xff);
        b[3] = (int) (ip & 0xff);
        String x;
        x = Integer.toString(b[0]) + "." + Integer.toString(b[1]) + "." + Integer.toString(b[2]) + "." + Integer.toString(b[3]);
        return x;

    }

    /**
     * 判断是否整型
     */
    public static boolean isNumber(String s) {
        try {
            if (s == null || ("".equals(s))) {
                return false;
            }
            for (int index = 0; index < s.length(); index++) {
                if (48 > (int) s.charAt(index) || (int) s.charAt(index) > 57) {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static String getHttpContent(String http) {
        try {
            StringBuilder sb = new StringBuilder();
            URL url = new URL(http);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.addRequestProperty("host", url.getHost());
            con.addRequestProperty("referer", url.getHost());
            con.setDoInput(true);
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = "";
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            in.close();
            con.disconnect();
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }

    public static String htmlSpecialReplace(String content) {
        StringBuilder sb = new StringBuilder();
        char[] chars = content.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            switch (chars[i]) {
                case '&':
                    sb.append("&amp;");
                    break;
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '"':
                    sb.append("&quot;");
                    break;
                default:
                    sb.append(chars[i]);
                    break;
            }
        }
        return sb.toString();
    }

    /**
     * 获取字符串前面的数字
     *
     * @param text
     * @return
     */
    public static int getHeaderNumber(String text) {
        if (text == null || text.length() == 0) {
            return 0;
        }
        for (int i = 0; i < text.length(); i++) {
            char temp = text.charAt(i);
            if (temp >= '9' || temp <= '0') {
                if (i == 0) {
                    return 0;
                } else {
                    return Integer.parseInt(text.substring(0, i));
                }
            }
        }

        return Integer.parseInt(text);
    }

    public static String getTime(int time) {
        String ret = "0";
        time = time / 1000;
        if (time >= 3600) {
            int h = time / 3600;
            int m = (time - h * 3600) / 60;
            int s = time - h * 3600 - m * 60;

            ret = h + ":" + (m > 9 ? m : ("0" + m)) + ":" + (s > 9 ? s : ("0" + s));
        } else if (time >= 60 && time < 3600) {
            int m = time / 60;
            int s = time - m * 60;

            ret = (m > 9 ? m : ("0" + m)) + ":" + (s > 9 ? s : ("0" + s));
        } else {
            ret = "00:" + (time > 9 ? time : ("0" + time));
        }

        return ret;
    }

    /**
     * 返回四舍五入后精确小数点2位
     *
     * @return
     */
    public static double formatDouble(double money) {
        BigDecimal b = new BigDecimal(money);
        double d2 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return d2;
    }
}
