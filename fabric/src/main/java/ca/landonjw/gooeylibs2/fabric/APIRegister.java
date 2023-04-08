package ca.landonjw.gooeylibs2.fabric;

import ca.landonjw.gooeylibs2.bootstrap.GooeyBootstrapper;
import ca.landonjw.gooeylibs2.bootstrap.GooeyServiceProvider;
import java.lang.reflect.Method;

public class APIRegister {
    private static final Method REGISTER;

    public static GooeyBootstrapper register(GooeyBootstrapper service) {
        try {
            REGISTER.invoke(null, service);
            return service;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static {
        try {
            REGISTER = GooeyServiceProvider.class.getDeclaredMethod("register", GooeyBootstrapper.class);
            REGISTER.setAccessible(true);
        }
        catch (NoSuchMethodException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}

