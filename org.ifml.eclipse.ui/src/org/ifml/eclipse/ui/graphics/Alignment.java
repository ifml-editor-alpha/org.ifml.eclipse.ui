package org.ifml.eclipse.ui.graphics;

import org.eclipse.swt.SWT;

/**
 * Alignment behaviour as provided by {@link SWT} constants.
 */
public enum Alignment {

    /** Top alignment. */
    TOP(SWT.TOP),

    /** Left alignment. */
    LEFT(SWT.LEFT),

    /** Bottom alignment. */
    BOTTOM(SWT.BOTTOM),

    /** Right alignment. */
    RIGHT(SWT.RIGHT),

    /** Center alignment. */
    CENTER(SWT.CENTER),

    /** Default alignment. */
    DEFAULT(SWT.DEFAULT);

    private int value;

    Alignment(int value) {
        this.value = value;
    }

    /**
     * Returns the SWT value.
     * 
     * @return one of the {@link SWT} constants.
     * @see SWT#TOP
     * @see SWT#LEFT
     * @see SWT#BOTTOM
     * @see SWT#RIGHT
     * @see SWT#CENTER
     * @see SWT#DEFAULT
     */
    public int getSwtValue() {
        return value;
    }

}
