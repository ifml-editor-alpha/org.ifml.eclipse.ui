package org.ifml.eclipse.ui.wizards;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchWizard;
import org.ifml.base.Objects2;
import org.ifml.eclipse.ui.Workbenches;
import org.ifml.eclipse.ui.viewers.Selections;

/**
 * Provides utility methods for {@link IWizard}.
 */
public final class Wizards {

    private Wizards() {
    }

    /**
     * Creates and opens a wizard's dialog.
     * 
     * @param shell
     *            the parent shell.
     * @param wizard
     *            the wizard to be executed.
     * @return the window-specific result code: either {@link Window#OK} or {@link Window#CANCEL}.
     */
    public static final int openDialog(Shell shell, IWizard wizard) {
        if (wizard instanceof IWorkbenchWizard) {
            ((IWorkbenchWizard) wizard).init(Workbenches.getActiveWindow().getWorkbench(),
                    Selections.toStructuredSelection(Workbenches.getActiveWindow().getSelectionService().getSelection()));
        }
        WizardDialog dialog = new WizardDialog(shell, wizard);
        dialog.create();
        return dialog.open();
    }

    /**
     * Either advances to the next page or completes the wizard.
     * 
     * @param page
     *            the current wizard page.
     */
    public static final void advanceToNextOrFinish(IWizardPage page) {
        if (page.canFlipToNextPage()) {
            page.getWizard().getContainer().showPage(page.getNextPage());
        } else if ((page.getNextPage() == null) && page.getWizard().canFinish()) {
            WizardDialog dialog = Objects2.as(page.getWizard().getContainer(), WizardDialog.class);
            if (dialog != null) {
                Button button = dialog.getShell().getDefaultButton();
                if (button != null) {
                    Integer buttonId = Objects2.as(button.getData(), Integer.class);
                    if ((buttonId != null) && (buttonId.intValue() == IDialogConstants.FINISH_ID)) {
                        Method[] methods = dialog.getClass().getDeclaredMethods();
                        for (int i = 0; i < methods.length; i++) {
                            if (methods[i].getName().equals("finishPressed") && methods[i].getParameterTypes().length == 0) {
                                try {
                                    methods[i].setAccessible(true);
                                    methods[i].invoke(dialog);
                                    break;
                                } catch (IllegalArgumentException e) {
                                } catch (IllegalAccessException e) {
                                } catch (InvocationTargetException e) {
                                }
                            }
                        }

                    }
                }
            }
        }
    }

    /**
     * Utility method which forces a text control in a wizard page to having its text fully selected the very first time the wizard
     * page is displayed.
     * <p>
     * This method is useful for a wizard page providing a default text (for instance a name) for the element being created, but
     * "suggesting" the user to review and modify such text by selecting it at first display.
     * 
     * @param text
     *            the text control.
     * @param wizardPage
     *            the wizard page containing the {@code text} control.
     */
    public static void selectAllOnFirstShow(Text text, IWizardPage wizardPage) {
        IWizard wizard = wizardPage.getWizard();
        if (wizard == null) {
            return;
        }
        WizardDialog dialog = Objects2.as(wizard.getContainer(), WizardDialog.class);
        if (dialog == null) {
            return;
        }
        dialog.addPageChangedListener(new SelectAllOnFirstShowListener(text, wizardPage));
    }

    private static final class SelectAllOnFirstShowListener implements IPageChangedListener {

        private boolean first = true;

        private final Text text;

        private final IWizardPage wizardPage;

        public SelectAllOnFirstShowListener(Text text, IWizardPage wizardPage) {
            this.text = text;
            this.wizardPage = wizardPage;
        }

        @Override
        public void pageChanged(PageChangedEvent event) {
            if (event.getSelectedPage() == wizardPage) {
                if (first) {
                    first = false;
                    text.selectAll();
                }
            }
        }

    }

}
