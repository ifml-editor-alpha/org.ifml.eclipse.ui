package org.ifml.eclipse.ui.graphics;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.jface.resource.CompositeImageDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.io.Closeables;
import com.google.common.io.InputSupplier;

/**
 * Provides utility methods for image descriptors.
 */
public final class Images {

    private Images() {
    }

    /**
     * Loads an image from a byte stream supplier.
     * <p>
     * Callers are responsible for disposing the image when it is no longer needed.
     * 
     * @param input
     *            the byte stream supplier.
     * @param device
     *            the device.
     * @return the image.
     * @throws SWTException
     *             if an error occurred loading the image.
     * @throws IOException
     *             if an error occurred loading the image.
     */
    public static Image loadImage(InputSupplier<InputStream> input, Device device) throws SWTException, IOException {
        InputStream in = null;
        try {
            in = input.getInput();
            return new Image(device, in);
        } finally {
            Closeables.closeQuietly(in);
        }
    }

    /**
     * Creates an image descriptor able to scale the image produced by an existing image descriptor.
     * <p>
     * Note that this method is not cached: multiple calls to this method for the same base image descriptor should be replaced by the
     * use of an {@link ImageRegistry}.
     * 
     * @param baseDescr
     *            the base image descriptor.
     * @param width
     *            the scaled width.
     * @param height
     *            the scaled height.
     * @return the new image descriptor.
     */
    public static ImageDescriptor newResizedDescriptor(ImageDescriptor baseDescr, int width, int height) {
        // TODO: if baseDescr has already the correct size, return it ?
        // TODO: try to use the code described in bug 2888
        // TODO: infinite loop when image is enlarged
        return new ResizedImageDescriptor(baseDescr, width, height);
    }

    /**
     * Creates an image descriptor able to place in the center of another image (width x height) an existing image descriptor.
     * 
     * @param baseDescr
     *            the base image descriptor.
     * @param width
     *            the scaled width.
     * @param height
     *            the scaled height.
     * @return the new image descriptor.
     */
    public static ImageDescriptor newExpandedDescriptor(ImageDescriptor baseDescr, int width, int height) {
        return new ExpandedImageDescriptor(baseDescr, width, height);
    }

    /**
     * Creates an image descriptor based on a original image descriptor but with a disabled look.
     * <p>
     * Note that this method is not cached: multiple calls to this method for the same base image descriptor should be replaced by the
     * use of an {@link ImageRegistry}.
     * 
     * @param baseDescr
     *            the base image descriptor.
     * @return the new image descriptor.
     */
    public static ImageDescriptor newDisabledDescriptor(ImageDescriptor baseDescr) {
        return ImageDescriptor.createWithFlags(baseDescr, SWT.IMAGE_DISABLE);
    }

    /**
     * Returns a new image registry.
     * 
     * @return a new image registry.
     * @throws SWTError
     *             if the current thread is not the UI thread and the workbench is not yet created.
     */
    public static ImageRegistry newImageRegistry() {
        return new ImageRegistry(findDisplay());
    }

    /**
     * Returns a new image catalog, whose {@link IImageCatalog#put put} and {@link IImageCatalog#remove remove} methods behave as the
     * associated methods of {@link ImageRegistry}.
     * 
     * @return a new image catalog.
     * @throws SWTError
     *             if the current thread is not the UI thread and the workbench is not yet created.
     */
    public static IImageCatalog newDefaultImageCatalog() {
        return new ImageCatalog(findDisplay());
    }

    /**
     * Returns a new image catalog, whose {@link IImageCatalog#put put} and {@link IImageCatalog#remove remove} methods never disposes
     * old images; instead the most recent version of an image is stored/removed.
     * 
     * @return a new image catalog.
     * @throws SWTError
     *             if the current thread is not the UI thread and the workbench is not yet created.
     */
    public static IImageCatalog newVersioningImageCatalog() {
        return new VersioningImageCatalog(findDisplay());
    }

    private static Display findDisplay() {
        if (Display.getCurrent() != null) {
            return Display.getCurrent();
        }
        if (PlatformUI.isWorkbenchRunning()) {
            return PlatformUI.getWorkbench().getDisplay();
        }
        throw new SWTError(SWT.ERROR_THREAD_INVALID_ACCESS);
    }

    private static class ResizedImageDescriptor extends CompositeImageDescriptor {

        private final ImageDescriptor baseDescr;

        private final Point size;

        /**
         * Constructs a new resized image descriptor.
         * 
         * @param baseDescr
         *            the base image descriptor
         * @param width
         *            the scaled width.
         * @param height
         *            the scaled height.
         */
        public ResizedImageDescriptor(ImageDescriptor baseDescr, int width, int height) {
            Preconditions.checkNotNull(baseDescr);
            Preconditions.checkArgument(width > 0);
            Preconditions.checkArgument(height > 0);
            this.baseDescr = baseDescr;
            this.size = new Point(width, height);
        }

        @Override
        protected void drawCompositeImage(int width, int height) {
            ImageData bg = baseDescr.getImageData();
            float ratioWidth = (float) width / bg.width;
            float ratioHeight = (float) height / bg.height;
            float ratio = Math.min(ratioWidth, ratioHeight);
            int newWidth = (int) Math.max(bg.width * ratio, 1);
            int newHeight = (int) Math.max(bg.height * ratio, 1);
            bg = AwtImages.toImageData(AwtImages.newResizedImage(AwtImages.toBufferedImage(bg), newWidth, newHeight));
            int x = (width - bg.width) / 2;
            int y = (height - bg.height) / 2;
            drawImage(bg, x, y);
        }

        @Override
        protected Point getSize() {
            return size;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof ResizedImageDescriptor)) {
                return false;
            }
            ResizedImageDescriptor otherDescr = (ResizedImageDescriptor) obj;
            return baseDescr.equals(otherDescr.baseDescr) && size.equals(otherDescr.size);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(baseDescr, size);
        }

        @Override
        public String toString() {
            return Strings.nullToEmpty(baseDescr.toString()) + "[resized to" + size.x + "x" + size.y + "]";
        }

    }

    private static class ExpandedImageDescriptor extends CompositeImageDescriptor {

        private final ImageDescriptor baseDescr;

        private final Point size;

        /**
         * Constructs a new expanded image descriptor, placing the given base image in the center of another image having a given
         * dimension.
         * 
         * @param baseDescr
         *            the base image descriptor
         * @param width
         *            the scaled width.
         * @param height
         *            the scaled height.
         */
        public ExpandedImageDescriptor(ImageDescriptor baseDescr, int width, int height) {
            Preconditions.checkNotNull(baseDescr);
            Preconditions.checkArgument(width > 0);
            Preconditions.checkArgument(height > 0);
            this.baseDescr = baseDescr;
            this.size = new Point(width, height);
        }

        @Override
        protected void drawCompositeImage(int width, int height) {
            ImageData bg = baseDescr.getImageData();
            int x = (width - bg.width) / 2;
            int y = (height - bg.height) / 2;
            drawImage(bg, x, y);
        }

        @Override
        protected Point getSize() {
            return size;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof ExpandedImageDescriptor)) {
                return false;
            }
            ExpandedImageDescriptor otherDescr = (ExpandedImageDescriptor) obj;
            return baseDescr.equals(otherDescr.baseDescr) && size.equals(otherDescr.size);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(baseDescr, size);
        }

        @Override
        public String toString() {
            return Strings.nullToEmpty(baseDescr.toString()) + "[expanded to" + size.x + "x" + size.y + "]";
        }

    }

}
