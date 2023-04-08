/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  net.minecraft.server.level.ServerPlayer
 */
package ca.landonjw.gooeylibs2.api;

import ca.landonjw.gooeylibs2.api.container.GooeyContainer;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.tasks.Task;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import javax.annotation.Nonnull;
import net.minecraft.server.level.ServerPlayer;

public class UIManager {
    public static void openUIPassively(@Nonnull ServerPlayer player, @Nonnull Page page, long timeout, TimeUnit timeoutUnit) {
        AtomicLong timeOutTicks = new AtomicLong(timeoutUnit.convert(timeout, TimeUnit.SECONDS) * 20L);
        Task.builder().execute(task -> {
            timeOutTicks.getAndDecrement();
            if (player.containerMenu.containerId == player.containerCounter || timeOutTicks.get() <= 0L) {
                UIManager.openUIForcefully(player, page);
                task.setExpired();
            }
        }).infinite().interval(1L).build();
    }

    public static void openUIForcefully(@Nonnull ServerPlayer player, @Nonnull Page page) {
        Task.builder().execute(() -> {
            GooeyContainer container = new GooeyContainer(player, page);
            container.open();
        }).build();
    }

    public static void closeUI(@Nonnull ServerPlayer player) {
        Task.builder().execute(((ServerPlayer)player)::closeContainer).build();
    }
}

