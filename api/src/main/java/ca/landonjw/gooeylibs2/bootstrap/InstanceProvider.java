package ca.landonjw.gooeylibs2.bootstrap;

import java.util.NoSuchElementException;
import java.util.function.Supplier;

public interface InstanceProvider {
    <T> T provide(Class<T> var1) throws NoSuchElementException;

    <T> boolean register(Class<T> var1, T var2);

    <T> boolean register(Class<T> var1, Supplier<T> var2);
}

