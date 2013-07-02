package org.ifml.eclipse.ui.actions;

import org.eclipse.jface.action.Action;

/**
 * An abstract sub-class of {@link Action} providing support for context.
 * 
 * @param <T>
 *            the context type.
 */
public abstract class ContextAction<T> extends Action {

    private T context;

    /**
     * Sets the context.
     * 
     * @param context
     *            the context.
     */
    public void setContext(T context) {
        this.context = context;
    }

    /**
     * Returns the current context.
     * 
     * @return the current context.
     */
    protected final T getContext() {
        return context;
    }

}
