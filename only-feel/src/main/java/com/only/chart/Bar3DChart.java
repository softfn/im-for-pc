package com.only.chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map;
import java.util.TreeMap;

import sun.swing.SwingUtilities2;
import com.only.util.UI3DUtil;
import com.only.util.OnlyUIUtil;

public class Bar3DChart extends Chart3D {

	private static final long serialVersionUID = -8775657595745367716L;
	private static final Font SCALE_FONT = new Font("Arial", Font.PLAIN, 11);
	private static final Color SCALE_COLOR = new Color(231, 231, 231);
	private static final Color SCALE_BORDER_COLOR = new Color(128, 128, 128);
	private static final int SCALE_GAP = 3;
	private static final int SCALE_WIDTH = 11;
	private int offsetX;
	private int offsetY;
	private int scaleYGap;
	private int step;
	private double max;
	private NumberFormat format;
	private Map<String, Bar3D> chartMap;

	public Bar3DChart(Map<String, Number> dataMap, int offsetX, int offsetY, String title) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.scaleYGap = 30;
		this.format = new DecimalFormat("#0.00");
		this.chartMap = new TreeMap<String, Bar3D>();
		init(dataMap, chartMap, title);
	}

	@Override
	protected synchronized void buildChartMap() {
		chartMap.clear();

		if (dataMap.isEmpty()) {
			return;
		}

		Color[] colors = OnlyUIUtil.createRandomColors(dataMap.size());
		int index = 0;
		max = 0;
		double value;
		Bar3D bar;

		for (String name : dataMap.keySet()) {
			value = dataMap.get(name).doubleValue();
			max = Math.max(max, value);
			bar = new Bar3D(name);
			bar.setBarColor(colors[index]);
			bar.setDarkerColor(OnlyUIUtil.createDarkerColor(colors[index], 0.6f));
			bar.setText(format == null ? String.valueOf(value) : format.format(value));
			bar.setToolTipText(name + ": " + bar.getText());
			bar.setData(value);
			bar.setOffsetX(offsetX);
			bar.setOffsetY(offsetY);
			bar.setTopAlpha(0.4f);
			chartMap.put(name, bar);
			index++;
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		currentChart = null;
		int width = this.getWidth();
		int height = this.getHeight();
		paintBackground(g2d);
		int titleHeight = paintTitle(g2d, width);

		if (chartMap.isEmpty()) {
			return;
		}

		g2d.setFont(LEGEND_FONT);
		FontMetrics fm = g2d.getFontMetrics();
		calculateLegendBounds(fm, width, height);
		int leftGap = paintScale(g2d, width, height, titleHeight);
		boolean paintChart = leftGap > 0;
		int ascent = fm.getAscent();
		int legendX, legendY;
		Rectangle bounds;
		int x = leftGap - offsetX;
		final int y = titleHeight + SCALE_WIDTH;
		int chartWidth = width - leftGap - SCALE_GAP - SCALE_WIDTH;
		int chartHeight = height - titleHeight - SCALE_WIDTH - legendBounds.height - offsetY / 2;
		int thickness = chartWidth / (chartMap.size() * 2);
		double startY;
		double rectHeight;
		Rectangle2D rect;
		Area area;

		for (Bar3D bar : chartMap.values()) {
			bounds = bar.getLegendBounds();
			legendX = bounds.x + legendBounds.x;
			legendY = bounds.y + legendBounds.y;
			bar.setLegendLocation(legendX, legendY);
			bounds = bar.getLegendBounds();

			if (bar.isSelected()) {
				currentChart = bar;
				g2d.setColor(LEGEND_SELECTED_COLOR);
				g2d.fill(bounds);
			}

			g2d.setColor(bar.getDarkerColor());
			g2d.fillRect(bounds.x + 1, bounds.y + (LEGEND_ROW_HEIGHT - LEGEND_LOGO_SIZE) / 2, LEGEND_LOGO_SIZE, LEGEND_LOGO_SIZE);
			g2d.setColor(bar.getBarColor());
			g2d.fillRect(bounds.x + 1, bounds.y + (LEGEND_ROW_HEIGHT - LEGEND_LOGO_SIZE) / 2, LEGEND_LOGO_SIZE - 1, LEGEND_LOGO_SIZE - 1);
			g2d.setColor(bar.isSelected() ? Color.WHITE : this.getForeground());
			g2d.setFont(LEGEND_FONT);
			SwingUtilities2.drawString(this, g2d, bar.getName(), bounds.x + LEGEND_LOGO_SIZE + 2, bounds.y + ascent);

			if (paintChart) {
				g2d.setFont(CHART_FONT);
				x += thickness;
				rectHeight = bar.getData().doubleValue() * (scaleYGap / (double) step);
				startY = y + (chartHeight - rectHeight);
				rect = new Rectangle2D.Double(x, startY, thickness, rectHeight);
				area = UI3DUtil.paint3DBar(this, g2d, rect, offsetX, offsetY, bar.getTopAlpha(), bar.getBarColor(), bar.getDarkerColor(), this.getForeground(), bar.getText(), bar.isSelected());
				bar.setArea(area);
				x += thickness;
			}
		}
	}

	protected int paintScale(Graphics2D g, int width, int height, int titleHeight) {
		g.setFont(SCALE_FONT);
		FontMetrics fm = g.getFontMetrics();
		int maxScaleWidth = fm.stringWidth((int) max + "0");
		int leftGap = maxScaleWidth + SCALE_GAP + SCALE_WIDTH;
		int chartWidth = width - leftGap - SCALE_GAP;
		int chartHeight = height - titleHeight - legendBounds.height - offsetY / 2;

		if (chartWidth <= SCALE_WIDTH || chartHeight <= SCALE_WIDTH) {
			return 0;
		}

		Object oldAntialias = g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
		chartWidth += SCALE_WIDTH;
		int deltaX = maxScaleWidth + SCALE_GAP;
		final int pointCount = 4;
		final int[] leftX = { SCALE_WIDTH - 1, 0, 0, SCALE_WIDTH - 1 };
		final int[] leftY = { 1, SCALE_WIDTH, chartHeight - 1, chartHeight - SCALE_WIDTH };
		final int[] bottomX = { 0, SCALE_WIDTH - 1, chartWidth - 1, chartWidth - SCALE_WIDTH };
		final int[] bottomY = { chartHeight - 1, chartHeight - SCALE_WIDTH, chartHeight - SCALE_WIDTH, chartHeight - 1 };
		Polygon left = new Polygon(leftX, leftY, pointCount);
		Polygon bottom = new Polygon(bottomX, bottomY, pointCount);
		g.translate(deltaX, titleHeight);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(SCALE_COLOR);
		g.fill(left);
		g.fill(bottom);
		g.setColor(SCALE_BORDER_COLOR);
		g.draw(left);
		g.draw(bottom);

		Stroke oldStroke = g.getStroke();
		Stroke stroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 2, new float[] { 2, 2 }, 3);
		double stepDouble = max / ((chartHeight - SCALE_WIDTH) / (double) scaleYGap);
		step = (int) (stepDouble / 10.0) * 10;

		while (max / step * scaleYGap > chartHeight - SCALE_WIDTH) {
			step += 5;
		}

		int y = chartHeight - 1;
		int scale = 0;
		int scaleWidth;
		String scaleStr;

		while (y >= SCALE_WIDTH) {
			if (y < chartHeight - 1) {
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g.setStroke(stroke);
				g.drawLine(0, y, SCALE_WIDTH - 1, y - SCALE_WIDTH + 1);
				g.drawLine(SCALE_WIDTH, y - SCALE_WIDTH + 1, chartWidth, y - SCALE_WIDTH + 1);
			}

			scaleWidth = fm.stringWidth(scaleStr = String.valueOf(scale));
			g.setStroke(oldStroke);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAntialias);
			SwingUtilities2.drawString(this, g, scaleStr, -scaleWidth - SCALE_GAP, y + fm.getDescent());
			scale += step;
			y -= scaleYGap;
		}

		g.translate(-deltaX, -titleHeight);
		return leftGap;
	}

	protected int paintTitle(Graphics2D g, int width) {
		int titleHeight = super.paintTitle(g, width);
		return titleHeight > 0 ? titleHeight : SCALE_GAP;
	}

	public int getOffsetX() {
		return offsetX;
	}

	public int getOffsetY() {
		return offsetY;
	}

	public NumberFormat getFormat() {
		return format;
	}

	public void setFormat(NumberFormat format) {
		this.format = format;

		for (Bar3D bar : chartMap.values()) {
			bar.setText(format == null ? String.valueOf(bar.getData()) : format.format(bar.getData()));
			bar.setToolTipText(bar.getName() + ": " + bar.getText());
		}

		this.repaint();
	}

	public int getScaleYGap() {
		return scaleYGap;
	}

	public void setScaleYGap(int scaleYGap) {
		if (scaleYGap < 10) {
			throw new IllegalArgumentException("scaleYGap must be greater than 10");
		}

		this.scaleYGap = scaleYGap;
		repaint();
	}
}