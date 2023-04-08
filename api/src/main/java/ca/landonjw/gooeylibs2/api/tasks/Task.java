package ca.landonjw.gooeylibs2.api.tasks;

import ca.landonjw.gooeylibs2.bootstrap.GooeyBootstrapper;
import java.util.function.Consumer;
import javax.annotation.Nonnull;

public interface Task {
    boolean isAsync();

    long delay();

    long iterations();

    Consumer<Task> executor();

    boolean infinite();

    boolean isExpired();

    void setExpired();

    static TaskBuilder builder() {
        return GooeyBootstrapper.instance().builders().provide(TaskBuilder.class);
    }

    interface TaskBuilder {
        TaskBuilder async();

        TaskBuilder execute(@Nonnull Runnable var1);

        TaskBuilder execute(@Nonnull Consumer<Task> var1);

        TaskBuilder delay(long var1);

        TaskBuilder interval(long var1);

        TaskBuilder iterations(long var1);

        TaskBuilder infinite();

        Task build();
    }
}

