package com.only;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Method;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.SortOrder;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.TableHeaderUI;
import javax.swing.plaf.basic.BasicLabelUI;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import sun.swing.SwingUtilities2;
import sun.swing.table.DefaultTableCellHeaderRenderer;

import com.only.box.UIBox;
import com.only.component.ImageBorder;
import com.only.laf.OnlyTableHeaderUI;
import com.only.util.OnlyUIUtil;

public class OnlyTableHeader extends JTableHeader {

    private static final long serialVersionUID = -6295367316832337062L;
    protected boolean pressable;
    protected boolean showPopupMenu;
    protected boolean packEnabled;
    protected int height;

    public OnlyTableHeader() {
        this(null);
    }

    public OnlyTableHeader(TableColumnModel cm) {
        super(cm);
        setUI(new OnlyTableHeaderUI());
        setFont(OnlyUIUtil.getDefaultFont());
        setBackground(UIBox.getWhiteColor());
        setForeground(new Color(0, 28, 48));
        setOpaque(false);
        addMouseListener(new HeaderListener());
        pressable = true;
        showPopupMenu = true;
        packEnabled = true;
        height = 21;
    }

    @Override
    protected TableCellRenderer createDefaultRenderer() {
        return new OnlyTableCellHeaderRenderer();
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        size.height = height;
        return size;
    }

    public boolean isPressable() {
        return pressable;
    }

    public void setPressable(boolean pressable) {
        this.pressable = pressable;
        this.repaint();
    }

    @Override
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
        this.revalidate();
    }

    public boolean isShowPopupMenu() {
        return showPopupMenu;
    }

    public void setShowPopupMenu(boolean showPopupMenu) {
        this.showPopupMenu = showPopupMenu;
    }

    public boolean isPackEnabled() {
        return packEnabled;
    }

    public void setPackEnabled(boolean packEnabled) {
        this.packEnabled = packEnabled;
    }

    protected int getRolloverColumn() {
        TableHeaderUI ui = this.getUI();

        if (ui != null) {
            if (ui instanceof OnlyTableHeaderUI) {
                return ((OnlyTableHeaderUI) ui).getRolloverColumn();
            } else {
                try {
                    Method getRolloverColumnMethod = BasicTableHeaderUI.class.getDeclaredMethod("getRolloverColumn");
                    getRolloverColumnMethod.setAccessible(true);
                    return (Integer) getRolloverColumnMethod.invoke(ui);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return -1;
    }

    @Deprecated
    @Override
    public void updateUI() {
    }

    public class OnlyTableCellHeaderRenderer extends DefaultTableCellHeaderRenderer {


        
        private static final long serialVersionUID = 6719110852077652189L;
        private final Image PRESS_IMAGE = UIBox.getImage(UIBox.key_image_table_header_pressed);
        private final Image ROLLOVER_IMAGE = UIBox.getImage(UIBox.key_image_table_header_rollover);
        private final Image BORDER_IMAGE = UIBox.getImage(UIBox.key_image_table_header_split);
        private final Icon UP_ICON = UIBox.getIcon(UIBox.key_icon_table_header_up);
        private final Icon DOWN_ICON = UIBox.getIcon(UIBox.key_icon_table_header_down);
        private final Border EMPTY_BORDER = new EmptyBorder(0, 0, 1, 0);
        private final Border SPLIT_BORDER = new CompoundBorder(EMPTY_BORDER, new ImageBorder(BORDER_IMAGE, 0, 0, 0, 2));
        private final int STATE_DEFAULT = 0;
        private final int STATE_PRESS = 1;
        private final int STATE_ROLLOVER = 2;
        private int state = STATE_DEFAULT;
        private Icon sortIcon;

        public OnlyTableCellHeaderRenderer() {
            setUI(new RendererUI());
            setHorizontalAlignment(JLabel.LEFT);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel renderer = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            TableColumn draggedColumn = OnlyTableHeader.this.getDraggedColumn();
            SortOrder sortOrder = getColumnSortOrder(table, column);
            sortIcon = null;

            if (draggedColumn != null && column == SwingUtilities2.convertColumnIndexToView(OnlyTableHeader.this.getColumnModel(), draggedColumn.getModelIndex())) {
                state = STATE_PRESS;
            } else if (isSelected || hasFocus || (column == getRolloverColumn())) {
                state = STATE_ROLLOVER;
            } else {
                state = STATE_DEFAULT;
            }

            if (sortOrder != null) {
                switch (sortOrder) {
                    case ASCENDING: {
                        sortIcon = UP_ICON;
                        break;
                    }
                    case DESCENDING: {
                        sortIcon = DOWN_ICON;
                        break;
                    }
                    default: {
                        break;
                    }
                }
            }

            renderer.setBackground(OnlyTableHeader.this.getBackground());
            renderer.setForeground(OnlyTableHeader.this.getForeground());
            renderer.setFont(OnlyTableHeader.this.getFont());
            renderer.setBorder(column == table.getColumnCount() - 1 ? EMPTY_BORDER : SPLIT_BORDER);
            renderer.setIcon(null);
            renderer.setOpaque(OnlyTableHeader.this.isOpaque());
            return renderer;
        }

        private Image getImage() {
            Image image = null;

            if (OnlyTableHeader.this.isPressable()) {
                switch (state) {
                    case STATE_ROLLOVER: {
                        image = ROLLOVER_IMAGE;
                        break;
                    }
                    case STATE_PRESS: {
                        image = PRESS_IMAGE;
                        break;
                    }
                }

                image = (image == null && sortIcon != null) ? ROLLOVER_IMAGE : image;

                if (image != null && !OnlyTableHeader.this.isEnabled()) {
                    image = OnlyUIUtil.toBufferedImage(image, 0.5f, this);
                }
            }

            return image;
        }

        private Icon getSortIcon() {
            return sortIcon;
        }

        private boolean isPress() {
            if (!OnlyTableHeader.this.isPressable()) {
                return false;
            } else {
                return state == STATE_PRESS;
            }
        }

        @Deprecated
        public void updateUI() {
        }
    }

    private class RendererUI extends BasicLabelUI {

        private final Insets IMAGE_INSETS = new Insets(1, 1, 1, 1);
        private int pressMoveX, pressMoveY;

        @Override
        protected void paintDisabledText(JLabel label, Graphics g, String text, int textX, int textY) {
            paintEnabledText(label, g, text, textX, textY);
        }

        @Override
        protected void paintEnabledText(JLabel label, Graphics g, String text, int textX, int textY) {
            int mnemIndex = label.getDisplayedMnemonicIndex();
            g.setColor(label.getForeground());
            SwingUtilities2.drawStringUnderlineCharAt(label, g, text, mnemIndex, textX + 5 + pressMoveX, textY + pressMoveY);
        }

        private void paintIcon(Graphics g, JComponent c) {
            if (c instanceof OnlyTableCellHeaderRenderer) {
                OnlyTableCellHeaderRenderer renderer = (OnlyTableCellHeaderRenderer) c;
                Icon icon = renderer.getSortIcon();

                if (icon != null) {
                    Graphics2D g2d = (Graphics2D) g;
                    Composite oldComposite = g2d.getComposite();
                    Insets insets = c.getInsets();
                    int x = c.getWidth() - insets.right - 5 - icon.getIconWidth();
                    int y = (c.getHeight() - insets.top - insets.bottom - icon.getIconHeight()) / 2 + insets.top + pressMoveY;

                    if (!OnlyTableHeader.this.isEnabled()) {
                        g2d.setComposite(AlphaComposite.SrcOver.derive(0.5f));
                    }

                    icon.paintIcon(c, g, x, y);
                    g2d.setComposite(oldComposite);
                }
            }
        }

        @Override
        public void update(Graphics g, JComponent c) {
            if (c.isOpaque()) {
                g.setColor(c.getBackground());
                g.fillRect(0, 0, c.getWidth(), c.getHeight());
            }

            if (c instanceof OnlyTableCellHeaderRenderer) {
                OnlyTableCellHeaderRenderer renderer = (OnlyTableCellHeaderRenderer) c;
                pressMoveX = pressMoveY = (renderer.isPress() ? 1 : 0);
                Image image = renderer.getImage();
                Insets insets = c.getInsets();
                int width = c.getWidth();
                int height = c.getHeight();

                if (image != null) {
                    OnlyUIUtil.paintImage(g, image, IMAGE_INSETS, new Rectangle(0, 0, width - insets.right, height - insets.bottom), c);
                }
            } else {
                pressMoveX = pressMoveY = 0;
            }

            paint(g, c);
            paintIcon(g, c);
        }

        @Override
        protected void installDefaults(JLabel c) {
        }
    }

    private class HeaderListener extends MouseAdapter {

        private TableColumn cachedResizingColumn;

        @Override
        public void mouseClicked(MouseEvent e) {
            if (!shouldIgnore(e) && e.getClickCount() == 2 && isColumnVisible(cachedResizingColumn)) {
                JTable table = getTable();

                if (table instanceof OnlyTable) {
                    ((OnlyTable) table).packColumn(cachedResizingColumn, -1);
                }
            }

            uncacheResizingColumn();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (!shouldIgnore(e)) {
                cacheResizingColumn(e);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            uncacheResizingColumn();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (showPopupMenu && SwingUtilities.isRightMouseButton(e) && table.isEnabled()) {
                JTable table = getTable();

                if (table instanceof OnlyTable) {
                    JPopupMenu menu = ((OnlyTable) table).getColumnControlMenu();

                    if (menu != null && menu.getComponentCount() > 0) {
                        menu.show(OnlyTableHeader.this, e.getX(), e.getY());
                    }
                }
            }
        }

        private boolean shouldIgnore(MouseEvent e) {
            return !SwingUtilities.isLeftMouseButton(e) || !table.isEnabled() || !packEnabled;
        }

        private void cacheResizingColumn(MouseEvent e) {
            TableColumn column = getResizingColumn();

            if (column != null) {
                cachedResizingColumn = column;
            }
        }

        private void uncacheResizingColumn() {
            cachedResizingColumn = null;
        }

        private boolean isColumnVisible(TableColumn column) {
            boolean visible = false;

            if (column != null) {
                TableColumnModel model = getColumnModel();

                for (int colIndex = 0; colIndex < model.getColumnCount(); colIndex++) {
                    if (model.getColumn(colIndex) == column) {
                        visible = true;
                        break;
                    }
                }
            }
            return visible;
        }
    }
}