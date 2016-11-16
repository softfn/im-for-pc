/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.only.laf;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import javax.swing.GrayFilter;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPanelUI;

import sun.swing.ImageIconUIResource;

import com.only.OnlyPanel;
import com.only.common.ImageDisplayMode;
import com.only.common.StackBlurFilter;
import com.only.util.OnlyUIUtil;

/**
 * 
 * @author XiaHui
 */
public class OnlyPanelUI extends BasicPanelUI {

	private Image bottomEdge;
	private BufferedImage edgeBlurImage;
	private Image rightEdge;
	public static final StackBlurFilter BLUR_FILTER = new StackBlurFilter(100, 15);
	private Insets borderInsets = new Insets(1, 1, 1, 1);
	Rectangle rectangle = new Rectangle();

	public static ComponentUI createUI(JComponent c) {
		return new OnlyPanelUI();
	}

	@Override
	public void installUI(JComponent c) {
		if (c instanceof OnlyPanel) {
			OnlyPanel panel = (OnlyPanel) c;
			Image backgroundImage = panel.getBackgroundImage();
			Insets backgroundInsets = panel.getBackgroundInsets();
			if (null != backgroundImage && null != backgroundInsets) {
				panel.setOpaque(false);
			}
		}
		super.installUI(c);
	}

	public static BufferedImage imageToBufferImage(Image image) {

		BufferedImage bufferedImage = null;
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		if (null != image) {
			try {
				int transparency = Transparency.TRANSLUCENT;
				GraphicsDevice gs = ge.getDefaultScreenDevice();
				GraphicsConfiguration gc = gs.getDefaultConfiguration();
				bufferedImage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
				int type = BufferedImage.TYPE_INT_RGB;
				bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
				Graphics g = bufferedImage.createGraphics();
				g.drawImage(image, 0, 0, null);
				g.dispose();
			} catch (HeadlessException e) {
				e.printStackTrace();
			}
		}

		return bufferedImage;
	}

	public static BufferedImage getRoundedCornerBufferedImage(Image backgroundImage, int width, int height, int cornersWidth, int cornerHeight) {
		BufferedImage image = imageToBufferImage(backgroundImage);
		int w = image.getWidth();
		int h = image.getHeight();
		if (0 != width && 0 < width) {
			w = width;
		}
		if (0 != height && 0 < height) {
			h = height;
		}

		BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = output.createGraphics();
		g2.setComposite(AlphaComposite.Src);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		// g2.setColor(new Color(0,0,0));
		// g2.setBackground(Color);
		g2.setStroke(new BasicStroke(1));
		g2.fillRoundRect(0, 0, w, h, cornersWidth, cornerHeight);
		// g2.setComposite(AlphaComposite.Src);
		// g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornersWidth,
		// cornerHeight));
		g2.setComposite(AlphaComposite.SrcAtop);
		// g2.setColor(Color.white);//这里设置背景颜色
		// g2.fillRect(0, 0, w, h);//这里填充背景颜色
		g2.drawImage(image, 0, 0, w, h, null);
		g2.dispose();
		return output;

	}

	@Override
	public void paint(Graphics g, JComponent c) {
		if (c instanceof OnlyPanel) {
			OnlyPanel panel = (OnlyPanel) c;
			boolean borderPainted = panel.isBorderPainted();
			ImageDisplayMode mode = panel.getImageDisplayMode();
			float imageAlpha = panel.getImageAlpha();
			int roundBorder = panel.getRoundBorder();
			Image backgroundImage = panel.getBackgroundImage();
			// Insets backgroundInsets = panel.getBackgroundInsets();
			boolean enabled = panel.isEnabled();
			int width = c.getWidth();
			int height = c.getHeight();
			if (!enabled && null != backgroundImage) {
				backgroundImage = new ImageIconUIResource(GrayFilter.createDisabledImage(((new ImageIcon(backgroundImage))).getImage())).getImage();
				// backgroundImage =
				// GrayFilter.createDisabledImage(backgroundImage);
			}

			if ((backgroundImage != null) && (imageAlpha > 0.0F)) {
				Graphics2D g2d = (Graphics2D) g;
				Composite oldComposite = g2d.getComposite();
				if (imageAlpha < 1.0F) {
					g2d.setComposite(AlphaComposite.SrcOver.derive(imageAlpha));
				}
				paintBackgroundImage(g, c, backgroundImage, mode, 0, roundBorder);
				// g2d.setComposite(AlphaComposite.SrcOver.derive(0.0f));
				g2d.setComposite(oldComposite);
				// paintBackgroundImage(g, c, backgroundImage, mode, 0);
				// g2d.setComposite(oldComposite);
			}

			if (borderPainted) {
				rectangle.setRect(0, 0, width, height);
				if (null != panel.getBorderImage()) {
					// paintImage(g, panel.getBorderImage(), new Insets(6, 6, 6,
					// 6), new Rectangle(0, 0, c.getWidth(), c.getHeight()), c);
					paintImage(g, panel.getBorderImage(), ((panel.getBorderInsets() != null) ? panel.getBorderInsets() : borderInsets), rectangle, c);
					// paintImage(g, panel.getBorderImage(),
					// panel.getBorderInsets(), new
					// Rectangle(panel.getBorderX(), panel.getBorderY(),
					// c.getWidth()-(panel.getBorderX()*2),
					// c.getHeight()-(panel.getBorderY()*2)), c);
				} else {
					Graphics2D g2 = (Graphics2D) g;
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2.setStroke(new BasicStroke((panel.getBorderSize() > 0) ? panel.getBorderSize() : 1));
					g2.setColor(panel.getBorderColor());
					g2.drawRoundRect(0, 0, c.getWidth() - 1, c.getHeight() - 1, panel.getRoundBorder(), panel.getRoundBorder());
				}
			} else {
				super.paint(g, c);
			}
		}
	}

	public Icon getDisabledIcon(Icon icon) {
		if (icon instanceof ImageIcon) {
			return new ImageIconUIResource(GrayFilter.createDisabledImage(((ImageIcon) icon).getImage()));
		}
		return null;
	}

	protected void paintBackgroundImage(Graphics g, JComponent c, Image image, ImageDisplayMode mode, int titleHeight, int corners) {
		int width = c.getWidth();
		int height = c.getHeight() - titleHeight;
		int imageWidth = image.getWidth(null);
		int imageHeight = image.getHeight(null);
		g.translate(0, titleHeight);

		if (mode == ImageDisplayMode.scaled) {
			if (0 >= corners) {
				g.drawImage(image, 0, 0, width, height, c);
			} else {
				g.drawImage(getRoundedCornerBufferedImage(image, width, height, corners, corners), 0, 0, width, height, c);
			}

		} else if (mode == ImageDisplayMode.tiled) {
			for (int x = 0; x < width; x += imageWidth) {
				for (int y = 0; y < height; y += imageHeight) {
					g.drawImage(image, x, y, c);
				}
			}
		} else {
			if (this.edgeBlurImage == null) {
				this.edgeBlurImage = OnlyUIUtil.createEdgeBlurryImage(image, 30, BLUR_FILTER, c);
			}

			imageWidth = this.edgeBlurImage.getWidth();
			imageHeight = this.edgeBlurImage.getHeight();

			if ((width > imageWidth) || (height > imageHeight)) {
				this.rightEdge = OnlyUIUtil.cutImage(this.edgeBlurImage, new Rectangle(imageWidth - 1, 0, 1, imageHeight), c);
				this.bottomEdge = OnlyUIUtil.cutImage(this.edgeBlurImage, new Rectangle(0, imageHeight - 1, imageWidth, 1), c);
				width = Math.max(width, imageWidth);
				height = Math.max(height, imageHeight);
				BufferedImage tempBlurImage = OnlyUIUtil.getGraphicsConfiguration(c).createCompatibleImage(width, height, 3);

				Graphics2D tempG2d = tempBlurImage.createGraphics();
				tempG2d.drawImage(this.edgeBlurImage, 0, 0, c);
				if (width > imageWidth) {
					int x1 = imageWidth;
					int y2 = Math.min(height, imageHeight);

					for (int i = 0; i < width - imageWidth; i++) {
						tempG2d.drawImage(this.rightEdge, x1, 0, x1 + 1, y2, 0, 0, 1, Math.min(height, imageHeight), c);
						x1++;
						if (y2 >= height) {
							continue;
						}
						y2++;
					}
				}
				if (height > imageHeight) {
					int x2 = Math.min(width, imageWidth + 1);
					int y1 = imageHeight;

					for (int i = 0; i < height - imageHeight; i++) {
						tempG2d.drawImage(this.bottomEdge, 0, y1, x2, y1 + 1, 0, 0, Math.min(width, imageWidth), 1, c);
						y1++;
						if (x2 >= width) {
							continue;
						}
						x2++;
					}
				}
				tempG2d.dispose();
				this.edgeBlurImage = tempBlurImage;
				tempBlurImage = null;

			} else {
			}

			if (0 >= corners) {
				g.drawImage(this.edgeBlurImage, 0, 0, width, height, c);
			} else {
				g.drawImage(getRoundedCornerBufferedImage(this.edgeBlurImage, width, height, corners, corners), 0, 0, width, height, c);
			}
			// g.drawImage(this.edgeBlurImage, 0, 0, width, height, c);

			// if (0 >= corners) {
			// g.drawImage(this.edgeBlurImage, 0, 0, width, height, c);
			// } else {
			// g.drawImage(getRoundedCornerBufferedImage(this.edgeBlurImage,
			// width, height, corners, corners), 0, 0, c);
			// }
		}
		g.translate(0, -titleHeight);
	}

	public void paintImage(Graphics g, Image image, Insets imageInsets, Rectangle paintRect, ImageObserver observer) {
		int x = paintRect.x;
		int y = paintRect.y;
		int width = paintRect.width;
		int height = paintRect.height;

		// int x = 0;
		// int y = 0;
		// int width = 100;
		// int height = 100;

		if (!(width > 0 && height > 0 && x + width > 0 && y + height > 0)) {
			return;
		}

		Graphics2D g2d = (Graphics2D) g;
		// Object oldHintValue =
		// g2d.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
		int imageLeft = imageInsets.left;
		int imageRight = imageInsets.right;
		int imageTop = imageInsets.top;
		int imageBottom = imageInsets.bottom;

		// int imageLeft = 5;
		// int imageRight = 5;
		// int imageTop = 5;
		// int imageBottom = 5;

		int imageWidth = image.getWidth(observer);
		int imageHeight = image.getHeight(observer);

		// g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		// RenderingHints.VALUE_ANTIALIAS_ON);
		// g2d.translate(x, y);
		// Top
		g2d.drawImage(image, 0, 0, imageLeft, imageTop, 0, 0, imageLeft, imageTop, observer);
		g2d.drawImage(image, imageLeft, 0, width - imageRight, imageTop, imageLeft, 0, imageWidth - imageRight, imageTop, observer);
		g2d.drawImage(image, width - imageRight, 0, width, imageTop, imageWidth - imageRight, 0, imageWidth, imageTop, observer);

		// Middle
		g2d.drawImage(image, 0, imageTop, imageLeft, height - imageBottom, 0, imageTop, imageLeft, imageHeight - imageBottom, observer);
		g2d.drawImage(image, imageLeft, imageTop, width - imageRight, height - imageBottom, imageLeft, imageTop, imageWidth - imageRight, imageHeight - imageBottom, observer);
		g2d.drawImage(image, width - imageRight, imageTop, width, height - imageBottom, imageWidth - imageRight, imageTop, imageWidth, imageHeight - imageBottom, observer);

		// Bottom
		g2d.drawImage(image, 0, height - imageBottom, imageLeft, height, 0, imageHeight - imageBottom, imageLeft, imageHeight, observer);
		g2d.drawImage(image, imageLeft, height - imageBottom, width - imageRight, height, imageLeft, imageHeight - imageBottom, imageWidth - imageRight, imageHeight, observer);
		g2d.drawImage(image, width - imageRight, height - imageBottom, width, height, imageWidth - imageRight, imageHeight - imageBottom, imageWidth, imageHeight, observer);
		// g2d.translate(-x, -y);
		// g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldHintValue);
	}
}
