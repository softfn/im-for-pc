package com.only.laf;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.EventObject;

import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicLabelUI;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.text.JTextComponent;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;

import com.only.OnlyTree;
import com.only.box.UIBox;
import com.only.util.OnlyUIUtil;

public class OnlyTreeUI extends BasicTreeUI {
	
	private static final Icon NODE_ICON = UIBox.getIcon(UIBox.key_icon_tree_node_default);
	private static final Color LINE_COLOR = UIBox.getColor(UIBox.key_color_tree_line);

	public static ComponentUI createUI(JComponent c) {
		return new OnlyTreeUI();
	}

	protected void paintHorizontalLine(Graphics g, JComponent c, int y, int left, int right) {
		g.setColor(LINE_COLOR);
		drawDashedHorizontalLine(g, y, left, right);
	}

	protected void paintVerticalLine(Graphics g, JComponent c, int x, int top, int bottom) {
		g.setColor(LINE_COLOR);
		drawDashedVerticalLine(g, x, top, bottom);
	}

	protected TreeCellRenderer createDefaultCellRenderer() {
		return new CTreeCellRenderer();
	}

	protected TreeCellEditor createDefaultCellEditor() {
		if (currentCellRenderer != null && (currentCellRenderer instanceof DefaultTreeCellRenderer)) {
			return new CTreeCellEditor(tree, (DefaultTreeCellRenderer) currentCellRenderer);
		} else {
			return new CTreeCellEditor(tree, null);
		}
	}

	public void update(Graphics g, JComponent c) {
		paintBackground(g, c);
		super.update(g, c);
	}

	private void paintBackground(Graphics g, JComponent c) {
		if (c instanceof OnlyTree) {
			OnlyTree tree = (OnlyTree) c;
			OnlyUIUtil.paintBackground(g, c, tree.getBackground(), tree.getBackground(), tree.getImage(), tree.isImageOnly(), tree.getAlpha(), tree.getVisibleInsets());
		}
	}

	protected void installDefaults() {
		largeModel = tree.isLargeModel() && tree.getRowHeight() > 0;
		setLeftChildIndent(4);
		setRightChildIndent(12);
	}

	public static class CTreeCellRenderer extends DefaultTreeCellRenderer {
		private static final long serialVersionUID = -615681618460114673L;

		private final Image BG_IMAGE = UIBox.getImage(UIBox.key_image_tree_selected_item_normal_background);

		private final Image BG_IMAGE_DISABLED = UIBox.getImage(UIBox.key_image_tree_selected_item_disabled_background);

		private JTree tree;

		public CTreeCellRenderer() {
			setUI(new BasicLabelUI() {
				protected void installDefaults(JLabel c) {
				}
			});

			setBorder(UIBox.getBorder(UIBox.key_border_tree_renderer));
			setOpaque(false);
			setOpenIcon(NODE_ICON);
			setClosedIcon(NODE_ICON);
			setLeafIcon(NODE_ICON);
		}

		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
			this.tree = tree;
			super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

			if (tree instanceof OnlyTree) {
				OnlyTree cTree = (OnlyTree) tree;
				Icon icon = null;

				if (leaf) {
					icon = getLeafIcon();
				} else if (expanded) {
					icon = getOpenIcon();
				} else {
					icon = getClosedIcon();
				}

				if (!cTree.isEnabled() && icon != null) {
					icon = new ImageIcon(OnlyUIUtil.toBufferedImage(((ImageIcon) icon).getImage(), 0.5f, this));
				}

				setEnabled(true);
				setFont(tree.getFont());
				setForeground(sel ? cTree.getSelectionForeground() : (cTree.isEnabled() ? cTree.getForeground() : cTree.getDisabledForeground()));
				setIconTextGap(sel ? 5 : 4);
				setIcon(icon);
			}

			return this;
		}

		public void paint(Graphics g) {
			int imageOffset = getSelectedBGStart();
			Rectangle rect = null;

			if (selected && imageOffset >= 0) {
				if (getComponentOrientation().isLeftToRight()) {
					rect = new Rectangle(imageOffset, 0, getWidth() - imageOffset, getHeight());
				} else {
					rect = new Rectangle(0, 0, getWidth() - imageOffset, getHeight());
				}

				Image image = (tree != null && !tree.isEnabled()) ? BG_IMAGE_DISABLED : BG_IMAGE;
				OnlyUIUtil.paintImage(g, image, new Insets(1, 1, 1, 1), rect, this);
			}

			paintComponent(g);
		}

		private int getSelectedBGStart() {
			if (getText() == null) {
				return -1;
			} else {
				Icon currentIcon = getIcon();
				return currentIcon == null ? 0 : currentIcon.getIconWidth() + getInsets().left + Math.max(getIconTextGap() - 2, 1);
			}
		}

		@Deprecated
		public void updateUI() {
		}
	}

	public class CTreeCellEditor extends DefaultTreeCellEditor {
		public CTreeCellEditor(JTree tree, DefaultTreeCellRenderer renderer) {
			super(tree, renderer);
		}

		public CTreeCellEditor(JTree tree, DefaultTreeCellRenderer renderer, TreeCellEditor editor) {
			super(tree, renderer, editor);
		}

		public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
			Component editorContainer = super.getTreeCellEditorComponent(tree, value, isSelected, expanded, leaf, row);
			JComponent editorComponent = (JComponent) editingComponent;
			editorComponent.setFont(tree.getFont());
			editorComponent.setForeground(tree.getForeground());
			editorComponent.setBackground(tree.getBackground());

			if (editorComponent instanceof JTextComponent && tree instanceof OnlyTree) {
				((JTextComponent) editorComponent).setSelectedTextColor(((OnlyTree) tree).getSelectionForeground());
			}

			return editorContainer;
		}

		protected TreeCellEditor createTreeCellEditor() {//
			final Border EDITOR_BORDER = UIBox.getBorder(UIBox.key_border_tree_editor);

			DefaultCellEditor editor = new DefaultCellEditor(new EditorComponent(EDITOR_BORDER)) {
				private static final long serialVersionUID = -4245514780481293601L;

				public boolean shouldSelectCell(EventObject event) {
					boolean retValue = super.shouldSelectCell(event);
					return retValue;
				}
			};

			editor.setClickCountToStart(1);
			return editor;
		}

		protected class EditorComponent extends DefaultTextField {
			private static final long serialVersionUID = 4975204501015290943L;

			private final Color SELECTION_COLOR = UIBox.getColor(UIBox.key_color_tree_text_selection);

			public EditorComponent(Border border) {
				super(null);
				setUI(new BasicTextFieldUI());
				setBorder(border);
				setMargin(new Insets(0, 0, 0, 0));
				setSelectionColor(SELECTION_COLOR);
				setCaretColor(Color.BLACK);
				setOpaque(false);
			}

			@Deprecated
			public void updateUI() {
			}
		}
	}
}