package org.ifml.eclipse.ui.wizards;

import org.eclipse.jface.wizard.Wizard;

import com.google.common.base.Preconditions;

/**
 * A subclass of {@link Wizard} providing easy customization.
 */
public abstract class CommonWizard extends Wizard {

    /**
     * Constructs a new wizard.
     * 
     * @param config
     *            the wizards's configuration.
     */
    public CommonWizard(WizardConfiguration config) {
        Preconditions.checkNotNull(config);
        setDefaultPageImageDescriptor(config.defaultPageImageDescriptor);
        setForcePreviousAndNextButtons(config.forcePreviousAndNextButtons);
        setNeedsProgressMonitor(config.needsProgressMonitor);
        setWindowTitle(config.windowTitle);
    }

}
