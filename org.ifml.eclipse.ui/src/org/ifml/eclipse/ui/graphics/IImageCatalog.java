package org.ifml.eclipse.ui.graphics;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;

/**
 * An image catalog maintains a mapping between symbolic image names and SWT image descriptors.
 */
public interface IImageCatalog {

    /**
     * Returns the image associated with the given key in this catalog, or {@code null} if none.
     * 
     * @param key
     *            the key
     * @return the image, or {@code null} if none.
     */
    Image get(String key);

    /**
     * Returns the descriptor associated with the given key in this registry, or {@code null} if none.
     * 
     * @param key
     *            the key
     * @return the descriptor, or {@code null} if none.
     */
    ImageDescriptor getDescriptor(String key);

    /**
     * Adds (or replaces) an image descriptor to this registry.
     * <p>
     * Based on the real class implementing this interface, provided by {@link Images#newDefaultImageCatalog()} and
     * {@link Images#newVersioningImageCatalog()}, this method could behave as {@link ImageRegistry#put(String, ImageDescriptor)} or
     * could just "archive" the old image.
     * 
     * @param key
     *            the key
     * @param descriptor
     *            the ImageDescriptor
     */
    void put(String key, ImageDescriptor descriptor);

    /**
     * Removes an image from this registry.
     * <p>
     * Based on the real class implementing this interface, provided by {@link Images#newDefaultImageCatalog()} and
     * {@link Images#newVersioningImageCatalog()}, this method could either dispose the image (if any) associated with this key or
     * could just stop serving the image.
     * 
     * @param key
     *            the key
     */
    void remove(String key);

}
