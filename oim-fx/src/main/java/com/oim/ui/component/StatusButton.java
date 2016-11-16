/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oim.ui.component;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * date 2012-9-14 7:41:47
 *
 * @author XiaHui
 */
public class StatusButton extends JPanel {

  
	private static final long serialVersionUID = 1L;
	private Image image;
    private boolean isMouseEntered = false;
   // private boolean isMousePressed = false;
    private String text;
    private Font font;
    private int rx = 5;
    private int ry = 5;
    private Color lineColor = new Color(204, 204, 204, 200);
    private Color mouseEnteredcolor = new Color(165, 218, 175, 220);
    private Color borderColor = new Color(110, 110, 110);
    private Color backgroundColor = new Color(255, 255, 255, 80);
    private boolean drawBorder = false;
    private JLabel statusIconLabel = new JLabel();
    private JLabel iconLabel = new JLabel();
    private JLabel intervalLabel = new JLabel(" ");
    JPanel panel=new JPanel();
    public StatusButton() {
        initFunctionPanel();
        initEvent();
    }

    public StatusButton(ImageIcon icon) {
        image = (icon.getImage());
        initFunctionPanel();
        initEvent();

    }

    public StatusButton(String text) {
        setText(text);
        initEvent();
    }

    public StatusButton(ImageIcon icon, String text) {
        image = (icon.getImage());
        setText(text);
        initEvent();
    }

    private void initFunctionPanel() {
        this.setLayout(new java.awt.GridBagLayout());
        // this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setOpaque(false);
       
        panel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 2));
    	
        panel.add(statusIconLabel);
        panel.add(intervalLabel);
        panel.add(iconLabel);
        
        add(panel);
        
        
    }

    private void initEvent() {

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // TODO Auto-generated method stub
                isMouseEntered = true;
                drawBorder = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // TODO Auto-generated method stub
                isMouseEntered = false;
                drawBorder = false;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
               // isMousePressed = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
               // isMousePressed = false;
                repaint();
            }
        });
    }

    public void setImage(String icon) {
        if (null != icon && !"".equals(icon)) {
            image = Toolkit.getDefaultToolkit().createImage(icon);
        }
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void setFont(Font font) {
        this.font = font;
    }

    public void setRoundedCorner(int rx, int ry) {
        this.rx = rx;
        this.ry = ry;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(1));
        if (drawBorder) {//边框
            g2.setColor(borderColor);
            g2.drawRoundRect(0, 0, this.getWidth() - 1, this.getHeight() - 1, rx, ry);
        }
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(1));
        if (isMouseEntered) {//鼠标进入颜色
            g2.setPaint(backgroundColor);
            g2.fillRoundRect(1, 1, this.getWidth() - 2, this.getHeight() - 2, rx, ry);
        }








//        GradientPaint p = new GradientPaint(0, 0, new Color(0xffffff), 0, this.getHeight(), new Color(0xc8d2de));
//        g2.setPaint(p);
//        g2.fillRoundRect(0, 0, this.getWidth() - 1, this.getHeight() - 1, rx, ry);
//        if (isMousePressed == true) {//边框
//            Color frameColor = new Color(128, 205, 226);
//            g2.setColor(frameColor);
//            g2.drawRoundRect(0, 0, this.getWidth() - 1, this.getHeight() - 1, rx, ry);
//
//        } else {
//            Color frameColor = new Color(181, 176, 176);
//            g2.setColor(frameColor);
//            g2.drawRoundRect(0, 0, this.getWidth() - 1, this.getHeight() - 1, rx, ry);
//        }
//
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g2.setStroke(new BasicStroke(1));
//        if (isMousePressed == true) {//按下颜色
//            Color color = new Color(255, 255, 255, 125);
//            g2.setPaint(color);
//            g2.fillRoundRect(1, 1, this.getWidth() - 2, this.getHeight() - 2, rx, ry);
//        }


        if (null != text && !"".equals(text)) {
            if (null != font) {
                g2.setFont(font);
                g2.setColor(Color.BLACK);
                g.setFont(font);
                g.setColor(Color.BLACK);
            }
            TextLayout layout = new TextLayout(text, g2.getFont(), g2.getFontRenderContext());
            int h = (int) ((this.getHeight() - layout.getAscent() - layout.getDescent()) / 2 + layout.getAscent());
            int w = (int) ((this.getWidth() - layout.getAdvance() - layout.getDescent()) / 2 + layout.getAdvance());

            if (null != image) {
                int strx = (this.getWidth() - w - 2) + (image.getWidth(this) / 2);
                int x = (strx - (image.getWidth(this)));
                g.drawImage(image, x, (this.getHeight() - image.getHeight(this)) / 2, this);
                layout.draw(g2, strx, (this.getHeight() - layout.getAscent() - layout.getDescent()) / 2 + layout.getAscent());
            } else {
                g.drawString(text, this.getWidth() - w - 2, h);
            }
        } else {
            if (null != image) {
                g.drawImage(image, (this.getWidth() - image.getWidth(this)) / 2, (this.getHeight() - image.getHeight(this)) / 2, this);
            }
        }
    }

    /**
     * @return the lineColor
     */
    public Color getLineColor() {
        return lineColor;
    }

    /**
     * @param lineColor the lineColor to set
     */
    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    /**
     * @return the mouseEnteredcolor
     */
    public Color getMouseEnteredcolor() {
        return mouseEnteredcolor;
    }

    /**
     * @param mouseEnteredcolor the mouseEnteredcolor to set
     */
    public void setMouseEnteredcolor(Color mouseEnteredcolor) {
        this.mouseEnteredcolor = mouseEnteredcolor;
    }

    /**
     * @return the borderColor
     */
    public Color getBorderColor() {
        return borderColor;
    }

    /**
     * @param borderColor the borderColor to set
     */
    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    /**
     * @return the backgroundColor
     */
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * @param backgroundColor the backgroundColor to set
     */
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**
     * @return the drawBorder
     */
    public boolean isDrawBorder() {
        return drawBorder;
    }

    /**
     * @param drawBorder the drawBorder to set
     */
    public void setDrawBorder(boolean drawBorder) {
        this.drawBorder = drawBorder;
    }

    public void setStatusIcon(ImageIcon icon) {
        this.statusIconLabel.setIcon(icon);
    }

    public void setIcon(ImageIcon icon) {
        this.iconLabel.setIcon(icon);
    }
}
