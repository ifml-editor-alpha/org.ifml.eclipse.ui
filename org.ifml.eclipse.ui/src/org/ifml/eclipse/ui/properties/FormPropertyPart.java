package org.ifml.eclipse.ui.properties;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;

/**
 * A form panel displaying a set of properties in a property section.
 */
public final class FormPropertyPart implements ITabbedPropertyConstants {

    private final Composite parent;

    private final TabbedPropertySheetWidgetFactory widgetFactory;

    private Composite composite;

    /**
     * Constructs a new panel.
     * 
     * @param parent
     *            the parent composite for the section.
     * @param tabbedPropertySheetPage
     *            the property sheet page.
     */
    public FormPropertyPart(Composite parent, TabbedPropertySheetPage tabbedPropertySheetPage) {
        this.parent = parent;
        this.widgetFactory = tabbedPropertySheetPage.getWidgetFactory();
    }

    /**
     * Adds a check box property.
     * 
     * @param label
     *            the property label.
     * @return the newly created check box button.
     */
    public Button addCheckBox(String label) {
        createCompositeIfNeeded();
        addLabel(label);
        Button button = widgetFactory.createButton(composite, "", SWT.CHECK);
        GridDataFactory.swtDefaults().align(SWT.BEGINNING, SWT.CENTER).grab(true, false).applyTo(button);
        return button;
    }

    /**
     * Adds a text property.
     * 
     * @param label
     *            the property label.
     * @return the newly created text widget.
     */
    public Text addText(String label) {
        createCompositeIfNeeded();
        addLabel(label);
        Text text = widgetFactory.createText(composite, "");
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(text);
        return text;
    }

    /**
     * Adds an externally editable property, composed by a read-only text widget and a button able to start the edit.
     * 
     * @param label
     *            the property label.
     * @return the newly created widget.
     */
    public FormExternalPropertyPart addExternalProperty(String label) {
        createCompositeIfNeeded();
        addLabel(label);
        FormExternalPropertyPart part = new FormExternalPropertyPart(composite, widgetFactory);
        GridDataFactory.swtDefaults().applyTo(part.getComposite());
        return part;
    }

    /**
     * Adds a {@link CCombo} property.
     * 
     * @param label
     *            the property label.
     * @return the newly created text widget.
     */
    public ComboViewer addCCombo(String label) {
        createCompositeIfNeeded();
        addLabel(label);
        CCombo combo = widgetFactory.createCCombo(composite, SWT.READ_ONLY);
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(combo);
        return new ComboViewer(combo);
    }

    private void addLabel(String label) {
        CLabel widgetLabel = widgetFactory.createCLabel(composite, label + ":");
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(false, false).applyTo(widgetLabel);

    }

    private void createCompositeIfNeeded() {
        if (composite == null) {
            composite = widgetFactory.createComposite(parent);
            GridLayoutFactory.swtDefaults().numColumns(2).applyTo(composite);
        }
    }

}
