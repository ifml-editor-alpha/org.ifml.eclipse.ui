package org.ifml.eclipse.ui.viewers;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * A base class for {@link ILabelProvider} to be used as singleton classes.
 * <p>
 * The main difference with respect to the {@link LabelProvider} class is the empty implementation of all the following methods:
 * <ul>
 * <li>{@link #addListener(ILabelProviderListener)}
 * <li>{@link #removeListener(ILabelProviderListener)}
 * <li>{@link #isLabelProperty(Object, String)}
 * <li>{@link #dispose()}
 * </ul>
 */
public class BasicLabelProvider implements ILabelProvider {

    @Override
    public final void addListener(ILabelProviderListener listener) {
    }

    @Override
    public final boolean isLabelProperty(Object element, String property) {
        return true;
    }

    @Override
    public final void removeListener(ILabelProviderListener listener) {
    }

    @Override
    public Image getImage(Object element) {
        return null;
    }

    @Override
    public String getText(Object element) {
        return (element == null) ? "" : element.toString();
    }

    @Override
    public final void dispose() {
    }

}
