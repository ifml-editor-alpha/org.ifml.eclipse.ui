package org.ifml.eclipse.ui.graphics;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.ifml.eclipse.ui.debug.CommonUiDebug;

/**
 * A base implementation of {@link IImageCatalog} based on a {@link ImageRegistry}.
 */
final class ImageCatalog implements IImageCatalog {

    private final ImageRegistry registry;

    public ImageCatalog(Display display) {
        registry = new ImageRegistry(display);
    }

    @Override
    public Image get(String key) {
        return registry.get(key);
    }

    @Override
    public ImageDescriptor getDescriptor(String key) {
        return registry.getDescriptor(key);
    }

    @Override
    public void put(String key, ImageDescriptor descriptor) {
        CommonUiDebug.IMAGE.debug("Registering image: key=%s, descriptor=%s", key, descriptor);
        registry.put(key, descriptor);
    }

    @Override
    public void remove(String key) {
        CommonUiDebug.IMAGE.debug("Removing image: key=%s", key);
        registry.remove(key);
    }

}
