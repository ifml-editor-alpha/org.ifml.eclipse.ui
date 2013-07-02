package org.ifml.eclipse.ui;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

/**
 * Provides utility methods for the workbench.
 */
public final class Workbenches {

    private Workbenches() {
    }

    /**
     * Returns the currently active window for the workbench (if any). Returns {@code null} if the workbench has not been created yet,
     * if there is no active workbench window or if called from a non-UI thread.
     * 
     * @return the active workbench window, or {@code null} if the workbench has not been created yet, if there is no active workbench
     *         window or if called from a non-UI thread.
     */
    public static IWorkbenchWindow getActiveWindow() {
        IWorkbench workbench = PlatformUI.getWorkbench();
        if (workbench != null) {
            return workbench.getActiveWorkbenchWindow();
        } else {
            return null;
        }
    }

    /**
     * Returns the shell containing the currently active window for the workbench (if any). Returns {@code null} if the workbench has
     * not been created yet, if there is no active workbench window, if the shell has not been created yet, if the window has been
     * closed or if called from a non-UI thread.
     * 
     * @return the active window's shell, or {@code null} if the workbench has not been created yet, if there is no active workbench
     *         window, if the shell has not been created yet, if the window has been closed or if called from a non-UI thread.
     */
    public static Shell getActiveShell() {
        IWorkbenchWindow window = getActiveWindow();
        if (window != null) {
            return window.getShell();
        } else {
            return null;
        }
    }

    /**
     * Returns the active page for the currently active window for the workbench (if any). Returns {@code null} if the workbench has
     * not been created yet, if there is no active workbench window, if no page is currently active or if called from a non-UI thread.
     * 
     * @return the active window's page, or {@code null} if the workbench has not been created yet, if there is no active workbench
     *         window, if no page is currently active or if called from a non-UI thread.
     */
    public static IWorkbenchPage getActivePage() {
        IWorkbenchWindow window = getActiveWindow();
        if (window != null) {
            return window.getActivePage();
        } else {
            return null;
        }
    }

    /**
     * Returns the active editor for the currently active window for the workbench (if any). Returns {@code null} if the workbench has
     * not been created yet, if there is no active workbench window, if no editor is currently active or if called from a non-UI
     * thread.
     * 
     * @return the active window's editor, or {@code null} if the workbench has not been created yet, if there is no active workbench
     *         window, if no editor is currently active or if called from a non-UI thread.
     */
    public static IEditorPart getActiveEditor() {
        IWorkbenchPage page = getActivePage();
        if (page != null) {
            return page.getActiveEditor();
        } else {
            return null;
        }
    }

    /**
     * Returns the active part for the currently active window for the workbench (if any). Returns {@code null} if the workbench has
     * not been created yet, if there is no active workbench window, if no part is currently active or if called from a non-UI thread.
     * 
     * @return the active window's part, or {@code null} if the workbench has not been created yet, if there is no active workbench
     *         window, if no editor is currently active or if called from a non-UI thread.
     */
    public static IWorkbenchPart getActivePart() {
        IWorkbenchPage page = getActivePage();
        if (page != null) {
            return page.getActivePart();
        } else {
            return null;
        }
    }

    /**
     * Returns the active view for the currently active window for the workbench (if any). Returns {@code null} if the workbench has
     * not been created yet, if there is no active workbench window, if no view is currently active or if called from a non-UI thread.
     * 
     * @return the active window's view, or {@code null} if the workbench has not been created yet, if there is no active workbench
     *         window, if no view is currently active or if called from a non-UI thread.
     */
    public static IViewPart getActiveView() {
        IWorkbenchPart part = getActivePart();
        if (part instanceof IViewPart) {
            return (IViewPart) part;
        } else {
            return null;
        }
    }

    /**
     * Makes a view visible, without providing it the focus.
     * 
     * @param viewId
     *            the identifier of the view.
     * @throws PartInitException
     *             if the view could not be initialized.
     */
    public static void showView(String viewId) throws PartInitException {
        showView(viewId, IWorkbenchPage.VIEW_VISIBLE);
    }

    /**
     * Makes a view visible and activated, providing it the focus.
     * 
     * @param viewId
     *            the identifier of the view.
     * @throws PartInitException
     *             if the view could not be initialized.
     */
    public static void showAndActivateView(String viewId) throws PartInitException {
        showView(viewId, IWorkbenchPage.VIEW_ACTIVATE);
    }

    private static void showView(String viewId, int mode) throws PartInitException {
        IWorkbenchPage page = Workbenches.getActivePage();
        if (page != null) {
            page.showView(viewId, null, mode);
        }
    }

}
