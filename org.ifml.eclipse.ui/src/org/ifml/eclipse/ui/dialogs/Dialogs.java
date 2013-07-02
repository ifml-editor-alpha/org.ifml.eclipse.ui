package org.ifml.eclipse.ui.dialogs;

import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.ifml.base.ImmutablePair;
import org.ifml.eclipse.ui.widgets.Controls;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

/**
 * Provides utility methods for dialogs.
 */
public class Dialogs {

    private static final int DEFAULT_VIEWER_WIDTH_IN_CHARS = 50;

    private static final int DEFAULT_VIEWER_HEIGHT_IN_CHARS = 15;

    private static final Map<Integer, String> BUTTON_LABELS;

    static {
        Map<Integer, String> map = Maps.newHashMap();
        map.put(IDialogConstants.ABORT_ID, IDialogConstants.ABORT_LABEL);
        map.put(IDialogConstants.BACK_ID, IDialogConstants.BACK_LABEL);
        map.put(IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL);
        map.put(IDialogConstants.CLOSE_ID, IDialogConstants.CLOSE_LABEL);
        map.put(IDialogConstants.FINISH_ID, IDialogConstants.FINISH_LABEL);
        map.put(IDialogConstants.HELP_ID, IDialogConstants.HELP_LABEL);
        map.put(IDialogConstants.IGNORE_ID, IDialogConstants.IGNORE_LABEL);
        map.put(IDialogConstants.NEXT_ID, IDialogConstants.NEXT_LABEL);
        map.put(IDialogConstants.NO_ID, IDialogConstants.NO_LABEL);
        map.put(IDialogConstants.NO_TO_ALL_ID, IDialogConstants.NO_TO_ALL_LABEL);
        map.put(IDialogConstants.OK_ID, IDialogConstants.OK_LABEL);
        map.put(IDialogConstants.OPEN_ID, IDialogConstants.OPEN_LABEL);
        map.put(IDialogConstants.PROCEED_ID, IDialogConstants.PROCEED_LABEL);
        map.put(IDialogConstants.RETRY_ID, IDialogConstants.RETRY_LABEL);
        map.put(IDialogConstants.SKIP_ID, IDialogConstants.SKIP_LABEL);
        map.put(IDialogConstants.STOP_ID, IDialogConstants.STOP_LABEL);
        map.put(IDialogConstants.YES_ID, IDialogConstants.YES_LABEL);
        map.put(IDialogConstants.YES_TO_ALL_ID, IDialogConstants.YES_TO_ALL_LABEL);
        BUTTON_LABELS = ImmutableMap.copyOf(map);
    }

    private Dialogs() {
    }

    /**
     * Returns the dialog settings stored in a specific UI plug-in under a specific key.
     * 
     * @param plugin
     *            the UI plug-in storing dialog settings.
     * @param settingsKey
     *            the key identifying a specific group of settings.
     * @return the dialog settings.
     */
    public static synchronized IDialogSettings getDialogSettings(AbstractUIPlugin plugin, String settingsKey) {
        IDialogSettings globalSettings = plugin.getDialogSettings();
        IDialogSettings settings = globalSettings.getSection(settingsKey);
        if (settings == null) {
            settings = globalSettings.addNewSection(settingsKey);
        }
        return settings;
    }

    /**
     * Returns the label associated with a predefined button identifier, chosen among the identifiers defined in
     * {@link IDialogConstants}.
     * 
     * @param buttonId
     *            the button identifier, chosen among the identifiers defined in {@link IDialogConstants}.
     * 
     * @return the button label.
     */
    public static String getButtonLabel(int buttonId) {
        return BUTTON_LABELS.get(buttonId);
    }

    /**
     * Returns the number of pixels corresponding to the width and height of the given number of characters.
     * 
     * @param control
     *            the base control.
     * @param widthInChars
     *            the number of horizontal characters.
     * @param heightInChars
     *            the number of vertical characters.
     * @return the width and height in pixels.
     */
    public static Point charsToPixels(Control control, int widthInChars, int heightInChars) {
        GC gc = new GC(control);
        gc.setFont(JFaceResources.getDialogFont());
        FontMetrics fontMetrics = gc.getFontMetrics();
        gc.dispose();
        return new Point(Dialog.convertWidthInCharsToPixels(fontMetrics, widthInChars), Dialog.convertHeightInCharsToPixels(
                fontMetrics, heightInChars));
    }

    /**
     * Returns the number of pixels corresponding to the default width and height of a structured viewer.
     * 
     * @param viewer
     *            a structured viewer.
     * @return the default number of pixels.
     */
    public static Point defaultSize(Viewer viewer) {
        return charsToPixels(viewer.getControl(), DEFAULT_VIEWER_WIDTH_IN_CHARS, DEFAULT_VIEWER_HEIGHT_IN_CHARS);
    }

    /**
     * Create two new components splitting vertically a given parent composite
     * 
     * @param composite
     *            the parent composite
     * @param initialValue
     *            the initial split value
     * @param comp0Style
     *            style of first child
     * @param comp1Style
     *            style of second child
     * @return the immutable pair containing the two new composites.
     * 
     *         use Controls.createSplit
     */
    @Deprecated
    public static final ImmutablePair<Composite, Composite> createVerticalSplit(Composite composite, int initialValue, int comp0Style,
            int comp1Style) {
        if ("".equals("")) {
            ImmutablePair<Composite, Composite> res = Controls.createSplit(composite, true, initialValue, comp0Style, comp1Style);
            ((FormData) ((Sash) ((FormData) res.first.getLayoutData()).bottom.control).getLayoutData()).top.numerator = initialValue;
            return res;
        }

        composite.setLayout(new FormLayout());

        // Create the sash first, so the other controls
        // can be attached to it.
        final Sash sash = new Sash(composite, SWT.HORIZONTAL);
        FormData data = new FormData();
        data.top = new FormAttachment(initialValue, 0); // Attach to top
        data.right = new FormAttachment(100, 0); // Attach to bottom
        data.left = new FormAttachment(0, 0); // Attach halfway across
        sash.setLayoutData(data);
        sash.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                // We reattach to the left edge, and we use the x value of the event to
                // determine the offset from the left
                ((FormData) sash.getLayoutData()).top = new FormAttachment(0, event.y);

                // Until the parent window does a layout, the sash will not be redrawn in
                // its new location.
                sash.getParent().layout();
            }
        });

        // Create the first text box and attach its right edge
        // to the sash
        Composite comp0 = new _SplitChildComposite(composite, comp0Style);
        data = new FormData();
        data.top = new FormAttachment(0, 0);
        data.bottom = new FormAttachment(sash, 0);
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        comp0.setLayoutData(data);

        // Create the second text box and attach its left edge
        // to the sash
        Composite comp1 = new _SplitChildComposite(composite, comp1Style);
        data = new FormData();
        data.top = new FormAttachment(sash, 0);
        data.bottom = new FormAttachment(100, 0);
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        comp1.setLayoutData(data);

        return ImmutablePair.of(comp0, comp1);
    }

    /**
     * Create two new components splitting vertically a given parent composite
     * 
     * @param composite
     *            the parent composite
     * @param initialValue
     *            the initial split value
     * @param comp0Style
     *            style of first child
     * @param comp1Style
     *            style of second child
     * @return the immutable pair containing the two new composites.
     * 
     *         use Controls.createSplit
     */
    @Deprecated
    public static final ImmutablePair<Composite, Composite> createHorizontalSplit(Composite composite, int initialValue,
            int comp0Style, int comp1Style) {
        if ("".equals("")) {
            ImmutablePair<Composite, Composite> res = Controls.createSplit(composite, false, initialValue, comp0Style, comp1Style);
            ((FormData) ((Sash) ((FormData) res.first.getLayoutData()).right.control).getLayoutData()).left.numerator = initialValue;
            return res;
        }

        composite.setLayout(new FormLayout());

        // Create the sash first, so the other controls
        // can be attached to it.
        final Sash sash = new Sash(composite, SWT.VERTICAL);
        FormData data = new FormData();
        data.top = new FormAttachment(0, 0); // Attach to top
        data.bottom = new FormAttachment(100, 0); // Attach to bottom
        data.left = new FormAttachment(initialValue, 0); // Attach halfway across
        sash.setLayoutData(data);
        sash.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                // We reattach to the left edge, and we use the x value of the event to
                // determine the offset from the left
                ((FormData) sash.getLayoutData()).left = new FormAttachment(0, event.x);

                // Until the parent window does a layout, the sash will not be redrawn in
                // its new location.
                sash.getParent().layout();
            }
        });

        // Create the first text box and attach its right edge
        // to the sash
        Composite comp0 = new _SplitChildComposite(composite, comp0Style);
        data = new FormData();
        data.top = new FormAttachment(0, 0);
        data.bottom = new FormAttachment(100, 0);
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(sash, 0);
        comp0.setLayoutData(data);

        // Create the second text box and attach its left edge
        // to the sash
        Composite comp1 = new _SplitChildComposite(composite, comp1Style);
        data = new FormData();
        data.top = new FormAttachment(0, 0);
        data.bottom = new FormAttachment(100, 0);
        data.left = new FormAttachment(sash, 0);
        data.right = new FormAttachment(100, 0);
        comp1.setLayoutData(data);

        return ImmutablePair.of(comp0, comp1);
    }

    private static final class _SplitChildComposite extends Composite {

        public _SplitChildComposite(Composite parent, int style) {
            super(parent, style);
        }

        @Override
        public void setLayoutData(Object layoutData) {
            if (layoutData != null) {
                Preconditions.checkArgument(layoutData instanceof FormData);
            }
            super.setLayoutData(layoutData);
        }

    }
}
