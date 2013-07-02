package org.ifml.eclipse.ui.widgets;

import org.eclipse.jface.action.IMenuManager;

/**
 * The interface of objects able to fill a context menu.
 */
public interface IContextMenuProvider {

    /**
     * Fills a context menu.
     * <p>
     * Note that this method is invoked quite often, right-clicking on the control, thus it should use existing actions instead of
     * creating them.
     * 
     * @param menuMgr
     *            the menu manager.
     */
    void fillContextMenu(IMenuManager menuMgr);

}
