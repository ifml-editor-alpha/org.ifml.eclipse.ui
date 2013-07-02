package org.ifml.eclipse.ui.properties;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;

/**
 * A form cell displaying a read-only text and a button able to start the external editing of a property.
 */
public final class FormExternalPropertyPart {

    private final Composite composite;

    private final Text text;

    private final Button button;

    /**
     * Constructs a new property part.
     * 
     * @param parent
     *            the parent composite.
     * @param widgetFactory
     *            the widget factory.
     */
    public FormExternalPropertyPart(Composite parent, TabbedPropertySheetWidgetFactory widgetFactory) {
        composite = widgetFactory.createComposite(parent);
        GridLayoutFactory.fillDefaults().numColumns(2).applyTo(composite);
        text = widgetFactory.createText(composite, "", SWT.READ_ONLY);
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(text);
        button = widgetFactory.createButton(composite, "...", SWT.PUSH);
        GridDataFactory.swtDefaults().grab(false, false).applyTo(button);
    }

    Control getComposite() {
        return composite;
    }

    /**
     * Returns the text widget.
     * 
     * @return the text widget.
     */
    public Widget getText() {
        return text;
    }

    /**
     * Returns the editing button.
     * 
     * @return the button.
     */
    public Button getButton() {
        return button;
    }

}
