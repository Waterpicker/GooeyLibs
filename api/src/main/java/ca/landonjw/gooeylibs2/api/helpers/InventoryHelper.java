/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.item.ItemStack
 */
package ca.landonjw.gooeylibs2.api.helpers;

import ca.landonjw.gooeylibs2.api.container.GooeyContainer;
import ca.landonjw.gooeylibs2.api.tasks.Task;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class InventoryHelper {
    public static void setToInventorySlot(@Nonnull ServerPlayer player, int inventorySlot, @Nullable ItemStack stack) {
        if (inventorySlot < 0) {
            return;
        }
        if (stack == null) {
            stack = ItemStack.EMPTY;
        }
        if (inventorySlot >= 27) {
            player.getInventory().setItem(inventorySlot - 27, stack.copy());
        } else {
            player.getInventory().setItem(inventorySlot + 9, stack.copy());
        }
        if (player.containerMenu instanceof GooeyContainer container) {
            Task.builder().execute(container::refresh).build();
        }
    }

    public static void setToInventorySlot(@Nonnull ServerPlayer player, int inventoryRow, int inventoryCol, @Nullable ItemStack stack) {
        InventoryHelper.setToInventorySlot(player, inventoryRow * 9 + inventoryCol, stack);
    }

    public static void addToInventorySlot(@Nonnull ServerPlayer player, @Nonnull ItemStack stack) {
        if (stack == ItemStack.EMPTY) {
            return;
        }
        player.getInventory().add(stack.copy());
        if (player.containerMenu instanceof GooeyContainer container) {
            Task.builder().execute(container::refresh).build();
        }
    }

    @Nonnull
    public static ItemStack getStackAtSlot(@Nonnull ServerPlayer player, int inventorySlot) {
        if (inventorySlot >= 27) {
            return player.getInventory().getItem(inventorySlot - 27);
        }
        return player.getInventory().getItem(inventorySlot + 9);
    }
}

