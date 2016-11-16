package com.only;

import com.only.common.ImageDisplayMode;
import com.only.component.ImagePane;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.KeyboardFocusManager;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.FocusManager;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JRootPane;
import javax.swing.LayoutFocusTraversalPolicy;
import javax.swing.Timer;
import javax.swing.plaf.RootPaneUI;

import com.sun.awt.AWTUtilities;
import com.only.laf.OnlyRootPaneUI;
import com.only.util.OnlyUIUtil;

public class OnlyDialog extends JDialog {

	private static final long serialVersionUID = -1642900259343757988L;
	private boolean showIconImage;
	private boolean showTitle;
	private boolean titleOpaque;
	private boolean borderPainted;
	private Image backgroundImage;
	private ImageDisplayMode imageDisplayMode;
	private float imageAlpha;
	private int locationLimit;
	private OnlyRootPaneUI ui;
	private Timer timer;
	private boolean topRootPane = true;

	public OnlyDialog() {
		this((Frame) null, false);
	}

	public OnlyDialog(Frame owner) {
		this(owner, false);
	}

	public OnlyDialog(Frame owner, boolean modal) {
		this(owner, null, modal);
	}

	public OnlyDialog(Frame owner, String title) {
		this(owner, title, false);
	}

	public OnlyDialog(Dialog owner) {
		this(owner, false);
	}

	public OnlyDialog(Dialog owner, boolean modal) {
		this(owner, null, modal);
	}

	public OnlyDialog(Dialog owner, String title) {
		this(owner, title, false);
	}

	public OnlyDialog(Window owner) {
		this(owner, Dialog.ModalityType.MODELESS);
	}

	public OnlyDialog(Window owner, Dialog.ModalityType modalityType) {
		this(owner, null, modalityType);
	}

	public OnlyDialog(Window owner, String title) {
		this(owner, title, Dialog.ModalityType.MODELESS);
	}

	public OnlyDialog(Dialog owner, String title, boolean modal, GraphicsConfiguration gc) {
		super(owner, title, modal, gc);
		init();
	}

	public OnlyDialog(Dialog owner, String title, boolean modal) {
		super(owner, title, modal);
		init();
	}

	public OnlyDialog(Frame owner, String title, boolean modal, GraphicsConfiguration gc) {
		super(owner, title, modal, gc);
		init();
	}

	public OnlyDialog(Frame owner, String title, boolean modal) {
		super(owner, title, modal);
		init();
	}

	public OnlyDialog(Window owner, String title, Dialog.ModalityType modalityType) {
		super(owner, title, modalityType);
		init();
	}

	public OnlyDialog(Window owner, String title, Dialog.ModalityType modalityType, GraphicsConfiguration gc) {
		super(owner, title, modalityType, gc);
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
		Container parent = this.getParent();
		setFont(OnlyUIUtil.getDefaultFont());
		setUndecorated(true);
		root.setUI(ui = new OnlyRootPaneUI());
		root.setForeground(Color.BLACK);
		root.setBackground(new Color(233, 242, 249));
		root.setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
		setBackground(root.getBackground());

		if (parent != null && parent instanceof Window) {
			copyBackgroundImage(parent);
			setIconImages(((Window) parent).getIconImages());
		}

		if (contentPane instanceof JComponent) {
			((JComponent) contentPane).setOpaque(false);
		}
	}

	@Override
	protected JRootPane createRootPane() {
		JRootPane rp = new JRootPane() {
			private static final long serialVersionUID = 6817397706458749155L;

			@Deprecated
			@Override
			public void updateUI() {
				// updateUI空实现在JDK6中正常，但是JDK7中若JDialog无JFrame父窗体，则其中的组件焦点无法用Tab键切换
				// 跟踪到UIManager.getUI(target)时发现在JDK7中多了一行maybeInitializeFocusPolicy(target)，其中对JRootPane做了特殊处理
				// 以下为maybeInitializeFocusPolicy中的有效代码
				if (FocusManager.isFocusManagerEnabled()) {
					KeyboardFocusManager.getCurrentKeyboardFocusManager().setDefaultFocusTraversalPolicy(new LayoutFocusTraversalPolicy());
				}
			}
		};

		rp.setOpaque(true);
		return rp;
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

	@Override
	public void setVisible(boolean visible) {
		/*
		 * JDK的超级大bug Dialog中，
		 * undecorated为true、modal为true时，鼠标移出该Dialog，若鼠标位置仍在其父窗体的覆盖范围内，
		 * 则Dialog不会响应MouseExited事件，此时若鼠标再移入Dialog，则MouseEntered事件也不会响应。
		 * 同样，如果该Dialog中某组件的边缘覆盖了窗体边缘，则鼠标沿覆盖一侧移出该组件时不会响应MouseExited事件，
		 * 再在同侧移入该组件时也不会响应MouseEntered事件，前提是鼠标移出后的位置仍在其父窗体的覆盖范围内。
		 * 故这里启动了一个Timer来监视鼠标位置，并适时指派MouseExited或MouseEntered事件。
		 */
		if (this.isUndecorated() && this.isModal()) {
			if (visible) {
				if (timer == null) {
					timer = new Timer(100, new OnlyDialog.ActionHandler());
				}

				if (!timer.isRunning()) {
					timer.start();
				}
			} else if (timer != null && timer.isRunning()) {
				timer.stop();
			}
		}

		super.setVisible(visible);
	}

	private class ActionHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (!isVisible() && timer != null && timer.isRunning()) {
				timer.stop();
				timer = null;
			} else {
				ui.changeCloseButtonState();
			}
		}
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
}