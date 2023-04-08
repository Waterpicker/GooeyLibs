package ca.landonjw.gooeylibs2.api.button;

import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.Template;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Optional;

public class ButtonAction {
    private final ServerPlayer player;
    private final ButtonClick clickType;
    private final Button button;
    private final Template template;
    private final Page page;
    private final int slot;

    public ButtonAction(@Nonnull ServerPlayer player, @Nonnull ButtonClick clickType, @Nonnull Button button, @Nonnull Template template, @Nonnull Page page, int slot) {
        this.player = Objects.requireNonNull(player);
        this.clickType = Objects.requireNonNull(clickType);
        this.button = Objects.requireNonNull(button);
        this.template = Objects.requireNonNull(template);
        this.page = Objects.requireNonNull(page);
        this.slot = slot;
    }

    public ServerPlayer getPlayer() {
        return this.player;
    }

    public ButtonClick getClickType() {
        return this.clickType;
    }

    public Button getButton() {
        return this.button;
    }

    public Template getTemplate() {
        return this.template;
    }

    public Page getPage() {
        return this.page;
    }

    public int getSlot() {
        return this.slot;
    }

    public boolean isSlotInInventory() {
        return this.page.getInventoryTemplate().isPresent() && this.slot >= this.template.getSize();
    }

    public Optional<Integer> getInventorySlot() {
        if (this.isSlotInInventory()) {
            return Optional.of(this.slot - this.template.getSize());
        }
        return Optional.empty();
    }
}

