package com.only.common;

import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

import com.only.OnlyDialog;
import com.only.OnlyFrame;
import com.only.util.OnlyUIUtil;

/**
 * 描述：负责监听鼠标在窗口的位置，以便调整窗口的大小。
 * 
 * @author 夏辉
 * @date 2013年12月28日 下午4:00:31 version 0.0.1
 */
public class MouseInputHandler implements MouseInputListener {

	private static final Cursor DEFAULT_CURSOR = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
	private static final int CORNER_DRAG_WIDTH = 10;
	private static final int BORDER_DRAG_THICKNESS = 5;
	public static final int BLUR_SIZE = 30;
	private final int[] CURSOR_MAPPING = { Cursor.NW_RESIZE_CURSOR, 
			Cursor.NW_RESIZE_CURSOR, Cursor.N_RESIZE_CURSOR, 
			Cursor.NE_RESIZE_CURSOR, Cursor.NE_RESIZE_CURSOR, 
			Cursor.NW_RESIZE_CURSOR, 0, 0, 0, Cursor.NE_RESIZE_CURSOR, 
			Cursor.W_RESIZE_CURSOR, 0, 0, 0,
			Cursor.E_RESIZE_CURSOR, Cursor.SW_RESIZE_CURSOR, 0, 0, 0,
			Cursor.SE_RESIZE_CURSOR, Cursor.SW_RESIZE_CURSOR, 
			Cursor.SW_RESIZE_CURSOR, Cursor.S_RESIZE_CURSOR, 
			Cursor.SE_RESIZE_CURSOR, Cursor.SE_RESIZE_CURSOR };
	private boolean isMovingWindow;
	private boolean dragging;
	private int dragCursor;
	private int dragOffsetX;
	private int dragOffsetY;
	private int dragWidth;
	private int dragHeight;
	private Window window;
	private JRootPane root;

	public MouseInputHandler(Window window) {
		this.window = window;
		window.addMouseListener(this);
		window.addMouseMotionListener(this);
		if (window instanceof JFrame) {
			root = ((JFrame) window).getRootPane();
		}
		if (window instanceof JDialog) {
			root = ((JDialog) window).getRootPane();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (!SwingUtilities.isLeftMouseButton(e)) {
			return;
		}

		Point dragWindowOffset = e.getPoint();
		Window win = (Window) e.getSource();
		dragging = true;

		if (win != null) {
			win.toFront();
		}

		Frame frame = win instanceof Frame ? (Frame) win : null;
		Dialog dialog = win instanceof Dialog ? (Dialog) win : null;
		int frameState = (frame != null) ? frame.getExtendedState() : 0;

		if (((frame != null && (frameState & Frame.MAXIMIZED_BOTH) == 0) || dialog != null)
				&& (dragWindowOffset.y >= BORDER_DRAG_THICKNESS && dragWindowOffset.y < win.getHeight() - BORDER_DRAG_THICKNESS && dragWindowOffset.x >= BORDER_DRAG_THICKNESS && dragWindowOffset.x < win.getWidth() - BORDER_DRAG_THICKNESS)) {
			isMovingWindow = true;
			dragOffsetX = dragWindowOffset.x;
			dragOffsetY = dragWindowOffset.y;
		}

		if (!isMovingWindow && ((frame != null && frame.isResizable() && (frameState & Frame.MAXIMIZED_BOTH) == 0) || (dialog != null && dialog.isResizable()))) {
			dragOffsetX = dragWindowOffset.x;
			dragOffsetY = dragWindowOffset.y;
			dragWidth = win.getWidth();
			dragHeight = win.getHeight();
			dragCursor = getCursor(calculateCorner(win, dragWindowOffset.x, dragWindowOffset.y));
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (!SwingUtilities.isLeftMouseButton(e)) {
			return;
		}

		if (dragCursor != 0 && window != null && !window.isValid()) {
			window.validate();
			if (null != root) {
				root.repaint();
			}
		}

		dragging = false;
		isMovingWindow = false;
		dragCursor = 0;
		mouseMoved(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		Window win = (Window) e.getSource();
		Frame frame = win instanceof Frame ? (Frame) win : null;
		Dialog dialog = win instanceof Dialog ? (Dialog) win : null;
		int cursor = getCursor(calculateCorner(win, e.getX(), e.getY()));

		if (cursor != 0 && win.getBounds().contains(e.getLocationOnScreen()) && ((frame != null && (frame.isResizable() && (frame.getExtendedState() & Frame.MAXIMIZED_BOTH) == 0)) || (dialog != null && dialog.isResizable()))) {
			win.setCursor(Cursor.getPredefinedCursor(cursor));
		} else {
			win.setCursor(DEFAULT_CURSOR);
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (!dragging) {
			Window win = (Window) e.getSource();
			win.setCursor(DEFAULT_CURSOR);
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (!SwingUtilities.isLeftMouseButton(e)) {
			if (window.getBounds().contains(e.getPoint())) {
				mouseMoved(e);
			} else {
				mouseExited(e);
			}

			return;
		}

		Window win = (Window) e.getSource();
		Point point = e.getPoint();

		if (isMovingWindow) {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			Point eventLocationOnScreen = e.getLocationOnScreen();
			int x = eventLocationOnScreen.x - dragOffsetX;
			int y = eventLocationOnScreen.y - dragOffsetY;
			int locationLimit = 0;

			if (win instanceof OnlyFrame) {
				locationLimit = ((OnlyFrame) win).getLocationLimit();
			} else if (win instanceof OnlyDialog) {
				locationLimit = ((OnlyDialog) win).getLocationLimit();
			}

			if (locationLimit > 0) {
				int deltaWidth = screenSize.width - win.getWidth();
				int deltaHeight = screenSize.height - win.getHeight();

				if (x < 0 && (locationLimit & OnlyUIUtil.LOCATION_LIMIT_LEFT) != 0) {
					x = 0;
					dragOffsetX = e.getX() + 1;
				}

				if (y < 0 && (locationLimit & OnlyUIUtil.LOCATION_LIMIT_TOP) != 0) {
					y = 0;
					dragOffsetY = e.getY() + 1;
				}

				if (x > deltaWidth && (locationLimit & OnlyUIUtil.LOCATION_LIMIT_RIGHT) != 0) {
					x = deltaWidth;
					dragOffsetX = e.getX() - 1;
				}

				if (y > deltaHeight && (locationLimit & OnlyUIUtil.LOCATION_LIMIT_BOTTOM) != 0) {
					y = deltaHeight;
					dragOffsetY = e.getY() - 1;
				}
			}

			win.setLocation(x, y);
		} else if (dragCursor != 0) {
			Rectangle rect = win.getBounds();
			Rectangle startBounds = new Rectangle(rect);
			Dimension min = win.getMinimumSize();
			Dimension max = win.getMaximumSize();

			switch (dragCursor) {
			case Cursor.E_RESIZE_CURSOR: {
				adjust(rect, min, max, 0, 0, point.x + (dragWidth - dragOffsetX) - rect.width, 0);
				break;
			}
			case Cursor.S_RESIZE_CURSOR: {
				adjust(rect, min, max, 0, 0, 0, point.y + (dragHeight - dragOffsetY) - rect.height);
				break;
			}
			case Cursor.N_RESIZE_CURSOR: {
				adjust(rect, min, max, 0, point.y - dragOffsetY, 0, -(point.y - dragOffsetY));
				break;
			}
			case Cursor.W_RESIZE_CURSOR: {
				adjust(rect, min, max, point.x - dragOffsetX, 0, -(point.x - dragOffsetX), 0);
				break;
			}
			case Cursor.NE_RESIZE_CURSOR: {
				adjust(rect, min, max, 0, point.y - dragOffsetY, point.x + (dragWidth - dragOffsetX) - rect.width, -(point.y - dragOffsetY));
				break;
			}
			case Cursor.SE_RESIZE_CURSOR: {
				adjust(rect, min, max, 0, 0, point.x + (dragWidth - dragOffsetX) - rect.width, point.y + (dragHeight - dragOffsetY) - rect.height);
				break;
			}
			case Cursor.NW_RESIZE_CURSOR: {
				adjust(rect, min, max, point.x - dragOffsetX, point.y - dragOffsetY, -(point.x - dragOffsetX), -(point.y - dragOffsetY));
				break;
			}
			case Cursor.SW_RESIZE_CURSOR: {
				adjust(rect, min, max, point.x - dragOffsetX, 0, -(point.x - dragOffsetX), point.y + (dragHeight - dragOffsetY) - rect.height);
				break;
			}
			default: {
				break;
			}
			}

			if (!rect.equals(startBounds)) {
				win.setBounds(rect);

				if (Toolkit.getDefaultToolkit().isDynamicLayoutActive()) {
					win.validate();
					if (null != root) {
						root.repaint();
					}
				}
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (!SwingUtilities.isLeftMouseButton(e)) {
			return;
		}

		Window win = (Window) e.getSource();
		Frame frame = win instanceof Frame ? (Frame) win : null;
		Point point = e.getPoint();
		int clickCount = e.getClickCount();

		if (null != root) {
			if (clickCount % 2 == 0 && point.x <= 22 && point.y <= 22 && root.getWindowDecorationStyle() != JRootPane.NONE) {
				window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
				return;
			}
		} else {
			if (clickCount % 2 == 0 && point.x <= 22 && point.y <= 22) {
				window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
				return;
			}
		}

		if (frame == null) {
			return;
		}

		int state = frame.getExtendedState();
		Window fullWin = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getFullScreenWindow();

		if (frame.isResizable() && frame != fullWin && clickCount % 2 == 0) {
			if ((state & Frame.MAXIMIZED_BOTH) != 0) {
				frame.setExtendedState(state & ~Frame.MAXIMIZED_BOTH);
			} else {
				frame.setExtendedState(state | Frame.MAXIMIZED_BOTH);
			}
		}
	}

	private void adjust(Rectangle bounds, Dimension min, Dimension max, int deltaX, int deltaY, int deltaWidth, int deltaHeight) {
		bounds.x += deltaX;
		bounds.y += deltaY;
		bounds.width += deltaWidth;
		bounds.height += deltaHeight;

		if (min != null) {
			if (bounds.width < min.width) {
				int correction = min.width - bounds.width;

				if (deltaX != 0) {
					bounds.x -= correction;
				}

				bounds.width = min.width;
			}

			if (bounds.height < min.height) {
				int correction = min.height - bounds.height;

				if (deltaY != 0) {
					bounds.y -= correction;
				}

				bounds.height = min.height;
			}
		}

		if (max != null) {
			if (bounds.width > max.width) {
				int correction = max.width - bounds.width;

				if (deltaX != 0) {
					bounds.x -= correction;
				}

				bounds.width = max.width;
			}

			if (bounds.height > max.height) {
				int correction = max.height - bounds.height;

				if (deltaY != 0) {
					bounds.y -= correction;
				}

				bounds.height = max.height;
			}
		}
	}

	private int calculateCorner(Window win, int x, int y) {
		Insets insets = win.getInsets();
		int xPosition = calculatePosition(x - insets.left, win.getWidth() - insets.left - insets.right);
		int yPosition = calculatePosition(y - insets.top, win.getHeight() - insets.top - insets.bottom);

		if (xPosition == -1 || yPosition == -1) {
			return -1;
		}

		return yPosition * 5 + xPosition;
	}

	private int getCursor(int corner) {
		if (corner == -1) {
			return 0;
		}

		return CURSOR_MAPPING[corner];
	}

	private int calculatePosition(int spot, int width) {
		if (spot < BORDER_DRAG_THICKNESS) {
			return 0;
		} else if (spot < CORNER_DRAG_WIDTH) {
			return 1;
		} else if (spot >= (width - BORDER_DRAG_THICKNESS)) {
			return 4;
		} else if (spot >= (width - CORNER_DRAG_WIDTH)) {
			return 3;
		} else {
			return 2;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}
