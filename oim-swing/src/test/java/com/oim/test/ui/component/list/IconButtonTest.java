package com.oim.test.ui.component.list;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UnsupportedLookAndFeelException;

import com.oim.ui.component.list.IconButton;
import com.only.OnlyBorderFrame;

/**
 * @author XiaHui
 * @date 2015年1月5日 上午11:52:00
 */
public class IconButtonTest extends OnlyBorderFrame {

	private static final long serialVersionUID = 1L;

	private JPanel chatPanel = new JPanel();
	private JButton grayButton = new JButton();
	private JButton drawBorderButton = new JButton();
//	private OnlyLabel head=new OnlyLabel();
	private IconButton head=new IconButton();
	/**
	 * Create the frame.
	 */
	public IconButtonTest() {
		initComponent();
		initEvent();
	}

	private void initComponent() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setMinimumSize(new java.awt.Dimension(520, 500));
		this.setLocationRelativeTo(null);
		this.setLayout(null);

		chatPanel.setLayout(null);
		
		grayButton.setText("变灰");
		drawBorderButton.setText("边框");
		
		grayButton.setBounds(20, 50, 120, 25);
		drawBorderButton.setBounds(20, 85, 120, 25);
		
		head.setBounds(20, 120, 60, 60);
		head.setIcon(new ImageIcon("Resources/Images/Head/User/1.png"));
		
		grayButton.setSelectedIcon(new ImageIcon("Resources/Images/Head/User/1.png"));
		
		chatPanel.add(grayButton);
		chatPanel.add(drawBorderButton);
		chatPanel.add(head);
		
		add(chatPanel);

	}

	private void initEvent() {
		grayButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				gray();
			}
		});
		drawBorderButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				drawBorder();
			}
		});
	}
	
	private void gray(){
		head.setEnabled(!head.isEnabled());
		//head.setGray(!head.isGray());
	}
	private void drawBorder(){
		//head.setb
		head.setDrawBorder(!head.isDrawBorder());
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
					IconButtonTest frame = new IconButtonTest();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
