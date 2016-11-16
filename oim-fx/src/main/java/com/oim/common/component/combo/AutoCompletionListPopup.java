/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oim.common.component.combo;

/**
 *
 * @author XiaHui
 */
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputListener;

import com.only.OnlyList;
import com.only.OnlyScrollPane;

/**
 * 
 * @author William Chen
 */
@SuppressWarnings("rawtypes")
public class AutoCompletionListPopup extends JPopupMenu implements MouseInputListener {

	private static final long serialVersionUID = 1L;
	// private JLabel label = new JLabel();
	// private JPanel panel = new JPanel();
	private JList list = new OnlyList();
	private OnlyScrollPane scrollPane = new OnlyScrollPane();
	private ArrayList<ListSelectionListener> listeners = new ArrayList<ListSelectionListener>();

	@SuppressWarnings({ "unchecked" })
	public AutoCompletionListPopup() {
		this.setLayout(new java.awt.CardLayout());
		// label.setLayout(new java.awt.CardLayout());

		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.addMouseListener(this);
		list.addMouseMotionListener(this);
		list.setModel(new DefaultListModel());
		list.setBorder(null);
		
		scrollPane.setHeaderVisible(false);
		scrollPane.setViewportView(list);
		scrollPane.setBorder(null);

		// label.add(pane);
		add(scrollPane);
	}

	// public void setIcon(Icon icon) {
	// label.setIcon(icon);
	// }
	public void addListSelectionListener(ListSelectionListener l) {
		if (!listeners.contains(l)) {
			listeners.add(l);
		}
	}

	public void setSelectedIndex(int index) {
		if (index >= list.getModel().getSize()) {
			index = 0;
		}
		if (index < 0) {
			index = list.getModel().getSize() - 1;
		}
		list.ensureIndexIsVisible(index);
		list.setSelectedIndex(index);
	}

	public Object getSelectedValue() {
		return list.getSelectedValue();
	}

	public int getSelectedIndex() {
		return list.getSelectedIndex();
	}

	public boolean isSelected() {
		return list.getSelectedIndex() != -1;
	}

	public void setLastOneSelected() {
		int count = list.getModel().getSize();
		if (count > 0) {
			list.ensureIndexIsVisible(count - 1);
			list.setSelectedIndex(count - 1);
		}
	}

	public void removeListSelectionListener(ListSelectionListener l) {
		if (listeners.contains(l)) {
			listeners.remove(l);
		}
	}

	private void fireValueChanged(ListSelectionEvent e) {
		for (ListSelectionListener l : listeners) {
			l.valueChanged(e);
		}
	}

	@SuppressWarnings("unchecked")
	public void setListCellRenderer(ListCellRenderer listCellRenderer) {
		list.setCellRenderer(listCellRenderer);
	}

	public int getItemCount() {
		DefaultListModel model = (DefaultListModel) list.getModel();
		return model.getSize();
	}

	public Object getItem(int index) {
		DefaultListModel model = (DefaultListModel) list.getModel();
		return model.get(index);
	}

	@SuppressWarnings({ "unchecked" })
	public void addItem(Object o) {
		DefaultListModel model = (DefaultListModel) list.getModel();
		model.addElement(o);
		list.repaint();
	}

	public void removeItem(Object o) {
		DefaultListModel model = (DefaultListModel) list.getModel();
		model.removeElement(o);
		list.repaint();
	}

	@SuppressWarnings("unchecked")
	public void setList(Iterable iterable) {
		DefaultListModel model = new DefaultListModel();
		model.removeAllElements();
		for (Object o : iterable) {
			model.addElement(o);
		}
		list.setModel(model);
		list.repaint();
	}

	@SuppressWarnings("unchecked")
	public void setList(Enumeration e) {
		DefaultListModel model = new DefaultListModel();
		while (e.hasMoreElements()) {
			model.addElement(e.nextElement());
		}
		list.setModel(model);
		list.repaint();
	}

	@SuppressWarnings("unchecked")
	public void setList(Object... objects) {
		DefaultListModel model = new DefaultListModel();
		for (Object o : objects) {
			model.addElement(o);
		}
		list.setModel(model);
		list.repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (list.getSelectedIndex() != -1) {
			fireValueChanged(new ListSelectionEvent(list, list.getSelectedIndex(), list.getSelectedIndex(), true));
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent anEvent) {
		if (anEvent.getSource() == list) {
			Point location = anEvent.getPoint();
			Rectangle r = new Rectangle();
			list.computeVisibleRect(r);
			if (r.contains(location)) {
				updateListBoxSelectionForEvent(anEvent, false);
			}
		}
	}

	protected void updateListBoxSelectionForEvent(MouseEvent anEvent, boolean shouldScroll) {

		Point location = anEvent.getPoint();
		if (list == null) {
			return;
		}
		int index = list.locationToIndex(location);
		if (index == -1) {
			if (location.y < 0) {
				index = 0;
			} else {
				index = list.getModel().getSize() - 1;
			}
		}
		if (list.getSelectedIndex() != index) {
			list.setSelectedIndex(index);
			if (shouldScroll) {
				list.ensureIndexIsVisible(index);
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}
}
