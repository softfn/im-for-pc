package com.only.laf;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicScrollBarUI;

import com.only.OnlyButton;
import com.only.box.UIBox;

public class OnlyScrollBarUI extends BasicScrollBarUI implements MouseListener {
	// private static final int DEFAULT_SCROLLBAR_WIDTH = 14;

	private static final int DEFAULT_SCROLLBAR_WIDTH = 12;
	private static final Color BORDER_COLOR = UIBox.getColor(UIBox.key_color_scroll_bar_border);
	private static final Color BACKGROUND = UIBox.getColor(UIBox.key_color_scroll_bar_background);
	protected OnlyButton increaseButton;
	protected OnlyButton decreaseButton;
	private boolean pressed;
	private boolean mouseEntered = false;

	public static ComponentUI createUI(JComponent c) {
		return new OnlyScrollBarUI();
	}

	@Override
	protected JButton createIncreaseButton(int orientation) {
		if (increaseButton == null) {
			increaseButton = new OnlyButton();
			increaseButton.setFocusable(false);
			increaseButton.setRequestFocusEnabled(false);

			if (scrollbar.getOrientation() == VERTICAL) {
				increaseButton.setNormalImage(UIBox.getImage(UIBox.key_image_scroll_bar_down_normal));
				increaseButton.setRolloverImage(UIBox.getImage(UIBox.key_image_scroll_bar_down_rollover));
				increaseButton.setPressedImage(UIBox.getImage(UIBox.key_image_scroll_bar_down_pressed));
			} else {
				increaseButton.setNormalImage(UIBox.getImage(UIBox.key_image_scroll_bar_right_normal));
				increaseButton.setRolloverImage(UIBox.getImage(UIBox.key_image_scroll_bar_right_rollover));
				increaseButton.setPressedImage(UIBox.getImage(UIBox.key_image_scroll_bar_right_pressed));
			}

			Image image = increaseButton.getNormalImage();
			increaseButton.setPreferredSize(new Dimension(image.getWidth(null), image.getHeight(null)));
			increaseButton.addMouseListener(this);
		}

		return increaseButton;
	}

	@Override
	protected JButton createDecreaseButton(int orientation) {
		if (decreaseButton == null) {
			decreaseButton = new OnlyButton();
			decreaseButton.setFocusable(false);
			decreaseButton.setRequestFocusEnabled(false);

			if (scrollbar.getOrientation() == VERTICAL) {
				decreaseButton.setNormalImage(UIBox.getImage(UIBox.key_image_scroll_bar_up_normal));
				decreaseButton.setRolloverImage(UIBox.getImage(UIBox.key_image_scroll_bar_up_rollover));
				decreaseButton.setPressedImage(UIBox.getImage(UIBox.key_image_scroll_bar_up_pressed));
			} else {
				decreaseButton.setNormalImage(UIBox.getImage(UIBox.key_image_scroll_bar_left_normal));
				decreaseButton.setRolloverImage(UIBox.getImage(UIBox.key_image_scroll_bar_left_rollover));
				decreaseButton.setPressedImage(UIBox.getImage(UIBox.key_image_scroll_bar_left_pressed));
			}

			Image image = decreaseButton.getNormalImage();
			decreaseButton.setPreferredSize(new Dimension(image.getWidth(null), image.getHeight(null)));
			decreaseButton.addMouseListener(this);
		}

		return decreaseButton;
	}

	@Override
	protected void installListeners() {
		super.installListeners();
		scrollbar.addMouseListener(this);
	}

	protected void uninstallListeners() {
		super.uninstallListeners();
		scrollbar.removeMouseListener(this);
	}

	private Image getThumbImage() {
		Image image = null;
		boolean vertical = scrollbar.getOrientation() == VERTICAL;

		if (pressed) {
			image = UIBox.getImage(vertical ? UIBox.key_image_scroll_bar_v_pressed : UIBox.key_image_scroll_bar_h_pressed);
		} else if (isThumbRollover()) {
			image = UIBox.getImage(vertical ? UIBox.key_image_scroll_bar_v_rollover : UIBox.key_image_scroll_bar_h_rollover);
		} else {
			image = UIBox.getImage(vertical ? UIBox.key_image_scroll_bar_v_normal : UIBox.key_image_scroll_bar_h_normal);
		}

		return image;
	}

	public Dimension getPreferredSize(JComponent c) {
		if (scrollbar.getOrientation() == VERTICAL) {
			return new Dimension(DEFAULT_SCROLLBAR_WIDTH, DEFAULT_SCROLLBAR_WIDTH * 3 + 10);
		} else {
			return new Dimension(DEFAULT_SCROLLBAR_WIDTH * 3 + 10, DEFAULT_SCROLLBAR_WIDTH);
		}
	}

	protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
		if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
			return;
		}

		Image image = getThumbImage();
		int width = thumbBounds.width;
		int height = thumbBounds.height;
		int imageWidth = image.getWidth(scrollbar);
		int imageHeight = image.getHeight(scrollbar);
		g.translate(thumbBounds.x, thumbBounds.y);

		// if (scrollbar.getOrientation() == VERTICAL) {
		// g.drawImage(image, 0, 0, width, 3, 0, 0, imageWidth, 3, null);
		// g.drawImage(image, 0, 3, width, height - 3, 0, 3, imageWidth,
		// imageHeight - 3, null);
		// g.drawImage(image, 0, height - 3, width, height, 0, imageHeight - 3,
		// imageWidth, imageHeight, null);
		// } else {
		// g.drawImage(image, 0, 0, 3, height, 0, 0, 3, imageHeight, null);
		// g.drawImage(image, 3, 0, width - 3, height, 3, 0, imageWidth - 3,
		// imageHeight, null);
		// g.drawImage(image, width - 3, 0, width, height, imageWidth - 3, 0,
		// imageWidth, imageHeight, null);
		// }

		if (this.scrollbar.getOrientation() == VERTICAL) {
			Graphics2D g2d = (Graphics2D) g;
			Object oldHintValue = g2d.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
			g2d.drawImage(image, 0, 0, width, 5, 0, 0, imageWidth, 5, null);
			g2d.drawImage(image, 0, 5, width, height - 5, 0, 5, imageWidth, imageHeight - 5, null);
			g2d.drawImage(image, 0, height - 5, width, height, 0, imageHeight - 5, imageWidth, imageHeight, null);
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldHintValue);

		} else {
			Graphics2D g2d = (Graphics2D) g;
			Object oldHintValue = g2d.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.drawImage(image, 0, 0, 6, height, 0, 0, 6, imageHeight, null);
			g2d.drawImage(image, 6, 0, width - 6, height, 6, 0, imageWidth - 5, imageHeight, null);
			g2d.drawImage(image, width - 6, 0, width, height, imageWidth - 6, 0, imageWidth, imageHeight, null);
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldHintValue);
		}

		g.translate(-thumbBounds.x, -thumbBounds.y);
	}

	@Override
	protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {

		if (pressed || mouseEntered) {
			if (scrollbar.getOrientation() == VERTICAL) {
				g.setColor(BORDER_COLOR);
				g.drawRect(trackBounds.x, trackBounds.y, trackBounds.width - 1, trackBounds.height - 1);
				g.setColor(BACKGROUND);
				g.fillRect(trackBounds.x + 1, trackBounds.y, trackBounds.width - 2, trackBounds.height);
			} else {
				g.setColor(BORDER_COLOR);
				g.drawRect(trackBounds.x, trackBounds.y, trackBounds.width - 1, trackBounds.height - 1);
				g.setColor(BACKGROUND);
				g.fillRect(trackBounds.x, trackBounds.y + 1, trackBounds.width, trackBounds.height - 2);
			}
		}
	}

	public boolean getSupportsAbsolutePositioning() {
		return true;
	}

	protected void installDefaults() {
		minimumThumbSize = new Dimension(DEFAULT_SCROLLBAR_WIDTH, DEFAULT_SCROLLBAR_WIDTH);
		maximumThumbSize = new Dimension(4096, 4096);
		trackHighlight = NO_HIGHLIGHT;

		if (scrollbar.getLayout() == null || (scrollbar.getLayout() instanceof UIResource)) {
			scrollbar.setLayout(this);
		}
	}

	protected void uninstallDefaults() {
	}

	public void mousePressed(MouseEvent e) {
		boolean flag = SwingUtilities.isRightMouseButton(e) || (!getSupportsAbsolutePositioning() && SwingUtilities.isMiddleMouseButton(e));

		if (!flag && getThumbBounds().contains(e.getPoint())) {
			boolean oldPress = pressed;
			pressed = true;

			if (oldPress != pressed) {
				scrollbar.repaint();
			}
		}
	}

	public void mouseReleased(MouseEvent e) {
		pressed = false;
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
		// if (getThumbBounds().contains(e.getPoint())) {
		// boolean oldMouseEntered = mouseEntered;
		// mouseEntered=true;
		//
		// if (oldMouseEntered != mouseEntered) {
		// scrollbar.repaint();
		// }
		// }

	}

	public void mouseExited(MouseEvent e) {
		// boolean oldMouseEntered = mouseEntered;
		// mouseEntered=false;
		//
		// if (oldMouseEntered != mouseEntered) {
		// scrollbar.repaint();
		// }
		//
	}
}