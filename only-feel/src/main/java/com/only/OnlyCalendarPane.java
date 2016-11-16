package com.only;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JWindow;
import javax.swing.ListSelectionModel;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.Popup;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.LabelUI;
import javax.swing.plaf.basic.BasicLabelUI;
import javax.swing.plaf.basic.BasicPopupMenuUI;
import javax.swing.plaf.basic.BasicTableUI;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import com.only.box.UIBox;
import com.only.laf.OnlyListCellRenderer;
import com.only.layout.LineLayout;
import com.only.util.OnlyUIUtil;
import com.sun.awt.AWTUtilities;

public class OnlyCalendarPane extends JComponent {

	private static final long serialVersionUID = -6243749367273378444L;
	private static final Font DEFAULT_FONT = OnlyUIUtil.getDefaultFont();
	private static final Color DEFAULT_FOREGROUND = new Color(0, 0, 0);//= new Color(0, 28, 48);
	private static final Color button_color = new Color(50, 50, 50);
	private static final Color button_rollover_color = new Color(255, 255, 255);
	private static final Insets IMAGE_INSETS = new Insets(52, 3, 26, 3);
	
	//private static final Insets B_INSETS = new Insets(3, 3, 3, 3);
	private static final Border DAY_BORDER = UIBox.getBorder(UIBox.key_border_calendar_day_normal);
	private static final Border DAY_SELECTED_BORDER = UIBox.getBorder(UIBox.key_border_calendar_day_selected);
	private static final DateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private static final DateFormat YEAR_MONTH_FORMAT = new SimpleDateFormat("yyyy-MM");
	private static final String YEAR = "Year";
	private static final String MONTH = "Month";
	private JLabel lbYearAndMonth;
	private JTable dayTable;
	private OnlyButton btnNextMonth, btnNextYear, btnPreviousMonth, btnPreviousYear, btnClose;
	private OnlyButton btnBeforeYesterday, btnYesterday, btnToday, btnTomorrow, btnAfterTomorrow, btnClear;
	private JPanel topPane, bottomPane, weekPane, dayPane;
	private OnlyCalendarPane.CalendarPopup yearPopup, monthPopup;
	private Action closeAction;
	private OnlyCalendarPane.ButtonListener buttonListener;
	private OnlyCalendarPane.CalendarMouseListener mouseListener;
	private Calendar date, currentDate;
	private Rectangle rolloverRect;
	private int curRow, curColumn, firstWeekDayOfMonth;
	private boolean mouseIn, showShortcutMenu;
	private Integer[][] dayDatas;
	private Integer selectedDay;

	public OnlyCalendarPane() {
		this((Calendar) null);
	}

	public OnlyCalendarPane(String dateString) {
		this();
		setDateString(dateString);
	}

	public OnlyCalendarPane(Calendar date) {
		buttonListener = new OnlyCalendarPane.ButtonListener();
		mouseListener = new OnlyCalendarPane.CalendarMouseListener();
		curRow = curColumn = -1;
		rolloverRect = new Rectangle();
		yearPopup = new OnlyCalendarPane.CalendarPopup(YEAR, 1970, 2050);
		monthPopup = new OnlyCalendarPane.CalendarPopup(MONTH, 1, 12);
		showShortcutMenu = true;
		setDate(date, false);
		setBorder(new EmptyBorder(3, 3, 3, 3));
		setLayout(new LineLayout(0, LineLayout.LEADING, LineLayout.LEADING, LineLayout.VERTICAL));
		setOpaque(false);
		setDoubleBuffered(true);
		setPreferredSize(new Dimension(180, 179));
		initUI();
		setFont(new Font("Arial", Font.PLAIN, 12));
		addMouseListener(mouseListener);
	}

	private void initUI() {
		String[][] weekDatas = new String[][] { { "日", "一", "二", "三", "四", "五", "六" } };
		initPanes();
		initTopPane();
		initBottomPane();
		weekPane.add(createTable(new OnlyCalendarPane.CalendarTableModel(weekDatas), weekPane.getPreferredSize().height, false), BorderLayout.CENTER);
		dayPane.add(dayTable = createTable(new OnlyCalendarPane.CalendarTableModel(createDayDatas()), 18, true), BorderLayout.CENTER);
		this.add(topPane, LineLayout.START_FILL);
		this.add(weekPane, LineLayout.START_FILL);
		this.add(dayPane, LineLayout.MIDDLE_FILL);
		this.add(bottomPane, LineLayout.END_FILL);
	}

	private void initPanes() {
		topPane = new JPanel(new LineLayout(3, 0, 0, 0, 0, LineLayout.CENTER, LineLayout.CENTER, LineLayout.HORIZONTAL));
		bottomPane = new JPanel(new GridLayout(1, 6));
		weekPane = new JPanel(new BorderLayout());
		dayPane = new JPanel(new BorderLayout());
		topPane.setOpaque(false);
		topPane.setBorder(new EmptyBorder(0, 2, 3, 2));
		topPane.setPreferredSize(new Dimension(-1, 23));
		weekPane.setOpaque(false);
		weekPane.setBorder(null);
		weekPane.setPreferredSize(new Dimension(-1, 20));
		dayPane.setOpaque(false);
		dayPane.setBorder(new EmptyBorder(1, 0, 1, 0));
		bottomPane.setOpaque(false);
		bottomPane.setBorder(null);
		bottomPane.setPreferredSize(new Dimension(-1, 21));
	}

	private void initTopPane() {
		Dimension buttonSize = new Dimension(21, 21);
		lbYearAndMonth = new JLabel(YEAR_MONTH_FORMAT.format(currentDate.getTime())) {
			private static final long serialVersionUID = -3899939777706194998L;

			@Deprecated
			public void updateUI() {
			}
		};
		btnClose = new OnlyButton();
		btnNextMonth = new OnlyButton();
		btnNextYear = new OnlyButton();
		btnPreviousMonth = new OnlyButton();
		btnPreviousYear = new OnlyButton();
		//OnlyButton[] buttons = { btnPreviousYear, btnPreviousMonth, btnNextMonth, btnNextYear, btnClose };
		OnlyButton[] buttons = { btnPreviousYear, btnPreviousMonth, btnNextMonth, btnNextYear};
		String[] layoutArgs = { LineLayout.START, LineLayout.START, LineLayout.END, LineLayout.END, LineLayout.END };
		int index = 0;

		lbYearAndMonth.setUI(new OnlyCalendarPane.CalendarLabelUI());
		lbYearAndMonth.setOpaque(false);
		lbYearAndMonth.setBorder(null);
		lbYearAndMonth.setHorizontalAlignment(JLabel.CENTER);
		lbYearAndMonth.setVerticalAlignment(JLabel.CENTER);
		lbYearAndMonth.setForeground(DEFAULT_FOREGROUND);
		lbYearAndMonth.addMouseListener(mouseListener);
		btnClose.setVisible(false);
		btnClose.setNormalImage(UIBox.getImage(UIBox.key_image_calendar_close_normal));
		btnClose.setRolloverImage(UIBox.getImage(UIBox.key_image_calendar_close_rollover));
		btnClose.setPressedImage(UIBox.getImage(UIBox.key_image_calendar_close_pressed));
		btnNextMonth.setNormalImage(UIBox.getImage(UIBox.key_image_calendar_next_month_normal));
		btnNextMonth.setRolloverImage(UIBox.getImage(UIBox.key_image_calendar_next_month_rollover));
		btnNextMonth.setPressedImage(UIBox.getImage(UIBox.key_image_calendar_next_month_pressed));
		btnNextYear.setNormalImage(UIBox.getImage(UIBox.key_image_calendar_next_year_normal));
		btnNextYear.setRolloverImage(UIBox.getImage(UIBox.key_image_calendar_next_year_rollover));
		btnNextYear.setPressedImage(UIBox.getImage(UIBox.key_image_calendar_next_year_pressed));
		btnPreviousMonth.setNormalImage(UIBox.getImage(UIBox.key_image_calendar_previous_month_normal));
		btnPreviousMonth.setRolloverImage(UIBox.getImage(UIBox.key_image_calendar_previous_month_rollover));
		btnPreviousMonth.setPressedImage(UIBox.getImage(UIBox.key_image_calendar_previous_month_pressed));
		btnPreviousYear.setNormalImage(UIBox.getImage(UIBox.key_image_calendar_previous_year_normal));
		btnPreviousYear.setRolloverImage(UIBox.getImage(UIBox.key_image_calendar_previous_year_rollover));
		btnPreviousYear.setPressedImage(UIBox.getImage(UIBox.key_image_calendar_previous_year_pressed));

		for (OnlyButton button : buttons) {
			button.addActionListener(buttonListener);
			button.addMouseListener(mouseListener);
			button.setFocusable(false);
			button.setPreferredSize(buttonSize);
			button.setNormalImageInsets(2, 2, 2, 2);
			topPane.add(button, layoutArgs[index++]);
		}

		topPane.add(lbYearAndMonth, LineLayout.MIDDLE_FILL);
	}

	private void initBottomPane() {
		Image image = createBottomButtonImage(false);
		Image rolloverImage = createBottomButtonImage(true);
		btnBeforeYesterday = new OnlyButton("前天");
		btnYesterday = new OnlyButton("昨天");
		btnToday = new OnlyButton("今天");
		btnTomorrow = new OnlyButton("明天");
		btnAfterTomorrow = new OnlyButton("后天");
		btnClear = new OnlyButton("清除");
		OnlyButton[] buttons = { btnBeforeYesterday, btnYesterday, btnToday, btnTomorrow, btnAfterTomorrow, btnClear };
		String[] cmds = { "-2", "-1", "0", "1", "2", null };
		int index = 0;

		for (OnlyButton button : buttons) {
			button.setForeground(button_color);
			button.setRolloverTextColor(button_rollover_color);
			button.setFocusable(false);
			button.setNormalImage(image);
			button.setRolloverImage(rolloverImage);
			button.setPressedImage(rolloverImage);
			button.setActionCommand(cmds[index++]);
			button.addActionListener(buttonListener);
			button.addMouseListener(mouseListener);
			bottomPane.add(button);
		}
	}

	private JTable createTable(TableModel tableModel, int rowHeight, boolean isDayTable) {
		final JTable table = new JTable(tableModel) {
			private static final long serialVersionUID = -771844785042284235L;

			@Deprecated
			public void updateUI() {
			}
		};
		table.setUI(new OnlyCalendarPane.DefaultTableUI());
		table.setDefaultRenderer(Object.class, new OnlyCalendarPane.CalendarTableCellRenderer(isDayTable));
		table.getTableHeader().setVisible(false);
		table.setFont(DEFAULT_FONT);
		table.setShowGrid(false);
		table.setFocusable(false);
		table.setRowSelectionAllowed(false);
		table.setOpaque(false);
		table.setBorder(new EmptyBorder(0, 0, 0, 0));
		table.setRowHeight(rowHeight);
		table.setRowMargin(0);
		table.getColumnModel().setColumnMargin(0);
		table.addMouseListener(mouseListener);

		if (isDayTable) {
			table.addMouseMotionListener(mouseListener);
			table.addComponentListener(new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent e) {
					table.setRowHeight(table.getHeight() / table.getRowCount());
				}
			});
		}

		return table;
	}

	private Image createBottomButtonImage(boolean fill) {
		BufferedImage bufferedImage = OnlyUIUtil.getGraphicsConfiguration(this).createCompatibleImage(10, 10, Transparency.TRANSLUCENT);

		if (fill) {
			Graphics g = bufferedImage.getGraphics();
			g.setColor(UIBox.getColor(UIBox.key_color_calendar_day_background));
			g.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
			g.dispose();
		}

		return bufferedImage;
	}

	private Integer[][] createDayDatas() {
		if (dayDatas == null) {
			dayDatas = new Integer[6][7];
		}

		int oldDay = currentDate.get(Calendar.DAY_OF_MONTH);
		currentDate.set(Calendar.DAY_OF_MONTH, 1);
		firstWeekDayOfMonth = currentDate.get(Calendar.DAY_OF_WEEK);
		currentDate.set(Calendar.DAY_OF_MONTH, oldDay);
		int maxDay = currentDate.getActualMaximum(Calendar.DAY_OF_MONTH);
		int day = 0;
		int row, col;

		for (int i = 0; i < 42; i++) {
			row = i / 7;
			col = i % 7;

			if (i >= firstWeekDayOfMonth - 1 && day < maxDay) {
				dayDatas[row][col] = ++day;
			} else {
				dayDatas[row][col] = null;
			}
		}

		return dayDatas;
	}

	private void refreshDays() {
		lbYearAndMonth.setText(YEAR_MONTH_FORMAT.format(currentDate.getTime()));
		createDayDatas();
		((AbstractTableModel) dayTable.getModel()).fireTableDataChanged();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		OnlyUIUtil.paintImage(g, UIBox.getImage(UIBox.key_image_calendar_background), IMAGE_INSETS, new Rectangle(0, 0, getWidth(), getHeight()), this);
	}

	public Calendar getDate() {
		return date;
	}

	private void setDate(Calendar date, boolean refresh) {
		this.date = date;
		this.currentDate = date == null ? Calendar.getInstance() : (Calendar) date.clone();
		this.selectedDay = date == null ? null : date.get(Calendar.DAY_OF_MONTH);

		if (refresh) {
			refreshDays();
		}
	}

	public void setDate(Calendar date) {
		setDate(date, true);
	}

	public String getDateString() {
		return date == null ? null : FORMAT.format(date.getTime());
	}

	public void setDateString(String dateString) {
		try {
			if (dateString == null || dateString.isEmpty()) {
				setDate(null);
			} else {
				Date newDate = FORMAT.parse(dateString);

				if (date == null) {
					date = Calendar.getInstance();
				}

				date.setTime(newDate);
				setDate(date);
			}
		} catch (ParseException e) {
			setDate(null);
		}
	}

	public void setClosable(boolean closable) {
		btnClose.setVisible(closable);
	}

	public boolean isClosable() {
		return btnClose.isVisible();
	}

	public Action getCloseAction() {
		return closeAction;
	}

	public void setCloseAction(Action closeAction) {
		this.closeAction = closeAction;
	}

	public boolean isShowShortcutMenu() {
		return showShortcutMenu;
	}

	public void setShowShortcutMenu(boolean showShortcutMenu) {
		this.showShortcutMenu = showShortcutMenu;
	}

	public void setYearInterval(int min, int max) {
		yearPopup.resetValues(min, max);
	}

	@Override
	public void setFont(Font font) {
		super.setFont(font);

		if (lbYearAndMonth != null) {
			lbYearAndMonth.setFont(font);
		}

		if (dayTable != null) {
			dayTable.setFont(font);
		}

		if (yearPopup != null) {
			yearPopup.list.setFont(font);
		}

		if (monthPopup != null) {
			monthPopup.list.setFont(font);
		}
	}

	private boolean isCurrentMonth() {
		return date != null && date.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR) && date.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH);
	}

	private boolean isSelectedDay(Integer day) {
		return isCurrentMonth() && day != null && selectedDay != null && selectedDay.intValue() == day.intValue();
	}

	public void clear() {
		setDate(null);
		close();
	}

	public void clearMouseStatus() {
		mouseIn = false;
		curRow = -1;
		curColumn = -1;
	}

	public void close() {
		if (closeAction != null) {
			closeAction.actionPerformed(new ActionEvent(this, 0, ""));
		}
	}

	public void hidePopupMenu() {
		if (yearPopup.isVisible()) {
			yearPopup.setVisible(false);
		}

		if (monthPopup.isVisible()) {
			monthPopup.setVisible(false);
		}
	}

	private Rectangle getYearAndMonthTextRect(FontMetrics metrics) {
		Insets insets = lbYearAndMonth.getInsets(null);
		Rectangle iconRect = new Rectangle();
		Rectangle textRect = new Rectangle();
		Rectangle viewRect = new Rectangle(insets.left, insets.top, lbYearAndMonth.getWidth() - (insets.left + insets.right), lbYearAndMonth.getHeight() - (insets.top + insets.bottom));
		SwingUtilities.layoutCompoundLabel(lbYearAndMonth, metrics, lbYearAndMonth.getText(), null, lbYearAndMonth.getVerticalAlignment(), lbYearAndMonth.getHorizontalAlignment(), lbYearAndMonth.getVerticalTextPosition(), lbYearAndMonth.getHorizontalTextPosition(), viewRect,
				iconRect, textRect, lbYearAndMonth.getIconTextGap());
		return textRect;
	}

	private void showPopupMenu(MouseEvent e) {
		LabelUI ui = lbYearAndMonth.getUI();

		if (showShortcutMenu && ui instanceof OnlyCalendarPane.CalendarLabelUI && SwingUtilities.isLeftMouseButton(e)) {
			String text = lbYearAndMonth.getText();

			if (text != null && !text.isEmpty()) {
				FontMetrics metrics = lbYearAndMonth.getFontMetrics(lbYearAndMonth.getFont());
				Rectangle textRect = getYearAndMonthTextRect(metrics);
				Point p = e.getPoint();
				String year1 = text.substring(0, 4);
				String year2 = text.substring(0, 5);
				int yearWidth1 = metrics.stringWidth(year1);
				int yearWidth2 = metrics.stringWidth(year2);

				if (p.y >= textRect.y && p.y <= textRect.y + textRect.height) {
					int menuX = textRect.x;
					int menuY = textRect.y + textRect.height;

					if (p.x >= textRect.x && p.x <= textRect.x + yearWidth1) {
						yearPopup.show(lbYearAndMonth, menuX, menuY);
					} else if (p.x >= textRect.x + yearWidth2 && p.x <= textRect.x + textRect.width) {
						menuX = textRect.x + yearWidth2;
						monthPopup.show(lbYearAndMonth, menuX, menuY);
					}
				}
			}
		}
	}

	@Deprecated
	@Override
	public void updateUI() {
		if (yearPopup != null) {
			SwingUtilities.updateComponentTreeUI(yearPopup);
		}

		if (monthPopup != null) {
			SwingUtilities.updateComponentTreeUI(monthPopup);
		}
	}

	private class CalendarMouseListener extends MouseAdapter {

		@Override
		public void mouseMoved(MouseEvent e) {
			Point point = e.getPoint();
			int oldRow = curRow;
			int oldColumn = curColumn;
			curRow = dayTable.rowAtPoint(point);
			curColumn = dayTable.columnAtPoint(point);
			Rectangle rect = dayTable.getCellRect(curRow, curColumn, true);
			boolean existOld = oldRow >= 0 && oldColumn >= 0 && dayDatas[oldRow][oldColumn] != null;
			boolean repaintOld = false;
			boolean repaintCur = false;

			if (curRow < 0 || curColumn < 0) {
				repaintOld = existOld;
			} else {
				boolean isEmptyDay = dayDatas[curRow][curColumn] == null;
				JLabel label = (JLabel) dayTable.getCellRenderer(curRow, curColumn);
				Insets insets = label.getInsets();
				boolean oldMouseIn = mouseIn;
				mouseIn = getRolloverRect(rect, insets).contains(point) && !isEmptyDay;

				if (curRow != oldRow || curColumn != oldColumn) {
					if (existOld) {
						repaintOld = true;
					}

					if (mouseIn) {
						repaintCur = true;
					}
				} else if (oldMouseIn != mouseIn && !isEmptyDay) {
					repaintCur = true;
				}
			}

			if (repaintOld) {
				dayTable.repaint(dayTable.getCellRect(oldRow, oldColumn, true));
			}

			if (repaintCur && !rect.isEmpty()) {
				dayTable.repaint(rect);
			}
		}

		public void mouseExited(MouseEvent e) {
			if (e.getSource() == dayTable) {
				int oldRow = curRow;
				int oldColumn = curColumn;
				clearMouseStatus();

				if (oldRow >= 0 && oldColumn >= 0 && dayDatas[oldRow][oldColumn] != null) {
					dayTable.repaint(dayTable.getCellRect(oldRow, oldColumn, true));
				}
			}
		}

		public void mouseReleased(MouseEvent e) {
			Object source = e.getSource();

			if (source == lbYearAndMonth) {
				showPopupMenu(e);
			} else if (source == dayTable && SwingUtilities.isLeftMouseButton(e) && mouseIn) {
				Integer oldSelectedDay = selectedDay;
				selectedDay = dayDatas[curRow][curColumn];

				if (!isSelectedDay(oldSelectedDay)) {
					date = (Calendar) currentDate.clone();
					date.set(Calendar.DAY_OF_MONTH, selectedDay);
					dayTable.repaint(dayTable.getCellRect(curRow, curColumn, true));
				}

				if (oldSelectedDay != null && isCurrentMonth()) {
					int count = firstWeekDayOfMonth + oldSelectedDay - 2;
					dayTable.repaint(dayTable.getCellRect(count / 7, count % 7, true));
				}

				close();
			}
		}

		public void mousePressed(MouseEvent e) {
			if (showShortcutMenu) {
				hidePopupMenu();
			}
		}

		private Rectangle getRolloverRect(Rectangle rect, Insets insets) {
			rolloverRect.setBounds(rect);
			rolloverRect.x += insets.left;
			rolloverRect.y += insets.top;
			rolloverRect.width -= insets.left + insets.right;
			rolloverRect.height -= insets.top + insets.bottom;
			return rolloverRect;
		}
	}

	private class ButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();

			if (source == btnClose) {
				close();
			} else if (source == btnNextMonth) {
				currentDate.add(Calendar.MONTH, 1);
				refreshDays();
			} else if (source == btnNextYear) {
				currentDate.add(Calendar.YEAR, 1);
				refreshDays();
			} else if (source == btnPreviousMonth) {
				currentDate.add(Calendar.MONTH, -1);
				refreshDays();
			} else if (source == btnPreviousYear) {
				currentDate.add(Calendar.YEAR, -1);
				refreshDays();
			} else if (source == btnClear) {
				clear();
			} else {
				int amount = Integer.parseInt(e.getActionCommand());
				Calendar date = Calendar.getInstance();

				if (amount != 0) {
					date.add(Calendar.DAY_OF_YEAR, Integer.parseInt(e.getActionCommand()));
				}

				setDate(date);
				close();
			}
		}
	}

	private class CalendarTableModel extends AbstractTableModel {

		private static final long serialVersionUID = -3633867494696004985L;
		private Object[][] rowData;
		private String[] columnNames;

		public CalendarTableModel(Object[][] rowData) {
			this.rowData = rowData;
			columnNames = new String[rowData[0].length];
			Arrays.fill(columnNames, "");
		}

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

		public void setValueAt(Object value, int row, int col) {
			rowData[row][col] = value == null ? null : value.toString();
			fireTableCellUpdated(row, col);
		}

		public boolean isCellEditable(int row, int column) {
			return false;
		}
	}

	private class CalendarTableCellRenderer extends DefaultTableCellRenderer {

		private static final long serialVersionUID = 1874605203390504635L;
		private boolean isDayTable;

		public CalendarTableCellRenderer(boolean isDayTable) {
			this.isDayTable = isDayTable;
			setUI(isDayTable ? new OnlyCalendarPane.CalendarLabelUI() : new OnlyCalendarPane.DefaultLabelUI());
			setOpaque(false);
			setBorder(isDayTable ? DAY_BORDER : null);
			setHorizontalAlignment(JLabel.CENTER);
			setVerticalAlignment(JLabel.CENTER);
			setBackground(UIBox.getColor(UIBox.key_color_calendar_day_background));
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			setText(value == null ? null : value.toString());
			setFont(table.getFont());
			Color color = (column == 0 || column == 6) ? UIBox.getColor(UIBox.key_color_calendar_weekend_foreground) : UIBox.getColor(UIBox.key_color_calendar_week_foreground);
			setForeground(color);

			if (isDayTable) {
				if (isSelectedDay((Integer) value)) {
					setOpaque(true);
					setBorder(DAY_SELECTED_BORDER);
				} else {
					setOpaque(value != null && mouseIn && curRow == row && curColumn == column);
					setBorder(DAY_BORDER);
				}
			}

			return this;
		}

		@Deprecated
		public void updateUI() {
		}
	}

	@SuppressWarnings("rawtypes")
	private class CalendarPopup extends JPopupMenu {

		private static final long serialVersionUID = -9198339742476660346L;
		private Field popupField;
		//private Method getPopupMethod;
		private MenuElement[] elements;
		public OnlyList list;
		public DefaultListModel listModel;
		private JWindow heavyWeightWindow;
		private Boolean heavyWeightWindowOpaque;
		private Boolean heavyWeightWindowContentPaneOpaque;
		private boolean buffered;

		@SuppressWarnings("unchecked")
		public CalendarPopup(String name, int minValue, int maxValue) {
			super();
			setUI(new BasicPopupMenuUI() {
				public void installDefaults() {
				}

				protected void uninstallDefaults() {
				}
			});
			OnlyScrollList scList = new OnlyScrollList(listModel = new DefaultListModel());
			OnlyCalendarPane.CalendarPopup.ListListener listener = new OnlyCalendarPane.CalendarPopup.ListListener();
			list = scList.getList();
			list.setRendererOpaque(false);
			list.setFocusable(false);
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			list.setCellRenderer(new OnlyCalendarPane.CalendarPopup.ListRenderer());
			list.addMouseListener(listener);
			list.addMouseMotionListener(listener);
			scList.setHorizontalScrollBar(null);
			setName(name);
			setBorder(new EmptyBorder(0, 0, 0, 0));
			setOpaque(false);
			setLayout(new BorderLayout());
			add(scList, BorderLayout.CENTER);
			resetValues(minValue, maxValue);

			try {
				popupField = JPopupMenu.class.getDeclaredField("popup");
				//getPopupMethod = JPopupMenu.class.getDeclaredMethod("getPopup");
				popupField.setAccessible(true);
				//getPopupMethod.setAccessible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@SuppressWarnings("unchecked")
		public void resetValues(int minValue, int maxValue) {
			listModel.removeAllElements();

			for (int value = minValue; value <= maxValue; value++) {
				listModel.addElement(value);
			}
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

			if (!visible) {
				Boolean doCanceled = (Boolean) getClientProperty("JPopupMenu.firePopupMenuCanceled");

				if (doCanceled != null && doCanceled == Boolean.TRUE) {
					putClientProperty("JPopupMenu.firePopupMenuCanceled", Boolean.FALSE);
					firePopupMenuCanceled();
				}

				getSelectionModel().clearSelection();
			} else if (isPopupMenu()) {
				MenuElement[] me;

				if (getParentElements().length > 0) {
					me = new MenuElement[elements.length + 1];
					me[elements.length] = (MenuElement) this;
					System.arraycopy(elements, 0, me, 0, elements.length);
				} else {
					me = new MenuElement[] { (MenuElement) this };
				}

				MenuSelectionManager.defaultManager().setSelectedPath(me);
			}

			Popup popup = getThePopup();

			if (visible) {
				firePopupMenuWillBecomeVisible();
				//setThePopup(invokeGetPopup());
				firePropertyChange("visible", Boolean.FALSE, Boolean.TRUE);
			} else if (popup != null) {
				firePopupMenuWillBecomeInvisible();
				popup.hide();
				setThePopup(null);
				firePropertyChange("visible", Boolean.TRUE, Boolean.FALSE);
			}

			if (OnlyUIUtil.isTranslucencySupported()) {
				if (visible && !isOpaque()) {
					heavyWeightWindow = OnlyUIUtil.getHeavyWeightWindow(this);
				}

				if (heavyWeightWindow != null) {
					JComponent contentPane = (JComponent) heavyWeightWindow.getContentPane();

					if (visible) {
						buffered = true;
						heavyWeightWindowOpaque = AWTUtilities.isWindowOpaque(heavyWeightWindow);
						heavyWeightWindowContentPaneOpaque = contentPane.isOpaque();
						contentPane.setOpaque(false);
						AWTUtilities.setWindowOpaque(heavyWeightWindow, false);
					} else {
						contentPane.setOpaque(heavyWeightWindowContentPaneOpaque);
						AWTUtilities.setWindowOpaque(heavyWeightWindow, heavyWeightWindowOpaque);
						heavyWeightWindowOpaque = null;
						heavyWeightWindowContentPaneOpaque = null;
						heavyWeightWindow = null;
					}
				}
			}
		}

		private boolean isPopupMenu() {
			Component invoker = getInvoker();
			return ((invoker != null) && !(invoker instanceof JMenu));
		}

		public void hide() {
			MenuSelectionManager manager = MenuSelectionManager.defaultManager();
			MenuElement[] selection = manager.getSelectedPath();
			List<MenuElement> meList = new ArrayList<MenuElement>();

			for (MenuElement e : selection) {
				if (e != this) {
					meList.add(e);
				}
			}

			selection = new MenuElement[meList.size()];
			meList.toArray(selection);
			manager.setSelectedPath(selection);
		}

		public void show(Component invoker, int x, int y) {
			String name = this.getName();
			list.clearSelection();

			if (YEAR.equals(name)) {
				list.setSelectedValue(currentDate.get(Calendar.YEAR), true);
			} else if (MONTH.equals(name)) {
				list.setSelectedValue(currentDate.get(Calendar.MONTH) + 1, true);
			}

			super.show(invoker, x, y);
		}

		public Dimension getPreferredSize() {
			Dimension size = super.getPreferredSize();
			int maxHeight = weekPane.getHeight() + dayPane.getHeight();

			if (size.height > maxHeight) {
				size.height = maxHeight;
			}

			return size;
		}

		private void dateChanged() {
			String name = this.getName();
			Integer selectedValue = (Integer) list.getSelectedValue();

			if (selectedValue != null && YEAR.equals(name)) {
				currentDate.set(Calendar.YEAR, selectedValue);
				refreshDays();
			} else if (selectedValue != null && MONTH.equals(name)) {
				currentDate.set(Calendar.MONTH, selectedValue - 1);
				refreshDays();
			}

			setVisible(false);
		}

		private void initParentElements() {
			Container parent = lbYearAndMonth.getParent();
			List<MenuElement> meList = new ArrayList<MenuElement>();

			while (parent != null) {
				if (parent instanceof MenuElement) {
					meList.add((MenuElement) parent);
				}

				parent = parent.getParent();
			}

			meList.toArray(elements = new MenuElement[meList.size()]);
		}

		public MenuElement[] getParentElements() {
			if (elements == null) {
				initParentElements();
			}

			return elements;
		}

		private void setThePopup(Popup popup) {
			try {
				popupField.set(this, popup);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private Popup getThePopup() {
			try {
				return (Popup) popupField.get(this);
			} catch (Exception e) {
				return null;
			}
		}

//		private Popup invokeGetPopup() {
//			try {
//				return (Popup) getPopupMethod.invoke(this);
//			} catch (Exception e) {
//				return null;
//			}
//		}

		@Deprecated
		public void updateUI() {
			setUI(this.getUI());
		}

		private class ListRenderer extends OnlyListCellRenderer {

			private static final long serialVersionUID = -408969737197174965L;
			private final String ZERO = "0";

			public ListRenderer() {
				super();
				setBorder(new EmptyBorder(0, 1, 0, 3));
			}

			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

				if (value != null && value instanceof Integer) {
					int intValue = ((Integer) value).intValue();

					if (intValue < 10) {
						setText(ZERO + value);
					}
				}

				return this;
			}
		}

		private class ListListener extends MouseAdapter {

			public void mouseMoved(MouseEvent e) {
				list.setSelectedIndex(list.locationToIndex(e.getPoint()));
			}

			public void mouseReleased(MouseEvent e) {
				dateChanged();
			}
		}
	}

	private static class DefaultTableUI extends BasicTableUI {

		public static ComponentUI createUI(JComponent c) {
			return new OnlyCalendarPane.DefaultTableUI();
		}

		protected void installDefaults() {
		}
	}

	private static class DefaultLabelUI extends BasicLabelUI {

		public static ComponentUI createUI(JComponent c) {
			return new OnlyCalendarPane.DefaultLabelUI();
		}

		protected void installDefaults(JLabel c) {
		}
	}

	private static class CalendarLabelUI extends BasicLabelUI {

		public static ComponentUI createUI(JComponent c) {
			return new OnlyCalendarPane.CalendarLabelUI();
		}

		public void update(Graphics g, JComponent c) {
			if (c.isOpaque()) {
				Insets insets = c.getInsets();
				g.setColor(c.getBackground());
				g.fillRect(insets.left, insets.top, c.getWidth() - insets.left - insets.right, c.getHeight() - insets.top - insets.bottom);
			}

			paint(g, c);
		}

		protected void installDefaults(JLabel c) {
		}
	}
}