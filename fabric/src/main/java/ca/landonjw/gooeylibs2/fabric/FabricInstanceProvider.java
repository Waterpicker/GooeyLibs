package ca.landonjw.gooeylibs2.fabric;

import ca.landonjw.gooeylibs2.bootstrap.InstanceProvider;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Supplier;

public class FabricInstanceProvider
implements InstanceProvider {
    private final Map<Class<?>, Object> instances = new HashMap();

    @Override
    public <T> T provide(Class<T> type) throws NoSuchElementException {
        return (T)Optional.ofNullable(this.instances.get(type)).map(value -> value).get();
    }

    @Override
    public <T> boolean register(Class<T> type, T instance) {
        this.instances.put(type, instance);
        return true;
    }

    @Override
    public <T> boolean register(Class<T> type, Supplier<T> instance) {
        this.instances.put(type, instance);
        return true;
    }
}

