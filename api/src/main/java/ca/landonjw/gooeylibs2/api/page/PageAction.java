/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  net.minecraft.server.level.ServerPlayer
 */
package ca.landonjw.gooeylibs2.api.page;

import ca.landonjw.gooeylibs2.api.page.Page;
import javax.annotation.Nonnull;
import net.minecraft.server.level.ServerPlayer;

public class PageAction {
    private final ServerPlayer player;
    private final Page page;

    public PageAction(@Nonnull ServerPlayer player, @Nonnull Page page) {
        this.player = player;
        this.page = page;
    }

    public ServerPlayer getPlayer() {
        return this.player;
    }

    public Page getPage() {
        return this.page;
    }
}

