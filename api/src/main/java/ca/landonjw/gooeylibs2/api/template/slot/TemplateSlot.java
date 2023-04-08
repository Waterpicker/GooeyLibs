/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.world.Container
 *  net.minecraft.world.inventory.Slot
 *  net.minecraft.world.item.ItemStack
 *  org.jetbrains.annotations.NotNull
 */
package ca.landonjw.gooeylibs2.api.template.slot;

import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.template.slot.TemplateSlotDelegate;
import javax.annotation.Nullable;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class TemplateSlot
extends Slot {
    private final TemplateSlotDelegate delegate;

    public TemplateSlot(Container container, @NotNull TemplateSlotDelegate delegate, int xPosition, int yPosition) {
        super(container, delegate.getIndex(), xPosition, yPosition);
        this.delegate = delegate;
    }

    public TemplateSlotDelegate getDelegate() {
        return this.delegate;
    }

    public void setButton(@Nullable Button button) {
        this.delegate.setButton(button);
    }

    public ItemStack getItem() {
        return this.delegate.getButton().map(Button::getDisplay).orElse(ItemStack.EMPTY);
    }

    public boolean mayPlace(@NotNull ItemStack stack) {
        return false;
    }
}

