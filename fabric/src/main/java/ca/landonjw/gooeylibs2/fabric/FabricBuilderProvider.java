package ca.landonjw.gooeylibs2.fabric;

import ca.landonjw.gooeylibs2.bootstrap.BuilderProvider;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Supplier;

public class FabricBuilderProvider
implements BuilderProvider {
    private final Map<Class<?>, Supplier<?>> builders = new HashMap();

    @Override
    public <T> T provide(Class<T> type) throws NoSuchElementException {
        return (T)Optional.ofNullable(this.builders.get(type)).map(value -> value.get()).get();
    }

    @Override
    public <T> boolean register(Class<T> type, Supplier<T> supplier) {
        this.builders.put(type, supplier);
        return true;
    }
}

