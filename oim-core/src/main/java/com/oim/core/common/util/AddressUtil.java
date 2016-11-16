/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oim.core.common.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hero
 */
public class AddressUtil {

    
    public static String getIP(String url) {

        try {
            InetAddress inetAddress = InetAddress.getByName(url);
            return inetAddress.getHostAddress();
        } catch (UnknownHostException ex) {
            Logger.getLogger(AddressUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static String getLocalHostIP() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            Logger.getLogger(AddressUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * 按照"XX-XX-XX-XX-XX-XX"格式，获取本机MAC地址
     *
     * @return
     * @throws Exception
     */
    public static String getMacAddress() {
        try {
            Enumeration<NetworkInterface> ni = NetworkInterface.getNetworkInterfaces();
            while (ni.hasMoreElements()) {
                NetworkInterface net = ni.nextElement();
                if (net != null) {
                    byte[] bytes = net.getHardwareAddress();
                    if (net.isUp() && bytes != null && bytes.length == 6) {
                        StringBuilder sb = new StringBuilder();
                        for (byte b : bytes) {
                            sb.append(Integer.toHexString((b & 240) >> 4));// 与11110000作按位与运算以便读取当前字节高4位
                            sb.append(Integer.toHexString(b & 15)); // 与00001111作按位与运算以便读取当前字节低4位
                            sb.append("-");
                        }
                        sb.deleteCharAt(sb.length() - 1);
                        return sb.toString().toUpperCase();
                    }
                }
            }
        } catch (Exception e) {
        }
        return null;
    }
    
    public static void main(String[] args) {
        System.out.println(getMacAddress());
    }
}
