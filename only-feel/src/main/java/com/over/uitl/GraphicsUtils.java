/*
 * This file is part of WebLookAndFeel library.
 *
 * WebLookAndFeel library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * WebLookAndFeel library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with WebLookAndFeel library.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.over.uitl;

import java.awt.*;
import java.awt.geom.Area;
import java.util.HashMap;
import java.util.Map;

import com.over.global.StyleConstants;

/**
 * This class provides a set of utilities to work with Graphics2D.
 * 
 * @author Mikle Garin
 */

public class GraphicsUtils {
	/**
	 * Setting antialias on
	 */

	public static Object setupAntialias(final Graphics g) {
		return setupAntialias((Graphics2D) g, RenderingHints.VALUE_ANTIALIAS_ON);
	}

	public static Object setupAntialias(final Graphics2D g2d) {
		return setupAntialias(g2d, RenderingHints.VALUE_ANTIALIAS_ON);
	}

	public static Object disableAntialias(final Graphics g) {
		return setupAntialias((Graphics2D) g, RenderingHints.VALUE_ANTIALIAS_OFF);
	}

	public static Object disableAntialias(final Graphics2D g2d) {
		return setupAntialias(g2d, RenderingHints.VALUE_ANTIALIAS_OFF);
	}

	private static Object setupAntialias(final Graphics2D g2d, final Object aa) {
		final Object old = g2d.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, aa);
		return old;
	}

	public static void restoreAntialias(final Graphics g, final Object old) {
		restoreAntialias((Graphics2D) g, old);
	}

	public static void restoreAntialias(final Graphics2D g2d, final Object old) {
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, old);
	}

	/**
	 * Installing system text settings
	 */

	private static boolean systemTextHintsInitialized = false;
	private static Map<?, ?> systemTextHints = null;

	public static Map<?, ?> getSystemTextHints() {
		if (!systemTextHintsInitialized) {
			systemTextHints = (Map<?, ?>) Toolkit.getDefaultToolkit().getDesktopProperty("awt.font.desktophints");
			systemTextHintsInitialized = true;
		}
		return systemTextHints;
	}

	public static void setupSystemTextHints(final Graphics g) {
		setupSystemTextHints((Graphics2D) g);
	}

	public static void setupSystemTextHints(final Graphics2D g2d) {
		final Map<?, ?> systemTextHints = getSystemTextHints();
		if (systemTextHints != null) {
			g2d.addRenderingHints(systemTextHints);
		}
	}

	/**
	 * Setting AlphaComposite by taking old AlphaComposite settings into account
	 */

	public static Composite setupAlphaComposite(final Graphics2D g2d, final Float alpha) {
		return setupAlphaComposite(g2d, alpha, true);
	}

	public static Composite setupAlphaComposite(final Graphics2D g2d, final Float alpha, final boolean shouldSetup) {
		return setupAlphaComposite(g2d, g2d.getComposite(), alpha, shouldSetup);
	}

	public static Composite setupAlphaComposite(final Graphics2D g2d, final Composite composeWith, final Float alpha) {
		return setupAlphaComposite(g2d, composeWith, alpha, true);
	}

	public static Composite setupAlphaComposite(final Graphics2D g2d, final Composite composeWith, final Float alpha, final boolean shouldSetup) {
		final Composite comp = g2d.getComposite();
		if (!shouldSetup || alpha == null) {
			return comp;
		}

		// Determining old composite alpha
		float currentComposite = 1f;
		if (composeWith != null && composeWith instanceof AlphaComposite) {
			currentComposite = ((AlphaComposite) composeWith).getAlpha();
		}

		// Creating new composite
		final AlphaComposite newComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, currentComposite * alpha);
		g2d.setComposite(newComposite);

		return comp;
	}

	public static void restoreComposite(final Graphics2D g2d, final Composite composite) {
		g2d.setComposite(composite);
	}

	public static void restoreComposite(final Graphics2D g2d, final Composite composite, final boolean shouldRestore) {
		if (shouldRestore) {
			g2d.setComposite(composite);
		}
	}

	/**
	 * Setting new stroke
	 */

	public static Stroke setupStroke(final Graphics2D g2d, final Stroke stroke) {
		return setupStroke(g2d, stroke, true);
	}

	public static Stroke setupStroke(final Graphics2D g2d, final Stroke stroke, final boolean shouldSetup) {
		if (shouldSetup && stroke != null) {
			final Stroke old = g2d.getStroke();
			g2d.setStroke(stroke);
			return old;
		} else {
			return null;
		}
	}

	public static void restoreStroke(final Graphics2D g2d, final Stroke stroke) {
		restoreStroke(g2d, stroke, true);
	}

	public static void restoreStroke(final Graphics2D g2d, final Stroke stroke, final boolean shouldRestore) {
		if (shouldRestore && stroke != null) {
			g2d.setStroke(stroke);
		}
	}

	/**
	 * Setting image quality on
	 */

	public static Object setupImageQuality(final Graphics g) {
		return setupImageQuality((Graphics2D) g);
	}

	public static Object setupImageQuality(final Graphics2D g2d) {
		final Object old = g2d.getRenderingHint(RenderingHints.KEY_INTERPOLATION);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		return old;
	}

	public static void restoreImageQuality(final Graphics g, final Object old) {
		restoreImageQuality((Graphics2D) g, old);
	}

	public static void restoreImageQuality(final Graphics2D g2d, final Object old) {
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, old != null ? old : RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	}

	/**
	 * Setting font
	 */

	public static Font setupFont(final Graphics g, final Font font) {
		if (font != null) {
			final Font oldFont = g.getFont();
			g.setFont(font);
			return oldFont;
		} else {
			return null;
		}
	}

	public static void restoreFont(final Graphics g, final Font font) {
		if (font != null) {
			g.setFont(font);
		}
	}

	/**
	 * Setting clip Shape by taking old clip Shape into account
	 */

	public static Shape intersectClip(final Graphics2D g2d, final Shape clip) {
		return intersectClip(g2d, clip, true);
	}

	public static Shape intersectClip(final Graphics2D g2d, final Shape clip, final boolean shouldSetup) {
		if (shouldSetup && clip != null) {
			final Shape oldClip = g2d.getClip();

			// Optimized by Graphics2D clip intersection
			g2d.clip(clip);

			return oldClip;
		} else {
			return null;
		}
	}

	public static Shape subtractClip(final Graphics g, final Shape clip) {
		return subtractClip(g, clip, true);
	}

	public static Shape subtractClip(final Graphics g, final Shape clip, final boolean shouldSetup) {
		if (shouldSetup && clip != null) {
			final Shape oldClip = g.getClip();
			if (oldClip != null) {
				// Area-based substraction
				final Area finalClip = new Area(oldClip);
				finalClip.subtract(new Area(clip));
				g.setClip(finalClip);
			}
			return oldClip;
		} else {
			return null;
		}
	}

	public static void restoreClip(final Graphics g, final Shape clip) {
		restoreClip(g, clip, true);
	}

	public static void restoreClip(final Graphics g, final Shape clip, final boolean shouldRestore) {
		// todo Maybe let setup null clip? Might cause errors in case when
		// initial clip was null
		if (shouldRestore && clip != null) {
			g.setClip(clip);
		}
	}

	/**
	 * Strokes caching
	 */

	private static final Map<String, Stroke> cachedStrokes = new HashMap<String, Stroke>();

	public static Stroke getStroke(final int width) {
		return getStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	}

	public static Stroke getStroke(final int width, final int cap) {
		return getStroke(width, cap, BasicStroke.JOIN_ROUND);
	}

	public static Stroke getStroke(final int width, final int cap, final int join) {
		final String key = width + "," + cap + "," + join;
		Stroke stroke = cachedStrokes.get(key);
		if (stroke == null) {
			stroke = new BasicStroke(width, cap, join);
			cachedStrokes.put(key, stroke);
		}
		return stroke;
	}

	/**
	 * Draws web styled shade using specified shape
	 */

	public static void drawShade(final Graphics2D g2d, final Shape shape, final Color shadeColor, final int width) {
		drawShade(g2d, shape, StyleConstants.shadeType, shadeColor, width);
	}

	public static void drawShade(final Graphics2D g2d, final Shape shape, final ShadeType shadeType, final Color shadeColor, final int width) {
		drawShade(g2d, shape, shadeType, shadeColor, width, null, true);
	}

	public static void drawShade(final Graphics2D g2d, final Shape shape, final Color shadeColor, final int width, final Shape clip) {
		drawShade(g2d, shape, StyleConstants.shadeType, shadeColor, width, clip, true);
	}

	public static void drawShade(final Graphics2D g2d, final Shape shape, final ShadeType shadeType, final Color shadeColor, final int width, final Shape clip) {
		drawShade(g2d, shape, shadeType, shadeColor, width, clip, true);
	}

	public static void drawShade(final Graphics2D g2d, final Shape shape, final Color shadeColor, final int width, final boolean round) {
		drawShade(g2d, shape, StyleConstants.shadeType, shadeColor, width, null, round);
	}

	public static void drawShade(final Graphics2D g2d, final Shape shape, final ShadeType shadeType, final Color shadeColor, final int width, final boolean round) {
		drawShade(g2d, shape, shadeType, shadeColor, width, null, round);
	}

	public static void drawShade(final Graphics2D g2d, final Shape shape, final Color shadeColor, final int width, final Shape clip, final boolean round) {
		drawShade(g2d, shape, StyleConstants.shadeType, shadeColor, width, clip, round);
	}

	public static void drawShade(final Graphics2D g2d, final Shape shape, final ShadeType shadeType, final Color shadeColor, int width, final Shape clip, final boolean round) {
		// Ignoring shade with width less than 2
		if (width <= 1) {
			return;
		}

		// Applying clip
		final Shape oldClip = clip != null ? intersectClip(g2d, clip) : subtractClip(g2d, shape);

		// Saving composite
		final Composite oldComposite = g2d.getComposite();
		float currentComposite = 1f;
		if (oldComposite instanceof AlphaComposite) {
			currentComposite = ((AlphaComposite) oldComposite).getAlpha();
		}

		// Saving stroke
		final Stroke oldStroke = g2d.getStroke();

		// Drawing shade
		if (shadeColor != null) {
			g2d.setPaint(shadeColor);
		}
		if (shadeType.equals(ShadeType.simple)) {
			// Drawing simple shade
			if (StyleConstants.simpleShadeTransparency < 1f) {
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, StyleConstants.simpleShadeTransparency * currentComposite));
			}
			g2d.setStroke(getStroke(width * 2, round ? BasicStroke.CAP_ROUND : BasicStroke.CAP_BUTT));
			g2d.draw(shape);
		} else {
			// Drawing comples gradient shade
			width = width * 2;
			for (int i = width; i >= 2; i -= 2) {
				// float minTransp = 0.2f;
				// float maxTransp = 0.6f;
				// float opacity = minTransp + ( maxTransp - minTransp ) * ( 1 -
				// ( i - 2 ) / ( width - 2 ) );
				final float opacity = (float) (width - i) / (width - 1);
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity * currentComposite));
				g2d.setStroke(getStroke(i, round ? BasicStroke.CAP_ROUND : BasicStroke.CAP_BUTT));
				g2d.draw(shape);
			}
		}

		// Restoring initial grphics settings
		restoreStroke(g2d, oldStroke);
		restoreComposite(g2d, oldComposite);
		restoreClip(g2d, oldClip);
	}
}