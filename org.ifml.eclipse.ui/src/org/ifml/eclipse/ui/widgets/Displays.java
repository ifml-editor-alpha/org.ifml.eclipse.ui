package org.ifml.eclipse.ui.widgets;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

/**
 * Provides utility methods for displays.
 * 
 */
public final class Displays {

    private Displays() {
    }

    /**
     * Returns the display associated with the user-interface thread.
     * 
     * @return the display associated with the user-interface thread.
     */
    public static Display getDisplay() {
        Display display = Display.getCurrent();
        if (display == null) { // may be null if outside the UI thread
            display = PlatformUI.getWorkbench().getDisplay();
        }
        return display;
    }

}
