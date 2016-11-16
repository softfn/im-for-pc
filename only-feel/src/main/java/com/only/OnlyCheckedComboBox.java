package com.only;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListDataEvent;
import javax.swing.plaf.basic.BasicPopupMenuUI;
import javax.swing.plaf.basic.BasicScrollPaneUI;
import javax.swing.plaf.basic.ComboPopup;

import com.only.box.UIBox;
import com.only.component.TristateCheckBoxState;
import com.only.laf.OnlyComboBoxUI;
import com.only.laf.OnlyScrollBarUI;

@SuppressWarnings("rawtypes")
public class OnlyCheckedComboBox<E> extends OnlyComboBox {

    private static final long serialVersionUID = 6723787291459475194L;
    private OnlyCheckBoxList<?> checkList;
    private OnlyTristateCheckBox selectAllCheckBox;
    private String text4SelectedAll;
    private DefaultListModel listModel;
    private List<Integer> partiallySelectedIndexes;
    private boolean fireQuickSelect;
    private boolean clearPartiallySelected;

    public OnlyCheckedComboBox() {
        super();
        init(null);
    }

    @SuppressWarnings("unchecked")
	public OnlyCheckedComboBox(ComboBoxModel<E> model) {
        super(model);
        init(null);
    }

    public OnlyCheckedComboBox(Object[] listData) {
        super(listData);
        init(listData);
    }

    @SuppressWarnings("unchecked")
	public OnlyCheckedComboBox(Vector<?> listData) {
        super(listData);
        init(listData);
    }

    @SuppressWarnings({"unchecked"})
    private void init(Object listData) {
        this.fireQuickSelect = true;
        this.clearPartiallySelected = true;
        this.partiallySelectedIndexes = new ArrayList<Integer>();
        this.checkList = new OnlyCheckBoxList(listModel = new DefaultListModel());
        this.selectAllCheckBox = new OnlyTristateCheckBox("\u5168\u9009");

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

        setUI(new CCheckedComboBoxUI());
        checkList.setForeground(this.getForeground());
        selectAllCheckBox.setForeground(this.getForeground());
        this.displaySelect();
    }

    public OnlyCheckBoxList<?> getCheckedList() {
        return checkList;
    }

    public OnlyTristateCheckBox getSelectAllCheckBox() {
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

    @Override
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
        PopupList<Object> list = new PopupList<Object>();

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

    @Override
    public void intervalRemoved(ListDataEvent e) {
        super.intervalRemoved(e);

        for (int index = e.getIndex1(); index >= e.getIndex0(); index--) {
            listModel.remove(index);
        }

        displaySelect();
    }

    @SuppressWarnings("unchecked")
    @Override
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

    @SuppressWarnings("hiding")
	private class PopupList<E> extends ArrayList<E> {

        private static final long serialVersionUID = -641625025177013444L;

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

    private class CCheckedComboBoxUI extends OnlyComboBoxUI {

        protected ComboPopup createPopup() {
            return new CheckedListPopup(comboBox);
        }

        protected void selectNextPossibleValue() {
        }

        protected void selectPreviousPossibleValue() {
        }
    }

    private class CheckedListPopup extends JPopupMenu implements ComboPopup {

        private static final long serialVersionUID = 6354944188855377523L;
        private JScrollPane scrollPane;
        protected OnlyCheckedComboBox<?> checkedComboBox;
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
            this.checkedComboBox = (OnlyCheckedComboBox) checkedComboBox;
            OnlyCheckBoxList checkedList = this.checkedComboBox.getCheckedList();
            OnlyTristateCheckBox selectAllCheck = this.checkedComboBox.getSelectAllCheckBox();
            JComponent listParent = new JComponent() {
                private static final long serialVersionUID = -7045470370589799476L;
            };
            setUI(new BasicPopupMenuUI() {
                public void installDefaults() {
                }

                protected void uninstallDefaults() {
                }
            });
            setBorder(new EmptyBorder(0, 0, 0, 0));
            setLayout(new BorderLayout());
            setOpaque(false);
            setFocusable(false);
            setLightWeightPopupEnabled(checkedComboBox.isLightWeightPopupEnabled());
            configureListCheckBox(selectAllCheck);
            configureList(checkedList);
            listParent.setLayout(new BorderLayout());
            listParent.add(checkedList, BorderLayout.CENTER);
            listParent.add(selectAllCheck, BorderLayout.NORTH);
            add(createScroller(listParent), BorderLayout.CENTER);
        }

        private JScrollPane createScroller(JComponent listParent) {
            scrollPane = new JScrollPane(listParent) {
                private static final long serialVersionUID = -3772916298038957310L;

                public JScrollBar createVerticalScrollBar() {
                    ScrollBar vBar = new ScrollBar(JScrollBar.VERTICAL) {
                        private static final long serialVersionUID = -7301889224776927442L;

                        @Deprecated
                        public void updateUI() {
                        }
                    };

                    vBar.setUI(new OnlyScrollBarUI());
                    vBar.setBorder(null);
                    return vBar;
                }

                public JScrollBar createHorizontalScrollBar() {
                    ScrollBar hBar = new ScrollBar(JScrollBar.HORIZONTAL) {
                        private static final long serialVersionUID = -7726079574528933660L;

                        @Deprecated
                        public void updateUI() {
                        }
                    };

                    hBar.setUI(new OnlyScrollBarUI());
                    hBar.setBorder(null);
                    return hBar;
                }

                @Deprecated
                public void updateUI() {
                }
            };

            scrollPane.setUI(new BasicScrollPaneUI() {
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
            scrollPane.getHorizontalScrollBar().setOpaque(false);
            scrollPane.getHorizontalScrollBar().setFocusable(false);
            scrollPane.getVerticalScrollBar().setOpaque(false);
            scrollPane.getVerticalScrollBar().setFocusable(false);
            scrollPane.getViewport().setOpaque(false);
            scrollPane.setViewportBorder(new EmptyBorder(0, 0, 0, 0));
            scrollPane.setOpaque(false);
            scrollPane.setBorder(UIBox.getBorder(UIBox.key_border_checked_combo_box_popup));
            scrollPane.setFocusable(false);
            return scrollPane;
        }

        private void configureListCheckBox(OnlyTristateCheckBox selectAllCheck) {
            OnlyCheckBoxList<?> checkedList = checkedComboBox.getCheckedList();
            JComponent cellRenderer = (JComponent) checkedList.getCellRenderer();
            int cellHeight = checkedList.getFixedCellHeight();
            selectAllCheck.setBorder(cellRenderer.getBorder());
            selectAllCheck.setRolloverEnabled(false);
            selectAllCheck.setContentAreaFilled(false);
            selectAllCheck.setFocusable(false);
            selectAllCheck.setBorderPainted(false);
            selectAllCheck.setOpaque(false);
            selectAllCheck.setMargin(new Insets(0, 0, 0, 0));
            selectAllCheck.setRolloverIcon(null);
            selectAllCheck.setRolloverNotspecifiedIcon(null);
            selectAllCheck.setRolloverSelectedIcon(null);
            selectAllCheck.setPressedIcon(null);
            selectAllCheck.setPressedNotspecifiedIcon(null);
            selectAllCheck.setPressedSelectedIcon(null);

            if (cellHeight < 0) {
                selectAllCheck.setPreferredSize(cellRenderer.getPreferredSize());
            } else {
                selectAllCheck.setPreferredSize(new Dimension(-1, cellHeight));
            }

            selectAllCheck.addItemListener(selectAllCheckListener);
        }

        private void configureList(OnlyCheckBoxList<?> checkedList) {
            checkedList.setBorder(new EmptyBorder(0, 0, 0, 0));
            checkedList.setDisabledBorder(checkedList.getBorder());
            checkedList.setVisibleInsets(0, 0, 0, 0);
            checkedList.setAlpha(0.0f);
            checkedList.setRendererOpaque(false);
            checkedList.setFocusable(false);
            checkedList.addMouseListener(selectListener);
            checkedList.addKeyListener(keySelectListener);
        }

        public void show() {
            if (!checkedComboBox.isEditableAll()) {
                return;
            }

            updatePopup();
            show(checkedComboBox, 0, checkedComboBox.getHeight());
        }

        public void hide() {
            setVisible(false);
            checkedComboBox.firePropertyChange("popupVisible", true, false);
        }

        public OnlyCheckBoxList<?> getList() {
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
                mouseListener = new InvocationMouseHandler();
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
            OnlyCheckBoxList<?> list = checkedComboBox.getCheckedList();
            OnlyTristateCheckBox checkBox = checkedComboBox.getSelectAllCheckBox();
            Insets insets = this.getInsets();
            Insets scrollerInsets = scrollPane.getInsets();
            Insets viewportInsets = scrollPane.getViewportBorder().getBorderInsets(null);
            Insets listInsets = list.getInsets();
            Insets selectAllCheckBoxInsets = checkBox.getInsets();
            insets.top += scrollerInsets.top + selectAllCheckBoxInsets.top + viewportInsets.top;
            insets.bottom += scrollerInsets.bottom + listInsets.bottom + viewportInsets.bottom;
            int maxHeight = 230;
            Dimension listSize = list.getPreferredSize();
            Dimension selectAllCheckBoxSize = checkBox.getPreferredSize();
            int preferredHeight = listSize.height + selectAllCheckBoxSize.height + listInsets.top + selectAllCheckBoxInsets.bottom + insets.top + insets.bottom + (listSize.width > checkedComboBox.getWidth() ? scrollPane.getHorizontalScrollBar().getPreferredSize().height + 1 : 0);
            preferredHeight = preferredHeight > maxHeight ? maxHeight : preferredHeight;
            setPreferredSize(new Dimension(checkedComboBox.getSize().width, preferredHeight));
            List<Integer> selectedIndexes = checkedComboBox.getSelectedIndexes();
            list.clearSelection();
            list.setItemSelectedIndexes(selectedIndexes);
            checkBox.setFont(list.getFont());
            checkBox.setEnabled(list.isEnabled());
            checkBox.setComponentOrientation(list.getComponentOrientation());
        }

        private void quickSelect() {
            if (fireQuickSelect) {
                clearPartiallySelected = false;
                OnlyTristateCheckBox checkBox = checkedComboBox.getSelectAllCheckBox();
                OnlyCheckBoxList<?> list = checkedComboBox.getCheckedList();

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

        public void setVisible(boolean visible) {
            if (!visible) {
                displaySelect();
            }

            super.setVisible(visible);

            if (visible) {
                float alpha = checkedComboBox.isImageOnly() ? 0.0f : checkedComboBox.getAlpha();
                Color oldBg = checkedComboBox.getBackground();
                scrollPane.setBackground(new Color(oldBg.getRed(), oldBg.getGreen(), oldBg.getBlue(), (int) Math.round(255 * alpha)));
                int right = scrollPane.getVerticalScrollBar().isVisible() ? 1 : 0;
                int bottom = scrollPane.getHorizontalScrollBar().isVisible() ? 1 : 0;
                scrollPane.setViewportBorder(new EmptyBorder(0, 0, bottom, right));
            } else {
                checkedComboBox.resetBorder();
            }
        }

        protected class InvocationMouseHandler extends MouseAdapter {

            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e) && checkedComboBox.isEnabled() && checkedComboBox.isEditableAll()) {
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

        @Deprecated
        public void updateUI() {
            setUI(this.getUI());
        }
    }
}