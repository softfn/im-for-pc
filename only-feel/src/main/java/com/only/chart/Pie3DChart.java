package com.only.chart;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map;
import java.util.TreeMap;

import sun.swing.SwingUtilities2;
import com.only.util.UI3DUtil;
import com.only.util.OnlyUIUtil;

public class Pie3DChart extends Chart3D {

    private static final long serialVersionUID = 3585557597017329246L;
    private int thickness;
    private NumberFormat percentFormat, dataFormat;
    private Map<String, Pie3D> chartMap;

    public Pie3DChart(Map<String, Number> dataMap, int thickness, String title) {
        if (thickness <= 0) {
            throw new IllegalArgumentException("thickness must be greater than 1");
        }

        this.thickness = thickness;
        this.percentFormat = new DecimalFormat("#0.00%");
        this.chartMap = new TreeMap<String, Pie3D>();
        init(dataMap, chartMap, title);
    }

    @Override
    protected synchronized void buildChartMap() {
        double count = 0;
        chartMap.clear();

        for (String name : dataMap.keySet()) {
            count += dataMap.get(name).doubleValue();
        }

        if (count <= 0) {
            return;
        }

        Color[] colors = OnlyUIUtil.createRandomColors(dataMap.size());
        Pie3D pie;
        Number rawData;
        double startAngle = 0.0;
        double extentAngle = 0.0;
        double per = 0;
        int index = 0;

        for (String name : dataMap.keySet()) {
            per = (rawData = dataMap.get(name)).doubleValue() / count;
            extentAngle = per * 360;
            pie = new Pie3D(name, startAngle, extentAngle);
            pie.setPieColor(colors[index]);
            pie.setDarkerColor(OnlyUIUtil.createDarkerColor(colors[index], 0.6f));
            pie.setPercent(per);
            pie.setText(percentFormat == null ? String.valueOf(per) : percentFormat.format(per));
            pie.setToolTipText(name + ": " + (dataFormat == null ? rawData : dataFormat.format(rawData)) + "(" + pie.getText() + ")");
            pie.setRawData(rawData);
            chartMap.put(name, pie);
            startAngle += extentAngle;
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
        int imageWidth = width;
        int imageHeight = height - legendBounds.height;
        int chartWidth = imageWidth - SELECTED_GAP * 2;
        int chartHeight = imageHeight - thickness - SELECTED_GAP * 2 - titleHeight;
        boolean paintChart = chartWidth > 0 && chartHeight > 0;
        BufferedImage image = paintChart ? OnlyUIUtil.getGraphicsConfiguration(this).createCompatibleImage(imageWidth,
                imageHeight, Transparency.TRANSLUCENT) : null;
        Ellipse2D ellipse = image == null ? null : new Ellipse2D.Double(SELECTED_GAP, SELECTED_GAP + titleHeight, chartWidth, chartHeight);
        Area ellipseArea = image == null ? null : new Area(ellipse);
        Graphics2D imageG2d = image == null ? null : image.createGraphics();
        double startAngle = 90;
        double extentAngle = 0;
        boolean compositeChanged = false;
        int ascent = fm.getAscent();
        int legendX, legendY;
        Arc2D arc = null;
        Area area;
        Rectangle bounds;

        if (paintChart) {
            if (chartWidth > chartHeight) {
                arc = new Arc2D.Double(SELECTED_GAP, SELECTED_GAP + titleHeight + (chartHeight - chartWidth) / 2,
                        chartWidth, chartWidth, 0, 0, Arc2D.PIE);
            } else if (chartWidth < chartHeight) {
                arc = new Arc2D.Double(SELECTED_GAP + (chartWidth - chartHeight) / 2, SELECTED_GAP + titleHeight,
                        chartHeight, chartHeight, 0, 0, Arc2D.PIE);
            } else {
                arc = new Arc2D.Double(SELECTED_GAP, SELECTED_GAP + titleHeight, chartWidth, chartHeight, 0, 0, Arc2D.PIE);
            }

            imageG2d.setFont(CHART_FONT);
        }

        for (Pie3D pie : chartMap.values()) {
            bounds = pie.getLegendBounds();
            legendX = bounds.x + legendBounds.x;
            legendY = bounds.y + legendBounds.y;
            pie.setLegendLocation(legendX, legendY);
            bounds = pie.getLegendBounds();

            if (pie.isSelected()) {
                currentChart = pie;
                g2d.setColor(LEGEND_SELECTED_COLOR);
                g2d.fill(bounds);
            }

            g2d.setColor(pie.getDarkerColor());
            g2d.fillRect(bounds.x + 1, bounds.y + (LEGEND_ROW_HEIGHT - LEGEND_LOGO_SIZE) / 2, LEGEND_LOGO_SIZE, LEGEND_LOGO_SIZE);
            g2d.setColor(pie.getPieColor());
            g2d.fillRect(bounds.x + 1, bounds.y + (LEGEND_ROW_HEIGHT - LEGEND_LOGO_SIZE) / 2, LEGEND_LOGO_SIZE - 1, LEGEND_LOGO_SIZE - 1);
            g2d.setColor(pie.isSelected() ? Color.WHITE : this.getForeground());
            SwingUtilities2.drawString(this, g2d, pie.getName(), bounds.x + LEGEND_LOGO_SIZE + 2, bounds.y + ascent);

            if (paintChart) {
                if (startAngle > 270 && !compositeChanged) {
                    imageG2d.setComposite(AlphaComposite.DstAtop);
                    compositeChanged = true;
                }

                arc.setAngleStart(startAngle);
                arc.setAngleExtent(extentAngle = pie.getExtentAngle());
                pie.setArea(area = new Area(arc));
                area.intersect(ellipseArea);
                UI3DUtil.paint3DPie(this, imageG2d, area, startAngle, extentAngle, 0, thickness, ellipse.getBounds2D(), pie.getPieColor(),
                        pie.getDarkerColor(), this.getForeground(), pie.getText(), pie.getTextPosition(), pie.isSelected(), false);
                startAngle += extentAngle;
            }
        }

        if (chartHeight > 0) {
            imageG2d.dispose();
            g2d.drawImage(image, 0, 0, this);
            g2d.setFont(CHART_FONT);
            g2d.setColor(this.getForeground());

            for (Pie3D pie : chartMap.values()) {
                SwingUtilities2.drawString(this, g2d, pie.getText(), pie.getTextPosition().x, pie.getTextPosition().y);
            }
        }
    }

    public int getThickness() {
        return thickness;
    }

    public NumberFormat getPercentFormat() {
        return percentFormat;
    }

    public void setPercentFormat(NumberFormat percentFormat) {
        this.percentFormat = percentFormat;

        for (Pie3D pie : chartMap.values()) {
            pie.setText(percentFormat == null ? String.valueOf(pie.getPercent()) : percentFormat.format(pie.getPercent()));
            pie.setToolTipText(pie.getName() + ": " + (dataFormat == null ? pie.getRawData() : dataFormat.format(pie.getRawData()))
                    + "(" + pie.getText() + ")");
        }

        this.repaint();
    }

    public NumberFormat getDataFormat() {
        return dataFormat;
    }

    public void setDataFormat(NumberFormat dataFormat) {
        this.dataFormat = dataFormat;

        for (Pie3D pie : chartMap.values()) {
            pie.setToolTipText(pie.getName() + ": " + (dataFormat == null ? pie.getRawData() : dataFormat.format(pie.getRawData()))
                    + "(" + pie.getText() + ")");
        }

        this.repaint();
    }
}