package ca.landonjw.gooeylibs2.api.data;

import java.util.function.Consumer;
import javax.annotation.Nonnull;

public abstract class UpdateEmitter<T> implements Subject<T> {
    private final EventEmitter<T> eventEmitter = new EventEmitter<>();

    public UpdateEmitter() {
        try {
            UpdateEmitter<T> updateEmitter = this;
        }
        catch (ClassCastException e) {
            throw new IllegalStateException("bad generic given for superclass");
        }
    }

    @Override
    public void subscribe(@Nonnull Object observer, @Nonnull Consumer<T> consumer) {
        this.eventEmitter.subscribe(observer, consumer);
    }

    @Override
    public void unsubscribe(@Nonnull Object observer) {
        this.eventEmitter.unsubscribe(observer);
    }

    public void update() {
        this.eventEmitter.emit((T) this);
    }
}

