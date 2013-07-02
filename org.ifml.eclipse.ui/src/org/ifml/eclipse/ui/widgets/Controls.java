package org.ifml.eclipse.ui.widgets;

import javax.annotation.Nullable;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.PixelConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Text;
import org.ifml.base.ImmutablePair;
import org.ifml.base.Objects2;
import org.ifml.eclipse.core.runtime.Logs;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * Provides utility-methods for {@link Control}.
 */
public final class Controls {

    /**
     * Recursively initializes all the scrolled composites descending from the {@code control} argument.
     * <p>
     * This method should be called as late as possible, when the control is about to be displayed for the first time.
     * 
     * @param control
     *            the control to apply the initialization to.
     */
    public static void initializeScrolledComposites(Control control) {
        if (control instanceof Composite) {
            Control[] children = ((Composite) control).getChildren();
            for (int i = 0; i < children.length; i++) {
                initializeScrolledComposites(children[i]);
            }
        }
        if (control instanceof ScrolledComposite) {
            initializeScrolledComposite((ScrolledComposite) control);
        }
    }

    /**
     * Initializes the given composite by setting its minimum size (based on the size of its control) and by adding a resize listener
     * able to recompute the minimum size.
     * <p>
     * This method should be called as late as possible, when the scrolled composite is about to be displayed for the first time.
     * 
     * @param scrolledComposite
     *            the scrolled composite to initialize.
     * @deprecated use {@link #initializeScrolledComposite(ScrolledComposite, boolean, boolean)}
     */
    @Deprecated
    public static void initializeScrolledComposite(final ScrolledComposite scrolledComposite) {
        scrolledComposite.setMinSize(scrolledComposite.getContent().computeSize(SWT.DEFAULT, SWT.DEFAULT));
        scrolledComposite.getContent().addListener(SWT.Resize, new Listener() {
            @Override
            public void handleEvent(Event event) {
                scrolledComposite.setMinSize(scrolledComposite.getContent().computeSize(SWT.DEFAULT, SWT.DEFAULT));
            }
        });
    }

    /**
     * Initializes the given composite by setting its minimum size (based on the size of its control if default flag is true or based
     * on the current size of the scrolling composite) and by adding a resize listener able to recompute the minimum size.
     * <p>
     * This method should be called as late as possible, when the scrolled composite is about to be displayed for the first time.
     * 
     * @param scrolledComposite
     *            the scrolled composite to initialize.
     * @param useDefaultWidth
     *            if true the minimum width will be recomputed basing on the default required width of the content composite, on the
     *            current scrolled composite width otherwise.
     * @param useDefaultHeight
     *            if true the minimum height will be recomputed basing on the default required height of the content composite, on the
     *            current scrolled composite height otherwise.
     */
    public static void initializeScrolledComposite(final ScrolledComposite scrolledComposite, final boolean useDefaultWidth,
            final boolean useDefaultHeight) {
        scrolledComposite.setMinSize(scrolledComposite.getContent().computeSize(SWT.DEFAULT, SWT.DEFAULT));
        Listener listener = new Listener() {
            @Override
            public void handleEvent(Event event) {
                Rectangle bounds = scrolledComposite.getBounds();
                ScrollBar bar = null;
                bar = scrolledComposite.getVerticalBar();
                if (bar != null && !bar.isDisposed() && bar.isVisible()) {
                    bounds.width -= bar.getSize().x;
                }
                bar = scrolledComposite.getHorizontalBar();
                if (bar != null && !bar.isDisposed() && bar.isVisible()) {
                    bounds.height -= bar.getSize().y;
                }
                scrolledComposite.setMinSize(scrolledComposite.getContent().computeSize(useDefaultWidth ? SWT.DEFAULT : bounds.width,
                        useDefaultHeight ? SWT.DEFAULT : bounds.height));
            }
        };
        // TODO: experiment
        // if ("".equals("")) {
        // scrolledComposite.getContent().addListener(SWT.Resize, new Listener() {
        // public void handleEvent(Event event) {
        // scrolledComposite.setMinSize(scrolledComposite.getContent().computeSize(SWT.DEFAULT, SWT.DEFAULT));
        // }
        // });
        // } else {
        // scrolledComposite.getContent().addListener(SWT.Resize, listener);
        // }
        scrolledComposite.addListener(SWT.Resize, listener);
    }

    /**
     * Forces the given scrolled composite to recompute itself.
     * <p>
     * If the nested component is a {@link Composite} then {@link Composite#layout(boolean, boolean) layout(true, true)} is invoked.
     * 
     * @param scrolledComposite
     *            the scrolled composite to relayout.
     */
    public static void relayout(ScrolledComposite scrolledComposite) {
        Composite content = Objects2.as(scrolledComposite.getContent(), Composite.class);
        if (content != null) {
            content.layout(true, true);
        }
        scrolledComposite.setMinSize(scrolledComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        // workaround
        Rectangle bounds = scrolledComposite.getBounds();
        bounds.width += 2;
        scrolledComposite.setBounds(bounds);
        bounds.width -= 2;
        scrolledComposite.setBounds(bounds);
    }

    /**
     * Updates the value of a text field. Does nothing if the new value is equal to the old value. Empty and {@code null} strings are
     * treated as the same value.
     * 
     * @param text
     *            the text field.
     * @param newValue
     *            the new value.
     */
    public static void setText(Text text, @Nullable String newValue) {
        String oldValue = Strings.nullToEmpty(text.getText());
        newValue = Strings.nullToEmpty(newValue);
        if (!newValue.equals(oldValue)) {
            text.setText(newValue);
        }
    }

    /**
     * Returns the extent of the given string. Tab expansion and carriage return processing are performed.
     * <p>
     * The <em>extent</em> of a string is the width and height of the rectangular area it would cover if drawn in a particular font (in
     * this case, the current font in the receiver).
     * </p>
     * In case of exception this method returns an empty point logging the error.
     * 
     * 
     * @param string
     *            the string to measure
     * @param control
     *            the control where the given string will be shown
     * @return a point containing the extent of the string
     */
    public static final Point textExtent(@Nullable String string, @Nullable Control control) { // TODO:move to common class
        GC gc = null;
        Point pt = new Point(0, 0);
        try {
            if (control == null) {
                control = Displays.getDisplay().getActiveShell();
            }
            gc = new GC(control.getDisplay());
            gc.setFont(control.getFont());
            pt = gc.textExtent(Strings.nullToEmpty(string));
            pt.x = Math.max(pt.x, 1);
            pt.y = Math.max(pt.y, 1);
        } catch (Throwable exception) {
            Logs.logError(exception, null, null);
            return new Point(0, 0);
        } finally {
            try {
                if (gc != null && !gc.isDisposed()) {
                    gc.dispose();
                }
            } catch (Throwable exception) {
                Logs.logError(exception, null, null);
            }
        }
        return pt;
    }

    /**
     * Adds a context menu to a control.
     * 
     * @param control
     *            a control.
     * @param contextMenuProvider
     *            the object able to fill the context menu.
     */
    public static final void addContextMenu(Control control, final IContextMenuProvider contextMenuProvider) {
        MenuManager popupMenuManager = new MenuManager();
        IMenuListener listener = new IMenuListener() {
            @Override
            public void menuAboutToShow(IMenuManager menuMgr) {
                contextMenuProvider.fillContextMenu(menuMgr);
            }
        };
        popupMenuManager.addMenuListener(listener);
        popupMenuManager.setRemoveAllWhenShown(true);
        Menu menu = popupMenuManager.createContextMenu(control);
        control.setMenu(menu);
    }

    /**
     * Sets a hint on the button layout data, based on DLUs defined as {@link IDialogConstants#BUTTON_WIDTH}.
     * <p>
     * This method uses the font currently set on the button and works only if the layout data is a {@link GridData}.
     * 
     * @param button
     *            the button.
     */
    public static final void setDefaultWidthHint(Button button) {
        Object layoutData = button.getLayoutData();
        if (layoutData instanceof GridData) {
            PixelConverter converter = new PixelConverter(button);
            int widthHint = converter.convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
            ((GridData) layoutData).widthHint = Math.max(widthHint, button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
        }
    }

    /**
     * Instances of the receiver represent a selectable user interface object that allows the user to drag a rubber banded outline of
     * the sash within the parent control. This method creates and returns the two new composite children of a given parent composite.
     * 
     * @param parent
     *            the parent composite.
     * @param isVertical
     *            indicates the vertical or horizontal sash style.
     * @param initialValue
     *            the initial value to use to divide the parent area.
     * @param firstChildStyle
     *            the style of first child.
     * @param secondChildStyle
     *            the style of second child.
     * @return the {@link ImmutablePair} containing the two new create children.
     * 
     * @deprecated use createSplittedPanels
     */
    @Deprecated
    public static final ImmutablePair<Composite, Composite> createSplit(Composite parent, boolean isVertical, int initialValue,
            int firstChildStyle, int secondChildStyle) {
        SplittedPanels splittedPanels = createSplittedPanels(parent, isVertical, initialValue, firstChildStyle, secondChildStyle);
        return ImmutablePair.of(splittedPanels.first, splittedPanels.second);
    }

    public static final SplittedPanels createSplittedPanels(Composite parent, boolean isVertical, int initialValue,
            int firstChildStyle, int secondChildStyle) {
        Preconditions.checkNotNull(parent);
        if (isVertical) {
            return createVerticalSplit(parent, initialValue, firstChildStyle, secondChildStyle);
        } else {
            return createHorizontalSplit(parent, initialValue, firstChildStyle, secondChildStyle);
        }
    }

    public static final class SplittedPanels {

        public final Composite first;

        public final Composite second;

        public final Sash sash;

        private SplittedPanels(Composite firstPanel, Composite secondPanel, Sash sash) {
            this.first = firstPanel;
            this.second = secondPanel;
            this.sash = sash;
            Preconditions.checkNotNull(firstPanel);
            Preconditions.checkNotNull(secondPanel);
            Preconditions.checkNotNull(sash);
        }

    }

    private static final SplittedPanels createVerticalSplit(Composite composite, int initialValue, int comp0Style, int comp1Style) {
        composite.setLayout(new FormLayout());

        // Create the sash first, so the other controls
        // can be attached to it.
        final Sash sash = new Sash(composite, SWT.HORIZONTAL);
        sash.setBackground(sash.getParent().getBackground());
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
        Composite comp0 = new Composite(composite, comp0Style);
        data = new FormData();
        data.top = new FormAttachment(0, 0);
        data.bottom = new FormAttachment(sash, 0);
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        comp0.setLayoutData(data);

        // Create the second text box and attach its left edge
        // to the sash
        Composite comp1 = new Composite(composite, comp1Style);
        data = new FormData();
        data.top = new FormAttachment(sash, 0);
        data.bottom = new FormAttachment(100, 0);
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        comp1.setLayoutData(data);

        return new SplittedPanels(comp0, comp1, sash);
    }

    private static final SplittedPanels createHorizontalSplit(Composite composite, int initialValue, int comp0Style, int comp1Style) {
        composite.setLayout(new FormLayout());

        // Create the sash first, so the other controls
        // can be attached to it.
        final Sash sash = new Sash(composite, SWT.VERTICAL);
        sash.setBackground(sash.getParent().getBackground());
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
        Composite comp0 = new Composite(composite, comp0Style);
        data = new FormData();
        data.top = new FormAttachment(0, 0);
        data.bottom = new FormAttachment(100, 0);
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(sash, 0);
        comp0.setLayoutData(data);

        // Create the second text box and attach its left edge
        // to the sash
        Composite comp1 = new Composite(composite, comp1Style);
        data = new FormData();
        data.top = new FormAttachment(0, 0);
        data.bottom = new FormAttachment(100, 0);
        data.left = new FormAttachment(sash, 0);
        data.right = new FormAttachment(100, 0);
        comp1.setLayoutData(data);

        return new SplittedPanels(comp0, comp1, sash);
    }

    /**
     * Add a wheel listener to the given scrolled composite and to all its children recursively.
     * 
     * @param scrolledComposite
     *            the scrolled composite
     * @param vertical
     *            if vertical
     */
    public static final void addRedirectWheelListener(final ScrolledComposite scrolledComposite, final boolean vertical) {
        final Listener wheelListener = new Listener() {

            @Override
            public void handleEvent(Event event) {
                if (!(event.widget instanceof Control)) {
                    return;
                }
                final Control control = (Control) event.widget;
                if (scrolledComposite == null || scrolledComposite.isDisposed() || control == null || control.isDisposed()) {
                    return;
                }
                ScrollBar bar = (vertical ? scrolledComposite.getVerticalBar() : scrolledComposite.getHorizontalBar());
                if (bar != null && !bar.isDisposed()) {
                    // System.out.println("scroll: " + targetControl);// TODO
                    bar.setSelection(bar.getSelection() - event.count * bar.getIncrement());
                    Listener[] listeners = bar.getListeners(SWT.Selection);
                    if (listeners == null) {
                        return;
                    }
                    for (Listener l : listeners) {
                        l.handleEvent(event);// redirect
                    }
                }
            }

        };
        addRedirectWheelListener(scrolledComposite, scrolledComposite, wheelListener, vertical);
    }

    private static final void addRedirectWheelListener(final Composite parent, final ScrolledComposite scrolledComposite,
            final Listener wheelListener, final boolean vertical) {
        if (parent == null || parent.isDisposed()) {
            return;
        }
        Control[] children = parent.getChildren();
        if (children == null || children.length <= 0) {
            return;
        }
        for (Control child : children) {
            child.addListener(vertical ? SWT.MouseVerticalWheel : SWT.MouseHorizontalWheel, wheelListener);
            if (child instanceof Composite) {
                addRedirectWheelListener((Composite) child, scrolledComposite, wheelListener, vertical);
            }
        }
    }
}
