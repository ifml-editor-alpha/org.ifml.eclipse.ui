package org.ifml.eclipse.ui.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.HyperlinkSettings;
import org.eclipse.ui.forms.widgets.Hyperlink;

import com.google.common.base.Preconditions;

/**
 * A builder for {@code Hyperlink}.
 * 
 * <p>
 * The initial settings of a newly created builder is equivalent to a builder configured in the following way:
 * 
 * <pre>
 * {@code new HyperlinkBuilder()
 *         .wrap(false)
 *         .text("")
 * }
 * </pre>
 */
public class HyperlinkBuilder {

    private boolean wrap;

    private String text;

    /**
     * Sets the automatic line wrap behaviour.
     * 
     * @param wrap
     *            if {@code true} the automatic line wrap behaviour is activated.
     * @return this builder.
     */
    public HyperlinkBuilder wrap(boolean wrap) {
        this.wrap = wrap;
        return this;
    }

    /**
     * Sets the text.
     * 
     * @param text
     *            the text.
     * @return this builder.
     */
    public HyperlinkBuilder text(String text) {
        this.text = text;
        return this;
    }

    /**
     * Builds a new hyperlink.
     * 
     * @param parent
     *            the parent composite.
     * @return the new hyperlink.
     */
    public Hyperlink build(Composite parent) {
        Preconditions.checkNotNull(parent);
        Hyperlink link = new Hyperlink(parent, wrap ? SWT.WRAP : SWT.NONE);
        link.setText(text);
        HyperlinkSettings linkSettings = new HyperlinkSettings(parent.getDisplay());
        link.setForeground(linkSettings.getActiveForeground());
        link.setUnderlined(linkSettings.getHyperlinkUnderlineMode() == HyperlinkSettings.UNDERLINE_ALWAYS);
        return link;
    }
}
