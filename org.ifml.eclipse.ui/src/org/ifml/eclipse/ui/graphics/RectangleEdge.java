package org.ifml.eclipse.ui.graphics;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 * Represents a rectangle edge.
 */
public enum RectangleEdge {

    /** The top edge. */
    TOP,

    /** The left edge. */
    LEFT,

    /** The bottom edge. */
    BOTTOM,

    /** The right edge. */
    RIGHT;

    /**
     * Returns the edge a point is located with respect to a rectangle.
     * 
     * @param p
     *            the point.
     * @param rect
     *            the rectangle.
     * @return the edge where {@code p} is located with respect to {@code rect}.
     */
    public static RectangleEdge get(Rectangle rect, Point p) {
        Point min = new Point(rect.x, rect.y);
        Point max = new Point(rect.x + rect.width, rect.y + rect.height);
        boolean bottomOrLeft = (p.y - min.y) * rect.width > rect.height * (p.x - min.x);
        boolean bottomOrRight = (p.y - min.y) * rect.width > rect.height * (max.x - p.x);
        return (bottomOrLeft && bottomOrRight ? BOTTOM : bottomOrLeft && !bottomOrRight ? LEFT : !bottomOrLeft && !bottomOrRight ? TOP
                : RIGHT);
    }

}
