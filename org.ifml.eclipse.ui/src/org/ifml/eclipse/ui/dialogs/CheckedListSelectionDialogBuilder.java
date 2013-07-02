package org.ifml.eclipse.ui.dialogs;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ListSelectionDialog;
import org.ifml.eclipse.ui.CommonUi;
import org.ifml.eclipse.ui.viewers.LabelProviderComparator;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

/**
 * A builder for {@link ListSelectionDialog}.
 */
public final class CheckedListSelectionDialogBuilder {

    private Shell shell;

    private String settingsKey;

    private String title;

    private String message = "&Select one or more items:";

    private List<?> items;

    private ILabelProvider labelProvider;

    private List<?> initialSelections;

    /**
     * Sets the parent shell.
     * 
     * @param shell
     *            the parent shell.
     * @return this builder.
     */
    public CheckedListSelectionDialogBuilder shell(Shell shell) {
        this.shell = shell;
        return this;
    }

    /**
     * Sets the dialog's title.
     * 
     * @param title
     *            the dialog's title.
     * @return this builder.
     */
    public CheckedListSelectionDialogBuilder title(String title) {
        this.title = title;
        return this;
    }

    /**
     * Sets the message to be displayed at the top of this dialog.
     * 
     * @param message
     *            the message to be displayed at the top of this dialog.
     * @return this builder.
     */
    public CheckedListSelectionDialogBuilder message(String message) {
        this.message = message;
        return this;
    }

    /**
     * Sets the key used to store dialog's settings, in order to persist the location and size of the dialog. A {@code null} value
     * indicates that dialog's bounds are not persisted.
     * 
     * @param settingsKey
     *            the key used to store dialog's settings.
     * @return this builder.
     */
    public CheckedListSelectionDialogBuilder settingsKey(@Nullable String settingsKey) {
        this.settingsKey = settingsKey;
        return this;
    }

    /**
     * Sets the label provider.
     * 
     * @param labelProvider
     *            the label provider.
     * @return this builder.
     */
    public CheckedListSelectionDialogBuilder labelProvider(ILabelProvider labelProvider) {
        this.labelProvider = labelProvider;
        return this;
    }

    /**
     * Sets the list of items. Items do not need to be ordered: they are automatically ordered, ignoring case differences, using the
     * text returned by {@link ILabelProvider#getText(Object)}.
     * 
     * @param items
     *            the list of items.
     * @return the list of items.
     */
    public CheckedListSelectionDialogBuilder items(List<?> items) {
        this.items = ImmutableList.copyOf(items);
        return this;
    }

    /**
     * Sets the list of initial checked items. Items do not need to be ordered: they are automatically ordered, ignoring case
     * differences, using the text returned by {@link ILabelProvider#getText(Object)}.
     * 
     * @param initialSelections
     *            the list of initial checked items.
     * @return the list of items.
     */
    public CheckedListSelectionDialogBuilder initialSelections(List<?> initialSelections) {
        this.initialSelections = ImmutableList.copyOf(initialSelections);
        return this;
    }

    /**
     * Builds the dialog.
     * 
     * @return the dialog.
     */
    public ListSelectionDialog build() {
        Preconditions.checkNotNull(shell);
        Preconditions.checkNotNull(title);
        Preconditions.checkNotNull(message);
        Preconditions.checkNotNull(items);
        Preconditions.checkNotNull(labelProvider);
        List<?> sortedItems = Lists.newArrayList(items);
        Collections.sort(sortedItems, new LabelProviderComparator(labelProvider));
        ListSelectionDialog dialog = new ListSelectionDialog(shell, sortedItems, ArrayContentProvider.getInstance(), labelProvider,
                message);
        dialog.setTitle(title);
        if (initialSelections != null) {
            dialog.setInitialElementSelections(initialSelections);
        }
        if (settingsKey != null) {
            dialog.setDialogBoundsSettings(Dialogs.getDialogSettings(CommonUi.getDefault(), settingsKey),
                    Dialog.DIALOG_PERSISTLOCATION | Dialog.DIALOG_PERSISTSIZE);
        }
        return dialog;
    }

}
