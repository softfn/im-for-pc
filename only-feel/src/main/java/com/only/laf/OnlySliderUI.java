package com.only.laf;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.basic.BasicSliderUI;

import com.only.OnlySlider;
import com.only.box.UIBox;
import com.only.util.OnlyUIUtil;

public class OnlySliderUI extends BasicSliderUI {

	private static final Icon THUMB_ICON_H = UIBox.getIcon(UIBox.key_icon_slider_thumb_h);
	private static final Icon THUMB_ICON_V = UIBox.getIcon(UIBox.key_icon_slider_thumb_v);
	private static final Icon THUMB_MINI_ICON_H = UIBox.getIcon(UIBox.key_icon_slider_min_thumb_h);
	private static final Icon THUMB_MINI_ICON_V = UIBox.getIcon(UIBox.key_icon_slider_min_thumb_v);
	private static final Image BG_IMAGE_H = UIBox.getImage(UIBox.key_image_slider_background_h);
	private static final Image BG_IMAGE_V = UIBox.getImage(UIBox.key_image_slider_background_v);

	private static final Color VALUE_MINI_COLOR_OUTER = new Color(78, 117, 160);
	private static final Color VALUE_MINI_BACKGROUND = Color.WHITE;
	private static final Color VALUE_MINI_COLOR_INNER_1 = new Color(73, 239, 54);
	private static final Color VALUE_MINI_COLOR_INNER_2 = new Color(238, 255, 71);
	private static final Color VALUE_COLOR_OUTER_1 = new Color(157, 199, 240);
	private static final Color VALUE_COLOR_OUTER_2 = new Color(154, 235, 171);
	private static final Color VALUE_COLOR_INNER_1 = new Color(76, 181, 237);
	private static final Color VALUE_COLOR_INNER_2 = new Color(53, 215, 89);

	private static final Composite DISABLED_COMPOSITE = AlphaComposite.SrcOver.derive(0.5f);

	public OnlySliderUI() {
		this(null);
	}

	public OnlySliderUI(JSlider slider) {
		super(slider);
	}

	public static ComponentUI createUI(JComponent c) {
		return new OnlySliderUI();
	}

	public void paintFocus(Graphics g) {
	}

	public void paintTrack(Graphics g) {
		if (slider instanceof OnlySlider) {
			boolean miniMode = ((OnlySlider) slider).isMiniMode();
			boolean horizontal = slider.getOrientation() == JSlider.HORIZONTAL;
			int trackThickness = miniMode ? 4 : 9;
			Rectangle rect = new Rectangle(trackRect);
			int delta = (horizontal ? rect.height : rect.width) - trackThickness;
			Graphics2D g2d = (Graphics2D) g;
			Composite oldComposite = g2d.getComposite();

			if (!slider.isEnabled()) {
				g2d.setComposite(DISABLED_COMPOSITE);
			}

			if (miniMode) {
				if (horizontal) {
					rect.y += delta / 2;
					rect.height -= delta;
				} else {
					rect.x += delta / 2;
					rect.width -= delta;
				}

				g2d.setColor(VALUE_MINI_COLOR_OUTER);
				g2d.drawRoundRect(rect.x, rect.y, rect.width - 1, rect.height - 1, 2, 2);
				g2d.setColor(VALUE_MINI_BACKGROUND);
				g2d.fillRect(rect.x + 1, rect.y + 1, rect.width - 2, rect.height - 2);
			} else {
				Image image;
				Insets imageInsets = new Insets(2, 2, 2, 2);

				if (horizontal) {
					image = BG_IMAGE_H;
					rect.y += delta / 2;
					rect.height -= delta;
				} else {
					image = BG_IMAGE_V;
					rect.x += delta / 2;
					rect.width -= delta;
				}

				OnlyUIUtil.paintImage(g2d, image, imageInsets, rect, slider);
			}

			paintValue(g2d, rect, miniMode);
			g2d.setComposite(oldComposite);
		} else {
			super.paintTrack(g);
		}
	}

	private void paintValue(Graphics2D g2d, Rectangle rect, boolean miniMode) {
		int value = slider.getValue();
		int max = slider.getMaximum();
		int min = slider.getMinimum();
		float percent = (value - min) / (float) (max - min);
		boolean leftToRight = slider.getComponentOrientation().isLeftToRight();
		boolean inverted = slider.getInverted();
		boolean horizontal = slider.getOrientation() == JSlider.HORIZONTAL;
		Rectangle paintRect = new Rectangle();
		Paint oldPaint = g2d.getPaint();
		Paint paint1 = null;
		Paint paint2 = null;

		if (horizontal) {
			// // Left to Right
			// if ((!leftToRight && inverted) || (leftToRight && !inverted)) {
			// paintRect.setBounds(1, 1, (int) Math.round(rect.width * percent)
			// - 2, rect.height - 2);
			// paint1 = new GradientPaint(paintRect.x, paintRect.y, miniMode ?
			// VALUE_MINI_COLOR_INNER_1 : VALUE_COLOR_OUTER_1, paintRect.x +
			// paintRect.width - 1, paintRect.y, miniMode ?
			// VALUE_MINI_COLOR_INNER_2 : VALUE_COLOR_OUTER_2);
			// paint2 = miniMode ? null : new GradientPaint(paintRect.x + 1,
			// paintRect.y + 1, VALUE_COLOR_INNER_1, paintRect.x +
			// paintRect.width - 2, paintRect.y + 1, VALUE_COLOR_INNER_2);
			// }
			// // Right to Left
			// else if ((!leftToRight && !inverted) || (leftToRight &&
			// inverted)) {
			// int x = (int) Math.round(1 + (1 - percent) * rect.width);
			// paintRect.setBounds(x, 1, (int) Math.round(rect.width * percent)
			// - 2, rect.height - 2);
			// paint1 = new GradientPaint(paintRect.x, paintRect.y, miniMode ?
			// VALUE_MINI_COLOR_INNER_2 : VALUE_COLOR_OUTER_2, paintRect.x +
			// paintRect.width - 1, paintRect.y, miniMode ?
			// VALUE_MINI_COLOR_INNER_1 : VALUE_COLOR_OUTER_1);
			// paint2 = miniMode ? null : new GradientPaint(paintRect.x + 1,
			// paintRect.y + 1, VALUE_COLOR_INNER_2, paintRect.x +
			// paintRect.width - 2, paintRect.y + 1, VALUE_COLOR_INNER_1);
			// }
			// Left to Right
			if ((!leftToRight && inverted) || (leftToRight && !inverted)) {
				paintRect.setBounds(0, 0, (int) Math.round(rect.width * percent), rect.height);
				paint1 = new GradientPaint(paintRect.x, paintRect.y, miniMode ? VALUE_MINI_COLOR_INNER_1 : VALUE_COLOR_OUTER_1, paintRect.x + paintRect.width - 1, paintRect.y, miniMode ? VALUE_MINI_COLOR_INNER_2 : VALUE_COLOR_OUTER_2);
				paint2 = miniMode ? null : new GradientPaint(paintRect.x + 1, paintRect.y + 1, VALUE_COLOR_INNER_1, paintRect.x + paintRect.width - 2, paintRect.y + 1, VALUE_COLOR_INNER_2);
			}
			// Right to Left
			else if ((!leftToRight && !inverted) || (leftToRight && inverted)) {
				int x = (int) Math.round(1+(1 - percent) * rect.width);
				paintRect.setBounds(x, 0, (int) Math.round(rect.width * percent), rect.height );
				paint1 = new GradientPaint(paintRect.x, paintRect.y, miniMode ? VALUE_MINI_COLOR_INNER_2 : VALUE_COLOR_OUTER_2, paintRect.x + paintRect.width - 1, paintRect.y, miniMode ? VALUE_MINI_COLOR_INNER_1 : VALUE_COLOR_OUTER_1);
				paint2 = miniMode ? null : new GradientPaint(paintRect.x + 1, paintRect.y + 1, VALUE_COLOR_INNER_2, paintRect.x + paintRect.width - 2, paintRect.y + 1, VALUE_COLOR_INNER_1);
			}
		} else {
			// if (inverted) {
			// paintRect.setBounds(1, 1, rect.width - 2, (int)
			// Math.round(rect.height * percent) - 2);
			// paint1 = new GradientPaint(paintRect.x, paintRect.y, miniMode ?
			// VALUE_MINI_COLOR_INNER_1 : VALUE_COLOR_OUTER_1, paintRect.x,
			// paintRect.y + paintRect.height - 1, miniMode ?
			// VALUE_MINI_COLOR_INNER_2 : VALUE_COLOR_OUTER_2);
			// paint2 = miniMode ? null : new GradientPaint(paintRect.x + 1,
			// paintRect.y + 1, VALUE_COLOR_INNER_1, paintRect.x + 1,
			// paintRect.y + paintRect.height - 2, VALUE_COLOR_INNER_2);
			// } else {
			// int y = (int) Math.round(1 + (1 - percent) * rect.height);
			// paintRect.setBounds(1, y, rect.width - 2, (int)
			// Math.round(rect.height * percent) - 2);
			// paint1 = new GradientPaint(paintRect.x, paintRect.y, miniMode ?
			// VALUE_MINI_COLOR_INNER_2 : VALUE_COLOR_OUTER_2, paintRect.x,
			// paintRect.y + paintRect.height - 1, miniMode ?
			// VALUE_MINI_COLOR_INNER_1 : VALUE_COLOR_OUTER_1);
			// paint2 = miniMode ? null : new GradientPaint(paintRect.x + 1,
			// paintRect.y + 1, VALUE_COLOR_INNER_2, paintRect.x + 1,
			// paintRect.y + paintRect.height - 2, VALUE_COLOR_INNER_1);
			// }
			if (inverted) {
				paintRect.setBounds(0, 0, rect.width, (int) Math.round(rect.height * percent));
				paint1 = new GradientPaint(paintRect.x, paintRect.y, miniMode ? VALUE_MINI_COLOR_INNER_1 : VALUE_COLOR_OUTER_1, paintRect.x, paintRect.y + paintRect.height - 1, miniMode ? VALUE_MINI_COLOR_INNER_2 : VALUE_COLOR_OUTER_2);
				paint2 = miniMode ? null : new GradientPaint(paintRect.x + 1, paintRect.y + 1, VALUE_COLOR_INNER_1, paintRect.x + 1, paintRect.y + paintRect.height - 2, VALUE_COLOR_INNER_2);
			} else {
				int y = (int) Math.round(1 + (1 - percent) * rect.height);
				paintRect.setBounds(0, y, rect.width , (int) Math.round(rect.height * percent));
				paint1 = new GradientPaint(paintRect.x, paintRect.y, miniMode ? VALUE_MINI_COLOR_INNER_2 : VALUE_COLOR_OUTER_2, paintRect.x, paintRect.y + paintRect.height - 1, miniMode ? VALUE_MINI_COLOR_INNER_1 : VALUE_COLOR_OUTER_1);
				paint2 = miniMode ? null : new GradientPaint(paintRect.x + 1, paintRect.y + 1, VALUE_COLOR_INNER_2, paintRect.x + 1, paintRect.y + paintRect.height - 2, VALUE_COLOR_INNER_1);
			}
		}

		g2d.translate(rect.x, rect.y);
		g2d.setPaint(paint1);
		g2d.fillRect(paintRect.x, paintRect.y, paintRect.width, paintRect.height);

		if (!miniMode) {
			g2d.setPaint(paint2);
			g2d.fillRect(paintRect.x + 1, paintRect.y + 1, paintRect.width - 2, paintRect.height - 2);
		}

		g2d.setPaint(oldPaint);
		g2d.translate(-rect.x, -rect.y);
	}

	protected void paintMinorTickForHorizSlider(Graphics g, Rectangle tickBounds, int x) {
		Color oldColor = g.getColor();
		setTickColor(g, false);
		super.paintMinorTickForHorizSlider(g, tickBounds, x);
		g.setColor(oldColor);
	}

	protected void paintMajorTickForHorizSlider(Graphics g, Rectangle tickBounds, int x) {
		Color oldColor = g.getColor();
		setTickColor(g, true);
		super.paintMajorTickForHorizSlider(g, tickBounds, x);
		g.setColor(oldColor);
	}

	protected void paintMinorTickForVertSlider(Graphics g, Rectangle tickBounds, int y) {
		Color oldColor = g.getColor();
		setTickColor(g, false);
		super.paintMinorTickForVertSlider(g, tickBounds, y);
		g.setColor(oldColor);
	}

	protected void paintMajorTickForVertSlider(Graphics g, Rectangle tickBounds, int y) {
		Color oldColor = g.getColor();
		setTickColor(g, true);
		super.paintMajorTickForVertSlider(g, tickBounds, y);
		g.setColor(oldColor);
	}

	private void setTickColor(Graphics g, boolean major) {
		if (slider instanceof OnlySlider) {
			if (slider.isEnabled()) {
				g.setColor(major ? ((OnlySlider) slider).getMajorTickColor() : ((OnlySlider) slider).getMinorTickColor());
			} else {
				g.setColor(UIBox.getColor(UIBox.key_color_slider_disabled_tick));
			}
		}
	}

	public void paintThumb(Graphics g) {
		if (slider instanceof OnlySlider) {
			boolean miniMode = ((OnlySlider) slider).isMiniMode();
			Graphics2D g2d = (Graphics2D) g;
			Composite oldComposite = g2d.getComposite();
			Icon icon = null;

			if (!slider.isEnabled()) {
				g2d.setComposite(DISABLED_COMPOSITE);
			}

			g2d.translate(thumbRect.x, thumbRect.y);

			if (slider.getOrientation() == JSlider.HORIZONTAL) {
				icon = miniMode ? THUMB_MINI_ICON_H : THUMB_ICON_H;
			} else {
				icon = miniMode ? THUMB_MINI_ICON_V : THUMB_ICON_V;
			}

			if (icon != null) {
				icon.paintIcon(slider, g2d, 0, 0);
			}

			g2d.translate(-thumbRect.x, -thumbRect.y);
			g2d.setComposite(oldComposite);
		} else {
			super.paintThumb(g);
		}
	}

	protected Dimension getThumbSize() {
		if (slider instanceof OnlySlider) {
			boolean miniMode = ((OnlySlider) slider).isMiniMode();
			Icon icon = null;

			if (slider.getOrientation() == JSlider.HORIZONTAL) {
				icon = miniMode ? THUMB_MINI_ICON_H : THUMB_ICON_H;
			} else {
				icon = miniMode ? THUMB_MINI_ICON_V : THUMB_ICON_V;
			}

			return icon == null ? null : new Dimension(icon.getIconWidth(), icon.getIconHeight());
		} else {
			return super.getThumbSize();
		}
	}

	public void calculateGeometry() {
		super.calculateGeometry();
	}

	protected void installDefaults(JSlider slider) {
		focusInsets = new InsetsUIResource(0, 0, 0, 0);
	}
}