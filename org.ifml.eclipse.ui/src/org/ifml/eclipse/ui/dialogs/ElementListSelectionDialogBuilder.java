package org.ifml.eclipse.ui.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.dialogs.FilteredItemsSelectionDialog;
import org.ifml.eclipse.ui.CommonUi;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

/**
 * A builder for {@link ElementListSelectionDialog}.
 * <p>
 * This kind of dialog is quite deprecated and should be replaced with the dialog created by {@link FilteredListSelectionDialogBuilder}
 * unless an initial selection is required, which is not supported by {@link FilteredItemsSelectionDialog}.
 */
public final class ElementListSelectionDialogBuilder {

    private Shell shell;

    private Boolean multiSelection;

    private String settingsKey;

    private String message = "&Select an item (? = any character, * = any string, ** = all):";

    private String title;

    private Iterable<?> items;

    private ILabelProvider labelProvider;

    /**
     * Sets the parent shell.
     * 
     * @param shell
     *            the parent shell.
     * @return this builder.
     */
    public ElementListSelectionDialogBuilder shell(Shell shell) {
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
    public ElementListSelectionDialogBuilder title(String title) {
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
    public ElementListSelectionDialogBuilder message(String message) {
        this.message = message;
        return this;
    }

    /**
     * Sets the key used to store dialog's settings, in order to persist the location and size of the dialog.
     * 
     * @param settingsKey
     *            the key used to store dialog's settings.
     * @return this builder.
     */
    public ElementListSelectionDialogBuilder settingsKey(String settingsKey) {
        this.settingsKey = settingsKey;
        return this;
    }

    /**
     * Sets the label provider for the list of elements.
     * 
     * @param labelProvider
     *            the label provider.
     * @return this builder.
     */
    public ElementListSelectionDialogBuilder labelProvider(ILabelProvider labelProvider) {
        this.labelProvider = labelProvider;
        return this;
    }

    /**
     * Allows the selection of multiple elements.
     * 
     * @return this builder.
     */
    public ElementListSelectionDialogBuilder multiSelection() {
        this.multiSelection = true;
        return this;
    }

    /**
     * Allows the selection of a single element.
     * 
     * @return this builder.
     */
    public ElementListSelectionDialogBuilder singleSelection() {
        this.multiSelection = false;
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
    public ElementListSelectionDialogBuilder items(Iterable<?> items) {
        this.items = ImmutableList.copyOf(items);
        return this;
    }

    /**
     * Builds the dialog.
     * 
     * @return the dialog.
     */
    public ElementListSelectionDialog build() {
        Preconditions.checkNotNull(shell);
        Preconditions.checkNotNull(multiSelection);
        Preconditions.checkNotNull(title);
        Preconditions.checkNotNull(items);
        Preconditions.checkNotNull(labelProvider);
        ElementListSelectionDialog dialog = new ElementListSelectionDialog(shell, labelProvider);
        dialog.setMultipleSelection(multiSelection);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setElements(Iterables.toArray(items, Object.class));
        if (settingsKey != null) {
            dialog.setDialogBoundsSettings(Dialogs.getDialogSettings(CommonUi.getDefault(), settingsKey),
                    Dialog.DIALOG_PERSISTLOCATION | Dialog.DIALOG_PERSISTSIZE);
        }
        dialog.setFilter("**");
        return dialog;
    }

}
