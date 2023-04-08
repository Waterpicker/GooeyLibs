/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package ca.landonjw.gooeylibs2.bootstrap;

import ca.landonjw.gooeylibs2.bootstrap.GooeyBootstrapper;
import org.jetbrains.annotations.NotNull;

public class GooeyServiceProvider {
    private static GooeyBootstrapper instance;

    @NotNull
    public static GooeyBootstrapper get() {
        if (instance == null) {
            throw new IllegalStateException("The GooeyLibs API is not loaded");
        }
        return instance;
    }

    static void register(GooeyBootstrapper service) {
        instance = service;
    }

    private GooeyServiceProvider() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }
}

