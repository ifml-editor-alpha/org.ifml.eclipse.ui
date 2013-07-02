package org.ifml.eclipse.ui.wizards;

import java.net.URI;

import javax.annotation.Nullable;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.dialogs.WizardNewProjectReferencePage;

/**
 * Provides utility methods for predefined wizards dealing able to create workspace resources.
 */
public final class NewResourceWizards {

    private NewResourceWizards() {
    }

    /**
     * Creates a project description using the information provided by a project creation page and, if not null, by a project reference
     * page.
     * 
     * @param creationPage
     *            the project creation page.
     * @param referencePage
     *            the project reference page.
     * @return the new project description.
     */
    public static IProjectDescription createProjectDescription(WizardNewProjectCreationPage creationPage,
            @Nullable WizardNewProjectReferencePage referencePage) {
        IProject newProjectHandle = creationPage.getProjectHandle();
        URI location = null;
        if (!creationPage.useDefaults()) {
            location = creationPage.getLocationURI();
        }
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IProjectDescription description = workspace.newProjectDescription(newProjectHandle.getName());
        description.setLocationURI(location);
        if (referencePage != null) {
            IProject[] refProjects = referencePage.getReferencedProjects();
            if (refProjects.length > 0) {
                description.setReferencedProjects(refProjects);
            }
        }
        return description;
    }

}
