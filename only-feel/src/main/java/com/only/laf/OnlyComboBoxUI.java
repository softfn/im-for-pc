package com.only.laf;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;

import javax.swing.ComboBoxEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.BasicListUI;
import javax.swing.plaf.basic.BasicPopupMenuUI;
import javax.swing.plaf.basic.BasicScrollPaneUI;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.plaf.basic.ComboPopup;

import sun.swing.DefaultLookup;

import com.only.OnlyButton;
import com.only.OnlyComboBox;
import com.only.box.UIBox;
import com.only.util.OnlyUIUtil;

public class OnlyComboBoxUI extends BasicComboBoxUI {

	private static final Color DISABLED_BG = UIBox.getColor(UIBox.key_color_combo_box_text_disabled_background);
	private static final Color NON_EDITABLE_BG = UIBox.getColor(UIBox.key_color_combo_box_text_not_editable_background);
	private static final Image RENDERER_BORDER_IMAGE = UIBox.getImage(UIBox.key_image_combo_box_renderer_border_inner);
	private static final Image RENDERER_BORDER_DISABLED_IMAGE = UIBox.getImage(UIBox.key_image_combo_box_renderer_border_disabled);
	protected Image buttonImage, buttonRolloverImage;

	public static ComponentUI createUI(JComponent c) {
		return new OnlyComboBoxUI();
	}

	@Override
	public void paint(Graphics g, JComponent c) {
		hasFocus = comboBox.hasFocus();
		Rectangle r = rectangleForCurrentValue();
		paintCurrentValueBackground(g, r, hasFocus);

		if (!comboBox.isEditable()) {
			paintCurrentValue(g, r, hasFocus);
		}
	}

	@Override
	public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
		if (comboBox instanceof OnlyComboBox) {
			OnlyComboBox<?> ccombox = (OnlyComboBox<?>) comboBox;
			float alpha = ccombox.getAlpha();
			Image image = ccombox.getImage();
			boolean imageOnly = ccombox.isImageOnly();

			if (alpha > 0.0 && !(imageOnly && image == null)) {
				OnlyUIUtil.paintBackground(g, ccombox, ccombox.isEditableAll() ? ccombox.getBackground() : NON_EDITABLE_BG, DISABLED_BG, image, imageOnly, alpha, ccombox.getVisibleInsets());

				if (!ccombox.isEnabled() || ccombox.isEditableAll()) {
					paintRendererBorder(g, bounds);
				}
			}
		} else {
			super.paintCurrentValueBackground(g, bounds, hasFocus);
		}
	}

	private void paintRendererBorder(Graphics g, Rectangle bounds) {
		Image image = comboBox.isEnabled() ? RENDERER_BORDER_IMAGE : RENDERER_BORDER_DISABLED_IMAGE;
		Insets imageInsets = new Insets(2, 2, 0, 0);
		Rectangle paintRect = new Rectangle(bounds.x, bounds.y, bounds.width + arrowButton.getWidth(), bounds.height);
		OnlyUIUtil.paintImage(g, image, imageInsets, paintRect, comboBox);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
		ListCellRenderer renderer = comboBox.getRenderer();
		Component c;

		if (hasFocus && !isPopupVisible(comboBox)) {
			c = renderer.getListCellRendererComponent(listBox, comboBox.getSelectedItem(), -1, true, false);
		} else {
			c = renderer.getListCellRendererComponent(listBox, comboBox.getSelectedItem(), -1, false, false);
		}

		((JComponent) c).setOpaque(false);
		//((JComponent) c).setBorder(OnlyComboBoxRenderer.SELECTED_BORDER);
		c.setFont(comboBox.getFont());

		if (comboBox instanceof OnlyComboBox) {
			c.setForeground(comboBox.isEnabled() ? comboBox.getForeground() : ((OnlyComboBox) comboBox).getDisabledTextColor());
		} else {
			c.setForeground(comboBox.isEnabled() ? comboBox.getForeground() : DefaultLookup.getColor(comboBox, this, "ComboBox.disabledForeground", null));
		}

		currentValuePane.paintComponent(g, c, comboBox, bounds.x, bounds.y, bounds.width, bounds.height, c instanceof JPanel);
	}

	@Override
	protected JButton createArrowButton() {
		buttonImage = UIBox.getImage(UIBox.key_image_combo_box_button_normal);
		buttonRolloverImage = UIBox.getImage(UIBox.key_image_combo_box_button_rollover);
		OnlyButton button = new OnlyButton();
		button.setName("ComboBox.arrowButton");
		button.setNormalImage(buttonImage);
		button.setDisabledImage(button.getNormalImage());
		button.setRolloverImage(buttonRolloverImage );
		button.setPressedImage(UIBox.getImage(UIBox.key_image_combo_box_button_pressed));
		
		button.setIcon(UIBox.getIcon(UIBox.key_icon_combo_box_button_arrow_normal));
		button.setDisabledIcon(UIBox.getIcon(UIBox.key_icon_combo_box_button_arrow_disabled));
		button.setNormalImageInsets(1, 2, 1, 1);
		return button;
	}
	
	
	
	
	@Override
	public void configureArrowButton() {
		super.configureArrowButton();

		if (arrowButton != null) {
			arrowButton.setFocusable(false);
		}
	}

	@Override
	protected ComboBoxEditor createEditor() {

		ComboBoxEditor editor = new OnlyComboBoxEditor();
		JTextField field = (JTextField) editor.getEditorComponent();
		//field.setBorder(null);
		field.setBorder(UIBox.getBorder(UIBox.key_border_combo_box_editor));
		field.setSelectionColor(UIBox.getColor(UIBox.key_color_combo_box_editor_text_selection));
		field.setSelectedTextColor(UIBox.getColor(UIBox.key_color_combo_box_editor_text_selection_foregroun));
		field.setOpaque(false);
		field.setBackground(UIBox.getWhiteColor());
		field.setForeground(Color.BLACK);
		field.setCaretColor(Color.BLACK);
		field.setMargin(new Insets(0, 0, 0, 0));
		field.setFont(OnlyUIUtil.getDefaultFont());

		if (comboBox instanceof OnlyComboBox) {
			field.setDisabledTextColor(((OnlyComboBox<?>) comboBox).getDisabledTextColor());
		}

		return editor;
	}

	@Override
	protected ComboPopup createPopup() {
		return new OnlyComboPopup(comboBox);
	}

	public ComboPopup getPopup() {
		return popup;
	}

	public JButton getArrowButton() {
		return arrowButton;
	}

	public void changeButtonBorder(boolean mouseIn) {
		((OnlyButton) arrowButton).setNormalImage(mouseIn ? buttonRolloverImage : buttonImage);
	}

	protected void selectNextPossibleValue() {
		if (comboBox instanceof OnlyComboBox && !((OnlyComboBox<?>) comboBox).isEditableAll()) {
			return;
		}

		super.selectNextPossibleValue();
	}

	protected void selectPreviousPossibleValue() {
		if (comboBox instanceof OnlyComboBox && !((OnlyComboBox<?>) comboBox).isEditableAll()) {
			return;
		}

		super.selectPreviousPossibleValue();
	}

	protected void installDefaults() {
	}

	protected void uninstallDefaults() {
	}

	private class OnlyComboBoxEditor extends BasicComboBoxEditor.UIResource {

		@Override
		protected JTextField createEditorComponent() {
			JTextField editor = new BorderlessTextField("", 9);
			editor.setBorder(null);
			return editor;
		}
	}

	private static class BorderlessTextField extends JTextField {

		private static final long serialVersionUID = -330778040758821755L;

		public BorderlessTextField(String value, int n) {
			super(value, n);
			setUI(new BasicTextFieldUI());
		}

		public void setText(String s) {
			if (getText().equals(s)) {
				return;
			}

			super.setText(s);
			setSelectionStart(0);
			setSelectionEnd(0);
		}

		public void setBorder(Border b) {
			if (!(b instanceof BasicComboBoxEditor.UIResource)) {
				super.setBorder(b);
			}
		}

		@Deprecated
		public void updateUI() {
		}
	}

	private class OnlyComboPopup extends BasicComboPopup {

		private static final long serialVersionUID = 7897660999228190162L;
		private final Border VIEWPORT_BORDER = new EmptyBorder(0, 0, 0, 1);
		@SuppressWarnings("rawtypes")
		private JComboBox combo;

		@SuppressWarnings("rawtypes")
		public OnlyComboPopup(JComboBox combo) {
			super(combo);
			setUI(new BasicPopupMenuUI() {
				public void installDefaults() {
				}

				protected void uninstallDefaults() {
				}
			});
			this.combo = combo;
			setBorder(new EmptyBorder(0, 0, 0, 0));
			setOpaque(false);
		}

		protected JScrollPane createScroller() {
			JScrollPane sp = new JScrollPane(list, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER) {
				private static final long serialVersionUID = -2214894633749404512L;

				public JScrollBar createVerticalScrollBar() {
					ScrollBar vBar = new ScrollBar(JScrollBar.VERTICAL) {
						private static final long serialVersionUID = 397439195886258194L;

						@Deprecated
						public void updateUI() {
						}
					};

					vBar.setUI(new OnlyScrollBarUI());
					vBar.setBorder(null);
					return vBar;
				}

				@Deprecated
				public void updateUI() {
				}
			};

			sp.setUI(new BasicScrollPaneUI() {
				public void update(Graphics g, JComponent c) {
					g.setColor(c.getBackground());
					g.fillRect(0, 0, c.getWidth(), c.getHeight());
					paint(g, c);
				}

				protected void installDefaults(JScrollPane scrollpane) {
				}

				protected void uninstallDefaults(JScrollPane scrollpane) {
				}
			});
			sp.setHorizontalScrollBar(null);
			return sp;
		}

		protected void configureScroller() {
			super.configureScroller();
			scroller.getVerticalScrollBar().setOpaque(false);
			scroller.getViewport().setOpaque(false);
			scroller.setOpaque(false);
			scroller.setBorder(UIBox.getBorder(UIBox.key_border_combo_box_popup));
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		protected JList createList() {
			JList list = new JList(comboBox.getModel()) {
				private static final long serialVersionUID = -3870496246576971437L;

				@Override
				public void processMouseEvent(MouseEvent e) {
					if ((e.getModifiers() & Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()) != 0) {
						Toolkit toolkit = Toolkit.getDefaultToolkit();
						e = new MouseEvent((Component) e.getSource(), e.getID(), e.getWhen(), e.getModifiers() ^ toolkit.getMenuShortcutKeyMask(), e.getX(), e.getY(), e.getXOnScreen(), e.getYOnScreen(), e.getClickCount(), e.isPopupTrigger(), MouseEvent.NOBUTTON);
					}

					super.processMouseEvent(e);
				}

				@Deprecated
				public void updateUI() {
				}
			};

			list.setUI(new BasicListUI() {
				protected void installDefaults() {
					list.setLayout(null);
				}

				protected void uninstallDefaults() {
					if (list.getTransferHandler() instanceof UIResource) {
						list.setTransferHandler(null);
					}
				}
			});
			return list;
		}

		protected void configureList() {
			super.configureList();
			list.setOpaque(false);
		}

		public void show() {
			if (combo instanceof OnlyComboBox && !((OnlyComboBox<?>) combo).isEditableAll()) {
				return;
			}

			super.show();
		}

		public void setVisible(boolean visible) {
			super.setVisible(visible);

			if (visible) {
				if (combo instanceof OnlyComboBox) {
					OnlyComboBox<?> cbox = (OnlyComboBox<?>) combo;
					float alpha = cbox.isImageOnly() ? 0.0f : cbox.getAlpha();
					Color oldBg = cbox.getBackground();
					scroller.setBackground(new Color(oldBg.getRed(), oldBg.getGreen(), oldBg.getBlue(), (int) Math.round(255 * alpha)));
				} else {
					scroller.setBackground(combo.getBackground());
				}

				scroller.setViewportBorder(scroller.getVerticalScrollBar().isVisible() ? VIEWPORT_BORDER : null);
			} else if (combo instanceof OnlyComboBox) {
				((OnlyComboBox<?>) combo).resetBorder();
			}
		}

		@Deprecated
		@Override
		public void updateUI() {
			setUI(this.getUI());
		}
	}
}