package org.ifml.eclipse.ui.graphics;

import javax.annotation.Nullable;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

/**
 * The interface of objects able to provide images and image descriptors.
 * 
 * <p>
 * Concrete implementations of this interface must be provided by {@code Enum}s only, in order to correctly compute unique keys for
 * caching of base images and overlayed images. See {@link ImageProviders} for constraints and {@link CommonImage} as an example
 * 
 */
public interface IImageProvider {

    /**
     * Returns the image.
     * 
     * @return the image.
     */
    Image get();

    /**
     * Returns the disabled image.
     * 
     * @return the disabled image.
     */
    Image getDisabled();

    /**
     * Returns the image descriptor.
     * 
     * @return the image descriptor.
     */
    ImageDescriptor getDescriptor();

    /**
     * Returns the disabled image descriptor.
     * 
     * @return the disabled image descriptor.
     */
    ImageDescriptor getDisabledDescriptor();

    /**
     * Returns the same image provided by {@link #get()} optionally decorated with images on to the 4 corner quadrants.
     * 
     * @param topLeftOverlayProvider
     *            the image to be placed on the top left quadrant.
     * @param topRightOverlayProvider
     *            the image to be placed on the top right quadrant.
     * @param bottomRightOverlayProvider
     *            the image to be placed on the bottom right quadrant.
     * @param bottomLeftOverlayProvider
     *            the image to be placed on the bottom left quadrant.
     * @return the decorated image.
     */
    Image get(@Nullable IImageProvider topLeftOverlayProvider, @Nullable IImageProvider topRightOverlayProvider,
            @Nullable IImageProvider bottomRightOverlayProvider, @Nullable IImageProvider bottomLeftOverlayProvider);

    /**
     * Returns the same image provided by {@link #getDisabled()} optionally decorated with disabled images on to the 4 corner
     * quadrants.
     * 
     * @param topLeftOverlayProvider
     *            the image to be placed on the top left quadrant.
     * @param topRightOverlayProvider
     *            the image to be placed on the top right quadrant.
     * @param bottomRightOverlayProvider
     *            the image to be placed on the bottom right quadrant.
     * @param bottomLeftOverlayProvider
     *            the image to be placed on the bottom left quadrant.
     * @return the decorated image.
     */
    Image getDisabled(@Nullable IImageProvider topLeftOverlayProvider, @Nullable IImageProvider topRightOverlayProvider,
            @Nullable IImageProvider bottomRightOverlayProvider, @Nullable IImageProvider bottomLeftOverlayProvider);

    /**
     * Returns the same image descriptor provided by {@link #getDescriptor()} optionally decorated with images on to the 4 corner
     * quadrants.
     * 
     * @param topLeftOverlayProvider
     *            the image to be placed on the top left quadrant.
     * @param topRightOverlayProvider
     *            the image to be placed on the top right quadrant.
     * @param bottomRightOverlayProvider
     *            the image to be placed on the bottom right quadrant.
     * @param bottomLeftOverlayProvider
     *            the image to be placed on the bottom left quadrant.
     * @return the decorated image descriptor.
     */
    ImageDescriptor getDescriptor(@Nullable IImageProvider topLeftOverlayProvider, @Nullable IImageProvider topRightOverlayProvider,
            @Nullable IImageProvider bottomRightOverlayProvider, @Nullable IImageProvider bottomLeftOverlayProvider);

    /**
     * Returns the same disabled image descriptor provided by {@link #getDescriptor()} optionally decorated with disabled images on to
     * the 4 corner quadrants.
     * 
     * @param topLeftOverlayProvider
     *            the image to be placed on the top left quadrant.
     * @param topRightOverlayProvider
     *            the image to be placed on the top right quadrant.
     * @param bottomRightOverlayProvider
     *            the image to be placed on the bottom right quadrant.
     * @param bottomLeftOverlayProvider
     *            the image to be placed on the bottom left quadrant.
     * @return the decorated image descriptor.
     */
    ImageDescriptor getDisabledDescriptor(@Nullable IImageProvider topLeftOverlayProvider,
            @Nullable IImageProvider topRightOverlayProvider, @Nullable IImageProvider bottomRightOverlayProvider,
            @Nullable IImageProvider bottomLeftOverlayProvider);

}
