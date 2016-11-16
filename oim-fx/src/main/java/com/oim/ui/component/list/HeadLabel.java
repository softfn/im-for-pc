package com.oim.ui.component.list;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import com.oim.ui.component.BaseLabel;

/**
 * 描述：
 * 
 * @author 夏辉
 * @date 2015年2月28日 下午7:43:44
 * @version 0.0.1
 */
public class HeadLabel extends BaseLabel {

	private static final long serialVersionUID = 1L;

	private JPanel linePanel = new JPanel();
	private JSeparator separator = new JSeparator();
	private JPanel mainPanel = new JPanel();
	private JPanel baseHeadPanel = new JPanel();
	private JPanel baseTextPanel = new JPanel();

	private JPanel headPanel = new JPanel();
	private JPanel dataPanel = new JPanel();
	private JPanel textPanel = new JPanel();

	private IconPanel iocnPanel = new IconPanel();

	private JLabel remarkLabel = new JLabel();
	private JLabel nicknameLabel = new JLabel();
	private JLabel statusLabel = new JLabel();

	private JLabel textLabel = new JLabel();

	private JPanel businessPanel = new JPanel();

	private Color mouseEnteredColor = new Color(70, 190, 200);
	private Color focusGainedColor = new Color(252, 238, 141);

	// private Color mouseEnteredColor = new Color(192, 224, 248);
	// private Color foregroundColor = new Color(130, 130, 130);
	// private Color focusGainedColor = new Color(252, 238, 141);
	private	Map<String,Component> map=new HashMap<String,Component>();
	private Color textNormalForegroundColor = new Color(130, 130, 130);
	private Color textRolloverForegroundColor = new Color(240, 240, 240);

	private Color remarNormalforegroundColor = new Color(0, 0, 0);
	private Color remarRolloverForegroundColor = new Color(255, 255, 255);

	private boolean mouseEntered = false;
	private boolean selected = false;

	private Font Font = new Font("微软雅黑", 0, 12);

	private Set<HeadLabelAction> clickedActionSet = new HashSet<HeadLabelAction>();

	private String remark;
	private String nickname;
	private String status;
	private String showText;

	public HeadLabel() {
		initComponents();
		initEvent();
	}

	private void initComponents() {
		this.setLayout(new CardLayout());

		mainPanel.setLayout(new java.awt.BorderLayout());

		baseHeadPanel.setLayout(new java.awt.GridBagLayout());

		baseTextPanel.setLayout(new BoxLayout(baseTextPanel, BoxLayout.Y_AXIS));

		headPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 1));

		dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.X_AXIS));

		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.X_AXIS));

		linePanel.setOpaque(false);
		mainPanel.setOpaque(false);
		baseHeadPanel.setOpaque(false);
		baseTextPanel.setOpaque(false);
		headPanel.setOpaque(false);
		dataPanel.setOpaque(false);
		textPanel.setOpaque(false);

		dataPanel.setBackground(new Color(125, 54, 45));
		textPanel.setBackground(new Color(25, 154, 145));
		mainPanel.setBackground(Color.blue);
		baseHeadPanel.setBackground(Color.red);
		baseTextPanel.setBackground(Color.green);

		JPanel dataRootPanel = new JPanel();
		dataRootPanel.setLayout(new java.awt.BorderLayout());
		dataRootPanel.setOpaque(false);

		JPanel dataNodePanel = new JPanel();
		dataNodePanel.setLayout(new java.awt.GridBagLayout());
		dataNodePanel.setOpaque(false);

		dataNodePanel.add(remarkLabel);
		dataNodePanel.add(nicknameLabel);
		dataNodePanel.add(statusLabel);

		dataRootPanel.add(dataNodePanel, java.awt.BorderLayout.WEST);

		dataPanel.add(dataRootPanel);

		JPanel textRootPanel = new JPanel();
		textRootPanel.setLayout(new java.awt.BorderLayout());
		textRootPanel.setOpaque(false);

		JPanel textNodePanel = new JPanel();
		textNodePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));
		textNodePanel.setOpaque(false);

		textRootPanel.add(textNodePanel, java.awt.BorderLayout.WEST);

		textNodePanel.add(textLabel);

		JPanel businessRootPanel = new JPanel();
		businessRootPanel.setLayout(new java.awt.BorderLayout());
		businessRootPanel.setOpaque(false);

		JPanel businessNodePanel = new JPanel();
		businessNodePanel.setLayout(new java.awt.GridBagLayout());
		businessNodePanel.setOpaque(false);

		businessNodePanel.add(businessPanel);
		businessNodePanel.add(textRootPanel);

		businessRootPanel.add(businessNodePanel, java.awt.BorderLayout.WEST);
		textPanel.add(businessRootPanel);

		remarkLabel.setForeground(remarNormalforegroundColor);

		nicknameLabel.setForeground(textNormalForegroundColor);
		statusLabel.setForeground(textNormalForegroundColor);

		textLabel.setForeground(textNormalForegroundColor);

		remarkLabel.setFont(Font);
		statusLabel.setFont(Font);
		nicknameLabel.setFont(Font);
		textLabel.setFont(Font);

		businessPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 1));
		businessPanel.setOpaque(false);

		headPanel.add(iocnPanel);

		baseHeadPanel.add(headPanel);

		JPanel headTempPanel = new JPanel();
		headTempPanel.setOpaque(false);

		baseTextPanel.add(headTempPanel);

		baseTextPanel.add(dataPanel);
		baseTextPanel.add(textPanel);

		// JPanel textTempPanel = new JPanel();
		// baseTextPanel.add(textTempPanel);

		mainPanel.add(baseHeadPanel, java.awt.BorderLayout.WEST);
		mainPanel.add(baseTextPanel, java.awt.BorderLayout.CENTER);

		businessPanel.setVisible(false);

		linePanel.setLayout(new BoxLayout(linePanel, BoxLayout.Y_AXIS));
		// linePanel.setLayout(new CardLayout());

		separator.setVisible(false);

		linePanel.add(mainPanel);
		linePanel.add(separator);

		add(linePanel);

		//
		// businessPanel.add(new JLabel(new
		// ImageIcon("Resources/Images/Default/Status/FLAG/Big/imonline.png")));
		//
		// headLabel.setIcon(new
		// ImageIcon("Resources/Images/Default/UserHead/1.png"));
	}

	private void initEvent() {
		addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				headModelMouseClicked(e);
			}
		});
		mainPanel.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				headModelMouseClicked(evt);
			}

			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				headModelMouseEntered(evt);
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				headModelMouseExited(evt);
			}

			@Override
			public void mousePressed(java.awt.event.MouseEvent evt) {
				headModelMousePressed(evt);
			}

			@Override
			public void mouseReleased(java.awt.event.MouseEvent evt) {
				headModelMouseReleased(evt);
			}
		});
		iocnPanel.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				headModelMouseClicked(evt);
			}

			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				headModelMouseEntered(evt);
				//System.out.println(HeadLabel.this.getLocationOnScreen());
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				headModelMouseExited(evt);
			}

			@Override
			public void mousePressed(java.awt.event.MouseEvent evt) {
				headModelMousePressed(evt);
			}

			@Override
			public void mouseReleased(java.awt.event.MouseEvent evt) {
				headModelMouseReleased(evt);
			}
		});
		//		mainPanel.addFocusListener(new java.awt.event.FocusAdapter() {
		//			@Override
		//			public void focusGained(java.awt.event.FocusEvent evt) {
		//	
		//				update();
		//			}
		//
		//			@Override
		//			public void focusLost(java.awt.event.FocusEvent evt) {
		//		
		//				update();
		//			}
		//		});
	}

	private void headModelMouseClicked(java.awt.event.MouseEvent evt) {
		mainPanel.requestFocus();
		for (HeadLabelAction headLabelAction : clickedActionSet) {
			headLabelAction.action(evt, this);
		}
		if (evt.getButton() == MouseEvent.BUTTON3) {

		}
		if (evt.getButton() == MouseEvent.BUTTON1) {
			if (evt.getClickCount() == 2) {

			}
		}
	}

	private void headModelMouseEntered(java.awt.event.MouseEvent evt) {
		mouseEntered = true;
		update();

	}

	private void headModelMouseExited(java.awt.event.MouseEvent evt) {
		mouseEntered = false;
		update();
	}

	private void headModelMousePressed(java.awt.event.MouseEvent evt) {
		// TODO add your handling code here:
	}

	private void headModelMouseReleased(java.awt.event.MouseEvent evt) {
		// TODO add your handling code here:
	}

	public void update() {
		if (isSelected()) {
			mainPanel.setOpaque(true);
			mainPanel.setBackground(focusGainedColor);

			remarkLabel.setForeground(remarNormalforegroundColor);

			nicknameLabel.setForeground(textNormalForegroundColor);
			statusLabel.setForeground(textNormalForegroundColor);

			textLabel.setForeground(textNormalForegroundColor);

			iocnPanel.setDrawBorder(true);

		} else {
			if (mouseEntered) {
				mainPanel.setOpaque(true);
				mainPanel.setBackground(mouseEnteredColor);
				remarkLabel.setForeground(remarRolloverForegroundColor);

				nicknameLabel.setForeground(textRolloverForegroundColor);
				statusLabel.setForeground(textRolloverForegroundColor);

				textLabel.setForeground(textRolloverForegroundColor);
			} else {
				mainPanel.setOpaque(false);
				remarkLabel.setForeground(remarNormalforegroundColor);

				nicknameLabel.setForeground(textNormalForegroundColor);
				statusLabel.setForeground(textNormalForegroundColor);

				textLabel.setForeground(textNormalForegroundColor);
			}
			iocnPanel.setDrawBorder(false);

		}
		this.repaint();
	}

	public synchronized void addMouseListener(MouseListener l) {
		mainPanel.addMouseListener(l);
	}

	public void setGray(boolean gray) {
		iocnPanel.setGray(gray);
	}

	public boolean isGray() {
		return iocnPanel.isGray();
	}

	public void setHeadIcon(ImageIcon icon) {
		iocnPanel.setIcon(icon);
	}

	public void setStatusIcon(Icon icon) {
		iocnPanel.setStatusIcon(icon);
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
		remarkLabel.setText(remark);
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
		nicknameLabel.setText(nickname);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
		statusLabel.setText(status);
	}

	public String getShowText() {
		return showText;
	}

	public void setShowText(String showText) {
		this.showText = showText;
		textLabel.setText(showText);
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		if (this.selected != selected) {
			this.selected = selected;
			update();
		}
	}

	public void addBusinessAttribute(String key,Component component) {
		Component c=map.get(key);
		if(null!=c){
			businessPanel.remove(c);
		}
		businessPanel.add(component);
		map.put(key, component);
		if (!businessPanel.isVisible()) {
			businessPanel.setVisible(true);
		}
	}

	public void setShowSeparator(boolean showSeparator) {
		separator.setVisible(showSeparator);
	}

	public IconPanel getIocnPanel() {
		return iocnPanel;
	}

	public void setRoundedCorner(int rx, int ry) {
		iocnPanel.setRoundedCorner(rx, ry);
	}

	public void addAction(HeadLabelAction headLabelAction) {
		clickedActionSet.add(headLabelAction);
	}
}
