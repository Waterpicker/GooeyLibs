package ca.landonjw.gooeylibs2.fabric;

import ca.landonjw.gooeylibs2.api.tasks.Task;
import ca.landonjw.gooeylibs2.bootstrap.BuilderProvider;
import ca.landonjw.gooeylibs2.bootstrap.GooeyBootstrapper;
import ca.landonjw.gooeylibs2.bootstrap.InstanceProvider;
import ca.landonjw.gooeylibs2.fabric.APIRegister;
import ca.landonjw.gooeylibs2.fabric.FabricBuilderProvider;
import ca.landonjw.gooeylibs2.fabric.FabricInstanceProvider;
import ca.landonjw.gooeylibs2.fabric.implementation.tasks.FabricTask;

public class FabricBootstrapper
implements GooeyBootstrapper {
    private final FabricInstanceProvider provider = new FabricInstanceProvider();
    private final FabricBuilderProvider builders = new FabricBuilderProvider();

    @Override
    public void bootstrap() {
        APIRegister.register(this);
        this.builders.register(Task.TaskBuilder.class, FabricTask.FabricTaskBuilder::new);
    }

    @Override
    public InstanceProvider provider() {
        return this.provider;
    }

    @Override
    public BuilderProvider builders() {
        return this.builders;
    }
}

