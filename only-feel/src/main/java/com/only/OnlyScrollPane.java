package com.only;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicLabelUI;
import javax.swing.plaf.basic.BasicViewportUI;

import com.only.box.UIBox;
import com.only.component.HeaderPane;
import com.only.laf.OnlyScrollBarUI;
import com.only.laf.OnlyScrollPaneUI;
import com.only.util.OnlyUIUtil;

public class OnlyScrollPane extends JScrollPane implements AdjustmentListener {

	private static final long serialVersionUID = -8833386850571879174L;
	private Image image;
	private float alpha;
	private boolean imageOnly;
	private Insets visibleInsets;
	private Border insideBorder;
	private Border outsideBorder;
	private HeaderPane header;
	private HeaderPane upperRightCorner;
	private JLabel headerLabel;
	private Color headerForeground;
	private Color headerDisabledForeground;
	private Font headerFont;
	private boolean headerVisible;

	public OnlyScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
		super(view, vsbPolicy, hsbPolicy);
		setUI(new OnlyScrollPaneUI());
		super.setBorder(new CompoundBorder(outsideBorder = super.getBorder(), insideBorder = new EmptyBorder(1, 1, 1, 1)));
		super.setOpaque(false);
		setBackground(Color.white);
		setForeground(Color.BLACK);
		setFont(OnlyUIUtil.getDefaultFont());
		setHeaderFont(this.getFont());
		setHeaderVisible(true);
		setHeaderForeground(new Color(0, 28, 48));
		setHeaderDisabledForeground(new Color(128, 142, 152));
		initHeader();
		setCorner(LOWER_RIGHT_CORNER, createLowerRightCorner());
		alpha = 1.0f;
		visibleInsets = new Insets(1, 1, 1, 1);

		viewport.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				int right = verticalScrollBar.isVisible() ? 1 : 0;
				int bottom = (horizontalScrollBar != null && horizontalScrollBar.isVisible()) ? 1 : 0;
				setViewportBorder(new EmptyBorder(0, 0, bottom, right));
			}
		});
	}

	public OnlyScrollPane(Component view) {
		this(view, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}

	public OnlyScrollPane(int vsbPolicy, int hsbPolicy) {
		this(null, vsbPolicy, hsbPolicy);
	}

	public OnlyScrollPane() {
		this(null, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}

	protected void initHeader() {
		setColumnHeaderView(header = new HeaderPane(headerLabel = createHeaderLabel()));
		setCorner(UPPER_RIGHT_CORNER, upperRightCorner = new HeaderPane());
		headerLabel.setBorder(new EmptyBorder(0, 7, 0, 0));
		headerLabel.setOpaque(false);
		this.getHorizontalScrollBar().addAdjustmentListener(this);
		this.getVerticalScrollBar().addAdjustmentListener(this);
	}

	private JComponent createLowerRightCorner() {
		JLabel label = new JLabel(UIBox.getIcon(UIBox.key_icon_scroll_pane_lower_right_corner_icon)) {
			private static final long serialVersionUID = 5657359502965563304L;

			@Deprecated
			@Override
			public void updateUI() {
			}
		};

		label.setUI(new BasicLabelUI() {
			@Override
			protected void installDefaults(JLabel c) {
			}
		});
		label.setOpaque(false);
		return label;
	}

	protected JLabel createHeaderLabel() {
		JLabel label = new JLabel() {
			private static final long serialVersionUID = 5966526868775976808L;

			@Deprecated
			@Override
			public void updateUI() {
			}
		};

		label.setUI(new BasicLabelUI() {
			@Override
			protected void installDefaults(JLabel c) {
			}
		});

		return label;
	}

	protected JViewport createViewport() {
		JViewport viewport = new JViewport() {
			private static final long serialVersionUID = -4480846449651574857L;

			@Deprecated
			public void updateUI() {
			}
		};

		viewport.setUI(new BasicViewportUI() {
			protected void installDefaults(JComponent c) {
			}
		});

		viewport.setFont(OnlyUIUtil.getDefaultFont());
		viewport.setForeground(Color.BLACK);
		viewport.setBackground(UIBox.getEmptyColor());
		viewport.setOpaque(false);
		return viewport;
	}

	public JScrollBar createHorizontalScrollBar() {
		return new CScrollBar(JScrollBar.HORIZONTAL);
	}

	public JScrollBar createVerticalScrollBar() {
		return new CScrollBar(JScrollBar.VERTICAL);
	}

	public void setColumnHeader(JViewport columnHeader) {
		super.setColumnHeader(columnHeader);

		if (columnHeader != null) {
			columnHeader.setVisible(headerVisible);
		}
	}

	public void setColumnHeaderView(Component view) {
		super.setColumnHeaderView(view);

		if (view != null) {
			if (headerFont != null) {
				updateHeaderProperty(view, "font", headerFont);
			}

			Color color = null;

			if (this.isEnabled() && headerForeground != null) {
				color = headerForeground;
			} else if (!this.isEnabled() && headerDisabledForeground != null) {
				color = headerDisabledForeground;
			}

			if (color != null) {
				updateHeaderProperty(view, "foreground", color);
			}
		}
	}

	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);

		if (header != null) {
			header.setEnabled(enabled);
		}

		if (upperRightCorner != null) {
			upperRightCorner.setEnabled(enabled);
		}

		Component view, headerView;

		if (viewport != null && (view = viewport.getView()) != null) {
			view.setEnabled(enabled);
		}

		if (columnHeader != null && (headerView = columnHeader.getView()) != null) {
			updateHeaderProperty(headerView, "foreground", enabled ? headerForeground : headerDisabledForeground);
		}
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
		this.repaint();
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

	public Insets getVisibleInsets() {
		return visibleInsets;
	}

	public void setVisibleInsets(int top, int left, int bottom, int right) {
		this.visibleInsets.set(top, left, bottom, right);
		this.repaint();
	}

	public void setBorder(Border border) {
		if (border == null && visibleInsets != null) {
			visibleInsets.set(0, 0, 0, 0);
		}

		this.outsideBorder = border;
		super.setBorder(new CompoundBorder(outsideBorder, insideBorder));
	}

	public void setInsideBorder(Border insideBorder) {
		this.insideBorder = insideBorder;
		super.setBorder(new CompoundBorder(outsideBorder, insideBorder));
	}

	public Border getInsideBorder() {
		return this.insideBorder;
	}

	public boolean isImageOnly() {
		return imageOnly;
	}

	public void setImageOnly(boolean imageOnly) {
		this.imageOnly = imageOnly;
		this.repaint();
	}

	public JLabel getHeaderLabel() {
		return headerLabel;
	}

	public HeaderPane getHeader() {
		return header;
	}

	public String getHeaderText() {
		return headerLabel == null ? null : headerLabel.getText();
	}

	public void setHeaderText(String text) {
		if (headerLabel != null) {
			headerLabel.setText(text);
		}
	}

	public boolean isHeaderVisible() {
		return headerVisible && columnHeader != null;
	}

	public void setHeaderVisible(boolean visible) {
		this.headerVisible = visible;

		if (columnHeader != null) {
			columnHeader.setVisible(headerVisible);
		}
	}

	public Color getHeaderForeground() {
		return headerForeground;
	}

	public void setHeaderForeground(Color headerForeground) {
		this.headerForeground = headerForeground;
		Component view;

		if (this.isEnabled() && columnHeader != null && (view = columnHeader.getView()) != null) {
			updateHeaderProperty(view, "foreground", headerForeground);
		}
	}

	public Color getHeaderDisabledForeground() {
		return headerDisabledForeground;
	}

	public void setHeaderDisabledForeground(Color headerDisabledForeground) {
		this.headerDisabledForeground = headerDisabledForeground;
		Component view;

		if (!this.isEnabled() && columnHeader != null && (view = columnHeader.getView()) != null) {
			updateHeaderProperty(view, "foreground", headerDisabledForeground);
		}
	}

	public Font getHeaderFont() {
		return headerFont;
	}

	public void setHeaderFont(Font headerFont) {
		this.headerFont = headerFont;
		Component view;

		if (columnHeader != null && (view = columnHeader.getView()) != null) {
			updateHeaderProperty(view, "font", headerFont);
		}
	}

	private void updateHeaderProperty(Component c, String propertyName, Object value) {
		if (propertyName.equals("font")) {
			c.setFont((Font) value);
		} else if (propertyName.equals("foreground")) {
			c.setForeground((Color) value);
		}

		if (c instanceof Container) {
			for (Component child : ((Container) c).getComponents()) {
				updateHeaderProperty(child, propertyName, value);
			}
		}
	}

	public void adjustmentValueChanged(AdjustmentEvent e) {
		if (header != null && header.isVisible()) {
			header.revalidate();
		}
	}

	@Deprecated
	public void updateUI() {
	}

	@Deprecated
	public void setOpaque(boolean isOpaque) {
	}

	protected class CScrollBar extends ScrollBar {

		private static final long serialVersionUID = -8174518362746135594L;

		public CScrollBar(int orientation) {
			super(orientation);
			setUI(new OnlyScrollBarUI());
			setOpaque(false);
			setBorder(null);
		}

		@Deprecated
		public void updateUI() {
		}
	}
}