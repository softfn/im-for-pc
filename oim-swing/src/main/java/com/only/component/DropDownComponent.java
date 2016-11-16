package com.only.component;

import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

public class DropDownComponent extends JComponent implements AncestorListener {

    private static final long serialVersionUID = 3991938330108392016L;

    protected JWindow popup;//弹出面板
    protected int statusCode;//状态码
    protected String status;//状态文字

    public DropDownComponent(JFrame frame) {

        //弹出窗口
        popup = new JWindow(frame);
        popup.getContentPane().add(new JLabel("1111"));
        popup.addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowLostFocus(WindowEvent evt) {
                popup.setVisible(false);
            }
        });
        popup.pack();
        initComponent();
    }

    protected void initComponent() {

        setLayout(new FlowLayout());
        addAncestorListener(this);
    }

    protected JFrame getJFrame(JComponent comp) {
        if (comp == null) {
            comp = this;
        }
        if (comp.getParent() instanceof JFrame) {
            return (JFrame) comp.getParent();
        }

        return getJFrame((JComponent) comp.getParent());
    }

    public void ancestorAdded(AncestorEvent event) {
        hidePopup();
    }

    public void ancestorMoved(AncestorEvent event) {
        if (event.getSource() != popup) {
            hidePopup();
        }
    }

    public void ancestorRemoved(AncestorEvent event) {
        hidePopup();
    }

    public void hidePopup() {
        if (popup != null && popup.isVisible()) {
            popup.setVisible(false);
        }
    }

    public void showPopup(JComponent component) {

        //显示弹出窗口
        Point pt = component.getLocationOnScreen();
        pt.translate(0, component.getHeight());
        popup.setLocation(pt);
        popup.toFront();
        popup.setVisible(true);
        popup.requestFocusInWindow();
    }

}
