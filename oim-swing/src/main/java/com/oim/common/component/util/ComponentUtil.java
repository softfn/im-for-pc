/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oim.common.component.util;

import java.awt.Font;

/**
 * 2013-8-29 17:57:17
 *
 * @author XiaHui
 */
public class ComponentUtil {

    /**
     * 获取缺省字体
     *
     * @return 缺省字体
     */
    public static Font getDefaultFont() {
        String osVersion = System.getProperty("os.version");
        Font font;
        if (isWindows()) {
            if (osVersion.compareTo("6") >= 0) {
                font = new Font("微软雅黑", Font.PLAIN, 12);
            } else {
                font = new Font("微软雅黑", Font.PLAIN, 12);
                if (!"微软雅黑".equals(font.getFontName())) {
                    font = new Font("宋体", Font.PLAIN, 12);
                }
            }
        } else {
            font = new Font(Font.DIALOG, Font.PLAIN, 12);
        }
        return font;
    }

    /**
     * 获取缺省字体
     *
     * @return 缺省字体
     */
    public static Font getDefaultFont(int size) {
        String osVersion = System.getProperty("os.version");
        Font font;

        if (isWindows()) {
            if (osVersion.compareTo("6") >= 0) {
                font = new Font("微软雅黑", Font.PLAIN, size);
            } else {
                font = new Font("微软雅黑", Font.PLAIN, size);
                if (!"微软雅黑".equals(font.getFontName())) {
                    font = new Font("宋体", Font.PLAIN, size);
                }
            }
        } else {
            font = new Font(Font.DIALOG, Font.PLAIN, size);
        }

        return font;
    }

    /**
     * 获取缺省字体
     *
     * @return 缺省字体
     */
    public static Font getDefaultFont(int style, int size) {
        String osVersion = System.getProperty("os.version");
        Font font;

        if (isWindows()) {
            if (osVersion.compareTo("6") >= 0) {
                font = new Font("微软雅黑", style, size);
            } else {
                font = new Font("微软雅黑", style, size);
                if (!"微软雅黑".equals(font.getFontName())) {
                    font = new Font("宋体", style, size);
                }
            }
        } else {
            font = new Font(Font.DIALOG, style, size);
        }
        return font;
    }

    /**
     * 判断当前操作系统是否为Windows
     *
     * @return 是否为Windows
     */
    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().startsWith("windows");
    }
}
