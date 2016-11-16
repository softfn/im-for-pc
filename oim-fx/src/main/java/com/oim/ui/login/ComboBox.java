package com.oim.ui.login;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.event.DocumentEvent;

import com.oim.common.component.event.DocumentListenerAdapter;
import com.oim.swing.common.util.ImageUtil;
import com.only.OnlyButton;
import com.only.OnlyLabel;
import com.only.OnlyPanel;
import com.only.OnlyScrollPane;
import com.only.OnlyTextField;


public class ComboBox extends OnlyPanel {

    private static final long serialVersionUID = 1L;
    private JPanel basePanel = new JPanel();
    private OnlyTextField textField = new OnlyTextField();
    private OnlyLabel iconLabel = new OnlyLabel();
    private OnlyButton actionButton = new OnlyButton();
    private ComboBoxMenu comboBoxMenu = new ComboBoxMenu();
    private ImageIcon icon = ImageUtil.getEmptyImageIcon(20, 100);
    private Set<ComboBoxAction> comboBoxActionSet = new HashSet<ComboBoxAction>();
    private ComboBoxRenderer comboBoxRenderer = new DefaultComboBoxRenderer();
    private ComboBoxActionAdapter comboBoxActionAdapter;

    public ComboBox() {
        initComponents();
        initEvent();
    }

    private void initComponents() {
        this.setBackground(Color.white);
        this.setLayout(null);
        this.setBorderPainted(false);
        this.setBorderColor(new java.awt.Color(180, 180, 180));

        basePanel.setLayout(new BoxLayout(basePanel, BoxLayout.X_AXIS));
        basePanel.setOpaque(false);

        textField.setBorder(null);
        textField.setAlpha(0.0f);
        textField.setFont(new Font("微软雅黑", 0, 14));
        Image buttonNormalImage = new ImageIcon("Resources/Images/Default/Login/combo_box_button_normal.png").getImage();
		Image buttonRolloverImage = new ImageIcon("Resources/Images/Default/Login/combo_box_button_rollover.png").getImage();
		Image buttonPressedImage = new ImageIcon("Resources/Images/Default/Login/combo_box_button_pressed.png").getImage();

		ImageIcon normalIcon = new ImageIcon("Resources/Images/Default/Login/user_name_normal.png");
		ImageIcon disabledIcon = new ImageIcon("Resources/Images/Default/Login/user_name_disabled.png");

		actionButton.setNormalImage(buttonNormalImage);
		actionButton.setDisabledImage(buttonNormalImage);
		actionButton.setRolloverImage(buttonRolloverImage);
		actionButton.setPressedImage(buttonPressedImage);

		actionButton.setIcon(normalIcon);
		actionButton.setDisabledIcon(disabledIcon);
		actionButton.setNormalImageInsets(1, 2, 1, 1);

		actionButton.setText(null);
		actionButton.setFocusable(false);
		actionButton.setFocusPainted(false);
        
        
       
        basePanel.add(iconLabel);
        basePanel.add(textField);

        add(basePanel);
        add(actionButton);
    }

    private void initEvent() {
        actionButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actionButtonActionPerformed(evt);
            }
        });

        textField.getDocument().addDocumentListener(new DocumentListenerAdapter() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changedText(textField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changedText(textField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                changedText(textField.getText());
            }
        });
        comboBoxActionAdapter = new ComboBoxActionAdapter() {
            @Override
            public void select(Object o) {
                selectItem(o);
                comboBoxMenu.setVisible(false);
            }

            @Override
            public void delete(Object o) {
                if (o instanceof ComboBoxItem) {
                    removeItem((ComboBoxItem) o);
                }
            }
        };
    }

    private void actionButtonActionPerformed(ActionEvent evt) {
        comboBoxMenu.show(this, 0, this.getHeight());
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        basePanel.setBounds(1, 1, width - 30, height - 2);
        actionButton.setBounds(width - 29, 1, 28, height - 2);
        icon.setImage(icon.getImage().getScaledInstance((width), 120, Image.SCALE_DEFAULT));
        comboBoxMenu.setIcon(icon);
    }

    public void setCaretColor(Color color) {
        textField.setCaretColor(color);
    }

    @Override
    public void setForeground(Color color) {
        super.setForeground(color);
        if (null != textField) {
            textField.setForeground(color);
        }
    }

    private void removeItem(ComboBoxItem comboBoxItem) {
        comboBoxMenu.removeComboBoxItem(comboBoxItem);
        for (ComboBoxAction comboBoxAction : comboBoxActionSet) {
            comboBoxAction.delete(comboBoxItem.getUserObject());
        }
    }

    private void selectItem(Object o) {
        String text = comboBoxRenderer.getText(o);
        this.setText(text);
        for (ComboBoxAction comboBoxAction : comboBoxActionSet) {
            comboBoxAction.select(o);
        }
    }

    private void changedText(String text) {
        for (ComboBoxAction comboBoxAction : comboBoxActionSet) {
            comboBoxAction.itemChange(text);
        }
    }

    public void setIcon(Icon icon) {
        iconLabel.setIcon(icon);
    }

    public String getText() {
        return textField.getText();
    }

    public void setText(String text) {
        textField.setText(text);
        textField.setCaretPosition(null != text ? text.length() : 0);
    }

    public void addItem(Object o) {
        String text = comboBoxRenderer.getText(o);
        ComboBoxItem comboBoxItem = new ComboBoxItem();
        comboBoxItem.setText(text);
        comboBoxItem.setComboBoxAction(comboBoxActionAdapter);
        comboBoxItem.setUserObject(o);
        comboBoxMenu.addComboBoxItem(comboBoxItem);
        if (null == this.getText() || "".equals(this.getText())) {
            this.setText(text);
        }
    }

    public void addComboBoxAction(ComboBoxAction comboBoxAction) {
        comboBoxActionSet.add(comboBoxAction);
    }

    public ComboBoxRenderer getComboBoxRenderer() {
        return comboBoxRenderer;
    }

    public void setComboBoxRenderer(ComboBoxRenderer comboBoxRenderer) {
        this.comboBoxRenderer = comboBoxRenderer;
    }

    class DefaultComboBoxRenderer implements ComboBoxRenderer {

        @Override
        public String getText(Object value) {
            return (null != value) ? value.toString() : "";
        }
    }

    /**
     * 下拉组件
     *
     * @author XiaHui 2013-10-25
     *
     */
    class ComboBoxMenu extends JPopupMenu {

        private static final long serialVersionUID = 1L;
        private Color color = new Color(255, 255, 255);
        private OnlyScrollPane scrollPane = new OnlyScrollPane();
        private JLabel label = new JLabel();
        private JPanel panel = new JPanel();

        public ComboBoxMenu() {
            initComponents();
        }

        private void initComponents() {
            this.setOpaque(false);
            this.setLayout(new java.awt.CardLayout());
            label.setLayout(new java.awt.CardLayout());
            panel.setLayout(new OneBoxLayout(panel, OneBoxLayout.PAGE_AXIS));
            panel.setBackground(color);

            scrollPane.setViewportView(panel);
            // scrollPane.setBorder(null);
            scrollPane.setHeaderVisible(false);
            scrollPane.getVerticalScrollBar().setUnitIncrement(20);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            label.add(scrollPane);
            label.setIcon(icon);
            add(label);

        }

        public void setIcon(Icon icon) {
            label.setIcon(icon);
        }

        public void addComboBoxItem(ComboBoxItem comboBoxItem) {
            panel.add(comboBoxItem);
            scrollPane.setViewportView(panel);
        }

        public void removeComboBoxItem(ComboBoxItem comboBoxItem) {
            panel.remove(comboBoxItem);
            scrollPane.setViewportView(panel);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setPaint(color);
            g2.fillRect(0, this.getHeight(), this.getWidth(), this.getHeight() + 50);
        }
    }

	public void setLabelText(String labelText) {
		textField.setLabelText(labelText);
	}
	
	   
}
