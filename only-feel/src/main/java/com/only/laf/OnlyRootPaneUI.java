package com.only.laf;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.LayoutManager2;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.basic.BasicRootPaneUI;

import com.only.OnlyDialog;
import com.only.OnlyFrame;
import com.only.box.UIBox;
import com.only.common.ImageDisplayMode;
import com.only.common.StackBlurFilter;
import com.only.component.ImagePane;
import com.only.util.OnlyUIUtil;

public class OnlyRootPaneUI extends BasicRootPaneUI {

	private static final Border MAX_BORDER = new EmptyBorder(-2, -2, -2, -2);
	private static final Image BG_IMAGE = UIBox.getImage(UIBox.key_image_window_background);
	private static final Image BG_IMAGE_MAX = OnlyUIUtil.cutImage(BG_IMAGE, new Rectangle(2, 2, BG_IMAGE.getWidth(null) - 4, BG_IMAGE.getHeight(null) - 4), null);
	private static final Image TITLE_IMAGE = UIBox.getImage(UIBox.key_image_window_title);
	public static final StackBlurFilter BLUR_FILTER = new StackBlurFilter(100, 15);
	private static final Cursor DEFAULT_CURSOR = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
	private static final int CORNER_DRAG_WIDTH = 10;
	private static final int BORDER_DRAG_THICKNESS = 5;
	public static final int BLUR_SIZE = 30;
	private Window window;
	private JComponent titlePane;
	private MouseInputListener mouseInputListener;
	private ComponentListener componentListener;
	private LayoutManager layoutManager;
	private LayoutManager savedOldLayout;
	private JRootPane root;
	private BufferedImage edgeBlurImage;
	private Image rightEdge;
	private Image bottomEdge;
	private Insets borderInsets = new Insets(5, 5, 5, 5);
	Rectangle rectangle = new Rectangle();

	public static ComponentUI createUI(JComponent c) {
		return new OnlyRootPaneUI();
	}

	@Override
	public void installUI(JComponent c) {
		super.installUI(c);
		root = (JRootPane) c;
		Container parent = root.getParent();

		if (parent instanceof Window) {
			window = (Window) parent;
		} else {
			window = SwingUtilities.getWindowAncestor(parent);
		}

		installClientDecorations(root);
	}

	@Override
	public void uninstallUI(JComponent c) {
		super.uninstallUI(c);
		uninstallClientDecorations(root);
		layoutManager = null;
		mouseInputListener = null;
		componentListener = null;
		root = null;

		if (window != null) {
			window.setCursor(DEFAULT_CURSOR);
		}

		window = null;
	}

	void installBorder(JRootPane root) {
		root.setBorder(null);
	}

	void uninstallBorder(JRootPane root) {
		root.setBorder(MAX_BORDER);
	}

	private void installWindowListeners(JRootPane root) {
		if (window != null) {
			if (mouseInputListener == null) {
				mouseInputListener = createWindowMouseInputListener(root);
			}

			if (componentListener == null) {
				componentListener = createWindowComponentListener(root);
			}

			window.addMouseListener(mouseInputListener);
			window.addMouseMotionListener(mouseInputListener);
			window.addComponentListener(componentListener);
		}
	}

	private void uninstallWindowListeners(JRootPane root) {
		if (window != null) {
			window.removeMouseListener(mouseInputListener);
			window.removeMouseMotionListener(mouseInputListener);
			window.removeComponentListener(componentListener);
		}
	}

	private void installLayout(JRootPane root) {
		if (layoutManager == null) {
			layoutManager = createLayoutManager();
		}

		savedOldLayout = root.getLayout();
		root.setLayout(layoutManager);
	}

	private void uninstallLayout(JRootPane root) {
		if (savedOldLayout != null) {
			root.setLayout(savedOldLayout);
			savedOldLayout = null;
		}
	}

	private void installClientDecorations(JRootPane root) {
		installBorder(root);

		if (root.getWindowDecorationStyle() != JRootPane.NONE) {
			setTitlePane(root, createTitlePane(root));
		}

		installWindowListeners(root);
		installLayout(root);

		if (window != null) {
			root.revalidate();
			root.repaint();
		}
	}

	private void uninstallClientDecorations(JRootPane root) {
		uninstallBorder(root);
		uninstallWindowListeners(root);
		setTitlePane(root, null);
		uninstallLayout(root);
		root.repaint();
		root.revalidate();
	}

	public void updateLayout(JRootPane root) {
		layoutManager = createLayoutManager();
		savedOldLayout = root.getLayout();
		root.setLayout(layoutManager);
	}

	private JComponent createTitlePane(JRootPane root) {
		return new OnlyTitlePane(root, this);
	}

	private MouseInputListener createWindowMouseInputListener(JRootPane root) {
		return new MouseInputHandler();
	}

	private ComponentListener createWindowComponentListener(JRootPane root) {
		return new ComponentHandler();
	}

	public LayoutManager createLayoutManager() {
		return new OnlyRootLayout();
	}

	private void setTitlePane(JRootPane root, JComponent titlePane) {
		JLayeredPane layeredPane = root.getLayeredPane();

		if (this.titlePane != null) {
			this.titlePane.setVisible(false);
			layeredPane.remove(this.titlePane);
		}

		if (titlePane != null) {
			// layeredPane.add(titlePane, JLayeredPane.FRAME_CONTENT_LAYER);
			layeredPane.add(titlePane);
			titlePane.setVisible(true);
		}

		this.titlePane = titlePane;
	}

	@Override
	public void paint(Graphics g, JComponent c) {

		boolean borderPainted = false;
		ImageDisplayMode mode = null;
		boolean titleOpaque = root.getWindowDecorationStyle() != JRootPane.NONE;
		Image image = null;
		float imageAlpha = 1.0f;

		if (window instanceof OnlyFrame) {
			OnlyFrame frame = (OnlyFrame) window;
			image = frame.getBackgroundImage();
			borderPainted = frame.isBorderPainted();
			mode = frame.getImageDisplayMode();
			titleOpaque = titleOpaque && frame.isTitleOpaque();
			imageAlpha = frame.getImageAlpha();
		} else if (window instanceof OnlyDialog) {
			OnlyDialog dialog = (OnlyDialog) window;
			image = dialog.getBackgroundImage();
			borderPainted = dialog.isBorderPainted();
			mode = dialog.getImageDisplayMode();
			titleOpaque = titleOpaque && dialog.isTitleOpaque();
			imageAlpha = dialog.getImageAlpha();
		}

		int titleHeight = titleOpaque ? root.getInsets().top + titlePane.getHeight() : 0;

		if (titleOpaque && titleHeight > 0) {
			Rectangle rect = new Rectangle(0, 0, root.getWidth(), titleHeight);
			OnlyUIUtil.paintImage(g, TITLE_IMAGE, new InsetsUIResource(5, 3, 1, 3), rect, c);
		}

		if (image != null && imageAlpha > 0.0f) {
			Graphics2D g2d = (Graphics2D) g;
			Composite oldComposite = g2d.getComposite();

			if (imageAlpha < 1.0f) {
				g2d.setComposite(AlphaComposite.SrcOver.derive(imageAlpha));
			}

			paintBackgroundImage(g, c, image, mode, titleHeight);
			g2d.setComposite(oldComposite);
		} else {
			Color backgroundColor = c.getBackground();

			if (null != backgroundColor && backgroundColor.getAlpha() > 0) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setPaint(backgroundColor);
				g2.fillRect(0, 0, c.getWidth(), c.getHeight());
			}
		}

		if (borderPainted) {

			rectangle.setRect(0, 0, c.getWidth(), c.getHeight());
			//rectangle.setRect(-5, -5, c.getWidth() + 10, c.getHeight() + 10);

			Frame frame = window instanceof Frame ? (Frame) window : null;
			OnlyDialog dialog = window instanceof OnlyDialog ? (OnlyDialog) window : null;

			if ((null != dialog) || ((frame != null && (frame.getExtendedState() & Frame.MAXIMIZED_BOTH) == 0))) {
				OnlyUIUtil.paintImage(g, BG_IMAGE, borderInsets, rectangle, c);
			} else {
				OnlyUIUtil.paintImage(g, BG_IMAGE_MAX, borderInsets, rectangle, c);
			}
			// if (frame == null || (frame != null && (frame.getExtendedState()
			// & Frame.MAXIMIZED_BOTH) == 0)) {
			// UIUtil.paintImage(g, BG_IMAGE, new Insets(3, 3, 3, 3), new
			// Rectangle(0, 0, c.getWidth(), c.getHeight()), c);
			// } else {
			// UIUtil.paintImage(g, BG_IMAGE_MAX, new Insets(1, 1, 1, 1), new
			// Rectangle(0, 0, c.getWidth(), c.getHeight()), c);
			// }
		}
	}

	protected void paintBackgroundImage(Graphics g, JComponent c, Image image, ImageDisplayMode mode, int titleHeight) {
		int width = c.getWidth();
		int height = c.getHeight() - titleHeight;
		int imageWidth = image.getWidth(null);
		int imageHeight = image.getHeight(null);
		g.translate(0, titleHeight);

		if (mode == ImageDisplayMode.scaled) {
			g.drawImage(image, 0, 0, width, height, c);
		} else if (mode == ImageDisplayMode.tiled) {
			for (int x = 0; x < width; x += imageWidth) {
				for (int y = 0; y < height; y += imageHeight) {
					g.drawImage(image, x, y, c);
				}
			}
		} else {
			if (edgeBlurImage == null) {
				edgeBlurImage = OnlyUIUtil.createEdgeBlurryImage(image, BLUR_SIZE, BLUR_FILTER, c);
			}

			imageWidth = edgeBlurImage.getWidth();
			imageHeight = edgeBlurImage.getHeight();

			if (width > imageWidth || height > imageHeight) {
				rightEdge = OnlyUIUtil.cutImage(edgeBlurImage, new Rectangle(imageWidth - 1, 0, 1, imageHeight), c);
				bottomEdge = OnlyUIUtil.cutImage(edgeBlurImage, new Rectangle(0, imageHeight - 1, imageWidth, 1), c);
				width = Math.max(width, imageWidth);
				height = Math.max(height, imageHeight);
				BufferedImage tempBlurImage = OnlyUIUtil.getGraphicsConfiguration(c).createCompatibleImage(width, height, Transparency.TRANSLUCENT);
				Graphics2D tempG2d = tempBlurImage.createGraphics();
				tempG2d.drawImage(edgeBlurImage, 0, 0, c);

				// draw right edge
				if (width > imageWidth) {
					int x1 = imageWidth;
					int y2 = Math.min(height, imageHeight);

					for (int i = 0; i < width - imageWidth; i++) {
						tempG2d.drawImage(rightEdge, x1, 0, x1 + 1, y2, 0, 0, 1, Math.min(height, imageHeight), c);
						x1++;

						if (y2 < height) {
							y2++;
						}
					}
				}

				// draw bottom edge
				if (height > imageHeight) {
					int x2 = Math.min(width, imageWidth + 1);
					int y1 = imageHeight;

					for (int i = 0; i < height - imageHeight; i++) {
						tempG2d.drawImage(bottomEdge, 0, y1, x2, y1 + 1, 0, 0, Math.min(width, imageWidth), 1, c);
						y1++;

						if (x2 < width) {
							x2++;
						}
					}
				}

				tempG2d.dispose();
				edgeBlurImage = tempBlurImage;
				tempBlurImage = null;
			}

			g.drawImage(edgeBlurImage, 0, 0, c);
		}

		g.translate(0, -titleHeight);
	}

	public void removeBlurImage() {
		edgeBlurImage = null;
		rightEdge = null;
		bottomEdge = null;
	}

	public void setEdgeBlurImage(BufferedImage edgeBlurImage) {
		this.edgeBlurImage = edgeBlurImage;
	}

	public BufferedImage getEdgeBlurImage() {
		return edgeBlurImage;
	}

	public ImagePane getTitleContentPane() {
		if (titlePane instanceof OnlyTitlePane) {
			return ((OnlyTitlePane) titlePane).getContentPane();
		}

		return null;
	}

	public OnlyTitlePane getTitlePane() {
		if (titlePane instanceof OnlyTitlePane) {
			return ((OnlyTitlePane) titlePane);
		}
		return null;
	}

	public void changeCloseButtonState() {
		if (titlePane instanceof OnlyTitlePane) {
			((OnlyTitlePane) titlePane).changeCloseButtonState();
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		super.propertyChange(e);
		String propertyName = e.getPropertyName();

		if ("windowDecorationStyle".equals(propertyName)) {
			JRootPane root = (JRootPane) e.getSource();
			uninstallClientDecorations(root);
			installClientDecorations(root);
		} else if ("ancestor".equals(propertyName)) {
			uninstallWindowListeners(root);
			installWindowListeners(root);
		}
	}

	private static class OnlyRootLayout implements LayoutManager2 {

		@Override
		public Dimension preferredLayoutSize(Container parent) {
			Dimension contentPaneSize, menuBarSize, titlePaneSize;
			int contentPaneWidth = 0;
			int contentPaneHeight = 0;
			int menuBarWidth = 0;
			int menuBarHeight = 0;
			int titlePaneWidth = 0;
			Insets insets = parent.getInsets();
			JRootPane root = (JRootPane) parent;

			if (root.getContentPane() != null) {
				contentPaneSize = root.getContentPane().getPreferredSize();
			} else {
				contentPaneSize = root.getSize();
			}

			if (contentPaneSize != null) {
				contentPaneWidth = contentPaneSize.width;
				contentPaneHeight = contentPaneSize.height;
			}

			if (root.getJMenuBar() != null) {
				menuBarSize = root.getJMenuBar().getPreferredSize();

				if (menuBarSize != null) {
					menuBarWidth = menuBarSize.width;
					menuBarHeight = menuBarSize.height;
				}
			}

			if (root.getWindowDecorationStyle() != JRootPane.NONE && root.getUI() instanceof OnlyRootPaneUI) {
				JComponent titlePane = ((OnlyRootPaneUI) root.getUI()).titlePane;

				if (titlePane != null) {
					titlePaneSize = titlePane.getPreferredSize();

					if (titlePaneSize != null) {
						titlePaneWidth = titlePaneSize.width;
					}
				}
			}

			return new Dimension(Math.max(Math.max(contentPaneWidth, menuBarWidth), titlePaneWidth) + insets.left + insets.right, contentPaneHeight + menuBarHeight + titlePaneWidth + insets.top + insets.bottom);
		}

		@Override
		public Dimension minimumLayoutSize(Container parent) {
			Dimension contentPaneSize, menuBarSize, titlePaneSize;
			int contentPaneWidth = 0;
			int contentPaneHeight = 0;
			int menuBarWidth = 0;
			int menuBarHeight = 0;
			int titlePaneWidth = 0;
			Insets insets = parent.getInsets();
			JRootPane root = (JRootPane) parent;

			if (root.getContentPane() != null) {
				contentPaneSize = root.getContentPane().getMinimumSize();
			} else {
				contentPaneSize = root.getSize();
			}

			if (contentPaneSize != null) {
				contentPaneWidth = contentPaneSize.width;
				contentPaneHeight = contentPaneSize.height;
			}

			if (root.getJMenuBar() != null) {
				menuBarSize = root.getJMenuBar().getMinimumSize();

				if (menuBarSize != null) {
					menuBarWidth = menuBarSize.width;
					menuBarHeight = menuBarSize.height;
				}
			}

			if (root.getWindowDecorationStyle() != JRootPane.NONE && root.getUI() instanceof OnlyRootPaneUI) {
				JComponent titlePane = ((OnlyRootPaneUI) root.getUI()).titlePane;

				if (titlePane != null) {
					titlePaneSize = titlePane.getMinimumSize();

					if (titlePaneSize != null) {
						titlePaneWidth = titlePaneSize.width;
					}
				}
			}

			return new Dimension(Math.max(Math.max(contentPaneWidth, menuBarWidth), titlePaneWidth) + insets.left + insets.right, contentPaneHeight + menuBarHeight + titlePaneWidth + insets.top + insets.bottom);
		}

		@Override
		public Dimension maximumLayoutSize(Container target) {
			Dimension contentPaneSize, menuBarSize, titlePaneSize;
			int contentPaneWidth = Integer.MAX_VALUE;
			int contentPaneHeight = Integer.MAX_VALUE;
			int menuBarWidth = Integer.MAX_VALUE;
			int menuBarHeight = Integer.MAX_VALUE;
			int titlePaneWidth = Integer.MAX_VALUE;
			int titlePaneHeight = Integer.MAX_VALUE;
			Insets insets = target.getInsets();
			JRootPane root = (JRootPane) target;

			if (root.getContentPane() != null) {
				contentPaneSize = root.getContentPane().getMaximumSize();

				if (contentPaneSize != null) {
					contentPaneWidth = contentPaneSize.width;
					contentPaneHeight = contentPaneSize.height;
				}
			}

			if (root.getJMenuBar() != null) {
				menuBarSize = root.getJMenuBar().getMaximumSize();

				if (menuBarSize != null) {
					menuBarWidth = menuBarSize.width;
					menuBarHeight = menuBarSize.height;
				}
			}

			if (root.getWindowDecorationStyle() != JRootPane.NONE && root.getUI() instanceof OnlyRootPaneUI) {
				JComponent titlePane = ((OnlyRootPaneUI) root.getUI()).titlePane;

				if (titlePane != null) {
					titlePaneSize = titlePane.getMaximumSize();

					if (titlePaneSize != null) {
						titlePaneWidth = titlePaneSize.width;
						titlePaneHeight = titlePaneSize.height;
					}
				}
			}

			int maxWidth = Math.max(Math.max(contentPaneWidth, menuBarWidth), titlePaneWidth);
			int maxHeight = Math.max(Math.max(contentPaneHeight, menuBarHeight), titlePaneHeight);

			if (maxWidth != Integer.MAX_VALUE) {
				maxWidth += insets.left + insets.right;
			}

			if (maxHeight != Integer.MAX_VALUE) {
				maxHeight = contentPaneHeight + menuBarHeight + titlePaneHeight + insets.top + insets.bottom;
			}

			return new Dimension(maxWidth, maxHeight);
		}

		@Override
		public void layoutContainer(Container parent) {
			boolean isTopRootPane = true;
			if (null != parent) {
				if (parent.getParent() instanceof OnlyFrame) {
					isTopRootPane = ((OnlyFrame) parent.getParent()).isTopRootPane();
				}
				if (parent.getParent() instanceof OnlyDialog) {
					isTopRootPane = ((OnlyDialog) parent.getParent()).isTopRootPane();
				}
			}
			JRootPane root = (JRootPane) parent;
			Rectangle rect = root.getBounds();
			Insets insets = root.getInsets();
			int nextY = 0;
			int width = rect.width - insets.right - insets.left;
			int height = rect.height - insets.top - insets.bottom;

			if (root.getLayeredPane() != null) {
				root.getLayeredPane().setBounds(insets.left, insets.top, width, height);
			}

			if (root.getGlassPane() != null) {
				root.getGlassPane().setBounds(insets.left, insets.top, width, height);
			}

			if (root.getWindowDecorationStyle() != JRootPane.NONE && root.getUI() instanceof OnlyRootPaneUI) {
				JComponent titlePane = ((OnlyRootPaneUI) root.getUI()).titlePane;

				if (titlePane != null) {
					Dimension titlePaneSize = titlePane.getPreferredSize();

					if (titlePaneSize != null) {
						int titlePaneHeight = titlePaneSize.height;
						titlePane.setBounds(0, 0, width, titlePaneHeight);
						nextY += titlePaneHeight;
					}
				}
			}

			if (root.getJMenuBar() != null) {
				Dimension menuBarSize = root.getJMenuBar().getPreferredSize();
				root.getJMenuBar().setBounds(0, nextY, width, menuBarSize.height);
				nextY += menuBarSize.height;
			}

			if (root.getContentPane() != null) {
				if (isTopRootPane) {
					root.getContentPane().setBounds(0, 0, width, height < nextY ? 0 : height);
				} else {
					root.getContentPane().setBounds(0, nextY, width, height < nextY ? 0 : height - nextY);
				}
			}
		}

		@Override
		public float getLayoutAlignmentX(Container target) {
			return 0.0f;
		}

		@Override
		public float getLayoutAlignmentY(Container target) {
			return 0.0f;
		}

		@Override
		public void addLayoutComponent(String name, Component comp) {
		}

		@Override
		public void removeLayoutComponent(Component comp) {
		}

		@Override
		public void addLayoutComponent(Component comp, Object constraints) {
		}

		@Override
		public void invalidateLayout(Container target) {
		}
	}

	private class MouseInputHandler extends MouseInputAdapter {

		private final int[] CURSOR_MAPPING = { Cursor.NW_RESIZE_CURSOR, Cursor.NW_RESIZE_CURSOR, Cursor.N_RESIZE_CURSOR, Cursor.NE_RESIZE_CURSOR, Cursor.NE_RESIZE_CURSOR, Cursor.NW_RESIZE_CURSOR, 0, 0, 0, Cursor.NE_RESIZE_CURSOR, Cursor.W_RESIZE_CURSOR, 0, 0, 0,
				Cursor.E_RESIZE_CURSOR, Cursor.SW_RESIZE_CURSOR, 0, 0, 0, Cursor.SE_RESIZE_CURSOR, Cursor.SW_RESIZE_CURSOR, Cursor.SW_RESIZE_CURSOR, Cursor.S_RESIZE_CURSOR, Cursor.SE_RESIZE_CURSOR, Cursor.SE_RESIZE_CURSOR };
		private boolean isMovingWindow;
		private boolean dragging;
		private int dragCursor;
		private int dragOffsetX;
		private int dragOffsetY;
		private int dragWidth;
		private int dragHeight;

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
				root.repaint();
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
						root.repaint();
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

			if (clickCount % 2 == 0 && point.x <= 22 && point.y <= 22 && root.getWindowDecorationStyle() != JRootPane.NONE) {
				window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
				return;
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
	}

	private class ComponentHandler extends ComponentAdapter {

		@Override
		public void componentResized(ComponentEvent e) {
			if (!OnlyUIUtil.isShapeSupported()) {
				return;
			}

			Window win = (Window) e.getSource();
			// titlePane.setBounds(0, 0, win.getWidth(), 28);
			Frame frame = win instanceof Frame ? (Frame) win : null;
			// TODO
			if (frame != null && (frame.getExtendedState() & Frame.MAXIMIZED_BOTH) != 0) {
				// AWTUtilities.setWindowShape(win, null);
			} else {

				// AWTUtilities.setWindowShape(win, new
				// RoundRectangle2D.Double(0, 0, win.getWidth(),
				// win.getHeight(), 6, 6));
			}
		}
	}
}