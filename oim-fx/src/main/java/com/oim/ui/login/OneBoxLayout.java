package com.oim.ui.login;

/**
 *
 * @author Hero
 */
import java.awt.AWTError;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.beans.ConstructorProperties;
import java.io.PrintStream;
import java.io.Serializable;

import javax.swing.SizeRequirements;

@SuppressWarnings("serial")
public class OneBoxLayout implements LayoutManager2, Serializable {

    public static final int X_AXIS = 0;
    public static final int Y_AXIS = 1;
    public static final int LINE_AXIS = 2;
    public static final int PAGE_AXIS = 3;
//    private int gap = 0;
    private int w = 0;
    private int h = 0;
    private int axis;
    private Container target;
    private transient SizeRequirements[] xChildren;
    private transient SizeRequirements[] yChildren;
    private transient SizeRequirements xTotal;
    private transient SizeRequirements yTotal;
    private transient PrintStream dbg;

    @ConstructorProperties({"target", "axis"})
    public OneBoxLayout(Container target, int axis) {
        if (axis != X_AXIS && axis != Y_AXIS && axis != LINE_AXIS && axis != PAGE_AXIS) {
            throw new AWTError("Invalid axis");
        }
        this.axis = axis;
        this.target = target;
    }

    @ConstructorProperties({"target", "axis"})
    public OneBoxLayout(Container target, int axis, int gap) {
        if (axis != X_AXIS && axis != Y_AXIS && axis != LINE_AXIS && axis != PAGE_AXIS) {
            throw new AWTError("Invalid axis");
        }
        this.axis = axis;
        this.target = target;
        if (gap > 0) {
//            this.gap = gap;
        }

    }

    public OneBoxLayout(Container target, int axis, int w, int h, int gap) {
        if (axis != X_AXIS && axis != Y_AXIS && axis != LINE_AXIS && axis != PAGE_AXIS) {
            throw new AWTError("Invalid axis");
        }
        this.axis = axis;
        this.target = target;
        if (gap > 0) {
//            this.gap = gap;
        }
        if (w > 0) {
            this.w = w;
        }
        if (h > 0) {
            this.h = h;
        }
    }

    OneBoxLayout(Container target, int axis, PrintStream dbg) {
        this(target, axis);
        this.dbg = dbg;
    }

    public final Container getTarget() {
        return this.target;
    }

    public final int getAxis() {
        return this.axis;
    }

    @Override
    public synchronized void invalidateLayout(Container target) {
        checkContainer(target);
        xChildren = null;
        yChildren = null;
        xTotal = null;
        yTotal = null;
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
        invalidateLayout(comp.getParent());
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        invalidateLayout(comp.getParent());
    }

    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        invalidateLayout(comp.getParent());
    }

    @Override
    public Dimension preferredLayoutSize(Container target) {
        Dimension size;
        synchronized (this) {
            checkContainer(target);
            checkRequests();
            size = new Dimension(xTotal.preferred, yTotal.preferred);
        }

        Insets insets = target.getInsets();
        size.width = (int) Math.min((long) size.width + (long) insets.left + (long) insets.right, Integer.MAX_VALUE);
        size.height = (int) Math.min((long) size.height + (long) insets.top + (long) insets.bottom, Integer.MAX_VALUE);
        return size;
    }

    @Override
    public Dimension minimumLayoutSize(Container target) {
        Dimension size;
        synchronized (this) {
            checkContainer(target);
            checkRequests();
            size = new Dimension(xTotal.minimum, yTotal.minimum);
        }

        Insets insets = target.getInsets();
        size.width = (int) Math.min((long) size.width + (long) insets.left + (long) insets.right, Integer.MAX_VALUE);
        size.height = (int) Math.min((long) size.height + (long) insets.top + (long) insets.bottom, Integer.MAX_VALUE);
        return size;
    }

    @Override
    public Dimension maximumLayoutSize(Container target) {
        Dimension size;
        synchronized (this) {
            checkContainer(target);
            checkRequests();
            size = new Dimension(xTotal.maximum, yTotal.maximum);
        }

        Insets insets = target.getInsets();
        size.width = (int) Math.min((long) size.width + (long) insets.left + (long) insets.right, Integer.MAX_VALUE);
        size.height = (int) Math.min((long) size.height + (long) insets.top + (long) insets.bottom, Integer.MAX_VALUE);
        return size;
    }

    @Override
    public synchronized float getLayoutAlignmentX(Container target) {
        checkContainer(target);
        checkRequests();
        return xTotal.alignment;
    }

    @Override
    public synchronized float getLayoutAlignmentY(Container target) {
        checkContainer(target);
        checkRequests();
        return yTotal.alignment;
    }

    @Override
    public void layoutContainer(Container target) {
        checkContainer(target);
        int nChildren = target.getComponentCount();
        int[] xOffsets = new int[nChildren];
        int[] xSpans = new int[nChildren];
        int[] yOffsets = new int[nChildren];
        int[] ySpans = new int[nChildren];

        Dimension alloc = target.getSize();
        Insets in = target.getInsets();
        alloc.width -= in.left + in.right;
        alloc.height -= in.top + in.bottom;

        // Resolve axis to an absolute value (either X_AXIS or Y_AXIS)
        ComponentOrientation o = target.getComponentOrientation();
        int absoluteAxis = resolveAxis(axis, o);
        boolean ltr = (absoluteAxis != axis) ? o.isLeftToRight() : true;

        // determine the child placements
        synchronized (this) {
            checkRequests();

            if (absoluteAxis == X_AXIS) {
                calculateTiledPositions(alloc.width, xTotal, xChildren, xOffsets, xSpans, ltr);
                calculateAlignedPositions(alloc.height, yTotal, yChildren, yOffsets, ySpans);
            } else {
                calculateAlignedPositions(alloc.width, xTotal, xChildren, xOffsets, xSpans, ltr);
                calculateTiledPositions(alloc.height, yTotal, yChildren, yOffsets, ySpans);
            }
        }

        // flush changes to the container
        for (int i = 0; i < nChildren; i++) {
            Component c = target.getComponent(i);
//            if (c instanceof JLabel) {
            c.setBounds(0, (int) Math.min((long) in.top + (long) yOffsets[i], Integer.MAX_VALUE), target.getWidth(), ySpans[i]);
//            } else {
//                c.setBounds(0, (int) Math.min((long) in.top + (long) yOffsets[i], Integer.MAX_VALUE), xSpans[i], ySpans[i]);
//            }
        }
        if (dbg != null) {
            for (int i = 0; i < nChildren; i++) {
                Component c = target.getComponent(i);
                dbg.println(c.toString());
                dbg.println("X: " + xChildren[i]);
                dbg.println("Y: " + yChildren[i]);
            }
        }
    }

    void checkContainer(Container target) {
        if (this.target != target) {
            throw new AWTError("BoxLayout can't be shared");
        }
    }

    public static void calculateTiledPositions(int allocated, SizeRequirements total, SizeRequirements[] children, int[] offsets, int[] spans) {
        calculateTiledPositions(allocated, total, children, offsets, spans, true);
    }

    void checkRequests() {
        if (xChildren == null || yChildren == null) {
            int n = target.getComponentCount();
            xChildren = new SizeRequirements[n];
            yChildren = new SizeRequirements[n];
            for (int i = 0; i < n; i++) {
                Component c = target.getComponent(i);
                if (!c.isVisible()) {
                    xChildren[i] = new SizeRequirements(0, 0, 0, c.getAlignmentX());
                    yChildren[i] = new SizeRequirements(0, 0, 0, c.getAlignmentY());
                    continue;
                }
                Dimension min = c.getMinimumSize();
                Dimension typ = c.getPreferredSize();
                Dimension max = c.getMaximumSize();
                xChildren[i] = new SizeRequirements(min.width + w, typ.width + w, max.width + w, c.getAlignmentX());
                yChildren[i] = new SizeRequirements(min.height + h, typ.height + h, max.height + h, c.getAlignmentY());
            }

            // Resolve axis to an absolute value (either X_AXIS or Y_AXIS)
            int absoluteAxis = resolveAxis(axis, target.getComponentOrientation());

            if (absoluteAxis == X_AXIS) {
                xTotal = getTiledSizeRequirements(xChildren);
                yTotal = getAlignedSizeRequirements(yChildren);
            } else {
                xTotal = getAlignedSizeRequirements(xChildren);
                yTotal = getTiledSizeRequirements(yChildren);
            }
        }
    }

    private int resolveAxis(int axis, ComponentOrientation o) {
        int absoluteAxis;
        if (axis == LINE_AXIS) {
            absoluteAxis = o.isHorizontal() ? X_AXIS : Y_AXIS;
        } else if (axis == PAGE_AXIS) {
            absoluteAxis = o.isHorizontal() ? Y_AXIS : X_AXIS;
        } else {
            absoluteAxis = axis;
        }
        return absoluteAxis;
    }

    // /////////////////////
    public static SizeRequirements getTiledSizeRequirements(SizeRequirements[] children) {
        SizeRequirements total = new SizeRequirements();
        for (int i = 0; i < children.length; i++) {
            SizeRequirements req = children[i];
            total.minimum = (int) Math.min((long) total.minimum + (long) req.minimum, Integer.MAX_VALUE);
            total.preferred = (int) Math.min((long) total.preferred + (long) req.preferred, Integer.MAX_VALUE);
            total.maximum = (int) Math.min((long) total.maximum + (long) req.maximum, Integer.MAX_VALUE);
        }
        return total;
    }

    public static SizeRequirements getAlignedSizeRequirements(SizeRequirements[] children) {
        SizeRequirements totalAscent = new SizeRequirements();
        SizeRequirements totalDescent = new SizeRequirements();
        for (int i = 0; i < children.length; i++) {
            SizeRequirements req = children[i];

            int ascent = (int) (req.alignment * req.minimum);
            int descent = req.minimum - ascent;
            totalAscent.minimum = Math.max(ascent, totalAscent.minimum);
            totalDescent.minimum = Math.max(descent, totalDescent.minimum);

            ascent = (int) (req.alignment * req.preferred);
            descent = req.preferred - ascent;
            totalAscent.preferred = Math.max(ascent, totalAscent.preferred);
            totalDescent.preferred = Math.max(descent, totalDescent.preferred);

            ascent = (int) (req.alignment * req.maximum);
            descent = req.maximum - ascent;
            totalAscent.maximum = Math.max(ascent, totalAscent.maximum);
            totalDescent.maximum = Math.max(descent, totalDescent.maximum);
        }
        int min = (int) Math.min((long) totalAscent.minimum + (long) totalDescent.minimum, Integer.MAX_VALUE);
        int pref = (int) Math.min((long) totalAscent.preferred + (long) totalDescent.preferred, Integer.MAX_VALUE);
        int max = (int) Math.min((long) totalAscent.maximum + (long) totalDescent.maximum, Integer.MAX_VALUE);
        float alignment = 0.0f;
        if (min > 0) {
            alignment = (float) totalAscent.minimum / min;
            alignment = alignment > 1.0f ? 1.0f : alignment < 0.0f ? 0.0f : alignment;
        }
        return new SizeRequirements(min, pref, max, alignment);
    }

    static void calculateTiledPositions(int allocated, SizeRequirements total, SizeRequirements[] children, int[] offsets, int[] spans, boolean forward) {

        long min = 0;
        long pref = 0;
        long max = 0;
        for (int i = 0; i < children.length; i++) {
            min += children[i].minimum;
            pref += children[i].preferred;
            max += children[i].maximum;
        }
        if (allocated >= pref) {
            expandedTile(allocated, min, pref, max, children, offsets, spans, forward);
        } else {
            compressedTile(allocated, min, pref, max, children, offsets, spans, forward);
        }
    }

    private static void compressedTile(int allocated, long min, long pref, long max, SizeRequirements[] request, int[] offsets, int[] spans, boolean forward) {

        // ---- determine what we have to work with ----
        float totalPlay = Math.min(pref - allocated, pref - min);
        float factor = (pref - min == 0) ? 0.0f : totalPlay / (pref - min);

        // ---- make the adjustments ----
        int totalOffset;
        if (forward) {
            // lay out with offsets increasing from 0
            totalOffset = 0;
            for (int i = 0; i < spans.length; i++) {
                offsets[i] = totalOffset;
                SizeRequirements req = request[i];
                float play = factor * (req.preferred - req.minimum);
                spans[i] = (int) (req.preferred - play);
                totalOffset = (int) Math.min((long) totalOffset + (long) spans[i], Integer.MAX_VALUE);
            }
        } else {
            // lay out with offsets decreasing from the end of the allocation
            totalOffset = allocated;
            for (int i = 0; i < spans.length; i++) {
                SizeRequirements req = request[i];
                float play = factor * (req.preferred - req.minimum);
                spans[i] = (int) (req.preferred - play);
                offsets[i] = totalOffset - spans[i];
                totalOffset = (int) Math.max((long) totalOffset - (long) spans[i], 0);
            }
        }
    }

    private static void expandedTile(int allocated, long min, long pref, long max, SizeRequirements[] request, int[] offsets, int[] spans, boolean forward) {

        // ---- determine what we have to work with ----
        float totalPlay = Math.min(allocated - pref, max - pref);
        float factor = (max - pref == 0) ? 0.0f : totalPlay / (max - pref);

        // ---- make the adjustments ----
        int totalOffset;
        if (forward) {
            // lay out with offsets increasing from 0
            totalOffset = 0;
            for (int i = 0; i < spans.length; i++) {
                offsets[i] = totalOffset;
                SizeRequirements req = request[i];
                int play = (int) (factor * (req.maximum - req.preferred));
                spans[i] = (int) Math.min((long) req.preferred + (long) play, Integer.MAX_VALUE);
                totalOffset = (int) Math.min((long) totalOffset + (long) spans[i], Integer.MAX_VALUE);
            }
        } else {
            // lay out with offsets decreasing from the end of the allocation
            totalOffset = allocated;
            for (int i = 0; i < spans.length; i++) {
                SizeRequirements req = request[i];
                int play = (int) (factor * (req.maximum - req.preferred));
                spans[i] = (int) Math.min((long) req.preferred + (long) play, Integer.MAX_VALUE);
                offsets[i] = totalOffset - spans[i];
                totalOffset = (int) Math.max((long) totalOffset - (long) spans[i], 0);
            }
        }
    }

    public static void calculateAlignedPositions(int allocated, SizeRequirements total, SizeRequirements[] children, int[] offsets, int[] spans) {
        calculateAlignedPositions(allocated, total, children, offsets, spans, true);
    }

    public static void calculateAlignedPositions(int allocated, SizeRequirements total, SizeRequirements[] children, int[] offsets, int[] spans, boolean normal) {
        float totalAlignment = normal ? total.alignment : 1.0f - total.alignment;
        int totalAscent = (int) (allocated * totalAlignment);
        int totalDescent = allocated - totalAscent;
        for (int i = 0; i < children.length; i++) {
            SizeRequirements req = children[i];
            float alignment = normal ? req.alignment : 1.0f - req.alignment;
            int maxAscent = (int) (req.maximum * alignment);
            int maxDescent = req.maximum - maxAscent;
            int ascent = Math.min(totalAscent, maxAscent);
            int descent = Math.min(totalDescent, maxDescent);

            offsets[i] = totalAscent - ascent;
            spans[i] = (int) Math.min((long) ascent + (long) descent, Integer.MAX_VALUE);
        }
    }

    public static int[] adjustSizes(int delta, SizeRequirements[] children) {
        return new int[0];
    }
}
