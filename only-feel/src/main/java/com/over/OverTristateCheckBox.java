package com.over;

import java.awt.Graphics;
import java.awt.event.ItemEvent;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.swing.ButtonModel;
import javax.swing.JCheckBox;

import com.only.component.TristateCheckBoxState;

public class OverTristateCheckBox extends JCheckBox {

    private static final long serialVersionUID = 380025453524576636L;
    private boolean isPainting;
    private ButtonModel buttonModel;
    private TristateCheckBoxState state;

    public OverTristateCheckBox() {
        this(null);
    }

    public OverTristateCheckBox(String text) {
        this(text, false);
    }

    public OverTristateCheckBox(String text, boolean isSelected) {
        this(text, isSelected ? TristateCheckBoxState.SELECTED : TristateCheckBoxState.DESELECTED);
    }

    public OverTristateCheckBox(String text, TristateCheckBoxState state) {
        super(text, null, false);
        this.state = state;
        this.buttonModel = this.getModel();
        ButtonModel proxyModel = (ButtonModel) Proxy.newProxyInstance(OverTristateCheckBox.class.getClassLoader(), new Class[]{ButtonModel.class}, new ProxyHandler());
        setModel(proxyModel);
    }

    public TristateCheckBoxState getState() {
        return state;
    }

    public void setState(TristateCheckBoxState state) {
        setState(state, true);
    }

    private void setState(TristateCheckBoxState state, boolean fireEventAndRepaint) {
        if (state != getState()) {
            this.state = state;

            if (fireEventAndRepaint) {
                fireStateChanged();
                fireItemStateChanged(new ItemEvent(buttonModel, ItemEvent.ITEM_STATE_CHANGED, buttonModel, buttonModel.isSelected() ? ItemEvent.SELECTED : ItemEvent.DESELECTED));
                repaint();
            }
        }
    }

    private void nextState() {
        if (state == TristateCheckBoxState.SELECTED) {
            state = TristateCheckBoxState.NOTSPECIFIED;
        } else if (state == TristateCheckBoxState.DESELECTED) {
            state = TristateCheckBoxState.SELECTED;
        } else {
            state = TristateCheckBoxState.DESELECTED;
        }

        fireStateChanged();
    }

    public void paintComponent(Graphics g) {
        isPainting = true;
        super.paintComponent(g);
        isPainting = false;
    }

    private class ProxyHandler implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodName = method.getName();

            if (isEnabled() && !isPainting && methodName.equals("setPressed")) {
                boolean isPressed = ((Boolean) args[0]).booleanValue();

                if (!isPressed && buttonModel.isArmed()) {
                    nextState();
                }
            }

            if ((isPainting && state == TristateCheckBoxState.NOTSPECIFIED) && (methodName.equals("isPressed") || methodName.equals("isArmed"))) {
                return Boolean.TRUE;
            }

            if (methodName.equals("isSelected")) {
                if (state == TristateCheckBoxState.SELECTED || state == TristateCheckBoxState.NOTSPECIFIED) {
                    return Boolean.TRUE;
                } else {
                    return Boolean.FALSE;
                }
            }

            if (methodName.equals("setSelected")) {
                if (Boolean.TRUE.equals(args[0])) {
                    setState(TristateCheckBoxState.SELECTED, false);
                } else {
                    setState(TristateCheckBoxState.DESELECTED, false);
                }
            }

            return method.invoke(buttonModel, args);
        }
    }
}