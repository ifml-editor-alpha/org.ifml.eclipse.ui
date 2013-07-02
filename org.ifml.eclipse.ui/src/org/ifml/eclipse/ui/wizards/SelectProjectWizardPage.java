package org.ifml.eclipse.ui.wizards;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.ifml.eclipse.core.runtime.Adaptables;
import org.ifml.eclipse.ui.dialogs.Dialogs;
import org.ifml.eclipse.ui.viewers.Selections;
import org.ifml.eclipse.ui.widgets.HyperlinkBuilder;
import org.ifml.eclipse.ui.widgets.LabelBuilder;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.Sets;

/**
 * A wizard page displaying a list of projects, with an optional hyperlink to create a new project.
 */
public final class SelectProjectWizardPage extends CommonWizardPage {

    private final IStructuredSelection selection;

    private final Supplier<List<IProject>> projectsSupplier;

    private final String message;

    private final Callable<IProject> creationLinkCallable;

    private final String creationLinkText;

    private final Image creationLinkImage;

    private TableViewer projectsViewer;

    private final ILabelProvider labelProvider;

    private SelectProjectWizardPage(Builder builder) {
        Preconditions.checkNotNull(builder.title);
        Preconditions.checkNotNull(builder.description);
        Preconditions.checkNotNull(builder.message);
        Preconditions.checkNotNull(builder.projectsSupplier);
        setTitle(builder.title);
        setDescription(builder.description);
        this.selection = Selections.toStructuredSelection(builder.selection);
        this.projectsSupplier = builder.projectsSupplier;
        this.labelProvider = Objects.firstNonNull(builder.labelProvider, new WorkbenchLabelProvider());
        this.message = builder.message;
        this.creationLinkCallable = builder.creationLinkCallable;
        this.creationLinkText = builder.creationLinkText;
        this.creationLinkImage = builder.creationLinkImage;
        if (creationLinkCallable != null) {
            Preconditions.checkNotNull(creationLinkText);
            Preconditions.checkNotNull(creationLinkImage);
        }
    }

    @Override
    protected Control doCreateControl(Composite parent) {
        Composite mainArea = new Composite(parent, SWT.NONE);
        GridDataFactory.fillDefaults().grab(true, true).applyTo(mainArea);
        GridLayoutFactory.swtDefaults().applyTo(mainArea);

        /* creates the top label */
        Label topLabel = new LabelBuilder().text(message).build(mainArea);
        GridDataFactory.swtDefaults().applyTo(topLabel);

        /* creates the projects table */
        Table projectsTable = new Table(mainArea, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        projectsViewer = new TableViewer(projectsTable);
        projectsViewer.setContentProvider(ArrayContentProvider.getInstance());
        projectsViewer.setLabelProvider(labelProvider);
        projectsViewer.setSorter(new ViewerSorter());
        List<IProject> projects = projectsSupplier.get();
        projectsViewer.setInput(projects);
        selectAndRevel(selection, projects, projectsViewer);
        GridDataFactory.fillDefaults().grab(true, true).hint(Dialogs.defaultSize(projectsViewer)).applyTo(projectsTable);
        projectsViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                setPageComplete(validatePage());
            }
        });
        projectsViewer.addDoubleClickListener(new IDoubleClickListener() {

            @Override
            public void doubleClick(DoubleClickEvent event) {
                if (validatePage()) {
                    advanceToNextOrFinish();
                }
            }
        });

        /* creates the link to the new components project wizard */
        if (creationLinkCallable != null) {
            Composite creationArea = new Composite(mainArea, SWT.NONE);
            GridDataFactory.swtDefaults().grab(true, false).applyTo(creationArea);
            GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(false).applyTo(creationArea);
            new LabelBuilder().image(creationLinkImage).build(creationArea);
            Hyperlink link = new HyperlinkBuilder().text(creationLinkText).build(creationArea);
            link.addHyperlinkListener(new HyperlinkAdapter() {

                @Override
                public void linkActivated(HyperlinkEvent e) {
                    try {
                        IProject newProject = creationLinkCallable.call();
                        List<IProject> projects = projectsSupplier.get();
                        projectsViewer.setInput(projects);
                        selectAndRevel(new StructuredSelection(newProject), projects, projectsViewer);
                        setPageComplete(validatePage());
                    } catch (Exception e2) {
                    }
                }
            });
            GridDataFactory.swtDefaults().applyTo(link);
        }

        /* selects the unique project (if any) */
        if (projects.size() == 1) {
            projectsViewer.setSelection(new StructuredSelection(projects.get(0)));
        }
        setPageComplete(validatePage());
        return mainArea;
    }

    private void selectAndRevel(IStructuredSelection selection, List<IProject> projects, TableViewer projectsViewer) {
        Set<IProject> set = Sets.newHashSet(projects);
        for (Iterator<?> i = selection.iterator(); i.hasNext();) {
            Object obj = i.next();
            IResource res = null;
            if (obj instanceof IResource) {
                res = (IResource) obj;
            } else if (obj instanceof IAdaptable) {
                res = Adaptables.getAdapter((IAdaptable) obj, IResource.class);
            }
            if (res != null) {
                IProject project = res.getProject();
                if (set.contains(project)) {
                    projectsViewer.setSelection(new StructuredSelection(project), true);
                    break;
                }
            }
        }

    }

    private boolean validatePage() {
        IProject project = getProject();
        if (project == null) {
            setMessage("No project selected.");
            return false;
        }
        setMessage(null);
        setErrorMessage(null);
        return true;
    }

    /**
     * Returns the selected project.
     * 
     * @return the selected project or {@code null} if none is selected.
     */
    public IProject getProject() {
        return (IProject) Selections.getSingleElement(projectsViewer);
    }

    /**
     * A builder for the {@link SelectProjectWizardPage}.
     */
    public static final class Builder {

        private IStructuredSelection selection;

        private Supplier<List<IProject>> projectsSupplier;

        private ILabelProvider labelProvider;

        private String title;

        private String description;

        private String message;

        private Callable<IProject> creationLinkCallable;

        private String creationLinkText;

        private Image creationLinkImage;

        /**
         * Sets the workbench selection.
         * 
         * @param selection
         *            the workbench selection.
         * @return this builder.
         */
        public Builder selection(IStructuredSelection selection) {
            this.selection = selection;
            return this;
        }

        /**
         * Sets the supplier able to provide the list of projects.
         * 
         * @param projectsSupplier
         *            the supplier able to provide the list of projects.
         * @return this builder.
         */
        public Builder projectsSupplier(Supplier<List<IProject>> projectsSupplier) {
            this.projectsSupplier = projectsSupplier;
            return this;
        }

        /**
         * Sets the label provider, in order to replace the default {@link WorkbenchLabelProvider}.
         * 
         * @param labelProvider
         *            the label provider.
         * @return this builder.
         */
        public Builder labelProvider(ILabelProvider labelProvider) {
            this.labelProvider = labelProvider;
            return this;
        }

        /**
         * Sets the title of the page.
         * 
         * @param title
         *            the title of the page.
         * @return this builder.
         */
        public Builder title(String title) {
            this.title = title;
            return this;
        }

        /**
         * Sets the description of the page.
         * 
         * @param description
         *            the description of the page.
         * @return this builder.
         */
        public Builder description(String description) {
            this.description = description;
            return this;
        }

        /**
         * Sets the message displayed at the top of the selection list.
         * 
         * @param message
         *            the message displayed at the top of the selection list.
         * @return this builder.
         */
        public Builder message(String message) {
            this.message = message;
            return this;
        }

        /**
         * Sets the text of the creation link.
         * 
         * @param creationLinkCallable
         *            the callable to be executed when the creation link is selected.
         * @param creationLinkImage
         *            the image of the creation link
         * @param creationLinkText
         *            the text of the creation link.
         * @return this builder.
         */
        public Builder creationLink(Callable<IProject> creationLinkCallable, Image creationLinkImage, String creationLinkText) {
            this.creationLinkCallable = creationLinkCallable;
            this.creationLinkText = creationLinkText;
            this.creationLinkImage = creationLinkImage;
            return this;
        }

        /**
         * Creates the wizard page.
         * 
         * @return the new wizard page.
         */
        public SelectProjectWizardPage build() {
            return new SelectProjectWizardPage(this);
        }

    }

}
