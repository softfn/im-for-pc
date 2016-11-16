package com.only;

import java.awt.Color;
import java.awt.Container;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.image.BufferedImage;

import javax.swing.FocusManager;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.LayoutFocusTraversalPolicy;
import javax.swing.plaf.RootPaneUI;

import com.only.common.ImageDisplayMode;
import com.only.component.ImagePane;
import com.only.laf.OnlyRootPaneUI;
import com.only.laf.OnlyTitlePane;
import com.only.util.OnlyUIUtil;
import com.only.util.OnlyFeelUtil;
import com.sun.awt.AWTUtilities;

public class OnlyFrame extends JFrame {

	private static final long serialVersionUID = 854652168416021730L;
	private boolean showIconImage;
	private boolean showTitle;
	private boolean titleOpaque;
	private boolean borderPainted;
	private boolean isMaximizedBoundsSet;
	private Image backgroundImage;
	private ImageDisplayMode imageDisplayMode;
	private float imageAlpha;
	private int locationLimit;
	private OnlyRootPaneUI ui;
	private boolean topRootPane=true;

	public OnlyFrame() throws HeadlessException {
		super();
		init();
	}

	public OnlyFrame(GraphicsConfiguration gc) {
		super(gc);
		init();
	}

	public OnlyFrame(String title) throws HeadlessException {
		super(title);
		init();
	}

	public OnlyFrame(String title, GraphicsConfiguration gc) {
		super(title, gc);
		init();
	}

	private void init() {
		borderPainted = true;
		imageDisplayMode = ImageDisplayMode.tiled;
		imageAlpha = 1.0f;
		showIconImage = true;
		showTitle = true;
		JRootPane root = getRootPane();
		Container contentPane = getContentPane();
		setFont(OnlyUIUtil.getDefaultFont());
		setUndecorated(true);
		root.setUI(ui = new OnlyRootPaneUI());
		root.setForeground(Color.BLACK);
		root.setBackground(new Color(233, 242, 249));
		root.setWindowDecorationStyle(JRootPane.FRAME);
		setBackground(root.getBackground());

		if (contentPane instanceof JComponent) {
			((JComponent) contentPane).setOpaque(false);
		}
	}

	@Override
	protected JRootPane createRootPane() {
		JRootPane rp = new JRootPane() {
			private static final long serialVersionUID = 6817397706458749155L;

			@Deprecated
			public void updateUI() {
				// updateUI空实现在JDK6中正常，但是JDK7中若JDialog无JFrame父窗体，则其中的组件焦点无法用Tab键切换
				// 跟踪到UIManager.getUI(target)时发现在JDK7中多了一行maybeInitializeFocusPolicy(target)，其中对JRootPane做了特殊处理
				// 虽然JFrame尚未发现该问题，但是为了避免其他问题产生，也将maybeInitializeFocusPolicy中的有效代码借来一用
				if (FocusManager.isFocusManagerEnabled()) {
					KeyboardFocusManager.getCurrentKeyboardFocusManager().setDefaultFocusTraversalPolicy(new LayoutFocusTraversalPolicy());
				}
			}
		};

		rp.setOpaque(true);
		return rp;
	}

	@Override
	public synchronized void setMaximizedBounds(Rectangle bounds) {
		isMaximizedBoundsSet = bounds != null;
		super.setMaximizedBounds(bounds);
	}

	@Override
	public synchronized void setExtendedState(int state) {
		if (OnlyFeelUtil.isWindows() && (state & Frame.MAXIMIZED_BOTH) != 0 && !isMaximizedBoundsSet) {
			Rectangle bounds = getGraphicsConfiguration().getBounds();
			Rectangle maxBounds = null;
			if (bounds.x == 0 && bounds.y == 0) {
				Insets screenInsets = getToolkit().getScreenInsets(getGraphicsConfiguration());
				maxBounds = new Rectangle(screenInsets.left, screenInsets.top, bounds.width - screenInsets.right - screenInsets.left, bounds.height - screenInsets.bottom - screenInsets.top);
			}
			super.setMaximizedBounds(maxBounds);
		}

		super.setExtendedState(state);
	}

	@Override
	public synchronized int getExtendedState() {
		int state = super.getExtendedState();

		if (OnlyFeelUtil.isWindows()) {
			Window fullWin = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getFullScreenWindow();
			return this == fullWin ? state | Frame.MAXIMIZED_BOTH : state;
		} else {
			return state;
		}
	}

	public boolean isTitleOpaque() {
		return titleOpaque;
	}

	public void setTitleOpaque(boolean titleOpaque) {
		if (this.titleOpaque != titleOpaque) {
			this.titleOpaque = titleOpaque;
			getRootPane().repaint();
		}
	}

	public boolean isShowIconImage() {
		return showIconImage;
	}

	public void setShowIconImage(boolean showIconImage) {
		if (this.showIconImage != showIconImage) {
			this.showIconImage = showIconImage;
			getRootPane().repaint();
		}
	}

	public boolean isShowTitle() {
		return showTitle;
	}

	public void setShowTitle(boolean showTitle) {

		if (this.showTitle != showTitle) {
			this.showTitle = showTitle;
			getRootPane().repaint();
		}
	}

	public BufferedImage getEdgeBlurImage() {
		return ui.getEdgeBlurImage();
	}

	public Image getBackgroundImage() {
		return backgroundImage;
	}

	public void setBackgroundImage(Image backgroundImage) {
		setBackgroundImage(backgroundImage, null);
	}

	public void setBackgroundImage(Image backgroundImage, BufferedImage edgeBlurImage) {
		if (this.backgroundImage != backgroundImage) {
			this.backgroundImage = backgroundImage;
			ui.removeBlurImage();
			ui.setEdgeBlurImage(edgeBlurImage);
			getRootPane().repaint();
		}
	}

	public boolean isBorderPainted() {
		return borderPainted;
	}

	public void setBorderPainted(boolean borderPainted) {
		if (this.borderPainted != borderPainted) {
			this.borderPainted = borderPainted;
			getRootPane().repaint();
		}
	}

	public ImageDisplayMode getImageDisplayMode() {
		return imageDisplayMode;
	}

	public void setImageDisplayMode(ImageDisplayMode imageDisplayMode) {
		if (this.imageDisplayMode != imageDisplayMode) {
			this.imageDisplayMode = imageDisplayMode;
			getRootPane().repaint();
		}
	}

	public float getImageAlpha() {
		return imageAlpha;
	}

	public void setImageAlpha(float imageAlpha) {
		if (imageAlpha >= 0.0f && imageAlpha <= 1.0f) {
			this.imageAlpha = imageAlpha;
			this.repaint();
		} else {
			throw new IllegalArgumentException("Invalid alpha:" + imageAlpha);
		}
	}

	public int getLocationLimit() {
		return this.locationLimit;
	}

	public void setLocationLimit(int locationLimit) {
		this.locationLimit = locationLimit;
	}

	public ImagePane getTitleContentPane() {
		return ui.getTitleContentPane();
	}

	public OnlyTitlePane getTitlePane() {
		return ui.getTitlePane();
	}

	public boolean isTopRootPane() {
		return topRootPane;
	}

	public void setTopRootPane(boolean topRootPane) {
		if (this.topRootPane != topRootPane) {
			this.topRootPane = topRootPane;
			RootPaneUI ui = getRootPane().getUI();
			if (ui instanceof OnlyRootPaneUI) {
				((OnlyRootPaneUI) ui).updateLayout(getRootPane());
			}
		}
	}

	public void copyBackgroundImage(Container c) {
		this.setBackgroundImage(OnlyUIUtil.getBackgroundImageFromContainer(c), OnlyUIUtil.getEdgeBlurImageFromContainer(c));
		this.setImageDisplayMode(OnlyUIUtil.getImageDisplayModeFromContainer(c));
		this.setImageAlpha(OnlyUIUtil.getImageAlphaFromContainer(c));
		this.repaint();

		Window parent = OnlyUIUtil.getWindowFromComponent(c);
		float alpha = 1.0f;

		if (parent != null && OnlyUIUtil.isTranslucencySupported() && (alpha = AWTUtilities.getWindowOpacity(parent)) < 1.0f) {
			AWTUtilities.setWindowOpacity(this, alpha);
		}

	}

	
}