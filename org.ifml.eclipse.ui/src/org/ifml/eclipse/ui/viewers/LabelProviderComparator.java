package org.ifml.eclipse.ui.viewers;

import java.util.Comparator;

import org.eclipse.jface.viewers.ILabelProvider;

import com.google.common.base.Strings;

/**
 * An object comparator comparing two objects using the values returned by {@link ILabelProvider#getText(Object)}.
 */
public final class LabelProviderComparator implements Comparator<Object> {

    private final ILabelProvider labelProvider;

    /**
     * Creates a new comparator.
     * 
     * @param labelProvider
     *            the label provider.
     */
    public LabelProviderComparator(ILabelProvider labelProvider) {
        this.labelProvider = labelProvider;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(Object o1, Object o2) {
        String s1 = Strings.nullToEmpty(labelProvider.getText(o1));
        String s2 = Strings.nullToEmpty(labelProvider.getText(o2));
        return s1.compareToIgnoreCase(s2);
    }

}
