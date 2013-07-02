package org.ifml.eclipse.ui.dialogs;

import java.lang.reflect.InvocationTargetException;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.widgets.Shell;
import org.ifml.eclipse.core.runtime.Statuses;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;

/**
 * A builder for {@link ErrorDialog}.
 */
public final class ErrorDialogBuilder {

    private Shell shell;

    private String title;

    private String message;

    private Throwable exception;

    /**
     * Sets the parent shell.
     * 
     * @param shell
     *            the parent shell.
     * @return this builder.
     */
    public ErrorDialogBuilder shell(Shell shell) {
        this.shell = shell;
        return this;
    }

    /**
     * Sets the title.
     * 
     * @param title
     *            the title.
     * @return this builder.
     */
    public ErrorDialogBuilder title(@Nullable String title) {
        this.title = title;
        return this;
    }

    /**
     * Sets the message.
     * 
     * @param message
     *            the message.
     * @return this builder.
     */
    public ErrorDialogBuilder message(@Nullable String message) {
        this.message = message;
        return this;
    }

    /**
     * Sets the exception.
     * 
     * @param exception
     *            the exception.
     * @return this builder.
     */
    public ErrorDialogBuilder exception(@Nullable Throwable exception) {
        this.exception = simplify(exception);
        return this;
    }

    private Throwable simplify(Throwable exception) {
        if (exception instanceof InvocationTargetException) {
            InvocationTargetException invException = (InvocationTargetException) exception;
            if (invException.getCause() != null) {
                return invException.getCause();
            }
        }
        return exception;
    }

    /**
     * Builds the dialog.
     * 
     * @return the dialog.
     */
    public ErrorDialog build() {
        Preconditions.checkNotNull(shell);
        if (exception != null) {
            IStatus errorStatus = Statuses.getErrorStatus(exception, message, null);
            String statusMsg = Objects.firstNonNull(Strings.emptyToNull(exception.getMessage()), exception.toString());
            MultiStatus status = new MultiStatus("unknown", 1, statusMsg, exception);
            for (String line : Splitter.on(Pattern.compile("\r?\n")).omitEmptyStrings().trimResults()
                    .split(Throwables.getStackTraceAsString(exception))) {
                status.add(Statuses.getErrorStatus(null, line, null));
            }
            if (status.getChildren().length != 0) { // the dialog won't open being multistatus.severity == OK
                if (status.getChildren().length == 1) { // force the dialog to not show the (redundant) details
                    IStatus newStatus = Statuses.getErrorStatus(null, getSingleStatusMessage(message, status), null);
                    return new ErrorDialog(shell, title, null, newStatus, IStatus.ERROR);
                } else {
                    return new ErrorDialog(shell, title, (message != null) ? errorStatus.getMessage() : message, status, IStatus.ERROR);
                }
            }
        }
        IStatus errorStatus = Statuses.getErrorStatus(exception, message, null);
        return new ErrorDialog(shell, title, null, errorStatus, IStatus.ERROR);
    }

    private String getSingleStatusMessage(String message, MultiStatus status) {
        return (message == null) ? status.getMessage() : JFaceResources
                .format("Reason", new Object[] { message, status.getMessage() });
    }

}
