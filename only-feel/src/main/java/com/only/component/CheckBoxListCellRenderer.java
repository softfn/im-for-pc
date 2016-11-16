package com.only.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicHTML;

import sun.swing.DefaultLookup;

@SuppressWarnings("rawtypes")
public class CheckBoxListCellRenderer extends JCheckBox implements ListCellRenderer, Serializable {

    private static final long serialVersionUID = 4141370835229193265L;
    private static final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
    private static final Border DEFAULT_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
    protected static Border noFocusBorder = DEFAULT_NO_FOCUS_BORDER;
    private List<?> selectedList;

    public CheckBoxListCellRenderer(List<?> selectedList) {
        super();
        setOpaque(true);
        setBorder(getNoFocusBorder());
        setName("List.cellRenderer");
        this.selectedList = selectedList;
    }

    private Border getNoFocusBorder() {
        Border border = DefaultLookup.getBorder(this, ui, "List.cellNoFocusBorder");

        if (System.getSecurityManager() != null) {
            if (border != null) {
                return border;
            }

            return SAFE_NO_FOCUS_BORDER;
        } else {
            if (border != null && (noFocusBorder == null || noFocusBorder == DEFAULT_NO_FOCUS_BORDER)) {
                return border;
            }

            return noFocusBorder;
        }
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        setComponentOrientation(list.getComponentOrientation());
        Color bg = null;
        Color fg = null;
        JList.DropLocation dropLocation = list.getDropLocation();

        if (dropLocation != null && !dropLocation.isInsert() && dropLocation.getIndex() == index) {
            bg = DefaultLookup.getColor(this, ui, "List.dropCellBackground");
            fg = DefaultLookup.getColor(this, ui, "List.dropCellForeground");
            isSelected = true;
        }

        if (isSelected) {
            setBackground(bg == null ? list.getSelectionBackground() : bg);
            setForeground(fg == null ? list.getSelectionForeground() : fg);
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        setText((value == null) ? "" : value.toString());
        setEnabled(list.isEnabled());
        setFont(list.getFont());
        setSelected(index >= selectedList.size() ? false : selectedList.get(index) == value);
        Border border = null;

        if (cellHasFocus) {
            if (isSelected) {
                border = DefaultLookup.getBorder(this, ui, "List.focusSelectedCellHighlightBorder");
            }

            if (border == null) {
                border = DefaultLookup.getBorder(this, ui, "List.focusCellHighlightBorder");
            }
        } else {
            border = getNoFocusBorder();
        }

        setBorder(border);
        return this;
    }

    public boolean isOpaque() {
        Color back = getBackground();
        Component p = getParent();

        if (p != null) {
            p = p.getParent();
        }

        boolean colorMatch = (back != null) && (p != null) && back.equals(p.getBackground()) && p.isOpaque();
        return !colorMatch && super.isOpaque();
    }

    public void validate() {
    }

    public void invalidate() {
    }

    public void repaint() {
    }

    public void revalidate() {
    }

    public void repaint(long tm, int x, int y, int width, int height) {
    }

    public void repaint(Rectangle r) {
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        if (propertyName.equals("text")
                || ((propertyName.equals("font") || propertyName.equals("foreground"))
                && oldValue != newValue && getClientProperty(BasicHTML.propertyKey) != null)) {

            super.firePropertyChange(propertyName, oldValue, newValue);
        }
    }

    public void firePropertyChange(String propertyName, byte oldValue, byte newValue) {
    }

    public void firePropertyChange(String propertyName, char oldValue, char newValue) {
    }

    public void firePropertyChange(String propertyName, short oldValue, short newValue) {
    }

    public void firePropertyChange(String propertyName, int oldValue, int newValue) {
    }

    public void firePropertyChange(String propertyName, long oldValue, long newValue) {
    }

    public void firePropertyChange(String propertyName, float oldValue, float newValue) {
    }

    public void firePropertyChange(String propertyName, double oldValue, double newValue) {
    }

    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
    }

    public static class UIResource extends CheckBoxListCellRenderer implements javax.swing.plaf.UIResource {

        private static final long serialVersionUID = -4437418539365040975L;

        public UIResource(List<Object> selectedList) {
            super(selectedList);
        }
    }
}