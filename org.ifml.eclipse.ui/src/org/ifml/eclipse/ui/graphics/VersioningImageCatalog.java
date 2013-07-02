package org.ifml.eclipse.ui.graphics;

import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.ifml.eclipse.ui.debug.CommonUiDebug;

import com.google.common.base.Preconditions;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * A specialized {@link IImageCatalog} which permits to replace images registered under specific keys and never disposes old images.
 * <p>
 * The {@link #remove} method simulates the removal of the most recent image but doesn't dispose it.
 * <p>
 * The {@link #put} method replaces the image with a new version but doesn't dispose the previous version.
 */
final class VersioningImageCatalog implements IImageCatalog {

    private static final String KEY_INDEX_SEPARATOR = "___";

    private LoadingCache<String, AtomicInteger> keyIndices;

    private ImageRegistry registry;

    public VersioningImageCatalog(Display display) {
        registry = new ImageRegistry(display);
        keyIndices = CacheBuilder.newBuilder().concurrencyLevel(1).build(new CacheLoader<String, AtomicInteger>() {
            @Override
            public AtomicInteger load(String input) {
                return new AtomicInteger(0);
            }
        });
    }

    @Override
    public Image get(String key) {
        return registry.get(getIndexedKey(key));
    }

    @Override
    public ImageDescriptor getDescriptor(String key) {
        return registry.getDescriptor(getIndexedKey(key));
    }

    @Override
    public void put(String key, ImageDescriptor descriptor) {
        Preconditions.checkNotNull(descriptor);
        int index = keyIndices.getUnchecked(key).get();
        if (registry.get(getIndexedKey(key, index)) != null) {
            index = keyIndices.getUnchecked(key).incrementAndGet();
        }
        CommonUiDebug.IMAGE.debug("Registering image: key=%s, index=%s, descriptor=%s", key, index, descriptor);
        registry.put(getIndexedKey(key, index), descriptor);
    }

    @Override
    public void remove(String key) {
        if (registry.get(getIndexedKey(key)) != null) {
            int oldIndex = keyIndices.getUnchecked(key).getAndIncrement();
            CommonUiDebug.IMAGE.debug("Removing image: key=%s, index=%s", key, oldIndex);
        }
    }

    private String getIndexedKey(String key) {
        return getIndexedKey(key, keyIndices.getUnchecked(key).get());
    }

    private String getIndexedKey(String key, int index) {
        StringBuilder str = new StringBuilder(key.length() + KEY_INDEX_SEPARATOR + 1);
        str.append(key);
        str.append(KEY_INDEX_SEPARATOR);
        str.append(index);
        return str.toString();
    }

}
