package org.ifml.eclipse.ui;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class CommonUi extends AbstractUIPlugin {

    /** The identifier of this plug-in. */
    public static final String ID = CommonUi.class.getPackage().getName();

    /** The shared instance. */
    private static CommonUi plugin;

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static CommonUi getDefault() {
        return plugin;
    }

}
