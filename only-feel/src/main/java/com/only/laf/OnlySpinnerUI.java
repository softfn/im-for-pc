package com.only.laf;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSpinnerUI;

import com.only.OnlyButton;
import com.only.OnlySpinner;
import com.only.box.UIBox;
import com.only.util.OnlyUIUtil;

public class OnlySpinnerUI extends BasicSpinnerUI {
	
	private static final Color DISABLED_BG = UIBox.getColor(UIBox.key_color_spinner_text_disabled_background);
	private LayoutManager layout;

	public static ComponentUI createUI(JComponent c) {
		return new OnlySpinnerUI();
	}

	protected Component createPreviousButton() {
		OnlyButton button = new OnlyButton();
		button.setPreferredSize(new Dimension(20, -1));
		button.setRequestFocusEnabled(false);
		installPreviousButtonListeners(button);
		button.setNormalImage(UIBox.getImage(UIBox.key_image_spinner_previous_normal));
		button.setPressedImage(UIBox.getImage(UIBox.key_image_spinner_previous_pressed));
		button.setRolloverImage(UIBox.getImage(UIBox.key_image_spinner_previous_rollover));
		button.setDisabledImage(UIBox.getImage(UIBox.key_image_spinner_previous_disabled));
		button.setIcon(UIBox.getIcon(UIBox.key_icon_spinner_previous_normal));
		button.setDisabledIcon(UIBox.getIcon(UIBox.key_icon_spinner_previous_disabled));
		button.setNormalImageInsets(2, 2, 2, 2);


		return button;
	}

	protected Component createNextButton() {

		
		OnlyButton button = new OnlyButton();
		button.setPreferredSize(new Dimension(20, -1));
		button.setRequestFocusEnabled(false);
		installNextButtonListeners(button);
		button.setNormalImage(UIBox.getImage(UIBox.key_image_spinner_next_normal));
		button.setPressedImage(UIBox.getImage(UIBox.key_image_spinner_next_pressed));
		button.setRolloverImage(UIBox.getImage(UIBox.key_image_spinner_next_rollover));
		button.setDisabledImage(UIBox.getImage(UIBox.key_image_spinner_next_disabled));
		button.setIcon(UIBox.getIcon(UIBox.key_icon_spinner_next_normal));
		button.setDisabledIcon(UIBox.getIcon(UIBox.key_icon_spinner_next_disabled));
		button.setNormalImageInsets(2, 2, 2, 2);
		return button;
	}

	public void update(Graphics g, JComponent c) {
		paintBackground(g, c);
		super.update(g, c);
	}

	private void paintBackground(Graphics g, JComponent c) {
		if (c instanceof OnlySpinner) {
			OnlySpinner spinner = (OnlySpinner) c;
			OnlyUIUtil.paintBackground(g, spinner, spinner.getBackground(), DISABLED_BG, spinner.getImage(), spinner.isImageOnly(), spinner.getAlpha(), spinner.getVisibleInsets());
		}
	}

	protected LayoutManager createLayout() {
		if (layout == null) {
			layout = new SpinnerUILayout();
		}

		return layout;
	}

	protected void installDefaults() {
		spinner.setLayout(createLayout());
	}

	protected void uninstallDefaults() {
	}

	private class SpinnerUILayout implements LayoutManager {
		private final Dimension ZERO_SIZE = new Dimension(0, 0);

		private Component nextButton;

		private Component previousButton;

		private Component editor;

		public void addLayoutComponent(String name, Component c) {
			if ("Next".equals(name)) {
				nextButton = c;
			} else if ("Previous".equals(name)) {
				previousButton = c;
			} else if ("Editor".equals(name)) {
				editor = c;
			}
		}

		public void removeLayoutComponent(Component c) {
			if (c == nextButton) {
				nextButton = null;
			} else if (c == previousButton) {
				previousButton = null;
			} else if (c == editor) {
				editor = null;
			}
		}

		private Dimension preferredSize(Component c) {
			return (c == null) ? ZERO_SIZE : c.getPreferredSize();
		}

		public Dimension preferredLayoutSize(Container parent) {
			Dimension nextD = preferredSize(nextButton);
			Dimension previousD = preferredSize(previousButton);
			Dimension editorD = preferredSize(editor);
			editorD.height = ((editorD.height + 1) / 2) * 2;
			Dimension size = new Dimension(editorD.width, editorD.height);
			size.width += Math.max(nextD.width, previousD.width);
			Insets insets = parent.getInsets();
			size.width += insets.left + insets.right;
			size.height += insets.top + insets.bottom;
			return size;
		}

		public Dimension minimumLayoutSize(Container parent) {
			return preferredLayoutSize(parent);
		}

		private void setBounds(Component c, int x, int y, int width, int height) {
			if (c != null) {
				c.setBounds(x, y, width, height);
			}
		}

		public void layoutContainer(Container parent) {
			int width = parent.getWidth();
			int height = parent.getHeight();
			Insets insets = parent.getInsets();
			Dimension nextD = preferredSize(nextButton);
			Dimension previousD = preferredSize(previousButton);
			int buttonsWidth = Math.max(nextD.width, previousD.width);
			int editorHeight = height - (insets.top + insets.bottom);
			int editorX, editorWidth, buttonsX;

			if (parent.getComponentOrientation().isLeftToRight()) {
				editorX = insets.left;
				editorWidth = width - buttonsWidth - insets.left - insets.right;
				buttonsX = width - buttonsWidth - insets.right;
			} else {
				buttonsX = insets.left;
				editorX = buttonsX + buttonsWidth;
				editorWidth = width - buttonsWidth - insets.left - insets.right;
			}

			int nextY = insets.top;
			int nextHeight = editorHeight / 2;
			int previousY = insets.top + nextHeight + editorHeight % 2;
			int previousHeight = height - previousY - insets.bottom;

			setBounds(editor, editorX, nextY, editorWidth, editorHeight);
			setBounds(nextButton, buttonsX, nextY, buttonsWidth, nextHeight);
			setBounds(previousButton, buttonsX, previousY, buttonsWidth, previousHeight);
		}
	}
}