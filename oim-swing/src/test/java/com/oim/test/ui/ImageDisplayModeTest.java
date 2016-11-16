package com.oim.test.ui;

import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.UnsupportedLookAndFeelException;

import com.only.OnlyBorderFrame;
import com.only.common.ImageDisplayMode;
import com.only.util.OnlyImageUtil;

/**
 * @author XiaHui
 * @date 2015年1月5日 上午11:52:00
 */
public class ImageDisplayModeTest extends OnlyBorderFrame {

	private static final long serialVersionUID = 1L;

	public ImageDisplayModeTest() {
		initComponent();
		initEvent();
	}

	private void initComponent() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setMinimumSize(new java.awt.Dimension(520, 500));
		this.setLocationRelativeTo(null);
		this.setLayout(null);
		this.setImageDisplayMode(ImageDisplayMode.fill);

		Image icon = new ImageIcon("Resources/Images/Wallpaper/106.jpg").getImage();

		BufferedImage bi = new BufferedImage(icon.getWidth(null), icon.getHeight(null), BufferedImage.TYPE_INT_RGB);
		Graphics2D biContext = bi.createGraphics();
		biContext.drawImage(icon, 0, 0, null);
		bi = OnlyImageUtil.applyGaussianFilter(bi, null, 50f);
		this.setBackgroundImage(bi);
	}

	private void initEvent() {

	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ImageDisplayModeTest frame = new ImageDisplayModeTest();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
