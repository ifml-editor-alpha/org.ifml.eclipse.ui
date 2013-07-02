package org.ifml.eclipse.ui.dialogs;

import java.util.Comparator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.FilteredItemsSelectionDialog;
import org.ifml.eclipse.ui.CommonUi;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;

/**
 * A builder for {@link FilteredItemsSelectionDialog}.
 * <p>
 * Note that this kind of dialog not correctly handle the initial selection and (only) in such a case it must be replaced by the dialog
 * provided by {@link ElementListSelectionDialogBuilder}.
 */
public final class FilteredListSelectionDialogBuilder {

    private Shell shell;

    private Boolean multiSelection;

    private String settingsKey;

    private String title;

    private String message = "&Select an item (? = any character, * = any string, ** = all):";

    private List<?> items;

    private ILabelProvider listLabelProvider;

    private ILabelProvider detailsLabelProvider;

    private Function<Composite, Control> extendedContentAreaCreator;

    /**
     * Sets the parent shell.
     * 
     * @param shell
     *            the parent shell.
     * @return this builder.
     */
    public FilteredListSelectionDialogBuilder shell(Shell shell) {
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
    public FilteredListSelectionDialogBuilder title(String title) {
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
    public FilteredListSelectionDialogBuilder message(String message) {
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
    public FilteredListSelectionDialogBuilder settingsKey(String settingsKey) {
        this.settingsKey = settingsKey;
        return this;
    }

    /**
     * Sets the label provider for the list of elements.
     * 
     * @param listLabelProvider
     *            the label provider.
     * @return this builder.
     */
    public FilteredListSelectionDialogBuilder listLabelProvider(ILabelProvider listLabelProvider) {
        this.listLabelProvider = listLabelProvider;
        return this;
    }

    /**
     * Sets the label provider for the element detail.
     * 
     * @param detailsLabelProvider
     *            the label provider.
     * @return this builder.
     */
    public FilteredListSelectionDialogBuilder detailsLabelProvider(ILabelProvider detailsLabelProvider) {
        this.detailsLabelProvider = detailsLabelProvider;
        return this;
    }

    /**
     * Allows the selection of multiple elements.
     * 
     * @return this builder.
     */
    public FilteredListSelectionDialogBuilder multiSelection() {
        this.multiSelection = true;
        return this;
    }

    /**
     * Allows the selection of a single element.
     * 
     * @return this builder.
     */
    public FilteredListSelectionDialogBuilder singleSelection() {
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
    public FilteredListSelectionDialogBuilder items(Iterable<?> items) {
        this.items = ImmutableList.copyOf(items);
        return this;
    }

    /**
     * Sets the extended content area creator, able to create a {@link Control} from a {@link Composite} parent.
     * 
     * @param extendedContentAreaCreator
     *            the creator.
     * @return this builder.
     */
    public FilteredListSelectionDialogBuilder extendedContentAreaCreator(Function<Composite, Control> extendedContentAreaCreator) {
        this.extendedContentAreaCreator = extendedContentAreaCreator;
        return this;
    }

    /**
     * Builds the dialog.
     * 
     * @return the dialog.
     */
    public FilteredItemsSelectionDialog build() {
        Preconditions.checkNotNull(shell);
        Preconditions.checkNotNull(multiSelection);
        Preconditions.checkNotNull(title);
        Preconditions.checkNotNull(message);
        Preconditions.checkNotNull(items);
        Preconditions.checkNotNull(listLabelProvider);
        FilteredItemsSelectionDialog dialog = new FilteredItemsSelectionDialog(shell, multiSelection) {

            @Override
            protected IStatus validateItem(Object item) {
                return Status.OK_STATUS;
            }

            @Override
            protected Comparator<?> getItemsComparator() {
                return new DefaultItemsComparator();
            }

            @Override
            public String getElementName(Object item) {
                return Strings.nullToEmpty(listLabelProvider.getText(item));
            }

            @Override
            protected IDialogSettings getDialogSettings() {
                return Dialogs.getDialogSettings(CommonUi.getDefault(), settingsKey);
            }

            @Override
            protected void fillContentProvider(AbstractContentProvider contentProvider, ItemsFilter itemsFilter,
                    IProgressMonitor progressMonitor) throws CoreException {
                progressMonitor.beginTask("Filling item list", items.size());
                for (Object item : items) {
                    if (itemsFilter.isConsistentItem(item)) {
                        contentProvider.add(item, itemsFilter);
                    }
                    progressMonitor.worked(1);
                }
                progressMonitor.done();
            }

            @Override
            protected ItemsFilter createFilter() {
                return new DefaultItemsFilter();
            }

            @Override
            protected Control createExtendedContentArea(Composite parent) {
                if (extendedContentAreaCreator != null) {
                    return extendedContentAreaCreator.apply(parent);
                }
                return null;
            }

            class DefaultItemsFilter extends ItemsFilter {

                @Override
                public String getPattern() {
                    return Strings.isNullOrEmpty(super.getPattern()) ? "**" : super.getPattern();
                }

                @Override
                public boolean isConsistentItem(Object item) {
                    return true;
                }

                @Override
                public boolean matchItem(Object item) {
                    return matches(getElementName(item));
                }
            }

            class DefaultItemsComparator implements Comparator<Object> {

                @Override
                public int compare(Object o1, Object o2) {
                    String s1 = Strings.nullToEmpty(listLabelProvider.getText(o1));
                    String s2 = Strings.nullToEmpty(listLabelProvider.getText(o2));
                    return s1.compareToIgnoreCase(s2);
                }

            }

        };
        dialog.setInitialPattern("**");
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setListLabelProvider(listLabelProvider);
        dialog.setDetailsLabelProvider((detailsLabelProvider != null) ? detailsLabelProvider : listLabelProvider);
        return dialog;
    }

}
