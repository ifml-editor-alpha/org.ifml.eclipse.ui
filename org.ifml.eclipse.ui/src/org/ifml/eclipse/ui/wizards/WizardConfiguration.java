package org.ifml.eclipse.ui.wizards;

import org.eclipse.jface.resource.ImageDescriptor;

import com.google.common.base.Strings;

/**
 * A wizard configuration stores settings that control how a wizard is rendered.
 */
public final class WizardConfiguration {

    /** The default page image descriptor. */
    public final ImageDescriptor defaultPageImageDescriptor;

    /** Indicates whether the wizard requires the {@code Previous} and {@code Next} buttons also in case of a single page. */
    public final boolean forcePreviousAndNextButtons;

    /** Indicates whether the wizard requires a progress monitor. */
    public final boolean needsProgressMonitor;

    /** The title of the window hosting the wizard. */
    public final String windowTitle;

    private WizardConfiguration(Builder builder) {
        this.defaultPageImageDescriptor = builder.defaultPageImageDescriptor;
        this.forcePreviousAndNextButtons = builder.forcePreviousAndNextButtons;
        this.needsProgressMonitor = builder.needsProgressMonitor;
        this.windowTitle = Strings.nullToEmpty(builder.windowTitle);
    }

    /**
     * A builder for {@link WizardConfiguration}.
     * <p>
     * The initial settings of a newly created builder is equivalent to a builder configured in the following way:
     * 
     * <pre>
     * {@code new WizardConfiguration.Builder()
     *         .defaultPageImageDescriptor(null)
     *         .forcePreviousAndNextButtons(false)
     *         .needsProgressMonitor(false)
     *         .windowTitle("")
     * }
     * </pre>
     */
    public static class Builder {

        private ImageDescriptor defaultPageImageDescriptor;

        private boolean forcePreviousAndNextButtons = false;

        private boolean needsProgressMonitor = false;

        private String windowTitle;

        /**
         * Sets the default page descriptor.
         * 
         * @param defaultPageImageDescriptor
         *            the default page image descriptor.
         * @return this builder.
         */
        public Builder defaultPageImageDescriptor(ImageDescriptor defaultPageImageDescriptor) {
            this.defaultPageImageDescriptor = defaultPageImageDescriptor;
            return this;
        }

        /**
         * Sets whether the wizard requires the {@code Previous} and {@code Next} buttons also in case of a single page.
         * 
         * @param forcePreviousAndNextButtons
         *            indicates whether the wizard requires the {@code Previous} and {@code Next} buttons also in case of a single
         *            page.
         * @return this builder.
         */
        public Builder forcePreviousAndNextButtons(boolean forcePreviousAndNextButtons) {
            this.forcePreviousAndNextButtons = forcePreviousAndNextButtons;
            return this;
        }

        /**
         * Sets whether the wizard requires a progress monitor.
         * 
         * @param needsProgressMonitor
         *            indicates whether the wizard requires a progress monitor.
         * @return this builder.
         */
        public Builder needsProgressMonitor(boolean needsProgressMonitor) {
            this.needsProgressMonitor = needsProgressMonitor;
            return this;
        }

        /**
         * Sets the title of the window hosting the wizard.
         * 
         * @param windowTitle
         *            the title of the window hosting the wizard.
         * @return this builder.
         */
        public Builder windowTitle(String windowTitle) {
            this.windowTitle = windowTitle;
            return this;
        }

        /**
         * Builds a new wizard configuration.
         * 
         * @return the new wizard configuration.
         */
        public WizardConfiguration build() {
            return new WizardConfiguration(this);
        }

    }

}
