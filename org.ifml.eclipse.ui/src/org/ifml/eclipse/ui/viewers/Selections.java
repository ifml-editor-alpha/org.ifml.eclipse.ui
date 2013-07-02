package org.ifml.eclipse.ui.viewers;

import javax.annotation.Nullable;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;

/**
 * Provides utility methods for {@link ISelection}.
 */
public final class Selections {

    private Selections() {
    }

    /**
     * Returns the single selected element.
     * 
     * @param provider
     *            the selection provided.
     * @return the single selected element or {@code null} if the selection of {@code provider} is not a {@link IStructuredSelection}
     *         containing exactly one element.
     */
    public static Object getSingleElement(ISelectionProvider provider) {
        return getSingleElement(provider.getSelection());
    }

    /**
     * Returns the single selected element.
     * 
     * @param s
     *            the selection.
     * @return the single selected element or {@code null} if {@code s} is not a {@link IStructuredSelection} containing exactly one
     *         element.
     */
    public static Object getSingleElement(ISelection s) {
        if (!(s instanceof IStructuredSelection)) {
            return null;
        }
        IStructuredSelection selection = (IStructuredSelection) s;
        if (selection.size() != 1) {
            return null;
        }
        return selection.getFirstElement();
    }

    /**
     * Returns the first selected element.
     * 
     * @param s
     *            the selection.
     * @return the first selected element or {@code null} if {@code s} is not a {@link IStructuredSelection} containing at least one
     *         element.
     */
    public static Object getFirstElement(ISelection s) {
        if (!(s instanceof IStructuredSelection)) {
            return null;
        }
        IStructuredSelection selection = (IStructuredSelection) s;
        return selection.getFirstElement();
    }

    /**
     * Converts the given selection into a (possibly empty) structured selection.
     * 
     * @param s
     *            the selection.
     * @return a structured selection, never {@code null}.
     */
    public static IStructuredSelection toStructuredSelection(@Nullable ISelection s) {
        if (s instanceof IStructuredSelection) {
            return (IStructuredSelection) s;
        } else {
            return StructuredSelection.EMPTY;
        }
    }

}
