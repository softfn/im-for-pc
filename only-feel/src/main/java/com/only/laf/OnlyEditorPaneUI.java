package com.only.laf;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.BasicEditorPaneUI;

/**
 * Provides the Synth L&F UI delegate for {@link javax.swing.JEditorPane}.
 *
 * @author Shannon Hickey
 * @since 1.7
 */
public class OnlyEditorPaneUI extends BasicEditorPaneUI {

    private Boolean localTrue = Boolean.TRUE;

    /**
     * Creates a new UI object for the given component.
     *
     * @param c component to create UI object for
     * @return the UI object
     */
    public static ComponentUI createUI(JComponent c) {
        return new OnlyEditorPaneUI();
    }

    /**
     * @inheritDoc
     */
    @Override
    protected void installDefaults() {
        // Installs the text cursor on the component
        super.installDefaults();
        JComponent c = getComponent();
        Object clientProperty = c.getClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES);
        if (clientProperty == null) {
            c.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, localTrue);
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    protected void uninstallDefaults() {
        JComponent c = getComponent();
        c.putClientProperty("caretAspectRatio", null);
        Object clientProperty = c.getClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES);
        if (clientProperty == localTrue) {
            c.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.FALSE);
        }
        super.uninstallDefaults();
    }

    /**
     * This method gets called when a bound property is changed on the
     * associated JTextComponent. This is a hook which UI implementations may
     * change to reflect how the UI displays bound properties of JTextComponent
     * subclasses. This is implemented to rebuild the ActionMap based upon an
     * EditorKit change.
     *
     * @param evt the property change event
     */
    @Override
    protected void propertyChange(PropertyChangeEvent evt) {
        super.propertyChange(evt);
    }

    @Override
    public void update(Graphics g, JComponent c) {
        super.paint(g, getComponent());
    }
}
