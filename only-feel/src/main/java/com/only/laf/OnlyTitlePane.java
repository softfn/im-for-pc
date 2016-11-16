package com.only.laf;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.Transparency;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.accessibility.AccessibleContext;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;

import sun.awt.SunToolkit;
import sun.swing.SwingUtilities2;

import com.only.OnlyButton;
import com.only.OnlyDialog;
import com.only.OnlyFrame;
import com.only.box.UIBox;
import com.only.component.ImagePane;
import com.only.layout.LineLayout;
import com.only.util.OnlyFeelUtil;
import com.only.util.OnlyUIUtil;

public class OnlyTitlePane extends JComponent {

	private static final long serialVersionUID = -1438138765372695794L;

	private static final Font TITLE_FONT_CH = OnlyUIUtil.getTitleFont(true);
	private static final Font TITLE_FONT_EN = OnlyUIUtil.getTitleFont(false);
	private static final int IMAGE_HEIGHT = 16;
	private static final int IMAGE_WIDTH = 16;
	private static final int HALO_SIZE = 12;
	private static final float SHADOW_ALPHA_STEP = 0.075f;
	private PropertyChangeListener propertyChangeListener;
	private Action closeAction;
	private Action iconifyAction;
	private Action restoreAction;
	private Action maximizeAction;
	private OnlyButton maxOrRestoreButton;
	private OnlyButton iconifyButton;
	private OnlyButton closeButton;
	private ImagePane contentPane;
	private Image systemIcon;
	private Image shadowImage;
	private String title;
	private int textX;
	private int textY;
	private String[] titleChars;
	private boolean[] chFlags;
	private WindowListener windowListener;
	private Window window;
	private JRootPane rootPane;
	private int state;
	private OnlyRootPaneUI rootPaneUI;

	public OnlyTitlePane(JRootPane root, OnlyRootPaneUI ui) {
		this.rootPane = root;
		rootPaneUI = ui;
		state = -1;
		setLayout(new LineLayout(0, LineLayout.LEADING, LineLayout.LEADING, LineLayout.HORIZONTAL));
		installSubcomponents();
		setPreferredSize(new Dimension(-1, 28));
		setOpaque(false);
	}

	private void installListeners() {
		if (window != null) {
			windowListener = createWindowListener();
			window.addWindowListener(windowListener);
			propertyChangeListener = createWindowPropertyChangeListener();
			window.addPropertyChangeListener(propertyChangeListener);
		}
	}

	private void uninstallListeners() {
		if (window != null) {
			window.removeWindowListener(windowListener);
			window.removePropertyChangeListener(propertyChangeListener);
		}
	}

	private WindowListener createWindowListener() {
		return new OnlyTitlePane.WindowHandler();
	}

	private PropertyChangeListener createWindowPropertyChangeListener() {
		return new OnlyTitlePane.PropertyChangeHandler();
	}

	public JRootPane getRootPane() {
		return rootPane;
	}

	private int getWindowDecorationStyle() {
		return rootPane.getWindowDecorationStyle();
	}

	public void addNotify() {
		super.addNotify();
		uninstallListeners();
		window = SwingUtilities.getWindowAncestor(this);

		if (window != null) {
			if (window instanceof Frame) {
				setState(((Frame) window).getExtendedState());
			} else {
				setState(0);
			}

			setActive(window.isActive());
			installListeners();
			updateSystemIcon();
		}
	}

	public void removeNotify() {
		super.removeNotify();
		uninstallListeners();
		window = null;
	}

	private void installSubcomponents() {
		int decorationStyle = getWindowDecorationStyle();

		if (decorationStyle == JRootPane.FRAME) {
			createActions();
			createButtons();
			add(iconifyButton, LineLayout.END);
			add(maxOrRestoreButton, LineLayout.END);
			add(closeButton, LineLayout.END);
		} else if (decorationStyle == JRootPane.INFORMATION_DIALOG) {
			createActions();
			createButtons();
			add(iconifyButton, LineLayout.END);
			add(closeButton, LineLayout.END);
		} else if (decorationStyle != JRootPane.NONE) {
			createActions();
			createButtons();
			add(closeButton, LineLayout.END);
		}

		contentPane = new ImagePane();
		contentPane.setBackground(UIBox.getEmptyColor());
		contentPane.setImageOnly(true);
		add(contentPane, LineLayout.MIDDLE_FILL);
	}

	private void close() {
		if (window != null) {
			window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
		}
	}

	private void iconify() {
		Frame frame = getFrame();
		if (frame != null) {
			frame.setExtendedState(state | Frame.ICONIFIED);
		} else {
			this.window.setVisible(false);
		}
	}

	private void maximize() {
		Frame frame = getFrame();

		if (frame != null) {
			frame.setExtendedState(state | Frame.MAXIMIZED_BOTH);
		}
	}

	private void restore() {
		Frame frame = getFrame();

		if (frame == null) {
			return;
		}

		if ((state & Frame.ICONIFIED) != 0) {
			frame.setExtendedState(state & ~Frame.ICONIFIED);
		} else {
			frame.setExtendedState(state & ~Frame.MAXIMIZED_BOTH);
		}
	}

	private void createActions() {
		closeAction = new OnlyTitlePane.CloseAction();

		if (getWindowDecorationStyle() == JRootPane.FRAME) {
			iconifyAction = new OnlyTitlePane.IconifyAction();
			restoreAction = new OnlyTitlePane.RestoreAction();
			maximizeAction = new OnlyTitlePane.MaximizeAction();
		} else if (getWindowDecorationStyle() == JRootPane.INFORMATION_DIALOG) {
			iconifyAction = new OnlyTitlePane.IconifyAction();
		}
	}

	private void createButtons() {
		closeButton = new OnlyButton(closeAction);
		iconifyButton = new OnlyButton(iconifyAction);
		maxOrRestoreButton = new OnlyButton(restoreAction);
		OnlyButton[] buttons = new OnlyButton[] { closeButton, iconifyButton, maxOrRestoreButton };
		String[] names = { "Close", "Iconify", "Maximize" };
		int index = 0;

		
		closeButton.setNormalImage(UIBox.getImage(UIBox.key_image_window_close_normal));
		closeButton.setRolloverImage(UIBox.getImage(UIBox.key_image_window_close_rollover));
		closeButton.setPressedImage(UIBox.getImage(UIBox.key_image_window_close_pressed));
		closeButton.setPreferredSize(new Dimension(closeButton.getNormalImage().getWidth(null), closeButton.getNormalImage().getHeight(null)));

		iconifyButton.setNormalImage(UIBox.getImage(UIBox.key_image_window_min_normal));
		iconifyButton.setRolloverImage(UIBox.getImage(UIBox.key_image_window_min_rollover));
		iconifyButton.setPressedImage(UIBox.getImage(UIBox.key_image_window_min_pressed));
		iconifyButton.setPreferredSize(new Dimension(iconifyButton.getNormalImage().getWidth(null), iconifyButton.getNormalImage().getHeight(null)));

		for (OnlyButton button : buttons) {
			button.setToolTipText(button.getText());
			button.setText(null);
			button.setFocusable(false);
			button.setFocusPainted(false);
			button.putClientProperty("paintActive", Boolean.TRUE);
			button.putClientProperty(AccessibleContext.ACCESSIBLE_NAME_PROPERTY, names[index++]);
		}
	}

	private void setActive(boolean isActive) {
		closeButton.putClientProperty("paintActive", isActive);

		if (getWindowDecorationStyle() == JRootPane.FRAME) {
			iconifyButton.putClientProperty("paintActive", isActive);
			maxOrRestoreButton.putClientProperty("paintActive", isActive);
		}

		rootPane.repaint();
	}

	private void setState(int state) {
		setState(state, false);
	}

	private void setState(int state, boolean updateRegardless) {
		if (window != null && getWindowDecorationStyle() == JRootPane.FRAME && (this.state != state || updateRegardless)) {
			Frame frame = getFrame();

			if (frame != null) {
				if (((state & Frame.MAXIMIZED_BOTH) != 0) && frame.isShowing()) {
					rootPaneUI.uninstallBorder(rootPane);
				} else if ((state & Frame.MAXIMIZED_BOTH) == 0) {
					rootPaneUI.installBorder(rootPane);
				}

				if (frame.isResizable()) {
					if ((state & Frame.MAXIMIZED_BOTH) != 0) {
						updateRestoreButton(restoreAction);
						maximizeAction.setEnabled(false);
						restoreAction.setEnabled(true);
					} else {
						updateRestoreButton(maximizeAction);
						maximizeAction.setEnabled(true);
						restoreAction.setEnabled(false);
					}

					if (maxOrRestoreButton.getParent() == null || iconifyButton.getParent() == null) {
						maxOrRestoreButton.setVisible(true);
						iconifyButton.setVisible(true);
						revalidate();
						repaint();
					}
				} else {
					maximizeAction.setEnabled(false);
					restoreAction.setEnabled(false);

					if (maxOrRestoreButton.getParent() != null) {
						maxOrRestoreButton.setVisible(false);
						revalidate();
						repaint();
					}
				}
			} else {
				maximizeAction.setEnabled(false);
				restoreAction.setEnabled(false);
				iconifyAction.setEnabled(false);
				maxOrRestoreButton.setVisible(false);
				iconifyButton.setVisible(false);
				revalidate();
				repaint();
			}

			closeAction.setEnabled(true);
			this.state = state;
		}
	}

	private void updateRestoreButton(Action action) {
		
		if (action == restoreAction) {
			maxOrRestoreButton.setNormalImage(UIBox.getImage(UIBox.key_image_window_restore_normal));
			maxOrRestoreButton.setRolloverImage(UIBox.getImage(UIBox.key_image_window_restore_rollover));
			maxOrRestoreButton.setPressedImage(UIBox.getImage(UIBox.key_image_window_restore_pressed));
			maxOrRestoreButton.setPreferredSize(new Dimension(maxOrRestoreButton.getNormalImage().getWidth(null), maxOrRestoreButton.getNormalImage().getHeight(null)));
		} else {
			maxOrRestoreButton.setNormalImage(UIBox.getImage(UIBox.key_image_window_max_normal));
			maxOrRestoreButton.setRolloverImage(UIBox.getImage(UIBox.key_image_window_max_rollover));
			maxOrRestoreButton.setPressedImage(UIBox.getImage(UIBox.key_image_window_max_pressed));
			maxOrRestoreButton.setPreferredSize(new Dimension(maxOrRestoreButton.getNormalImage().getWidth(null), closeButton.getNormalImage().getHeight(null)));
		}

		maxOrRestoreButton.setAction(action);
		maxOrRestoreButton.setToolTipText(maxOrRestoreButton.getText());
		maxOrRestoreButton.setText(null);
	}

	public void paintComponent(Graphics g) {
		Frame frame = getFrame();
		String title = getTitle();
		int x = 5;
		int y = 5;
		boolean showIconImage = true;
		if (window instanceof OnlyFrame) {
			showIconImage = ((OnlyFrame) window).isShowIconImage();
		}
		if (window instanceof OnlyDialog) {
			showIconImage = ((OnlyDialog) window).isShowIconImage();
		}
		if (systemIcon != null && showIconImage) {
			g.drawImage(systemIcon, x, y, IMAGE_WIDTH, IMAGE_HEIGHT, rootPane);
			x += IMAGE_WIDTH + 3;
		}

		boolean showTitle = true;
		if (window instanceof OnlyFrame) {
			showTitle = ((OnlyFrame) window).isShowTitle();
		}
		if (window instanceof OnlyDialog) {
			showTitle = ((OnlyDialog) window).isShowTitle();
		}

		if (title != null && !title.trim().isEmpty() && showTitle) {
			if (!title.equals(this.title)) {
				this.title = title;
				createTitleShadow(g);
			}

			int shadowX = x - 3;
			int shadowY = (this.getHeight() - shadowImage.getHeight(rootPane)) / 2;
			x = textX;

			g.translate(shadowX, shadowY);
			g.setColor(rootPane.getForeground());
			g.drawImage(shadowImage, 0, 0, rootPane);

			for (int i = 0; i < title.length(); i++) {
				g.setFont(chFlags[i] ? TITLE_FONT_CH : TITLE_FONT_EN);
				SwingUtilities2.drawString(rootPane, g, titleChars[i], x, textY);
				x += g.getFontMetrics().stringWidth(titleChars[i]);
			}

			g.translate(-shadowX, -shadowY);
		}

		if (frame != null) {
			setState(frame.getExtendedState());
		}
	}

	private Frame getFrame() {
		if (window instanceof Frame) {
			return (Frame) window;
		}

		return null;
	}

	private String getTitle() {
		if (window instanceof Frame) {
			return ((Frame) window).getTitle();
		} else if (window instanceof Dialog) {
			return ((Dialog) window).getTitle();
		}

		return null;
	}

	public ImagePane getContentPane() {
		return contentPane;
	}

	private void createTitleShadow(Graphics g) {
		int titleLength = title.length();
		Map<Shape, Integer> shapeMap = new LinkedHashMap<Shape, Integer>();
		titleChars = new String[titleLength];
		chFlags = new boolean[titleLength];
		Stroke stroke;
		int shadowX = 0;
		int shadowY = 0;
		int shadowWidth = 0;
		int shadowHeight = 0;
		int textY = 0;
		int textWidth = 0;
		int textHeight = 0;

		for (int i = 0; i < titleLength; i++) {
			titleChars[i] = String.valueOf(title.charAt(i));
			chFlags[i] = OnlyFeelUtil.isChinese(titleChars[i]);
			Font font = chFlags[i] ? TITLE_FONT_CH : TITLE_FONT_EN;
			FontMetrics fm = g.getFontMetrics(font);
			GlyphVector vector = font.createGlyphVector(fm.getFontRenderContext(), titleChars[i]);
			stroke = new BasicStroke(HALO_SIZE, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
			Shape textShape = vector.getOutline();
			Shape shadowShape = stroke.createStrokedShape(textShape);
			Rectangle textRect = textShape.getBounds();
			Rectangle shadowRect = shadowShape.getBounds();
			shadowY = Math.min(shadowRect.y, shadowY);
			textY = Math.min(textRect.y, textY);
			shadowHeight = Math.max(shadowRect.y + shadowRect.height, shadowHeight);
			textHeight = Math.max(textRect.y + textRect.height, textHeight);

			if (i == titleLength - 1) {
				shadowWidth = shadowX + shadowRect.width;
			}

			shapeMap.put(shadowShape, shadowX);
			shadowX += fm.stringWidth(titleChars[i]);
		}

		shadowHeight -= shadowY;
		textWidth = shadowX;
		textHeight -= textY;
		GraphicsConfiguration gc = OnlyUIUtil.getGraphicsConfiguration(this);
		BufferedImage image = gc.createCompatibleImage(shadowWidth, shadowHeight, Transparency.TRANSLUCENT);
		Graphics2D imageG2D = (Graphics2D) image.createGraphics();
		imageG2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		imageG2D.setColor(Color.WHITE);

		for (Shape shape : shapeMap.keySet()) {
			Rectangle rect = shape.getBounds();
			int deltaX = shapeMap.get(shape);
			imageG2D.translate(-rect.x + deltaX, -shadowY);
			imageG2D.fill(shape);
			imageG2D.translate(rect.x - deltaX, shadowY);
		}

		imageG2D.dispose();

		BufferedImage shadowImage = gc.createCompatibleImage(shadowWidth, shadowHeight, Transparency.TRANSLUCENT);
		imageG2D = (Graphics2D) shadowImage.createGraphics();
		int width = shadowWidth;
		int height = shadowHeight;
		int x = 0;
		int y = 0;
		imageG2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		for (int i = 0; i < HALO_SIZE; i++) {
			imageG2D.setComposite(AlphaComposite.SrcOver.derive(SHADOW_ALPHA_STEP));
			imageG2D.drawImage(image, x, y, width, height, null);
			x++;
			y++;
			width -= 2;
			height -= 2;
		}

		imageG2D.dispose();
		this.textX = Math.round((shadowWidth - textWidth) / 2.0f);
		this.textY = -textY + Math.round((shadowHeight - textHeight) / 2.0f) + (1 - (shadowHeight - textHeight) % 2);
		this.shadowImage = shadowImage;
	}

	private void updateSystemIcon() {
		if (window == null) {
			systemIcon = null;
			return;
		}

		List<Image> icons = window.getIconImages();

		if (icons == null || icons.isEmpty()) {
			systemIcon = null;
			return;
		}

		if (icons.size() == 1) {
			systemIcon = icons.get(0);
		} else {
			systemIcon = SunToolkit.getScaledIconImage(icons, IMAGE_WIDTH, IMAGE_HEIGHT);
		}

	}

	void changeCloseButtonState() {
		ButtonModel model = closeButton.getModel();
		Point mouseLocation = OnlyFeelUtil.getMouseLocation();
		Point buttonLocation = closeButton.getLocationOnScreen();
		int x = mouseLocation.x - buttonLocation.x;
		int y = mouseLocation.y - buttonLocation.y;

		if (model.isRollover() && (mouseLocation.x > buttonLocation.x + closeButton.getWidth() || mouseLocation.y < buttonLocation.y)) {
			closeButton.dispatchEvent(new MouseEvent(closeButton, MouseEvent.MOUSE_EXITED, System.currentTimeMillis(), 0, x, y, 0, false));
		} else if (!model.isPressed() && !model.isRollover() && closeButton.getBounds().contains(closeButton.getX() + x, closeButton.getY() + y)) {
			closeButton.dispatchEvent(new MouseEvent(closeButton, MouseEvent.MOUSE_ENTERED, System.currentTimeMillis(), 0, x, y, 0, false));
		}
	}

	private class CloseAction extends AbstractAction {

		private static final long serialVersionUID = -1022852041948761832L;

		public CloseAction() {
			super("关闭");
		}

		public void actionPerformed(ActionEvent e) {
			close();
		}
	}

	private class IconifyAction extends AbstractAction {

		private static final long serialVersionUID = -1509739185778590162L;

		public IconifyAction() {
			super("最小化");
		}

		public void actionPerformed(ActionEvent e) {
			iconify();
		}
	}

	private class RestoreAction extends AbstractAction {

		private static final long serialVersionUID = 7027403948364374755L;

		public RestoreAction() {
			super("还原");
		}

		public void actionPerformed(ActionEvent e) {
			restore();
		}
	}

	private class MaximizeAction extends AbstractAction {

		private static final long serialVersionUID = -7252759733748229590L;

		public MaximizeAction() {
			super("最大化");
		}

		public void actionPerformed(ActionEvent e) {
			maximize();
		}
	}

	private class PropertyChangeHandler implements PropertyChangeListener {

		public void propertyChange(PropertyChangeEvent e) {
			String name = e.getPropertyName();

			if ("resizable".equals(name) || "state".equals(name)) {
				Frame frame = getFrame();

				if (frame != null) {
					setState(frame.getExtendedState(), true);
				}

				if ("resizable".equals(name)) {
					rootPane.repaint();
				}
			} else if ("title".equals(name)) {
				repaint();
			} else if ("iconImage" == name) {
				updateSystemIcon();
				repaint();
			}
		}
	}

	private class WindowHandler extends WindowAdapter {

		public void windowActivated(WindowEvent ev) {
			setActive(true);
		}

		public void windowDeactivated(WindowEvent ev) {
			setActive(false);
		}
	}
}