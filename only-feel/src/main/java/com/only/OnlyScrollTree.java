package com.only;

import java.awt.Color;

import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

import com.only.box.UIBox;

public class OnlyScrollTree extends OnlyScrollPane {
	
	private static final long serialVersionUID = -9002172235348323323L;
	private static final Color DISABLED_BG = UIBox.getColor(UIBox.key_color_tree_text_disabled_background);
	private Border border;
	private Border disabledBorder;
	private Color background;
	private OnlyTree tree;

	public OnlyScrollTree(OnlyTree tree) {
		super();
		setViewportView(this.tree = tree);
		init();
	}

	public OnlyScrollTree() {
		this(new OnlyTree());
	}

	public OnlyScrollTree(TreeNode root) {
		this(new OnlyTree(root));
	}

	public OnlyScrollTree(TreeModel newModel) {
		this(new OnlyTree(newModel));
	}

	private void init() {
		setBorder(new LineBorder(new Color(84, 165, 213)));
		setDisabledBorder(new LineBorder(new Color(84, 165, 213, 128)));
		setBackground(UIBox.getWhiteColor());
		setHeaderVisible(false);
		tree.setBorder(new EmptyBorder(0, 7, 0, 0));
		tree.setDisabledBorder(tree.getBorder());
		tree.setVisibleInsets(0, 0, 0, 0);
		tree.setAlpha(0.0f);
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
		return tree.getDisabledForeground();
	}

	public void setDisabledForeground(Color disabledForeground) {
		tree.setDisabledForeground(disabledForeground);
	}

	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		super.setBorder(enabled ? border : disabledBorder);
		super.setBackground(enabled ? background : DISABLED_BG);
	}

	public void setBackground(Color background) {
		this.background = background;
		super.setBackground(background);
	}

	public OnlyTree getTree() {
		return tree;
	}
}