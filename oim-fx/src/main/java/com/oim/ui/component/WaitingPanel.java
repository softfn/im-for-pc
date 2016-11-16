package com.oim.ui.component;

import java.awt.CardLayout;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JFrame;

import com.oim.common.component.OurLabel;
import com.oim.common.component.OurPanel;
import com.oim.swing.common.box.ImageBox;

@SuppressWarnings("serial")
public class WaitingPanel extends OurPanel {

	private OurPanel iconPanel = new OurPanel();
	private OurLabel iconLabel = new OurLabel();
	private OurPanel textPanel = new OurPanel();
	private OurLabel textLabel = new OurLabel();
	private String waitingText;
	private String resultText;
	private Icon waitingIcon;
	private Icon resultIcon;
	private WaitingType waitingType;

	public WaitingPanel() {
		initComponents();
		initData();
	}

	private void initComponents() {
		this.setLayout(new java.awt.GridBagLayout());
		this.setOpaque(false);

		iconPanel.setLayout(new java.awt.GridBagLayout());
		textPanel.setLayout(new java.awt.GridBagLayout());

		iconPanel.setOpaque(false);
		textPanel.setOpaque(false);

		iconPanel.add(iconLabel);
		textPanel.add(textLabel);

		OurPanel panel = new OurPanel();
		panel.setOpaque(false);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		panel.add(iconPanel);
		panel.add(textPanel);

		add(panel);
	}

	private void initData() {
		setWaitingIcon(ImageBox.getImageIcon("Resources/Images/Default/Loading/loading_140_9.gif"));
		setWaitingText("正在发送中,请稍等。");
		// 载入
		setResultIocn(ImageBox.getImageIcon("Resources/Images/Default/MessageBox/message_box_warning.png"));
		setResultText("消息发送失败！");
		show(WaitingType.waiting);
	}

	public void setWaitingText(String waitingText) {
		setWaiting(waitingIcon, waitingText);
	}

	public void setWaitingIcon(Icon waitingIcon) {
		setWaiting(waitingIcon, waitingText);
	}

	public void showWaiting(String waitingText) {
		this.waitingType = WaitingType.waiting;
		this.waitingText = waitingText;
		this.iconLabel.setIcon(waitingIcon);
		this.textLabel.setText(waitingText);
	}

	public void showWaiting(Icon waitingIcon, String waitingText) {
		this.waitingType = WaitingType.waiting;
		this.waitingIcon = waitingIcon;
		this.waitingText = waitingText;
		this.iconLabel.setIcon(waitingIcon);
		this.textLabel.setText(waitingText);
	}

	public void setWaiting(Icon waitingIcon, String waitingText) {
		this.waitingIcon = waitingIcon;
		this.waitingText = waitingText;
		if (WaitingType.waiting == waitingType) {
			showWaiting();
		}
	}

	public void setResultText(String resultText) {
		setResult(resultIcon, resultText);
	}

	public void setResultIocn(Icon resultIcon) {
		setResult(resultIcon, resultText);
	}

	public void setResult(Icon resultIcon, String resultText) {
		this.resultIcon = resultIcon;
		this.resultText = resultText;
		if (WaitingType.result == waitingType) {
			showResult();
		}
	}

	public void showResult(String resultText) {
		this.waitingType = WaitingType.result;
		this.resultText = resultText;
		this.iconLabel.setIcon(resultIcon);
		this.textLabel.setText(resultText);
	}

	public void showResult(Icon resultIcon, String resultText) {
		this.waitingType = WaitingType.result;
		this.resultIcon = resultIcon;
		this.resultText = resultText;
		this.iconLabel.setIcon(resultIcon);
		this.textLabel.setText(resultText);
	}

	public void showResult() {
		this.waitingType = WaitingType.result;
		this.iconLabel.setIcon(resultIcon);
		this.textLabel.setText(resultText);
	}

	public void showWaiting() {
		this.waitingType = WaitingType.waiting;
		this.iconLabel.setIcon(waitingIcon);
		this.textLabel.setText(waitingText);
	}

	public void show(WaitingType waitingType) {
		if (this.waitingType != waitingType) {
			if (WaitingType.waiting == waitingType) {
				showWaiting();
			} else {
				showResult();
			}
		}
	}

	public void show(WaitingType waitingType, String message) {
		if (this.waitingType != waitingType) {
			if (WaitingType.waiting == waitingType) {
				if (null == message || "".equals(message)) {
					showWaiting();
				} else {
					showWaiting(message);
				}
			} else {
				if (null != message || "".equals(message)) {
					showResult();
				} else {
					showResult(message);
				}
			}
		}
	}

	public WaitingType getWaitingType() {
		return waitingType;
	}

	public enum WaitingType {

		waiting, result
	}

	public static void main(String[] args) {
		WaitingPanel waitingPanel = new WaitingPanel();
		JFrame frame = new JFrame();
		frame.setLayout(new CardLayout());
		frame.setSize(600, 400);
		frame.add(waitingPanel);
		frame.setVisible(true);
	}
}
