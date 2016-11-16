package com.over;

import java.awt.Component;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.plaf.ListUI;
import javax.swing.plaf.basic.BasicRadioButtonUI;

import com.only.component.CheckBoxListCellRenderer;

@SuppressWarnings("rawtypes")
public class OverCheckBoxList extends JList implements ListDataListener {
	
	private static final long serialVersionUID = 9088315318391533664L;

	private List<Object> selectedList;
	private boolean actionAllRow;

	public OverCheckBoxList() {
		super();
		init();
	}

	@SuppressWarnings("unchecked")
	public OverCheckBoxList(ListModel dataModel) {
		super(dataModel);
		init();
	}
	@SuppressWarnings("unchecked")
	public OverCheckBoxList(Object[] listData) {
		super(listData);
		init();
	}
	@SuppressWarnings("unchecked")
	public OverCheckBoxList(Vector<?> listData) {
		super(listData);
		init();
	}
	@SuppressWarnings("unchecked")
	private void init() {
		selectedList = new ArrayList<Object>();

		for (int index = 0; index < getModel().getSize(); index++) {
			selectedList.add(null);
		}

		setCellRenderer(new CheckBoxListCellRenderer.UIResource(selectedList));
		addMouseListener(new MouseListener());
		addKeyListener(new KeyListener());
		this.getModel().addListDataListener(this);
	}

	private void reverseSelected() {
		reverseSelected(this.getSelectedIndex());
	}

	public void reverseSelected(int index) {
		if (index >= 0 && index < getModel().getSize()) {
			selectedList.set(index, selectedList.get(index) == null ? getModel().getElementAt(index) : null);
			this.updateCellRenderer();
		}
	}

	public void unselectedAllItems() {
		changeAll(false);
	}

	public void selectedAllItems() {
		changeAll(true);
	}

	private void changeAll(boolean selected) {
		ListModel model = this.getModel();

		for (int index = 0; index < model.getSize(); index++) {
			selectedList.set(index, selected ? model.getElementAt(index) : null);
		}

		this.updateCellRenderer();
	}

	public List<Object> getAllItems() {
		List<Object> items = new ArrayList<Object>();
		ListModel model = this.getModel();

		for (int index = 0; index < model.getSize(); index++) {
			items.add(model.getElementAt(index));
		}

		return items;
	}

	public void setSelectedItems(List<?> items) {
		ListModel model = this.getModel();
		Object item;

		for (int index = 0; index < model.getSize(); index++) {
			item = model.getElementAt(index);
			selectedList.set(index, items.contains(item) ? item : null);
		}

		this.updateCellRenderer();
	}

	public void setItemSelectedIndexes(List<Integer> indexes) {
		ListModel model = this.getModel();

		for (int index = 0; index < model.getSize(); index++) {
			selectedList.set(index, indexes.contains(index) ? model.getElementAt(index) : null);
		}

		this.updateCellRenderer();
	}

	public List<?> getSelectedItems() {
		List<Object> list = new ArrayList<Object>();

		for (Object item : selectedList) {
			if (item != null) {
				list.add(item);
			}
		}

		return list;
	}

	public List<Integer> getItemSelectedIndexes() {
		List<Integer> list = new ArrayList<Integer>();

		for (int index = 0; index < selectedList.size(); index++) {
			if (selectedList.get(index) != null) {
				list.add(index);
			}
		}

		return list;
	}

	public void setItemSelectedAt(int index, boolean selected) {
		ListModel model = this.getModel();

		if (index >= 0 && index < model.getSize()) {
			selectedList.set(index, selected ? model.getElementAt(index) : null);
			this.updateCellRenderer();
		}
	}

	public void setSelectedItem(Object item, boolean selected) {
		ListModel model = this.getModel();
		int itemIndex = -1;

		for (int index = 0; index < model.getSize(); index++) {
			if (item == model.getElementAt(index)) {
				itemIndex = index;
				break;
			}
		}

		setItemSelectedAt(itemIndex, selected);
		this.updateCellRenderer();
	}

	public boolean isItemSelectedAt(int index) {
		if (index >= 0 && index < getModel().getSize()) {
			return selectedList.get(index) != null;
		}

		return false;
	}

	public boolean isSelectedItem(Object item) {
		ListModel model = this.getModel();
		int itemIndex = -1;

		for (int index = 0; index < model.getSize(); index++) {
			if (item == model.getElementAt(index)) {
				itemIndex = index;
				break;
			}
		}

		return isItemSelectedAt(itemIndex);
	}

	public boolean isSelectedAll() {
		int count = getModel().getSize();
		return count > 0 && getItemSelectedIndexes().size() == count;
	}

	public boolean isSelectedEmpty() {
		return getItemSelectedIndexes().size() == 0;
	}

	public boolean isActionAllRow() {
		return actionAllRow;
	}

	public void setActionAllRow(boolean actionAllRow) {
		this.actionAllRow = actionAllRow;
	}

	private void updateCellRenderer() {
		this.repaint();
	}
	@SuppressWarnings("unchecked")
	public void updateUI() {
		ListCellRenderer renderer = this.getCellRenderer();
		setUI((ListUI) UIManager.getUI(this));
		setCellRenderer(renderer);

		if (renderer instanceof Component) {
			SwingUtilities.updateComponentTreeUI((Component) renderer);
		}
	}
	@SuppressWarnings("unchecked")
	public void setModel(ListModel model) {
		if (model == null) {
			throw new IllegalArgumentException("model must be non null");
		} else {
			ListModel oldModel = this.getModel();
			oldModel.removeListDataListener(this);
			super.setModel(model);
			selectedList.clear();

			for (int index = 0; index < model.getSize(); index++) {
				selectedList.add(null);
			}

			model.addListDataListener(this);
		}
	}

	public void intervalRemoved(ListDataEvent e) {
		for (int index = e.getIndex1(); index >= e.getIndex0(); index--) {
			selectedList.remove(index);
		}
	}

	public void contentsChanged(ListDataEvent e) {
		ListModel model = (ListModel) e.getSource();

		for (int index = e.getIndex0(); index <= e.getIndex1(); index++) {
			if (selectedList.get(index) != null) {
				selectedList.set(index, model.getElementAt(index));
			}
		}
	}

	public void intervalAdded(ListDataEvent e) {
		for (int index = e.getIndex0(); index <= e.getIndex1(); index++) {
			selectedList.add(index, null);
		}
	}

	private class KeyListener extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			char keyChar = e.getKeyChar();

			if (keyChar == KeyEvent.VK_SPACE || keyChar == KeyEvent.VK_ENTER) {
				reverseSelected();
			}
		}
	}

	private class MouseListener extends MouseAdapter {
		public void mouseReleased(MouseEvent e) {
			JCheckBox box = (JCheckBox) getCellRenderer();
			boolean selected = isItemSelectedAt(getSelectedIndex());
			box.setSelected(selected);
			Icon icon = selected ? box.getSelectedIcon() : box.getIcon();
			icon = icon == null ? ((BasicRadioButtonUI) box.getUI()).getDefaultIcon() : icon;
			Insets insets = getInsets();
			int startX = insets.left + (actionAllRow ? 0 : box.getInsets().left);
			int endX = actionAllRow ? getWidth() - insets.right : startX + (icon == null ? 0 : icon.getIconWidth());

			if (e.getX() >= startX && e.getX() <= endX && SwingUtilities.isLeftMouseButton(e)) {
				reverseSelected();
			}
		}
	}
}