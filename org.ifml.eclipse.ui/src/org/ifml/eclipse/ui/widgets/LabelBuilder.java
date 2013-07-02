package org.ifml.eclipse.ui.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * A builder for {@link Label}.
 * 
 * <p>
 * The initial settings of a newly created builder is equivalent to a builder configured in the following way:
 * 
 * <pre>
 * {@code new LabelBuilder()
 *         .left()
 *         .layoutData(null)
 *         .wrap(false)
 *         .systemColors()
 *         .parentFont()
 *         .text("")
 *         .image(null)
 * }
 * </pre>
 */
public final class LabelBuilder {

    private int alignment = SWT.LEFT;

    private Object layoutData;

    private boolean wrap = false;

    private Color fgColor;

    private Color bgColor;

    private Font font;

    private String text;

    private Image image;

    /**
     * Sets left alignment.
     * 
     * @return this builder.
     */
    public LabelBuilder left() {
        this.alignment = SWT.LEFT;
        return this;
    }

    /**
     * Sets center alignment.
     * 
     * @return this builder.
     */
    public LabelBuilder center() {
        this.alignment = SWT.CENTER;
        return this;
    }

    /**
     * Sets right alignment.
     * 
     * @return this builder.
     */
    public LabelBuilder right() {
        this.alignment = SWT.RIGHT;
        return this;
    }

    /**
     * Sets the automatic line wrap behaviour.
     * 
     * @param wrap
     *            if {@code true} the automatic line wrap behaviour is activated.
     * @return this builder.
     */
    public LabelBuilder wrap(boolean wrap) {
        this.wrap = wrap;
        return this;
    }

    /**
     * Sets the layout data.
     * 
     * @param layoutData
     *            the layout data.
     * @return this builder.
     */
    public LabelBuilder layoutData(Object layoutData) {
        this.layoutData = layoutData;
        return this;
    }

    /**
     * Sets the foreground and background colors.
     * 
     * @param fgColor
     *            the foreground color, or {@code null} to select the default system color.
     * @param bgColor
     *            the background color, or {@code null} to select the default system color.
     * @return this builder.
     */
    public LabelBuilder colors(Color fgColor, Color bgColor) {
        this.fgColor = fgColor;
        this.bgColor = bgColor;
        return this;
    }

    /**
     * Sets the default system colors. This is equivalent to {@link #colors(Color, Color)} with both arguments {@code null}.
     * 
     * @return this builder.
     */
    public LabelBuilder systemColors() {
        return colors(null, null);
    }

    /**
     * Sets the font, or {@code null} to force {@link #build(Composite)} to set the font obtained by invoking
     * {@link Composite#getFont()} on the parent component.
     * 
     * @param font
     *            the font.
     * @return this builder.
     */
    public LabelBuilder font(Font font) {
        this.font = font;
        return this;
    }

    /**
     * Sets the font of the parent composite. This is equivalent to {@link #font(Font)} with {@code null} argument.
     * 
     * @return this builder.
     */
    public LabelBuilder parentFont() {
        return font(null);
    }

    /**
     * Sets the text.
     * 
     * @param text
     *            the text.
     * @return this builder.
     */
    public LabelBuilder text(String text) {
        this.text = text;
        return this;
    }

    /**
     * Sets the image.
     * 
     * @param image
     *            the image.
     * @return this builder.
     */
    public LabelBuilder image(Image image) {
        this.image = image;
        return this;
    }

    /**
     * Builds a new label.
     * 
     * @param parent
     *            the parent composite.
     * @return this label.
     */
    public Label build(Composite parent) {
        Preconditions.checkNotNull(parent);
        int style = alignment | (wrap ? SWT.WRAP : 0);
        Label label = new Label(parent, style);
        label.setLayoutData(layoutData);
        label.setForeground(fgColor);
        label.setBackground(bgColor);
        label.setFont((font != null) ? font : parent.getFont());
        label.setText(Strings.nullToEmpty(text));
        if (image != null) {
            label.setImage(image);
        }
        return label;
    }
}
