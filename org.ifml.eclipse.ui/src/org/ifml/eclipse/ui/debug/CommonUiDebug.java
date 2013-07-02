package org.ifml.eclipse.ui.debug;

import javax.annotation.Nullable;

import org.ifml.eclipse.core.runtime.Debugs;
import org.ifml.eclipse.core.runtime.IDebug;

/**
 * Provides debug facilities for the common UI plug-in.
 */
public enum CommonUiDebug implements IDebug {

    /** Debug for base components */
    BASE,

    /** Debug for colors. */
    COLOR,

    /** Debug for fonts. */
    FONT,

    /** Debug for images. */
    IMAGE,

    /** Debug for widgets. */
    WIDGET;

    private final boolean enabled = Debugs.isDebugEnabled(this);

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void debug(@Nullable String template, @Nullable Object... args) {
        Debugs.debug(this, template, args);
    }

}
