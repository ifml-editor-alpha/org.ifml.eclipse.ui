package org.ifml.eclipse.ui.viewers;

import java.util.Map;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

/**
 * A builder for {@link ViewerFilter} based on predicates.
 * 
 * @param <T>
 *            the type parameter.
 */
public class PredicateViewerFilterBuilder<T> {

    private final Map<Class<? extends T>, Predicate<?>> predicates = Maps.newHashMap();

    /**
     * Adds a predicate for a specific type.
     * 
     * @param <E>
     *            the type parameter.
     * @param cl
     *            the class type.
     * @param predicate
     *            the predicate.
     * @return this builder.
     */
    public <E extends T> PredicateViewerFilterBuilder<T> predicate(Class<? extends E> cl, Predicate<? super E> predicate) {
        predicates.put(cl, predicate);
        return this;
    }

    /**
     * Creates the viewer filter.
     * 
     * @return the viewer filter.
     */
    public ViewerFilter build() {
        return new PredicateViewerFilter<T>(this);
    }

    private static final class PredicateViewerFilter<T> extends ViewerFilter {

        private final Map<Class<? extends T>, Predicate<?>> predicates;

        public PredicateViewerFilter(PredicateViewerFilterBuilder<T> builder) {
            predicates = ImmutableMap.copyOf(builder.predicates);
        }

        @Override
        public boolean select(Viewer viewer, Object parentElement, Object element) {
            boolean classFound = false;
            for (Map.Entry<Class<? extends T>, Predicate<?>> entry : predicates.entrySet()) {
                Class<? extends T> cl = entry.getKey();
                if (cl.isInstance(element)) {
                    classFound = true;
                    T item = cl.cast(element);
                    @SuppressWarnings("unchecked")
                    Predicate<? super T> predicate = (Predicate<? super T>) entry.getValue();
                    if (!predicate.apply(item)) {
                        return false;
                    }
                }
            }
            return classFound;
        }

    }

}
