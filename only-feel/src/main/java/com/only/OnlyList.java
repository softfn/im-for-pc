package com.only;

import java.awt.Color;
import java.awt.Image;
import java.awt.Insets;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.only.box.UIBox;
import com.only.laf.OnlyListCellRenderer;
import com.only.laf.OnlyListUI;
import com.only.util.OnlyUIUtil;

@SuppressWarnings("rawtypes")
public class OnlyList<E> extends JList {
	private static final long serialVersionUID = 4140226075621300872L;

	private static final Color DISABLED_BG = UIBox.getColor(UIBox.key_color_list_disabled_background);

	private Image image;
	private float alpha;
	private boolean imageOnly;
	private boolean rendererOpaque;
	private Insets visibleInsets;
	private Border border;
	private Border disabledBorder;
	private Color disabledForeground;
	private Color background;
	private Color rendererBackground1;
	private Color rendererBackground2;

	public OnlyList() {
		this(new AbstractListModel() {
			private static final long serialVersionUID = 7026637611528615834L;

			public int getSize() {
				return 0;
			}

			public Object getElementAt(int i) {
				return "No Data Model";
			}
		});
	}

	public OnlyList(final Object[] listData) {
		this(new AbstractListModel() {
			private static final long serialVersionUID = 4555057559640490290L;

			public int getSize() {
				return listData.length;
			}

			public Object getElementAt(int i) {
				return listData[i];
			}
		});
	}

	public OnlyList(final Vector<?> listData) {
		this(new AbstractListModel() {
			private static final long serialVersionUID = 4410354317412379521L;

			public int getSize() {
				return listData.size();
			}

			public Object getElementAt(int i) {
				return listData.elementAt(i);
			}
		});
	}

	@SuppressWarnings("unchecked")
	public OnlyList(ListModel dataModel) {
		super(dataModel);
		setUI(new OnlyListUI());
		setBorder(new CompoundBorder(new LineBorder(new Color(84, 165, 213)), new EmptyBorder(1, 1, 1, 1)));
		setDisabledBorder(new CompoundBorder(new LineBorder(new Color(84, 165, 213, 128)), new EmptyBorder(1, 1, 1, 1)));
		setFixedCellHeight(20);
		setCellRenderer(new OnlyListCellRenderer());
		setFont(OnlyUIUtil.getDefaultFont());
		setBackground(UIBox.getWhiteColor());
		setForeground(Color.BLACK);
		setSelectionForeground(UIBox.getWhiteColor());
		setDisabledForeground(new Color(123, 123, 122));
		setSelectionBackground(Color.BLUE);
		super.setOpaque(false);
		alpha = 1.0f;
		visibleInsets = new Insets(1, 1, 1, 1);
		rendererOpaque = true;
		rendererBackground1 = new Color(251, 251, 255);
		rendererBackground2 = new Color(243, 248, 251);
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

	public boolean isRendererOpaque() {
		return rendererOpaque;
	}

	public void setRendererOpaque(boolean rendererOpaque) {
		this.rendererOpaque = rendererOpaque;
		this.repaint();
	}

	public Color getRendererBackground1() {
		return rendererBackground1;
	}

	public void setRendererBackground1(Color rendererBackground1) {
		this.rendererBackground1 = rendererBackground1;
		this.repaint();
	}

	public Color getRendererBackground2() {
		return rendererBackground2;
	}

	public void setRendererBackground2(Color rendererBackground2) {
		this.rendererBackground2 = rendererBackground2;
		this.repaint();
	}

	@Deprecated
	public void updateUI() {
	}

	@Deprecated
	public void setOpaque(boolean isOpaque) {
	}
}