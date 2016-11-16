package com.only.util;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import sun.swing.SwingUtilities2;

public class UI3DUtil {
	/**
	 * 通过将2D的形状在X和Y方向偏移而模拟出对应的3D形状
	 * 
	 * @param shape2d 2D形状
	 * @param offsetX X方向偏移量
	 * @param offsetY Y方向偏移量
	 * @param include2d 是否包含原来的2D区域，若为false，则从结果中移除包含在原2D区域内的部分
	 * @return 模拟的3D形状区域
	 */
	public static Area create3DArea(Shape shape2d, int offsetX, int offsetY, boolean include2d) {
		Area area2d = new Area(shape2d);
		Area area = include2d ? (Area) area2d.clone() : new Area();
		List<Path2D> paths = createIntersectionPaths(shape2d, offsetX, offsetY);

		for (Path2D path : paths) {
			area.add(new Area(path));
		}

		// 一个变态的问题，当include2d == true，在某些角度的时候最终的Area内会多出一条线
		// 在此重复添加area2d会大大减少出现这种问题的概率，至于这种方法是否会杜绝此问题的出现，目前未知
		if (include2d) {
			area.add(area2d);
		} else {
			area.subtract(area2d);
		}

		return area;
	}

	/**
	 * 通过将2D的形状在X和Y方向偏移而模拟出对应的3D形状
	 * 
	 * @param shape2d 2D形状
	 * @param offsetX X方向偏移量
	 * @param offsetY Y方向偏移量
	 * @param include2d 是否包含原来的2D区域，若为false，则结果中只包含原2D形状边缘偏移前和偏移后的交叉路径
	 * @return 模拟的3D形状路径
	 */
	public static Path2D create3DPath(Shape shape2d, int offsetX, int offsetY, boolean include2d) {
		Path2D path3d = include2d ? new Path2D.Double(shape2d) : new Path2D.Double();
		List<Path2D> paths = createIntersectionPaths(shape2d, offsetX, offsetY);

		for (Path2D path : paths) {
			path3d.append(path, false);
		}

		return path3d;
	}

	/**
	 * 在通过2D形状模拟3D效果时，创建2D形状偏移前和偏移后对应边缘的交叉路径
	 * 
	 * @param shape2d 2D形状
	 * @param offsetX X方向偏移量
	 * @param offsetY Y方向偏移量
	 * @return 2D形状偏移前和偏移后所有对应边缘的交叉路径的集合
	 */
	public static List<Path2D> createIntersectionPaths(Shape shape2d, int offsetX, int offsetY) {
		List<Path2D> paths = new ArrayList<Path2D>();
		PathIterator pi = new Path2D.Double(shape2d).getPathIterator(null);
		double coords[] = new double[6];
		Point2D start = new Point2D.Double();
		Point2D end = new Point2D.Double();
		Path2D subPath;

		while (!pi.isDone()) {
			int segment = pi.currentSegment(coords);

			switch (segment) {
			case PathIterator.SEG_MOVETO: {
				start.setLocation(coords[0], coords[1]);
				end.setLocation(coords[0], coords[1]);
				break;
			}
			case PathIterator.SEG_LINETO: {
				subPath = createIntersectionPath(end, coords, offsetX, offsetY, segment);
				paths.add(subPath);
				end.setLocation(coords[0], coords[1]);
				break;
			}
			case PathIterator.SEG_QUADTO: {
				subPath = createIntersectionPath(end, coords, offsetX, offsetY, segment);
				paths.add(subPath);
				end.setLocation(coords[2], coords[3]);
				break;
			}
			case PathIterator.SEG_CUBICTO: {
				subPath = createIntersectionPath(end, coords, offsetX, offsetY, segment);
				paths.add(subPath);
				end.setLocation(coords[4], coords[5]);
				break;
			}
			case PathIterator.SEG_CLOSE: {
				subPath = createIntersectionPath(start, new double[] { end.getX(), end.getY() }, offsetX, offsetY, segment);
				paths.add(subPath);
				break;
			}
			}

			pi.next();
		}

		return paths;
	}

	/**
	 * 在通过2D形状模拟3D效果时，创建指定路径段偏移前和偏移后的交叉路径
	 * 
	 * @param start 路径段的起始点
	 * @param coords 路径段的控制坐标
	 * @param offsetX X方向偏移量
	 * @param offsetY Y方向偏移量
	 * @param type 路径段的类型
	 * @return 路径段偏移前和偏移后的交叉路径
	 */
	private static Path2D createIntersectionPath(Point2D start, double[] coords, double offsetX, double offsetY, int type) {
		Path2D path = new Path2D.Double();
		path.moveTo(start.getX(), start.getY());

		switch (type) {
		case PathIterator.SEG_CLOSE:
		case PathIterator.SEG_LINETO: {
			path.lineTo(coords[0], coords[1]);
			path.lineTo(coords[0] + offsetX, coords[1] + offsetY);
			path.lineTo(start.getX() + offsetX, start.getY() + offsetY);
			break;
		}
		case PathIterator.SEG_QUADTO: {
			path.quadTo(coords[0], coords[1], coords[2], coords[3]);
			path.lineTo(coords[2] + offsetX, coords[3] + offsetY);
			path.quadTo(coords[0] + offsetX, coords[1] + offsetY, start.getX() + offsetX, start.getY() + offsetY);
			break;
		}
		case PathIterator.SEG_CUBICTO: {
			path.curveTo(coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);
			path.lineTo(coords[4] + offsetX, coords[5] + offsetY);
			path.curveTo(coords[2] + offsetX, coords[3] + offsetY, coords[0] + offsetX, coords[1] + offsetY, start.getX() + offsetX, start.getY() + offsetY);
			break;
		}
		}

		path.closePath();
		return path;
	}

	/**
	 * 绘制3D柱形
	 * 
	 * @param c 绘制字体时用于获取渲染参数的组件
	 * @param g 画笔
	 * @param rect 3D柱形前表面的矩形
	 * @param offsetX X方向偏移量
	 * @param offsetY Y方向偏移量
	 * @param topAlpha 顶部的透明度
	 * @param barColor 前表面的颜色
	 * @param darkerColor 右侧背光区域的颜色
	 * @param textColor 字体颜色
	 * @param text 文本信息
	 * @param selected 是否绘制选中状态
	 * @return 3D柱形的区域
	 */
	public static Area paint3DBar(JComponent c, Graphics2D g, Rectangle2D rect, int offsetX, int offsetY, float topAlpha, Color barColor, Color darkerColor, Color textColor, String text, boolean selected) {
		if (offsetX < 0 || offsetY < 0) {
			throw new IllegalArgumentException("offsetX and offsetY must be greater than or equals 0");
		}

		if (topAlpha < 0.0f || topAlpha >= 1.0f) {
			throw new IllegalArgumentException("topAlpha must be between 0.0f to 1.0f");
		}

		Area area = new Area(rect);
		final int x = Math.round((float) rect.getX());
		final int y = Math.round((float) rect.getY());
		final int w = Math.round((float) rect.getWidth());
		final int h = Math.round((float) rect.getHeight());
		final int pointCount = 4;
		final int[] topX = { x, x + offsetX, x + offsetX + w, x + w };
		final int[] topY = { y, y - offsetY, y - offsetY, y };
		final int[] rightX = { x + offsetX + w, x + w, x + w, x + w + offsetX };
		final int[] rightY = { y - offsetY, y, y + h, y + h - offsetY };
		Polygon top = new Polygon(topX, topY, pointCount);
		Polygon right = new Polygon(rightX, rightY, pointCount);
		Composite oldComposite = g.getComposite();
		Object oldAntialias = g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);

		if (selected) {
			g.translate(-3, 3);
		}

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(darkerColor);
		g.fill(right);
		g.setColor(barColor);
		g.fill(rect);
		g.setComposite(AlphaComposite.SrcOver.derive(topAlpha));
		g.fill(top);
		g.setComposite(oldComposite);

		if (selected) {
			Stroke oldStroke = g.getStroke();
			g.setColor(darkerColor);
			g.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
			g.draw(rect);
			g.draw(top);
			g.draw(right);
			g.setStroke(oldStroke);
		}

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAntialias);

		if (text != null && !text.trim().isEmpty()) {
			int textWidth = g.getFontMetrics().stringWidth(text);
			g.setColor(textColor);
			SwingUtilities2.drawString(c, g, text, x + (w + offsetX - textWidth) / 2, y - offsetY / 2);
		}

		if (selected) {
			g.translate(3, -3);
		}

		area.add(new Area(top));
		area.add(new Area(right));
		return area;
	}

	/**
	 * 绘制3D饼状图
	 * 
	 * @param c 绘制字体时用于获取渲染参数的组件
	 * @param g 画笔
	 * @param pie 基准2D饼形
	 * @param startAngle 开始角度
	 * @param extentAngle 跨越角度
	 * @param offsetX X方向偏移量
	 * @param offsetY Y方向偏移量
	 * @param bounds 基准饼形图所在椭圆外围矩形的位置和大小
	 * @param pieColor 饼形图的颜色
	 * @param darkerColor 3D饼形图中用于描绘厚度的颜色
	 * @param textColor 字体颜色
	 * @param text 文本信息
	 * @param textPosition 用于存储文本的绝对位置
	 * @param selected 是否绘制选中状态
	 * @param paintText 是否绘制文本信息
	 * @return 3D饼状区域
	 */
	public static Area paint3DPie(JComponent c, Graphics2D g, Shape pie, double startAngle, double extentAngle, int offsetX, int offsetY, Rectangle2D bounds, Color pieColor, Color darkerColor, Color textColor, String text, Point textPosition, boolean selected, boolean paintText) {
		Area pie3d = UI3DUtil.create3DArea(pie, offsetX, offsetY, true);
		Object oldAntialias = g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
		Composite oldComposite = g.getComposite();
		double centerDegree = (startAngle + extentAngle / 2.0) % 360.0;
		centerDegree = centerDegree < 0 ? 360 + centerDegree : centerDegree;
		double centerRadian = Math.toRadians(centerDegree);
		int deltaX = 0;
		int deltaY = 0;

		if (selected) {
			deltaX = Math.round((float) Math.cos(centerRadian) * 3);
			deltaY = Math.round((float) Math.sin(centerRadian) * 3);
			g.translate(deltaX, -deltaY);
		}

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(darkerColor);
		g.fill(pie3d);
		g.setComposite(AlphaComposite.SrcOver);
		g.setColor(pieColor);
		g.fill(pie);

		if (selected) {
			Stroke oldStroke = g.getStroke();
			g.setColor(darkerColor);
			g.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
			g.setComposite(oldComposite);
			g.draw(pie3d);
			g.setStroke(oldStroke);
			g.setComposite(AlphaComposite.SrcOver);
		}

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAntialias);

		if (text != null && !text.trim().isEmpty()) {
			FontMetrics fm = g.getFontMetrics();
			double factor = 0.618;
			double sizeFactor = 0.5 * factor;
			double locationFactor = 0.5 * (1 - factor);
			double x = bounds.getX() + bounds.getWidth() * locationFactor;
			double y = bounds.getY() + bounds.getHeight() * locationFactor;
			double a = bounds.getWidth() * sizeFactor;
			double b = bounds.getHeight() * sizeFactor;
			double r = (a * b) / (Math.sqrt(Math.pow(b * Math.cos(centerRadian), 2) + Math.pow(a * Math.sin(centerRadian), 2)));
			int textX = (int) (r * Math.cos(centerRadian) + a + x + deltaX - fm.stringWidth(text) / 2.0);
			int textY = (int) (-r * Math.sin(centerRadian) + b + y - deltaY + fm.getDescent() + fm.getLeading());
			textPosition.setLocation(textX, textY);

			if (paintText) {
				g.setColor(textColor);
				SwingUtilities2.drawString(c, g, text, textX, textY);
			}
		}

		if (selected) {
			g.translate(-deltaX, deltaY);
		}

		g.setComposite(oldComposite);
		return pie3d;
	}
}