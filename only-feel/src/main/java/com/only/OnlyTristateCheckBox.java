package com.only;

import java.awt.Graphics;
import java.awt.event.ItemEvent;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.swing.ButtonModel;
import javax.swing.Icon;

import com.only.box.UIBox;
import com.only.component.TristateCheckBoxState;

public class OnlyTristateCheckBox extends OnlyCheckBox {

	private static final long serialVersionUID = 380025453524576636L;
	private boolean isPainting;
	private ButtonModel buttonModel;
	private TristateCheckBoxState state;
	private Icon notspecifiedIcon;
	private Icon disabledNotspecifiedIcon;
	private Icon rolloverNotspecifiedIcon;
	private Icon pressedNotspecifiedIcon;

	public OnlyTristateCheckBox() {
		this(null);
	}

	public OnlyTristateCheckBox(String text) {
		this(text, false);
	}

	public OnlyTristateCheckBox(String text, boolean isSelected) {
		this(text, isSelected ? TristateCheckBoxState.SELECTED : TristateCheckBoxState.DESELECTED);
	}

	public OnlyTristateCheckBox(String text, TristateCheckBoxState state) {
		super(text, null, false);
		this.state = state;
		this.buttonModel = this.getModel();
		ButtonModel proxyModel = (ButtonModel) Proxy.newProxyInstance(OnlyTristateCheckBox.class.getClassLoader(), new Class[] { ButtonModel.class }, new ProxyHandler());
		setModel(proxyModel);
		setNotspecifiedIcon(UIBox.getIcon(UIBox.key_icon_tristate_checkbox_notspecified_normal));
		setDisabledNotspecifiedIcon(UIBox.getIcon(UIBox.key_icon_tristate_checkbox_notspecified_disabled));
		setRolloverNotspecifiedIcon(UIBox.getIcon(UIBox.key_icon_tristate_checkbox_notspecified_rollover));
		setPressedNotspecifiedIcon(UIBox.getIcon(UIBox.key_icon_tristate_checkbox_notspecified_pressed));
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

	public Icon getNotspecifiedIcon() {
		return this.notspecifiedIcon;
	}

	public void setNotspecifiedIcon(Icon notspecifiedIcon) {
		this.notspecifiedIcon = notspecifiedIcon;
		this.repaint();
	}

	public Icon getDisabledNotspecifiedIcon() {
		return this.disabledNotspecifiedIcon;
	}

	public void setDisabledNotspecifiedIcon(Icon disabledNotspecifiedIcon) {
		this.disabledNotspecifiedIcon = disabledNotspecifiedIcon;
		this.repaint();
	}

	public Icon getRolloverNotspecifiedIcon() {
		return this.rolloverNotspecifiedIcon;
	}

	public void setRolloverNotspecifiedIcon(Icon rolloverNotspecifiedIcon) {
		this.rolloverNotspecifiedIcon = rolloverNotspecifiedIcon;
		this.repaint();
	}

	public Icon getPressedNotspecifiedIcon() {
		return this.pressedNotspecifiedIcon;
	}

	public void setPressedNotspecifiedIcon(Icon pressedNotspecifiedIcon) {
		this.pressedNotspecifiedIcon = pressedNotspecifiedIcon;
		this.repaint();
	}

	private class ProxyHandler implements InvocationHandler {
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			String methodName = method.getName();

			if (isEnabled() && !isPainting && methodName.equals("setPressed")) {
				boolean isPressed = ((Boolean) args[0]).booleanValue();

				if (!isPressed && buttonModel.isArmed()) {
					nextState();
				}
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