package com.oim.ui.theme;

import javax.swing.JLabel;

import com.oim.swing.common.util.ImageUtil;


@SuppressWarnings("serial")
public class ThemeIconLabel extends JLabel {

    private String iconPath;

    /**
     * @return the iconPath
     */
    public String getIconPath() {
        return iconPath;
    }

    /**
     * @param iconPath the iconPath to set
     */
    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
        if (null != iconPath && !"".equals(iconPath)) {
            this.setIcon(ImageUtil.getRoundedCornerImageIcon(iconPath, 180, 120, 10, 10));
        }
    }
}
