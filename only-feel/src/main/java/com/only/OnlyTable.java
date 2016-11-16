package com.only;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Image;
import java.awt.Insets;
import java.lang.reflect.Constructor;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.UIDefaults;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import com.only.box.UIBox;
import com.only.laf.OnlyTableBooleanRenderer;
import com.only.laf.OnlyTableCellRenderer;
import com.only.laf.OnlyTableColumnModel;
import com.only.laf.OnlyTableUI;
import com.only.util.OnlyUIUtil;

public class OnlyTable extends JTable {

	private static final long serialVersionUID = -3423702493595092240L;
	private static final Color DISABLED_BG = UIBox.getColor(UIBox.key_color_table_text_disabled_background);
	private Image image;
	private float alpha;
	private boolean imageOnly;
	private boolean rendererOpaque;
	private boolean showRowLines;
	private boolean showColumnLines;
	private boolean intelliMode;
	private boolean inLayout;
	private int oldAutoResizeMode;
	private Insets visibleInsets;
	private Border border;
	private Border disabledBorder;
	private Border cellInsideBorder;
	private Color disabledForeground;
	private Color background;
	private Color rendererBackground1;
	private Color rendererBackground2;
	private Color gridColor;
	private Color disabledGridColor;
	private JPopupMenu columnControlMenu;
	protected Vector<Vector<Color>> colorList = new Vector<Vector<Color>>();

	public OnlyTable() {
		this(null, null, null);
	}

	public OnlyTable(TableModel dm) {
		this(dm, null, null);
	}

	public OnlyTable(TableModel dm, TableColumnModel cm) {
		this(dm, cm, null);
	}

	public OnlyTable(int numRows, int numColumns) {
		this(new DefaultTableModel(numRows, numColumns));
	}

	public OnlyTable(Vector<?> rowData, Vector<?> columnNames) {
		this(new DefaultTableModel(rowData, columnNames));
	}

	public OnlyTable(final Object[][] rowData, final Object[] columnNames) {
		this(new AbstractTableModel() {
			private static final long serialVersionUID = -4672208434155296416L;

			public String getColumnName(int column) {
				return columnNames[column].toString();
			}

			public int getRowCount() {
				return rowData.length;
			}

			public int getColumnCount() {
				return columnNames.length;
			}

			public Object getValueAt(int row, int col) {
				return rowData[row][col];
			}

			public boolean isCellEditable(int row, int column) {
				return true;
			}

			public void setValueAt(Object value, int row, int col) {
				rowData[row][col] = value;
				fireTableCellUpdated(row, col);
			}
		});
	}

	public OnlyTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm) {
		super(dm, cm, sm);
		setUI(new OnlyTableUI());
		setBorder(new CompoundBorder(new LineBorder(new Color(84, 165, 213)), new EmptyBorder(1, 1, 1, 1)));
		setDisabledBorder(new CompoundBorder(new LineBorder(new Color(84, 165, 213, 128)), new EmptyBorder(1, 1, 1, 1)));
		setCellInsideBorder(new EmptyBorder(0, 3, 0, 3));
		setColumnModel(new OnlyTableColumnModel(columnControlMenu = new OnlyPopupMenu()));
		setRowHeight(20);
		setRowMargin(0);
		columnModel.setColumnMargin(0);
		setFont(OnlyUIUtil.getDefaultFont());
		setBackground(UIBox.getWhiteColor());
		setForeground(Color.BLACK);
		setSelectionForeground(UIBox.getWhiteColor());
		setDisabledForeground(new Color(123, 123, 122));
		setGridColor(new Color(84, 165, 213));
		setDisabledGridColor(new Color(84, 165, 213, 128));
		setHorizontalScrollEnabled(true);
		super.setShowVerticalLines(false);
		super.setShowHorizontalLines(false);
		super.setOpaque(false);
		setFillsViewportHeight(true);
		alpha = 1.0f;
		visibleInsets = new Insets(1, 1, 1, 1);
		rendererOpaque = true;
		rendererBackground1 = new Color(251, 251, 255);
		rendererBackground2 = new Color(243, 248, 251);
	}

	protected JTableHeader createDefaultTableHeader() {
		return new OnlyTableHeader(columnModel);
	}

	protected void createDefaultRenderers() {
		defaultRenderersByColumnClass = new UIDefaults(8, 0.75f);
		setDefaultRenderer(Object.class, new OnlyTableCellRenderer.UIResource());
		setDefaultRenderer(Boolean.class, new OnlyTableBooleanRenderer.UIResource());
		setDefaultRenderer(Number.class, new NumberRenderer());
		setDefaultRenderer(Float.class, new DoubleRenderer());
		setDefaultRenderer(Double.class, new DoubleRenderer());
		setDefaultRenderer(Date.class, new DateRenderer());
		setDefaultRenderer(Icon.class, new IconRenderer());
		setDefaultRenderer(ImageIcon.class, new IconRenderer());
	}

	protected void createDefaultEditors() {
		defaultEditorsByColumnClass = new UIDefaults(3, 0.75f);
		setDefaultEditor(Object.class, new GenericEditor());
		setDefaultEditor(Number.class, new GenericEditor());
		setDefaultEditor(Boolean.class, new BooleanEditor());
	}

	protected void configureEnclosingScrollPane() {
		super.configureEnclosingScrollPane();
		Container p = getParent();

		if (p instanceof JViewport) {
			Container gp = p.getParent();

			if (gp instanceof JScrollPane) {
				JScrollPane scrollPane = (JScrollPane) gp;
				JViewport viewport = scrollPane.getColumnHeader();

				if (viewport != null && viewport.isOpaque()) {
					viewport.setOpaque(false);
				}
			}
		}
	}

	public void packAllColumns() {
		for (int col = 0; col < this.getColumnCount(); col++) {
			packColumn(this.getColumnModel().getColumn(col), -1);
		}
	}

	public void packColumn(Object identifier, int margin) {
		packColumn(this.getColumn(identifier), margin);
	}

	public void packColumn(TableColumn column, int margin) {
		TableColumnModel colModel = this.getColumnModel();

		if (colModel instanceof OnlyTableColumnModel && !((OnlyTableColumnModel) colModel).isColumnVisible(column)) {
			throw new IllegalStateException("column must be visible to pack");
		}

		int columnIndex = this.convertColumnIndexToView(column.getModelIndex());
		int width = 0;
		TableCellRenderer headerRenderer = getHeaderRenderer(column);
		TableCellRenderer renderer = getCellRenderer(column);
		margin = margin < 0 ? 3 : margin;

		if (headerRenderer != null) {
			Component comp = headerRenderer.getTableCellRendererComponent(this, column.getHeaderValue(), false, false, 0, columnIndex);
			width = comp.getPreferredSize().width;
		}

		for (int row = 0; row < this.getRowCount(); row++) {
			Component comp = renderer.getTableCellRendererComponent(this, this.getValueAt(row, columnIndex), false, false, row, columnIndex);
			width = Math.max(width, comp.getPreferredSize().width);
		}

		width += 2 * margin;
		column.setPreferredWidth(width);
	}

	public TableCellRenderer getHeaderRenderer(TableColumn column) {
		TableCellRenderer renderer = column.getHeaderRenderer();

		if (renderer == null) {
			JTableHeader header = this.getTableHeader();

			if (header != null) {
				renderer = header.getDefaultRenderer();
			}
		}

		return renderer;
	}

	public TableCellRenderer getCellRenderer(TableColumn column) {
		int viewColumnIndex = this.convertColumnIndexToView(column.getModelIndex());

		if (viewColumnIndex >= 0) {
			return this.getCellRenderer(0, viewColumnIndex);
		}

		TableCellRenderer renderer = column.getCellRenderer();

		if (renderer == null) {
			renderer = this.getDefaultRenderer(this.getModel().getColumnClass(column.getModelIndex()));
		}

		return renderer;
	}

	public void setHorizontalScrollEnabled(boolean enabled) {
		if (enabled == isHorizontalScrollEnabled()) {
			return;
		}

		if (enabled) {
			if (getAutoResizeMode() != AUTO_RESIZE_OFF) {
				oldAutoResizeMode = getAutoResizeMode();
			}

			setAutoResizeMode(AUTO_RESIZE_OFF);
			intelliMode = true;
		} else {
			setAutoResizeMode(oldAutoResizeMode);
		}
	}

	public boolean isHorizontalScrollEnabled() {
		return intelliMode && getAutoResizeMode() == AUTO_RESIZE_OFF;
	}

	public void setAutoResizeMode(int mode) {
		if (mode != AUTO_RESIZE_OFF) {
			oldAutoResizeMode = mode;
		}

		intelliMode = false;
		super.setAutoResizeMode(mode);
	}

	public boolean getScrollableTracksViewportWidth() {
		boolean shouldTrack = super.getScrollableTracksViewportWidth();

		if (isHorizontalScrollEnabled()) {
			return getPreferredSize().width < getParent().getWidth();
		}

		return shouldTrack;
	}

	public void columnMarginChanged(ChangeEvent e) {
		if (isEditing()) {
			removeEditor();
		}

		TableColumn resizingColumn = getResizingColumn();

		if (resizingColumn != null && autoResizeMode == AUTO_RESIZE_OFF && !inLayout) {
			resizingColumn.setPreferredWidth(resizingColumn.getWidth());
		}

		resizeAndRepaint();
	}

	public void doLayout() {
		int resizeMode = getAutoResizeMode();

		if (isHorizontalScrollEnabled()) {
			autoResizeMode = oldAutoResizeMode;
		}

		inLayout = true;
		super.doLayout();
		inLayout = false;
		autoResizeMode = resizeMode;
	}

	private TableColumn getResizingColumn() {
		return tableHeader == null ? null : tableHeader.getResizingColumn();
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

	public Border getCellInsideBorder() {
		return cellInsideBorder;
	}

	public void setCellInsideBorder(Border cellInsideBorder) {
		this.cellInsideBorder = cellInsideBorder;
		this.repaint();
	}

	public Color getDisabledForeground() {
		return disabledForeground;
	}

	public void setDisabledForeground(Color disabledForeground) {
		this.disabledForeground = disabledForeground;

		if (!this.isEnabled()) {
			this.repaint();
		}
	}

	public void setBorder(Border border) {
		this.border = border;
		super.setBorder(border);
	}

	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		this.getTableHeader().setEnabled(enabled);
		super.setBorder(enabled ? border : disabledBorder);
		super.setBackground(enabled ? background : DISABLED_BG);
		super.setGridColor(enabled ? gridColor : disabledGridColor);
	}

	public void setBackground(Color background) {
		this.background = background;
		super.setBackground(background);
	}

	public void setGridColor(Color gridColor) {
		this.gridColor = gridColor;
		super.setGridColor(gridColor);
	}

	public Color getDisabledGridColor() {
		return disabledGridColor;
	}

	public void setDisabledGridColor(Color disabledGridColor) {
		this.disabledGridColor = disabledGridColor;

		if (!this.isEnabled()) {
			this.repaint();
		}
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		if (alpha >= 0.0f && alpha <= 1.0f) {
			this.alpha = alpha;
			this.repaint();
		} else {
			throw new IllegalArgumentException("Invalid alpha:" + alpha);
		}
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
		this.repaint();
	}

	public Insets getVisibleInsets() {
		return visibleInsets;
	}

	public void setVisibleInsets(int top, int left, int bottom, int right) {
		this.visibleInsets.set(top, left, bottom, right);
		this.repaint();
	}

	public boolean isImageOnly() {
		return imageOnly;
	}

	public void setImageOnly(boolean imageOnly) {
		this.imageOnly = imageOnly;
		this.repaint();
	}

	public boolean isRendererOpaque() {
		return rendererOpaque;
	}

	public void setRendererOpaque(boolean rendererOpaque) {
		this.rendererOpaque = rendererOpaque;
		this.repaint();
	}

	public Color getRendererBackground1() {
		return rendererBackground1;
	}

	public void setRendererBackground1(Color rendererBackground1) {
		this.rendererBackground1 = rendererBackground1;
		this.repaint();
	}

	public Color getRendererBackground2() {
		return rendererBackground2;
	}

	public void setRendererBackground2(Color rendererBackground2) {
		this.rendererBackground2 = rendererBackground2;
		this.repaint();
	}

	public boolean isShowRowLines() {
		return showRowLines;
	}

	public void setShowRowLines(boolean showRowLines) {
		this.showRowLines = showRowLines;
		this.repaint();
	}

	public boolean isShowColumnLines() {
		return showColumnLines;
	}

	public void setShowColumnLines(boolean showColumnLines) {
		this.showColumnLines = showColumnLines;
		this.repaint();
	}

	public void setShowGrid(boolean showGrid) {
		setShowRowLines(showGrid);
		setShowColumnLines(showGrid);
		this.repaint();
	}

	public JPopupMenu getColumnControlMenu() {
		return columnControlMenu;
	}

	@Deprecated
	public void updateUI() {
	}

	@Deprecated
	public void setOpaque(boolean isOpaque) {
	}

	@Deprecated
	public void setShowVerticalLines(boolean showVerticalLines) {
	}

	@Deprecated
	public void setShowHorizontalLines(boolean showHorizontalLines) {
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		if (e == null || TableModelEvent.HEADER_ROW == e.getFirstRow() && TableModelEvent.HEADER_ROW == e.getLastRow()) {
			removeAllCellRendererBackground();
		}
		super.tableChanged(e);
	}

	public void setCellRendererBackgroundAt(Color color, int row, int column) {
		Vector<Color> colors = null;
		if (colorList.size() > row) {
			colors = colorList.elementAt(row);
		} else {
			int b = colorList.size();
			int d = (row + 1);
			for (int i = b; i < d; i++) {
				colors = new Vector<Color>();
				colorList.add(colors);
			}
		}
		if (null != colors) {
			if (colors.size() > column) {
				colors.setElementAt(color, column);
			} else {
				int b = colors.size();
				int d = (column + 1);
				for (int i = b; i < d; i++) {
					colors.add(null);
				}
				colors.setElementAt(color, column);
				this.repaint();
			}
		}
	}

	public Color getCellRendererBackgroundAt(int row, int column) {
		if (colorList.size() > row) {
			Vector<Color> colors = colorList.elementAt(row);
			if (colors.size() > column) {
				return colors.elementAt(column);
			}
		}
		return null;
	}

	public void removeAllCellRendererBackground() {
		if (null != colorList) {
			colorList.clear();
		}
	}

	public static Color createEditorBackground(JTable table, Component renderer) {
		Color color = renderer.getBackground();
		OnlyTable onlyTable;

		if (table instanceof OnlyTable && (!(onlyTable = (OnlyTable) table).isRendererOpaque() || onlyTable.getRendererBackground1() == null || onlyTable.getRendererBackground2() == null)) {
			color = null;
		}

		return color;
	}

	static class NumberRenderer extends OnlyTableCellRenderer.UIResource {

		private static final long serialVersionUID = 4026292660464065849L;

		public NumberRenderer() {
			super();
			setHorizontalAlignment(JLabel.RIGHT);
		}
	}

	static class DoubleRenderer extends NumberRenderer {

		private static final long serialVersionUID = -634858611608640293L;
		NumberFormat formatter;

		public DoubleRenderer() {
			super();
		}

		public void setValue(Object value) {
			if (formatter == null) {
				formatter = NumberFormat.getInstance();
			}

			setText((value == null) ? "" : formatter.format(value));
		}
	}

	static class DateRenderer extends OnlyTableCellRenderer.UIResource {

		private static final long serialVersionUID = -155254367088760418L;
		DateFormat formatter;

		public DateRenderer() {
			super();
		}

		public void setValue(Object value) {
			if (formatter == null) {
				formatter = DateFormat.getDateInstance();
			}

			setText((value == null) ? "" : formatter.format(value));
		}
	}

	static class IconRenderer extends OnlyTableCellRenderer.UIResource {

		private static final long serialVersionUID = 1317588288028835312L;

		public IconRenderer() {
			super();
			setHorizontalAlignment(JLabel.CENTER);
		}

		public void setValue(Object value) {
			setIcon((value instanceof Icon) ? (Icon) value : null);
		}
	}

	public static class GenericEditor extends DefaultCellEditor {

		private static final long serialVersionUID = 2204159572123005700L;
		private static final Border EDITOR_BORDER = UIBox.getBorder(UIBox.key_border_table_editor);
		private static final Border ERROR_BORDER = new CompoundBorder(new LineBorder(Color.RED, 2), new EmptyBorder(0, 1, 0, 1));
		private static final Color SELECTION_COLOR = UIBox.getColor(UIBox.key_color_table_text_selection);
		private Class<?>[] argTypes = new Class[] { String.class };
		private Constructor<?> constructor;
		private Object value;

		public GenericEditor() {
			super(new JTextField() {
				private static final long serialVersionUID = 4114369806668311391L;

				@Deprecated
				public void updateUI() {
				}
			});

			JTextField field = (JTextField) editorComponent;
			field.setUI(new BasicTextFieldUI());
			field.setMargin(new Insets(0, 0, 0, 0));
			field.setCaretColor(Color.BLACK);
			field.setSelectionColor(SELECTION_COLOR);
			field.setSelectedTextColor(UIBox.getColor(UIBox.key_color_table_text_selection_foreground));
			getComponent().setName("Table.editor");
		}

		public boolean stopCellEditing() {
			String str = (String) super.getCellEditorValue();

			if ("".equals(str)) {
				if (constructor.getDeclaringClass() == String.class) {
					value = str;
				}
				super.stopCellEditing();
			}

			try {
				value = constructor.newInstance(new Object[] { str });
			} catch (Exception e) {
				((JComponent) getComponent()).setBorder(ERROR_BORDER);
				return false;
			}

			return super.stopCellEditing();
		}

		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			this.value = null;
			JComponent editor = (JComponent) getComponent();
			TableCellRenderer renderer = table.getCellRenderer(row, column);
			Component c = renderer.getTableCellRendererComponent(table, value, isSelected, true, row, column);

			editor.setBorder(EDITOR_BORDER);
			editor.setFont(table.getFont());
			editor.setBackground(createEditorBackground(table, c));
			editor.setForeground(table.getForeground());
			editor.setOpaque(editor.getBackground() != null);

			if (editor instanceof JTextField && c instanceof JLabel) {
				((JTextField) editor).setHorizontalAlignment(((JLabel) c).getHorizontalAlignment());
			}

			try {
				Class<?> type = table.getColumnClass(column);

				if (type == Object.class) {
					type = String.class;
				}

				constructor = type.getConstructor(argTypes);
			} catch (Exception e) {
				return null;
			}

			return super.getTableCellEditorComponent(table, value, isSelected, row, column);
		}

		public Object getCellEditorValue() {
			return value;
		}
	}

	static class BooleanEditor extends DefaultCellEditor {

		private static final long serialVersionUID = 7383268047934155911L;

		public BooleanEditor() {
			super(new OnlyCheckBox());
			OnlyCheckBox checkBox = (OnlyCheckBox) getComponent();
			checkBox.setHorizontalAlignment(JCheckBox.CENTER);
			checkBox.setBorderPainted(true);
			checkBox.setFocusPainted(false);
		}

		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			OnlyCheckBox checkBox = (OnlyCheckBox) super.getTableCellEditorComponent(table, value, isSelected, row, column);
			TableCellRenderer renderer = table.getCellRenderer(row, column);
			Component c = renderer.getTableCellRendererComponent(table, value, isSelected, true, row, column);
			checkBox.setBackground(createEditorBackground(table, c));
			checkBox.setOpaque(checkBox.getBackground() != null);
			return checkBox;
		}
	}
}