package com.only.common;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.only.util.OnlyUIUtil;

public class WindowMove extends MouseAdapter implements ComponentListener {

	private Point point;
	private Window window;
	private Component component;
	private boolean moveable;
	private int locationLimit;
	private WindowAutoHide autoHide;

	public WindowMove(Component component) {
		this(component, null);
	}

	public WindowMove(Component component, WindowAutoHide autoHide) {
		this.moveable = true;
		this.point = new Point(-1, -1);
		changeComponent(component, autoHide);
	}

	public void changeComponent(Component component) {
		changeComponent(component, null);
	}

	public void changeComponent(Component component, WindowAutoHide autoHide) {
		if (this.component != null) {
			this.component.removeMouseMotionListener(this);
			this.component.removeMouseListener(this);
			this.window.removeComponentListener(this);
		}

		this.component = component;
		this.autoHide = autoHide;
		this.window = OnlyUIUtil.getWindowFromComponent(component);

		if (window != null) {
			component.addMouseMotionListener(this);
			component.addMouseListener(this);
			window.addComponentListener(this);
		}
	}

	public boolean isMoveable() {
		return moveable;
	}

	public void setMoveable(boolean moveable) {
		this.moveable = moveable;
	}

	public int getLocationLimit() {
		return this.locationLimit;
	}

	public void setLocationLimit(int locationLimit) {
		this.locationLimit = locationLimit;
	}

	public void mouseDragged(MouseEvent e) {
		boolean move = moveable;

		if (move && window instanceof Frame) {
			move = (((Frame) window).getExtendedState() & Frame.MAXIMIZED_BOTH) == 0;
		}

		if (move && point.x >= 0 && point.y >= 0) {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			Point locationOnScreen = e.getLocationOnScreen();
			int x = locationOnScreen.x - point.x;
			int y = locationOnScreen.y - point.y;
			int deltaWidth = screenSize.width - window.getWidth();
			int deltaHeight = screenSize.height - window.getHeight();

			if (x < 0 && (locationLimit & OnlyUIUtil.LOCATION_LIMIT_LEFT) != 0) {
				x = 0;
				point.move(locationOnScreen.x, point.y);
			}

			if (y < 0 && (locationLimit & OnlyUIUtil.LOCATION_LIMIT_TOP) != 0) {
				y = 0;
				point.move(point.x, locationOnScreen.y);
			}

			if (x > deltaWidth && (locationLimit & OnlyUIUtil.LOCATION_LIMIT_RIGHT) != 0) {
				x = deltaWidth;
				point.move(locationOnScreen.x - x, point.y);
			}

			if (y > deltaHeight && (locationLimit & OnlyUIUtil.LOCATION_LIMIT_BOTTOM) != 0) {
				y = deltaHeight;
				point.move(point.x, locationOnScreen.y - y);
			}

			window.setLocation(x, y);
		}
	}

	public void mousePressed(MouseEvent e) {
		if (moveable) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				point.move(e.getXOnScreen() - window.getX(), e.getYOnScreen() - window.getY());
			} else {
				point.move(-1, -1);
			}
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (moveable && e.getButton() == MouseEvent.BUTTON1) {
			point.move(-1, -1);
		}
	}

	public void componentMoved(ComponentEvent e) {
		if (window instanceof Frame && ((Frame) window).getExtendedState() != Frame.NORMAL) {
			return;
		}

		if (locationLimit > 0 && (autoHide == null || autoHide.limitLocationAllowed())) {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			Point location = window.getLocation();
			int deltaWidth = screenSize.width - window.getWidth();
			int deltaHeight = screenSize.height - window.getHeight();
			boolean reset = false;

			if (location.x < 0 && (locationLimit & OnlyUIUtil.LOCATION_LIMIT_LEFT) != 0) {
				location.x = 0;
				reset = true;
			}

			if (location.y < 0 && (locationLimit & OnlyUIUtil.LOCATION_LIMIT_TOP) != 0) {
				location.y = 0;
				reset = true;
			}

			if (location.x > deltaWidth && (locationLimit & OnlyUIUtil.LOCATION_LIMIT_RIGHT) != 0) {
				location.x = deltaWidth;
				reset = true;
			}

			if (location.y > deltaHeight && (locationLimit & OnlyUIUtil.LOCATION_LIMIT_BOTTOM) != 0) {
				location.y = deltaHeight;
				reset = true;
			}

			if (reset) {
				window.setLocation(location);
			}
		}
	}

	public void componentResized(ComponentEvent e) {
	}

	public void componentShown(ComponentEvent e) {
	}

	public void componentHidden(ComponentEvent e) {
	}
}