package com.only.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.io.Serializable;
/**
 * 
 * @date 2013年12月28日 上午11:04:29
 * version 0.0.1
 */
public class TictactoemidletLayout implements LayoutManager2, Serializable {

    private static final long serialVersionUID = 8146653535954456852L;
    public static final String NORTH = "North";
    public static final String SOUTH = "South";
    public static final String EAST = "East";
    public static final String WEST = "West";
    public static final String NORTH_WEST = "North-West";
    public static final String NORTH_EAST = "North-East";
    public static final String SOUTH_WEST = "South-West";
    public static final String SOUTH_EAST = "South-East";
    public static final String CENTER = "Center";
    private Component north;
    private Component west;
    private Component east;
    private Component south;
    private Component northWest;
    private Component northEast;
    private Component southWest;
    private Component southEast;
    private Component center;
    private int hgap;
    private int vgap;

    public TictactoemidletLayout() {
        this(0, 0);
    }

    public TictactoemidletLayout(int hgap, int vgap) {
        this.hgap = hgap;
        this.vgap = vgap;
    }

    public int getHgap() {
        return hgap;
    }

    public void setHgap(int hgap) {
        this.hgap = hgap;
    }

    public int getVgap() {
        return vgap;
    }

    public void setVgap(int vgap) {
        this.vgap = vgap;
    }

    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        synchronized (comp.getTreeLock()) {
            if ((constraints == null) || (constraints instanceof String)) {
                addLayoutComponent((String) constraints, comp);
            } else {
                throw new IllegalArgumentException("cannot add to layout: constraint must be a string (or null)");
            }
        }
    }

    @Deprecated
    @Override
    public void addLayoutComponent(String name, Component comp) {
        synchronized (comp.getTreeLock()) {
            if (name == null) {
                name = CENTER;
            }

            if (CENTER.equals(name)) {
                center = comp;
            } else if (NORTH.equals(name)) {
                north = comp;
            } else if (SOUTH.equals(name)) {
                south = comp;
            } else if (EAST.equals(name)) {
                east = comp;
            } else if (WEST.equals(name)) {
                west = comp;
            } else if (NORTH_WEST.equals(name)) {
                northWest = comp;
            } else if (NORTH_EAST.equals(name)) {
                northEast = comp;
            } else if (SOUTH_WEST.equals(name)) {
                southWest = comp;
            } else if (SOUTH_EAST.equals(name)) {
                southEast = comp;
            } else {
                throw new IllegalArgumentException("cannot add to layout: unknown constraint: " + name);
            }
        }
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        synchronized (comp.getTreeLock()) {
            if (comp == center) {
                center = null;
            } else if (comp == north) {
                north = null;
            } else if (comp == south) {
                south = null;
            } else if (comp == east) {
                east = null;
            } else if (comp == west) {
                west = null;
            } else if (comp == northWest) {
                northWest = null;
            } else if (comp == northEast) {
                northEast = null;
            } else if (comp == southWest) {
                southWest = null;
            } else if (comp == southEast) {
                southEast = null;
            }
        }
    }

    @Override
    public Dimension minimumLayoutSize(Container target) {
        synchronized (target.getTreeLock()) {
            Dimension dim = new Dimension(0, 0);
            Component c;

            if ((c = getChild(EAST)) != null) {
                Dimension d = c.getMinimumSize();
                dim.width += d.width + hgap;
                dim.height = Math.max(d.height, dim.height);
            }

            if ((c = getChild(WEST)) != null) {
                Dimension d = c.getMinimumSize();
                dim.width += d.width + hgap;
                dim.height = Math.max(d.height, dim.height);
            }

            if ((c = getChild(CENTER)) != null) {
                Dimension d = c.getMinimumSize();
                dim.width += d.width;
                dim.height = Math.max(d.height, dim.height);
            }

            if ((c = getChild(NORTH)) != null) {
                Dimension d = c.getMinimumSize();
                dim.width = Math.max(d.width, dim.width);
                dim.height += d.height + vgap;
            }

            if ((c = getChild(SOUTH)) != null) {
                Dimension d = c.getMinimumSize();
                dim.width = Math.max(d.width, dim.width);
                dim.height += d.height + vgap;
            }

            Insets insets = target.getInsets();
            dim.width += insets.left + insets.right;
            dim.height += insets.top + insets.bottom;

            return dim;
        }
    }

    @Override
    public Dimension preferredLayoutSize(Container target) {
        synchronized (target.getTreeLock()) {
            Dimension dim = new Dimension(0, 0);
            Component c;

            if ((c = getChild(EAST)) != null) {
                Dimension d = c.getPreferredSize();
                dim.width += d.width + hgap;
                dim.height = Math.max(d.height, dim.height);
            }

            if ((c = getChild(WEST)) != null) {
                Dimension d = c.getPreferredSize();
                dim.width += d.width + hgap;
                dim.height = Math.max(d.height, dim.height);
            }

            if ((c = getChild(CENTER)) != null) {
                Dimension d = c.getPreferredSize();
                dim.width += d.width;
                dim.height = Math.max(d.height, dim.height);
            }

            if ((c = getChild(NORTH)) != null) {
                Dimension d = c.getPreferredSize();
                dim.width = Math.max(d.width, dim.width);
                dim.height += d.height + vgap;
            }

            if ((c = getChild(SOUTH)) != null) {
                Dimension d = c.getPreferredSize();
                dim.width = Math.max(d.width, dim.width);
                dim.height += d.height + vgap;
            }

            Insets insets = target.getInsets();
            dim.width += insets.left + insets.right;
            dim.height += insets.top + insets.bottom;

            return dim;
        }
    }

    @Override
    public Dimension maximumLayoutSize(Container target) {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    @Override
    public float getLayoutAlignmentX(Container parent) {
        return 0.5f;
    }

    @Override
    public float getLayoutAlignmentY(Container parent) {
        return 0.5f;
    }

    @Override
    public void invalidateLayout(Container target) {
    }

    @Override
    public void layoutContainer(Container target) {
        synchronized (target.getTreeLock()) {
            Insets insets = target.getInsets();
            int top = insets.top;
            int bottom = target.getHeight() - insets.bottom;
            int left = insets.left;
            int right = target.getWidth() - insets.right;
            Component north = getChild(NORTH);
            Component south = getChild(SOUTH);
            Component east = getChild(EAST);
            Component west = getChild(WEST);
            Component center = getChild(CENTER);
            Component northWest = getChild(NORTH_WEST);
            Component northEast = getChild(NORTH_EAST);
            Component southWest = getChild(SOUTH_WEST);
            Component southEast = getChild(SOUTH_EAST);

            if (north != null) {
                top += north.getPreferredSize().height + vgap;
            }

            if (south != null) {
                bottom -= south.getPreferredSize().height + vgap;
            }

            if (east != null) {
                right -= east.getPreferredSize().width + hgap;
            }

            if (west != null) {
                left += west.getPreferredSize().width + hgap;
            }

            if (north != null) {
                north.setBounds(left, insets.top, right - left, north.getPreferredSize().height);
            }

            if (south != null) {
                south.setBounds(left, bottom + vgap, right - left, south.getPreferredSize().height);
            }

            if (west != null) {
                west.setBounds(insets.left, top, west.getPreferredSize().width, bottom - top);
            }

            if (east != null) {
                east.setBounds(right + hgap, top, east.getPreferredSize().width, bottom - top);
            }

            if (center != null) {
                center.setBounds(left, top, right - left, bottom - top);
            }

            if (northWest != null) {
                northWest.setBounds(insets.left, insets.top, west == null ? 0 : west.getWidth(), north == null ? 0 : north.getHeight());
            }

            if (northEast != null) {
                northEast.setBounds(right + hgap, insets.top, east == null ? 0 : east.getWidth(), north == null ? 0 : north.getHeight());
            }

            if (southWest != null) {
                southWest.setBounds(insets.left, bottom + vgap, west == null ? 0 : west.getWidth(), south == null ? 0 : south.getHeight());
            }

            if (southEast != null) {
                southEast.setBounds(right + hgap, bottom + vgap, east == null ? 0 : east.getWidth(), south == null ? 0 : south.getHeight());
            }
        }
    }

    private Component getChild(String key) {
        Component result = null;

        if ( NORTH.equals(key)) {
            result = north;
        } else if (SOUTH.equals(key)) {
            result = south;
        } else if (WEST.equals(key)) {
            result = west;
        } else if (EAST.equals(key)) {
            result = east;
        } else if (CENTER.equals(key)) {
            result = center;
        } else if (NORTH_WEST.equals(key)) {
            result = northWest;
        } else if (NORTH_EAST.equals(key)) {
            result = northEast;
        } else if (SOUTH_WEST.equals(key)) {
            result = southWest;
        } else if (SOUTH_EAST.equals(key)) {
            result = southEast;
        }

        if (result != null && !result.isVisible()) {
            result = null;
        }

        return result;
    }

    @Override
    public String toString() {
        return getClass().getName() + "[hgap=" + hgap + ",vgap=" + vgap + "]";
    }
}