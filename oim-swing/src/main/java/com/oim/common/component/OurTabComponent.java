/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oim.common.component;

/**
 *
 * @author XiaHui
 */
import com.oim.common.component.util.ComponentUtil;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicButtonUI;



@SuppressWarnings("serial")
public class OurTabComponent extends JPanel {

    private final JTabbedPane TabbedPane;
    private JLabel label;

    public OurTabComponent(final JTabbedPane TabbedPane, boolean close) {
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));//设置标签头要水平的，从左向右的排列组件，并且组件的间距为0（横向和纵向）
        setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
        if (TabbedPane == null) {
            throw new NullPointerException("pane can not be null");
        }
        this.TabbedPane = TabbedPane;
        setOpaque(false); //不画出panel的边界
        label = new JLabel() {//创建标签头的文字内容
            //重写方法，返回pane指定位置的名字，这里有点绕，可以好好理解理解
            //为什么不直接设置标签的内容，而是通过重写函数来修改标签内容
            //可能是希望业务逻辑尽量不要散落在外部
            @Override
            public String getText() {
                int i = TabbedPane.indexOfTabComponent(OurTabComponent.this);//可以获得当前panel在tab中的位置
                if (i != -1) {//得到当前panel的名字（实际是tab的名字）
                    return TabbedPane.getTitleAt(i);
                }
                return null;
            }
        };

        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        label.setFont(ComponentUtil.getDefaultFont());
        add(label);
        if (close) {
            JButton button = new TabButton(); //创建关闭按钮（就是那个差按键）
            add(button);
        }
       
    }

    public void setIcon(Icon icon) {
        label.setIcon(icon);
    }

    private class TabButton extends JButton implements ActionListener {

        public TabButton() {

            final int size = 17; //设置按键的大小
            setPreferredSize(new Dimension(size, size));
            setToolTipText("关闭窗口");//设置按键的提示信息
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
        public void actionPerformed(ActionEvent arg0) {
            int i = TabbedPane.indexOfTabComponent(OurTabComponent.this); //当按键被点击，关闭当前标签页
            if (i != -1) {
                TabbedPane.remove(i);
            }
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