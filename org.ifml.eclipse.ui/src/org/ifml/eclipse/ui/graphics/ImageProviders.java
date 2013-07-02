package org.ifml.eclipse.ui.graphics;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Set;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.ifml.eclipse.core.runtime.Logs;
import org.ifml.eclipse.osgi.Bundles;
import org.ifml.eclipse.ui.CommonUi;
import org.osgi.framework.Bundle;

import com.google.common.collect.Sets;

/**
 * Provides utility methods for image providers.
 * 
 * <p>
 * Images are described by enumerations implementing the {@link IImageProvider} interface whose methods are implemented by simply
 * invoking their matching method on this utility class. Enumeration constants follows a specific naming convention.
 * <p>
 * For example the following enum bounds the constants {@code FOO_BAR} and {@code FOO_BAZ_OVR} respectively with the images stored at
 * {@code icons/full/obj16/foo_bar_obj.png} and {@code icons/full/ovr16/foo_baz_ovr.png}:
 * 
 * <pre>
 * public enum FooImages implements IImageProvider {
 *    
 *     FOO_BAR, FOO_BAZ_OVR;
 *     
 *     public CustomImage get() {
 *         return ImageProviders.get(this); // invoke matching method
 *     }
 * 
 *     ...
 *     
 * }
 * </pre>
 */
public final class ImageProviders {

    private static final Set<String> MISSING_IMAGES = Sets.newHashSet();

    private ImageProviders() {
    }

    /**
     * Returns the image associated with a specific image provider.
     * 
     * @param <T>
     *            the image type.
     * @param imageProvider
     *            the image provider.
     * @return the image.
     */
    public static <T extends Enum<T> & IImageProvider> Image get(T imageProvider) {
        imageProvider.getDescriptor(); // forces description creation if required
        String key = getKey(imageProvider, false);
        return CommonUi.getDefault().getImageRegistry().get(key);
    }

    /**
     * Returns the disabled image associated with a specific image provider.
     * 
     * @param <T>
     *            the image type.
     * @param imageProvider
     *            the image provider.
     * @return the disabled image.
     */
    public static <T extends Enum<T> & IImageProvider> Image getDisabled(T imageProvider) {
        getDisabledDescriptor(imageProvider); // forces description creation if required
        String key = getKey(imageProvider, true);
        return CommonUi.getDefault().getImageRegistry().get(key);
    }

    /**
     * Returns the overlayed image associated with a specific image provider.
     * 
     * @param <T>
     *            the image type.
     * @param imageProvider
     *            the image provider.
     * @param topLeftOverlayProvider
     *            the provider of the image to be placed on the top left quadrant.
     * @param topRightOverlayProvider
     *            the provider of the image to be placed on the top right quadrant.
     * @param bottomRightOverlayProvider
     *            the provider of the image to be placed on the bottom right quadrant.
     * @param bottomLeftOverlayProvider
     *            the provider of the image to be placed on the bottom left quadrant.
     * @return the image.
     */
    public static <T extends Enum<T> & IImageProvider> Image get(T imageProvider, IImageProvider topLeftOverlayProvider,
            IImageProvider topRightOverlayProvider, IImageProvider bottomRightOverlayProvider, IImageProvider bottomLeftOverlayProvider) {
        checkNotNull(imageProvider);
        if ((topLeftOverlayProvider == null) && (topRightOverlayProvider == null) && (bottomRightOverlayProvider == null)
                && (bottomLeftOverlayProvider == null)) {
            return get(imageProvider);
        }
        getDescriptor(imageProvider, topLeftOverlayProvider, topRightOverlayProvider, bottomRightOverlayProvider,
                bottomLeftOverlayProvider); // forces description creation if required
        String key = getCompositeKey(imageProvider, topLeftOverlayProvider, topRightOverlayProvider, bottomRightOverlayProvider,
                bottomLeftOverlayProvider, false);
        return CommonUi.getDefault().getImageRegistry().get(key);
    }

    /**
     * Returns the disabled overlayed image associated with a specific image provider.
     * 
     * @param <T>
     *            the image type.
     * @param imageProvider
     *            the image provider.
     * @param topLeftOverlayProvider
     *            the provider of the image to be placed on the top left quadrant.
     * @param topRightOverlayProvider
     *            the provider of the image to be placed on the top right quadrant.
     * @param bottomRightOverlayProvider
     *            the provider of the image to be placed on the bottom right quadrant.
     * @param bottomLeftOverlayProvider
     *            the provider of the image to be placed on the bottom left quadrant.
     * @return the image.
     */
    public static <T extends Enum<T> & IImageProvider> Image getDisabled(T imageProvider, IImageProvider topLeftOverlayProvider,
            IImageProvider topRightOverlayProvider, IImageProvider bottomRightOverlayProvider, IImageProvider bottomLeftOverlayProvider) {
        checkNotNull(imageProvider);
        if ((topLeftOverlayProvider == null) && (topRightOverlayProvider == null) && (bottomRightOverlayProvider == null)
                && (bottomLeftOverlayProvider == null)) {
            return getDisabled(imageProvider);
        }
        getDisabledDescriptor(imageProvider, topLeftOverlayProvider, topRightOverlayProvider, bottomRightOverlayProvider,
                bottomLeftOverlayProvider); // forces description creation if required
        String key = getCompositeKey(imageProvider, topLeftOverlayProvider, topRightOverlayProvider, bottomRightOverlayProvider,
                bottomLeftOverlayProvider, true);
        return CommonUi.getDefault().getImageRegistry().get(key);
    }

    /**
     * Returns the image descriptor associated with a specific image provider.
     * 
     * @param <T>
     *            the image type.
     * @param imageProvider
     *            the image provider.
     * @return the image descriptor.
     */
    public static <T extends Enum<T> & IImageProvider> ImageDescriptor getDescriptor(T imageProvider) {
        checkNotNull(imageProvider);
        String key = getKey(imageProvider, false);
        ImageRegistry registry = CommonUi.getDefault().getImageRegistry();
        ImageDescriptor imageDescr = registry.getDescriptor(key);
        if (imageDescr == null) {

            /* initializes the image descriptor */
            Bundle bundle = getDeclaringBundle(imageProvider);
            ImageDescriptor descr = createDescriptor(imageProvider, bundle);
            if (descr != null) {
                registry.put(key, descr);
            }
            imageDescr = registry.getDescriptor(key);
        }
        return imageDescr;
    }

    /**
     * Returns the disabled image descriptor associated with a specific image provider.
     * 
     * @param <T>
     *            the image type.
     * @param imageProvider
     *            the image provider.
     * @return the disabled image descriptor.
     */
    public static <T extends Enum<T> & IImageProvider> ImageDescriptor getDisabledDescriptor(T imageProvider) {
        checkNotNull(imageProvider);
        String key = getKey(imageProvider, true);
        ImageRegistry registry = CommonUi.getDefault().getImageRegistry();
        ImageDescriptor imageDescr = registry.getDescriptor(key);
        if (imageDescr == null) {

            /* initializes the image descriptor */
            ImageDescriptor enabledImageDescr = imageProvider.getDescriptor()/* getDescriptor(imageProvider) */;
            if (enabledImageDescr != null) {
                ImageDescriptor descr = Images.newDisabledDescriptor(enabledImageDescr);
                if (descr != null) {
                    registry.put(key, descr);
                }
            }
            imageDescr = registry.getDescriptor(key);
        }
        return imageDescr;
    }

    /**
     * Returns the overlayed image descriptor associated with a specific image provider.
     * 
     * @param <T>
     *            the image type.
     * @param imageProvider
     *            the image provider.
     * @param topLeftOverlayProvider
     *            the provider of the image to be placed on the top lef quadrant.
     * @param topRightOverlayProvider
     *            the provider of the image to be placed on the top right quadrant.
     * @param bottomRightOverlayProvider
     *            the provider of the image to be placed on the bottom right quadrant.
     * @param bottomLeftOverlayProvider
     *            the provider of the image to be placed on the bottom left quadrant.
     * @return the image descriptor.
     */
    public static <T extends Enum<T> & IImageProvider> ImageDescriptor getDescriptor(T imageProvider,
            IImageProvider topLeftOverlayProvider, IImageProvider topRightOverlayProvider, IImageProvider bottomRightOverlayProvider,
            IImageProvider bottomLeftOverlayProvider) {
        checkNotNull(imageProvider);
        if ((topLeftOverlayProvider == null) && (topRightOverlayProvider == null) && (bottomRightOverlayProvider == null)
                && (bottomLeftOverlayProvider == null)) {
            return imageProvider.getDescriptor();
        }
        String key = getCompositeKey(imageProvider, topLeftOverlayProvider, topRightOverlayProvider, bottomRightOverlayProvider,
                bottomLeftOverlayProvider, false);
        ImageRegistry registry = CommonUi.getDefault().getImageRegistry();
        ImageDescriptor imageDescr = registry.getDescriptor(key);
        if (imageDescr == null) {

            /* initializes the image descriptors */
            registry.put(
                    key,
                    createCompositeDescriptor(imageProvider, topLeftOverlayProvider, topRightOverlayProvider,
                            bottomRightOverlayProvider, bottomLeftOverlayProvider));
            imageDescr = registry.getDescriptor(key);
        }
        return imageDescr;
    }

    /**
     * Returns the disabled overlayed image descriptor associated with a specific image provider.
     * 
     * @param <T>
     *            the image type.
     * @param imageProvider
     *            the image provider.
     * @param topLeftOverlayProvider
     *            the provider of the image to be placed on the top lef quadrant.
     * @param topRightOverlayProvider
     *            the provider of the image to be placed on the top right quadrant.
     * @param bottomRightOverlayProvider
     *            the provider of the image to be placed on the bottom right quadrant.
     * @param bottomLeftOverlayProvider
     *            the provider of the image to be placed on the bottom left quadrant.
     * @return the image descriptor.
     */
    public static <T extends Enum<T> & IImageProvider> ImageDescriptor getDisabledDescriptor(T imageProvider,
            IImageProvider topLeftOverlayProvider, IImageProvider topRightOverlayProvider, IImageProvider bottomRightOverlayProvider,
            IImageProvider bottomLeftOverlayProvider) {
        checkNotNull(imageProvider);
        if ((topLeftOverlayProvider == null) && (topRightOverlayProvider == null) && (bottomRightOverlayProvider == null)
                && (bottomLeftOverlayProvider == null)) {
            return getDisabledDescriptor(imageProvider);
        }
        String key = getCompositeKey(imageProvider, topLeftOverlayProvider, topRightOverlayProvider, bottomRightOverlayProvider,
                bottomLeftOverlayProvider, true);
        ImageRegistry registry = CommonUi.getDefault().getImageRegistry();
        ImageDescriptor imageDescr = registry.getDescriptor(key);
        if (imageDescr == null) {

            /* initializes the image descriptor */
            ImageDescriptor enabledImageDescr = getDescriptor(imageProvider, topLeftOverlayProvider, topRightOverlayProvider,
                    bottomRightOverlayProvider, bottomLeftOverlayProvider);
            if (enabledImageDescr != null) {
                ImageDescriptor descr = Images.newDisabledDescriptor(enabledImageDescr);
                if (descr != null) {
                    registry.put(key, descr);
                }
            }
            imageDescr = registry.getDescriptor(key);
        }
        return imageDescr;
    }

    private static <T extends Enum<T> & IImageProvider> Bundle getDeclaringBundle(T imageProvider) {
        Bundle bundle = Bundles.getDeclaringBundle(imageProvider.getClass());
        checkArgument(bundle != null, "bundle not found for image %s", imageProvider.name());
        return bundle;
    }

    private static <T extends Enum<T> & IImageProvider> ImageDescriptor createDescriptor(T imageProvider, Bundle bundle) {
        String path = getPath(imageProvider);
        ImageDescriptor descr = AbstractUIPlugin.imageDescriptorFromPlugin(bundle.getSymbolicName(), path);
        if (descr == null) {
            if (!MISSING_IMAGES.contains(path)) {
                MISSING_IMAGES.add(path);
                Logs.logError(null, String.format("image %s (path=%s) not found in bundle %s", imageProvider.name(), path, bundle),
                        bundle);
            }
        }
        return descr;
    }

    private static <T extends Enum<T> & IImageProvider> ImageDescriptor createCompositeDescriptor(T imageProvider,
            IImageProvider topLeftOverlayProvider, IImageProvider topRightOverlayProvider, IImageProvider bottomRightOverlayProvider,
            IImageProvider bottomLeftOverlayProvider) {
        return new DecorationOverlayIcon(imageProvider.get(), new ImageDescriptor[] {
                (topLeftOverlayProvider != null) ? topLeftOverlayProvider.getDescriptor() : null,
                (topRightOverlayProvider != null) ? topRightOverlayProvider.getDescriptor() : null,
                (bottomLeftOverlayProvider != null) ? bottomLeftOverlayProvider.getDescriptor() : null,
                (bottomRightOverlayProvider != null) ? bottomRightOverlayProvider.getDescriptor() : null });
    }

    private static <T extends Enum<T> & IImageProvider> String getKey(T imageProvider, boolean disabled) {
        return imageProvider.getDeclaringClass().getName() + '.' + imageProvider.name() + (disabled ? ".disabled" : "");
    }

    @SuppressWarnings("unchecked")
    private static <T extends Enum<T> & IImageProvider> String getCompositeKey(T imageProvider, IImageProvider topLeftOverlayProvider,
            IImageProvider topRightOverlayProvider, IImageProvider bottomRightOverlayProvider,
            IImageProvider bottomLeftOverlayProvider, boolean disabled) {
        checkArgument(topLeftOverlayProvider == null || topLeftOverlayProvider instanceof Enum);
        checkArgument(topRightOverlayProvider == null || topRightOverlayProvider instanceof Enum);
        checkArgument(bottomRightOverlayProvider == null || bottomRightOverlayProvider instanceof Enum);
        checkArgument(bottomLeftOverlayProvider == null || bottomLeftOverlayProvider instanceof Enum);
        StringBuilder key = new StringBuilder();
        key.append(imageProvider != null ? getKey(imageProvider, false) : "");
        key.append('|');
        key.append(topLeftOverlayProvider != null ? getKey((T) topLeftOverlayProvider, false) : "");
        key.append('|');
        key.append(topRightOverlayProvider != null ? getKey((T) topRightOverlayProvider, false) : "");
        key.append('|');
        key.append(bottomRightOverlayProvider != null ? getKey((T) bottomRightOverlayProvider, false) : "");
        key.append('|');
        key.append(bottomLeftOverlayProvider != null ? getKey((T) bottomLeftOverlayProvider, false) : "");
        if (disabled) {
            key.append(".disabled");
        }
        return key.toString();
    }

    /**
     * Returns the path of the image.
     * 
     * @param imageProvider
     *            the image provider.
     * @return the path.
     */
    public static <T extends Enum<T> & IImageProvider> String getPath(T imageProvider) {
        String name = imageProvider.name();
        if (name.endsWith("_OVR")) {
            return "icons/full/ovr16/" + name.toLowerCase() + ".png";
        } else if (name.startsWith("NEW_")) {
            return "icons/full/tool16/" + name.toLowerCase() + ".png";
        } else if (name.endsWith("_TOOL")) {
            return "icons/full/tool16/" + name.toLowerCase() + ".png";
        } else if (name.endsWith("_WIZBAN")) {
            return "icons/full/wizban/" + name.toLowerCase() + ".png";
        } else if (name.endsWith("_BTN")) {
            return "icons/full/btn/" + name.toLowerCase() + ".png";
        } else if (name.endsWith("_32")) {
            return "icons/full/obj32/" + name.toLowerCase().substring(0, name.length() - 3) + "_obj.png";
        } else if (name.endsWith("_64")) {
            return "icons/full/obj64/" + name.toLowerCase().substring(0, name.length() - 3) + "_obj.png";
        } else {
            return "icons/full/obj16/" + name.toLowerCase() + "_obj.png";
        }
    }

}
