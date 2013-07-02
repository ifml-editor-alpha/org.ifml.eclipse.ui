package org.ifml.eclipse.ui.layouts;

import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Control;
import org.ifml.eclipse.ui.graphics.Alignment;

/**
 * A convenient shorthand for creating and initializing {@link FormData}.
 * <p>
 * In order to minimize variant methods, the {@code denominator} parameter is always omitted and its value is {@code 100}.
 */
public final class FormDataFactory {

    private final FormData data;

    private FormDataFactory(FormData data) {
        this.data = data;
    }

    /**
     * Creates a new factory initialized with the SWT defaults.
     * 
     * @return a new factory instance.
     */
    public static FormDataFactory swtDefaults() {
        return new FormDataFactory(new FormData());
    }

    /**
     * Specifies the attachment for the left side of the control.
     * 
     * @param numerator
     *            the percentage of the position.
     * @param offset
     *            the offset of the side from the position.
     * @return this factory.
     */
    public FormDataFactory left(int numerator, int offset) {
        data.left = new FormAttachment(numerator, 100, offset);
        return this;
    }

    /**
     * Specifies the attachment for the left side of the control.
     * 
     * @param control
     *            the control the side is attached to
     * @param offset
     *            the offset of the side from the control
     * @param alignment
     *            the alignment of the side to the control it is attached to.
     * @return this factory.
     */
    public FormDataFactory left(Control control, int offset, Alignment alignment) {
        data.left = new FormAttachment(control, offset, alignment.getSwtValue());
        return this;
    }

    /**
     * Specifies the attachment for the right side of the control.
     * 
     * @param numerator
     *            the percentage of the position.
     * @param offset
     *            the offset of the side from the position.
     * @return this factory.
     */
    public FormDataFactory right(int numerator, int offset) {
        data.right = new FormAttachment(numerator, 100, offset);
        return this;
    }

    /**
     * Specifies the attachment for the right side of the control.
     * 
     * @param control
     *            the control the side is attached to
     * @param offset
     *            the offset of the side from the control
     * @param alignment
     *            the alignment of the side to the control it is attached to.
     * @return this factory.
     */
    public FormDataFactory right(Control control, int offset, Alignment alignment) {
        data.right = new FormAttachment(control, offset, alignment.getSwtValue());
        return this;
    }

    /**
     * Specifies the attachment for the upper side of the control.
     * 
     * @param numerator
     *            the percentage of the position.
     * @param offset
     *            the offset of the side from the position.
     * @return this factory.
     */
    public FormDataFactory top(int numerator, int offset) {
        data.top = new FormAttachment(numerator, 100, offset);
        return this;
    }

    /**
     * Specifies the attachment for the upper side of the control.
     * 
     * @param control
     *            the control the side is attached to
     * @param offset
     *            the offset of the side from the control
     * @param alignment
     *            the alignment of the side to the control it is attached to.
     * @return this factory.
     */
    public FormDataFactory top(Control control, int offset, Alignment alignment) {
        data.top = new FormAttachment(control, offset, alignment.getSwtValue());
        return this;
    }

    /**
     * Specifies the attachment for the lower side of the control.
     * 
     * @param numerator
     *            the percentage of the position.
     * @param offset
     *            the offset of the side from the position.
     * @return this factory.
     */
    public FormDataFactory bottom(int numerator, int offset) {
        data.bottom = new FormAttachment(numerator, 100, offset);
        return this;
    }

    /**
     * Specifies the attachment for the lower side of the control.
     * 
     * @param control
     *            the control the side is attached to
     * @param offset
     *            the offset of the side from the control
     * @param alignment
     *            the alignment of the side to the control it is attached to.
     * @return this factory.
     */
    public FormDataFactory bottom(Control control, int offset, Alignment alignment) {
        data.bottom = new FormAttachment(control, offset, alignment.getSwtValue());
        return this;
    }

    /**
     * Sets the layout data on the given control. Creates a new {@link FormData} instance and assigns it to the control by calling
     * {@link Control#setLayoutData(Object)}.
     * 
     * @param control
     *            control whose layout data will be initialized
     */
    public void applyTo(Control control) {
        control.setLayoutData(create());
    }

    private FormData create() {
        return copyData(data);
    }

    private static FormData copyData(FormData data) {
        FormData newData = new FormData(data.width, data.height);
        newData.left = copyData(data.left);
        newData.right = copyData(data.right);
        newData.top = copyData(data.top);
        newData.bottom = copyData(data.bottom);
        newData.width = data.width;
        newData.height = data.height;
        return newData;
    }

    private static FormAttachment copyData(FormAttachment att) {
        if (att == null) {
            return null;
        }
        FormAttachment newAtt = new FormAttachment(att.numerator, att.denominator, att.offset);
        newAtt.control = att.control;
        newAtt.alignment = att.alignment;
        return newAtt;
    }

}
