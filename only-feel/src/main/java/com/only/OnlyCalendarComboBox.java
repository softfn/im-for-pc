package com.only;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Calendar;

import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.MenuSelectionManager;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.BasicPopupMenuUI;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.text.JTextComponent;

import com.only.box.UIBox;
import com.only.laf.OnlyComboBoxUI;
import com.only.util.OnlyUIUtil;
import com.only.util.OnlyFeelUtil;

@SuppressWarnings("rawtypes")
public class OnlyCalendarComboBox extends OnlyComboBox {

	private static final long serialVersionUID = -3823377723957390277L;
	private OnlyCalendarPane calendarPane;

	public OnlyCalendarComboBox() {
		super();
		setUI(new OnlyCalendarComboBoxUI());
		super.setEditable(true);
		JTextComponent editorComponent = (JTextComponent) (this.getEditor().getEditorComponent());
		editorComponent.setEditable(false);
		editorComponent.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		editorComponent.addMouseListener(new EditorListener());
		calendarPane.setClosable(true);
		calendarPane.setCloseAction(new AbstractAction() {
			private static final long serialVersionUID = 4087888078109547665L;

			@Override
			public void actionPerformed(ActionEvent e) {
				updateSelected();
			}
		});
	}

	private void updateSelected() {
		String date = calendarPane.getDateString();
		Object oldDate = getSelectedItem();

		if ((date != null && !date.equals(oldDate)) || (oldDate != null && !oldDate.equals(date))) {
			setSelectedItem(date);
		}

		if (isPopupVisible()) {
			hidePopup();
		}
	}

	public Calendar getDate() {
		return calendarPane.getDate();
	}

	public void setDate(Calendar date) {
		calendarPane.setDate(date);
		updateSelected();
	}

	public String getDateString() {
		return calendarPane.getDateString();
	}

	public void setDateString(String dateString) {
		calendarPane.setDateString(dateString);
		updateSelected();
	}

	public OnlyCalendarPane getCalendarPane() {
		return calendarPane;
	}

	private void resetBorderAfterPopupHidden() {
		Rectangle rect = new Rectangle(this.getLocationOnScreen(), this.getSize());

		if (!rect.contains(OnlyFeelUtil.getMouseLocation())) {
			mouseOut();
		}
	}

	@Override
	protected void resetShortcutKeys() {
		InputMap inputMap = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		Object value;

		for (KeyStroke ks : inputMap.allKeys()) {
			value = inputMap.get(ks);

			if (value.equals("selectNext") || value.equals("selectNext2") || value.equals("selectPrevious") || value.equals("selectPrevious2")) {
				inputMap.put(ks, "");
			}
		}
	}

	@Deprecated
	@Override
	public void setEditable(boolean editable) {
		JTextComponent editorComponent = (JTextComponent) (this.getEditor().getEditorComponent());
		if (null != editorComponent) {
			editorComponent.setEditable(false);
		}
	}

	private class EditorListener extends MouseAdapter {

		@Override
		public void mousePressed(MouseEvent e) {
			calendarPane.close();
		}
	}

	private class CCalendarPopup extends BasicComboPopup {

		private static final long serialVersionUID = -2699306734874068484L;
		private final int SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
//		private JWindow heavyWeightWindow;
//		private Boolean heavyWeightWindowOpaque;
//		private Boolean heavyWeightWindowContentPaneOpaque;
		private boolean buffered;

		public CCalendarPopup() {
			super(OnlyCalendarComboBox.this);
			setUI(new BasicPopupMenuUI() {
				public void installDefaults() {
				}

				protected void uninstallDefaults() {
				}
			});
			setBorder(new EmptyBorder(0, 0, 0, 0));
			setOpaque(false);
			remove(scroller);
			setLayout(new BorderLayout());
			add(calendarPane = new OnlyCalendarPane(), BorderLayout.CENTER);
		}

		public void paint(Graphics g) {
			if (!OnlyUIUtil.isTranslucencySupported() || !buffered) {
				super.paint(g);
			} else {
				Insets insets = this.getInsets();
				int x = insets.left;
				int y = insets.top;
				int width = this.getWidth();
				int height = this.getHeight();
				int contentWidth = width - insets.left - insets.right;
				int contentHeight = height - insets.top - insets.bottom;
				BufferedImage image = OnlyUIUtil.getGraphicsConfiguration(this).createCompatibleImage(width, height, Transparency.TRANSLUCENT);
				BufferedImage contentImage = OnlyUIUtil.getGraphicsConfiguration(this).createCompatibleImage(contentWidth, contentHeight, Transparency.OPAQUE);
				Graphics2D g2d = image.createGraphics();
				Graphics2D contentG2d = contentImage.createGraphics();
				contentG2d.translate(-x, -y);
				super.paint(g2d);
				super.paint(contentG2d);
				g2d.dispose();
				contentG2d.dispose();
				g.drawImage(image, 0, 0, this);
				g.drawImage(contentImage, x, y, this);
			}
		}

		public void setVisible(boolean visible) {
			if (visible == isVisible()) {
				return;
			}

			if (visible) {
				Object value = getSelectedItem();
				calendarPane.setDateString(value == null ? null : value.toString());
			} else {
				calendarPane.clearMouseStatus();
			}

			super.setVisible(visible);

//			if (UIUtil.isTranslucencySupported()) {
//				if (visible && !isOpaque()) {
//					heavyWeightWindow = UIUtil.getHeavyWeightWindow(this);
//				}
//
//				if (heavyWeightWindow != null) {
//					JComponent contentPane = (JComponent) heavyWeightWindow.getContentPane();
//
//					if (visible) {
//						buffered = true;
//						heavyWeightWindowOpaque = AWTUtilities.isWindowOpaque(heavyWeightWindow);
//						heavyWeightWindowContentPaneOpaque = contentPane.isOpaque();
//						contentPane.setOpaque(false);
//						AWTUtilities.setWindowOpaque(heavyWeightWindow, false);
//					} else {
//						contentPane.setOpaque(heavyWeightWindowContentPaneOpaque);
//						AWTUtilities.setWindowOpaque(heavyWeightWindow, heavyWeightWindowOpaque);
//						heavyWeightWindowOpaque = null;
//						heavyWeightWindowContentPaneOpaque = null;
//						heavyWeightWindow = null;
//					}
//				}
//			}

			if (!visible) {
				resetBorderAfterPopupHidden();
			}
		}

		public void show() {
			if (comboBox instanceof OnlyComboBox && !((OnlyComboBox<?>) comboBox).isEditableAll()) {
				return;
			}

			super.show();
		}

		public void show(Component invoker, int x, int y) {
			if (invoker != null) {
				int parentWidth = invoker.getWidth();
				Dimension size = this.getPreferredSize();
				int width = size.width;
				int height = size.height;
				x = parentWidth > width ? parentWidth - width : 0;
				y = SCREEN_HEIGHT - (invoker.getLocationOnScreen().y + invoker.getHeight()) < height ? -height : invoker.getHeight();
			}

			super.show(invoker, x, y);
		}

		@Deprecated
		public void updateUI() {
			setUI(this.getUI());
		}

		protected void togglePopup() {
		}
	}

	private class OnlyCalendarComboBoxUI extends OnlyComboBoxUI {

		private MouseListener mouseListener;
		private KeyListener keyListener;

		protected JButton createArrowButton() {
			OnlyButton button = new OnlyButton();
			button.setName("CalendarComboBox.arrowButton");
			button.setNormalImage(buttonImage = UIBox.getImage(UIBox.key_image_calendar_combo_box_button_normal));
			button.setDisabledImage(OnlyUIUtil.toBufferedImage(buttonImage, 0.5f, button));
			button.setRolloverImage(buttonRolloverImage = UIBox.getImage(UIBox.key_image_calendar_combo_box_button_rollover));
			button.setPressedImage(UIBox.getImage(UIBox.key_image_calendar_combo_box_button_pressed));
			button.setNormalImageInsets(2, 2, 0, 0);
			return button;
		}

		public void configureArrowButton() {
			super.configureArrowButton();

			if (arrowButton != null) {
				arrowButton.addMouseListener(getMouseListener());
			}
		}

		public void unconfigureArrowButton() {
			super.unconfigureArrowButton();

			if (arrowButton != null) {
				arrowButton.removeMouseListener(getMouseListener());
			}
		}

		protected ComboPopup createPopup() {
			return new CCalendarPopup();
		}

		protected void installListeners() {
			super.installListeners();
			comboBox.getEditor().getEditorComponent().addKeyListener(getKeyListener());
		}

		protected void uninstallListeners() {
			super.uninstallListeners();
			comboBox.getEditor().getEditorComponent().removeKeyListener(keyListener);
		}

		protected void selectNextPossibleValue() {
			if (comboBox instanceof OnlyCalendarComboBox && !((OnlyCalendarComboBox) comboBox).isEditableAll()) {
				return;
			}

			changeDate(1);
		}

		protected void selectPreviousPossibleValue() {
			if (comboBox instanceof OnlyCalendarComboBox && !((OnlyCalendarComboBox) comboBox).isEditableAll()) {
				return;
			}

			changeDate(-1);
		}

		private void changeDate(int day) {
			if (comboBox instanceof OnlyCalendarComboBox) {
				OnlyCalendarComboBox calendarBox = (OnlyCalendarComboBox) comboBox;
				Calendar date = calendarBox.getDate();

				if (date == null) {
					date = Calendar.getInstance();
				} else {
					date.add(Calendar.DAY_OF_YEAR, day);
				}

				calendarBox.setDate(date);
			}
		}

		private MouseListener getMouseListener() {
			if (mouseListener == null) {
				mouseListener = new MouseAdapter() {
					private boolean visible;

					public void mouseReleased(MouseEvent e) {
						if (!(!visible && comboBox instanceof OnlyComboBox && (!comboBox.isEnabled() || !((OnlyComboBox<?>) comboBox).isEditableAll()))) {
							comboBox.setPopupVisible(!visible);
						}
					}

					public void mousePressed(MouseEvent e) {
						visible = comboBox.isPopupVisible();
						MenuSelectionManager.defaultManager().clearSelectedPath();
					}
				};
			}

			return mouseListener;
		}

		private KeyListener getKeyListener() {
			if (keyListener == null) {
				keyListener = new KeyAdapter() {
					public void keyPressed(KeyEvent e) {
						int keyCode = e.getKeyCode();

						if (e.getModifiers() == 0) {
							if (keyCode == KeyEvent.VK_UP) {
								selectPreviousPossibleValue();
							} else if (keyCode == KeyEvent.VK_DOWN) {
								selectNextPossibleValue();
							}
						}
					}
				};
			}

			return keyListener;
		}
	}
}