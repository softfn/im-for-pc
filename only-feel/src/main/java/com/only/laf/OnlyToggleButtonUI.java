package com.only.laf;

import com.only.OnlyToggleButton;
import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicToggleButtonUI;

import sun.awt.AppContext;
import sun.swing.SwingUtilities2;
import com.only.util.OnlyUIUtil;

public class OnlyToggleButtonUI extends BasicToggleButtonUI {

    private static final Object ONLY_TOGGLE_BUTTON_UI_KEY = new Object();
    private int pressMoveX, pressMoveY;

    public static ComponentUI createUI(JComponent c) {
        AppContext appContext = AppContext.getAppContext();
        OnlyToggleButtonUI onlyToggleButtonUI = (OnlyToggleButtonUI) appContext.get(ONLY_TOGGLE_BUTTON_UI_KEY);

        if (onlyToggleButtonUI == null) {
            onlyToggleButtonUI = new OnlyToggleButtonUI();
            appContext.put(ONLY_TOGGLE_BUTTON_UI_KEY, onlyToggleButtonUI);
        }

        return onlyToggleButtonUI;
    }

    @Override
    protected void paintFocus(Graphics g, AbstractButton button, Rectangle viewRect, Rectangle textRect, Rectangle iconRect) {
        if (!(button instanceof OnlyToggleButton)) {
            super.paintFocus(g, button, viewRect, textRect, iconRect);
        }
    }

    @Override
    protected void paintText(Graphics g, JComponent c, Rectangle textRect, String text) {
        if (c instanceof OnlyToggleButton) {
            OnlyToggleButton button = (OnlyToggleButton) c;
            ButtonModel model = button.getModel();
            FontMetrics fm = SwingUtilities2.getFontMetrics(c, g);
            int mnemIndex = button.getDisplayedMnemonicIndex();

            if (model.isEnabled()) {
                g.setColor(button.getForeground());
            } else {
                g.setColor(button.getDisabledTextColor());
            }

            SwingUtilities2.drawStringUnderlineCharAt(c, g, text, mnemIndex, textRect.x + pressMoveX, textRect.y + pressMoveY + fm.getAscent());
        } else {
            super.paintText(g, c, textRect, text);
        }
    }

    @Override
    protected void paintButtonPressed(Graphics g, AbstractButton b) {
        if (b instanceof OnlyToggleButton && ((OnlyToggleButton) b).isPaintPressDown()) {
            pressMoveX = pressMoveY = 1;
        }

        super.paintButtonPressed(g, b);
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        pressMoveX = pressMoveY = 0;
        super.paint(g, c);
    }

    @Override
    public void update(Graphics g, JComponent c) {
        Graphics2D g2d = (Graphics2D) g;
        Composite oldComposite = g2d.getComposite();
        boolean opaque = c.isOpaque();

        if (c instanceof OnlyToggleButton) {
            g2d.setComposite(AlphaComposite.SrcOver.derive(((OnlyToggleButton) c).getAlpha()));
            opaque = opaque || !((OnlyToggleButton) c).isImageOnly();
        }

        if (opaque) {
            g.setColor(c.getBackground());
            g.fillRect(0, 0, c.getWidth(), c.getHeight());
        }

        paintBackgroundImage(g, c);
        g2d.setComposite(oldComposite);
        paint(g, c);
    }

    @Override
    protected void paintIcon(Graphics g, JComponent c, Rectangle iconRect) {
        Graphics2D g2d = (Graphics2D) g;
        Composite oldComposite = g2d.getComposite();

        if (c instanceof OnlyToggleButton) {
            g2d.setComposite(AlphaComposite.SrcOver.derive(((OnlyToggleButton) c).getAlpha()));
        }

        super.paintIcon(g, c, new Rectangle(iconRect.x + pressMoveX, iconRect.y + pressMoveY, iconRect.width, iconRect.height));
        g2d.setComposite(oldComposite);
    }

    protected void paintBackgroundImage(Graphics g, JComponent c) {
        if (c instanceof OnlyToggleButton) {
            OnlyToggleButton button = (OnlyToggleButton) c;
            ButtonModel model = button.getModel();
            Image image = button.getNormalImage();
            Image tempImage = null;

            if (image == null) {
                return;
            }

            if (!model.isEnabled()) {
                tempImage = model.isSelected() ? button.getDisabledSelectedImage() : button.getDisabledImage();
            } else if (model.isPressed() && model.isArmed()) {
                tempImage = button.getPressedImage();

                if (tempImage == null) {
                    tempImage = button.getSelectedImage();
                }
            } else if (model.isSelected()) {
                if (button.isRolloverEnabled() && model.isRollover()) {
                    tempImage = button.getRolloverSelectedImage();

                    if (tempImage == null) {
                        tempImage = button.getSelectedImage();
                    }
                } else {
                    tempImage = button.getSelectedImage();
                }
            } else if (button.isRolloverEnabled() && model.isRollover()) {
                tempImage = button.getRolloverImage();
            }

            if (tempImage != null) {
                image = tempImage;
            }

            Rectangle paintRect = new Rectangle(0, 0, button.getWidth(), button.getHeight());
            OnlyUIUtil.paintImage(g, image, button.getImageInsets(), paintRect, button);
        }
    }

    @Override
    protected void installDefaults(AbstractButton b) {
    }

    @Override
    protected void uninstallDefaults(AbstractButton b) {
    }
}