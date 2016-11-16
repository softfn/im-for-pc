package com.oim.test.ui.component.list;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UnsupportedLookAndFeelException;

import com.oim.ui.component.WaitingPanel;
import com.only.OnlyBorderFrame;

/**
 * @author XiaHui
 * @date 2015年1月5日 上午11:52:00
 */
public class WaitingPanelTest extends OnlyBorderFrame {

	private static final long serialVersionUID = 1L;

	private JPanel chatPanel = new JPanel();
	private WaitingPanel waitingPanel = new WaitingPanel();
	private JButton wButton = new JButton();
	private JButton rButton = new JButton();

	/**
	 * Create the frame.
	 */
	public WaitingPanelTest() {
		initComponent();
		initEvent();
	}

	private void initComponent() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setMinimumSize(new java.awt.Dimension(520, 500));
		this.setLocationRelativeTo(null);
		this.setLayout(null);

		chatPanel.setLayout(new java.awt.GridBagLayout());
		wButton.setText("等待");
		rButton.setText("结果");
		
		chatPanel.add(wButton);
		chatPanel.add(rButton);
		chatPanel.add(waitingPanel);

		add(chatPanel);

	}

	private void initEvent() {
		wButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				w();
			}
		});

		rButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				r();
			}
		});
	}

	private void w() {
		waitingPanel.showWaiting();
	}

	private void r() {
		waitingPanel.showResult();
	}

	public void formComponentResized() {
		chatPanel.setBounds(3, 50, this.getWidth() - 6, this.getHeight() - 100);
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
					WaitingPanelTest frame = new WaitingPanelTest();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
