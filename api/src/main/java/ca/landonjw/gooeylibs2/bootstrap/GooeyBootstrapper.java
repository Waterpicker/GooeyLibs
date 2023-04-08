package ca.landonjw.gooeylibs2.bootstrap;

import ca.landonjw.gooeylibs2.bootstrap.BuilderProvider;
import ca.landonjw.gooeylibs2.bootstrap.GooeyServiceProvider;
import ca.landonjw.gooeylibs2.bootstrap.InstanceProvider;

public interface GooeyBootstrapper {
    static GooeyBootstrapper instance() {
        return GooeyServiceProvider.get();
    }

    void bootstrap();

    InstanceProvider provider();

    BuilderProvider builders();
}

