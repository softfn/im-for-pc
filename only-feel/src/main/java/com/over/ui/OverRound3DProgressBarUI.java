package com.over.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;

import javax.swing.JComponent;
import javax.swing.JProgressBar;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicProgressBarUI;

import sun.swing.SwingUtilities2;

import com.only.box.UIBox;
import com.over.OverRound3DProgressBar;

public class OverRound3DProgressBarUI extends BasicProgressBarUI {

    private static final float[] FRACTIONS = new float[]{0f, 0.55f, 1f};
    private static final int SPEED = 2;
    private Color[] colors = new Color[3];
    private Paint paint;
    private int startX, startY;

    public static ComponentUI createUI(JComponent c) {
        return new OverRound3DProgressBarUI();
    }

    protected void paintDeterminate(Graphics g, JComponent c) {
        if (!(g instanceof Graphics2D)) {
            return;
        }

        Insets insets = progressBar.getInsets();
        int barRectWidth = progressBar.getWidth() - (insets.right + insets.left);
        int barRectHeight = progressBar.getHeight() - (insets.top + insets.bottom);

        if (barRectWidth <= 0 || barRectHeight <= 0) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        Paint oldPaint = g2d.getPaint();
        RenderingHints oldHints = g2d.getRenderingHints();
        int amountFull = getAmountFull(insets, barRectWidth, barRectHeight);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        paintBackground(g2d, insets, barRectWidth, barRectHeight);

        if (progressBar.getOrientation() == JProgressBar.HORIZONTAL) {
            createColors(progressBar.getForeground());
            paint = new LinearGradientPaint(0, insets.top, 0, barRectHeight + insets.top, FRACTIONS, colors);
            g2d.setPaint(paint);
            g2d.fillRoundRect(insets.left, insets.top, amountFull, barRectHeight, barRectHeight, barRectHeight);
        } else {
            createColors(progressBar.getForeground());
            paint = new LinearGradientPaint(barRectWidth + insets.left, 0, insets.left, 0, FRACTIONS, colors);
            g2d.setPaint(paint);
            g2d.fillRoundRect(insets.left, insets.top + (barRectHeight - amountFull), barRectWidth, amountFull, barRectWidth, barRectWidth);
        }

        g2d.setPaint(oldPaint);
        g2d.setRenderingHints(oldHints);

        if (progressBar.isStringPainted()) {
            paintString(g2d, insets.left, insets.top, barRectWidth, barRectHeight, amountFull, insets);
        }
    }

    protected void paintIndeterminate(Graphics g, JComponent c) {
        if (!(g instanceof Graphics2D)) {
            return;
        }

        Insets insets = progressBar.getInsets();
        int barRectWidth = progressBar.getWidth() - (insets.right + insets.left);
        int barRectHeight = progressBar.getHeight() - (insets.top + insets.bottom);

        if (barRectWidth <= 0 || barRectHeight <= 0) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        Paint oldPaint = g2d.getPaint();
        RenderingHints oldHints = g2d.getRenderingHints();
        boxRect = getBox(boxRect);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        paintBackground(g2d, insets, barRectWidth, barRectHeight);

        if (boxRect != null) {
            if (progressBar.getOrientation() == JProgressBar.HORIZONTAL) {
                createColors(progressBar.getForeground());
                paint = new LinearGradientPaint(0, insets.top, 0, barRectHeight + insets.top, FRACTIONS, colors);
                g2d.setPaint(paint);
                g2d.fillRoundRect(boxRect.x, boxRect.y, boxRect.width, boxRect.height, barRectHeight, barRectHeight);
            } else {
                createColors(progressBar.getForeground());
                paint = new LinearGradientPaint(barRectWidth + insets.left, 0, insets.left, 0, FRACTIONS, colors);
                g2d.setPaint(paint);
                g2d.fillRoundRect(boxRect.x, boxRect.y, boxRect.width, boxRect.height, barRectWidth, barRectWidth);
            }
        }

        g2d.setPaint(oldPaint);
        g2d.setRenderingHints(oldHints);

        if (progressBar.isStringPainted()) {
            if (progressBar.getOrientation() == JProgressBar.HORIZONTAL) {
                paintString(g2d, insets.left, insets.top, barRectWidth, barRectHeight, boxRect.x, boxRect.width, insets);
            } else {
                paintString(g2d, insets.left, insets.top, barRectWidth, barRectHeight, boxRect.y, boxRect.height, insets);
            }
        }
    }

    private void paintBackground(Graphics2D g2d, Insets insets, int barRectWidth, int barRectHeight) {
        if (progressBar.getOrientation() == JProgressBar.HORIZONTAL) {
            createColors(progressBar.getBackground());
            paint = new LinearGradientPaint(0, insets.top, 0, barRectHeight + insets.top, FRACTIONS, colors);
            g2d.setPaint(paint);
            g2d.fillRoundRect(insets.left, insets.top, barRectWidth, barRectHeight, barRectHeight, barRectHeight);
        } else {
            createColors(progressBar.getBackground());
            paint = new LinearGradientPaint(barRectWidth + insets.left, 0, insets.left, 0, FRACTIONS, colors);
            g2d.setPaint(paint);
            g2d.fillRoundRect(insets.left, insets.top, barRectWidth, barRectHeight, barRectWidth, barRectWidth);
        }
    }

    protected void paintString(Graphics g, int x, int y, int width, int height, int amountFull, Insets b) {
        if (progressBar.getOrientation() == JProgressBar.HORIZONTAL) {
            if (progressBar.isIndeterminate()) {
                boxRect = getBox(boxRect);
                paintString(g, x, y, width, height, boxRect.x, boxRect.width, b);
            } else {
                paintString(g, x, y, width, height, x, amountFull, b);
            }
        } else {
            if (progressBar.isIndeterminate()) {
                boxRect = getBox(boxRect);
                paintString(g, x, y, width, height, boxRect.y, boxRect.height, b);
            } else {
                paintString(g, x, y, width, height, y + height - amountFull, amountFull, b);
            }
        }
    }

    private void paintString(Graphics g, int x, int y, int width, int height, int fillStart, int amountFull, Insets b) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(progressBar.getFont());
        String str = progressBar.getString();
        Point renderLocation = getStringPlacement(g2d, str, x, y, width, height);
        Rectangle oldClip = g2d.getClipBounds();
        boolean is3D = progressBar instanceof OverRound3DProgressBar;
        Color fontColor = is3D ? ((OverRound3DProgressBar) progressBar).getFontColor() : Color.ORANGE;
        Color fontCoverClor = is3D ? ((OverRound3DProgressBar) progressBar).getFontCoverClor() : UIBox.getWhiteColor();

        if (progressBar.getOrientation() == JProgressBar.HORIZONTAL) {
            g2d.setColor(fontColor);
            SwingUtilities2.drawString(progressBar, g2d, str, renderLocation.x, renderLocation.y);
            g2d.setColor(fontCoverClor);
            g2d.clipRect(fillStart, y, amountFull, height);
            SwingUtilities2.drawString(progressBar, g2d, str, renderLocation.x, renderLocation.y);
        } else {
            g2d.setColor(fontColor);
            AffineTransform rotate = AffineTransform.getRotateInstance(Math.PI / 2);
            g2d.setFont(progressBar.getFont().deriveFont(rotate));
            renderLocation = getStringPlacement(g2d, str, x, y, width, height);
            SwingUtilities2.drawString(progressBar, g2d, str, renderLocation.x, renderLocation.y);
            g2d.setColor(fontCoverClor);
            g2d.clipRect(x, fillStart, width, amountFull);
            SwingUtilities2.drawString(progressBar, g2d, str, renderLocation.x, renderLocation.y);
        }

        g2d.setClip(oldClip);
    }

    protected Rectangle getBox(Rectangle r) {
        Rectangle rect = super.getBox(r);

        if (rect != null) {
            if (progressBar.getOrientation() == JProgressBar.HORIZONTAL) {
                rect.x = startX;
            } else {
                rect.y = startY;
            }
        }

        return rect;
    }

    protected int getBoxLength(int availableLength, int otherDimension) {
        Insets insets = progressBar.getInsets();
        int barRectWidth = progressBar.getWidth() - (insets.right + insets.left);
        int barRectHeight = progressBar.getHeight() - (insets.top + insets.bottom);
        int step = getStep();
        int frameCount = getFrameCount() / SPEED;
        int currentFrame = getAnimationIndex() % frameCount;
        int baseLength = (int) Math.round(availableLength / 6.0);
        int boxLength = baseLength;

        if (progressBar.getOrientation() == JProgressBar.HORIZONTAL) {
            int endX = currentFrame * step;

            if (endX < baseLength) {
                startX = 0;
            } else {
                startX = endX - baseLength;

                if (endX > barRectWidth) {
                    startX = startX > barRectWidth ? barRectWidth : startX;
                    endX = barRectWidth;
                }
            }

            boxLength = endX - startX;
        } else {
            startY = barRectHeight - currentFrame * step;
            int endY;

            if (startY > barRectHeight - baseLength) {
                endY = barRectHeight;
            } else {
                endY = startY + baseLength;

                if (startY < 0) {
                    endY = endY < 0 ? 0 : endY;
                    startY = 0;
                }
            }

            boxLength = endY - startY;
        }

        return boxLength;
    }

    private int getStep() {
        Insets insets = progressBar.getInsets();
        double frameCount = getFrameCount() / (double) SPEED;
        int barRectWidth = progressBar.getWidth() - (insets.right + insets.left);
        int barRectHeight = progressBar.getHeight() - (insets.top + insets.bottom);
        int step = progressBar.getOrientation() == JProgressBar.HORIZONTAL ? barRectWidth : barRectHeight;
        step = (int) Math.round((step + step / 6.0) / frameCount);
        return step;
    }

    private void createColors(Color baseColor) {
        final double whiteRate = 0.75;
        final double otherRate = 1 - whiteRate;
        final double white = 255 * whiteRate;
        int red = (int) Math.round(baseColor.getRed() * otherRate + white);
        int green = (int) Math.round(baseColor.getGreen() * otherRate + white);
        int blue = (int) Math.round(baseColor.getBlue() * otherRate + white);
        colors[0] = colors[2] = new Color(red, green, blue);
        colors[1] = baseColor;
    }
}