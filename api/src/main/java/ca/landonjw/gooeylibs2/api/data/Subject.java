package ca.landonjw.gooeylibs2.api.data;

import java.util.function.Consumer;
import javax.annotation.Nonnull;

public interface Subject<T> {
    void subscribe(@Nonnull Object var1, @Nonnull Consumer<T> var2);

    default void subscribe(@Nonnull Object observer, @Nonnull Runnable runnable) {
        this.subscribe(observer, (T t) -> runnable.run());
    }

    void unsubscribe(@Nonnull Object var1);
}

