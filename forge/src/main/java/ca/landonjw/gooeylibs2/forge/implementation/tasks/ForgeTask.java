/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.event.TickEvent$Phase
 *  net.minecraftforge.event.TickEvent$ServerTickEvent
 */
package ca.landonjw.gooeylibs2.forge.implementation.tasks;

import ca.landonjw.gooeylibs2.api.tasks.AbstractTask;
import ca.landonjw.gooeylibs2.api.tasks.ScheduledTask;
import java.util.function.Consumer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;

public final class ForgeTask
extends AbstractTask {
    private Consumer<TickEvent.ServerTickEvent> event;

    ForgeTask(ForgeTaskBuilder builder) {
        super(builder);
    }

    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            this.tick();
            if (this.isExpired()) {
                MinecraftForge.EVENT_BUS.unregister(this.event);
            }
        }
    }

    @Override
    protected ScheduledTask sync() {
        this.event = this::onServerTick;
        MinecraftForge.EVENT_BUS.addListener(this.event);
        return () -> MinecraftForge.EVENT_BUS.unregister(this.event);
    }

    public static final class ForgeTaskBuilder
    extends AbstractTask.AbstractTaskBuilder<ForgeTask> {
        @Override
        public ForgeTask build() {
            if (this.consumer == null) {
                throw new IllegalStateException("consumer must be set");
            }
            return new ForgeTask(this);
        }
    }
}

