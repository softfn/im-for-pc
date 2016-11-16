package com.oim.ui;

import java.awt.CardLayout;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;

import com.oim.app.UIBox;
import com.oim.common.config.ConfigManage;
import com.oim.common.config.data.Theme;
import com.oim.ui.component.BaseFrame;
import com.oim.ui.component.WaitingPanel;
import com.oim.ui.theme.ThemeIconLabel;
import com.oim.ui.view.ThemeView;
import com.only.OnlyBorderDialog;
import com.only.OnlyBorderFrame;
import com.only.OnlyButton;
import com.only.OnlyPanel;
import com.only.OnlyScrollPane;
import com.only.OnlySlider;
import com.only.common.ImageDisplayMode;
import com.only.layout.TileLayout;
import com.only.util.OnlyImageUtil;

@SuppressWarnings("serial")
public class ThemeFrame extends BaseFrame {

	private OnlyPanel basePanel = new OnlyPanel();
	private WaitingPanel waitingPanel = new WaitingPanel();
	private OnlyPanel baseListPanel = new OnlyPanel();
	private OnlyPanel skinListPanel = new OnlyPanel();
	private OnlyScrollPane skinScrollPane = new OnlyScrollPane();
	private JLabel skinLabel = new JLabel();
	private JLabel baseLabel = new JLabel();
	private OnlySlider skinSlider = new OnlySlider();
	private OnlySlider baseSlider = new OnlySlider();
	private OnlyButton saveButton = new OnlyButton();
	private String imagePath = "";

	public ThemeFrame() {
		initComponents();
		initData();
		initEvent();
	}

	public ThemeFrame(ThemeView themeView) {
		initComponents();
		initData();
		initEvent();
	}

	private void initComponents() {
		this.setLayout(null);
		this.setSize(680, 480);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setShowTitle(false);
		this.setShowIconImage(false);
		
		baseListPanel.setOpaque(false);
		baseListPanel.setLayout(new CardLayout());
		baseListPanel.add(skinScrollPane);
		
		waitingPanel.setWaitingText("正在设置中...");
		
		basePanel.setOpaque(false);
		basePanel.setLayout(new CardLayout());
		basePanel.setBounds(-1, 50, this.getWidth() + 2, this.getHeight() - 100);
		basePanel.add(baseListPanel);
		basePanel.add(waitingPanel);

		
		skinScrollPane.setViewportView(skinListPanel);

		skinScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		skinScrollPane.setHeaderVisible(false);
		skinScrollPane.getVerticalScrollBar().setUnitIncrement(20);
		skinScrollPane.setAlpha(0.0f);

		skinListPanel.setOpaque(false);
		skinListPanel.setLayout((new TileLayout(30, 30, 1, TileLayout.INDENTIC_SIZE)));
		skinListPanel.setImageDisplayMode(ImageDisplayMode.tiled);
		skinListPanel.setBackgroundImage(new ImageIcon("Resources/Images/Wallpaper/7.jpg").getImage());
		skinListPanel.setImageAlpha(0.1f);

		skinLabel.setIcon(new ImageIcon("Resources/Images/Default/Application/SkinManage/TransSet.png"));
		baseLabel.setIcon(new ImageIcon("Resources/Images/Default/Application/SkinManage/MaterialAlphaSet.png"));
		skinSlider.setMaximum(100);
		skinSlider.setMinimum(1);
		baseSlider.setMaximum(255);
		baseSlider.setMinimum(1);

		add(basePanel);
		add(skinLabel);
		add(baseLabel);
		add(skinSlider);
		add(baseSlider);
		add(saveButton);
		
		showWaiting(false);
	}

	private void initEvent() {
		addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent evt) {
				formWindowClosing(evt);
			}
		});
		skinSlider.addChangeListener(new javax.swing.event.ChangeListener() {
			@Override
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
				skinSliderStateChanged(evt);
			}
		});
		baseSlider.addChangeListener(new javax.swing.event.ChangeListener() {
			@Override
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
				baseSliderStateChanged(evt);
			}
		});
	}

	@Override
	public void formComponentResized() {
		basePanel.setBounds(-1, 50, this.getWidth() + 2, this.getHeight() - 100);
		skinLabel.setBounds(128, basePanel.getHeight() + basePanel.getY() + 15, 22, 20);
		skinSlider.setBounds(150, basePanel.getHeight() + basePanel.getY() + 15, 180, 25);
		baseLabel.setBounds(358, basePanel.getHeight() + basePanel.getY() + 15, 22, 20);
		baseSlider.setBounds(380, basePanel.getHeight() + basePanel.getY() + 15, 180, 25);

	}

	private void skinSliderStateChanged(ChangeEvent evt) {
		float a = 0.1f;
		a = skinSlider.getValue();
		float v = a / 100.0f;
		for (OnlyBorderFrame ourFrame : UIBox.frameSet) {
			ourFrame.setImageAlpha(v);
		}
	}

	private void baseSliderStateChanged(ChangeEvent evt) {

		// int ab = baseSlider.getValue();
		// BasePanel.setBackgroundColorAlpha(ab);
		for (OnlyBorderFrame ourFrame : UIBox.frameSet) {
			ourFrame.repaint();
		}

	}

	private void skinMouseClicked(ThemeIconLabel skinIconLable) {
		imagePath = skinIconLable.getIconPath();
		setTheme();
	}

	private void showWaiting(boolean waiting) {
		waitingPanel.setVisible(waiting);
		baseListPanel.setVisible(!waiting);
	}

	private void setTheme() {
		Runnable run = new Runnable() {
			@Override
			public void run() {
				showWaiting(true);
				Image imaeg = new ImageIcon(imagePath).getImage();

				for (OnlyBorderFrame ourFrame : UIBox.frameSet) {
					ourFrame.setBackgroundImage(imaeg);
				}
				for (OnlyBorderDialog ourFrame : UIBox.dialogSet) {
					ourFrame.setBackgroundImage(imaeg);
				}
				BufferedImage bi = new BufferedImage(imaeg.getWidth(null), imaeg.getHeight(null), BufferedImage.TYPE_INT_RGB);

				Graphics2D biContext = bi.createGraphics();
				biContext.drawImage(imaeg, 0, 0, null);
				bi = OnlyImageUtil.applyGaussianFilter(bi, null, 50f);

				for (OnlyBorderFrame ourFrame : UIBox.frameSet) {
					ourFrame.setBackgroundImage(bi);
				}
				for (OnlyBorderDialog ourFrame : UIBox.dialogSet) {
					ourFrame.setBackgroundImage(bi);
				}
				showWaiting(false);
			}
		};
		new Thread(run).start();
	}

	private void formWindowClosing(java.awt.event.WindowEvent evt) {
		if (null != imagePath && !"".equals(imagePath)) {
			Theme theme = new Theme();
			theme.setWindowBackgroundImage(imagePath);
			ConfigManage.addOrUpdate(Theme.config_file_path, theme);
		}
		this.setVisible(false);
	}

	/**
	 * 中心显示
	 */

	private void initData() {
		for (int i = 0; i < 25; i++) {
			final ThemeIconLabel iconButton = new ThemeIconLabel();
			iconButton.setIconPath("Resources/Images/Wallpaper/" + i + ".jpg");

			// iconButton.setIconPath("Resources/Images/Skins/1.45_" + i +
			// "/main.jpg");
			iconButton.setFocusable(false);
			iconButton.setVerticalTextPosition(SwingConstants.BOTTOM);
			iconButton.setHorizontalTextPosition(SwingConstants.CENTER);
			iconButton.setIconTextGap(0);
			iconButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
			skinListPanel.add(iconButton);
			iconButton.addMouseListener(new java.awt.event.MouseAdapter() {
				@Override
				public void mouseClicked(java.awt.event.MouseEvent evt) {
					skinMouseClicked(iconButton);
				}
			});
		}
	}

	public static void main(String args[]) {

		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new ThemeFrame().setVisible(true);
			}
		});
	}
}
