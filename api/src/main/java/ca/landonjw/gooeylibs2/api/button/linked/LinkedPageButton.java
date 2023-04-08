package ca.landonjw.gooeylibs2.api.button.linked;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.ButtonAction;
import ca.landonjw.gooeylibs2.api.button.FlagType;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.button.linked.LinkType;
import ca.landonjw.gooeylibs2.api.page.LinkedPage;
import ca.landonjw.gooeylibs2.api.page.Page;
import java.util.Collection;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.function.Consumer;

public class LinkedPageButton extends GooeyButton {
    private final LinkType linkType;

    protected LinkedPageButton(@NotNull ItemStack display, @Nullable Consumer<ButtonAction> onClick, @NotNull LinkType linkType) {
        super(display, onClick);
        this.linkType = linkType;
    }

    @Override
    public void onClick(@NotNull ButtonAction action) {
        super.onClick(action);
        Page page = action.getPage();
        if (page instanceof LinkedPage linkedPage) {
            UIManager.openUIForcefully(action.getPlayer(), linkedPage);
        }
    }

    public LinkType getLinkType() {
        return this.linkType;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder
    extends GooeyButton.Builder {
        private LinkType linkType;

        @Override
        public Builder display(@NotNull ItemStack display) {
            super.display(display);
            return this;
        }

        @Override
        public Builder title(@Nullable String title) {
            super.title(title);
            return this;
        }

        @Override
        public Builder title(@Nullable Component title) {
            super.title(title);
            return this;
        }

        public Builder linkType(@NotNull LinkType linkType) {
            this.linkType = linkType;
            return this;
        }

        @Override
        public Builder lore(@Nullable Collection<String> lore) {
            super.lore(lore);
            return this;
        }

        @Override
        public <T> Builder lore(Class<T> type, @Nullable Collection<T> lore) {
            super.lore(type, lore);
            return this;
        }

        @Override
        public Builder hideFlags(FlagType ... flags) {
            super.hideFlags(flags);
            return this;
        }

        @Override
        public Builder onClick(@Nullable Runnable behaviour) {
            super.onClick(behaviour);
            return this;
        }

        @Override
        public Builder onClick(@Nullable Consumer<ButtonAction> behaviour) {
            super.onClick(behaviour);
            return this;
        }

        @Override
        protected ItemStack buildDisplay() {
            return super.buildDisplay();
        }

        @Override
        public LinkedPageButton build() {
            this.validate();
            return new LinkedPageButton(this.buildDisplay(), this.onClick, this.linkType);
        }

        @Override
        protected void validate() {
            super.validate();
            if (this.linkType == null) {
                throw new IllegalStateException("link type must be defined!");
            }
        }
    }
}

