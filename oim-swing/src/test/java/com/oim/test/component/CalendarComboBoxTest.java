package com.oim.test.component;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

import javax.swing.JFrame;

import com.oim.common.component.calendar.CalendarComboBox;
import com.only.OnlyCalendarComboBox;
import com.only.OnlyFrame;
import com.only.OnlyPanel;

/**
 * @author XiaHui
 * @date 2015年1月5日 上午11:52:00
 */
public class CalendarComboBoxTest extends OnlyFrame {

	private static final long serialVersionUID = 1L;

	private OnlyPanel panel = new OnlyPanel();
	private OnlyCalendarComboBox progressBarH = new OnlyCalendarComboBox();
	private CalendarComboBox calendarComboBox = new CalendarComboBox();

	/**
	 * Create the frame.
	 */
	public CalendarComboBoxTest() {
		initComponent();
		initEvent();
	}

	private void initComponent() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 450, 300);
		this.setLocationRelativeTo(null);
		this.getContentPane().setLayout(new CardLayout());
		this.getRootPane().setBackground(new Color(0, 128, 157));

		panel.setLayout(null);

		progressBarH.setBounds(10, 10, 160, 25);
		calendarComboBox.setBounds(10, 45, 160, 25);
		
		panel.add(progressBarH);
		panel.add(calendarComboBox);
		
		getContentPane().add(panel);

	}

	private void initEvent() {

	}

	// 模糊算法（已优化）
	public static ConvolveOp getGaussianBlurFilter(int radius, boolean horizontal) {
		if (radius < 1) {
			throw new IllegalArgumentException("Radius must be >= 1");
		}

		int size = radius * 2 + 1;
		float[] data = new float[size];

		float sigma = radius / 3.0f;
		float twoSigmaSquare = 2.0f * sigma * sigma;
		float sigmaRoot = (float) Math.sqrt(twoSigmaSquare * Math.PI);
		float total = 0.0f;

		for (int i = -radius; i <= radius; i++) {
			float distance = i * i;
			int index = i + radius;
			data[index] = (float) Math.exp(-distance / twoSigmaSquare) / sigmaRoot;
			total += data[index];
		}

		for (int i = 0; i < data.length; i++) {
			data[i] /= total;
		}

		Kernel kernel = null;
		if (horizontal) {
			kernel = new Kernel(size, 1, data);
		} else {
			kernel = new Kernel(1, size, data);
		}
		return new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
//		try {
//			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//				if ("Nimbus".equals(info.getName())) {
//					javax.swing.UIManager.setLookAndFeel(info.getClassName());
//					break;
//				}
//			}
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		} catch (InstantiationException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		} catch (UnsupportedLookAndFeelException e) {
//			e.printStackTrace();
//		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CalendarComboBoxTest frame = new CalendarComboBoxTest();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
