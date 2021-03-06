package com.oim.ui.video;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.oim.common.event.ExecuteAction;
import com.oim.ui.component.BasePanel;
import com.only.OnlyBorderButton;

/**
 * @Author: XiaHui
 * @Date: 2016年1月19日
 * @ModifyUser: XiaHui
 * @ModifyDate: 2016年1月19日
 */
public class VideoShowPanel extends BasePanel {

	private static final long serialVersionUID = 1L;
	JLabel iconLabel = new JLabel();
	JPanel videoPanel = new JPanel();
	JPanel buttonPanel = new JPanel();
	OnlyBorderButton shutButton = new OnlyBorderButton();
	OnlyBorderButton agreeButton = new OnlyBorderButton();

	private Set<ExecuteAction> shutExecuteActionSet = new HashSet<ExecuteAction>();
	private Set<ExecuteAction> agreeExecuteActionSet = new HashSet<ExecuteAction>();

	public VideoShowPanel() {
		initUI();
		initEvent();
	}

	private void initUI() {
		this.setLayout(new BorderLayout());
		this.add(videoPanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);

		videoPanel.setBackground(Color.red);

		videoPanel.setLayout(new CardLayout());
		videoPanel.add(iconLabel);

		shutButton.setText("挂断");
		agreeButton.setText("接受");

		shutButton.setPreferredSize(new Dimension(60, 25));
		agreeButton.setPreferredSize(new Dimension(60, 25));

		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(shutButton);
		buttonPanel.add(agreeButton);

		agreeButton.setVisible(false);
	}

	private void initEvent() {
		shutButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (ExecuteAction ea : shutExecuteActionSet) {
					ea.execute(VideoShowPanel.this);
				}
			}
		});
		agreeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (ExecuteAction ea : agreeExecuteActionSet) {
					ea.execute(VideoShowPanel.this);
				}
			}
		});
	}

	public void setVideoIcon(Icon icon) {
		iconLabel.setIcon(icon);
	}

	public void showGetRequest(boolean isGetRequest) {
		if (isGetRequest) {
			agreeButton.setVisible(true);
		} else {
			agreeButton.setVisible(false);
		}
	}

	public void addShutExecuteAction(ExecuteAction a) {
		shutExecuteActionSet.add(a);
	}

	public void addAgreeExecuteAction(ExecuteAction a) {
		agreeExecuteActionSet.add(a);
	}
}
