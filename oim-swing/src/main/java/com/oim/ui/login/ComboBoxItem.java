/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oim.ui.login;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicButtonUI;

import com.oim.common.box.ImageBox;



/**
 * date 2012-9-15 10:41:52
 *
 * @author XiaHui
 */
public class ComboBoxItem extends JLabel {

    private static final long serialVersionUID = 1L;
    private JPanel basePanel = new JPanel();
    private JLabel textLabel = new JLabel();
    private TabButton closeButton = new TabButton();
    private Color mouseEnteredColor = new Color(49, 157, 228);
    private Color backgroundColor = new Color(255, 255, 255);
    private ImageIcon icon = ImageBox.getEmptyImageIcon(20, 25);
    private ComboBoxAction comboBoxAction;
    private Object userObject;

    public ComboBoxItem() {
        initComponents();
        initEvent();
    }

    public ComboBoxItem(String text) {
        initComponents();
        initEvent();
        textLabel.setText(text);
    }

    public ComboBoxItem(Object userObject, String text) {
        this.userObject = userObject;
        initComponents();
        initEvent();
        textLabel.setText(text);
    }

    private void initComponents() {
        this.setLayout(new java.awt.CardLayout());
        this.setBackground(backgroundColor);
        this.setIcon(icon);
        basePanel.setBackground(backgroundColor);
        basePanel.setLayout(null);

        basePanel.add(textLabel);
        basePanel.add(closeButton);
        add(basePanel);
    }

    private void initEvent() {
        basePanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized();
            }
        });
        basePanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                menuMouseClicked(e);
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menuMouseEntered();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menuMouseExited();
            }
        });
        closeButton.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menuMouseEntered();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menuMouseExited();
            }
        });
    }

    private void formComponentResized() {
        int h = basePanel.getHeight() / 2;
        textLabel.setBounds(0, 0, basePanel.getWidth() - 20, basePanel.getHeight());
        closeButton.setBounds(textLabel.getWidth(), h - 8, 16, 16);
    }

    private void menuMouseEntered() {
        basePanel.setBackground(mouseEnteredColor);
    }

    private void menuMouseExited() {
        basePanel.setBackground(backgroundColor);
    }

    private void menuMouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 1) {
            if (null != comboBoxAction) {
                comboBoxAction.select(userObject);
                basePanel.setBackground(backgroundColor);
            }
        }
    }

    @Override
    public void setText(String text) {
        if (null != textLabel) {
            textLabel.setText(text);
        }
    }

    public Object getUserObject() {
        return userObject;
    }

    public void setUserObject(Object userObject) {
        this.userObject = userObject;
    }

    public ComboBoxAction getComboBoxAction() {
        return comboBoxAction;
    }

    public void setComboBoxAction(ComboBoxAction comboBoxAction) {
        this.comboBoxAction = comboBoxAction;
    }

    public void setIconSize(int w,int h){
    	 icon.setImage(icon.getImage().getScaledInstance((w), h, Image.SCALE_DEFAULT));
    	 this.setIcon(icon);
    }
    private void delete() {
        if (null != comboBoxAction) {
            comboBoxAction.delete(this);
        }
    }

    private class TabButton extends JButton implements ActionListener {

        private static final long serialVersionUID = 1L;
		public TabButton() {

            final int size = 17; //设置按键的大小
            setPreferredSize(new Dimension(size, size));
            setToolTipText("删除");//设置按键的提示信息
            setUI(new BasicButtonUI());//设置按键的绘制于普通按键相同
            setContentAreaFilled(false);//不对Button进行填充，就是按键是透明的
            setFocusable(false);//按键不能获得焦点
            setBorder(BorderFactory.createEtchedBorder());//设置按键的边框为雕刻样式
            setBorderPainted(false); //系统不自动绘制按键边界（这个边界在鼠标放上去之后才绘制）
            addActionListener(TabButton.this); //添加按键点击事件
            addMouseListener(mouseListener);//添加鼠标事件（主要是mouseover 和 mouse exit）
        }

        //重写Button的绘制函数
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create(); //创建一个graphics2D，因为需要在Button上画差
            g2.setStroke(new BasicStroke(2));//设置画笔，宽度为2
            g2.setColor(Color.BLACK); //设置画笔颜色
            if (getModel().isRollover()) {//当鼠标移动到Button上时，画笔为紫色
                g2.setColor(Color.PINK);
            }
            int delta = 6;//绘制差
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setStroke(new BasicStroke(1));
            //  g2.setColor(panel.getBorderColor());
            // g2.drawRoundRect(0, 0, c.getWidth() - 1, c.getHeight() - 1, panel.getRoundBorder(), panel.getRoundBorder());

            g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
            g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
            g2.dispose(); //释放画笔资源
        }

        @Override
        public void actionPerformed(ActionEvent event) {
            delete();
        }
        private final MouseListener mouseListener = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent event) {
                Component c = event.getComponent(); //鼠标移入按键，绘制按键边界
                if (c instanceof AbstractButton) {
                    ((AbstractButton) c).setBorderPainted(true);
                }
            }

            @Override
            public void mouseExited(MouseEvent event) {
                Component c = event.getComponent(); //鼠标移出按键，不绘制按键边界
                if (c instanceof AbstractButton) {
                    ((AbstractButton) c).setBorderPainted(false);
                }
            }
        };
    }
}
