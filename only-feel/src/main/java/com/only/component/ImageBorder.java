package com.only.component;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.border.AbstractBorder;

public class ImageBorder extends AbstractBorder
{
    private static final long serialVersionUID = 2081328874316180356L;

    private Image image;
    
    private int top, left, bottom, right;
    
    public ImageBorder(Image image)
    {
        this(image, 5, 5, 5, 5);
    }
    
    public ImageBorder(Image image, int top, int left, int bottom, int right)
    {
        this.image = image;
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
    }
    
    public Insets getBorderInsets(Component c)
    {
        return getBorderInsets();
    }
    
    public Insets getBorderInsets(Component c, Insets insets)
    {
        insets.left = left;
        insets.top = top;
        insets.right = right;
        insets.bottom = bottom;
        return insets;
    }
    
    public Insets getBorderInsets()
    {
        return new Insets(top, left, bottom, right);
    }

    public boolean isBorderOpaque()
    {
        return false;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
    {
        if(image == null)
        {
            return;
        }
        
        int imgWidth = image.getWidth(c);
        int imgHeight = image.getHeight(c);
        g.drawImage(image, x, y, x + left, y + top, 0, 0, left, top, c);
        g.drawImage(image, left + x, y, x + width - right, y + top, left, 0, imgWidth - right, top, c);
        g.drawImage(image, x + width - right, y, x + width, y + top, imgWidth - right, 0, imgWidth, top, c);
        g.drawImage(image, x, y + top, x + left, y + height - bottom, 0, top, left, imgHeight - bottom, c);
        g.drawImage(image, x + width - right, top + y, x + width, y + height - bottom, imgWidth - right, top, imgWidth,
                        imgHeight - bottom, c);
        g.drawImage(image, x, y + height - bottom, left + x, y + height, 0, imgHeight - bottom, left, imgHeight, c);
        g.drawImage(image, left + x, y + height - bottom, x + width - right, y + height, left, imgHeight - bottom,
                        imgWidth - right, imgHeight, c);
        g.drawImage(image, x + width - right, y + height - bottom, x + width, y + height, imgWidth - right, imgHeight -
                        bottom, imgWidth, imgHeight, c);
    }
}