package org.ifml.eclipse.ui.wizards;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.ifml.eclipse.ui.widgets.Controls;

/**
 * A subclass of {@link WizardPage} providing easy customization.
 */
public abstract class CommonWizardPage extends WizardPage {

    /** Constructs a new wizard page. */
    public CommonWizardPage() {
        super("");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public final void createControl(Composite parent) {
        initializeDialogUnits(parent);
        Control control = doCreateControl(parent);
        Dialog.applyDialogFont(control);
        Controls.initializeScrolledComposites(parent);
        setControl(control);
    }

    /**
     * Creates the top level control for this dialog page under the given parent composite.
     * 
     * @param parent
     *            the parent composite.
     * @return the top level control.
     */
    protected abstract Control doCreateControl(Composite parent);

    @Override
    public final String getName() {
        return getClass().getName();
    }

    @Override
    public final String toString() {
        return getName();
    }

    /** Either advances to the next page or completes the wizard. */
    protected final void advanceToNextOrFinish() {
        Wizards.advanceToNextOrFinish(this);
    }

}
