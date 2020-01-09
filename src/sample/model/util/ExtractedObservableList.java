package sample.model.util;

import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;

import java.util.function.Function;

public class ExtractedObservableList<T, V> extends ObservableListBase<T> {
    private final ObservableValue<V> base;
    private final Function<V, ObservableList<T>> extractor;
    private ObservableList<T> wrapped = null;
    private ObservedListListener listener = new ObservedListListener();

    public ExtractedObservableList(ObservableValue<V> base, Function<V, ObservableList<T>> extractor) {
        this.base = base;
        this.extractor = extractor;
        base.addListener((bnd, nv, ov) -> {
            wrapped.removeListener(listener);
            wrapped = this.extractor.apply(nv);
            wrapped.addListener(listener);
        });
        wrapped = extractor.apply(base.getValue());
        if (wrapped != null) wrapped.addListener(listener);
    }

    @Override
    public T get(int index) {
        if (wrapped == null)
            throw new UnsupportedOperationException("Access to an unset backing list");
        return wrapped.get(index);
    }

    @Override
    public int size() {
        if (wrapped == null)
            return 0;
        return wrapped.size();
    }

    private class ObservedListListener implements ListChangeListener<T> {
        @Override
        public void onChanged(Change<? extends T> change) {
            ExtractedObservableList.this.fireChange(change);
        }
    }
}
