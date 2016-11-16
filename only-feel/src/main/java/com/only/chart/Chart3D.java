package com.only.chart;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.border.EmptyBorder;

import sun.swing.SwingUtilities2;
import com.only.util.OnlyUIUtil;

public abstract class Chart3D extends JComponent {

    private static final long serialVersionUID = 5990850212081253215L;
    protected static final Color LEGEND_SELECTED_COLOR = new Color(130, 130, 130);
    protected static final Font LEGEND_FONT = OnlyUIUtil.getDefaultFont();
    protected static final Font CHART_FONT = new Font("Arial", Font.BOLD, 12);
    protected static final Font TITLE_FONT = new Font(OnlyUIUtil.getDefaultFont().getName(), Font.BOLD, 14);
    protected static final int LEGEND_ROW_HEIGHT = 16;
    protected static final int CHART_GAP = 4;
    protected static final int LEGEND_LOGO_SIZE = 8;
    protected static final int LEGEND_GAP_X = 1;
    protected static final int SELECTED_GAP = 4;
    protected Map<String, Number> dataMap;
    protected Rectangle legendBounds;
    protected String title;
    protected Chart3DBean currentChart;
    private Map<String, ? extends Chart3DBean> chartMap;

    protected void init(Map<String, Number> dataMap, Map<String, ? extends Chart3DBean> chartMap, String title) {
        this.title = title;
        this.chartMap = chartMap;
        this.dataMap = new HashMap<String, Number>();
        this.legendBounds = new Rectangle();
        putClientProperty(SwingUtilities2.AA_TEXT_PROPERTY_KEY, OnlyUIUtil.COMMON_AATEXT_INFO);
        setBorder(new EmptyBorder(0, 0, 0, 0));
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);
        setOpaque(true);
        setDatas(dataMap);
        addMouseListener(new MouseHandle());
        ToolTipManager.sharedInstance().registerComponent(this);
    }

    protected void paintBackground(Graphics2D g) {
        if (this.isOpaque()) {
            g.setColor(this.getBackground());
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
        }
    }

    protected int paintTitle(Graphics2D g, int width) {
        if (title == null || title.trim().isEmpty()) {
            return 0;
        }

        g.setFont(TITLE_FONT);
        FontMetrics fm = g.getFontMetrics();
        int titleWidth = fm.stringWidth(title);
        int titleHeight = fm.getHeight() - fm.getLeading();
        int titleX = (width - titleWidth) / 2;
        int titleY = fm.getAscent();
        g.setColor(this.getForeground());
        SwingUtilities2.drawString(this, g, title, titleX, titleY);
        return titleHeight + CHART_GAP;
    }

    protected void calculateLegendBounds(FontMetrics fm, int width, int height) {
        int rowCount = 1;
        int x = 0;
        int maxRowWidth = 0;
        int textWidth;
        int legendWidth;

        for (Chart3DBean chart : chartMap.values()) {
            textWidth = fm.stringWidth(chart.getName());
            legendWidth = LEGEND_LOGO_SIZE + textWidth + 2;

            if (x > 0 && x + legendWidth > width) {
                maxRowWidth = Math.max(maxRowWidth, x - LEGEND_GAP_X);
                x = 0;
                rowCount++;
            }

            chart.setLegendBounds(x, (rowCount - 1) * LEGEND_ROW_HEIGHT, legendWidth, LEGEND_ROW_HEIGHT);
            x += legendWidth + LEGEND_GAP_X;
        }

        maxRowWidth = Math.max(maxRowWidth, x - LEGEND_GAP_X);
        int legendHeight = rowCount * LEGEND_ROW_HEIGHT + CHART_GAP;
        int deltaX = (width - maxRowWidth) / 2;
        int deltaY = height - rowCount * LEGEND_ROW_HEIGHT;
        legendBounds.setBounds(deltaX, deltaY, maxRowWidth, legendHeight);
    }

    @Override
    public String getToolTipText(MouseEvent event) {
        Point point = event.getPoint();

        for (Chart3DBean chart : chartMap.values()) {
            if (chart.getArea().contains(point) || chart.getLegendBounds().contains(point)) {
                return chart.getToolTipText();
            }
        }

        return null;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        this.repaint();
    }

    public Map<String, Number> getDatas() {
        return new HashMap<String, Number>(dataMap);
    }

    public void setDatas(Map<String, Number> dataMap) {
        this.dataMap.clear();

        if (dataMap != null) {
            this.dataMap.putAll(dataMap);
        }

        buildChartMap();
    }

    public void setDatasAndRepaint(Map<String, Number> dataMap) {
        setDatas(dataMap);
        repaint();
    }

    public BufferedImage createImage() {
        BufferedImage image = OnlyUIUtil.getGraphicsConfiguration(this).createCompatibleImage(this.getWidth(),
                this.getHeight(), Transparency.TRANSLUCENT);
        Graphics2D g = image.createGraphics();
        this.paint(g);
        g.dispose();
        return image;
    }

    protected abstract void buildChartMap();

    private class MouseHandle extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            if (!SwingUtilities.isLeftMouseButton(e)) {
                return;
            }

            Point point = e.getPoint();
            Chart3DBean selectedChart = null;

            for (Chart3DBean chart : chartMap.values()) {
                if (chart.getArea().contains(point) || chart.getLegendBounds().contains(point)) {
                    selectedChart = chart;
                    break;
                }
            }

            if (selectedChart != currentChart) {
                if (selectedChart != null) {
                    selectedChart.setSelected(true);
                }

                if (currentChart != null) {
                    currentChart.setSelected(false);
                }

                repaint();
            }
        }
    }
}