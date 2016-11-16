package com.over;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListDataEvent;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.ComboPopup;

import com.only.component.TristateCheckBoxState;

@SuppressWarnings("rawtypes")
public class OverCheckedComboBox extends JComboBox {

    private static final long serialVersionUID = 938062179790029115L;
    private OverCheckBoxList checkList;
    private OverTristateCheckBox selectAllCheckBox;
    private String text4SelectedAll;
    private DefaultListModel listModel;
    private List<Integer> partiallySelectedIndexes;
    private boolean fireQuickSelect;
    private boolean clearPartiallySelected;

    public OverCheckedComboBox() {
        super();
        init(null);
    }

    @SuppressWarnings("unchecked")
	public OverCheckedComboBox(ComboBoxModel model) {
        super(model);
        init(null);
    }
    @SuppressWarnings("unchecked")
    public OverCheckedComboBox(Object[] listData) {
        super(listData);
        init(listData);
    }
    @SuppressWarnings("unchecked")
    public OverCheckedComboBox(Vector<?> listData) {
        super(listData);
        init(listData);
    }
    @SuppressWarnings("unchecked")
    private void init(Object listData) {
        this.fireQuickSelect = true;
        this.clearPartiallySelected = true;
        this.partiallySelectedIndexes = new ArrayList<Integer>();
        this.checkList = new OverCheckBoxList(listModel = new DefaultListModel());
        this.selectAllCheckBox = new OverTristateCheckBox("\u5168\u9009");

        if (listData != null) {
            if (listData instanceof Object[]) {
                for (Object data : (Object[]) listData) {
                    listModel.addElement(data);
                }
            } else if (listData instanceof Vector<?>) {
                for (Object data : (Vector<?>) listData) {
                    listModel.addElement(data);
                }
            }
        }

        this.updateUI();
        checkList.setForeground(this.getForeground());
        selectAllCheckBox.setForeground(this.getForeground());
        this.displaySelect();
    }

    public OverCheckBoxList getCheckedList() {
        return checkList;
    }

    public OverTristateCheckBox getSelectAllCheckBox() {
        return selectAllCheckBox;
    }

    public List<Integer> getSelectedIndexes() {
        return checkList.getItemSelectedIndexes();
    }

    public void setSelectedIndexes(List<Integer> selectedIndexes) {
        checkList.setItemSelectedIndexes(selectedIndexes);
        displaySelect();
    }

    public List<?> getSelectedItems() {
        return checkList.getSelectedItems();
    }

    public void setSelectedItems(List<?> items) {
        checkList.setSelectedItems(items);
        displaySelect();
    }

    public void selecteAll() {
        checkList.selectedAllItems();
        displaySelect();
    }

    public void selecteEmpty() {
        checkList.unselectedAllItems();
        displaySelect();
    }

    public String getText4SelectedAll() {
        return text4SelectedAll;
    }

    public void setText4SelectedAll(String text4SelectedAll) {
        this.text4SelectedAll = text4SelectedAll;
        this.repaint();
    }

    public void setSelectedAllActionLabel(String label) {
        if (label != null && !label.isEmpty()) {
            selectAllCheckBox.setText(label);
        }
    }

    public String getSelectedAllActionLabel() {
        return selectAllCheckBox.getText();
    }

    public void setForeground(Color fg) {
        super.setForeground(fg);

        if (checkList != null) {
            checkList.setForeground(fg);
        }

        if (selectAllCheckBox != null) {
            selectAllCheckBox.setForeground(fg);
        }
    }

    private void displaySelect() {
        OverCheckedComboBox.PopupList<Object> list = new OverCheckedComboBox.PopupList<Object>();

        for (Object o : checkList.getSelectedItems()) {
            list.add(o);
        }

        getModel().setSelectedItem(list);
        fireQuickSelect = false;

        if (clearPartiallySelected) {
            partiallySelectedIndexes.clear();
        }

        if (checkList.isSelectedAll()) {
            selectAllCheckBox.setState(TristateCheckBoxState.SELECTED);
        } else if (checkList.isSelectedEmpty()) {
            selectAllCheckBox.setState(TristateCheckBoxState.DESELECTED);
        } else {
            selectAllCheckBox.setState(TristateCheckBoxState.NOTSPECIFIED);
        }

        fireQuickSelect = true;
    }

    public void intervalRemoved(ListDataEvent e) {
        super.intervalRemoved(e);

        for (int index = e.getIndex1(); index >= e.getIndex0(); index--) {
            listModel.remove(index);
        }

        displaySelect();
    }
    @SuppressWarnings("unchecked")
    public void intervalAdded(ListDataEvent e) {
        super.intervalAdded(e);

        for (int index = e.getIndex0(); index <= e.getIndex1(); index++) {
            listModel.add(index, getItemAt(index));
        }

        displaySelect();
    }

    @Deprecated
    public void setSelectedIndex(int index) {
    }

    @Deprecated
    public void setSelectedItem(Object item) {
    }

    @Deprecated
    public void setEditable(boolean flag) {
        super.setEditable(false);
    }

    public void updateUI() {
        super.updateUI();

        try {
            //变态的代码，解决变态的问题
            BasicComboBoxUI ui = (BasicComboBoxUI) getUI();
            Field popupField = BasicComboBoxUI.class.getDeclaredField("popup");
            Field listBoxField = BasicComboBoxUI.class.getDeclaredField("listBox");
            Method installListenersMethod = BasicComboBoxUI.class.getDeclaredMethod("installListeners");
            Method uninstallListenersMethod = BasicComboBoxUI.class.getDeclaredMethod("uninstallListeners");
            OverCheckedComboBox.CheckedListPopup popup = new OverCheckedComboBox.CheckedListPopup(this);
            popupField.setAccessible(true);
            listBoxField.setAccessible(true);
            installListenersMethod.setAccessible(true);
            uninstallListenersMethod.setAccessible(true);
            ui.unconfigureArrowButton();
            uninstallListenersMethod.invoke(ui);
            popupField.set(ui, popup);
            listBoxField.set(ui, popup.getList());
            installListenersMethod.invoke(ui);
            ui.configureArrowButton();
            SwingUtilities.updateComponentTreeUI(popup);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class PopupList<E> extends ArrayList<E> {

        private static final long serialVersionUID = 6562964910585649012L;

        public String toString() {
            if (text4SelectedAll != null && checkList.isSelectedAll()) {
                return text4SelectedAll;
            }

            StringBuilder sb = new StringBuilder();
            int size = this.size();

            for (int index = 0; index < size; index++) {
                sb.append(this.get(index));

                if (index != size - 1) {
                    sb.append(", ");
                }
            }

            return sb.toString();
        }
    }

    private class CheckedListPopup extends JPopupMenu implements ComboPopup {

        private static final long serialVersionUID = 5017468454834870949L;
        private JScrollPane scrollPane;
        protected OverCheckedComboBox checkedComboBox;
        protected MouseListener mouseListener;
        private MouseListener selectListener = new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    checkedComboBox.setSelectedIndexes(checkedComboBox.getSelectedIndexes());
                }
            }
        };
        private KeyListener keySelectListener = new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_SPACE || e.getKeyChar() == KeyEvent.VK_ENTER) {
                    checkedComboBox.setSelectedIndexes(checkedComboBox.getSelectedIndexes());
                }
            }
        };
        private ItemListener selectAllCheckListener = new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                quickSelect();
            }
        };

        public CheckedListPopup(JComboBox checkedComboBox) {
            this.checkedComboBox = (OverCheckedComboBox) checkedComboBox;
            setBorder(BorderFactory.createLineBorder(Color.BLACK));
            setLayout(new BorderLayout());
            setFocusable(false);
            setLightWeightPopupEnabled(checkedComboBox.isLightWeightPopupEnabled());
            OverCheckBoxList checkedList = this.checkedComboBox.getCheckedList();

            if (checkedList != null) {
                JComponent listParent = new JComponent() {
                    private static final long serialVersionUID = -1136152163163115638L;
                };
                OverTristateCheckBox selectAllCheck = this.checkedComboBox.getSelectAllCheckBox();

                listParent.setLayout(new BorderLayout());
                listParent.add(checkedList, BorderLayout.CENTER);

                if (selectAllCheck != null) {
                    JComponent cellRenderer = (JComponent) checkedList.getCellRenderer();
                    int cellHeight = checkedList.getFixedCellHeight();
                    selectAllCheck.setBorder(cellRenderer.getBorder());
                    selectAllCheck.setRolloverEnabled(false);
                    selectAllCheck.setContentAreaFilled(false);
                    selectAllCheck.setFocusable(false);
                    selectAllCheck.setBorderPainted(false);
                    selectAllCheck.setOpaque(true);
                    selectAllCheck.setMargin(new Insets(0, 0, 0, 0));

                    if (cellHeight < 0) {
                        selectAllCheck.setPreferredSize(cellRenderer.getPreferredSize());
                    } else {
                        selectAllCheck.setPreferredSize(new Dimension(-1, cellHeight));
                    }

                    listParent.add(selectAllCheck, BorderLayout.NORTH);
                    selectAllCheck.addItemListener(selectAllCheckListener);
                }

                scrollPane = new JScrollPane(listParent);
                scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
                scrollPane.setFocusable(false);
                scrollPane.getVerticalScrollBar().setFocusable(false);
                add(scrollPane, BorderLayout.CENTER);
                checkedList.setFocusable(false);
                checkedList.setBorder(new EmptyBorder(0, 0, 0, 0));
                checkedList.addMouseListener(selectListener);
                checkedList.addKeyListener(keySelectListener);
            }
        }

        public void show() {
            updatePopup();
            show(checkedComboBox, 0, checkedComboBox.getHeight());
        }

        public void hide() {
            setVisible(false);
            checkedComboBox.firePropertyChange("popupVisible", true, false);
        }

        public void setVisible(boolean visible) {
            if (!visible) {
                displaySelect();
            }

            super.setVisible(visible);
        }

        public JList getList() {
            return checkedComboBox.getCheckedList();
        }

        public MouseMotionListener getMouseMotionListener() {
            return null;
        }

        public KeyListener getKeyListener() {
            return null;
        }

        public MouseListener getMouseListener() {
            if (mouseListener == null) {
                mouseListener = new OverCheckedComboBox.CheckedListPopup.InvocationMouseHandler();
            }

            return mouseListener;
        }

        public void uninstallingUI() {
        }

        protected void togglePopup() {
            if (isVisible()) {
                hide();
            } else {
                show();
            }
        }

        protected void updatePopup() {
            OverCheckBoxList list = checkedComboBox.getCheckedList();
            OverTristateCheckBox checkBox = checkedComboBox.getSelectAllCheckBox();
            Insets insets = this.getInsets();
            Insets scrollerInsets = scrollPane.getInsets();
            Insets viewportInsets = scrollPane.getViewportBorder() == null ? null : scrollPane.getViewportBorder().getBorderInsets(null);
            Insets listInsets = list.getInsets();
            Insets selectAllCheckBoxInsets = checkBox.getInsets();
            insets.top += scrollerInsets.top + selectAllCheckBoxInsets.top + (viewportInsets == null ? 0 : viewportInsets.top);
            insets.bottom += scrollerInsets.bottom + listInsets.bottom + (viewportInsets == null ? 0 : viewportInsets.bottom);
            int maxHeight = 200;
            Dimension listSize = list.getPreferredSize();
            Dimension selectAllCheckBoxSize = checkBox.getPreferredSize();
            int preferredHeight = listSize.height + selectAllCheckBoxSize.height + listInsets.top + selectAllCheckBoxInsets.bottom + insets.top + insets.bottom
                    + (listSize.width > checkedComboBox.getWidth() ? scrollPane.getHorizontalScrollBar().getPreferredSize().height : 0);
            preferredHeight = preferredHeight > maxHeight ? maxHeight : preferredHeight;
            setPreferredSize(new Dimension(checkedComboBox.getSize().width, preferredHeight));
            List<Integer> selectedIndexes = checkedComboBox.getSelectedIndexes();
            list.clearSelection();
            list.setItemSelectedIndexes(selectedIndexes);
            list.setBackground(checkedComboBox.getBackground());
            checkBox.setBackground(checkedComboBox.getBackground());
            checkBox.setFont(list.getFont());
            checkBox.setEnabled(list.isEnabled());
            checkBox.setComponentOrientation(list.getComponentOrientation());
        }

        private void quickSelect() {
            if (fireQuickSelect) {
                clearPartiallySelected = false;
                OverTristateCheckBox checkBox = checkedComboBox.getSelectAllCheckBox();
                OverCheckBoxList list = checkedComboBox.getCheckedList();

                if (checkBox.getState() == TristateCheckBoxState.DESELECTED) {
                    partiallySelectedIndexes.clear();

                    if (!list.isSelectedEmpty() && !list.isSelectedAll()) {
                        partiallySelectedIndexes.addAll(getSelectedIndexes());
                    }

                    selecteEmpty();
                } else if (checkBox.getState() == TristateCheckBoxState.SELECTED) {
                    selecteAll();
                } else {
                    if (partiallySelectedIndexes.isEmpty()) {
                        checkBox.setState(TristateCheckBoxState.DESELECTED);
                    } else {
                        setSelectedIndexes(partiallySelectedIndexes);
                    }
                }

                clearPartiallySelected = true;
            }
        }

        protected class InvocationMouseHandler extends MouseAdapter {

            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e) && checkedComboBox.isEnabled()) {
                    if (checkedComboBox.isEditable()) {
                        Component editor = checkedComboBox.getEditor().getEditorComponent();

                        if ((!(editor instanceof JComponent)) || ((JComponent) editor).isRequestFocusEnabled()) {
                            editor.requestFocus();
                        }
                    } else if (checkedComboBox.isRequestFocusEnabled()) {
                        checkedComboBox.requestFocus();
                    }

                    togglePopup();
                }
            }
        }
    }
}