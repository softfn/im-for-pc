package com.only;

import java.awt.Color;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.lang.reflect.Field;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.only.box.UIBox;
import com.only.laf.OnlyTreeUI;
import com.only.util.OnlyUIUtil;

public class OnlyTree extends JTree {
	
	//key_icon_tree_expanded
	//key_icon_tree_collapsed
	private static final long serialVersionUID = -645647197684495937L;
	private static final Icon EXPANDED_ICON = UIBox.getIcon(UIBox.key_icon_tree_expanded);
	private static final Icon COLLAPSED_ICON = UIBox.getIcon(UIBox.key_icon_tree_collapsed);
	
	//key_color_tree_text_disabled_background
	private static final Color DISABLED_BG = UIBox.getColor(UIBox.key_color_tree_text_disabled_background);
	public static final Border DEFAULT_OUTSIDE_BORDER = new LineBorder(new Color(84, 165, 213));
	public static final Border DEFAULT_DISABLED_OUTSIDE_BORDER = new LineBorder(new Color(84, 165, 213, 128));
	public static final Border DEFAULT_INSIDE_BORDER = new EmptyBorder(1, 8, 1, 1);
	private Image image;
	private float alpha;
	private boolean imageOnly;
	private boolean paintLines;
	private Insets visibleInsets;
	private Border border;
	private Border disabledBorder;
	private Color disabledForeground;
	private Color selectionForeground;
	private Color background;

	public OnlyTree() {
		this(getDefaultTreeModel());
	}

	public OnlyTree(TreeNode root) {
		this(root, false);
	}

	public OnlyTree(TreeNode root, boolean asksAllowsChildren) {
		this(new DefaultTreeModel(root, asksAllowsChildren));
	}

	public OnlyTree(Object[] value) {
		super(value);
		init();
	}

	public OnlyTree(Vector<?> value) {
		super(value);
		init();
	}

	public OnlyTree(Hashtable<?, ?> value) {
		super(value);
		init();
	}

	public OnlyTree(TreeModel newModel) {
		super(newModel);
		init();
	}

	private void init() {
		setUI(new OnlyTreeUI());
		setExpandedIcon(EXPANDED_ICON);
		setCollapsedIcon(COLLAPSED_ICON);
		setBorder(new CompoundBorder(DEFAULT_OUTSIDE_BORDER, DEFAULT_INSIDE_BORDER));
		setDisabledBorder(new CompoundBorder(DEFAULT_DISABLED_OUTSIDE_BORDER, DEFAULT_INSIDE_BORDER));
		setFont(OnlyUIUtil.getDefaultFont());
		setBackground(UIBox.getWhiteColor());
		setForeground(Color.BLACK);
		setSelectionForeground(UIBox.getWhiteColor());
		setDisabledForeground(new Color(123, 123, 122));
		super.setOpaque(false);
		setRowHeight(20);
		setShowsRootHandles(true);
		setLargeModel(false);
		setScrollsOnExpand(true);
		alpha = 1.0f;
		visibleInsets = new Insets(1, 1, 1, 1);
		paintLines = true;
	}

	public Icon getCollapsedIcon() {
		return ((BasicTreeUI) getUI()).getCollapsedIcon();
	}

	public void setCollapsedIcon(Icon icon) {
		((BasicTreeUI) getUI()).setCollapsedIcon(icon);
	}

	public Icon getExpandedIcon() {
		return ((BasicTreeUI) getUI()).getExpandedIcon();
	}

	public void setExpandedIcon(Icon icon) {
		((BasicTreeUI) getUI()).setExpandedIcon(icon);
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

	public Color getDisabledForeground() {
		return disabledForeground;
	}

	public void setDisabledForeground(Color disabledForeground) {
		this.disabledForeground = disabledForeground;

		if (!this.isEnabled()) {
			this.repaint();
		}
	}

	public Color getSelectionForeground() {
		return selectionForeground;
	}

	public void setSelectionForeground(Color selectionForeground) {
		this.selectionForeground = selectionForeground;
		this.repaint();
	}

	public void setBorder(Border border) {
		this.border = border;
		super.setBorder(border);
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

	public boolean isPaintLines() {
		return paintLines;
	}

	public void setPaintLines(boolean paintLines) {
		if (this.paintLines != paintLines) {
			try {
				this.paintLines = paintLines;
				Field paintLinesField = BasicTreeUI.class.getDeclaredField("paintLines");
				paintLinesField.setAccessible(true);
				paintLinesField.set(this.getUI(), paintLines);
				this.repaint();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public TreePath getPathForY(int y) {
		TreePath closestPath = getClosestPathForLocation(0, y);

		if (closestPath != null) {
			Rectangle pathBounds = getPathBounds(closestPath);

			if (pathBounds != null && y >= pathBounds.y && y < (pathBounds.y + pathBounds.height)) {
				return closestPath;
			}
		}

		return null;
	}

	@Deprecated
	public void updateUI() {
	}

	@Deprecated
	public void setOpaque(boolean isOpaque) {
	}
}