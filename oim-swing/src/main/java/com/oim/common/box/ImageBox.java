/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oim.common.box;

import java.awt.Image;
import java.util.HashMap;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.oim.common.util.ImageUtil;


/**
 *为了减少io操作，系统中用到的一些
 * @author Hero
 */
public class ImageBox {

    private static HashMap<String, Image> imageMap = new HashMap<String, Image>();
    private static HashMap<String, ImageIcon> imageIconMap = new HashMap<String, ImageIcon>();
    private static HashMap<String, Icon> iconMap = new HashMap<String, Icon>();
    ///////////////////////////////////
    private static HashMap<String, Image> emptyImageMap = new HashMap<String, Image>();
    private static HashMap<String, ImageIcon> emptyImageIconMap = new HashMap<String, ImageIcon>();
    private static HashMap<String, Icon> emptyIconMap = new HashMap<String, Icon>();

    public static Image getImage(String path) {
        Image image = imageMap.get(path);
        if (null == image) {
            image = ImageUtil.getImage(path);
            if (null != image) {
                imageMap.put(path, image);
            }
            return image;
        }
        return image;
    }

    public static ImageIcon getImageIcon(String path) {
        ImageIcon imageIcon = imageIconMap.get(path);
        if (null == imageIcon) {
            imageIcon = ImageUtil.getImageIcon(path);
            if (null != imageIcon) {
                imageIconMap.put(path, imageIcon);
            }
            return imageIcon;
        }
        return imageIcon;
    }

    public static Icon getIcon(String path) {
        Icon icon = iconMap.get(path);
        if (null == icon) {
            icon = ImageUtil.getIcon(path);
            if (null != icon) {
                iconMap.put(path, icon);
            }
            return icon;
        }
        return icon;
    }

    public static Image getImage(String path, int w, int h) {
        Image image = imageMap.get(path + "_" + w + "_" + h);
        if (null == image) {
            image = ImageUtil.getImage(path, w, h);
            if (null != image) {
                imageMap.put(path + "_" + w + "_" + h, image);
            }
            return image;
        }
        return image;
    }

    public static ImageIcon getImageIcon(String path, int w, int h) {
        ImageIcon imageIcon = imageIconMap.get(path + "_" + w + "_" + h);
        if (null == imageIcon) {
            imageIcon = ImageUtil.getImageIcon(path, w, h);
            if (null != imageIcon) {
                imageIconMap.put(path + "_" + w + "_" + h, imageIcon);
            }
            return imageIcon;
        }
        return imageIcon;
    }

    public static Icon getIcon(String path, int w, int h) {
        Icon icon = iconMap.get(path + "_" + w + "_" + h);
        if (null == icon) {
            icon = ImageUtil.getIcon(path, w, h);
            if (null != icon) {
                iconMap.put(path + "_" + w + "_" + h, icon);
            }
            return icon;
        }
        return icon;
    }

    public static Image getImage(String path, int width, int height, int cornersWidth, int cornerHeight) {
        Image image = imageMap.get(path + "_" + width + "_" + height + "_" + cornersWidth + "_" + cornerHeight);
        if (null == image) {
            image = ImageUtil.getRoundedCornerImage(path, width, height, cornersWidth, cornerHeight);
            if (null != image) {
                imageMap.put(path + "_" + width + "_" + height + "_" + cornersWidth + "_" + cornerHeight, image);
            }
            return image;
        }
        return image;
    }

    public static ImageIcon getImageIcon(String path, int width, int height, int cornersWidth, int cornerHeight) {
        ImageIcon imageIcon = imageIconMap.get(path + "_" + width + "_" + height + "_" + cornersWidth + "_" + cornerHeight);
        if (null == imageIcon) {
            imageIcon = ImageUtil.getRoundedCornerImageIcon(path, width, height, cornersWidth, cornerHeight);
            if (null != imageIcon) {
                imageIconMap.put(path + "_" + width + "_" + height + "_" + cornersWidth + "_" + cornerHeight, imageIcon);
            }
            return imageIcon;
        }
        return imageIcon;
    }

    public static Icon getIcon(String path, int width, int height, int cornersWidth, int cornerHeight) {
        Icon icon = iconMap.get(path + "_" + width + "_" + height + "_" + cornersWidth + "_" + cornerHeight);
        if (null == icon) {
            icon = ImageUtil.getRoundedCornerIcon(path, width, height, cornersWidth, cornerHeight);
            if (null != icon) {
                iconMap.put(path + "_" + width + "_" + height + "_" + cornersWidth + "_" + cornerHeight, icon);
            }
            return icon;
        }
        return icon;
    }
    ////////////////////////////////////

    public static Image getEmptyImage(int w, int h) {
        String key = w + "_" + h;
        Image image = emptyImageMap.get(key);
        if (null == image) {
            image = ImageUtil.getEmptyImage(w, h);
            if (null != image) {
                emptyImageMap.put(key, image);
            }
        }
        return image;
    }

    public static ImageIcon getEmptyImageIcon(int w, int h) {
        String key = w + "_" + h;
        ImageIcon imageIcon = emptyImageIconMap.get(key);
        if (null == imageIcon) {
            imageIcon = ImageUtil.getEmptyImageIcon(w, h);
            if (null != imageIcon) {
                emptyImageIconMap.put(key, imageIcon);
            }
        }
        return imageIcon;
    }

    public static Icon getEmptyIcon(int w, int h) {
        String key = w + "_" + h;
        Icon icon = emptyIconMap.get(key);
        if (null == icon) {
            icon = ImageUtil.getEmptyIcon(w, h);
            if (null != icon) {
                emptyIconMap.put(key, icon);
            }
        }
        return icon;
    }
}
