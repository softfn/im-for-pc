package com.only;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.TableModel;

import com.only.box.UIBox;
import com.only.component.HeaderPane;
import com.only.component.ImagePane;

public class OnlyScrollTable extends OnlyScrollPane implements ActionListener {

	private static final long serialVersionUID = 4276053919432949896L;
	private static final Color DISABLED_BG = UIBox.getColor(UIBox.key_color_scroll_table_text_disabled_background);
	public static final String COMMAND_SHOW_MENU = "ShowMenu";
	private Border border;
	private Border disabledBorder;
	private Color background;
	private OnlyTable table;
	private OnlyButton btnUpperRight;

	public OnlyScrollTable(OnlyTable table) {
		super();
		setViewportView(this.table = table);
		init();
	}

	public OnlyScrollTable() {
		this(new OnlyTable());
	}

	public OnlyScrollTable(TableModel dm) {
		this(new OnlyTable(dm));
	}

	public OnlyScrollTable(final Object[][] rowData, final Object[] columnNames) {
		this(new OnlyTable(rowData, columnNames));
	}

	public OnlyScrollTable(Vector<?> rowData, Vector<?> columnNames) {
		this(new OnlyTable(rowData, columnNames));
	}

	private void init() {
		setBorder(new LineBorder(new Color(84, 165, 213)));
		setDisabledBorder(new LineBorder(new Color(84, 165, 213, 128)));
		setBackground(UIBox.getWhiteColor());
		setCorner(UPPER_RIGHT_CORNER, createUpperRightCorner());
		setHeaderDisabledForeground(new Color(123, 123, 122));
		table.setBorder(new EmptyBorder(0, 0, 0, 0));
		table.setDisabledBorder(table.getBorder());
		table.setVisibleInsets(0, 0, 0, 0);
		table.setAlpha(0.0f);
		table.setBackground(UIBox.getEmptyColor());
		table.getTableHeader().setBackground(UIBox.getEmptyColor());
	}

	protected void initHeader() {
	}

	private JComponent createUpperRightCorner() {

		final Image defaultImage = UIBox.getImage(UIBox.key_image_scroll_table_show_menu_button_normal);
		ImagePane pane = new ImagePane();
		btnUpperRight = new OnlyButton();
		pane.setImageOnly(true);
		pane.setBorder(new EmptyBorder(0, 0, 1, 0));
		pane.setLayout(new BorderLayout());
		pane.setFilledBorderArea(false);
		pane.setMode(ImagePane.SCALED);
		pane.setImage(defaultImage);
		pane.setAlpha(0.5f);
		btnUpperRight.setNormalImage(defaultImage);
		btnUpperRight.setRolloverImage(UIBox.getImage(UIBox.key_image_scroll_table_show_menu_button_rollover));
		btnUpperRight.setPressedImage(UIBox.getImage(UIBox.key_image_scroll_table_show_menu_button_pressed));
		btnUpperRight.setDisabledImage(btnUpperRight.getNormalImage());
		btnUpperRight.setFocusable(false);
		btnUpperRight.setActionCommand(COMMAND_SHOW_MENU);
		btnUpperRight.addActionListener(this);
		pane.add(btnUpperRight);
		return pane;
	}

	public Border getDisabledBorder() {
		return disabledBorder;
	}

	public void setDisabledBorder(Border disabledBorder) {
		this.disabledBorder = disabledBorder;

		if (!this.isEnabled()) {
			super.setBorder(disabledBorder);
		}
	}

	public void setBorder(Border border) {
		this.border = border;
		super.setBorder(border);
	}

	public Color getDisabledForeground() {
		return table.getDisabledForeground();
	}

	public void setDisabledForeground(Color disabledForeground) {
		table.setDisabledForeground(disabledForeground);
	}

	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		super.setBorder(enabled ? border : disabledBorder);
		super.setBackground(enabled ? background : DISABLED_BG);
		btnUpperRight.setVisible(enabled);
	}

	public void setBackground(Color background) {
		this.background = background;
		super.setBackground(background);
	}

	public boolean isColumnControlEnabled() {
		return btnUpperRight.isEnabled();
	}

	public void setColumnControlEnabled(boolean enabled) {
		btnUpperRight.setEnabled(enabled);
	}

	public OnlyTable getTable() {
		return table;
	}

	@Deprecated
	public JLabel getHeaderLabel() {
		return null;
	}

	@Deprecated
	@Override
	public HeaderPane getHeader() {
		return null;
	}

	@Deprecated
	@Override
	public String getHeaderText() {
		return null;
	}

	@Deprecated
	public void setHeaderText(String text) {
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(COMMAND_SHOW_MENU)) {
			JPopupMenu menu = table.getColumnControlMenu();

			if (menu != null && menu.getComponentCount() > 0) {
				Dimension buttonSize = btnUpperRight.getSize();
				menu.show(btnUpperRight, buttonSize.width - menu.getPreferredSize().width, buttonSize.height);
			}
		}
	}

	public void adjustmentValueChanged(AdjustmentEvent e) {
	}
}